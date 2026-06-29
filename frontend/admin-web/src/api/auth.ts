import request from "./request";

export function login(data: any) {
  return request.post("/employee/login", data);
}

export function logout() {
  return request.post("/employee/logout");
}

export function getCurrentUser() {
  return request.get("/employee/info");
}
