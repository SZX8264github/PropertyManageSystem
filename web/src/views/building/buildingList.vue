<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        ref="searchFormRef"
        :inline="true"
        :model="dataList.searchParams"
      >
        <el-form-item label="楼栋名称" prop="name">
          <el-input
            v-model="dataList.searchParams.name"
            placeholder="请输入楼栋名称"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="所属小区"
          prop="districtId"
          v-if="dataList.loginUser.roleId === 3"
        >
          <el-select
            v-model="dataList.searchParams.districtId"
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
        <el-form-item>
          <el-button type="primary" icon="search" plain @click="getBuildingList"
            >搜索</el-button
          >
          <el-button type="danger" icon="refresh" plain @click="resetSearchForm"
            >重置</el-button
          >
        </el-form-item>
      </el-form>

      <el-row
        :gutter="10"
        v-if="
          dataList.loginUser.roleId === 2 || dataList.loginUser.roleId === 3
        "
      >
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
        :data="dataList.buildingList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="140px"></el-table-column>
        <el-table-column
          label="楼栋名称"
          prop="name"
          width="200px"
        ></el-table-column>
        <el-table-column
          label="单元名称"
          prop="unitName"
          width="200px"
        ></el-table-column>
        <el-table-column label="所属小区" :show-overflow-tooltip="true">
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

    <Dialog ref="buildingDialogRef" :title="title" @onConfirm="saveBuilding">
      <template #content>
        <el-form :model="dataList.buildingForm" label-width="100px">
          <el-form-item label="楼栋名称">
            <el-input
              v-model="dataList.buildingForm.name"
              placeholder="请输入楼栋名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="单元名称">
            <el-input
              v-model="dataList.buildingForm.unitName"
              placeholder="请输入单元名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="所属小区" v-if="dataList.loginUser.roleId === 3">
            <el-select
              v-model="dataList.buildingForm.districtId"
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
      @onConfirm="removeBuilding"
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
const buildingDialogRef = ref();
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
    getBuildingList();
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
    name: "",
    districtId: "",
  },
  loginUser: {
    id: "",
    roleId: "",
  },
  buildingForm: {
    id: "",
    name: "",
    unitName: "",
    districtId: "",
  },
  multipleSelection: [],
  districtList: [],
  buildingList: [],
});

// 打开添加楼栋的模态框
const openAddDialog = () => {
  title.value = "添加楼栋信息";
  dataList.buildingForm = {
    id: "",
    name: "",
    unitName: "",
    districtId: "",
  };
  buildingDialogRef.value.openDialog();
};

// 打开编辑楼栋的模态框
const openEditDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行编辑！");
    return;
  }
  title.value = "编辑楼栋信息";
  dataList.buildingForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  buildingDialogRef.value.openDialog();
};

// 打开删除楼栋的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.buildingForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  confirmDialogRef.value.openDialog();
};

// 删除楼栋信息
const removeBuilding = async () => {
  const response = await axiosPostRequest(
    "/building/delete",
    dataList.buildingForm
  );
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getBuildingList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存楼栋信息
const saveBuilding = async () => {
  const response = await axiosPostRequest("/building/save", {
    ...dataList.buildingForm,
    districtId:
      dataList.loginUser.roleId === 3
        ? dataList.buildingForm.districtId
        : dataList.loginUser.districtId,
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    buildingDialogRef.value.closeDialog();
    getBuildingList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getBuildingList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取楼栋信息
const getBuildingList = async () => {
  const response = await axiosPostRequest("/building/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      districtId:
        dataList.loginUser.roleId === 2
          ? dataList.loginUser.districtId
          : dataList.searchParams.districtId,
      name: dataList.searchParams.name,
    },
  });
  if (response.code === 0) {
    dataList.buildingList = response.data.list;
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
