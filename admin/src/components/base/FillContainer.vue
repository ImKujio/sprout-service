<template>
  <div :style="style">
    <div style="position: absolute; width: 100%;height: 100%">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>

import {computed} from "vue";

const props = defineProps({
  width: {type: String, default: null},
  height: {type: String, default: null},
})

const style = computed(() => {
  if (!props.width && !props.height) return {
    flex:1,
    position: 'relative'
  }
  return {
    width: props.width,
    height: props.height,
    position: 'relative'
  }
})

</script>

<style scoped>

</style>