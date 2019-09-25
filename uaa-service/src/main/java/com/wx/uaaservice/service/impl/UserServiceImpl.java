package com.wx.uaaservice.service.impl;

import com.wx.uaaservice.dao.UserMapper;
import com.wx.uaaservice.domain.User;
import com.wx.uaaservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public User insert(String userName, String passWord) {
        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passwordEncoder.encode(passWord));
        userMapper.insert(user);
        return userMapper.selectOne(user);
    }

    @Override
    public List<User> queryUserByuserName(User user) {
        return userMapper.queryUserByuserName(user);
    }

    @Override
    public List<User> list(User user) {
        return userMapper.list(user);
    }
}
