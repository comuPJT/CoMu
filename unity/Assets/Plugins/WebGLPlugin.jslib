mergeInto(LibraryManager.library, {

    GetMyCharacterNum: function () {
        return localStorage.getItem("characterNum");
    },

    GetInputActive: function () {
        var value = localStorage.getItem("isUnityInputActive");
        return value !== null;
    },

    SetInputInactive: function () {
        localStorage.removeItem("isUnityInputActive");
    },

    OpenPlaylistModal: function () {
        localStorage.setItem("showPlayList", "TRUE");
    },

    OpenPlayListAddModal: function () {
        localStorage.setItem("showPlayListAdd", "TRUE");
    },

    OpenTodayStoryModal: function () {
        localStorage.setItem("showTodayStory", "TRUE");
    },

    OpenBestStoryModal: function () {
        localStorage.setItem("showBestStory", "TRUE");
    },

    OpenCharacterModal: function () {
        localStorage.setItem("showCharacter", "TRUE");
    },

    SetRoomName: function(str) {
        var name = Pointer_stringify(str);
        localStorage.setItem("roomName", name);
    }

});