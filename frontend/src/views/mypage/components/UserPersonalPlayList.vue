<template>
  <div class="user-personal-wrapper">
    <!-- 현재 재생중인 플레이리스트 -->
    <div class="modal-window" v-show="shareMusicView == 'list'">
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
          <table>
            <!-- 클릭시 selectOnPlayList 배열에 아이디를 담는데 임시로 id라는 문자열을 담습니다-->
            <tr
              v-for="music in personalPlayList"
              :key="music.id"
              v-bind:class="{'selected-table': music.isselected}"
            >
              <td class="content-body-td2">
                <img :src="music.thumbnail" />
              </td>
              <td class="content-body-td3">{{ music.name }}</td>
              <td class="content-body-td4">{{ music.singer }}</td>
              <td class="content-body-td5">{{ music.album }}</td>
              <td class="content-body-td6">
                <img
                  src="@/assets/images/close_button.svg"
                  style="width: 1.5vw; margin-right: 1vw"
                  @click="deletePersonal(music.id)"
                />
              </td>
            </tr>
          </table>
        </div>
        <!--내 개인 플레이 리스트 끝-->

        <!--리스트에 저장 / 신청하기 버튼-->
        <div class="playlist-button-wrapper">
          <div class="smallbuttonbrown">
            <div class="buttoncontent">편집(구현예정)</div>
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
              <table>
                <tr
                  v-for="titleResult in searchResultTitle"
                  :key="titleResult.id"
                  v-bind:class="{
                    'selected-table': titleResult.isselected,
                  }"
                >
                  <td class="content-body-td2">
                    <img :src="titleResult.album.images[2].url" />
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
  </div>
</template>

<script>
import axios from "axios";
import youtubeAPI from "@/api/youtube";
import myPlayListApi from "@/api/myPlayList";
export default {
  name: "UserPersonalPlayList",

  components: {},

  props: {},
  data() {
    return {
      showModal: false,
      shareMusicView: "list", //플레이리스트, 곡 검색, 곡 신청 화면중 어떤걸 표시하는지
      //공용-재생중인 음악목록
      personalPlayList: [], //현재 공용 플레이 리스트에서 재생중인 곡들
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
    const data = "흐으음";
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
        alert("1글자 이상의 검색어를 입력해주세요!");
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
            thumbnail: titleResult.album.images[2].url,
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
            thumbnail: titleResult.album.images[2].url,
          });
          console.log(res);
          alert("곡이 추가되었습니다.");
        },
        (err) => {
          console.log(err);
        }
      );
    },

    deletePersonal(id) {
      const data = {
        musicIds: [id],
        userSeq: localStorage.getItem("userSeq"),
      };
      myPlayListApi.deletePersonal(
        data,
        (res) => {
          var index;
          for (var i = 0; i < this.personalPlayList.length; i++) {
            if (this.personalPlayList[i].id == id) {
              index = i;
              break;
            }
          }
          this.$delete(this.personalPlayList, index);
          console.log(res);
          alert("곡이 삭제되었습니다.");
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
@import "./UserPersonalPlayList.scss";
</style>
