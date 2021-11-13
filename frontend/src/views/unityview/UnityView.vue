<template>
  <div>
    <nav-bar></nav-bar>
    <!-- 유니티 화면 -->
    <div id="unity">
      <unity
      v-if="false"
      tabindex="1"
        src="unity/Build/unity.json"
        unityLoader="unity/Build/UnityLoader.js"
        :height="unityHeight"
        :hideFooter="true"
      ></unity>
      <!-- 버튼 숨기기 (임시) -->
      <div style="position:absolute ; bottom:0 ; right:0;">
        <button id="show-modal" @click="showModal = true">
          공용플레이리스트 띄우기
        </button>
        <public-play-list v-if="showModal" @close="showModal = false">
        </public-play-list>
      </div>
    </div>
  </div>
</template>

<script>
import Unity from "vue-unity-webgl";
import PublicPlayList from "./components/PublicPlayList.vue";

export default {
  name: "UnityView",

  components: {
    Unity,
    PublicPlayList,
  },

  props: {},
  data() {
    return {
      showModal: false,
      unityHeight: 0,
    };
  },

  mounted() {
    // 테스트용 캐릭터 번호 값 셋팅
    localStorage.setItem("characterNum", 1);

    // 창 크기에 맞춰서 유니티 화면 크기 변경
    this.unityHeight = document.getElementById("unity").offsetHeight;
    window.addEventListener("resize", this.handleResize);
  },

  methods: {
    handleResize() {
      this.unityHeight = document.getElementById("unity").offsetHeight;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UnityView.scss";
</style>
