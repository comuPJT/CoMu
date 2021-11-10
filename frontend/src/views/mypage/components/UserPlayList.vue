<template>
  <div class="play-list-wrapper">
    <div class="play-list-table">
      <!--수납장의 흰 줄-->
      <img class="line-1" src="@/assets/images/table-line.png" />
      <img class="line-2" src="@/assets/images/table-line.png" />
      <img class="line-3" src="@/assets/images/table-line.png" />

      <!--각각의 플레이 리스트-->
      <div class="play-list-detail">
        <div class="play-list-detail-title">플레이리스트1</div>
        <img class="img-1" src="@/assets/images/lp_now_playing.png" />
        <img class="img-2" src="@/assets/images/tempcover1.jpg" />
        <img class="img-3" src="@/assets/images/tempcover2.jpg" />
        <img class="img-4" src="@/assets/images/tempcover3.jpg" />
        <img class="img-5" src="@/assets/images/tempcover4.jpg" />
      </div>
      
      <!--각각의 플레이 리스트 끝-->
    </div>
  </div>
</template>

<script>
export default {
  name: "UserPlayList",

  components: {},

  props: {},
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./UserPlayList.scss";
</style>
