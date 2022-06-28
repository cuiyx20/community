package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {       //持有用户的信息，用于代替Session对象
    private ThreadLocal<User> users = new ThreadLocal<User>();
    public void setUsers(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
