<template>
  <div>
    <!--네비바 좌측 메뉴선택부분-->
    <div class="nav-bar-left">
      <img class="logo-2X2" src="@/assets/images/logo_2X2.png" />
      <div class="menu_icons">
        <img
          src="@/assets/images/online_icon.svg"
          class="menu_icon"
          :class="{ 'menu-selected': selectedMenu === 'online' }"
          @click="changeMenu('online')"
        />
        <img
          src="@/assets/images/chat_icon.svg"
          class="menu_icon"
          :class="{ 'menu-selected': selectedMenu === 'chat' }"
          @click="changeMenu('chat')"
        />
        <img
          src="@/assets/images/setting_icon.svg"
          class="menu_icon"
          :class="{ 'menu-selected': selectedMenu === 'setting' }"
          @click="changeMenu('setting')"
        />
        <img
          src="@/assets/images/mypage_icon.svg"
          class="menu_icon"
          :class="{ 'menu-selected': selectedMenu === 'mypage' }"
          @click="[changeMenu('mypage'), $router.push('/mypage')]"
        />
      </div>
    </div>
    <!--네비바 좌측 메뉴선택부분 끝-->

    <!--네비바 우측 -->
    <div class="nav-bar-right">
      <!--현재 재생중인 음악-->
      <div class="now-playing">
        <div class="now-playing-img">
          <img class="img1" src="@/assets/images/lp_now_playing.png" />
          <img
            class="img2"
            src="https://upload.wikimedia.org/wikipedia/ko/f/f4/%EA%B1%B4%EC%A6%88_%EC%95%A4_%EB%A1%9C%EC%A7%80%EC%8A%A4_-_Use_Your_Illusion_I.jpg"
          />
        </div>
        <div style="float: left; width: 42%; height: 77%; margin-top: 10%">
          <div class="music-name-box" ref="musicNameBox">
            <!--노래제목 길이 구하기용 쓰레기태그-->
            <p ref="musicNameTag">
              {{ musicName }}
            </p>

            <marquee-text
              class="marquee-text"
              v-if="isMusicOverflow"
              :repeat="1"
              :duration="5"
            >
              {{ musicName }}
            </marquee-text>
            <div v-if="!isMusicOverflow">
              {{ musicName }}
            </div>
          </div>
          <div class="artist-name-box" ref="artistNameBox">
            <!--아티스트 이름 길이 구하기용 쓰레기태그-->
            <p ref="artistNameTag">
              {{ artistName }}
            </p>
            <marquee-text
              class="marquee-text"
              v-if="isArtistOverflow"
              :repeat="1"
              :duration="5"
            >
              {{ artistName }}
            </marquee-text>
            <div v-if="!isArtistOverflow">
              {{ artistName }}
            </div>
          </div>
        </div>
      </div>
      <div class="room-title">별이빛나는낮에 30.4FM</div>
      <!--현재 재생중인 음악 끝-->
      <!--접속중인 유저 목록-->
      <div v-show="selectedMenu == 'online'">
        
        <div class="user-list-box">
          <div class="user-list">
            <div class="user-list-title">&nbsp;ONLINE - 5</div>
            <img
              class="fold-button"
              src="@/assets/images/fold_icon.svg"
              v-show="isOnlineUnfold"
              @click="isOnlineUnfold = !isOnlineUnfold"
            />
            <img
              class="fold-button"
              src="@/assets/images/unfold_icon.svg"
              v-show="!isOnlineUnfold"
              @click="isOnlineUnfold = !isOnlineUnfold"
            />
            <img class="online-dot" src="@/assets/images/online_dot.svg" />
          </div>
          <div
            class="user-list-unfold"
            v-show="isOnlineUnfold"
            v-for="n in 5"
            :key="n"
          >
            <div class="user-details">
              <img
                src="https://file.mk.co.kr/meet/neds/2015/09/image_readtop_2015_891935_14423221542127136.jpg"
              />
              <div>&nbsp;&nbsp;dnguszz</div>
            </div>
          </div>

          <div class="user-list">
            <div class="user-list-title">&nbsp;GUEST - 3</div>
            <img
              class="fold-button"
              src="@/assets/images/fold_icon.svg"
              v-show="isGuestUnfold"
              @click="isGuestUnfold = !isGuestUnfold"
            />
            <img
              class="fold-button"
              src="@/assets/images/unfold_icon.svg"
              v-show="!isGuestUnfold"
              @click="isGuestUnfold = !isGuestUnfold"
            />
          </div>
          <div
            class="user-list-unfold"
            v-show="isGuestUnfold"
            v-for="n in 3"
            :key="n"
          >
            <div class="user-details">
              <img src="@/assets/images/guest_icon.svg" />
              <div>&nbsp;&nbsp;Guest - {{ n }}</div>
            </div>
          </div>
        </div>
      </div>
      <!--접속중인 유저 목록 끝-->

      <!--채팅-->
      <div v-show="selectedMenu == 'chat'">
        <receive-chat :message="'그래요'" :nickname="'가장긴닉네임'" :time="'11:00AM'"></receive-chat>
        <send-chat :message="'채팅채팅'" :nickname="'dnguszz'" :time="'11:00AM'"></send-chat>
        <send-chat :message="'반갑습니다안녕하세요오랜만이에요긴내용채우기123 하나둘셋반갑습니다안녕하세요오랜만이에요긴내용채우기123 하나둘셋반갑습니다안녕하세요오랜만이에요긴내용채우기123 하나둘셋'" :nickname="'dnguszz'" :time="'11:00AM'"></send-chat>
        <receive-chat :message="'그래요'" :nickname="'가장긴닉네임'" :time="'11:00AM'"></receive-chat>
      </div>
      <!--채팅끝-->

      <!--설정-->
      <!--설정끝-->
    </div>
    <!--네비바 우측 끝-->
  </div>
