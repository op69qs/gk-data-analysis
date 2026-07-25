import { axios } from '@/utils/request'
import { deleteAction, getAction, postAction, putAction } from '@/api/manage'

export const queryReportBatches = params => getAction('/reporting/batches', params)
export const getReportBatchDetail = batchId => getAction(`/reporting/batches/${batchId}`)
export const retryReportTask = (batchId, taskType) => postAction(`/reporting/batches/${batchId}/retry?taskType=${taskType}`, {})
export const deleteReportBatch = batchId => deleteAction(`/reporting/batches/${batchId}`)

export function uploadReport(formData) {
  return axios({
    url: '/reporting/batches/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function downloadReportFile(fileId) {
  return axios({ url: `/reporting/files/${fileId}/download`, method: 'get', responseType: 'blob' })
}

export const queryKeyMonitoring = params => getAction('/reporting/monitoring/key', params)
export const queryTimsMonitoring = params => getAction('/reporting/monitoring/tims', params)
export const queryTreasuryOptions = () => getAction('/reporting/monitoring/treasuries')

export const queryAgentTreasuries = params => getAction('/reporting/agent-treasuries', params)
export const addAgentTreasury = data => postAction('/reporting/agent-treasuries', data)
export const updateAgentTreasury = (treasuryCode, data) => putAction(`/reporting/agent-treasuries/${treasuryCode}`, data)

export const queryChangeSource = params => getAction('/reporting/changes/source', params)
export const queryChangeHistory = params => getAction('/reporting/changes', params)
export const addReportChange = data => postAction('/reporting/changes', data)
