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
            @keyup.enter="handleSearch"
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
            >批量删除
          </el-button>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px">
              <Plus />
            </el-icon>
            新增菜品
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
        <el-table-column prop="code" label="编码" width="110" align="center" />
        <el-table-column label="图片" width="90" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.image"
              :src="getFileUrl(row.image)"
              style="width: 50px; height: 50px; border-radius: 6px"
              fit="cover"
              :preview-src-list="[getFileUrl(row.image)]"
            >
              <template #error>
                <div class="image-viewer-slot image-slot">
                  <el-icon style="font-size: 24px; color: #999"
                    ><Picture
                  /></el-icon>
                </div>
              </template>
            </el-image>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="菜品分类" width="120" align="center">
          <template #default="{ row }">
            {{ getCategoryName(row.categoryId) }}
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleEdit(row)"
              >修改
            </el-button>
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              size="small"
              link
              @click="handleStatusChange(row)"
            >
              {{ row.status === 1 ? "停售" : "起售" }}
            </el-button>
            <el-button
              type="danger"
              size="small"
              link
              @click="handleDelete(row)"
              >删除
            </el-button>
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
      :title="formData.id ? '编辑菜品' : '新增菜品'"
      width="720px"
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
            <el-form-item label="菜品编码" prop="code">
              <el-input v-model="formData.code" placeholder="请输入菜品编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number
                v-model="formData.sort"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="售价" prop="price">
              <div class="price-input">
                <el-input-number
                  v-model="formData.price"
                  :min="0"
                  :precision="2"
                  :step="0.1"
                  controls-position="right"
                  class="price-input-number"
                />
                <span class="price-suffix">元</span>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="售卖状态" prop="status">
              <el-select v-model="formData.status" style="width: 100%">
                <el-option label="启售" :value="1" />
                <el-option label="停售" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="菜品图片">
          <div class="image-upload-wrapper">
            <!-- 无图：显示上传触发器 -->
            <el-upload
              v-if="!formData.imageUrl"
              :before-upload="beforeUpload"
              :show-file-list="false"
              accept=".jpg,.jpeg,.png"
            >
              <el-icon class="upload-icon"><Plus /></el-icon>
            </el-upload>
            <!-- 有图：显示预览 + 右上角删除按钮，点击图片可放大查看 -->
            <div v-else class="image-preview">
              <div class="preview-img-wrapper">
                <el-image
                  :src="formData.imageUrl"
                  class="preview-img"
                  fit="cover"
                  :preview-src-list="[formData.imageUrl]"
                  :preview-teleported="true"
                  :z-index="3000"
                  hide-on-click-modal
                />
              </div>
              <span
                class="delete-btn"
                title="删除图片"
                @click.stop="handleRemoveImage"
              >
                <el-icon><Close /></el-icon>
              </span>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="口味配置">
          <div class="flavor-section">
            <div
              v-for="(flavor, index) in flavorList"
              :key="index"
              class="flavor-item"
            >
              <el-select
                v-model="flavor.name"
                placeholder="请选择口味"
                style="width: 160px"
              >
                <el-option
                  v-for="item in FLAVOR_TYPE_OPTIONS"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-input-tag
                v-model="flavor.value"
                placeholder="输入标签后回车"
                :max="10"
                style="flex: 1"
              />
              <el-button
                type="danger"
                size="small"
                circle
                @click="flavorList.splice(index, 1)"
              >
                <el-icon>
                  <Delete />
                </el-icon>
              </el-button>
            </div>
            <el-button
              size="small"
              type="primary"
              plain
              @click="handleAddFlavor"
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
import { Plus, Delete, Close, Picture } from "@element-plus/icons-vue";
import { ref, reactive, onMounted } from "vue";
import {
  listDishByPage,
  getDishById,
  createDish,
  updateDish,
  enabledDishStatus,
  disabledDishStatus,
  deleteDishById,
  deleteBatchDish,
} from "@/api/dish";
import { listCategoryByPage } from "@/api/category";
import { uploadFile } from "@/api/auth";
import { getFileUrl } from "@/utils";
import { FLAVOR_TYPE_OPTIONS } from "@/constants/dish";

/** 口味表单项：name 对应 DishFlavor.name；value 为标签数组，提交时序列化为 JSON 字符串存入 DishFlavor.value */
interface FlavorItem {
  name: string;
  value: string[];
}

const loading = ref(false);
const tableData = ref<any[]>([]);
const categoryList = ref<any[]>([]);
const total = ref(0);
const dialogVisible = ref(false);
const formRef = ref();
const selectedIds = ref<number[]>([]);

const pager = reactive({ current: 1, pageSize: 10 });
const searchForm = reactive({
  name: undefined,
  categoryId: undefined,
  status: undefined as number | undefined,
});
const formData = reactive<Record<string, any>>({});
const flavorList = ref<FlavorItem[]>([]);

const formRules = {
  name: [{ required: true, message: "请输入菜品名称", trigger: "blur" }],
  categoryId: [
    { required: true, message: "请选择菜品分类", trigger: "change" },
  ],
  price: [{ required: true, message: "请输入售价", trigger: "blur" }],
};

/** 根据 categoryId 解析分类名称（Dish 实体无 categoryName 字段，前端回显） */
function getCategoryName(categoryId: any): string {
  if (!categoryId) return "-";
  const target = categoryList.value.find((c) => c.id === categoryId);
  return target?.name || "-";
}

