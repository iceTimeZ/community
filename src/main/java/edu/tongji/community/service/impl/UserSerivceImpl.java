package edu.tongji.community.service.impl;

import edu.tongji.community.dao.UserMapper;
import edu.tongji.community.entity.User;
import edu.tongji.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSerivceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
