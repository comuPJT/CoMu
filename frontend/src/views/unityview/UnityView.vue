<template>
  <div>
    <nav-bar></nav-bar>
    <!-- 유니티 화면 -->
    <div id="unity">
      <unity
        src="unity/Build/unity.json"
        unityLoader="unity/Build/UnityLoader.js"
        :width="unityWidth"
        :height="unityHeight"
        :hideFooter="true"
      ></unity>
      <!-- 공용 플레이리스트 -->
      <public-play-list v-if="showModal" @close="closeModal">
      </public-play-list>
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
      unityWidth: 0,
      unityHeight: 0,
    };
  },

  mounted() {
    // 테스트용 캐릭터 번호 값 설정
    localStorage.setItem("characterNum", 1);

    // 창 크기에 맞춰서 유니티 화면 크기 변경
    this.unityWidth = document.getElementById("unity").offsetWidth;
    this.unityHeight = document.getElementById("unity").offsetHeight;
    window.addEventListener("resize", this.handleResize);

    setInterval(this.fetchShowModal, 100);
  },

  methods: {
    handleResize() {
      this.unityWidth = document.getElementById("unity").offsetWidth;
      this.unityHeight = document.getElementById("unity").offsetHeight;
    },
    fetchShowModal() {
      this.showModal =
        localStorage.getItem("showPlayList") == "TRUE" ? true : false;
    },
    closeModal() {
      this.showModal = false;
      localStorage.setItem("showPlayList", "FALSE");
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UnityView.scss";
</style>
