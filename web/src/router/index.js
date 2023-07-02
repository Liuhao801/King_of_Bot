import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import UserBotIndexView from '../views/user/bots/UserBotsIndexView.vue'
import NotFound from '../views/error/NotFound.vue'
import UserAccountLoginView from "../views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView";
import store from '../store/index'

const routes = [
  {
    path: '/',
    name: "root",
    redirect:'/pk/',
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RanklistIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/user/account/login",
    name:"user_account_login",
    component:UserAccountLoginView,
    meta:{
      requestAuth:false,
    }
  },
  {
    path:"/user/account/register",
    name:"user_account_register",
    component:UserAccountRegisterView,
    meta:{
      requestAuth:false,
    }
  },
  {
    path:"/user/bots/",
    name:"user_bots_index",
    component:UserBotIndexView,
    meta:{
      requestAuth:true,
    }
  },
  {
    path:"/404/",
    name:"4040",
    component:NotFound,
    meta:{
      requestAuth:false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to,from,next)=>{
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name:"user_account_login"});
  }else{
    next();
  }

})
export default router
