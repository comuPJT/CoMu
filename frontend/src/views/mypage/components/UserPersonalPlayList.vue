<template>
  <div class="user-personal-wrapper">
    <youtube
      v-if="
        this.personalPlayList.length > 0 &&
        (this.shareMusicView == 'list' || this.shareMusicView == 'search')
      "
      :video-id="
        (this.musicUrl = this.$youtube.getIdFromURL(
          this.personalPlayList[this.musicIndex].source
        ))
      "
      :player-vars="{start: 0, autoplay: 1}"
      @ended="endVideo()"
      style="display: none"
    ></youtube>
    <!-- 현재 재생중인 플레이리스트 -->
    <div class="modal-window" v-if="shareMusicView == 'list'">
      <!--현재 방 이름 / 모달 닫기 버튼-->
      <div class="modal-top">
        <p>내 재생 목록</p>
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

        <!--내 개인 플레이 리스트-->
        <div class="content-body">
          <table class="table">
            <!-- 클릭시 selectOnPlayList 배열에 아이디를 담는데 임시로 id라는 문자열을 담습니다-->
            <tr
              v-for="music in personalPlayList"
              :key="music.spotifyid"
              v-bind:class="{
                'selected-table':
                  music.spotifyId == personalPlayList[musicIndex].spotifyId,
              }"
              @click="changeMusic(music.spotifyId)"
            >
              <td class="content-body-td2">
                <img
                  v-if="
                    music.spotifyId == personalPlayList[musicIndex].spotifyId
                  "
                  src="@/assets/images/sound-wave.gif"
                />
              </td>
              <td class="content-body-td2">
                <img :src="music.thumbnail" />
              </td>
              <td class="content-body-td3">{{ music.name }}</td>
              <td class="content-body-td4">{{ music.singer }}</td>
              <td class="content-body-td5">{{ music.album }}</td>
              <td class="content-body-td6"></td>
            </tr>
          </table>
        </div>
        <!--내 개인 플레이 리스트 끝-->

        <!--리스트에 저장 / 신청하기 버튼-->
        <div class="playlist-button-wrapper">
          <div class="smallbuttonbrown">
            <div class="buttoncontent" @click="goEditPage()">편집</div>
          </div>
          <div class="smallbuttonwhite">
            <div class="buttoncontent" @click="shareMusicView = 'search'">
              곡 추가하기
            </div>
          </div>
        </div>
        <!--리스트에 저장 / 신청하기 버튼 끝-->
      </div>
    </div>
    <!-- 현재 재생중인 플레이리스트 끝-->

    <!-- 곡 검색화면 -->
    <div class="modal-window" v-if="shareMusicView == 'search'">
      <div class="modal-top">
        <p>곡 추가하기</p>
      </div>

      <div class="modal-content modal-content-search">
        <div class="search-input-wrapper">
          <input
            v-model="searchKeyword"
            class="search-input"
            placeholder="검색어를 입력해주세요!"
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
          <div class="search-result-exist" v-if="searchResultTitle.length != 0">
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
              <table class="table">
                <tr
                  v-for="titleResult in searchResultTitle"
                  :key="titleResult.id"
                  v-bind:class="{
                    'selected-table': titleResult.isselected,
                  }"
                >
                  <td class="content-body-td2">
                    <img :src="titleResult.album.images[1].url" />
                  </td>
                  <td class="content-body-td3">
                    {{ titleResult.name }}
                  </td>
                  <td class="content-body-td4">
                    {{ titleResult.artists }}
                  </td>
                  <td class="content-body-td5">
                    {{ titleResult.album.name }}
                  </td>
                  <td class="content-body-td6">
                    <img
                      src="@/assets/images/add_icon.svg"
                      @click="addToPersonal(titleResult)"
                    />
                  </td>
                </tr>
              </table>
            </div>
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

    <!--리스트편집-->
    <div class="modal-window" v-show="shareMusicView == 'edit'">
      <div class="modal-top">
        <p>재생목록 편집</p>
      </div>
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

        <!--내 개인 플레이 리스트-->
        <div class="content-body">
          <div class="table">
            <draggable
              v-model="personalPlayList"
              @start="drag = true"
              @end="drag = false"
              style="width: 100%; height: 100%"
            >
              <!-- 클릭시 selectOnPlayList 배열에 아이디를 담는데 임시로 id라는 문자열을 담습니다-->
              <div
                class="drag-tr"
                v-for="music in personalPlayList"
                :key="music.spotifyid"
              >
                <div class="content-body-td2 drag-td"></div>
                <div class="content-body-td2 drag-td">
                  <img :src="music.thumbnail" />
                </div>
                <div class="content-body-td3 drag-td">{{ music.name }}</div>
                <div class="content-body-td4 drag-td">{{ music.singer }}</div>
                <div class="content-body-td5 drag-td">{{ music.album }}</div>
                <div class="content-body-td6 drag-td">
                  <img
                    src="@/assets/images/close_button.svg"
                    style="width: 1.5vw; margin-right: 1vw"
                    @click="deleteMusic(music.spotifyId, music.id)"
                  />
                </div>
              </div>
            </draggable>
          </div>
        </div>
        <!--내 개인 플레이 리스트 끝-->

        <!--리스트에 저장 / 신청하기 버튼-->
        <div class="playlist-button-wrapper">
          <div class="smallbuttonwhite">
            <div class="buttoncontent" @click="saveChange()">완료</div>
          </div>
        </div>
        <!--리스트에 저장 / 신청하기 버튼 끝-->
      </div>
    </div>
    <!--리스트편집 끝-->
  </div>
