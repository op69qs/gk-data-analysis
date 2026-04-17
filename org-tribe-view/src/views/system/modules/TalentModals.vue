<template>
  <a-drawer
    :title="title"
    :width="drawerWidth"
    :maskClosable="false"
    @close="handleCancel"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <div :style="{width: '100%',border: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',}">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-divider>基本信息</a-divider>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="姓名"
            :required="true">
            <a-input placeholder="请输入姓名" v-decorator="[ 'NAME', validatorRules.NAME]"/>
          </a-form-item>

          <!-- <a-form-item
            label="所属国库编码"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :hasFeedback="true"
            :required="true">
            <a-input v-model="model.GUOKU_ID" disabled/>
          </a-form-item> -->
          <a-form-item
            label="所属国库编码"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="validateStatus"
            :hasFeedback="true"
            :required="true">
            <span slot="help">{{ validateStatus=='error'?'请所属国库':'&nbsp;&nbsp;' }}</span>
            <a-tree-select
              labelInValue
              showSearch
              key="1"
              treeNodeFilterProp="label"
              v-model="model.GUOKU_ID"
              style="width:100%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="treeDatas"
              placeholder="请选择所属国库"
              @select="handleParentIdSelects">
            </a-tree-select>
          </a-form-item>

          <a-form-item
            label="所属国库"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validate-status="validateStatus"
            :hasFeedback="true"
            :required="true">
            <span slot="help">{{ validateStatus=='error'?'请所属国库':'&nbsp;&nbsp;' }}</span>
            <a-tree-select
              labelInValue
              showSearch
              treeNodeFilterProp="label"
              v-model="model.GUOKU_DSCR"
              style="width:100%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="treeData"
              placeholder="请选择所属国库"
              @select="handleParentIdSelect">
            </a-tree-select>
          </a-form-item>

          <!--<a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="身份证号"
            :required="true">
            <a-input placeholder="请输入身份证号" v-decorator="[ 'ID', validatorRules.ID]"/>
          </a-form-item>-->

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="性别"
            :required="true">
            <a-select placeholder="请选择性别" v-decorator="[ 'SEX', validatorRules.SEX]">
              <a-select-option :value="d.id" v-for="d in sexOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="出生年月"
            :required="true">
            <a-month-picker style="width:100%;" placeholder="请选择出生年月"
                            v-decorator="[ 'BIRTHDAY', validatorRules.BIRTHDAY]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="年龄">
            <a-input-number placeholder="请选择年龄" style="width: 100%;" :min="1" :max="100"
                            v-decorator="[ 'AGE', validatorRules.AGE]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="民族"
            :required="true">
            <a-select placeholder="请选择民族" v-decorator="[ 'NATION', validatorRules.NATION]">
              <a-select-option :value="d.id" v-for="d in nationOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="政治面貌"
            :required="true">
            <a-select placeholder="请选择政治面貌" v-decorator="[ 'POLITICS', validatorRules.POLITICS]">
              <a-select-option :value="d.id" v-for="d in politicalStatusOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="所学专业"
            :required="true">
            <a-input placeholder="请输入所学专业" v-decorator="[ 'MAJOR', validatorRules.MAJOR]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="学历"
            :required="true">
            <a-select placeholder="请选择学历" v-decorator="[ 'EDUCATION', validatorRules.EDUCATION]">
              <a-select-option :value="d.id" v-for="d in academicOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="职 务"
            :required="true">
            <a-select placeholder="请选择职务" v-decorator="[ 'DUTIES', validatorRules.DUTIES]">
              <a-select-option :value="d.id" v-for="d in jobOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="当前工作岗位"
            :required="true">
            <a-input placeholder="请输入当前工作岗位" v-decorator="[ 'CUR_POSITION', validatorRules.CUR_POSITION]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="电话"
            :required="true">
            <a-input v-decorator="[ 'PHONE', validatorRules.PHONE]" placeholder="请输入电话"/>
          </a-form-item>

           <!-- <a-row :style="{textAlign:'center'}">
           <a-button  @click="handleUser" type="primary">关联系统用户</a-button>
          </a-row> -->

         <!--  <a-form-item

            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="关联用户"
            >
           <a-input v-model="message" placeholder="请点击"  @click="handleUser"/>
          </a-form-item> -->

          <a-divider>技能信息</a-divider>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="从事国库年限"
            :required="true">
            <a-input-number placeholder="请输入从事国库年限" style="width: 100%;" :min="1" :max="99"
                            v-decorator="[ 'WORK_LIFE', validatorRules.WORK_LIFE]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="专长"
            :required="true">
            <a-select placeholder="请选择专长" mode="multiple" v-decorator="[ 'EXPERTISE', validatorRules.EXPERTISE]">
              <a-select-option :value="d.id" v-for="d in expertiseOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="职称"
            :required="true">
            <a-select placeholder="请选择职称" v-decorator="[ 'TITLE', validatorRules.TITLE]">
              <a-select-option :value="d.id" v-for="d in TITLEOption" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="是否具有执法证"
            :required="true">
            <j-dict-select-tag v-decorator="['LAWCERT', validatorRules.LAWCERT]" placeholder="请选择是否具有执法证"
                               dictCode="if" @change="changeLawcere" :triggerChange="true"/>
          </a-form-item>

          <a-form-item
            v-show="LAWCERT"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="执法证号"
            :required="true">
            <a-input placeholder="请输入执法证号"
                     v-decorator="[ 'LAWCERT_NO', validatorRules.LAWCERT_NO ]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="参加检查次数"
            :required="true">
            <a-input-number placeholder="请输入参加检查次数" style="width: 100%;" :min="1" :max="99"
                            v-decorator="[ 'CHECK_NO', validatorRules.CHECK_NO]"/>
          </a-form-item>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="是否担任过主查"
            :required="true">
            <j-dict-select-tag v-decorator="['CHIEF', validatorRules.CHIEF]" placeholder="请选择是否担任过主查" dictCode="if"
                               :triggerChange="true"/>
          </a-form-item>

          <a-divider>备注</a-divider>

          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="备注">
            <a-input placeholder="请输入备注" v-decorator="[ 'MEMO', validatorRules.MEMO]"/>


          </a-form-item>
        </a-form>
      </a-spin>
      <a-row :style="{textAlign:'right'}">
        <a-button :style="{marginRight: '8px'}" @click="handleCancel">
          关闭
        </a-button>
        <a-button @click="handleOk" type="primary">确定</a-button>
      </a-row>
