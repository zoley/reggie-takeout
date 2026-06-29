// 分类管理 API
import request from "./request";

/** 查询分类列表 */
export function listCategory(type?: number) {
  return request.get("/category/list", { params: type ? { type } : {} });
}

/** 分页查询分类 */
export function listCategoryByPage(data: any) {
  return request.post("/category/page", data);
}

/** 根据 ID 查询分类 */
export function getCategoryById(id: number | string) {
  return request.get(`/category/${id}`);
}

/** 新增分类 */
export function createCategory(data: any) {
  return request.post("/category", data);
}

/** 修改分类 */
export function updateCategory(data: any) {
  return request.put("/category", data);
}

/** 删除分类 */
export function deleteCategory(id: number | string) {
  return request.delete(`/category/${id}`);
}
