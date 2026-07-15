<template>
    <a-modal
            :title="title"
            :width="800"
            :visible="visible"
            :confirmLoading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭"
            wrapClassName="ant-modal-cust-warp"
            style="top:5%;height: 85%;overflow-y: hidden">

        <a-spin :spinning="confirmLoading">
            <a-form :form="form">

                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="类型名称">
                    <a-input placeholder="请输入角色名称" v-decorator.trim="[ 'business_name', validatorRules.business_name]"/>
                </a-form-item>

                <a-form-item
                        v-if="model.id"
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="类型编码">
                    <a-input placeholder="请输入角色编码" :disabled="roleDisabled"
                             v-decorator.trim="[ 'business_id']"/>
                </a-form-item>

                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="状态">
                    <j-dict-select-tag v-decorator="['business_state', validatorRules.business_state]"
                                       placeholder="请选择状态"
                                       :type="'radio'" :triggerChange="true" dictCode="enable_status"/>
                </a-form-item>

            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import pick from 'lodash.pick'
    import { addBusinessType, editBusinessType } from '@/api/visScreen'

    export default {
        name: "BusinessTypeListModal",
        data() {
            return {
                title: "操作",
                visible: false,
                roleDisabled: false,
                model: {},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16},
                },
                confirmLoading: false,
                form: this.$form.createForm(this),
                validatorRules: {
                    business_name: {
                        rules: [
                            {required: true, message: '请输入角色名称!'},
                            {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
                        ]
                    },
                    business_state: {
                        initialValue: '0',
                        rules: [
                            {required: true, message: '请选择状态!'}
                        ]
                    }
                },
            }
        },
        computed: {
            userInfo() {
                return this.$store.getters.userInfo
            }
        },
        methods: {
            add() {
                this.edit({});
            },
            edit(record) {
                this.form.resetFields();
                this.model = Object.assign({}, record);
                this.visible = true;

                //编辑页面禁止修改角色编码
                if (this.model.id) {
                    this.roleDisabled = true;
                } else {
                    this.roleDisabled = false;
                }
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'business_name', 'business_state'))
                });

            },
            close() {
                this.$emit('close');
                this.visible = false;
            },
            handleOk() {
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        that.confirmLoading = true;
                        let formData = Object.assign(this.model, values);
                        let obj;
                        if (!this.model.business_id) {
                            formData.add_user = this.userInfo.id;
                            obj = addBusinessType(formData);
                        } else {
                            obj = editBusinessType(formData);
                        }
                        obj.then((res) => {
                            if (res.result === 'success') {
                                that.$message.success(res.msg);
                                that.$emit('ok');
                            } else {
                                that.$message.warning(res.msg);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                            that.close();
                        })
                        this.handleCancel();
                    }
                })
            },
            handleCancel() {
                this.close()
            }

        }
    }
</script>

<style scoped>

</style>
