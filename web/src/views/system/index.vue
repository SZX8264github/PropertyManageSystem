<template>
  <div
    style="
      padding: 6px;
      display: flex;
      flex-direction: column;
      height: calc(100% - 12px);
    "
  >
    <el-card class="card" shadow="hover" style="flex-shrink: 0; flex-grow: 0">
      <div
        class="user"
        v-waterMarker="{ text: 'Welcome', textColor: '#D9D9D9' }"
      >
        <img
          class="user-img"
          :src="filterPhoto(data.loginUser.headPic)"
          alt="avatar"
        />
        <div style="padding-left: 20px">
          <div class="user-info">
            <span v-text="data.loginUser.username"></span>
            <span
              v-if="data.loginUser.roleId === 1"
              style="color: rgb(64 158 255)"
              >[普通用户]</span
            >
            <span
              v-if="data.loginUser.roleId === 2"
              style="color: rgb(64 158 255)"
              >[管理员]</span
            >
          </div>
          <div class="user-info" style="padding-bottom: 0">
            欢迎来到小区物业管理系统~🌻
          </div>
        </div>
      </div>
    </el-card>
    <el-row :gutter="20" style="margin-top: 5px; flex-shrink: 0; flex-grow: 0">
      <el-col :span="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card class="card" shadow="hover">
          <div class="data-status">
            <span class="title" v-if="data.loginUser.roleId === 1"
              >未受理维修数</span
            >
            <span class="title" v-if="data.loginUser.roleId === 2">业主数</span>
            <span class="title" v-if="data.loginUser.roleId === 3">小区数</span>
            <el-tag type="info">总数</el-tag>
          </div>
          <div class="value" v-text="data.statistic.total1"></div>
        </el-card>
      </el-col>
      <el-col :span="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card class="card" shadow="hover">
          <div class="data-status">
            <span class="title" v-if="data.loginUser.roleId === 1"
              >已受理维修数</span
            >
            <span class="title" v-if="data.loginUser.roleId === 2"
              >未受理维修数</span
            >
            <span class="title" v-if="data.loginUser.roleId === 3"
              >物业员工数</span
            >
            <el-tag type="success">总数</el-tag>
          </div>
          <div class="value" v-text="data.statistic.total2"></div>
        </el-card>
      </el-col>
      <el-col :span="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card class="card" shadow="hover">
          <div class="data-status">
            <span class="title" v-if="data.loginUser.roleId === 1"
              >未受理投诉数</span
            >
            <span class="title" v-if="data.loginUser.roleId === 2"
              >未受理投诉数</span
            >
            <span class="title" v-if="data.loginUser.roleId === 3">楼栋数</span>
            <el-tag type="warning">总数</el-tag>
          </div>
          <div class="value" v-text="data.statistic.total3"></div>
        </el-card>
      </el-col>
      <el-col :span="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card class="card" shadow="hover">
          <div class="data-status">
            <span class="title" v-if="data.loginUser.roleId === 1"
              >已受理投诉数</span
            >
            <span class="title" v-if="data.loginUser.roleId === 2">公告数</span>
            <span class="title" v-if="data.loginUser.roleId === 3">房屋数</span>
            <el-tag type="danger">总数</el-tag>
          </div>
          <div class="value" v-text="data.statistic.total4"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row
      :gutter="20"
      style="
        margin-top: 5px;
        flex-shrink: 1;
        flex-grow: 1;
        overflow-y: auto;
        display: flex;
        align-items: flex-end;
        justify-content: center;
      "
    >
      <div ref="refChart" style="height: 100%; width: 95%"></div>
    </el-row>
  </div>
</template>
<script setup>
import {
  ref,
  reactive,
  onMounted,
  computed,
  onUnmounted,
  shallowRef,
} from "vue";
import { eachMonthOfInterval, subMonths, format } from "date-fns";
import * as echarts from "echarts";
import { ElMessage } from "element-plus";
import {
  axiosPostRequest,
  getSessionStorage,
  myEventBus,
} from "@/util/index.js";
import { useRouter } from "vue-router";
const router = useRouter();
const emitter = myEventBus();
emitter.on("refresh", () => {
  getLoginUser();
});

