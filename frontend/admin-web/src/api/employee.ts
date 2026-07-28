// 员工管理 API
import request from "./request";

/** 分页查询员工 */
export function listEmployeeByPage(data: any) {
  return request.post("/employee/listByPage", data);
}

/** 根据 ID 查询员工 */
export function getEmployeeById(params: Record<string, any>) {
  return request.get(`/employee/getById`, { params });
}

/** 新增员工 */
export function createEmployee(data: any) {
  return request.post("/employee/create", data);
}

/** 修改员工 */
export function updateEmployee(data: any) {
  return request.put("/employee/update", data);
}

/** 启停用员工账号 */
export function enabledEmployeeStatus(params: Record<string, any>) {
  return request.get(`/employee/enabledStatus`, { params });
}
/** 启停用员工账号 */
export function disabledEmployeeStatus(params: Record<string, any>) {
  return request.get(`/employee/disabledStatus`, { params });
}

/** 批量删除员工 */
export function deleteBatchEmployee(data: string[]) {
  return request.post(`/employee/batchDelete`, data);
}
export function deleteEmployeeById(id: string) {
  return request.delete(`/employee/delete/${id}`);
}

/** 重置员工密码 */
export function resetEmployeePassword(data: any) {
  return request.put("/employee/resetPassword", data);
}
