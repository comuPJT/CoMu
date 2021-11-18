
export default {
    state: {
        themeName: null,
        themeId: null,
    },
    getters: {
        themeName: state => state.themeName,
        themeId: state => state.themeId
    },
    mutations: {
        setThemeId(state, themeId) {
            state.themeId = themeId
        },
        setThemeName(state, themeName) {
            state.themeName = themeName
        },
    }
}
