<template>
  <div class="menu-field">
    <div class="menu">
      <div @click="click_pk_handler" class="menu-item">对战</div>
      <div @click="click_record_handler" class="menu-item">对局列表</div>
      <div @click="click_ranklist_handler" class="menu-item">排行榜</div>
      <div @click="click_user_bot_handler" class="menu-item">我的Bot</div>
    </div>
  </div>
</template>

<script>
import {useStore} from 'vuex';

export default {
  setup(){
    const store=useStore();

    const click_pk_handler=()=>{
      store.commit("updateRouterName","pk");
    }
    const click_record_handler=()=>{
      store.commit("updateRouterName","record");
    }
    const click_ranklist_handler=()=>{
      store.commit("updateRouterName","ranklist");
    }
    const click_user_bot_handler=()=>{
      store.commit("updateRouterName","user_bot");
    }

    return{
      click_pk_handler,
      click_record_handler,
      click_ranklist_handler,
      click_user_bot_handler,
    }
  }
}
</script>

<style scoped>
div.menu-field{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
div.menu{
  width: 25vw;
  height: 30vh;
  background-color: rgba(50,50,50,0.5);
}
div.menu-item{
  height: 7.5vh;
  width: 100%;
  font-size: 25px;
  line-height: 7.5vh;
  color: white;
  text-align: center;
  font-style: italic;
  font-weight: 600;
  cursor: pointer;
  user-select: none;
}
div.menu-item:hover{
  transform: scale(1.2);
  transition: 200ms;
  color: yellow;
}
</style>