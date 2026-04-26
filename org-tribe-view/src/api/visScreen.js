import { getAction, postAction } from '@/api/manage'

const api = {
  schemeList: '/vis/api/schemeInfo/getAll',
  schemePageList: '/vis/api/schemeInfo/getPage',
  schemeAllPage: '/vis/api/schemeInfo/getAllPage',
  pageList: '/vis/api/pageInfo/getPage',
  pageAdd: '/vis/api/pageInfo/add',
  pageEdit: '/vis/api/pageInfo/edit',
  pageEditState: '/vis/api/pageInfo/editState',
  pageDelete: '/vis/api/pageInfo/del',
  pageSubAll: '/vis/api/pageSub/getAll',
  galleryList: '/vis/api/gallery/getPage',
  dictList: '/vis/api/visDict/list',
  dictExport: '/vis/api/visDict/exportXls',
  businessTypeList: '/vis/api/bussType/getAll',
  businessTypePageList: '/vis/api/bussType/getPage',
  treasuryTreeList: '/vis/api/GuokuController/getGuoKuTreeList'
}

export function getSchemeList(parameter) {
  return getAction(api.schemeList, parameter)
}

export function getSchemePageList(parameter) {
  return postAction(api.schemePageList, parameter)
}

export function getSchemeAllPage(parameter) {
  return postAction(api.schemeAllPage, parameter)
}

export function getPageList(parameter) {
  return postAction(api.pageList, parameter)
}

export function addPageInfo(parameter) {
  return postAction(api.pageAdd, parameter)
}

export function editPageInfo(parameter) {
  return postAction(api.pageEdit, parameter)
}

export function editPageState(parameter) {
  return postAction(api.pageEditState, parameter)
}

export function deletePageInfo(parameter) {
  return postAction(api.pageDelete, parameter)
}

export function getPageSubAll(parameter) {
  return postAction(api.pageSubAll, parameter)
}

export function getGalleryList(parameter) {
  return postAction(api.galleryList, parameter)
}

export function getBusinessTypeList(parameter) {
  return postAction(api.businessTypeList, parameter || {})
}

export function getBusinessTypePageList(parameter) {
  return postAction(api.businessTypePageList, parameter)
}

export function getTreasuryTreeList(parameter) {
  return postAction(api.treasuryTreeList, parameter)
}

export default api