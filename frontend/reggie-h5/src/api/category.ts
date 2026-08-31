// 分类管理 API
import request from "./request";

/** 查询分类列表 */
export function listCategory(type?: number) {
  return request.get("/category/list", { params: type ? { type } : {} });
}

/** 分页查询分类 */
export function listCategoryByPage(data: any) {
  return request.post("/category/listByPage", data);
}

/** 根据 ID 查询分类 */
export function getCategoryById(id: number | string) {
  return request.get(`/category/getById`, { params: { id } });
}

/** 新增分类 */
export function createCategory(data: any) {
  return request.post("/category/create", data);
}

/** 修改分类 */
export function updateCategory(data: any) {
  return request.put("/category/update", data);
}

/** 删除分类 */
export function deleteCategory(id: number | string) {
  return request.delete(`/category/delete/${id}`);
}
