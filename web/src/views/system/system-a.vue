<template>
  <div class="dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>{{ getGreeting() }}，{{ data.loginUser.username || '用户' }}</h1>
          <p>欢迎回到物业管理系统，今天也是美好的一天</p>
        </div>
        <div class="welcome-avatar">
          <img :src="filterPhoto(data.loginUser.headPic)" alt="avatar" />
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="(stat, index) in stats" :key="index">
        <div class="stat-header">
          <span class="stat-label">{{ stat.label }}</span>
          <span class="stat-index">{{ String(index + 1).padStart(2, '0') }}</span>
        </div>
        <div class="stat-value">{{ stat.value }}</div>
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path :d="stat.icon" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="chart-section">
      <div class="chart-header">
        <h3>近五个月缴费统计</h3>
        <div class="chart-legend">
          <span class="legend-item">
            <span class="legend-dot" style="background: #2563EB"></span>
            待支付
          </span>
          <span class="legend-item">
            <span class="legend-dot" style="background: #1A1A1A"></span>
            已支付
          </span>
        </div>
      </div>
      <div ref="refChart" class="chart-container"></div>
    </div>
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

const refChart = ref();
const chartInstance = shallowRef(null);

const initOption = ref({
  tooltip: {
    trigger: "axis",
    backgroundColor: "rgba(255, 255, 255, 0.95)",
    borderColor: "#E5E5E5",
    textStyle: {
      color: "#1A1A1A",
    },
  },
  grid: {
    left: "3%",
    right: "4%",
    bottom: "3%",
    containLabel: true,
  },
  xAxis: {
    type: "category",
    boundaryGap: false,
    data: [],
    axisLine: {
      lineStyle: {
        color: "#E5E5E5",
      },
    },
    axisLabel: {
      color: "#6B7280",
    },
  },
  yAxis: {
    type: "value",
    minInterval: 1,
    axisLine: {
      show: false,
    },
    axisLabel: {
      color: "#6B7280",
    },
    splitLine: {
      lineStyle: {
        color: "#F3F4F6",
      },
    },
  },
  series: [
    {
      name: "待支付",
      type: "line",
      data: [],
      smooth: true,
      symbol: "circle",
      symbolSize: 6,
      lineStyle: {
        color: "#2563EB",
        width: 2,
      },
      itemStyle: {
        color: "#2563EB",
      },
      areaStyle: {
        color: {
          type: "linear",
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: "rgba(37, 99, 235, 0.15)" },
            { offset: 1, color: "rgba(37, 99, 235, 0.01)" },
          ],
        },
      },
    },
    {
      name: "已支付",
      type: "line",
      data: [],
      smooth: true,
      symbol: "circle",
      symbolSize: 6,
      lineStyle: {
        color: "#1A1A1A",
        width: 2,
      },
      itemStyle: {
        color: "#1A1A1A",
      },
      areaStyle: {
        color: {
          type: "linear",
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: "rgba(26, 26, 26, 0.08)" },
            { offset: 1, color: "rgba(26, 26, 26, 0.01)" },
          ],
        },
      },
    },
  ],
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

const stats = computed(() => {
  const roleId = data.loginUser.roleId;
  if (roleId === 1) {
    return [
      { label: "未受理维修", value: data.statistic.total1, icon: "M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" },
      { label: "已受理维修", value: data.statistic.total2, icon: "M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" },
      { label: "未受理投诉", value: data.statistic.total3, icon: "M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" },
      { label: "已受理投诉", value: data.statistic.total4, icon: "M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" },
    ];
  } else if (roleId === 2) {
    return [
      { label: "业主总数", value: data.statistic.total1, icon: "M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" },
      { label: "未受理维修", value: data.statistic.total2, icon: "M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" },
      { label: "未受理投诉", value: data.statistic.total3, icon: "M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" },
      { label: "公告数", value: data.statistic.total4, icon: "M11 5.882V19.24a1.76 1.76 0 01-3.417.592l-2.147-6.15M18 13a3 3 0 100-6M5.436 13.683A4.001 4.001 0 017 6h1.832c4.1 0 7.625-1.234 9.168-3v14c-1.543-1.766-5.067-3-9.168-3H7a3.988 3.988 0 01-1.564-.317z" },
    ];
  } else {
    return [
      { label: "小区总数", value: data.statistic.total1, icon: "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" },
      { label: "物业员工", value: data.statistic.total2, icon: "M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" },
      { label: "楼栋数", value: data.statistic.total3, icon: "M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" },
      { label: "房屋数", value: data.statistic.total4, icon: "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" },
    ];
  }
});

