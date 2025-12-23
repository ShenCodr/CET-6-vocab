package com.chao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chao.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    boolean register(String username, String password);
}
