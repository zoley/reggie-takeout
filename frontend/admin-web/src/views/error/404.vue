<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-icon">
        <el-icon class="icon-404">
          <DocumentRemove />
        </el-icon>
      </div>
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面未找到</h2>
      <p class="error-desc">抱歉，您访问的页面不存在或已被删除</p>
      <el-button type="primary" size="large" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>
    <div class="floating-elements">
      <div class="float-item food-1">
        <el-icon><Food /></el-icon>
      </div>
      <div class="float-item food-2">
        <el-icon><Bowl /></el-icon>
      </div>
      <div class="float-item food-3">
        <el-icon><Coffee /></el-icon>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DocumentRemove, ArrowLeft, Food, Bowl, Coffee } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

const router = useRouter();

function goBack() {
  const token = localStorage.getItem("token");
  router.push(token ? "/employee" : "/login");
}
</script>

<style scoped>
.not-found-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.not-found-content {
  text-align: center;
  z-index: 1;
  animation: fadeInUp 0.8s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.error-icon {
  width: 120px;
  height: 120px;
  margin: 0 auto 30px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.icon-404 {
  font-size: 60px;
  color: #fff;
}

.error-code {
  font-size: 96px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 16px;
  text-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.8;
  }
}

.error-title {
  font-size: 28px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 12px;
}

.error-desc {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0 0 32px;
}

.go-back-btn {
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 30px;
  background: #fff;
  color: #667eea;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
}

.go-back-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(255, 255, 255, 0.3);
}

.floating-elements {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.float-item {
  position: absolute;
  font-size: 48px;
  color: rgba(255, 255, 255, 0.15);
}

.food-1 {
  top: 20%;
  left: 10%;
  animation: float 4s ease-in-out infinite;
}

.food-2 {
  top: 60%;
  right: 15%;
  animation: float 5s ease-in-out infinite 1s;
}

.food-3 {
  bottom: 25%;
  left: 20%;
  animation: float 6s ease-in-out infinite 2s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}
</style>
