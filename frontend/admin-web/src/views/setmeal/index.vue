<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <span class="filter-label">套餐名称</span>
          <el-input
            v-model="searchForm.name"
            placeholder="请输入套餐名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">套餐分类</span>
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
        <span class="table-title">套餐列表</span>
        <div class="table-header-actions">
          <el-button
            type="danger"
            plain
            :disabled="!selectedIds.length"
            @click="handleBatchDelete"
            >批量删除</el-button
          >
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>新增套餐
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
        <el-table-column prop="name" label="套餐名称" min-width="140" />
        <el-table-column label="图片" width="90" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.imageUrl"
              :src="row.imageUrl"
              style="width: 50px; height: 50px; border-radius: 6px"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
              :preview-teleported="true"
              :z-index="3000"
              hide-on-click-modal
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
        <el-table-column label="套餐分类" width="120" align="center">
          <template #default="{ row }">
            {{ getCategoryName(row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="price"
          label="套餐价格"
          width="110"
          align="center"
        >
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
      :title="formData.id ? '编辑套餐' : '新增套餐'"
      width="820px"
      :close-on-click-modal="false"
      destroy-on-close
      class="setmeal-dialog"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        class="setmeal-form"
      >
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="套餐名称" prop="name">
              <el-input
                v-model="formData.name"
                placeholder="请填写套餐名称"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="套餐分类" prop="categoryId">
              <el-select
                v-model="formData.categoryId"
                placeholder="请选择套餐分类"
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

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="套餐价格" prop="price">
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
              <el-select
                v-model="formData.status"
                placeholder="请选择"
                style="width: 100%"
              >
                <el-option label="启售" :value="1" />
                <el-option label="停售" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="套餐图片">
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
            <!-- 有图：显示预览 + 右上角删除按钮 -->
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

        <el-form-item label="套餐菜品">
          <div class="dish-card">
            <div class="dish-card-toolbar">
              <el-button
                size="small"
                type="warning"
                class="add-dish-btn"
                @click="openDishPicker"
              >
                <el-icon style="margin-right: 4px"><Plus /></el-icon>
                添加菜品
              </el-button>
            </div>
            <el-table
              :data="setmealDishList"
              size="small"
              class="dish-table"
              empty-text="暂未添加菜品"
            >
              <el-table-column prop="name" label="名称" min-width="140" />
              <el-table-column
                prop="price"
                label="原价"
                width="120"
                align="center"
              >
                <template #default="{ row }">
                  <span class="dish-price">¥{{ row.price }}</span>
                </template>
              </el-table-column>
              <el-table-column label="份数" width="200" align="center">
                <template #default="{ row }">
                  <el-input-number
                    v-model="row.copies"
                    :min="1"
                    :max="99"
                    controls-position="both"
                    class="copies-input"
                    size="small"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80" align="center">
                <template #default="{ $index }">
                  <el-button
                    type="warning"
                    size="small"
                    link
                    @click="setmealDishList.splice($index, 1)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>

        <el-form-item label="套餐描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="套餐描述，最长200字"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="warning" @click="handleSubmit(false)"
            >保存</el-button
          >
          <el-button type="primary" @click="handleSubmit(true)">
            保存并继续添加套餐
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 选择菜品弹窗 -->
    <el-dialog
      v-model="dishDialogVisible"
      title="选择菜品"
      width="560px"
      append-to-body
    >
      <div class="dish-picker-toolbar">
        <el-input
          v-model="dishSearchName"
          placeholder="搜索菜品名称"
          clearable
          class="dish-picker-search"
          @input="filterDishes"
        />
        <el-select
          v-model="dishSearchCategoryId"
          placeholder="全部分类"
          clearable
          class="dish-picker-category"
          @change="filterDishes"
        >
          <el-option
            v-for="item in dishCategoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>
      <el-table
        :data="filteredDishList"
        size="small"
        max-height="340"
        stripe
        :header-cell-style="{
          background: '#fafafa',
          color: '#333',
          fontWeight: 600,
        }"
        empty-text="未找到匹配的菜品"
      >
        <el-table-column prop="name" label="菜品名称" />
        <el-table-column label="分类" width="120" align="center">
          <template #default="{ row }">
            {{ getDishCategoryName(row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="80" align="center">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              :disabled="isDishAdded(row)"
              @click="addDishToSetmeal(row)"
            >
              {{ isDishAdded(row) ? "已添加" : "添加" }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Close, Picture } from "@element-plus/icons-vue";
import { ref, reactive, computed, onMounted } from "vue";
import {
  listSetmealByPage,
  getSetmealById,
  createSetmeal,
  updateSetmeal,
  enabledSetmealStatus,
  disabledSetmealStatus,
  deleteBatchSetmeal,
  deleteSetmealById,
} from "@/api/setmeal";
import { listCategoryByPage } from "@/api/category";
import { listDishByPage } from "@/api/dish";
import { uploadFile } from "@/api/auth";
import { getFileUrl } from "@/utils";

const loading = ref(false);
const tableData = ref<any[]>([]);
const categoryList = ref<any[]>([]);
const dishCategoryList = ref<any[]>([]);
const dishAllList = ref<any[]>([]);
const total = ref(0);
const dialogVisible = ref(false);
const dishDialogVisible = ref(false);
const formRef = ref();
const selectedIds = ref<number[]>([]);
const dishSearchName = ref("");
const dishSearchCategoryId = ref<number | undefined>(undefined);
const setmealDishList = ref<any[]>([]);

const pager = reactive({ current: 1, pageSize: 10 });
const searchForm = reactive({
  name: undefined as string | undefined,
  categoryId: undefined as number | undefined,
  status: undefined as number | undefined,
});
const formData = reactive<Record<string, any>>({});

const formRules = {
  name: [{ required: true, message: "请输入套餐名称", trigger: "blur" }],
  categoryId: [
    { required: true, message: "请选择套餐分类", trigger: "change" },
  ],
  price: [{ required: true, message: "请输入套餐价格", trigger: "blur" }],
  status: [{ required: true, message: "请选择售卖状态", trigger: "change" }],
};

// 过滤菜品列表：按名称模糊 + 分类精确
const filteredDishList = computed(() => {
  const keyword = dishSearchName.value?.trim();
  const categoryId = dishSearchCategoryId.value;
  return dishAllList.value.filter((d: any) => {
    if (keyword && !d.name?.includes(keyword)) return false;
    if (categoryId != null && d.categoryId !== categoryId) return false;
    return true;
  });
});

/** 根据 categoryId 解析分类名称（Setmeal 实体无 categoryName 字段，前端回显） */
function getCategoryName(categoryId: any): string {
  if (!categoryId) return "-";
  const target = categoryList.value.find((c) => c.id === categoryId);
  return target?.name || "-";
}

/** 解析选择菜品弹窗中菜品的分类名称（取自菜品分类列表） */
function getDishCategoryName(categoryId: any): string {
  if (!categoryId) return "-";
  const target = dishCategoryList.value.find((c) => c.id === categoryId);
  return target?.name || "-";
}

/** 判断菜品是否已加入套餐（用于禁用重复添加） */
function isDishAdded(dish: any): boolean {
  return setmealDishList.value.some((d: any) => d.dishId === dish.id);
}

/** 上传前校验并上传（图片：image 存文件 id，imageUrl 存可访问的下载 URL） */
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

/** 加载套餐分类列表（type=2 套餐分类），后端 CategoryController 仅提供 /listByPage */
function loadCategories() {
  listCategoryByPage({ current: 1, pageSize: 1000, type: 2 }).then(
    (res: any) => {
      if (res?.code === 200) {
        categoryList.value = res.data?.records || [];
      }
    },
  );
}

/** 加载菜品分类列表（type=1 菜品分类），供选择菜品弹窗分类筛选使用 */
function loadDishCategories() {
  listCategoryByPage({ current: 1, pageSize: 1000, type: 1 }).then(
    (res: any) => {
      if (res?.code === 200) {
        dishCategoryList.value = res.data?.records || [];
      }
    },
  );
}

/** 加载所有启售菜品（供套餐选择） */
function loadAllDishes() {
  listDishByPage({ current: 1, pageSize: 1000, status: 1 }).then((res: any) => {
    if (res?.code === 200) {
      dishAllList.value = res.data?.records || [];
    }
  });
}

/** 过滤菜品（computed 已处理，保留方法以兼容 @input/@change 绑定） */
function filterDishes() {
  // computed 自动处理
}

/** 打开选择菜品弹窗：刷新菜品和分类，并重置筛选条件 */
function openDishPicker() {
  loadAllDishes();
  loadDishCategories();
  dishSearchName.value = "";
  dishSearchCategoryId.value = undefined;
  dishDialogVisible.value = true;
}

/** 添加菜品到套餐 */
function addDishToSetmeal(dish: any) {
  const exists = setmealDishList.value.find((d: any) => d.dishId === dish.id);
  if (exists) {
    ElMessage.warning("该菜品已添加");
    return;
  }
  setmealDishList.value.push({
    dishId: dish.id,
    name: dish.name,
    price: dish.price,
    copies: 1,
  });
  ElMessage.success("添加成功");
}

/** 搜索 */
function handleSearch() {
  loading.value = true;
  listSetmealByPage({ ...pager, ...searchForm })
    .then((res: any) => {
      if (res?.code === 200) {
        const data = res.data;
        // 后端 SetmealController.listByPage 当前直接返回 List<Setmeal>，兼容处理分页结构
        if (Array.isArray(data)) {
          tableData.value = data;
          total.value = data.length;
        } else {
          tableData.value = data?.records || [];
          total.value = data?.total || 0;
        }
        // 列表行补充 imageUrl（用于缩略图展示）
        tableData.value = tableData.value.map((row: any) => ({
          ...row,
          imageUrl: row.image ? getFileUrl(row.image) : "",
        }));
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

/** 重置表单（用于"保存并继续"） */
function resetForm() {
  Object.assign(formData, {
    id: undefined,
    name: "",
    categoryId: undefined,
    price: undefined,
    image: undefined,
    imageUrl: undefined,
    description: "",
    status: 1,
  });
  setmealDishList.value = [];
}

/** 新增 */
function handleAdd() {
  resetForm();
  dialogVisible.value = true;
}

/** 编辑：调用 getById 获取含菜品的完整数据（listByPage 不返回 series） */
function handleEdit(row: Record<string, any>) {
  getSetmealById({ id: row.id }).then((res: any) => {
    if (res?.code !== 200) return;
    const detail = res.data || {};
    Object.assign(formData, {
      ...detail,
      imageUrl: detail.image ? getFileUrl(detail.image) : undefined,
    });
    setmealDishList.value = (detail.series || []).map((d: any) => ({
      ...d,
      dishId: d.dishId,
      name: d.name,
      price: d.price,
      copies: d.copies ?? 1,
    }));
    dialogVisible.value = true;
  });
}

/** 提交：continueAdd=true 时保存后不关闭弹窗（仅新增模式有效） */
async function handleSubmit(continueAdd: boolean) {
  try {
    await formRef.value.validate();
  } catch {
    return;
  }
  if (!setmealDishList.value.length) {
    ElMessage.warning("请至少添加一个菜品");
    return;
  }

  const data = {
    ...formData,
    series: setmealDishList.value.map((d) => ({
      dishId: d.dishId,
      name: d.name,
      price: d.price,
      copies: d.copies,
    })),
  };
  const isEdit = !!formData.id;
  const api = isEdit ? updateSetmeal : createSetmeal;
  const msg = isEdit ? "修改成功" : "新增成功";
  api(data).then((res: any) => {
    if (res?.code === 200) {
      ElMessage.success(msg);
      if (continueAdd && !isEdit) {
        resetForm();
        formRef.value?.clearValidate();
      } else {
        dialogVisible.value = false;
      }
      handleSearch();
    }
  });
}

/** 状态切换 */
function handleStatusChange(row: Record<string, any>) {
  const newStatus = row.status === 1 ? 0 : 1;
  const action = newStatus === 1 ? "起售" : "停售";
  ElMessageBox.confirm(`确定要${action}该套餐吗？`, "提示")
    .then(() => {
      (newStatus === 1
        ? enabledSetmealStatus({ id: row.id })
        : disabledSetmealStatus({ id: row.id })
      ).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success(`${action}成功`);
          handleSearch();
        }
      });
    })
    .catch(() => {});
}

/** 删除单条 */
function handleDelete(row: Record<string, any>) {
  ElMessageBox.confirm(`确定要删除套餐「${row.name}」吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteSetmealById(String(row.id)).then((res: any) => {
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
      deleteBatchSetmeal(selectedIds.value).then((res: any) => {
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
  loadDishCategories();
  loadAllDishes();
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

.setmeal-dish-section {
  width: 100%;
}

/* ========== 套餐弹窗样式 ========== */
.setmeal-dialog :deep(.el-dialog__body) {
  padding: 24px 32px 8px;
}

.setmeal-form {
  padding-right: 8px;
}

.setmeal-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #4a4a4a;
}

.setmeal-form :deep(.el-input__wrapper) {
  border-radius: 6px;
}

/* 图片上传（与菜品页一致） */
.image-upload-wrapper {
  display: flex;
  align-items: flex-start;
}
.el-image {
  display: block;
}
.image-viewer-slot {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
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
  border-color: #f0a020;
  color: #f0a020;
}

.image-preview {
  position: relative;
  width: 100px;
  height: 100px;
}

.preview-img-wrapper {
  width: 100%;
  height: 100%;
  border-radius: 6px;
  overflow: hidden;
}

.preview-img {
  width: 100%;
  height: 100%;
}

.delete-btn {
  position: absolute;
  top: -8px;
  right: -8px;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: #f56c6c;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s;
}

.delete-btn:hover {
  transform: scale(1.1);
}

/* 套餐菜品卡片 */
.dish-card {
  width: 100%;
  background: #fdfaf3;
  border: 1px solid #f5e6c5;
  border-radius: 8px;
  padding: 14px 16px;
}

.dish-card-toolbar {
  margin-bottom: 10px;
}

.add-dish-btn {
  background: #f0a020;
  border-color: #f0a020;
  color: #fff;
  font-weight: 500;
  padding: 6px 14px;
}

.add-dish-btn:hover {
  background: #d98910;
  border-color: #d98910;
  color: #fff;
}

.dish-table {
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
}

.dish-table :deep(th.el-table__cell) {
  background: #fafafa;
  color: #555;
  font-weight: 500;
}

.dish-table :deep(td.el-table__cell) {
  background: #fff;
}

.dish-price {
  color: #e6a23c;
  font-weight: 600;
}

.copies-input {
  width: 130px;
}

.copies-input :deep(.el-input-number__decrease),
.copies-input :deep(.el-input-number__increase) {
  background: #fff7e6;
  border-color: #ffd591;
  color: #d98910;
}

.copies-input :deep(.el-input-number__decrease:hover),
.copies-input :deep(.el-input-number__increase:hover) {
  background: #ffe7ba;
  color: #ad6800;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 选择菜品弹窗工具栏 */
.dish-picker-toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  align-items: center;
}

.dish-picker-search {
  flex: 1;
}

.dish-picker-category {
  width: 160px;
}

/* 套餐价格输入框 */
.price-input {
  display: flex;
  align-items: stretch;
  width: 100%;
}

.price-input-number {
  flex: 1;
}

.price-input-number :deep(.el-input-number__decrease),
.price-input-number :deep(.el-input-number__increase) {
  background: #fafafa;
  border-color: #dcdfe6;
  color: #606266;
}

.price-input-number :deep(.el-input__wrapper),
.price-input-number :deep(.el-input-number) {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
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
</style>
