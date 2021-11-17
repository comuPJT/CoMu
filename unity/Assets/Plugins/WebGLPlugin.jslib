mergeInto(LibraryManager.library, {

    GetUserNickname: function () {
        var returnStr = localStorage.getItem("user-nickname");
        var bufferSize = lengthBytesUTF8(returnStr) + 1;
        var buffer = _malloc(bufferSize);
        stringToUTF8(returnStr, buffer, bufferSize);
        return buffer;
    },

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