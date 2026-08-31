// 店铺端（C 端）接口
// 说明：当前为演示占位接口，后续对接真实后端时在此替换为具体控制器接口。
// 例如：
//   - 商品菜单 -> 分类 category/list + 菜品 dish/listByPage
//   - 店铺信息/评价 -> 由后端补充对应聚合接口
import request from "./request";

/** 店铺详情（头部、公告、评分、起送价、配送费等） */
export function getStore() {
  return request.get("/store/detail");
}

/** 商品菜单（分类 + 该分类下的菜品） */
export function getGoods() {
  return request.get("/store/goods");
}

/** 店铺评价列表 */
export function getRatings() {
  return request.get("/store/ratings");
}