<template>
    <!--文件列表-->
    <a-modal title="文件列表"
             :maskClosable="false"
             v-model="visible"
             width="60%"
             @cancel="handleCancel"
             :confirmLoading="confirmLoading">
        <div>
            <a-table
                    ref="table"
                    size="middle"
                    bordered
                    :rowKey="(record, i) => i"
                    :dataSource="dataSource"
                    :pagination="false"
                    :columns="[{
                     title: '文件名',
                     dataIndex: 'FILE_NAME',
                     width:'85%'
                    },{
                    title: '操作',
                    dataIndex: 'action',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                     width:'15%'
                    }]"
                    :loading="confirmLoading">
                <template slot="action" slot-scope="text, record">
                    <a @click="$refs.annexPreview.edit(record)">查看</a>
                    <a-divider type="vertical"/>
                    <a @click="handleDownload(record)">下载</a>
                </template>
            </a-table>
        </div>
        <!--附件预览-->
        <annex-preview ref="annexPreview"></annex-preview>
        <template slot="footer">
            <a-button @click="handleCancel">取消</a-button>
        </template>
    </a-modal>
</template>

<script>
    import {skip} from '@/api/integratedQueryApi'
    import annexPreview from './annexPreview'//附件预览
    export default {
        name: "fileListModal",
        components: {annexPreview},
        data() {
            return {
                visible: false,
                confirmLoading: false,
                dataSource: []
            }
        },
        methods: {
            edit(record, tableName) {
                this.visible = true;
                let name = tableName.split('▲')[1], key = tableName.split('▲')[2], params = {};
                params[key] = record[name][key];
                params.table = name
                params.tableName = tableName
                this.dataSource = [];
                skip(params).then(res => {
                    if (res.result === 'success') {
                        this.dataSource = res.rows;
                    }
                })
            },
            handleCancel() {
                this.visible = false
            },
            //预览
            handleDownload(record) {
                window.open(`${window._CONFIG['domianURL']}/seo/forSkip/downFile?path=${record.PATH}&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${this.$sessionStorage.ls.get('Login_Userinfo').id}`)
            }
        }
    }
</script>

<style scoped>

</style>