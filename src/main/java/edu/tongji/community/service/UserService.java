package edu.tongji.community.service;

import edu.tongji.community.entity.LoginTicket;
import edu.tongji.community.entity.User;

import java.util.HashMap;
import java.util.Map;

public interface UserService {

    public User findUserById(int id);

    public Map<String, Object> register(User user);

    public int activation(int userId, String code);

    public Map<String, Object> login(String username, String password, int expiredSeconds);

    public void logout(String ticket);

    public LoginTicket findLoginTicket(String ticket);

    public int updateHeader(int userId, String headerUrl);

    public User findUserByName(String username);
}