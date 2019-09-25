package com.wx.userservice.service.impl;

import com.wx.userservice.client.AuthServiceClient;
import com.wx.userservice.dao.UserMapper;
import com.wx.userservice.dao.UserRoleMapper;
import com.wx.userservice.domain.JWT;
import com.wx.userservice.domain.User;
import com.wx.userservice.domain.UserRole;
import com.wx.userservice.dto.UserLoginDTO;
import com.wx.userservice.exception.UserLoginException;
import com.wx.userservice.service.UserService;
import com.wx.userservice.utils.BPwdEncoderUtil;
import com.wx.userservice.utils.PlatformConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: wangxiang
 * Date: 2019/9/21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    AuthServiceClient authServiceClient;

    @Override
    @Transactional
    public User insert(String userName, String passWord) {
        //注册的时候默认给用户user的权限
        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passwordEncoder.encode(passWord));
        userMapper.insert(user);
        User selectOne = userMapper.selectOne(user);
        UserRole userRole = new UserRole();
        userRole.setRoleId(PlatformConstants.ROLE_USER);
        userRole.setUserId(selectOne.getId());
        userRoleMapper.insert(userRole);
        return selectOne;
    }

    @Override
    public List<User> queryUserByuserName(User user) {
        return userMapper.queryUserByuserName(user);
    }

    @Override
    public List<User> list(User user) {
        return userMapper.list(user);
    }

    @Override
    public UserLoginDTO login(String username, String password) {
        User user = new User();
        user.setUserName(username);
        List<User> users = userMapper.queryUserByuserName(user);
        if (Objects.isNull(users) || users.isEmpty()) {
            throw new UserLoginException("error username");
        }
        user = user = users.get(0);
        if (!BPwdEncoderUtil.matches(password, user.getPassword())) {
            throw new UserLoginException("error password");
        }
        // 获取token
        JWT jwt = authServiceClient.getToken("Basic dXNlci1zZXJ2aWNlOjEyMzQ1Ng==", "password", username, password);
        // 获得用户菜单
        if (jwt == null) {
            throw new UserLoginException("error internal");
        }
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setJwt(jwt);
        userLoginDTO.setUser(user);
        return userLoginDTO;
    }
}
