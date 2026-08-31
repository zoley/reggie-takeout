<template>
  <van-popup
    v-model:show="isShow"
    position="right"
    :style="{ width: '100%', height: '100%' }"
    class="food-detail"
  >
    <van-nav-bar title="商品详情" left-arrow fixed placeholder @click-left="closeDetail" />

    <van-image :src="selectedFood.image" fit="cover" width="100%" height="40vh">
      <template #error>
        <div class="image-error">暂无图片</div>
      </template>
    </van-image>

    <div class="food-main">
      <h3 class="food-name">{{ selectedFood.name }}</h3>
      <p class="food-sell">月售 {{ selectedFood.sellCount }} 份 · 好评率 {{ selectedFood.rating }}%</p>
      <div class="food-price">
        <span class="price-main">￥{{ selectedFood.price }}</span>
        <del v-if="selectedFood.oldPrice" class="price-old">￥{{ selectedFood.oldPrice }}</del>
        <div class="food-action">
          <stepper v-if="selectedFood.count" :food="selectedFood" @add="onAdd" />
          <van-button v-else type="primary" round size="small" @click="addCart">加入购物车</van-button>
        </div>
      </div>
    </div>

    <template v-if="selectedFood.info">
      <van-divider />
      <div class="food-desc">
        <h4 class="desc-title">商品介绍</h4>
        <p class="desc-con">{{ selectedFood.info }}</p>
      </div>
    </template>

    <template v-if="selectedFood.ratings">
      <van-divider />
      <div class="food-comments">
        <h4 class="desc-title">商品评价</h4>
        <comment :comments="selectedFood.ratings" @refresh="onRefresh" />
      </div>
    </template>
  </van-popup>
</template>

<script setup>
import { ref } from "vue";
import stepper from "@/components/stepper/index.vue";
import comment from "@/components/comment/index.vue";

const props = defineProps({
  selectedFood: { type: Object, default: () => ({}) },
});
const emit = defineEmits(["detailAddFood"]);

const isShow = ref(false);

function showDetail() {
  isShow.value = true;
}
function closeDetail() {
  isShow.value = false;
}
function onAdd(el) {
  emit("detailAddFood", el);
}
function addCart() {
  props.selectedFood.count = 1;
  emit("detailAddFood", null);
}
function onRefresh() {
  // 内容刷新预留
}

defineExpose({ showDetail });
</script>

<style scoped>
.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  background: #f1f1f1;
  font-size: 16px;
  letter-spacing: 2px;
}
.food-main {
  padding: 16px;
}
.food-name {
  font-size: 16px;
  color: #333;
}
.food-sell {
  font-size: 12px;
  color: #999;
  margin: 8px 0 14px;
}
.food-price {
  position: relative;
  font-size: 12px;
}
.price-main {
  font-size: 16px;
  color: #ee0a24;
  margin-right: 12px;
}
.price-old {
  color: #999;
}
.food-action {
  position: absolute;
  right: 0;
  top: 0;
}
.food-desc,
.food-comments {
  padding: 0 16px 16px;
}
.desc-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}
.desc-con {
  font-size: 12px;
  line-height: 24px;
  color: #666;
}
.food-comments {
  padding-bottom: 24px;
}
</style>