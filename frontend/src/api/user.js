import http from "@/util/http-common.js";

const login = (callback, errorCallback) => {
  http
    .get("/v1/users")
    .then((res) => {
      console.log(res);
      callback(res);
    })
    .catch((err) => {
      console.log(err);
      errorCallback(err);
    });
};

const logout = (callback, errorCallback) => {
  http
    .get("/user/logout")
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
      errorCallback(err);
    });
};

const withdrawal = (data, callback, errorCallback) => {
  //회원탈퇴
  http
    .delete("/user", {})
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
      errorCallback(err);
    });
};

const updateNickname = (data, callback, errorCallback) => {
  http
    .put("/v1/users", {
      userSeq: data[0],
      username: data[1],
    })
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
      errorCallback(err);
    });
};

const updateCharacter = (data, callback, errorCallback) => {
  http
    .put("/v1/users/character", {
      userSeq: data[0],
      characterNum: data[2],
    })
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
      errorCallback(err);
    });
};

export default {
  login,
  logout,
  withdrawal,
  updateNickname,
  updateCharacter,
};
