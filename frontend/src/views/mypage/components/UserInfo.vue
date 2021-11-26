<template>
  <div class="user-info-wrapper">
    <div class="user-info-text">회원 정보</div>
    <div class="user-info-character">
      <img
        :src="
          require(`@/assets/images/character0${this.$store.getters.user.user.characterNum}.png`)
        "
      />
    </div>
    <div class="user-info-input">
      <div class="input_box" style="width: 18vw">
        <div class="title">닉네임</div>
        <input
          v-model="inputNickName"
          :placeholder="`${this.$store.getters.user.user.username}`"
        />
      </div>
    </div>
    <div class="user-info-button">
      <div class="smallbuttonbrown">
        <div class="buttoncontent" @click="updateInfo()">닉네임 변경</div>
      </div>
      <a>로그아웃</a>
    </div>
  </div>
</template>

<script>
import userApi from "@/api/user";
import {mapMutations} from "vuex";

export default {
  name: "UserInfo",

  components: {},

  props: {},
  data() {
    return {
      inputNickName: "",
    };
  },
  methods: {
    ...mapMutations(["setUserName"]),
    updateInfo() {
      if (this.inputNickName.length < 2 || this.inputNickName.length > 8) {
        this.$alert("2~8글자 사이의 닉네임을 입력해주세요!");
      } else {
        const data = [
          parseInt(this.$store.getters.user.userSeq),
          this.inputNickName,
          localStorage.getItem("characterNum"),
        ];
        userApi.updateNickname(
          data,
          (res) => {
            this.setUserName(data[1]);
            localStorage.setItem("userNickname", data[1]);
            this.$alert("닉네임이 변경되었습니다.");
            console.log(res);
          },
          (err) => {
            this.$alert("중복된 닉네임입니다.");
            console.log(err);
          }
        );
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UserInfo.scss";
</style>
