// 员工管理 API
import request from "./request";

/** 分页查询员工 */
export function listEmployeeByPage(data: any) {
  return request.post("/employee/listByPage", data);
}

/** 根据 ID 查询员工 */
export function getEmployeeById(id: number | string) {
  return request.get(`/employee/${id}`);
}

/** 新增员工 */
export function createEmployee(data: any) {
  return request.post("/employee", data);
}

/** 修改员工 */
export function updateEmployee(data: any) {
  return request.put("/employee", data);
}

/** 启停用员工账号 */
export function updateEmployeeStatus(id: number | string, status: number) {
  return request.post(`/employee/status/${id}?status=${status}`);
}

/** 删除员工 */
export function deleteEmployee(id: number | string) {
  return request.delete(`/employee/${id}`);
}
