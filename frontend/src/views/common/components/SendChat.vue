<template>
  <div class="chat-wrapper">
    <div class="message-wrapper message-send">
      {{ message }}
      <div class="time-wrapper-send">{{time}}</div>
    </div>
    <div class="profile-wrapper">
      <div class="profile-wrapper-box">
        <div class="profile-nickname">{{ nickname }}</div>
        <img
          src="https://file.mk.co.kr/meet/neds/2015/09/image_readtop_2015_891935_14423221542127136.jpg"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SendChat",

  components: {},

  props: ["message", "nickname", "time"],
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./SendChat.scss";
</style>
