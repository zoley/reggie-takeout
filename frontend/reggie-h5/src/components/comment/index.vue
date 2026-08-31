<template>
  <div class="comment">
    <van-tabs
      v-model:active="activeTab"
      class="comment-tabs"
      @change="emitRefresh"
    >
      <van-tab
        v-for="item in tabListCopy"
        :key="item.type"
        :name="item.type"
        :title="`${item.name} ${item.number}`"
      />
    </van-tabs>

    <van-checkbox
      v-model="hasContent"
      class="comment-switch"
      @change="emitRefresh"
    >
      只看有内容的评价
    </van-checkbox>

    <div class="comment-list">
      <div
        v-for="(item, index) in comments"
        :key="index"
        v-show="isVisible(item)"
        class="comment-item"
      >
        <!-- 菜品详情评价 -->
        <template v-if="mark === 'detailComment'">
          <div class="detail-head">
            <span class="detail-date">{{
              formatDate(item.rateTime, "YYYY-MM-DD HH:mm")
            }}</span>
            <div class="detail-user">
              <span>{{ item.username }}</span>
              <van-image round :src="item.avatar" width="14" height="14" />
            </div>
          </div>
          <div class="detail-body">
            <van-tag
              v-if="item.rateType === 0"
              type="primary"
              plain
              size="small"
              >赞</van-tag
            >
            <van-tag
              v-if="item.rateType === 1"
              type="default"
              plain
              size="small"
              >踩</van-tag
            >
            <p class="detail-text">{{ item.text }}</p>
          </div>
        </template>

        <!-- 店铺评价列表 -->
        <template v-else>
          <div class="rating-item">
            <van-image
              round
              :src="item.avatar"
              width="28"
              height="28"
              class="rating-avatar"
            />
            <div class="rating-body">
              <div class="rating-user">
                <span>{{ item.username }}</span>
                <span class="rating-date">{{
                  formatDate(item.rateTime, "YYYY-MM-DD HH:mm")
                }}</span>
              </div>
              <div class="rating-star">
                <star :score="item.score" :size="14" :letter-space="1" />
                <span v-if="item.deliveryTime" class="rating-delivery"
                  >{{ item.deliveryTime }}分钟送达</span
                >
              </div>
              <p class="rating-text">{{ item.text }}</p>
              <div class="rating-tags">
                <van-tag
                  v-if="item.rateType === 0"
                  type="primary"
                  plain
                  size="small"
                  >赞</van-tag
                >
                <van-tag
                  v-if="item.rateType === 1"
                  type="default"
                  plain
                  size="small"
                  >踩</van-tag
                >
                <van-tag
                  v-for="(tag, ti) in item.recommend"
                  :key="ti"
                  plain
                  size="small"
                  class="recommend-tag"
                  >{{ tag }}</van-tag
                >
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import star from "@/components/star/index.vue";
import { formatDate } from "@/utils";

const POSITIVE = 0;
const NEGATIVE = 1;
const ALL = 2;

const props = defineProps({
  comments: { type: Array, default: () => [] },
  tabList: {
    type: Array,
    default: () => [
      { name: "全部", type: ALL, number: 0 },
      { name: "推荐", type: POSITIVE, number: 0 },
      { name: "吐槽", type: NEGATIVE, number: 0 },
    ],
  },
  mark: { type: String, default: "detailComment" },
});
const emit = defineEmits(["refresh"]);

const activeTab = ref(ALL);
const hasContent = ref(false);

const tabListCopy = computed(() => {
  const positive = props.comments.filter((item) => item.rateType === POSITIVE);
  const negative = props.comments.filter((item) => item.rateType === NEGATIVE);
  return props.tabList.map((tab) => {
    if (tab.type === ALL) return { ...tab, number: props.comments.length };
    if (tab.type === POSITIVE) return { ...tab, number: positive.length };
    if (tab.type === NEGATIVE) return { ...tab, number: negative.length };
    return tab;
  });
});

function isVisible(item) {
  if (hasContent.value && !item.text) return false;
  if (activeTab.value === ALL) return true;
  return activeTab.value === item.rateType;
}

function emitRefresh() {
  emit("refresh");
}
</script>

<style scoped>
.comment-tabs {
  margin: 0 16px;
}
.comment-switch {
  padding: 12px 16px;
  border-bottom: 1px solid #ebedf0;
}
.comment-list {
  padding: 0 16px;
}
.comment-item {
  padding: 14px 0;
  border-bottom: 1px solid #ebedf0;
}
.comment-item:last-child {
  border-bottom: none;
}

.detail-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 12px;
  color: #999;
}
.detail-user {
  display: flex;
  align-items: center;
  gap: 8px;
}
.detail-body {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}
.detail-text {
  flex: 1;
  font-size: 12px;
  line-height: 16px;
  color: #333;
}

.rating-item {
  display: flex;
  align-items: flex-start;
}
.rating-avatar {
  flex: 0 0 28px;
  margin-right: 12px;
}
.rating-body {
  flex: 1;
  min-width: 0;
}
.rating-user {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #333;
}
.rating-date {
  color: #999;
}
.rating-star {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0;
}
.rating-delivery {
  font-size: 12px;
  color: #999;
}
.rating-text {
  font-size: 14px;
  line-height: 20px;
  color: #333;
  margin-bottom: 8px;
}
.rating-tags {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
}
</style>
