<template>
  <div>
    <transition name="modal" appear>
      <div class="modal-overlay" @click.self="$emit('close')">
        <!-- 현재 재생중인 플레이리스트 -->
        <div class="modal-window" v-show="shareMusicView == 'list'">
          <!--현재 방 이름 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>별이빛나는낮에 30.4FM</p>
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
                  :key="music.id"
                  v-bind:class="{'selected-table': music.isselected}"
                  @click="
                    [
                      selectOnPlayList('id'),
                      (music.isselected = !music.isselected),
                    ]
                  "
                >
                  <td class="content-body-td2">
                    <img :src="music.cover" />
                  </td>
                  <td class="content-body-td3">{{ music.title }}</td>
                  <td class="content-body-td4">{{ music.artist }}</td>
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
              <div class="smallbuttonwhite">
                <div
                  class="buttoncontent"
                  @click="[(shareMusicView = 'search'), getSpotifyToken()]"
                >
                  새로운 곡 신청하기
                </div>
              </div>
            </div>
            <!--리스트에 저장 / 신청하기 버튼 끝-->
          </div>
        </div>
        <!-- 현재 재생중인 플레이리스트 끝-->

        <!-- 곡 검색화면 -->
        <div class="modal-window" v-if="shareMusicView == 'search'">
          <!--현재 방 이름 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>곡 신청하기</p>
            <img
              src="@/assets/images/close_button.svg"
              @click.self="$emit('close')"
            />
          </div>
          <!--현재 방 이름 / 모달 닫기 버튼 끝-->

          <div class="modal-content modal-content-search">
            <div class="search-input-wrapper">
              <input
                v-model="searchKeyword"
                class="search-input"
                placeholder="검색어를 입력하세요."
                @keyup.enter="searchMusic(searchKeyword)"
              />
              <img
                class="search-input-buttom"
                src="@/assets/images/search_button.svg"
                @click="searchMusic(searchKeyword)"
              />
            </div>
            <div class="search-result-wrapper">
              <div class="search-result-title">
                곡 ({{ searchResultTitle.length }})
              </div>
              <div v-show="searchResultTitle.length == 0" class="none-result">
                검색 결과가 없습니다.
              </div>
              <div
                class="search-result-exist"
                v-if="searchResultTitle.length != 0"
              >
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
                <div class="content-body">
                  <table>
                    <tr
                      v-for="titleResult in searchResultTitle"
                      :key="titleResult.id"
                      v-bind:class="{
                        'selected-table': titleResult.isselected,
                      }"
                      @click="selectOnSearhResult(titleResult.id)"
                    >
                      <td class="content-body-td2">
                        <img :src="titleResult.album.images[2].url" />
                      </td>
                      <td class="content-body-td3">
                        {{ titleResult.name }}
                      </td>
                      <td v-show="titleResult.isselected">트루</td>
                      <td v-show="!titleResult.isselected">폴즈</td>
                      <td class="content-body-td4">
                        {{ titleResult.artists[0].name }}
                      </td>
                      <td class="content-body-td5">
                        {{ titleResult.album.name }}
                      </td>
                    </tr>
                  </table>
                </div>
              </div>
            </div>
            <div class="playlist-button-wrapper music-applicate">
              <div class="smallbuttonbrown">
                <div class="buttoncontent">신청</div>
              </div>
              <div class="smallbuttonwhite" @click="moveToPostCard">
                <div class="buttoncontent">사연과 함께 신청</div>
              </div>
            </div>

            <div class="playlist-button-wrapper playlist-button-wrapper-left">
              <div class="smallbuttonwhite" @click="shareMusicView = 'list'">
                <div class="buttoncontent">이전</div>
              </div>
            </div>
          </div>
        </div>
        <!-- 곡 검색화면 끝-->

        <!--사연과 함께 음악 신청화면-->
        <div class="modal-window" v-if="shareMusicView == 'postcard'">
          <!--현재 방 이름 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>곡 신청하기</p>
            <img
              src="@/assets/images/close_button.svg"
              @click.self="$emit('close')"
            />
          </div>
          <!--현재 방 이름 / 모달 닫기 버튼 끝-->

          <div class="modal-content">
            <!--신청곡 리스트-->
            <div class="postcard-margin"></div>
            <div class="content-body postcard-wrapper">
              <div class="postcard-body">
                <div class="smallbuttonbeige">
                  <div class="buttoncontent">POST CARD</div>
                </div>
                <img :src="selectedMusicOnSearch.album.images[0].url" />
                <div class="postcard-text-wrapper">
                  <div class="postcard-text-left">
                    <div class="input_box postcard-title">
                      <div class="title">제목</div>
                      <input
                        v-model="postcardTitle"
                        placeholder="제목을 입력해주세요."
                      />
                    </div>

                    <div class="input_box postcard-content">
                      <div class="title">내용</div>
                      <textarea
                        v-model="postcardContent"
                        placeholder="내용을 입력해주세요."
                      />
                    </div>
                  </div>
                  <div class="postcard-text-right">
                    <div class="postcard-line">
                      {{ selectedMusicOnSearch.artists[0].name }}
                    </div>
                    <div class="postcard-line">
                      {{ selectedMusicOnSearch.name }}
                    </div>
                    <div class="postcard-line">usernickname</div>
                    <div class="postcard-line">&nbsp;</div>
                    <div class="postcard-line">&nbsp;</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="playlist-button-wrapper">
              <div class="smallbuttonbrown">
                <div class="buttoncontent" @click="shareMusicView = 'search'">
                  이전
                </div>
              </div>
              <div class="smallbuttonwhite">
                <div class="buttoncontent">신청</div>
              </div>
            </div>
            <!--신청곡 리스트 끝-->
          </div>
        </div>
        <!--사연과 함께 음악 신청화면끝-->
      </div>
    </transition>
    <add-to-my-list v-if="showModal" @close="showModal = false">
      <h3 slot="header">custom header</h3>
    </add-to-my-list>
  </div>
