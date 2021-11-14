<template>
  <transition name="modal" appear>
    <div class="play-list-overlay" @click.self="$emit('close')">
      <div class="play-list-window">
        <div class="list-info-wrapper">
          <div class="list-info-cover">
            <img src="@/assets/images/tempcover1.jpg" />
            <div class="list-info-text" style="height: 100%">
              <div class="list-info-name" v-if="!isEdit">
                나의 플레이리스트1
              </div>
              <div class="list-info-name" v-if="isEdit">
                <div class="input_box" style="width: 18vw">
                  <input placeholder="기존이름" />
                </div>
              </div>
              <div class="list-info-update">업데이트 : 2021/11/11</div>
              <div class="list-info-amount">총 곡수 : 4곡</div>
              <div class="list-info-playtime">재생 시간 : 00:48:37</div>
            </div>
          </div>
        </div>

        <div class="list-info-detail" v-if="!isEdit">
          <div class="list-info-detail-music" v-for="n in 6" :key="n">
            <div class="list-info-detail-music-cover">
              <img src="@/assets/images/tempcover2.jpg" />
            </div>
            <div class="list-info-detail-music-name">희재</div>
            <div class="list-info-detail-music-artist">성시경</div>
            <div class="list-info-detail-music-album">Best Album</div>
          </div>
        </div>

        <div class="list-info-detail" v-if="isEdit">
          <draggable
            group="people"
            @start="drag = true"
            @end="drag = false"
            ghost-class="ghost"
            style="width:100% ; height:100%"
          >
            <div class="list-info-detail-music" v-for="n in 6" :key="n">
            <div class="list-info-detail-music-cover">
              <img src="@/assets/images/tempcover2.jpg" />
            </div>
            <div class="list-info-detail-music-name">희재편집{{n}}</div>
            <div class="list-info-detail-music-artist">성시경</div>
            <div class="list-info-detail-music-album">Best Album</div>
          </div>
          </draggable>

        </div>

        <div class="list-info-detail-button-wrapper" v-if="!isEdit">
          <div class="smallbuttonbrown">
            <div class="buttoncontent">이전</div>
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
import draggable from "vuedraggable";

export default {
  name: "UserPlayListDetail",

  components: {draggable},

  props: {},
  data() {
    return {isEdit: false};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "./UserPlayListDetail.scss";
</style>
