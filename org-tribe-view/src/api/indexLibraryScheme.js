import { postAction } from '@/api/manage'

export const listSchemes = params =>
  postAction('/vis/api/indexSchemeController/selectSchemeTable', params)

export const deleteScheme = params =>
  postAction('/vis/api/indexSchemeController/deleteScheme', params)

export const getIndexInfo = params =>
  postAction('/vis/api/indexSchemeController/getIndexInfo', params)

export const previewBarLine = params =>
  postAction('/vis/api/IndexBarLine/getIndexBarLineData', params)

export const saveBarLine = params =>
  postAction('/vis/api/IndexBarLine/saveIndexBarLine', params)

export const previewPie = params =>
  postAction('/vis/api/IndexPie/getIndexPieData', params)

export const savePie = params =>
  postAction('/vis/api/IndexPie/saveIndexPie', params)
