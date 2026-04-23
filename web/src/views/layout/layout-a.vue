<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: store.isCollapse }">
      <div class="sidebar-header">
        <div class="brand-mark">
          <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="4" width="24" height="24" stroke="#2563EB" stroke-width="1.5"/>
            <rect x="8" y="8" width="16" height="16" stroke="#2563EB" stroke-width="1"/>
            <rect x="12" y="12" width="8" height="8" fill="#2563EB"/>
          </svg>
        </div>
        <transition name="fade">
          <div class="brand-text" v-show="!store.isCollapse">
            <span class="brand-name">物业管理</span>
            <span class="brand-en">PROPERTY</span>
          </div>
        </transition>
      </div>

      <nav class="sidebar-nav">
        <el-menu
          :router="false"
          :default-active="route.path"
          :collapse="store.isCollapse"
          :collapse-transition="false"
          class="nav-menu"
        >
          <AsideMenu :menuList="menuList"></AsideMenu>
        </el-menu>
      </nav>

      <div class="sidebar-footer">
        <button class="collapse-btn" @click="toggleSidebar">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path v-if="!store.isCollapse" d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path v-else d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部栏 -->
      <header class="topbar">
        <div class="topbar-left">
          <h2 class="page-title">{{ currentPageTitle }}</h2>
        </div>
        <div class="topbar-right">
          <div class="user-info">
            <div class="user-avatar">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.5"/>
                <path d="M4 20C4 16.6863 7.58172 14 12 14C16.4183 14 20 16.6863 20 20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
              </svg>
            </div>
            <span class="user-name">{{ data.loginUser.name || '用户' }}</span>
          </div>
        </div>
      </header>

      <!-- 页面内容 -->
      <div class="page-content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import AsideMenu from "@/components/AsideMenu/index.vue";
import usePiniaStore from "@/store/index.js";
import { useRoute, useRouter } from "vue-router";
import { onMounted, reactive, computed } from "vue";
import { axiosPostRequest, getSessionStorage } from "@/util/index.js";
import { ElMessage } from "element-plus";

const route = useRoute();
const router = useRouter();
const store = usePiniaStore();

const menuList = computed(() => {
  if (data.loginUser.roleId === 1) return store.ownerMenuList;
  if (data.loginUser.roleId === 2) return store.staffMenuList;
  return store.menuList;
});

const currentPageTitle = computed(() => {
  const path = route.path;
  const titleMap = {
    '/index-a': '首页',
    '/district-a': '小区管理',
    '/building-a': '楼栋管理',
    '/house-a': '房屋管理',
    '/parking-a': '车位管理',
    '/owner-a': '业主管理',
    '/property-a': '物业员工',
    '/admin-a': '管理员',
    '/fee-a': '费用管理',
    '/repair-a': '报修管理',
    '/complaint-a': '投诉管理',
    '/announce-a': '公告管理',
    '/system-a': '系统管理',
  };
  return titleMap[path] || '首页';
});

const data = reactive({
  loginUser: {
    id: "",
    roleId: "",
    name: "",
  },
});

const toggleSidebar = () => {
  store.isCollapse = !store.isCollapse;
};

const getLoginUser = async () => {
  const token = getSessionStorage();
  if (!token) {
    data.loginUser = {
      id: 1,
      roleId: 3,
      name: "访客",
    };
    return;
  }

  const response = await axiosPostRequest("/user/check_login", {
    token: token,
  });
  if (response.code === 0) {
    data.loginUser = response.data;
  } else {
    data.loginUser = {
      id: 1,
      roleId: 3,
      name: "访客",
    };
  }
};

onMounted(() => {
  getLoginUser();
});
</script>

<style lang="scss" scoped>
.layout-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  background: #FFFFFF;
  overflow: hidden;
}

// 侧边栏
.sidebar {
  width: 240px;
  height: 100vh;
  background: #1A1A1A;
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 100;

  &.collapsed {
    width: 72px;

    .sidebar-header {
      justify-content: center;
      padding: 24px 0;
    }

    .brand-mark {
      margin-right: 0;
    }

    .nav-menu {
      :deep(.el-menu-item),
      :deep(.el-sub-menu__title) {
        justify-content: center;
        padding: 0;
      }
    }
  }
}

.sidebar-header {
  display: flex;
  align-items: center;
  padding: 24px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.brand-mark {
  width: 32px;
  height: 32px;
  flex-shrink: 0;

  svg {
    width: 100%;
    height: 100%;
  }
}

.brand-text {
  margin-left: 12px;
  display: flex;
  flex-direction: column;
}

.brand-name {
  font-size: 14px;
  font-weight: 600;
  color: #FFFFFF;
  letter-spacing: 0.02em;
}

.brand-en {
  font-size: 9px;
  color: rgba(255, 255, 255, 0.3);
  letter-spacing: 0.15em;
  margin-top: 2px;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 16px 0;
}

.nav-menu {
  border-right: none !important;
  background: transparent !important;

  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    height: 44px;
    line-height: 44px;
    margin: 4px 12px;
    border-radius: 6px;
    color: rgba(255, 255, 255, 0.5);
    transition: all 0.2s ease;
    font-size: 13px;
    letter-spacing: 0.01em;

    &:hover {
      background: rgba(255, 255, 255, 0.08);
      color: #FFFFFF;
    }

    &.is-active {
      background: #2563EB;
      color: #FFFFFF;
    }
  }

  :deep(.el-sub-menu) {
    .el-sub-menu__title {
      &:hover {
        background: rgba(255, 255, 255, 0.08);
        color: #FFFFFF;
      }
    }

    .el-menu-item {
      margin: 4px 20px;
      min-width: auto;

      &:hover {
        background: rgba(255, 255, 255, 0.08);
        color: #FFFFFF;
      }

      &.is-active {
        background: #2563EB;
        color: #FFFFFF;
      }
    }
  }
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.collapse-btn {
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background: rgba(255, 255, 255, 0.08);
    border-color: rgba(255, 255, 255, 0.2);
  }

  svg {
    width: 18px;
    height: 18px;
    color: rgba(255, 255, 255, 0.5);
  }
}

// 主内容区
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

// 顶部栏
.topbar {
  height: 72px;
  background: #FFFFFF;
  border-bottom: 1px solid #E5E5E5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  flex-shrink: 0;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #1A1A1A;
  letter-spacing: -0.01em;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  background: #F9FAFB;
  border: 1px solid #E5E5E5;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    border-color: #D1D5DB;
  }
}

.user-avatar {
  width: 32px;
  height: 32px;
  background: #E5E7EB;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  svg {
    width: 18px;
    height: 18px;
    color: #6B7280;
  }
}

.user-name {
  font-size: 13px;
  font-weight: 500;
  color: #1A1A1A;
  letter-spacing: 0.01em;
}

// 页面内容
.page-content {
  flex: 1;
  padding: 32px 40px;
  overflow-y: auto;
  background: #F9FAFB;
}

// 过渡动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

// 响应式
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: 0;
    top: 0;
    z-index: 1000;
    transform: translateX(-100%);

    &:not(.collapsed) {
      transform: translateX(0);
    }
  }

  .topbar {
    padding: 0 16px;
  }

  .page-content {
    padding: 16px;
  }
}
</style>