</template>

<script>
import MarqueeText from "vue-marquee-text-component";
import SendChat from './components/SendChat.vue'
import ReceiveChat from './components/ReceiveChat.vue'

export default {
  name: "NavBar",

  components: {
    MarqueeText,
    SendChat,
    ReceiveChat,
  },

  props: {},
  data() {
    return {
      selectedMenu: "online", //네비바에서 현재 선택된 메뉴 => 온라인유저 보기가 디폴트
      musicName: "November Rain",
      artistName: "Guns & Roses",
      isMusicOverflow: false,
      isArtistOverflow: false, //아티스트 이름이 상위 div보다 클때 true
      isOnlineUnfold: true, //온라인 유저 목록 펼쳐져있는지
      isGuestUnfold: true, //비회원 목록 펼쳐져있는지
    };
  },
  mounted() {
    this.musicOverflowValid();
    this.artistOverflowValid();
  },
  methods: {
    //다른 메뉴로 변경할때 호출되는 메소드
    changeMenu(select) {
      this.selectedMenu = select;
    },

    //노래제목이 상위 div에서 overflow되는지 파악하는 메소드
    musicOverflowValid() {
      if (
        this.$refs.musicNameTag.clientWidth <
        this.$refs.musicNameBox.clientWidth
      ) {
        this.isMusicOverflow = false;
      } else {
        this.isMusicOverflow = true;
      }
    },

    //아티스트이름이 상위 div에서 overflow되는지 파악하는 메소드
    artistOverflowValid() {
      if (
        this.$refs.artistNameTag.clientWidth <
        this.$refs.artistNameBox.clientWidth
      ) {
        this.isArtistOverflow = false;
      } else {
        this.isArtistOverflow = true;
      }
    },
  },
  watch: {
    musicName: function () {
      this.musicOverflowValid;
    },
    artistName: function () {
      this.artistOverflowValid;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./NavBar.scss";
</style>
