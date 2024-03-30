package com.happygh0st.remember;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.happygh0st.remember.mapper")
public class RememberApplication {

    public static void main(String[] args) {
        SpringApplication.run(RememberApplication.class, args);
    }

}
