<template>
  <div class="not-found">
    <van-empty image="error" description="页面不存在或已被移除" />
    <div class="actions">
      <van-button type="primary" round block @click="goHome">返回首页</van-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

function goHome() {
  router.replace("/");
}
</script>

<style scoped>
.not-found {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.actions {
  width: 200px;
}
</style>