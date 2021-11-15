<template>
  <div class="user-info-wrapper">
    <div class="user-info-text">회원 정보</div>
    <div class="user-info-character">
      <img src="@/assets/images/google.svg" v-if="this.$store.getters.user.providerType=='GOOGLE'">
      <img src="@/assets/images/kakao.svg" v-if="this.$store.getters.user.providerType=='KAKAO'">
      <img src="@/assets/images/naver.svg" v-if="this.$store.getters.user.providerType=='NAVER'">
      {{this.$store.getters.user.email}}
      <img :src="require(`@/assets/images/character0${this.$store.getters.user.characterNum}.png`)" />
    </div>
    <div class="user-info-input">
      <div class="input_box" style="width: 18vw">
        <div class="title">닉네임</div>
        <input v-model="inputNickName" :placeholder="`${this.$store.getters.user.nickName}`" />
      </div>
    </div>
    <div class="user-info-button">
      <div class="smallbuttonbrown">
        <div class="buttoncontent">닉네임 변경</div>
      </div>
      <a>로그아웃</a>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserInfo",

  components: {},

  props: {},
  data() {
    return {
      inputNickName:"",
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./UserInfo.scss";
</style>
