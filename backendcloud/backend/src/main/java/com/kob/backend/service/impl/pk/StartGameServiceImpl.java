package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer a_id, Integer b_id) {
        System.out.println("start game: "+a_id+" "+b_id);
        WebSocketServer.startGame(a_id,b_id);
        return "start game success";
    }
}
