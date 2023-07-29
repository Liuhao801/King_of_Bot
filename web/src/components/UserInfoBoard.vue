<template>
  <div class="user-board">
    <div class="row">

      <div class="col-4">
        <div class="user_photo">
          <img :src="$store.state.user.photo" alt="'">
        </div>
      </div>

      <div class="col-4">
        <div class="user_name">
          {{$store.state.user.username}}
        </div>
      </div>

      <div class="col-12">
        <div class="user-board-text" style="color: #4876EC" v-if="$store.state.pk.a_id===parseInt($store.state.user.id)">
          您的蛇的颜色是：蓝色
        </div>
        <div class="user-board-text" style="color: #F94848" v-else-if="$store.state.pk.b_id===parseInt($store.state.user.id)">
          您的蛇的颜色是：红色
        </div>
      </div>

    </div>
  </div>
</template>

<script>

</script>

<style scoped>
div.user-board{
  height: 40vh;
  width: 28vw;
  position: absolute;
  top:30vh;
  left: 3vw;
}
div.user-board-text{
  text-align: left;
  font-size: 25px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}
div.user_photo{
  text-align: center;
  padding-top: 2vh;
}
div.user_photo>img{
  border-radius: 50%;
  width:16vh;
}
div.user_name{
  text-align: center;
  font-size: 50px;
  font-weight: 600;
  color:white;
  padding-top: 5vh;
}
</style>
