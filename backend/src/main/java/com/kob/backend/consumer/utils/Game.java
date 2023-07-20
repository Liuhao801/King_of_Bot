package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_wall_count;//墙个数
    final private int[][] g;
    final private int[] dx={-1,0,1,0}, dy={0,1,0,-1};

    public Game(Integer rows,Integer cols,Integer inner_wall_count){
        this.rows=rows;
        this.cols=cols;
        this.inner_wall_count=inner_wall_count;
        this.g=new int[rows][cols];
    }

    public int[][] getG(){
        return g;
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
}
