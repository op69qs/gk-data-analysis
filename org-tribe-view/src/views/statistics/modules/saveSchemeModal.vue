<template>
    <!--保存方案-->
    <a-modal
            :title="null"
            :maskClosable="false"
            v-model="visibleModal"
            width="50%"
            @ok="handleOk"
            :confirmLoading="confirmLoading"
            @cancel="handleCancel"
    >
        <div style="padding-top: 20px;"></div>
        <a-radio-group :options="inquiryOptions"
                       @change="e =>{e.target.value === '保存方案'?SCHEME_NAME = programmeData.SCHEME_NAME:SCHEME_NAME = '' }"
                       v-model="inquiryMode"
                       v-if="programmeData && programmeData.ID"></a-radio-group>
        <div style="display: flex;padding-top:15px;">方案描述：
            <a-textarea :rows="4" style="width: 85%;" v-model="SCHEME_NAME"></a-textarea>
        </div>
    </a-modal>
</template>

<script>
    import {addSchemeMain, editSchemeMain, checkScheme} from '@/api/integratedQueryApi'

    export default {
        name: "saveSchemeModal",
        data() {
            return {
                confirmLoading: false,
                visibleModal: false,
                SCHEME_NAME: '',
                inquiryOptions: ['保存方案', '另存方案'],
                inquiryMode: '保存方案',
                programmeData: null,
                programParams: null
            }
        },
        methods: {
            handleOk() {
                if (!this.SCHEME_NAME) {
                    this.$message.warning('请输入方案描述！');
                    return
                }
                this.confirmLoading = true;
                checkScheme({
                    SCHEME_NAME: this.SCHEME_NAME,
                    ID: this.programmeData ? this.programmeData.ID : ''
                }).then(res => {
                    if (res.result === 'success' && res.rows.length > 0) {
                        this.confirmLoading = false;
                        this.$message.warning('方案描述重复，请重新输入！');
                    } else {
                        let programParams = Object.assign({}, this.programParams),
                            obj;
                        programParams.SCHEME_NAME = this.SCHEME_NAME;
                        if (this.programmeData && this.programmeData.ID && this.inquiryMode === '保存方案') {
                            obj = editSchemeMain;
                        } else {
                            programParams.CREATE_USER = this.$sessionStorage.ls.get('Login_Userinfo').id;
                            obj = addSchemeMain;
                        }
                        let formParams = new FormData();
                        for (let key in programParams) {
                            formParams.append(key, key === 'INDEX_NAME' ? programParams[key].join('▲') : programParams[key]);
                        }
                        console.log(formParams.get('INDEX_NAME'));
                        obj(formParams).then(res1 => {
                            this.confirmLoading = false;
                            if (res1.result === 'success') {
                                this.$message.success(res1.msg);
                                this.handleCancel();
                            } else {
                                this.$message.warning(res1.msg);
                            }
                        }).catch(err => {
                            this.confirmLoading = false;
                        })
                    }
                }).catch(err => {
                    this.confirmLoading = false;
                });

            },
            handleCancel() {
                this.visibleModal = false;
                this.SCHEME_NAME = '';
                this.programmeData = null;
                this.programParams = null;
                this.inquiryMode = '保存方案';
            }
        }
    }
</script>

<style scoped>

</style>