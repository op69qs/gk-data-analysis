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
                        {{model.area_no_pid ? '子级':'一级'}}
                    </a-form-item>

                    <a-form-item
                            v-if="model.area_no_pid"
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
                                :treeData="areaData"
                                v-model="model.area_no_pid"
                                :disabled="disableSubmit"
                                @change="handleParentIdChange">
                        </a-tree-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="地区编码">
                        <a-input placeholder="请输入地区编码" v-decorator="[ 'area_no_id',validatorRules.area_no_id]"
                                 :disabled="disableSubmit || editDisable"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="地区名称"
                            hasFeedback>
                        <a-input placeholder="请输入地区名称" v-decorator="[ 'area_dscr', validatorRules.area_dscr]"
                                 :disabled="disableSubmit"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="地区级次"
                            hasFeedback>
                        <a-select labelInValue placeholder="请选择地区级次" v-decorator="[ 'level', validatorRules.level]"
                                  :disabled="disableSubmit">
                            <a-select-option v-for="d in guoKuLevel" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="地域简称"
                            hasFeedback>
                        <a-input placeholder="请输入地域简称" v-decorator="[ 'area_dscr_s', validatorRules.area_dscr_s]"
                                 :disabled="disableSubmit"/>
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
    import {getEnumTypeAll, getOrgTree, getAreaTree, getArea, addArea, editArea} from '@/api/nationalTreasury'
    import pick from 'lodash.pick'


    export default {
        name: "regionModal",
        data() {
            return {
                drawerWidth: 700,
                areaData: [],//国库tree
                bookorgTreeData: [],//地区tree
                guoKuLevel: [],//地区级次
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
                    area_no_id: {rules: [{required: true, message: '请输入地区编码!'}]},
                    area_dscr: {rules: [{required: true, message: '请输入地区名称!'}]},
                    level: {rules: [{required: true, message: '请选择地区级次!'}]},
                    area_dscr_s: {rules: [{required: true, message: '请输入地域简称!'}]},
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
                getArea().then((res) => {
                    if (res.result === 'success') {
                        that.areaData = [];
                        that.areaData = res.rows;
                    }
                });
                //地区级别
                getEnumTypeAll(35).then((res) => {
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
                if (record && record.isleaf) {
                    //上级
                    this.model.area_no_pid = record.area_no_pid;
                    //级次
                    this.model.level = {
                        label: record.level_dscr,
                        value: record.level
                    };
                } else {

                }
                //----------------------------------------------------------------------------------------------

                this.visible = true;
                this.loadTree();
                let fieldsVal = pick(this.model, 'area_no_id', 'area_dscr', 'level', 'area_dscr_s', 'isleaf');
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
                        /*if (!formData.area_no_pid) {
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
                        that.confirmLoading = true;
                        //级别
                        formData.level_dscr = formData.level.label;
                        formData.level = formData.level.key;
                        let obj;
                        if (!this.model.id) {
                            obj = addArea(formData);
                        } else {
                            obj = editArea(formData);
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