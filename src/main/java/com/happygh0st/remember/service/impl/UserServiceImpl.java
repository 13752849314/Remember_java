package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.UserMapper;
import com.happygh0st.remember.service.UserService;
import com.happygh0st.remember.utils.Util;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    @Override
    public void Login(User user) {
        User userDb = userMapper.getUserByUsername(user.getUsername());
        if (Objects.isNull(userDb)) {
            throw new RuntimeException("用户不存在");
        }
        String pass = Util.PasswordEncrypt(user.getPassword());
        if (!pass.equals(userDb.getPassword())) {
            throw new RuntimeException("密码错误");
        }
    }

    @Override
    public void Registration(User user) {
        User userDb = userMapper.getUserByUsername(user.getUsername());
        if (!Objects.isNull(userDb)) {
            throw new RuntimeException("用户名已经存在");
        }
        user.setPassword(Util.PasswordEncrypt(user.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        user.setCreated_at(now);
        user.setUpdated_at(now);
        userMapper.insert(user);
    }
}
