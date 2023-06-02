<template>
  <div style="flex: 1;position: relative">
    <el-table ref="tableRef" style="position: absolute; width: 100%;" height="100%" :data="list" v-loading="loading"
              highlight-current-row @current-change="onSelect">
      <slot></slot>
    </el-table>
  </div>
</template>

<script setup>
import {computed, ref, watch} from "vue";

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {type: String, default: null},
  list: {type: Array, default: []},
  loading: {type: Boolean, default: false},
})

const tableRef = ref()

const selRow = computed(() => {
  if (!tableRef.value) return null
  if (!props.modelValue) return null
  if (!props.list || props.list.length === 0) return null
  return props.list[props.modelValue]
})

watch(selRow, (val) => {
  if (!val) {
    tableRef.value.setCurrentRow()
  } else {
    tableRef.value.setCurrentRow(val)
  }
})

function onSelect(row) {
  emits("update:modelValue", !row ? null : "" + props.list.indexOf(row))
}
</script>

<style scoped>

</style>