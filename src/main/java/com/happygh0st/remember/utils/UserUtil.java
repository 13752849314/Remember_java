package com.happygh0st.remember.utils;

import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.Bill;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserUtil {

    private final UserMapper userMapper;

    public UserUtil(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getRequest();
        }
        throw new RuntimeException("请求失败");
    }

    public User getUser() {
        HttpServletRequest request = getRequest();
        return (User) request.getAttribute("user");
    }

    public boolean isDo(User user, Bill bill) {
        if (user.getUsername().equals(bill.getUsername())) return true;
        User userBill = userMapper.getUserByUsername(bill.getUsername());
        return Role.ge(user.getRoles(), userBill.getRoles());
    }

}
