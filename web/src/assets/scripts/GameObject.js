const GAME_OBJECT=[];

export class GameObject{
    constructor(){
        GAME_OBJECT.push(this);
        this.timedalta=0;  //距离上一帧时间间隔
        this.has_called_start=false;
    }

    start(){  //只执行一次

    }

    update(){  // 每一帧执行一次,除了第一帧之外

    }

    on_destory(){  //删除之前执行

    }
    destory(){
        this.on_destory();

        for(let i in GAME_OBJECT){
            const obj =GAME_OBJECT[i];
            if(obj===this){
                GAME_OBJECT.splice(i);
                break;
            }
        }

    }
}

let last_timestamp;  //上一次执行的时间
const step = timestamp =>{
    for(let obj of GAME_OBJECT){
        if(!obj.has_called_start){
            obj.has_called_start=true;
            obj.start();
        }else{
            obj.timedalta=timestamp-last_timestamp;
            obj.update();
        }
    }
    last_timestamp=timestamp;
    requestAnimationFrame(step)
}

requestAnimationFrame(step)

