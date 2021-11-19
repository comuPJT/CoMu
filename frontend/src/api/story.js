import http from "@/util/http-common.js";


const getNormalStory = (data, callback, errorCallback) => {
    //각 방의 사연을 가져옵니다
    http
        .get("/share/" + data + "/story/")
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};

const getNormalStoryDetail = (data, callback, errorCallback) => {
    //사연의 상세정보를 가져옵니다.
    http
        .get("/share/" + data.roomId + "/story/" + data.storyId)
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};

const storyLike = (data, callback, errorCallback) => {
    //사연에 좋아요를 누릅니다.
    http
        .get("​/share/like/" + data.playId + "/user/" + data.userId)
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};

const getHonorList = (data, callback, errorCallback) => {
    //명예의전당 리스트를 반납합니다.
    http
        .get("​/share/honor")
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};





export default {
    getNormalStory,
    getNormalStoryDetail,
    storyLike,
    getHonorList
};