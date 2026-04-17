<template>
    <a-modal
            :title="title"
            :maskClosable="false"
            :visible="visible"
            @cancel="handleCancel"
            width="50%"
            :footer="null"
    >
        <div style="padding:0 50px;">
            <!--步骤条-->
            <a-steps :current="current" size="small">
                <a-step v-for="item in steps" :key="item.title" :title="item.title"></a-step>
            </a-steps>
            <!--文件上传-->
            <div v-if="current === 0">
                科目年度：
                <el-date-picker v-model="S_BDGSBTVSION" type="year" placeholder="选择年" value-format="yyyy"
                                format="yyyy" style="padding-bottom: 10px"></el-date-picker>
                <br/>
                上传文件：
                <a-upload :file-list="fileList" :remove="handleRemove" :before-upload="beforeUpload">
                    <a-button>
                        <a-icon type="upload"/>
                        选择文件
                    </a-button>
                </a-upload>
            </div>
            <div v-if="current === 1 || current === 2">
                上传文件：
                <a-upload :file-list="fileList" :remove="handleRemove" :before-upload="beforeUpload">
                    <a-button>
                        <a-icon type="upload"/>
                        选择文件
                    </a-button>
                </a-upload>
            </div>
            <!--按钮-->
            <div style="text-align: center;padding-top: 10px;">
                <a-button v-if="current < steps.length - 1" type="primary" @click="next" :loading="confirmLoading"
                          :disabled="fileList.length === 0">
                    下一步
                </a-button>
                <a-button
                        v-if="current === steps.length - 1"
                        type="primary" :loading="confirmLoading"
                        @click="next">
                    完成
                </a-button>
                <a-button v-if="current > 0" style="margin-left: 8px" @click="current--">
                    上一步
                </a-button>
            </div>
        </div>
    </a-modal>
</template>

<script>
    import {subjectImportReadExcel, subjectImportReadExcelStat, subjectImportReadExcelT} from '@/api/nationalTreasury'

    export default {
        name: "budgetAccountImportModal",
        data() {
            return {
                title: '导入',
                visible: false,
                confirmLoading: false,
                model: {},
                current: 0,
                steps: [
                    {
                        title: '上传预算科目'
                    },
                    {
                        title: '上传统计科目',
                    },
                    {
                        title: '上传T科目',
                    },
                ],
                fileList: [],
                S_BDGSBTVSION: ''
            }
        },
        methods: {
            add(record) {
                this.edit(record)
            },
            edit(record) {
                this.visible = true

            },
            handleRemove(file) {
                const index = this.fileList.indexOf(file);
                const newFileList = this.fileList.slice();
                newFileList.splice(index, 1);
                this.fileList = newFileList;
            },
            beforeUpload(file) {
                this.fileList = [file];
                return false;
            },
            next() {
                let obj, formData = new FormData();
                console.log(this.S_BDGSBTVSION);
                if (this.fileList.length === 0) {
                    this.$message.warning('请选择文件！');
                    return
                }
                //文件
                this.fileList.forEach((file, i) => {
                    formData.append('file', file);
                });
                formData.append('S_BDGSBTVSION', this.S_BDGSBTVSION);
                formData.append('ADD_USER', this.$sessionStorage.ls.get('Login_Userinfo').id);
                if (this.current === 0) {
                    obj = subjectImportReadExcel
                } else if (this.current === 1) {
                    obj = subjectImportReadExcelStat
                } else if (this.current === 2) {
                    obj = subjectImportReadExcelT
                }
                this.confirmLoading = true;
                obj(formData).then((res) => {
                    if (res.result === 'success') {
                        this.$message.success(res.msg);
                        if (this.current === 2) {
                            this.$emit('ok');
                            this.handleCancel();
                        } else {
                            this.current++;
                        }
                        this.fileList = [];
                    } else {
                        this.$message.warning(res.msg);
                    }
                    this.confirmLoading = false;
                }).finally(() => {
                });
            },
            handleCancel(e) {
                this.visible = false;
                this.current = 0;
                this.S_BDGSBTVSION = '';
                this.fileList = [];
            }
        }
    }
</script>

<style scoped>
    .ant-steps {
        padding: 0 0 30px 0;
    }
</style>