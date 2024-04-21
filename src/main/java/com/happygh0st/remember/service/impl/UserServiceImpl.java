package com.happygh0st.remember.service.impl;

import com.happygh0st.remember.common.Modifiable;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.UserMapper;
import com.happygh0st.remember.service.UserService;
import com.happygh0st.remember.utils.JwtUtils;
import com.happygh0st.remember.utils.UserUtils;
import com.happygh0st.remember.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
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
        Date now = new Date();
        user.setRoles("user"); // 通过注册的用户只能为 user
        user.setCreated_at(now);
        user.setUpdated_at(now);
        userMapper.insert(user);
        log.info("新用户：{}注册成功", user.getUsername());
    }

    @Override
    public void Logout(String username, String token) {
        if (!userUtils.deleteInfo(username, token)) {
            throw new RuntimeException("用户已经退出");
        }
        log.info("用户：" + username + "成功退出");
    }

    @Override
    public void Delete(String controller, String username) {
        User userC = userMapper.getUserByUsername(controller);
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户：" + username + "已经删除过了");
        }
        String userCRoles = userC.getRoles();
        String userRoles = user.getRoles();
        Date now = new Date();
        user.setDeleted_at(now);
        user.setUpdated_at(now);
        if (controller.equals(username)) {
            userMapper.updateById(user);
            userUtils.deleteInfo(username);
            log.info("用户：{}成功注销", controller);
            throw new RuntimeException("注销成功");
        } else {
            if (userCRoles.equals(Role.ADMINS.getValue()) && !userRoles.equals(Role.ADMINS.getValue())) {
                userMapper.updateById(user);
            } else if (userCRoles.equals(Role.ADMIN.getValue()) && userRoles.equals(Role.USER.getValue())) {
                userMapper.updateById(user);
            } else {
                log.info("用户：{}没有权限删除用户：{}", controller, username);
                throw new RuntimeException("用户：" + controller + "没有权限删除用户：" + username);
            }
            log.info("用户：{}成功删除用户：{}", controller, username);
        }
    }

    @Override
    public void ChangePassword(String oldPassword, String newPassword) {
        HttpServletRequest request = userUtils.getRequest();
        User user = (User) request.getAttribute("user");
        // 验证旧密码是否正确
        if (!user.getPassword().equals(Util.PasswordEncrypt(oldPassword))) {
            throw new RuntimeException("密码错误");
        }
        // 新旧密码不能相同
        if (oldPassword.equals(newPassword)) {
            throw new RuntimeException("新旧密码不能相同");
        }
        // 修改信息
        user.setPassword(Util.PasswordEncrypt(newPassword));
        user.setUpdated_at(userUtils.getLocalTime());
        userMapper.updateById(user);
        // 退出登录
        Logout(user.getUsername(), request.getHeader("jwt"));
    }

    @Override
    public User getUserInfo() {
        HttpServletRequest request = userUtils.getRequest();
        User user = (User) request.getAttribute("user");
        return user.Desensitization();
    }

    @Override
    public void ChangeUserInfo(Map<String, String> map) {
        HttpServletRequest request = userUtils.getRequest();
        User user = (User) request.getAttribute("user");
        try {
            userUtils.changeFields(user, map, Modifiable.class);
            user.setUpdated_at(userUtils.getLocalTime());
            userMapper.updateById(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void ChangeUserInfo(Integer id, Map<String, String> map) {
        User userC = userUtils.getUser();
        User user = userMapper.selectById(id);
        if (Role.ge(userC.getRoles(), user.getRoles()) || userC.getUsername().equals(user.getUsername())) {
            try {
                userUtils.changeFields(user, map, Modifiable.class);
                user.setUpdated_at(userUtils.getLocalTime());
                userMapper.updateById(user);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("权限不够");
        }
    }

    @Override
    public void addUser(User user) {
        User userC = userUtils.getUser();
        if (Role.ge(userC.getRoles(), user.getRoles())) {
            user.setPassword(Util.PasswordEncrypt(user.getPassword()));
            Date now = userUtils.getLocalTime();
            user.setCreated_at(now);
            user.setUpdated_at(now);
            userMapper.insert(user);
            log.info("新用户：{}添加成功", user.getUsername());
        } else {
            throw new RuntimeException("权限不够");
        }
    }
}
