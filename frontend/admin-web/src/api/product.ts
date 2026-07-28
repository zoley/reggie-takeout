// 商品管理 API
import request from "./request";

/** 查询商品列表 */
export function listProduct(params: Record<string, any>) {
  return request.get("/product/list", { params });
}

/** 根据 ID 查询商品 */
export function getProductById(params: Record<string, any>) {
  return request.get(`/product/getById`, { params });
}

/** 新增商品 */
export function createProduct(data: any) {
  return request.post("/product/create", data);
}

/** 修改商品 */
export function updateProduct(data: any) {
  return request.put("/product/update", data);
}

/** 根据 ID 删除商品 */
export function deleteProductById(id: string) {
  return request.delete(`/product/delete/${id}`);
}

/** 分页查询商品 */
export function listProductByPage(data: any) {
  return request.post(`/product/listByPage`, data);
}