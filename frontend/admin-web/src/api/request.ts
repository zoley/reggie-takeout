// axios 请求封装
import axios from "axios";
import { ElMessage } from "element-plus";
import {BASE_PATH} from "@/utils";
import router from "@/router";

// 创建 axios 实例
const request = axios.create({
  baseURL: BASE_PATH,
  timeout: 1000000,
});

// 请求拦截器：自动附加 token
request.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// 响应拦截器：统一处理错误
request.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== undefined && res.code !== 200) {
      ElMessage.error(res.msg || "请求失败");
      if(res.code === 401){
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("user");
        router.replace("/login");
        return;
      }
      return Promise.reject(new Error(res.msg || "请求失败"));
    }
    return res;
  },
  (error) => {
    const msg = error.response?.data?.msg || error.message || "网络错误";
    ElMessage.error(msg);
    return Promise.reject(error);
  },
);

export default request;
