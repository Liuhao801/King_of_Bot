import {GameObject} from "@/assets/scripts/GameObject";
import {Cell} from "@/assets/scripts/Cell";

export class Snake extends GameObject{
    constructor(info,gamemap) {
        super();
        this.id=info.id;
        this.color=info.color;
        this.gamemap=gamemap;

        this.cells=[new Cell(info.r,info.c)];  //存放蛇的身体，cells[0]是蛇头
        this.next_cell=null;//下一步位置
        this.speed=5;//蛇每秒走5个格子

        this.dr=[-1,0,1,0];//偏移量
        this.dc=[0,1,0,-1];
        this.direction=-1;//-1表示没有方向,0123分别表示上右下左
        this.status="idle";//"idle"表示静止,"move"表示正在移动,"die"表示死亡

        this.step=0;//回合数
        this.eps=1e-2;//允许的误差

        this.eye_direction=0;  //眼睛方向
        if(this.id===1)this.eye_direction=2;  //初始化左下角蛇眼睛向上，右上角蛇眼睛向下
        this.eye_dx=[
            [-1,1],
            [1,1],
            [1,-1],
            [-1,-1],
        ];
        this.eye_dy=[
            [-1,-1],
            [-1,1],
            [1,1],
            [1,-1],
        ]

    }

    start() {

    }

    set_direction(d){  //设置蛇的方向
        this.direction=d;
    }

    check_tail_increasing(){  //判断蛇是否增长
        if(this.step<=10)return true;
        if(this.step%3===1)return true;
        return false;
    }

    next_step(){  //将蛇的状态变成下一步
        const d=this.direction;
        this.next_cell=new Cell(this.cells[0].r+this.dr[d],this.cells[0].c+this.dc[d]);
        this.eye_direction=d;
        this.direction=-1; //清空操作
        this.status="move";
        this.step++;

        if(!this.gamemap.check_vaild(this.next_cell)){  //下一步走到非法位置，蛇直接去世
            this.status="die";
        }

        //将蛇的身体向后移动一格
        const k=this.cells.length;
        for(let i=k;i>0;i--){
            this.cells[i]=JSON.parse(JSON.stringify(this.cells[i-1])); //深拷贝
        }
    }

    update_move(){  //移动蛇,支持斜线
        const dx=this.next_cell.x-this.cells[0].x;
        const dy=this.next_cell.y-this.cells[0].y;
        const distance =Math.sqrt(dx*dx+dy*dy);

        if(distance<this.eps){  //走到目标位置
            this.cells[0]=this.next_cell;  //添加新蛇头
            this.next_cell=null;
            this.status="idle";  //走完了，停下来

            if(!this.check_tail_increasing()){
                this.cells.pop();  //删除蛇尾
            }

        }else{
            const move_distance=this.speed*this.timedalta/1000;  //每两帧之间走的距离
            this.cells[0].x+=move_distance*dx/distance;
            this.cells[0].y+=move_distance*dy/distance;

            if(!this.check_tail_increasing()){  //若蛇不增长,则需要移动尾部
                const k=this.cells.length;
                const tail=this.cells[k-1],tail_target=this.cells[k-2];
                const tail_dx=tail_target.x-tail.x;
                const tail_dy=tail_target.y-tail.y;
                tail.x+=move_distance*tail_dx/distance;
                tail.y+=move_distance*tail_dy/distance;
            }
        }
    }

    update() {
        if(this.status==="move"){
            this.update_move();
        }
        this.render();
    }

    render(){
        const L=this.gamemap.L;
        const ctx=this.gamemap.ctx;

        ctx.fillStyle=this.color;
        if(this.status==="die"){
            ctx.fillStyle="white";
        }
        for(const cell of this.cells){  //画蛇的身体
            ctx.beginPath(); //画圆
            ctx.arc(cell.x*L,cell.y*L,L/2*0.8,0,Math.PI*2);  //*0.8变小一点
            ctx.fill();
        }

        //将相邻两个圆之间填充矩形
        for(let i=1;i<this.cells.length;i++){
            const a=this.cells[i-1],b=this.cells[i];
            if(Math.abs(a.x-b.x)<this.eps && Math.abs(a.y-b.y)<this.eps){  //a,b重合
                continue;
            }
            if(Math.abs(a.x-b.x)<this.eps){
                //  L缩小20%，所以两边各向内偏移0.1
                ctx.fillRect((a.x-0.5+0.1)*L,Math.min(a.y,b.y)*L,L*0.8,Math.abs(a.y-b.y)*L);
            }else{
                ctx.fillRect(Math.min(a.x,b.x)*L,(a.y-0.4)*L,Math.abs(a.x-b.x)*L,L*0.8);
            }
        }

        //画眼睛
        ctx.fillStyle="black";
        for(let i=0;i<2;i++){
            const eye_x=(this.cells[0].x+this.eye_dx[this.eye_direction][i]*0.15)*L;
            const eye_y=(this.cells[0].y+this.eye_dy[this.eye_direction][i]*0.15)*L;

            ctx.beginPath();
            ctx.arc(eye_x,eye_y,L*0.05,0,Math.PI*2);
            ctx.fill();
        }
    }
}