</template>

<script>
import AddToMyList from "./AddToMyList.vue";
import Spotify from "@/api/spotify.js";
import axios from "axios";
export default {
  name: "PublicPlayList",

  components: {AddToMyList},

  props: {},
  data() {
    return {
      showModal: false,
      shareMusicView: "list", //플레이리스트, 곡 검색, 곡 신청 화면중 어떤걸 표시하는지
      //공용-재생중인 음악목록
      sharePlayListMusic: [], //현재 공용 플레이 리스트에서 재생중인 곡들
      selectedMusicOnPlayList: [], //공용플레이리스트에서 내 리스트로 옮기기 위해 선택한 곡"들"
      //음악검색
      searchKeyword: "", //검색어
      searchResultTitle: [], //노래 검색결과 배열
      selectedMusicOnSearch: {}, //검색결과에서 선택된 음악
      //사연과 신청
      postcardTitle: "",
      postcardContent: "",
    };
  },

  mounted() {
    for (var i = 0; i < 21; i++) {
      this.sharePlayListMusic.push({
        isselected: false,
        id: i + 99,
        cover:
          "https://t1.daumcdn.net/thumb/R720x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/8fXh/image/0_JTh3JET7ZCHaT_IJhG4VbhEpI.png",
        title: "Insecure (Feat. Pink Sweat$)",
        artist: "Bren Joy",
        album: "Nothing Feels Better",
      });
    }
  },

  methods: {
    selectOnPlayList(musicId) {
      //공용뮤직리스트에서 클릭된 음악들을 배열(selectedMusicOnPlaylist)에 담기
      this.selectedMusicOnPlayList.push(musicId);
    },

    selectMusic(isselected) {
      console.log(isselected);
      isselected = !isselected;
    },

    getSpotifyToken() {
      //스포티파이 api키를 발급합니다. 현재는 검색창 띄울때마다 요청하는데 추후에 로그인시 한번만 요청하도록 위치변경할 예정
      Spotify.spotifyAccess();
    },
    async searchMusic(keyword) {
      //검색어로 음악을 검색합니다. 아래 반복문은 더미데이터 넣기용이라 추후 수정예정
      if (keyword.length < 2) {
        alert("2글자 이상의 검색어를 입력해주세요!");
      } else {
        //this.searchResultTitle=await Spotify.searchMusic(keyword);
        const headers = {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("SPOTIFY_TOKEN"),
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        };

        await axios
          .get(
            "https://api.spotify.com/v1/search?q=" + keyword + "&type=track",
            headers
          )
          .then((res) => {
            this.searchResultTitle = res.data.tracks.items;
          })
          .catch((err) => {
            console.log(err);
          });

        for (var i = 0; i < this.searchResultTitle.length; i++) {
          this.$delete(this.searchResultTitle[i], "explicit");
          this.$delete(this.searchResultTitle[i], "available_markets");
          this.$delete(this.searchResultTitle[i], "disc_number");
          this.$delete(this.searchResultTitle[i], "duration_ms");
          this.$delete(this.searchResultTitle[i], "external_ids");
          this.$delete(this.searchResultTitle[i], "external_urls");
          this.$delete(this.searchResultTitle[i], "href");
          this.$delete(this.searchResultTitle[i], "is_local");
          this.$delete(this.searchResultTitle[i], "popularity");
          this.$delete(this.searchResultTitle[i], "preview_url");
          this.$delete(this.searchResultTitle[i], "track_number");
          this.$delete(this.searchResultTitle[i], "type");
          this.$delete(this.searchResultTitle[i], "uri");
          this.searchResultTitle[i].isselected = false;
        }
      }
    },

    selectOnSearhResult(id) {
      for (var i = 0; i < this.searchResultTitle.length; i++) {
        if (this.searchResultTitle[i].id != id) {
          this.searchResultTitle[i].isselected = false;
        } else {
          this.searchResultTitle[i].isselected = true;
          this.selectedMusicOnSearch = this.searchResultTitle[i];
        }
      }
    },

    moveToPostCard() {
      Object.keys(this.selectedMusicOnSearch).length;
      if (Object.keys(this.selectedMusicOnSearch).length === 0) {
        alert("곡을 선택해");
      } else {
        this.shareMusicView = "postcard";
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./PublicPlayList.scss";
</style>
