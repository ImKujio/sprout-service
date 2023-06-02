<template>
    <el-button
        :type="type"
        plain
        :icon="icon"
        :disabled="disabled"
        @click="emits('click')"
        v-hasPermi="[permission]"
    >{{ label }}
    </el-button>
</template>
<script setup>
const emits = defineEmits(['click'])
const props = defineProps({
  type: {type:String,default: "info"}, // primary, success, danger, warning, info
  icon: {type:String,default:null},
  permission: {type:String},
  label: String,
  disabled: {type: Boolean, default: false}
})

</script>

<style scoped>

</style>