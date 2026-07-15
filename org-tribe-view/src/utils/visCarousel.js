/**
 * 解析大屏轮播间隔（毫秒）。
 * - 未配置、非法值或小于 5000ms：使用 5000ms
 * - 小于 100 的数值：按秒存储的历史数据，乘以 1000
 * - 其余：按毫秒处理，并保留 5000ms 以上的配置
 */
export function resolveVisCarouselInterval(raw) {
  var MINIMUM_MS = 5000
  if (raw == null || raw === '') {
    return MINIMUM_MS
  }
  var n = parseInt(String(raw).trim(), 10)
  if (isNaN(n) || n <= 0) {
    return MINIMUM_MS
  }
  var milliseconds = n < 100 ? n * 1000 : n
  return Math.max(milliseconds, MINIMUM_MS)
}
