<template>
    <!--指标计算-->
    <a-modal
            title="指标计算"
            :maskClosable="false"
            v-model="visibleModal"
            width="70%"
            @ok="handleOk"
            @cancel="handleCancel"
            :confirmLoading="confirmLoading">
        <a-spin :spinning="confirmLoading">
            <a-button type="primary" @click="handleAdd">新增</a-button>
            <a-card v-for="(item,i) in dataSource" :key="i" :title="null" style="margin-top: 15px;line-height: 45px;">
                <a-button shape="circle" icon="close" class="close-button" v-if="i !== 0"
                          @click="dataSource.splice(i,1)"/>
                选择用来计算&nbsp;
                <a-select style="width: 150px" v-model="item.COUNT_TYPE" @change="handleChange(item)">
                    <a-select-option :value="item1.value" v-for="item1 in COUNT_TYPE_OPTIONS" :key="item1.value">
                        {{item1.name}}
                    </a-select-option>
                </a-select>&nbsp;
                的金额或数量字段：
                <a-select style="width: 260px" :disabled="item.COUNT_TYPE === '计数'" v-model="item.COUNT_COLUMN"
                          :maxTagCount="1" showSearch :filter-option="filterOption">
                    <a-select-option :value="i.FIELD_EN" v-for="i in listData" :key="i.value">
                        {{i.FIELD_NAME}}
                    </a-select-option>
                </a-select>
                <br/>选择&nbsp;&nbsp;<span style="font-weight: bold;color: black;">{{item.COUNT_TYPE}}</span>
                &nbsp;&nbsp;的计算指标
                <br/>
                <a-checkbox-group v-model="item.INDEX_NAME">
                    <a-row>
                        <a-col :span="8">
                            <a-checkbox value="上期值">上期值</a-checkbox>
                        </a-col>
                        <a-col :span="8">
                            <a-checkbox value="环比增量">环比增量</a-checkbox>
                        </a-col>
                        <a-col :span="8">
                            <a-checkbox value="环比增速">环比增速</a-checkbox>
                        </a-col>
                    </a-row>
                    <a-row>
                        <a-col :span="8">
                            <a-checkbox value="去年同期值">去年同期值</a-checkbox>
                        </a-col>
                        <a-col :span="8">
                            <a-checkbox value="同比增量">同比增量</a-checkbox>
                        </a-col>
                        <a-col :span="8">
                            <a-checkbox value="同比增速">同比增速</a-checkbox>
                        </a-col>
                    </a-row>
                </a-checkbox-group>
            </a-card>
        </a-spin>
    </a-modal>
</template>

<script>
    import {calculate} from '@/api/integratedQueryApi'

    export default {
        name: "indicatorCalculationModal",
        data() {
            return {
                confirmLoading: false,
                visibleModal: false,
                dataSource: [{COUNT_TYPE: '合计值', INDEX_NAME: [], COUNT_COLUMN: []}],
                listData: [],
                COUNT_TYPE_OPTIONS: [{value: '合计值', name: '合计值'},
                    {value: '均值', name: '均值'},
                    {value: '计数', name: '计数（count）'}],
                tableName: '',
                oldData: [{COUNT_TYPE: '合计值', INDEX_NAME: [], COUNT_COLUMN: []}]
            }
        },
        methods: {
            setOptions() {
                this.COUNT_TYPE_OPTIONS.map(item1 => {
                    item1.disabled = false;
                });
                this.dataSource.map(item => {
                    this.COUNT_TYPE_OPTIONS.map(item1 => {
                        if (item.COUNT_TYPE && item.COUNT_TYPE === item1.value) {
                            item1.disabled = true;
                        }
                    })
                });
            },
            handleAdd() {
                /*if (this.dataSource.length === 3) {
                  this.$message.warning('新增已到上限！');
                  return;
                }*/
                this.setOptions();
                this.dataSource.push({
                    COUNT_TYPE: this.COUNT_TYPE_OPTIONS.filter(item => item.disabled === false)[0].value,
                    INDEX_NAME: [],
                    COUNT_COLUMN: []
                })
            },
            //确认
            handleOk() {
                let showList = [],
                    COUNT_TYPE = [],//计算类型
                    COUNT_COLUMN = [],// 需要计算的列
                    COLUMN_CN = [],//需要计算列的中文名称
                    INDEX_NAME = [];//需要计算你的指标
                this.oldData = JSON.parse(JSON.stringify(this.dataSource));
                for (let key in this.dataSource) {
                    if (this.dataSource[key].COUNT_TYPE === '计数') {
                        /*if (this.dataSource[key].INDEX_NAME.length === 0) {
                          this.$message.warning('请选择计算指标！');
                          return
                        }*/
                        // } else if (this.dataSource[key].COUNT_COLUMN.length === 0 || this.dataSource[key].INDEX_NAME.length === 0) {
                    } else if (this.dataSource[key].COUNT_COLUMN.length === 0) {
                        this.$message.warning('请选择字段/计算指标！');
                        return
                    }
                }
                this.dataSource.map(item => {
                    COUNT_TYPE.push(item.COUNT_TYPE);
                    COUNT_COLUMN.push(item.COUNT_COLUMN);
                    let cn = [];
                    // item.COUNT_COLUMN.map(item1 => {
                    if (item.COUNT_TYPE !== "计数")
                        cn.push(this.listData.filter(item2 => item2.FIELD_EN === item.COUNT_COLUMN)[0].FIELD_NAME);
                    // });
                    COLUMN_CN.push(cn.join(','));
                    INDEX_NAME.push(item.INDEX_NAME.length === 0 ? ' ' : item.INDEX_NAME.join(','));
                });
                this.confirmLoading = true;
                this.$emit('ok', {
                    isCount: 'true',
                    tableName: this.tableName,
                    COUNT_TYPE: COUNT_TYPE,
                    COUNT_COLUMN: COUNT_COLUMN,
                    COLUMN_CN: COLUMN_CN,
                    INDEX_NAME: INDEX_NAME,
                    userId: this.$sessionStorage.ls.get('Login_Userinfo').id
                });
            },
            //取消
            handleCancel() {
                console.log(this.oldData)
                this.visibleModal = false;
                this.dataSource = JSON.parse(JSON.stringify(this.oldData));
            },
            handleChange(item) {
                if (item.COUNT_TYPE === '计数') {
                    item.COUNT_COLUMN = []
                }
                this.setOptions();
            },
            filterOption(input, option) {
                return (
                    option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
                );
            }
        }
    }
</script>

<style scoped lang="less">
    .ant-checkbox-group {
        display: block;
    }

    .ant-card {
        position: relative;

        .close-button {
            position: absolute;
            right: -11px;
            top: -6%;
        }
    }
</style>