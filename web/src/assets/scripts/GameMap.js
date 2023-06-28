import {GameObject} from "@/assets/scripts/GameObject";
import {Wall} from "@/assets/scripts/Wall";
import {Snake} from "@/assets/scripts/Snake";

export class GameMap extends GameObject{
    constructor(ctx,parent) {
        super();
        this.ctx=ctx;  //画布
        this.parent=parent  //画布父元素，用于动态修改画布长宽
        this.L=0;  //一个单位的长度

        this.rows=12;
        this.cols=13;

        this.inner_walls_count=20;  //墙个数
        this.walls=[];

        this.snakes=[
            new Snake({id:0,color:"#4876EC",r:this.rows-2,c:1},this),
            new Snake({id:1,color:"#F94848",r:1,c:this.cols-2},this),
        ];
    }

    //判断(sx,sy)到(tx,ty)是否连通
    check_connectivity(g,sx,sy,tx,ty){
        if(sx==tx&&sy==ty)return true;
        g[sx][sy]=true;

        let dx=[-1,0,1,0],dy=[0,1,0,-1];
        for(let i=0;i<4;i++){
            let x=sx+dx[i],y=sy+dy[i];
            if(!g[x][y]&& this.check_connectivity(g,x,y,tx,ty)){
                return true;
            }
        }
        return false;
    }

    create_walls(){
        const g=[];
        for(let r=0;r<this.rows;r++){
            g[r]=[];
            for(let c=0;c<this.cols;c++){
                g[r][c]=false;
            }
        }

        //给四周加上障碍
        for(let r=0;r<this.rows;r++){
            g[r][0]=g[r][this.cols-1]=true;
        }
        for(let c=0;c<this.cols;c++){
            g[0][c]=g[this.rows-1][c]=true;
        }

        //随机生成墙
        for(let i=0;i<this.inner_walls_count/2;i++){
            //随机生成1000次
            for(let j=0;j<1000;j++){
                let r=parseInt(Math.random()*this.rows);
                let c=parseInt(Math.random()*this.cols);
                if(g[r][c]||g[this.rows-1-r][this.cols-1-c])continue;  //中心对称
                if(r == this.rows-2 && c==1 || r==1&&c==this.cols-2)continue;  //不会在左下和右上角建墙
                g[r][c]=g[this.rows-1-r][this.cols-1-c]=true;
                break;
            }
        }

        //检查起点到终点是否连通
        const copy_g=JSON.parse(JSON.stringify(g));
        if(!this.check_connectivity(copy_g,this.rows-2,1,1,this.cols-2)){
            return false;
        }

        for(let r=0;r<this.rows;r++){
            for(let c=0;c<this.cols;c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
        }

        return true;
    }

    add_listening_events(){  //监听按键，设置蛇的方向
        this.ctx.canvas.focus();  //聚焦到画布
        const[snake0,snake1]=this.snakes;
        this.ctx.canvas.addEventListener("keydown",e =>{
            if(e.key==='w')snake0.set_direction(0);
            else if(e.key==='d')snake0.set_direction(1);
            else if(e.key==='s')snake0.set_direction(2);
            else if(e.key==='a')snake0.set_direction(3);
            else if(e.key==='ArrowUp')snake1.set_direction(0);
            else if(e.key==='ArrowRight')snake1.set_direction(1);
            else if(e.key==='ArrowDown')snake1.set_direction(2);
            else if(e.key==='ArrowLeft')snake1.set_direction(3);
        });
    }

    start() {
        for(let i=0;i<100;i++){
            if(this.create_walls())
                break;
        }
        this.add_listening_events();
    }

    update_size(){
        this.L=parseInt(Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows));
        this.ctx.canvas.height=this.L*this.rows;
        this.ctx.canvas.width=this.L*this.cols;
    }

    check_ready(){  //判断两条蛇是否可移动
        for(const snake of this.snakes){
            if(snake.status!=="idle")return false; //未到静止状态
            if(snake.direction===-1)return false;  //未接受到方向
        }
        return true;
    }

    check_vaild(cell){  //检测目标位置是否合法,包括不撞墙和两条蛇的身体
        for(const wall of this.walls){
            if(cell.r===wall.r&&cell.c===wall.c){
                return false;
            }
        }
        for(const snake of this.snakes){
            let k=snake.cells.length;
            if(!snake.check_tail_increasing()){ //若蛇的尾巴会移动，则不判断蛇尾
                k--;
            }
            for(let i=0;i<k;i++) {
                if (cell.r === snake.cells[i].r && cell.c === snake.cells[i].c) {
                    return false;
                }
            }
        }
        return true;
    }

    next_step(){  //让两条蛇进入到下一回合
        for(const snake of this.snakes){
            snake.next_step();
        }
    }
    update() {
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#A2d149";
        for (let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;  //设置画布颜色
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(this.L*c,this.L*r,this.L,this.L);  //画一个矩形(坐标,边长)
            }
        }
    }

}