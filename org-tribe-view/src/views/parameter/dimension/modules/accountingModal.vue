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

                    <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        {{model.bookorgcodepid ? '子级':'一级'}}
                    </a-form-item>

                    <a-form-item
                            v-show="model.bookorgcodepid"
                            label="上级核算主体"
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            :validate-status="validateStatus"
                            hasFeedback
                            required>
                        <span slot="help">{{ validateStatus=='error'?'请选择上级':'&nbsp;&nbsp;' }}</span>
                        <a-tree-select
                                style="width:100%"
                                showSearch
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="treeData"
                                v-model="model.bookorgcodepid"
                                placeholder="请选择父级"
                                :disabled="disableSubmit"
                                @change="handleParentIdChange">
                        </a-tree-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="核算主体编码"
                            hasFeedback>
                        <a-input placeholder="请输入核算主体编码" v-decorator="[ 'bookorgcode',validatorRules.bookorgcode]"
                                 :disabled="disableSubmit || editDisable"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="核算主体名称"
                            hasFeedback>
                        <a-input placeholder="请输入核算主体名称" v-decorator="[ 'bookorgname', validatorRules.bookorgname]"
                                 :disabled="disableSubmit"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            :validate-status="validateStatus1"
                            hasFeedback
                            required
                            label="管辖国库名称">
                        <a-tree-select
                                style="width:100%"
                                showSearch
                                labelInValue
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="guoKuTreeData"
                                v-model="model.guoku_dscr"
                                placeholder="请选择管辖国库名称"
                                :disabled="disableSubmit"
                                @change="handleGuokuDscrChange">
                        </a-tree-select>
                    </a-form-item>

                    <!--<a-form-item
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                      :validate-status="validateStatus2"
                      hasFeedback
                      required
                      label="管辖国库代码">
                      <a-tree-select
                        style="width:100%"
                        showSearch
                        treeNodeFilterProp="label"
                        :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                        :treeData="guoKuTreeData"
                        v-model="model.guoku_id"
                        placeholder="请选择管辖国库代码"
                        :disabled="disableSubmit"
                        @change="handleGuokuIdChange">
                      </a-tree-select>
                    </a-form-item>-->
                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="是否末级"
                            :required="true">
                        <j-dict-select-tag v-decorator="['isleaf', validatorRules.isleaf]" placeholder="请选择"
                                           :type="'radio'"
                                           :triggerChange="true"
                                           dictCode="	if" :disabled="disableSubmit"/>

                    </a-form-item>
                    <!--<a-form-item
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                      label="核算主体级别"
                      hasFeedback>
                      <a-select placeholder="请选择核算主体级别" v-decorator="[ 'name', validatorRules.name]" :disabled="disableSubmit">
                        <a-select-option value="jack">Jack</a-select-option>
                        <a-select-option value="lucy">Lucy</a-select-option>
                        <a-select-option value="disabled" disabled>Disabled</a-select-option>
                        <a-select-option value="Yiminghe">yiminghe</a-select-option>
                      </a-select>
                    </a-form-item>-->

                </a-form>
            </a-spin>
            <a-row :style="{textAlign:'right'}">
                <a-button :style="{marginRight: '8px'}" @click="handleCancel">
                    关闭
                </a-button>
                <a-button :disabled="disableSubmit" @click="handleOk" type="primary">确定</a-button>
            </a-row>
        </div>
    </a-drawer>
</template>

