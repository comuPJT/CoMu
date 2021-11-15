<template>
  <div></div>
</template>

<script>
import {mapMutations, mapActions} from "vuex";
import Spotify from "@/api/spotify.js";
import store from "@/store/index";

export default {
  created() {
    const token = this.$route.query.token;
    if (token) {
      //소셜로그인에 성공하면 jwt토큰과 유저 정보를 스토어에 저장하고 스포티파이 인증을 받습니다.
      this.setToken(token);
      this.fetchUser();
      // characterNum:0
      // createdAt:"2021-11-14T21:57:42"
      // email:"aoffltk88@gmail.com"
      // emailVerifiedYn:"Y"
      // firstVisitYn:"Y"
      // modifiedAt:"2021-11-14T21:57:42"
      // providerType:"GOOGLE"
      // roleType:"USER"
      // userId:"103866708591396004823"
      // username:"우현"
      // userseq: 4

      //로컬에 유저닉네임이랑 캐릭터번호 저장합니다.
      localStorage.setItem("user-nickname", store.getters.users.username);
      localStorage.setItem(
        "user-character-num",
        store.getters.users.characterNum
      );
      Spotify.spotifyAccess();
    }
    if (store.getters.user.firstVisitYn == "Y") {
      //처음방문이면 닉네임, 캐릭터선택으로
      this.$router.replace({name: "Main", params: {order: 3}});
    } else {
      //초기 설정이 완료된 플레이어는 바로 유니티화면으로
      this.$router.replace("/unityview");
    }
  },
  methods: {
    ...mapActions(["fetchUser"]),
    ...mapMutations(["setToken"]),
  },
};
</script>
