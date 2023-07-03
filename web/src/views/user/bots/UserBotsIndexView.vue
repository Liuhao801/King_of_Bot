<template>
  <ContentField>我的Bots</ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import {useStore} from "vuex";
import $ from 'jquery'

export default {
  components:{
    ContentField,
  },
  setup(){
    const store=useStore();
    // $.ajax({
    //   url:"http://127.0.0.1:8088/user/bot/add/",
    //   type:"post",
    //   data:{
    //     title:'标题',
    //     description:'描述',
    //     content:'内容',
    //   },
    //   headers:{
    //     Authorization:"Bearer "+ store.state.user.token,
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // })

    // $.ajax({
    //   url:"http://127.0.0.1:8088/user/bot/remove/",
    //   type:"post",
    //   data:{
    //     bot_id:2
    //   },
    //   headers:{
    //     Authorization:"Bearer "+ store.state.user.token,
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // })

    // $.ajax({
    //   url:"http://127.0.0.1:8088/user/bot/update/",
    //   type:"post",
    //   data:{
    //     bot_id:4,
    //     title:'新标题',
    //     description:'新描述',
    //     content:'新代码'
    //   },
    //   headers:{
    //     Authorization:"Bearer "+ store.state.user.token,
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // })

    $.ajax({
      url:"http://127.0.0.1:8088/user/bot/getlist/",
      type:"get",
      headers:{
        Authorization:"Bearer "+ store.state.user.token,
      },
      success(resp){
        console.log(resp);
      },
      error(resp){
        console.log(resp);
      }
    })
  }
}
</script>

<style scoped>

</style>
