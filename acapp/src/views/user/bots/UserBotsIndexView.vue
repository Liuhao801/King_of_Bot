<template>
  <content-field>
    <div class="game-table">
      <div>
          <span style="font-size: 200%;font-weight: 600;color: white">我的Bot</span>
          <div class="game-btn">
            <button style="background-color: #0D6EFD;float: right" type="button" @click="show_add_modal_handler(true)">
              创建Bot
            </button>
          </div>


          <!-- 创建Modal -->
          <div class="game-modal" id="add-bot-btn" tabindex="-1" v-if="show_add_modal">

            <h3 style="margin: 2px">创建Bot</h3>

            <div>
              <form>
                <div>
                  <label for="add-bot-title">名称</label>
                  <input style="width: 85%" v-model="botadd.title" type="text" id="add-bot-title" placeholder="请输入Bot名称">
                </div>

                <div>
                  <label for="add-bot-description" >简介</label>
                  <textarea style="width: 85%;margin-top: 1vh" v-model="botadd.description" id="add-bot-description" rows="2" placeholder="请输入Bot简介"></textarea>
                </div>

                <div>
                  <label >代码</label>
                  <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="java"
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

            <div class="game-btn" style="margin-top: 1vh;float: right">
              <button type="button"  style="background-color: #6C757D;margin-right: 10px" @click="show_add_modal_handler(false)">取消</button>
              <button type="button"  style="background-color: #0D6EFD" @click="add_bot">创建</button>
            </div>

          </div>

        <table style="text-align: center">
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
              <td style="font-weight: 600">{{bot.title}}</td>
              <td>{{bot.createTime}}</td>
              <td>{{bot.modifyTime}}</td>

              <td class="game-btn">
                <button type="button"  style="margin-right: 10px;background-color: #198754" @click="show_update_modal_handler(bot.id,true)">修改</button>
                <button type="button"  style="background-color: #DC3545" @click="show_remove_modal_handler(bot.id,true)">删除</button>

                <!-- 修改Modal -->
                <div class="game-modal" :id="'update-bot-'+bot.id" tabindex="-1" v-if="bot.show_update_modal">

                  <h3 style="margin: 2px">创建Bot</h3>

                  <form>
                    <div>
                      <label for="update-bot-title" >名称</label>
                      <input style="width: 85%" v-model="bot.title" type="text"  id="update-bot-title" placeholder="请输入Bot名称">
                    </div>
                    <div>
                      <label for="update-bot-description" >简介</label>
                      <textarea style="width: 85%;margin-top: 1vh" v-model="bot.description" id="update-bot-description" rows="2" placeholder="请输入Bot简介"></textarea>
                    </div>
                    <div>
                      <label>代码</label>
                      <VAceEditor v-model:value="bot.content" @init="editorInit" lang="java"
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

                  <div class="game-btn" style="margin-top: 1vh;float: right">
                    <button type="button"  style="background-color: #6C757D;margin-right: 10px" @click="refresh_bots()">取消</button>
                    <button type="button"  style="background-color: #0D6EFD" @click="update_bot(bot)">确认</button>
                  </div>

                </div>

                <!-- 删除Modal -->
                <div class="game-remove-modal" :id="'remove-bot-'+bot.id" tabindex="-1" v-if="bot.show_remove_modal">
                  <h3 style="margin: 2px">删除Bot</h3>

                  <div style="margin-top: 1vh">
                    是否删除{{bot.title}}
                  </div>
                  <div class="game-btn" style="margin-top: 1vh;float: right">
                    <button type="button"  style="background-color: #6C757D;margin-right: 10px" @click="refresh_bots()">取消</button>
                    <button type="button"  style="background-color: #0D6EFD" @click="remove_bot(bot)">确定</button>
                  </div>
                </div>

              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

  </content-field>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import {ref,reactive} from "vue";
