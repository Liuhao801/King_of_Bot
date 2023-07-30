<template>
  <PlayGround></PlayGround>
  <button @click="jumpToRecordIndex" type="button" class="btn btn-primary btn-lg">返回对战列表</button>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import {useStore} from "vuex";

export default {
  components:{
    PlayGround,
  },
  setup(){
    const store=useStore();
    const jumpToRecordIndex=()=>{
      store.commit("updateRouterName",'record');
    }
    return{
      jumpToRecordIndex,
    }
  }
}
</script>

<style scoped>
button.btn-primary{
  float: right;
  margin-right: 18vw;
}
</style>
