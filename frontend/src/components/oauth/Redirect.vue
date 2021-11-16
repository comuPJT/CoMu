<template>
  <div></div>
</template>

<script>
import {mapMutations, mapActions} from "vuex";
import Spotify from "@/api/spotify.js";

export default {
  async created() {
    const token = this.$route.query.token;
    if (token) {
      //소셜로그인에 성공하면 jwt토큰과 유저 정보를 스토어에 저장하고 스포티파이 인증을 받습니다.
      this.setToken(token);
      Spotify.spotifyAccess();
      await this.fetchUser();
      setTimeout(() => {
        this.$router.replace("/redirectrouter");
      }, 1000);
      //await this.fetchUser();
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
      //로컬스토리지에 유저닉네임이랑 캐릭터번호, 유저시퀀스 저장합니다.
    }
  },
  methods: {
    ...mapActions(["fetchUser"]),
    ...mapMutations(["setToken"]),
  },
};
</script>
