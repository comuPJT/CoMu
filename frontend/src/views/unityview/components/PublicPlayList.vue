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
                <tr v-for="music in sharePlayListMusic" :key="music.title" v-bind:class="{ 'selected-table' : music.isselected }" @click="[selectOnPlayList('id'), music.isselected=!music.isselected]">
                  <td class="content-body-td2">
                    <img
                      :src="music.cover"
                    />
                  </td>
                  <td class="content-body-td3">{{music.title}}</td>
                  <td class="content-body-td4">{{music.artist}}</td>
                  <td class="content-body-td5">{{music.album}}</td>
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
                <div class="buttoncontent" @click="shareMusicView = 'search'">
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
                    <tr v-for="titleResult in searchResultTitle" :key="titleResult.title">
                      <td class="content-body-td2">
                        <img
                          :src= "titleResult.cover"
                        />
                      </td>
                      <td class="content-body-td3">
                        {{titleResult.title}}
                      </td>
                      <td class="content-body-td4">{{titleResult.artist}}</td>
                      <td class="content-body-td5">{{titleResult.album}}</td>
                    </tr>
                  </table>
                </div>
              </div>
            </div>

            <div class="search-result-wrapper">
              <div class="search-result-title">
                아티스트({{ searchResultArtist.length }})
              </div>
              <div v-show="searchResultArtist.length == 0" class="none-result">
                검색 결과가 없습니다.
              </div>
              <div
                class="search-result-exist"
                v-if="searchResultArtist.length != 0"
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
                    <tr v-for="artistResult in searchResultArtist" :key="artistResult.title">
                      <td class="content-body-td2">
                        <img
                          :src= "artistResult.cover"
                        />
                      </td>
                      <td class="content-body-td3">
                        {{artistResult.title}}
                      </td>
                      <td class="content-body-td4">{{artistResult.artist}}</td>
                      <td class="content-body-td5">{{artistResult.album}}</td>
                    </tr>
                  </table>
                </div>
              </div>
            </div>
            <div class="playlist-button-wrapper music-applicate">
              <div class="smallbuttonbrown">
                <div class="buttoncontent">신청</div>
              </div>
              <div class="smallbuttonwhite">
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
      </div>
    </transition>
    <add-to-my-list v-if="showModal" @close="showModal = false">
      <h3 slot="header">custom header</h3>
    </add-to-my-list>
  </div>
</template>

<script>
import AddToMyList from "./AddToMyList.vue";
export default {
  name: "PublicPlayList",

  components: { AddToMyList },

  props: {},
  data() {
    return {
      showModal: false,
      shareMusicView: "list", //플레이리스트, 곡 검색, 곡 신청 화면중 어떤걸 표시하는지
      sharePlayListMusic:[], //현재 공용 플레이 리스트에서 재생중인 곡들
      selectedMusicOnPlayList:[],
      searchKeyword: "", //검색어
      searchResultTitle: [],
      searchResultArtist: [], //노래제목, 아티스트별 검색결과 배열
    };
  },

  mounted() {
    for(var i=0 ; i<21 ; i++){
      this.sharePlayListMusic.push({
            isselected:false,
            cover:
              "https://t1.daumcdn.net/thumb/R720x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/8fXh/image/0_JTh3JET7ZCHaT_IJhG4VbhEpI.png",
            title: "Insecure (Feat. Pink Sweat$)",
            artist: "Bren Joy",
            album: "Nothing Feels Better",
          });
    }
  },

  methods: {
    selectOnPlayList(musicId){ //공용뮤직리스트에서 클릭된 음악들을 배열(selectedMusicOnPlaylist)에 담기
      this.selectedMusicOnPlayList.push(musicId);
    },
    selectMusic(isselected){
      console.log(isselected);
      isselected=!isselected;
    },
    searchMusic(keyword) {
      //검색어로 음악을 검색합니다. 아래 반복문은 더미데이터 넣기용이라 추후 수정예정
      if (keyword.length < 2) {
        alert("2글자 이상의 검색어를 입력해주세요!");
      } else {
        for (var i = 0; i < 5; i++) {
          this.searchResultTitle.push({
            cover:
              "https://t1.daumcdn.net/thumb/R720x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/8fXh/image/0_JTh3JET7ZCHaT_IJhG4VbhEpI.png",
            title: "Insecure (Feat. Pink Sweat$)",
            artist: "Bren Joy",
            album: "Nothing Feels Better",
          });

          this.searchResultArtist.push({
            cover:
              "https://t1.daumcdn.net/thumb/R720x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/8fXh/image/0_JTh3JET7ZCHaT_IJhG4VbhEpI.png",
            title: "Insecure (Feat. Pink Sweat$)",
            artist: "Bren Joy",
            album: "Nothing Feels Better",
          });
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./PublicPlayList.scss";
</style>
