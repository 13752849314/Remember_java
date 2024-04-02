package com.happygh0st.remember;

import com.happygh0st.remember.common.Role;
import com.happygh0st.remember.entity.User;
import com.happygh0st.remember.utils.JwtUtils;
import com.happygh0st.remember.utils.Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RememberApplicationTests {

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
        System.out.println(role==Role.USER);
        System.out.println(role==Role.ADMIN);
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
}
