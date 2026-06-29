// 商品相关 API 请求封装
// 将所有 HTTP 请求集中管理，方便维护

import axios from "axios";

// 创建 axios 实例，设置基础路径
// 所有请求都会以 /api 开头，Vite proxy 会转发到后端
const request = axios.create({
  baseURL: "/api",
  timeout: 5000, // 请求超时时间：5秒
});
request.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  },
);

// 添加响应拦截器
request.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response.data;
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  },
);
/**
 * 查询商品列表
 * @param {string} name 可选，商品名称关键词
 * @returns {Promise} 商品列表
 */
export function getProducts(name: string) {
  return request.get("/product/list", { params: { name } });
}

/**
 * 根据 ID 查询商品
 * @param {number} id 商品ID
 * @returns {Promise} 商品详情
 */
export function getProductById(id: any): Promise<any> {
  return request.get(`/product/getById?id=${id}`);
}

/**
 * 新增商品
 * @param {Object} product 商品数据
 * @returns {Promise} 操作结果
 */
export function createProduct(product: any): Promise<any> {
  return request.post("/product/create", product);
}

/**
 * 修改商品
 * @param {number} id 商品ID
 * @param {Object} product 商品数据
 * @returns {Promise} 操作结果
 */
export function updateProduct(id: any, product: any) {
  return request.post(`/product/update/${id}`, product);
}

/**
 * 删除商品
 * @param {number} id 商品ID
 * @returns {Promise} 操作结果
 */
export function deleteProduct(id: any) {
  return request.delete(`/product/delete/${id}`);
}
/**
 * 分页查询
 */
export function listProductByPage(data: any) {
  return request.post(`/product/listByPage`, data);
}
