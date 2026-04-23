<template>
  <div class="login-container">
    <!-- 左侧登录区 -->
    <div class="login-left">
      <div class="login-card">
        <div class="card-header">
          <div class="brand-mark">
            <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="8" y="8" width="32" height="32" stroke="#2563EB" stroke-width="2"/>
              <rect x="14" y="14" width="20" height="20" stroke="#2563EB" stroke-width="1.5"/>
              <rect x="20" y="20" width="8" height="8" fill="#2563EB"/>
            </svg>
          </div>
          <h1>小区物业管理系统</h1>
          <p>Property Management System</p>
        </div>

        <form class="login-form" @submit.prevent="handleLogin">
          <div class="form-group">
            <label class="form-label">手机号</label>
            <input
              type="text"
              v-model="loginForm.phone"
              class="form-input"
              placeholder="请输入手机号"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">密码</label>
            <input
              type="password"
              v-model="loginForm.password"
              class="form-input"
              placeholder="请输入密码"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">角色</label>
            <div class="role-selector">
              <label
                v-for="role in roles"
                :key="role.id"
                class="role-option"
                :class="{ active: loginForm.roleId === role.id }"
              >
                <input
                  type="radio"
                  :value="role.id"
                  v-model="loginForm.roleId"
                  hidden
                />
                <span class="role-name">{{ role.name }}</span>
              </label>
            </div>
          </div>

          <button type="submit" class="login-btn" :disabled="loginLoading">
            <span v-if="!loginLoading">登 录</span>
            <span v-else class="loading"></span>
          </button>
        </form>

        <div class="card-footer">
          <p>智慧社区 · 温馨家园</p>
        </div>
      </div>
    </div>

    <!-- 右侧装饰区 -->
    <div class="login-right">
      <div class="right-content">
        <div class="typography-showcase">
          <span class="large-text">P</span>
          <span class="medium-text">roperty</span>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-number">01</span>
            <span class="info-label">专业管理</span>
          </div>
          <div class="info-item">
            <span class="info-number">02</span>
            <span class="info-label">高效服务</span>
          </div>
          <div class="info-item">
            <span class="info-number">03</span>
            <span class="info-label">智慧社区</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 登录成功动画 -->
    <div class="success-overlay" :class="{ active: showOverlay }">
      <div class="success-content">
        <div class="success-icon">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="24" cy="24" r="22" stroke="#2563EB" stroke-width="2"/>
            <path d="M14 24L21 31L34 18" stroke="#2563EB" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <p>登录成功</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { axiosPostRequest, setSessionStorage } from "@/util/index.js";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();
const loginLoading = ref(false);
const showOverlay = ref(false);

const loginForm = reactive({
  phone: "",
  password: "",
  roleId: 1,
});

const roles = [
  { id: 1, name: "业主" },
  { id: 2, name: "物业员工" },
  { id: 3, name: "管理员" },
];

const handleLogin = async () => {
  if (!loginForm.phone || !loginForm.password) {
    ElMessage.warning("请输入手机号和密码");
    return;
  }

  loginLoading.value = true;
  const response = await axiosPostRequest("/user/login", loginForm);

  if (response.code === 0) {
    ElMessage.success(response.msg);
    setSessionStorage(response.data.token);
    showOverlay.value = true;

    setTimeout(() => {
      router.push("/index-a");
    }, 1500);
  } else {
    ElMessage.error(response.msg);
  }

  loginLoading.value = false;
};
</script>

<style lang="scss" scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  background: #FFFFFF;
  overflow: hidden;
}

// 左侧登录区
.login-left {
  width: 480px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  background: #FFFFFF;
  border-right: 1px solid #E5E5E5;
}

.login-card {
  width: 100%;
  max-width: 360px;
}

