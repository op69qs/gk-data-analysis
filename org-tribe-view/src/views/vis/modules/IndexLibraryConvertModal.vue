<template>
  <a-modal
    title="转图"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    okText="确定"
  >
    <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 17 }">
      <a-form-item label="方案名称">
        <a-input :value="model.name" disabled />
      </a-form-item>
      <a-form-item label="图库标题">
        <a-input
          v-decorator="['title', { rules: [{ required: true, message: '请输入图库标题' }], initialValue: model.name }]"
          placeholder="请输入图库标题"
        />
      </a-form-item>
      <a-form-item label="图表类型">
        <a-select
          v-decorator="['type', { rules: [{ required: true, message: '请选择图表类型' }], initialValue: 'bar' }]"
          placeholder="请选择图表类型"
        >
          <a-select-option value="bar">柱状图</a-select-option>
          <a-select-option value="line">折线图</a-select-option>
          <a-select-option value="pie">饼图</a-select-option>
          <a-select-option value="barAndLine">柱状折线图</a-select-option>
          <a-select-option value="map">地图</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { postAction } from '@/api/manage'

export default {
  name: 'IndexLibraryConvertModal',
  data() {
    return {
      visible: false,
      confirmLoading: false,
      model: {},
      form: this.$form.createForm(this)
    }
  },
  methods: {
    open(record) {
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.resetFields()
        this.form.setFieldsValue({
          title: record.name || '',
          type: 'bar'
        })
      })
    },
    handleCancel() {
      this.visible = false
      this.confirmLoading = false
    },
    handleOk() {
      this.form.validateFields((err, values) => {
        if (err) {
          return
        }
        let addUser = ''
        try {
          addUser = JSON.parse(localStorage.pro__Login_Userinfo).value.id
        } catch (e) {
          addUser = ''
        }
        this.confirmLoading = true
        postAction('/vis/api/indexLibraryScheme/toGallery', {
          id: this.model.id,
          title: values.title,
          type: values.type,
          business_id: '1010',
          add_user: addUser
        })
          .then(res => {
            if (res.result === 'success') {
              this.$message.success(res.msg || '转图成功')
              this.visible = false
              this.$emit('ok')
            } else {
              this.$message.warning(res.msg || '转图失败')
            }
          })
          .catch(() => {
            this.$message.error('转图失败')
          })
          .finally(() => {
            this.confirmLoading = false
          })
      })
    }
  }
}
</script>
