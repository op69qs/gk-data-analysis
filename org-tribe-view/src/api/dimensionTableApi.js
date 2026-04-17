import { getAction, deleteAction, putAction, postAction, formPostAction } from '@/api/manage'
/* 获取维度信息查询接口*/
const getMainPage = params => postAction('/seo/dimensionController/getMainPage', params)
/*维度信息新增*/
const addMain = params => postAction('/seo/dimensionController/addMain', params)
//维度信息编辑
const editMain = params => postAction('/seo/dimensionController/editMain', params)
//维度信息删除
const delMain  = params => postAction('/seo/dimensionController/delMain', params)
//维度信息明细新增
const addSub = params => postAction('/seo/dimensionController/addSub', params)
//维度信息明细修改
const editSub = params => postAction('/seo/dimensionController/editSub', params)
//维度信息明细删除
const delSub = params => postAction('/seo/dimensionController/delSub', params)
//维度信息明细列表
const getSubPage = params => postAction('/seo/dimensionController/getSubPage', params)
//维度信息下拉列表
const getMainAll = params => postAction('/seo/dimensionController/getMainAll', params)
//维度信息明细导入
const readExcel = params => postAction('/seo/dimensionController/readExcel', params)
export {
  getMainPage,
  addMain,
  editMain,
  delMain,
  addSub,
  editSub,
  delSub,
  getSubPage,
  getMainAll,
  readExcel
}
