<template>
    <div class="container">
      <div class="row">

        <div class="col-2">
          <div class="card" style="margin-top: 20px">
            <div class="card-body">
              <img :src="$store.state.user.photo"  alt="" style="width: 100%">
            </div>
          </div>
        </div>

        <div class="col-10">
          <div class="card" style="margin-top: 20px">
            <div class="card-header">
              <span style="font-size: 120%">我的Bot</span>
              <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                创建Bot
              </button>
            </div>

            <!-- Modal -->
            <div class="modal fade" id="add-bot-btn" tabindex="-1" >
              <div class="modal-dialog modal-dialog-centered modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5">创建Bot</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">

                    <form>
                      <div class="mb-3">
                        <label for="add-bot-title" class="form-label">名称</label>
                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                      </div>
                      <div class="mb-3">
                        <label for="add-bot-description" class="form-label">简介</label>
                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="2" placeholder="请输入Bot简介"></textarea>
                      </div>
                      <div class="mb-3">
                        <label class="form-label">代码</label>
                        <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp"
                                    theme="textmate" style="height: 300px" :options="{
                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                    enableSnippets: true, // 启用代码段
                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                    fontSize: 18, //设置字号
                                                    tabSize: 4, // 标签大小
                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                    highlightActiveLine: true,
                                                }" />

                      </div>
                    </form>
                    <div class="error_message">{{ botadd.error_message }}</div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="card-body">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>名称</th>
                    <th>创建时间</th>
                    <th>上次修改时间</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="bot in bots" :key="bot.id">
                    <td>{{bot.title}}</td>
                    <td>{{bot.createTime}}</td>
                    <td>{{bot.modifyTime}}</td>
                    <td>
                      <button type="button" class="btn btn-success" style="margin-right: 10px" data-bs-toggle="modal" :data-bs-target="'#update-bot-'+bot.id">修改</button>
                      <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>

                      <div class="modal fade" :id="'update-bot-'+bot.id" tabindex="-1" >
                        <div class="modal-dialog modal-dialog-centered modal-xl">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h1 class="modal-title fs-5">创建Bot</h1>
                              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">

                              <form>
                                <div class="mb-3">
                                  <label for="update-bot-title" class="form-label">名称</label>
                                  <input v-model="bot.title" type="text" class="form-control" id="update-bot-title" placeholder="请输入Bot名称">
                                </div>
                                <div class="mb-3">
                                  <label for="update-bot-description" class="form-label">简介</label>
                                  <textarea v-model="bot.description" class="form-control" id="update-bot-description" rows="2" placeholder="请输入Bot简介"></textarea>
                                </div>
                                <div class="mb-3">
                                  <label class="form-label">代码</label>
                                  <VAceEditor v-model:value="bot.content" @init="editorInit" lang="c_cpp"
                                              theme="textmate" style="height: 300px" :options="{
                                                        enableBasicAutocompletion: true, //启用基本自动完成
                                                        enableSnippets: true, // 启用代码段
                                                        enableLiveAutocompletion: true, // 启用实时自动完成
                                                        fontSize: 18, //设置字号
                                                        tabSize: 4, // 标签大小
                                                        showPrintMargin: false, //去除编辑器里的竖线
                                                        highlightActiveLine: true,
                                                    }" />

                                </div>
                              </form>
                              <div class="error_message">{{ bot.error_message }}</div>
                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                              <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存并修改</button>
                            </div>
                          </div>
                        </div>
                      </div>

                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

          </div>
        </div>

      </div>
    </div>
</template>

<script>
import {ref,reactive} from "vue";
import $ from 'jquery'
import {useStore} from 'vuex'
import {Modal} from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default {
  components:{
    VAceEditor,
  },
  setup(){
    ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@" +
        require("ace-builds").version +
        "/src-noconflict/")

    const store=useStore();
    let bots=ref([]);
    const botadd=reactive({
      title:'',
      description:'',
      content:'',
      error_message:'',
    });

    const refresh_bots =()=>{
      $.ajax({
        url:'http://127.0.0.1:8088/user/bot/getlist/',
        type:'get',
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          bots.value=resp;
        }
      })
    }

    const add_bot =()=>{
      botadd.error_message='';
      $.ajax({
        url:'http://127.0.0.1:8088/user/bot/add/',
        type:'post',
        data:{
          title:botadd.title,
          description:botadd.description,
          content:botadd.content,
        },
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          if(resp.error_message==='success'){
            botadd.title='';
            botadd.description='';
            botadd.content='';
            Modal.getInstance("#add-bot-btn").hide();
            refresh_bots();
          }else{
            botadd.error_message=resp.error_message;
          }
        }
      })
    }

    const update_bot =(bot)=>{
      botadd.error_message='';
      $.ajax({
        url:'http://127.0.0.1:8088/user/bot/update/',
        type:'post',
        data:{
          bot_id:bot.id,
          title:bot.title,
          description:bot.description,
          content:bot.content,
        },
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          if(resp.error_message==='success'){
            Modal.getInstance('#update-bot-'+bot.id).hide();
            refresh_bots();
          }else{
            bot.error_message=resp.error_message;
          }
        }
      })
    }

    const remove_bot = (bot)=>{
      $.ajax({
        url: 'http://127.0.0.1:8088/user/bot/remove/',
        type: 'post',
        data: {
          bot_id: bot.id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          if(resp.error_message==='success'){
            refresh_bots();
          }
        }
      })
    }

    refresh_bots();

    return{
      bots,
      botadd,
      add_bot,
      remove_bot,
      update_bot,
    }
  }

}
</script>

<style scoped>
div.error_message{
  color:red;
}
</style>
