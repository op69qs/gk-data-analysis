<template>
    <a-modal
            :title="title"
            width="50%"
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
                    label="标题：">
                    <a-input placeholder="请输入标题" v-decorator.trim="[ 'title', validatorRules.title]" autocomplete="off"/>
                </a-form-item>
                <a-form-item
                    v-if="model.type == 'h'"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="内容：">
                    <div
                       v-html="model.content"
                       contenteditable="true"
                       class="text"
                       id="content"
                       @focus="textColor = '#ff4d4f'"
                       @blur="textColor = '#d9d9d9'"
                       :style="{'borderColor':textColor}"
                       @input="changeText">
                    </div>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import pick from 'lodash.pick'
    import { editGallery } from '@/api/visScreen'
    export default {
        name: "GalleryListModal",
        components: {},
        data() {
            return {
                title: "操作",
                visible: false,
                roleDisabled: false,
                textColor: '#d9d9d9',
                model: {},
                inputHtml: "", // 修改后内容 
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
                    title: {
                        rules: [
                            {required: true, message: '请输入标题!'}
                        ]
                    }
                }
            }
        },
        methods: {
            add() {
                this.edit({});
            },
            edit(record) {
                this.form.resetFields();
                this.model = Object.assign({}, record);
                console.log(this.model)
                this.visible = true;
                this.$nextTick(() => {
                    if(this.model.type == 'h'){
                        this.inputHtml = this.model.content;
                    }
                    this.form.setFieldsValue(pick(this.model, 'title'))
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
                        if(this.model.type == 'h' && this.inputHtml == ''){
                            this.$message.warning("内容不能为空");
                            return
                        }
                        let formData = Object.assign({},values);
                        formData.id = this.model.id;
                        if(this.inputHtml) {
                            this.model.content = this.inputHtml;
                            formData.content = this.inputHtml;
                        }
                        let obj;
                        obj = editGallery(formData)
                        obj.then((res) => {
                            if (res.result=='success') {
                                that.$message.success(res.msg);
                                that.$emit('ok');
                            } else {
                                that.$message.warning(res.msg);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                            that.close();
                        })
                        console.log(formData)
                        this.handleCancel();
                    }
                })
            },
            handleCancel() {
                this.close();
                this.confirmLoading = false;
            },
            changeText (val) {
                this.inputHtml = val.target.innerHTML;
                // console.log(val.target.innerHTML);
            }
        }
    }
</script>

<style scoped>
    .d_w {
        width: 25%;
        display: inline-block;
        margin-right: 20px;
    }

    .d_w > div {
        text-align: center;
    }

    .i_s {
        width: 100%;
    }

    .i_s:last-child {
        margin-right: 0;
    }
    .text {
        width: 100%;
        height: auto;
        min-height: 35vh;
        max-height: 45wh;
        text-align: justify;
        overflow-y: auto;
        outline: none;
        margin: 0 auto;
        padding: 4px 11px;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
        -webkit-user-select:text
    }
    .text:hover {
        border-color: #ff4d4f;
        border-right-width: 1px !important;
    }
</style>
