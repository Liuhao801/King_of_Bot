<template>
  <div class="playground">
    <GameMap></GameMap>
    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
      <button type="button" class="btn btn-primary btn-lg" @click="refresh">
        随机切换地图
      </button>
    </div>
  </div>
</template>

<script>
import GameMap from './GameMap.vue'

export default {
  inject:['reload'],
  components:{
    GameMap,
  },
  methods:{
    refresh(){
      this.reload()
    }
  }
}
</script>

<style scoped>
div.playground{
  width: 80vw;
  height: 80vh;
  margin: 20px auto;
}
</style>