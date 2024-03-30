package com.happygh0st.remember.controller;

import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    List<User> test() {
        return userService.getAllUsers();
    }

}
