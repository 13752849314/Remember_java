package com.happygh0st.remember.controller;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.service.UserService;
import com.happygh0st.remember.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public Results Login(@RequestBody Map<String, String> map) {
        try {
            String token = userService.Login(map.get("username"), map.get("password"));
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

    @PostMapping("/logout")
    public Results Logout(@RequestHeader("jwt") String token) {
        try {
            Map<String, Object> user = JwtUtils.checkToken(token);
            userService.Logout((String) user.get("username"), token);
            return Results.StatusOk().setMessage("退出成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }

    @PostMapping("/delete")
    @Roles()
    public Results Delete(@RequestHeader("jwt") String token, @RequestBody LinkedHashMap<String, String> map) {
        try {
            Map<String, Object> user = JwtUtils.checkToken(token);
            userService.Delete((String) user.get("username"), map.get("username"));
            return Results.StatusOk().setMessage("删除成功");
        } catch (Exception e) {
            return Results.StatusErr().setMessage(e.getMessage());
        }
    }
}
