<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <span class="filter-label">菜品名称</span>
          <el-input
            v-model="searchForm.name"
            placeholder="请输入菜品名称"
            clearable
            style="width: 200px"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">菜品分类</span>
          <el-select
            v-model="searchForm.categoryId"
            placeholder="请选择分类"
            clearable
            style="width: 160px"
          >
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </div>
        <div class="filter-item">
          <span class="filter-label">售卖状态</span>
          <el-select
            v-model="searchForm.status"
            placeholder="请选择"
            clearable
            style="width: 120px"
          >
            <el-option label="启售" :value="1" />
            <el-option label="停售" :value="0" />
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
        <span class="table-title">菜品列表</span>
        <div class="table-header-actions">
          <el-button
            type="danger"
            plain
            :disabled="!selectedIds.length"
            @click="handleBatchDelete"
            >批量删除</el-button
          >
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>新增菜品
          </el-button>
        </div>
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
        @selection-change="(rows) => (selectedIds = rows.map((r: any) => r.id))"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="菜品名称" min-width="140" />
        <el-table-column label="图片" width="90" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.image"
              :src="row.image"
              style="width: 50px; height: 50px; border-radius: 6px"
              fit="cover"
              :preview-src-list="[row.image]"
            />
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="categoryName"
          label="菜品分类"
          width="120"
          align="center"
        />
        <el-table-column prop="price" label="售价" width="100" align="center">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="售卖状态"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-tag
              :type="row.status === 1 ? 'success' : 'danger'"
              effect="light"
              round
            >
              {{ row.status === 1 ? "启售" : "停售" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="updateTime"
          label="最后操作时间"
          min-width="170"
        />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleEdit(row)"
              >修改</el-button
            >
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              size="small"
              link
              @click="handleStatusChange(row)"
            >
              {{ row.status === 1 ? "停售" : "起售" }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-bar">
        <el-pagination
          v-model:current-page="pager.page"
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
      :title="formData.id ? '编辑菜品' : '新增菜品'"
      width="650px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="90px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜品名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入菜品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜品分类" prop="categoryId">
              <el-select
                v-model="formData.categoryId"
                placeholder="请选择分类"
                style="width: 100%"
              >
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="售价" prop="price">
              <el-input-number
                v-model="formData.price"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜品图片">
              <el-upload
                action="/api/common/upload"
                :headers="{
                  Authorization: `Bearer ${localStorage.getItem('token')}`,
                }"
                :show-file-list="false"
                :on-success="(res: any) => (formData.image = res.data)"
                accept=".jpg,.jpeg,.png"
              >
                <el-image
                  v-if="formData.image"
                  :src="formData.image"
                  style="width: 60px; height: 60px"
                  fit="cover"
                />
                <el-button v-else size="small" type="primary"
                  >上传图片</el-button
                >
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="口味配置">
          <div class="flavor-section">
            <div
              v-for="(flavor, index) in flavorList"
              :key="index"
              class="flavor-item"
            >
              <el-input
                v-model="avor.name"
                placeholder="口味名（如：辣度）"
                style="width: 120px"
              />
              <el-select
                v-model="flavor.value"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="选项"
                style="flex: 1"
              >
                <!-- 动态添加选项 -->
              </el-select>
              <el-button
                type="danger"
                size="small"
                circle
                @click="flavorList.splice(index, 1)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
            <el-button
              size="small"
              type="primary"
              plain
              @click="flavorList.push({ name: '', value: [] })"
            >
              + 添加口味
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="菜品描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入菜品描述"
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
import { Plus, Delete } from "@element-plus/icons-vue";
import { ref, reactive, onMounted } from "vue";
import {
  listDishByPage,
  createDish,
  updateDish,
  updateDishStatus,
  deleteDish,
} from "../../api/dish";
import { listCategory } from "../../api/category";

const loading = ref(false);
const tableData = ref([]);
const categoryList = ref<any[]>([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();
const selectedIds = ref<number[]>([]);

const pager = reactive({ page: 1, pageSize: 10 });
const searchForm = reactive({
  name: "",
  categoryId: "",
  status: undefined as number | undefined,
});
const formData = reactive<Record<string, any>>({});
const flavorList = ref<{ name: string; value: string[] }[]>([]);

const formRules = {
  name: [{ required: true, message: "请输入菜品名称", trigger: "blur" }],
  categoryId: [
    { required: true, message: "请选择菜品分类", trigger: "change" },
  ],
  price: [{ required: true, message: "请输入售价", trigger: "blur" }],
};

/** 加载分类列表 */
function loadCategories() {
  listCategory(1).then((res: any) => {
    if (res?.code === 200) {
      categoryList.value = res.data || [];
    }
  });
}

/** 搜索 */
function handleSearch() {
  loading.value = true;
  listDishByPage({ ...pager, ...searchForm })
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
  Object.assign(searchForm, { name: "", categoryId: "", status: undefined });
  pager.page = 1;
  handleSearch();
}

/** 新增 */
function handleAdd() {
  Object.assign(formData, {
    name: "",
    categoryId: "",
    price: 0,
    image: "",
    description: "",
    status: 1,
  });
  flavorList.value = [];
  dialogVisible.value = true;
}

/** 编辑 */
function handleEdit(row: Record<string, any>) {
  Object.assign(formData, { ...row });
  flavorList.value = row.flavors || [];
  dialogVisible.value = true;
}

/** 提交 */
async function handleSubmit() {
  try {
    await formRef.value.validate();
  } catch {
    return;
  }

  const data = { ...formData, flavors: flavorList.value };
  const api = formData.id ? updateDish : createDish;
  const msg = formData.id ? "修改成功" : "新增成功";
  api(data).then((res: any) => {
    if (res?.code === 200) {
      ElMessage.success(msg);
      dialogVisible.value = false;
      handleSearch();
    }
  });
}

/** 状态切换 */
function handleStatusChange(row: Record<string, any>) {
  const newStatus = row.status === 1 ? 0 : 1;
  const action = newStatus === 1 ? "起售" : "停售";
  ElMessageBox.confirm(`确定要${action}该菜品吗？`, "提示")
    .then(() => {
      updateDishStatus(newStatus, row.id)
        .then((res: any) => {
          if (res?.code === 200) {
            ElMessage.success(`${action}成功`);
            handleSearch();
          }
        })
        .catch(() => {});
    })
    .catch(() => {});
}

/** 批量删除 */
function handleBatchDelete() {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`)
    .then(() => {
      deleteDish(selectedIds.value.join(",")).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success("删除成功");
          selectedIds.value = [];
          handleSearch();
        }
      });
    })
    .catch(() => {});
}

onMounted(() => {
  loadCategories();
  handleSearch();
});
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

.table-header-actions {
  display: flex;
  gap: 8px;
}

.pagination-bar {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.price-text {
  color: #e6a23c;
  font-weight: 600;
}

.text-muted {
  color: #c0c4cc;
}

.flavor-section {
  width: 100%;
}

.flavor-item {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
  align-items: center;
}
</style>
