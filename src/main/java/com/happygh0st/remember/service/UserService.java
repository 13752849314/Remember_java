package com.happygh0st.remember.service;

import com.happygh0st.remember.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

}
