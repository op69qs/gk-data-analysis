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
                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="征收机构代码">
                        <a-input placeholder="请输入征收机构代码" v-decorator="[ 'TAX_ORG_ID',validatorRules.TAX_ORG_ID]"
                                 :disabled="disableSubmit || editDisable"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="征收机构名称"
                            hasFeedback>
                        <a-input placeholder="请输入征收机构名称" v-decorator="[ 'TAX_ORG_DSCR', validatorRules.TAX_ORG_DSCR]"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="征收机关类型"
                            hasFeedback>
                        <a-select placeholder="请选择征收机关类型" v-decorator="[ 'TYPE_ID', validatorRules.TYPE_ID]">
                            <a-select-option v-for="d in guoKuLevel" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            :validate-status="validateStatus2"
                            hasFeedback
                            :required="true"
                            label="核算主体">
                        <span slot="help">{{ validateStatus2==='error'?'请选择核算主体':'&nbsp;&nbsp;' }}</span>
                        <a-tree-select
                                style="width:100%"
                                showSearch
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="bookorgTreeData"
                                v-model="model.BOOK_ORG_CODE"
                                placeholder="请选择核算主体"
                                @change="handleParentIdChange2">
                        </a-tree-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            :validate-status="validateStatus"
                            label="国库"
                            hasFeedback
                            required>
                        <span slot="help">{{ validateStatus==='error'?'请选择国库代码':'&nbsp;&nbsp;' }}</span>
                        <a-tree-select
                                style="width:100%"
                                showSearch
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="treeData"
                                v-model="model.TRECODE"
                                @change="handleParentIdChange">
                        </a-tree-select>
                    </a-form-item>
                </a-form>
            </a-spin>
            <a-row :style="{textAlign:'right'}">
                <a-button :style="{marginRight: '8px'}" @click="handleCancel">
                    关闭
                </a-button>
                <a-button @click="handleOk" type="primary">确定</a-button>
            </a-row>
        </div>
    </a-drawer>
</template>

<script>
    import {getEnumTypeAll, getOrgTree, getGuokuTree, levyingBodiesAdd, levyingBodiesEdit} from '@/api/nationalTreasury'
    import pick from 'lodash.pick'


    export default {
        name: "levyingBodiesModal",
        data() {
            return {
                drawerWidth: 700,
                treeData: [],//国库tree
                bookorgTreeData: [],//地区tree
                guoKuLevel: [],//征收机关类型
                title: "操作",
                visible: false,
                disableSubmit: false,
                editDisable: false,
                model: {},
                localMenuType: 0,
                isRequrie: true,  // 是否需要验证
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
                validateStatus: "",//上级
                validateStatus1: "",//地区
                validateStatus2: "",//核算主体
            }
        },
        computed: {
            validatorRules: function () {
                return {
                    TAX_ORG_ID: {rules: [{required: true, message: '请输入征收机构代码!'}]},
                    TAX_ORG_DSCR: {rules: [{required: true, message: '请输入征收机构名称!'}]},
                    TYPE_ID: {rules: [{required: true, message: '请选择征收机关类型!'}]},
                }
            }
        },
        created() {
            this.initDictConfig();
        },
        methods: {
            loadTree() {
                var that = this;
                //国库
                getGuokuTree().then((res) => {
                    if (res.result === 'success') {
                        that.treeData = [];
                        that.treeData = res.rows;
                    }
                });
                //核算主体
                getOrgTree().then((res) => {
                    if (res.result === 'success') {
                        that.bookorgTreeData = [];
                        that.bookorgTreeData = res.rows;
                    }
                });
                //征收机关类型
                getEnumTypeAll(36).then((res) => {
                    if (res.result === 'success') {
                        this.guoKuLevel = res.rows;
                    }
                });
            },
            add() {
                // 默认值
                this.edit();
            },
            edit(record) {
                this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
                this.form.resetFields();
                this.model = Object.assign({}, record);
                //--------------------------------------------------------------------------------------------------
                //根据菜单类型，动态展示页面字段
                //----------------------------------------------------------------------------------------------

                this.visible = true;
                this.loadTree();
                let fieldsVal = pick(this.model, 'TAX_ORG_ID', 'TAX_ORG_DSCR', 'TYPE_ID');
                console.log(fieldsVal)
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldsVal)
                });
            },
            close() {
                this.$emit('close');
                this.disableSubmit = false;
                this.validateStatus = '';
                this.validateStatus1 = '';
                this.validateStatus2 = '';
                this.visible = false;
            },
            handleOk() {
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        let formData = Object.assign({}, this.model, values);
                        //核算主体
                        if (!formData.BOOK_ORG_CODE) {
                            that.validateStatus2 = 'error';
                            return;
                        } else {
                            that.validateStatus2 = 'success';
                        }
                        //国库
                        if (!formData.TRECODE) {
                            that.validateStatus = 'error';
                            return;
                        } else {
                            that.validateStatus = 'success';
                        }
                        that.confirmLoading = true;
                        //级别
                        // formData.level_dscr = formData.TYPE_ID.label;
                        // formData.TYPE_ID = formData.TYPE_ID.key;
                        let obj;
                        if (!this.model.TAX_ORG_ID) {
                            obj = levyingBodiesAdd(formData);
                            formData.STATE = '0'
                        } else {
                            obj = levyingBodiesEdit(formData);
                        }
                        obj.then((res) => {
                            if (res.result === 'success') {
                                that.$message.success(res.msg);
                                that.$emit('ok');
                                this.handleCancel()
                            } else {
                                that.$message.warning(res.msg);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                        });
                    }
                })
            },
            handleCancel() {
                this.close()
            },
            validateNumber(rule, value, callback) {
                if (!value || new RegExp(/^[0-9]*[1-9][0-9]*$/).test(value)) {
                    callback();
                } else {
                    callback("请输入正整数!");
                }
            },
            // 根据屏幕变化,设置抽屉尺寸
            resetScreenSize() {
                let screenWidth = document.body.clientWidth;
                if (screenWidth < 500) {
                    this.drawerWidth = screenWidth;
                } else {
                    this.drawerWidth = 700;
                }
            },
            initDictConfig() {
            },
            handleParentIdChange(value) {
                if (!value) {
                    this.validateStatus = "error"
                } else {
                    this.validateStatus = "success"
                }
            },
            handleParentIdChange1(value) {
                if (!value) {
                    this.validateStatus1 = "error"
                } else {
                    this.validateStatus1 = "success"
                }
            },
            handleParentIdChange2(value) {
                if (!value) {
                    this.validateStatus2 = "error"
                } else {
                    this.validateStatus2 = "success"
                }
            }
        }
    }
</script>

<style scoped>

</style>