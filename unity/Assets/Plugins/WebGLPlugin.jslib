mergeInto(LibraryManager.library, {

    GetMyCharacterNum: function () {
        return localStorage.getItem("characterNum");
    },

    OpenPlaylistModal: function () {
        localStorage.setItem("showPlayList", "TRUE");
        localStorage.setItem("showApplyMusic", "FALSE");
        localStorage.setItem("showTodayStory", "FALSE");
        localStorage.setItem("showBestStory", "FALSE");
        localStorage.setItem("showCharacter", "FALSE");
    },

    OpenApplyMusicModal: function () {
        localStorage.setItem("showPlayList", "FALSE");
        localStorage.setItem("showApplyMusic", "TRUE");
        localStorage.setItem("showTodayStory", "FALSE");
        localStorage.setItem("showBestStory", "FALSE");
        localStorage.setItem("showCharacter", "FALSE");
    },

    OpenTodayStoryModal: function () {
        localStorage.setItem("showPlayList", "FALSE");
        localStorage.setItem("showApplyMusic", "FALSE");
        localStorage.setItem("showTodayStory", "TRUE");
        localStorage.setItem("showBestStory", "FALSE");
        localStorage.setItem("showCharacter", "FALSE");
    },

    OpenBestStoryModal: function () {
        localStorage.setItem("showPlayList", "FALSE");
        localStorage.setItem("showApplyMusic", "FALSE");
        localStorage.setItem("showTodayStory", "FALSE");
        localStorage.setItem("showBestStory", "TRUE");
        localStorage.setItem("showCharacter", "FALSE");
    },

    OpenCharacterModal: function () {
        localStorage.setItem("showPlayList", "FALSE");
        localStorage.setItem("showApplyMusic", "FALSE");
        localStorage.setItem("showTodayStory", "FALSE");
        localStorage.setItem("showBestStory", "FALSE");
        localStorage.setItem("showCharacter", "TRUE");
    }

});