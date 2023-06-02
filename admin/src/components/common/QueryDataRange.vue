<template>
  <el-form-item class="query-item" :label="label" :prop="prop">
    <el-date-picker v-model="value" :placeholder="'搜索'+label" :disabled="disabled" clearable
                    value-format="YYYY-MM-DD" type="daterange" range-separator="-"
                    start-placeholder="开始日期" end-placeholder="结束日期"
                    style="width: 280px"/>
  </el-form-item>
</template>
<script setup>
import {computed} from "vue";

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {type: String, default: ""},
  label: {type: String, default: null},
  prop: {type: String, default: null},
  disabled: {type: Boolean, default: false},
})

const value = computed({
  get() {
    if (!props.modelValue || props.modelValue === '')
      return []
    return props.modelValue.replaceAll("BETWEEN|", "").split('|')
  },
  set(val) {
    const date = !val ? '' : val.join('|')
    emits('update:modelValue', "BETWEEN|" + date)
  }
})
</script>

<style scoped>

</style>