<template>
  <div class="header">
    <div class="header-left">
      <el-icon
        class="icon-collapse"
        :size="20"
        v-if="!store.isCollapse"
        @click="changeCollapse"
        ><Fold
      /></el-icon>
      <el-icon
        class="icon-collapse"
        :size="20"
        v-if="store.isCollapse"
        @click="changeCollapse"
        ><Expand
      /></el-icon>
      <div class="breadcrumb-box mask-image">
        <el-breadcrumb separator-icon="ArrowRight">
          <transition-group name="breadcrumb">
            <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.path">
              <div class="el-breadcrumb__inner is-link">
                <el-icon class="breadcrumb-icon">
                  <component :is="item.icon"></component>
                </el-icon>
                <span class="breadcrumb-title" v-text="item.name"></span>
              </div>
            </el-breadcrumb-item>
          </transition-group>
        </el-breadcrumb>
      </div>
    </div>
    <div class="header-right">
      <img
        class="header-avatar"
        :src="filterPhoto(data.loginUser.headPic)"
        alt="avatar"
      />
      <el-dropdown
        style="margin-left: 10px"
        :hide-on-click="false"
        @command="handleCommand"
      >
        <div class="header-dropdown">
          <span v-text="data.loginUser.username"></span>
          <el-icon><arrow-down /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import usePiniaStore from "@/store/index.js";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { getBreadcrumbList } from "@/util/index.js";
import Dialog from "@/components/Dialog/index.vue";
import { computed, reactive, onMounted, ref } from "vue";
import {
  axiosPostRequest,
  getSessionStorage,
  myEventBus,
} from "@/util/index.js";
const emitter = myEventBus();
const store = usePiniaStore();
const route = useRoute();
const router = useRouter();
const changeCollapse = () => {
  store.setCollapse(store.isCollapse);
};

emitter.on("refresh", () => {
  getLoginUser();
});

const userDialogRef = ref();

onMounted(() => {
  getLoginUser();
});


const data = reactive({
  loginUser: {
    id: "",
    headPic: "common/no_image.jpg",
    username: "",
    roleId: "",
  },
});

const filterPhoto = computed(() => (photo) => {
  return import.meta.env.VITE_SERVER + "/photo/view?filename=" + photo;
});

// 获取当前登录用户
const getLoginUser = async () => {
  const response = await axiosPostRequest("/user/check_login", {
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    data.loginUser = response.data;
    data.userForm = JSON.parse(JSON.stringify(response.data));
  } else {
    ElMessage.error(response.msg);
    router.push("/login");
  }
};

// 退出登录操作
const handleLogout = async () => {
  const response = await axiosPostRequest("/user/logout", {
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    router.push("/login");
  } else {
    ElMessage.error(response.msg);
  }
};

// 下拉框选项点击监听
const handleCommand = (command) => {
  if (command === "logout") {
    handleLogout();
  }
};


// 获取面包屑数据
const breadcrumbList = computed(() => {
  return getBreadcrumbList(
    store.menuList,
    route.matched.length > 0
      ? route.matched[route.matched.length - 1].path
      : "",
    []
  );
});
onMounted(() => {});
</script>
<style lang="scss" scoped>
.breadcrumb-enter-active {
  transition: all 0.2s;
}
.breadcrumb-enter-from,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(10px);
}

.header {
  display: flex;
  justify-content: space-between;
  height: 60px;
  padding: 0 20px;

  .header-left {
    display: flex;
    align-items: center;
    overflow: hidden;
    white-space: nowrap;

    .icon-collapse {
      color: #606266;
      outline: none;
      padding: 8px;
      border-radius: 6px;
      transition: all 0.2s ease;

      &:hover {
        color: #20a162;
        background: rgba(32, 161, 98, 0.08);
        cursor: pointer;
      }
    }

    .breadcrumb-box {
      display: flex;
      align-items: center;
      margin-left: 16px;
      overflow: hidden;
      user-select: none;

      .el-breadcrumb {
        white-space: nowrap;

        .el-breadcrumb__item {
          position: relative;
          display: inline-block;
          float: none;

          .breadcrumb-title {
            font-weight: 600;
            font-size: 14px;
          }

          .el-breadcrumb__inner {
            display: inline-flex;
            align-items: center;
            padding: 4px 8px;
            border-radius: 4px;
            transition: all 0.2s ease;

            &.is-link {
              color: #606266;

              &:hover {
                color: #20a162;
                background: rgba(32, 161, 98, 0.06);
              }
            }

            .breadcrumb-icon {
              margin-right: 6px;
              font-size: 14px;
              color: #909399;
            }

            .breadcrumb-title {
              margin-top: 0;
            }
          }

          &:last-child .el-breadcrumb__inner,
          &:last-child .el-breadcrumb__inner:hover {
            color: #303133;
            background: transparent;
          }

          :deep(.el-breadcrumb__separator) {
            transform: translateY(-1px);
            color: #dcdfe6;
          }
        }
      }
    }

    .mask-image {
      padding-right: 50px;
      mask-image: linear-gradient(
        90deg,
        #000000 0%,
        #000000 calc(100% - 50px),
        transparent
      );
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;

    .header-avatar {
      width: 36px;
      height: 36px;
      user-select: none;
      border-radius: 50%;
      border: 2px solid #ebeef5;
      transition: all 0.2s ease;

      &:hover {
        border-color: #20a162;
      }
    }

    .header-dropdown {
      color: #606266;
      white-space: nowrap;
      cursor: pointer;
      outline: none;
      padding: 6px 10px;
      border-radius: 6px;
      transition: all 0.2s ease;
      font-weight: 500;

      &:hover {
        color: #20a162;
        background: rgba(32, 161, 98, 0.06);
      }
    }
  }
}
</style>
