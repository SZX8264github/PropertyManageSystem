<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        ref="searchFormRef"
        :inline="true"
        :model="dataList.searchParams"
      >
        <el-form-item label="缴费内容" prop="content">
          <el-input
            v-model="dataList.searchParams.content"
            placeholder="请输入缴费内容"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="所属业主"
          prop="userId"
          v-if="
            dataList.loginUser.roleId === 3 || dataList.loginUser.roleId === 2
          "
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
          <el-button type="primary" icon="search" plain @click="getFeeList"
            >搜索</el-button
          >
          <el-button type="danger" icon="refresh" plain @click="resetSearchForm"
            >重置</el-button
          >
        </el-form-item>
      </el-form>

      <el-row :gutter="10">
        <el-col
          :span="1.5"
          v-if="
            dataList.loginUser.roleId === 2 || dataList.loginUser.roleId === 3
          "
        >
          <el-button type="primary" icon="plus" plain @click="openAddDialog"
            >新增</el-button
          >
        </el-col>
        <el-col
          :span="1.5"
          v-if="
            dataList.loginUser.roleId === 2 || dataList.loginUser.roleId === 3
          "
        >
          <el-button type="success" icon="edit" plain @click="openEditDialog"
            >修改</el-button
          >
        </el-col>
        <el-col
          :span="1.5"
          v-if="
            dataList.loginUser.roleId === 2 || dataList.loginUser.roleId === 3
          "
        >
          <el-button type="danger" icon="delete" plain @click="openRemoveDialog"
            >删除</el-button
          >
        </el-col>
        <el-col :span="1.5" v-if="dataList.loginUser.roleId === 1">
          <el-button type="primary" icon="Money" plain @click="openPayFeeDialog"
            >支付</el-button
          >
        </el-col>
      </el-row>

      <el-table
        border
        @selection-change="handleSelectionChange"
        v-adaptive
        :data="dataList.feeList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="120px"></el-table-column>
        <el-table-column
          label="缴费内容"
          prop="content"
          width="200px"
        ></el-table-column>
        <el-table-column label="所属小区" width="150px">
          <template #default="scope">
            <div
              v-if="scope.row.districtDTO"
              v-text="scope.row.districtDTO?.name"
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
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
        <el-table-column label="缴费金额" width="120">
          <template #default="scope">
            <div v-text="scope.row.price + '元'"></div>
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          prop="createTime"
          width="200px"
        ></el-table-column>
        <el-table-column label="缴费状态" width="100">
          <template #default="scope">
            <div v-if="scope.row.state === 1" style="color: orange">待支付</div>
            <div v-if="scope.row.state === 2" style="color: green">已支付</div>
            <div v-if="scope.row.state === 3" style="color: red">已逾期</div>
          </template>
        </el-table-column>
        <el-table-column
          label="逾期日期"
          prop="deadTime"
          width="150px"
        ></el-table-column>
        <el-table-column label="逾期罚金(元/天)" width="150">
          <template #default="scope">
            <div v-text="scope.row.deadPrice + '元'"></div>
          </template>
        </el-table-column>
        <el-table-column label="附加金额" width="150">
          <template #default="scope">
            <div v-text="scope.row.addPrice + '元'"></div>
          </template>
        </el-table-column>
        <el-table-column label="总费用" width="120">
          <template #default="scope">
            <div v-text="scope.row.addPrice + scope.row.price + '元'"></div>
          </template>
        </el-table-column>
        <el-table-column
          label="缴费时间"
          prop="payTime"
          width="200px"
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

    <Dialog ref="feeDialogRef" :title="title" @onConfirm="saveFee">
      <template #content>
        <el-form :model="dataList.feeForm" label-width="120px">
          <el-form-item label="缴费内容">
            <el-input
              v-model="dataList.feeForm.content"
              :autosize="{ minRows: 4 }"
              type="textarea"
              placeholder="请输入缴费内容"
            />
          </el-form-item>
          <el-form-item label="缴费金额">
            <el-input-number
              :min="0"
              :max="99999999.99"
              :precision="2"
              v-model="dataList.feeForm.price"
              placeholder="请输入缴费金额"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="所属业主">
            <el-select
              v-model="dataList.feeForm.userId"
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
            <template #label>
              <div style="display: flex; align-items: center">
                <div style="margin-right: 5px">逾期日期</div>
                <el-tooltip content="若逾期日期不为空，逾期将增收罚金！">
                  <el-icon><WarningFilled /></el-icon>
                </el-tooltip>
              </div>
            </template>
            <el-date-picker
              :editable="false"
              value-format="YYYY-MM-DD"
              :disabled-date="disabledDate"
              v-model="dataList.feeForm.deadTime"
              type="date"
              placeholder="请选择逾期日期"
            />
          </el-form-item>

          <el-form-item label="逾期罚金(元/天)">
            <el-input-number
              :min="0"
              :max="99999999.99"
              :precision="2"
              v-model="dataList.feeForm.deadPrice"
              placeholder="请输入逾期罚金(元/天)"
            ></el-input-number>
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
const feeDialogRef = ref();
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
  if (confirmEvent.value === "removeFee") {
    removeFee();
  }
  if (confirmEvent.value === "payFee") {
    payFee();
  }
};