<!--      <user-list-modal ref="modalForm" @userShow="userShow" @user="user" @realName="realName"></user-list-modal>-->
    </div>
  </a-drawer>
</template>
<script>
  import {getGuoKuTree, getGuoKuIdTreeTrans, getEnumTypeAll, editTalentPool, addTalentPool} from '@/api/nationalTreasury'
  import pick from 'lodash.pick'
  // import userListModal from '../../talentPool/userListModal'

  export default {
    name: 'TalentModal',
    // components: {userListModal},
    data() {
      return {
        ownGuokuOption: [],//所属国库
        sexOption: [],//性别
        nationOption: [],//性别
        politicalStatusOption: [],//政治面貌
        TITLEOption: [],//职称面貌
        drawerWidth: 700,
        treeData: [],//国库数据
        treeDatas: [],
        treeValue: '0-0-4',
        title: '操作',
        visible: false,
        model: {},
        LAWCERTNOType: 0,
        isKeepalive: true, //是否缓存路由
        userVisible: false,//用户模态框
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validateStatus: '',
        LAWCERT: false,
        //职务option
        jobOption: [],
        //学历option
        academicOption: [],
        //专长option
        expertiseOption: [],
        isShow:false,
        message:'',
        message2:''
      }
    },
    computed: {
      validatorRules: function () {
        return {
          GUOKU_DSCR: {rules: [{required: true, message: '请选择所属国库!'}]},
          GUOKU_LVL_DSCR2: {rules: [{required: true, message: '请选择所属国库!'}]},
          // ID: {rules: [{required: true, pattern: /^\d{15}(\d{2}[A-Za-z0-9])?$/, message: '请输入正确身份证号!'}]},
          NAME: {rules: [{required: true, message: '请输入姓名!'}]},
          SEX: {rules: [{required: true, message: '请选择性别!'}]},
          EDUCATION: {rules: [{required: true, message: '请选择学历!'}]},
          NATION: {rules: [{required: true, message: '请选择民族!'}]},
          POLITICS: {rules: [{required: true, message: '请选择政治面貌!'}]},
          MAJOR: {rules: [{required: true, message: '请输入所学专业!'}]},
          BIRTHDAY: {rules: [{required: true, message: '请选择出生年月!'}]},
          AGE: {rules: [{required: true, message: '请输入年龄!'}]},
          DUTIES: {rules: [{required: true, message: '请选择职务!'}]},
          TITLE: {rules: [{required: true, message: '请选择职称!'}]},
          CUR_POSITION: {rules: [{required: true, message: '请输入职称!'}]},
          PHONE: {rules: [{required: true, message: '请输入电话!'}, {validator: this.validatePhone}]},
          WORK_LIFE: {rules: [{required: true, message: '请输入从事国库年限!'}]},
          LAWCERT_NO: {rules: [{required: this.LAWCERT, message: '请输入执法证号!'}]},
          EXPERTISE: {rules: [{required: true, message: '请选择专长'}]},
          LAWCERT: {rules: [{required: true, message: '请选择是否具有执法证'}]},
          CHECK_NO: {rules: [{required: true, message: '请输入参加检查次数'}]},
          CHIEF: {rules: [{required: true, message: '请选择是否担任过主查'}]}
        }
      }
    },
    created() {
      /*职务请求*/
      getEnumTypeAll(4).then(data => {
        if (data.result === 'success') {
          this.jobOption = data.rows;
        }
      });
      /*学历请求*/
      getEnumTypeAll(1).then(data => {
        if (data.result === 'success') {
          this.academicOption = data.rows;
        }
      });
      /*专长请求*/
      getEnumTypeAll(3).then(data => {
        if (data.result === 'success') {
          this.expertiseOption = data.rows;
        }
      });
      //性别
      getEnumTypeAll(5).then(res => {
        if (res.result === 'success')
          this.sexOption = res.rows
      });
      //民族
      getEnumTypeAll(2).then(res => {
        if (res.result === 'success')
          this.nationOption = res.rows
      });
      //政治面貌
      getEnumTypeAll(10).then(res => {
        if (res.result === 'success')
          this.politicalStatusOption = res.rows
      });
      getGuoKuTree().then((res) => {
        if (res.result === 'success')
          this.treeData = res.rows
      });
      //职称
      getEnumTypeAll(14).then((res) => {
        if (res.result === 'success')
          this.TITLEOption = res.rows
      })

      getGuoKuIdTreeTrans().then((res) => {
        if (res.result === 'success')
          /* this.treeDatas = [{
		"value": "2400000000",
		"label": "国家金库云南省分库",
		"title": "2400000000",
		"key": "国家金库云南省分库",
		"children": [{
			"value": "2401000000",
			"label": "国家金库昆明市中心支库",
			"title": "2401000000",
			"key": "国家金库昆明市中心支库",
			"children": []
		}, {
			"value": "2402000000",
			"label": "国家金库云南滇中新区中心支库",
			"title": "2402000000",
			"key": "国家金库云南滇中新区中心支库",
			"children": []
		}, {
			"value": "2404000000",
			"label": "国家金库昭通市中心支库",
			"title": "2404000000",
			"key": "国家金库昭通市中心支库",
			"children": []
		},]
  }] */
      this.treeDatas = res.rows
      })
    },
    methods: {
      add() {
        // 默认值
        this.edit()
      },
      changeLawcere(value, option) {
        if (value === '0') {
          this.LAWCERT = true
        } else {
          this.LAWCERT = false
          this.form.setFieldsValue({
            LAWCERT_NO: ''
          })
        }
      },
      edit(record) {
        this.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
        this.form.resetFields()
        this.model = Object.assign({}, record)
        //--------------------------------------------------------------------------------------------------
        //根据菜单类型，动态展示页面字段
        if (record) {
           this.model.GUOKU_DSCR = {
            value: record.GUOKU_ID,
            label: record.GUOKU_DSCR
          }
          this.model.GUOKU_ID = {
            value: record.GUOKU_DSCR,
            label: record.GUOKU_ID
          }
          if (record.keepAlive != null) {
            this.isKeepalive = record.keepAlive ? true : false
          } else {
            this.isKeepalive = false // 升级兼容 如果没有（后台没有传过来、或者是新建）默认为false
          }
        }
        //----------------------------------------------------------------------------------------------

        this.visible = true
        let fieldsVal = pick(this.model, /*'ID',*/ 'NAME', 'SEX', 'BIRTHDAY', 'AGE', 'NATION', 'POLITICS', 'MAJOR', 'EDUCATION', 'DUTIES', 'CUR_POSITION', 'PHONE', 'WORK_LIFE', 'EXPERTISE', 'TITLE', 'LAWCERT', 'LAWCERT_NO', 'CHECK_NO', 'CHIEF', 'MEMO')
        fieldsVal.BIRTHDAY = !fieldsVal.BIRTHDAY ? null : this.moment(fieldsVal.BIRTHDAY, 'YYYY-MM')
        fieldsVal.EXPERTISE = !fieldsVal.EXPERTISE ? [] : fieldsVal.EXPERTISE.split(',')//专长
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldsVal)
        })
      },
      userShow(){
        this.isShow = true;
      },
      user(key){
        this.message2 = key;
      },
      realName(key){
        this.message = key;
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        console.log(this.message2);
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            values.BIRTHDAY = values.BIRTHDAY.format('YYYY-MM')
            if (!this.model.GUOKU_DSCR) {
              that.validateStatus = 'error'
              return
            } else {
              that.validateStatus = 'success'
              values.GUOKU_DSCR = this.model.GUOKU_DSCR.label
              console.log(this.model.GUOKU_DSCR)
              values.GUOKU_ID = this.model.GUOKU_DSCR.value
            }
            if (!this.model.GUOKU_ID) {
              that.validateStatus = 'error'
              return
            } else {
              that.validateStatus = 'success'
              //values.GUOKU_ID = this.model.GUOKU_ID
              //values.GUOKU_ID = this.model.GUOKU_DSCR.value
            }
            values.EXPERTISE = values.EXPERTISE.join(',')
            let formData = Object.assign(this.model, values)
            formData.SYS_USERID = this.message2;
            that.confirmLoading = true
            let obj
            if (!this.model.type) {
              obj = addTalentPool(formData)
            } else {
              obj = editTalentPool(formData)
            }
            obj.then((res) => {
              if (res.result === 'success') {
                that.$message.success(res.msg)
                that.$emit('ok')
                that.close()
              } else {
                that.confirmLoading = false
                that.$message.warning(res.msg)
              }
            }).finally(() => {
              that.confirmLoading = false
            })
          } else {
            this.$message.error('*为必填项！')
          }
        })
      },
      handleUser(){
       this.$refs.modalForm.edit();
      },
      handleCancel() {
        this.close()
      },
      onChangeMenuType(e) {
        this.$nextTick(() => {
          this.form.validateFields(['url', 'component'], {force: true})
        })
      },
      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize() {
        let screenWidth = document.body.clientWidth
        if (screenWidth < 500) {
          this.drawerWidth = screenWidth
        } else {
          this.drawerWidth = 700
        }
      },
      handleParentIdSelect(value, node, extra) {
        this.model.GUOKU_ID = {
          value:value.label,
          label:value.value
        }
      },
      handleParentIdSelects(value, node, extra) {
        this.model.GUOKU_DSCR  = {
          value:value.label,
          label:value.value
        }
        //this.$refs.treeSelect.selectedKeys = value.value
      }
      /*/!*出生年月change*!/
      birthdayChange(date, dateString) {
        this.form.setFieldsValue({
          AGE: this.ages(dateString),
        });
      }*/
    }
  }
</script>

<style scoped>

</style>