import $ from 'jquery'
import {useStore} from 'vuex'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default {
  components:{
    VAceEditor,
    ContentField,
  },
  setup(){
    ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@" +
        require("ace-builds").version +
        "/src-noconflict/")

    const store=useStore();
    let bots=ref([]);
    let show_add_modal=ref(false);

    const botadd=reactive({
      title:'',
      description:'',
      content:'package com.kob.botrunningsystem.utils;\n' +
          '\n' +
          'import java.io.File;\n' +
          'import java.io.FileNotFoundException;\n' +
          'import java.util.Scanner;\n' +
          '\n' +
          'public class Bot implements java.util.function.Supplier<Integer>{\n' +
          '\n' +
          '    //请实现以下函数\n' +
          '    public Integer nextMove(String input) {\n' +
          '        return 0;\n' +
          '    }\n' +
          '\n' +
          '    @Override\n' +
          '    public Integer get() {\n' +
          '        File file=new File("input.txt");\n' +
          '        try {\n' +
          '            Scanner sc=new Scanner(file);\n' +
          '            return nextMove(sc.next());\n' +
          '        }catch (FileNotFoundException e){\n' +
          '            throw new RuntimeException(e);\n' +
          '        }\n' +
          '    }\n' +
          '}\n',
      error_message:'',
    });

    const refresh_bots =()=>{
      $.ajax({
        url:'https://app5804.acapp.acwing.com.cn/api/user/bot/getlist/',
        type:'get',
        headers:{
          Authorization: "Bearer "+store.state.user.token,
        },
        success(resp){
          for(const bot of resp){
            bot.show_update_modal=false;
            bot.show_remove_modal=false;
          }
          bots.value=resp;
        }
      })
    }

    const add_bot =()=>{
      botadd.error_message='';
      $.ajax({
        url:'https://app5804.acapp.acwing.com.cn/api/user/bot/add/',
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
            botadd.content='package com.kob.botrunningsystem.utils;\n' +
                '\n' +
                'import java.io.File;\n' +
                'import java.io.FileNotFoundException;\n' +
                'import java.util.Scanner;\n' +
                '\n' +
                'public class Bot implements java.util.function.Supplier<Integer>{\n' +
                '\n' +
                '    //请实现以下函数\n' +
                '    public Integer nextMove(String input) {\n' +
                '        return 0;\n' +
                '    }\n' +
                '\n' +
                '    @Override\n' +
                '    public Integer get() {\n' +
                '        File file=new File("input.txt");\n' +
                '        try {\n' +
                '            Scanner sc=new Scanner(file);\n' +
                '            return nextMove(sc.next());\n' +
                '        }catch (FileNotFoundException e){\n' +
                '            throw new RuntimeException(e);\n' +
                '        }\n' +
                '    }\n' +
                '}\n';
            show_add_modal.value=false;
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
        url:'https://app5804.acapp.acwing.com.cn/api/user/bot/update/',
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
            refresh_bots();
          }else{
            bot.error_message=resp.error_message;
          }
        }
      })
    }

    const remove_bot = (bot)=>{
      $.ajax({
        url: 'https://app5804.acapp.acwing.com.cn/api/user/bot/remove/',
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

    const show_add_modal_handler=(is_show)=>{
      show_add_modal.value=is_show;
    }

    const show_update_modal_handler=(bot_id,is_show)=>{
      const new_bots=[];
      for(const bot of bots.value){
        if(bot.id===bot_id){
          bot.show_update_modal=is_show;
        }
        new_bots.push(bot);
      }
      bots.value=new_bots;
    }

    const show_remove_modal_handler=(bot_id,is_show)=>{
      const new_bots=[];
      for(const bot of bots.value){
        if(bot.id===bot_id){
          bot.show_remove_modal=is_show;
        }
        new_bots.push(bot);
      }
      bots.value=new_bots;
    }

    refresh_bots();

    return{
      bots,
      botadd,
      add_bot,
      remove_bot,
      update_bot,
      refresh_bots,
      show_add_modal,
      show_add_modal_handler,
      show_update_modal_handler,
      show_remove_modal_handler,
    }
  }

}
</script>

<style scoped>
div.error_message{
  color:red;
}
div.game-table{
  display: flex;
  justify-content: center;
  padding-top: 5vh;
  width: 100%;
  height: calc(100% - 5vh);
}
div.game-table table{
  background-color: rgba(255,255,255,0.8);
  border-radius: 5px;
}
td{
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
  padding: 1vw;
}
th{
  text-align: center;
}
.game-btn>button{
  font-size: 20px;
  color: white;
  border-radius: 5px;
  padding: 6px 12px;
  border: none;
  cursor: pointer;
  user-select: none;
}
.game-btn>button:hover{
  transform: scale(1.2);
  transition: 200ms;
}
.game-modal{
  background-color: whitesmoke;
  padding: 10px;
  border-radius: 5px;
  position: absolute;
  width: 40vw;
  height: 70vh;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
  text-align: left;
}
.game-remove-modal{
  background-color: whitesmoke;
  padding: 10px;
  border-radius: 5px;
  position: absolute;
  width: 40vw;
  height: 20vh;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
  text-align: left;
}
</style>
