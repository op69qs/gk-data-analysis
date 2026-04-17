<template>
  <a-modal
    :title="title"
    :maskClosable="false"
    :visible="visible"
    @ok="handleOk"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-form :form="form">
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="级次" :required="true">
        <a-select
          labelInValue
          placeholder="请选择新增指标的级次"
          v-decorator="[ 'C_BDGLEVEL', validatorRules.C_BDGLEVEL]"
          allowClear
        >
          <a-select-option v-for="d in C_BDGLEVEL_OPTIONS" :key="d.id" :value="d.id">{{d.label}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="辖属" :required="true">
        <a-select
          placeholder="请选择新增指标的辖属"
          v-decorator="[ 'JURISDICTION', validatorRules.JURISDICTION]"
          allowClear
        >
          <a-select-option :value="d.id" v-for="d in JURISDICTION_OPTION" :key="d.id">{{d.label}}</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { addEnum, editEnum, getIndexLevels, getIndexJurisdiction } from '@/api/nationalTreasury'
import pick from 'lodash.pick'

export default {
  name: 'levelModel',
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      title: '指标级次辖属',
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      model: {},
      JURISDICTION_OPTION: [],
      C_BDGLEVEL_OPTIONS: []
    }
  },
  computed: {
    validatorRules: function() {
      return {
        C_BDGLEVEL: { rules: [{ required: true, message: '请输入级次!' }] },
        JURISDICTION: { rules: [{ required: true, message: '请输入辖属!' }] }
      }
    }
  },
  methods: {
    edit(record) {
      if (typeof record === 'string') {
        getIndexLevels({ INDEX_ID: record }).then(res => {
          if (res.result === 'success') {
            this.C_BDGLEVEL_OPTIONS = res.rows
          }
        })
        getIndexJurisdiction({ INDEX_ID: record }).then(res => {
          if (res.result === 'success') {
            this.JURISDICTION_OPTION = res.rows
          }
        })
        this.form.resetFields()
        this.model = {}
      } else {
        getIndexLevels({ INDEX_ID: record.ID }).then(res => {
          if (res.result === 'success') {
            this.C_BDGLEVEL_OPTIONS = res.rows
          }
        })
        getIndexJurisdiction({ INDEX_ID: record.ID }).then(res => {
          if (res.result === 'success') {
            this.JURISDICTION_OPTION = res.rows
          }
        })
        this.form.resetFields()
        this.model = {}
        this.model = Object.assign({}, record)
        this.model.C_BDGLEVEL = !record.C_BDGLEVEL
          ? {}
          : {
              label: record.C_BDGLEVEL_DSCR,
              key: record.C_BDGLEVEL
            }
        let fieldsVal = pick(this.model, 'C_BDGLEVEL', 'JURISDICTION')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldsVal)
        })
      }
      this.visible = true
    },
    handleOk() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          debugger
          let formData = Object.assign(this.model, values)
          that.confirmLoading = true
          that.$emit('ok', formData)
          that.visible = false
          that.confirmLoading = false
        }
      })
    },
    handleCancel(e) {
      this.visible = false
    }
  }
}
</script>

<style scoped>
</style>