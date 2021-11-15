<template>
  <div class="user-custom-wrapper">
    <div class="user-custom-text">커스터마이징 변경</div>
    <div class="user-custom-body">
      <div class="user-custom-wrapper-text">캐릭터 선택</div>
      <div class="user-custom-carousel">
        <carousel
          class="user-custom-carousel-inner"
          :navigation-enabled="true"
          :navigation-next-label="nextLabel"
          :navigation-prev-label="prevLabel"
          :per-page="1"
          :pagination-enabled="false"
        >
          <slide class="user-custom-carousel-slide">
            <img src="@/assets/images/tempchar1.png" />
          </slide>
          <slide class="user-custom-carousel-slide">
            <img src="@/assets/images/tempchar2.png" />
          </slide>
        </carousel>
      </div>
    </div>
    <div class="user-custom-button">
      <div class="smallbuttonbrown">
        <div class="buttoncontent">저장</div>
      </div>
    </div>
  </div>
</template>

<script>
import {Carousel, Slide} from "vue-carousel";

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
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./UserCustom.scss";
</style>
