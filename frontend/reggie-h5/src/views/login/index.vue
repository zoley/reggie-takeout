<template>
  <div class="login">
    <!-- 背景光晕氛围 -->
    <div class="login__glow" aria-hidden="true">
      <span class="glow glow--amber"></span>
      <span class="glow glow--tomato"></span>
    </div>
    <!-- 颗粒质感纹理 -->
    <div class="login__grain" aria-hidden="true"></div>

    <div class="login__inner">
      <!-- 品牌标识 -->
      <header class="brand reveal" style="--d: 0ms">
        <div class="brand__mark" aria-hidden="true">瑞</div>
        <div class="brand__meta">
          <h1 class="brand__name">瑞吉外卖</h1>
          <p class="brand__slogan">热气腾腾 · 随时送达</p>
        </div>
      </header>

      <!-- 登录表单卡片 -->
      <section class="panel">
        <h2 class="panel__title reveal" style="--d: 80ms">欢迎回来</h2>
        <p class="panel__sub reveal" style="--d: 140ms">手机号登录，解锁今日份美味</p>

        <form class="form" @submit.prevent="onSubmit">
          <!-- 手机号 -->
          <div class="field reveal" style="--d: 200ms">
            <label class="field__label" for="phone">手机号</label>
            <div class="field__box">
              <span class="field__prefix">+86</span>
              <input
                id="phone"
                v-model="phone"
                class="field__input"
                type="tel"
                inputmode="numeric"
                autocomplete="tel"
                maxlength="11"
                placeholder="请输入 11 位手机号"
                @input="onPhoneInput"
              />
              <button
                v-if="phone"
                type="button"
                class="field__clear"
                aria-label="清空手机号"
                @click="phone = ''"
              >
                ×
              </button>
            </div>
          </div>

          <!-- 验证码 -->
          <div class="field reveal" style="--d: 260ms">
            <label class="field__label" for="code">验证码</label>
            <div class="field__box">
              <input
                id="code"
                v-model="code"
                class="field__input"
                type="text"
                inputmode="numeric"
                autocomplete="one-time-code"
                maxlength="6"
                placeholder="请输入验证码"
                @input="onCodeInput"
              />
              <button
                type="button"
                class="field__code"
                :disabled="counting || !validPhone"
                @click="onSendCode"
              >
                {{ counting ? `${countdown}s 后重发` : "获取验证码" }}
              </button>
            </div>
          </div>

          <!-- 协议 -->
          <div class="agree reveal" style="--d: 320ms" @click="agreed = !agreed">
            <span class="agree__box" :class="{ 'is-on': agreed }" aria-hidden="true">
              <svg
                v-if="agreed"
                class="agree__tick"
                viewBox="0 0 12 10"
                fill="none"
              >
                <path
                  d="M1 5l3.2 3.2L11 1.5"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
              </svg>
            </span>
            <span class="agree__text">
              我已阅读并同意
              <a class="agree__link" href="javascript:;" @click.stop>《用户协议》</a>
              与
              <a class="agree__link" href="javascript:;" @click.stop>《隐私政策》</a>
            </span>
          </div>

          <!-- 提交 -->
          <button
            type="submit"
            class="submit reveal"
            style="--d: 380ms"
            :class="{ 'is-disabled': !canSubmit }"
            :disabled="!canSubmit"
          >
            <span class="submit__label">登录 / 注册</span>
            <span class="submit__arrow" aria-hidden="true">→</span>
          </button>
        </form>
      </section>

      <p class="foot reveal" style="--d: 440ms">登录即代表你已同意瑞吉外卖相关服务条款</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onUnmounted, ref } from "vue";
import { useRouter } from "vue-router";
import { showSuccessToast } from "vant";
import { sendSmsCode, loginByPhone } from "@/api/auth";
import { useUserStore } from "@/stores/user";

const router = useRouter();
const userStore = useUserStore();

const phone = ref("");
const code = ref("");
const agreed = ref(true);
const counting = ref(false);
const countdown = ref(60);
let timer = null;

