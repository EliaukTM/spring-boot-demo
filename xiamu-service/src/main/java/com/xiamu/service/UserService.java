package com.xiamu.service;

import com.xiamu.dao.UserDao;
import com.xiamu.entity.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author haoyuan.yang
 * @version 1.0
 * @date: 2017/3/11
 * @since JDK 1.8
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> getAllUser() {
        return userDao.findAllUsers();
    }
}
