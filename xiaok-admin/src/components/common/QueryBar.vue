<template>
  <el-form class="query-bar" :model="modelValue" label-width="auto">
    <slot></slot>
    <span style="margin-bottom: 8px;">
    <el-button type="primary" plain @click="load">查询</el-button>
    <el-button v-if="reset" plain @click="reset">重置</el-button>
    </span>
  </el-form>
</template>

<script setup>

const props = defineProps({
  modelValue: {type: Object, default: {}},
  reset: {type: Boolean, default: true}
})

const emits = defineEmits(["load", "update:modelValue"])

const defValue = Object.assign({}, props.modelValue)

function load() {
  emits("load")
}

function reset() {
  emits("update:modelValue", defValue)
  load()
}

</script>

<style>
.query-bar{
  display: flex; /* 将容器设置为Flex布局 */
  flex-wrap: wrap; /* 允许自动换行 */
  justify-content: flex-start; /* 水平方向从左开始排列 */
  padding-top: 8px;
  padding-left: 16px;
  padding-right: 8px;
}
.query-item{
  margin-bottom: 8px;
  margin-right: 24px;
}
</style>