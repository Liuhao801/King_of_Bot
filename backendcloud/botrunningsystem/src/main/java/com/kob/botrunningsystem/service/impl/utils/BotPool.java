package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
    private final ReentrantLock lock=new ReentrantLock();
    private final Condition condition=lock.newCondition(); //条件变量
    private Queue<Bot>bots=new LinkedList<>();

    public void addBot(Integer userId,String botCode,String input){
        lock.lock();
        try{
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll(); //唤醒所有被阻塞的线程
        }finally {
            lock.unlock();
        }
    }

    //执行bot的代码
    private void consume(Bot bot){
        Consumer consumer=new Consumer();
        consumer.startTimeout(2000,bot);
    }

    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await(); //阻塞当前进程,同时释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else{
                Bot bot=bots.remove(); //返回队头并删除
                lock.unlock();
                consume(bot); //需要编译代码,所有耗时,可能需要几秒钟
            }
        }
    }
}
