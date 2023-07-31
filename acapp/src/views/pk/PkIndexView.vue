<template>
  <ContentField>
    <PlayGround v-if="$store.state.pk.status=== 'playing'" ></PlayGround>
    <MatchGround v-if="$store.state.pk.status=== 'matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser!=='none'"></ResultBoard>
    <UserInfoBoard v-if="$store.state.pk.status=== 'playing'"></UserInfoBoard>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import UserInfoBoard from '../../components/UserInfoBoard.vue'
import {onMounted,onUnmounted} from 'vue'
import {useStore} from 'vuex'

export default {
  components:{
    PlayGround,
    MatchGround,
    ResultBoard,
    UserInfoBoard,
    ContentField
  },
  setup(){
    const store=useStore();
    const socketUrl=`wss://app5801.acapp.acwing.com.cn/websocket/${store.state.user.token}/`;

    store.commit("updateIsRecord",false);

    let socket=null;
    onMounted(()=>{
      store.dispatch("getInfo",{
        success(){
        }
      })

      store.commit("updateOpponent",{
        username:'旗鼓相当的对手',
        photo:'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
        rating:'',
      })

      socket=new WebSocket(socketUrl);

      socket.onopen=()=>{
        store.commit("updateSocket",socket);
      }

      socket.onmessage =msg=>{
        const data=JSON.parse(msg.data);
        if(data.event==="success-match"){
          store.commit("updateOpponent",{
            username:data.opponent_username,
            photo:data.opponent_photo,
            rating:data.opponent_rating,
          });
          setTimeout(()=>{
            store.commit("updateStatus",'playing')
          },200);
            store.commit("updateGameMap",data.game);
        }else if(data.event==="move"){
          const game=store.state.pk.gameObject;
          const [snake0,snake1]=game.snakes;
          snake0.set_direction(data.a_direction);
          snake1.set_direction(data.b_direction);
        }else if(data.event==="result"){
          const game=store.state.pk.gameObject;
          const [snake0,snake1]=game.snakes;
          if(data.loser==='all' || data.loser==='A'){
            snake0.status='die';
          }
          if(data.loser==='all' || data.loser==='B'){
            snake1.status='die';
          }
          store.commit("updateLoser",data.loser);
        }
      }

    });

    onUnmounted(()=>{
      socket.close();
      store.commit("updateStatus",'matching');
    })

  }
}
</script>

<style scoped>

</style>
