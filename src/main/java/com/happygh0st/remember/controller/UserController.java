package com.happygh0st.remember.controller;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.service.UserService;
import com.happygh0st.remember.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Roles(value = Role.ADMINS)
    public Results getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Results.StatusOk().addData("users", users).setMessage("获取用户成功");
    }

    @GetMapping("/user")
    @Roles(value = Role.ADMIN)
    public Results getAllUser() {
        List<User> users = userService.getAllUser();
        return Results.StatusOk().addData("user", users).setMessage("获取用户成功");
    }

    @PostMapping("/login")
    public Results Login(@RequestBody User user) {
        try {
            String token = userService.Login(user);
            return Results.StatusOk().setMessage("登录成功").addData("jwt", token);
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Results Registration(@RequestBody User user) {
        try {
            userService.Registration(user);
            return Results.StatusOk().setMessage("注册成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }
}
