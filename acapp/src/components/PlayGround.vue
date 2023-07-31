<template>
  <div class="content-field">
    <div class="playground">
      <GameMap></GameMap>
    </div>
    <div class="go_back" @click="click_go_back_handler">
      返回
    </div>
  </div>
</template>

<script>
import GameMap from './GameMap.vue'
import {onUnmounted} from "vue";
import {useStore} from "vuex";

export default {
  components:{
    GameMap,
  },
  setup(){
    const store=useStore();
    const click_go_back_handler=()=>{
      store.commit("updateRouterName","record");
    }

    onUnmounted(()=>{
      store.commit("updateLoser","none");
    })

    return{
      click_go_back_handler,
    }
  }
}
</script>

<style scoped>
div.content-field{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
div.playground{
  width: 40vw;
  height: 50vh;
}
div.content-field{
  width: 100%;
  height: 100%;
}
div.go_back{
  position: absolute;
  right: 5vh;
  bottom:5vh;
  color: #F94848;
  font-size: 28px;
  font-weight: 600;
  font-style: italic;
  cursor: pointer;
  user-select: none;
}
div.go_back:hover{
  transform: scale(1.2);
  color: white;
  transition: 200ms;
}
</style>