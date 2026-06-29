// 订单管理 API
import request from "./request";

/** 分页查询订单 */
export function listOrderByPage(data: any) {
  return request.post("/order/page", data);
}

/** 订单详情 */
export function getOrderDetail(orderId: string) {
  return request.get(`/order/detail/${orderId}`);
}

/** 更新订单状态 */
export function updateOrderStatus(orderId: string, status: number) {
  return request.put(`/order`, { id: orderId, status });
}
