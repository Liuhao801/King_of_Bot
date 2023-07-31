<template>
  <div class="matchground-field">
    <div class="matchground">

      <div class="matchground-head">
        <div>
          <div class="user_photo">
            <img :src="$store.state.user.photo" alt="'">
          </div>
          <div class="user_name">
            {{$store.state.user.username}}
          </div>
          <div class="user_name">
            积分：{{$store.state.user.rating}}
          </div>
          <div class="user-select-bot">
            <select v-model="select_bot" class="form-select" aria-label="Default select example">
              <option value="-1" selected>我亲自出马</option>
              <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                {{bot.title}}
              </option>
            </select>
          </div>
        </div>


        <div class="pk">
          PK
        </div>

        <div>
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

      </div>

      <div class="start_match_btn">
        <button @click="click_match_btn" type="button">{{ match_btn_info }}</button>
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
        url:'https://app5801.acapp.acwing.com.cn/api/user/bot/getlist/',
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
div.matchground-field{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
div.matchground{
  width: 80%;
  height: 80%;
  background-color: rgba(50,50,50,0.5);
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
}
div.matchground-head{
  display: flex;
  justify-content: space-evenly;
}
div.user_photo{
  text-align: center;
}
div.user_photo>img{
  border-radius: 50%;
  width:16vh;
}
div.pk{
  padding-top: 5vh;
  text-align: center;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  color: red;
}
div.user_name{
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  color:white;
  padding-top: 1vh;
}
div.user-select-bot{
  padding-top: 2vh;
  text-align: center;
}
div.user-select-bot > select{
  margin:0 auto;
  width: 12vw;
  font-size: 15px;
  border-radius: 5px;
  height: 4.5vh;
  border: none;
}
div.start_match_btn{
  text-align: center;
}
div.start_match_btn>button{
  font-size: 20px;
  color: white;
  border-radius: 5px;
  background-color:#0D6EFD;
  padding: 8px 14px;
  border: none;
  cursor: pointer;
}
div.start_match_btn>button:hover{
  transform: scale(1.2);
  transition: 200ms;
}
</style>