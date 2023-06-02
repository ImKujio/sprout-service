<template>
  <el-col :span="span">
    <el-form-item class="form-input-item" :label="label" :prop="prop" :required="required" :rules="rules">
      <el-input class="form-item-input" v-model="value" :placeholder="'请输入'+label"
                @blur="onblur" :disabled="disabled" clearable :type="textarea ? 'textarea' : 'text'">
        <template v-if="slotPrepend" #prepend>
          <slot name="prepend"/>
        </template>
        <template v-if="slotAppend" #append>
          <slot name="append"/>
        </template>
      </el-input>
      <div v-if="tip" class="form-input-tip">{{ tip }}</div>
    </el-form-item>
  </el-col>
</template>
<script setup>
import {computed} from "vue";
import {useSlots} from "vue";

const slotPrepend = !!useSlots().prepend
const slotAppend = !!useSlots().append
const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {type: [String, Number], default: null},
  span: {type: Number, default: 5},
  label: {type: String, default: null},
  prop: {type: String, default: null},
  disabled: {type: Boolean, default: false},
  required: {type: Boolean, default: false},
  tip: {type: String, default: null},
  rules: {type: Object, default: []},

  textarea: {type: Boolean, default: false},
  number: {type: Boolean, default: false},
  decimal: {type: Boolean, default: false},
})

const value = computed({
  get() {
    if (props.modelValue === null) return null
    if (props.number) {
      const num = parseInt(props.modelValue)
      return isNaN(num) || num == null ? null : num
    }
    if (props.decimal) {
      const dec = findFloat(props.modelValue + "")
      return !dec ? null : dec
    }
    return props.modelValue
  },
  set(val) {
    if (val === null) {
      emits('update:modelValue', val)
      return;
    }
    if (props.number) {
      const num = parseInt(val)
      emits('update:modelValue', isNaN(num) || num == null ? null : num)
      return;
    }
    if (props.decimal) {
      const dec = findFloat(val + "")
      emits('update:modelValue', !dec ? null : dec)
      return;
    }
    emits('update:modelValue', val)
  }
})

function onblur() {
  if (props.decimal) {
    let dec = findFloat(props.modelValue)
    dec = parseFloat(dec)
    emits('update:modelValue', isNaN(dec) || dec == null ? null : dec)
  }
}

function findFloat(str) {
  if (!str || str === '') return str
  const matches = str.match(/[\d\\.]*/g);
  str = ""
  for (let i = 0; i < matches.length; i++) {
    str = str + matches[i]
  }
  str = str.replaceAll(/\.+/g, ".")
  if (str.startsWith(".")) str = str.substring(1)
  const di = str.indexOf(".")
  if (di === -1) return str
  str = str.replaceAll(".", "")
  str = str.substring(0, di) + "." + str.substring(di)
  return str;
}
</script>

<style scoped>

</style>