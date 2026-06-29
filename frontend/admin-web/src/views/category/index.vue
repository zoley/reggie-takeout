<template>
  <div class="page-container">
    <div class="category-layout">
      <!-- 左侧：分类列表 + 操作按钮 -->
      <div class="left-panel">
        <div class="panel-header">
          <span>分类管理</span>
          <div>
            <el-button
              type="primary"
              size="small"
              @click="handleAddDishCategory"
              >新增菜品分类</el-button
            >
            <el-button
              type="warning"
              size="small"
              @click="handleAddSetmealCategory"
              >新增套餐分类</el-button
            >
          </div>
        </div>
        <el-table :data="tableData" border v-loading="loading">
          <el-table-column prop="name" label="分类名称" min-width="140" />
          <el-table-column prop="type" label="分类类型" width="110">
            <template #default="{ row }">
              <el-tag :type="row.type === 1 ? '' : 'warning'">
                {{ row.type === 1 ? "菜品分类" : "套餐分类" }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="操作时间" width="170" />
          <el-table-column prop="sort" label="排序" width="70" align="center" />
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="{ row }">
              <el-button
                type="primary"
                size="small"
                link
                @click="handleEdit(row)"
                >修改</el-button
              >
              <el-popconfirm
                title="确定删除该分类？"
                @confirm="handleDelete(row)"
              >
                <template #reference>
                  <el-button type="danger" size="small" link>删除</el-button>
                </template>
              </el-popconfirm>
              <el-icon class="sort-handle"><Rank /></el-icon>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 右侧：新建/编辑表单 -->
      <div class="right-panel">
        <div class="panel-header">
          {{ formData.id ? "修改分类" : "新建分类" }}
        </div>
        <el-form
          v-if="showForm"
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="80px"
          style="padding: 20px"
        >
          <el-form-item label="分类名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入分类名称" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model="formData.sort"
              :min="1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
            <el-button @click="showForm = false">取消</el-button>
          </el-form-item>
        </el-form>
        <div v-else class="empty-hint">
          点击左侧按钮新增或选择一条记录进行修改
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from "element-plus";
import { ref, reactive, onMounted } from "vue";
import {
  listCategoryByPage,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../api/category";

const loading = ref(false);
const tableData = ref([]);
const showForm = ref(false);
const formRef = ref();

const formData = reactive<Record<string, any>>({
  name: "",
  sort: 1,
  type: 1,
});

const formRules = {
  name: [{ required: true, message: "请输入分类名称", trigger: "blur" }],
};

/** 加载列表 */
function loadData() {
  loading.value = true;
  listCategoryByPage({ page: 1, pageSize: 100 })
    .then((res: any) => {
      if (res?.code === 200) {
        tableData.value = res.data?.records || [];
      }
    })
    .finally(() => (loading.value = false));
}

/** 新增菜品分类 */
function handleAddDishCategory() {
  Object.assign(formData, { id: undefined, name: "", sort: 1, type: 1 });
  showForm.value = true;
}

/** 新增套餐分类 */
function handleAddSetmealCategory() {
  Object.assign(formData, { id: undefined, name: "", sort: 1, type: 2 });
  showForm.value = true;
}

/** 编辑 */
function handleEdit(row: Record<string, any>) {
  Object.assign(formData, { ...row });
  showForm.value = true;
}

/** 提交 */
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
      showForm.value = false;
      loadData();
    }
  });
}

/** 删除 */
function handleDelete(row: Record<string, any>) {
  deleteCategory(row.id).then((res: any) => {
    if (res?.code === 200) {
      ElMessage.success("删除成功");
      loadData();
    }
  });
}

onMounted(() => loadData());
</script>

<style scoped>
.category-layout {
  display: flex;
  gap: 24px;
  height: calc(100vh - 140px);
}
.left-panel {
  flex: 2;
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.right-panel {
  flex: 1;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  max-width: 380px;
}
.panel-header {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.sort-handle {
  cursor: move;
  color: #999;
  vertical-align: middle;
  margin-left: 6px;
}
.empty-hint {
  padding: 40px 20px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style>
