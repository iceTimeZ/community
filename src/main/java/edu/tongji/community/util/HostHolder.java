package edu.tongji.community.util;

import edu.tongji.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 服务器同时处理多个浏览器的请求，属于多线程工作，需要进行线程隔离
 * 持用用户信息，用于代替session对象
 */
@Component
public class HostHolder {

    // 根据线程来存取对象
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
