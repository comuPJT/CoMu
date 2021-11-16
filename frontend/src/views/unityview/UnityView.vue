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
      <public-play-list v-if="showModalPlayList" @close="closePlayListModal">
      </public-play-list>
      <!-- 새로운 곡 신청 -->
      <public-play-list-add
        v-if="showModalPlayListAdd"
        @close="closePlayListAddModal"
      >
      </public-play-list-add>
      <!-- 오늘의 사연 보기 -->
      <normal-story v-if="showModalTodayStory" @close="closeTodayStoryModal">
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
      showModalTodayStory: false,
      unityWidth: 0,
      unityHeight: 0,
    };
  },

  mounted() {
    // 창 크기에 맞춰서 유니티 화면 크기 변경
    this.unityWidth = document.getElementById("unity").offsetWidth;
    this.unityHeight = document.getElementById("unity").offsetHeight;
    window.addEventListener("resize", this.handleResize);

    // 유니티 키보드 입력 활성화
    this.SetUnityKeyboardInput("TRUE");

    // 모달창 모두 닫힌 상태로 시작
    localStorage.setItem("showPlayList", "FALSE");
    localStorage.setItem("showPlayListAdd", "FALSE");
    localStorage.setItem("showTodayStory", "FALSE");

    // localStorage 값 변경 여부 확인할 인터벌함수
    setInterval(this.fetchShowModal, 100);
  },

  beforeDestroy() {
    // 유니티 키보드 입력 비활성화
    this.SetUnityKeyboardInput("FALSE");
  },

  methods: {
    // 유니티 키보드 입력 활성화 여부 변경
    SetUnityKeyboardInput(value) {
      this.$refs.comu.message(
        "textbox-playlist",
        "SetUnityKeyboardInput",
        value
      );
    },

    handleResize() {
      this.unityWidth = document.getElementById("unity").offsetWidth;
      this.unityHeight = document.getElementById("unity").offsetHeight;
    },
    fetchShowModal() {
      this.showModalPlayList =
        localStorage.getItem("showPlayList") == "TRUE" ? true : false;
      this.showModalPlayListAdd =
        localStorage.getItem("showPlayListAdd") == "TRUE" ? true : false;
      this.showModalTodayStory =
        localStorage.getItem("showTodayStory") == "TRUE" ? true : false;
    },
    // 공용 플레이리스트 닫기
    closePlayListModal() {
      this.showModalPlayList = false;
      localStorage.setItem("showPlayList", "FALSE");
      this.SetUnityKeyboardInput("TRUE");
    },
    // 곡 신청 페이지 닫기
    closePlayListAddModal() {
      this.showModalPlayListAdd = false;
      localStorage.setItem("showPlayListAdd", "FALSE");
      this.SetUnityKeyboardInput("TRUE");
    },
    // 오늘의 사연 페이지 닫기
    closeTodayStoryModal() {
      this.showModalTodayStory = false;
      localStorage.setItem("showTodayStory", "FALSE");
      this.SetUnityKeyboardInput("TRUE");
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UnityView.scss";
</style>
