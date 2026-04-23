<template>
  <template v-for="item in menuList" :key="item.path">
    <el-sub-menu v-if="item?.children" :index="item.path">
      <template #title>
        <el-icon>
          <component :is="item.icon"></component>
        </el-icon>
        <span v-text="item.name"></span>
      </template>
      <AsideMenu :menuList="item.children"></AsideMenu>
    </el-sub-menu>
    <el-menu-item v-else :index="item.path" @click="clickMenuItem(item)">
      <el-icon>
        <component :is="item.icon"></component>
      </el-icon>
      <template #title>
        <span v-text="item.name"></span>
      </template>
    </el-menu-item>
  </template>
</template>
<script setup>
import AsideMenu from "@/components/AsideMenu/index.vue";
import { useRoute, useRouter } from "vue-router";
defineProps(["menuList"]);
const router = useRouter();
const clickMenuItem = (value) => {
  router.push(value.path);
};
</script>
<style lang="scss" scoped>
.el-menu-item {
  height: 50px;
  line-height: 50px;
  margin: 0 10px;
  font-weight: 500;
  color: #606266;
  user-select: none;
  border-radius: 8px;
  transition: all var(--transition-fast);

  i {
    font-size: 16px;
  }

  &:hover {
    background-color: rgba(32, 161, 98, 0.08) !important;
    color: var(--primary-color) !important;

    i {
      color: var(--primary-color) !important;
    }
  }

  &.is-active {
    background-color: rgba(32, 161, 98, 0.15) !important;
    color: var(--primary-color) !important;
    font-weight: 500;

    i {
      color: var(--primary-color) !important;
    }
  }
}

:deep(.el-sub-menu__title) {
  height: 50px;
  line-height: 50px;
  margin: 0 10px;
  font-weight: 500;
  color: #606266;
  user-select: none;
  border-radius: 8px;
  transition: all var(--transition-fast);

  i {
    font-size: 16px;
  }

  &:hover {
    background-color: rgba(32, 161, 98, 0.08) !important;
    color: var(--primary-color) !important;
  }

  &:active {
    background-color: rgba(32, 161, 98, 0.12) !important;
    color: var(--primary-color) !important;
  }
}

:deep(.el-sub-menu) {
  .el-menu {
    background: #fff !important;

    .el-menu-item {
      margin: 0 20px;
      font-size: 13px;

      &:hover {
        background-color: rgba(32, 161, 98, 0.08) !important;
        color: var(--primary-color) !important;
      }

      &.is-active {
        background-color: rgba(32, 161, 98, 0.15) !important;
        color: var(--primary-color) !important;
      }
    }
  }
}

.el-menu--collapse {
  .el-menu-item {
    margin: 0 5px;
  }
  
  .el-sub-menu {
    .el-sub-menu__title {
      margin: 0 5px;
    }
    
    .el-menu-item {
      margin: 0 10px;
    }
  }
}
</style>
