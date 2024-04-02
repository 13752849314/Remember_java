package com.happygh0st.remember.service;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.UserMapper;
import com.happygh0st.remember.utils.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

@Component
@Aspect
public class RolesService {

    private final UserMapper userMapper;

    public RolesService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Around("@annotation(com.happygh0st.remember.common.Roles)")
    public Results around(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String jwt = request.getHeader("jwt");
            if (Objects.isNull(jwt) || jwt.isEmpty()) {
                return Results.StatusErr().setMessage("请先登录");
            }
            boolean authentication = Authentication(jwt, point);
            if (authentication) {
                return (Results) point.proceed();
            }
            return Results.StatusErr().setMessage("权限不够");
        } else {
            return Results.StatusErr().setMessage("请先登录");
        }
    }

    private boolean Authentication(String jwt, ProceedingJoinPoint point) throws Exception {
        // 获取接口上的注解 value
        Class<?> targetClass = point.getTarget().getClass();
        Object[] args = point.getArgs();
        Class<?>[] param = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            param[i] = args[i].getClass();
        }
        Method method = targetClass.getMethod(point.getSignature().getName(), param);
        Roles annotation = method.getAnnotation(Roles.class);
        Role value = annotation.value();
        String valueInterface = value.getValue();
        Map<String, Object> map = JwtUtils.checkToken(jwt);
        int id = (int) map.get("id");
        String username = (String) map.get("username");
        User user = userMapper.selectById(id);
        String roles = user.getRoles();
        return roles.equals(valueInterface) && username.equals(user.getUsername());
    }
}
