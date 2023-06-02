<template>
  <el-form :style="{width:width}" ref="formRef" :model="form" label-width="auto" :show-message="false">
    <slot></slot>
  </el-form>
</template>

<script setup>
import {computed} from "vue";

const wSize = [320, 672, 1024]

const props = defineProps({
  form: {type: Object, default: {}},
  cols: {type: Number, default: 1}
})

const width = computed(() => {
  const col = props.cols < 1 || props.cols > 3 ? 2 : props.cols
  return wSize[col - 1] + 'px'
})
</script>

<style>
.form-input-item{
  margin-bottom: 0;
}

.form-item-input{
  margin-bottom: 24px;
}

.form-input-item .el-input{
  width: 100%;
}

.form-item-input .el-textarea__inner{
  min-height: 86px !important;
}

.form-input-item .el-input-number .el-input__inner{
  text-align: unset;
}

.form-input-tip{
  position: absolute;
  line-height: 1.6;
  bottom: 3px;
  left: 2px;
  font-size: 13px;
}
</style>