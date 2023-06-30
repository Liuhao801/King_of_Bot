package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("pyxc"));
        System.out.println(passwordEncoder.encode("pgzn"));
        System.out.println(passwordEncoder.encode("phh"));
        System.out.println(passwordEncoder.encode("pdd"));

    }

}
