// 订单相关常量

/** 订单状态选项：0=全部 1=待付款 2=待收货 3=待评价 4=退款/售后 5=已完成 6=已取消 */
export const ORDER_STATUS_OPTIONS = [
  { label: "全部", value: 0 },
  { label: "待付款", value: 1 },
  { label: "待收货", value: 2 },
  { label: "待评价", value: 3 },
  { label: "退款/售后", value: 4 },
  { label: "已完成", value: 5 },
  { label: "已取消", value: 6 },
] as const;

/** 订单状态文字映射 */
export const ORDER_STATUS_TEXT: Record<number, string> = {
  0: "全部",
  1: "待付款",
  2: "待收货",
  3: "待评价",
  4: "退款/售后",
  5: "已完成",
  6: "已取消",
};

/** 订单状态标签类型映射 */
export const ORDER_STATUS_TAG_TYPE: Record<number, string> = {
  0: "",
  1: "warning",
  2: "primary",
  3: "success",
  4: "danger",
  5: "success",
  6: "info",
};
