<template>
  <div class="matchground">
    <div class="row">

      <div class="col-4">
        <div class="user_photo">
          <img :src="$store.state.user.photo" alt="'">
        </div>
        <div class="user_name">
          {{$store.state.user.username}}
        </div>
        <div class="user_name">
          积分：{{$store.state.user.rating}}
        </div>
      </div>

      <div class="col-4">
        <div class="pk_photo">
          <img src="../assets/images/vs.png" alt="'">
        </div>
      </div>

      <div class="col-4">
        <div class="user_photo">
          <img :src="$store.state.pk.opponent_photo" alt="'">
        </div>
        <div class="user_name">
          {{$store.state.pk.opponent_username}}
        </div>
        <div class="user_name">
          积分：{{$store.state.pk.opponent_rating}}
        </div>
      </div>

      <div class="col-4">
        <div class="user-select-bot">
          <select v-model="select_bot" class="form-select" aria-label="Default select example">
            <option value="-1" selected>我亲自出马</option>
            <option v-for="bot in bots" :key="bot.id" :value="bot.id">
              {{bot.title}}
            </option>
          </select>
        </div>

      </div>
      <div class="col-12" style="text-align: center ;padding-top: 10vh">
        <button @click="click_match_btn" type="button" class="btn btn-primary btn-lg">{{ match_btn_info }}</button>
      </div>

    </div>
  </div>
</template>

<script>
import {ref} from "vue";
import {useStore} from 'vuex'
import $ from "jquery";

export default {
  setup(){
    let match_btn_info=ref('开始匹配');
    const store=useStore();
    let bots=ref([]);
    let select_bot=ref('-1');

    const click_match_btn=()=>{
      if(match_btn_info.value==='开始匹配') {
        match_btn_info.value = '取消';
        console.log(select_bot.value);
        store.state.pk.socket.send(JSON.stringify({
          event:"start-matching",
          bot_id:select_bot.value,
        }));
      }else{
        match_btn_info.value='开始匹配';
        store.state.pk.socket.send(JSON.stringify({
          event:'stop-matching',
        }));
      }
    }

    const refresh_bots =()=>{
      $.ajax({
        url:'http://127.0.0.1:8088/user/bot/getlist/',
        type:'get',
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          bots.value=resp;
        }
      })
    }

    refresh_bots();

    return{
      match_btn_info,
      click_match_btn,
      bots,
      select_bot,
    }
  }
}
</script>

<style scoped>
div.matchground{
  width: 80vw;
  height: 80vh;
  margin: 20px auto;
  background-color: rgba(50,50,50,0.5);
}
div.user_photo{
  text-align: center;
  padding-top: 10vh;
}
div.user_photo>img{
  border-radius: 50%;
  width:25vh;
}
div.pk_photo{
  text-align: center;
  padding-top: 8vh;
}
div.pk_photo>img{
  width:40vh;
}
div.user_name{
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color:white;
  padding-top: 1vh;
}
div.user-select-bot{
  padding-top: 5vh;
}
div.user-select-bot > select{
  width: 60%;
  margin:0 auto;
}
</style>