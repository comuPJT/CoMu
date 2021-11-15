import { API_SERVER_URL } from "@/constant/index.js";
import axios from "axios";
import store from '@/store/index';
// import jwt_decode from 'jwt-decode';
const storage = localStorage;
axios.defaults.baseURL = API_SERVER_URL;
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.put["Content-Type"] = "application/json";

// 요청 인터셉터 추가
const instance = axios.create();

instance.interceptors.request.use(
  function (config) {
    console.log(config);
    //요청을 보내기 전에 수행할 일
    //...
    //1. 로그인, 공용플레이리스트, 사연함의 요청은 헤더 없이 전송
    if (config.url.split("/")[3] === "user" && config.url.split("/")[4] !== "info") {
      //조건 true 추후 스웨거보고 수정 예정 ex)config.url.split("/")[3] === "user" && config.url.split("/")[4] !== "info"
      return config;
    }
    // 2. 그 외의 요청에는 항상 토큰을 헤더에 넣어서 보냄.
    else {
      config.headers["Authorization"] =
        `Bearer ${store.getters.token}`;
      return config;
    }
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    // ...
    return Promise.reject(error);
  }
);

// 응답 인터셉터 추가
instance.interceptors.response.use(
  function (response) {
    // 응답 데이터를 가공
    // ...
    if (response.headers["authorization"]) {
      storage.setItem(
        "jwt-access-token",
        response.headers["authorization"].substring(7)
      );
    }
    return response;
  },
  function (error) {
    // 오류 응답을 처리
    // …
    return Promise.reject(error);
  }
);

// axios 객체 생성
export default instance;
