<template>
  <div>
    <transition name="modal" appear>
      <div class="modal-overlay" @click.self="$emit('close')">
        <!-- 현재 재생중인 플레이리스트 -->
        <div class="modal-window">
          <!--현재 방 이름 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>{{ this.$store.getters.themeName }}</p>
            <img
              src="@/assets/images/close_button.svg"
              @click.self="$emit('close')"
            />
          </div>
          <!--현재 방 이름 / 모달 닫기 버튼 끝-->

          <div class="modal-content">
            <!--신청곡 리스트 헤더 => 스크롤때문에 th안쓰고 따로작성-->
            <div class="content-header">
              <div class="content-header-1">
                <div class="header-text">곡 제목</div>
              </div>
              <div class="content-header-2">
                <div class="header-text">아티스트</div>
              </div>
              <div class="content-header-3">
                <div class="header-text">앨범</div>
              </div>
            </div>
            <!--신청곡 리스트 헤더 끝-->

            <!--신청곡 리스트-->
            <div class="content-body">
              <table>
                <!-- 클릭시 selectOnPlayList 배열에 아이디를 담는데 임시로 id라는 문자열을 담습니다-->
                <tr
                  v-for="music in sharePlayListMusic"
                  :key="music.playId"
                  v-bind:class="{
                    'selected-table': music.playId == clickIndex,
                  }"
                  @click="
                    [
                      selectOnPlayList(music),
                      (music.isselected = !music.isselected),
                      (clickIndex = music.playId),
                    ]
                  "
                >
                  <td class="content-body-td2">
                    <img :src="music.thumbnail" />
                  </td>
                  <td class="content-body-td3">{{ music.name }}</td>
                  <td class="content-body-td4">{{ music.singer }}</td>
                  <td class="content-body-td5">{{ music.album }}</td>
                </tr>
              </table>
            </div>
            <!--신청곡 리스트 끝-->

            <!--리스트에 저장 / 신청하기 버튼-->
            <div class="playlist-button-wrapper">
              <div class="smallbuttonbrown">
                <div class="buttoncontent" @click="showModal = true">
                  내 음악리스트에 저장
                </div>
              </div>
            </div>
            <!--리스트에 저장 / 신청하기 버튼 끝-->
          </div>
        </div>
        <!-- 현재 재생중인 플레이리스트 끝-->
      </div>
    </transition>
    <add-to-my-list v-if="showModal" @close="showModal = false">
      <h3 slot="header">custom header</h3>
    </add-to-my-list>
  </div>
</template>

<script>
import AddToMyList from "./AddToMyList.vue";
import shareApi from "@/api/share";

export default {
  name: "PublicPlayList",

  components: {AddToMyList},

  props: {},

  data() {
    return {
      showModal: false,
      //공용-재생중인 음악목록
      sharePlayListMusic: [], //현재 공용 플레이 리스트에서 재생중인 곡들
      selectedMusicOnPlayList: {}, //공용플레이리스트에서 내 리스트로 옮기기 위해 선택한 곡
      clickIndex: "",
    };
  },

  mounted() {
    shareApi.getPublicPlayList(
      this.$store.getters.themeId, //몇번 플레이리스트인지
      (res) => {
        //res.data가 배열
        console.log(res);
        this.sharePlayListMusic = res.data;
      },
      (err) => {
        console.log(err);
      }
    );
  },

  methods: {
    selectOnPlayList(music) {
      //공용뮤직리스트에서 클릭된 음악들을 배열(selectedMusicOnPlaylist)에 담기
      this.selectedMusicOnPlayList = music;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./PublicPlayList.scss";
</style>
