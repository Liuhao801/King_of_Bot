<template>
  <ContentField v-if="!$store.state.user.pulling_info">
      <div class="row justify-content-md-center">
        <div class="col-3">
          <form @submit.prevent="login">
            <div class="mb-3">
              <label for="username" class="form-label">用户名</label>
              <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
            </div>
            <div class="mb-3">
              <label for="pwd" class="form-label">密码</label>
              <input v-model="pwd" type="password" class="form-control" id="pwd" placeholder="请输入密码">
            </div>
            <div class="error_message">{{error_message}}</div>
            <button type="submit" class="btn btn-primary">提交</button>
          </form>
          <div style="text-align: center;margin-top: 5vh;cursor: pointer" @click="acwing_login">
            <img width="40" src="https://cdn.acwing.com/media/article/image/2022/09/06/1_32f001fd2d-acwing_logo.png">
            AcWing一键登录
          </div>
        </div>
      </div>
  </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import {useStore} from 'vuex'
import {ref} from "vue";
import router from '../../../router/index'
import $ from 'jquery'

export default {
  components:{
    ContentField,
  },
  setup(){
    const store=useStore();
    let username=ref('');
    let pwd=ref('');
    let error_message=ref('');

    const jwt_token=localStorage.getItem("jwt_token");
    if(jwt_token){
      store.commit("updateToken",jwt_token);
      store.dispatch("getInfo",{
        success(){
          router.push({name:"root"});
          store.commit("updatePullingInfo",false);
        },
        error(){
          store.commit("updatePullingInfo",false);
        }
      })
    }else{
      store.commit("updatePullingInfo",false);
    }

    const login=()=>{
      error_message.value='';
      store.dispatch("login",{
        username:username.value,
        pwd:pwd.value,
        success(){
          store.dispatch("getInfo",{
            success(){
              router.push({name:'root'});
            }
          })
        },
        error(){
          error_message.value="用户名或密码错误";
        }
      })
    }

    const acwing_login=()=>{
      $.ajax({
        url:"https://app5801.acapp.acwing.com.cn/api/user/account/acwing/web/apply_code/",
        type:'get',
        success:resp=>{
          if(resp.result==='success'){
            window.location.replace(resp.apply_code_url)
          }
        }
      })
    }
    return{
      username,
      pwd,
      error_message,
      login,
      acwing_login,
    }
  }
}
</script>

<style scoped>
button{
  width: 100%;
}
div.error_message{
  color: red;
}
</style>
