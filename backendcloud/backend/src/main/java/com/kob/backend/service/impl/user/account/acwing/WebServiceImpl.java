package com.kob.backend.service.impl.user.account.acwing;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.user.account.acwing.utils.HttpClientUtil;
import com.kob.backend.service.user.account.acwing.WebService;
import com.kob.backend.utils.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class WebServiceImpl implements WebService {
    private final static String appId="5801";
    private final static String appSecret="053b98c39e234a70917e23b4b538cfc4";
    private final static String redirectUrl="https://app5801.acapp.acwing.com.cn/user/account/acwing/web/receive_code/";
    private final static String applyAccessToken="https://www.acwing.com/third_party/api/oauth2/access_token/";
    private final static String getUserInfoUrl="https://www.acwing.com/third_party/api/meta/identity/getinfo/";

    private final static Random random=new Random();

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject applyCode() {
        JSONObject resp=new JSONObject();
        String encodeUrl="";
        try {
            encodeUrl=URLEncoder.encode(redirectUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.put("result","failed");
            return resp;
        }

        StringBuilder state=new StringBuilder();
        for(int i=0;i<10;i++){
            state.append((char)(random.nextInt(10)+'0'));
        }
        resp.put("result","success");
        redisTemplate.opsForValue().set(state.toString(),"true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10)); //10分钟过期

        String applyCodeUrl="https://www.acwing.com/third_party/api/oauth2/web/authorize/?appid="+appId
                +"&redirect_uri="+encodeUrl
                +"&scope=userinfo"
                +"&state="+state;
        resp.put("apply_code_url",applyCodeUrl);
        return resp;
    }

    @Override
    public JSONObject receiveCode(String code, String state) {
        JSONObject resp=new JSONObject();
        resp.put("result","failed");
        if(code==null||state==null)return resp;
        if(Boolean.FALSE.equals(redisTemplate.hasKey(state)))return resp;

        redisTemplate.delete(state); //一个随机数只用一次

        //申请授权令牌access_token和用户的openid
        List<NameValuePair> nameValuePairs=new LinkedList<>(); //参数列表
        nameValuePairs.add(new BasicNameValuePair("appid",appId));
        nameValuePairs.add(new BasicNameValuePair("secret",appSecret));
        nameValuePairs.add(new BasicNameValuePair("code",code));

        String getString= HttpClientUtil.get(applyAccessToken,nameValuePairs); //返回结果
        if(getString==null)return resp;
        JSONObject getResp=JSONObject.parseObject(getString);
        String accessToken=getResp.getString("access_token");
        String openid=getResp.getString("openid");
        if(accessToken==null||openid==null)return resp;

        QueryWrapper<User> queryWrapper=new QueryWrapper<>(); //查询openid是否已经存在
        queryWrapper.eq("openid",openid);
        List<User> users=userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){ //用户存在
            User user=users.get(0);
            String jwt= JwtUtil.createJWT(user.getId().toString());
            resp.put("result","success");
            resp.put("jwt_token",jwt);
            return resp;
        }

        //申请用户信息
        nameValuePairs=new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token",accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid",openid));
        getString= HttpClientUtil.get(getUserInfoUrl,nameValuePairs); //返回结果
        if(getString==null)return resp;
        getResp=JSONObject.parseObject(getString);
        String username=getResp.getString("username");
        String photo=getResp.getString("photo");

        if(username==null||photo==null)return resp;
        //防止重名用户
        for(int i=0;i<100;i++){
            QueryWrapper<User>userNameQueryWrapper=new QueryWrapper<>();
            userNameQueryWrapper.eq("username",username);
            if(userMapper.selectList(userNameQueryWrapper).isEmpty()){
                break;
            }
            username+=(char)(random.nextInt(10)+'0');
            if(i==99)return resp;
        }

        User user=new User(
                null,
                username,
                null,
                photo,
                1500,
                openid
        );
        userMapper.insert(user);
        String jwt= JwtUtil.createJWT(user.getId().toString());
        resp.put("result","success");
        resp.put("jwt_token",jwt);
        return resp;
    }
}
