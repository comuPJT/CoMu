<template>
  <div></div>
</template>

<script>
export default {
  mounted() {
    console.log(this.$store.getters.user.user.username);
    console.log(this.$store.getters.user);
    if (this.$store.getters.user.user.firstVisitYn == "Y") {
      //처음방문이면 닉네임, 캐릭터선택으로
      this.$router.replace({name: "Main", params: {order: 3}});
    } else {
      //초기 설정이 완료된 플레이어는 바로 유니티화면으로
      localStorage.setItem(
        "userNickname",
        this.$store.getters.user.user.username
      );
      localStorage.setItem(
        "characterNum",
        this.$store.getters.user.user.characterNum
      );
      localStorage.setItem("userSeq", this.$store.getters.user.userSeq);
      this.$router.replace("/unityview");
    }
  },
};
</script>
