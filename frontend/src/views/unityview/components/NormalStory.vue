<template>
  <div>
    <transition name="modal" appear>
      <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-window">
          <!-- 사연 모아보기 타이틀 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>오늘의 사연함 - {{ this.$store.getters.themeName }}</p>
            <img
              src="@/assets/images/close_button.svg"
              @click.self="$emit('close')"
            />
          </div>
          <!-- 사연 모아보기 타이틀 / 모달 닫기 버튼 끝-->

          <!--사연 리스트-->
          <div class="modal-content" v-if="!isDetailView">
            <div class="normal-story-wrapper">
              <div class="normal-story-header">
                <div class="td1"></div>
                <div class="td2">제목</div>
                <div class="td3">작성자</div>
                <div class="td4">신청곡</div>
                <div class="td5"><img src="@/assets/images/heart.svg" /></div>
              </div>
              <div
                v-for="story in storyList"
                :key="story.playId"
                class="normal-story-content"
                @click="gotoDetail(story)"
              >
                <div class="td1"></div>
                <div class="td2">{{ story.title }}</div>
                <div class="td3">{{ story.username }}</div>
                <div class="td4">
                  <p class="title">{{ story.name }}</p>
                  <p class="artist">{{ story.singer }}</p>
                </div>
                <div class="td5">{{ story.likes }}</div>
              </div>
            </div>

            <div class="playlist-button-wrapper">
              <div class="smallbuttonbrown">
                <div class="buttoncontent" @click.self="$emit('close')">
                  이전
                </div>
              </div>
            </div>
          </div>
          <!--사연 리스트 끝-->

          <!--사연 상세보기-->
          <div class="modal-content-detail" v-if="isDetailView">
            <div class="modal-content-detail-wrapper">
              <div class="detail-header">
                <div>{{ story.title }}</div>
                <div class="detail-header-nickname">{{ story.username }}</div>
                <div class="detail-header-time">{{ story.timestamp }}</div>
              </div>
              <div class="detail-content">
                <div class="detail-content-top">
                  <img :src="story.thumbnail" />
                  <div class="detail-content-left">
                    <div class="detail-content-left-title">
                      {{ story.name }}
                    </div>
                    <div class="detail-content-left-artist">
                      {{ story.singer }}
                    </div>
                    <div class="detail-content-left-album">
                      {{ story.album }}
                    </div>
                  </div>
                </div>
                <div class="detail-content-bottom">
                  {{ story.contents }}
                </div>
              </div>
              <div class="detail-button-wrapper">
                <div class="detail-button-like">
                  <img
                    src="@/assets/images/heart_fill.svg"
                    @click="likeStory(story)"
                  />&nbsp;{{ story.likes }}
                </div>
                <div class="smallbuttonwhite">
                  <div class="buttoncontent" @click="isDetailView = false">
                    목록
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!--사연 상세보기 끝-->
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import shareApi from "@/api/share";
import storyApi from "@/api/story";

export default {
  name: "NormalStory",

  components: {},

  props: {},
  data() {
    return {
      isDetailView: false, //현재 사연 상세보기중인지
      storyList: [],
      story: {},
    };
  },
  mounted() {
    shareApi.getPublicPlayList(
      this.$store.getters.themeId, //몇번 플레이리스트인지
      (res) => {
        console.log(res.data);
        for (var i = 0; i < res.data.length; i++) {
          if (res.data[i].contents != "") {
            this.storyList.push(res.data[i]);
          }
        }
      },
      (err) => {
        console.log(err);
      }
    );
  },

  methods: {
    //클릭한 사연 상세보기
    gotoDetail(selectStory) {
      // const data = {
      //   storyId: story.playId,
      //   roomId: this.$store.getters.themeId,
      // };
      // this.isDetailView = true;
      // storyApi.getNormalStoryDetail(
      //   data,
      //   (res) => {
      //     console.log(res);
      //     this.story = res.data;
      //   },
      //   (err) => {
      //     console.log(err);
      //   }
      // );
      this.story = selectStory;
      this.isDetailView = true;
    },

    likeStory(story) {
      const data = {
        playId: story.playId,
        userId: localStorage.getItem("userSeq"),
      };
      storyApi.storyLike(
        data,
        (res) => {
          console.log(res);
          story.likes++;
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
@import "./NormalStory.scss";
</style>
