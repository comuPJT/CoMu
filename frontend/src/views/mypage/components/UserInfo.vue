<template>
  <div class="user-info-wrapper">
    <div class="user-info-text">회원 정보</div>
    <div class="user-info-character">
      useremail
      <img src="@/assets/images/tempchar1.png" />
    </div>
    <div class="user-info-input">
      <div class="input_box" style="width: 18vw">
        <div class="title">닉네임</div>
        <input placeholder="유저가 사용중이던 닉네임" />
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
    return {};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./UserInfo.scss";
</style>
