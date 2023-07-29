package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.security.core.parameters.P;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_wall_count;//墙个数
    private final int[][] g;
    private final int[] dx={-1,0,1,0}, dy={0,1,0,-1};
    private final Player playerA,playerB;

    private Integer nextStepA=null;//下一步操作
    private Integer nextStepB=null;
    private ReentrantLock lock=new ReentrantLock();//锁

    private String status="playing"; //playing->finished
    private String loser=""; //all:平局; A:A输; B:B输

    private final static String addBotUrl="http://127.0.0.1:8090/bot/add/";

    public Game(
            Integer rows,
            Integer cols,
            Integer inner_wall_count,
            Integer IdA,
            Bot botA,
            Integer IdB,
            Bot botB
    ){
        this.rows=rows;
        this.cols=cols;
        this.inner_wall_count=inner_wall_count;
        this.g=new int[rows][cols];

        Integer botIdA=-1,botIdB=-1;
        String botCodeA="",botCodeB="";
        if(botA!=null){
            botIdA=botA.getId();
            botCodeA=botA.getContent();
        }
        if(botB!=null){
            botIdB=botB.getId();
            botCodeB=botB.getContent();
        }

        this.playerA=new Player(IdA,botIdA,botCodeA,this.rows-2,1,new ArrayList<>());
        this.playerB=new Player(IdB,botIdB,botCodeB,1,this.cols-2,new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public int[][] getG(){
        return g;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }

    //判断(sx,sy)到(tx,ty)是否连通
    private boolean check_connectivity(int sx,int sy,int tx,int ty){
        if(sx==tx && sy==ty)return true;
        g[sx][sy]=1;

        for(int i=0;i<4;i++){
            int x=sx+dx[i],y=sy+dy[i];
            if(x<0||x>=this.rows||y<=0||y>=this.cols||g[x][y]==1)continue;;
            if(check_connectivity(x,y,tx,ty)){
                g[sx][sy]=0;
                return true;
            }
        }

        g[sx][sy]=0;
        return false;
    }

    private boolean draw(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++) {
                g[i][j] = 0;
            }
        }
        //给四周加上障碍
        for(int r=0;r<this.rows;r++){
            g[r][0]=g[r][this.cols-1]=1;
        }
        for(int c=0;c<this.cols;c++){
            g[0][c]=g[this.rows-1][c]=1;
        }

        Random random=new Random();
        //随机生成墙
        for(int i=0;i<inner_wall_count/2;i++){
            //随机生成1000次
            for(int j=0;j<=1000;j++){
                int r=random.nextInt(this.rows);
                int c=random.nextInt(this.cols);

                if(g[r][c]==1 || g[this.rows-1-r][this.cols-1-c]==1)//中心对称
                    continue;
                if(r==this.rows-2&&c==1 || c==this.cols-2&&r==1)//不会在左下和右上角建墙
                    continue;

                g[r][c]=g[this.rows-1-r][this.cols-1-c]=1;
                break;
            }
        }
        return check_connectivity(this.rows-2,1,1,this.cols-2); //检查起点到终点是否连通
    }

    public void createMap(){
        for(int i=0;i<=1000;i++)
            if(draw())
                break;
    }

    private String getInput(Player player){ //将当前的局面信息，编码成字符串
        Player me,you;
        if(playerA.getId().equals(player.getId())){
            me=playerA;
            you=playerB;
        }else{
            me=playerB;
            you=playerA;
        }

        return getMapString()+"#"+
                me.getSx()+"#"+
                me.getSy()+"#("+
                me.getStepsString()+")#"+
                you.getSx()+"#"+
                you.getSy()+"#("+
                you.getStepsString()+")";
    }

    private void sendBotCode(Player player){  //发送Bot
        if(player.getBotId().equals(-1))return; //亲自出马
        MultiValueMap<String,String>data=new LinkedMultiValueMap<>();
        data.add("user_id",player.getId().toString());
        data.add("bot_code",player.getBotCode());
        data.add("input",getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl,data,String.class);
    }

    private boolean nextStep(){ //等待两名玩家下一步操作
        try {
            Thread.sleep(200); //前端每200ms画一个格子
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for(int i=0;i<50;i++){ //每名玩家有5s输入时间
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA!=null&&nextStepB!=null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean check_valid(List<Cell>cellsA,List<Cell>cellsB){
        int n=cellsA.size();
        Cell head=cellsA.get(n-1); //蛇头
        if(g[head.getX()][head.getY()]==1)return false; //撞墙

        for(int i=0;i<n-1;i++){
            if(cellsA.get(i).getX()==head.getX()&&cellsA.get(i).getY()==head.getY()){
                return false;
            }
        }
        for(int i=0;i<n-1;i++){
            if(cellsB.get(i).getX()==head.getX()&&cellsB.get(i).getY()==head.getY()){
                return false;
            }
        }
        return true;
    }

    private void judge(){
        List<Cell>cellsA= playerA.getCells();
        List<Cell>cellsB= playerB.getCells();

        boolean validA=check_valid(cellsA,cellsB);
        boolean validB=check_valid(cellsB,cellsA);

        if(!validA||!validB){
            status="finished";

            if(!validA&&!validB){
                loser="all";
            }else if(!validA){
                loser="A";
            }else {
                loser="B";
            }

        }
    }

    private void sendAllMessage(String message){  //向两名玩家发送message
        if(WebSocketServer.users.get(playerA.getId())!=null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId())!=null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendMove(){  //发送双方这一步的操作
        lock.lock();
        try {
            JSONObject resp=new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA=nextStepB=null; //清空这一步操作
        }finally {
            lock.unlock();
        }
    }

    private String getMapString(){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void updateUserRating(Player player,Integer rating){
        User user=WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private void saveToDatabase(){
        Integer ratingA=WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB=WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if("A".equals(loser)){
            ratingA-=5;
            ratingB+=5;
        }else if("B".equals(loser)){
            ratingA+=5;
            ratingB-=5;
        }

        updateUserRating(playerA,ratingA);
        updateUserRating(playerB,ratingB);

        Record record=new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult(){  //发送游戏结果
        JSONObject resp=new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }


    @Override
    public void run() { //线程的执行入口

        for(int i=0;i<1000;i++){
            if(nextStep()){ //A和B都有下一步
                judge();
                if(status=="playing"){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }else{
                status="finished";
                lock.lock();
                try{
                    if(nextStepA==null&&nextStepB==null){
                        loser="all";
                    }else if(nextStepA==null) {
                        loser = "A";
                    }else{
                        loser="B";
                    }
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
