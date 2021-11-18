<template>
  <div @click="$emit('clickNavbar')">
    <video
      ref="video"
      width="100%"
      height="640"
      controls
      autoplay="autoplay"
      muted="muted"
      style="display: none"
    ></video>
    <!--네비바 좌측 메뉴선택부분-->
    <div class="nav-bar-left">
      <img class="logo-2X2" src="@/assets/images/logo_2X2.png" />
      <div class="menu_icons">
        <img
          src="@/assets/images/chat_icon.svg"
          class="menu_icon"
          :class="{'menu-selected': selectedMenu === 'chat'}"
          @click="changeMenu('chat')"
        />
        <img
          src="@/assets/images/letter_icon.svg"
          class="menu_icon"
          :class="{'menu-selected': selectedMenu === 'online'}"
          @click="changeMenu('online')"
        />
        <img
          v-if="!isMute"
          src="@/assets/images/mute-icon.png"
          class="menu_icon"
          @click="mute()"
        />
        <img
          v-if="isMute"
          src="@/assets/images/unmute-icon.png"
          class="menu_icon"
          @click="mute()"
        />
        <img
          src="@/assets/images/mypage_icon.svg"
          class="menu_icon"
          :class="{'menu-selected': selectedMenu === 'mypage'}"
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
      <div class="room-title">{{ this.$store.getters.themeName }}</div>
      <!--현재 재생중인 음악 끝-->
      <!--접속중인 유저 목록-->
      <div v-show="selectedMenu == 'online'">
        <div class="user-list-box">
          <div class="story-wrapper">
            <div class="story-wrapper-box">
              <div
                class="story-wrapper-box-top"
                style="width: 100%; height: 40%"
              >
                <img src="@/assets/images/tempcover1.jpg" />
                <div class="title">
                  <div class="title-title">노래제목</div>
                  <div class="title-artist">가수이름</div>
                </div>
              </div>

              <div class="story-wrapper-box-bottom">
                <div class="story-title">
                  사연제목사연제목사연제목사연제목사연제목사연제목사연제목사연제목사연제목
                </div>
                <div class="story-content">
                  내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--접속중인 유저 목록 끝-->

      <!--채팅-->
      <div v-show="selectedMenu == 'chat'">
        <div id="container" class="chat-box" v-chat-scroll>
          <!--채팅 UI테스트-->
          <div class="chat-item" v-for="chat in chats" :key="chat.key">
            <div>
              <div v-if="chat.sendDate > curDate">
                <send-chat
                  v-if="chat.user == idChat"
                  :message="chat.message"
                  :nickname="chat.userName"
                  :time="chat.sendDate"
                  :img="chat.img ? chat.img : 0"
                ></send-chat>
                <receive-chat
                  v-if="chat.user != idChat"
                  :message="chat.message"
                  :nickname="chat.userName"
                  :time="chat.sendDate"
                  :img="chat.img ? chat.img : 0"
                ></receive-chat>
              </div>
            </div>
          </div>
          <!--채팅 UI테스트 끝-->
        </div>

        <div class="message-send-wrapper">
          <div class="input_box inputbox-none-title" style="width: 14vw">
            <input
              id="message"
              v-model="inputChat"
              placeholder="메시지를 입력해주세요!"
              @keypress.enter="onSubmit"
            />
          </div>
          <img
            type="submit"
            src="@/assets/images/message_send_icon.svg"
            @click="onSubmit"
          />
        </div>
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
import SendChat from "./components/SendChat.vue";
import ReceiveChat from "./components/ReceiveChat.vue";
import {mapMutations} from "vuex";
import firebase from "firebase";
import Hls from "hls.js";
import storyApi from "@/api/story";

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
      //채팅관련
      inputChat: "",
      idChat: "",
      nickNameChat: "",
      curDate: "",
      roomid: this.$route.params.roomid,
      data: {type: "", nickname: "", message: ""},
      chats: [],
      errors: [],
      offStatus: false,
      roomName: "",
      chatRoomId: "-MoYW1WEd9p5xM_OxIDz",
      isMute: true,
      story: [],
    };
  },

  created() {
    this.idChat = localStorage.getItem("userSeq");
    this.nickNameChat = localStorage.getItem("userNickname");
    firebase
      .database()
      .ref("chatrooms/" + this.chatRoomId + "/chats")
      .on("value", (snapshot) => {
        this.chats = [];
        snapshot.forEach((doc) => {
          let item = doc.val();
          item.key = doc.key;
          this.chats.push(item);
        });
      });
  },

  mounted() {
    //스트리밍
    let hls = new Hls();
    let stream =
      "http://k5a304.p.ssafy.io:8234/hls/" +
      this.$store.getters.themeId +
      "/music.m3u8";

    //let stream = "http://k5a304.p.ssafy.io:8234/hls/" + "1" + "/music.m3u8";

    // let stream =
    //   "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8";

    let video = this.$refs["video"];
    hls.loadSource(stream);
    hls.attachMedia(video);
    //스트리밍

    this.musicOverflowValid();
    this.artistOverflowValid();
    setInterval(this.fetchRoom, 100);

    storyApi.getMormalStory(
      this.$store.getters.themeId,
      (res) => {
        console.log(res.data);
        this.story = res.data;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  updated() {
    this.scrollToBottom();
  },
  methods: {
    ...mapMutations(["setThemeName"]),
    ...mapMutations(["setThemeId"]),
    fetchRoom() {
      this.roomName = localStorage.getItem("roomName");
    },

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

    //채팅방초기화
    initChatRoom() {
      firebase
        .database()
        .ref("chatrooms/" + this.chatRoomId + "/chats")
        .on("value", (snapshot) => {
          this.chats = [];
          snapshot.forEach((doc) => {
            let item = doc.val();
            item.key = doc.key;
            this.chats.push(item);
          });
        });
    },

    //채팅 보내기
    onSubmit(evt) {
      if (localStorage.getItem("userType") == "user") {
        if (this.inputChat.length > 0) {
          evt.preventDefault();
          let newData = firebase
            .database()
            .ref("chatrooms/" + this.chatRoomId + "/chats")
            .push();
          newData.set({
            type: "newmsg",
            user: localStorage.getItem("userSeq"),
            userName: localStorage.getItem("userNickname"),
            message: this.inputChat,
            img: localStorage.getItem("characterNum"),
            sendDate: this.$moment().format("MM-DD HH:mm:ss"),
          });
          this.inputChat = "";
        }
      } else {
        this.$alert("로그인 후 이용해주세요.");
      }
    },

    //항상 채팅 맨 아래에 오도록 스크롤 조절
    scrollToBottom() {
      const container = this.$el.querySelector("#container");
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    },

    mute() {
      let video = this.$refs["video"];
      if (this.isMute) {
        video.muted = false;
      } else {
        video.muted = true;
      }
      video.play();
      this.isMute = !this.isMute;
    },

    setStream() {
      let hls = new Hls();
      let stream =
        "http://k5a304.p.ssafy.io:8234/hls/" +
        this.$store.getters.themeId +
        "/music.m3u8";

      //let stream = "http://k5a304.p.ssafy.io:8234/hls/" + "1" + "/music.m3u8";

      // let stream =
      //   "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8";

      let video = this.$refs["video"];
      hls.loadSource(stream);
      hls.attachMedia(video);
    },
  },
  watch: {
    musicName: function () {
      this.musicOverflowValid;
    },
    artistName: function () {
      this.artistOverflowValid;
    },
    roomName: function () {
      if (this.roomName === "Main") {
        this.setThemeName("메인공간");
        this.setThemeId(1);
        this.chatRoomId = "-MoYW1WEd9p5xM_OxIDz";
      } else if (this.roomName === "Theme1") {
        this.setThemeName("별이빛나는 낮에 30.4FM");
        this.setThemeId(3);
        this.chatRoomId = "-MoYW38qSow1G3SNIYH3";
      } else if (this.roomName === "Theme2") {
        this.setThemeName("내적 댄스 유발");
        this.setThemeId(4);
        this.chatRoomId = "-MoYW40D_W8h5lLMpoge";
      } else if (this.roomName === "Theme3") {
        this.setThemeName("코뮤-다방");
        this.setThemeId(2);
        this.chatRoomId = "-MoYW4eYGD_57sLRCrTP";
      } else if (this.roomName === "Theme4") {
        this.setThemeName("멀캠도서관");
        this.setThemeId(6);
        this.chatRoomId = "-MoYW5hC1bb9mXtR_gjA";
      } else if (this.roomName === "Theme5") {
        this.setThemeName("COMU CAFE");
        this.setThemeId(5);
        this.chatRoomId = "-MoYW6JDoLl-kmso69ax";
      } else if (this.roomName === "MyRoom") {
        this.setThemeName("마이룸");
        this.chatRoomId = "-MoYW1WEd9p5xM_OxIDz";
      }
      this.initChatRoom();
      this.setStream();
      this.curDate = this.$moment().format("MM-DD HH:mm:ss");
      storyApi.getMormalStory(
        this.$store.getters.themeId,
        (res) => {
          console.log(res.data);
          this.story = res.data;
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
@import "./NavBar.scss";
</style>
