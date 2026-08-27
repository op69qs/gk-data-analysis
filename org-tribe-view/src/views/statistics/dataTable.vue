<template>
    <a-row :gutter="10" style="margin-right: 0;">
        <a-col :md="9" :sm="24">
            <a-card id="add3" :bordered="false">
                <div style="background: #fff;padding-left:16px;min-height: 500px; margin-top: 5px">
                    <a-input-search style="padding:4px 6px 4px 0;" placeholder="请输入数据表关键字查询"
                                    v-model="queryParam.filterText"
                                    @change="getDatas"></a-input-search>
                    <a-row style="margin:10px 0;">
                        <a-button @click="handleAdd" type="primary">新增</a-button>
                        <a-button style="margin-left: 8px;" :disabled="!TABLE_ID || !isInfo" @click="handleEdit">修改</a-button>
                    </a-row>
                    <!-- tableTree-->
                    <a-col :sm="24" style="height: 500px;overflow-y: auto;">
                        <el-tree
                                id="filter-tree"
                                :data="treeData"
                                @node-click="handleNode"
                                ref="tree"
                                node-key="id"
                        >
                  <span class="span-ellipsis" style="disaplay:inline-block;width:100%;" slot-scope="{ node, data }">
                    <span :title="node.label.split('▲')[0]"
                          style="display:inline-block;width:82%;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">{{ node.label.split('▲')[0]}}</span>
                    <span v-if="node.label.split('▲')[1]==='启用'"
                          style="disaplay:inline-block;position:absolute;right:0px;">{{ node.label.split('▲')[1]}}</span>
                    <span v-if="node.label.split('▲')[1]==='停用'"
                          style="disaplay:inline-block;position:absolute;right:0px;color:red;">{{ node.label.split('▲')[1]}}</span>
                  </span>
                        </el-tree>
                    </a-col>
                </div>
            </a-card>
        </a-col>
        <a-col :md="15" :sm="24" style="background:#fff;height: 645px;overflow-y: auto;">
            <a-card :bordered="false">
                <div id="add2" v-if="isInfo" style="min-height: 500px;line-height:30px;">
                    <a-row style="margin-bottom: 12px;" v-if="status">
                        <a-col :span="24" style="text-align: right;">
                            <a-button type="primary" @click="handleEdit">编辑</a-button>
                        </a-col>
                    </a-row>
                    <a-row style="margin-bottom: 12px;" v-if="status1">
                        <a-col :span="24" style="text-align: right;">
                            <a-button style="margin-right: 8px;" @click="back">取消</a-button>
                            <a-button type="primary" @click="handleSubmit">保存</a-button>
                        </a-col>
                    </a-row>
                    <a-form layout="inline" :key="formRenderKey">
                        <a-row :gutter="24">
                            <a-col :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;" label="一级分类" :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <a-select :disabled="formReadonly" v-model="queryParam.FIRST_CLASSIFY"
                                              placeholder="请选择一级分类"
                                              @change="headerChange">
                                        <a-select-option :value="d.id" v-for="d in FIRST_CLASSIFY_OPTION" :key="d.id">
                                            {{d.name}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;" label="二级分类" :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <el-select
                                            :disabled="formReadonly"
                                            style="width: 100%;"
                                            clearable
                                            v-model="queryParam.SECOND_CLASSIFY"
                                            filterable
                                            allow-create
                                            default-first-option value="">
                                        <el-option
                                                v-for="(item,i) in SECOND_CLASSIFY_OPTION"
                                                :key="i"
                                                :label="item.name"
                                                :value="item.name">
                                        </el-option>
                                    </el-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;" :label="label.name"
                                             :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <a-select :disabled="true" @select="findatabase" v-model="queryParam.SOURCE_ID"
                                              placeholder="请选择数据源"
                                              labelInValue>
                                        <a-select-option :value="d.id" v-for="d in SOURCE_ID_OPTION" :key="d.id">
                                            {{d.name}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;" :label="label.name1"
                                             :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <a-select :disabled="true" @select="findtablesign" v-model="queryParam.DATABASE_ID"
                                              placeholder="请选择数据库"
                                              labelInValue>
                                        <a-select-option :value="d.id" v-for="d in DATABASE_ID_OPTION" :key="d.id">
                                            {{ databaseOptionLabel(BASE_TYPE, d) }}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col v-if="isVastbase" :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;" label="Schema"
                                             :labelCol="{span: 7}" :wrapperCol="{span: 17}" required>
                                    <a-input v-model="SCHEMA_NAME" placeholder="由数据源维护配置" :disabled="true"/>
                                </a-form-item>
                            </a-col>
                            <a-col :md="24" :sm="24">
                                <a-form-item style="width:100%;margin-bottom:12px;margin-left: 1.3%"
                                             :label="label.name2"
                                             :labelCol="{span: 3}"
                                             :wrapperCol="{span: 20}" required>
                                    <a-select :disabled="true" @select="findcomments" v-model="queryParam.TABLE_SIGN"
                                              placeholder="请选择数据表"
                                              showSearch>
                                        <a-select-option :value="d.id" v-for="d in TABLE_SIGN_OPTION" :key="d.id">
                                            {{d.name}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="24" :sm="24">
                                <a-form-item style="width:100%;margin-bottom:12px;margin-left: 1.3%"
                                             :label="label.name3"
                                             :labelCol="{span: 3}"
                                             :wrapperCol="{span: 21}" required>
                                    <a-input
                                            :disabled="formReadonly"
                                            placeholder="请输入表描述"
                                            v-model="TABLE_NAME"
                                    ></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="24" :sm="24">
                                <a-form-item style="width:100%;margin-bottom:12px;margin-left: 1.3%"
                                             :label="label.name4"
                                             :labelCol="{span: 3}"
                                             :wrapperCol="{span: 21}" required>
                                    <a-input
                                            :disabled="formReadonly"
                                            placeholder="请输入表用途"
                                            v-model="queryParam.TABLE_WORD"
                                    ></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;margin-left: -1%" :label="label.name5"
                                             :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <a-select :disabled="formReadonly" v-model="queryParam.STATE" placeholder="请选择状态"
                                              allowClear>
                                        <a-select-option :value="d.id" v-for="d in STATE_OPTION" :key="d.id">
                                            {{d.label}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="12" :sm="12">
                                <a-form-item style="width:100%;margin-bottom:12px;margin-left: -1%" label="是否跳转"
                                             :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <a-select :disabled="formReadonly" v-model="isJump" placeholder="请选择是否跳转"
                                              allowClear>
                                        <a-select-option value="0">是</a-select-option>
                                        <a-select-option value="1">否</a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="12" :sm="12" v-if="isJump === '0'">
                                <a-form-item style="width:100%;margin-bottom:12px;margin-left: -1%" label="附件主键"
                                             :labelCol="{span: 7}"
                                             :wrapperCol="{span: 17}" required>
                                    <a-select :disabled="formReadonly" v-model="queryParam.FOR_SKIP" placeholder="请选择附件主键"
                                              allowClear>
                                       <a-select-option :value="d.columnName" v-for="d in termsDataSource" :key="d.columnName">
                                           {{d.columnName}}
                                       </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                        </a-row>
                    </a-form>
                    <a-row>
                        <span style="padding-left:5px;">字 段 描 述：</span>
                    </a-row>
                    <a-table

                            style="margin-top:10px;"
                            :columns="termsColumns"
                            size="middle"
                            :pagination="false"
                            :dataSource="termsDataSource"
                            :rowKey="record => record.columnName"
                            bordered
                            :loading="loading">

               <span slot="DBTIT" slot-scope="text, record">
                  <a-select @select="e => handleChange(e,record.columnName,'DBTIT')" :disabled="formReadonly"
                            style="width:100%;" v-model="record.DBTIT" placeholder="请选择特殊标识" allowClear>
                      <a-select-option :value="d.id" v-for="d in DBTIT_OPTION" :key="d.id">{{d.label}}</a-select-option>
                  </a-select>
               </span>
                        <span slot="columnComment" slot-scope="text, record, index">
                  <a-input :disabled="formReadonly" :value="text"
                           @change="e => handleChange(e.target.value,record.columnName,'columnComment')"/>
               </span>
                    </a-table>
                </div>
                <div v-else style="text-align: center;padding:100px; color:#bfbfbf;">
                    <img src="~@/assets/noData.png" width="200"/>
                    <br/>
                    请点击左侧最末级数据表节点查看相关设置，再点击「修改」进行编辑
                </div>
                <data-table-modal ref="DataTableModal" @show="show"></data-table-modal>
            </a-card>
        </a-col>
    </a-row>
</template>

<script>
    import {
        getDataSourceTree,
        getDataTableData,
        getDataSourceSelection,
        getDataBaseSelection,
        getDataTableSelection,
        getDataTableComments,
        editDataTable,
        getFirstClassifySelection,
        getSecondClassifySelection
    } from '@/api/nationalTreasury'
    import DataTableModal from './modules/DataTableModal'
    import {
        databaseOptionLabel,
        databaseTreeLabel,
        dataSourceType,
        isVastbaseType,
        schemaForDatabaseSelection
    } from './modules/dataSourceSchemaSupport.mjs'

    export default {
        name: "dataTable",
        components: {
            DataTableModal
        },
        data() {
            return {
                queryParam: {},
                label: {
                    name: "数" + '\xa0\xa0' + "据" + '\xa0\xa0' + "源",
                    name1: "数" + '\xa0\xa0' + "据" + '\xa0\xa0' + "库",
                    name2: "数" + '\xa0\xa0' + "据" + '\xa0\xa0' + "表",
                    name3: "表" + '\xa0\xa0' + "描" + '\xa0\xa0' + "述",
                    name4: "表" + '\xa0\xa0' + "用" + '\xa0\xa0' + "途",
                    name5: "状" + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + "态",
                },
                FIRST_CLASSIFY: '国库',
                termsColumns: [{
                    title: '字段名',
                    align: 'center',
                    key: '1',
                    width: '30%',
                    dataIndex: 'columnName',
                }, {
                    title: '字段描述',
                    align: 'center',
                    key: '2',
                    width: '30%',
                    dataIndex: 'columnComment',
                    scopedSlots: {customRender: 'columnComment'}
                }, {
                    title: '特殊标识',
                    align: 'center',
                    key: '3',
                    width: '30%',
                    dataIndex: 'STATE',
                    scopedSlots: {customRender: 'DBTIT'}
                }],
                termsDataSource: [],
                treeData: [],
                expandedRowKeys: [],
                loading: false,
                // true=只读；点「修改」后为 false。定位类字段另用 :disabled="true" 永久锁定
                formReadonly: true,
                formRenderKey: 0,
                FIRST_CLASSIFY_OPTION: [],
                SOURCE_ID_OPTION: [],
                DATABASE_ID_OPTION: [],
                TABLE_SIGN_OPTION: [],
                isInfo: false,
                STATE_OPTION: [
                    {id: '0', label: '启用'},
                    {id: '1', label: '停用'}
                ],
                BASE_TYPE: '',
                BASE_ID: '',
                DATA_BASE_ID: '',
                DATA_BASE_NAME: '',
                databaseRequestSourceId: '',
                SCHEMA_NAME: '',
                TABLE_SIGN: '',
                TABLE_NAME: '',
                DBTIT_OPTION: [{
                    id: 'O', label: '文本标识'
                }, {
                    id: 'D', label: '日期标识'
                }, {
                    id: 'T', label: '国库标识'
                }, {
                    id: 'B', label: '核算主体标识'
                }, {
                    id: 'N', label: '数字标识'
                },{
                    id: 'L', label: '长度标识'
                }],
                status: true,
                status1: false,
                TABLE_ID: '',
                isShow: false,
                SECOND_CLASSIFY_OPTION: [],//二级分类下拉值
                PRIMARY_OPTION:[],
                isJump:''
            }
        },
        computed: {
            isVastbase() {
                return isVastbaseType(this.BASE_TYPE)
            }
        },
        created() {
            getDataSourceTree({}).then(res => {
                if (res.result === 'success') {
                    this.treeData = res.rows;
                }
            });
            getDataSourceSelection().then(res => {
                if (res.result === 'success') {
                    this.SOURCE_ID_OPTION = res.rows;
                }
            });
            getFirstClassifySelection().then(res => {
                if (res.result === 'success') {
                    this.FIRST_CLASSIFY_OPTION = res.rows;
                }
            });
        },
        methods: {
            //新增
            handleAdd() {
                let ref = this.$refs.DataTableModal;
                ref.add();
            },
            getDatas() {
                setTimeout(() => {
                    getDataSourceTree({tableName: this.queryParam.filterText}).then(res => {
                        if (res.result === 'success') {
                            this.treeData = res.rows;
                        }
                    })
                }, 1000)
            },
            findatabase(value, option) {
                const sourceId = value.key;
                this.BASE_TYPE = dataSourceType(value);
                this.BASE_ID = sourceId;
                this.databaseRequestSourceId = sourceId;
                this.SCHEMA_NAME = '';
                this.DATABASE_ID_OPTION = [];
                this.TABLE_SIGN_OPTION = [];
                getDataBaseSelection({SOURCE_ID: sourceId}).then(res => {
                    if (this.databaseRequestSourceId !== sourceId) {
                        return
                    }
                    if (res.result === 'success') {
                        this.DATABASE_ID_OPTION = res.rows;
                    }
                })
            },
            findtablesign(value, option) {
                this.SCHEMA_NAME = schemaForDatabaseSelection(this.BASE_TYPE, this.DATABASE_ID_OPTION, value);
                getDataTableSelection({
                    SOURCE_ID: value.key,
                    BASE_TYPE: this.BASE_TYPE,
                    DATABASE: value.label,
                    SCHEMA_NAME: this.SCHEMA_NAME
                }).then(res => {
                    if (res.result === 'success') {
                        this.TABLE_SIGN_OPTION = res.rows;
                        this.DATA_BASE_ID = value.key;
                        this.DATA_BASE_NAME = value.label;
                    }
                })
            },
            findcomments(value, option) {
                getDataTableComments({
                    SOURCE_ID: this.DATA_BASE_ID,
                    BASE_TYPE: this.BASE_TYPE,
                    DATABASE: this.DATA_BASE_NAME,
                    SCHEMA_NAME: this.SCHEMA_NAME,
                    TABLE_SIGN: value
                }).then(res => {
                    if (res.result === 'success') {
                        //this.isShow=true;
                        this.termsDataSource = res.rows;
                        this.TABLE_SIGN = value;
                        this.TABLE_NAME = res.TABLE_NAME
                    }
                })
            },
            show(res) {
                this.treeData = res;
            },
            handleNode(data = {}) {
                if (data.nodeType !== 'table') {
                    return;
                }
                this.loadTableDetail(data.id, false);
            },
            loadTableDetail(tableId, enterEdit) {
                if (!tableId) {
                    this.$message.warning('请先选择左侧数据表');
                    return;
                }
                this.TABLE_ID = tableId;
                this.loading = true;
                getDataTableData({TABLE_ID: tableId}).then(res => {
                        if (this.TABLE_ID !== tableId) {
                            return
                        }
                        this.loading = false;
                        if (res.result === 'success') {
                            if (!res.rows || res.rows.length === 0) {
                                this.$message.warning('未查询到数据表配置');
                                this.isInfo = false;
                                return;
                            }
                            const tableData = res.rows[0];
                            const sourceId = tableData.SOURCE_ID;
                            const databaseId = tableData.DATABASE_ID;
                            const sourceName = tableData.DATASOURCE_NAME;
                            const baseType = dataSourceType(sourceName);
                            this.isInfo = true;
                            this.queryParam = tableData;
                            this.BASE_TYPE = baseType;
                            this.SCHEMA_NAME = tableData.SCHEMA_NAME || '';
                            if(tableData.FOR_SKIP){
                                this.isJump = '0'
                            }else{
                                this.isJump = '1' 
                            }
                            this.queryParam.SOURCE_ID = {
                                value: sourceId,
                                label: sourceName
                            };
                            this.BASE_ID = this.queryParam.SOURCE_ID;
                            //获取二级分类下拉
                            this.headerChange(tableData.FIRST_CLASSIFY);
                            this.queryParam.DATABASE_ID = {
                                value: databaseId,
                                label: databaseTreeLabel(baseType, tableData.DBNAME, tableData.SCHEMA_NAME)
                            };
                            this.DATA_BASE_ID = this.queryParam.DATABASE_ID;
                            this.DATA_BASE_NAME = tableData.DBNAME;
                            this.databaseRequestSourceId = sourceId;
                            this.DATABASE_ID_OPTION = [];
                            this.TABLE_SIGN_OPTION = [];
                            getDataBaseSelection({SOURCE_ID: sourceId}).then(ress => {
                                if (this.TABLE_ID !== tableId || this.databaseRequestSourceId !== sourceId) {
                                    return
                                }
                                if (ress.result === 'success') {
                                    this.DATABASE_ID_OPTION = ress.rows;
                                }
                            });
                            getDataTableSelection({
                                SOURCE_ID: databaseId,
                                BASE_TYPE: baseType,
                                DATABASE: tableData.DBNAME,
                                SCHEMA_NAME: this.SCHEMA_NAME
                            }).then(ress => {
                                if (this.TABLE_ID !== tableId) {
                                    return
                                }
                                if (ress.result === 'success') {
                                    this.TABLE_SIGN_OPTION = ress.rows;
                                }
                            })

                            this.TABLE_NAME = tableData.TABLE_NAME
                            this.termsDataSource = res.columns || [];
                            if (enterEdit) {
                                this.enterEditMode();
                            } else {
                                this.exitEditMode();
                            }
                        } else {
                            this.isInfo = false;
                            this.$message.error(res.msg || '获取数据表信息失败');
                        }
                    }).catch(() => {
                        if (this.TABLE_ID === tableId) {
                            this.loading = false;
                            this.isInfo = false;
                            this.$message.error('获取数据表信息失败');
                        }
                    })
            },
            enterEditMode() {
                this.formReadonly = false;
                this.status = false;
                this.status1 = true;
                this.formRenderKey += 1;
            },
            exitEditMode() {
                this.formReadonly = true;
                this.status = true;
                this.status1 = false;
                this.formRenderKey += 1;
            },
            handleEdit() {
                if (!this.TABLE_ID) {
                    this.$message.warning('请先选择左侧数据表');
                    return;
                }
                if (!this.isInfo) {
                    this.loadTableDetail(this.TABLE_ID, true);
                    return;
                }
                this.enterEditMode();
            },
            back() {
                this.loadTableDetail(this.TABLE_ID, false);
            },
            handleChange(value, id, columnComment) {
                const newData = [...this.termsDataSource];
                const target = newData.filter(item => id === item.columnName)[0];
                if (target) {
                    target[columnComment] = value;
                    this.termsDataSource = newData;
                }
            },
            handleSubmit() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                editDataTable({
                    SOURCE_ID: this.BASE_ID.value,
                    DATABASE_ID: this.DATA_BASE_ID.value,
                    TABLE_SIGN: this.queryParam.TABLE_SIGN,
                    TABLE_NAME: this.TABLE_NAME,
                    TABLE_WORD: this.queryParam.TABLE_WORD,
                    COLUMNS: this.termsDataSource,
                    FIRST_CLASSIFY: this.queryParam.FIRST_CLASSIFY,
                    SECOND_CLASSIFY: this.queryParam.SECOND_CLASSIFY,
                    STATE: this.queryParam.STATE,
                    TABLE_ID: this.TABLE_ID,
                    isJump:this.isJump,
                    FOR_SKIP:this.isJump==='0'?this.queryParam.FOR_SKIP:'',
                    userId: userId
                }).then(res => {
                    if (res.result === 'success') {
                        this.$message.success(res.msg);
                        this.exitEditMode();
                        getDataSourceTree({tableName: this.queryParam.filterText || undefined}).then(treeRes => {
                            if (treeRes.result === 'success') {
                                this.treeData = treeRes.rows;
                            }
                        });
                    } else {
                        this.$message.error(res.msg);
                    }
                })
            },
            //一级分类change
            headerChange(value) {
                getSecondClassifySelection({FIRST_CLASSIFY: value}).then(res => {
                    if (res.result === 'success') {
                        this.SECOND_CLASSIFY_OPTION = res.rows;
                    }
                })
            },
            databaseOptionLabel,
            databaseTreeLabel
        }
    }
</script>

<style scoped>
    /* 避免强制改写栅格宽度导致表单项互相遮挡、看起来像 disabled */
    #add3 .ant-card-body {
        padding: 12px !important;
    }
</style>
