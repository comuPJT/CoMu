<template>
  <div>
    <nav-bar
      @clickNavbar="[SetUnityKeyboardInput('FALSE'), (isShowBlind = true)]"
      :isMute="ismute"
    ></nav-bar>
    <!-- 유니티 화면 -->
    <div id="unity">
      <div
        v-if="isShowBlind"
        class="blind"
        @click="[SetUnityKeyboardInput('TRUE'), (isShowBlind = false)]"
      >
        <div class="blind-msg">
          다시 캐릭터를 움직이려면 화면을 클릭해주세요!
        </div>
      </div>
      <unity
        v-if="false"
        src="unity/Build/unity.json"
        unityLoader="unity/Build/UnityLoader.js"
        ref="comu"
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

      <!--사연 명예의전당 보기-->
      <best-story v-if="showModalBestStory" @close="closeBestStoryModal">
      </best-story>
      <!-- 캐릭터 커스텀 변경 -->
      <character-custom
        v-if="showModalCharacterCustom"
        @setCharacter="setCharacterNum"
        @close="closeCharacterCustomModal"
      >
      </character-custom>
    </div>
    <!-- 음악 재생 -->
    <video v-show="false" id="video"></video>
  </div>
</template>

<script>
import Unity from "vue-unity-webgl";
import PublicPlayList from "./components/PublicPlayList.vue";
import PublicPlayListAdd from "./components/PublicPlayListAdd.vue";
import NormalStory from "./components/NormalStory.vue";
import BestStory from "./components/BestStory.vue";
import CharacterCustom from "./components/CharacterCustom.vue";

export default {
  name: "UnityView",

  components: {
    Unity,
    PublicPlayList,
    PublicPlayListAdd,
    NormalStory,
    BestStory,
    CharacterCustom,
  },

  props: {},
  data() {
    return {
      showModalPlayList: false,
      showModalPlayListAdd: false,
      showModalTodayStory: false,
      showModalBestStory: false,
      showModalCharacterCustom: false,
      unityWidth: 0,
      unityHeight: 0,
      isShowBlind: false,
      ismute: true,
    };
  },

  mounted() {
    // 창 크기가 바뀔 때마다 유니티 화면 크기 변경
    this.handleResize();
    window.addEventListener("resize", this.handleResize);

    // 유니티 키보드 입력 활성화
    this.SetUnityKeyboardInput("TRUE");

    // 모달창 모두 닫힌 상태로 시작
    localStorage.setItem("showPlayList", "FALSE");
    localStorage.setItem("showPlayListAdd", "FALSE");
    localStorage.setItem("showTodayStory", "FALSE");
    localStorage.setItem("showCharacter", "FALSE");

    // localStorage 값 변경 확인할 인터벌 함수 실행
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
    // 유니티 화면에 표시되는 닉네임 변경
    setUserNickname(nickname) {
      this.$refs.comu.message("Nickname", "SetUserNickname", nickname);
      localStorage.setItem("user-nickname", nickname);
    },
    // 유니티 화면에 보여지는 캐릭터 번호 변경
    setCharacterNum(num) {
      this.$refs.comu.message("PlayerObject", "SetCharacterNum", num);
    },
    // 창 크기에 맞춰서 유니티 화면 크기 변경
    handleResize() {
      this.unityWidth = document.getElementById("unity").offsetWidth;
      this.unityHeight = document.getElementById("unity").offsetHeight;
    },
    // localStorage의 모달 관련 값 변경 여부 확인
    fetchShowModal() {
      this.showModalPlayList =
        localStorage.getItem("showPlayList") == "TRUE" ? true : false;
      this.showModalPlayListAdd =
        localStorage.getItem("showPlayListAdd") == "TRUE" ? true : false;
      this.showModalTodayStory =
        localStorage.getItem("showTodayStory") == "TRUE" ? true : false;
      this.showModalBestStory =
        localStorage.getItem("showestStory") == "TRUE" ? true : false;
      this.showModalCharacterCustom =
        localStorage.getItem("showCharacter") == "TRUE" ? true : false;
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
    // 사연 명예의전당 페이지 닫기
    closeBestStoryModal() {
      this.showModalBestStory = false;
      localStorage.setItem("showBestStory", "FALSE");
      this.SetUnityKeyboardInput("TRUE");
    },
    // 유저 캐릭터 커스텀창 닫기
    closeCharacterCustomModal() {
      this.showModalCharacterCustom = false;
      localStorage.setItem("showCharacter", "FALSE");
      this.SetUnityKeyboardInput("TRUE");
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UnityView.scss";
</style>
