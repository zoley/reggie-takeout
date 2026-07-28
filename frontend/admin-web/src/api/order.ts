// 订单管理 API
import request from "./request";

/** 分页查询订单 */
export function listOrderByPage(data: any) {
  return request.post("/order/listByPage", data);
}

/** 根据 ID 查询订单 */
export function getOrderById(params: Record<string, any>) {
  return request.get(`/order/getById`, { params });
}

/** 修改订单 */
export function updateOrder(data: any) {
  return request.put("/order/update", data);
}