const validPhone = computed(() => /^1[3-9]\d{9}$/.test(phone.value));
const canSubmit = computed(
  () => validPhone.value && code.value.length === 6 && agreed.value,
);

function onPhoneInput() {
  phone.value = phone.value.replace(/\D/g, "");
}

function onCodeInput() {
  code.value = code.value.replace(/\D/g, "");
}

function startCountdown() {
  counting.value = true;
  countdown.value = 60;
  timer = setInterval(() => {
    countdown.value -= 1;
    if (countdown.value <= 0) {
      clearInterval(timer);
      timer = null;
      counting.value = false;
    }
  }, 1000);
}

function onSendCode() {
  if (!validPhone.value || counting.value) return;
  sendSmsCode({ phone: phone.value })
    .then(() => {
      showSuccessToast("验证码已发送");
      startCountdown();
    })
    .catch(() => {});
}

function onSubmit() {
  if (!canSubmit.value) return;
  loginByPhone({ phone: phone.value, code: code.value })
    .then((user) => {
      userStore.setLogin(user);
      showSuccessToast("登录成功");
      router.replace("/");
    })
    .catch(() => {});
}

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>

<style scoped>
.login {
  --font-serif: "Songti SC", "STSong", "Noto Serif SC", "Source Han Serif SC",
    "SimSun", serif;

  position: relative;
  min-height: 100dvh;
  padding: 48px 24px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  color: #2a201b;
  background: #faf5ec;
}

