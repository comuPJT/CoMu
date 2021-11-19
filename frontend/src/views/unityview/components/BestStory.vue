<template>
  <div>
    <transition name="modal" appear>
      <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-window">
          <!-- 사연 모아보기 타이틀 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>명예의 전당</p>
            <img
              src="@/assets/images/close_button.svg"
              @click.self="$emit('close')"
            />
          </div>
          <!-- 사연 모아보기 타이틀 / 모달 닫기 버튼 끝-->

          <!--사연 리스트-->
          <div class="modal-content" v-if="!isDetailView">
            <div class="best-date-wrapper">
              <div class="best-date"></div>
            </div>
            <div
              v-for="honor in honorStory"
              :key="honor.playId"
              class="story-wrapper"
              @click="gotoDetail()"
            >
              <img src="@/assets/images/tempcover1.jpg" />
              <div class="story-music">
                <div class="artist">{{ honor.singer }}</div>
                <div class="title">{{ honor.name }}</div>
              </div>
              <div class="story-story">
                <div class="story-story-top">
                  <div>{{ honor.title }}</div>
                  <div>{{ honor.username }}</div>
                </div>
                <div class="story-story-bottom">
                  {{ honor.content }}
                </div>
              </div>
              <div class="story-like">
                <div class="heart">
                  <img src="@/assets/images/heart.svg" />
                  {{ honor.likes }}
                </div>
              </div>
            </div>
          </div>
          <!--사연 리스트 끝-->

          <!--사연 상세보기-->
          <div class="modal-content-detail" v-if="isDetailView">
            <div class="modal-content-detail-wrapper">
              <div class="detail-header">
                <div></div>
                <div class="detail-header-nickname"></div>
                <div class="detail-header-time"></div>
              </div>
              <div class="detail-content">
                <div class="detail-content-top">
                  <img src="@/assets/images/tempcover1.jpg" />
                  <div class="detail-content-left">
                    <div class="detail-content-left-title"></div>
                    <div class="detail-content-left-artist"></div>
                    <div class="detail-content-left-album"></div>
                  </div>
                </div>
                <div class="detail-content-bottom"></div>
              </div>
              <div class="detail-button-wrapper">
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
import storyApi from "@/api/story";

export default {
  name: "NormalStory",

  components: {},

  props: {},
  data() {
    return {
      isDetailView: false, //현재 사연 상세보기중인지
      honorStory: [],
    };
  },

  mounted() {
    storyApi.getHonorList(
      (res) => {
        console.log(res.data);
        this.honorStory = res.data;
      },
      (err) => {
        console.log(err);
      }
    );
  },

  methods: {
    gotoDetail() {
      //클릭한 사연 상세보기
      this.isDetailView = true;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./BestStory.scss";
</style>
