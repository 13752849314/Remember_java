package com.happygh0st.remember.service;

import com.happygh0st.remember.common.Results;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.common.Roles;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class RolesService {
    @Around("@annotation(com.happygh0st.remember.common.Roles)")
    public Results around(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String jwt = request.getHeader("jwt");
            if (Objects.isNull(jwt) || jwt.isEmpty()) {
                return Results.StatusErr().setMessage("请先登录");
            }
            // todo 鉴权
            Authentication(jwt, point);
            Results results = (Results) point.proceed();
            results.addData("jwt", jwt);
            return results;
        } else {
            return Results.StatusErr().setMessage("请先登录");
        }
    }

    private boolean Authentication(String jwt, ProceedingJoinPoint point) throws NoSuchMethodException {
        // 获取接口上的注解 value
        Class<?> targetClass = point.getTarget().getClass();
        Method method = targetClass.getMethod(point.getSignature().getName());
        Roles annotation = method.getAnnotation(Roles.class);
        Role value = annotation.value();
        String valueInterface = value.getValue();
        // 解析 jwt 获取该用户的实际权限
        // todo
        System.out.println(valueInterface + " --- " + jwt);
        return true;
    }
}
