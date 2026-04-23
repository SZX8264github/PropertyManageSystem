<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        ref="searchFormRef"
        :inline="true"
        v-if="
          dataList.loginUser.roleId === 3 || dataList.loginUser.roleId === 2
        "
        :model="dataList.searchParams"
      >
        <el-form-item label="门牌号" prop="card">
          <el-input
            v-model="dataList.searchParams.card"
            placeholder="请输入门牌号"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="所属楼栋" prop="buildingId">
          <el-select
            v-model="dataList.searchParams.buildingId"
            placeholder="请选择所属楼栋"
          >
            <el-option
              v-for="(item, index) in dataList.buildingList"
              :key="index"
              :label="item.name + '(' + item.unitName + ')'"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain @click="getHouseList"
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
        :data="dataList.houseList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="140px"></el-table-column>
        <el-table-column
          label="门牌号"
          prop="card"
          width="150px"
        ></el-table-column>
        <el-table-column label="房屋面积" width="150px">
          <template #default="scope">
            <div v-text="scope.row.area + '㎡'"></div>
          </template>
        </el-table-column>
        <el-table-column label="所属楼栋" width="200px">
          <template #default="scope">
            <div
              v-if="scope.row.buildingDTO"
              v-text="
                scope.row.buildingDTO?.name +
                '(' +
                scope.row.buildingDTO?.unitName +
                ')'
              "
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column
          label="楼层"
          prop="floor"
          width="100px"
        ></el-table-column>
        <el-table-column label="业主姓名" width="100px">
          <template #default="scope">
            <div
              v-if="scope.row.userDTO"
              v-text="scope.row.userDTO?.username"
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column label="业主联系方式" width="200px">
          <template #default="scope">
            <div
              v-if="scope.row.userDTO"
              v-text="scope.row.userDTO?.phone"
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column
          width="250px"
          label="房屋详细描述"
          prop="content"
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

    <Dialog ref="houseDialogRef" :title="title" @onConfirm="saveHouse">
      <template #content>
        <el-form :model="dataList.houseForm" label-width="100px">
          <el-form-item label="门牌号">
            <el-input
              v-model="dataList.houseForm.card"
              placeholder="请输入门牌号"
            ></el-input>
          </el-form-item>
          <el-form-item label="楼层">
            <el-input-number
              :min="1"
              :max="99999999"
              :precision="0"
              v-model="dataList.houseForm.floor"
              placeholder="请输入楼层"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="房屋面积(㎡)">
            <el-input-number
              :min="0.0"
              :max="99999999.99"
              :precision="2"
              v-model="dataList.houseForm.area"
              placeholder="请输入房屋面积(㎡)"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="所属楼栋">
            <el-select
              v-model="dataList.houseForm.buildingId"
              placeholder="请选择所属楼栋"
            >
              <el-option
                v-for="(item, index) in dataList.buildingList"
                :key="index"
                :label="item.name + '(' + item.unitName + ')'"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="所属业主">
            <el-select
              v-model="dataList.houseForm.userId"
              placeholder="请选择所属楼栋"
            >
              <el-option
                v-for="(item, index) in dataList.ownerList"
                :key="index"
                :label="item.username + '(' + item.phone + ')'"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="房屋详细信息">
            <el-input
              v-model="dataList.houseForm.content"
              :autosize="{ minRows: 4 }"
              type="textarea"
              placeholder="请输入房屋详细信息"
            />
          </el-form-item>
        </el-form>
      </template>
    </Dialog>
    <Dialog
      ref="confirmDialogRef"
      :width="width"
      :title="title"
      @onConfirm="removeHouse"
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
const houseDialogRef = ref();
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
    getHouseList();
    getAllBuilding();
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
      dataList.loginUser.roleId === 3 ? "" : dataList.loginUser.districtId,
  });
  if (response.code === 0) {
    dataList.ownerList = response.data;
  }
};

// 获取所有楼栋信息
const getAllBuilding = async () => {
  const response = await axiosPostRequest("/building/all", {
    districtId:
      dataList.loginUser.roleId === 3 ? "" : dataList.loginUser.districtId,
  });
  if (response.code === 0) {
    dataList.buildingList = response.data;
  }
};

// 重置搜索表单
const resetSearchForm = () => {
  searchFormRef.value.resetFields();
};

const dataList = reactive({
  searchParams: {
    card: "",
    buildingId: "",
  },
  loginUser: {
    id: "",
    roleId: "",
  },
  houseForm: {
    id: "",
    area: 0.0,
    floor: 1,
    card: "",
    content: "",
    userId: "",
    buildingId: "",
  },
  multipleSelection: [],
  buildingList: [],
  ownerList: [],
});

// 打开添加房屋的模态框
const openAddDialog = () => {
  title.value = "添加房屋信息";
  dataList.houseForm = {
    id: "",
    area: 0.0,
    floor: 1,
    card: "",
    content: "",
    userId: "",
    buildingId: "",
  };
  houseDialogRef.value.openDialog();
};

// 打开编辑房屋的模态框
const openEditDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行编辑！");
    return;
  }
  title.value = "编辑房屋信息";
  dataList.houseForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  houseDialogRef.value.openDialog();
};

// 打开删除房屋的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.houseForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  confirmDialogRef.value.openDialog();
};

// 删除房屋信息
const removeHouse = async () => {
  const response = await axiosPostRequest("/house/delete", dataList.houseForm);
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getHouseList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存房屋信息
const saveHouse = async () => {
  const response = await axiosPostRequest("/house/save", {
    ...dataList.houseForm,
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    houseDialogRef.value.closeDialog();
    getHouseList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getHouseList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取房屋信息
const getHouseList = async () => {
  const response = await axiosPostRequest("/house/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      districtId:
        dataList.loginUser.roleId === 2 ? dataList.loginUser.districtId : "",
      userId: dataList.loginUser.roleId === 1 ? dataList.loginUser.id : "",
      card: dataList.searchParams.card,
      buildingId: dataList.searchParams.buildingId,
    },
  });
  if (response.code === 0) {
    dataList.houseList = response.data.list;
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