.card-header {
  margin-bottom: 48px;

  .brand-mark {
    width: 48px;
    height: 48px;
    margin-bottom: 24px;

    svg {
      width: 100%;
      height: 100%;
    }
  }

  h1 {
    font-size: 24px;
    font-weight: 600;
    color: #1A1A1A;
    letter-spacing: -0.02em;
    margin-bottom: 8px;
  }

  p {
    font-size: 14px;
    color: #6B7280;
    letter-spacing: 0.01em;
  }
}

.login-form {
  .form-group {
    margin-bottom: 24px;
  }

  .form-label {
    display: block;
    font-size: 12px;
    font-weight: 500;
    color: #6B7280;
    letter-spacing: 0.02em;
    margin-bottom: 8px;
  }

  .form-input {
    width: 100%;
    padding: 12px 16px;
    font-size: 14px;
    color: #1A1A1A;
    background: #F9FAFB;
    border: 1px solid #E5E5E5;
    border-radius: 6px;
    outline: none;
    transition: all 0.2s ease;

    &::placeholder {
      color: #9CA3AF;
    }

    &:focus {
      border-color: #2563EB;
      background: #FFFFFF;
      box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
    }
  }
}

.role-selector {
  display: flex;
  gap: 16px;

  .role-option {
    cursor: pointer;
    padding: 8px 16px;
    border: 1px solid #E5E5E5;
    border-radius: 6px;
    transition: all 0.2s ease;

    &:hover {
      border-color: #D1D5DB;
    }

    &.active {
      border-color: #2563EB;
      background: rgba(37, 99, 235, 0.05);

      .role-name {
        color: #2563EB;
      }
    }

    .role-name {
      font-size: 13px;
      color: #6B7280;
      transition: color 0.2s ease;
    }
  }
}

.login-btn {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  font-weight: 500;
  color: #FFFFFF;
  background: #2563EB;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 8px;

  &:hover:not(:disabled) {
    background: #1D4ED8;
  }

  &:active:not(:disabled) {
    transform: scale(0.98);
  }

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .loading {
    display: inline-block;
    width: 16px;
    height: 16px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    border-top-color: #FFFFFF;
    animation: spin 0.8s linear infinite;
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.card-footer {
  margin-top: 64px;
  text-align: center;

  p {
    font-size: 12px;
    color: #9CA3AF;
    letter-spacing: 0.02em;
  }
}

// 右侧装饰区
.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #F9FAFB;
  position: relative;
  overflow: hidden;
}

.right-content {
  position: relative;
  z-index: 1;
  padding: 60px;
}

.typography-showcase {
  margin-bottom: 80px;

  .large-text {
    font-size: 120px;
    font-weight: 700;
    color: #1A1A1A;
    line-height: 1;
    letter-spacing: -0.04em;
  }

  .medium-text {
    font-size: 48px;
    font-weight: 300;
    color: #6B7280;
    letter-spacing: -0.02em;
  }
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 16px;

  .info-number {
    font-size: 12px;
    font-weight: 600;
    color: #2563EB;
    letter-spacing: 0.05em;
  }

  .info-label {
    font-size: 14px;
    color: #6B7280;
    letter-spacing: 0.02em;
  }
}

// 成功动画
.success-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(26, 26, 26, 0.95);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  opacity: 0;
  visibility: hidden;
  transition: all 0.4s ease;

  &.active {
    opacity: 1;
    visibility: visible;
  }
}

.success-content {
  text-align: center;
  transform: translateY(20px);
  opacity: 0;
  transition: all 0.4s ease 0.2s;

  .active & {
    transform: translateY(0);
    opacity: 1;
  }

  p {
    margin-top: 24px;
    font-size: 16px;
    color: #FFFFFF;
    letter-spacing: 0.05em;
    font-weight: 500;
  }
}

.success-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto;

  svg {
    width: 100%;
    height: 100%;
  }
}

// 响应式
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }

  .login-left {
    width: 100%;
    flex: 1;
    padding: 40px 24px;
  }

  .login-right {
    display: none;
  }

  .typography-showcase {
    display: none;
  }
}
</style>
