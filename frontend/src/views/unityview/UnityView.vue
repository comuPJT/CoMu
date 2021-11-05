<template>
  <div>
    <nav-bar></nav-bar>
    <!-- 유니티 화면 임시-->
    <div
      style="
        float: right;
        width: 76vw;
        height: 100vh;
        background-repeat: no-repeat;
        background-size: 76vw 100vh;
        background-image: url(https://sm.ign.com/ign_ap/screenshot/default/screenshot-gathertown-20210709-17-56-26fffd_jx1f.png);
      "
    >
      <button id="show-modal" @click="showModal = true">
        공용플레이리스트 띄우기
      </button>
      <public-play-list v-if="showModal" @close="showModal = false">
        <h3 slot="header">custom header</h3>
      </public-play-list>
    </div>
  </div>
</template>

<script>
import PublicPlayList from "./components/PublicPlayList.vue";

export default {
  name: "UnityView",

  components: { PublicPlayList },

  props: {},
  data() {
    return {
      showModal: false,
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped></style>
