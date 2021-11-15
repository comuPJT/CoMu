import http from "@/util/http-common.js";

const login = (callback, errorCallback) => {
  http
    .post("/user/login", {})
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
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
  http
    .delete("/user", {
    })
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
      errorCallback(err);
    });
};

const updateNickname = (data, callback, errorCallback) => {
  http
    .put("/v1/users", data)
    .then((res) => {
      callback(res);
    })
    .catch((err) => {
      errorCallback(err);
    });
};

const updateCharacter = (data, callback, errorCallback) => {
  http
    .put("/user", data)
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