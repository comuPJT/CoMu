<template>
  <transition name="modal" appear>
    <div class="mypage-wrapper">
      <!-- 마이페이지용 네비바-->
      <div class="mypage-nav">
        <div class="mypage-nav-logo">
          <img src="@/assets/images/logo_4X1.png" />
        </div>
        <div class="mypage-nav-text">마이페이지</div>

        <div class="mypage-nav-type">
          <img src="@/assets/images/mypage_account_logo.svg" />
          계정
        </div>
        <div
          class="mypage-nav-detail"
          @click="selectedMenu = 'userInfo'"
          v-bind:class="{
            'mypage-nav-detail-selected': selectedMenu == 'userInfo',
          }"
        >
          회원 정보
        </div>
        <div
          class="mypage-nav-detail"
          @click="selectedMenu = 'userCustom'"
          v-bind:class="{
            'mypage-nav-detail-selected': selectedMenu == 'userCustom',
          }"
        >
          커스터마이징 변경
        </div>

        <div class="mypage-nav-type">
          <img src="@/assets/images/mypage_music_logo.svg" />
          음악
        </div>
        <div
          class="mypage-nav-detail"
          @click="selectedMenu = 'userPersonalPlayList'"
          v-bind:class="{
            'mypage-nav-detail-selected':
              selectedMenu == 'userPersonalPlayList',
          }"
        >
          내 재생 목록
        </div>
        <div
          class="mypage-nav-detail"
          @click="selectedMenu = 'userPlayList'"
          v-bind:class="{
            'mypage-nav-detail-selected': selectedMenu == 'userPlayList',
          }"
        >
          내 플레이 리스트
        </div>

        <div class="mypage-nav-exit">
          <img
            src="@/assets/images/exit-div.svg"
            @click.self="$emit('close')"
          />
        </div>
      </div>
      <!-- 마이페이지용 네비바 끝 -->

      <!-- 마이페이지 컨텐츠(회원정보, 캐릭터, 내 플레이 리스트) 뷰 -->
      <div class="mypage-content">
        <user-info v-if="selectedMenu == 'userInfo'"></user-info>
        <user-custom v-if="selectedMenu == 'userCustom'"></user-custom>
        <user-play-list v-if="selectedMenu == 'userPlayList'"></user-play-list>
        <user-personal-play-list
          v-if="selectedMenu == 'userPersonalPlayList'"
        ></user-personal-play-list>
      </div>
      <!-- 마이페이지 컨텐츠(회원정보, 캐릭터, 내 플레이 리스트) 뷰 끝 -->
    </div>
  </transition>
</template>

<script>
import UserInfo from "./components/UserInfo.vue";
import UserCustom from "./components/UserCustom.vue";
import UserPlayList from "./components/UserPlayList.vue";
import UserPersonalPlayList from "./components/UserPersonalPlayList.vue";

export default {
  name: "MyPage",

  components: {UserInfo, UserCustom, UserPlayList, UserPersonalPlayList},

  props: {},
  data() {
    return {
      selectedMenu: "userInfo",
    };
  },
  mounted() {
    if (localStorage.getItem("userType") == "guest") {
      this.$router.replace({name: "Main", params: {order: 2}});
    }
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./MyPage.scss";
</style>
