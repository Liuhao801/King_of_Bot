<template>
  <NavBar></NavBar>
  <router-view v-if="isRouterAlive"></router-view>
</template>

<script>
import NavBar from './components/NavBar.vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap'

export default {
  components:{
    NavBar
  },
  provide(){
    return {
      reload:this.reload
    }
  },
  data(){
    return {
      isRouterAlive:true
    }
  },
  methods:{
    reload(){
      this.isRouterAlive=false;
      this.$nextTick(function (){
        this.isRouterAlive=true
      })
    }
  }

}
</script>


<style>
body{
  background-image: url("./assets/images/background.png");
  background-size: cover;
}
</style>
