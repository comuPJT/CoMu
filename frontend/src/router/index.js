import Vue from "vue";
import VueRouter from "vue-router";

import Main from "@/views/main/Main.vue"
import UnityView from "@/views/unityview/UnityView.vue"
import MyPage from "@/views/mypage/MyPage.vue"

import OauthRedirect from '@/components/oauth/Redirect'

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Main",
    component: Main,
  },
  {
    path: "/unityview",
    name: "UnityView",
    component: UnityView,
  },
  {
    path: "/mypage",
    name: "MyPage",
    component: MyPage,
  },
  {
    path: '/oauth/redirect',
    name: 'OauthRedrect',
    component: OauthRedirect
  },

];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;