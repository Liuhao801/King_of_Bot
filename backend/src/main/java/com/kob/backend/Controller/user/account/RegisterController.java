package com.kob.backend.Controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/user/account/register")
    public Map<String,String> register(@RequestParam Map<String,String>map){
        String username=map.get("username");
        String pwd=map.get("pwd");
        String confirmedPwd=map.get("confirmedPwd");
        return registerService.register(username,pwd,confirmedPwd);
    }
}
