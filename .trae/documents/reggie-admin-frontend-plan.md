# 瑞吉外卖后台管理系统 - 前端实现计划

## 一、摘要

基于现有 Vue 3 + Element Plus + Vite 技术栈，为瑞吉外卖项目搭建完整的后台管理系统前端，包含 **5 个业务模块** 的 CRUD 页面、路由系统、布局框架、API 接口层及通用工具封装。

## 二、现状分析

### 当前技术栈

| 技术                      | 版本      | 用途                                       |
| ----------------------- | ------- | ---------------------------------------- |
| Vue 3                   | ^3.5.34 | 前端框架（Composition API + `<script setup>`） |
| Element Plus            | ^2.14.1 | UI 组件库                                   |
| Axios                   | ^1.16.1 | HTTP 请求                                  |
| Vite                    | ^8.0.12 | 构建工具                                     |
| unplugin-auto-import    | ^21.0.0 | 自动导入（Vue API）                            |
| unplugin-vue-components | ^32.1.0 | 按需引入组件                                   |

### 当前项目结构

```
src/
├── main.js              # 入口，仅挂载 App + Element Plus 样式
├── App.vue              # 根组件，直接引用 Home
├── style.css            # 全局样式
├── api/
│   └── product.ts       # 商品 API（含 axios 实例）
└── views/
    └── home/
        └── index.vue    # 商品管理页面（唯一页面）
```

### 缺失能力

* **无路由**：当前直接 import 组件，无法多页面切换

* **无布局**：无侧边栏/顶栏等管理后台典型布局

* **API 层单薄**：仅商品模块，axios 实例与业务 API 混在一起

* **缺少通用工具**：无 request 封装、无状态管理等

### 后端接口约定（从现有代码推断）

* 基础路径：`/api`（代理到 `http://localhost:9530`）

* 统一响应格式：`{ code: 200, data: ..., msg: ... }`

* 分页格式：`{ records: [], total: number }`

## 三、目标功能模块（5 个菜单）

根据截图和数据库表结构，需要实现以下 5 个模块：

| 序号 | 菜单名称 | 对应数据表                  | 功能说明                   |
| -- | ---- | ---------------------- | ---------------------- |
| 1  | 员工管理 | employee               | 员工账号 CRUD、启停用          |
| 2  | 分类管理 | category               | 菜品/套餐分类 CRUD、排序、类型区分   |
| 3  | 菜品管理 | dish                   | 菜品 CRUD、图片上传、口味配置、状态管理 |
| 4  | 套餐管理 | setmeal                | 套餐 CRUD、关联菜品、图片上传、状态管理 |
| 5  | 订单明细 | orders / order\_detail | 订单列表查询、订单详情查看          |

## 四、实施方案

### Step 1：安装依赖

安装 `vue-router`：

```bash
pnpm add vue-router@4
```

### Step 2：重构目录结构

```
src/
├── main.js                    # 入口（新增 router 注册）
├── App.vue                    # 根组件（改为 <router-view>）
├── style.css                  # 全局样式
├── api/
│   ├── request.ts             # axios 实例封装（从 product.ts 抽取）
│   ├── employee.ts            # 员工管理 API
│   ├── category.ts            # 分类管理 API
│   ├── dish.ts                # 菜品管理 API
│   ├── setmeal.ts             # 套餐管理 API
│   └── order.ts               # 订单管理 API
├── router/
│   └── index.ts               # 路由配置
├── layout/
│   └── index.vue              # 后台布局（侧边栏 + 顶栏 + 内容区）
├── views/
│   ├── employee/
│   │   └── index.vue          # 员工管理页
│   ├── category/
│   │   └── index.vue          # 分类管理页
│   ├── dish/
│   │   └── index.vue          # 菜品管理页
│   ├── setmeal/
│   │   └── index.vue          # 套餐管理页
│   └── order/
│       └── index.vue          # 订单明细页
└── utils/
    └── index.ts               # 通用工具函数
```

### Step 3：各文件详细设计

#### 3.1 `src/api/request.ts` — HTTP 请求封装

**来源**：从 `api/product.ts` 中抽取 axios 实例逻辑
**内容**：

* 创建 axios 实例（baseURL: `/api`, timeout: 10000）

* 请求拦截器：自动附加 token（从 localStorage 读取）

* 响应拦截器：统一处理 code !== 200 的错误提示

* 导出 `request` 实例供各 API 模块使用

#### 3.2 `src/router/index.ts` — 路由配置

**内容**：

```typescript
// 路由结构
const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/employee',
    children: [
      { path: 'employee', component: () => import('@/views/employee/index.vue'), meta: { title: '员工管理' } },
      { path: 'category', component: () => import('@/views/category/index.vue'), meta: { title: '分类管理' } },
      { path: 'dish', component: () => import('@/views/dish/index.vue'), meta: { title: '菜品管理' } },
      { path: 'setmeal', component: () => import('@/views/setmeal/index.vue'), meta: { title: '套餐管理' } },
      { path: 'order', component: () => import('@/views/order/index.vue'), meta: { title: '订单明细' } },
    ]
  }
]
```

#### 3.3 `src/layout/index.vue` — 后台布局

**参考截图风格**：

* 左侧深色侧边栏（#304156），宽度 200px

* 顶部显示 "瑞吉外卖 LOGO" / 项目标题

* 侧边菜单项：员工管理、分类管理、菜品管理、套餐管理、订单明细

