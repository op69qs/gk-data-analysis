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
                saveRequest: 0,
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
                this.saveRequest++;
                this.confirmLoading = false;
                this.inputHtml = record.type === 'h' ? record.content || '' : '';
                this.form.resetFields();
                this.model = Object.assign({}, record);
                this.visible = true;
                this.$nextTick(() => {
                    if(this.model.type == 'h'){
                        this.inputHtml = this.model.content;
                    }
                    this.form.setFieldsValue(pick(this.model, 'title'))
                });

            },
            close() {
                this.saveRequest++;
                this.confirmLoading = false;
                this.$emit('close');
                this.visible = false;
            },
            handleOk() {
                if (this.confirmLoading) return;
                const request = this.saveRequest;
                // 触发表单验证
                return this.form.validateFields(async (err, values) => {
                    if (!err && !this.confirmLoading && request === this.saveRequest) {
                        if(this.model.type == 'h' && this.inputHtml == ''){
                            this.$message.warning("内容不能为空");
                            return
                        }
                        let formData = Object.assign({},values);
                        formData.id = this.model.id;
                        if(this.model.type === 'h') {
                            formData.content = this.inputHtml;
                        }
                        this.confirmLoading = true;
                        try {
                            const res = await editGallery(formData);
                            if (request !== this.saveRequest) return;
                            if (res && res.result === 'success') {
                                this.$message.success(res.msg);
                                this.$emit('ok');
                                this.close();
                            } else {
                                this.$message.warning((res && res.msg) || '图库保存失败，请重试');
                            }
                        } catch (error) {
                            if (request === this.saveRequest) this.$message.error('图库保存失败，请重试');
                        } finally {
                            if (request === this.saveRequest) this.confirmLoading = false;
                        }
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
