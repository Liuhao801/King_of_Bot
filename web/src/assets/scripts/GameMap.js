import {GameObject} from "@/assets/scripts/GameObject";
import {Wall} from "@/assets/scripts/Wall";
import {Snake} from "@/assets/scripts/Snake";

export class GameMap extends GameObject{
    constructor(ctx,parent,store) {
        super();
        this.ctx=ctx;  //画布
        this.parent=parent  //画布父元素，用于动态修改画布长宽
        this.store=store
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


    create_walls(){
        const g=this.store.state.pk.game_map;

        for(let r=0;r<this.rows;r++){
            for(let c=0;c<this.cols;c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
        }
    }

    add_listening_events(){  //监听按键，设置蛇的方向
        //回放功能
        if(this.store.state.record.is_record){
            let k=0;
            const a_steps=this.store.state.record.a_steps;
            const b_steps=this.store.state.record.b_steps;
            const loser=this.store.state.record.record_loser;
            const [snake0,snake1]=this.snakes;
            const interval_id=setInterval(()=>{
                if(k>=a_steps.length-1){ //最后一步非法
                    if(loser==='all' || loser==='A'){
                        snake0.status='die';
                    }
                    if(loser==='all' || loser==='B'){
                        snake1.status='die';
                    }
                    clearInterval(interval_id);
                }else{
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));

                }
                k++;
            },300);//每300毫秒执行一次
        }else{
            this.ctx.canvas.focus();  //聚焦到画布

            this.ctx.canvas.addEventListener("keydown",e =>{
                let d=-1;
                if(e.key==='w')d=0;
                else if(e.key==='d')d=1;
                else if(e.key==='s')d=2;
                else if(e.key==='a')d=3;

                if(d>=0){
                    this.store.state.pk.socket.send(JSON.stringify({
                        event:'move',
                        direction:d,
                    }));
                }
            });
        }
    }

    start() {
        this.create_walls()
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