<template>
  <div class="user-board">

    <div class="user_photo">
      <img :src="$store.state.user.photo" alt="'">
    </div>

    <div class="user_name">
      {{$store.state.user.username}}
    </div>

    <div class="user-board-text" style="color: #4876EC" v-if="$store.state.pk.a_id===parseInt($store.state.user.id)">
      您的蛇的颜色是：蓝色
    </div>
    <div class="user-board-text" style="color: #F94848" v-else-if="$store.state.pk.b_id===parseInt($store.state.user.id)">
      您的蛇的颜色是：红色
    </div>

  </div>
</template>

<script>

</script>

<style scoped>
div.user-board{
  height: 40vh;
  width: 10vw;
  position: absolute;
  top:20vh;
  left: 2vw;
}
div.user-board-text{
  text-align: center;
  font-size: 3vmin;
  font-weight: 600;
  font-style: italic;
  padding-top: 1vh;
}
div.user_photo{
  text-align: center;
}
div.user_photo>img{
  border-radius: 50%;
  width:10vw;
}
div.user_name{
  text-align: center;
  font-size: 4vmin;
  font-weight: 600;
  color:white;
  padding-top: 1vh;
}
</style>
