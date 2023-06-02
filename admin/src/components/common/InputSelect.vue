<template>
  <el-col :span="span">
    <el-form-item class="query-item" :label="label" :prop="prop">
      <el-input v-model="value" :placeholder="'输入'+label" :disabled="disabled" clearable
                style="width: 200px"/>
    </el-form-item>
  </el-col>
</template>
<script setup>
import {computed} from "vue";

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {type: String, default: ""},
  span: {type: Number, default: 5},
  label: {type: String, default: null},
  prop: {type: String, default: null},
  disabled: {type: Boolean, default: false},
  required: {type: Boolean, default: false},
  tip: {type: String, default: null},
})

const value = computed({
  get() {
    if (!props.modelValue || props.modelValue === '')
      return ''
    return props.modelValue
  },
  set(val) {
    emits('update:modelValue', val)
  }
})
</script>

<style scoped>

</style>