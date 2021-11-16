<template>
  <div class="chat-wrapper">
    <div class="profile-wrapper">
      <div class="profile-wrapper-box">
        <div class="profile-nickname">{{ nickname }}</div>
        <img :src="require(`@/assets/images/bgr_character0${img}.png`)" />
      </div>
    </div>
    <div class="message-wrapper message-receive">
      {{ message }}
      <div class="time-wrapper-receive">{{ time }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReceiveChat",

  components: {},

  props: ["message", "nickname", "time", "img"],
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./SendChat.scss";
//CSS 전부 공통이라 파일 분리 안하고 SendChat.scss사용
</style>