/* 背景光晕 */
.login__glow {
  position: absolute;
  inset: 0;
  z-index: 0;
}
.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
}
.glow--amber {
  width: 320px;
  height: 320px;
  top: -90px;
  right: -70px;
  opacity: 0.5;
  background: radial-gradient(circle at 30% 30%, #ffd98a, #f6c453 60%, transparent);
}
.glow--tomato {
  width: 360px;
  height: 360px;
  bottom: -130px;
  left: -110px;
  opacity: 0.34;
  background: radial-gradient(circle at 60% 40%, #ff9d80, #ee6a4a 55%, transparent);
}

/* 颗粒质感 */
.login__grain {
  position: absolute;
  inset: 0;
  z-index: 1;
  pointer-events: none;
  opacity: 0.06;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='140' height='140'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E");
}

.login__inner {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 420px;
}

/* 品牌 */
.brand {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 40px;
}
.brand__mark {
  width: 52px;
  height: 52px;
  flex: 0 0 52px;
  display: grid;
  place-items: center;
  font-family: var(--font-serif);
  font-size: 26px;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(135deg, #ff8a5c, #e6432c);
  border-radius: 16px;
  box-shadow: 0 12px 28px -12px rgba(230, 67, 44, 0.6);
}
.brand__name {
  font-family: var(--font-serif);
  font-size: 24px;
  font-weight: 700;
  letter-spacing: 0.02em;
}
.brand__slogan {
  margin-top: 2px;
  font-size: 12px;
  letter-spacing: 0.12em;
  color: #a59384;
}

/* 卡片 */
.panel {
  padding: 32px 26px;
  background: rgba(255, 255, 255, 0.86);
  backdrop-filter: blur(14px);
  border-radius: 28px;
  box-shadow:
    0 30px 60px -30px rgba(42, 32, 27, 0.35),
    inset 0 2px 0 rgba(255, 255, 255, 0.9);
}
.panel__title {
  font-family: var(--font-serif);
  font-size: 30px;
  font-weight: 700;
  letter-spacing: 0.01em;
}
.panel__sub {
  margin-top: 8px;
  font-size: 13px;
  color: #a59384;
}

/* 表单 */
.form {
  margin-top: 28px;
}
.field {
  margin-bottom: 22px;
}
.field__label {
  display: block;
  margin-bottom: 8px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.08em;
  color: #a59384;
}
.field__box {
  position: relative;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 4px;
  border-bottom: 1.5px solid rgba(42, 32, 27, 0.12);
  transition: border-color 0.25s ease;
}
.field__box::after {
  content: "";
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1.5px;
  height: 1.5px;
  background: linear-gradient(90deg, #e6432c, #ff8a5c);
  transform: scaleX(0);
  transform-origin: left;
  transition: transform 0.3s ease;
}
.field__box:focus-within {
  border-color: transparent;
}
.field__box:focus-within::after {
  transform: scaleX(1);
}
.field__prefix {
  padding-right: 10px;
  border-right: 1px solid rgba(42, 32, 27, 0.15);
  font-size: 15px;
  font-weight: 600;
}
.field__input {
  flex: 1;
  min-width: 0;
  font-size: 17px;
  letter-spacing: 0.02em;
  color: #2a201b;
  background: transparent;
  border: none;
  outline: none;
}
.field__input::placeholder {
  color: #c9bbab;
}
.field__clear {
  flex: 0 0 auto;
  width: 22px;
  height: 22px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  font-size: 16px;
  line-height: 1;
  color: #b7a896;
  background: rgba(42, 32, 27, 0.06);
  transition: background 0.2s ease;
}
.field__clear:hover {
  background: rgba(42, 32, 27, 0.12);
}
.field__code {
  flex: 0 0 auto;
  padding: 7px 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 600;
  color: #e6432c;
  background: rgba(230, 67, 44, 0.08);
  transition:
    transform 0.15s ease,
    background 0.2s ease,
    color 0.2s ease;
}
.field__code:not(:disabled):active {
  transform: scale(0.94);
}
.field__code:disabled {
  color: #c9bbab;
  background: rgba(42, 32, 27, 0.05);
}

/* 协议 */
.agree {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin-top: 26px;
  cursor: pointer;
  user-select: none;
}
.agree__box {
  flex: 0 0 18px;
  width: 18px;
  height: 18px;
  margin-top: 1px;
  display: grid;
  place-items: center;
  border: 1.5px solid rgba(42, 32, 27, 0.2);
  border-radius: 6px;
  color: #fff;
  transition:
    background 0.2s ease,
    border-color 0.2s ease;
}
.agree__box.is-on {
  background: #e6432c;
  border-color: #e6432c;
}
.agree__tick {
  width: 12px;
  height: 10px;
  display: block;
}
.agree__text {
  font-size: 12px;
  line-height: 1.5;
  color: #a59384;
}
.agree__link {
  font-weight: 600;
  color: #2a201b;
}

/* 提交按钮 */
.submit {
  width: 100%;
  margin-top: 26px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border: none;
  border-radius: 999px;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: #fff;
  cursor: pointer;
  background: linear-gradient(135deg, #ff8a5c 0%, #e6432c 100%);
  box-shadow: 0 16px 32px -14px rgba(230, 67, 44, 0.65);
  transition:
    transform 0.15s ease,
    box-shadow 0.25s ease,
    opacity 0.2s ease;
}
.submit:not(:disabled):active {
  transform: translateY(1px) scale(0.99);
}
.submit.is-disabled {
  opacity: 0.45;
  box-shadow: none;
  cursor: not-allowed;
}
.submit__arrow {
  font-size: 18px;
  transition: transform 0.2s ease;
}
.submit:not(:disabled):hover .submit__arrow {
  transform: translateX(4px);
}

.foot {
  margin-top: 24px;
  text-align: center;
  font-size: 11px;
  color: #c0b3a4;
}

/* 载入交错动画 */
.reveal {
  opacity: 0;
  transform: translateY(14px);
  animation: rise 0.6s cubic-bezier(0.22, 1, 0.36, 1) forwards;
  animation-delay: var(--d, 0ms);
}
@keyframes rise {
  to {
    opacity: 1;
    transform: none;
  }
}
@media (prefers-reduced-motion: reduce) {
  .reveal {
    animation: none;
    opacity: 1;
    transform: none;
  }
}
</style>
