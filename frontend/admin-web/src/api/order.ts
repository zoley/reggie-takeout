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

/** 完成订单 */
export function completeOrder(id: number | string) {
  return request.get(`/order/done/${id}`);
}

/** 取消订单 */
export function cancelOrder(id: number | string) {
  return request.get(`/order/cancel/${id}`);
}
