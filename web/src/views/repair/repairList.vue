<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        ref="searchFormRef"
        :inline="true"
        :model="dataList.searchParams"
      >
        <el-form-item label="维修内容描述" prop="content">
          <el-input
            v-model="dataList.searchParams.content"
            placeholder="请输入维修内容描述"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="所属业主"
          prop="userId"
          v-if="dataList.loginUser.roleId !== 1"
        >
          <el-select
            v-model="dataList.searchParams.userId"
            placeholder="请选择所属业主"
          >
            <el-option
              v-for="(item, index) in dataList.ownerList"
              :key="index"
              :label="item.username + '(' + item.phone + ')'"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain @click="getRepairList"
            >搜索</el-button
          >
          <el-button type="danger" icon="refresh" plain @click="resetSearchForm"
            >重置</el-button
          >
        </el-form-item>
      </el-form>

      <el-row :gutter="10">
        <el-col :span="1.5" v-if="dataList.loginUser.roleId === 1">
          <el-button type="primary" icon="plus" plain @click="openAddDialog"
            >新增</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="edit" plain @click="openEditDialog"
            >修改</el-button
          >
        </el-col>
        <el-col
          :span="1.5"
          v-if="
            dataList.loginUser.roleId === 1 || dataList.loginUser.roleId === 3
          "
        >
          <el-button type="danger" icon="delete" plain @click="openRemoveDialog"
            >删除</el-button
          >
        </el-col>
      </el-row>

      <el-table
        border
        @selection-change="handleSelectionChange"
        v-adaptive
        :data="dataList.repairList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="120px"></el-table-column>
        <el-table-column
          label="维修内容"
          prop="content"
          width="250px"
        ></el-table-column>
        <el-table-column label="所属业主" width="200px">
          <template #default="scope">
            <div
              v-if="scope.row.userDTO"
              v-text="
                scope.row.userDTO?.username +
                '(' +
                scope.row.userDTO?.phone +
                ')'
              "
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column
          label="业主住址"
          prop="location"
          width="200px"
        ></el-table-column>
        <el-table-column
          label="创建时间"
          prop="createTime"
          width="200px"
        ></el-table-column>
        <el-table-column label="维修状态" width="100">
          <template #default="scope">
            <div v-if="scope.row.state === 1" style="color: orange">待受理</div>
            <div v-if="scope.row.state === 2" style="color: green">已受理</div>
          </template>
        </el-table-column>
        <el-table-column
          label="受理回复"
          prop="reply"
          width="250px"
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

    <Dialog ref="repairDialogRef" :title="title" @onConfirm="saveRepair">
      <template #content>
        <el-form :model="dataList.repairForm" label-width="120px">
          <el-form-item label="维修内容描述">
            <el-input
              v-model="dataList.repairForm.content"
              :autosize="{ minRows: 4 }"
              type="textarea"
              placeholder="请输入维修内容描述"
            />
          </el-form-item>
          <el-form-item label="维修状态" v-if="dataList.loginUser.roleId !== 1">
            <el-select
              v-model="dataList.repairForm.state"
              placeholder="请选择维修状态"
            >
              <el-option label="待受理" :value="1" />
              <el-option label="已受理" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="受理回复" v-if="dataList.loginUser.roleId !== 1">
            <el-input
              v-model="dataList.repairForm.reply"
              :autosize="{ minRows: 4 }"
              type="textarea"
              placeholder="请输入受理回复"
            />
          </el-form-item>
        </el-form>
      </template>
    </Dialog>
    <Dialog
      ref="confirmDialogRef"
      :width="width"
      :title="title"
      @onConfirm="onConfirmEvent"
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
const repairDialogRef = ref();
const confirmDialogRef = ref();
const title = ref("");
const confirmEvent = ref("");
const confirmDesc = ref("");
const width = ref("50%");

const paginationProps = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
});

// 确认框事件处理
const onConfirmEvent = () => {
  if (confirmEvent.value === "removeRepair") {
    removeRepair();
  }
};

// 获取当前登录用户
const getLoginUser = async () => {
  const response = await axiosPostRequest("/user/check_login", {
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    dataList.loginUser = response.data;
    getRepairList();
    getAllOwnerList();
  } else {
    ElMessage.error(response.msg);
    router.push("/login");
  }
};

// 获取所有业主信息
const getAllOwnerList = async () => {
  const response = await axiosPostRequest("/user/all", {
    roleId: 1,
    districtId:
      dataList.loginUser.roleId === 2 ? dataList.loginUser.districtId : "",
  });
  if (response.code === 0) {
    dataList.ownerList = response.data;
  }
};

// 重置搜索表单
const resetSearchForm = () => {
  searchFormRef.value.resetFields();
};

const dataList = reactive({
  searchParams: {
    content: "",
    userId: "",
  },
  loginUser: {
    id: "",
    roleId: "",
  },
  repairForm: {
    id: "",
    content: "",
    reply: "",
    userId: "",
    state: 1,
  },
  multipleSelection: [],
  ownerList: [],
});

// 打开添加维修的模态框
const openAddDialog = () => {
  title.value = "添加维修信息";
  dataList.repairForm = {
    id: "",
    content: "",
    userId: "",
    deadTime: "",
    deadPrice: 0.0,
    price: 0.0,
  };
  repairDialogRef.value.openDialog();
};

// 打开编辑维修的模态框
const openEditDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行编辑！");
    return;
  }
  console.log(dataList.multipleSelection[0].deadTime);
  if (
    dataList.multipleSelection[0].deadTime &&
    dataList.multipleSelection[0].deadTime !== ""
  ) {
    ElMessage.warning("设置逾期日期的维修不能编辑，请删除重新创建！");
    return;
  }
  title.value = "编辑维修信息";
  dataList.repairForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  repairDialogRef.value.openDialog();
};

// 打开删除维修的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.repairForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  confirmEvent.value = "removeRepair";
  confirmDialogRef.value.openDialog();
};

// 删除维修信息
const removeRepair = async () => {
  const response = await axiosPostRequest(
    "/repair/delete",
    dataList.repairForm
  );
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getRepairList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存维修信息
const saveRepair = async () => {
  const response = await axiosPostRequest("/repair/save", {
    ...dataList.repairForm,
    userId:
      dataList.loginUser.roleId === 1
        ? dataList.loginUser.id
        : dataList.repairForm.userId,
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    repairDialogRef.value.closeDialog();
    getRepairList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getRepairList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取维修信息
const getRepairList = async () => {
  const response = await axiosPostRequest("/repair/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      content: dataList.searchParams.content,
      userId: dataList.searchParams.userId,
      loginUserId: dataList.loginUser.id,
    },
  });
  if (response.code === 0) {
    dataList.repairList = response.data.list;
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
