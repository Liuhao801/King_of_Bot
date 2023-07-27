package com.kob;

import com.kob.service.BotRunningService;
import com.kob.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotRunningSystem {
    public static void main(String[] args) {
        BotRunningServiceImpl.botpool.start();
        SpringApplication.run(BotRunningSystem.class,args);
    }
}
