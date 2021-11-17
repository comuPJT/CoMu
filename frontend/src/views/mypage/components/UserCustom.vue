<template>
  <div class="user-custom-wrapper">
    <div class="user-custom-text">커스터마이징 변경</div>
    <div class="user-custom-body">
      <div class="user-custom-wrapper-text">캐릭터 선택</div>
      <div class="user-custom-carousel">
        <carousel
          class="user-custom-carousel-inner"
          ref="my-carousel"
          :navigate-to="[character - 1, false]"
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
    <div class="user-custom-button">
      <div class="smallbuttonbrown">
        <div class="buttoncontent" @click="updateInfo()">저장</div>
        <button @click="deleteTest()">딜리트테스트</button>
      </div>
    </div>
  </div>
</template>

<script>
import {Carousel, Slide} from "vue-carousel";
import userApi from "@/api/user";
import myPlayListApi from "@/api/myPlayList";
import {mapMutations} from "vuex";

export default {
  name: "UserCustom",

  components: {Carousel, Slide},

  props: {},
  data() {
    return {
      nextLabel:
        "<img src='https://i.ibb.co/SsQz0vB/next-1.png' style='width:1.2vw'/>",
      prevLabel:
        "<img src='https://i.ibb.co/0GW9F05/prev-1.png' style='width:1.2vw'/>",
      //캐릭터선택 좌/우 버튼
      character: "",
    };
  },
  created() {
    this.character = localStorage.getItem("characterNum");
  },
  methods: {
    ...mapMutations(["setUserCharacter"]),
    updateInfo() {
      const data = [
        parseInt(this.$store.getters.user.userSeq),
        this.$store.getters.user.user.username,
        parseInt(this.$refs["my-carousel"].currentPage + 1),
      ];

      console.log(data);
      userApi.updateCharacter(
        data,
        (res) => {
          this.setUserCharacter(data[2]);
          localStorage.setItem("characterNum", data[2]);
          alert("변경되었습니다.");
          console.log(res);
        },
        (err) => {
          console.log(err);
        }
      );
    },

    deleteTest() {
      const data = [1, 2];
      myPlayListApi.deletePlayList(
        data,
        (res) => {
          console.log(res);
        },
        (err) => {
          console.log(err);
        }
      );
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UserCustom.scss";
</style>
