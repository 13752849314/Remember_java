package com.happygh0st.remember.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.happygh0st.remember.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JwtUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";


    public static String key;
    public static SimpleDateFormat TimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${remember.key}")
    public void setKey(String key) {
        JwtUtils.key = key;
    }

    public static String encrypt(String plaintext, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String ciphertext, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String createToken(User user) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 3);
        map.put("ExpiresAt", TimeFormat.format(calendar.getTime()));
        String s = JSON.toJSONString(map);
        return encrypt(s, key);
    }

    public static Map<String, Object> checkToken(String token) throws Exception {
        String decrypt = decrypt(token, key);
        Map<String, Object> map = JSON.parseObject(decrypt, new TypeReference<Map<String, Object>>() {
        });
        String expiresAt = (String) map.get("ExpiresAt");
        Date exTime = TimeFormat.parse(expiresAt);
        if (Calendar.getInstance().after(exTime)) {
            throw new Exception("token 已过期，请重新登录");
        }
        return map;
    }
}
