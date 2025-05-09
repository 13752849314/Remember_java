package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> getAllUsers();

    List<User> getAllUser();

    String Login(String username, String password) throws Exception;

    void Registration(User user);

    void Logout(String username, String token);

    void Delete(String controller, String username);

    void ChangePassword(String oldPassword, String newPassword);

    User getUserInfo();

    void ChangeUserInfo(Map<String, String> map);

    void ChangeUserInfo(Integer id, Map<String, String> map);

    void addUser(User user);
}
