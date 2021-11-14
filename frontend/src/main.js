import Vue from 'vue'
import App from './App.vue'
import Vuex from 'vuex'
import store from "./store";

import router from "./router";

import NavBar from "@/views/common/NavBar";
Vue.config.productionTip = false
Vue.use(Vuex)
Vue.component('NavBar', NavBar)
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
