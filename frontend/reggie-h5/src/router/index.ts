import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "Layout",
    component: () => import("../views/layout/index.vue"),
    redirect: "/goods",
    children: [
      {
        path: "goods",
        name: "goods",
        component: () => import("../views/goods/index.vue"),
        meta: { title: "商品" },
      },
      {
        path: "ratings",
        name: "ratings",
        component: () => import("../views/ratings/index.vue"),
        meta: { title: "评价" },
      },
      {
        path: "seller",
        name: "seller",
        component: () => import("../views/seller/index.vue"),
        meta: { title: "商家" },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("../views/errorPage/404.vue"),
    meta: { title: "404" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.afterEach((to) => {
  document.title = to.meta?.title ? `${to.meta.title} - 瑞吉外卖` : "瑞吉外卖";
});

export default router;