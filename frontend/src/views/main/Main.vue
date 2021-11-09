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
        <div>
        <div
          class="login_button kakao"
          @click="[socialLoginUrl('kakao'), nextStep()]"
        >
          <img
            src="@/assets/images/kakao.svg"
          />
          Kakao로 로그인
        </div>
        <!-- <a :href="socialLoginUrl('google')"> -->
          <a href="http://localhost:8080/oauth2/authorization/google?redirect_uri=http://localhost:8081/oauth/redirect">
        <div
          class="login_button google"
        >
          <img
            src="@/assets/images/google.svg"
          />
          Google로 로그인
        </div>
        </a>
        <div
          class="login_button naver"
          @click="[socialLoginUrl('naver')]"
        >
          <img
            src="@/assets/images/naver.svg"
          />
          Naver로 로그인
        </div>
        <div
          class="login_button guest"
          @click="[socialLoginUrl('guest'), nextStep()]"
        >
          비회원으로 시작
        </div>
        </div>
      </div>
    </div>
    <!--로그인화면 끝-->

    <!--첫 소셜 로그인시 회원가입-->
    <div class="login" v-if="order == 3">
      <div class="join_box1">
        <div class="join_wrapper">
          <div class="join_wrapper_text">회원가입</div>
        </div>
        <div class="join_character_wrapper">
          <div class="join_character_wrapper_text">캐릭터 선택</div>
          <div class="join_carousel">
            <carousel
              class="join_carousel_inner"
              :navigation-enabled="true"
              :navigation-next-label="nextLabel"
              :navigation-prev-label="prevLabel"
              :per-page="1"
              :pagination-enabled="false"
            >
              <slide class="join_carousel_slide">
                <img src="@/assets/images/tempchar1.png" />
              </slide>
              <slide class="join_carousel_slide">
                <img src="@/assets/images/tempchar2.png" />
              </slide>
            </carousel>
          </div>
        </div>

        <div class="join_nickname_wrapper">
          <div class="input_box" style="width: 18vw">
            <div class="title">닉네임</div>
            <input placeholder="2~8자 이내의 닉네임을 입력해주세요." />
            <div class="nickname_valid" v-if="false">
              사용 가능한 닉네임입니다.
            </div>
            <div class="nickname_unvalid">유효한 닉네임을 입력해주세요.</div>
          </div>
        </div>
          <div class="join_button_wrapper">
            <div class="smallbuttonbrown" @click="$router.push({ name: 'UnityView'})">
              <div class="buttoncontent">가입하기</div>
            </div>
          </div>
      </div>
    </div>
    <!--첫 소셜 로그인시 회원가입 끝-->
  </div>
</template>

<script>
import $ from "@/util/utils";
import { Carousel, Slide } from "vue-carousel";

export default {
  name: "Main",

  components: {
    Carousel,
    Slide,
  },

  props: {},
  data() {
    return {
      order: 1,
      //1=시작화면, 2=로그인, 3=회원가입
      nextLabel:
        "<img src='https://i.ibb.co/SsQz0vB/next-1.png' style='width:1.2vw'/>",
      prevLabel:
        "<img src='https://i.ibb.co/0GW9F05/prev-1.png' style='width:1.2vw'/>",
      //캐릭터선택 좌/우 버튼
      inputNickname: "",
    };
  },
  methods: {
    nextStep() {
      //다음 화면으로 이동하는 메소드
      this.order += 1;
    },
    refresh() {
      //새로고침
      this.$router.go();
    },
    socialLoginUrl(socialType) {
      return $.getSocialLoginUrl(socialType);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./Main.scss";
</style>
