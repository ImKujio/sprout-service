<template>
  <el-form-item class="query-item" :label="label" :prop="prop">
    <el-input-number v-model="value" :placeholder="'搜索'+label" :disabled="disabled" :precision="decimal?2:0"
                     clearable :controls="false"
                     style="width: 160px"/>
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
  decimal: {type: Boolean, default: false}
})

const value = computed({
  get() {
    if (!props.modelValue || props.modelValue === '')
      return undefined
    const val = props.modelValue.replaceAll("=|", "")
    return props.decimal ? parseFloat(val) : parseInt(val)
  },
  set(val) {
    emits('update:modelValue', "=|" + val)
  }
})
</script>

<style scoped>

</style>