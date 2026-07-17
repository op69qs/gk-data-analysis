<template>
  <a-modal
    :title="title"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    width="640px"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['task_name', rules.task_name]" placeholder="请输入任务名称" />
        </a-form-item>
        <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="['task_type', rules.task_type]" placeholder="请选择任务类型">
            <a-select-option value="1">存储过程</a-select-option>
            <a-select-option value="2">脚本</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="脚本路径" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['shell_path', rules.shell_path]" placeholder="请输入脚本路径" />
        </a-form-item>
        <a-form-item label="脚本名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['shell_name', rules.shell_name]" placeholder="存储过程填写 schema.过程名" />
        </a-form-item>
        <a-form-item label="参数" :labelCol="labelCol" :wrapperCol="wrapperCol" extra="多个参数请使用 @ 分隔">
          <a-input v-decorator="['shell_param']" placeholder="请输入参数" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import pick from 'lodash.pick'
import { postAction } from '@/api/manage'

export default {
  name: 'CallModal',
  data() {
    return {
      title: '新增',
      visible: false,
      confirmLoading: false,
      model: {},
      form: this.$form.createForm(this),
      labelCol: { xs: { span: 24 }, sm: { span: 5 } },
      wrapperCol: { xs: { span: 24 }, sm: { span: 17 } },
      rules: {
        task_name: { rules: [{ required: true, message: '请输入任务名称' }] },
        task_type: { rules: [{ required: true, message: '请选择任务类型' }] },
        shell_path: { rules: [{ required: true, message: '请输入脚本路径' }] },
        shell_name: { rules: [{ required: true, message: '请输入脚本或存储过程名称' }] }
      }
    }
  },
  methods: {
    add() {
      this.edit({ task_type: '1', shell_path: '/home/app/dwbi/' })
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record || {})
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'task_name', 'task_type', 'shell_path', 'shell_name', 'shell_param'))
      })
    },
    handleOk() {
      this.form.validateFields((error, values) => {
        if (error) return
        this.confirmLoading = true
        const payload = Object.assign({}, this.model, values)
        const url = payload.id ? '/errorLogController/edit' : '/errorLogController/add'
        postAction(url, payload).then(res => {
          if (res.result === 'success') {
            this.$message.success(res.msg)
            this.visible = false
            this.$emit('ok')
          } else {
            this.$message.warning(res.msg)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      })
    },
    handleCancel() {
      this.visible = false
      this.form.resetFields()
    }
  }
}
</script>
