// 套餐管理 API
import request from "./request";

/** 分页查询套餐 */
export function listSetmealByPage(data: any) {
  return request.post("/setmeal/page", data);
}

/** 根据 ID 查询套餐 */
export function getSetmealById(id: number | string) {
  return request.get(`/setmeal/${id}`);
}

/** 新增套餐（含关联菜品） */
export function createSetmeal(data: any) {
  return request.post("/setmeal", data);
}

/** 修改套餐 */
export function updateSetmeal(data: any) {
  return request.put("/setmeal", data);
}

/** 套餐停售/起售 */
export function updateSetmealStatus(status: number, id: number | string) {
  return request.post(`/setmeal/status/${status}/${id}`);
}

/** 删除套餐 */
export function deleteSetmeal(ids: string) {
  return request.delete(`/setmeal?ids=${ids}`);
}
