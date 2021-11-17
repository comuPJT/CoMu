import { API_SERVER_URL } from "@/constant/index.js";
import axios from "axios";
import store from '@/store/index';
// import jwt_decode from 'jwt-decode';
axios.defaults.baseURL = API_SERVER_URL;
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.put["Content-Type"] = "application/json";

// 요청 인터셉터 추가
const instance = axios.create();

instance.interceptors.request.use(
  function (config) {
    console.log(config);
    //요청에는 항상 토큰을 헤더에 넣어서 보냄.
    config.headers["Authorization"] =
      `Bearer ${store.getters.token}`;
    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    // ...
    return Promise.reject(error);
  }
);


// axios 객체 생성
export default instance;
