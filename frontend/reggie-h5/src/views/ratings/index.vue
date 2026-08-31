<template>
  <div class="ratings">
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <div class="overview">
        <div class="overview-left">
          <div class="score">{{ seller.score }}</div>
          <div class="score-desc">综合评分</div>
          <div class="score-rate">高于周边商家{{ seller.rankRate }}%</div>
        </div>
        <div class="overview-right">
          <div class="cell">
            <span class="cell-label">服务态度</span>
            <star :score="seller.serviceScore" :size="16" />
            <span class="cell-value">{{ seller.serviceScore }}</span>
          </div>
          <div class="cell">
            <span class="cell-label">描述相符</span>
            <star :score="seller.foodScore" :size="16" />
            <span class="cell-value">{{ seller.foodScore }}</span>
          </div>
          <div class="cell">
            <span class="cell-label">送达时间</span>
            <span class="cell-value">{{ seller.deliveryTime }}分钟</span>
          </div>
        </div>
      </div>

      <van-divider class="divider" />

      <comment :comments="ratings" mark="ratingComment" :tab-list="tabList" @refresh="onRefreshList" />
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import star from "@/components/star/index.vue";
import comment from "@/components/comment/index.vue";
import { getRatings } from "@/api/store";

defineProps({
  seller: { type: Object, default: () => ({}) },
});

const ratings = ref([]);
const refreshing = ref(false);
const tabList = [
  { name: "全部", type: 2, number: 0 },
  { name: "满意", type: 0, number: 0 },
  { name: "不满意", type: 1, number: 0 },
];

function load() {
  return getRatings()
    .then((data) => {
      ratings.value = data || [];
    })
    .catch(() => {});
}

function onRefresh() {
  load().finally(() => {
    refreshing.value = false;
  });
}
function onRefreshList() {
  // 评价筛选变化时无需重新请求，仅为占位
}

onMounted(() => {
  load();
});
</script>

<style scoped>
.ratings {
  height: 100%;
  overflow-y: auto;
  background: #fff;
  padding-bottom: 60px;
}
.overview {
  display: flex;
  padding: 18px 0;
}
.overview-left {
  flex: 0 0 36%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-right: 1px solid #ebedf0;
}
.score {
  font-size: 24px;
  color: #ff9900;
}
.score-desc {
  font-size: 12px;
  color: #333;
  margin: 8px 0;
}
.score-rate {
  font-size: 12px;
  color: #999;
}
.overview-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
  padding-left: 20px;
}
.cell {
  display: flex;
  align-items: center;
}
.cell-label {
  flex: 0 0 60px;
  font-size: 12px;
  color: #666;
}
.cell-value {
  margin-left: 8px;
  font-size: 13px;
  color: #ff9900;
}
.divider {
  margin: 0;
}
</style>