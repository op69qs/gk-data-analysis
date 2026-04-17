<template>
  <!--生成报告-->
  <a-modal
    title="添加项目"
    :maskClosable="false"
    v-model="visibleModal"
    width="50%"
    @ok="handleOk"
    @cancel="handleClose"
    :confirmLoading="confirmLoading"
  >
    <div :style="{width: '100%',background: '#fff',}">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="报告名称" :required="true">
            <a-input
              v-decorator="[ 'REPORT_NAME', validatorRules.REPORT_NAME]"
              placeholder="请输入报告名称"
            />
          </a-form-item>

          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="报告类型" :required="true">
            <a-select
              placeholder="请选择报告模板类型"
              v-decorator="[ 'REPORT_TYPE_ID', validatorRules.REPORT_TYPE_ID]"
              labelInValue
            >
              <a-select-option value="1">月报模板</a-select-option>
              <a-select-option value="2">月度快报模板</a-select-option>
              <a-select-option value="3">季度模板</a-select-option>
              <a-select-option value="4">季度快报模板</a-select-option>
            </a-select>
          </a-form-item>

          <!--<a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :required="true">
            <span slot="label">维度类型</span>
            <a-select placeholder="请选择维度" @change="handleDimensionChange"
                      v-decorator="[ 'DIMENSION_FLAG',{initialValue:'2'}, validatorRules.DIMENSION_FLAG]">
              &lt;!&ndash;              <a-select-option value="1">国库</a-select-option>&ndash;&gt;
              <a-select-option value="2">地区</a-select-option>
            </a-select>
          </a-form-item>-->
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="areaValidateStatus"
            :hasFeedback="true"
            :required="true"
          >
            <span slot="label">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区</span>
            <span slot="help">{{ areaValidateStatus === 'error'?'请选择地区':'&nbsp;&nbsp;' }}</span>
            <a-tree-select
              style="width:100%"
              showSearch
              labelInValue
              treeNodeFilterProp="label"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="areaTree"
              v-model="model.DIM_VALUE"
              placeholder="请选择地区"
              @change="areaChange"
            ></a-tree-select>
          </a-form-item>

          <!--<a-form-item
            v-else-if="form.getFieldValue('DIMENSION_FLAG') === '1'"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="guoKuValidateStatus"
            :hasFeedback="true"
            :required="true">
            <span slot="label">国&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库</span>
            <span slot="help">{{ guoKuValidateStatus==='error'?'请选择国库':'&nbs p;&nbsp;' }}</span>
            <a-tree-select
              style="width:100%"
              showSearch
              labelInValue
              treeNodeFilterProp="label"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="guoKuTree"
              v-model="model.DIM_VALUE"
              placeholder="请选择国库"
              @change="guoKuChange">
            </a-tree-select>

          </a-form-item>-->

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="accountPeriodValidateStatus"
            :required="true"
          >
            <span slot="label">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期</span>
            <span slot="help">{{ accountPeriodValidateStatus==='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
            <a-month-picker
              v-if="form.getFieldValue('REPORT_TYPE_ID') && (form.getFieldValue('REPORT_TYPE_ID').key === '1' || form.getFieldValue('REPORT_TYPE_ID').key === '2') || !form.getFieldValue('REPORT_TYPE_ID')"
              style="width: 100%;"
              placeholder="请选择日"
              format="YYYY-MM"
              v-decorator="[ 'ACCOUNT_PERIOD']"
              @change="dateChange"
              :disabled-date="disabledMonth"
            ></a-month-picker>
            <!-- 自己写的控件 -->
            <quarter-date
              v-else-if="form.getFieldValue('REPORT_TYPE_ID') && (form.getFieldValue('REPORT_TYPE_ID').key === '3' || form.getFieldValue('REPORT_TYPE_ID').key === '4')"
              @value="value => {model.ACCOUNT_PERIOD = value;dateChange(value);}"
            ></quarter-date>
          </a-form-item>
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="金额单位" required>
            <a-select placeholder="请选择金额单位" v-decorator="['AMT_UNIT', validatorRules.AMT_UNIT]">
              <a-select-option value="1">元</a-select-option>
              <a-select-option value="10000">万元</a-select-option>
              <a-select-option value="100000000">亿元</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-spin>
    </div>
  </a-modal>
</template>

<script>
import { getGuokuTree } from '@/api/nationalTreasury'
import {
  addEntityReport,
  addMonthlyReport,
  areaReport,
  addNewsFlashReport,
  addQuarterReport,
  getQuarterReport,
  addQuarterQuickReport
} from '@/api/intelligenceReport'
import quarterDate from '../../../components/intelligenceReport/quarterDate'

export default {
  name: 'editReport',
  components: { quarterDate },
  data() {
    return {
      //日期开关
      yearOpen: false,
      visibleModal: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      labelCol: {
        xs: { span: 23 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      model: {},
      //地区验证状态
      areaValidateStatus: '',
      //国库验证状态
      guoKuValidateStatus: '',
      //账期验证状态
      accountPeriodValidateStatus: '',
      guoKuTree: [],
      areaTree: [],
      checkBoxGroup: [],
      templateParams: {
        pageNo: 1,
        pageSize: 10,
        total: '',
        USERID: this.$sessionStorage.ls.get('Login_Userinfo').id
      }
    }
  },
  computed: {
    validatorRules: function() {
      return {
        REPORT_NAME: { rules: [{ required: true, message: '请输入报告名称!' }] },
        REPORT_TYPE_ID: { rules: [{ required: true, message: '请选择报告类型!' }] },
        AMT_UNIT: { rules: [{ required: true, message: '请选择金额单位!' }] },
        DIMENSION_FLAG: { rules: [{ required: true, message: '请选择维度!' }] }
      }
    }
  },
  methods: {
    //  限制月的方法
    disabledMonth(current) {
      // console.log(current, 'ccccc') //current是月份的最后一个月
      // console.log(this.moment().endOf('month'), 'mmm') // moment是当前月
      return current && current > this.moment().endOf('month')
    },
    add() {
      // 默认值
      this.edit()
    },
    edit(record) {
      this.form.resetFields()
      this.visibleModal = true
      //国库
      getGuokuTree().then(res => {
        if (res.result === 'success') {
          this.guoKuTree = res.rows
        }
      })
      //地区
      areaReport().then(res => {
        if (res.result === 'success') {
          this.areaTree = res.rows
        }
      })
    },
    //确认
    handleOk() {
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          // console.log(values, 'vvvvvvv')
          let formData = Object.assign({}, this.model, values)
          // console.log(this.model, 'mmmmmm')
          // console.log(formData, 'ffffffffff')
          if (!this.model.DIM_VALUE) {
            this.areaValidateStatus = 'error'
            return
          } else {
            this.areaValidateStatus = 'success'
          }
          if (values.AMT_UNIT === '1') {
            formData.AMT_UNIT_NAME = '元'
          } else if (values.AMT_UNIT === '10000') {
            formData.AMT_UNIT_NAME = '万元'
          } else if (values.AMT_UNIT === '100000000') {
            formData.AMT_UNIT_NAME = '亿元'
          }
          formData.DIM_VALUE = this.model.DIM_VALUE.value //地区id
          formData.DIM_DESC = this.model.DIM_VALUE.label //地区描述
          //账期
          if (values.REPORT_TYPE_ID.key === '3' || values.REPORT_TYPE_ID.key === '4') {
            //报告类型为季报
            if (this.model.ACCOUNT_PERIOD) {
              this.accountPeriodValidateStatus = 'success'
            } else {
              this.accountPeriodValidateStatus = 'error'
              return
            }
          } else {
            if (values.ACCOUNT_PERIOD) {
              this.accountPeriodValidateStatus = 'success'
            } else {
              this.accountPeriodValidateStatus = 'error'
              return
            }
            if (values.REPORT_TYPE_ID.key === '1' || values.REPORT_TYPE_ID.key === '2') {
              formData.ACCOUNT_PERIOD = formData.ACCOUNT_PERIOD.format('YYYY-MM')
            }
          }
          formData.ADD_USERID = this.$sessionStorage.ls.get('Login_Userinfo').id
          formData.REPORT_TYPE_ID = values.REPORT_TYPE_ID.key //报告类型id
          formData.REPORT_TYPE_DESC = values.REPORT_TYPE_ID.label //报告类型描述
          this.confirmLoading = true
          console.log(formData, 'fffffffff')

          // debugger
          if (formData.REPORT_TYPE_ID === '1') {
            formData.isNotes = '0' // 是否有注释 0没有1有
          } else if (formData.REPORT_TYPE_ID === '2') {
            formData.isNotes = '1' // 是否有注释 0没有1有
            formData.notes = ['重庆市'] // 所有注释
            formData.notesNum = [2] //对应注释在文档中第几次出现
            formData.notesDesc = [
              '1.地方级国库收入包括一般公共预算收入、基金预算收入和国有资本经营预算收入，即地方政府自有财力，不包含地方政府债务收入和转移性收入。'
            ] // 对应注释描述
          } else if (formData.REPORT_TYPE_ID === '3') {
            //formData.isNotes = '0' // 是否有注释 0没有1有
            formData.isNotes = '1' // 是否有注释 0没有1有
            formData.notes = ['国库收入'] // 所有注释
            formData.notesNum = [1] //对应注释在文档中第几次出现
            formData.notesDesc = [
              '1.由于社保征缴方式处于过渡期，只有城乡居民社会保险纳入预算内，国库收入不含社保收入。'
            ] // 对应注释描述
          } else if (formData.REPORT_TYPE_ID === '4') {
            formData.isNotes = '1' // 是否有注释 0没有1有
            formData.notes = ['重庆市','税收'] // 所有注释
            formData.notesNum = [2,9] //对应注释在文档中第几次出现
            formData.notesDesc = [
              '1.地方级国库收入包括一般公共预算收入、基金预算收入和国有资本经营预算收入，即地方政府自有财力，不包含地方政府债务收入和转移性收入。',
              '2.快报中金融业税收为全口径数据，即中央级与地方级合计，其余数据口径为地方级。'
            ] // 对应注释描述
          }
          console.log(formData, 'fffffffffddddddd')
          addEntityReport(formData)
            .then(res => {
              this.confirmLoading = false
              if (res.result === 'success') {
                console.log(res)
                // debugger
                if (formData.REPORT_TYPE_ID === '1') {
                  console.log(this)
                  // debugger
                  //生成月度报告数据
                  addMonthlyReport(Object.assign({ REPORT_ID: res.REPORT_ID }, formData))
                  console.log(formData, 'fffffffffffdddddddddddddddd')
                  this.handleClose()
                  this.$emit('ok')
                } else if (formData.REPORT_TYPE_ID === '2') {
                  // 生成月度快报数据
                  addNewsFlashReport(Object.assign({ REPORT_ID: res.REPORT_ID }, formData))
                  console.log(formData, 'fffffffffffdddddddddddddddd')
                  //生成月度快报数据
                  this.handleClose()
                  this.$emit('ok')
                } else if (formData.REPORT_TYPE_ID === '3') {
                  // 生成季度报告数据
                  addQuarterReport(Object.assign({ REPORT_ID: res.REPORT_ID }, formData))
                  console.log(formData, 'fffffffffffdddddddddddddddd')
                  //生成季度报告数据
                  this.handleClose()
                  this.$emit('ok')
                }else{
                  // 生成季度报告数据
                  addQuarterQuickReport(Object.assign({ REPORT_ID: res.REPORT_ID }, formData))
                  console.log(formData, 'fffffffffffdddddddddddddddd')
                  //生成季度报告数据
                  this.handleClose()
                  this.$emit('ok')
                }
              }
              this.$message[res.result === 'success' ? 'success' : 'warning'](res.msg)
            })

            .catch(err => {
              this.confirmLoading = false
            })
        }
      })
    },
    handleClose() {
      this.visibleModal = false
      this.areaValidateStatus = ''
      this.model = {}
    },
    //维度change
    handleDimensionChange() {
      this.model.DIM_VALUE = {}
    },
    //日期
    panelChange(e) {
      this.form.setFieldsValue({
        ACCOUNT_PERIOD: e
      })
      this.yearOpen = false
    },
    //  地区change
    areaChange(value) {
      this.areaValidateStatus = value ? 'success' : 'error'
    },
    //  国库change
    guoKuChange(value) {
      this.guoKuValidateStatus = value ? 'success' : 'error'
    },
    //  周期change
    cycleChange(value) {
      this.form.setFieldsValue({ ACCOUNT_PERIOD: undefined })
    },
    //  账期change
    dateChange(value, dateString) {
      // console.log(value)
      // console.log(dateString)
      this.accountPeriodValidateStatus = value ? 'success' : 'error'
    },
    //模板滚动条监听
    scrollEvent(e) {
      //变量scrollTop是滚动条滚动时，距离顶部的距离
      let scrollTop = e.target.scrollTop
      //变量windowHeight是可视区的高度
      let windowHeight = e.target.clientHeight
      //变量scrollHeight是滚动条的总高度
      let scrollHeight = e.target.scrollHeight
      //滚动条到底部的条件
      if (scrollTop + windowHeight === scrollHeight) {
        if (this.checkBoxGroup.length >= this.templateParams.total && this.checkBoxGroup.length > 0) return
        this.getTemplateData(this.templateParams.pageNo++)
      }
    }
  }
}
</script>

<style scoped lang="less">
@border: 1px solid #cecece;
.ant-form-item {
  display: inline-block;
  width: 50%;
}

.template {
  border: @border;
  padding: 24px 15px 0 15px;
  max-height: 240px;
  overflow-y: auto;

  .ant-form-item {
    display: inline-block;
    width: 45%;
  }

  .ant-radio-group {
    width: 100%;

    .ant-row {
      margin-bottom: 20px;

      .ant-col-24 {
        .ant-radio-wrapper {
          display: flex;
          align-items: center;
          justify-content: space-between;
          white-space: normal;

          /deep/ & > span:nth-child(2) {
            width: 90%;

            & > div {
              border: @border;
              border-radius: 10px;

              & > div {
                padding: 10px 15px;
                overflow: hidden;

                &:nth-child(1) {
                  height: 200px;
                  overflow: hidden;
                }

                &:nth-child(2) {
                  border-top: @border;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>