const getGreeting = () => {
  const hour = new Date().getHours();
  if (hour < 6) return "夜深了";
  if (hour < 9) return "早上好";
  if (hour < 12) return "上午好";
  if (hour < 14) return "中午好";
  if (hour < 17) return "下午好";
  if (hour < 19) return "傍晚好";
  return "晚上好";
};

const filterPhoto = computed(() => (photo) => {
  return import.meta.env.VITE_SERVER + "/photo/view?filename=" + photo;
});

const initChart = () => {
  chartInstance.value = echarts.init(refChart.value);
  chartInstance.value.setOption(initOption.value);
};

const resizeChart = () => {
  if (chartInstance.value) {
    chartInstance.value.resize();
  }
};

const getLoginUser = async () => {
  const token = getSessionStorage();
  if (!token) {
    data.loginUser = {
      id: 1,
      headPic: "common/no_image.jpg",
      username: "访客",
      roleId: 3,
    };
    getStatisticTotal();
    return;
  }

  const response = await axiosPostRequest("/user/check_login", {
    token: token,
  });
  if (response.code === 0) {
    data.loginUser = response.data;
    getStatisticTotal();
    getFeeStatistic();
  } else {
    data.loginUser = {
      id: 1,
      headPic: "common/no_image.jpg",
      username: "访客",
      roleId: 3,
    };
    getStatisticTotal();
  }
};

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

onMounted(() => {
  const now = new Date();
  const numMonths = 4;
  const lastFiveMonths = eachMonthOfInterval({
    start: subMonths(now, numMonths),
    end: now,
  });
  let xAxis = [];
  lastFiveMonths.forEach((date) => {
    xAxis.push(format(date, "yyyy-MM"));
  });
  initOption.value.xAxis.data = xAxis;

  initChart();
  getLoginUser();
  window.addEventListener("resize", resizeChart);
});

onUnmounted(() => {
  if (chartInstance.value) {
    chartInstance.value.dispose();
    chartInstance.value = null;
  }
  window.removeEventListener("resize", resizeChart);
});
</script>

<style lang="scss" scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 32px;
  height: 100%;
}

// 欢迎区域
.welcome-section {
  padding: 40px 0;
  border-bottom: 1px solid #E5E5E5;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text {
  h1 {
    font-size: 28px;
    font-weight: 600;
    color: #1A1A1A;
    margin-bottom: 8px;
    letter-spacing: -0.02em;
  }

  p {
    font-size: 14px;
    color: #6B7280;
    letter-spacing: 0.01em;
  }
}

.welcome-avatar {
  img {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    border: 2px solid #E5E5E5;
    object-fit: cover;
  }
}

// 统计卡片
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-card {
  background: #FFFFFF;
  border: 1px solid #E5E5E5;
  padding: 24px;
  position: relative;
  transition: all 0.2s ease;

  &:hover {
    border-color: #2563EB;
  }
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.stat-label {
  font-size: 12px;
  font-weight: 500;
  color: #6B7280;
  letter-spacing: 0.02em;
  text-transform: uppercase;
}

.stat-index {
  font-size: 11px;
  font-weight: 600;
  color: #2563EB;
  letter-spacing: 0.05em;
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  color: #1A1A1A;
  letter-spacing: -0.02em;
}

.stat-icon {
  position: absolute;
  bottom: 24px;
  right: 24px;

  svg {
    width: 20px;
    height: 20px;
    color: #2563EB;
  }
}

// 图表区域
.chart-section {
  background: #FFFFFF;
  border: 1px solid #E5E5E5;
  padding: 32px;
  flex: 1;
  min-height: 300px;
  display: flex;
  flex-direction: column;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #1A1A1A;
    letter-spacing: -0.01em;
  }
}

.chart-legend {
  display: flex;
  gap: 24px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #6B7280;
  letter-spacing: 0.01em;
}

.legend-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.chart-container {
  width: 100%;
  flex: 1;
  min-height: 240px;
}

// 响应式
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .welcome-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }

  .welcome-text h1 {
    font-size: 22px;
  }
}
</style>
