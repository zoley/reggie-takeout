// 菜品管理 API
import request from "./request";

/** 分页查询菜品 */
export function listDishByPage(data: any) {
  return request.post("/dish/page", data);
}

/** 根据 ID 查询菜品 */
export function getDishById(id: number | string) {
  return request.get(`/dish/${id}`);
}

/** 新增菜品（含口味） */
export function createDish(data: any) {
  return request.post("/dish", data);
}

/** 修改菜品 */
export function updateDish(data: any) {
  return request.put("/dish", data);
}

/** 菜品停售/起售 */
export function updateDishStatus(status: number, id: number | string) {
  return request.post(`/dish/status/${status}/${id}`);
}

/** 删除菜品 */
export function deleteDish(ids: string) {
  return request.delete(`/dish?ids=${ids}`);
}
