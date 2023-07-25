package com.kob.service.impl.utils;

import com.kob.service.impl.MatchingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{
    private List<Player> players=new ArrayList<>();
    private final ReentrantLock lock=new ReentrantLock();
    private static RestTemplate restTemplate;
    private final static String startGameUrl="http://127.0.0.1:8088/pk/start/game/";

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate=restTemplate;
    }

    public void addPlayer(Integer user_id,Integer rating){
        lock.lock();
        try {
            players.add(new Player(user_id,rating,0));
        }finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer user_id){
        lock.lock();
        try {
            List<Player>newPlayers=new ArrayList<>();
            for(Player player:players){
                if(!player.getUser_id().equals(user_id)){
                    newPlayers.add(player);
                }
            }
            players=newPlayers;
        }finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime(){ //所有当前玩家等待时间+1
        for(Player player:players){
            player.setWaiting_time(player.getWaiting_time()+1);
        }
    }

    private boolean checkMatched(Player a,Player b){  //判断两名玩家是否可以匹配
        int ratingDelta= Math.abs(a.getRating()-b.getRating());
        int waitingTime=Math.min(a.getWaiting_time(),b.getWaiting_time());
        return ratingDelta<=waitingTime*10;
    }

    private void sendResult(Player a,Player b){  //返回a和b作为匹配结果
        System.out.println("send result: "+a.getUser_id()+" "+b.getUser_id());
        MultiValueMap<String,String>data=new LinkedMultiValueMap<>();
        data.add("a_id",a.getUser_id().toString());
        data.add("b_id",b.getUser_id().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);
    }

    private void matchPlayers(){  //尝试匹配所有玩家
        System.out.println("match players: "+players.toString());
        boolean[] used=new boolean[players.size()];
        for(int i=0;i<players.size();i++){
            if(used[i])continue;
            for(int j=i+1;j<players.size();j++){
                if(used[j])continue;
                Player a=players.get(i),b=players.get(j);
                if(checkMatched(a,b)){
                    used[i]=used[j]=true;
                    sendResult(a,b);
                    break;
                }
            }
        }

        List<Player>newPlayer=new ArrayList<>();
        for(int i=0;i<players.size();i++){
            if(!used[i]) {
                newPlayer.add(players.get(i));
            }
        }
        players=newPlayer;
    }

    @Override
    public void run() {
        while (true){
            try {
                sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
