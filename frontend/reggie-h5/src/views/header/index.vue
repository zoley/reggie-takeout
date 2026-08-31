<template>
  <div class="store-header">
    <div class="store-bg" :style="{ backgroundImage: `url(${store.avatar || ''})` }"></div>

    <div class="store-top">
      <van-image round :src="store.avatar" width="64" height="64" class="avatar" />
      <div class="store-info">
        <div class="store-name">
          <van-tag type="danger" round class="brand-tag">品牌</van-tag>
          <span class="name-text">{{ store.name }}</span>
        </div>
        <p class="store-desc">{{ store.description }} / {{ store.deliveryTime }}分钟送达</p>
        <p class="store-support">
          <span class="promo" :class="promoClass(firstSupport && firstSupport.type)">{{ promoChar(firstSupport && firstSupport.type) }}</span>
          <span class="support-text">{{ firstSupport ? firstSupport.description : "" }}</span>
        </p>
      </div>
      <div class="support-num" @click="showMask = true">
        <span>{{ (store.supports || []).length }} 个</span>
        <van-icon name="arrow" color="#fff" size="12" />
      </div>
    </div>

    <div class="store-bottom" @click="showMask = true">
      <van-tag plain color="#fff" text-color="#fff" class="bulletin-tag">公告</van-tag>
      <span class="bulletin">{{ store.bulletin }}</span>
      <van-icon name="arrow" color="#fff" size="12" />
    </div>

    <van-popup v-model:show="showMask" position="bottom" round closeable>
      <div class="mask-content">
        <h2 class="mask-name">{{ store.name }}</h2>
        <div class="mask-star">
          <star :score="store.score" :size="18" />
        </div>
        <div class="mask-line">优惠信息</div>
        <div v-for="(item, i) in store.supports" :key="i" class="mask-item">
          <span class="promo" :class="promoClass(item.type)">{{ promoChar(item.type) }}</span>
          <span>{{ item.description }}</span>
        </div>
        <div class="mask-line">商家公告</div>
        <p class="mask-bulletin">{{ store.bulletin }}</p>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import star from "@/components/star/index.vue";

const props = defineProps({
  store: { type: Object, default: () => ({}) },
});

const showMask = ref(false);

const PROMOS = {
  0: { char: "减", cls: "promo-red" },
  1: { char: "折", cls: "promo-blue" },
  2: { char: "特", cls: "promo-green" },
  3: { char: "惠", cls: "promo-orange" },
};
function promoChar(type) {
  return PROMOS[type]?.char || "";
}
function promoClass(type) {
  return PROMOS[type]?.cls || "";
}

const firstSupport = computed(() => (props.store.supports || [])[0] || null);
</script>

<style scoped>
.store-header {
  position: relative;
  color: #fff;
  overflow: hidden;
}
.store-bg {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  filter: blur(12px);
  transform: scale(1.2);
}
.store-header::after {
  content: "";
  position: absolute;
  inset: 0;
  background: rgba(7, 17, 27, 0.6);
}
.store-top,
.store-bottom {
  position: relative;
  z-index: 1;
}
.store-top {
  display: flex;
  align-items: center;
  padding: 22px 16px 18px;
}
.avatar {
  flex: 0 0 64px;
  margin-right: 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}
.store-info {
  flex: 1;
  min-width: 0;
}
.store-name {
  display: flex;
  align-items: center;
  gap: 8px;
}
.brand-tag {
  flex: 0 0 auto;
}
.name-text {
  font-size: 16px;
  font-weight: 600;
}
.store-desc {
  font-size: 12px;
  margin: 8px 0;
  opacity: 0.9;
}
.store-support {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
}
.support-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.support-num {
  display: flex;
  align-items: center;
  gap: 4px;
  align-self: flex-end;
  padding: 0 12px;
  height: 28px;
  line-height: 28px;
  border-radius: 14px;
  font-size: 12px;
  background: rgba(0, 0, 0, 0.2);
}
.store-bottom {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 16px;
  height: 30px;
  font-size: 12px;
  background: rgba(7, 17, 27, 0.2);
}
.bulletin-tag {
  flex: 0 0 auto;
}
.bulletin {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.promo {
  display: inline-block;
  width: 16px;
  height: 16px;
  line-height: 16px;
  text-align: center;
  border-radius: 3px;
  font-size: 11px;
  color: #fff;
  flex: 0 0 auto;
}
.promo-red {
  background: #ee0a24;
}
.promo-blue {
  background: #1989fa;
}
.promo-green {
  background: #07c160;
}
.promo-orange {
  background: #ff976a;
}

.mask-content {
  padding: 28px 24px 32px;
  color: #333;
}
.mask-name {
  text-align: center;
  font-size: 16px;
  font-weight: 600;
}
.mask-star {
  display: flex;
  justify-content: center;
  margin: 16px 0;
}
.mask-line {
  position: relative;
  text-align: center;
  font-size: 13px;
  color: #999;
  margin: 18px 0 10px;
}
.mask-line::before,
.mask-line::after {
  content: "";
  position: absolute;
  top: 50%;
  width: 30%;
  height: 1px;
  background: #ebedf0;
}
.mask-line::before {
  left: 0;
}
.mask-line::after {
  right: 0;
}
.mask-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  line-height: 28px;
}
.mask-bulletin {
  font-size: 12px;
  line-height: 22px;
  color: #666;
}
</style>