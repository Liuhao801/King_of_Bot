<template>
  <ContentField>
    <table class="table table-striped table-hover" style="text-align: center">
      <thead>
      <tr>
        <th>玩家A</th>
        <th>胜负</th>
        <th>玩家B</th>
        <th>胜负</th>
        <th>对局时间</th>
        <th>查看录像</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="record in records" :key="record.record.id">
        <td>
          <img :src="record.a_photo" alt="" class="record-user-photo">
          &nbsp;&nbsp;
          <span class="record-user-name">{{record.a_username}}</span>
        </td>
        <td>
          <span style="color: red;font-weight: 600" v-if="record.record.loser ==='A'">负</span>
          <span style="color: green;font-weight: 600" v-else-if="record.record.loser ==='B'">胜</span>
          <span v-else style="font-weight: 600">平局</span>
        </td>
        <td>
          <img :src="record.b_photo" alt="" class="record-user-photo">
          &nbsp;&nbsp;
          <span class="record-user-name">{{record.b_username}}</span>
        </td>
        <td>
          <span style="color: red;font-weight: 600" v-if="record.record.loser ==='B'">负</span>
          <span style="color: green;font-weight: 600" v-else-if="record.record.loser ==='A'">胜</span>
          <span v-else style="font-weight: 600">平局</span>
        </td>
        <td>
          <span>{{record.record.createTime}}</span>
        </td>
        <td>
          <button @click="open_record_content(record.record)" type="button" class="btn btn-success" >查看</button>
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
import router from "../../router/index";

export default {
  components:{
    ContentField,
  },
  setup(){
    const store=useStore();
    let current_page=1;
    let records=ref([]);
    let total_record=0;
    let pages=ref([]);

    const update_pages=()=>{
      let max_pages=parseInt(Math.ceil((total_record/9)));
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
      let max_pages=parseInt(Math.ceil((total_record/9)));
      if(page>=1&&page<=max_pages){
        pull_page(page);
      }
    }

    const pull_page=(page)=>{
      current_page=page;
      $.ajax({
        url:'https://app5801.acapp.acwing.com.cn/api/record/getlist/',
        data:{
          page:page,
        },
        type:'get',
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          records.value=resp.records;
          total_record=resp.records_count;
          update_pages();
        },
      })
    }

    pull_page(current_page);

    const stringTo2D= map =>{
      let g=[];
      for(let i=0,k=0;i<12;i++){
        let line=[];
        for(let j=0;j<13;j++,k++){
          if(map[k]==='0')line.push(0);
          else line.push(1);
        }
        g.push(line);
      }
      return g;
    }

    const open_record_content =(record)=>{
      store.commit("updateIsRecord",true);
      store.commit("updateGameMap",{
        map: stringTo2D(record.map),
        a_id:record.aid,
        a_sx:record.asx,
        a_sy:record.asy,
        b_id:record.bid,
        b_sx:record.bsx,
        b_sy:record.bsy,
      });
      store.commit("updateSteps",{
        a_steps:record.asteps,
        b_steps:record.bsteps,
      });
      store.commit("updateRecordLoser",record.loser);

      router.push({
        name:"record_content",
        params:{
          recordId:record.id,
        }
      })
    }

    return{
      records,
      open_record_content,
      pages,
      click_page,
    }
  }
}
</script>

<style scoped>
img.record-user-photo{
  width: 5vh;
  border-radius: 50%;
}
</style>
