<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-toolbar">
      <div class="toolbar-item">
        <label>套餐名称：</label>
        <el-input
          v-model="searchForm.name"
          placeholder="请输入套餐名称"
          clearable
          style="width: 220px"
        />
      </div>
      <div class="toolbar-item">
        <label>套餐分类：</label>
        <el-select
          v-model="searchForm.categoryId"
          placeholder="请选择分类"
          clearable
          style="width: 180px"
        >
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>
      <div class="toolbar-item">
        <label>售卖状态：</label>
        <el-select
          v-model="searchForm.status"
          placeholder="请选择"
          clearable
          style="width: 130px"
        >
          <el-option label="启售" :value="1" />
          <el-option label="停售" :value="0" />
        </el-select>
      </div>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">+ 新增套餐</el-button>
      <el-button
        type="danger"
        :disabled="!selectedIds.length"
        @click="handleBatchDelete"
        >批量删除</el-button
      >
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="tableData"
      border
      stripe
      v-loading="loading"
      @selection-change="(rows) => (selectedIds = rows.map((r: any) => r.id))"
    >
      <el-table-column type="selection" width="50" />
      <el-table-column prop="name" label="套餐名称" min-width="140" />
      <el-table-column label="图片" width="90">
        <template #default="{ row }">
          <el-image
            v-if="row.image"
            :src="row.image"
            style="width: 50px; height: 50px; border-radius: 4px"
            fit="cover"
            :preview-src-list="[row.image]"
          />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="套餐分类" width="120" />
      <el-table-column prop="price" label="套餐价格" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="status" label="售卖状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? "启售" : "停售" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="最后操作时间" width="175" />
      <el-table-column label="操作" width="210" fixed="right">
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
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.id ? '编辑套餐' : '新增套餐'"
      width="700px"
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
            <el-form-item label="套餐名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入套餐名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="套餐分类" prop="categoryId">
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
            <el-form-item label="套餐价格" prop="price">
              <el-input-number
                v-model="formData.price"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="套餐图片">
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
        <el-form-item label="套餐菜品">
          <div class="setmeal-dish-section">
            <el-table
              :data="setmealDishList"
              border
              size="small"
              style="width: 100%"
            >
              <el-table-column prop="name" label="菜品名称" min-width="120" />
              <el-table-column prop="copies" label="份数" width="80">
                <template #default="{ row }">
                  <el-input-number v-model="row.copies" :min="1" size="small" />
                </template>
              </el-table-column>
              <el-table-column prop="price" label="价格" width="80">
                <template #default="{ row }">¥{{ row.price }}</template>
              </el-table-column>
              <el-table-column label="操作" width="60">
                <template #default="{ $index }">
                  <el-button
                    type="danger"
                    size="small"
                    link
                    @click="setmealDishList.splice($index, 1)"
                  >
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button
              size="small"
              type="primary"
              plain
              style="margin-top: 8px"
              @click="dishDialogVisible = true"
            >
              + 添加菜品
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="套餐描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入套餐描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 选择菜品弹窗 -->
    <el-dialog
      v-model="dishDialogVisible"
      title="选择菜品"
      width="500px"
      append-to-body
    >
      <el-input
        v-model="dishSearchName"
        placeholder="搜索菜品"
        clearable
        style="margin-bottom: 12px"
        @input="filterDishes"
      />
      <el-table :data="filteredDishList" border size="small" max-height="300">
        <el-table-column prop="name" label="菜品名称" />
        <el-table-column prop="price" label="价格" width="80">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="addDishToSetmeal(row)"
              >添加</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, reactive, computed, onMounted } from "vue";
import {
  listSetmealByPage,
  createSetmeal,
  updateSetmeal,
  updateSetmealStatus,
  deleteSetmeal,
} from "../../api/setmeal";
import { listCategory } from "../../api/category";
import { listDishByPage } from "../../api/dish";

const loading = ref(false);
const tableData = ref([]);
const categoryList = ref<any[]>([]);
const dishAllList = ref<any[]>([]);
const total = ref(0);
const dialogVisible = ref(false);
const dishDialogVisible = ref(false);
const formRef = ref();
const selectedIds = ref<number[]>([]);
const dishSearchName = ref("");
const setmealDishList = ref<any[]>([]);

const pager = reactive({ page: 1, pageSize: 10 });
const searchForm = reactive({
  name: "",
  categoryId: "",
  status: undefined as number | undefined,
});
const formData = reactive<Record<string, any>>({});

const formRules = {
  name: [{ required: true, message: "请输入套餐名称", trigger: "blur" }],
  categoryId: [
    { required: true, message: "请选择套餐分类", trigger: "change" },
  ],
  price: [{ required: true, message: "请输入套餐价格", trigger: "blur" }],
};

// 过滤菜品列表
const filteredDishList = computed(() => {
  if (!dishSearchName.value) return dishAllList.value;
  return dishAllList.value.filter((d: any) =>
    d.name.includes(dishSearchName.value),
  );
});

/** 加载分类 */
function loadCategories() {
  listCategory(2).then((res: any) => {
    if (res?.code === 200) categoryList.value = res.data || [];
  });
}

/** 加载所有菜品（供选择） */
function loadAllDishes() {
  listDishByPage({ page: 1, pageSize: 100, status: 1 }).then((res: any) => {
    if (res?.code === 200) dishAllList.value = res.data?.records || [];
  });
}

/** 过滤菜品 */
function filterDishes() {
  // computed 自动处理
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
  ElMessage.success("已添加");
}

/** 搜索 */
function handleSearch() {
  loading.value = true;
  listSetmealByPage({ ...pager, ...searchForm })
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
  setmealDishList.value = [];
  dialogVisible.value = true;
}

/** 编辑 */
function handleEdit(row: Record<string, any>) {
  Object.assign(formData, { ...row });
  setmealDishList.value = row.setmealDishes || [];
  dialogVisible.value = true;
}

/** 提交 */
async function handleSubmit() {
  try {
    await formRef.value.validate();
  } catch {
    return;
  }
  if (!setmealDishList.value.length) {
    ElMessage.warning("请至少添加一个菜品");
    return;
  }

  const data = { ...formData, setmealDishes: setmealDishList.value };
  const api = formData.id ? updateSetmeal : createSetmeal;
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
  ElMessageBox.confirm(`确定要${action}该套餐吗？`, "提示")
    .then(() => {
      updateSetmealStatus(newStatus, row.id).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success(`${action}成功`);
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
      deleteSetmeal(selectedIds.value.join(",")).then((res: any) => {
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
  loadAllDishes();
  handleSearch();
});
</script>

<style scoped>
.search-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}
.toolbar-item {
  display: flex;
  align-items: center;
  gap: 4px;
}
.action-bar {
  margin-bottom: 16px;
}
.pagination-bar {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
.setmeal-dish-section {
  width: 100%;
}
</style>
