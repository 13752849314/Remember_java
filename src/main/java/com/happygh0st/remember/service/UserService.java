package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<User> getAllUser();

    String Login(String username, String password) throws Exception;

    void Registration(User user);

    void Logout(String username, String token);

    void Delete(String controller, String username);

    void ChangePassword(String oldPassword,String newPassword);
}