// 禁用日期
const disabledDate = (date) => {
  if (date.getTime() <= Date.now()) {
    return true;
  }
  return false;
};

// 获取当前登录用户
const getLoginUser = async () => {
  const response = await axiosPostRequest("/user/check_login", {
    token: getSessionStorage(),
  });
  if (response.code === 0) {
    dataList.loginUser = response.data;
    getFeeList();
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
  feeForm: {
    id: "",
    content: "",
    userId: "",
    deadTime: "",
    deadPrice: 0.0,
    price: 0.0,
  },
  multipleSelection: [],
  ownerList: [],
});

// 打开添加缴费的模态框
const openAddDialog = () => {
  title.value = "添加缴费信息";
  dataList.feeForm = {
    id: "",
    content: "",
    userId: "",
    deadTime: "",
    deadPrice: 0.0,
    price: 0.0,
  };
  feeDialogRef.value.openDialog();
};

// 打开支付缴费的模态框
const openPayFeeDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行支付！");
    return;
  }
  let fee = dataList.multipleSelection[0];
  if (fee.state === 2) {
    ElMessage.warning("请选择一条未支付的数据进行支付！");
    return;
  }
  confirmDesc.value = "确定要支付" + (fee.price + fee.addPrice) + "元吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.feeForm = JSON.parse(JSON.stringify(fee));
  confirmEvent.value = "payFee";
  confirmDialogRef.value.openDialog();
};

// 支付缴费
const payFee = async () => {
  const response = await axiosPostRequest("/fee/save", {
    ...dataList.feeForm,
    state: 2,
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getFeeList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 打开编辑缴费的模态框
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
    ElMessage.warning("设置逾期日期的缴费不能编辑，请删除重新创建！");
    return;
  }
  title.value = "编辑缴费信息";
  dataList.feeForm = JSON.parse(JSON.stringify(dataList.multipleSelection[0]));
  feeDialogRef.value.openDialog();
};

// 打开删除缴费的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.feeForm = JSON.parse(JSON.stringify(dataList.multipleSelection[0]));
  confirmEvent.value = "removeFee";
  confirmDialogRef.value.openDialog();
};

// 删除缴费信息
const removeFee = async () => {
  const response = await axiosPostRequest("/fee/delete", dataList.feeForm);
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getFeeList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存缴费信息
const saveFee = async () => {
  const response = await axiosPostRequest("/fee/save", {
    ...dataList.feeForm,
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    feeDialogRef.value.closeDialog();
    getFeeList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getFeeList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取缴费信息
const getFeeList = async () => {
  const response = await axiosPostRequest("/fee/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      content: dataList.searchParams.content,
      districtId:
        dataList.loginUser.roleId === 2 ? dataList.loginUser.districtId : "",
      userId:
        dataList.loginUser.roleId === 1
          ? dataList.loginUser.id
          : dataList.searchParams.userId,
    },
  });
  if (response.code === 0) {
    dataList.feeList = response.data.list;
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