const initChart = () => {
  chartInstance.value = echarts.init(refChart.value);
  chartInstance.value.setOption(initOption.value);
};

const resizeChart = () => {
  chartInstance.value.setOption(initOption.value);
  chartInstance.value.resize();
};

const refChart = ref();
const chartInstance = shallowRef(null);
const initOption = ref({
  title: {
    text: "近五个月缴费数",
    x: "center",
  },
  tooltip: {
    trigger: "axis",
  },
  grid: {
    left: "3%",
    right: "4%",
    bottom: "3%",
    containLabel: true,
  },
  color: ["#FF9F7F", "#00EE00"],
  toolbox: {
    feature: {
      saveAsImage: {
        title: "保存为图片",
      },
    },
  },
  xAxis: {
    type: "category",
    boundaryGap: false,
    data: [],
  },
  yAxis: {
    type: "value",
    minInterval: 1,
  },
  series: [
    {
      name: "待支付",
      type: "line",
      data: [],
    },
    {
      name: "已支付",
      type: "line",
      data: [],
    },
  ],
});

onMounted(() => {
  initChart();
  const now = new Date(); // 当前日期
  const numMonths = 4; // 近5个月
  const lastFiveMonths = eachMonthOfInterval({
    start: subMonths(now, numMonths),
    end: now,
  });
  let xAxis = [];
  // 输出近5个月的日期
  lastFiveMonths.forEach((date) => {
    xAxis.push(format(date, "yyyy-MM"));
  });
  initOption.value.xAxis.data = xAxis;
  getLoginUser();
  window.addEventListener("resize", resizeChart);
});

onUnmounted(() => {
  chartInstance.value.dispose();
  chartInstance.value = null;
  window.removeEventListener("resize", resizeChart);
});

const data = reactive({
  loginUser: {
    id: "",
    headPic: "common/no_image.jpg",
    username: "",
    roleId: "",
  },
  statistic: { total1: 0, total2: 0, total3: 0, total4: 0 },
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
    getStatisticTotal();
    getFeeStatistic();
  } else {
    ElMessage.error(response.msg);
    router.push("/login");
  }
};

// 获取缴费数据统计
const getFeeStatistic = async () => {
  const response = await axiosPostRequest("/fee/statistic", {
    id: data.loginUser.id,
    roleId: data.loginUser.roleId,
    districtId: data.loginUser.districtId,
  });
  if (response.code === 0) {
    initOption.value.series[0].data = response.data.noPayedList.reverse();
    initOption.value.series[1].data = response.data.payedList.reverse();
    chartInstance.value.setOption(initOption.value);
  }
};

// 获取系统首页总数统计
const getStatisticTotal = async () => {
  const response = await axiosPostRequest("/user/statistic", {
    id: data.loginUser.id,
    roleId: data.loginUser.roleId,
    districtId: data.loginUser.districtId,
  });
  if (response.code === 0) {
    data.statistic = response.data;
  }
};
</script>
<style lang="scss" scoped>
.card {
  border-radius: 0.375rem;
  .user {
    display: flex;
    align-items: center;
    .user-img {
      user-select: none;
      width: 60px;
      height: 60px;
      border-radius: 50%;
    }
    .user-info {
      white-space: nowrap;
      padding-bottom: 8px;
      font-weight: bold;
    }
  }
  .data-status {
    display: flex;
    justify-content: space-between;
    .title {
      font-size: 0.875rem;
      line-height: 1.25rem;
    }
  }
  .value {
    font-size: 1.5rem;
    line-height: 2rem;
  }
  :deep(.el-calendar-day) {
    height: 50px !important;
    p {
      margin: 0 !important;
    }
  }
  :deep(.el-calendar__body) {
    padding: 0 20px;
  }
  :deep(.el-calendar__header) {
    padding: 0 20px 12px 20px;
  }
}
</style>
