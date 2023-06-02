<template>
  <el-col :span="span">
    <el-form-item class="form-input-item" :label="label" :prop="prop">
      <el-switch class="form-item-input" v-model="value" :disabled="disabled" />
      <div v-if="tip" class="form-input-tip">{{ tip }}</div>
    </el-form-item>
  </el-col>
</template>
<script setup>
import {computed, onMounted} from "vue";

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {type: Boolean, default: false},
  span: {type: Number, default: 5},
  label: {type: String, default: null},
  prop: {type: String, default: null},
  disabled: {type: Boolean, default: false},
  required: {type: Boolean, default: false},
  tip: {type: String, default: null},

  default:{type:Boolean,default:false}
})

const value = computed({
  get() {
    return props.modelValue || props.default
  },
  set(val) {
    emits('update:modelValue', val)
  }
})

onMounted(() => {
  if (props.modelValue || props.default)
    emits('update:modelValue', true)
  else
    emits('update:modelValue', false)
})
</script>

<style scoped>

</style>