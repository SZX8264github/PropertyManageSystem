<template>
  <el-container class="layout-container">
    <el-aside
      class="layout-aside"
      :style="{ width: !store.isCollapse ? '220px' : '70px' }"
    >
      <div class="logo">
        <img src="@/assets/logo.png" class="logo-img" />
        <div
          class="logo-title animate__animated animate__fadeInLeft"
          v-show="!store.isCollapse"
        >
          小区物业管理系统
        </div>
      </div>
      <el-scrollbar class="layout-scrollbar">
        <!-- :unique-opened="true" 子菜单不能同时展开 -->
        <el-menu
          :router="false"
          :default-active="route.path"
          :collapse="store.isCollapse"
          :collapse-transition="false"
          class="animate__animated animate__fadeInLeft"
        >
          <AsideMenu :menuList="data.loginUser.roleId === 1 ? store.ownerMenuList : data.loginUser.roleId === 2 ? store.staffMenuList : store.menuList"></AsideMenu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="layout-header">
        <Header></Header>
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
      <el-footer class="layout-footer">
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import AsideMenu from "@/components/AsideMenu/index.vue";
import Header from "@/components/Header/index.vue";
import usePiniaStore from "@/store/index.js";
import { useRoute, useRouter } from "vue-router";
import {onMounted, reactive} from "vue";
import {
  axiosPostRequest,
  getSessionStorage,
} from "@/util/index.js";
import {ElMessage} from "element-plus";
const route = useRoute();
const router = useRouter();
const store = usePiniaStore();
onMounted(() => {
  getLoginUser();
});
const data = reactive({
  loginUser: {
    id: "",
    roleId: "",
  },
});
// 获取当前登录用户
const getLoginUser = async () => {
  const response = await axiosPostRequest("/user/check_login", {
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    data.loginUser = response.data;
  } else {
    ElMessage.error(response.msg);
    router.push("/login");
  }
};
</script>

<style lang="scss" scoped>
.layout-container {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background-color: var(--bg-color);

  .layout-aside {
    z-index: 10;
    background-color: #fff;
    border-right: 1px solid #ebeef5;
    box-shadow: 2px 0 12px rgba(0, 0, 0, 0.05);
    transition: all var(--transition-normal);
  }

  .layout-header {
    height: 60px;
    background-color: #fff;
    border-bottom: 1px solid #ebeef5;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    transition: all var(--transition-normal);
  }

  .layout-main {
    box-sizing: border-box;
    padding: 20px;
    overflow-x: hidden;
    background-color: var(--bg-color);
  }

  .layout-footer {
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 500;
    font-size: 13px;
    color: #909399;
    background-color: #fff;
    border-top: 1px solid #ebeef5;
    transition: all var(--transition-normal);
  }
}

.el-menu {
  border-right: none;
  background-color: #fff !important;
  
  .el-menu-item {
    height: 50px;
    line-height: 50px;
    margin: 0 10px;
    border-radius: 8px;
    transition: all var(--transition-fast);
    
    &:hover {
      background-color: rgba(32, 161, 98, 0.08) !important;
      color: var(--primary-color) !important;
    }
    
    &.is-active {
      background-color: rgba(32, 161, 98, 0.15) !important;
      color: var(--primary-color) !important;
      font-weight: 500;
    }
  }
  
  .el-sub-menu {
    .el-sub-menu__title {
      height: 50px;
      line-height: 50px;
      margin: 0 10px;
      border-radius: 8px;
      transition: all var(--transition-fast);
      
      &:hover {
        background-color: rgba(32, 161, 98, 0.08) !important;
        color: var(--primary-color) !important;
      }
    }
    
    .el-menu-item {
      margin: 0 20px;
      
      &:hover {
        background-color: rgba(32, 161, 98, 0.08) !important;
        color: var(--primary-color) !important;
      }
      
      &.is-active {
        background-color: rgba(32, 161, 98, 0.15) !important;
        color: var(--primary-color) !important;
        font-weight: 500;
      }
    }
  }
}

.layout-scrollbar {
  width: 100%;
  height: calc(100vh - 60px);
}

.logo {
  display: flex;
  align-items: center;
  width: 100%;
  height: 60px;
  line-height: 60px;
  text-align: center;
  border-bottom: 1px solid #ebeef5;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
  transition: all var(--transition-normal);

  .logo-title {
    font-weight: bold;
    font-size: 16px;
    user-select: none;
    color: #fff;
  }

  .logo-img {
    height: 34px;
    width: 34px;
    margin-left: 12px;
    margin-right: 12px;
  }
}

.el-menu--collapse {
  width: 70px;
  
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
