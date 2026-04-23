<template>
  <div class="bigBox">
    <div class="box">
      <!-- 左侧内容简介 -->
      <div class="info-box">
        <div class="info-content">
          <h1>小区物业管理系统</h1>
          <p class="slogan">智慧社区，便捷生活</p>
          <div class="features">
            <div class="feature-item">
              <div class="feature-icon">🏠</div>
              <div class="feature-text">
                <h3>业主服务</h3>
                <p>在线缴费、报修、投诉</p>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">👨‍💼</div>
              <div class="feature-text">
                <h3>物业管理</h3>
                <p>工单管理、费用管理</p>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">👑</div>
              <div class="feature-text">
                <h3>管理员</h3>
                <p>系统配置、用户管理</p>
              </div>
            </div>
          </div>
          <div class="footer">
            <p>&copy; 2026 小区物业管理系统</p>
          </div>
        </div>
      </div>
      
      <!-- 右侧登录盒子 -->
      <div class="login-form">
        <div class="login-title">
          <h2>用户登录</h2>
          <p>请输入您的账号和密码</p>
        </div>
        <el-form ref="loginFormRef" :model="loginForm">
          <el-form-item prop="phone">
            <el-input
              type="text"
              v-model="loginForm.phone"
              placeholder="请输入手机号"
              prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              v-model="loginForm.password"
              placeholder="请输入用户密码"
              show-password
              prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item prop="roleId">
            <div style="width: 100%; text-align: center">
              <el-radio-group v-model="loginForm.roleId">
                <el-radio :label="1">业主</el-radio>
                <el-radio :label="2">物业员工</el-radio>
                <el-radio :label="3">管理员</el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
        </el-form>
        <!-- 登录按钮 -->
        <div class="login-btn-box">
          <el-button
            class="login-btn"
            :loading="loginLoading"
            @click="handleLogin"
            >登录</el-button
          >
        </div>
      </div>
    </div>
    
    <!-- 登录成功后渐变覆盖层 -->
    <div class="login-overlay" :class="{ 'active': showOverlay }">
      <div class="overlay-content">
        <div class="success-icon">✓</div>
        <div class="success-text">登录成功</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { axiosPostRequest, setSessionStorage } from "@/util/index.js";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
const router = useRouter();

const loginFormRef = ref();
const loginLoading = ref(false);
const showOverlay = ref(false);

const loginForm = reactive({
  phone: "",
  password: "",
  roleId: 1,
});

// 登录操作
const handleLogin = async () => {
  loginLoading.value = true;
  const response = await axiosPostRequest("/user/login", loginForm);
  if (response.code === 0) {
    ElMessage.success(response.msg);
    setSessionStorage(response.data.token);
    loginFormRef.value.resetFields();
    
    // 显示渐变覆盖层
    showOverlay.value = true;
    
    // 0.5秒后跳转到首页
    setTimeout(() => {
      router.push("/index");
    }, 500);
  } else {
    ElMessage.error(response.msg);
  }
  loginLoading.value = false;
};
</script>

<style lang="scss" scoped>
.bigBox {
  display: flex;
  height: 100vh;
  overflow-x: hidden;
  background: linear-gradient(135deg, #20a162 0%, #1e90ff 100%);

  .box {
    position: relative;
    z-index: 2;
    display: flex;
    width: 900px;
    height: 520px;
    margin: auto;
    border-radius: 16px;
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
    overflow: hidden;
    background: #fff;
    
    .info-box {
      flex: 1;
      background: linear-gradient(145deg, #20a162 0%, #1a8f52 100%);
      color: #fff;
      padding: 60px 40px;
      display: flex;
      align-items: center;
      
      .info-content {
        width: 100%;
        
        h1 {
          font-size: 28px;
          font-weight: bold;
          margin-bottom: 10px;
          text-align: center;
        }
        
        .slogan {
          font-size: 16px;
          margin-bottom: 40px;
          text-align: center;
          opacity: 0.9;
        }
        
        .features {
          margin-bottom: 60px;
          
          .feature-item {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            
            .feature-icon {
              font-size: 22px;
              margin-right: 15px;
              width: 42px;
              height: 42px;
              border-radius: 10px;
              background: rgba(255, 255, 255, 0.2);
              display: flex;
              align-items: center;
              justify-content: center;
            }
            
            .feature-text {
              flex: 1;
              
              h3 {
                font-size: 15px;
                font-weight: bold;
                margin-bottom: 5px;
              }
              
              p {
                font-size: 13px;
                opacity: 0.8;
                margin: 0;
              }
            }
          }
        }
        
        .footer {
          text-align: center;
          font-size: 12px;
          opacity: 0.7;
        }
      }
    }
    
    .login-form {
      flex: 1;
      padding: 50px 40px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      
      .login-title {
        text-align: center;
        margin-bottom: 35px;
        
        h2 {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 10px;
        }
        
        p {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
      
      .el-form {
        width: 100%;
        
        .el-form-item {
          margin-bottom: 22px;
        }
      }
      
      .login-btn-box {
        margin-top: 25px;
        
        .login-btn {
          width: 100%;
          height: 44px;
          font-size: 16px;
          background: linear-gradient(135deg, #20a162 0%, #1e90ff 100%);
          border: none;
          color: #fff;
          border-radius: 10px;
          font-weight: 500;
          transition: all 0.3s ease;
          
          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(32, 161, 98, 0.35);
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .bigBox {
    .box {
      flex-direction: column;
      width: 100%;
      height: 100vh;
      border-radius: 0;
      
      .info-box {
        padding: 40px 20px;
        min-height: 280px;
      }
      
      .login-form {
        padding: 40px 20px;
      }
    }
  }
}

// 登录成功渐变覆盖层
.login-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #20a162 0%, #1e90ff 100%);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  visibility: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  
  &.active {
    opacity: 1;
    visibility: visible;
    
    .overlay-content {
      transform: scale(1);
      opacity: 1;
    }
  }
  
  .overlay-content {
    text-align: center;
    color: #fff;
    transform: scale(0.8);
    opacity: 0;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) 0.1s;
    
    .success-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 30px;
      font-weight: bold;
      margin: 0 auto 15px;
      animation: pulse 0.5s ease-in-out;
    }
    
    .success-text {
      font-size: 20px;
      font-weight: bold;
      animation: fadeInUp 0.3s ease-out 0.1s both;
    }
  }
}

@keyframes pulse {
  0% {
    transform: scale(0.8);
    opacity: 0;
  }
  50% {
    transform: scale(1.1);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes fadeInUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
