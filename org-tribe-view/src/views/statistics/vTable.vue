<template>
    <div class="container">
        <section class="tableTwo">
            <div @mouseleave="moveTableOutside">
                <el-table :header-cell-style="{background:'#fafafa',color:'#606266'}"
                          class="drag_table"
                          id="drag_table"
                          :data="tableDatas"
                          border
                          @header-contextmenu="colClick"
                          style="width: 100%"
                          :max-height="tableMaxHeight"
                          row-key="id"
                          @sort-change="sortChange"
                          v-if="show"
                >
                    <el-table-column
                            sortable="custom"
                            prop="ACCOUNT_PERIOD"
                            label="账期"
                            align="center"
                            class-name="cannotDrag"
                            v-if="level==='1'||level==='2'||level==='3'"
                    ></el-table-column>
                    <el-table-column sortable="custom" class-name="cannotDrag" prop="GK" label="国库"
                                     header-align="center" align="left" v-if="level==='1'"></el-table-column>
                    <el-table-column sortable="custom" class-name="cannotDrag" prop="GK" label="地区"
                                     header-align="center" align="left " v-if="level==='2'"></el-table-column>
                    <el-table-column class-name="cannotDrag" prop="GK" label="核算主体" align="center"
                                     v-if="level==='3'"></el-table-column>
                    <el-table-column
                            v-for="(col, index) in dropData"
                            :key="index"
                            :prop="col.label!=undefined?dropData[index].id:dropData[index].INDEX_ID"
                            :label="col.label!=undefined?dropData[index].label.split('▲')[0]+(dropData[index].label.split('▲')[2].indexOf('%')!==-1?'(%)':(price==='1'?'(元)':(price==='10000'?'(万元)':'(亿元)'))):dropData[index].INDEX_NAME.indexOf('%')!==-1?dropData[index].INDEX_NAME:(price==='1'?dropData[index].INDEX_NAME+'(元)':(price==='10000'?dropData[index].INDEX_NAME+'(万元)':dropData[index].INDEX_NAME+'(亿元)'))"
                            :width="col.width"
                            min-width="300"
                            sortable="custom"
                            :type="col.type"
                            header-align="center"
                            align="right"
                            :column-key="index.toString()"
                            :render-header="(h,obj) => renderHeader(h,obj,index)"
                            class-name="canDrag"
                    >
                        <template slot-scope="scope">
                            <span :class="scope.row[col.label!=undefined?dropData[index].id:dropData[index].INDEX_ID].indexOf('RGB')>-1? 'is-link': ''">{{outputFilter(scope.row[col.label!=undefined?dropData[index].id:dropData[index].INDEX_ID])}}</span>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- <el-table
                  class="drag_table"
                  :data="tableDatas"
                  border
                  @header-click="colClick"
                  style="width: 100%"
                  v-if="show1"
                >
                  <el-table-column prop="ACCOUNT_PERIOD" width="160" label="账期" align="center"></el-table-column>
                  <el-table-column prop="GK" width="160" label="国库" align="center"></el-table-column>
                  <el-table-column
                    v-for="(col, index) in tableData"
                    :key="index"
                    :prop="col.INDEX_ID"
                    :label="col.INDEX_NAME"
                    :width="col.width"
                    :min-width="col.minWidth"
                    :type="col.type"
                    header-align="center"
                    :column-key="index.toString()"
                    :render-header="renderHeader"
                  ></el-table-column>
                </el-table> -->
            </div>
        </section>
        <!-- <el-dialog
          title="操作"
         :visible.sync="visible"
         v-dialogDrag
         width="30%">
          <a-form>
            <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 19}" label="请选择条件">
              <a-select v-model="queryParam.IS_SEARCH">
                <a-select-option value="0">过滤</a-select-option>
                <a-select-option value="1">排序</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item
              :labelCol="{span: 5}"
              :wrapperCol="{span: 19}"
              label="排序条件"
              v-if="queryParam.IS_SEARCH === '1'"
            >
              <a-select v-model="queryParam.IS_TURN">
                <a-select-option value="asc">升序</a-select-option>
                <a-select-option value="desc">降序</a-select-option>
              </a-select>
            </a-form-item>
            <a-row>
              <a-col :md="14" :sm="14">
                <a-form-item
                  :labelCol="{span: 9}"
                  :wrapperCol="{span: 15}"
                  label="过滤逻辑"
                  v-if="queryParam.IS_SEARCH === '0'"
                >
                  <a-select v-model="queryParam.IS_FILTER">
                    <a-select-option :value="d.id" v-for="d in guolvlogic" :key="d.id">{{d.name}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="10" :sm="10">
                <a-form-item
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 19}"
                  label="值"
                  v-if="queryParam.IS_SEARCH === '0'"
                >
                  <a-input v-model="queryParam.num" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
          <span slot="footer" class="dialog-footer">
           <el-button @click="visible = false">取 消</el-button>
           <el-button type="primary" @click="handleOk">确 定</el-button>
          </span>
        </el-dialog> -->
        <a-modal
                title="过滤"
                v-dialogDrag
                width="30%"
                :visible="visible"
                @ok="handleOk"
                @cancel="handleCancels"
                :maskClosable="false"
        >
            <a-form>
                <!-- <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 19}" label="请选择条件">
                  <a-select v-model="queryParam.IS_SEARCH">
                    <a-select-option value="0">过滤</a-select-option>
                    <a-select-option value="1">排序</a-select-option>
                  </a-select>
                </a-form-item> -->
                <!-- <a-form-item
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 19}"
                  label="排序条件"
                  v-if="queryParam.IS_SEARCH === '1'"
                >
                  <a-select v-model="queryParam.IS_TURN">
                    <a-select-option value="asc">升序</a-select-option>
                    <a-select-option value="desc">降序</a-select-option>
                  </a-select>
                </a-form-item> -->
                <a-row>
                    <a-col :md="14" :sm="14">
                        <a-form-item
                                :labelCol="{span: 9}"
                                :wrapperCol="{span: 15}"
                                label="过滤逻辑"

                        >
                            <a-select v-model="queryParam.IS_FILTER">
                                <a-select-option :value="d.id" v-for="d in guolvlogic" :key="d.id">{{d.name}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="10">
                        <a-form-item
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 19}"
                                label="值"

                        >
                            <a-input v-model="queryParam.num"/>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-modal>
        <a-modal
                title="组合过滤"
                width="40%"
                :visible="visible1"
                @ok="handlefilterOk"
                @cancel="handlefilterCancel"
                :maskClosable="false"
        >
            <a-row>
                <a-col>
                    <a-button type="primary" @click="addRow">新增</a-button>
                    <a-button style="margin-left:8px;" @click="clear">清空</a-button>
                </a-col>
            </a-row>
            <!-- <a-table bordered :dataSource="dataSource" :columns="columns" style="margin-top:10px;">
              <template slot="filtername" slot-scope="text, record">
                <a-select v-model="queryParam.filtername" style="width:100%;">
                  <a-select-option
                    :value="d.label.split('▲')[1]"
                    v-for="d in filtertableData"
                    :key="d.label.split('▲')[1]"
                  >{{d.label.split('▲')[0]}}</a-select-option>
                </a-select>
              </template>
              <template slot="filterlogic" slot-scope="text, record">
                <a-select v-model="queryParam.filterlogic" style="width:100%;">
                  <a-select-option value="0">等于</a-select-option>
                  <a-select-option value="1">不等于</a-select-option>
                </a-select>
              </template>
              <template slot="filternumber" slot-scope="text, record">
                <a-input v-model="queryParam.filternumber" />
              </template>
              <template slot="operation" slot-scope="text, record">
                <a-popconfirm
                  v-if="dataSource.length"
                  title="Sure to delete?"
                  @confirm="() => onDelete(record.key)"
                >
                  <a href="javascript:;">删除</a>
                </a-popconfirm>
              </template>
            </a-table>-->
            <el-table :header-cell-style="{background:'#fafafa',color:'#606266'}" :data="dataSource" size="small" border
                      empty-text="暂无数据" style="width: 100%;margin-top:10px;">
                <el-table-column
                        prop="filtername"
                        label="过滤字段"
                        show-overflow-tooltip
                        width="300"
                        align="center"
                >
                    <template slot-scope="scope">
                        <a-select v-model="scope.row.filtername" @select="selected" labelInValue style="width:100%;">
                            <a-select-option
                                    :value="d.label!=undefined?d.id:d.INDEX_ID"
                                    v-for="d in filtertableData"
                                    :key="d.label!=undefined?d.label.split('▲')[0]:d.INDEX_ID"
                            >{{d.label!=undefined?d.label.split('▲')[0]:d.INDEX_NAME}}
                            </a-select-option>
                        </a-select>
                    </template>
                </el-table-column>
                <el-table-column prop="filterlogic" label="过滤逻辑" width="150" align="center">
                    <template slot-scope="scope">
                        <a-select v-model="scope.row.filterlogic" style="width:100%;">
                            <a-select-option :value="d.id" v-for="d in guolvlogic" :key="d.id">{{d.name}}
                            </a-select-option>
                        </a-select>
                    </template>
                </el-table-column>
                <el-table-column prop="filternumber" label="过滤值" width="100" align="center">
                    <template slot-scope="scope">
                        <a-input v-model="scope.row.filternumber" type="text" placeholder="过滤值"></a-input>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="160" align="center">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                type="danger"
                                plain
                                @click.native="delRow(scope.$index,dataSource)"
                                title="删除"
                        >删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </a-modal>
        <a-modal
                title="组合排序"
                width="40%"
                :visible="visible2"
                @ok="handlesortOk"
                @cancel="handlesortCancel"
                :maskClosable="false"
        >
            <a-row>
                <a-col>
                    <a-button type="primary" @click="addRow1">新增</a-button>
                    <a-button style="margin-left:8px;" @click="clear1">清空</a-button>
                </a-col>
            </a-row>
            <!-- <a-table bordered :dataSource="dataSource1" :columns="columns1" style="margin-top:10px;">
              <template slot="sortname" slot-scope="text, record">
                <a-select v-model="queryParam.filtername" style="width:100%;">
                  <a-select-option
                    :value="d.label"
                    v-for="d in filtertableData"
                    :key="d.label"
                  >{{d.label}}</a-select-option>
                </a-select>
              </template>
              <template slot="sortrule" slot-scope="text, record">
                <a-select v-model="queryParam.IS_FILTER" style="width:100%;">
                  <a-select-option value="0">升序</a-select-option>
                  <a-select-option value="1">降序</a-select-option>
                </a-select>
              </template>
              <template slot="operation1" slot-scope="text, record">
                <a-popconfirm
                  v-if="dataSource1.length"
                  title="Sure to delete?"
                  @confirm="() => onDelete1(record.key)"
                >
                  <a href="javascript:;">删除</a>
                </a-popconfirm>
              </template>
            </a-table> -->
            <el-table :header-cell-style="{background:'#fafafa',color:'#606266'}" :data="dataSource1" size="small"
                      border empty-text="暂无数据" style="width: 100%;margin-top:10px;">
                <el-table-column
                        prop="sortname"
                        label="排序字段"
                        show-overflow-tooltip
                        width="300"
                        align="center"
                >
                    <template slot-scope="scope">
                        <a-select v-model="scope.row.sortname" style="width:100%;">
                            <!-- <a-select-option
                              :value="d.id"
                              v-for="d in filtertableData"
                              :key="d.id"
                            >{{d.label.split('▲')[0]}}</a-select-option> -->
                            <a-select-option
                                    :value="d.label!=undefined?d.id:d.INDEX_ID"
                                    v-for="d in filtertableData"
                                    :key="d.label!=undefined?d.label.split('▲')[0]:d.INDEX_ID"
                            >{{d.label!=undefined?d.label.split('▲')[0]:d.INDEX_NAME}}
                            </a-select-option>
                        </a-select>
                    </template>
                </el-table-column>
                <el-table-column prop="sortrule" label="排序规则" width="150" align="center">
                    <template slot-scope="scope">
                        <a-select v-model="scope.row.sortrule" style="width:100%;">
                            <a-select-option value="asc">升序</a-select-option>
                            <a-select-option value="desc">降序</a-select-option>
                        </a-select>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="160" align="center">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                type="danger"
                                plain
                                @click.native="delRow(scope.$index,dataSource1)"
                                title="删除"
                        >删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </a-modal>
        <a-modal
                title="操作"
                width="40%"
                :visible="visible3"
                @cancel="handleplansaveCancels"
                @ok="handleplansave"
                :maskClosable="false"
        >
            <a-form>
                <a-form-item :labelCol="{span: 3}" :wrapperCol="{span: 21}" label="方案描述">
                    <a-textarea v-model="queryParam.plandespection" style="width:100%;"/>
                </a-form-item>
            </a-form>
        </a-modal>
        <a-modal
                title="保存"
                width="40%"
                :visible="visible6"
                @ok="handleplansave2"
                @cancel="handlesaveCancel2"
                :maskClosable="false"
        >
            <a-radio-group v-model="savevalue">
                <a-radio :value="1" style="margin-right:30px;">保存</a-radio>
                <a-radio :value="2" v-if="planStatus==='1'">另存</a-radio>
            </a-radio-group>
        </a-modal>
        <!-- <a-modal
          title="查看方案"
          width="40%"
          :visible="visible4"
          @ok="handlecheckOk"
          @cancel="handlecheckCancel"
          :maskClosable="false"
        >
          <a-table bordered :dataSource="dataSource2" :columns="columns2" style="margin-top:10px;">
            <template slot="action" slot-scope="text, record">
              <a @click="handleRun(record)">执行</a>
              <a-divider type="vertical" />
              <a @click="handleDelete(record)">删除</a>
            </template>
          </a-table>
        </a-modal> -->
    </div>
