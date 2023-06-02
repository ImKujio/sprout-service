<template>
  <el-dialog class="i-dialog" :closeOnPressEscape=false :closeOnClickModal=false :show-close="false"
             :model-value="modelValue"
             @update:modelValue="val => emits('update:modelValue',val)" :width="width" append-to-body>
    <template #header>
      <span class="el-dialog__title">{{ title }}</span>
      <span style="float: right;margin-top: -6px">
        <slot name="right">
          <el-button :disabled="loading" type="primary" @click="emits('save')">保存</el-button>
        </slot>
        <el-button @click="emits('update:modelValue',false)">取消</el-button>
      </span>
    </template>
    <div v-loading="loading">
      <slot></slot>
    </div>
  </el-dialog>
</template>

<script setup>
const emits = defineEmits(["update:modelValue", "save"])
const props = defineProps({
  modelValue: {type: Boolean, default: false},
  title: {type: String, default: "标题"},
  width: {type: String, default: "fit-content"},
  loading: {type: Boolean, default: false},
})
</script>

<style>
.i-dialog .el-dialog__header {
  margin-right: 0;
}

.i-dialog .el-dialog__body {
  padding: 20px;
}
</style>