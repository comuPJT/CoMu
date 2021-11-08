<template>
<div></div>
</template>

<script>
import { mapMutations, mapActions } from 'vuex'

const storage = localStorage;

export default {
  created () {
    const token = this.$route.query.token
    console.log('token', token)
    storage.setItem("jwt-access-token", token.substring(7));
    this.$router.replace('/')
  },
  methods: {
    ...mapActions(['fetchUser']),
    ...mapMutations(['setToken'])
  }
}
</script>
