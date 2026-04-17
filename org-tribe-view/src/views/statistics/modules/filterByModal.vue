<template>
    <!--综合查询-筛选条件-->
    <a-modal
            title="筛选条件"
            :maskClosable="false"
            v-model="visibleModal"
            width="90%"
            @ok="handleOk"
            @cancel="handleCancel"
            :confirmLoading="confirmLoading"
    >
        <div style="line-height: 30px;padding-bottom: 15px;">
            <a-checkbox @change="onCheckChange" :checked="JSON.parse(IS_COUNT)"/>
            是否进行指标计算<span style="color: #cecece">（注：当查询的数据表中有多个时间字段时，需提前选择用来计算指标的主体时间）</span>
            <br/>
            <div :style="{color:IS_COUNT === 'false' ? '#cecece' : ''}">
                选择计算指标的时间：
                <a-select v-model="TIME_COLUMN" style="width: 200px;" allowClear v-if="listData.length > 0"
                          :disabled="IS_COUNT === 'false'" @change="handleTimeColumnChange">
                    <a-select-option :value="key.value"
                                     v-for="(key,i) in indicatorDropDown"
                                     :key="i">
                        {{key.name}}
                    </a-select-option>
                </a-select>
            </div>
          <div style="margin-top: 10px">
            选择维度：
            <a-select style="width: 200px;" v-model="DIMENSION_ID" allowClear  @change="handleMainColumnChange">
              <a-select-option :value="key.id"
                               v-for="(key,i) in dimensionDropDown"
                               :key="i">
                {{key.name}}
              </a-select-option>
            </a-select>
            过滤字段：
            <a-select style="width: 200px;" v-model="DIMENSION_COLUMN " allowClear  @change="handleMainColumnChange1">
              <a-select-option :value="key.FIELD_EN"
                               v-for="(key,i) in columnList1"
                               :key="i">
                {{key.FIELD_NAME}}
              </a-select-option>
            </a-select>
          </div>
        </div>
        <a-collapse accordion activeKey="0">
            <a-collapse-panel :header="item.title" :key="i" v-for="(item,i) in listData">
                <div style="padding-bottom: 10px;">
                    <a-button type="primary" @click="onAdd(i)">新增</a-button>
                    <a-button @click="onClear(i)">清空</a-button>
                </div>
                <a-table
                        size="middle"
                        bordered
                        :rowKey="(record, i) => i"
                        :columns="columns"
                        :dataSource="item.dataSource"
                        :pagination="false"
                >
          <span slot="action" slot-scope="text, record, index">
            <a @click="handleDel(i,index)">删除</a>
          </span>
                    <template slot="WHERE_LEFT" slot-scope="text, record, index">
                        <a-select v-model="record.WHERE_LEFT" style="width: 100%;" allowClear showSearch
                                  :filter-option="filterOption"
                                  @change="onChange(i,index)">
                            <a-select-option :value="key.FIELD_SIGN" v-for="(key,i) in item.columnList" :key="i"
                                             >
                                {{key.FIELD_NAME}}
                            </a-select-option>
                        </a-select>
                    </template>
                    <template slot="WHERE_MIDDLE" slot-scope="text, record">
                        <a-select v-model="record.WHERE_MIDDLE" style="width: 100%;" allowClear
                                  :disabled="record.WHERE_LEFT && (record.WHERE_LEFT.split('▲')[1] === 'D' || record.WHERE_LEFT.split('▲')[1] === 'T' || record.WHERE_LEFT.split('▲')[1] === 'B')">
                            <a-select-option value="=">等于</a-select-option>
                            <a-select-option value="!=">不等于</a-select-option>
                            <a-select-option value=">">大于</a-select-option>
                            <a-select-option value=">=">大于等于</a-select-option>
                            <a-select-option value="<">小于</a-select-option>
                            <a-select-option value="<=">小于等于</a-select-option>
                            <a-select-option value="like">包含</a-select-option>
                            <a-select-option value="not like">不包含</a-select-option>
                            <a-select-option value=" like">开头是</a-select-option>
                            <a-select-option value="like ">结尾是</a-select-option>
                            <a-select-option value="L:>">字段长度大于</a-select-option>
                            <a-select-option value="L:<">字段长度小于</a-select-option>
                            <a-select-option value="L:=">字段长度等于</a-select-option>
                            <a-select-option value="IN" v-if="record.WHERE_LEFT && record.WHERE_LEFT.split('▲')[1] !== 'L'">IN</a-select-option>
                        </a-select>
                    </template>
                    <template slot="WHERE_RIGHT" slot-scope="text, record,index">
                        <template v-if="record.WHERE_LEFT && record.WHERE_LEFT.split('▲')[1] === 'T'"><!--国库-->
                            <a-tree-select
                                    style="width:100%"
                                    treeCheckable
                                    showSearch
                                    checkStrictly
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="treasuryTreeData"
                                    v-model="record.WHERE_RIGHT"
                                    :maxTagCount="1">
                            </a-tree-select>
                        </template>
                        <template v-else-if="record.WHERE_LEFT && record.WHERE_LEFT.split('▲')[1] === 'B'"><!--核算主体-->
                            <a-tree-select
                                    style="width:100%"
                                    treeCheckable
                                    showSearch
                                    checkStrictly
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="accountingSubjectTreeData"
                                    v-model="record.WHERE_RIGHT"
                                    :maxTagCount="1">
                            </a-tree-select>
                        </template>
                        <template
                                v-else-if="record.WHERE_LEFT && record.WHERE_LEFT.split('▲')[1] === 'D'">
                            <a-select v-model="record.DATA_TYPE" style="width: 15%;"
                                      @change="record.dataStart = '';record.dataEnd = '';">
                                <a-select-option value="D">日</a-select-option>
                                <a-select-option value="M">月</a-select-option>
                                <a-select-option value="Y">年</a-select-option>
                            </a-select>
                            &nbsp;
                            <!--日期-->
                            <!--日-->
                            <template v-if="record.DATA_TYPE  === 'D'">
                                <el-date-picker
                                        style="width: 38%;"
                                        size="small"
                                        v-model="record.dataStart"
                                        type="date"
                                        placeholder="选择开始日期"
                                        value-format="yyyy-MM-dd"
                                        @change="handleDataChange(i,index)">
                                </el-date-picker>
                                ~
                                <el-date-picker
                                        style="width: 38%;"
                                        clearable
                                        size="small"
                                        v-model="record.dataEnd"
                                        type="date"
                                        placeholder="选择结束日期"
                                        value-format="yyyy-MM-dd"
                                        @change="handleDataChange(i,index)">
                                </el-date-picker>
                            </template>
                            <!--月-->
                            <template v-if="record.DATA_TYPE  === 'M'">
                                <el-date-picker
                                        style="width: 38%;"
                                        size="small"
                                        v-model="record.dataStart"
                                        type="month"
                                        placeholder="选择开始月份"
                                        value-format="yyyy-MM"
                                        @change="handleDataChange(i,index)">
                                </el-date-picker>
                                ~
                                <el-date-picker
                                        style="width: 38%;"
                                        clearable
                                        size="small"
                                        v-model="record.dataEnd"
                                        type="month"
                                        placeholder="选择结束月份"
                                        value-format="yyyy-MM"
                                        @change="handleDataChange(i,index)">
                                </el-date-picker>
                            </template>
                            <!--年-->
                            <template v-if="record.DATA_TYPE  === 'Y'">
                                <el-date-picker
                                        style="width: 38%;"
                                        size="small"
                                        v-model="record.dataStart"
                                        type="year"
                                        placeholder="选择开始年"
                                        value-format="yyyy"
                                        @change="handleDataChange(i,index)">
                                </el-date-picker>
                                ~
                                <el-date-picker
                                        style="width: 38%;"
                                        clearable
                                        size="small"
                                        v-model="record.dataEnd"
                                        type="year"
                                        placeholder="选择结束年"
                                        value-format="yyyy"
                                        @change="handleDataChange(i,index)">
                                </el-date-picker>
                            </template>
                            <!--<a-range-picker v-model="record.WHERE_RIGHT" style="width: 80%;"></a-range-picker>-->
                        </template>
                        <template v-else-if="record.WHERE_LEFT && record.WHERE_LEFT.split('▲')[1] === 'L'"><!--字段长度-->
                            <a-input-number style="width: 100%;"v-model="record.WHERE_RIGHT"/>
                        </template>
                        <template v-else>
                            <a-input v-model="record.WHERE_RIGHT"/>
                        </template>
                    </template>
                </a-table>
            </a-collapse-panel>
        </a-collapse>
    </a-modal>
