package com.kob.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;

public class Bot implements com.kob.utils.BotInterface{
    static class Cell{
        public int x,y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean check_tail_increasing(int step){ //判断蛇尾是否增长
        if(step<=10)return true;
        return step%3==1;
    }

    private List<Cell> getCells(int sx,int sy,String steps){ //得到蛇的身体
        steps=steps.substring(1,steps.length()-1);
        List<Cell>res=new ArrayList<>();

        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int x=sx,y=sy;
        int step=0;
        res.add(new Cell(x,y));

        for(int i=0;i<steps.length();i++){
            int d=steps.charAt(i)-'0';
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing(++step)){
                res.remove(0);
            }
        }
        return res;
    }

    @Override
    //判断下一步能走的方向
    public Integer nextMove(String input) {
        String[] strs=input.split("#");
        int[][] g=new int[12][13];
        for(int i=0,k=0;i<12;i++){
            for(int j=0;j<13;j++,k++){
                if(strs[0].charAt(k)=='1'){
                    g[i][j]=1;
                }
            }
        }

        int aSx=Integer.parseInt(strs[1]),aSy=Integer.parseInt(strs[2]);
        int bSx=Integer.parseInt(strs[4]),bSy=Integer.parseInt(strs[5]);

        List<Cell> aCells=getCells(aSx,aSy,strs[3]);
        List<Cell> bCells=getCells(bSx,bSy,strs[6]);

        for(Cell c:aCells)g[c.x][c.y]=1;
        for(Cell c:bCells)g[c.x][c.y]=1;

        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        for(int i=0;i<4;i++){
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            if(x<0||x>=12||y<0||y>=13)continue;;
            if(g[x][y]==1)continue;
            return i;
        }

        return 0;
    }
}
