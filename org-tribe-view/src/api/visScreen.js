import { getAction, postAction } from '@/api/manage'

const api = {
  schemeList: '/vis/api/schemeInfo/getAll',
  schemePageList: '/vis/api/schemeInfo/getPage',
  schemeAllPage: '/vis/api/schemeInfo/getAllPage',
  schemeAdd: '/vis/api/schemeInfo/add',
  schemeEdit: '/vis/api/schemeInfo/edit',
  schemeRel: '/vis/api/schemeInfo/getAllRel',
  pageList: '/vis/api/pageInfo/getPage',
  pageAdd: '/vis/api/pageInfo/add',
  pageEdit: '/vis/api/pageInfo/edit',
  pageEditState: '/vis/api/pageInfo/editState',
  pageDelete: '/vis/api/pageInfo/del',
  pageSubAll: '/vis/api/pageSub/getAll',
  galleryList: '/vis/api/gallery/getPage',
  galleryEdit: '/vis/api/gallery/edit',
  dictList: '/vis/api/visDict/list',
  dictExport: '/vis/api/visDict/exportXls',
  businessTypeList: '/vis/api/bussType/getAll',
  businessTypePageList: '/vis/api/bussType/getPage',
  businessTypeAdd: '/vis/api/bussType/add',
  businessTypeEdit: '/vis/api/bussType/edit',
  treasuryTreeList: '/vis/api/GuokuController/getGuoKuTreeList',
  guokuTree: '/vis/api/GuokuController/getGuokuTree',
  orgTree: '/vis/api/GuokuController/getOrgTree',
  areaTree: '/vis/api/GuokuController/getAreaTree',
  areaControllerTree: '/vis/api/areaController/getArea',
  enumTypeAll: '/vis/api/EnumController/getEnumTypeAll',
  guokuAdd: '/vis/api/GuokuController/addGuoku',
  guokuEdit: '/vis/api/GuokuController/editGuoku',
  kemuTreeName: '/vis/api/GuokuController/getKeMuTreeName',
  subjectTree: '/vis/api/GuokuController/getSubjectT'
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

export function addSchemeInfo(parameter) {
  return postAction(api.schemeAdd, parameter)
}

export function editSchemeInfo(parameter) {
  return postAction(api.schemeEdit, parameter)
}

export function getSchemeRel(parameter) {
  return postAction(api.schemeRel, parameter)
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

export function editGallery(parameter) {
  return postAction(api.galleryEdit, parameter)
}

export function getBusinessTypeList(parameter) {
  return postAction(api.businessTypeList, parameter || {})
}

export function getBusinessTypePageList(parameter) {
  return postAction(api.businessTypePageList, parameter)
}

export function addBusinessType(parameter) {
  return postAction(api.businessTypeAdd, parameter)
}

export function editBusinessType(parameter) {
  return postAction(api.businessTypeEdit, parameter)
}

export function getTreasuryTreeList(parameter) {
  return postAction(api.treasuryTreeList, parameter)
}

export function getGuokuTree(parameter) {
  return postAction(api.guokuTree, parameter)
}

export function getOrgTree(parameter) {
  return postAction(api.orgTree, parameter)
}

export function getAreaTree(parameter) {
  return postAction(api.areaTree, parameter)
}

export function getAreaControllerTree(parameter) {
  return postAction(api.areaControllerTree, parameter)
}

export function getEnumTypeAll(parameter) {
  return postAction(api.enumTypeAll, { ENUM_TYPE_ID: parameter })
}

export function addGuoku(parameter) {
  return postAction(api.guokuAdd, parameter)
}

export function editGuoku(parameter) {
  return postAction(api.guokuEdit, parameter)
}

export function getKeMuTreeName(parameter) {
  return postAction(api.kemuTreeName, parameter)
}

export function getSubjectTree(parameter) {
  return postAction(api.subjectTree, parameter)
}

export default api