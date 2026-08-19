<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <span class="filter-label">分类名称</span>
          <el-input
            v-model="searchForm.name"
            placeholder="请输入分类名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">分类类型</span>
          <el-select
            v-model="searchForm.type"
            placeholder="请选择分类类型"
            clearable
            style="width: 160px"
          >
            <el-option label="菜品分类" :value="1" />
            <el-option label="套餐分类" :value="2" />
          </el-select>
        </div>
        <div class="filter-actions">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </div>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-card">
      <div class="table-header">
        <span class="table-title">分类列表</span>
        <el-button type="primary" @click="handleAdd">
          <el-icon style="margin-right: 4px"><Plus /></el-icon>新增分类
        </el-button>
      </div>

      <el-table
        :data="tableData"
        stripe
        v-loading="loading"
        style="width: 100%"
        :header-cell-style="{
          background: '#fafafa',
          color: '#333',
          fontWeight: 600,
        }"
      >
        <el-table-column prop="name" label="分类名称" min-width="140" />
        <el-table-column
          prop="type"
          label="分类类型"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <el-tag
              :type="row.type === 1 ? 'primary' : 'warning'"
              effect="light"
              round
            >
              {{ row.type === 1 ? "菜品分类" : "套餐分类" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="100" align="center" />
        <el-table-column prop="updateTime" label="操作时间" min-width="170" />
        <el-table-column label="操作" width="120" fixed="right" align="left">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleEdit(row)"
              >编辑</el-button
            >
            <el-button
              type="danger"
              size="small"
              link
              @click="handleDelete(row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-bar">
        <el-pagination
          v-model:current-page="pager.current"
          v-model:page-size="pager.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.id ? '编辑分类' : '新增分类'"
      width="480px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类类型" prop="type">
          <el-select
            v-model="formData.type"
            placeholder="请选择分类类型"
            style="width: 100%"
          >
            <el-option label="菜品分类" :value="1" />
            <el-option label="套餐分类" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="formData.sort"
            :min="1"
            style="width: 100%"
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
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import { ref, reactive, onMounted } from "vue";
import {
  listCategoryByPage,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../api/category";

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();

const pager = reactive({ current: 1, pageSize: 10 });
const searchForm = reactive({ name: "", type: undefined });
const formData = reactive<Record<string, any>>({
  name: "",
  sort: 1,
  type: 1,
});

const formRules = {
  name: [{ required: true, message: "请输入分类名称", trigger: "blur" }],
  type: [{ required: true, message: "请选择分类类型", trigger: "change" }],
};

/** 搜索/加载列表 */
function handleSearch() {
  loading.value = true;
  listCategoryByPage({ ...pager, ...searchForm })
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
  searchForm.type = undefined;
  pager.current = 1;
  handleSearch();
}

/** 新增 */
function handleAdd() {
  Object.keys(formData).forEach((key) => {
    delete formData[key];
  });
  Object.assign(formData, { name: "", sort: 1, type: 1 });
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

  const api = formData.id ? updateCategory : createCategory;
  const msg = formData.id ? "修改成功" : "新增成功";
  api(formData).then((res: any) => {
    if (res?.code === 200) {
      ElMessage.success(msg);
      dialogVisible.value = false;
      handleSearch();
    }
  });
}

/** 删除 */
function handleDelete(row: Record<string, any>) {
  ElMessageBox.confirm(`确定要删除分类「${row.name}」吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteCategory(row.id).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success("删除成功");
          handleSearch();
        }
      });
    })
    .catch(() => {});
}

onMounted(() => handleSearch());
</script>

<style scoped>
.filter-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px 24px 12px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.filter-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
}

.filter-actions {
  display: flex;
  gap: 8px;
  margin-left: auto;
}

.table-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.table-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.pagination-bar {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
