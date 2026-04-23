import axios from 'axios';
import { getSessionStorage } from './index.js';

const BASE_URL = import.meta.env.VITE_WEB_BASE_API;

// 创建axios实例
const service = axios.create({
  baseURL: BASE_URL,
  timeout: 10000
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从sessionStorage中获取token
    const token = getSessionStorage();
    if (token) {
      config.headers.token = token;
    }
    return config;
  },
  error => {
    console.log(error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    console.log('err' + error);
    return Promise.reject(error);
  }
);

// 封装post请求
export default {
  post(url, data) {
    return service({
      method: 'post',
      url,
      data
    });
  }
};
