import {GameObject} from "@/assets/scripts/GameObject";

export class Wall extends GameObject{
    constructor(r,c,gamemap) {  //横纵坐标，画布
        super();

        this.r=r;
        this.c=c;
        this.gamemap=gamemap;
        this.color="#B37226";
    }

    update(){
        this.render();
    }

    render(){
        const L=this.gamemap.L;
        const ctx=this.gamemap.ctx;

        ctx.fillStyle=this.color;
        ctx.fillRect(this.c*L,this.r*L,L,L);
    }
}