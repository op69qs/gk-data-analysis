<template>
  <a-drawer title="新增" :width="drawerWidth" :maskClosable="false" @close="handleCancel" :visible="visible"
            :confirmLoading="confirmLoading"
            :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}">
    <div id="add1" :style="{width: '100%',border: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',}">
      <a-spin :spinning="confirmLoading">
        <a-form layout="inline" :form="form">
          <a-row :gutter="24">
            <a-col :md="12" :sm="12">
              <a-form-item :validate-status="validateStatus0" style="width:100%;margin-bottom:12px;" label="一级分类"
                           :labelCol="{span: 7}" :wrapperCol="{span: 17}" required>
                <span slot="help">{{ validateStatus0=='error'?'请选择一级分类':'&nbsp;&nbsp;' }}</span>
                <a-select v-model="queryParam.FIRST_CLASSIFY" placeholder="请选择一级分类" @change="headerChange">
                  <a-select-option :value="d.id" v-for="d in FIRST_CLASSIFY_OPTION" :key="d.id">{{d.name}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
              <a-form-item :validate-status="validateStatus" style="width:100%;margin-bottom:12px;" label="二级分类"
                           :labelCol="{span: 7}" :wrapperCol="{span: 17}" required>
                <span slot="help">{{ validateStatus=='error'?'请输入分类名称以便后续查找':'&nbsp;&nbsp;' }}</span>
                <el-select
                  placeholder="请输入/选择分类名称以便后续查找"
                  style="width: 100%;"
                  clearable
                  v-model="queryParam.SECOND_CLASSIFY"
                  filterable
                  allow-create
                  default-first-option>
                  <el-option
                    v-for="(item,i) in SECOND_CLASSIFY_OPTION"
                    :key="i"
                    :label="item.name"
                    :value="item.name">
                  </el-option>
                </el-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
              <a-form-item :validate-status="validateStatus1" style="width:100%;margin-bottom:12px;" :label="label.name"
                           :labelCol="{span: 7}" :wrapperCol="{span: 17}" required>
                <span slot="help">{{ validateStatus1=='error'?'请选择数据源':'&nbsp;&nbsp;' }}</span>
                <a-select @select="findatabase" v-model="queryParam.SOURCE_ID" placeholder="请选择数据源" labelInValue>
                  <a-select-option :value="d.id" v-for="d in SOURCE_ID_OPTION" :key="d.id">{{d.name}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
              <a-form-item :validate-status="validateStatus2" style="width:100%;margin-bottom:12px;"
                           :label="label.name1" :labelCol="{span: 7}" :wrapperCol="{span: 17}" required>
                <span slot="help">{{ validateStatus2=='error'?'请选择数据库':'&nbsp;&nbsp;' }}</span>
                <a-select @change="findtablesign" v-model="queryParam.DATABASE_ID" placeholder="请选择数据库" labelInValue>
                  <a-select-option :value="d.id" v-for="d in DATABASE_ID_OPTION" :key="d.id">{{d.name}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="24" :sm="24">
              <a-form-item ref="test" :validate-status="validateStatus3" style="width:100%;margin-bottom:12px;"
                           :label="label.name2" :labelCol="{span: 3}" :wrapperCol="{span: 21}" required>
                <span slot="help">{{ validateStatus3=='error'?'请选择数据表':'&nbsp;&nbsp;' }}</span>
                <a-select @change="findcomments" v-model="model.TABLE_SIGNS" placeholder="请选择数据表" showSearch>
                  <a-select-option :value="d.id" v-for="d in TABLE_SIGN_OPTION" :key="d.id">{{d.name}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="24" :sm="24">
              <a-form-item :validate-status="validateStatus4" style="width:100%;margin-bottom:12px;"
                           :label="label.name3" :labelCol="{span: 3}" :wrapperCol="{span: 21}" required>
                <span slot="help">{{ validateStatus4=='error'?'请输入表描述':'&nbsp;&nbsp;' }}</span>
                <a-input
                  placeholder="请输入表描述"
                  v-model="TABLE_NAME"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="24" :sm="24">
              <a-form-item :validate-status="validateStatus5" style="width:100%;margin-bottom:12px;"
                           :label="label.name4" :labelCol="{span: 3}" :wrapperCol="{span: 21}" required>
                <span slot="help">{{ validateStatus4=='error'?'请输入表用途':'&nbsp;&nbsp;' }}</span>
                <a-input
                  placeholder="请输入表用途"
                  v-model="queryParam.TABLE_WORD"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
              <a-form-item style="width:100%;margin-bottom:12px;" :label="label.name5" :labelCol="{span: 7}"
                           :wrapperCol="{span: 17}" required>
                <a-select v-model="STATE" placeholder="请选择状态" allowClear>
                  <a-select-option :value="d.id" v-for="d in STATE_OPTION" :key="d.id">{{d.label}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
                  <a-form-item style="width:100%;margin-bottom:12px;margin-left: -1%" label="是否跳转"
                               :labelCol="{span: 7}"
                               :wrapperCol="{span: 17}" required>
                      <a-select v-model="queryParam.isJump" placeholder="请选择是否跳转"
                                allowClear>
                          <a-select-option value="0">是</a-select-option>
                          <a-select-option value="1">否</a-select-option>
                      </a-select>
                  </a-form-item>
              </a-col>
              <a-col :md="12" :sm="12" v-if="queryParam.isJump === '0'">
                  <a-form-item :validate-status="validateStatus6" style="width:100%;margin-bottom:12px;margin-left: -1%" label="附件主键"
                               :labelCol="{span: 7}"
                               :wrapperCol="{span: 17}" required>
                               <span slot="help">{{ validateStatus6=='error'?'请选择附件主键':'&nbsp;&nbsp;' }}</span>
                      <a-select v-model="queryParam.FOR_SKIP" placeholder="请选择附件主键"
                                allowClear>
                          <a-select-option :value="d.columnName" v-for="d in termsDataSource" :key="d.columnName">
                              {{d.columnName}}
                          </a-select-option>
                      </a-select>
                  </a-form-item>
              </a-col>
          </a-row>
        </a-form>
        <a-row>
          <span style="padding-left:5px;">字 段 描 述：</span>
        </a-row>
        <a-table
          style="margin-top:10px;"
          :columns="termsColumns"
          size="middle"
          :pagination="false"
          :dataSource="termsDataSource"
          rowKey="columnName"
          bordered
          :loading="loading">
                <span slot="DBTIT" slot-scope="text, record">
                  <a-select style="width:100%;" v-model="record.DBTIT" placeholder="请选择特殊标识" allowClear>
                      <a-select-option :value="d.id" v-for="d in DBTIT_OPTION" :key="d.id">{{d.label}}</a-select-option>
                  </a-select>
               </span>
          <span slot="columnComment" slot-scope="text, record">
                  <a-input v-model="record.columnComment"/>
               </span>
        </a-table>
      </a-spin>
      <a-row :style="{textAlign:'right',marginTop:'10px'}">
        <a-button :style="{marginRight: '8px'}" @click="handleCancel">
          关闭
        </a-button>
        <a-button :style="{marginRight: '8px'}" @click="handleOk" type="primary">确定</a-button>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
  import {
    getDataSourceTree,
    getDataSourceSelection,
    getDataBaseSelection,
    getDataTableSelection,
    getDataTableComments,
    addDataTable,
    getFirstClassifySelection,
    getSecondClassifySelection
  } from '@/api/nationalTreasury'

  export default {
    name: 'DataTableModal',
    data() {
      return {
        drawerWidth: 700,
        visible: false,
        confirmLoading: false,
        queryParam: {},
        model: {},
        validateStatus0: '',
        validateStatus: '',
        validateStatus1: '',
        validateStatus2: '',
        validateStatus3: '',
        validateStatus4: '',
        validateStatus5: '',
        validateStatus6:'',
        label: {
          name: "数" + '\xa0\xa0' + "据" + '\xa0\xa0' + "源",
          name1: "数" + '\xa0\xa0' + "据" + '\xa0\xa0' + "库",
          name2: "数" + '\xa0\xa0' + "据" + '\xa0\xa0' + "表",
          name3: "表" + '\xa0\xa0' + "描" + '\xa0\xa0' + "述",
          name4: "表" + '\xa0\xa0' + "用" + '\xa0\xa0' + "途",
          name5: "状" + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + "态",
        },
        form: this.$form.createForm(this),
        FIRST_CLASSIFY: '',
        STATE: '0',
        termsColumns: [{
          title: '字段名',
          align: 'center',
          key: '1',
          width: '30%',
          dataIndex: 'columnName',
        }, {
          title: '字段描述',
          align: 'center',
          key: '2',
          width: '30%',
          dataIndex: 'columnComment',
          scopedSlots: {customRender: 'columnComment'}
        }, {
          title: '特殊标识',
          align: 'center',
          key: '3',
          width: '30%',
          dataIndex: 'STATE',
          scopedSlots: {customRender: 'DBTIT'}
        }],
        termsDataSource: [],
        loading: false,
        FIRST_CLASSIFY_OPTION: [],
        SOURCE_ID_OPTION: [],
        DATABASE_ID_OPTION: [],
        TABLE_SIGN_OPTION: [],
        STATE_OPTION: [
          {id: '0', label: '启用'},
          {id: '1', label: '停用'}
        ],
        BASE_TYPE: '',
        BASE_ID: '',
        DATA_BASE_ID: '',
        DATA_BASE_NAME: '',
        TABLE_SIGN: '',
        TABLE_NAME: '',
        TABLE_SIGNS: '',
        DBTIT_OPTION: [{
                    id: 'O', label: '文本标识'
                }, {
                    id: 'D', label: '日期标识'
                }, {
                    id: 'T', label: '国库标识'
                }, {
                    id: 'B', label: '核算主体标识'
                }, {
                    id: 'N', label: '数字标识'
                },{
                    id: 'L', label: '长度标识'
                }],
        SECOND_CLASSIFY_OPTION: [],//二级分类下拉值
        PRIMARY_OPTION:[]
      }
    },
    mounted() {
      getDataSourceSelection().then(res => {
        if (res.result === 'success') {
          this.SOURCE_ID_OPTION = res.rows;
        }
      }),
        getFirstClassifySelection().then(res => {
          if (res.result === 'success') {
            this.FIRST_CLASSIFY_OPTION = res.rows;
          }
        })
    },
    methods: {
      add() {
        this.visible = true;
        //this.form.resetFields()
        this.model = {};
        if (this.TABLE_NAME !== '') {
          this.TABLE_NAME = '';
        }
        this.queryParam = {
          isJump:'1'
        };
        this.termsDataSource = [];
      },
      findatabase(value, option) {
        this.queryParam.DATABASE_ID = ''
        //this.form.setFieldsValue({'DATABASE_ID': ''});
        this.model = {};
        this.TABLE_SIGN_OPTION = [];
        this.termsDataSource = [];
        this.form.resetFields()
        console.log(this.form);
        getDataBaseSelection({SOURCE_ID: value.key}).then(res => {
          if (res.result === 'success') {
            this.DATABASE_ID_OPTION = res.rows;
            var rt = /(.+)?(?:\(|（)(.+)(?=\)|）)/.exec(value.label);
            this.BASE_TYPE = rt[2];
            this.BASE_ID = value.key;
          }
        })
      },
      findtablesign(value, option) {
        //this.TABLE_SIGNS = ''
        this.model = {};
        this.termsDataSource = [];
        getDataTableSelection({
          SOURCE_ID: value.key,
          BASE_TYPE: this.BASE_TYPE,
          DATABASE: value.label.replace(/\s+/g, "")
        }).then(res => {
          if (res.result === 'success') {
            this.TABLE_SIGN_OPTION = res.rows;
            this.DATA_BASE_ID = value.key;
            this.DATA_BASE_NAME = value.label;
          }
        })
      },
      findcomments(value, option) {
        //this.queryParam.TABLE_SIGN = value;
        getDataTableComments({
          SOURCE_ID: this.DATA_BASE_ID,
          BASE_TYPE: this.BASE_TYPE,
          DATABASE: this.DATA_BASE_NAME.replace(/\s+/g, ""),
          TABLE_SIGN: value
        }).then(res => {
          if (res.result === 'success') {
            this.termsDataSource = res.rows;
            this.TABLE_SIGN = value;
            this.TABLE_NAME = res.TABLE_NAME
          }
        })
      },
      handleOk() {
        if (!this.queryParam.FIRST_CLASSIFY) {
          this.validateStatus0 = 'error';
          return
        } else {

          this.validateStatus0 = 'success'
        }
        if (!this.queryParam.SECOND_CLASSIFY) {
          this.validateStatus = 'error';
          return
        } else {

          this.validateStatus = 'success'
        }
        if (!this.queryParam.SOURCE_ID) {
          this.validateStatus1 = 'error';
          return
        } else {

          this.validateStatus1 = 'success'
        }
        if (!this.queryParam.DATABASE_ID) {
          this.validateStatus2 = 'error';
          return
        } else {

          this.validateStatus2 = 'success'
        }
        if (!this.model.TABLE_SIGNS) {
          this.validateStatus3 = 'error';
          return
        } else {

          this.validateStatus3 = 'success'
        }
        if (!this.TABLE_NAME) {
          this.validateStatus4 = 'error';
          return
        } else {

          this.validateStatus4 = 'success'
        }
        if (!this.queryParam.TABLE_WORD) {
          this.validateStatus5 = 'error';
          return
        } else {

          this.validateStatus5 = 'success'
        }
        if (this.queryParam.isJump==='0') {
          if(!this.queryParam.FOR_SKIP){
          this.validateStatus6 = 'error';
          return
        } else {

          this.validateStatus6 = 'success'
        }
        }
        let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
        let userId = userInfo.id
        addDataTable({
          SOURCE_ID: this.BASE_ID,
          DATABASE_ID: this.DATA_BASE_ID,
          TABLE_SIGN: this.TABLE_SIGN,
          TABLE_NAME: this.TABLE_NAME,
          TABLE_WORD: this.queryParam.TABLE_WORD,
          COLUMNS: this.termsDataSource,
          FIRST_CLASSIFY: this.queryParam.FIRST_CLASSIFY,
          SECOND_CLASSIFY: this.queryParam.SECOND_CLASSIFY,
          STATE: this.STATE,
          isJump:this.queryParam.isJump,
          FOR_SKIP:this.queryParam.FOR_SKIP,
          userId: userId
        }).then(res => {
          if (res.result === 'success') {
            this.$message.success(res.msg);
            this.visible = false;
            /* getDataSourceSelection({}).then(res=>{
             if(res.result==='success'){
                 this.SOURCE_ID_OPTION = res.rows;
             }
         }) */
            getDataSourceTree({}).then(res => {
              if (res.result === 'success') {
                //this.treeData = res.rows;
                this.$emit('show', res.rows);
              }
            })
          } else {
            this.$message.error(res.msg);
          }
        })
      },
      handleCancel() {
        this.visible = false;
      },
      //一级分类change
      headerChange(value) {
        let that = this;
        getSecondClassifySelection({FIRST_CLASSIFY: value}).then(res => {
          if (res.result === 'success') {
            that.SECOND_CLASSIFY_OPTION = res.rows;
          }
        })
      }
    }
  }
</script>

<style>
  #add1 .ant-col-3 {
    width: 14% !important;
  }

  #add1 .ant-col-21 {
    width: 86% !important;
  }
</style>