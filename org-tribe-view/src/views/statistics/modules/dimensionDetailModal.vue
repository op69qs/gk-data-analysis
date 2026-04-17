<template>
  <a-drawer :title="title" :width="drawerWidth" :maskClosable="false" @close="handleCancel" :visible="visible"
            :confirmLoading="confirmLoading"
            :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}">
    <div :style="{width: '100%',border: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',}">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form" autoComplete="off">
          <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label=" 编 码 ">
            <a-input placeholder="请输入维度明细编码"
                     v-decorator="['code', {rules: [{ required: true, message: '请输入维度明细编码'}]}]"
                     :disabled="disabled"/>
          </a-form-item>
          <div>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名 称">
              <a-input placeholder="" v-decorator="['name', {rules: [{ required: true, message: '请输入维度明细名称'}]}]"
                       autocomplete="new-password"/>
            </a-form-item>
          </div>
        </a-form>
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
  import pick from 'lodash.pick'
  import {
    addSub,
    editSub,
    getSubPage,
  } from '@/api/dimensionTableApi'

  export default {
    name: 'dimensionDetailModal',
    data() {
      return {
        drawerWidth: 700,
        visible: false,
        disabled: false,
        title: "新增",
        main_id: '', // 维度信息编码
        isEdit: false,
        model: {},
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
      }
    },
    methods: {
      loadData() {
        // getSubPage({SOURCE_ID: this.model.ID}).then(res => {
        //   if (res.result === 'success' && res.rows.length > 0) {
        //     this.dataSource = res.rows;
        //   } else {
        //     this.dataSource = [];
        //   }
        // })
      },
      add() {
        console.log(this.main_id)
        this.edit(false, false)
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.disabled = false;
        if (record) {
          this.isEdit = true;
          // this.loadData();
        } else {
          this.isEdit = false;
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'name', 'code'))
        })
      },
      // 确定
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);


            formData.main_id = this.main_id;
            console.log(formData)
            that.submitData(formData);
          } else {
            this.$message.error('请按照规范输入！')
          }
        })
      },
      submitData(formData) {
        const that = this;
        let obj;
        if (this.isEdit) {
          let params = {DBNAME: [], USERNAME: [], PASSWORD: [], STATE: []};

          formData = Object.assign(formData, params);
          obj = editSub(formData);
        } else {
          formData = Object.assign(formData, {});
          obj = addSub(formData);
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
      }
    }
  }
</script>
