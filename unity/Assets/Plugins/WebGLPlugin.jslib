mergeInto(LibraryManager.library, {

    GetMyCharacterNum: function () {
        return localStorage.getItem("characterNum");
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