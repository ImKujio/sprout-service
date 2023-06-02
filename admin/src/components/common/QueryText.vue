<template>
  <el-form-item class="query-item" :label="label" :prop="prop">
    <el-input v-model="value" :placeholder="'搜索'+label" :disabled="disabled" clearable
              style="width: 200px"/>
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
      return ''
    return props.modelValue.replaceAll("=|", "")
  },
  set(val) {
    emits('update:modelValue', "=|" + val)
  }
})
</script>

<style scoped>

</style>