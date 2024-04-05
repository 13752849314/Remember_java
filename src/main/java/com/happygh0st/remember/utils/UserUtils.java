package com.happygh0st.remember.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserUtils {

    private final Map<String, String> login = new HashMap<>();

    public void addInfo(String username, String token) throws Exception {
        String s = login.get(username);
        if (s != null) {
            throw new Exception("用户已经登录，请勿重复登录");
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

    public LocalDateTime getLocalTime() {
        return LocalDateTime.now();
    }
}
