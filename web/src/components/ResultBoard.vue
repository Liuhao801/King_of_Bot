<template>
  <div class="result-board">
    <div class="result-board-text" style="color: white" v-if="$store.state.pk.loser==='all'">
      平局
    </div>
    <div class="result-board-text " style="color: red" v-else-if="$store.state.pk.loser==='A' && $store.state.pk.a_id===parseInt($store.state.user.id)">
      负
    </div>
    <div class="result-board-text" style="color: red" v-else-if="$store.state.pk.loser==='B' && $store.state.pk.b_id===parseInt($store.state.user.id)">
      负
    </div>
    <div class="result-board-text" style="color: yellow" v-else>
      胜
    </div>

    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-primary btn-lg">
        再来一局
      </button>
    </div>
  </div>
</template>

<script>
import {useStore} from 'vuex';

export default {
  setup(){
    const store=useStore();

    const restart=()=>{
      store.commit("updateStatus","matching");
      store.commit("updateLoser","none");
      store.commit("updateOpponent",{
        username:'旗鼓相当的对手',
        photo:'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
      })
    }

    return {
      restart,
    }
  }
}
</script>

<style scoped>
div.result-board{
  height: 40vh;
  width: 28vw;
  background-color: rgba(50,50,50,0.5);
  position: absolute;
  top:30vh;
  left: 36vw;
}
div.result-board-text{
  text-align: center;
  font-size: 80px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}
div.result-board-btn{
  padding-top:7vh;
  text-align: center;
}
</style>
