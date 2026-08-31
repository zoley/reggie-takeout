<template>
  <van-rate
    :model-value="score"
    readonly
    allow-half
    :size="size"
    :gutter="letterSpace"
    color="#ff9900"
    void-color="#e8e8e8"
  />
</template>

<script setup>
defineProps({
  score: { type: Number, default: 5 },
  size: { type: Number, default: 18 },
  letterSpace: { type: Number, default: 5 },
});
</script>