package com.wx.userservice.service.impl;

import com.wx.userservice.domain.User;
import com.wx.userservice.exception.CommonException;
import com.wx.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: wangxiang
 * Date: 2019/9/21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = new User();
        user.setUserName(userName);
        List<User> userList = userService.queryUserByuserName(user);
        if (Objects.isNull(userList)) {
            throw new CommonException("error.user.not.exist");
        }
        return userService.queryUserByuserName(user).get(0);
    }
}