<script>
    import {getOrgTree, getGuokuTree, addBookOrg, editBookOrg} from '@/api/nationalTreasury'
    import pick from 'lodash.pick'


    export default {
        name: 'accountingModal',
        data() {
            return {
                drawerWidth: 700,
                treeData: [],
                guoKuTreeData: [],
                title: '操作',
                visible: false,
                disableSubmit: false,
                editDisable: false,
                model: {},
                localMenuType: 0,
                isRequrie: true,  // 是否需要验证
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
                validateStatus: '',//上级核算主体
                validateStatus1: '',//国库名称
                validateStatus2: ''//国库代码
            }
        },
        computed: {
            validatorRules: function () {
                return {
                    bookorgcode: {rules: [{required: true, message: '请输入核算主体编码!'}]},
                    bookorgname: {rules: [{required: true, message: '请输入核算主体名称!'}]},
                    isleaf: {rules: [{required: true, message: '请选择!'}]}
                }
            }
        },
        created() {
            this.loadTree()
        },
        methods: {
            loadTree() {
                var that = this
                //核算主体
                getOrgTree().then((res) => {
                    if (res.result === 'success') {
                        that.treeData = []
                        that.treeData = res.rows
                    }
                })
                //国库
                getGuokuTree().then((res) => {
                    if (res.result === 'success') {
                        that.guoKuTreeData = []
                        that.guoKuTreeData = res.rows
                    }
                })
            },
            add() {
                // 默认值
                this.edit()
            },
            edit(record) {
                let that = this
                this.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
                this.form.resetFields()
                that.model = Object.assign({}, record)
                //--------------------------------------------------------------------------------------------------
                //根据菜单类型，动态展示页面字段
                console.log(record)
                if (record) {
                    this.editDisable = !record.bookorgcode ? false : true
                    // this.model.isleaf = record.isleaf ? '0' : '1';
                    if (record.guoku_id) {
                        that.model.guoku_dscr = {
                            value: record.guoku_id,
                            label: record.guoku_dscr
                        }
                    }/* else {
                        console.log(that.guoKuTreeData[0])
                        that.model.guoku_dscr = {
                            value: that.guoKuTreeData[0].key,
                            label: that.guoKuTreeData[0].label
                        }
                    }*/
                }
                //----------------------------------------------------------------------------------------------

                this.visible = true
                let fieldsVal = pick(this.model, 'bookorgcode', 'bookorgname', 'isleaf')
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldsVal)
                })
            },
            close() {
                this.$emit('close');
                this.disableSubmit = false;
                this.visible = false;
                this.model = {};
            },
            handleOk() {
                const that = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        let formData = Object.assign(this.model, values)
                        if (!formData.guoku_dscr.label) {
                            that.validateStatus1 = 'error'
                            that.$message.error('请检查你填的类型以及信息是否正确！')
                            return
                        } else {
                            that.validateStatus1 = 'success'
                        }
                        formData.guoku_id = formData.guoku_dscr.value
                        formData.guoku_dscr = formData.guoku_dscr.label
                        that.confirmLoading = true
                        let obj
                        if (!this.model.id) {
                            obj = addBookOrg(formData)
                        } else {
                            obj = editBookOrg(formData)
                        }
                        obj.then((res) => {
                            if (res.result === 'success') {
                                that.$message.success(res.msg)
                                that.$emit('ok')
                            } else {
                                that.$message.warning(res.msg)
                            }
                        }).finally(() => {
                            that.confirmLoading = false
                            that.close()
                        })
                    }
                })
            },
            handleCancel() {
                this.close()
            },
            validateNumber(rule, value, callback) {
                if (!value || new RegExp(/^[0-9]*[1-9][0-9]*$/).test(value)) {
                    callback()
                } else {
                    callback('请输入正整数!')
                }
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
            handleParentIdChange(value) {
                if (!value) {
                    this.validateStatus = 'error'
                } else {
                    this.validateStatus = 'success'
                }
            },
            handleGuokuDscrChange(value) {
                if (!value) {
                    this.validateStatus1 = 'error'
                } else {
                    this.validateStatus1 = 'success'
                }
            },
            handleGuokuIdChange(value) {
                if (!value) {
                    this.validateStatus2 = 'error'
                } else {
                    this.validateStatus2 = 'success'
                }
            }
        }
    }
</script>

<style scoped>

</style>