/** 上传前校验并上传 */
function beforeUpload(file: File) {
  const isImage = ["image/jpeg", "image/png"].includes(file.type);
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isImage) {
    ElMessage.error("只能上传 JPG/PNG 格式的图片");
    return false;
  }
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过 2MB");
    return false;
  }
  const postData = new FormData();
  postData.append("file", file);
  uploadFile(postData).then((res: any) => {
    if (res?.code === 200) {
      formData.imageUrl = getFileUrl(res.data.id);
      formData.image = res.data.id;
    }
  });
  return false;
}

/** 删除已上传图片 */
function handleRemoveImage() {
  formData.image = undefined;
  formData.imageUrl = undefined;
}

/** 加载分类列表（type=1 菜品分类）。后端 CategoryController 仅提供 /listByPage 接口，无 /list */
function loadCategories() {
  listCategoryByPage({ current: 1, pageSize: 1000, type: 1 }).then(
    (res: any) => {
      if (res?.code === 200) {
        categoryList.value = res.data?.records || [];
      }
    },
  );
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
  Object.assign(searchForm, {
    name: undefined,
    categoryId: undefined,
    status: undefined,
  });
  pager.current = 1;
  handleSearch();
}

/** 新增 */
function handleAdd() {
  Object.assign(formData, {
    name: undefined,
    categoryId: undefined,
    code: undefined,
    sort: 0,
    price: 0,
    image: undefined,
    imageUrl: undefined,
    description: undefined,
    status: 1,
  });
  flavorList.value = [];
  dialogVisible.value = true;
}

/** 添加口味行 */
function handleAddFlavor() {
  flavorList.value.push({ name: "", value: [] });
}

/** 编辑：调用 getById 获取含口味与分类名的完整 DishDTO（listByPage 不返回 flavors） */
function handleEdit(row: Record<string, any>) {
  getDishById({ id: row.id }).then((res: any) => {
    if (res?.code !== 200) return;
    const detail = res.data || {};
    Object.assign(formData, { ...detail });
    // DishFlavor.value 后端存储为 JSON 字符串，编辑时解析为数组以供 el-input-tag 使用
    flavorList.value = (detail.flavors || []).map((f: any) => ({
      name: f.name || "",
      value: parseFlavorValue(f.value),
    }));
    dialogVisible.value = true;
  });
}

/** 将 DishFlavor.value（JSON 字符串）解析为标签数组 */
function parseFlavorValue(value: any): string[] {
  if (!value) return [];
  if (Array.isArray(value)) return value;
  try {
    const parsed = JSON.parse(value);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

/** 提交 */
async function handleSubmit() {
  try {
    await formRef.value.validate();
  } catch {
    return;
  }

  // 口味字段对齐 DishFlavor 实体：name、value（标签数组序列化为 JSON 字符串）；dishId 由后端设置
  const flavors = flavorList.value
    .filter((f) => f.name.trim() && f.value.length)
    .map((f) => ({
      name: f.name.trim(),
      value: JSON.stringify(f.value),
    }));

  const data = { ...formData, flavors };
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
      (newStatus === 1
        ? enabledDishStatus({ id: row.id })
        : disabledDishStatus({ id: row.id })
      )
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

/** 删除单条 */
function handleDelete(row: Record<string, any>) {
  ElMessageBox.confirm(`确定要删除菜品「${row.name}」吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteDishById(row.id).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success("删除成功");
          handleSearch();
        }
      });
    })
    .catch(() => {});
}

/** 批量删除 */
function handleBatchDelete() {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`)
    .then(() => {
      deleteBatchDish(selectedIds.value).then((res: any) => {
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

.upload-icon {
  width: 100px;
  height: 100px;
  font-size: 28px;
  color: #8c939d;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-icon:hover {
  border-color: #409eff;
  color: #409eff;
}

/* 售价输入框（带「元」后缀） */
.price-input {
  display: flex;
  align-items: stretch;
  width: 100%;
}

.price-input-number {
  flex: 1;
}

.price-input-number :deep(.el-input__wrapper),
.price-input-number :deep(.el-input-number) {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}
.el-image {
  display: block;
}
.price-suffix {
  display: inline-flex;
  align-items: center;
  padding: 0 14px;
  background: #fafafa;
  border: 1px solid #dcdfe6;
  border-left: none;
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
  color: #606266;
  font-size: 14px;
}
.image-viewer-slot {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-upload-wrapper {
  width: 100px;
  height: 100px;
  padding: 8px;
  margin: -8px;
  box-sizing: content-box;
}

.image-preview {
  position: relative;
  width: 100px;
  height: 100px;
  cursor: pointer;
}

.image-preview .preview-img-wrapper {
  width: 100%;
  height: 100%;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  overflow: hidden;
}

.image-preview :deep(.preview-img) {
  width: 100%;
  height: 100%;
  display: block;
}

.image-preview :deep(.preview-img .el-image__inner) {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.delete-btn {
  position: absolute;
  top: -8px;
  right: -8px;
  width: 20px;
  height: 20px;
  background: #f56c6c;
  border: 2px solid #fff;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 12px;
  box-shadow: 0 2px 6px rgba(245, 108, 108, 0.45);
  transition: all 0.25s ease;
  z-index: 2;
  box-sizing: border-box;
}

.delete-btn:hover {
  background: #f23c3c;
  transform: scale(1.15);
  box-shadow: 0 2px 10px rgba(245, 108, 108, 0.65);
}

.delete-btn .el-icon {
  font-size: 12px;
}
</style>
