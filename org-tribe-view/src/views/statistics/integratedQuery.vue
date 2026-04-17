<template>
    <a-spin :spinning="spinning">
        <div id="integratedQuery">
            <transition name="fade">
                <div v-show="advanced">
                    <!--<div style="padding:15px 15px 15px 25px;background: #e6f7ff;color:#1890ff;">
                      查询方式：
                      <a-radio-group :options="inquiryOptions" @change="onChange"
                                     v-model="inquiryMode"></a-radio-group>
                    </div>-->
                    <div class="inquiryBottom" :style="{height:treeHeight}">
                        <div>
                            <a-input-search style="padding:4px 6px 4px 0;" placeholder="请输入数据表关键字查询"
                                            @search="onSearch"></a-input-search>
                        </div>
                        <a-tree
                                labelInValue
                                :multiple="inquiryMode === '多表查询'"
                                @select="onSelect"
                                :selectedKeys="selectedKeys"
                                @expand="onExpand"
                                :expandedKeys="expandedKeys"
                                :autoExpandParent="autoExpandParent"
                                :treeData="treeData"
                                :scroll="{y: 300 }">
                            <template slot="title" slot-scope="record">
              <span v-if="record.title.indexOf(searchValue) > -1" :title="record.dscr">
                {{record.title.substr(0, record.label.indexOf(searchValue))}}
                <span style="color: #f50">{{searchValue}}</span>
                {{record.title.substr(record.label.indexOf(searchValue) + searchValue.length)}}
              </span>
                                <span v-else :title="record.dscr">{{record.label}}</span>
                            </template>
                        </a-tree>
                        <a-button type="primary" @click="onBtnClick('tableRelationshipModal')"
                                  style="position:fixed;bottom: 60px;left: 29%;"
                                  v-if="inquiryMode === '多表查询'">确认
                        </a-button>
                    </div>
                </div>
            </transition>
            <div class="model-middle">
                <!-- <button :disabled="disable" @click="moveRight"><icon-font type="icon-xiangzuo" style="font-size:28px;" /></button> -->
                <!-- <a-button type="primary" icon="arrow-right" @click="moveicon-fontRight" :disabled="disable"></a-button> -->
                <button @click="advanced = !advanced">
                    <icon-font
                            :type="advanced===false?'icon-xiangyoushuangjiantou':'icon-xiangzuoshuangjiantou'"
                            style="font-size:18px;margin-right:5px;"/>
                </button>
            </div>
            <div :style="{width:advanced?'73%':'98%'}">
                <div v-if="tableDesc.length === 0">数据表英文名（中文描述）：数据表用途</div>
                <div v-else>
          <span v-for="(item,index) in tableDesc">
          {{item.table_en}}（{{item.label}}）：{{item.dscr}}<span v-if="inquiryMode === '多表查询'"> 表{{index + 1}}</span><br/>
          </span>
                </div>
                <a-divider style="margin:0 0 15px 0;"/>
                <div>
                    <div>
                        <!--<a-button type="primary" @click="onBtnClick('filterByModal')">筛选条件</a-button>-->
                        <a-button type="primary" @click="onInquire(1)">查询</a-button>
                        <a-button type="primary" @click="onBtnClick('columnFilteringModal')">展示列过滤</a-button>
                        <!--<a-button type="primary" @click="onBtnClick('combinationSortModal')">组合排序</a-button>-->
                        <a-button type="primary" @click="onResetCondition()">重置条件</a-button>
                        <!--<a-button type="primary" @click="onSaveScheme()">保存方案</a-button>-->
                        <!--<a-button type="primary" @click="onViewScheme()">查看方案</a-button>-->
                        <!--<a-button type="primary" @click="onExportExcel()">导出Excel</a-button>-->
                        <a-button type="primary" @click="onClear()">清空</a-button>
                    </div>
                    <div>
                        <img class="charts" @click="onBtnClick('combinationSortModal')"
                             style="margin-left: 8px;width:32px;"
                             src="~@/assets/2.png" alt="组合排序"
                             title="组合排序"/>
                        <img class="charts" @click="onBtnClick('filterByModal')" style="margin-left: 8px;width:32px;"
                             src="~@/assets/s.png" alt="筛选条件"
                             title="筛选条件"/>
                        <img class="charts" @click="onBtnClick('indicatorCalculationModal')"
                             style="margin-left: 8px;width:32px;"
                             src="~@/assets/1.png" alt="指标计算"
                             title="指标计算"/>
                        <img class="charts" @click="onSaveScheme()" style="margin-left: 8px;width:32px;"
                             src="~@/assets/6.png"
                             alt="保存方案"
                             title="保存方案"/>
                        <img class="charts" @click="onViewScheme()" style="margin-left: 8px;width:32px;"
                             src="~@/assets/3.png"
                             alt="查看方案"
                             title="查看方案"/>
                        <img class="charts" @click="download" v-if="roleId=='f6817f48af4fb3af11b9e8bf182f618b'" style="margin-left: 8px;width:32px;" src="~@/assets/5.png"
                             alt="下载"
                             title="下载"/>
                    </div>
                </div>
                <div>
                    <!--<a-table
                      ref="table"
                      size="middle"
                      bordered
                      :rowKey="(record, i) => i"
                      :dataSource="dataSource"
                      :pagination="false"
                      :loading="loading"
                      :scroll="{ x: scrollX,y:300}"
                      @change="handleTableChange"
                      v-loadmore="loadMore">
                      <a-table-column :dataIndex="item.dataIndex" :key="i" :width="item.width" align="center"
                                      v-for="(item,i) in columns">
                        <span slot="title">{{item.title}}<br/>{{item.FIELD_EN}}</span>
                      </a-table-column>
                    </a-table>-->
                    <a-table
                            ref="table"
                            size="middle"
                            bordered
                            :rowKey="(record, i) => i"
                            :dataSource="dataSource"
                            :pagination="ipagination"
                            :loading="loading"
                            :scroll="{ x: scrollX,y:scrollY}"
                            @change="handleTableChange">
                        <a-table-column :key="`${i + 1}`" v-for="(item,i) in columns" :width="item.width"
                                        align="center">
                            <span slot="title">
                                <template
                                        v-if="item.title && item.FIELD_EN">{{item.title}}<br/>{{item.FIELD_EN}}</template>
                                <template v-else>{{item.title}}</template>
                            </span>
                            <div slot-scope="text, record">
                                <a @click="$refs.fileListModal.edit(record,params.table)"
                                   v-if="item.title === '操作'">操作</a>
                                <span v-else>{{record[item.dataIndex.split('.')[0]][item.dataIndex.split('.')[1]]}}</span>
                            </div>
                        </a-table-column>
                    </a-table>
                </div>
                <template v-if="indexShowList">
                    <a-divider style="margin:0 0 15px 0;"/>
                    <a-row>
                        <a-col :span="8" v-for="(item,key) in indexShowList" :key="key">
                            <span>{{key}}</span>：{{item}}
                        </a-col>
                    </a-row>
                </template>
            </div>
        </div>
        <!--指标计算-->
        <indicator-calculation-modal ref="indicatorCalculationModal"
                                     @ok="indexCalculationOk"></indicator-calculation-modal>
        <!--筛选条件-->
        <filter-by-modal ref="filterByModal" @ok="filterByOK" @loadData="onInquire(1)"></filter-by-modal>
        <!--展示列过滤-->
        <column-filtering-modal ref="columnFilteringModal" @ok="columnFilteringOK"></column-filtering-modal>
        <!--组合排序-->
        <combination-sort-modal ref="combinationSortModal" @ok="combinationSortOK"></combination-sort-modal>
        <!--保存方案-->
        <save-scheme-modal ref="saveSchemeModal"></save-scheme-modal>
        <!--查看方案-->
        <view-scheme-modal ref="viewSchemeModal" @ok="viewSchemeOK"></view-scheme-modal>
        <!--表间关系-->
        <table-relationship-modal ref="tableRelationshipModal" @ok="tableRelationshipOK"></table-relationship-modal>
        <!--文件列表-->
        <file-list-modal ref="fileListModal"></file-list-modal>
    </a-spin>
