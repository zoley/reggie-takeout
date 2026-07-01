/**
 * 通用工具函数
 */
export const BASE_PATH="/api/v1"
/** 格式化日期 */
export function formatDate(date: string | number | Date, fmt = "YYYY-MM-DD HH:mm:ss") {
  if (!date) return "";
  const d = new Date(date);
  const map: Record<string, string> = {
    YYYY: String(d.getFullYear()),
    MM: String(d.getMonth() + 1).padStart(2, "0"),
    DD: String(d.getDate()).padStart(2, "0"),
    HH: String(d.getHours()).padStart(2, "0"),
    mm: String(d.getMinutes()).padStart(2, "0"),
    ss: String(d.getSeconds()).padStart(2, "0"),
  };
  let result = fmt;
  for (const [key, val] of Object.entries(map)) {
    result = result.replace(key, val);
  }
  return result;
}

/** 格式化价格（¥前缀） */
export function formatPrice(price: number): string {
  if (price == null) return "¥0.00";
  return `¥${Number(price).toFixed(2)}`;
}

/** 本地存储读取 */
export function getLocalStorage(key: string): string | null {
  return localStorage.getItem(key);
}

/** 本地存储写入 */
export function setLocalStorage(key: string, value: string): void {
  localStorage.setItem(key, value);
}
