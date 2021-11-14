import userApi from '@/api/user'

export default {
  state: {
    user: null,
    token: null
  },
  getters: {
    user: state => state.user,
    token: state => state.token
  },
  actions: {
    fetchUser({ state, commit }, callback) {
      state.user
        ? callback && callback()
        : userApi.login(
          res => {
            console.log(res)
            commit('setUser', res.data.body.user)
            callback && callback()
          }
        )
    }
  },
  mutations: {
    setToken(state, token) {
      state.token = token
    },
    setUser(state, user) {
      state.user = user
    }
  }
}
