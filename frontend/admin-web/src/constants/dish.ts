// 菜品相关常量

/** 菜品口味类型选项：label 为展示文本，value 为提交后端的值（DishFlavor.name） */
export const FLAVOR_TYPE_OPTIONS = [
  { label: "份量", value: "portion" },
  { label: "温度", value: "temperature" },
  { label: "甜度", value: "sweetness" },
  { label: "辣度", value: "spiciness" },
  { label: "加料", value: "topping" },
] as const;
