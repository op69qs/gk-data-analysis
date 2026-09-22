<template>
  <a-modal title="新建数据上报" :visible="visible" :confirmLoading="submitting" :maskClosable="false"
           okText="上传并开始处理" @ok="submit" @cancel="close">
    <a-alert class="upload-tip" type="info" showIcon
             :message="uploadMessage" />
    <a-form layout="vertical">
      <a-form-item label="数据来源">
        <a-radio-group v-model="form.sourceDomain" @change="sourceChanged">
          <a-radio-button value="TIMS">TIMS Excel</a-radio-button>
          <a-radio-button value="KEY">KEY 文本</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="业务类型">
        <a-select v-model="form.businessType">
          <a-select-option v-for="item in businessOptions" :key="item.value" :value="item.value">
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item v-if="canDownloadTemplate" label="上报模板">
        <a-button icon="download" :loading="templateDownloading" @click="downloadTemplate">
          下载「快报_收入数据.xls」模板
        </a-button>
        <div class="template-hint">与信创前 DIS 快报收入模板一致；可直接上传 xls/xlsx，或打成 ZIP 再上传。</div>
      </a-form-item>
      <a-form-item label="上报账期" :help="periodHelp">
        <a-month-picker v-model="period" format="YYYY-MM" style="width: 100%"
                        :placeholder="form.sourceDomain === 'KEY' ? '可留空并从原 KEY 文件名识别' : '请选择账期'" />
      </a-form-item>
      <a-form-item v-if="form.sourceDomain === 'KEY'" label="国库代码" help="KEY 可留空并按原程序从 k...t...zip 文件名识别">
        <a-input v-model.trim="form.treasuryCode" placeholder="选填" />
      </a-form-item>
      <a-form-item :label="uploadFileLabel">
        <a-upload :fileList="fileList" :beforeUpload="beforeUpload" :remove="removeFile" :accept="uploadAccept">
          <a-button><a-icon type="folder-open" />{{ uploadFileButton }}</a-button>
        </a-upload>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { uploadReport, downloadReportTemplate } from '@/api/reporting'

export default {
  name: 'ReportUploadModal',
  props: { visible: { type: Boolean, default: false } },
  data() {
    return {
      submitting: false,
      templateDownloading: false,
      period: null,
      fileList: [],
      form: { sourceDomain: 'TIMS', businessType: 'INCOME', treasuryCode: '' }
    }
  },
  computed: {
    businessOptions() {
      if (this.form.sourceDomain === 'KEY') {
        return [
          { value: 'ALL', label: '全部（收入、支出、库存、退库）' },
          { value: 'INCOME', label: '收入' }, { value: 'PAYOUT', label: '支出' },
          { value: 'STOCK', label: '库存' }, { value: 'BACK', label: '退库' }
        ]
      }
      return [
        { value: 'INCOME', label: '收入（指标库）' },
        { value: 'PAYOUT', label: '支出' },
        { value: 'STOCK', label: '库存' },
        { value: 'FLASH_INCOME', label: '快报收入' }
      ]
    },
    canDownloadTemplate() {
      return this.form.sourceDomain === 'TIMS' && this.form.businessType === 'FLASH_INCOME'
    },
    uploadAccept() {
      return this.canDownloadTemplate ? '.zip,.xls,.xlsx' : '.zip'
    },
    uploadFileLabel() {
      return this.canDownloadTemplate ? '上报文件' : 'ZIP 文件'
    },
    uploadFileButton() {
      return this.canDownloadTemplate ? '选择 Excel 或 ZIP' : '选择 ZIP'
    },
    periodHelp() {
      return this.form.sourceDomain === 'TIMS'
        ? '数据必须与所选月份一致。'
        : '留空时严格兼容原 JAR：从 k<业务日期>t<国库代码>.zip 中识别。'
    },
    uploadMessage() {
      if (this.form.sourceDomain === 'KEY') {
        return '上传后自动完成安全解压、解析和入库，关闭页面不会中断。完成或异常请在批次列表查看状态与错误明细。'
      }
      if (this.form.businessType === 'FLASH_INCOME') {
        return '快报收入支持直接上传「快报_收入数据.xls/xlsx」，也支持 ZIP。异步解析写入 STG；完成/失败请在列表查看状态；大屏依赖库内日编排 Event（batch_date=当天）。'
      }
      return '上传后自动完成安全解压、全包解析和 STG 原子入库。完成或异常请在批次列表查看状态与错误明细。'
    }
  },
  watch: {
    'form.businessType'() {
      this.fileList = []
    }
  },
  methods: {
    sourceChanged() {
      this.form.businessType = this.form.sourceDomain === 'KEY' ? 'ALL' : 'INCOME'
      this.form.treasuryCode = ''
      this.period = null
      this.fileList = []
    },
    beforeUpload(file) {
      this.fileList = [file]
      return false
    },
    removeFile() {
      this.fileList = []
    },
    close() {
      if (!this.submitting) this.$emit('close')
    },
    async downloadTemplate() {
      if (!this.canDownloadTemplate) return
      this.templateDownloading = true
      try {
        const blob = await downloadReportTemplate(this.form.businessType)
        if (blob && blob.type && String(blob.type).indexOf('application/json') >= 0) {
          throw new Error('模板下载失败')
        }
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '快报_收入数据.xls'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (error) {
        this.$message.error((error && error.message) || '模板下载失败')
      } finally {
        this.templateDownloading = false
      }
    },
    async submit() {
      if (!this.fileList.length) {
        return this.$message.warning(this.canDownloadTemplate ? '请选择 Excel 或 ZIP 文件' : '请选择 ZIP 文件')
      }
      if (this.form.sourceDomain === 'TIMS' && !this.period) return this.$message.warning('请选择 TIMS 上报账期')
      const data = new FormData()
      data.append('file', this.fileList[0])
      data.append('sourceDomain', this.form.sourceDomain)
      data.append('businessType', this.form.businessType)
      if (this.period) data.append('accountingPeriod', this.period.format('YYYY-MM'))
      if (this.form.sourceDomain === 'KEY' && this.form.treasuryCode) data.append('treasuryCode', this.form.treasuryCode)
      this.submitting = true
      try {
        const response = await uploadReport(data)
        if (!response.success) throw new Error(response.message || '上传失败')
        this.$message.success('文件已接收，后台正在自动解析和入库')
        this.fileList = []
        this.$emit('submitted', response.result)
      } catch (error) {
        this.$message.error(error.message || '上传失败')
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style scoped>
.upload-tip { margin-bottom: 16px; }
.template-hint { margin-top: 6px; color: #888; font-size: 12px; line-height: 1.5; }
</style>
