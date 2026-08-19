<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <span class="filter-label">订单号</span>
          <el-input
            v-model="searchForm.number"
            placeholder="请输入订单号"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">手机号</span>
          <el-input
            v-model="searchForm.phone"
            placeholder="请输入收货人电话"
            clearable
            style="width: 160px"
            @keyup.enter="handleSearch"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">订单状态</span>
          <el-select
            v-model="searchForm.status"
            placeholder="请选择"
            clearable
            style="width: 130px"
          >
            <el-option
              v-for="item in ORDER_STATUS_OPTIONS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
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
        <span class="table-title">订单列表</span>
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
        <el-table-column prop="number" label="订单号" min-width="180" />
        <el-table-column prop="orderTime" label="下单时间" min-width="165" />
        <el-table-column
          prop="status"
          label="订单状态"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)" effect="light" round>
              {{ statusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="amount"
          label="订单金额"
          width="110"
          align="center"
        >
          <template #default="{ row }">
            <span class="price-text">¥{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="consignee"
          label="收货人"
          width="90"
          align="center"
        />
        <el-table-column prop="phone" label="联系电话" width="125" />
        <el-table-column
          prop="address"
          label="地址"
          min-width="180"
          show-overflow-tooltip
        />
        <el-table-column label="操作" width="150" fixed="right" align="left">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleDetail(row)"
              >详情</el-button
            >
            <el-button
              v-if="row.status === 2"
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

    <!-- 订单详情弹窗 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="720px"
      destroy-on-close
    >
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{
            detailData.number
          }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{
            detailData.orderTime
          }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag
              :type="statusTagType(detailData.status)"
              effect="light"
              round
            >
              {{ statusText(detailData.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="订单金额">
            <span class="price-text">¥{{ detailData.amount }}</span>
          </el-descriptions-item>
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
        <el-table
          :data="detailData.orderDetails || []"
          size="small"
          stripe
          :header-cell-style="{
            background: '#fafafa',
            color: '#333',
            fontWeight: 600,
          }"
        >
          <el-table-column prop="name" label="名称" min-width="140" />
          <el-table-column prop="number" label="数量" width="70" />
          <el-table-column prop="amount" label="单价" width="80">
            <template #default="{ row }">¥{{ row.amount }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, reactive, onMounted } from "vue";
import {
  listOrderByPage,
  getOrderById,
  completeOrder,
  cancelOrder,
} from "@/api/order";
import {
  ORDER_STATUS_OPTIONS,
  ORDER_STATUS_TEXT,
  ORDER_STATUS_TAG_TYPE,
} from "@/constants/order";

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const detailVisible = ref(false);
const detailLoading = ref(false);
const detailData = reactive<Record<string, any>>({ orderDetails: [] });

const pager = reactive({ current: 1, pageSize: 10 });
const searchForm = reactive({
  number: "",
  phone: "",
  status: undefined as number | undefined,
});

/** 状态文字映射 */
function statusText(status: number): string {
  return ORDER_STATUS_TEXT[status] || "未知";
}

/** 状态标签类型 */
function statusTagType(status: number): string {
  return ORDER_STATUS_TAG_TYPE[status] || "";
}

/** 搜索 */
function handleSearch() {
  loading.value = true;
  const params = { ...pager, ...searchForm };
  // 状态选择“全部（0）”时不传过滤条件
  if (params.status === 0) {
    params.status = undefined;
  }
  listOrderByPage(params)
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
  pager.current = 1;
  handleSearch();
}

/** 查看详情 */
function handleDetail(row: Record<string, any>) {
  Object.assign(detailData, { orderDetails: [] });
  detailVisible.value = true;
  detailLoading.value = true;
  getOrderById({ id: row.id })
    .then((res: any) => {
      if (res?.code === 200) {
        Object.assign(detailData, res.data || row);
      }
    })
    .finally(() => (detailLoading.value = false));
}

/** 完成订单 */
function handleComplete(row: Record<string, any>) {
  ElMessageBox.confirm("确定将该订单标记为已完成？", "提示")
    .then(() => {
      completeOrder(row.id).then((res: any) => {
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
      cancelOrder(row.id).then((res: any) => {
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

.price-text {
  color: #e6a23c;
  font-weight: 600;
}
</style>
