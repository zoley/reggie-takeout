<template>
  <el-container class="layout-container">
    <!-- 左侧侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
      <div class="logo">
        <span v-if="!isCollapse">瑞吉外卖</span>
        <span v-else>瑞吉</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/employee">
          <el-icon><User /></el-icon>
          <template #title>员工管理</template>
        </el-menu-item>
        <el-menu-item index="/category">
          <el-icon><MenuIcon /></el-icon>
          <template #title>分类管理</template>
        </el-menu-item>
        <el-menu-item index="/dish">
          <el-icon><Bowl /></el-icon>
          <template #title>菜品管理</template>
        </el-menu-item>
        <el-menu-item index="/setmeal">
          <el-icon><Food /></el-icon>
          <template #title>套餐管理</template>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><List /></el-icon>
          <template #title>订单明细</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="page-title">{{ currentPageTitle }}</span>
        </div>
        <div class="header-right">
          <span>管理员</span>
          <el-button link @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  User,
  Menu as MenuIcon,
  Bowl,
  Food,
  List,
  Fold,
  Expand,
} from "@element-plus/icons-vue";

const route = useRoute();
const router = useRouter();
const isCollapse = ref(false);

const activeMenu = computed(() => route.path);

const currentPageTitle = computed(() => route.meta?.title || "后台管理");

const handleLogout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("user");
  router.push("/login");
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 2px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar .el-menu {
  border-right: none;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.collapse-btn:hover {
  color: #409eff;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #666;
}

.main-content {
  background-color: #f0f2f5;
  overflow-y: auto;
  padding: 16px;
}
</style>
