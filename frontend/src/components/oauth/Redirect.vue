<template>
  <div></div>
</template>

<script>
import {mapMutations, mapActions} from "vuex";
import Spotify from "@/api/spotify.js";
import store from '@/store/index'

export default {
  created() {
    const token = this.$route.query.token;
    if (token) {
      //소셜로그인에 성공하면 jwt토큰과 유저 정보를 스토어에 저장하고 스포티파이 인증을 받습니다.
      this.setToken(token)
      this.fetchUser()
      Spotify.spotifyAccess();
    }
    if(store.getters.user.firstVisitYn=='Y'){ //처음방문이면 닉네임, 캐릭터선택으로
      this.$router.replace({name:"Main", params:{order:3}});
    }
    else{ //초기 설정이 완료된 플레이어는 바로 유니티화면으로
      this.$router.replace("/unityview");
    }
  },
  methods: {
    ...mapActions(["fetchUser"]),
    ...mapMutations(["setToken"]),
  },
};
</script>
