package com.kob.backend.Controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/user/all/")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }

    @GetMapping("user/{userId}")
    public User getuser(@PathVariable int userId){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/user/add/{id}/{username}/{pwd}/")
    public String addUser(
            @PathVariable int id,
            @PathVariable String username,
            @PathVariable String pwd){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String encodedPwd=passwordEncoder.encode(pwd);
        User user=new User(id,username,encodedPwd);
        userMapper.insert(user);
        return "Add User Successfully";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable int id){
        userMapper.deleteById(id);
        return "Delete User Successfully";
    }

}