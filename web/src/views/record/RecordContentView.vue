<template>
  <PlayGround></PlayGround>
  <div class="record_btn">
    <button @click="jumpToRecordIndex" type="button" class="btn btn-primary btn-lg">返回</button>
  </div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import router from "@/router";

export default {
  components:{
    PlayGround,
  },
  setup(){
    const jumpToRecordIndex=()=>{
      router.push({
        name:"record_index",
      })
    }
    return{
      jumpToRecordIndex,
    }
  }
}
</script>

<style scoped>
div.record_btn{
  text-align:right;
  margin-right: 15vw;
  padding-top: 1vh;
}
div.record_btn>button{
  width: 15vw;
  font-size:3vmin;
}
</style>
