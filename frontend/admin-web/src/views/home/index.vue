<template>
  <div class="page-container">
    <h2>商品管理</h2>
    <div class="search-toolbar">
      <div class="toolbar-item">
        <label class="toolbar-label">商品名称:</label>
        <div class="toolbar-control">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" />
        </div>
      </div>
      <div class="toolbar-search">
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <div class="add-toolbar">
      <el-button type="primary" @click="handleAdd">新增商品</el-button>
    </div>
    <el-table :data="productData" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="200" />
      <el-table-column prop="name" label="商品名称" min-width="150" />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? "上架" : "下架" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)"
            >编辑</el-button
          >
          <el-button type="danger" size="small" @click="handleDelete(row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div class="add-toolbar" style="margin-top: 10px">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pager.pageSize"
        :current-page="pager.currentPage"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新增/编辑弹窗表单 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData?.id ? '编辑商品' : '新增商品'"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="formData" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="formData.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="formData.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="formData.stock" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="formData.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
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
import {
  createProduct,
  deleteProduct,
  getProducts,
  listProductByPage,
  updateProduct,
} from "../../api/product";
type ProductProps = {
  id?: number;
  name?: string;
  price?: number;
  description?: string;
  stock?: number;
  status?: number;
  createTime?: string;
};
const productData = ref([]);
const dialogVisible = ref(false);
const searchForm = ref<ProductProps>({});
const formData = ref<ProductProps>({});
const total = ref<number>(0);
const pager = ref({
  // current:1,
  // size:3,
  currentPage: 1,
  pageSize: 3,
});

const handleSearch = () => {
  // TODO: 搜索商品
  // getProducts(searchForm.value.name || "").then((res: any) => {
  //   console.log(res);
  //   if (res?.code === 200) {
  //     productData.value = res.data;
  //   }
  // });
  listProductByPage({
    ...pager.value,
    ...searchForm.value,
  }).then((res: any) => {
    if (res?.code === 200) {
      productData.value = res.data?.records || [];
      total.value = res.data?.total || 0;
    }
  });
};

const handleEdit = (row) => {
  // TODO: 编辑商品
  formData.value = { ...row };
  dialogVisible.value = true;
};

const handleDelete = (row) => {
  // TODO: 删除商品
  ElMessageBox.confirm("确定删除吗?")
    .then(() => {
      // TODO: 删除商品
      deleteProduct(row.id).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success("删除成功.");
          handleSearch();
        }
      });
    })
    .catch(() => {
      // catch error
    });
};
const handleAdd = () => {
  // TODO: 新增商品
  formData.value = {};
  dialogVisible.value = true;
};
const handleSubmit = () => {
  // TODO: 提交表单
  if (formData.value.id) {
    updateProduct(formData.value.id, formData.value).then((res: any) => {
      if (res?.code === 200) {
        ElMessage.success("更新成功.");
        dialogVisible.value = false;
        handleSearch();
      }
    });
  } else {
    createProduct(formData.value).then((res: any) => {
      if (res?.code === 200) {
        ElMessage.success("新增成功.");
        dialogVisible.value = false;
        handleSearch();
      }
    });
  }
};
const handlePageChange = (page: number) => {
  pager.value.currentPage = page;
  handleSearch();
};
// 页面加载时获取商品列表
onMounted(() => {
  // TODO: 获取商品列表
  handleSearch();
});
</script>
<style scoped>
.search-toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.toolbar-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.toolbar-label {
  margin-right: 10px;
}

.toolbar-control {
  width: 200px;
}

.toolbar-search {
  margin-left: auto;
}

.add-toolbar {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
