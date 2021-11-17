import http from "@/util/http-common.js";
import store from "@/store/index"


const getPersonalPlayList = (callback, errorCallback) => {
    //내 재생목록을 가져옵니다 (이름있는 플레이리스트 여러개 아님!)
    http
        .get("/mymainplaylist/" + store.getters.user.userSeq)
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};

const addPersonalPlayList = (data, callback, errorCallback) => {
    //내 재생목록에 곡을 추가합니다.
    console.log(data);
    http
        .post("/mymainplaylist/", {
            musicList: data.musicList,
            userSeq: data.userSeq,
        })
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};

const deletePersonal = (data, callback, errorCallback) => {
    //내 재생목록에서 곡을 삭제합니다.
    console.log(data);
    http
        .put("/mymainplaylist/", {
            musicIds: data.musicIds,
            userSeq: data.userSeq,
        })
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            console.log(data);
            errorCallback(err);
        });
};


export default {
    getPersonalPlayList,
    addPersonalPlayList,
    deletePersonal
};