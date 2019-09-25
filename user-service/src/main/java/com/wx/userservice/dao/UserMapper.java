package com.wx.userservice.dao;

import com.wx.userservice.domain.User;
import com.wx.userservice.utils.IBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wangxiang
 * Date: 2019/9/21
 * To change this template use File | Settings | File Templates.
 */
@Mapper
public interface UserMapper extends IBaseDao<User> {
    List<User> queryUserByuserName(@Param("user") User user);

    List<User> list(User user);
}
