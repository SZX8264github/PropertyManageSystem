<template>
  <div class="register-container">
    <div class="register-box">
      <h3>用户注册</h3>
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="form.birthDate"
            type="date"
            placeholder="选择出生日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="身份证号" prop="identifyCard">
          <el-input v-model="form.identifyCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="所属小区" prop="districtId">
          <el-select v-model="form.districtId" placeholder="请选择小区">
            <el-option
              v-for="district in districtList"
              :key="district.id"
              :label="district.name"
              :value="district.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="2">女</el-radio>
            <el-radio label="3">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="学历" prop="study">
          <el-input v-model="form.study" placeholder="请输入学历"></el-input>
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position" placeholder="请输入职位"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" :loading="loading">注册</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from '@/util/request'
import { ElMessage } from 'element-plus'
import { md5 } from '@/util/index.js'

export default {
  name: 'Register',
  data() {
    return {
      loading: false,
      form: {
        username: '',
        phone: '',
        password: '',
        confirmPassword: '',
        birthDate: '',
        identifyCard: '',
        districtId: '',
        sex: 3,
        study: '',
        position: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式错误', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.form.password) {
                callback(new Error('两次输入密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        birthDate: [
          { required: true, message: '请选择出生日期', trigger: 'blur' }
        ],
        identifyCard: [
          { required: true, message: '请输入身份证号', trigger: 'blur' }
        ],
        districtId: [
          { required: false, message: '请选择所属小区', trigger: 'blur' }
        ]
      },
      districtList: []
    }
  },
  mounted() {
    this.getDistrictList()
  },
  methods: {
    getDistrictList() {
      request.post('/district/all', {}).then(res => {
        if (res.code === 0) {
          this.districtList = res.data
        }
      })
    },
    register() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          // 对密码进行加密处理，并排除confirmPassword字段
          const formData = {
            ...this.form,
            password: md5(this.form.password)
          }
          // 删除confirmPassword字段，不需要发送到后端
          delete formData.confirmPassword
          request.post('/user/register', formData).then(res => {
            this.loading = false
            if (res.code === 0) {
              ElMessage.success('注册成功！')
              this.$router.push('/login')
            } else {
              ElMessage.error(res.message)
            }
          })
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
    },
    goLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.register-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-box {
  width: 500px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-box h3 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
</style>