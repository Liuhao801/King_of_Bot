package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String pwd, String confirmedPwd) {
        Map<String,String>map=new HashMap<>();
        if(username==null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        username=username.trim();
        if(username.length()==0){
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if(username.length()>100){
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }
        if(pwd==null||confirmedPwd==null) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        if(pwd.length()==0||confirmedPwd.length()==0){
            map.put("error_message","密码不能为空");
            return map;
        }
        if(!pwd.equals(confirmedPwd)){
            map.put("error_message","两次密码不一致");
            return map;
        }
        if(pwd.length()>100||confirmedPwd.length()>100){
            map.put("error_message", "密码长度不能大于100");
            return map;
        }

        QueryWrapper<User>queryWrapper=new QueryWrapper<User>();
        queryWrapper.eq("username",username);
        List<User> users=userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("error_message","用户名已存在");
            return map;
        }

        String encodeedPwd=passwordEncoder.encode(pwd);
        String photo="https://cdn.acwing.com/media/user/profile/photo/282278_lg_1b88926ab4.jpg";
        User user=new User(null,username,encodeedPwd,photo,1500);
        userMapper.insert(user);

        map.put("error_message","success");
        return map;
    }
}
