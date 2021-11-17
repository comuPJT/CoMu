<template>
  <div class="chat-wrapper">
    <div class="message-wrapper message-send">
      {{ message }}
      <div class="time-wrapper-send">{{ time }}</div>
    </div>
    <div class="profile-wrapper">
      <div class="profile-wrapper-box">
        <div class="profile-nickname">{{ nickname }}</div>
        <div class="profile-img">
          <img :src="require(`@/assets/images/character0${img}.png`)" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SendChat",

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
</style>
