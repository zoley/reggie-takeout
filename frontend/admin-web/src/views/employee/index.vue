<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-toolbar">
      <div class="toolbar-item">
        <label>姓名：</label>
        <el-input
          v-model="searchForm.name"
          placeholder="请输入姓名"
          clearable
          style="width: 200px"
        />
      </div>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">+ 新增员工</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="name" label="姓名" min-width="120" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="idNumber" label="身份证号" width="180" />
      <el-table-column prop="sex" label="性别" width="80">
        <template #default="{ row }">
          {{ row.sex === "1" ? "男" : row.sex === "0" ? "女" : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="账号状态" width="100">
        <template #default="{ row }">
          <el-switch
            v-model="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="(val) => handleStatusChange(row, val)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="最后操作时间" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" link @click="handleEdit(row)"
            >编辑</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-bar">
      <el-pagination
        v-model:current-page="pager.page"
        v-model:page-size="pager.pageSize"
        :total="total"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.id ? '编辑员工' : '新增员工'"
      width="520px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            :disabled="!!formData.id"
          />
        </el-form-item>
        <el-form-item v-if="!formData.id" label="密码" prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="formData.phone"
            placeholder="请输入手机号"
            maxlength="11"
          />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="formData.sex">
            <el-radio value="1">男</el-radio>
            <el-radio value="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="身份证号" prop="idNumber">
          <el-input
            v-model="formData.idNumber"
            placeholder="请输入身份证号"
            maxlength="18"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from "element-plus";
import { ref, reactive, onMounted } from "vue";
import {
  listEmployeeByPage,
  createEmployee,
  updateEmployee,
  updateEmployeeStatus,
} from "../../api/employee";

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();

const pager = reactive({ page: 1, pageSize: 10 });
const searchForm = reactive({ name: "" });
const formData = reactive<Record<string, any>>({});

const formRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: "手机号格式不正确", trigger: "blur" },
  ],
  idNumber: [
    {
      pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
      message: "身份证号格式不正确",
      trigger: "blur",
    },
  ],
};

/** 搜索 */
function handleSearch() {
  loading.value = true;
  listEmployeeByPage({ ...pager, ...searchForm })
    .then((res: any) => {
      if (res?.code === 200) {
        tableData.value = res.data?.records || [];
        total.value = res.data?.total || 0;
      }
    })
    .finally(() => (loading.value = false));
}

/** 重置 */
function resetSearch() {
  searchForm.name = "";
  pager.page = 1;
  handleSearch();
}

/** 新增 */
function handleAdd() {
  Object.assign(formData, {
    username: "",
    password: "",
    name: "",
    phone: "",
    sex: "1",
    idNumber: "",
  });
  dialogVisible.value = true;
}

/** 编辑 */
function handleEdit(row: Record<string, any>) {
  Object.assign(formData, { ...row });
  dialogVisible.value = true;
}

/** 提交表单 */
async function handleSubmit() {
  try {
    await formRef.value.validate();
  } catch {
    return;
  }

  const api = formData.id ? updateEmployee : createEmployee;
  const msg = formData.id ? "修改成功" : "新增成功";
  api(formData).then((res: any) => {
    if (res?.code === 200) {
      ElMessage.success(msg);
      dialogVisible.value = false;
      handleSearch();
    }
  });
}

/** 切换状态 */
function handleStatusChange(row: Record<string, any>, val: number) {
  updateEmployeeStatus(row.id, val)
    .then((res: any) => {
      if (res?.code === 200) {
        ElMessage.success(val === 1 ? "已启用" : "已禁用");
      } else {
        // 回滚状态
        row.status = val === 1 ? 0 : 1;
      }
    })
    .catch(() => {
      row.status = val === 1 ? 0 : 1;
    });
}

onMounted(() => handleSearch());
</script>

<style scoped>
.search-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.action-bar {
  margin-bottom: 16px;
}
.pagination-bar {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
