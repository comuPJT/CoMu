import Vue from "vue";
import App from "./App.vue";
import Vuex from "vuex";
import store from "./store/index";
import VueChatScroll from 'vue-chat-scroll'
import router from "./router";
import * as firebase from 'firebase';
import VueMoment from 'vue-moment';
import VueSimpleAlert from "vue-simple-alert";
import VueYouTubeEmbed from 'vue-youtube-embed'

import NavBar from "@/views/common/NavBar";
Vue.config.productionTip = false

const config = {
  apiKey: "AIzaSyB4ZmoHupffIt49JZkQIO1kWjnXNdutmGU",
  authDomain: "comu-14e68.firebaseapp.com",
  databaseURL: "https://comu-14e68-default-rtdb.firebaseio.com",
  projectId: "comu-14e68",
  storageBucket: "comu-14e68.appspot.com",
};

firebase.initializeApp(config);
Vue.use(Vuex)
Vue.use(VueChatScroll)
Vue.use(VueMoment);
Vue.use(VueSimpleAlert, { reverseButtons: true });
Vue.use(VueYouTubeEmbed)
Vue.component('NavBar', NavBar)
Vue.component('store', store)
new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
