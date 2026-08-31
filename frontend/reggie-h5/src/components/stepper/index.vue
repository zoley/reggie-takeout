<template>
  <div ref="rootRef" class="stepper">
    <van-stepper
      :model-value="count"
      min="0"
      integer
      button-size="22px"
      theme="round"
      @update:model-value="onChange"
      @plus="onPlus"
    />
  </div>
</template>

<script setup>
import { computed, ref } from "vue";

const props = defineProps({
  food: { type: Object, required: true },
});
const emit = defineEmits(["add"]);

const rootRef = ref(null);
const count = computed(() => Number(props.food?.count) || 0);

function onChange(value) {
  props.food.count = value;
}
function onPlus() {
  emit("add", rootRef.value);
}
</script>

<style scoped>
.stepper {
  display: inline-block;
  vertical-align: middle;
}
</style>