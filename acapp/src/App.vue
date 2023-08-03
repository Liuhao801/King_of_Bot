<template>
<!--  <div class="window">-->
    <div class="game-body">
      <!-- 路由 -->
      <MenuView v-if="$store.state.router.router_name==='menu'"></MenuView>
      <PkIndexView v-else-if="$store.state.router.router_name==='pk'"></PkIndexView>
      <RecordIndexView v-else-if="$store.state.router.router_name==='record'"></RecordIndexView>
      <RecordContentView v-else-if="$store.state.router.router_name==='record_content'"></RecordContentView>
      <RanklistIndexView v-else-if="$store.state.router.router_name==='ranklist'"></RanklistIndexView>
      <UserBotsIndexView v-else-if="$store.state.router.router_name==='user_bot'"></UserBotsIndexView>
    </div>
<!--  </div>-->
</template>

<script>
import {useStore} from "vuex";
import MenuView from './views/MenuView';
import PkIndexView from "./views/pk/PkIndexView";
import RecordIndexView from "./views/record/RecordIndexView";
import RecordContentView from "./views/record/RecordContentView";
import RanklistIndexView from "./views/ranklist/RanklistIndexView";
import UserBotsIndexView from "./views/user/bots/UserBotsIndexView";
import $ from 'jquery'

export default {
  components:{
    MenuView,
    PkIndexView,
    RecordIndexView,
    RecordContentView,
    RanklistIndexView,
    UserBotsIndexView,
  },
  setup(){
    const store=useStore();

    $.ajax({
      url:"https://app5801.acapp.acwing.com.cn/api/user/account/acwing/acapp/apply_code/",
      type:'get',
      success(resp){
        if(resp.result==='success'){
          store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp=>{
            if(resp.result==='success'){
              const jwt_token=resp.jwt_token;
              store.commit("updateToken",jwt_token);
              store.dispatch("getInfo",{
                success(){
                  store.commit("updatePullingInfo",false);
                },
                error(){
                  store.commit("updatePullingInfo",false);
                }
              })
            }else{
              store.state.user.AcWingOS.window.close();
            }
          });
        }else{
          store.state.user.AcWingOS.window.close();
        }
      }
    })

  }

}
</script>


<style scoped>
body{
  margin:0;
}
div.game-body{
  background-image: url("./assets/images/background.jpg");
  background-size: cover;
  width: 100%;
  height: 100%;
}
div.window{
  width: 100vw;
  height: 100vh;
}
</style>