</template>

<script>
    const loadmore = {
        bind(el, binding) {
            const selectWrap = el.querySelector('.el-table__body-wrapper')
            selectWrap.addEventListener('scroll', function () {
                let sign = 0
                const scrollDistance = this.scrollHeight - this.scrollTop - (this.clientHeight + 2)
                if (scrollDistance <= sign) {
                    binding.value()
                }
            })
        }
    }
    /* import Sortable from 'vuedraggable' */
    import Sortable from "sortablejs";
    import deepClone from '../../utils/deepClone.js'
    import {
        getIndicatorsTable,
        getEnumTypeAll,
        saveIndexScheme,
        selectSchemeData,
        deleteScheme
    } from '@/api/nationalTreasury'

    export default {
        props: ['tableData', 'tableDatas', 'obj', 'level', 'dataSource2', 'record', 'price', 'planStatus','loadings'],
        components: {
            Sortable
        },
        /* directives: {dialogDrag}, */
        data() {
            return {
                show: true,
                show1: false,
                filtertableData: [],
                guolvlogic: [],
                total: 1,
                pageNo: 1,
                aq: true,
                visible: false,
                visible1: false,
                visible2: false,
                visible3: false,
                visible4: false,
                sortColumn: '',
                savevalue: '',
                visible6: false,
                queryParam: {},
                dropData: [],
                dataSource: [],
                dataSource1: [],
                obj22: {},
                //dataSource2: [],
                count: 2,
                count1: 2,
                classStyle: false,
                pictLoading:false,
                columns: [
                    {
                        title: '过滤字段',
                        dataIndex: 'filtername',
                        width: '30%',
                        align: 'center',
                        scopedSlots: {customRender: 'filtername'}
                    },
                    {
                        title: '过滤逻辑',
                        dataIndex: 'filterlogic',
                        width: '30%',
                        align: 'center',
                        scopedSlots: {customRender: 'filterlogic'}
                    },
                    {
                        title: '过滤值',
                        dataIndex: 'filternumber',
                        width: '20%',
                        align: 'center',
                        scopedSlots: {customRender: 'filternumber'}
                    },
                    {
                        title: '操作',
                        dataIndex: 'operation',
                        align: 'center',
                        scopedSlots: {customRender: 'operation'}
                    }
                ],
                columns1: [
                    {
                        title: '排序字段',
                        dataIndex: 'sortname',
                        width: '30%',
                        align: 'center',
                        scopedSlots: {customRender: 'sortname'}
                    },
                    {
                        title: '排序规则',
                        dataIndex: 'sortrule',
                        width: '30%',
                        align: 'center',
                        scopedSlots: {customRender: 'sortrule'}
                    },
                    {
                        title: '操作',
                        dataIndex: 'operation1',
                        align: 'center',
                        scopedSlots: {customRender: 'operation1'}
                    }
                ],
                columns2: [
                    {
                        title: '方案描述',
                        dataIndex: 'SCHEME_DESCR',
                        width: '70%',
                        align: 'center'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: {customRender: 'action'}
                    }
                ],
                style1: {
                    position: 'absolute',
                    top: '10px'
                },
                dragState: {
                    startIndex: -1, // 拖动起始元素的index
                    endIndex: -1, // 拖动结束元素的index
                    afterMoveIndex: -1, // 拖动后元素的index
                    dragging: false, // 是否正在拖动
                    direction: null, // 拖动方向
                    moveTableOutsideBack: false // 拖出到table外之后又拖回来
                },
                where1: [],
                order1: [],
            }
        },
        mounted() {
            /* selectSchemeData({schemeId:JSON.parse(localStorage.getItem('records')).ID}).then(res=>{
              if(res.result =='success'){
                this.tableDatas = res.rows;
                this.tableData = res.columns
              }
            }) */
            if (document.getElementById("drag_table") !== null) {
                document.getElementById("drag_table").oncontextmenu = function (e) {
                    return false;
                }
            }
            console.log(document.querySelector('.el-table__header-wrapper'))
            document.querySelector('.el-table__header-wrapper').oncontextmenu = function (e) {
                return false;
            }
            document.body.ondrop = function (event) {
                event.preventDefault();
                event.stopPropagation();
            }
            this.columnDrop();
        },
        watch: {
            'loadings'(val){
               this.pictLoading = val;
             },
            'tableData'(val) {
                this.dropData = deepClone(val)
                //this.dropData = Object.assign([],val)
            },
            'dropData'(val) {
                if (val.length > 0) {
                    this.$nextTick(() => {
                        var lista = document.querySelectorAll('.has-gutter .canDrag')
                        for (var i = 0; i < lista.length; i++) {
                            lista[i].onmouseover = function (e) {
                                if (e.target.querySelector('.el-icon-close') !== null) {
                                    let cc = e.target.querySelector('.el-icon-close').attributes[1].value = 'position:absolute;top:8px;right:2px;font-size:16px;display:block';
                                }
                            }
                            lista[i].onmouseleave = function (e) {
                                if (e.target.querySelector('.el-icon-close') !== null) {
                                    let cc = e.target.querySelector('.el-icon-close').attributes[1].value = 'position:absolute;top:8px;right:2px;font-size:16px;display:none';
                                }
                            }
                        }
                    })
                }
            }
        },
        directives: {loadmore},
        computed: {
            tableMaxHeight() {
                return window.innerHeight - 300 + 'px';
            }
        },
        methods: {
            // 在渲染表头的时候,会调用此方法, h为createElement的缩写版, 也可以添加事件click、change等
            // drag_table在渲染表头时调用
            // 在渲染表头的时候,会调用此方法, h为createElement的缩写版, 也可以添加事件click、change等
            columnDrop() {
                const that = this;
                const wrapperTr = document.querySelector('.el-table__header-wrapper tr')
                this.sortable = Sortable.create(wrapperTr, {
                    animation: 180,
                    delay: 0,
                    filter: ".cannotDrag",
                    draggable: ".canDrag",
                    onEnd: evt => {
                        const oldItem = this.dropData[evt.oldIndex - 2]
                        this.dropData.splice(evt.oldIndex - 2, 1)
                        console.log(this.tableData);
                        this.dropData.splice(evt.newIndex - 2, 0, oldItem)
                        var newArray = that.dropData.slice(0);
                        that.dropData = [];
                        that.$nextTick(function () {
                            that.dropData = newArray;
                        });
                        console.log(this.dropData)
                        console.log(this.tableData);
                    }
                })
            },
            renderHeader(h, {column, $index}, index) {
                console.log($index, index)
                if (
                    column.label !== '账期' &&
                    column.label !== '国库' &&
                    column.label !== '地区' &&
                    column.label !== '核算主体'
                ) {
                    /*  return(
                       <div>
                          <span>{column.label}</span>
                          <span onClick={this.setClose} class="el-icon-close" style="position:absolute;top:-4px;font-size:16px;right:0px;"></span>
                          <el-input />
                       </div>
                     ) */
                    return h('span', [
                        h('span', column.label),
                        h('span', {
                            class: 'el-icon-close',
                            style: {
                                top: '20px',
                                fontSize: '16px',
                                position: 'absolute',
                                display: 'none',
                                right: '0px',
                            },
                            on: {
                                click: $event => {
                                    console.log(`${column.label}   ${$index}`)
                                    this.$emit('del', `${$index}`)
                                    $event.cancelBubble = true
                                }
                            }
                        }),
                        /* h('span',{
                          class:'el-icon-caret-top',
                          style: {
                            top: '2px',
                            position:'absolute',
                          },
                           on: {
                            click: $event => {
                              debugger
                              event.currentTarget.attributes[1].value = 'position: absolute;top: 2px;color: #409eff;';
                              event.currentTarget.nextElementSibling.attributes[1].value = 'position: absolute;top: 10px;';
                              console.log(event)
                              console.log(`${column.label}   ${$index}`)
                               $event.cancelBubble = true
                              let oThis = this;
                              let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                              let userId = userInfo.id
                              this.obj22 = {
                                where: '',
                                order:[{
                                  sortname:column.property,
                                  sortrule:'asc'
                                }],
                              }
                              getIndicatorsTable({
                        mainCondition: this.obj,
                        userId: userId,
                        columns: this.obj.columns,
                        screenConditon: this.obj22
                      }).then(res => {
                        if (res.result == 'success') {
                          this.tableDatas = res.rows
                          this.visible = false
                          this.$message.success(res.msg)
                          this.$emit('toLoad',this.obj22,this.tableDatas)
                          $event.cancelBubble = false
                        }
                      })

                            }
                          }
                        }),
                         h('span',{
                          class:'el-icon-caret-bottom',
                          style: this.style1,
                           on: {
                            click: $event => {
                              console.log(event.currentTarget.attributes[1].value)
                              event.currentTarget.attributes[1].value = 'position: absolute;top: 10px;color: #409eff;';
                              event.currentTarget.previousElementSibling.attributes[1].value = 'position: absolute;top: 2px;';
                              console.log(event.currentTarget.attributes[1].value)
                              console.log(`${column.label}   ${$index}`)
                               $event.cancelBubble = true
                              let oThis = this;
                              let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                              let userId = userInfo.id
                              this.obj22 = {
                                where: '',
                                order:[{
                                  sortname:column.property,
                                  sortrule:'desc'
                                }],
                              }
                              getIndicatorsTable({
                        mainCondition: this.obj,
                        userId: userId,
                        columns: this.obj.columns,
                        screenConditon: this.obj22
                      }).then(res => {
                        if (res.result == 'success') {
                          this.tableDatas = res.rows
                          this.visible = false
                          this.$message.success(res.msg)
                          this.$emit('toLoad',this.obj22,this.tableDatas)
                        }
                      })
                            }
                          }
                        }),*/
                    ])
                } else {
                    return h('span', [h('span', column.label)])
                }
            },
            outputFilter(callState) {
                if (callState.indexOf('RGB') > -1) {
                    //this.classStyle = true;
                    return (callState.substring('4'))

                } else {
                    //this.classStyle = false;
                    return (callState)
                }
            },
            handleCurrentChange(page) {
                this.pageNo = page;
                //this.getData(this.groupData);
            },
            /* loadMore(){
              debugger
              console.log(this.loadSign)
              if(this.aq == false){
                   return
              }
              if(this.pageNo == 1){
                this.pageNo++;
              }
             let col = []
              this.tableData.forEach((el, index) => {
                if (el.id) {
                  col.push(el.id)
                } else {
                  col.push(el.INDEX_ID)
                }
              })
              if (col.length == 0) {
                this.$message.error('请选择要查询的指标')
                return false
              }
              let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
              let userId = userInfo.id
              console.log(this.obj)
              getIndicatorsTable({
                mainCondition: this.obj,
                userId: userId,
                columns: col.join(','),
                pageNo: this.pageNo,
                pageSize: 10
              }).then(res => {
                if (res.result == 'success') {
                  if(res.rows.length>0){
                    this.pageNo++
                    res.rows.forEach(res => {
                         this.tableDatas.push(res)
                   });
                  console.log('到底了', this.pageNo)
                  }else{
                     this.aq = false
                  }
                } else {
                  this.tableDatas = []
                  this.$message.error(res.msg)
                }
              })
            }, */
            sortChange(column, prop, order) {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                if (column.order) {
                    this.order1 = [{
                        sortname: (column.column.label === '国库' || column.column.label === '地区') ? 'CODE' : column.column.label === '账期' ? 'ACCOUNT_PERIOD' : column.column.label === '辖属' ? 'JURISDICTION' : column.prop,
                        sortrule: column.order === 'descending' ? 'desc' : 'asc',
                    }]
                    this.obj22 = {
                        where: this.where1.length === 0 ? '' : this.where1,
                        order: this.order1.length === 0 ? '' : this.order1,
                    }
                    this.visible = false;
                    this.$emit('toLoad', this.obj22)
                    /* getIndicatorsTable({
                      mainCondition: this.obj,
                      userId: userId,
                      pageNo:1,
                      pageSize:100,
                      columns: this.obj.columns,
                      screenConditon: this.obj22
                    }).then(res => {
                      if (res.result == 'success') {
                        this.tableDatas = res.rows
                        this.visible = false
                        this.$emit('toLoad',this.obj22,this.tableDatas)
                      }
                    }) */
                }
            },
            handleOk() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                if (this.queryParam.IS_SEARCH === '1') {
                    this.obj22 = {
                        //筛选(组合查询、排序...)查询条件
                        where: '', //组合过滤条件
                        order: [{
                            sortname: this.sortColumn,
                            sortrule: this.queryParam.IS_TURN,
                        }],
                    }
                    this.pictLoading = true
                    getIndicatorsTable({
                        mainCondition: this.obj,
                        userId: userId,
                        columns: this.obj.columns,
                        screenConditon: this.obj22
                    }).then(res => {
                        if (res.result == 'success') {
                            this.pictLoading = false
                            this.tableDatas = res.rows
                            this.visible = false
                            this.$message.success(res.msg)
                            this.$emit('toLoad', this.obj22, this.tableDatas)
                        }
                    })
                } else {
                    /* data.list.forEach(function(el, key) {
                     if(el.filtername.label.indexOf("%") != -1){
                       el.filtername = el.filtername.key;
                       el.filternumber = el.filternumber/100;
                     }else{
                       el.filtername = el.filtername.key;
                     }
                  }) */
                    this.where1 = [{
                        filtername: this.sortColumn,
                        filterlogic: this.queryParam.IS_FILTER,
                        filternumber: this.queryParam.num
                    }]
                    this.obj22 = {
                        //筛选(组合查询、排序...)查询条件
                        where: this.where1.length === 0 ? '' : this.where1,
                        order: this.order1.length === 0 ? '' : this.order1,
                    }
                    this.visible = false
                    this.$emit('toLoad', this.obj22)
                    /* getIndicatorsTable({
                      mainCondition: this.obj,
                      userId: userId,
                      columns: this.obj.columns,
                      screenConditon: this.obj22
                    }).then(res => {
                      if (res.result == 'success') {
                        this.tableDatas = res.rows
                        this.visible = false
                        this.$message.success(res.msg)
                        this.$emit('toLoad',this.obj22,this.tableDatas)
                      } else {
                        this.$message.error(res.msg)
                      }
                    }) */
                }
            },
            handleCancels() {
                this.visible = false
            },
            handlefilterCancel() {
                this.visible1 = false
            },
            selected(value, obj) {

            },
            handlefilterOk() {
                //console.log(this.dataSource)
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value;
                let userId = userInfo.id;
                /* let sar = [];
                if(this.level = 1){
                  sar = [{'账期':ACCOUNT_PERIOD},]
                } */
                let _this = this
                let data = {
                    list: _this.dataSource
                }
                let objs = [];
                data.list.forEach(function (el, key) {
                    if ((el.filtername.label != undefined ? el.filtername.label : el.filtername.INDEX_NAME).indexOf("%") != -1) {
                        el.filtername = el.filtername.key;
                        el.filternumber = el.filternumber / 100;
                    } else {
                        el.filtername = el.filtername.key;
                    }
                })
                console.log(objs);
                this.where1 = data.list;
                this.obj22 = { //筛选(组合查询、排序...)查询条件
                    where: this.where1.length === 0 ? '' : this.where1,
                    order: this.order1.length === 0 ? '' : this.order1,
                }
                //this.visible1 = false;
                this.visible1 = false;
                this.$emit('toLoad', this.obj22)
                //this.$emit('toLoad',this.obj22)
                /* getIndicatorsTable({
                        "mainCondition":this.obj,
                        "userId":userId,
                        "columns":this.obj.columns,
                        "screenConditon":this.obj22

          }).then(res=>{
                   if(res.result == 'success'){
                     this.tableDatas = res.rows
                     this.visible1 = false;
                     this.$message.success(res.msg)
                     this.$emit('toLoad',this.obj22,this.tableDatas)
                   }else{
                     this.$message.error(res.msg)
                   }
                }) */


            },
            handlesortCancel() {
                this.visible2 = false
            },
            handlesortOk() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value;
                let userId = userInfo.id;
                /* let _this = this
                let data = {
                  list: _this.dataSource1
                }
                let objs = '';
                data.list.forEach(function(el, key) {
                   if(el){
                     objs += el.sortname+' '+el.sortrule+ ' and '
                   }
                }) */
                let _this = this
                let data = {
                    list: _this.dataSource1
                }
                let objs = [];
                data.list.forEach(function (el, key) {
                    if (el.sortname !== '' && el.sortrule !== '') {
                        objs.push(el.sortname, el.sortrule)
                    }
                })
                console.log(objs);
                this.order1 = data.list;
                this.obj22 = { //筛选(组合查询、排序...)查询条件
                    where: this.where1.length === 0 ? '' : this.where1,
                    order: this.order1.length === 0 ? '' : this.order1,
                },
                    this.visible2 = false;
                this.$emit('toLoad', this.obj22)
                /* getIndicatorsTable({
                        "mainCondition":this.obj,
                        "userId":userId,
                        "columns":this.obj.columns,
                        "screenConditon":this.obj22

          }).then(res=>{
                   if(res.result == 'success'){
                     this.tableDatas = res.rows
                     this.visible2 = false;
                     this.$message.success(res.msg)
                     this.$emit('toLoad',this.obj22,this.tableDatas)
                   }else{
                     this.$message.error(res.msg)
                   }
                }) */
            },
            handleplansaveCancels() {
                this.visible3 = false
            },
            handleplansave2() {
                if (this.planStatus === '1') {
                    if (this.savevalue === 1) {
                        let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value;
                        let userId = userInfo.id;
                        saveIndexScheme({
                            "mainCondition": this.obj,
                            "userId": userId,
                            "columns": this.obj.columns,
                            "schemeId": this.record,
                            "screenConditon":this.obj22,
                            "schemeDescr": this.queryParam.plandespection

                        }).then(res => {
                            if (res.result == 'success') {
                                /*  this.tableDatas = res.rows */
                                this.visible6 = false;
                                this.$message.success(res.msg)
                                this.$emit('toLoad', this.obj22, this.tableDatas)
                            } else {
                                this.$message.error(res.msg)
                            }
                        })
                    } else {
                        this.visible3 = true;
                        this.visible6 = false;
                    }
                } else {
                    this.visible3 = true;
                    this.visible6 = false;
                }
            },
            handlesaveCancel2() {
                this.visible6 = false;
            },
            handlecheckCancel() {
                this.visible4 = false
            },
            handlecheckOk() {
            },
            filter() {
                this.visible1 = true
                this.dataSource = [];
                getEnumTypeAll(20).then((res) => {
                    if (res.result === 'success') {
                        this.guolvlogic = res.rows
                    }
                });
                let sar = [];
                if (this.level === '1') {
                    sar = [{INDEX_NAME: '账期', INDEX_ID: 'ACCOUNT_PERIOD'}, {
                        INDEX_NAME: '国库',
                        INDEX_ID: 'GK'
                    }]
                } else if (this.level === '2') {
                    sar = [{INDEX_NAME: '账期', INDEX_ID: 'ACCOUNT_PERIOD'}, {
                        INDEX_NAME: '地区',
                        INDEX_ID: 'GK'
                    }]
                }
                this.filtertableData = deepClone(this.tableData.concat(sar))
            },
            sort() {
                this.visible2 = true
                this.dataSource1 = [];
                let sar = [];
                if (this.level === '1') {
                    sar = [{INDEX_NAME: '账期', INDEX_ID: 'ACCOUNT_PERIOD'}, {
                        INDEX_NAME: '国库',
                        INDEX_ID: 'CODE'
                    }]
                } else if (this.level === '2') {
                    sar = [{INDEX_NAME: '账期', INDEX_ID: 'ACCOUNT_PERIOD'}, {
                        INDEX_NAME: '地区',
                        INDEX_ID: 'CODE'
                    }]
                }
                this.filtertableData = deepClone(this.tableData.concat(sar))
            },
            onDelete(key) {
                const dataSource = [...this.dataSource]
                this.dataSource = dataSource.filter(item => item.key !== key)
            },
            clear() {
                this.dataSource = []
            },
            //数据的新增
            addRow() {
                let j = {
                    filtername: '',
                    filterlogic: '',
                    filternumber: ''
                }
                this.dataSource.push(j)
            },
            // 数据的删除
            delRow(index, rows) {
                rows.splice(index, 1)
            },
            clear1() {
                this.dataSource1 = []
            },
            addRow1() {
                let j = {
                    sortname: '',
                    sortrule: '',
                }
                this.dataSource1.push(j)
            },
            saveplan() {
                if (this.record === "") {
                    this.visible3 = true
                } else {
                    this.visible6 = true;
                }
                //this.visible3 = true
            },
            checkplan() {
                this.visible4 = true
            },

            handleplansave() {
                console.log(this.record)
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value;
                let userId = userInfo.id;
                saveIndexScheme({
                    "mainCondition": this.obj,
                    "userId": userId,
                    "columns": this.obj.columns,
                    "schemeId": "",
                    "screenConditon": this.obj22,
                    "schemeDescr": this.queryParam.plandespection

                }).then(res => {
                    if (res.result == 'success') {
                        /*  this.tableDatas = res.rows */
                        this.visible3 = false;
                        this.$message.success(res.msg)
                        this.$emit('toLoad', this.obj22, this.tableDatas)
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            },
            handleDelete(record) {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value;
                let userId = userInfo.id;
                deleteScheme({userId: userId, schemeId: record}).then(res => {

                })
            },
            // 按下鼠标开始拖动
            handleMouseDown(e, column) {
                // 判断是鼠标左键
                if (e.button === 0) {
                    this.dragState.dragging = true
                    this.dragState.startIndex = parseInt(column.columnKey)
                    console.log(`开始移动的位置 ${this.dragState.startIndex}`)
                    // 给当前要拖动列的th设置class
                    document.querySelectorAll('.drag_table table thead tr th')[this.dragState.startIndex].className =
                        'dragging_column'
                    // 给拖动时的虚拟容器添加宽高
                    let table = document.getElementsByClassName('drag_table')[0]
                    let virtual = document.getElementsByClassName('virtual')
                    // 设置每一列的宽度、高度
                    for (let item of virtual) {
                        item.style.height = table.clientHeight - 1 + 'px'
                        item.style.width = item.parentElement.parentElement.clientWidth + 'px'
                    }
                    this.dragState.moveTableOutsideBack = false
                }
                console.log(111)
            },

            // 拖动中
            handleMouseMove(e, column) {
                // 判断是鼠标左键
                if (e.button === 0) {
                    if (this.dragState.dragging) {
                        let currentIndex = parseInt(column.columnKey) // 拖动的当前列index
                        console.log(`移动到了${currentIndex}`)
                        if (currentIndex !== this.dragState.startIndex) {
                            this.dragState.direction = currentIndex - this.dragState.startIndex < 0 ? 'left' : 'right' // 判断拖动方向
                            this.dragState.afterMoveIndex = currentIndex
                        } else {
                            this.dragState.direction = null
                        }
                    } else {
                        return false
                    }
                }
                e.cancelBubble = true
            },

            // 鼠标放开结束拖动
            handleMouseUp(e, column) {
                // 判断是鼠标左键
                if (e.button === 0) {
                    // 拖出当前table外之后又拖回来，不再进行易位操作（拖出去时已处理）
                    if (this.dragState.moveTableOutsideBack) {
                        return false
                    } else {
                        this.dragState.endIndex = parseInt(column.columnKey) // 记录结束列index
                        console.log(`结束移动的位置 ${this.dragState.endIndex}`)
                        if (this.dragState.startIndex !== this.dragState.endIndex) {
                            this.dragColumn(this.dragState)
                        }
                        this.finishDragInit()
                    }
                }
            },

            // 拖动到当前table之外的处理
            moveTableOutside() {
                console.log(this.dragColumn)
                if (this.dragState.dragging) {
                    this.dragState.endIndex = this.dragState.startIndex
                    console.log(`已移动到table外，结束移动的位置 ${this.dragState.endIndex}`)
                    this.$emit('del', this.dragState.endIndex)
                    if (this.dragState.startIndex !== this.dragState.endIndex) {
                        this.dragColumn(this.dragState)
                    }
                    this.finishDragInit()
                    this.dragState.moveTableOutsideBack = true
                }
            },

            // 拖动易位
            dragColumn({startIndex, endIndex, direction}) {
                console.log(`从${startIndex}移动到了${endIndex}`)
                // 判断是向左移动还是向右移动
                if (direction === 'left') {
                    this.tableData.splice(endIndex, 0, this.tableData[startIndex])
                    this.tableData.splice(startIndex + 1, 1)
                } else {
                    this.tableData.splice(endIndex + 1, 0, this.tableData[startIndex])
                    this.tableData.splice(startIndex, 1)
                }
            },

            // 拖动完成后的初始化
            finishDragInit() {
                // 给当前要拖动列的th取消class
                document.querySelectorAll('.drag_table table thead tr th')[this.dragState.startIndex].className = ''
                // 再次初始化拖动状态
                this.dragState = {
                    startIndex: -1,
                    endIndex: -1,
                    afterMoveIndex: -1,
                    dragging: false,
                    direction: null,
                    moveTableOutsideBack: false
                }
            },

            // 动态给表头单元格添加 class，实现拖动中的虚线效果
            headerCellClassName({column, columnIndex}) {
                return columnIndex === this.dragState.afterMoveIndex ? `drag_active_${this.dragState.direction}` : ''
            },

            // 动态给表头单元格th添加class，实现拖动中的背景
            cellClassName({column, columnIndex}) {
                return columnIndex === this.dragState.startIndex ? `dragging_column` : ''
            },

            colClick(column, event) {
                window.event.returnValue = false;
                console.log(this.obj)
                console.log(222)
                this.queryParam = {};
                getEnumTypeAll(20).then((res) => {
                    if (res.result === 'success') {
                        this.guolvlogic = res.rows
                    }
                });
                if (
                    column.label !== '账期' &&
                    column.label !== '国库' &&
                    column.label !== '地区' &&
                    column.label !== '核算主体' &&
                    column.label !== '级次' &&
                    column.label !== '辖属'
                ) {
                    this.visible = true
                    this.sortColumn = column.property
                    event.cancelBubble = true
                } else {
                    this.visible = false
                }
            },
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scode>
    .errorIcon {
        display: inline-flex;
        flex-direction: row;
        width: 15px;
        height: 15px;
    }

    .errorIcon2 {
        display: inline-flex;
        flex-direction: row-reverse;
        margin-right: 3px;
        width: 15px;
        height: 15px;
    }

    .errorIcon3::before {
        content: '##';
    }

    .errorIcon,
    .errorIcon2,
    .errorIcon3:hover {
        cursor: pointer;
    }

    .drag_table th {
        cursor: pointer;
    }

    .virtual {
        position: fixed;
        display: block;
        margin-top: -13px;
        margin-left: -11px;
    }

    .drag_active_left .virtual {
        border-left: 1px dotted #666;
        z-index: 99;
    }

    .drag_active_right .virtual {
        border-right: 1px dotted #666;
        z-index: 99;
    }

    .thead-cell {
        display: inline-flex;
        flex-direction: column;
    }

    .thead-cell:before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
    }

    .dragging_column {
        background-color: #f3f3f3 !important;
    }

    .sort {
        padding: 0;
        list-style: none;
        margin: 150px 150px;
    }

    .sort li {
        width: 80px;
        height: 50px;
        display: inline-block;
        border-radius: 8px;
        border: 1px #000 solid;
        text-align: center;
        line-height: 50px;
        cursor: pointer;
        transition: all 0.3s linear;
        margin-left: 5px;
    }

    .sort li:hover {
        background-color: #0CF;
        color: #fff;
        border: 1px #fff solid;
    }

    .sort li.checked {
        background-color: #0CF;
        color: #fff;
        border: 1px #fff solid;
    }

    .el-dialog__header {
        padding: 16px 24px;
        color: rgba(0, 0, 0, 0.65);
        background: #fff;
        border-bottom: 1px solid #e8e8e8;
        border-radius: 4px 4px 0 0;
    }

    .el-dialog__title {
        margin: 0;
        color: rgba(0, 0, 0, 0.85);
        font-weight: 500;
        font-size: 16px;
        line-height: 22px;
    }

    .el-dialog__body {
        padding: 24px;
        font-size: 14px;
        line-height: 1.5;
        word-wrap: break-word;
        max-height: 600px;
        overflow: hidden auto;
    }

    #drag_table .el-table__body-wrapper {
        height: calc(100% - 40px) !important;
    }

    .is-link {
        cursor: pointer;
        color: #29e;
    }
</style>
