<template>
  <div class="goods">
    <div class="goods-body">
      <van-sidebar v-model="current" class="sidebar" @change="onSidebarChange">
        <van-sidebar-item
          v-for="(item, index) in goods"
          :key="index"
          :title="item.name"
        />
      </van-sidebar>

      <div ref="listRef" class="goods-list" @scroll="onScroll">
        <div
          v-for="(item, index) in goods"
          :key="index"
          :ref="(el) => setSectionRef(el, index)"
          class="section"
        >
          <div class="section-title">{{ item.name }}</div>
          <div
            v-for="(food, fi) in item.foods"
            :key="fi"
            class="food"
            @click="onSelectFood(food)"
          >
            <van-image
              :src="food.icon"
              width="56"
              height="56"
              radius="4"
              class="food-img"
            />
            <div class="food-info">
              <div class="food-name">{{ food.name }}</div>
              <div v-if="food.description" class="food-desc">
                {{ food.description }}
              </div>
              <div class="food-sell">
                月售 {{ food.sellCount }} 份 · 好评率 {{ food.rating }}%
              </div>
              <div class="food-price">
                <span class="price-main">￥{{ food.price }}</span>
                <del v-if="food.oldPrice" class="price-old"
                  >￥{{ food.oldPrice }}</del
                >
              </div>
            </div>
            <stepper class="food-stepper" :food="food" @add="onAddFood" />
          </div>
        </div>
      </div>
    </div>

    <shopcart
      ref="shopcartRef"
      :select-foods="selectFoods"
      :delivery-price="seller.deliveryPrice"
      :min-price="seller.minPrice"
    />
    <food-detail
      ref="foodDetailRef"
      :selected-food="selectedFood"
      @detail-add-food="onAddFood"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import stepper from "@/components/stepper/index.vue";
import shopcart from "@/components/shopcart/index.vue";
import foodDetail from "@/components/foodDetail/index.vue";
import { getGoods } from "@/api/store";

defineProps({
  seller: { type: Object, default: () => ({}) },
});

const goods = ref([]);
const current = ref(0);
const listRef = ref(null);
const sectionRefs = ref([]);
const selectedFood = ref({});
const shopcartRef = ref(null);
const foodDetailRef = ref(null);

function setSectionRef(el, index) {
  sectionRefs.value[index] = el;
}

const selectFoods = computed(() => {
  const result = [];
  goods.value.forEach((group) => {
    (group.foods || []).forEach((food) => {
      if (food.count) result.push(food);
    });
  });
  return result;
});

function loadGoods() {
  getGoods()
    .then((data) => {
      goods.value = data || [];
    })
    .catch(() => {});
}
onMounted(loadGoods);

function onSidebarChange(index) {
  const el = sectionRefs.value[index];
  if (el && listRef.value) {
    listRef.value.scrollTo({ top: el.offsetTop, behavior: "smooth" });
  }
}

function onScroll() {
  if (!listRef.value) return;
  const top = listRef.value.scrollTop;
  let idx = 0;
  sectionRefs.value.forEach((el, i) => {
    if (el && el.offsetTop <= top + 10) idx = i;
  });
  current.value = idx;
}

function onSelectFood(food) {
  selectedFood.value = food;
  foodDetailRef.value && foodDetailRef.value.showDetail();
}

function onAddFood(el) {
  if (el) {
    shopcartRef.value && shopcartRef.value.drop(el);
  }
}
</script>

<style scoped>
.goods {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.goods-body {
  flex: 1;
  min-height: 0;
  display: flex;
}
.sidebar {
  height: 100%;
  flex: 0 0 80px;
}
.goods-list {
  flex: 1;
  position: relative;
  height: 100%;
  overflow-y: auto;
  padding-bottom: 60px;
  background: #fff;
}
.section-title {
  height: 26px;
  line-height: 26px;
  padding-left: 12px;
  font-size: 12px;
  color: #666;
  background: #f3f5f7;
  border-left: 2px solid #d9dde1;
}
.food {
  display: flex;
  align-items: flex-start;
  margin: 0 16px;
  padding: 14px 0;
  border-bottom: 1px solid #ebedf0;
}
.food:last-child {
  border-bottom: none;
}
.food-img {
  flex: 0 0 56px;
  margin-right: 10px;
}
.food-info {
  flex: 1;
  min-width: 0;
}
.food-name {
  font-size: 14px;
  color: #333;
}
.food-desc,
.food-sell {
  font-size: 12px;
  color: #999;
  margin: 4px 0;
  line-height: 16px;
}
.food-price {
  font-size: 12px;
}
.price-main {
  font-size: 14px;
  color: #ee0a24;
  margin-right: 12px;
}
.price-old {
  color: #999;
}
.food-stepper {
  margin-left: 8px;
  margin-top: 22px;
}
</style>
