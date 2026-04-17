import { getAction, deleteAction, putAction, postAction, formPostAction } from '@/api/manage'
/*报告新增*/
const addEntityReport = params => postAction('/fixedReport/newsFlash/addEntityReport', params)
/*月报详情*/
const getMonthlyReport = params => postAction('/fixedReport/monthReport/getMonthlyReport', params)
/*月度快报详情*/
const getMonthlyQuickReport = params => postAction('/fixedReport/newsFlash/getMonthlyReport', params)
/*季报详情*/
const getQuarterReport = params => postAction('/fixedReport/qurReport/getQuarterReport', params)
/*季报快报详情*/
const getQuarterQuickReport = params => postAction('/fixedReport/newsFlashQuarter/getMonthlyReport', params)
/*生成月度报告数据*/
const addMonthlyReport = params => postAction('/fixedReport/monthReport/addMonthlyReport', params)
/*生成月度快报数据*/
const addNewsFlashReport = params => postAction('/fixedReport/newsFlash/addNewsFlashReport', params)
/*生成季度报告数据*/
const addQuarterReport = params => postAction('/fixedReport/qurReport/addQuarterReport', params)
/*生成季度快报数据*/
const addQuarterQuickReport = params => postAction('/fixedReport/newsFlashQuarter/addNewsFlashQuarterReport', params)
/*修改报告模板*/
const editEntityReport = params => postAction('/fixedReport/newsFlash/editEntityReport', params)
// 报告导出
// const downLoadCheckList = params => postAction('/fixedReport/monthReport/downLoadCheckList', params)
// 获取地区报告
const areaReport = params => postAction('/fixedReport/monthReport/areaReport', params)
export {
  addEntityReport,
  getMonthlyReport,
  addMonthlyReport,
  editEntityReport,
  areaReport,
  addNewsFlashReport,
  addQuarterQuickReport,
  getMonthlyQuickReport,
  addQuarterReport,
  getQuarterReport,
  getQuarterQuickReport
}
