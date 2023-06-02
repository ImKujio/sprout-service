<template>
  <div id="head-bar">
    <span id="title">
      <svg-icon icon="logo" style="width: 28px;height: 28px"/>
      <span>{{ title }}</span>
    </span>
  </div>
</template>

<script setup>

const title = import.meta.env.VITE_APP_TITLE

</script>

<style lang="scss" scoped>
#head-bar {
  display: flex;
  flex-direction: row;
  align-items: center;
  border-bottom: 1px solid var(--el-border-color);
}

#title {
  width: 168px;
  margin-top: 16px;
  margin-bottom: 16px;
  padding-left: 16px;
  padding-right: 16px;
  display: flex;
  flex-direction: row;
  align-items: center;
  border-right: 1px solid var(--el-border-color);


  span {
    font-size: medium;
    font-weight: bold;
    margin-left: 8px;
  }
}



</style>