</template>

<script>
    import {
        getTableName,
        getColumn,
        executeSql,
        calculate,
        delRedis,
        getSchemeInfo,
        executeSqlFromFont,
        download
    } from '@/api/integratedQueryApi'
    import filterByModal from './modules/filterByModal'
    import columnFilteringModal from './modules/columnFilteringModal'
    import combinationSortModal from './modules/combinationSortModal'
    import saveSchemeModal from './modules/saveSchemeModal'
    import viewSchemeModal from './modules/viewSchemeModal'
    import tableRelationshipModal from './modules/tableRelationshipModal'
    import indicatorCalculationModal from './modules/indicatorCalculationModal'
    import fileListModal from './modules/fileListModal'

    const getParentKey = (key, tree) => {
        let parentKey;
        for (let i = 0; i < tree.length; i++) {
            const node = tree[i];
            if (node.children) {
                if (node.children.some(item => item.key === key)) {
                    parentKey = node.key;
                } else if (getParentKey(key, node.children)) {
                    parentKey = getParentKey(key, node.children);
                }
            }
        }
        return parentKey;
    };
    export default {
        name: "integratedQuery",
        data() {
            return {
                advanced: true,
                roleId: null,
                aa: '单表查询',
                inquiryOptions: ['单表查询', '多表查询'],//单选选项
                inquiryMode: '单表查询',//查询方式
                inputValue: '',//input value
                treeHeight: (document.body.clientHeight - 201) + 'px',
                /*tree data*/
                treeValue: '',//tree value
                selectedKeys: [],
                treeData: [],
                columns: [],//table 列
                dataSource: [],//table 数据
                scrollX: '',//table x
                scrollY: (document.body.clientHeight - 201 - 15 - 21 - 15 - 32 - 15 - 67 - 25 - 38),
                ipagination: {
                    current: 1,
                    pageSize: 100,
                    pageSizeOptions: ['100', '200', '300'],
                    showTotal: (total, range) => {
                        return range[0] + "-" + range[1] + " 共" + total + "条"
                    },
                    showQuickJumper: true,
                    showSizeChanger: true,
                    total: 0
                },//table 分页字段
                loading: false,//table loading
                treeSelect: [],//tree 选中
                params: {},
                spinning: false,//loading 遮罩
                searchValue: '',//搜索值
                expandedKeys: [],//展开key
                autoExpandParent: true,//是否展开
                dataList: [],//tree list
                tableDesc: [],
                indexShowList: {},
                programmeData: null,//方案数据
                inquireCount: 0,
                programParams: {},//方案参数
                programInfo: null//方案详情

            }
        },
        components: {
            filterByModal,
            columnFilteringModal,
            combinationSortModal,
            saveSchemeModal,
            viewSchemeModal,
            tableRelationshipModal,
            indicatorCalculationModal,
            fileListModal
        },
        mounted() {
            getTableName().then(res => {
                if (res.result === 'success') {
                    this.treeData = res.rows;
                    this.dataList = [];
                    this.generateList(this.treeData);
                } else {
                    this.treeData = [];
                    this.$message.warning('请求异常');
                }
            })
            this.roleId = this.$sessionStorage.ls.get('Login_Userinfo').roleId
          console.log(this.$sessionStorage.ls.get('Login_Userinfo').roleId)
        },
        /*//组件销毁
        destoryed() {
            delRedis({userId: this.$sessionStorage.ls.get('Login_Userinfo').id}).then(res => {
            });
        },*/
        methods: {
            /*查询方式*/
            onChange(e) {
                this.selectedKeys = [];//选中tree 数据清空
                delete this.params.where;//删除过滤条件
                delete this.params.column;//删除所选列
                delete this.params.order;//删除排序
                delete this.params.relation;//删除 表间关系
                /*清空所有模态框数据*/
                this.$refs['filterByModal'].listData = [];
                this.$refs['columnFilteringModal'].listData = [];
                this.$refs['combinationSortModal'].listData = [];
                this.$refs['tableRelationshipModal'].listData = [];
            },
            /*查询*/
            onInquire(arg) {
                if (this.selectedKeys.length === 0) {
                    this.$message.warning('请选择数据表！');
                    return
                }
                this.spinning = true;
                ++this.inquireCount;
                let params = new FormData();
                for (let x in this.params) {
                    params.append(x, this.params[x])
                }
                //加载数据 若传入参数1则加载第一页的内容
                if (arg === 1) {
                    this.ipagination.current = 1;
                    this.dataSource = [];
                    this.ipagination.total = 0;
                }
                params.append('userId', this.$sessionStorage.ls.get('Login_Userinfo').id);
                params.append('pageNo', this.ipagination.current);
                params.append('pageSize', this.ipagination.pageSize);
                params.append('guokuId',  this.$sessionStorage.ls.get('Login_Userinfo').guokuId);
                executeSql(params).then(res => {
                    if (res.result === 'success') {
                        // this.dataSource = this.dataSource.concat(res.rows);
                        this.dataSource = res.rows;
                        this.ipagination.total = res.total;
                        //重置指标计算
                        this.indicatorCalculationReset();
                    } else {
                        this.$message.error(res.msg);
                    }
                    this.indexShowList = {};
                    this.spinning = false;
                }).catch(err => {
                    this.spinning = false;
                    this.indexShowList = {};
                })
            },
            /* 指标计算重置 */
            indicatorCalculationReset() {
                delete this.programParams.IS_COUNT;
                delete this.programParams.tableName;
                delete this.programParams.COUNT_TYPE;
                delete this.programParams.COUNT_COLUMN;
                delete this.programParams.COLUMN_CN;
                delete this.programParams.INDEX_NAME;
                delete this.programParams.userId;
                delete this.programParams.INDEX_VALUE;
                delete this.programInfo.index;
                this.indexShowList = {};
                this.$refs.indicatorCalculationModal.listData = [];
                this.$refs.indicatorCalculationModal.dataSource = [{
                    COUNT_TYPE: '合计值',
                    INDEX_NAME: [],
                    COUNT_COLUMN: []
                }];
            },
            //滚动加载
            loadMore() {
                if (!this.spinning) {
                    if (this.dataSource.length >= this.ipagination.total && this.dataSource.length > 0) return;
                    this.ipagination.current++;
                    // 方式 多次加载
                    this.spinning = true;
                    this.onInquire();
                }
            },
            /*重置条件*/
            onResetCondition() {
                this.spinning = true;
                let refs = this.$refs,
                    indicatorCalculationModal = refs.indicatorCalculationModal,//指标计算
                    filterByModal = refs.filterByModal,//筛选条件
                    columnFilteringModal = refs.columnFilteringModal,//展示列过滤
                    combinationSortModal = refs.combinationSortModal,//组合排序
                    tableRelationshipModal = refs.tableRelationshipModal;//表间关系
                //指标计算
                indicatorCalculationModal.dataSource = [{COUNT_TYPE: '合计值', INDEX_NAME: [], COUNT_COLUMN: []}];
                this.indexShowList = {};
                //筛选条件
                if (filterByModal.listData.length > 0)
                    filterByModal.listData.map(item => {
                        item.dataSource = [];
                    });
                filterByModal.IS_COUNT = 'false';
                filterByModal.TIME_COLUMN = '';
                filterByModal.DIMENSION_ID = '';
                filterByModal.DIMENSION_COLUMN  = '';
                //  展示列过滤
                if (columnFilteringModal.listData.length > 0) {
                    this.columns = [];
                    this.params.column = [];
                    columnFilteringModal.listData.map(item => {
                        item.dataSource = [];
                        item.CheckboxData.map(item1 => {
                            item.dataSource.push(item1.value);
                            this.params.column.push(item1.value)
                        });
                        getColumn({TABLE_SIGN: item.key}).then(res => {
                            if (res.result === 'success') {
                                res.rows.map(item1 => {
                                    if (this.inquiryMode === '多表查询') {
                                        this.columns.push({
                                            title: `表${index + 1}.${item1.FIELD_NAME}`,
                                            dataIndex: item1.FIELD_SIGN.split('▲')[0],
                                            width: 200,
                                            FIELD_EN: item1.FIELD_EN,
                                            align: 'center'
                                        });
                                    } else {
                                        this.columns.push({
                                            title: item1.FIELD_NAME,
                                            dataIndex: item1.FIELD_SIGN.split('▲')[0],
                                            width: 200,
                                            FIELD_EN: item1.FIELD_EN,
                                            align: 'center'
                                        });
                                    }
                                });
                            }
                        });
                    });
                    setTimeout(() => {
                        this.scrollX = this.columns.length * 200;
                    }, 1000);
                    //查询
                    this.onInquire(1);
                }
                //组合排序
                if (combinationSortModal.listData.length > 0)
                    combinationSortModal.listData.map(item => {
                        item.dataSource = [];
                    });
                //表间关系
                if (tableRelationshipModal.ArrayData.length > 0)
                    tableRelationshipModal.ArrayData.map(item => {
                        item.listData = [];
                    });
                for (let key in this.params) {
                    if (key !== 'table') {
                        delete this.params[key]
                    }
                }
                this.programInfo = null;
                this.spinning = false;
                this.dataSource = [];
                this.ipagination.total = 0;
            },
            /*清空*/
            onClear() {
                let refs = this.$refs,
                    indicatorCalculationModal = refs.indicatorCalculationModal,//指标计算
                    filterByModal = refs.filterByModal,//筛选条件
                    columnFilteringModal = refs.columnFilteringModal,//展示列过滤
                    combinationSortModal = refs.combinationSortModal,//组合排序
                    tableRelationshipModal = refs.tableRelationshipModal;//表间关系
                //指标值
                this.indexShowList = {};
                this.programInfo = null;
                this.tableDesc = [];
                //选中数据源
                this.selectedKeys = [];
                //选中数据源数据
                this.treeSelect = [];
                //表头
                this.columns = [];
                //查询参数
                this.params = {};
                //table 数据
                this.dataSource = [];
                this.ipagination.total = 0;
                //指标计算
                indicatorCalculationModal.dataSource = [{COUNT_TYPE: '合计值', INDEX_NAME: [], COUNT_COLUMN: []}];
                indicatorCalculationModal.listData = [];
                //筛选条件
                filterByModal.listData = [];
                filterByModal.oldData = JSON.parse(JSON.stringify({
                    IS_COUNT: "false",
                    TIME_COLUMN: "",
                    listData: [],
                    indicatorDropDown: []
                }));
                // 是否需要计算
                filterByModal.IS_COUNT = 'false';
                // 选择的时间字段
                filterByModal.TIME_COLUMN = '';
                // 选择的维度
                filterByModal.DIMENSION_ID = '';
                // 选择的维度过滤字段
                filterByModal.DIMENSION_COLUMN = '';
              // 指标下拉数据
                filterByModal.indicatorDropDown = [];
                //  展示列过滤
                columnFilteringModal.listData = [];
                //组合排序
                combinationSortModal.listData = [];
                //表间关系
                tableRelationshipModal.ArrayData = [];
                this.programmeData = null;
            },
            /*下载*/
            download() {
                if (this.dataSource.length === 0) {
                    this.$message.warning('无数据！');
                    return
                }
                this.spinning = true;
                let COLUMN = [],
                    COLUMN_CN = [],
                    tableName = [],
                    tableName_en = [];
                this.columns.map(item => {
                    COLUMN.push(item.FIELD_EN);
                    COLUMN_CN.push(item.title);
                });
                this.tableDesc.map(item => {
                    tableName.push(item.table_en);
                    tableName_en.push(item.label);
                });
                let params = Object.assign({}, this.params);
                Object.assign(params, {
                    COLUMN_EN: COLUMN,//英文列名
                    COLUMN_CN: COLUMN_CN,//中文列名
                    tableName: tableName_en,//中文表名
                    tableName_en: tableName,//英文表名
                    userId: this.$sessionStorage.ls.get('Login_Userinfo').id//用户id
                });
                for (let key in params) {
                    if (params[key] instanceof Array) {
                        params[key] = params[key].join(',')
                    }
                }
                download(params, tableName_en.join(','), this.ipagination.total).then(res => {
                    this.spinning = false;
                }).catch(err => {
                    this.spinning = false;
                })

            },
            /*筛选条件/展示列过滤/组合排序/表间关系/指标计算*/
            onBtnClick(refName) {
                let ref = this.$refs[refName],
                    data = ref.listData,
                    programInfo = this.programInfo;
                if (this.treeSelect.length === 0) {
                    this.$message.error('请选择数据表！');
                    return
                } else if (refName === 'tableRelationshipModal') {
                    if (this.treeSelect.length < 2) {
                        this.$message.error('请至少选择两张数据表！');
                        return
                    }
                }
                if (refName === 'indicatorCalculationModal')
                    if (!this.params.IS_COUNT || this.params.IS_COUNT === 'false') {
                        this.$message.warning('请先在筛选条件中勾选指标计算并重新查询！');
                        return
                    } else if ((this.params.IS_COUNT === 'true' && this.dataSource.length === 0) || this.dataSource.length === 0) {
                        this.$message.warning('无数据无法进行指标计算！');
                        return
                    } else {
                        ref.tableName = this.params.table
                    }
                ref.visibleModal = true;
                if (data.length === 0) {
                    this.treeSelect.map((item, index) => {
                        if (refName === 'filterByModal') {//筛选条件
                            data.push({
                                key: item.key,
                                title: item.title,
                                dataSource: programInfo && programInfo.where.length > 0 ? programInfo.where : [],
                                columnList: item.columnList
                            });
                            ref.edit();
                        } else if (refName === 'columnFilteringModal') {//展示列过滤
                            let CheckboxData = [];
                            item.columnList.map(item1 => {
                                CheckboxData.push({
                                    label: item1.FIELD_NAME,
                                    value: item1.FIELD_SIGN.split('▲')[0],
                                    FIELD_EN: item1.FIELD_EN
                                });
                            });
                            data.push({
                                key: item.key,
                                title: item.title,
                                dataSource: programInfo && programInfo.column.length > 0 ? programInfo.column : [],
                                columnList: item.columnList,
                                CheckboxData
                            })
                        } else if (refName === 'combinationSortModal' && ((programInfo && programInfo.column.length > 0) || this.$refs.columnFilteringModal.listData.length > 0)) {//组合排序(执行过列过滤)
                            let columnList = [],
                                dataSource = this.$refs.columnFilteringModal.listData.length > 0 ? this.$refs.columnFilteringModal.listData[index].dataSource : programInfo.column;
                            if (this.treeSelect.length > 1) {
                                item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0])).map(item1 => {
                                    columnList.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                columnList = item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0]));
                            }
                            data.push({
                                key: item.key,
                                title: item.title,
                                dataSource: programInfo && programInfo.order.length > 0 ? programInfo.order : [],
                                columnList: columnList
                            });
                        } else if (refName === 'combinationSortModal' && ((programInfo && programInfo.column.length === 0) || this.$refs.columnFilteringModal.listData.length === 0)) {//组合排序(没有执行过滤列操作)
                            let columnList = [];
                            if (this.treeSelect.length > 1) {
                                item.columnList.map(item1 => {
                                    columnList.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                columnList = item.columnList;
                            }
                            data.push({
                                key: item.key,
                                title: item.title,
                                dataSource: programInfo && programInfo.order.length > 0 ? programInfo.order : [],
                                columnList: columnList
                            });
                        } else if (refName === 'tableRelationshipModal') {//表间关系
                            data.push({
                                key: item.key,
                                title: item.title,
                                dataSource: [],
                                columnList: item.columnList
                            })
                        } else if (refName === 'indicatorCalculationModal' && ((programInfo && programInfo.column.length > 0) || this.$refs.columnFilteringModal.listData.length > 0)) {//指标计算(执行过列过滤)
                            let dataSource = this.$refs.columnFilteringModal.listData.length > 0 ? this.$refs.columnFilteringModal.listData[index].dataSource : programInfo.column;
                            if (this.treeSelect.length > 1) {
                                item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0])).map(item1 => {
                                    ref.listData.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                ref.listData = item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0]));
                            }
                            ref.dataSource = programInfo && programInfo.index && programInfo.index.length > 0 ? programInfo.index : [{
                                COUNT_TYPE: '合计值',
                                INDEX_NAME: [],
                                COUNT_COLUMN: []
                            }];
                        } else if (refName === 'indicatorCalculationModal' && ((programInfo && programInfo.column.length === 0) || this.$refs.columnFilteringModal.listData.length === 0)) {//指标计算(没有执行过滤列操作)
                            if (this.treeSelect.length > 1) {
                                item.columnList.map(item1 => {
                                    ref.listData.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                ref.listData = item.columnList;
                            }
                            ref.dataSource = programInfo && programInfo.index && programInfo.index.length > 0 ? programInfo.index : [{
                                COUNT_TYPE: '合计值',
                                INDEX_NAME: [],
                                COUNT_COLUMN: []
                            }];
                        }
                    });
                    if (refName === 'columnFilteringModal' && !(programInfo && programInfo.column.length !== 0)) {
                        ref.listData.map((item, i) => {
                            ref.onSelectAll(i);
                        })
                    }
                } else {
                    this.treeSelect.map((item, index) => {
                        if (refName === 'combinationSortModal' && ((programInfo && programInfo.column.length > 0) || this.$refs.columnFilteringModal.listData.length > 0)) {//组合排序(执行过列过滤)
                            let columnList = [],
                                dataSource = this.$refs.columnFilteringModal.listData.length > 0 ? this.$refs.columnFilteringModal.listData[index].dataSource : programInfo.column;
                            if (this.treeSelect.length > 1) {
                                item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0])).map(item1 => {
                                    columnList.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                columnList = item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0]));
                            }
                            data[index].columnList = columnList;
                        } else if (refName === 'combinationSortModal' && ((programInfo && programInfo.column.length === 0) || this.$refs.columnFilteringModal.listData.length === 0)) {//组合排序(没有执行过滤列操作)
                            let columnList = [];
                            if (this.treeSelect.length > 1) {
                                item.columnList.map(item1 => {
                                    columnList.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                columnList = item.columnList;
                            }
                            data[index].columnList = columnList;
                        } else if (refName === 'indicatorCalculationModal' && ((programInfo && programInfo.column.length > 0) || this.$refs.columnFilteringModal.listData.length > 0)) {//指标计算(执行过列过滤)
                            let dataSource = this.$refs.columnFilteringModal.listData.length > 0 ? this.$refs.columnFilteringModal.listData[index].dataSource : programInfo.column;
                            if (this.treeSelect.length > 1) {
                                item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0])).map(item1 => {
                                    ref.listData.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                ref.listData = item.columnList.filter(item1 => dataSource.some(ele => ele === item1.FIELD_SIGN.split('▲')[0]));
                            }
                        } else if (refName === 'indicatorCalculationModal' && ((programInfo && programInfo.column.length === 0) || this.$refs.columnFilteringModal.listData.length === 0)) {//指标计算(没有执行过滤列操作)
                            if (this.treeSelect.length > 1) {
                                item.columnList.map(item1 => {
                                    ref.listData.push({
                                        FIELD_NAME: `表${index}.${item1.FIELD_NAME}`,
                                        FIELD_SIGN: item1.FIELD_SIGN
                                    })
                                })
                            } else {
                                ref.listData = item.columnList;
                            }
                        }
                    });
                    if (refName === 'indicatorCalculationModal') {
                        ref.setOptions();
                    }
                }

            },
            /*保存方案*/
            onSaveScheme() {
                const programParamsID = this.programParams.ID;
                if (this.treeSelect.length === 0) {
                    this.$message.error('请选择数据表！');
                    return
                } else if (this.inquireCount === 0) {
                    this.$message.error('请查询后在保存方案！');
                    return
                }

                let TABLE_USE = [],//表中文用途
                    TABLE_DSCR = [];//表中文描述
                for (let key in this.tableDesc) {
                    TABLE_USE.push(this.tableDesc[key].dscr);
                    TABLE_DSCR.push(this.tableDesc[key].label);
                }
                Object.assign(this.programParams, this.params);
                Object.assign(this.programParams, {
                    TABLE_USE: TABLE_USE.join(','),
                    TABLE_DSCR: TABLE_DSCR.join(','),
                    SCHEME_COUNT: this.ipagination.total,
                    ID: programParamsID,
                    DIMENSION_ID:this.params.DIMENSION_ID,
                    DIMENSION_COLUMN:this.params.DIMENSION_COLUMN,
                });
                Object.assign(this.programParams, {INDEX_VALUE: JSON.stringify(this.indexShowList)});
                this.$refs.saveSchemeModal.visibleModal = true;
                this.$refs.saveSchemeModal.programmeData = this.programmeData;
                this.$refs.saveSchemeModal.programParams = this.programParams;
                if (this.programmeData) {
                    this.$refs.saveSchemeModal.SCHEME_NAME = this.programmeData.SCHEME_NAME;
                }
            },
            /*查看方案*/
            onViewScheme() {
                this.$refs.viewSchemeModal.visibleModal = true;
                this.$refs.viewSchemeModal.loadData(1);
            },
            /*tree select*/
            onSelect(selectedKeys, {selectedNodes}) {
                this.onClear();
                if (selectedKeys.length > 5) {
                    this.$message.warning('多表最多关联5张表！');
                    return false
                }
                this.tableDesc = [];
                selectedNodes.map(item => {
                    this.tableDesc.push(item.data.props)
                });
                this.spinning = true;
                this.selectedKeys = selectedKeys;
                this.treeSelect = [];
                this.columns = [];
                this.params.table = selectedKeys.join(',');//表名
                // this.params.column = [];
                if (selectedNodes.length > 0) {
                    selectedKeys.map((item, index) => {
                        getColumn({TABLE_SIGN: item}).then(res => {
                            if (res.result === 'success') {
                                selectedNodes.map(item => {
                                    this.treeSelect.push({
                                        key: item.key,
                                        title: item.data.props.label,
                                        columnList: res.rows
                                    });
                                });
                                res.rows.map(item => {
                                    if (this.inquiryMode === '多表查询') {
                                        this.columns.push({
                                            title: `表${index + 1}.${item.FIELD_NAME}`,
                                            dataIndex: item.FIELD_SIGN.split('▲')[0],
                                            width: 200,
                                            FIELD_EN: item.FIELD_EN,
                                            align: 'center'
                                        });
                                    } else {
                                        this.columns.push({
                                            title: item.FIELD_NAME,
                                            dataIndex: item.FIELD_SIGN.split('▲')[0],
                                            width: 200,
                                            FIELD_EN: item.FIELD_EN,
                                            align: 'center'
                                        });
                                    }
                                    // this.params.column.push(item.FIELD_SIGN.split('▲')[0]);
                                });
                                if (item.split('▲').length === 3 && item.split('▲')[2]) {
                                    this.columns.push({
                                        title: '操作',
                                        width: 200,
                                        dataIndex: 'action'
                                    });
                                }
                                console.log(this.columns)
                                setTimeout(() => {
                                    this.scrollX = this.columns.length * 200;
                                    // this.params.column.join(',');
                                    this.spinning = false;
                                }, 1000)
                            }
                        });
                    });
                } else {
                    this.spinning = false;
                }
            },
            /*筛选条件回调*/
            filterByOK(WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, DATA_TYPE, IS_COUNT, TIME_COLUMN,DIMENSION_ID,DIMENSION_COLUMN) {
                this.params.WHERE_LEFT = WHERE_LEFT;
                this.params.WHERE_MIDDLE = WHERE_MIDDLE;
                this.params.WHERE_RIGHT = WHERE_RIGHT;
                this.params.WHERE_TYPE = WHERE_TYPE;
                this.params.DATA_TYPE = DATA_TYPE;
                this.params.IS_COUNT = IS_COUNT;
                this.params.TIME_COLUMN = TIME_COLUMN;
                this.params.DIMENSION_ID = DIMENSION_ID;
                this.params.DIMENSION_COLUMN = DIMENSION_COLUMN;
                this.onInquire(1);
            },
            /*展示列回调*/
            columnFilteringOK(column, columns, scrollX) {
                this.params.column = column;
                this.columns = columns;
                this.scrollX = scrollX;
                //查询
                this.onInquire(1);
            },
            /*组合排序回调*/
            combinationSortOK(filterData) {
                this.params.order = filterData;
                this.onInquire(1);
            },
            /*指标计算回调*/
            indexCalculationOk(params) {
                let queryParameters = Object.assign({}, {
                    WHERE_LEFT: this.params.WHERE_LEFT,
                    WHERE_MIDDLE: this.params.WHERE_MIDDLE,
                    WHERE_RIGHT: this.params.WHERE_RIGHT,
                    WHERE_TYPE: this.params.WHERE_TYPE,
                    DATA_TYPE: this.params.DATA_TYPE,
                    TIME_COLUMN: this.params.TIME_COLUMN
                });
                /*for (let x in queryParameter) {
                    param.append(x, queryParameter[x])
                }*/

                const promises = params.COUNT_TYPE.map((item, index) => {
                    let param = new FormData(),
                        queryParameter = Object.assign({}, queryParameters, {
                            isCount: params.isCount,
                            tableName: params.tableName,
                            COUNT_TYPE: item,
                            COUNT_COLUMN: params.COUNT_COLUMN[index],
                            COLUMN_CN: params.COLUMN_CN[index],
                            INDEX_NAME: params.INDEX_NAME[index],
                            userId: params.userId
                        });
                    for (let x in queryParameter) {
                        param.append(x, queryParameter[x])
                    }
                    return calculate(param)
                });
                Promise.all(promises).then(posts => {
                    this.$refs.indicatorCalculationModal.confirmLoading = false;
                    Object.assign(this.programParams, params);
                    this.$refs.indicatorCalculationModal.visibleModal = false;
                    let data = {};
                    posts.map(item => {
                        for (let key in item) {
                            item[key].forEach(item1 => {
                                if (item1)
                                    for (let key1 in item1) {
                                        data[key1] = item1[key1]
                                    }
                            })
                        }
                    });
                    this.indexShowList = data;
                }).catch((reason) => {
                    this.$refs.indicatorCalculationModal.confirmLoading = false;
                });

            },
            /*查看方案回调*/
            viewSchemeOK(record) {
                this.onClear();
                let filterByModal = this.$refs.filterByModal;//指标计算
                //方案数据
                this.programmeData = record;
                //查询计数
                this.inquireCount++;
                //遮罩开关
                this.spinning = true;
                //点击方案自动执行查询
                executeSqlFromFont({
                    sql: record.SCHEME_SQL,
                    TABLE_ID: record.TABLE_ID,
                    SCHEME_COUNT: record.SCHEME_COUNT,
                    ID: record.ID,
                    userId: this.$sessionStorage.ls.get('Login_Userinfo').id
                }).then(res => {
                    if (res.result === 'success') {
                        this.dataSource = res.rows;
                        this.ipagination.total = Number(res.total);
                        this.ipagination.current = 1;
                    } else {
                        this.$message.warning(res.msg);
                    }
                }).catch(err => {
                    this.spinning = false;
                });
                //获取方案详情
                getSchemeInfo({
                    SCHEME_ID: record.ID
                }).then(res => {
                    if (res.result === "success") {
                        //指标值
                        this.indexShowList = record.INDEX_VALUE ? JSON.parse(record.INDEX_VALUE) : {};
                        this.tableDesc = [];
                        this.treeSelect = [];
                        this.columns = [];
                        this.selectedKeys = [];
                        let TABLE_DESC = res.table.length === 0 ? [] : res.table[0].TABLE_DESC.split(','),
                            TABLE_USE = res.table.length === 0 ? [] : res.table[0].TABLE_USE.split(','),
                            TABLE_NAME = res.table.length === 0 ? [] : res.table[0].TABLE_NAME.split(','),
                            column = res.column.length === 0 ? [] : res.column[0].COLUMN_NAME.split(','),
                            orderData = res.order.length === 0 ? [] : res.order[0].ORDER_DSCR.split(','),
                            order = [],
                            WHERE_LEFT = res.where.length === 0 ? [] : res.where[0].WHERE_LEFT.split(','),
                            WHERE_RIGHT = res.where.length === 0 ? [] : res.where[0].WHERE_RIGHT.split(','),
                            WHERE_MIDDLE = res.where.length === 0 ? [] : res.where[0].WHERE_MIDDLE.split(','),
                            DATA_TYPE = res.where.length === 0 ? [] : res.where[0].DATA_TYPE.split(','),
                            WHERE_TYPE = res.where.length === 0 ? [] : res.where [0].WHERE_TYPE.split(','),
                            where = [],
                            COUNT_TYPE = res.index.length === 0 ? [] : res.index[0].COUNT_TYPE.split(','),
                            INDEX_NAME = res.index.length === 0 ? [] : res.index[0].INDICATION_NAME.split('▲'),
                            COUNT_COLUMN = res.index.length === 0 ? [] : res.index[0].COUNT_COLUMN.split(','),
                            index = [];
                        //排序
                        if (orderData.length > 0)
                            for (let key in orderData) {
                                let data = orderData[key].split('▲');
                                order.push({
                                    field: data[0],
                                    logic: data[1]
                                })
                            }
                        //过滤
                        if (WHERE_LEFT.length > 0)
                            for (let key in WHERE_LEFT) {
                                if (WHERE_TYPE[key] === 'D') {//日期
                                    where.push({
                                        WHERE_LEFT: WHERE_LEFT[key] + '▲' + WHERE_TYPE[key],
                                        WHERE_MIDDLE: '',
                                        dataStart: WHERE_RIGHT[key].split('▲')[0],
                                        dataEnd: WHERE_RIGHT[key].split('▲')[1],
                                        DATA_TYPE: DATA_TYPE[key]
                                    })
                                } else if (WHERE_TYPE[key] === 'B' || WHERE_TYPE[key] === 'T') {//核算主体
                                    where.push({
                                        WHERE_LEFT: WHERE_LEFT[key] + '▲' + WHERE_TYPE[key],
                                        WHERE_MIDDLE: '',
                                        WHERE_RIGHT: WHERE_RIGHT[key].split('▲'),
                                        DATA_TYPE: DATA_TYPE[key]
                                    })
                                } else {
                                    where.push({
                                        WHERE_LEFT: WHERE_LEFT[key] + '▲' + WHERE_TYPE[key],
                                        WHERE_MIDDLE: WHERE_MIDDLE[key],
                                        WHERE_RIGHT: WHERE_RIGHT[key],
                                        DATA_TYPE: DATA_TYPE[key]
                                    })
                                }
                            }
                        //指标计算
                        if (COUNT_TYPE.length > 0)
                            for (let key in COUNT_TYPE) {
                                index.push({
                                    COUNT_TYPE: COUNT_TYPE[key],
                                    INDEX_NAME: INDEX_NAME[key].replace(/\s+/g, "").split(','),
                                    COUNT_COLUMN: COUNT_COLUMN[key]
                                })
                            }
                        this.programInfo = {
                            column: res.column.length > 0 ? res.column[0].COLUMN_NAME.split(',') : [],
                            where: where,
                            order: order,
                            index: index
                        };
                        let expandedKeys = [];
                        TABLE_NAME.map((item, index) => {
                            //表名数据组装
                            this.tableDesc.push({
                                label: TABLE_DESC[index],
                                dscr: TABLE_USE[index],
                                table_en: item.split('▲')[1]
                            });
                            //树选择数据组装
                            this.selectedKeys.push(item);
                            //设置展开节点
                            [...expandedKeys] = this.dataList.map(item1 => {
                                if (item1.key === item) {
                                    return getParentKey(item1.key, this.treeData);
                                }
                                return null;
                            }).filter((item1, i, self) => item1 && self.indexOf(item1) === i)
                            this.treeData.map(item => {
                                expandedKeys.push(item.key);
                            });
                            //筛选条件
                            // filterByModal
                            //默认数据组装
                            getColumn({TABLE_SIGN: item}).then(res1 => {
                                if (res1.result === 'success') {
                                    this.treeSelect.push({
                                        key: item,
                                        title: TABLE_DESC[index],
                                        columnList: res1.rows
                                    });
                                    res1.rows.map((item1, index1) => {
                                        if (column.length > 0) {
                                            column.map((item2, index2) => {
                                                if (item1.FIELD_SIGN.split('▲')[0] === item2)
                                                    if (TABLE_NAME.length > 1) {
                                                        this.columns.push({
                                                            title: `表${index2 + 1}.${item1.FIELD_NAME}`,
                                                            dataIndex: item1.FIELD_SIGN.split('▲')[0],
                                                            width: 200,
                                                            FIELD_EN: item1.FIELD_EN,
                                                            align: 'center'
                                                        });
                                                    } else {
                                                        this.columns.push({
                                                            title: item1.FIELD_NAME,
                                                            dataIndex: item1.FIELD_SIGN.split('▲')[0],
                                                            width: 200,
                                                            FIELD_EN: item1.FIELD_EN,
                                                            align: 'center'
                                                        });
                                                    }
                                            });
                                        } else {
                                            if (TABLE_NAME.length > 1) {
                                                this.columns.push({
                                                    title: `表${index1 + 1}.${item1.FIELD_NAME}`,
                                                    dataIndex: item1.FIELD_SIGN.split('▲')[0],
                                                    width: 200,
                                                    FIELD_EN: item1.FIELD_EN,
                                                    align: 'center'
                                                });
                                            } else {
                                                this.columns.push({
                                                    title: item1.FIELD_NAME,
                                                    dataIndex: item1.FIELD_SIGN.split('▲')[0],
                                                    width: 200,
                                                    FIELD_EN: item1.FIELD_EN,
                                                    align: 'center'
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                            setTimeout(() => {
                                this.scrollX = this.columns.length * 200;
                                this.spinning = false;
                            }, 1000)
                        });
                        //过滤列
                        if (res.column && res.column.length > 0)
                            this.params.column = res.column[0].COLUMN_NAME;
                        //排序
                        if (res.ORDER_DSCR && res.ORDER_DSCR.length > 0)
                            this.params.ORDER_DSCR = res.order[0].ORDER_DSCR;
                        //是否计指标
                        if (record.IS_COUNT) {
                            this.params.IS_COUNT = record.IS_COUNT;
                            filterByModal.IS_COUNT = this.params.IS_COUNT;
                        }
                        //计算指标的时间
                        if (record.TIME_COLUMN) {
                            this.params.TIME_COLUMN = record.TIME_COLUMN;
                            //设置过滤条件
                            filterByModal.TIME_COLUMN = this.params.TIME_COLUMN;
                        }
                        //表名
                        this.params.table = TABLE_NAME.join(',');
                        //指标
                        Object.assign(this.programParams, res.index[0]);
                        //指标值
                        if (record.INDEX_VALUE)
                            this.programParams.INDEX_VALUE = record.INDEX_VALUE;
                        //指标
                        Object.assign(this.params, res.where[0]);//过滤
                        //方案ID
                        this.programParams.ID = record.ID;
                        //树展示id
                        this.expandedKeys = expandedKeys;
                    } else {
                        this.$message.warning(res.msg);
                    }
                }).catch(err => {

                })
            },
            /*表间关系回调*/
            tableRelationshipOK(relation) {
                // this.params.relation = relation;
            },
            /*table change*/
            handleTableChange(pagination) {
                this.ipagination = pagination;
                this.onInquire();
            },
            /*展开/收起节点时触发*/
            onExpand(expandedKeys) {
                this.expandedKeys = expandedKeys;
                this.autoExpandParent = false;
            },
            /*tree 转化为 list*/
            generateList(data) {
                for (let i = 0; i < data.length; i++) {
                    const node = data[i];
                    const key = node.key;
                    this.dataList.push({key, title: node.title});
                    if (node.children) {
                        this.generateList(node.children, node.key);
                    }
                }
            },
            // 树查询
            onSearch(value) {
                let expandedKeys;
                if (value) {
                    expandedKeys = this.dataList.map(item => {
                        if (item.title.indexOf(value) > -1) {
                            return getParentKey(item.key, this.treeData);
                        }
                        return null;
                    }).filter((item, i, self) => item && self.indexOf(item) === i);
                    this.treeData.map(item => {
                        expandedKeys.push(item.key);
                    })
                } else {
                    expandedKeys = []
                }
                Object.assign(this, {
                    expandedKeys,
                    searchValue: value,
                    autoExpandParent: true,
                });
            }
        }
    }
</script>

<style scoped>
    #integratedQuery {
        display: flex;
        background: #fff;
    }

    #integratedQuery > div {
        display: inline-block;
        /*vertical-align: top;*/
    }

    #integratedQuery > div:nth-child(1) {
        width: 25%;
        border-right: 1px solid #e4e4e4;
    }

    #integratedQuery > div:nth-child(3) {
        width: 73%;
        padding: 15px;
    }

    #integratedQuery > div:nth-child(3) > div:nth-child(1),
    #integratedQuery > div:nth-child(3) > div:nth-child(3) {
        padding-bottom: 15px;
    }

    #integratedQuery > div:nth-child(3) > div:nth-child(3) {
        display: flex;
        justify-content: space-between;
    }

    .inquiryBottom {
        padding: 15px;
        /*border: 1px solid #e4e4e4;*/
        border-right: 0;
    }

    .inquiryBottom > div:nth-child(1) {
        padding-bottom: 5px
    }

    .ant-tree {
        max-height: 547px;
        overflow-y: auto;
    }

    .ant-btn {
        margin-right: 8px;
    }

    .ant-radio-group >>> span.ant-radio + * {
        color: rgb(24, 144, 255) !important;
    }

    .ant-table-wrapper >>> .ant-spin-nested-loading.ant-table td {
        white-space: nowrap;
    }

    .ant-table-wrapper >>> .ant-spin-nested-loading .ant-table-tbody td {
        word-break: break-all;
    }

    li.ant-tree-treenode-disabled > span:not(.ant-tree-switcher),
    li.ant-tree-treenode-disabled > .ant-tree-node-content-wrapper,
    li.ant-tree-treenode-disabled > .ant-tree-node-content-wrapper span {
        color: black;
    }

    /*图标样式*/
    .charts {
        font-size: 28px;
        margin-right: 10px;
        cursor: pointer;
    }

    .ant-col-8 {
        line-height: 30px;


    }

    /*.ant-col-8 span {
      display: inline-block;
      width: 40%;
      text-align: right;
    }*/
    .model-middle {
        /* display: flex;
  flex-direction: column;
  align-items: flex-end; */
        float: left;
        margin-top: 240px;
        margin-left: -1px;
        z-index: 100;
    }

    .model-middle button {
        margin-top: 116px;
        border-left: #000 !important;
        background: #fff;
        border: 1px solid #ccc;
        padding-top: 10px;
        padding-left: 0px;
        padding-bottom: 10px;
        cursor: pointer;
    }

    /*动画效果*/
    .fade-enter-active {
        animation: bounce-in .5s;
    }

    .fade-leave-active {
        animation: bounce-in .5s reverse;
    }

    @keyframes bounce-in {
        0% {
            opacity: 0;
        }
        10% {
            opacity: 0.1;
        }
        20% {
            opacity: 0.2;
        }
        30% {
            opacity: 0.3;
        }
        40% {
            opacity: 0.4;
        }
        50% {
            opacity: 0.5;
        }
        60% {
            opacity: 0.6;
        }
        70% {
            opacity: 0.7;
        }
        80% {
            opacity: 0.8;
        }
        90% {
            opacity: 0.9;
        }
        100% {
            opacity: 1;
        }
    }
</style>
