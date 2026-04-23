<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        v-if="dataList.loginUser.roleId !== 2"
        ref="searchFormRef"
        :inline="true"
        :model="dataList.searchParams"
      >
        <el-form-item label="物业员工姓名" prop="username">
          <el-input
            v-model="dataList.searchParams.username"
            placeholder="请输入物业员工姓名"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input
            v-model="dataList.searchParams.phone"
            placeholder="请输入手机号码"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain @click="getUserList"
            >搜索</el-button
          >
          <el-button type="danger" icon="refresh" plain @click="resetSearchForm"
            >重置</el-button
          >
        </el-form-item>
      </el-form>

      <el-row :gutter="10">
        <el-col :span="1.5" v-if="dataList.loginUser.roleId === 3">
          <el-button type="primary" icon="plus" plain @click="openAddDialog"
            >新增</el-button
          >
        </el-col>
        <el-col
          :span="1.5"
          v-if="
            dataList.loginUser.roleId === 3 || dataList.loginUser.roleId === 2
          "
        >
          <el-button type="success" icon="edit" plain @click="openEditDialog"
            >修改</el-button
          >
        </el-col>
        <el-col :span="1.5" v-if="dataList.loginUser.roleId === 3">
          <el-button type="danger" icon="delete" plain @click="openRemoveDialog"
            >删除</el-button
          >
        </el-col>
      </el-row>

      <el-table
        border
        @selection-change="handleSelectionChange"
        v-adaptive
        :data="dataList.userList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="140px"></el-table-column>
        <el-table-column
          label="物业员工姓名"
          prop="username"
          width="200px"
        ></el-table-column>
        <el-table-column label="物业员工头像" width="145px">
          <template #default="scope">
            <div>
              <el-image
                :preview-teleported="true"
                :preview-src-list="[filterPhoto(scope.row.headPic)]"
                style="width: 120px; height: 80px"
                :src="filterPhoto(scope.row.headPic)"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="手机号码"
          prop="phone"
          width="200px"
        ></el-table-column>
        <el-table-column
          label="身份证号"
          prop="identifyCard"
          width="200px"
        ></el-table-column>
        <el-table-column label="物业员工性别" width="150px">
          <template #default="scope">
            <div>
              <span v-if="scope.row.sex === 1">男</span>
              <span v-if="scope.row.sex === 2">女</span>
              <span v-if="scope.row.sex === 3">未知</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="所属小区" width="200px">
          <template #default="scope">
            <div
              v-if="scope.row.districtDTO"
              v-text="
                scope.row.districtDTO?.name +
                '(' +
                scope.row.districtDTO?.id +
                ')'
              "
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column
          label="出生日期"
          prop="birthDate"
          width="150px"
        ></el-table-column>
      </el-table>

      <el-pagination
        background
        v-model:current-page="paginationProps.current"
        v-model:page-size="paginationProps.pageSize"
        :page-sizes="[10, 20, 50, 100, 200]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="paginationProps.total"
        @change="onPageChange"
      />
    </el-card>

    <Dialog ref="userDialogRef" :title="title" @onConfirm="saveUser">
      <template #content>
        <el-form :model="dataList.userForm" label-width="100px">
          <el-form-item label="物业员工姓名">
            <el-input
              v-model="dataList.userForm.username"
              placeholder="请输入物业员工姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="物业员工头像">
            <div style="display: flex; align-items: center">
              <el-image
                id="photo-view"
                :src="filterPhoto(dataList.userForm.headPic)"
                style="width: 100px; height: 70px; margin-right: 10px"
              />
              <el-button
                type="primary"
                icon="UploadFilled"
                plain
                @click="openUpload"
                >上传图片</el-button
              >
              <input
                type="file"
                id="photo-file"
                style="display: none"
                @change="uploadPhoto"
              />
            </div>
          </el-form-item>
          <el-form-item label="物业员工密码">
            <el-input
              type="password"
              v-model="dataList.userForm.password"
              placeholder="请输入物业员工昵称"
            ></el-input>
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input
              v-model="dataList.userForm.phone"
              placeholder="请输入手机号码"
            ></el-input>
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input
              v-model="dataList.userForm.identifyCard"
              placeholder="请输入身份证号"
            ></el-input>
          </el-form-item>
          <el-form-item label="物业员工性别">
            <el-select
              v-model="dataList.userForm.sex"
              placeholder="请选择物业员工性别"
            >
              <el-option label="男" :value="1" />
              <el-option label="女" :value="2" />
              <el-option label="未知" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker
              v-model="dataList.userForm.birthDate"
              type="date"
              placeholder="请选择出生日期"
            />
          </el-form-item>
          <el-form-item label="所属小区">
            <el-select
              v-model="dataList.userForm.districtId"
              placeholder="请选择所属小区"
            >
              <el-option
                v-for="(item, index) in dataList.districtList"
                :key="index"
                :label="item.name + '(' + item.id + ')'"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </template>
    </Dialog>
    <Dialog
      ref="confirmDialogRef"
      :width="width"
      :title="title"
      @onConfirm="removeUser"
    >
      <template #content>
        <div
          style="display: flex; justify-content: center; font-size: 16px"
          v-text="confirmDesc"
        ></div>
      </template>
    </Dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import Dialog from "@/components/Dialog/index.vue";
