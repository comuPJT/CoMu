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
              <div class="best-date">2021-11-17</div>
            </div>
            <div class="story-wrapper" @click="gotoDetail()">
              <img src="@/assets/images/tempcover1.jpg" />
              <div class="story-music">
                <div class="artist">제프버넷</div>
                <div class="title">콜유마인</div>
              </div>
              <div class="story-story">
                <div class="story-story-top">
                  <div>남편 생일이자 결혼기념일</div>
                  <div>우현2</div>
                </div>
                <div class="story-story-bottom">
                  친구는 세월의 도둑이다. 봄부터 흐르는 물은 겨울이 되어도 얼지
                  않듯이 마음에서 우러나오는 우적은 역경이 닥친다고 해서 식지
                  않는다. 불행했을 때 만난 친구는 가장 소중히 여겨야 한다.
                  행복했을
                </div>
              </div>
              <div class="story-like">
                <div class="heart">
                  <img src="@/assets/images/heart.svg" />
                  82
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
@import "./BestStory.scss";
</style>
