<template>
  <div style="padding: 6px 6px 0 6px">
    <el-card class="card-list" shadow="never">
      <el-form
        ref="searchFormRef"
        :inline="true"
        :model="dataList.searchParams"
      >
        <el-form-item label="公告名称" prop="content">
          <el-input
            v-model="dataList.searchParams.content"
            placeholder="请输入公告名称"
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain @click="getAnnounceList"
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
        :data="dataList.announceList"
        empty-text="暂时没查到数据哟🌻"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="编号" prop="id" width="140px"></el-table-column>
        <el-table-column
          label="公告内容"
          prop="content"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="发布者" width="150px">
          <template #default="scope">
            <div
              v-if="scope.row.userDTO"
              v-text="scope.row.userDTO?.username"
            ></div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column label="是否置顶" width="100px">
          <template #default="scope">
            <div v-if="scope.row.top === 0">
              <el-tag type="info">否</el-tag>
            </div>
            <div v-else><el-tag type="danger">是</el-tag></div>
          </template>
        </el-table-column>
        <el-table-column
          label="发布时间"
          prop="createTime"
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

    <Dialog ref="announceDialogRef" :title="title" @onConfirm="saveAnnounce">
      <template #content>
        <el-form :model="dataList.announceForm" label-width="100px">
          <el-form-item label="公告内容">
            <el-input
              v-model="dataList.announceForm.content"
              :autosize="{ minRows: 4 }"
              type="textarea"
              placeholder="请输入公告内容"
            />
          </el-form-item>
          <el-form-item label="是否置顶">
            <el-select
              v-model="dataList.announceForm.top"
              placeholder="请选择是否置顶"
            >
              <el-option label="否" :value="0" />
              <el-option label="是" :value="1" />
            </el-select>
          </el-form-item>
        </el-form>
      </template>
    </Dialog>
    <Dialog
      ref="confirmDialogRef"
      :width="width"
      :title="title"
      @onConfirm="removeAnnounce"
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
const announceDialogRef = ref();
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
    getAnnounceList();
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
    content: "",
    top: 0,
  },
  loginUser: {
    id: "",
    roleId: "",
  },
  announceForm: {
    id: "",
    content: "",
  },
  multipleSelection: [],
  announceList: [],
});

// 打开添加公告的模态框
const openAddDialog = () => {
  title.value = "添加公告信息";
  dataList.announceForm = {
    id: "",
    name: "",
    unitName: "",
    districtId: "",
  };
  announceDialogRef.value.openDialog();
};

// 打开编辑公告的模态框
const openEditDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行编辑！");
    return;
  }
  if (
    dataList.loginUser.roleId === 2 &&
    dataList.multipleSelection[0].userId !== dataList.loginUser.id
  ) {
    ElMessage.warning("你只能修改自己的公告哦！");
    return;
  }
  title.value = "编辑公告信息";
  dataList.announceForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  announceDialogRef.value.openDialog();
};

// 打开删除公告的确认弹框
const openRemoveDialog = () => {
  if (dataList.multipleSelection.length !== 1) {
    ElMessage.warning("请选择一条数据进行删除！");
    return;
  }
  if (
    dataList.loginUser.roleId === 2 &&
    dataList.multipleSelection[0].userId !== dataList.loginUser.id
  ) {
    ElMessage.warning("你只能删除自己的公告哦！");
    return;
  }
  confirmDesc.value = "确定要删除此数据吗？";
  title.value = "确认提示";
  width.value = "30%";
  dataList.announceForm = JSON.parse(
    JSON.stringify(dataList.multipleSelection[0])
  );
  confirmDialogRef.value.openDialog();
};

// 删除公告信息
const removeAnnounce = async () => {
  const response = await axiosPostRequest(
    "/announce/delete",
    dataList.announceForm
  );
  if (response.code === 0) {
    ElMessage.success(response.msg);
    confirmDialogRef.value.closeDialog();
    getAnnounceList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 保存公告信息
const saveAnnounce = async () => {
  const response = await axiosPostRequest("/announce/save", {
    ...dataList.announceForm,
    userId: dataList.loginUser.id,
    top:
      dataList.announceForm.top === 1
        ? dataList.loginUser.roleId === 3
          ? 2
          : 1
        : dataList.announceForm.top,
  });
  if (response.code === 0) {
    ElMessage.success(response.msg);
    announceDialogRef.value.closeDialog();
    getAnnounceList();
  } else {
    ElMessage.error(response.msg);
  }
};

// 分页变化时候
const onPageChange = (current, pageSize) => {
  paginationProps.current = current;
  paginationProps.pageSize = pageSize;
  getAnnounceList();
};

// 记录表格选中行
const handleSelectionChange = (val) => {
  dataList.multipleSelection = val;
};

// 获取公告信息
const getAnnounceList = async () => {
  const response = await axiosPostRequest("/announce/list", {
    page: paginationProps.current,
    size: paginationProps.pageSize,
    param: {
      content: dataList.searchParams.content,
      userId: dataList.loginUser.id,
    },
  });
  if (response.code === 0) {
    dataList.announceList = response.data.list;
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
