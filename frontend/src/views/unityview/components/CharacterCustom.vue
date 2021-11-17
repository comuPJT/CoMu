<template>
  <div>
    <transition name="modal" appear>
      <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-window">
          <!-- 캐릭터 커스텀 변경 타이틀 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>캐릭터 커스텀 변경</p>
            <img
              src="@/assets/images/close_button.svg"
              @click.self="$emit('close')"
            />
          </div>
          <!-- 캐릭터 커스텀 변경 타이틀 / 모달 닫기 버튼 끝-->

          <!--캐릭터 선택-->
          <div class="modal-content" v-if="!isDetailView">
            <div class="user-custom-text">캐릭터 선택</div>
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
            <div class="user-custom-button">
              <div class="smallbuttonbrown">
                <div class="buttoncontent" @click="updateInfo()">저장</div>
              </div>
            </div>
          </div>
          <!--캐릭터 선택 끝-->
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import {Carousel, Slide} from "vue-carousel";
import userApi from "@/api/user";
import {mapMutations} from "vuex";

export default {
  name: "CharacterCustom",

  components: {
    Carousel,
    Slide,
  },

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
      userApi.updateCharacter(
        data,
        (res) => {
          this.setUserCharacter(data[2]);
          localStorage.setItem("characterNum", data[2]);
          this.$emit("setCharacter", data[2]);
          alert("변경되었습니다.");
          this.$emit("close");
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
@import "./CharacterCustom.scss";
</style>
