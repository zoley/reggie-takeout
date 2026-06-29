<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-toolbar">
      <div class="toolbar-item">
        <label>订单号：</label>
        <el-input
          v-model="searchForm.number"
          placeholder="请输入订单号"
          clearable
          style="width: 220px"
        />
      </div>
      <div class="toolbar-item">
        <label>手机号：</label>
        <el-input
          v-model="searchForm.phone"
          placeholder="请输入收货人电话"
          clearable
          style="width: 180px"
        />
      </div>
      <div class="toolbar-item">
        <label>订单状态：</label>
        <el-select
          v-model="searchForm.status"
          placeholder="请选择"
          clearable
          style="width: 130px"
        >
          <el-option label="待付款" :value="1" />
          <el-option label="待接单" :value="2" />
          <el-option label="配送中" :value="3" />
          <el-option label="已完成" :value="4" />
          <el-option label="已取消" :value="5" />
        </el-select>
      </div>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="number" label="订单号" min-width="180" />
      <el-table-column prop="orderTime" label="下单时间" width="165" />
      <el-table-column prop="status" label="订单状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.status)">{{
            statusText(row.status)
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="订单金额" width="100">
        <template #default="{ row }">¥{{ row.amount }}</template>
      </el-table-column>
      <el-table-column prop="consignee" label="收货人" width="90" />
      <el-table-column prop="phone" label="联系电话" width="125" />
      <el-table-column
        prop="address"
        label="地址"
        min-width="180"
        show-overflow-tooltip
      />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" link @click="handleDetail(row)"
            >查看详情</el-button
          >
          <el-button
            v-if="row.status === 3"
            type="success"
            size="small"
            link
            @click="handleComplete(row)"
            >完成</el-button
          >
          <el-button
            v-if="row.status <= 3"
            type="danger"
            size="small"
            link
            @click="handleCancel(row)"
            >取消</el-button
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

    <!-- 订单详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="720px"
      destroy-on-close
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{
          detailData.number
        }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{
          detailData.orderTime
        }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="statusTagType(detailData.status)">{{
            statusText(detailData.status)
          }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="订单金额"
          >¥{{ detailData.amount }}</el-descriptions-item
        >
        <el-descriptions-item label="收货人">{{
          detailData.consignee
        }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{
          detailData.phone
        }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{
          detailData.address
        }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{
          detailData.remark || "无"
        }}</el-descriptions-item>
      </el-descriptions>

      <h4 style="margin: 16px 0 8px">订单明细</h4>
      <el-table :data="detailData.orderDetails || []" border size="small">
        <el-table-column prop="name" label="名称" min-width="140" />
        <el-table-column prop="number" label="数量" width="70" />
        <el-table-column prop="amount" label="单价" width="80">
          <template #default="{ row }">¥{{ row.amount }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, reactive, onMounted } from "vue";
import {
  listOrderByPage,
  getOrderDetail,
  updateOrderStatus,
} from "../../api/order";

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const detailVisible = ref(false);
const detailData = reactive<Record<string, any>>({ orderDetails: [] });

const pager = reactive({ page: 1, pageSize: 10 });
const searchForm = reactive({
  number: "",
  phone: "",
  status: undefined as number | undefined,
});

/** 状态文字映射 */
function statusText(status: number): string {
  const map: Record<number, string> = {
    1: "待付款",
    2: "待接单",
    3: "配送中",
    4: "已完成",
    5: "已取消",
  };
  return map[status] || "未知";
}

/** 状态标签类型 */
function statusTagType(status: number): string {
  const map: Record<number, string> = {
    1: "warning",
    2: "",
    3: "primary",
    4: "success",
    5: "info",
  };
  return map[status] || "";
}

/** 搜索 */
function handleSearch() {
  loading.value = true;
  listOrderByPage({ ...pager, ...searchForm })
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
  Object.assign(searchForm, { number: "", phone: "", status: undefined });
  pager.page = 1;
  handleSearch();
}

/** 查看详情 */
function handleDetail(row: Record<string, any>) {
  getOrderDetail(row.id).then((res: any) => {
    if (res?.code === 200) {
      Object.assign(detailData, res.data || row);
      detailVisible.value = true;
    }
  });
}

/** 完成订单 */
function handleComplete(row: Record<string, any>) {
  ElMessageBox.confirm("确定将该订单标记为已完成？", "提示")
    .then(() => {
      updateOrderStatus(row.id, 4).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success("订单已完成");
          handleSearch();
        }
      });
    })
    .catch(() => {});
}

/** 取消订单 */
function handleCancel(row: Record<string, any>) {
  ElMessageBox.confirm("确定要取消该订单吗？", "提示")
    .then(() => {
      updateOrderStatus(row.id, 5).then((res: any) => {
        if (res?.code === 200) {
          ElMessage.success("订单已取消");
          handleSearch();
        }
      });
    })
    .catch(() => {});
}

onMounted(() => handleSearch());
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
.pagination-bar {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
