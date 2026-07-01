import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/login/index.vue"),
    meta: { title: "登录" },
  },
  {
    path: "/",
    component: () => import("../layout/index.vue"),
    redirect: "/employee",
    children: [
      {
        path: "employee",
        name: "Employee",
        component: () => import("../views/employee/index.vue"),
        meta: { title: "员工管理", icon: "User" },
      },
      {
        path: "category",
        name: "Category",
        component: () => import("../views/category/index.vue"),
        meta: { title: "分类管理", icon: "Menu" },
      },
      {
        path: "dish",
        name: "Dish",
        component: () => import("../views/dish/index.vue"),
        meta: { title: "菜品管理", icon: "Bowl" },
      },
      {
        path: "setmeal",
        name: "Setmeal",
        component: () => import("../views/setmeal/index.vue"),
        meta: { title: "套餐管理", icon: "Food" },
      },
      {
        path: "order",
        name: "Order",
        component: () => import("../views/order/index.vue"),
        meta: { title: "订单明细", icon: "List" },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("../views/error/404.vue"),
    meta: { title: "404" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, from, next) => {
//   const token = sessionStorage.getItem("token");
//   if (to.path === "/login") {
//     if (token) {
//       next("/employee");
//     } else {
//       next();
//     }
//   } else {
//     if (token) {
//       next();
//     } else {
//       next("/login");
//     }
//   }
// });

router.afterEach((to) => {
  document.title = to.meta?.title ? `${to.meta.title} - 瑞吉外卖` : "瑞吉外卖";
});

export default router;
