<template>
  <div class="playground">
    <GameMap></GameMap>
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
    onUnmounted(()=>{
      store.commit("updateLoser","none");
    })
  }
}
</script>

<style scoped>
div.playground{
  width: 80vw;
  height: 80vh;
  margin: 20px auto 0px;
}
</style>