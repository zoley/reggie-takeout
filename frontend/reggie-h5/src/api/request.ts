// axios 请求封装
import axios from "axios";
import { showFailToast } from "vant";
import { BASE_PATH } from "@/utils";
import router from "@/router";

// 创建 axios 实例
const request = axios.create({
  baseURL: BASE_PATH,
  timeout: 10000,
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

// 响应拦截器：统一处理错误并解包 data
request.interceptors.response.use(
  (response) => {
    const res = response.data;
    // 后端统一响应结构 { code, msg, data }
    if (res && typeof res === "object" && "code" in res) {
      if (res.code === 200) {
        return res.data;
      }
      if (res.code === 401) {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("user");
        router.replace("/login");
        showFailToast(res.msg || "登录已过期");
        return Promise.reject(res);
      }
      showFailToast(res.msg || "请求失败");
      return Promise.reject(res);
    }
    return res;
  },
  (error) => {
    showFailToast(error.response?.data?.msg || error.message || "网络错误");
    return Promise.reject(error);
  },
);

export default request;