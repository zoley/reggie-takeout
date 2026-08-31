<template>
  <div class="layout">
    <store-header :store="store" />

    <van-tabs v-model:active="active" class="layout-tabs" @click-tab="onTabClick">
      <van-tab name="goods" title="商品" />
      <van-tab name="ratings" title="评价" />
      <van-tab name="seller" title="商家" />
    </van-tabs>

    <div class="layout-content">
      <router-view :store="store" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import storeHeader from "@/views/header/index.vue";
import { getStore } from "@/api/store";

const route = useRoute();
const router = useRouter();

const store = ref({});
const active = ref("goods");
const tabs = ["goods", "ratings", "seller"];

function syncActive() {
  active.value = tabs.includes(route.name) ? route.name : "goods";
}
watch(() => route.name, syncActive, { immediate: true });

function onTabClick({ name }) {
  if (name && name !== route.name) {
    router.push({ name });
  }
}

function loadStore() {
  getStore()
    .then((data) => {
      store.value = data || {};
    })
    .catch(() => {});
}
loadStore();
</script>

<style scoped>
.layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
.layout-tabs {
  flex: 0 0 44px;
}
.layout-content {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}
</style>