<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        ref="searchFormRef"
        :inline="true"
        v-if="dataList.loginUser.roleId === 3"
        :model="dataList.searchParams"
      >
        <el-form-item label="小区名称" prop="name">
          <el-input
            v-model="dataList.searchParams.name"
            placeholder="请输入小区名称"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="小区地址" prop="location">
          <el-input
            v-model="dataList.searchParams.location"
            placeholder="请输入小区地址"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain @click="getDistrictList"
            >搜索</el-button
          >
          <el-button type="danger" icon="refresh" plain @click="resetSearchForm"
            >重置</el-button
          >
        </el-form-item>
      </el-form>

      <el-row :gutter="10" v-if="dataList.loginUser.roleId === 3">
        <el-col :span="1.5">
          <el-button type="primary" icon="plus" plain @click="openAddDialog"
            >新增</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="edit" plain @click="openEditDialog"
            >修改</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="delete" plain @click="openRemoveDialog"
            >删除</el-button
          >
        </el-col>
      </el-row>

      <el-table
        border
        @selection-change="handleSelectionChange"
        v-adaptive
        :data="dataList.districtList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="140px"></el-table-column>
        <el-table-column
          label="小区名称"
          prop="name"
          width="150px"
        ></el-table-column>
        <el-table-column label="小区图片" width="145px">
          <template #default="scope">
            <div>
              <el-image
                :preview-teleported="true"
                :preview-src-list="[filterPhoto(scope.row.photo)]"
                style="width: 120px; height: 80px"
                :src="filterPhoto(scope.row.photo)"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="小区地址"
          prop="location"
          width="200px"
        ></el-table-column>
        <el-table-column label="占地面积" width="100px">
          <template #default="scope">
            <div v-text="scope.row.area + '㎡'"></div>
          </template>
        </el-table-column>
        <el-table-column
          label="楼栋数"
          prop="buildingTotal"
          width="100px"
        ></el-table-column>
        <el-table-column
          label="房屋数"
          prop="houseTotal"
          width="100px"
        ></el-table-column>
        <el-table-column
          label="车位数"
          prop="parkingTotal"
          width="100px"
        ></el-table-column>
        <el-table-column
          label="开发商名称"
          prop="devName"
          width="150px"
        ></el-table-column>
        <el-table-column
          label="物业公司名称"
          prop="propertyName"
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

    <Dialog ref="districtDialogRef" :title="title" @onConfirm="savedistrict">
      <template #content>
        <el-form :model="dataList.districtForm" label-width="100px">
          <el-form-item label="小区名称">
            <el-input
              v-model="dataList.districtForm.name"
              placeholder="请输入小区名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="小区图片">
            <div style="display: flex; align-items: center">
              <el-image
                id="photo-view"
                :src="filterPhoto(dataList.districtForm.photo)"
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
          <el-form-item label="小区地址">
            <el-input
              v-model="dataList.districtForm.location"
              placeholder="请输入小区地址"
            ></el-input>
          </el-form-item>
          <el-form-item label="占地面积(㎡)">
            <el-input-number
              :min="0.0"
              :max="99999999.99"
              :precision="2"
              v-model="dataList.districtForm.area"
              placeholder="请输入占地面积(㎡)"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="开发商名称">
            <el-input
              v-model="dataList.districtForm.devName"
              placeholder="请输入开发商名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="物业公司名称">
            <el-input
              v-model="dataList.districtForm.propertyName"
              placeholder="请输入物业公司名称"
            ></el-input>
          </el-form-item>
        </el-form>
      </template>
    </Dialog>
    <Dialog
      ref="confirmDialogRef"
      :width="width"
      :title="title"
      @onConfirm="removedistrict"
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
import { axiosPostRequest, getSessionStorage } from "@/util/index.js";
import { useRouter } from "vue-router";
onMounted(() => {
  getLoginUser();
});
const router = useRouter();
const searchFormRef = ref();
const districtDialogRef = ref();
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
    getDistrictList();
  } else {
    ElMessage.error(response.msg);
    router.push("/login");
  }
};

// 重置搜索表单
const resetSearchForm = () => {
  searchFormRef.value.resetFields();
};

const dataList = reactive({
  searchParams: {
    name: "",
    location: "",
  },
  loginUser: {
    id: "",
    roleId: "",
  },
  districtForm: {
    id: "",
    name: "",
    photo: "common/no_image.jpg",
    location: "",
    devName: "",
    area: 0.0,
    propertyName: "",
  },
  multipleSelection: [],
  districtList: [],
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
    dataList.districtForm.photo = response.data;
  } else {
    ElMessage.error(response.msg);
  }
};

// 打开添加小区的模态框
const openAddDialog = () => {
  title.value = "添加小区信息";
  dataList.districtForm = {
    id: "",
    name: "",
    photo: "common/no_image.jpg",
    location: "",
    devName: "",
    area: 0.0,
    propertyName: "",
  };
  districtDialogRef.value.openDialog();
};

// 打开编辑小区的模态框
const openEditDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行编辑！");
    return;
  }
  title.value = "编辑小区信息";
  dataList.districtForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  districtDialogRef.value.openDialog();
};

// 打开删除小区的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.districtForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  confirmDialogRef.value.openDialog();
};

// 删除小区信息
const removedistrict = async () => {
  const response = await axiosPostRequest(
    "/district/delete",
    dataList.districtForm
  );
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getDistrictList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存小区信息
const savedistrict = async () => {
  const response = await axiosPostRequest("/district/save", {
    ...dataList.districtForm,
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    districtDialogRef.value.closeDialog();
    getDistrictList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getDistrictList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取小区信息
const getDistrictList = async () => {
  const response = await axiosPostRequest("/district/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      id: dataList.loginUser.roleId === 3 ? "" : dataList.loginUser.districtId,
      name: dataList.searchParams.name,
      location: dataList.searchParams.location,
    },
  });
  if (response.code === 0) {
    dataList.districtList = response.data.list;
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
