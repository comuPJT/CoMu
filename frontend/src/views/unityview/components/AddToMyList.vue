<template>
  <transition name="modal" appear>
    <div class="modal-overlay-second" @click.self="$emit('close')">
      <div class="modal-window-second">
        <!--모달 타이틀-->
        <div class="modal-top-second">
          <p>나의 LP 수납장</p>
          <img
            src="@/assets/images/close_button.svg"
            @click.self="$emit('close')"
          />
        </div>
        <!--모달 타이틀 끝-->

        <!--내 수납장 목록-->
        <div class="modal-content-second">
          <div class="modal-mylist-wrapper">
            <div class="modal-mylist">
              <div class="modal-mylist-img">
                <img
                  src="@/assets/images/playlist_add_button.svg"
                  @click="addNewList()"
                />
              </div>
              <div class="modal-mylist-title">
                <input v-model="newListName" placeholder="새 수납장" />
              </div>
            </div>

            <div v-for="i in 5" :key="i" class="modal-mylist">
              <div class="modal-mylist-img">
                <img
                  src="https://t1.daumcdn.net/thumb/R720x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/8fXh/image/0_JTh3JET7ZCHaT_IJhG4VbhEpI.png"
                />
              </div>
              <div class="modal-mylist-title">플레이리스트 {{ i }}</div>
            </div>
          </div>
        </div>

        <!--내 수납장 목록 끝-->
      </div>
    </div>
  </transition>
</template>
<script>
import myPlayListApi from "@/api/myPlayList";

export default {
  name: "AddToMyList",

  components: {},

  props: {},
  data() {
    return {
      newListName: "",
    };
  },

  methods: {
    addNewList() {
      const data = {
        musicIds: [],
        name: this.newListName,
        userSeq: localStorage.getItem("userSeq"),
      };
      myPlayListApi.newPlayList(
        data,
        (res) => {
          console.log(res.data);
          this.$alert("새로운 리스트가 생성되었습니다!");
        },
        (err) => {
          console.log(err);
          this.$alert("이미 존재하는 리스트 이름입니다.");
        }
      );
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./AddToMyList.scss";
</style>
