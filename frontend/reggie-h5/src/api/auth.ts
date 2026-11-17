// 认证管理 API
import request from "./request";

/** 员工登录 */
export function loginEmployee(data: any) {
  return request.post("/employee/login", data);
}

/** 员工登出 */
export function logoutEmployee() {
  return request.post("/employee/logout");
}

/** 获取当前员工信息 */
export function getEmployeeInfo() {
  return request.get("/employee/info");
}

/** C 端：发送短信验证码 */
export function sendSmsCode(data: { phone: string }) {
  return request.post("/user/sendMsg", data);
}

/** C 端：手机号验证码登录 */
export function loginByPhone(data: { phone: string; code: string }) {
  return request.post("/user/login", data);
}

/** 文件上传 */
export function uploadFile(data) {
  return request.post("/common/file/upload", data);
}
