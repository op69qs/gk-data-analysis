<template>
    <!--附件预览-->
    <a-modal :title="title " v-model="visible" width="50%" :maskClosable="false" @cancel="handleCancel">
        <a-spin :spinning="spinning">
            <div
                    style="text-align: center"
                    v-if="!htmlData && !imgData &&!pdfData&&!spinning"
            >{{!htmlData && !imgData &&!pdfData&&!spinning?'':'无预览文件'}}
            </div>
            <div
                    v-if="htmlData"
                    class="htmlPreview"
                    v-html="htmlData"
                    style="max-height: 500px;overflow-y: auto;overflow-x: hidden;padding:15px;"
            ></div>
            <img
                    v-else-if="imgData"
                    :src="downFile + '?path='+imgData +'&X-Access-Token=' + this.$sessionStorage.ls.get('Access-Token')"
                    style="max-width: 100%;"
            />
            <iframe frameborder="0"
                    v-else-if="pdfData"
                    width="100%"
                    height="490"
                    :src="pdfFile + '?path='+pdfData +'&X-Access-Token=' + this.$sessionStorage.ls.get('Access-Token')"
            ></iframe>
        </a-spin>
        <template slot="footer">
            <a-button type="primary" @click="handleFileDown()">下载</a-button>
        </template>
    </a-modal>
</template>

<script>
    import {viewDoc} from '@/api/integratedQueryApi'

    export default {
        name: 'annexPreview',
        data() {
            return {
                title: '预览',
                visible: false,
                htmlData: '',
                imgData: '',
                pdfData: '',
                path: '',
                pdfFile: `${window._CONFIG['domianURL']}/seo/forSkip/viewPdf`,
                spinning: false,
                downFile: `${window._CONFIG['domianURL']}/seo/forSkip/downFile`,
                model:{}
            }
        },
        methods: {
            edit(record) {
                this.spinning = true;
                this.visible = true;
                this.model = record;
                let fileType = record.PATH.split('.')[1];
                if (fileType === 'doc' || fileType === 'docx' || fileType === 'xls' || fileType === 'xlsx') {
                    viewDoc({ATTA_ID: record.ATTA_ID,PATH:record.PATH,S_INFO_ID:record.S_INFO_ID,downFileName:record.downFileName}).then(res => {
                        this.htmlData = res.result;
                        this.spinning = false;
                    })
                } else if (fileType === 'png' || fileType === 'jpg') {
                    this.imgData = record.PATH;
                    this.spinning = false;
                } else if (fileType === 'pdf') {
                    this.pdfData = record.PATH;
                    this.spinning = false;
                }
            },
            handleFileDown() {
                //window.open(`${window._CONFIG['domianURL']}/seo/forSkip/downFile?&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`)
                window.open(`${window._CONFIG['domianURL']}/seo/forSkip/downFile?path=${this.model.PATH}&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${this.$sessionStorage.ls.get('Login_Userinfo').id}`)
            },
            handleCancel() {
                this.visible = false;
                this.htmlData = '';
                this.imgData = '';
                this.pdfData = '';
                this.model = {}
            }
        }
    }
</script>

<style scoped>
    .ant-spin-spinning {
        display: block;
    }
</style>