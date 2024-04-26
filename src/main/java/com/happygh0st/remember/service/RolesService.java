package com.happygh0st.remember.service;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.mapper.UserMapper;
import com.happygh0st.remember.utils.JwtUtils;
import com.happygh0st.remember.utils.UserUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

@Component
@Aspect
public class RolesService {

    private final UserMapper userMapper;
    private final UserUtils userUtils;

    public RolesService(UserMapper userMapper, UserUtils userUtils) {
        this.userMapper = userMapper;
        this.userUtils = userUtils;
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
            try {
                boolean authentication = Authentication(jwt, point, request);
                if (authentication) {
                    return (Results) point.proceed();
                }
                return Results.StatusErr().setMessage("权限不够");
            } catch (Exception e) {
                return Results.StatusErr().setMessage(e.getMessage());
            }
        } else {
            return Results.StatusErr().setMessage("请先登录");
        }
    }

    private boolean Authentication(String jwt, ProceedingJoinPoint point, HttpServletRequest request) throws Exception {
        // 获取接口上的注解 value
        Class<?> targetClass = point.getTarget().getClass();
        // 获取接口参数类型列表
        Object[] args = point.getArgs();
        Class<?>[] param = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                param[i] = Map.class;
            } else if (args[i] instanceof MultipartFile) {
                param[i] = MultipartFile.class;
            } else if (args[i] instanceof HttpServletResponse) {
                param[i] = HttpServletResponse.class;
            } else {
                param[i] = args[i].getClass();
            }
        }
        Method method = targetClass.getMethod(point.getSignature().getName(), param); // 拿到接口方法
        Roles annotation = method.getAnnotation(Roles.class); // 拿到接口上指定注解
        Role value = annotation.value(); // 获取注解值
        Map<String, Object> map = JwtUtils.checkToken(jwt);
        int id = (int) map.get("id");
        String username = (String) map.get("username");
        String info = userUtils.getInfo(username);
        if (info == null || !info.equals(jwt)) {
            throw new Exception("用户未登录或token过期");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new Exception("该用户：" + username + "已经注销");
        }
        request.setAttribute("user", user);
        String roles = user.getRoles();
        return value.ge(roles) && username.equals(user.getUsername());
    }
}
