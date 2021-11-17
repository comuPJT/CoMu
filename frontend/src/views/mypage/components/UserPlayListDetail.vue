<template>
  <transition name="modal" appear>
    <div class="play-list-overlay" @click.self="$emit('close')">
      <div class="play-list-window">
        <div class="list-info-wrapper">
          <div class="list-info-cover">
            <img
              v-if="musicList.musics.length > 0"
              :src="musicList.musics[0].thumbnail"
            />
            <img
              v-if="musicList.musics.length == 0"
              src="@/assets/images/no_result.png"
            />
            <div class="list-info-text" style="height: 100%">
              <div class="list-info-name" v-if="!isEdit">
                {{ name }}
              </div>
              <div class="list-info-name" v-if="isEdit">
                <div class="input_box" style="width: 18vw">
                  <input v-model="editInput" :placeholder="name" />
                </div>
                <img src="@/assets/images/edit_icon.svg" @click="editName()" />
              </div>
              <div class="list-info-update">생성일자 : {{ createDate }}</div>
              <div class="list-info-amount">
                총 곡수 : {{ musicList.numberOfMusics }}곡
              </div>
            </div>
          </div>
        </div>

        <div class="list-info-detail" v-if="!isEdit">
          <div
            class="list-info-detail-music"
            v-for="music in musicList.musics"
            :key="music.spotifyId"
          >
            <div class="list-info-detail-music-cover">
              <img :src="music.thumbnail" />
            </div>
            <div class="list-info-detail-music-name">{{ music.name }}</div>
            <div class="list-info-detail-music-artist">{{ music.singer }}</div>
            <div class="list-info-detail-music-album">{{ music.album }}</div>
          </div>
        </div>

        <div class="list-info-detail" v-if="isEdit">
          <draggable
            ref="drag"
            group="people"
            v-model="musicList.musics"
            @start="drag = true"
            @end="drag = false"
            ghost-class="ghost"
            style="width: 100%; height: 100%"
          >
            <div
              class="list-info-detail-music"
              v-for="music in musicList.musics"
              :key="music.spotifyId"
            >
              <div class="list-info-detail-music-cover">
                <img :src="music.thumbnail" />
              </div>
              <div class="list-info-detail-music-name">{{ music.name }}</div>
              <div class="list-info-detail-music-artist">
                {{ music.singer }}
              </div>
              <div class="list-info-detail-music-album">{{ music.album }}</div>
            </div>
          </draggable>
        </div>

        <div class="list-info-detail-button-wrapper" v-if="!isEdit">
          <div class="smallbuttonbrown">
            <div class="buttoncontent" @click.self="$emit('close')">이전</div>
          </div>
          <div class="smallbuttonwhite" @click="isEdit = true">
            <div class="buttoncontent">
              리스트 편집<img src="@/assets/images/list-edit-icon.png" />
            </div>
          </div>
        </div>

        <div class="list-info-detail-button-wrapper" v-if="isEdit">
          <div class="smallbuttonbrown" @click="isEdit = false">
            <div class="buttoncontent">이전</div>
          </div>
          <div class="smallbuttonwhite" @click="isEdit = false">
            <div class="buttoncontent">완료</div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import myPlayListApi from "@/api/myPlayList";
import draggable from "vuedraggable";

export default {
  name: "UserPlayListDetail",

  components: {draggable},

  props: {id: Number, name: String, create: String},
  data() {
    return {isEdit: false, createDate: "", musicList: [], editInput: ""};
  },
  mounted() {
    this.createDate = this.create.substring(0, 10);
    myPlayListApi.playListDetail(
      this.id,
      (res) => {
        this.musicList = res.data;
      },
      (err) => {
        console.log(err);
      }
    );
  },
  methods: {
    editName() {
      if (this.editInput.length > 0) {
        const data = {
          userSeq: localStorage.getItem("userSeq"),
          name: this.editInput,
          id: this.id,
        };
        myPlayListApi.editListName(
          data,
          (res) => {
            this.name = this.editInput;
            this.$alert("변경되었습니다!");
            console.log(res);
          },
          (err) => {
            this.$alert("중복된 이름이 존재합니다.");
            console.log(err);
          }
        );
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./UserPlayListDetail.scss";
</style>
