<template>
<a-spin :spinning="spinning">
    <a-card :bordered="false" class="card-area">
        <!-- <div class="leftLine" style="width:1px;height:30px;background:#ccc;position: absolute;
    left: 32px+25%;top:-5px;"></div> -->
        <!-- 查询区域 -->
        <!-- <div class="table-page-search-wrapper">
      <a style="padding-left:2px;" @click="toggleAdvanced">
                {{ advanced ? '收    起' : '展    开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>

    </div> -->
        <div id="model" class="model">
            <div class="model-left" style="border-right:1px solid #ccc;" v-if="advanced">
                <a-form layout="inline" :form="form">
                    <a-row>
                        <a-col :md="24" :sm="24">
                            <a-form-item label="维    度" :labelCol="{span: 4}" :wrapperCol="{span: 18, offset: 1}"
                                         style="width:100%;">
                                <a-select
                                        v-model="queryParam.dimensionFlag"
                                        placeholder="请输入维度"
                                        @change="select"

                                >
                                    <a-select-option
                                            :value="d.id"
                                            v-for="d in PLAN_DIMEN_OPTIONS"
                                            :key="d.id"
                                    >{{d.label}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="24" :sm="24" style="margin:10px 0;">
                            <a-form-item label="周    期" :labelCol="{span: 4}" :wrapperCol="{span: 18, offset:1}"
                                         style="width:100%;">
                                <a-select
                                        v-model="queryParam.periodFlag"
                                        placeholder="请输入周期"
                                        @change="select1"

                                >
                                    <a-select-option
                                            :value="d.id"
                                            v-for="d in PLAN_CYCLE_OPTIONS"
                                            :key="d.id"
                                    >{{d.label}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
                <div class="model-title">公共指标
                    <span
                            style="float:right;margin-right:10px;color: #1890FF;font-weight:normal;cursor:pointer;"
                    ><a-icon type="reload" @click="reload" style="margin-right:15px;color: #868686;"/><span
                            @click="downnew">指标导出</span></span>
                </div>
                <a-row>
                    <a-col :md="24" :sm="24">
                        <!-- <el-input
              placeholder="请输入指标关键字查询"
              v-model="queryParam.filterText"
              icon="circle-close"
              :on-icon-click="handleIconClick"
              class="wd228"
            ></el-input>
            <a-button  @click="getDatas" style="margin-left:20px;border:1px solid rgba(24, 144, 255, 1);color:rgba(24, 144, 255, 1)">搜索</a-button> -->
                        <a-input-search style="padding:4px 6px 4px 0;" placeholder="请输入指标关键字查询"
                                        v-model="queryParam.filterText" @change="getDatas"></a-input-search>
                    </a-col>
                </a-row>
                <div style="position:relative;">
                    <el-tree
          id="filter-tree"
          v-loading="loading"
          :data="treeData"
          :props="defaultProps"
          :show-checkbox="(queryParam.dimensionFlag===undefined||queryParam.periodFlag===undefined)?false:true"
          default-expand-all
          :check-strictly="true"
          @check="handleCheck"
          @node-click="handleNode"
          ref="tree"
          @node-contextmenu="rightClick"
          node-key="id"
          highlight-current
        >
          <span class="span-ellipsis" style="z-index:10;" slot-scope="{ node, data }">
            <span v-on:mouseover="enter($event,node)" v-on:mouseleave="leave($event,node)">{{ node.label.split('▲')[0] }}</span>
          </span>
                    </el-tree>
                    <div v-if="dragVisible" draggable="true" class="dragfile"
                         style="width:100%;position:absolute;top:0px;cursor:move;"></div>
                </div>
                <div id="perTreeMenu1" @click="getInfo" v-if="menuVisible1" class="tree_menu1" :style="{...rightMenu1}">
                    <!-- <ul>
              <li @click="getInfo"><i class="el-icon-tickets"></i> 详情</li>
            </ul> -->
                    {{INDEX_DESCR}}
                    <a v-if="rest.INDEX_DETAILS" style="display:block;float:right;margin-top:20px"
                       @click="getInfo">详情</a>
                </div>
                <div id="perTreeMenu" v-if="menuVisible" class="tree_menu" :style="{...rightMenu}">
                    <ul>
                        <li @click="getInfo"><i class="el-icon-tickets"></i> 详情</li>
                    </ul>
                </div>
                <div class="model-title" style="margin-top:20px;">
                    我的指标
                    <span
                            style="float:right;margin-right:10px;color: #1890FF;font-weight:normal;cursor:pointer;"

                    ><a-icon type="reload" @click="reloads" style="margin-right:15px;color: #868686;"/><span
                            @click="opennew">新增</span></span>
                </div>
                <a-row>
                    <a-col :md="24" :sm="24">
                        <!-- <el-input
              placeholder="请输入指标关键字查询"
              v-model="queryParam.filterTexts"
              icon="circle-close"
              :on-icon-click="handleIconClick"
              class="wd228"
            ></el-input>
            <a-button  style="margin-left:20px;border:1px solid rgba(24, 144, 255, 1);color:rgba(24, 144, 255, 1);" @click="getDatas2">搜索</a-button> -->
                        <a-input-search style="padding:4px 6px 4px 0;" placeholder="请输入指标关键字查询"
                                        v-model="queryParam.filterTexts" @change="getDatas2"></a-input-search>
                    </a-col>
                </a-row>
                <div style="position:relative;">
                    <el-tree
                            id="filter-tree1"
                            :data="treeDatas"
                            :props="defaultProps"
                            v-loading="loading1"
                            :show-checkbox="(queryParam.dimensionFlag===undefined||queryParam.periodFlag===undefined)?false:true"
                            default-expand-all
                            :check-strictly="true"
                            @check="handleCheck1"
                            @node-click="handleNode1"
                            ref="tree1"
                            node-key="id"
                            highlight-current
                    >
          <span class="span-ellipsis" style="z-index:10;" slot-scope="{ node, data }">
            <span v-on:mouseover="enter($event,node)" v-on:mouseleave="leave($event,node)">{{ node.label.split('▲')[0] }}</span>
          </span>
                    </el-tree>
                    <div v-if="dragVisible1" draggable="true" class="dragfile1"
                         style="width:100%;position:absolute;cursor:move;top:0px;"></div>
                </div>
            </div>
            <div class="line" v-if="lineDisplay"
                 style="float:left;height:615px;width:1px;background:rgb(204, 204, 204);"></div>
            <div class="model-middle">
                <!-- <button :disabled="disable" @click="moveRight"><icon-font type="icon-xiangzuo" style="font-size:28px;" /></button> -->
                <!-- <a-button type="primary" icon="arrow-right" @click="moveicon-fontRight" :disabled="disable"></a-button> -->
                <button @click="toggleAdvanced">
                    <icon-font :type="this.advanced===false?'icon-xiangyoushuangjiantou':'icon-xiangzuoshuangjiantou'"
                               style="font-size:18px;margin-right:5px;"/>
                </button>
            </div>
            <div class="model-right" id="model-right">
                <a-form :form="form1">
                    <a-row style="margin-bottom:20px;">
                        <a-col :md="10" :sm="10" v-if="showSearch">
                            <a-form-item
                                    label="账期"
                                    :labelCol="{span: 2}"
                                    :wrapperCol="{span: 19, offset: 1}"
                                    v-if="check1"
                                    :validate-status="validateStatus2||validateStatus6"

                            >
                                <span slot="help">{{ validateStatus2=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                                <span style="color:red;" slot="help">{{ validateStatus6=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                                <!-- <a-range-picker style="width: 100%" v-model="model.startDate" /> -->
                                <a-date-picker style="width: 46%"
                                               placeholder="请选择日期" v-model="model.startDate"/>
                                ~
                                <a-date-picker style="width: 46%"
                                               placeholder="请选择日期" v-model="model.endDate"/>
                            </a-form-item>
                            <a-form-item
                                    label="账期"
                                    :labelCol="{span: 2}"
                                    :wrapperCol="{span: 19, offset: 1}"
                                    v-if="check2"
                                    :validate-status="validateStatus2||validateStatus7"

                            >
                                <span slot="help">{{ validateStatus2=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                                <span slot="help" style="color:red;">{{ validateStatus7=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                                <a-month-picker
                                        style="width: 46%"
                                        placeholder="请选择月份"
                                        v-model="model.startDate"
                                />
                                ~
                                <a-month-picker
                                        style="width: 46%"
                                        placeholder="请选择月份"
                                        v-model="model.endDate"
                                />
                            </a-form-item>
                            <a-form-item
                                    label="账期"
                                    :labelCol="{span: 2}"
                                    :wrapperCol="{span: 19, offset: 1}"
                                    v-if="check3"
                                    :validate-status="validateStatus2||validateStatus8"

                            >
                                <span slot="help">{{ validateStatus2=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                                <span slot="help" style="color:red;">{{ validateStatus8=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                                <!-- <a-select v-model="model.startDate" placeholder="请选择年份" style="width:46%;">
                <a-icon slot="suffixIcon" type="calendar" />
                <a-select-option
                  :value="d.id"
                  v-for="d in YEAR_OPTIONS"
                  :key="d.id"
                >{{d.label}}</a-select-option></a-select> -->
                                <a-date-picker placeholder="请输入年度查询" mode="year" style="width: 46%"
                                               @panelChange="e=>{model.startDate = e;yearOpen = false;}" format="YYYY"
                                               v-model="model.startDate" :open="yearOpen"
                                               @focus="yearOpen = true"></a-date-picker>
                                ~
                                <a-date-picker placeholder="请输入年度查询" mode="year" style="width: 46%"
                                               @panelChange="e=>{model.endDate = e;yearOpen1 = false;}" format="YYYY"
                                               v-model="model.endDate" :open="yearOpen1"
                                               @focus="yearOpen1 = true"></a-date-picker>
                                <!-- <a-select v-model="model.endDate" placeholder="请选择年份" style="width:46%;">
                <a-icon slot="suffixIcon" type="calendar" />
                <a-select-option
                  :value="d.id"
                  v-for="d in YEAR_OPTIONS"
                  :key="d.id"
                >{{d.label}}</a-select-option>
              </a-select> -->
                            </a-form-item>
                            <a-form-item
                                    label="账期"
                                    :labelCol="{span: 2}"
                                    :wrapperCol="{span: 19, offset: 1}"
                                    v-if="check4"
                                    :validate-status="validateStatus2||validateStatus9"

                            >
                                <span slot="help">{{ validateStatus2=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                                <span slot="help" style="color:red;">{{ validateStatus9=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                                <data-month
                                        :choseQuarter="startquarter"
                                        :choseQuarter1="endquarter"
                                        @startquarter="startquarter1"
                                        @endquarter="endquarter1"
                                ></data-month>
                            </a-form-item>
                        </a-col>
                        <a-col :md="10" :sm="10" v-if="showSearch">
                            <a-form-item
                                    label="国库"
                                    :labelCol="{span: 2}"
                                    :wrapperCol="{span: 19, offset:1}"
                                    hasFeedback
                                    :validate-status="validateStatus1"
                                    v-if="status1"

                            >
                                <span slot="help">{{ validateStatus1=='error'?'请选择国库':'&nbsp;&nbsp;' }}</span>
                                <a-tree-select
                                        style="width:100%"
                                        showSearch
                                        multiple
                                        :maxTagCount="1"
                                        treeNodeFilterProp="label"
                                        :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                        :treeData="guokuData"
                                        v-model="model.dimCode"
                                        placeholder="请选择国库"
                                ></a-tree-select>
                            </a-form-item>
                            <a-form-item
                                    label="地区"
                                    :labelCol="{span: 2}"
                                    :wrapperCol="{span: 19, offset: 1}"
                                    v-if="status2"
                                    :validate-status="validateStatus1"

                            >
                                <span slot="help">{{ validateStatus1=='error'?'请选择地区':'&nbsp;&nbsp;' }}</span>
                                <a-tree-select
                                        style="width:100%"
                                        multiple
                                        :maxTagCount="1"
                                        :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                        :treeData="areaTreeData"
                                        v-model="model.dimCode"
                                        placeholder="请选择地区"
                                ></a-tree-select>
                            </a-form-item>
                            <a-form-item
                                    label="核算主体"
                                    :labelCol="{span: 5}"
                                    :wrapperCol="{span: 19, offset:1}"
                                    hasFeedback
                                    required
                                    :validate-status="validateStatus1"
                                    v-if="status3"
                            >
                                <span slot="help">{{ validateStatus1=='error'?'请选择核算主体':'&nbsp;&nbsp;' }}</span>
                                <a-tree-select
                                        style="width:100%"
                                        showSearch
                                        multiple
                                        :maxTagCount="1"
                                        treeNodeFilterProp="label"
                                        :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                        :treeData="hesuanData"
                                        v-model="model.dimCode"
                                        placeholder="请选择检查机构"
                                ></a-tree-select>
                            </a-form-item>
                        </a-col>

                        <a-col :md="4" :sm="4" v-if="showSearch">
                            <a-form-item
                                    label="单位"
                                    :labelCol="{span: 7}"
                                    :wrapperCol="{span: 15, offset:1}"
                                    hasFeedback
                                    :validate-status="validateStatus3"
                            >
                                <span slot="help">{{ validateStatus3=='error'?'请选择金额单位':'&nbsp;&nbsp;' }}</span>
                                <a-select
                                        v-model="price"
                                        placeholder="请选择金额单位"
                                        style="width:100%;"
                                        @select="selectPrices"
                                >
                                    <a-select-option
                                            :value="d.id"
                                            v-for="d in PRICE_OPTIONS"
                                            :key="d.id"
                                    >{{d.label}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>

                        <a-col :md="24" :sm="24" style="margin-top:12px;">
                            <a-button type="primary" @click="getData">查询</a-button>
                            <a-button type="primary" style="margin-left: 8px"
                                      @click="model = {},startquarter = '',endquarter = '',validateStatus1 = '',validateStatus2 = '',validateStatus3 = '',obj33 = {},price='1',selectPrice='1', getData(1)">
                                重置条件
                            </a-button>
                            <!-- <a-button type="primary" style="margin-left: 8px"   @click="saveplan">保存方案</a-button> -->
                            <a-button
                                    type="primary"
                                    style="margin-left: 8px"
                                    @click="clearAll"
                            >清空
                            </a-button>
                            <img class="chart" @click="download" style="margin-left: 8px;width:32px;"
                                 src="~@/assets/5.png" alt="下载" title="下载"/>
                            <img class="chart" @click="showChart" style="margin-left: 8px;width:32px;"
                                 src="~@/assets/z.png" alt="图表" title="图表"/>
                            <img class="chart" @click="checkplan" style="margin-left: 8px;width:32px;"
                                 src="~@/assets/3.png" alt="查看方案" title="查看方案"/>
                            <img class="chart" @click="saveplan" style="margin-left: 8px;width:32px;"
                                 src="~@/assets/6.png" alt="保存方案" title="保存方案"/>
                            <img class="chart" @click="filter" style="margin-left: 8px;width:32px;"
                                 src="~@/assets/s.png" alt="组合过滤" title="组合过滤"/>
                            <img class="chart" @click="sort" style="margin-left: 8px;width:32px;" src="~@/assets/2.png"
                                 alt="组合排序" title="组合排序"/>
                            <!-- <icon-font
              title="下载"
              class="chart"
                type="icon-xiazai1"
                style="margin-left: 8px;font-size:28px;"

                @click="download"
              />
              <icon-font title="图表" type="icon-tubiaozhuzhuangtu" class="chart" @click="showChart" ></icon-font>
              <icon-font title="查看方案" class="chart" type="icon-wj-fa" style="margin-left: 8px;font-size:28px;" @click="checkplan" />
              <icon-font title="保存方案" class="chart" type="icon-baocun" style="margin-left: 8px;font-size:28px;" @click="saveplan" />
              <icon-font
                title="组合过滤"
                class="chart"
                type="icon-guolv"
                style="margin-left: 8px;font-size:28px;"

                @click="filter"
              />
              <icon-font
                title="组合排序"
                class="chart"
                type="icon-paixu2"
                style="margin-left: 8px;font-size:28px;"

                @click="sort"
              /> -->
                        </a-col>
                    </a-row>
                </a-form>
                <v-table
                        ref="vTable"
                        :tableData="tableData"
                        :loadings="spinning"
                        :tableDatas="tableDatas"
                        :obj="obj"
                        :level="level"
                        :price="price"
                        :record="record"
                        :dataSource2="dataSource2"
                        :planStatus="planStatus"
                        @del="del"
                        @toLoad="toLoad"
                        @shows="shows"
                ></v-table>
                <div class="page">
                    <el-pagination
                            @current-change="handleCurrentChange"
                            :page-size="100"
                            :current-page.sync="currentPage"
                            layout="total, prev, pager, next, jumper"
                            :total="total"
                    ></el-pagination>
                </div>
                <div id="models">
                    <a-modal
                            title="查看方案"
                            width="40%"
                            :visible="visible4"
                            @ok="handlecheckOk"
                            @cancel="handlecheckCancel"
                            :maskClosable="false"
                    >
                        <a-row style="margin-bottom:20px;">
                            <a-col :md="24" :sm="24">
                                指标名称：
                                <el-input
                                        placeholder="请输入指标关键字查询"
                                        v-model="queryParam.filterKeyword"
                                        icon="circle-close"
                                        :on-icon-click="handleIconClick"
                                        class="wd229"
                                ></el-input>
                                <a-button type="primary" @click="getFilterName" style="margin-left:20px;">搜索</a-button>
                            </a-col>
                        </a-row>
                        <!-- <a-radio-group @change="onChanges" v-model="esql">
                            <a-radio-button value="0">我的方案</a-radio-button>
                            <a-radio-button value="1">公共方案</a-radio-button>
                        </a-radio-group> -->
                        <div id="table22">
                            <a-table
                                    bordered
                                    rowKey="ID"
                                    :dataSource="dataSource2"
                                    :columns="columns2"
                                    :pagination="pagination"
                                    @change="handleTableChange"
                                    style="margin-top:10px;"
                            >
                                <template slot="action" slot-scope="text, record">
                                    <a @click="handleRun(record)">执行</a>
                                    <a-divider type="vertical"/>
                                    <!-- <a v-if="esql==='0'" @click="handleSubmit(record)">提交</a>
                                    <a-divider v-if="esql==='0'" type="vertical"/> -->
                                    <a @click="handleGallery(record)" :disabled="record.IS_PUSH=='0'">推送至图库</a>
                                    <a-divider type="vertical"/>
                                    <a v-if="esql==='0'" @click="handleEdit(record)">编辑</a>
                                    <a-divider v-if="esql==='0'" type="vertical"/>
                                    <a @click="handleDelete(record)">删除</a>
                                </template>
                            </a-table>
                        </div>
                    </a-modal>

                </div>
                <a-modal title="编辑方案" width="40%" :visible="visible10" @ok="handleSubmit2" @cancel="handleCancels2"
                         :maskClosable="false">
                    <a-form>
                        <a-form-item label="方案描述" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
                            <a-input
                                    v-model="planDesc"
                            />
                        </a-form-item>
                    </a-form>
                </a-modal>
                <a-modal
                        title="选择图表类型"
                        width="80%"
                        :visible="visibles"
                        @cancel="handlechartCancel"
                        :maskClosable="false"
                >
                    <div class="flex-radio">
                        <a-radio-group @change="onChange" v-model="chartvalue">
                            <a-radio :value="1"><img style="vertical-align: middle;width:60px;" src="~@/assets/9.png"
                                                     alt="柱状图" title="柱状图"/>
                                <!-- <icon-font type="icon-zhuzhuangtu1" style="font-size:60px;vertical-align: middle; "/> -->
                            </a-radio>
                            <a-radio :value="2"><img style="vertical-align: middle;width:60px;" src="~@/assets/8.png"
                                                     alt="折线图" title="折线图"/>
                                <!-- <icon-font type="icon-zhexiantu" style="font-size:60px;vertical-align: middle; "/> -->
                            </a-radio>
                            <a-radio :value="3"><img style="vertical-align: middle;width:60px;" src="~@/assets/10.png"
                                                     alt="饼状图" title="饼状图"/>
                                <!-- <icon-font type="icon-bingtu" style="font-size:60px;vertical-align: middle; "/> -->
                            </a-radio>
                            <a-radio :value="4"><img style="vertical-align: middle;width:60px;" src="~@/assets/7.png"
                                                     alt="柱状折线图" title="柱状折线图"/>
                                <!-- <icon-font type="icon-zhuzhuangzhexiantu" style="font-size:60px;vertical-align: middle; "/> -->
                            </a-radio>


                        </a-radio-group>
                        <a-row v-if="chartvalue===1" style="margin-top:20px;">
                            <a-col :md="11" :sm="11">
                                <a-form-item label="横轴显示" :labelCol="{span:4}" :wrapperCol="{span: 18, offset:1}">
                                    <a-select
                                            v-model="xTurn"
                                            placeholder="横轴显示"
                                            allowClear
                                            @select="select4"
                                            style="width:100%;"
                                    >
                                        <a-select-option value="X">账期</a-select-option>
                                        <a-select-option v-if="queryParam.dimensionFlag=='1' " value="Y">国库
                                        </a-select-option>
                                        <a-select-option v-if="queryParam.dimensionFlag=='2' " value="Y">地区
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                                </a-form-item>
                            </a-col>
                            <a-col :md="11" :sm="11">
                                <a-form-item :label="queryParam.dimensionFlag=='1'?'国库':'地区'" :labelCol="{span:3}"
                                             :wrapperCol="{span: 18, offset:1}" v-if="toShow2">
                                    <a-select
                                            v-if="dimenOption.length>0"
                                            v-model="queryParam2.guokuId"
                                            placeholder="请输入维度"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option
                                                :value="d.value"
                                                v-for="d in dimenOption"
                                                :key="d.value"
                                        >{{d.label}}
                                        </a-select-option>
                                    </a-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='1'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   treeNodeFilterProp="label"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="guokuData"
                                                   v-model="queryParam2.guokuId"
                                                   placeholder="请选择国库"
                                    ></a-tree-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='2'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="areaTreeData"
                                                   v-model="queryParam2.guokuId"
                                                   placeholder="请选择地区"
                                    ></a-tree-select>
                                </a-form-item>

                                <a-form-item label="账期" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}"
                                             v-if="toShow3">
                                    <a-select
                                            v-model="queryParam2.dateId"
                                            placeholder="请选择具体账期"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option :value="d" v-for="d in periodFlagOption" :key="d">{{d}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>

                            <a-col :md="2" :sm="2">
                                <a-button type="primary" style="margin-top:4px;;" @click="setEcharts">确定</a-button>
                            </a-col>
                            <a-col :md="24" :sm="24" style="color:#777;text-align:left;font-size:14px;">注：纵轴显示金额及比率
                            </a-col>
                            <!-- <a-button type="primary" style="position:absolute;bottom:0px;right:10px;" @click="setEcharts">确定</a-button> -->
                            <a-col v-if="isTableEchart" :md="24" :sm="24"
                                   style="color:#777;text-align:left;font-size:14px;margin-top:20px;">
                                是否显示数值：
                                <a-switch @change="showDeals"/>
                            </a-col>
                        </a-row>
                        <a-row v-if="chartvalue===2" style="margin-top:20px;">
                            <a-col :md="11" :sm="11">
                                <a-form-item label="横轴显示" :labelCol="{span:4}" :wrapperCol="{span: 18, offset:1}">
                                    <a-select
                                            v-model="xTurn"
                                            placeholder="横轴显示"
                                            allowClear
                                            @select="select4"
                                            style="width:100%;"
                                    >
                                        <a-select-option value="X">账期</a-select-option>
                                        <a-select-option v-if="queryParam.dimensionFlag=='1' " value="Y">国库
                                        </a-select-option>
                                        <a-select-option v-if="queryParam.dimensionFlag=='2' " value="Y">地区
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                                </a-form-item>
                            </a-col>
                            <a-col :md="11" :sm="11">
                                <a-form-item :label="queryParam.dimensionFlag=='1'?'国库':'地区'" :labelCol="{span:3}"
                                             :wrapperCol="{span: 18, offset:1}" v-if="toShow2">
                                    <a-select
                                            v-if="dimenOption.length>0"
                                            v-model="queryParam2.guokuId1"
                                            placeholder="请输入维度"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option
                                                :value="d.value"
                                                v-for="d in dimenOption"
                                                :key="d.value"
                                        >{{d.label}}
                                        </a-select-option>
                                    </a-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='1'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   treeNodeFilterProp="label"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="guokuData"
                                                   v-model="queryParam2.guokuId1"
                                                   placeholder="请选择国库"
                                    ></a-tree-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='2'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="areaTreeData"
                                                   v-model="queryParam2.guokuId1"
                                                   placeholder="请选择地区"
                                    ></a-tree-select>
                                </a-form-item>

                                <a-form-item label="账期" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}"
                                             v-if="toShow3">
                                    <a-select
                                            v-model="queryParam2.dateId2"
                                            placeholder="请选择具体账期"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option :value="d" v-for="d in periodFlagOption" :key="d">{{d}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>

                            <a-col :md="2" :sm="2">
                                <a-button type="primary" style="margin-top:4px;;" @click="setEcharts">确定</a-button>
                            </a-col>
                            <a-col :md="24" :sm="24" style="color:#777;text-align:left;font-size:14px;">注：纵轴显示金额及比率
                            </a-col>
                            <!-- <a-button type="primary" style="position:absolute;bottom:0px;right:10px;" @click="setEcharts">确定</a-button> -->
                            <a-col v-if="isTableEchart" :md="24" :sm="24"
                                   style="color:#777;text-align:left;font-size:14px;margin-top:20px;">
                                是否显示数值：
                                <a-switch @change="showDeals"/>
                            </a-col>
                        </a-row>
                        <a-row v-if="chartvalue===3" style="margin-top:20px;">
                            <a-col :md="22" :sm="22">
                                <a-form-item label="统计方向" :labelCol="{span:2}" :wrapperCol="{span: 9}">
                                    <a-select
                                            v-model="queryParam.turnTo"
                                            placeholder="请输入统计方向"
                                            allowClear
                                            @select="select3"
                                            style="width:100%;"
                                    >
                                        <a-select-option value="X">横向</a-select-option>
                                        <a-select-option value="Y">纵向</a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="11" :sm="11">
                                <a-form-item label="账期" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}">
                                    <a-select
                                            v-model="queryParam2.periodFlag1"
                                            placeholder="请选择具体账期"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option :value="d" v-for="d in periodFlagOption" :key="d">{{d}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="11" :sm="11">
                                <a-form-item :label="queryParam.dimensionFlag=='1'?'国库':'地区'" :labelCol="{span:3}"
                                             :wrapperCol="{span: 18, offset:1}" v-if="toShow">
                                    <a-select
                                            v-if="dimenOption.length>0"
                                            v-model="queryParam2.dimensionFlag1"
                                            placeholder="请输入维度"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option
                                                :value="d.value"
                                                v-for="d in dimenOption"
                                                :key="d.value"
                                        >{{d.label}}
                                        </a-select-option>
                                    </a-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='1'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   treeNodeFilterProp="label"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="guokuData"
                                                   v-model="queryParam2.dimensionFlag1"
                                                   placeholder="请选择国库"
                                    ></a-tree-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='2'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="areaTreeData"
                                                   v-model="queryParam2.dimensionFlag1"
                                                   placeholder="请选择地区"
                                    ></a-tree-select>
                                </a-form-item>

                                <a-form-item label="指标" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}"
                                             v-if="toShow1">
                                    <a-select
                                            v-model="queryParam2.zhibiaoFlag1"
                                            placeholder="请输入维度"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option
                                                :value="d.INDEX_ID!==undefined?d.INDEX_ID:d.id"
                                                v-for="d in tableData"
                                                :key="d.INDEX_ID!==undefined?d.INDEX_ID:d.id"
                                        >{{d.INDEX_NAME!==undefined?d.INDEX_NAME:d.label.split('▲')[0]}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>

                            <a-button type="primary" style="position:absolute;bottom:30px;right:10px;"
                                      @click="pieChart">确定
                            </a-button>
                        </a-row>
                        <a-row v-if="chartvalue===4" style="margin-top:20px;">
                            <!-- <a-row style="text-align:left;">
                <a-col>
                  <a-button type="primary" @click="addRow1">新增</a-button>
                  <a-button style="margin-left:8px;" @click="clear1">清空</a-button>
                </a-col>
              </a-row> -->
                            <a-col :md="11" :sm="11">
                                <a-form-item label="横轴显示" :labelCol="{span:4}" :wrapperCol="{span: 18, offset:1}">
                                    <a-select
                                            v-model="xTurn"
                                            placeholder="横轴显示"
                                            allowClear
                                            @select="select4"
                                            style="width:100%;"
                                    >
                                        <a-select-option value="X">账期</a-select-option>
                                        <a-select-option v-if="queryParam.dimensionFlag=='1' " value="Y">国库
                                        </a-select-option>
                                        <a-select-option v-if="queryParam.dimensionFlag=='2' " value="Y">地区
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                                </a-form-item>
                            </a-col>
                            <a-col :md="11" :sm="11">
                                <!-- <a-form-item :label="queryParam.dimensionFlag=='1'?'国库':'地区'" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}" v-if="toShow2">
                  <a-select
                    v-model="queryParam2.guokuId1"
                    placeholder="请输入维度"
                    allowClear
                    style="width:100%;"
                  >
                    <a-select-option
                      :value="d.value"
                      v-for="d in dimenOption"
                      :key="d.value"
                    >{{d.label}}</a-select-option>
                  </a-select>
                </a-form-item> -->
                                <a-form-item :label="queryParam.dimensionFlag=='1'?'国库':'地区'" :labelCol="{span:3}"
                                             :wrapperCol="{span: 18, offset:1}" v-if="toShow2">
                                    <a-select
                                            v-if="dimenOption.length>0"
                                            v-model="queryParam2.guokuId2"
                                            placeholder="请输入维度"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option
                                                :value="d.value"
                                                v-for="d in dimenOption"
                                                :key="d.value"
                                        >{{d.label}}
                                        </a-select-option>
                                    </a-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='1'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   treeNodeFilterProp="label"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="guokuData"
                                                   v-model="queryParam2.guokuId2"
                                                   placeholder="请选择国库"
                                    ></a-tree-select>
                                    <a-tree-select v-if="queryParam.dimensionFlag=='2'&&dimenOption.length===0"
                                                   style="width:100%"
                                                   :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                                   :treeData="areaTreeData"
                                                   v-model="queryParam2.guokuId2"
                                                   placeholder="请选择地区"
                                    ></a-tree-select>
                                </a-form-item>
                                <a-form-item label="账期" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}"
                                             v-if="toShow3">
                                    <a-select
                                            v-model="queryParam2.dateId1"
                                            placeholder="请选择具体账期"
                                            allowClear
                                            style="width:100%;"
                                    >
                                        <a-select-option :value="d" v-for="d in periodFlagOption" :key="d">{{d}}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>

                            <a-col :md="24" :sm="24"
                                   style="color:#777;text-align:left;font-size:14px;margin-bottom:10px;">注：纵轴显示金额及比率
                            </a-col>
                            <el-table :data="dataSource1" size="small" border empty-text="暂无数据"
                                      style="width: 100%;margin-top:10px;">
                                <el-table-column
                                        prop="chartname"
                                        label="指标"
                                        show-overflow-tooltip
                                        width="auto"
                                        align="center"
                                >
                                    <template slot-scope="scope">
                                        <!-- <a-select
                    v-model="scope.row.chartname"
                    placeholder="请输入指标"
                    allowClear
                    style="width:100%;"
                  >
                    <a-select-option
                      :value="d.INDEX_ID!==undefined?d.INDEX_ID:d.id"
                      v-for="d in tableData"
                      :key="d.INDEX_ID!==undefined?d.INDEX_ID:d.id"
                    >{{d.INDEX_NAME!==undefined?d.INDEX_NAME:d.label.split('▲')[0]}}</a-select-option>
                  </a-select> -->
                                        <span>{{scope.row.chartname}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="chartDirection" label="方向" width="auto" align="center">
                                    <template slot-scope="scope">
                                        <a-select v-model="scope.row.chartDirection" style="width:100%;">
                                            <a-select-option value="Columnar">柱状图</a-select-option>
                                            <a-select-option value="Line">折线图</a-select-option>
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
                            <a-row style="margiin-top:20px;">
                                <a-button type="primary" style="float:left;" @click="setEcharts">确定</a-button>
                            </a-row>
                            <a-col v-if="isTableEchart" :md="24" :sm="24"
                                   style="color:#777;text-align:left;font-size:14px;margin-top:20px;">
                                是否显示数值：
                                <a-switch @change="showDeals"/>
                            </a-col>
                            <!-- <a-col :md="11" :sm="11">
                <a-form-item label="指标" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}">
                  <a-select
                    v-model="queryParam.zhibiaoFlag1"
                    placeholder="请输入指标"
                    allowClear
                    style="width:100%;"
                  >
                    <a-select-option
                      :value="d.INDEX_ID!==undefined?d.INDEX_ID:d.id"
                      v-for="d in tableData"
                      :key="d.INDEX_ID!==undefined?d.INDEX_ID:d.id"
                    >{{d.INDEX_NAME!==undefined?d.INDEX_NAME:d.label.split('▲')[0]}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="11" :sm="11">
                <a-form-item label="图形" :labelCol="{span:3}" :wrapperCol="{span: 18, offset:1}">
                  <a-select
                    v-model="queryParam.chart"
                    placeholder="请输入指标"
                    allowClear
                    style="width:100%;"
                  >
                    <a-select-option value="X">柱状图</a-select-option>
                    <a-select-option value="Y">折线图</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-button type="primary" style="position:absolute;bottom:30px;" @click="zhulineChart">确定</a-button> -->
                        </a-row>
                    </div>
                    <column-chart :echartsData="onlineData" v-if="isTableEchart"></column-chart>
                    <template slot="footer">
                        <a-button @click="handlechartCancel()">取消</a-button>
                    </template>
                </a-modal>
                <a-modal title="指标详情" width="40%" :visible="visible0" @cancel="handleCancels1" @ok="handleCancels1"
                         :maskClosable="false">
                    <span>{{rest.INDEX_DETAILS}}</span>
                </a-modal>
            </div>
        </div>
    </a-card>
</a-spin>
</template>

<script>
    const loadmore = {
        bind(el, binding) {
            const selectWrap = el.querySelector('.el-table__body-wrapper')
            selectWrap.addEventListener('scroll', function () {
                let sign = 0
                //console.log(scrollDistance,this.scrollHeight,this.scrollTop,this.clientHeight)
                const scrollDistance = this.scrollHeight - this.scrollTop - (this.clientHeight)
                console.log(scrollDistance, this.scrollHeight, this.scrollTop, this.clientHeight)
                if (scrollDistance <= sign) {
                    binding.value()
                }
            })
        }
    }

    import vTable from './vTable'
    import dataMonth from './component/dataMonth'
    import columnChart from './component/columnChart'
    import {
        getEnumTypeAll,
        getGuokuTree,
        getOrgTree,
        getAreaTree,
        selectIndexRelationTree,
        getIndicatorsTable,
        selectSchemeTable,
        selectSchemeData,
        deleteScheme,
        downLoadSchemeData,
        getIndicatorsECharts,
        getDateInterval,
        getDimensionSelect,
        getIndexDetails,
        downloadIndexInfo,
        deletePublicScheme,
        getColumnLineChart,
        submitScheme,
        schemeRename,
        getIndexJurisdiction,
        getIndexLevels,
        pushIndexToVS
    } from '@/api/nationalTreasury'
    import pick from 'lodash.pick'

    export default {
        name: 'indexLibrary',
        data() {
            return {
                advanced: true,
                form: this.$form.createForm(this),
                form1: this.$form.createForm(this),
                label: {
                    name: '级' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '次',
                },
                queryParam: {},
                queryParam2: {},
                level: '',
                status1: true,
                status2: false,
                status3: false,
                check1: true,
                check2: false,
                check3: false,
                check4: false,
                visible4: false,
                visibles: false,
                menuVisible: false,
                menuVisible1: false,
                treeId: '',
                chartvalue: '',
                validateStatus1: '',
                validateStatus2: '',
                validateStatus3: '',
                validateStatus6: '',
                validateStatus7: '',
                validateStatus8: '',
                validateStatus9: '',
                model: {price: 1},
                obj: {},
                obj33: {},
                aq: true,
                guokuData: [],
                hesuanData: [],
                areaTreeData: [],
                dataSource2: [],
                record: {},
                /* 分页参数 */
                pagination: {
                    current: 1,
                    total: 0,
                    pageSize: 10, //每页中显示10条数据
                    showSizeChanger: true,
                    pageSizeOptions: ['10', '20', '50', '100'], //每页中显示的数据
                    showTotal: total => `共有 ${total} 条数据` //分页中显示总的数据
                },
                onlineData: {},
                isTableEchart: false,
                PLAN_DIMEN_OPTIONS: [
                    {
                        id: '1',
                        label: '国库'
                    },
                    {
                        id: '2',
                        label: '地区'
                    },
                    /* {
          id: '3',
          label: '账期+核算主体'
        } */
                ],
                price: '1',
                PRICE_OPTIONS: [
                    {
                        id: '1',
                        label: '元'
                    },
                    {
                        id: '10000',
                        label: '万元'
                    },
                    {
                        id: '100000000',
                        label: '亿元'
                    }
                ],
                PLAN_CYCLE_OPTIONS: [
                    {
                        id: '1',
                        label: '日'
                    },
                    {
                        id: '2',
                        label: '月'
                    },
                    {
                        id: '3',
                        label: '季'
                    },
                    {
                        id: '4',
                        label: '年'
                    }
                ],
                filterText: '',
                treeData: [],
                obj3: [],
                treeDatas: [],
                defaultProps: {
                    children: 'children',
                    label: 'label',
                    disabled: 'disabled'
                },
                tableData: [],
                tableDatas: [],
                dataSource1: [],
                modelTargetData: null,
                currentSelect: [],
                typeName: null,
                showSearch: false,
                condition: [],
                status: false,
                startquarter: '',
                endquarter: '',
                dataSource2: [],
                chartData: [],
                total: 1,
                pageNo: 1,
                currentPage: 1,
                checkValue: '',
                periodFlagOption: [],
                dimenOption: [],
                columns2: [
                    {
                        title: '方案描述',
                        dataIndex: 'SCHEME_DESCR',
                        width: '55%',
                        align: 'center'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: {customRender: 'action'}
                    }
                ],
                YEAR_OPTIONS: [
                    {
                        id: '2010',
                        label: '2010'
                    },
                    {
                        id: '2011',
                        label: '2011'
                    },
                    {
                        id: '2012',
                        label: '2012'
                    },
                    {
                        id: '2013',
                        label: '2013'
                    },
                    {
                        id: '2014',
                        label: '2014'
                    },
                    {
                        id: '2015',
                        label: '2015'
                    },
                    {
                        id: '2016',
                        label: '2016'
                    },
                    {
                        id: '2017',
                        label: '2017'
                    },
                    {
                        id: '2018',
                        label: '2018'
                    },
                    {
                        id: '2019',
                        label: '2019'
                    },
                    {
                        id: '2020',
                        label: '2020'
                    },
                    {
                        id: '2021',
                        label: '2021'
                    }
                ],
                xAxisData: [], //X轴标签
                echartSeries: [],
                columnList: [],
                titles: '',
                toShow: true,
                toShow1: false,
                toShow2: true,
                toShow3: false,
                rest: {},
                INDEX_DESCR: '',
                visible0: false,
                runSelect: {},
                esql: '0',
                xTurn: 'X',
                timeOutLoading: 0,
                visible10: false,
                planDesc: '',
                schemeId: '',
                loading: false,
                loading1: false,
                dragVisible: false,
                dragVisible1: false,
                nodeLive: [],
                nodeLive1: [],
                lineDisplay: false,
                loadSign: '',
                //日期开关
                yearOpen: false,
                yearOpen1: false,
                showDeal: false,
                selectPrice: '1',
                planStatus: '0',
                spinning: false,//loading 遮罩
            }
        },
        created() {
            console.log(Object.keys(this.currentSelect).length == 0)
            console.log(this.queryParam.INSPECTION_PLAN_TYPE == undefined)
            let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
            let userId = userInfo.id
            //国库
            getGuokuTree().then(res => {
                if (res.result === 'success') {
                    this.guokuData = res.rows
                }
            })
            //核算主体
            getOrgTree().then(res => {
                if (res.result === 'success') {
                    this.hesuanData = res.rows
                }
            })
            //地区
            getAreaTree().then(res => {
                if (res.result === 'success') {
                    this.areaTreeData = res.rows
                }
            })
            selectIndexRelationTree({personalFlag: '1'}).then(res => {
                if (res.result == 'success') {
                    this.treeData = res.rows
                    /* var lista=document.querySelectorAll(".filter-tree .span-ellipsis span")
        lista.forEach(res=>{
          res.onmouseover
        }) */
                }
            })
            selectIndexRelationTree({userId: userId, personalFlag: '0'}).then(res => {
                if (res.result == 'success') {
                    this.treeDatas = res.rows
                }
            })
        },
        components: {
            vTable,
            dataMonth,
            columnChart
        },
        directives: {loadmore},
        computed: {
            disable() {
                console.log(this.currentSelect.length)
                return (
                    this.currentSelect.length == 0 ||
                    (this.queryParam.periodFlag == undefined && this.queryParam.dimensionFlag == undefined)
                )
            }
        },
        mounted() {
            //var lista=document.querySelectorAll(".filter-tree .span-ellipsis span")
            //console.log(lista)
            console.log(this.$route.query.length)
            if (this.$route.query !== undefined && JSON.stringify(this.$route.query) !== '{}') {
                this.runSelect = this.$route.query;
                this.spinning = true
                selectSchemeData({
                    pageNo: 1,
                    pageSize: 100,
                    schemeId: this.runSelect.ID
                }).then(res => {
                    if (res.result == 'success') {
                        this.spinning = false
                        this.tableDatas = res.rows
                        this.total = res.total
                        this.tableData = res.columns
                        this.shows()
                    }
                })
            }
        },
        watch: {
            'model.endDate'(val) {
                if (this.queryParam.periodFlag === '2') {
                    console.log(this.model.startDate)
                    let aa = new Date(this.model.startDate).getTime()
                    let bb = new Date(this.model.endDate).getTime()
                    if (this.model.startDate !== '' || this.model.startDate !== '') {
                        if (parseInt(aa) > parseInt(bb)) {
                            this.model.startDate = null;
                            this.model.endDate = null;
                            this.validateStatus7 = 'error'
                        } else {
                            this.validateStatus7 = ''
                        }
                    }
                } else if (this.queryParam.periodFlag === '1') {
                    console.log(new Date(this.model.startDate).getTime())
                    let aa = new Date(this.model.startDate).getTime()
                    let bb = new Date(this.model.endDate).getTime()
                    if (this.model.startDate !== '' || this.model.startDate !== '') {
                        if (parseInt(aa) > parseInt(bb)) {
                            this.model.startDate = null;
                            this.model.endDate = null;
                            this.validateStatus6 = 'error'
                        } else {
                            this.validateStatus6 = ''
                        }
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    let aa = new Date(this.model.startDate).getTime()
                    let bb = new Date(this.model.endDate).getTime()
                    if (this.model.startDate !== '' || this.model.startDate !== '') {
                        if (parseInt(aa) > parseInt(bb)) {
                            this.model.startDate = null;
                            this.model.endDate = null;
                            this.validateStatus8 = 'error'
                        } else {
                            this.validateStatus8 = ''
                        }
                    }
                }
            },
            'endquarter'(val) {
                let aa = this.startquarter.replace('-', '').replace('Q', '')
                let bb = this.endquarter.replace('-', '').replace('Q', '')
                if (this.startquarter !== '' || this.endquarter !== '') {
                    if (parseInt(aa) > parseInt(bb)) {
                        this.startquarter = '';
                        this.endquarter = '';
                        this.validateStatus9 = 'error'
                    } else {
                        this.validateStatus9 = ''
                    }
                }
            },
            $route(router) {
                if (this.$route.query !== undefined&&JSON.stringify(this.$route.query) !== '{}') {
                    this.runSelect = this.$route.query;
                    this.spinning = true
                    selectSchemeData({
                        pageNo: 1,
                        pageSize: 100,
                        schemeId: this.runSelect.ID
                    }).then(res => {
                        if (res.result == 'success') {
                            this.spinning = false
                            this.tableDatas = res.rows
                            this.total = res.total
                            this.tableData = res.columns
                            this.shows()
                        }
                    })
                }
            },
            'queryParam.dimensionFlag'(val) {
                if (val === '1') {
                    this.level = '1'
                    this.status1 = true;
                    this.status2 = false;
                } else if (val === '2') {
                    this.level = '2'
                    this.status1 = false;
                    this.status2 = true;
                } else if (val === '3') {
                    this.level = '3'
                } else if (val == undefined) {
                    this.getDatas();
                    this.getDatas2();
                }
                this.currentSelect = [];
            },
            'queryParam.periodFlag'(value) {
                /* if(val == undefined){
        this.getDatas();
        this.getDatas2();
      } */
                this.currentSelect = [];
                let col = []
                this.tableData.forEach((el, index) => {
                    if (el.id) {
                        col.push(el.id)
                    } else {
                        col.push(el.INDEX_ID)
                    }
                })
                if (value === '1' && this.model.periodFlag) {
                    this.check1 = true
                    this.check2 = false
                    this.check3 = false
                    this.check4 = false,
                        this.obj = {
                            //方案查询条件
                            startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                            endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                            dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                            periodFlag: this.queryParam.periodFlag, //周期
                            dimCode:
                                (this.model.dimCode && typeof this.model.dimCode === 'string')
                                    ? this.model.dimCode ? this.model.dimCode : ''
                                    : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                            price: this.price,
                            columns: Array.from(new Set(col)).join(',')
                        }
                } else if (value === '2' && this.model.periodFlag) {
                    console.log(this.model.dimCode)
                    this.check1 = false
                    this.check2 = true
                    this.check3 = false
                    this.check4 = false
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                    console.log(this.obj)
                } else if (value === '3' && this.model.periodFlag) {
                    this.check1 = false
                    this.check2 = false
                    this.check3 = false
                    this.check4 = true
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (value === '4' && this.model.periodFlag) {
                    this.check1 = false
                    this.check2 = false
                    this.check3 = true
                    this.check4 = false
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate, //账期起始日
                        endDate: this.model.endDate, //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                }
            },
            nodeLive:{
              handler (val) {
               this.moveRight();
              },
              deep: true
            },
            nodeLive1:{
              handler (val) {
               this.moveRight();
              },
              deep: true
            },
            'dragVisible'(val) {
                if (val === true) {
                    setTimeout(() => {
                        let dragfile = document.querySelector('.dragfile');
                        let boxs = document.getElementById('filter-tree')
                        console.log(boxs.clientHeight)
                        dragfile.attributes[2].value = dragfile.attributes[2].textContent + 'height:' + boxs.clientHeight + 'px';
                        let onBox = document.querySelector('.tableTwo')
                        this.move(dragfile, onBox);
                    }, 1000)

                }
            },
            'dragVisible1'(val) {
                if (val === true) {
                    setTimeout(() => {
                        let dragfile = document.querySelector('.dragfile1');
                        let boxs = document.getElementById('filter-tree1')
                        console.log(boxs.clientHeight)
                        dragfile.attributes[2].value = dragfile.attributes[2].textContent + 'height:' + boxs.clientHeight + 'px' + ';';
                        let onBox = document.querySelector('.tableTwo')
                        this.move(dragfile, onBox);
                    }, 1000)

                }
            },
            'tableData'(val) {
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
                        let arr = [];
                        val.forEach(item => {
                            arr.push(item.id ? item.id : item.INDEX_ID)
                        })
                        this.$refs.tree.setCheckedKeys(arr);
                        this.$refs.tree1.setCheckedKeys(arr);
                    })
                } else {
                    this.$refs.tree.setCheckedKeys([]);
                    this.$refs.tree1.setCheckedKeys([]);
                }
            }
        },
        methods: {
            move(ele, box) {
                document.addEventListener("dragstart", function (event) {
                    event.dataTransfer.setData("Text", event.target.id);
                });

                // 默认情况下,数据/元素不能在其他元素中被拖放。对于drop我们必须防止元素的默认处理
                document.addEventListener("dragover", function (event) {
                    event.preventDefault();
                });
                // ondragover拖拽事件,当被拖拽的元素经过时触发
                box.ondragover = function (target) {
                    target.preventDefault();
                }

                var flag = true; //假设ele元素还没有被克隆到 box
                var oThis = this;
                ele.onmousedown = function () {
                    var $clone = this.cloneNode(true);
                    $clone.style.backgroundColor = 'green';
                    box.ondrop = function () {
                        if (flag == true) {
                            oThis.moveRight();
                            oThis.dragVisible = false;
                            oThis.dragVisible1 = false;
                            //this.appendChild($clone);
                            flag = false; // ele元素已经被克隆到box,flag变为false
                        }
                    }

                }
            },
            toggleAdvanced() {
                this.advanced = !this.advanced;
                console.log(document.getElementById('model-right'))
                if (this.advanced == false) {
                    document.getElementById('model-right').style.width = "96%";
                    this.lineDisplay = true;
                } else {
                    document.getElementById('model-right').style.width = "72%";
                    this.lineDisplay = false;
                }
            },
            shows() {
                //debugger
                this.form.resetFields()
                this.form1.resetFields()
                let record = JSON.stringify(this.$route.query)!=='{}'?this.$route.query:JSON.parse(localStorage.getItem('records'))
                //let record = JSON.parse(localStorage.getItem('records'))
                this.queryParam = Object.assign({}, JSON.parse(record.SCHEME_CONDITON))
                this.model = Object.assign({}, JSON.parse(record.SCHEME_CONDITON))
                this.showSearch = true;
                let fieldsVal = pick(this.queryParam, 'dimensionFlag', 'periodFlag')
                let fieldsVal1 = pick(this.model, 'startDate', 'endDate', 'dimCode', 'price')
                this.price = this.model.price;
                if (this.model.periodFlag === '1') {
                    //fieldsVal1.startDate = !fieldsVal1.startDate ? [] : [this.moment(fieldsVal1.startDate, 'YYYY-MM-DD'), this.moment(this.model.endDate, 'YYYY-MM-DD')]
                    /* this.model.startDate = [this.moment(fieldsVal1.startDate, 'YYYY-MM-DD'), this.moment(this.model.endDate, 'YYYY-MM-DD')] */
                    this.model.startDate = fieldsVal1.startDate === '' ? null : this.moment(fieldsVal1.startDate, 'YYYY-MM-DD')
                    this.model.endDate = fieldsVal1.endDate === '' ? null : this.moment(fieldsVal1.endDate, 'YYYY-MM-DD')
                } else if (this.model.periodFlag === '2') {
                    //this.model.startDate = !fieldsVal1.startDate ? [] : [this.moment(fieldsVal1.startDate, 'YYYY-MM')]
                    //this.model.endDate = !fieldsVal1.endDate ? [] : [this.moment(fieldsVal1.endDate, 'YYYY-MM')]
                    this.model.startDate = fieldsVal1.startDate === '' ? null : this.moment(fieldsVal1.startDate, 'YYYY-MM')
                    this.model.endDate = fieldsVal1.endDate === '' ? null : this.moment(fieldsVal1.endDate, 'YYYY-MM')
                } else if (this.model.periodFlag === '3') {
                    this.startquarter = fieldsVal1.startDate;
                    this.endquarter = fieldsVal1.endDate;
                } else if (this.model.periodFlag === '4') {
                    this.model.startDate = fieldsVal1.startDate === '' ? null : this.moment(fieldsVal1.startDate, 'YYYY')
                    this.model.endDate = fieldsVal1.endDate === '' ? null : this.moment(fieldsVal1.endDate, 'YYYY')
                }
                /* this.model.BEGINMONTH = this.moment(fieldsVal1.startDate, 'YYYY')
      this.model.ENDMONTH = this.moment(fieldsVal1.endDate, 'YYYY')
      this.model.PLAN_BEGINTIME = [
        this.moment(fieldsVal1.startDate, 'YYYY-MM-DD'),
        this.moment(fieldsVal1.endDate, 'YYYY-MM-DD')
      ]
      this.model.PLAN_STARTYEAR = this.moment(fieldsVal1.startDate, 'YYYY')
      this.model.PLAN_ENDYEAR = this.moment(fieldsVal1.endDate, 'YYYY') */
                //this.startquarter = fieldsVal1.startDate
                //this.endquarter = fieldsVal1.endDate
                this.model.dimCode = !this.model.dimCode ? [] : this.model.dimCode.split(',') //被查库

                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldsVal)
                    this.form1.setFieldsValue(fieldsVal1)
                })
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    name: this.queryParam.filterText, //指标Tree关键字搜索条件
                    personalFlag: '1'
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableData)
                        this.treeData = res.rows
                    }
                })
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    //name: this.queryParam.filterText, //指标Tree关键字搜索条件
                    personalFlag: '0',
                    userId: userId
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableDatas)
                        this.treeDatas = res.rows
                    }
                })
            },
            downnew() {
                window.location.href = `${
                    window._CONFIG['domianURL']
                    }/indicatorsLib/indexRelationController/downloadIndexInfo?dimensionFlag=${
                    this.queryParam.dimensionFlag == undefined ? '' : this.queryParam.dimensionFlag
                    }&periodFlag=${
                    this.queryParam.periodFlag == undefined ? '' : this.queryParam.periodFlag
                    }&personalFlag=${
                    '1'
                    }&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
                /* downloadIndexInfo({
        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
        periodFlag: this.queryParam.periodFlag, //周期
        pId: '', //根据父节点主键查询子节点(可选可不选)
        personalFlag:'1',
        name: this.queryParam.filterText.replace(/，/g,"%") //指标Tree关键字搜索条件
      }).then(res => {
        if (res.result == 'success') {
          console.log(this.tableData)
          this.treeData = res.rows
        }
      }) */
            },
            handleNode(data, node, event) {
                if (data.disabled) {
                    return;
                }
                if (this.queryParam.dimensionFlag === undefined || this.queryParam.periodFlag === undefined) {
                    if (data.children.length === 0) {
                        this.$message.error("请先选择周期和维度")
                    }
                    return;
                }
                if (node.checked === true) {
                    node.checked = false;
                } else {
                    node.checked = true;
                    //this.dragVisible = true;
                }
                this.handleCheck(data,node)
            },
            handleNode1(data, node, event) {
                if (data.disabled) {
                    return;
                }
                if (this.queryParam.dimensionFlag === undefined || this.queryParam.periodFlag === undefined) {
                    if (data.children.length === 0) {
                        this.$message.error("请先选择周期和维度")
                    }
                    return;
                }
                if (node.checked === true) {
                    node.checked = false;
                } else {
                    node.checked = true;
                    //this.dragVisible1 = true;
                }
                this.handleCheck1(data,node)
            },
            handleCheck(data,node){
              if(data.disabled){
                return;
              }
              this.nodeLive = this.$refs.tree.getCheckedKeys();
                if(node.checked === true){
                  if(data.children.length===0){
                  this.currentSelect.push(data)
                  }
                }
            },
            handleCheck1(data,node){

              if(data.disabled){
                return;
              }

              this.nodeLive = this.$refs.tree1.getCheckedKeys();
                if(node.checked === true){
                  if(data.children.length===0){
                  this.currentSelect.push(data)
                  }
                }
            },

            /*      handleNode(data = {}) {
      debugger
      this.currentSelect = data
      console.log(this.tableDatas)
    },  */
            rightClick(e, data, node, comp) {
                console.log('e:', e, 'data', data)
                this.rightMenu = {top: e.pageY + 'px', left: e.pageX + 'px'}
                this.menuVisible = true
                const self = this
                this.treeId = data.id
                document.onclick = function (ev) {
                    if (ev.target !== document.getElementById('perTreeMenu')) {
                        self.menuVisible = false
                    }
                }
            },
            getTargetNode(ele, target) {
                //ele是内部元素，target是你想找到的包裹元素
                if (!ele || ele === document) return false;
                return ele === target ? true : this.getTargetNode(ele.parentNode, target);
            },
            enter($event, node) {
                console.log($event.currentTarget)
                this.rightMenu1 = {top: $event.pageY + 'px', left: $event.pageX - 10 + 'px'}
                this.INDEX_DESCR = ''
                this.timeOutLoading = setTimeout(() => {
                    getIndexDetails({INDEX_ID: node.data.id}).then(res => {
                        if (res.result == 'success') {
                            this.INDEX_DESCR = res.rows.INDEX_DESCR;
                            this.rest = res.rows;
                            if (this.INDEX_DESCR !== '') {
                                this.menuVisible1 = true
                            }
                        }
                    })
                    /* if(this.INDEX_DESCR!==''){
        this.menuVisible1 = true
      } */

                    const self = this
                    document.onmouseover = function (ev) {
                        console.log(ev.target)
                        if ((ev.target !== document.getElementById('perTreeMenu1') && ev.target !== document.getElementById('perTreeMenu1').getElementsByTagName('a')[0]) && ev.target !== $event.target) {
                            self.menuVisible1 = false
                            document.onmouseover = null;
                        }
                    }
                }, 1000)

            },
            leave($event, node) {
                //this.menuVisible1 = false;
                clearTimeout(this.timeOutLoading);
            },
            getInfo() {
                /* getIndexDetails({INDEX_ID:this.treeId}).then(res=>{
        if(res.result == 'success'){
          this.visible0 = true;
          this.rest = res.rows;
        }
      }) */
                this.visible0 = true;
            },
            handleCancels1() {
                this.visible0 = false;
            },
            check() {
                alert(1);
            },
            select(value, option) {
                this.tableData = [];
                this.tableDatas = []
                if (value === '1') {
                    this.level = '1'
                    /* this.tableData = [{
          label:"账期"
        },{
          label:"国库"
        }] */
                    this.status1 = true
                    this.status2 = false
                    this.status3 = false
                    if (this.model.periodFlag) {
                        this.model.dimCode = [];
                    } else {
                        this.model = {};
                    }
                } else if (value === '2') {
                    this.level = '2'
                    /* this.tableData = [{
          label:"账期"
        },{
          label:"地区"
        }] */
                    this.status1 = false
                    this.status2 = true
                    this.status3 = false
                    if (this.model.periodFlag) {
                        this.model.dimCode = [];
                    } else {
                        this.model = {};
                    }
                } else {
                    this.level = '3'
                    /*  this.tableData = [{
          label:"账期"
        },{
          label:"核算主体"
        }] */
                    this.status1 = false
                    this.status2 = false
                    this.status3 = true
                    if (this.model.periodFlag) {
                        this.model.dimCode = [];
                    } else {
                        this.model = {};
                    }
                }
                if (this.queryParam.periodFlag !== undefined) {
                    this.getDatas()
                    this.showSearch = true;
                }
            },
            select1(value, option) {
                console.log(this.queryParam.periodFlag)
                //this.queryParam.periodFlag = value
                this.tableDatas = []
                this.tableData = []
                if (this.queryParam.dimensionFlag !== undefined) {
                    this.getDatas(value)
                    this.showSearch = true;
                }
                if (value === '1') {
                    this.check1 = true
                    this.check2 = false
                    this.check3 = false
                    this.check4 = false
                    console.log(this.model)
                    //this.model = {}
                    if (this.model.periodFlag) {
                        this.model.startDate = '';
                        this.model.endDate = '';
                    } else {
                        this.model = {};
                    }

                } else if (value === '2') {
                    this.check1 = false
                    this.check2 = true
                    this.check3 = false
                    this.check4 = false
                    //this.model = {}
                    if (this.model.periodFlag) {
                        this.model.startDate = '';
                        this.model.endDate = '';
                    } else {
                        this.model = {};
                    }
                } else if (value === '3') {
                    this.check1 = false
                    this.check2 = false
                    this.check3 = false
                    this.check4 = true
                    //this.model = {}
                    if (this.model.periodFlag) {
                        this.model.startDate = '';
                        this.model.endDate = '';
                    } else {
                        this.model = {};
                    }
                } else {
                    this.check1 = false
                    this.check2 = false
                    this.check3 = true
                    this.check4 = false
                    //this.model = {}
                    if (this.model.periodFlag) {
                        this.model.startDate = '';
                        this.model.endDate = '';
                    } else {
                        this.model = {};
                    }
                }
            },
            startquarter1(msg) {
                this.startquarter = msg
            },
            endquarter1(msg) {
                this.endquarter = msg
            },
            opennew() {
                this.$router.push({path: '/statistics/checkLibrary'})
            },
            showChart() {
                if (this.tableDatas.length == 0) {
                    this.$message.error('请先查询')
                    return
                }

                this.isTableEchart = false;
                this.onlineData = {};
                this.queryParam2 = {}
                this.showDeal = false;
                this.visibles = true
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                    }
                }
                let col = []
                this.tableData.forEach((el, index) => {
                    if (el.id) {
                        col.push(el.id)
                    } else {
                        col.push(el.INDEX_ID)
                    }
                })
                getDateInterval({
                    columns: Array.from(new Set(col)).join(','),
                    periodFlag: this.queryParam.periodFlag,
                    dimensionFlag: this.queryParam.dimensionFlag,
                    startDate: this.obj.startDate,
                    endDate: this.obj.endDate,
                }).then(res => {
                    if (res.result == 'success') {
                        this.periodFlagOption = res.rows
                    }
                })
                getDimensionSelect({
                    dimCode:
                        (this.model.dimCode && typeof this.model.dimCode === 'string')
                            ? this.model.dimCode ? this.model.dimCode : ''
                            : this.model.dimCode ? this.model.dimCode.join(',') : ''
                }).then(res => {
                    if (res.result == 'success') {
                        this.dimenOption = res.rows
                        console.log(typeof Array.from(this.dimenOption))
                    }
                })
                //this.setEcharts()
                //document.getElementById('#depart-canvas').style.display==='none';
            },
            reload() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: (typeof value) === 'string' ? value : this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    personalFlag: '1',
                    name: this.queryParam.filterText == undefined ? '' : this.queryParam.filterText.replace(/，|,/g, "%") //指标Tree关键字搜索条件
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableData)
                        this.loading = true;
                        setTimeout(() => {
                            this.treeData = res.rows
                            this.loading = false;
                        }, 1000)
                    }
                })
            },
            reloads() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: (typeof value) === 'string' ? value : this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    personalFlag: '0',
                    name: this.queryParam.filterTexts == undefined ? '' : this.queryParam.filterTexts.replace(/，|,/g, "%") //指标Tree关键字搜索条件
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableData)
                        this.loading1 = true;
                        setTimeout(() => {
                            this.treeDatas = res.rows
                            this.loading1 = false;
                        }, 1000)
                    }
                })
            },
            getDatas(value) {
                console.log(typeof value)
                console.log(this.tableData)
                console.log(this.queryParam.dimensionFlag, this.queryParam.periodFlag)
                console.log(this.queryParam.filterText)
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: (typeof value) === 'string' ? value : this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    personalFlag: '1',
                    name: this.queryParam.filterText == undefined ? '' : this.queryParam.filterText.replace(/，|,/g, "%") //指标Tree关键字搜索条件
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableData)
                        this.treeData = res.rows
                    }
                })
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: (typeof value) === 'string' ? value : this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    personalFlag: '0',
                    name: this.queryParam.filterTexts == undefined ? '' : this.queryParam.filterTexts.replace(/，|,/g, "%") //指标Tree关键字搜索条件
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableData)
                        this.treeDatas = res.rows
                    }
                })
            },
            getDatas2(value) {
                console.log(typeof value)
                console.log(this.tableData)
                console.log(this.queryParam.dimensionFlag, this.queryParam.periodFlag)
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                selectIndexRelationTree({
                    dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                    periodFlag: (typeof value) === 'string' ? value : this.queryParam.periodFlag, //周期
                    pId: '', //根据父节点主键查询子节点(可选可不选)
                    personalFlag: '0',
                    name: this.queryParam.filterTexts == undefined ? '' : this.queryParam.filterTexts.replace(/，|,/g, "%") //指标Tree关键字搜索条件
                }).then(res => {
                    if (res.result == 'success') {
                        console.log(this.tableData)
                        this.treeDatas = res.rows
                    }
                })
            },
            getFilterName() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                if (this.esql === '0') {
                    selectSchemeTable({
                        isPublicScheme: 1,
                        userId: userId,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        pageNo: 1,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            const pagination = {...this.pagination}
                            pagination.total = res.total
                            this.pagination = pagination
                        }
                    })
                } else {
                    selectSchemeTable({
                        isPublicScheme: 0,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        userId: userId,
                        pageNo: 1,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            const pagination = {...this.pagination}
                            pagination.total = res.total
                            this.pagination = pagination
                        }
                    })
                }
            },
            filter() {
                if (this.tableDatas.length == 0) {
                    this.$message.error('请先查询')
                    return
                }
                this.$refs.vTable.filter()
            },
            sort() {
                if (this.tableDatas.length == 0) {
                    this.$message.error('请先查询')
                    return
                }
                this.$refs.vTable.sort()
            },
            saveplan(record) {
                if (this.tableDatas.length == 0) {
                    this.$message.error('请先查询')
                    return
                }
                this.$refs.vTable.saveplan(record)
            },
            checkplan() {
                console.log(this.pagination.pageNo)
                this.visible4 = true
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                //this.$refs.vTable.checkplan();
                if (this.esql === '0') {
                    selectSchemeTable({
                        isPublicScheme: 1,
                        userId: userId,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        pageNo: 1,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            const pagination = {...this.pagination}
                            pagination.total = res.total
                            this.pagination = pagination
                        }
                    })
                } else {
                    selectSchemeTable({
                        isPublicScheme: 0,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        userId: userId,
                        pageNo: 1,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            const pagination = {...this.pagination}
                            pagination.total = res.total
                            this.pagination = pagination
                        }
                    })
                }
            },
            onChanges(e) {
                this.pagination.current = 1;
                if (e.target.value === '0') {
                    let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                    let userId = userInfo.id
                    //this.$refs.vTable.checkplan();
                    selectSchemeTable({
                        isPublicScheme: 1,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        userId: userId,
                        pageNo: 1,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            const pagination = {...this.pagination}
                            pagination.total = res.total
                            this.pagination = pagination
                        }
                    })
                } else {
                    let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                    let userId = userInfo.id
                    //this.$refs.vTable.checkplan();
                    selectSchemeTable({
                        isPublicScheme: 0,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        userId: userId,
                        pageNo: 1,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            const pagination = {...this.pagination}
                            pagination.total = res.total
                            this.pagination = pagination
                        }
                    })
                }
            },
            handleTableChange(pagination, filters, sorter) {
                if (this.esql === '0') {
                    this.pagination.pageNo = pagination.current
                    this.pagination.pageSize = pagination.pageSize
                    //this.checkplan()
                    let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                    let userId = userInfo.id
                    //this.$refs.vTable.checkplan();
                    selectSchemeTable({
                        isPublicScheme: 1,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        userId: userId,
                        pageNo: this.pagination.pageNo,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            /* const pagination = { ...this.pagination }
          pagination.total = res.total */
                            this.pagination = pagination
                        }
                    })
                } else {
                    this.pagination.pageNo = pagination.current
                    this.pagination.pageSize = pagination.pageSize
                    //this.checkplan()
                    let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                    let userId = userInfo.id
                    //this.$refs.vTable.checkplan();
                    selectSchemeTable({
                        isPublicScheme: 0,
                        schemeDescr: this.queryParam.filterKeyword == undefined ? '' : this.queryParam.filterKeyword.replace(/，/g, "%"),
                        userId: userId,
                        pageNo: this.pagination.pageNo,
                        pageSize: 10
                    }).then(res => {
                        if (res.result == 'success') {
                            this.dataSource2 = res.rows
                            /* const pagination = { ...this.pagination }
          pagination.total = res.total */
                            this.pagination = pagination
                        }
                    })
                }
            },
            handleIconClick() {
                //清空筛选项
                this.filterText = ''
            },

            moveRight() {
                //移至右边
                /* const cellLevel = this.currentSelectPath.split('-')
      const lastPath = cellLevel.splice(-1)
      const result = []
      cellLevel.forEach(function(el, key) {
        result.push(el, 'children')
      })
      const currentItemParent = _.get(this.treeData, result.join('.'))
      const currentItem = currentItemParent.splice(lastPath, 1)[0]
      currentItemParent.forEach((chr, index) => {
        chr.label = `${cellLevel.join('-')}-${index}`
      })
      this.currentSelectPath = ''
      currentItem.modelName && */
                if (this.queryParam.dimensionFlag === undefined || this.queryParam.periodFlag === undefined) {
                    this.$message.error("请先选择周期和维度")
                    return;
                }
                /* if(this.currentSelect.length === 0){
        this.$message.error("请先选择查询的指标")
        return;
      } */
                console.log(this.$refs.tree1.getCheckedNodes());
                this.tableData = [];
                const currentSlect = this.$refs.tree.getCheckedNodes().concat(this.$refs.tree1.getCheckedNodes())
                const currentSlect1 = currentSlect.filter(function (val, index, arr) {
                    return (val.children.length === 0)
                })
                console.log(currentSlect1)
                var flag = false;
                for (var i in this.tableData) {
                    for (var j in currentSlect1) {
                        if ((this.tableData[i].INDEX_ID !== undefined ? this.tableData[i].INDEX_ID : this.tableData[i].id) === currentSlect[j].id) {
                            flag = true
                        } else {
                            flag = false
                            break;
                        }
                    }

                }
                if (flag === false) {
                    console.log(this.tableData.concat(currentSlect1))
                    this.tableData = Array.from(new Set(this.tableData.concat(currentSlect1)))
                    /* this.tableData.forEach((el,index) => {
          if(el.id||el.INDEX_ID)
        }) */
                    var hash = {};
                    this.tableData = this.tableData.reduce(function (item, next) {
                        hash[next.id || next.INDEX_ID] ? "" : hash[next.id || next.INDEX_ID] = true && item.push(next);
                        return item
                    }, [])
                    console.log(this.tableData)
                }
                this.tableDatas = []
            },
            del(data) {
                this.tableData.splice(parseInt(data) - 4, 1) //删除表格单元格数据
                let cols = [];
                this.tableData.forEach((el, index) => {
                    if (el.id) {
                        cols.push(el.id)
                    } else {
                        cols.push(el.INDEX_ID)
                    }
                })
                this.obj3 = cols;
                if (this.tableData.length === 0) {
                    this.tableDatas = []
                }
                //this.getData();
            },
            handlecheckCancel() {
                this.visible4 = false
            },
            handlecheckOk() {
            },
            handleRun(record) {
                this.$store.dispatch('schemeRecord', record)
                this.visible4 = false
                this.record = record.ID;
                /* this.show1 = true;
      this.show = false; */
                this.spinning = true
                selectSchemeData({
                    pageNo: 1,
                    pageSize: 100,
                    schemeId: JSON.parse(localStorage.getItem('records')).ID,
                    SCHEME_CONDITON: record.SCHEME_CONDITON
                }).then(res => {
                    if (res.result == 'success') {
                        this.spinning = false
                        this.tableDatas = res.rows
                        this.total = res.total
                        this.tableData = res.columns
                        this.currentPage = 1;
                        this.planStatus = '1';
                        /*  debugger
          var lista=document.querySelectorAll('.has-gutter .canDrag')
          for(var i=0;i<lista.length;i++){
            lista[i].onmouseover = function(e){
              debugger
              let cc = e.target.children[0].children[0].children[1].attributes[1].value = 'position:absolute;top:-4px;right:0px;font-size:16px;display:block';
            }
            lista[i].onmouseleave = function(e){
              let cc = e.target.children[0].children[0].children[1].attributes[1].value = 'position:absolute;top:-4px;right:0px;font-size:16px;display:none';
            }
          } */
                        this.shows()
                    }
                })

                //this.$emit('shows');
                //this.$router.push({ path: '/statistics/displayScheme'})
            },
            handleEdit(record) {
                this.visible10 = true;
                this.planDesc = record.SCHEME_DESCR;
                this.schemeId = record.ID;
            },
            handleGallery(record) {
              pushIndexToVS({ID:record.ID}).then(res=>{
                if (res.result == 'success') {
                  this.$message.success(res.msg);
                  this.checkplan()
                } else {
                  this.$message.error(res.msg);
                }

              })
            },
            handleCancels2() {
                this.visible10 = false;
            },
            handleSubmit2() {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                schemeRename({schemeId: this.schemeId, schemeDescr: this.planDesc, userId: userId}).then(res => {
                    if (res.result == 'success') {
                        this.$message.success(res.msg);
                        this.visible10 = false;
                        this.checkplan()
                    } else {
                        this.$message.error(res.msg);
                    }
                })
            },
            handleSubmit(record) {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                submitScheme({schemeId: record.ID, userId: userId}).then(res => {
                    if (res.result == 'success') {
                        this.$message.success(res.msg);
                        this.checkplan()
                    }
                })
            },
            handleDelete(record) {
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                if (this.esql === '0') {
                    deleteScheme({userId: userId, schemeId: record.ID, publicId: ''}).then(res => {
                        if (res.result == 'success') {
                            //this.dataSource2 = res.rows;
                            this.checkplan()
                            this.$message.success(res.msg)
                        }
                    })
                } else {
                    deletePublicScheme({userId: userId, schemeId: record.ID, publicId: record.PUBLIC_ID}).then(res => {
                        if (res.result == 'success') {
                            //this.dataSource2 = res.rows;
                            this.checkplan()
                            this.$message.success(res.msg)
                        }
                    })
                }
            },
            reset() {
                this.model = {}
                this.model.dimCode = ''
                this.price = ''
                this.startquarter = ''
                this.endquarter = ''
            },
            toLoad(val) {
                this.obj33 = val;
                //this.tableDatas = data;
                this.getData(val)
            },
            download() {
                if (this.tableDatas.length == 0) {
                    this.$message.error('请先查询')
                    return
                }
                let col = []
                var fields = {}
                var oThis = this;
                this.tableData.map(itm => {
                    if (itm.id) {
                        let aa = itm.label.split('▲')[0] + itm.label.split('▲')[2]
                        if (aa.indexOf("%") !== -1) {
                            fields[itm.id] = itm.label.split('▲')[0] + '(%)'
                        } else if (oThis.price === '1') {
                            fields[itm.id] = itm.label.split('▲')[0] + '(元)'
                        } else if (oThis.price === '10000') {
                            fields[itm.id] = itm.label.split('▲')[0] + '(万元)'
                        } else if (oThis.price === '100000000') {
                            fields[itm.id] = itm.label.split('▲')[0] + '(亿元)'
                        }
                    } else {
                        if (itm.INDEX_NAME.indexOf("%") !== -1) {
                            fields[itm.INDEX_ID] = itm.INDEX_NAME
                        } else if (oThis.price === '1') {
                            fields[itm.INDEX_ID] = itm.INDEX_NAME + '(元)'
                        } else if (oThis.price === '10000') {
                            fields[itm.INDEX_ID] = itm.INDEX_NAME + '(万元)'
                        } else if (oThis.price === '100000000') {
                            fields[itm.INDEX_ID] = itm.INDEX_NAME + '(亿元)'
                        }
                        //fields[itm.INDEX_ID] = itm.INDEX_NAME
                    }
                    //fields[itm.id] = itm.label.split('▲')[0]
                })
                console.log(fields)
                var obj1 = {}
                if (this.queryParam.dimensionFlag === '1') {
                    obj1 = {
                        ACCOUNT_PERIOD: '账期',
                        GK: '国库',
                    }
                } else if (this.queryParam.dimensionFlag === '2') {
                    obj1 = {
                        ACCOUNT_PERIOD: '账期',
                        GK: '地区',
                    }
                } else {
                    obj1 = {
                        ACCOUNT_PERIOD: '账期',
                        GK: '核算主体',
                    }
                }
                let newObj = {}
                Object.assign(newObj, obj1, fields)
                this.tableData.forEach((el, index) => {
                    if (el.id) {
                        col.push(el.id)
                    } else {
                        col.push(el.INDEX_ID)
                    }
                })
                console.log(newObj)
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                if (this.queryParam.periodFlag === '1' && this.queryParam.dimensionFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                    }
                }
                let params = {
                    mainCondition: this.obj,
                    columns: Array.from(new Set(col)).join(','),
                    titles: newObj,
                    screenConditon: this.obj33
                }
                window.location.href = `${
                    window._CONFIG['domianURL']
                    }/indicatorsLib/indexSchemeController/downLoadSchemeData?params=${encodeURIComponent(
                    JSON.stringify(params)
                )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
                /* downLoadSchemeData({
        mainCondition: this.obj,
        columns: col.join(','),
        titles: newObj
      }).then(res => {

      }) */
            },
            handleCurrentChange(page) {
                this.pageNo = page
                //this.getData(this.pageNo)
                if (this.queryParam.dimensionFlag === undefined || this.queryParam.periodFlag === undefined) {
                    this.$message.error("请先选择周期和维度")
                    return
                }
                /*  if(this.currentSelect.length === 0){
        this.$message.error("请先选择查询的指标")
        return;
      } */
                console.log(typeof page)
                /* if(typeof page!=='number'){
        this.pageNo = 1;
      } */
                console.log(this.tableData)
                //console.log(this.model.PLAN_BEGINTIME)
                /* if (this.model.dimCode === '' || this.model.dimCode === undefined) {
        this.$message.error('请填写完整搜索条件')
        return false
      } */
                if (!this.price) {
                    this.validateStatus3 = 'error';
                    return
                } else {
                    this.validateStatus3 = 'success'
                }
                /* if (!this.model.dimCode||this.model.dimCode === ''||this.model.dimCode.length === 0) {
                this.validateStatus1 = 'error';
                return
              } else {
                this.validateStatus1 = 'success'
              }
      if(this.queryParam.periodFlag === '1'){
        if (!this.model.startDate) {
                this.validateStatus2 = 'error';
                return
              } else {
                this.validateStatus2 = 'success'
              }
      }else if(this.queryParam.periodFlag ==='3'){
        if (!this.startquarter||!this.endquarter) {
                this.validateStatus2 = 'error';
                return
              } else {
                this.validateStatus2 = 'success'
              }
      }else{
        if (!this.model.startDate||!this.model.endDate) {
                this.validateStatus2 = 'error';
                return
              } else {
                this.validateStatus2 = 'success'
              }
      } */
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
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                }
                console.log(this.checkValue)
                this.spinning = true
                getIndicatorsTable({
                    mainCondition: this.obj,
                    userId: userId,
                    columns: Array.from(new Set(col)).join(','),
                    pageNo: page,
                    pageSize: 100,
                    screenConditon: this.$refs.vTable.obj22
                }).then(res => {
                    if (res.result == 'success') {
                        this.spinning = false
                        this.tableDatas = res.rows
                        this.total = res.total
                        /* debugger
          var lista=document.querySelectorAll('.has-gutter .canDrag')
          for(var i=0;i<lista.length;i++){
            lista[i].onmouseover = function(e){
              let cc = e.target.children[0].children[0].children[1].attributes[1].value = 'position:absolute;top:-4px;right:0px;font-size:16px;display:block';
            }
            lista[i].onmouseleave = function(e){
              let cc = e.target.children[0].children[0].children[1].attributes[1].value = 'position:absolute;top:-4px;right:0px;font-size:16px;display:none';
            }
          } */
                    } else {
                        this.tableDatas = []
                        this.$message.error(res.msg)
                    }
                })
            },
            loadMore() {
                console.log(this.loadSign)
                if (this.aq == false) {
                    return
                }
                /* if(this.loadSign === this.pageNo){
        debugger
        return;
      }
      this.loadSign = this.pageNo; */
                //this.pageNo == 1
                if (this.pageNo == 1) {
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
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                }
                let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
                let userId = userInfo.id
                console.log(this.obj)
                getIndicatorsTable({
                    mainCondition: this.obj,
                    userId: userId,
                    columns: Array.from(new Set(col)).join(','),
                    pageNo: this.pageNo,
                    pageSize: 100,
                    screenConditon: this.$refs.vTable.obj22
                }).then(res => {
                    if (res.result == 'success') {
                        if (res.rows.length > 0) {
                            this.pageNo++
                            res.rows.forEach(res => {
                                this.tableDatas.push(res)
                            });
                            console.log('到底了', this.pageNo)
                        } else {
                            this.aq = false
                        }
                    } else {
                        this.tableDatas = []
                        this.$message.error(res.msg)
                    }
                })
            },
            clearAll() {
                this.pageNo = 1;
                this.total = 1;
                this.obj33 = {};
                this.queryParam = {};
                this.model = {};
                this.tableData = [];
                this.tableDatas = [];
                this.startquarter = '';
                this.endquarter = '';
                this.level = '';
                this.price = '1';
                this.selectPrice = '1';
                this.validateStatus1 = '';
                this.validateStatus2 = '';
                this.validateStatus3 = '';
                this.$refs.vTable.sortColumn = '';
                this.$refs.vTable.obj22 = {}
                this.planStatus = '0'
            },
            selectPrices(val) {
                this.selectPrice = val
                this.getData(val);
            },
            getData(page, val) {
                if (this.queryParam.dimensionFlag === undefined || this.queryParam.periodFlag === undefined) {
                    this.$message.error("请先选择周期和维度")
                    return
                }
                /*  if(this.currentSelect.length === 0){
        this.$message.error("请先选择查询的指标")
        return;
      } */
                console.log(typeof page)
                /* if(typeof page!=='number'){
        this.pageNo = 1;
      } */
                console.log(this.tableData)
                //console.log(this.model.PLAN_BEGINTIME)
                /* if (this.model.dimCode === '' || this.model.dimCode === undefined) {
        this.$message.error('请填写完整搜索条件')
        return false
      } */
                if (!this.price) {
                    this.validateStatus3 = 'error';
                    return
                } else {
                    this.validateStatus3 = 'success'
                }
                /* if (!this.model.dimCode||this.model.dimCode === ''||this.model.dimCode.length === 0) {
                this.validateStatus1 = 'error';
                return
              } else {
                this.validateStatus1 = 'success'
              }
      if(this.queryParam.periodFlag === '1'){
        if (!this.model.startDate) {
                this.validateStatus2 = 'error';
                return
              } else {
                this.validateStatus2 = 'success'
              }
      }else if(this.queryParam.periodFlag ==='3'){
        if (!this.startquarter||!this.endquarter) {
                this.validateStatus2 = 'error';
                return
              } else {
                this.validateStatus2 = 'success'
              }
      }else{
        if (!this.model.startDate||!this.model.endDate) {
                this.validateStatus2 = 'error';
                return
              } else {
                this.validateStatus2 = 'success'
              }
      } */
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
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.selectPrice,
                        columns: Array.from(new Set(col)).join(',')
                    }
                }
                console.log(this.checkValue)
                this.spinning = true
                getIndicatorsTable({
                    mainCondition: this.obj,
                    userId: userId,
                    columns: Array.from(new Set(col)).join(','),
                    pageNo: page.order ? this.pageNo : 1,
                    pageSize: 100,
                    screenConditon: this.$refs.vTable.obj22
                }).then(res => {
                    if (res.result == 'success') {
                        this.spinning = false
                        this.tableDatas = res.rows
                        this.total = res.total
                        if (!page.order) {
                            this.currentPage = 1;
                            this.pageNo = 1;
                        }
                        /* debugger
          var lista=document.querySelectorAll('.has-gutter .canDrag')
          for(var i=0;i<lista.length;i++){
            lista[i].onmouseover = function(e){
              let cc = e.target.children[0].children[0].children[1].attributes[1].value = 'position:absolute;top:-4px;right:0px;font-size:16px;display:block';
            }
            lista[i].onmouseleave = function(e){
              let cc = e.target.children[0].children[0].children[1].attributes[1].value = 'position:absolute;top:-4px;right:0px;font-size:16px;display:none';
            }
          } */
                    } else {
                        this.tableDatas = []
                        this.$message.error(res.msg)
                    }
                })
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
                    chartname: '',
                    chartDirection: '',
                }
                this.dataSource1.push(j)
            },
            onChange(e) {
                console.log('radio checked', e.target.value)
                this.queryParam2 = {};
                this.showDeal = false;
                this.checkValue = e.target.value
                this.isTableEchart = false;
                if (this.checkValue === 4) {
                    this.dataSource1 = [];
                    this.tableData.forEach(item => {
                        this.dataSource1.push({
                            chartname: item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2],
                            chartId: item.INDEX_ID !== undefined ? item.INDEX_ID : item.id,
                            chartDirection: ''
                        });
                    })
                }
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                    }
                }
                let col = []
                this.tableData.forEach((el, index) => {
                    if (el.id) {
                        col.push(el.id)
                    } else {
                        col.push(el.INDEX_ID)
                    }
                })
                getDateInterval({
                    columns: Array.from(new Set(col)).join(','),
                    periodFlag: this.queryParam.periodFlag,
                    dimensionFlag: this.queryParam.dimensionFlag,
                    startDate: this.obj.startDate,
                    endDate: this.obj.endDate,
                }).then(res => {
                    if (res.result == 'success') {
                        this.periodFlagOption = res.rows
                    }
                })
                getDimensionSelect({
                    dimCode:
                        (this.model.dimCode && typeof this.model.dimCode === 'string')
                            ? this.model.dimCode ? this.model.dimCode : ''
                            : this.model.dimCode ? this.model.dimCode.join(',') : ''
                }).then(res => {
                    if (res.result == 'success') {
                        this.dimenOption = res.rows
                        console.log(typeof Array.from(this.dimenOption))
                    }
                })
                /*  if(this.queryParam.periodFlag === '2'){
        debugger
        console.log(this.model.startDate)
      } */
                //this.setEcharts()
            },
            handlechartCancel() {
                this.visibles = false
            },
            handlechartOk() {
            },
            select3(value) {
                if (value === 'X') {
                    this.toShow = true;
                    this.toShow1 = false;
                } else {
                    this.toShow = false;
                    this.toShow1 = true;
                }
            },
            select4(value) {
                if (value === 'X') {
                    this.toShow2 = true;
                    this.toShow3 = false;
                } else {
                    this.toShow2 = false;
                    this.toShow3 = true;
                }
            },
            pieChart() {
                this.isTableEchart = true;
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
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                }
                this.onlineData = {}
                getIndicatorsECharts({
                    mainCondition: this.obj,
                    userId: userId,
                    columns: Array.from(new Set(col)).join(','),
                    screenConditon: this.obj33,
                    eChartsCondition: {
                        eChartsFlag: 'cake', //columnar:柱状图、lineChart:折线图、cake:饼图
                        direction: this.queryParam.turnTo, //X:横向、Y:纵向
                        eChartsDate: this.queryParam2.periodFlag1, //饼图选择的账期
                        indexName: this.queryParam.turnTo === 'X' ? Array.from(new Set(col)).join(',') : this.queryParam2.zhibiaoFlag1,//饼图指标
                        GK: this.queryParam2.dimensionFlag1,
                    }
                }).then(res => {
                    if (res.result === 'success') {
                        var ss = []
                        var ss1 = []
                        this.onlineData = {};
                        this.echartSeries = []
                        this.chartData = res.rows
                        //this.chartData = [{"a5e063ab2d4511ea905a000c298a21af":"189,165.00","b2cddcec2d4511ea905a000c298a21af":"76,014.81"}]
                        //this.titles = res.rows[0].GK
                        let res1 = this.tableData
                        let res2 = this.echartSeries
                        if (this.queryParam.turnTo === 'X') {
                            res1.map(item => {
                                //ss = []
                                this.chartData.map(item1 => {
                                    if (item1.hasOwnProperty(item.INDEX_ID !== undefined ? item.INDEX_ID : item.id)) {
                                        var obj = {}
                                        obj.value = item1[item.INDEX_ID !== undefined ? item.INDEX_ID : item.id].replace(/,/g, "");
                                        obj.name = item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2]
                                        res2.push(obj)
                                        /* debugger
              ss.push(valueitem1[item.INDEX_ID].replace(',', '')) */
                                    }
                                })
                                ss1.push(item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2])
                            })
                            console.log(ss, 'xxxx')
                            console.log(ss1, 'yyy')
                            console.log(res2, 'zzz')
                            this.onlineData = {
                                /*  title: {
            text: '饼图',
            left: 'center',
            top: '10'
          }, */
                                toolbox: { //可视化的工具箱
                                    show: true,
                                    feature: {
                                        saveAsImage: {//保存图片
                                            show: true
                                        },

                                    }
                                },
                                tooltip: {
                                    trigger: 'item',
                                    formatter: '{a} <br/>{b} : {c} ({d}%)'
                                },
                                color: ['#85e2c5', '#fcbf49', '#73cdf3', '#b488f7', '#7986eb'],
                                legend: {
                                    orient: 'vertical',
                                    right: 10,
                                    top: 20,
                                    bottom: 20,
                                    data: ss1
                                },
                                series: [
                                    {
                                        name: '指标',
                                        type: 'pie',
                                        radius: '55%',
                                        center: ['50%', '50%'],
                                        emphasis: {
                                            itemStyle: {
                                                shadowBlur: 10,
                                                shadowOffsetX: 0,
                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                            }
                                        },
                                        data: this.echartSeries
                                    }
                                ]
                            }
                        } else {
                            console.log(ss1)
                            console.log(this.echartSeries)
                            res1.map(item => {
                                //ss = []
                                this.chartData.map(item1 => {
                                    if (item1.hasOwnProperty(item.INDEX_ID !== undefined ? item.INDEX_ID : item.id)) {
                                        var obj = {}
                                        obj.value = item1[item.INDEX_ID !== undefined ? item.INDEX_ID : item.id].replace(/,/g, "");
                                        obj.name = item1.GK
                                        res2.push(obj)
                                        ss1.push(item1.GK)
                                        /* debugger
              ss.push(valueitem1[item.INDEX_ID].replace(',', '')) */
                                    }
                                })
                                //ss1.push(item.INDEX_NAME)
                            })
                            this.onlineData = {
                                /*  title: {
            text: '饼图',
            left: 'center',
            top: '10'
          }, */
                                toolbox: { //可视化的工具箱
                                    show: true,
                                    feature: {
                                        saveAsImage: {//保存图片
                                            show: true
                                        },

                                    }
                                },
                                tooltip: {
                                    trigger: 'item',
                                    formatter: '{a} <br/>{b} : {c} ({d}%)'
                                },
                                color: ['#85e2c5', '#fcbf49', '#73cdf3', '#b488f7', '#7986eb'],
                                legend: {
                                    orient: 'vertical',
                                    right: 10,
                                    top: 20,
                                    bottom: 20,
                                    data: ss1
                                },
                                series: [
                                    {
                                        name: '维度',
                                        type: 'pie',
                                        radius: '55%',
                                        center: ['50%', '50%'],
                                        emphasis: {
                                            itemStyle: {
                                                shadowBlur: 10,
                                                shadowOffsetX: 0,
                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                            }
                                        },
                                        data: this.echartSeries
                                    }
                                ]
                            }
                        }
                    }
                })
            },
            showDeals(option) {
                if (option === true) {
                    this.showDeal = true;
                    this.setEcharts();
                } else {
                    this.showDeal = false;
                    this.setEcharts();
                }
            },
            setEcharts() {
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
                if (this.queryParam.periodFlag === '1') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM-DD') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM-DD') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '2') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY-MM') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY-MM') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else if (this.queryParam.periodFlag === '4') {
                    this.obj = {
                        //方案查询条件
                        startDate: this.model.startDate ? this.model.startDate.format('YYYY') : '', //账期起始日
                        endDate: this.model.endDate ? this.model.endDate.format('YYYY') : '', //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        startDate: this.startquarter.replace('-', ''), //账期起始日
                        endDate: this.endquarter.replace('-', ''), //账期结束日
                        dimensionFlag: this.queryParam.dimensionFlag, //查询维度
                        periodFlag: this.queryParam.periodFlag, //周期
                        dimCode:
                            (this.model.dimCode && typeof this.model.dimCode === 'string')
                                ? this.model.dimCode ? this.model.dimCode : ''
                                : this.model.dimCode ? this.model.dimCode.join(',') : '', //国库
                        price: this.price,
                        columns: Array.from(new Set(col)).join(',')
                    }
                }
                if (this.checkValue === 1) {
                    this.isTableEchart = true;
                    getIndicatorsECharts({
                        mainCondition: this.obj,
                        userId: userId,
                        columns: col.join(','),
                        screenConditon: this.obj33,
                        eChartsCondition: {
                            eChartsFlag: 'columnar', //columnar:柱状图、lineChart:折线图、cake:饼图
                            direction: this.xTurn === 'X' ? this.queryParam2.guokuId : '', //X:横向、Y:纵向
                            eChartsDate: this.xTurn === 'Y' ? this.queryParam2.dateId : '', //饼图选择的账期
                            indexName: '', //饼图指标
                            GK: '' //饼图选择的指标/国库/地区
                        }
                    }).then(res => {
                        if (res.result === 'success') {
                            var ss = []
                            var ss1 = []
                            this.echartSeries = []
                            this.xAxisData = []
                            this.chartData = res.rows
                            //this.titles = res.rows[1].GK
                            let res1 = this.tableData
                            let res2 = this.echartSeries
                            let yAxis = [];
                            let tooltip = {};
                            if (this.price === '1') {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    //splitNumber:10,
                                    axisLabel: {
                                        formatter: '{value} 元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max).toFixed(0);
                                        },
                                        min: function (value) {
                                            return ((value.max * yAxisMin) / yAxisMax).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }

                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '元';
                                            return res
                                        }

                                    },
                                }
                            } else if (this.price === '10000') {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    axisLabel: {
                                        formatter: '{value} 万元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max).toFixed(0);
                                        },
                                        min: function (value) { //y轴左右两边0刻度对齐 比例一致
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '万元';
                                            return res
                                        }
                                    },
                                }
                            } else {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    axisLabel: {
                                        formatter: '{value} 亿元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max).toFixed(0);
                                        },
                                        min: function (value) { //y轴左右两边0刻度对齐 比例一致
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '亿元';
                                            return res
                                        }
                                    },
                                }
                            }
                            res1.map(item => {
                                ss = []
                                this.chartData.map(item1 => {
                                    if (item1 === null) {
                                        return false;
                                    } else if (item1.hasOwnProperty(item.INDEX_ID !== undefined ? item.INDEX_ID : item.id)) {
                                        ss.push(item1[item.INDEX_ID !== undefined ? item.INDEX_ID : item.id].replace(/,/g, ""))
                                    }
                                })
                                ss1.push(item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2])
                                console.log(this.showDeal)
                                var oThis = this;
                                res2.push({
                                    name: item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2],
                                    type: 'bar',
                                    yAxisIndex: (item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label).indexOf("%") !== -1 ? 1 : 0,
                                    barMaxWidth: 30,
                                    data: ss,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                show: this.showDeal, position: 'top', formatter: function (params) {
                                                    if ((params.seriesName).indexOf("%") !== -1) {
                                                        let res = params.value + '%';
                                                        return res
                                                    } else {
                                                        if (oThis.price === '1') {
                                                            let res = params.value + '元';
                                                            return res
                                                        } else if (oThis.price === '10000') {
                                                            let res = params.value + '万元';
                                                            return res
                                                        } else if (oThis.price === '100000000') {
                                                            let res = params.value + '亿元';
                                                            return res
                                                        }
                                                    }

                                                },
                                            }
                                        }
                                    }
                                })
                                console.log(ss, '1234')
                                console.log(res2, '1111')
                            })

                            this.chartData.forEach(el => {
                                if (el === null) {
                                    return false;
                                } else {
                                    if (this.xTurn === 'X') {
                                        this.xAxisData.push(el.ACCOUNT_PERIOD)
                                    } else {
                                        this.xAxisData.push(el.GK)
                                    }
                                }
                            })

                            this.onlineData = {
                                /* title: {
              text: this.titles,
              left: 'center',
              top: '10'
            }, */
                                toolbox: { //可视化的工具箱
                                    show: true,
                                    feature: {
                                        saveAsImage: {//保存图片
                                            show: true
                                            /* icon: 'image:~@/assets/5.png' */
                                        },

                                    }
                                },
                                tooltip: tooltip,
                                legend: {
                                    data: ss1,
                                    //top:0,
                                    bottom: 0,
                                    left: 'center',
                                    //top: '50%', //距上边距
                                    //bottom: '20%', //距离下边距
                                    textStyle: {
                                        //图例文字的样式
                                        color: '#333',
                                        fontSize: 12
                                    }
                                },
                                dataZoom: [
                                    {
                                        type: 'slider',
                                        show: true,
                                        xAxisIndex: [0],
                                        start: 0,
                                        end: 200,
                                        bottom: '10%'
                                    }
                                ],
                                xAxis: [
                                    {
                                        type: 'category',
                                        data: this.xAxisData, //X轴标签
                                        nameTextStyle: {
                                            color: '#333',
                                            padding: [5, 0, 0, -5], //---坐标轴名称相对位置
                                            fontSize: 14
                                        },
                                        nameGap: 20,
                                        axisLine: {
                                            //---坐标轴 轴线
                                            show: true, //---是否显示
                                            lineStyle: {
                                                color: '#333',
                                                width: 1,
                                                type: 'solid'
                                            }
                                        }
                                    }
                                ],
                                yAxis: yAxis,
                                series: res2
                            }
                        } else {
                            this.$message.error(res.msg)
                            this.onlineData = {};
                        }
                    })
                } else if (this.checkValue === 2) {
                    this.isTableEchart = true;
                    getIndicatorsECharts({
                        mainCondition: this.obj,
                        userId: userId,
                        columns: col.join(','),
                        screenConditon: this.obj33,
                        eChartsCondition: {
                            eChartsFlag: 'lineChart', //columnar:柱状图、lineChart:折线图、cake:饼图
                            direction: this.xTurn === 'X' ? this.queryParam2.guokuId1 : '', //X:横向、Y:纵向
                            eChartsDate: this.xTurn === 'Y' ? this.queryParam2.dateId2 : '', //饼图选择的账期
                            indexName: '', //饼图指标
                            GK: '', //饼图选择的指标/国库/地区
                        }
                    }).then(res => {
                        if (res.result === 'success') {
                            var ss = []
                            var ss1 = []
                            this.echartSeries = []
                            this.xAxisData = []
                            this.chartData = res.rows
                            //this.titles = res.rows[1].GK
                            let res1 = this.tableData
                            let res2 = this.echartSeries
                            let yAxis = [];
                            let tooltip = {};
                            if (this.price === '1') {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max * 1.2).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    //splitNumber:10,
                                    axisLabel: {
                                        formatter: '{value} 元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max * 1.2).toFixed(0);
                                        },
                                        min: function (value) {
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '元';
                                            return res
                                        }

                                    },
                                }
                            } else if (this.price === '10000') {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max * 1.2).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    axisLabel: {
                                        formatter: '{value} 万元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max * 1.2).toFixed(0);
                                        },
                                        min: function (value) { //y轴左右两边0刻度对齐 比例一致
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '万元';
                                            return res
                                        }
                                    },
                                }
                            } else {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max * 1.2).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    axisLabel: {
                                        formatter: '{value} 亿元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max * 1.2).toFixed(0);
                                        },
                                        min: function (value) { //y轴左右两边0刻度对齐 比例一致
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '亿元';
                                            return res
                                        }
                                    },
                                }
                            }
                            res1.map(item => {
                                ss = []
                                this.chartData.map(item1 => {
                                    if (item1 === null) {
                                        return false;
                                    } else if (item1.hasOwnProperty(item.INDEX_ID !== undefined ? item.INDEX_ID : item.id)) {
                                        ss.push(item1[item.INDEX_ID !== undefined ? item.INDEX_ID : item.id].replace(/,/g, ""))
                                    }
                                })
                                ss1.push(item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2])
                                var oThis = this;
                                res2.push({
                                    name: item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label.split('▲')[0] + item.label.split('▲')[2],
                                    type: 'line',
                                    yAxisIndex: (item.INDEX_NAME !== undefined ? item.INDEX_NAME : item.label).indexOf("%") !== -1 ? 1 : 0,
                                    barMaxWidth: 30,
                                    data: ss,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                show: this.showDeal, position: 'top', formatter: function (params) {
                                                    if ((params.seriesName).indexOf("%") !== -1) {
                                                        let res = params.value + '%';
                                                        return res
                                                    } else {
                                                        if (oThis.price === '1') {
                                                            let res = params.value + '元';
                                                            return res
                                                        } else if (oThis.price === '10000') {
                                                            let res = params.value + '万元';
                                                            return res
                                                        } else if (oThis.price === '100000000') {
                                                            let res = params.value + '亿元';
                                                            return res
                                                        }
                                                    }

                                                },
                                            }
                                        }
                                    }
                                })
                                console.log(ss, '123')
                                console.log(res2, '1111')
                            })

                            this.chartData.forEach(el => {
                                if (el === null) {
                                    return false;
                                } else {
                                    if (this.xTurn === 'X') {
                                        this.xAxisData.push(el.ACCOUNT_PERIOD)
                                    } else {
                                        this.xAxisData.push(el.GK)
                                    }
                                }
                            })

                            this.onlineData = {
                                /* title: {
              text: this.titles,
              left: 'center',
              top: '10'
            }, */
                                toolbox: { //可视化的工具箱
                                    show: true,
                                    feature: {
                                        saveAsImage: {//保存图片
                                            show: true
                                        },

                                    }
                                },
                                tooltip: tooltip,
                                legend: {
                                    data: ss1,
                                    bottom: 0,
                                    left: 'center',
                                    //top: '10', //距上边距
                                    //bottom: '20%', //距离下边距
                                    textStyle: {
                                        //图例文字的样式
                                        color: '#333',
                                        fontSize: 12
                                    }
                                },
                                dataZoom: [
                                    {
                                        type: 'slider',
                                        show: true,
                                        xAxisIndex: [0],
                                        start: 0,
                                        end: 200,
                                        bottom: '10%'
                                    }
                                ],
                                xAxis: [
                                    {
                                        type: 'category',
                                        data: this.xAxisData, //X轴标签
                                        nameTextStyle: {
                                            color: '#333',
                                            padding: [5, 0, 0, -5], //---坐标轴名称相对位置
                                            fontSize: 14
                                        },
                                        nameGap: 20,
                                        axisLine: {
                                            //---坐标轴 轴线
                                            show: true, //---是否显示
                                            lineStyle: {
                                                color: '#333',
                                                width: 1,
                                                type: 'solid'
                                            }
                                        }
                                    }
                                ],
                                yAxis: yAxis,
                                series: res2
                            }
                        } else {
                            this.$message.error(res.msg)
                            this.onlineData = {};
                        }
                    })
                } else if (this.checkValue === 4) {
                    this.isTableEchart = true;
                    console.log(this.dataSource1)
                    let obj55 = {};
                    let obj66 = [];
                    this.dataSource1.forEach((item) => obj66 = item.chartId);
                    this.dataSource1.forEach((item) => obj55[item.chartId] = item.chartDirection);
                    console.log(obj55)
                    getColumnLineChart({
                        mainCondition: this.obj,
                        userId: userId,
                        columns: col.join(','),
                        eChartsCondition: {
                            direction: this.xTurn === 'X' ? this.queryParam2.guokuId2 : '', //X:横向、Y:纵向
                            eChartsDate: this.xTurn === 'Y' ? this.queryParam2.dateId1 : '', //饼图选择的账期
                        },
                        indexName: obj55,
                        screenConditon: this.obj33,
                    }).then(res => {
                        if (res.result === 'success') {
                            var ss = []
                            var sss = []
                            var ss1 = []
                            this.echartSeries = []
                            this.xAxisData = []
                            this.chartData = res.rows
                            //this.titles = res.rows[1].GK
                            let res1 = this.dataSource1
                            let res2 = this.echartSeries
                            let yAxis = [];
                            let tooltip = {};
                            if (this.price === '1') {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max * 1.2).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    //splitNumber:10,
                                    axisLabel: {
                                        formatter: '{value} 元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max * 1.2).toFixed(0);
                                        },
                                        min: function (value) {
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '元';
                                            return res
                                        }

                                    },
                                }
                            } else if (this.price === '10000') {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max * 1.2).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    axisLabel: {
                                        formatter: '{value} 万元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max * 1.2).toFixed(0);
                                        },
                                        min: function (value) { //y轴左右两边0刻度对齐 比例一致
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '万元';
                                            return res
                                        }
                                    },
                                }
                            } else {
                                let yAxisMax;
                                let yAxisMin;
                                yAxis = [{
                                    type: 'value',
                                    name: '金额',
                                    max: function (value) {
                                        yAxisMax = value.max;
                                        return (value.max * 1.2).toFixed(0);
                                    },
                                    min: function (value) {
                                        if (value.min < 0) {
                                            yAxisMin = value.min;
                                            return (value.min * 1.2).toFixed(0);
                                        } else {
                                            yAxisMin = 0;
                                            return 0;
                                        }
                                    },
                                    boundaryGap: [0, 0.01],
                                    axisLabel: {
                                        formatter: '{value} 亿元'
                                    }
                                },
                                    {
                                        type: 'value',
                                        name: '比率',
                                        max: function (value) {
                                            return (value.max * 1.2).toFixed(0);
                                        },
                                        min: function (value) { //y轴左右两边0刻度对齐 比例一致
                                            return ((value.max * yAxisMin) / yAxisMax * 1.2).toFixed(0);
                                        },
                                        axisLabel: {
                                            formatter: '{value} %'
                                        },
                                        splitLine: {
                                           show: false,
                                        }
                                    }]
                                tooltip = {
                                    trigger: 'item',
                                    /* formatter: '{a} <br/>{b} : {c} ({d}%)' */
                                    formatter: function (params) {
                                        if ((params.seriesName).indexOf("%") !== -1) {
                                            let res = params.seriesName + '：' + params.value + '%';
                                            return res
                                        } else {
                                            let res = params.seriesName + '：' + params.value + '亿元';
                                            return res
                                        }
                                    },
                                }
                            }
                            res1.map(item => {
                                ss = []
                                sss = []
                                this.chartData[0].map(item1 => {
                                    if (item1 === null) {
                                        return false;
                                    } else if (item1.hasOwnProperty(item.chartId !== undefined ? item.chartId : item.id)) {
                                        ss.push(item1[item.chartId !== undefined ? item.chartId : item.id].replace(/,/g, ""))
                                    }
                                })
                                this.chartData[1].map(item1 => {
                                    if (item1 === null) {
                                        return false;
                                    } else if (item1.hasOwnProperty(item.chartId !== undefined ? item.chartId : item.id)) {
                                        sss.push(item1[item.chartId !== undefined ? item.chartId : item.id].replace(/,/g, ""))
                                    }
                                })
                                ss1.push(item.chartname !== undefined ? item.chartname : item.label.split('▲')[0] + item.label.split('▲')[2])
                                var oThis = this;
                                res2.push({
                                    name: item.chartname !== undefined ? item.chartname : item.label.split('▲')[0] + item.label.split('▲')[2],
                                    type: item.chartDirection === "Columnar" ? 'bar' : 'line',
                                    barMaxWidth: 30,
                                    yAxisIndex: item.chartname.indexOf("%") !== -1 ? 1 : 0,
                                    data: item.chartDirection === "Columnar" ? ss : sss,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                show: this.showDeal, position: 'top', formatter: function (params) {
                                                    if ((params.seriesName).indexOf("%") !== -1) {
                                                        let res = params.value + '%';
                                                        return res
                                                    } else {
                                                        if (oThis.price === '1') {
                                                            let res = params.value + '元';
                                                            return res
                                                        } else if (oThis.price === '10000') {
                                                            let res = params.value + '万元';
                                                            return res
                                                        } else if (oThis.price === '100000000') {
                                                            let res = params.value + '亿元';
                                                            return res
                                                        }
                                                    }

                                                },
                                            }
                                        }
                                    }
                                })
                                console.log(ss, '123')
                                console.log(res2, '1111')
                            })

                            this.chartData[0].forEach(el => {
                                if (el === null) {
                                    return false;
                                } else {
                                    if (this.xTurn === 'X') {
                                        this.xAxisData.push(el.ACCOUNT_PERIOD)
                                    } else {
                                        this.xAxisData.push(el.GK)
                                    }
                                }
                            })
                            this.chartData[1].forEach(el => {
                                if (el === null) {
                                    return false;
                                } else {
                                    if (this.xTurn === 'X') {
                                        this.xAxisData.push(el.ACCOUNT_PERIOD)
                                    } else {
                                        this.xAxisData.push(el.GK)
                                    }
                                }
                            })
                            let xAxisDatas = Array.from(new Set(this.xAxisData))
                            this.onlineData = {
                                /* title: {
              text: this.titles,
              left: 'center',
              top: '10'
            }, */
                                toolbox: { //可视化的工具箱
                                    show: true,
                                    feature: {
                                        saveAsImage: {//保存图片
                                            show: true
                                        },

                                    }
                                },
                                tooltip: tooltip,
                                legend: {
                                    data: ss1,
                                    bottom: 0,
                                    left: 'center',
                                    //top: '10', //距上边距
                                    //bottom: '20%', //距离下边距
                                    textStyle: {
                                        //图例文字的样式
                                        color: '#333',
                                        fontSize: 12
                                    }
                                },
                                dataZoom: [
                                    {
                                        type: 'slider',
                                        show: true,
                                        xAxisIndex: [0],
                                        start: 0,
                                        end: 200,
                                        bottom: '10%'
                                    }
                                ],
                                xAxis: [
                                    {
                                        type: 'category',
                                        data: xAxisDatas, //X轴标签
                                        nameTextStyle: {
                                            color: '#333',
                                            padding: [5, 0, 0, -5], //---坐标轴名称相对位置
                                            fontSize: 14
                                        },
                                        nameGap: 20,
                                        axisLine: {
                                            //---坐标轴 轴线
                                            show: true, //---是否显示
                                            lineStyle: {
                                                color: '#333',
                                                width: 1,
                                                type: 'solid'
                                            }
                                        }
                                    }
                                ],
                                yAxis: yAxis,
                                series: res2
                            }
                        } else {
                            this.$message.error(res.msg)
                            this.onlineData = {};
                        }
                    })
                } else {
                    this.onlineData = {};
                }
            }
        }
    }
