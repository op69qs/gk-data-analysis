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
                            :validate-status="validateStatus"
                            required
                            hasFeedback
                            label="年份">
                        <span slot="help">{{ validateStatus==='error'?'请选择年份':'&nbsp;&nbsp;' }}</span>
                        <el-date-picker
                                style="width: 100%"
                                v-model="model.S_BDGSBTVSION"
                                type="year"
                                format="yyyy"
                                value-format="yyyy"
                                :disabled="disableSubmit"
                                placeholder="选择年份">
                        </el-date-picker>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="预算科目代码"
                            required>
                        <a-input placeholder="请输入预算科目代码"
                                 :disabled="disableSubmit"
                                 v-decorator="[ 'SUBJECT_CODE_4', validatorRules.SUBJECT_CODE_4]"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="预算科目名称"
                            required>
                        <a-input placeholder="请输入预算科目名称"
                                 v-decorator="[ 'SUBJECT_DSCR_4', validatorRules.SUBJECT_DSCR_4]"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="预算种类"
                            required>
                        <a-select v-decorator="[ 'BUDGET_TYPE', validatorRules.BUDGET_TYPE]"
                                  placeholder="请选择预算种类">
                            <a-select-option v-for="d in BUDGET_TYPE" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="调拨标志"
                    >
                        <a-select v-decorator="[ 'FLITTING_FLAG', validatorRules.FLITTING_FLAG]"
                                  placeholder="请选择调拨标志">
                            <a-select-option v-for="d in FLITTING_FLAG" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="统计科目代码"
                            required>
                        <a-input placeholder="请输入统计科目代码" v-decorator="[ 'STAT_CODE_4', validatorRules.STAT_CODE_4]"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="T科目分类编码"
                            required>
                        <a-input placeholder="请输入T科目分类编码"
                                 v-decorator="[ 'T_SUBJECT_CODE_3',validatorRules.T_SUBJECT_CODE_3]"/>
                    </a-form-item>

                    <a-form-item
                            :labelCol="labelCol"
                            :wrapperCol="wrapperCol"
                            label="T科目类别"
                            required>
                        <a-select v-decorator="[ 'T_SUBJECT_TYPE', validatorRules.T_SUBJECT_TYPE]"
                                  placeholder="请选择T科目类别">
                            <a-select-option v-for="d in T_SUBJECT_TYPE" :key="d.id">{{d.name}}</a-select-option>
                        </a-select>
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
    import {getEnumTypeAll, subjectImportAdd, subjectImportEdit} from '@/api/nationalTreasury'
    import pick from 'lodash.pick'

    export default {
        name: "budgetAccountModel",
        data() {
            return {
                drawerWidth: 700,
                T_SUBJECT_TYPE: [],//T科目类别
                BUDGET_TYPE: [],//预算种类
                FLITTING_FLAG: [],//调拨标志
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
                    SUBJECT_CODE_4: {rules: [{required: true, message: '请输入预算科目代码!'}]},
                    SUBJECT_DSCR_4: {rules: [{required: true, message: '请输入预算科目名称!'}]},
                    BUDGET_TYPE: {rules: [{required: true, message: '请选择预算种类!'}]},
                    FLITTING_FLAG: {rules: [{required: true, message: '请选择调拨标志!'}]},
                    STAT_CODE_4: {rules: [{required: true, message: '请输入统计科目代码!'}]},
                    T_SUBJECT_CODE_3: {rules: [{required: true, message: '请输入T科目分类编码!'}]},
                    T_SUBJECT_TYPE: {rules: [{required: true, message: '请选择T科目类别!'}]},
                }
            }
        },
        created() {
            this.initDictConfig();
        },
        methods: {
            loadTree() {
                //T科目类别
                getEnumTypeAll(32).then((res) => {
                    if (res.result === 'success') {
                        this.T_SUBJECT_TYPE = res.rows;
                    }
                });
                //预算种类
                getEnumTypeAll(33).then((res) => {
                    if (res.result === 'success') {
                        this.BUDGET_TYPE = res.rows;
                    }
                });
                //调拨标志
                getEnumTypeAll(34).then((res) => {
                    if (res.result === 'success') {
                        this.FLITTING_FLAG = res.rows;
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
                //----------------------------------------------------------------------------------------------

                this.visible = true;
                this.loadTree();
                let fieldsVal = pick(this.model, 'SUBJECT_CODE_4', 'SUBJECT_DSCR_4', 'BUDGET_TYPE', 'FLITTING_FLAG', 'STAT_CODE_4', 'T_SUBJECT_CODE_3', 'T_SUBJECT_TYPE');
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldsVal)
                });
            },
            close() {
                this.$emit('close');
                this.disableSubmit = false;
                this.visible = false;
            },
            handleOk() {
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        let formData = Object.assign(this.model, values);
                        if (!formData.S_BDGSBTVSION) {
                            that.validateStatus = 'error';
                            return;
                        } else {
                            that.validateStatus1 = 'success';
                        }
                        let obj;
                        if (this.model.SUBJECT_CODE_4 && this.model.S_BDGSBTVSION) {
                            obj = subjectImportEdit(formData);
                        } else {
                            formData.ADD_USER = this.$sessionStorage.ls.get('Login_Userinfo').id;
                            obj = subjectImportAdd(formData);
                        }
                        that.confirmLoading = true;
                        obj.then((res) => {
                            that.confirmLoading = false;
                            if (res.result === 'success') {
                                that.$message.success(res.msg);
                                that.$emit('ok');
                                that.handleCancel();
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