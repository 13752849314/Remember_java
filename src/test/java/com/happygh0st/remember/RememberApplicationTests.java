package com.happygh0st.remember;

import com.happygh0st.remember.common.Modifiable;
import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.utils.JwtUtils;
import com.happygh0st.remember.utils.UserUtils;
import com.happygh0st.remember.utils.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class RememberApplicationTests {

    @Autowired
    private UserUtils userUtils;

    @Test
    void testPasswordEncrypt() {
        String string = "admin";
        String pass = Util.PasswordEncrypt(string);
        System.out.println(string);
        System.out.println(pass);
    }

    @Test
    void testRoles() {
        Role role = Role.USER;
        System.out.println(role);
        System.out.println(role == Role.USER);
        System.out.println(role == Role.ADMIN);
        System.out.println(role.getValue());
    }

    @Test
    void testJwt() throws Exception {
        User user = new User();
        user.setId(2);
        user.setUsername("1234");
        String token = JwtUtils.createToken(user);
        System.out.println(token);
        System.out.println(JwtUtils.checkToken(token));
    }

    @Test
    void testChangeFields() {
        User user = new User();
        user.setPhone("12345678911");
        user.setEmail("123@qq.com");
        System.out.println(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("phone", "11987654321");
        map.put("email", "321@qq.com");
        System.out.println(map);

        try {
            userUtils.changeFields(user, map, Modifiable.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(user);

    }
}
