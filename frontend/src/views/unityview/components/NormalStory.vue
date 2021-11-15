<template>
  <div>
    <transition name="modal" appear>
      <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-window">
          <!-- 사연 모아보기 타이틀 / 모달 닫기 버튼-->
          <div class="modal-top">
            <p>오늘의 사연함</p>
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
                <div class="td1">No.</div>
                <div class="td2">제목</div>
                <div class="td3">작성자</div>
                <div class="td4">신청곡</div>
                <div class="td5"><img src="@/assets/images/heart.svg" /></div>
              </div>
              <div
                v-for="k in 15"
                :key="k.id"
                class="normal-story-content"
                @click="gotoDetail()"
              >
                <div class="td1">{{ k }}</div>
                <div class="td2">퇴근 전에 꼭꼭! 들어야할 것 같아요</div>
                <div class="td3">dnguszz</div>
                <div class="td4">
                  <p class="title">노래제목</p>
                  <p class="artist">아티스트</p>
                </div>
                <div class="td5">82</div>
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
                <div>중간고사까지 3일이 남았어요!</div>
                <div class="detail-header-nickname">oneway</div>
                <div class="detail-header-time">2021-10-27 10:16:50</div>
              </div>
              <div class="detail-content">
                <div class="detail-content-top">
                  <img src="@/assets/images/tempcover1.jpg" />
                  <div class="detail-content-left">
                    <div class="detail-content-left-title">
                      행복했던 날들이었다
                    </div>
                    <div class="detail-content-left-artist">DAY6</div>
                    <div class="detail-content-left-album">Best Album</div>
                  </div>
                </div>
                <div class="detail-content-bottom">
                  안녕하세요! 현역 고3 입니다! 벌써 고3 막바지를 달리고 있는데
                  고 3이라는 것도 믿기지 않고 곧 학교를 졸업하는 것도 믿기지
                  않아요ㅠㅠ 그동안 학교를 다니면서 10대 동안 행복했고
                  즐거웠다는 의미로 DAY6의 행복했던 날들이었다 신청합니다! :)
                </div>
              </div>
              <div class="detail-button-wrapper">
                <div class="detail-button-like">
                  <img src="@/assets/images/heart_unfill.svg" />&nbsp;82
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
export default {
  name: "NormalStory",

  components: {},

  props: {},
  data() {
    return {
      isDetailView: false, //현재 사연 상세보기중인지
    };
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
@import "./NormalStory.scss";
</style>
