import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
import ModulRecord from './record'
import ModulRouter from './router'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user:ModuleUser,
    pk:ModulePk,
    record:ModulRecord,
    router:ModulRouter,
  }
})