</script>

<style lang="less">
    .model-right {
        .ant-col-sm-10 {
            .ant-col-3 {
                text-align: left;
            }
        }
    }

    .ant-input {
        font-weight: bold;
    }

    .model {
        //display: flex;
    }

    .model .model-left {
        //width: 25%;
        //flex: 0 0 25%;
        width: 25%;
        display: inline-block;
        float: left;
    }

    .model .model-left .model-title {
        background: rgb(242, 242, 242);
        line-height: 40px;
        padding-left: 10px;
    }

    .model .model-right {
        //width: 73%;
        width: 71%;
        display: inline-block;
        float: right;
        /* height: 416px; */
        /*overflow: auto;*/
        /*  overflow-x: hidden; */

        .ant-form-explain {
            height: 0;
            min-height: 0;
        }

    }

    .model .model-middle {
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
        margin-right: 20px;
        border-left: #000 !important;
        background: #fff;
        border: 1px solid #ccc;
        padding-top: 10px;
        padding-left: 0px;
        padding-bottom: 10px;
    }

    .wd228 {
        width: 68% !important;
        margin-top: 15px;
    }

    .wd229 {
        width: 68% !important;
    }

    .model .el-tree {
        max-height: 240px;
        overflow-y: scroll;
        //overflow-x: auto;
        //overflow:-moz-scrollbars-none;
        scrollbar-width: none; //兼容火狐
        -ms-overflow-style: none; //兼容IE
        -webkit-overflow-style: none; //兼容IE
        margin-top: 10px;
    }

    /* .dragfile{
  position:absolute;
  top:200px;
  background:none;
  height:160px;
  width:25%;
  cursor:move;
} */
    .ant-select-selection--multiple .ant-select-selection__choice {
        max-width: 62% !important;
    }

    .page {
        float: right;
        margin-top: 10px;
    }

    .chart {
        font-size: 28px;
        /* display: inline-block; */
        float: right;
        margin-right: 10px;
    }

    .flex-radio {
        text-align: center;
    }

    .flex-radio .ant-radio-group {
        display: flex;
    }

    .flex-radio .ant-radio-group .ant-radio-wrapper {
        width: 25%;
    }

    #filter-tree .el-tree-node .el-checkbox .el-checkbox__inner {
        display: none !important;
    }

    #filter-tree .el-tree-node .is-leaf + .el-checkbox .el-checkbox__inner {
        display: block !important;
    }

    #filter-tree .el-tree-node .el-checkbox .el-checkbox__inner {
        display: inline-block;
    }

    #filter-tree1 .el-tree-node .el-checkbox .el-checkbox__inner {
        display: none !important;
    }

    #filter-tree1 .el-tree-node .is-leaf + .el-checkbox .el-checkbox__inner {
        display: block !important;
    }

    #filter-tree1 .el-tree-node .el-checkbox .el-checkbox__inner {
        display: inline-block;
    }

    #table22 {
        height: 300px;
        overflow-y: scroll;
    }

    #table22 .ant-table-tbody > tr td:first-child {
        text-align: left !important;
    }

    .tree_menu {
        position: fixed;
        display: block;
        z-index: 20000;
        background-color: #fff;
        padding: 5px 0;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

        ul {
            margin: 0;
            padding: 0;
        }

        ul li {
            list-style: none;
            margin: 0;
            padding: 0 15px;
            font-size: 14px;
            line-height: 30px;
            cursor: pointer;
        }

        ul li:hover {
            background-color: #ebeef5
        }
    }

    .tree_menu1 {
        position: fixed;
        display: block;
        z-index: 20000;
        background-color: #fff;
        width: 200px;
        padding: 10px 10px;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);

        ul {
            margin: 0;
            padding: 0;
        }

        ul li {
            list-style: none;
            margin: 0;
            padding: 0 30px;
            font-size: 14px;
            line-height: 30px;
            cursor: pointer;
        }

        ul li:hover {
            background-color: #ebeef5
        }
    }
</style>