</template>

<script>

    import {getGuokuTree, getOrgTree} from '@/api/nationalTreasury'
    import {getMainAll} from '@/api/dimensionTableApi'
    export default {
        name: "filterByModal",
        data() {
            return {
                confirmLoading: false,
                visibleModal: false,
                columns: [
                    {
                        title: '过滤字段',
                        dataIndex: 'WHERE_LEFT',
                        align: 'center',
                        width: '25%',
                        scopedSlots: {customRender: 'WHERE_LEFT'},
                    }, {
                        title: '过滤逻辑',
                        dataIndex: 'WHERE_MIDDLE',
                        align: 'center',
                        width: '25%',
                        scopedSlots: {customRender: 'WHERE_MIDDLE'},
                    }, {
                        title: '过滤值',
                        dataIndex: 'WHERE_RIGHT',
                        align: 'center',
                        width: '40%',
                        scopedSlots: {customRender: 'WHERE_RIGHT'},
                    }, {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        width: '10%',
                        scopedSlots: {customRender: 'action'}
                    }],
                listData: [],
                treasuryTreeData: [],//国库tree
                accountingSubjectTreeData: [],//核算主体tree
                columnList: [],
                columnList1:[], // 维度-过滤字段列表
                IS_COUNT: 'false',//是否需要计算
                TIME_COLUMN: '',//选择的时间字段
                indicatorDropDown: [],//指标下拉数据
                DIMENSION_ID: '', // 选择的维度id
                DIMENSION_COLUMN: '', // 维度表名称
              dimensionDropDown: [],//指标下拉数据
                oldData: {
                    IS_COUNT: 'false',
                    TIME_COLUMN: '',
                    listData: [],
                    indicatorDropDown: []
                }
            }
        },
        methods: {
            edit() {
                //国库
                getGuokuTree().then((res) => {
                    if (res.result === 'success') {
                        this.treasuryTreeData = res.rows
                    }
                });
                //核算主体
                getOrgTree().then((res) => {
                    if (res.result === 'success') {
                        this.accountingSubjectTreeData = res.rows
                    }
                });
                // 维度列表
                getMainAll().then((res)=>{
                  if (res.result === 'success') {
                    this.dimensionDropDown = res.rows
                  }

                })
                this.listData.map((item, index) => {
                    item.columnList.map(item1 => {
                        if (item1.FIELD_SIGN.split('▲')[1] === 'D')
                            if (this.listData.length > 1) {
                                this.indicatorDropDown.push({
                                    value: item1.FIELD_SIGN,
                                    name: `表${index + 1}.${item1.FIELD_NAME}`
                                })
                            } else {
                                this.indicatorDropDown.push({value: item1.FIELD_SIGN, name: item1.FIELD_NAME})
                            }
                    });
                    item.columnList.map(item1 => {
                        item1.disabled = false;
                    })
                    this.columnList1 = [...item.columnList]
                })
            },
            /*新增*/
            onAdd(index) {
                this.setColumnDisabled(index);
                this.listData[index].dataSource.push({
                    WHERE_LEFT: '',
                    WHERE_MIDDLE: '',
                    WHERE_RIGHT: '',
                    DATA_TYPE: 'D',
                    dataStart: '',
                    dataEnd: ''
                });
            },
            /*清空*/
            onClear(index) {
                // this.listData[index].dataSource = [];
                for (let key in this.listData[index].dataSource) {
                    if (this.listData[index].dataSource[key].WHERE_LEFT !== this.TIME_COLUMN) {
                        this.listData[index].dataSource.splice(key, 1);
                    }
                }
            },
            /*删除*/
            handleDel(i, index) {
                this.listData[i].dataSource.splice(index, 1);
                this.setColumnDisabled(index);
            },
            /*过滤字段 change*/
            onChange(i, index) {
                let data = this.listData[i].dataSource[index];
                if (data.WHERE_LEFT.split('▲')[1] === 'T' || data.WHERE_LEFT.split('▲')[1] === 'B') {
                    data.WHERE_RIGHT = [];
                } else {
                    data.WHERE_RIGHT = '';
                }
                data.WHERE_MIDDLE = ''
            },
            //复选框change
            onCheckChange(e) {
                this.IS_COUNT = e.target.checked.toString();
                if (this.IS_COUNT === 'false') {
                    this.TIME_COLUMN = '';
                }
            },
            /*确认*/
            handleOk() {
                let WHERE_LEFT = [],
                    WHERE_MIDDLE = [],
                    WHERE_RIGHT = [],
                    WHERE_TYPE = [],
                    DATA_TYPE = [],
                    flag = true;
                if (this.IS_COUNT === 'true' && !this.TIME_COLUMN) {
                    this.$message.warning('请选择计算指标的时间！');
                    return
                }
              if (!this.DIMENSION_ID) {
                this.$message.warning('请选择维度！');
                return
              }
              if (!this.DIMENSION_COLUMN ) {
                this.$message.warning('请选择与维度关联的字段！');
                return
              }
                // if (this.listData.length > 0 && this.listData[0].dataSource.length > 0) {
                this.listData.map(item => {
                    item.dataSource.map(item1 => {
                        try {
                            if (!item1.WHERE_LEFT) {
                                this.$message.error('请输入！');
                                flag = false;
                            }
                            if (item1.WHERE_LEFT.split('▲')[1] === 'T' || item1.WHERE_LEFT.split('▲')[1] === 'B' || item1.WHERE_LEFT.split('▲')[1] === 'D' || item1.WHERE_LEFT.split('▲')[1] === 'L') {
                                if (item1.WHERE_LEFT.split('▲')[1] === 'D') {
                                    if (!item1.dataStart || !item1.dataEnd) {
                                        this.$message.error('请输入开始或结束日期！');
                                        flag = false;
                                    }
                                } else {
                                    if (!item1.WHERE_RIGHT) {
                                        this.$message.error('请输入！');
                                        flag = false;
                                    }
                                }
                            } else {
                                if (!item1.WHERE_LEFT || !item1.WHERE_MIDDLE || !item1.WHERE_RIGHT) {
                                    this.$message.error('请输入1！');
                                    flag = false;
                                }
                            }
                            if(item1.WHERE_MIDDLE === 'IN' && item1.WHERE_LEFT.split('▲')[1] !== 'L' && item1.WHERE_RIGHT){
                              item1.WHERE_RIGHT = item1.WHERE_RIGHT.replace(/[(\r\n)|(\ +)|(，)|(,,)]/g,',').replace(/[(\‘)|(\’)]/g,'\'')
                              let pattern = /(\'|’)+[a-zA-Z0-9\u4e00-\u9fa5\,，]+(\'|‘)$/;
                              if(pattern.test(item1.WHERE_RIGHT)){
                                // item1.WHERE_RIGHT = item1.WHERE_RIGHT.replace(/[(\r\n)|(\ +)|(，)|(,,)]/g,',').replace(new RegExp(',+',"gm"),',').replace(/[(\‘)|(\’)]/g,'\'')
                                item1.WHERE_RIGHT = item1.WHERE_RIGHT.replace(new RegExp(',+',"gm"),',')
                                // console.log('正确',item1.WHERE_RIGHT )
                                // flag = false;
                              }else {
                                this.$message.error("格式不正确，例：'字段一','字段二'");
                                // console.log(item1.WHERE_RIGHT)
                                flag = false;
                              }
                            }
                            WHERE_LEFT.push(item1.WHERE_LEFT.split('▲')[0]);
                            if(item1.WHERE_MIDDLE === 'IN') {
                              WHERE_TYPE.push('O');
                            }else {
                              WHERE_TYPE.push(item1.WHERE_LEFT.split('▲')[1]);
                            }

                            DATA_TYPE.push(item1.WHERE_LEFT.split('▲')[1] === 'D' ? item1.DATA_TYPE : ' ');
                            if (item1.WHERE_MIDDLE === 'like') {
                                WHERE_RIGHT.push(`%${item1.WHERE_RIGHT}%`)
                            } else if (item1.WHERE_MIDDLE === 'not like') {
                                WHERE_RIGHT.push(`%${item1.WHERE_RIGHT}%`)
                            } else if (item1.WHERE_MIDDLE === ' like') {
                                WHERE_RIGHT.push(`${item1.WHERE_RIGHT}%`)
                            } else if (item1.WHERE_MIDDLE === 'like ') {
                                WHERE_RIGHT.push(`%${item1.WHERE_RIGHT}`)
                            } else if (item1.WHERE_LEFT.split('▲')[1] === 'T' || item1.WHERE_LEFT.split('▲')[1] === 'B') {
                                WHERE_RIGHT.push(item1.WHERE_RIGHT.join('▲'))
                            } else if (item1.WHERE_LEFT.split('▲')[1] === 'D') {
                                WHERE_RIGHT.push(`${item1.dataStart}▲${item1.dataEnd}`)
                            } else if (item1.WHERE_MIDDLE === 'IN') {
                                let incont = item1.WHERE_RIGHT.replace(/,/ig,'@')
                                WHERE_RIGHT.push('('+incont+')')
                            }else {
                                WHERE_RIGHT.push(item1.WHERE_RIGHT)
                            }
                            if (item1.WHERE_LEFT.split('▲')[1] === 'T' || item1.WHERE_LEFT.split('▲')[1] === 'B') {
                                WHERE_MIDDLE.push('in');
                            } else if (item1.WHERE_LEFT.split('▲')[1] === 'D') {
                                WHERE_MIDDLE.push('between');
                            } else {
                                WHERE_MIDDLE.push(item1.WHERE_MIDDLE);
                            }
                        } catch (e) {
                            this.$message.error('请输入！');
                            flag = false;
                        }
                    });
                });
                if (flag) {
                    if (this.IS_COUNT === 'true') {
                        if (!this.TIME_COLUMN) {
                            this.$message.warning('请选择选择计算指标的时间！');
                            return
                        }

                    }
                    if (!this.DIMENSION_ID) {
                      this.$message.warning('请选择选择计算指标的维度！');
                      return
                    }
                    if (!this.DIMENSION_COLUMN ) {
                      this.$message.warning('请选择维度！');
                      return
                    }
                    this.visibleModal = false;
                    this.oldData = JSON.parse(JSON.stringify({
                        IS_COUNT: this.IS_COUNT,
                        TIME_COLUMN: this.TIME_COLUMN,
                        listData: this.listData,
                        indicatorDropDown: this.indicatorDropDown
                    }));
                    this.$emit('ok', WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, DATA_TYPE, this.IS_COUNT, this.TIME_COLUMN,this.DIMENSION_ID,this.DIMENSION_COLUMN);
                }
                /*} else {
                  if (this.IS_COUNT === 'true') {
                    if (!this.TIME_COLUMN) {
                      this.$message.warning('请选择选择计算指标的时间！');
                      return
                    }
                  } else {
                    this.$emit('loadData');
                    this.$emit('ok', '', '', '', '', '', this.IS_COUNT, this.TIME_COLUMN);
                  }
                  this.visibleModal = false;
                }*/
            },
            //取消
            handleCancel() {
                this.visibleModal = false;
                Object.assign(this, JSON.parse(JSON.stringify(this.oldData)));
            },
            //指标计算change
            handleTimeColumnChange(value) {
                if (value)
                    if (this.listData.length > 1) {
                        let data = this.indicatorDropDown.filter(item => this.TIME_COLUMN === item.value).name.split('.')[0];
                        let flag = true;
                        this.listData[data.substring(1)].dataSource.map(item => {
                            if (item.WHERE_LEFT === value) {
                                flag = false;
                            }
                        });
                        if (flag)
                            this.listData[data.substring(1)].dataSource.push({
                                WHERE_LEFT: value,
                                WHERE_MIDDLE: '',
                                WHERE_RIGHT: '',
                                DATA_TYPE: 'D',
                                dataStart: '',
                                dataEnd: ''
                            });
                    } else {
                        let flag = true;
                        this.listData[0].dataSource.map(item => {
                            if (item.WHERE_LEFT === value) {
                                flag = false;
                            }
                        });
                        if (flag)
                            this.listData[0].dataSource.push({
                                WHERE_LEFT: value,
                                WHERE_MIDDLE: '',
                                WHERE_RIGHT: '',
                                DATA_TYPE: 'D',
                                dataStart: '',
                                dataEnd: ''
                            });
                    }
            },
          handleMainColumnChange(mainId) {
            this.DIMENSION_ID = mainId
          },
          handleMainColumnChange1(mainId) {
            this.DIMENSION_COLUMN  = mainId
          },
            //日期change
            handleDataChange(dataIndex, tableIndex) {
                let row = this.listData[dataIndex].dataSource[tableIndex];
                if (row.dataStart && row.dataEnd)
                    if (row.dataStart > row.dataEnd) {
                        row.dataStart = '';
                        row.dataEnd = '';
                        this.$message.warning('开始日期不能大于结束日期！');
                    }
            },
            //设置 column 是否禁用属性
            setColumnDisabled(index) {
                this.listData[index].columnList.map(item1 => {
                    item1.disabled = false;
                });
                this.listData[index].dataSource.map(item => {
                    this.listData[index].columnList.map(item1 => {
                        if (item.WHERE_LEFT === item1.FIELD_SIGN) {
                            item1.disabled = true;
                        }
                    })
                });
            },
            filterOption(input, option) {
                return (
                    option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
                );
            }
        }
    }
</script>

<style scoped>


    .ant-btn {
        margin-right: 8px;
    }
</style>