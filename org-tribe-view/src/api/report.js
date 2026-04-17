import { getAction, deleteAction, putAction, postAction, formPostAction } from '@/api/manage'
/* 获取所有科目接口*/
const getSubjectAll = params => postAction('/fixedReport/reportTndustry/getSubjectAll', params)
/*获取数据行业税收--分页*/
const getTndustryTaxData = params => postAction('/fixedReport/reportTndustry/getTndustryTaxData', params)
/*行业*/
const getIndustryTree = params => postAction('/industryDim/getIndustryTree', params)

//获取预算单位
const getBudgetUnit = params => postAction('/fixedReport/centralizedPaymentController/getBudgetUnit', params)
//获取科目树
const getKeMuTreeName  = params => postAction('/fixedReport/centralizedPaymentController/getKeMuTreeName', params)
//获取科目树编码
const getKeMuTreeCode = params => postAction('/fixedReport/centralizedPaymentController/getKeMuTreeCode', params)
//行业下拉
const getIndustryDrop = params => postAction('/fixedReport/kydReportController/getIndustryDrop', params)
export {
  getSubjectAll,
  getTndustryTaxData,
  getIndustryTree,
  getBudgetUnit,
  getKeMuTreeName,
  getKeMuTreeCode,
  getIndustryDrop
}
