package com.happygh0st.remember;

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
}