* 使用 `el-menu` + `router` 模式，`el-icon` 图标

* 右侧主内容区 `<router-view />`

#### 3.4 各业务模块 API 设计

**`src/api/employee.ts`** **— 员工管理**

| 方法                   | 路径                           | 说明   |
| -------------------- | ---------------------------- | ---- |
| listEmployeeByPage   | POST `/employee/page`        | 分页查询 |
| getEmployeeById      | GET `/employee/{id}`         | 详情   |
| createEmployee       | POST `/employee`             | 新增   |
| updateEmployee       | PUT `/employee`              | 修改   |
| updateEmployeeStatus | POST `/employee/status/{id}` | 启停用  |

**`src/api/category.ts`** **— 分类管理**

| 方法             | 路径                      | 说明      |
| -------------- | ----------------------- | ------- |
| listCategory   | GET `/category/list`    | 列表（按类型） |
| createCategory | POST `/category`        | 新增      |
| updateCategory | PUT `/category`         | 修改      |
| deleteCategory | DELETE `/category/{id}` | 删除      |

**`src/api/dish.ts`** **— 菜品管理**

| 方法               | 路径                                | 说明      |
| ---------------- | --------------------------------- | ------- |
| listDishByPage   | POST `/dish/page`                 | 分页查询    |
| getDishById      | GET `/dish/{id}`                  | 详情      |
| createDish       | POST `/dish`                      | 新增（含口味） |
| updateDish       | PUT `/dish`                       | 修改      |
| updateDishStatus | POST `/dish/status/{status}/{id}` | 停售/起售   |

**`src/api/setmeal.ts`** **— 套餐管理**

| 方法                  | 路径                                   | 说明        |
| ------------------- | ------------------------------------ | --------- |
| listSetmealByPage   | POST `/setmeal/page`                 | 分页查询      |
| getSetmealById      | GET `/setmeal/{id}`                  | 详情        |
| createSetmeal       | POST `/setmeal`                      | 新增（含关联菜品） |
| updateSetmeal       | PUT `/setmeal`                       | 修改        |
| updateSetmealStatus | POST `/setmeal/status/{status}/{id}` | 停售/起售     |

**`src/api/order.ts`** **— 订单管理**

| 方法              | 路径                            | 说明   |
| --------------- | ----------------------------- | ---- |
| listOrderByPage | POST `/order/page`            | 分页查询 |
| getOrderDetail  | GET `/order/detail/{orderId}` | 订单详情 |

#### 3.5 各业务页面设计

**每个页面统一包含**：

* 搜索工具栏（条件筛选 + 搜索按钮）

* 操作按钮栏（新增等）

* 数据表格（el-table，字段对应对应业务）

* 分页组件（el-pagination）

* 新增/编辑弹窗（el-dialog + el-form）

**各页面特有字段**：

**员工管理页** (`views/employee/index.vue`)：

* 表格列：用户名、姓名、手机号、账号状态、操作（编辑/启用禁用）

* 搜索：姓名

* 表单：用户名、姓名、手机号、性别、身份证号

**分类管理页** (`views/category/index.vue`)：

* 表格列：分类名称、分类类型（菜品分类/套餐分类）、操作时间、排序、操作（修改/删除）

* 参考 screenshot 2 的双栏布局（左侧列表 + 右侧新建表单）

**菜品管理页** (`views/dish/index.vue`)：

* 表格列：菜品名称、图片、菜品分类、售价、售卖状态、最后操作时间、操作（修改/停售/起售）

* 参考 screenshot 1 的风格

* 表单：菜品名、分类、价格、图片、口味

**套餐管理页** (`views/setmeal/index.vue`)：

* 表格列：套餐名称、图片、套餐分类、套餐价格、售卖状态、最后操作时间、操作

* 表单：套餐名、分类、价格、图片、套餐菜品（关联选择）

**订单明细页** (`views/order/index.vue`)：

* 表格列：订单号、下单时间、状态、金额、收货人、联系电话、地址、操作（查看详情/完成/取消）

* 订单详情弹窗：展示 order\_detail 明细列表

#### 3.6 `src/utils/index.ts` — 通用工具

* `formatDate(date)` — 日期格式化

* `formatPrice(price)` — 价格格式化（¥前缀）

* `getLocalStorage(key)` / `setLocalStorage(key, value)` — 本地存储封装

### Step 4：入口文件改造

**`src/main.js`**：注册 router
**`src/App.vue`**：改为 `<router-view />`

## 五、假设与决策

1. **后端接口路径**：参照瑞吉外卖标准接口规范设计，后端未就绪时前端使用 mock 数据或预留接口调用
2. **认证方式**：Token 存储在 localStorage，请求头携带 `Authorization`
3. **图片上传**：预留 el-upload 组件，接口路径 `/common/upload`
4. **分页参数**：统一 `{ page: number, pageSize: number }` 格式
5. **响应码**：统一 `code: 200` 表示成功

## 六、验证步骤

1. `pnpm start` 启动开发服务器，确认无编译错误
2. 访问 `http://localhost:9501`，验证：

   * 左侧导航栏正常展示 5 个菜单

   * 点击菜单可正常切换页面

   * 各页面表格/搜索/分页/弹窗 UI 正常渲染
3. 打开浏览器控制台，确认 API 请求路径正确（虽然后端未就绪会报错，但请求结构正确即可）

