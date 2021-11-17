import http from "@/util/http-common.js";
//import store from "@/store/index"


const getPublicPlayList = (data, callback, errorCallback) => {
    //공용 재생목록을 가져옵니다
    http
        .get("/share/" + data)
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};

const addMusicPublicPlayList = (roomdata, data, callback, errorCallback) => {
    //공용 재생목록에 노래를 추가합니다.
    console.log(data);
    http
        .post("/share/" + roomdata, {
            contents: data.contents,
            name: data.name,
            singer: data.singer,
            source: data.source,
            spotifyId: data.spotifyId,
            thumbnail: data.thumbnail,
            title: data.title,
            userId: data.userId,
        })
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};


export default {
    getPublicPlayList,
    addMusicPublicPlayList,
};