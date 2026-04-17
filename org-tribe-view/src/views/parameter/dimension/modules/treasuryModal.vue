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
                        {{model.guoku_pid ? '子级':'一级'}}
                    </a-form-item>

                    <a-form-item
                            v-if="model.guoku_pid"
                            label="上级"
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            :validate-status="validateStatus"
                            :hasFeedback="true"
                            :required="true">
                        <span slot="help">{{ validateStatus=='error'?'请选择上级':'&nbsp;&nbsp;' }}</span>
                        <a-tree-select
                                style="width:100%"
                                showSearch
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="treeData"
                                v-model="model.guoku_pid"
                                :disabled="disableSubmit"
                                @change="handleParentIdChange">
                        </a-tree-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="国库编码">
                        <a-input placeholder="请输入国库编码" v-decorator="[ 'guoku_id',validatorRules.guoku_id]"
                                 :disabled="disableSubmit || editDisable"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="国库名称"
                            hasFeedback>
                        <a-input placeholder="请输入国库名称" v-decorator="[ 'guoku_dscr', validatorRules.guoku_dscr]"
                                 :readOnly="disableSubmit"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="国库级别"
                            hasFeedback>
                        <a-select labelInValue placeholder="请选择国库级别" v-decorator="[ 'level', validatorRules.level]"
                                  :readOnly="disableSubmit">
                            <a-select-option v-for="d in guoKuLevel" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="国库属性"
                            hasFeedback>
                        <a-select labelInValue placeholder="请选择国库属性"
                                  v-decorator="[ 'guoku_shuxing_id', validatorRules.guoku_shuxing_id]"
                                  :readOnly="disableSubmit">
                            <a-select-option v-for="d in guoKuAttr" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="所属地区"
                            :validate-status="validateStatus1"
                            hasFeedback
                            :required="true">
                        <span slot="help">{{ validateStatus1=='error'?'请选择所属地区':'&nbsp;&nbsp;' }}</span>
                        <a-tree-select
                                labelInValue
                                style="width:100%"
                                showSearch
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="areaTreeData"
                                v-model="model.area_no_id"
                                placeholder="请选择所属地区"
                                :disabled="disableSubmit"
                                @change="handleParentIdChange1">
                        </a-tree-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            :validate-status="validateStatus2"
                            hasFeedback
                            :required="true"
                            label="核算主体">
                        <span slot="help">{{ validateStatus2=='error'?'请选择核算主体':'&nbsp;&nbsp;' }}</span>
                        <a-tree-select
                                labelInValue
                                style="width:100%"
                                showSearch
                                treeNodeFilterProp="label"
                                :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                :treeData="bookorgTreeData"
                                v-model="model.bookorgcode"
                                placeholder="请选择核算主体"
                                :disabled="disableSubmit"
                                @change="handleParentIdChange2">
                        </a-tree-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="支付行号">
                        <a-input placeholder="请输入支付行号" v-decorator="[ 'pay_bnk_no']" :readOnly="disableSubmit"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="支付行号名称">
                        <a-input placeholder="请输入支付行号" v-decorator="[ 'rcvbnk_name']" :readOnly="disableSubmit"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="是否末级"
                            :required="true">
                        <j-dict-select-tag v-decorator="['isleaf', validatorRules.isleaf]" placeholder="请选择"
                                           :type="'radio'"
                                           :triggerChange="true"
                                           dictCode="if" :disabled="disableSubmit"/>

                    </a-form-item>
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
    import {getEnumTypeAll, getOrgTree, getAreaTree, getGuokuTree, addGuoku, editGuoku} from '@/api/nationalTreasury'
    import pick from 'lodash.pick'


    export default {
        name: "treasuryModal",
        data() {
            return {
                drawerWidth: 700,
                treeData: [],//国库tree
                areaTreeData: [],//地区tree
                bookorgTreeData: [],//地区tree
                guoKuLevel: [],//国库级别
                guoKuAttr: [],//国库属性
                treeValue: '0-0-4',
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
                    guoku_id: {rules: [{required: true, message: '请输入菜单标题!'}]},
                    guoku_dscr: {rules: [{required: true, message: '请输入国库名称!'}]},
                    level: {rules: [{required: true, message: '请选择国库级别!'}]},
                    guoku_shuxing_id: {rules: [{required: true, message: '请选择国库属性!'}]},
                    isleaf: {rules: [{required: true, message: '请选择!'}]}
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
                //地区
                getAreaTree().then((res) => {
                    if (res.result === 'success') {
                        that.areaTreeData = [];
                        that.areaTreeData = res.rows;
                    }
                });
                //核算主体
                getOrgTree().then((res) => {
                    if (res.result === 'success') {
                        that.bookorgTreeData = [];
                        that.bookorgTreeData = res.rows;
                    }
                });
                //国库级别
                getEnumTypeAll(8).then((res) => {
                    if (res.result === 'success') {
                        this.guoKuLevel = res.rows;
                    }
                });
                //国库属性
                getEnumTypeAll(9).then((res) => {
                    if (res.result === 'success') {
                        this.guoKuAttr = res.rows;
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
                console.log(record);
                if (record && record.isleaf && record.area_no_id && record.bookorgcode) {
                    this.editDisable = !record.bookorgcode ? false : true;
                    //上级
                    this.model.guoku_pid = record.guoku_pid;
                    //级次
                    this.model.level = {
                        label: record.level_dscr,
                        value: record.level
                    };
                    //属性
                    this.model.guoku_shuxing_id = {
                        label: record.guoku_shuxing_dscr,
                        value: record.guoku_shuxing_id
                    };
                    //地区
                    this.model.area_no_id = {
                        label: record.area_dscr,
                        value: record.area_no_id
                    };
                    //核算主体
                    this.model.bookorgcode = {
                        label: record.bookorgname,
                        value: record.bookorgcode
                    };
                } else {

                }
                //----------------------------------------------------------------------------------------------

                this.visible = true;
                this.loadTree();
                let fieldsVal = pick(this.model, 'guoku_id', 'guoku_dscr', 'level', 'guoku_shuxing_id', 'pay_bnk_no', 'rcvbnk_name', 'isleaf');
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
                        let formData = Object.assign(this.model, values);
                        /*if (!formData.guoku_pid) {
                          that.validateStatus = 'error';
                          that.$message.error("请检查你填的类型以及信息是否正确！");
                          return;
                        } else {
                          that.validateStatus = 'success';
                        }*/
                        if (!formData.area_no_id) {
                            that.validateStatus1 = 'error';
                            that.$message.error("请检查你填的类型以及信息是否正确！");
                            return;
                        } else {
                            that.validateStatus1 = 'success';
                        }
                        if (!formData.bookorgcode) {
                            that.validateStatus2 = 'error';
                            that.$message.error("请检查你填的类型以及信息是否正确！");
                            return;
                        } else {
                            that.validateStatus2 = 'success';
                        }
                        that.confirmLoading = true;
                        //级别
                        formData.level_dscr = formData.level.label;
                        formData.level = formData.level.key;
                        //属性
                        formData.guoku_shuxing_dscr = formData.guoku_shuxing_id.label;
                        formData.guoku_shuxing_id = formData.guoku_shuxing_id.key;
                        //地区
                        formData.area_dscr = formData.area_no_id.label;
                        formData.area_no_id = formData.area_no_id.value;
                        //核算主体
                        formData.bookorgname = formData.bookorgcode.label;
                        formData.bookorgcode = formData.bookorgcode.value;
                        let obj;
                        if (!this.model.id) {
                            obj = addGuoku(formData);
                        } else {
                            obj = editGuoku(formData);
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