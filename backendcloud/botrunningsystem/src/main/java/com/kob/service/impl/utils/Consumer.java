package com.kob.service.impl.utils;

import com.kob.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl="http://127.0.0.1:8088/pk/receive/bot/move/";

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate=restTemplate;
    }

    public void startTimeout(long timeout,Bot bot){
        this.bot=bot;
        this.start();

        try {
            this.join(timeout); //最多等待timeout秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt(); //中断当前线程
        }

    }

    private String addUid(String code,String uid){ //在Bot类名后面加上uid
        int k=code.indexOf(" implements com.kob.utils.BotInterface");
        return code.substring(0,k)+uid+code.substring(k);
    }
    @Override
    public void run() {
        UUID uuid=UUID.randomUUID(); //返回随机字符串
        String uid=uuid.toString().substring(0,8); //返回前8位

        BotInterface botInterface= Reflect.compile(
                "com.kob.utils.Bot"+uid,
                addUid(bot.getBotCode(),uid)
        ).create().get();

        Integer direction=botInterface.nextMove(bot.getInput());
        System.out.println("move direction:"+bot.getUserId()+" "+direction);

        MultiValueMap<String,String>data=new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);
    }
}