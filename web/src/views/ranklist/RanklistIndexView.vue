<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
      <tr>
        <th>玩家</th>
        <th>天梯积分</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>
          <img :src="user.photo" alt="" class="user-photo">
          &nbsp;&nbsp;
          <span class="user-name">{{user.username}}</span>
        </td>
        <td class="rank_rating">
            <span v-if="is_first_page() && user.id===users[0].id" style="color: #FAC32A">{{user.rating}}</span>
            <span v-else-if="is_first_page() && user.id===users[1].id" style="color:#798899">{{user.rating}}</span>
            <span v-else-if="is_first_page() && user.id===users[2].id" style="color: #ba6e40">{{user.rating}}</span>
            <span v-else style="font-weight: 600">{{user.rating}}</span>
        </td>
      </tr>
      </tbody>
    </table>

    <nav aria-label="Page navigation example">
      <ul class="pagination" style="float: right">
        <li class="page-item" @click="click_page(-2)">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li :class="'page-item '+page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
          <a class="page-link" href="#">{{page.number}}</a>
        </li>
        <li class="page-item" @click="click_page(-1)">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>

  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import {useStore} from "vuex"
import {ref} from "vue";
import $ from 'jquery'

export default {
  components:{
    ContentField,
  },
  setup(){
    const store=useStore();
    let current_page=1;
    let total_user=0;
    let users=ref([]);
    let pages=ref([]);

    const update_pages=()=>{
      let max_pages=parseInt(Math.ceil((total_user/5)));
      let new_pages=[];
      for(let i=current_page-2;i<=current_page+2;i++){
        if(i>=1&&i<=max_pages){
          new_pages.push({
            number:i,
            is_active:i===current_page?"active":"",
          })
        }
      }
      pages.value=new_pages;
    }

    const click_page=(page)=>{
      if(page===-2)page=current_page-1;
      else if(page===-1)page=current_page+1;
      let max_pages=parseInt(Math.ceil((total_user/5)));
      if(page>=1&&page<=max_pages){
        pull_page(page);
      }
    }

    const pull_page=(page)=>{
      current_page=page;
      $.ajax({
        url:'https://app5804.acapp.acwing.com.cn/api/ranklist/getlist/',
        data:{
          page:page,
        },
        type:'get',
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          users.value=resp.users;
          total_user=resp.users_count;
          update_pages();
        },
      })
    }

    const is_first_page= ()=>{
      return current_page===1;
    }

    pull_page(current_page);

    return{
      users,
      pages,
      click_page,
      is_first_page,
    }
  }
}
</script>

<style scoped>
img.user-photo{
  width: 8vh;
  border-radius: 50%;
}
.rank_rating{
  font-weight: 600;
}
</style>
