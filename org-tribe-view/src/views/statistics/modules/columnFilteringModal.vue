<template>
    <!--展示列过滤-->
    <a-modal
            title="展示列过滤"
            :maskClosable="false"
            v-model="visibleModal"
            width="70%"
            @ok="handleOk"
            @cancel="handleCancel"
            :confirmLoading="confirmLoading"
    >
        <a-collapse accordion activeKey="0">
            <a-collapse-panel :header="item.title" :key="i" v-for="(item,i) in listData">
                <div style="padding-bottom: 10px;">
                    <a-button type="primary" @click="onSelectAll(i)">全选</a-button>
                    <a-button @click="onClear(i)">清空</a-button>
                </div>
                <a-checkbox-group :options="item.CheckboxData" v-model="item.dataSource"/>
            </a-collapse-panel>
        </a-collapse>
    </a-modal>
</template>

<script>
    export default {
        name: "columnFilteringModal",
        data() {
            return {
                confirmLoading: false,
                visibleModal: false,
                value1: [],
                listData: [],
                oldData: []

            }
        },
        methods: {
            /*全选*/
            onSelectAll(index) {
                this.listData[index].dataSource = [];
                this.listData[index].CheckboxData.map(item => {
                    this.listData[index].dataSource.push(item.value);
                });
            },
            /*清空*/
            onClear(index) {
                this.listData[index].dataSource = [];
            },
            /*确认*/
            handleOk() {
                let columns = [], column = [];
                this.oldData = JSON.parse(JSON.stringify(this.listData));
                this.listData.map(item => {
                    item.CheckboxData.map(item1 => {
                        item.dataSource.map(item2 => {
                            if (item1.value === item2) {
                                columns.push({
                                    title: item1.label,
                                    FIELD_EN: item1.FIELD_EN,
                                    dataIndex: item1.value,
                                    width: 200,
                                    align: 'center'
                                });
                                column.push(item1.value)
                            }
                        })
                    })
                });
                this.visibleModal = false;
                this.$emit('ok', column, columns, columns.length * 200);
            },
            /*取消*/
            handleCancel() {
                this.visibleModal = false;
                this.listData = JSON.parse(JSON.stringify(this.oldData));
            }
        }
    }
</script>

<style scoped>
    .ant-checkbox-group >>> .ant-checkbox-group-item {
        width: 18% !important;
        vertical-align: text-top !important;
    }

    .ant-btn {
        margin-right: 8px;
    }
</style>