import {
  axiosPostRequest,
  getSessionStorage,
  myEventBus,
} from "@/util/index.js";
import { useRouter } from "vue-router";
const emitter = myEventBus();
emitter.on("refresh", () => {
  getLoginUser();
});
onMounted(() => {
  getLoginUser();
});
const router = useRouter();
const searchFormRef = ref();
const userDialogRef = ref();
const confirmDialogRef = ref();
const title = ref("");
const confirmDesc = ref("");
const width = ref("50%");

const paginationProps = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
});

// 获取当前登录用户
const getLoginUser = async () => {
  const response = await axiosPostRequest("/user/check_login", {
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    dataList.loginUser = response.data;
    getUserList();
    getAllDistrict();
  } else {
    ElMessage.error(response.msg);
    router.push("/login");
  }
};

// 获取所有小区信息
const getAllDistrict = async () => {
  const response = await axiosPostRequest("/district/all");
  if (response.code === 0) {
    dataList.districtList = response.data;
  }
};

// 重置搜索表单
const resetSearchForm = () => {
  searchFormRef.value.resetFields();
};

const dataList = reactive({
  searchParams: {
    username: "",
    phone: "",
    roleId: 2,
  },
  loginUser: {
    id: "",
    roleId: "",
  },
  userForm: {
    id: "",
    username: "",
    password: "",
    headPic: "common/no_image.jpg",
    phone: "",
    identifyCard: "",
    sex: 3,
    birthDate: "",
    districtId: "",
  },
  multipleSelection: [],
  districtList: [],
  userList: [],
});

const filterPhoto = computed(() => (photo) => {
  return import.meta.env.VITE_SERVER + "/photo/view?filename=" + photo;
});

// 打开上传图片窗口
const openUpload = () => {
  document.getElementById("photo-file").click();
};
// 上传图片操作
const uploadPhoto = async () => {
  let fileObj = document.getElementById("photo-file");
  if (fileObj.value === "") return;
  let config = {
    headers: { "Content-Type": "multipart/form-data" },
  };
  let formData = new FormData();
  formData.append("photo", fileObj.files[0]);
  const response = await axiosPostRequest("/photo/upload", formData, config);
  fileObj.value = "";
  if (response.code === 0) {
    ElMessage.success(response.msg);
    document.getElementById("photo-view").src =
      import.meta.env.VITE_SERVER + "/photo/view?filename=" + response.data;
    dataList.userForm.headPic = response.data;
  } else {
    ElMessage.error(response.msg);
  }
};

// 打开添加物业员工的模态框
const openAddDialog = () => {
  title.value = "添加物业员工信息";
  dataList.userForm = {
    id: "",
    username: "",
    password: "",
    headPic: "common/no_image.jpg",
    identifyCard: "",
    phone: "",
    sex: 3,
    birthDate: "",
    districtId: "",
  };
  userDialogRef.value.openDialog();
};

// 打开编辑物业员工的模态框
const openEditDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行编辑！");
    return;
  }
  title.value = "编辑物业员工信息";
  dataList.userForm = JSON.parse(JSON.stringify(dataList.multipleSelection[0]));
  userDialogRef.value.openDialog();
};

// 打开删除物业员工的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.userForm = JSON.parse(JSON.stringify(dataList.multipleSelection[0]));
  confirmDialogRef.value.openDialog();
};

// 删除物业员工信息
const removeUser = async () => {
  const response = await axiosPostRequest("/user/delete", dataList.userForm);
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getUserList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存物业员工信息
const saveUser = async () => {
  const response = await axiosPostRequest("/user/save", {
    ...dataList.userForm,
    roleId: 2,
    token: getSessionStorage(),
    position: "暂无职位",
    study: "暂无学历",
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    userDialogRef.value.closeDialog();
    getUserList();
    emitter.emit("refresh");
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getUserList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取物业员工信息
const getUserList = async () => {
  const response = await axiosPostRequest("/user/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      districtId:
        dataList.loginUser.roleId === 1 ? dataList.loginUser.districtId : "",
      id: dataList.loginUser.roleId === 2 ? dataList.loginUser.id : "",
      username: dataList.searchParams.username,
      roleId: dataList.searchParams.roleId,
      phone: dataList.searchParams.phone,
    },
  });
  if (response.code === 0) {
    dataList.userList = response.data.list;
    paginationProps.total = response.data.total;
  } else {
    ElMessage.error(response.msg);
  }
};
</script>
<style lang="scss" scoped>
.card-list {
  border-radius: 0.375rem;
  .el-table {
    margin-top: 20px;
  }
  .el-pagination {
    margin-top: 20px;
  }
}
</style>
