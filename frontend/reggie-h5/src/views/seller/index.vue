<template>
  <div class="seller">
    <div class="overview">
      <div class="overview-head">
        <div class="head-left">
          <span class="name">{{ seller.name }}</span>
          <p class="head-meta">
            <star :score="seller.score" :size="16" />
            <span class="sell-count">月售 {{ seller.sellCount }} 单</span>
          </p>
        </div>
        <div class="head-right" @click="isCollection">
          <van-icon :name="collection ? 'like' : 'like-o'" :color="collection ? '#ee0a24' : '#999'" size="22" />
          <span class="collect-text">{{ collection ? "已收藏" : "收藏" }}</span>
        </div>
      </div>

      <div class="overview-body">
        <div class="cell">
          <span class="cell-desc">起送价</span>
          <span><b>{{ seller.minPrice }}</b> 元</span>
        </div>
        <div class="cell">
          <span class="cell-desc">商家配送</span>
          <span><b>{{ seller.deliveryPrice }}</b> 元</span>
        </div>
        <div class="cell">
          <span class="cell-desc">平均配送时间</span>
          <span><b>{{ seller.deliveryTime }}</b> 分钟</span>
        </div>
      </div>
    </div>

    <template v-if="seller.bulletin">
      <van-divider />
      <div class="block">
        <h3 class="block-title">公告与活动</h3>
        <p class="block-con">{{ seller.bulletin }}</p>
      </div>
    </template>

    <van-divider v-if="supports.length" />
    <div v-if="supports.length" class="block">
      <h3 class="block-title">活动</h3>
      <div v-for="(item, i) in supports" :key="i" class="support-item">
        <span class="promo" :class="promoClass(item.type)">{{ promoChar(item.type) }}</span>
        <span>{{ item.description }}</span>
      </div>
    </div>

    <template v-if="pics.length">
      <van-divider />
      <div class="block">
        <h3 class="block-title">商家实景</h3>
        <div class="imgs">
          <van-image
            v-for="(pic, i) in pics"
            :key="i"
            :src="pic"
            width="120"
            height="90"
            radius="4"
            fit="cover"
            class="img"
            @click="previewImage(i)"
          />
        </div>
      </div>
    </template>

    <van-divider v-if="infos.length" />
    <div v-if="infos.length" class="block">
      <h3 class="block-title">商家信息</h3>
      <p v-for="(info, i) in infos" :key="i" class="info-item">{{ info }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { showImagePreview } from "vant";
import star from "@/components/star/index.vue";

const props = defineProps({
  seller: { type: Object, default: () => ({}) },
});

const collection = ref(false);

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

const supports = computed(() => props.seller.supports || []);
const pics = computed(() => (Array.isArray(props.seller.pics) ? props.seller.pics : []));
const infos = computed(() => (Array.isArray(props.seller.infos) ? props.seller.infos : []));

function isCollection() {
  collection.value = !collection.value;
}
function previewImage(index) {
  showImagePreview({
    images: pics.value,
    startPosition: index,
  });
}
</script>

<style scoped>
.seller {
  height: 100%;
  overflow-y: auto;
  background: #fff;
  padding-bottom: 60px;
}
.overview-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #ebedf0;
}
.name {
  font-size: 14px;
  color: #333;
  display: block;
  margin-bottom: 8px;
}
.head-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}
.sell-count {
  font-size: 12px;
  color: #666;
}
.head-right {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}
.collect-text {
  font-size: 12px;
  color: #999;
}
.overview-body {
  display: flex;
  padding: 16px 0;
}
.cell {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #666;
}
.cell:nth-child(2) {
  border-left: 1px solid #ebedf0;
  border-right: 1px solid #ebedf0;
}
.cell b {
  font-size: 22px;
  font-weight: 400;
  color: #333;
}
.block {
  padding: 16px;
}
.block-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}
.block-con {
  font-size: 12px;
  line-height: 22px;
  color: #ee0a24;
}
.support-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  line-height: 28px;
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
.imgs {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.img {
  flex: 0 0 auto;
}
.info-item {
  font-size: 12px;
  line-height: 22px;
  color: #333;
  padding: 10px 0;
  border-top: 1px solid #ebedf0;
}
</style>