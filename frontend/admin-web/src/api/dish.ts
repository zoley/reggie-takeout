// 菜品管理 API
import request from "./request";

/** 分页查询菜品 */
export function listDishByPage(data: any) {
  return request.post("/dish/listByPage", data);
}

/** 根据 ID 查询菜品 */
export function getDishById(params: Record<string, any>) {
  return request.get(`/dish/getById`, { params });
}

/** 新增菜品 */
export function createDish(data: any) {
  return request.post("/dish/create", data);
}

/** 修改菜品 */
export function updateDish(data: any) {
  return request.put("/dish/update", data);
}

/** 起售菜品 */
export function enabledDishStatus(params: Record<string, any>) {
  return request.get(`/dish/enabledStatus`, { params });
}

/** 停售菜品 */
export function disabledDishStatus(params: Record<string, any>) {
  return request.get(`/dish/disabledStatus`, { params });
}

/** 批量删除菜品 */
export function deleteBatchDish(data: string[]) {
  return request.post(`/dish/batchDelete`, data);
}

/** 根据 ID 删除菜品 */
export function deleteDishById(id: string) {
  return request.delete(`/dish/delete/${id}`);
}