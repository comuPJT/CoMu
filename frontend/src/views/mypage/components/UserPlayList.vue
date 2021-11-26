<template>
  <div class="play-list-wrapper">
    <div class="play-list-table">
      <!--수납장의 흰 줄-->
      <img class="line-1" src="@/assets/images/table-line.png" />
      <img class="line-2" src="@/assets/images/table-line.png" />
      <img class="line-3" src="@/assets/images/table-line.png" />

      <!--각각의 플레이 리스트-->
      <div
        class="play-list-detail"
        v-for="playList in this.myPlayList"
        :key="playList.id"
        @click="
          [
            (showModal = true),
            (selectList = playList.myplaylist.id),
            (createDate = playList.myplaylist.createdAt),
            (listName = playList.myplaylist.name),
          ]
        "
      >
        <div class="play-list-detail-title">{{ playList.myplaylist.name }}</div>
        <img class="img-1" src="@/assets/images/lp_now_playing.png" />
        <img class="img-2" :src="playList.thumbnails[0]" />
        <img class="img-3" :src="playList.thumbnails[1]" />
        <img class="img-4" :src="playList.thumbnails[2]" />
        <img class="img-5" :src="playList.thumbnails[3]" />
      </div>

      <!--각각의 플레이 리스트 끝-->

      <!-- 플레이리스트 상세보기 모달 -->
      <user-play-list-detail
        :id="this.selectList"
        :name="this.listName"
        :create="createDate"
        v-if="showModal"
        @close="showModal = false"
      >
      </user-play-list-detail>
      <!-- 플레이리스트 상세보기 모달 끝-->
    </div>
  </div>
</template>

<script>
import UserPlayListDetail from "./UserPlayListDetail.vue";
import myPlayListApi from "@/api/myPlayList";

export default {
  name: "UserPlayList",

  components: {UserPlayListDetail},

  props: {},
  data() {
    return {
      showModal: false,
      myPlayList: [],
      selectList: "",
      listName: "",
      createDate: "",
    };
  },

  mounted() {
    myPlayListApi.listPlayList(
      localStorage.getItem("userSeq"),
      (res) => {
        console.log(res.data);
        this.myPlayList = res.data;
      },
      (err) => {
        console.log(err);
      }
    );
  },

  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./UserPlayList.scss";
</style>
