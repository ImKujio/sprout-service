<template>
  <el-pagination
      v-model:current-page="page"
      v-model:page-size="size"
      :page-sizes="[20, 50, 100, 200]"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      style="justify-content: right;padding: 8px 16px"
  />
</template>

<script setup>
import {computed} from "vue";

const emits = defineEmits(["load", "update:modelValue"])
const props = defineProps({
  modelValue: {type: Array, default: null},
  total: {type: Number, default: 0}
})

const size = computed({
  get() {
    return !props.modelValue || !props.modelValue[0] ? 20 : props.modelValue[0]
  },
  set(val) {
    console.log("size",val);
    emits('update:modelValue', [val, page.value])
    emits("load")
  }
})

const page = computed({
  get() {
    return !props.modelValue || !props.modelValue[1] ? 1 : props.modelValue[1]
  },
  set(val){
    console.log("page",val);
    emits('update:modelValue', [size.value, val])
    emits("load")
  }
})
</script>

<style scoped>

</style>