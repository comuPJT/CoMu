<template>
  <transition name="modal" appear>
    <div class="modal-overlay" @click.self="$emit('close')">
      <div class="modal-window">
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
              <tr v-for="i in 40" :key="i">
                <td class="content-body-td1">
                  <img src="@/assets/images/like_button.svg" />
                </td>
                <td class="content-body-td2">
                  <img
                    src="https://t1.daumcdn.net/thumb/R720x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/8fXh/image/0_JTh3JET7ZCHaT_IJhG4VbhEpI.png"
                  />
                </td>
                <td class="content-body-td3">Insecure (Feat. Pink Sweat$)</td>
                <td class="content-body-td4">Bren Joy</td>
                <td class="content-body-td5">Nothing Feels Better</td>
              </tr>
            </table>
          </div>
          <!--신청곡 리스트 끝-->

          <!--리스트에 저장 / 신청하기 버튼-->
          <div class="playlist-button-wrapper">
            <div class="smallbuttonbrown">
              <div class="buttoncontent">내 음악리스트에 저장</div>
            </div>
            <div class="smallbuttonwhite">
              <div class="buttoncontent">새로운 곡 신청하기</div>
            </div>
          </div>
          <!--리스트에 저장 / 신청하기 버튼 끝-->
        </div>
      </div>
    </div>
  </transition>
</template>
<script></script>

<style lang="scss" scoped>
@import "./PublicPlayList.scss";
</style>
