<template>
  <div class="content-field">
   <slot></slot>
    <div class="go_back" @click="click_go_back_handler">
      返回
    </div>
  </div>
</template>

<script>

import {useStore} from "vuex";

export default {
  setup(){
    const store=useStore();
    const click_go_back_handler=()=>{
      store.commit("updateRouterName","menu");
    }

    return{
      click_go_back_handler,
    }
  }
}
</script>

<style>
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