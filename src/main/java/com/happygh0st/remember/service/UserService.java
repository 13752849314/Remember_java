package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<User> getAllUser();

    String Login(User user) throws Exception;

    void Registration(User user);
}
