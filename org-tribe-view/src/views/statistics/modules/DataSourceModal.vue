<template>
  <a-drawer :title="title" :width="drawerWidth" :maskClosable="false" @close="handleCancel" :visible="visible"
            :confirmLoading="confirmLoading"
            :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}">
    <div :style="{width: '100%',border: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',}">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库类型">
            <a-select showSearch @blur="onBlur" @change="handleTypeChange" placeholder="请选择数据库类型" :filterOption="filterOption"
                      v-decorator="['TYPE', {rules: [{ required: true, message: '请选择数据库类型'}]}]" :disabled="isEdit">
              <a-select-option :value="d.id" v-for="d in dataBaseType" :key="d.id">{{d.name}}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="IP 地 址">
            <a-input @blur="onBlur" placeholder="" v-decorator="['IP', validatorRules.IP]" :disabled="isEdit"/>
          </a-form-item>
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label=" 端 口 ">
            <a-input @blur="onBlur" placeholder="" v-decorator="['PORT', validatorRules.PORT]" :disabled="isEdit"/>
          </a-form-item>
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label=" 名 称 ">
            <a-input placeholder="请输入链接地址的别名"
                     v-decorator="['DATASOURCE_NAME', {rules: [{ required: true, message: '请输入链接地址别名'}]}]"
                     :disabled="disabled"/>
          </a-form-item>

          <div v-if="isEdit">
            <a-table :columns="displayColumns" :pagination="false" :dataSource="dataSource" bordered rowKey="ID">
              <div slot="DBNAME" slot-scope="text, record,index">
                <a-input
                  style="margin: -5px 0"
                  v-model="text"
                  @change="e => handleChange(e.target.value, index, 'DBNAME')"
                />
              </div>
              <div slot="SCHEMA_NAME" slot-scope="text, record,index">
                <a-input
                  style="margin: -5px 0"
                  v-model="text"
                  @change="e => handleChange(e.target.value, index, 'SCHEMA_NAME')"
                />
              </div>
              <div slot="USERNAME" slot-scope="text, record,index">
                <a-input
                  style="margin: -5px 0"
                  v-model="text"
                  @change="e => handleChange(e.target.value, index, 'USERNAME')"
                />
              </div>
              <div slot="PASSWORD" slot-scope="text, record,index">
                <a-input type="password"
                         style="margin: -5px 0"
                         v-model="text"
                         @change="e => handleChange(e.target.value, index, 'PASSWORD')"
                />
              </div>
              <div slot="STATE" slot-scope="text, record,index">
                <j-dict-select-tag :triggerChange="true" v-model="text" placeholder="请选择状态查询" dictCode="STATE"
                                   @change="e => handleChange(e, index, 'STATE')"/>
              </div>
              <div slot="action" slot-scope="text, record,index">
                <a @click="handleTestConnection(record)" type="primary">测试链接</a>
              </div>
              <!--<div slot="action" slot-scope="text, record,index">
              </div>-->
            </a-table>
          </div>

          <div v-else>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用 户 名">
              <a-input placeholder="" v-decorator="['USERNAME', {rules: [{ required: true, message: '请输入用户名'}]}]"
                       autocomplete="new-password"/>
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label=" 密 码 ">
              <a-input type="password" v-decorator="['PASSWORD', {rules: [{ required: true, message: '请输入密码'}]}]"
                       autocomplete="new-password"></a-input>
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数 据 库">
              <a-input placeholder="请输入数据库名"
                       v-decorator="['DBNAME', {rules: [{ required: true, message: '请输入数据库名'}]}]"/>
            </a-form-item>
            <a-form-item v-if="isVastbase" :labelCol="labelCol" :wrapperCol="wrapperCol" label="Schema">
              <a-input placeholder="请输入Schema名称"
                       v-decorator="['SCHEMA_NAME', {rules: [{ required: true, message: '请输入Schema名称'}]}]"/>
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label=" 状 态 ">
              <a-select
                v-decorator="['STATE', {rules: [{ required: true, message: '请选择状态'}],initialValue: status[0].id}]">
                <a-select-option :value="d.id" v-for="d in status" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </div>
        </a-form>
      </a-spin>
      <a-row :style="{textAlign:'right',marginTop:'10px'}">
        <a-button :style="{marginRight: '8px'}" @click="handleCancel">
          关闭
        </a-button>
        <a-button :style="{marginRight: '8px'}" @click="handleOk" type="primary">确定</a-button>
        <a-button @click="handleTestConnection" type="primary" v-if="!isEdit">测试链接</a-button>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
  import pick from 'lodash.pick'
  import {
    getDataBase,
    editDataSource,
    getDataSource,
    addDataSource,
    testConnection,
    getDataSourceName
  } from '@/api/integratedQueryApi'
  import {
    databaseKey,
    duplicateLookup,
    isVastbaseType,
    schemaPayload
  } from './dataSourceSchemaSupport.mjs'

  export default {
    name: 'DataSourceModal',
    props: ['dataBaseType'],
    data() {
      return {
        drawerWidth: 700,
        visible: false,
        disabled: false,
        title: "新增",
        isEdit: false,
        dataSource: [],
        model: {},
        selectedType: '',
        labelCol: {
          xs: {span: 24},
          sm: {span: 6}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        columns: [{
          title: '数据库',
          align: 'center',
          key: '1',
          width: '10%',
          dataIndex: 'DBNAME',
          scopedSlots: {customRender: 'DBNAME'}
        }, {
          title: 'Schema',
          align: 'center',
          key: 'schema',
          width: '10%',
          dataIndex: 'SCHEMA_NAME',
          vastbaseOnly: true,
          scopedSlots: {customRender: 'SCHEMA_NAME'}
        }, {
          title: '用户名',
          align: 'center',
          key: '2',
          width: '10%',
          dataIndex: 'USERNAME',
          scopedSlots: {customRender: 'USERNAME'}
        }, {
          title: '密码',
          align: 'center',
          key: '3',
          width: '10%',
          dataIndex: 'PASSWORD',
          scopedSlots: {customRender: 'PASSWORD'}
        }, {
          title: '状态',
          dataIndex: 'STATE',
          scopedSlots: {customRender: 'STATE'},
          align: 'center',
          width: '10%',
          key: '4'
        }, {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'},
          align: 'center',
          width: '10%',
          key: '5'
        }],
        validatorRules: {
          IP: {
            validateTrigger: "blur",
            rules: [
              {required: true, message: '请输入IP地址'},
              {validator: this.validateIP}
            ]
          },
          PORT: {
            rules: [
              {required: true, message: '请输入端口号'},
              {validator: this.validatePORT}
            ]
          }
        },
        status: [
          {id: '0', name: '启用'},
          {id: '1', name: '禁用'}
        ]
      }
    },
    computed: {
      isVastbase() {
        return isVastbaseType(this.selectedType)
      },
      displayColumns() {
        return this.columns.filter(column => !column.vastbaseOnly || this.isVastbase)
      }
    },
    methods: {
      handleTypeChange(type) {
        this.selectedType = type
      },
      filterOption(input, option) {
        return (
          option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
        );
      },
      validateIP(rule, value, callback) {
        let regIp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        if (!value || regIp.test(value)) {
          callback();
        } else {
          callback("请输入正确的IP地址");
        }
      },
      validatePORT(rule, value, callback) {
        let regPort = /^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/;
        if (!value || regPort.test(value)) {
          callback();
        } else {
          callback("请输入正确的端口号");
        }
      },
      loadData() {
        this.dataSource = [];
        getDataBase({SOURCE_ID: this.model.ID}).then(res => {
          if (res.result === 'success' && res.rows.length > 0) {
            this.dataSource = res.rows;
          } else {
            this.dataSource = [];
          }
        })
      },
      add() {
        this.edit(false, false)
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.selectedType = record ? record.TYPE : '';
        this.visible = true;
        this.disabled = false;
        if (record) {
          this.isEdit = true;
          this.loadData();
        } else {
          this.isEdit = false;
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'TYPE', 'IP', 'PORT', 'DATASOURCE_NAME'))
        })
      },
      onBlur() {
        const that = this;
        if (!that.isEdit) {
          let fieldsValue = this.form.getFieldsValue();
          if (fieldsValue.TYPE != '' && typeof fieldsValue.TYPE != 'undefined' &&
            fieldsValue.IP != '' && typeof fieldsValue.IP != 'undefined' &&
            fieldsValue.PORT != '' && typeof fieldsValue.PORT != 'undefined'
          ) {
            getDataSource({
              TYPE: fieldsValue.TYPE,
              IP: fieldsValue.IP,
              PORT: fieldsValue.PORT
            }).then((res) => {
              if (res.result === 'success' && res.rows.length > 0) {
                that.$nextTick(() => {
                  that.form.setFieldsValue({
                    DATASOURCE_NAME: res.rows[0].DATASOURCE_NAME
                  })
                });
                that.model.ID = res.rows[0].ID;
                that.disabled = true;
              } else {
                that.$nextTick(() => {
                  that.form.setFieldsValue({
                    DATASOURCE_NAME: ''
                  })
                });
                that.model.ID = '';
                that.disabled = false;
              }
            })
          }
        }
      },
      handleChange(value, key, column) {
        let that = this;
        const newData = [...this.dataSource];
        const target = newData.filter((item, index) => index === key)[0];
        if (column === 'STATE' && value === '1') {
          // this.$message.warning('');
          this.$confirm({
            title: '提示',
            content: '禁用数据库同时会影响数据表维护和数据查询页面相关表的显示，是否仍要进行禁用操作？',
            onOk() {
              if (target) {
                target[column] = value;
                that.dataSource = newData;
              }
            },
            onCancel() {
            },
          });
        } else {
          if (target) {
            target[column] = value;
            this.dataSource = newData;
          }
        }
      },
      // 确定
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let createUser = JSON.parse(window.localStorage.pro__Login_Userinfo).value.id;
            let formData = Object.assign(this.model, values);
            if (isVastbaseType(formData.TYPE)) {
              formData.SCHEMA_NAME = schemaPayload(formData.TYPE, formData.SCHEMA_NAME);
            }
            formData.CREATE_USER = createUser;
            if (formData.ID && values.DBNAME && !that.isEdit) {
              //新增校验数据库是否重复
              getDataBase(duplicateLookup(formData.TYPE, formData.ID, values)).then(res => {
                if (res.result === 'success' && res.rows.length > 0) {
                  that.$message.warning('数据库重复！');
                  that.confirmLoading = false;
                } else {
                  that.submitData(formData, {});
                }
              })
            } else if (that.isEdit) {
              //修改时 名称是否重复
              getDataSourceName({
                ID: this.model.ID,
                DATASOURCE_NAME: values.DATASOURCE_NAME
              }).then(res => {
                that.confirmLoading = false;
                if (res.result === 'success' && res.rows.length > 0) {
                  that.$message.success('名称重复！');
                } else {
                  that.submitData(formData);
                }
              })
            } else {
              that.submitData(formData);
            }
          } else {
            this.$message.error('请按照规范输入！')
          }
        })
      },
      submitData(formData) {
        const that = this;
        let obj;
        if (this.isEdit) {
          let params = {DBNAME: [], SCHEMA_NAME: [], USERNAME: [], PASSWORD: [], STATE: []};
          let isEmpty = this.dataSource.some((item, index) => {
            return !item.DBNAME || !item.USERNAME || !item.PASSWORD || item.STATE === '' ||
              (this.isVastbase && !schemaPayload(this.selectedType, item.SCHEMA_NAME));
          });
          if (isEmpty) {
            this.$message.warning('数据不能为空！');
            this.confirmLoading = false;
            return;
          }
          //编辑 判断数据库名是否重复
          if (this.dataSource.length > 1) {
            let hash = {};
            let newData = this.dataSource.reduce((ss, item) => {
              const key = databaseKey(this.selectedType, item);
              hash[key] ? '' : hash[key] = true && ss.push(item);
              return ss;
            }, []);
            if (newData.length < this.dataSource.length) {
              that.$message.warning('数据库重复！');
              that.confirmLoading = false;
              return;
            }
          }
          if (this.dataSource.length > 0)
            this.dataSource.map((item, index) => {
              params.DBNAME.push(item.DBNAME);
              params.SCHEMA_NAME.push(schemaPayload(this.selectedType, item.SCHEMA_NAME));
              params.USERNAME.push(item.USERNAME);
              params.PASSWORD.push(item.PASSWORD);
              params.STATE.push(item.STATE);
            });
          for (var i in params) {
            params[i] = params[i].join(',');
          }
          formData = Object.assign(formData, params);
          obj = editDataSource(formData);
        } else {
          formData = Object.assign(formData, {});
          obj = addDataSource(formData);
        }
        obj.then((res) => {
          if (res.result === 'success') {
            that.$message.success(res.msg);
            that.$emit('ok');
            that.close();
          } else {
            that.confirmLoading = false;
            that.$message.warning(res.msg);
          }
          that.confirmLoading = false;
        }).finally(() => {
          that.confirmLoading = false;
        });
      },
      // 关闭
      handleCancel() {
        this.close()
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      //测试链接
      handleTestConnection(record) {
        let params;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            this.confirmLoading = true;
            params = Object.assign({}, values);
          } else {
            this.$message.error('请按照规范输入！')
          }
        });
        if (this.isEdit) {
          const missingRequired = !record.DBNAME || !record.USERNAME || !record.PASSWORD || record.STATE === '' ||
            (this.isVastbase && !schemaPayload(this.selectedType, record.SCHEMA_NAME));
          if (missingRequired) {
            this.$message.warning('请完整填写信息！');
            this.confirmLoading = false;
            return
          }
          params = Object.assign(params, record)
        }
        testConnection(params).then(res => {
          if (res.result === 'success') {
            this.$message.success(res.msg)
          } else {
            this.$message.error(res.msg)
          }
          this.confirmLoading = false;
        })
      }
    }
  }
</script>
