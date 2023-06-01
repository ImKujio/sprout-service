<template>
  <fill-container width="200px">
    <el-scrollbar class="nav-menu-scroll">
      <el-menu
        class="nav-menu"
        default-active="1-1"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon>
              <location/>
            </el-icon>
            <span>Navigator One</span>
          </template>
          <el-menu-item index="1-1">item one</el-menu-item>
          <el-menu-item index="1-2">item two</el-menu-item>
          <el-menu-item index="1-3">item three</el-menu-item>
          <el-sub-menu index="1-4">
            <template #title>item four</template>
            <el-menu-item index="1-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon>
              <icon-menu/>
            </el-icon>
            <span>Navigator Two</span>
          </template>
          <el-menu-item index="2-1">item one</el-menu-item>
          <el-menu-item index="2-2">item two</el-menu-item>
          <el-menu-item index="2-3">item three</el-menu-item>
          <el-sub-menu index="2-4">
            <template #title>item four</template>
            <el-menu-item index="2-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>
            <el-icon>
              <document/>
            </el-icon>
            <span>Navigator Three</span>
          </template>
          <el-menu-item index="3-1">item one</el-menu-item>
          <el-menu-item index="3-2">item two</el-menu-item>
          <el-menu-item index="3-3">item three</el-menu-item>
          <el-sub-menu index="3-4">
            <template #title>item four</template>
            <el-menu-item index="3-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>
            <el-icon>
              <setting/>
            </el-icon>
            <span>Navigator Four</span>
          </template>
          <el-menu-item index="4-1">item one</el-menu-item>
          <el-menu-item index="4-2">item two</el-menu-item>
          <el-menu-item index="4-3">item three</el-menu-item>
          <el-sub-menu index="4-4">
            <template #title>item four</template>
            <el-menu-item index="4-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
      </el-menu>
    </el-scrollbar>
  </fill-container>
</template>

<script setup>
import {Document, Menu as IconMenu, Location, Setting,} from '@element-plus/icons-vue'
import FillContainer from "@/components/base/FillContainer.vue";
import {computed} from "vue";
import {map2Tree} from "@/utils/collection-utils";
import {asyncRef, loadAsyncRef} from "@/utils/vue-utils";
import sysMenu from "@/api/sys/sys-menu.js";

const sysMenus = asyncRef(() => sysMenu.all(['pid', 'type', 'name', 'path', 'sort']), {})

const menus = computed(() => {
  return map2Tree(sysMenus.value)
})

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
loadAsyncRef()
</script>

<style lang="scss">
.nav-menu {
  --el-menu-item-height: 52px;
  --el-menu-sub-item-height: 52px;

  //--el-menu-hover-bg-color: var(--el-fill-color-light);
  --el-menu-base-level-padding: 16px;
  --el-menu-level-padding: 29px;

  border: none !important;
  padding-top: 3px;
  padding-bottom: 3px;

  .el-sub-menu__title {
    z-index: 0;
    background-color: unset !important;
  }

  .el-sub-menu__title::before {
    content: "";
    position: absolute;
    top: 3px;
    left: 6px;
    right: 6px;
    bottom: 3px;
    z-index: -1;
    border-radius: 6px;
    transition: background-color var(--el-transition-duration);
  }

  .el-menu-item::before {
    content: "";
    position: absolute;
    top: 3px;
    left: 6px;
    right: 6px;
    bottom: 3px;
    z-index: -1;
    border-radius: 6px;
    transition: background-color var(--el-transition-duration);
  }

  .el-sub-menu__title:hover::before {
    background-color: var(--el-fill-color-light);
  }

  .el-menu-item {
    z-index: 0;
    background-color: unset !important;
  }

  .el-menu-item:hover::before {
    background-color: var(--el-fill-color-light);
  }

  .el-menu-item.is-active::before {
    background-color: var(--el-menu-hover-bg-color);
  }

}


.nav-menu-scroll {
  position: relative;
  border-right: solid 1px var(--el-menu-border-color);
}

</style>