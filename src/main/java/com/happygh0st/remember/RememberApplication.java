package com.happygh0st.remember;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.happygh0st.remember.mapper")
@EnableScheduling
public class RememberApplication {

    public static void main(String[] args) {
        SpringApplication.run(RememberApplication.class, args);
    }

}
