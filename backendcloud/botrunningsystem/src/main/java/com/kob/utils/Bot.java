package com.kob.utils;

import java.util.Random;

public class Bot implements com.kob.utils.BotInterface{
    @Override
    //判断下一步能走的方向
    public Integer nextMove(String input) {
        Random random=new Random();
        return random.nextInt(4);
    }
}