</template>

<script>
import axios from "axios";
import youtubeAPI from "@/api/youtube";
import myPlayListApi from "@/api/myPlayList";
import draggable from "vuedraggable";

export default {
  name: "UserPersonalPlayList",

  components: {draggable},

  props: {},
  data() {
    return {
      showModal: false,
      shareMusicView: "list", //플레이리스트, 곡 검색, 곡 신청 화면중 어떤걸 표시하는지
      //공용-재생중인 음악목록
      personalPlayList: [],
      selectedMusicOnPlayList: [],
      //음악검색
      searchKeyword: "", //검색어
      searchResultTitle: [], //노래 검색결과 배열
      selectedMusicOnSearch: {}, //검색결과에서 선택된 음악
      //사연과 신청
      postcardTitle: "",
      postcardContent: "",
      musicIndex: 0,
      musicUrl: "",
    };
  },

  mounted() {
    const data = "";
    myPlayListApi.getPersonalPlayList(
      data,
      (res) => {
        console.log(res.data);
        this.personalPlayList = res.data;
      },
      (err) => {
        console.log(err);
      }
    );
  },

  methods: {
    async searchMusic(keyword) {
      //검색어로 음악을 검색합니다.
      if (keyword.length < 1) {
        this.$alert("1글자 이상의 검색어를 입력해주세요!");
      } else {
        const headers = {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("SPOTIFY_TOKEN"),
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        };

        await axios //awiat가 모듈화시키면 생각한대로 작동이 안되서 우선은 내부에 넣어놨습니다. 추후에 에러 해결하면 모듈화 할 예정입니다.
          .get(
            "https://api.spotify.com/v1/search?q=" + keyword + "&type=track",
            headers
          )
          .then((res) => {
            this.searchResultTitle = []; //검색하기 전 검색결과를 초기화
            this.selectedMusicOnSearch = {}; //선택된 노래또한 초기화
            //searchResultTitle에 바로 집어넣지않고 반복문으로 push한 이유는
            // 1. 필요한 데이터만 필터링하기 위해서
            //2.(중요) searchResultTitle=res.data 이렇게 바로 할당하면 신청할 노래를 선택하는과정에서 오류가 생깁니다... 이유는 모르겠지만 우선 이렇게 하는 방식으로 해결!

            for (var i = 0; i < res.data.tracks.items.length; i++) {
              var artistString = ""; //아티스트가 여러명일때 한 문자열로 합치기 위해 사용된 변수입니다.
              for (
                var j = 0;
                j < res.data.tracks.items[i].artists.length;
                j++
              ) {
                artistString = artistString.concat(
                  res.data.tracks.items[i].artists[j].name + ", "
                );
              }
              artistString = artistString.slice(0, -2);
              this.searchResultTitle.push({
                isselected: false,
                artists: artistString,
                name: res.data.tracks.items[i].name,
                album: res.data.tracks.items[i].album,
                id: res.data.tracks.items[i].id,
              });
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },

    goEditPage() {
      this.shareMusicView = "edit";
    },

    saveChange() {
      const data = {
        musicIdPlayOrderDtoList: [],
        userSeq: localStorage.getItem("userSeq"),
      };
      for (var i = 0; i < this.personalPlayList.length; i++) {
        data.musicIdPlayOrderDtoList.push({
          musicId: this.personalPlayList[i].id,
          playOrder: i,
        });
      }
      myPlayListApi.editPlayList(
        data,
        (res) => {
          console.log(res);
          this.$alert("리스트 수정이 완료되었습니다!.");
          this.shareMusicView = "list";
          this.musicIndex = 0;
        },
        (err) => {
          console.log(err);
        }
      );
    },

    deleteMusic(spotifyId, musicid) {
      const data = {
        musicIds: [musicid],
        userSeq: localStorage.getItem("userSeq"),
      };
      myPlayListApi.deletePersonal(
        data,
        (res) => {
          console.log(res);
          this.$alert("곡이 삭제되었습니다.");
        },
        (err) => {
          console.log(err);
        }
      );
      this.personalPlayList.splice(
        this.personalPlayList.findIndex(function (music) {
          return music.spotifyId === spotifyId;
        }),
        1
      );
    },

    async addToPersonal(titleResult) {
      var youtubesrc = "";
      youtubesrc += await youtubeAPI.getYouTubeUrl(
        titleResult.artists,
        titleResult.name
      );
      const data = {
        musicList: [
          {
            spotifyId: titleResult.id,
            name: titleResult.name,
            singer: titleResult.artists,
            source: youtubesrc,
            album: titleResult.album.name,
            thumbnail: titleResult.album.images[1].url,
          },
        ],
        userSeq: localStorage.getItem("userSeq"),
      };
      myPlayListApi.addPersonalPlayList(
        data,
        (res) => {
          this.personalPlayList.push({
            spotifyId: titleResult.id,
            name: titleResult.name,
            singer: titleResult.artists,
            source: youtubesrc,
            album: titleResult.album.name,
            thumbnail: titleResult.album.images[1].url,
          });
          console.log(res);
          this.$alert("곡이 추가되었습니다.");
        },
        (err) => {
          console.log(err);
        }
      );
    },

    endVideo() {
      if (this.musicIndex == this.personalPlayList.length - 1) {
        this.musicIndex = 0;
      } else {
        this.musicIndex++;
      }
    },

    changeMusic(spotifyId) {
      this.musicIndex = this.personalPlayList.findIndex(function (music) {
        return music.spotifyId === spotifyId;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UserPersonalPlayList.scss";
</style>
