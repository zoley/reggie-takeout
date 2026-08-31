<template>
  <div class="shopcart">
    <div class="shopcart-bar">
      <div class="cart-icon" :class="{ highlight: totalCount > 0 }" @click="onToggle">
        <van-icon name="cart-o" size="22" :color="totalCount > 0 ? '#fff' : 'rgba(255,255,255,0.4)'" />
        <van-badge v-if="totalCount > 0" :content="totalCount" class="cart-badge" />
      </div>
      <div class="cart-con">
        <span class="con-price" :class="{ highlight: totalPrice > 0 }">￥{{ totalPrice }}</span>
        <span class="con-desc">另需配送费￥{{ deliveryPrice }}元</span>
      </div>
      <div class="cart-pay" :class="{ enough: totalPrice >= minPrice }" @click="toPay">{{ payDesc }}</div>
    </div>

    <van-popup v-model:show="open" position="bottom" round>
      <div class="cart-list">
        <div class="cart-list-title">
          <span>购物车</span>
          <span class="clear" @click="clearList">清空</span>
        </div>
        <div class="cart-list-body">
          <div v-for="(item, index) in selectFoods" :key="index" class="cart-item">
            <span class="item-name">{{ item.name }}</span>
            <span class="item-price">￥{{ item.count * item.price }}</span>
            <stepper :food="item" @add="onAdd" />
          </div>
          <van-empty v-if="!selectFoods.length" description="购物车空空如也" image-size="60" />
        </div>
      </div>
    </van-popup>

    <!-- 加入购物车小球动画 -->
    <div v-for="(ball, index) in balls" :key="index">
      <transition @before-enter="beforeDrop" @enter="dropping" @after-enter="afterDrop">
        <div v-show="ball.show" class="ball">
          <div class="ball-inner"></div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { showToast } from "vant";
import stepper from "@/components/stepper/index.vue";

const props = defineProps({
  selectFoods: { type: Array, default: () => [] },
  minPrice: { type: Number, default: 20 },
  deliveryPrice: { type: Number, default: 4 },
});

const totalCount = computed(() =>
  props.selectFoods.reduce((prev, cur) => prev + (cur.count || 0), 0),
);
const totalPrice = computed(() =>
  props.selectFoods.reduce((prev, cur) => prev + (cur.count || 0) * (cur.price || 0), 0),
);
const payDesc = computed(() => {
  if (totalPrice.value === 0) return `￥${props.minPrice}起送`;
  if (totalPrice.value < props.minPrice) return `还差￥${props.minPrice - totalPrice.value}起送`;
  return "去结算";
});

const open = ref(false);
const balls = ref(
  new Array(7).fill(null).map(() => ({ show: false, el: null })),
);

function onToggle() {
  if (!totalCount.value) return;
  open.value = !open.value;
}
function clearList() {
  props.selectFoods.forEach((food) => (food.count = 0));
  open.value = false;
}
function toPay() {
  // TODO: 后续对接真实结算/下单流程
  showToast(`待支付 ￥${totalPrice.value} 元`);
}

function onAdd(el) {
  drop(el);
}

function drop(el) {
  const ball = balls.value.find((b) => !b.show);
  if (!ball) return;
  ball.show = true;
  ball.el = el;
}

function beforeDrop(el) {
  const ball = balls.value.find((b) => b.show);
  if (!ball || !ball.el) return;
  const rect = ball.el.getBoundingClientRect();
  const x = rect.left - 32;
  const y = -(window.innerHeight - rect.top - 16);
  el.style.transform = `translate3d(0, ${y}px, 0)`;
  const inner = el.firstElementChild;
  inner.style.transform = `translate3d(${x}px, 0, 0)`;
}
function dropping(el) {
  // 触发浏览器重绘
  void el.offsetHeight;
  el.style.transform = "translate3d(0, 0, 0)";
  if (el.firstElementChild) {
    el.firstElementChild.style.transform = "translate3d(0, 0, 0)";
  }
}
function afterDrop(el) {
  const ball = balls.value.find((b) => b.show);
  if (ball) {
    ball.show = false;
    ball.el = null;
    el.style.display = "none";
  }
}

defineExpose({ drop });
</script>

<style scoped>
.shopcart-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 48px;
  z-index: 50;
  display: flex;
  align-items: center;
  background: #141d27;
  color: rgba(255, 255, 255, 0.4);
}
.cart-icon {
  position: relative;
  flex: 0 0 80px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.cart-icon::before {
  content: "";
  position: absolute;
  left: 12px;
  right: 12px;
  bottom: 2px;
  height: 44px;
  border-radius: 50%;
  background: #2b343c;
}
.cart-icon > .van-icon {
  position: relative;
  z-index: 2;
}
.cart-badge {
  position: absolute;
  top: 2px;
  right: 14px;
  z-index: 3;
}
.cart-con {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
  overflow: hidden;
}
.con-price {
  font-size: 16px;
}
.con-price.highlight {
  color: #fff;
}
.con-desc {
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.cart-pay {
  flex: 0 0 96px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  background: #2b343c;
}
.cart-pay.enough {
  color: #fff;
  background: #07c160;
}

.cart-list-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 40px;
  padding: 0 16px;
  font-size: 14px;
  color: #333;
  background: #f3f5f7;
}
.cart-list-title .clear {
  color: #1989fa;
}
.cart-list-body {
  max-height: 50vh;
  overflow-y: auto;
  padding: 0 16px;
}
.cart-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 48px;
  font-size: 14px;
  color: #333;
  border-bottom: 1px solid #ebedf0;
}
.item-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.item-price {
  margin: 0 12px;
  color: #ee0a24;
}

.ball {
  position: fixed;
  left: 32px;
  bottom: 16px;
  z-index: 200;
  transition: all 0.4s cubic-bezier(0.49, -0.29, 0.75, 0.41);
}
.ball-inner {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #1989fa;
  transition: all 0.4s linear;
}
</style>