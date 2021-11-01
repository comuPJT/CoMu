<template>
  <div class="mainpage">
    <img class="logo" src="@/assets/images/logo_4X1.png" @click="refresh()" />

    <!--메인페이지 첫번째 - 시작하기 화면-->
    <div class="getstart" v-if="order == 1">
      <div class="box1">
        <img class="lp" src="@/assets/images/mainpage_lp.gif" />
        <img class="lp_text" src="@/assets/images/mainpage_lp_text.png" />
      </div>
      <div class="box2">
        <img
          class="enter_button"
          src="@/assets/images/mainpage_enter.png"
          @click="nextStep()"
        />
      </div>
    </div>
    <!--메인페이지 첫번째 - 시작하기 화면끝-->

    <!--로그인화면-->
    <div class="login" v-if="order == 2">
      <div class="login_box1">
        <img class="lp" src="@/assets/images/mainpage_lp.gif" />
        <div class="button kakao">
          <img src="@/assets/images/kakao.svg" />
          Kakao로 로그인
        </div>
        <div class="button google">
          <img src="@/assets/images/google.svg" />
          Google로 로그인
        </div>
        <div class="button naver">
          <img src="@/assets/images/naver.svg" />
          Naver로 로그인
        </div>
        <div class="button guest">비회원으로 시작</div>
      </div>
    </div>
    <!--로그인화면 끝-->
  </div>
</template>

<script>
export default {
  name: "Main",
  props: {},
  data() {
    return {
      order: 1,
      //1=시작화면, 2=로그인, 3=회원가입
    };
  },
  methods: {
    nextStep() {
      //다음 화면으로 이동하는 메소드
      this.order = 2;
    },
    refresh() {
      //새로고침
      this.$router.go();
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./Main.scss";
</style>
