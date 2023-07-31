<template>
  <div class="user-board">
      <div class="user_photo">
        <img :src="$store.state.user.photo" alt="'">
      </div>

      <div class="user_name">
        {{$store.state.user.username}}
      </div>

      <div class="user-board-text" style="color: #4876EC" v-if="$store.state.pk.a_id===parseInt($store.state.user.id)">
        您的蛇的颜色是:<br>蓝色
      </div>
      <div class="user-board-text" style="color: #F94848" v-else-if="$store.state.pk.b_id===parseInt($store.state.user.id)">
        您的蛇的颜色是:<br>红色
      </div>
  </div>
</template>

<script>

</script>

<style scoped>
div.user-board{
  height: 30vh;
  width: 10vw;
  position: absolute;
  left: 5vh;
  top:15vh;
}
div.user-board-text{
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  font-style: italic;
  padding-top: 2vh;
}
div.user_photo{
  text-align: center;
}
div.user_photo>img{
  border-radius: 50%;
  width:10vh;
}
div.user_name{
  text-align: center;
  font-size: 25px;
  font-weight: 600;
  color:white;
  padding-top: 1vh;
}
</style>
