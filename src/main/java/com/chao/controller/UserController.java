package com.chao.controller;

import com.chao.entity.User;
import com.chao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        User loggedInUser = userService.login(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            result.put("success", true);
            result.put("message", "Login successful");
            result.put("user", loggedInUser);
        } else {
            result.put("success", false);
            result.put("message", "Invalid username or password");
        }
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.register(user.getUsername(), user.getPassword());
        if (success) {
            result.put("success", true);
            result.put("message", "Registration successful");
        } else {
            result.put("success", false);
            result.put("message", "Username already exists");
        }
        return result;
    }
}
