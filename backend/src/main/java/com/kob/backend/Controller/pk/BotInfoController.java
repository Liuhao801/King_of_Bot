package com.kob.backend.Controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getInfo/")
    public List<Map<String,String>> getBotInfo(){
        List<Map<String,String>>list=new LinkedList<>();
        Map<String,String>bot1=new HashMap<>();
        bot1.put("name","yxc");
        bot1.put("rating","10000");

        Map<String,String>bot2=new HashMap<>();
        bot2.put("name","gzn");
        bot2.put("rating","5");

        list.add(bot1);
        list.add(bot2);
        return list;
    }
}
