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
          <a :href="socialLoginUrl('kakao')">
            <div class="login_button kakao">
              <img src="@/assets/images/kakao.svg" />
              Kakao로 로그인
            </div>
          </a>
          <a :href="socialLoginUrl('google')">
            <div class="login_button google">
              <img src="@/assets/images/google.svg" />
              Google로 로그인
            </div>
          </a>
          <a :href="socialLoginUrl('naver')">
            <div class="login_button naver">
              <img src="@/assets/images/naver.svg" />
              Naver로 로그인
            </div>
          </a>
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
              ref="my-carousel"
              class="join_carousel_inner"
              :navigation-enabled="true"
              :navigation-next-label="nextLabel"
              :navigation-prev-label="prevLabel"
              :per-page="1"
              :loop="true"
              :pagination-enabled="false"
            >
              <slide v-for="t in 10" :key="t.num" class="join_carousel_slide">
                <img :src="require(`@/assets/images/character0${t}.png`)" />
              </slide>
            </carousel>
          </div>
        </div>

        <div class="join_nickname_wrapper">
          <div class="input_box" style="width: 18vw">
            <div class="title">닉네임</div>
            <input
              v-model="inputNickname"
              placeholder="2~8자 이내의 닉네임을 입력해주세요."
            />
            <div class="nickname_valid" v-if="inputNicknameValid">
              사용 가능한 닉네임입니다.
            </div>
            <div class="nickname_unvalid" v-if="!inputNicknameValid">
              유효한 닉네임을 입력해주세요.
            </div>
          </div>
        </div>
        <div class="join_button_wrapper">
          <div class="smallbuttonbrown" @click="joinRequest()">
            <div class="buttoncontent">가입하기</div>
          </div>
        </div>
      </div>
    </div>
    <!--첫 소셜 로그인시 회원가입 끝-->
  </div>
</template>

<script>
import {API_SERVER_URL, API_CLIENT_URL} from "@/constant/index.js";
import {mapMutations} from "vuex";
import {Carousel, Slide} from "vue-carousel";
import userApi from "@/api/user";

export default {
  name: "Main",

  components: {
    Carousel,
    Slide,
  },

  props: {},
  data() {
    return {
      order: 1, //1=시작화면, 2=로그인, 3=회원가입
      nextLabel:
        "<img src='https://i.ibb.co/SsQz0vB/next-1.png' style='width:1.2vw'/>",
      prevLabel:
        "<img src='https://i.ibb.co/0GW9F05/prev-1.png' style='width:1.2vw'/>", //캐릭터선택 좌/우 버튼
      inputNickname: "",
      inputNicknameValid: false, // 닉네임 적합성 판단
    };
  },
  mounted() {
    // sns 로그인시 첫 방문이면 닉네임, 캐릭터 설정으로 넘어갑니다.
    if (this.$route.params.order == 3) {
      this.order = 3;
    } else {
      this.order = 1;
    }
  },
  methods: {
    ...mapMutations(["setUserName"]),
    ...mapMutations(["setUserCharacter"]),
    nextStep() {
      //다음 화면으로 이동하는 메소드
      this.order += 1;
    },
    refresh() {
      //새로고침
      this.$router.go();
    },
    socialLoginUrl(socialType) {
      return `${API_SERVER_URL}/oauth2/authorization/${socialType}?redirect_uri=${API_CLIENT_URL}/oauth/redirect`;
    },
    async joinRequest() {
      if (!this.inputNicknameValid) {
        this.$alert("2~8글자 사이의 닉네임을 입력해주세요!");
      } else {
        const data = [
          parseInt(this.$store.getters.user.userSeq),
          this.inputNickname,
          this.$refs["my-carousel"].currentPage + 1,
        ];
        //입력한 정보로 회원가입 요청을 보냅니다.
        await userApi.updateNickname(
          data,
          (res) => {
            //성공하면 회원정보 저장시키고 유니티화면으로 이동
            this.$router.push({name: "UnityView"});
            console.log(res);
          },
          (err) => {
            //실패(닉네임 중복)하면 중복된닉네임이라고 메시지 띄워줌
            this.$alert("중복된 닉네임입니다!");
            console.log(err);
          }
        );
        
        await userApi.updateCharacter(
          data,
          (res) => {
            console.log(res);
            this.setUserName(data[1]);
            this.setUserCharacter(data[2]);
          },
          (err) => {
            console.log(err);
          }
        );
      }
    },
  },

  watch: {
    inputNickname: function () {
      //닉네임길이 적합성 판단
      if (this.inputNickname.length < 2 || this.inputNickname.length > 8) {
        this.inputNicknameValid = false;
      } else {
        this.inputNicknameValid = true;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./Main.scss";
</style>
