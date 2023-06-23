package com.kob.backend.Controller.pk;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getInfo/")
    public Map<String,String> getBotInfo(){

        Map<String,String>bot1=new HashMap<>();
        bot1.put("name","yxc");
        bot1.put("rating","10000");

        return bot1;
    }
}
