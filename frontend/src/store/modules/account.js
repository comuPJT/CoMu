import userApi from '@/api/user'

export default {
  state: {
    user: null,
    token: null,
  },
  getters: {
    user: state => state.user,
    token: state => state.token
  },
  actions: {
    fetchUser({ commit }, callback) {
      userApi.login(
        res => {
          commit('setUser', res.data.body)
          callback && callback()
        },
      )
    }
  },
  mutations: {
    setToken(state, token) {
      state.token = token
    },
    setUser(state, user) {
      state.user = user
    },
  }
}
