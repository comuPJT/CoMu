<template>
  <div>
    <nav-bar></nav-bar>
    <!-- 유니티 화면 -->
    <div id="unity">
      <unity
        v-if="false"
        src="unity/Build/unity.json"
        unityLoader="unity/Build/UnityLoader.js"
        :width="unityWidth"
        :height="unityHeight"
        :hideFooter="true"
      ></unity>
      <!-- 버튼 숨기기 (임시) -->
      <div></div>
      <button id="show-modal" @click="showModalPlayList = true">
        공용플레이리스트 띄우기
      </button>
      <button id="show-modal" @click="showModalPlayListAdd = true">
        곡 신청 띄우기
      </button>
      <button id="show-modal" @click="showModalStory = true">
        사연함 띄우기
      </button>
      <public-play-list
        v-if="showModalPlayList"
        @close="showModalPlayList = false"
      >
      </public-play-list>
      <public-play-list-add
        v-if="showModalPlayListAdd"
        @close="showModalPlayListAdd = false"
      >
      </public-play-list-add>
      <normal-story v-if="showModalStory" @close="showModalStory = false">
      </normal-story>
    </div>
  </div>
</template>

<script>
import Unity from "vue-unity-webgl";
import PublicPlayList from "./components/PublicPlayList.vue";
import PublicPlayListAdd from "./components/PublicPlayListAdd.vue";
import NormalStory from "./components/NormalStory.vue";

export default {
  name: "UnityView",

  components: {
    Unity,
    PublicPlayList,
    PublicPlayListAdd,
    NormalStory,
  },

  props: {},
  data() {
    return {
      showModalPlayList: false,
      showModalPlayListAdd: false,
      showModalStory: false,
      unityWidth: 0,
      unityHeight: 0,
    };
  },

  mounted() {
    // 테스트용 캐릭터 번호 값 셋팅
    //localStorage.setItem("characterNum", 1);

    // 창 크기에 맞춰서 유니티 화면 크기 변경
    this.unityWidth = document.getElementById("unity").offsetWidth;
    this.unityHeight = document.getElementById("unity").offsetHeight;
    window.addEventListener("resize", this.handleResize);
  },

  methods: {
    handleResize() {
      this.unityWidth = document.getElementById("unity").offsetWidth;
      this.unityHeight = document.getElementById("unity").offsetHeight;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UnityView.scss";
</style>
