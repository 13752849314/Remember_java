package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.UserMapper;
import com.happygh0st.remember.service.UserService;
import com.happygh0st.remember.utils.JwtUtils;
import com.happygh0st.remember.utils.UserUtils;
import com.happygh0st.remember.utils.Util;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserUtils userUtils;

    public UserServiceImpl(UserMapper userMapper, UserUtils userUtils) {
        this.userMapper = userMapper;
        this.userUtils = userUtils;
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
    public String Login(String username, String password) throws Exception {
        User userDb = userMapper.getUserByUsername(username);
        if (Objects.isNull(userDb)) {
            throw new RuntimeException("用户不存在");
        }
        String pass = Util.PasswordEncrypt(password);
        if (!pass.equals(userDb.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String token = JwtUtils.createToken(userDb);
        userUtils.addInfo(username, token);
        return token;
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

    @Override
    public void Logout(String username, String token) {
        if (!userUtils.deleteInfo(username, token)) {
            throw new RuntimeException("用户已经退出");
        }
    }

    @Override
    public void Delete(String controller, String username) {
        User userC = userMapper.getUserByUsername(controller);
        User user = userMapper.getUserByUsername(username);
        String userCRoles = userC.getRoles();
        String userRoles = user.getRoles();
        LocalDateTime now = LocalDateTime.now();
        user.setDeleted_at(now);
        user.setUpdated_at(now);
        if (controller.equals(userRoles) && userCRoles.equals(Role.USER.getValue())) {
            userMapper.updateById(user);
            userUtils.deleteInfo(username);
            throw new RuntimeException("注销成功");
        }
        if (userCRoles.equals(Role.ADMINS.getValue()) && !userRoles.equals(Role.ADMINS.getValue())) {
            userMapper.updateById(user);
        } else if (!userCRoles.equals(Role.ADMINS.getValue()) && userRoles.equals(Role.USER.getValue())) {
            userMapper.updateById(user);
        } else {
            throw new RuntimeException("用户：" + controller + "没有权限删除用户：" + username);
        }
    }
}
