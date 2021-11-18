import http from "@/util/http-common.js";


const getMormalStory = (data, callback, errorCallback) => {
    //각 방의 사연을 가져옵니다
    http
        .get("/share/"+data+"/story/")
        .then((res) => {
            callback(res);
        })
        .catch((err) => {
            errorCallback(err);
        });
};



export default {
    getMormalStory
};