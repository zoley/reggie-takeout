// 套餐管理 API
import request from "./request";

/** 分页查询套餐 */
export function listSetmealByPage(data: any) {
  return request.post("/setmeal/listByPage", data);
}

/** 根据 ID 查询套餐 */
export function getSetmealById(params: Record<string, any>) {
  return request.get(`/setmeal/getById`, { params });
}

/** 新增套餐 */
export function createSetmeal(data: any) {
  return request.post("/setmeal/create", data);
}

/** 修改套餐 */
export function updateSetmeal(data: any) {
  return request.put("/setmeal/update", data);
}

/** 起售套餐 */
export function enabledSetmealStatus(params: Record<string, any>) {
  return request.get(`/setmeal/enabledStatus`, { params });
}

/** 停售套餐 */
export function disabledSetmealStatus(params: Record<string, any>) {
  return request.get(`/setmeal/disabledStatus`, { params });
}

/** 批量删除套餐 */
export function deleteBatchSetmeal(data: string[]) {
  return request.post(`/setmeal/batchDelete`, data);
}

/** 根据 ID 删除套餐 */
export function deleteSetmealById(id: string) {
  return request.delete(`/setmeal/delete/${id}`);
}