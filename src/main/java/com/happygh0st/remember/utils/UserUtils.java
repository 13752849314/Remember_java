package com.happygh0st.remember.utils;

import com.happygh0st.remember.common.Modifiable;
import com.happygh0st.remember.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class UserUtils {

    private final Map<String, String> login = new HashMap<>();

    public void addInfo(String username, String token) throws Exception {
        String s = login.get(username);
        if (s != null) {
            throw new Exception("用户已经登录，请勿重复登录\n" + "token:" + token);
        }
        login.put(username, token);
        log.info("用户：" + username + "成功登录");
    }

    public String getInfo(String username) {
        return login.get(username);
    }

    public boolean deleteInfo(String username, String token) {
        return login.remove(username, token);
    }

    public void deleteInfo(String username) {
        login.remove(username);
    }

    @Scheduled(cron = "* * 0/2 * * ?")
    private void maintainLogin() {
        if (login.isEmpty()) return;
        List<String> deleted = new ArrayList<>();
        for (String i : login.keySet()) {
            String token = login.get(i);
            try {
                JwtUtils.checkToken(token);
            } catch (Exception e) {
                deleted.add(i);
            }
        }
        if (deleted.isEmpty()) return;
        for (String i : deleted) {
            login.remove(i);
        }
        log.info("当前在线用户：" + login.keySet());
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

    public LocalDateTime getLocalTime() {
        return LocalDateTime.now();
    }

    public <T> void changeFields(T t, Map<String, String> map, Class<Modifiable> anno) throws Exception {
        // 拿到用户所有带指定注解的字段
        List<Field> fields = Util.getFieldsWithAnnotation(t.getClass(), anno);
        for (Field field : fields) {
            String name = field.getName();
            String s = map.get(name);
            if (s == null) continue;
            Modifiable modifiable = field.getDeclaredAnnotation(anno);
            if (modifiable.value()) { // 可修改
                field.setAccessible(true);
                if (modifiable.type() == String.class) {
                    field.set(t, s);
                } else if (modifiable.type() == Date.class) {
                    SimpleDateFormat format = new SimpleDateFormat(modifiable.pattern());
                    Date date = format.parse(s);
                    field.set(t, date);
                } else { //其它类型，需要该类型有一个接收String的构造器
                    Class<?> clazz = modifiable.type();
                    Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
                    constructor.setAccessible(true);
                    Object o = constructor.newInstance(s);
                    field.set(t, o);
                }
            }
        }
    }
}
