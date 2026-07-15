<template>
  <a-modal v-model="visible" @ok="handleOk" @cancel="clearSetting" :title="title" width="80%">
    <div :style="{ width: '100%', padding: '0px 16px', background: '#fff' }">
      <a-spin :spinning="confirmLoading">
        <div class="circle">
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
              <a-row :gutter="24">
                <a-col :md="6" :sm="12">
                  <a-form-item label="图库名称">
                    <a-input placeholder="请输入关键字" v-model="searchParam.title"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="12">
                  <a-form-item label="业务类型">
                    <a-select v-model="searchParam.business_id" placeholder="请选择类型">
                      <a-select-option value=""> 请选择 </a-select-option>
                      <a-select-option :value="item.business_id" v-for="(item, i) in bussTypeData" :key="i">
                        {{ item.business_name }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <div style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                    <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px"
                      >重置
                    </a-button>
                  </div>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <h2 style="font-size: 18px">选择图表</h2>
          <ul class="circle-ul">
            <li v-for="item of dataSource1" :key="item.id" class="circle-li">
              <div v-on:click="changeList(item.id, item)" value="change!" class="circle-img" v-if="item.id">
                <div v-html="item.content" v-if="item.type == 'h' && item.content"></div>
                <img :src="item.content" alt v-else-if="item.content" />
                <p>{{ item.title }}</p>
                <div class="topic-shade" v-show="item.id == currentImg">
                  <img src="@/assets/checkbox.png" style="width: 14px; height: 14px" alt />
                </div>
              </div>
            </li>
          </ul>
          <h2 style="font-size: 18px; margin-bottom: 1rem">设置图表逻辑</h2>
          <div class="chartSetClass" v-if="itemSet.time_type">
            <h2 class="required">单位</h2>
            <a-select :value="queryParam.unit" placeholder="单位" @change="unitChange">
              <a-select-option value="1">元</a-select-option>
              <a-select-option value="10000">万元</a-select-option>
              <a-select-option value="100000000">亿元</a-select-option>
            </a-select>
          </div>
          <div class="chartSetClass m1" v-if="itemSet.time_type">
            <h2 class="required">时间类型</h2>
            <a-radio-group :value="queryParam.timeType" @change="timeChange">
              <a-radio value="1" v-if="itemSet.dacct_radio == '1'"> 至今 </a-radio>
              <a-radio value="2" v-if="itemSet.dacct_radio == '1'"> 时间区间 </a-radio>
              <!-- <a-radio value="1"> 至今 </a-radio>
              <a-radio value="2"> 时间区间 </a-radio> -->
              <a-radio value="4" v-if="itemSet.dacct_radio == '0'"> 时间 </a-radio>
              <a-radio value="3"> 当前 </a-radio>
            </a-radio-group>
          </div>
          <div class="chartSetClass m1" v-if="itemSet.time_type">
            <h2 class="required">时间周期</h2>
            <a-select :value="queryParam.time_type" placeholder="时间周期" @change="cycleChange">
              <a-select-option value="d" v-if="itemSet.time_type.indexOf('d') >= 0">日</a-select-option>
              <a-select-option value="m" v-if="itemSet.time_type.indexOf('m') >= 0">月</a-select-option>
              <a-select-option value="q" v-if="itemSet.time_type.indexOf('q') >= 0">季</a-select-option>
              <a-select-option value="y" v-if="itemSet.time_type.indexOf('y') >= 0">年</a-select-option>
            </a-select>
          </div>
          <div
            class="chartSetClass m1"
            v-if="queryParam.time_type && queryParam.timeType != 3 && queryParam.timeType != 'null'"
          >
            <h2 class="required">选择时间</h2>
            <span v-if="(queryParam.timeType == '1'&& queryParam.time_type != 'q') || (queryParam.timeType == '4' && queryParam.time_type != 'q')">
              <el-date-picker
                v-model="startTimeData"
                :value-format="
                  queryParam.time_type == 'd' ? 'yyyy-MM-dd' : queryParam.time_type == 'm' ? 'yyyy-MM' : 'yyyy'
                "
                :type="queryParam.time_type == 'd' ? 'date' : queryParam.time_type == 'm' ? 'month' : 'year'"
                :placeholder="
                  queryParam.time_type == 'd' ? '请选择日期' : queryParam.time_type == 'm' ? '请选择月份' : '请选择年份'
                "
              >
              </el-date-picker>
              <!-- <a-date-picker v-model="startTimeData" :valueFormat="dateFormatList" placeholder="请选择起始时间" /> -->
              <span v-if="queryParam.timeType != '4'">~ 至今</span>
            </span>
            <!-- 至今季时间选择器开始 -->
            <span v-if="queryParam.timeType == '1' && queryParam.time_type == 'q'">
              <data-month
                :timeType="1"
                :choseQuarterData="startTimeData"
                @startquarter="getStartQuarterNow"
              ></data-month>
            </span>
            <span v-if="queryParam.timeType == '4' && queryParam.time_type == 'q'">
              <data-month
                :timeType="3"
                :choseQuarterData="startTimeData"
                @startquarter="getStartQuarterNow"
              ></data-month>
            </span>
            <!-- 至今季时间选择器结束 -->
            <!-- 时间区域 日、月、年时间选择期 -->
            <span v-if="queryParam.timeType == '2'">
              <data-year
                v-if="queryParam.time_type == 'y'"
                :startYearData="startYear"
                :endYearData="endYear"
                @startYearValue="getStartYearValue"
                @endYearValue="getEndYearValue"
              ></data-year>
              <el-date-picker
                v-else-if="queryParam.time_type == 'd' || queryParam.time_type == 'm'"
                v-model="timeData"
                :value-format="queryParam.time_type == 'd' ? 'yyyy-MM-dd' : 'yyyy-MM'"
                :type="queryParam.time_type == 'd' ? 'daterange' : 'monthrange'"
                range-separator="~"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
              <data-month
                v-else
                :timeType="2"
                :choseQuarterData="startquarter"
                :choseQuarterData1="endquarter"
                @startquarter="startquarter1"
                @endquarter="endquarter1"
              ></data-month>
            </span>

            <!-- <a-range-picker v-if="queryParam.timeType == '2'" :valueFormat="dateFormatList" v-model="timeData" /> -->
          </div>
          <div
            class="chartSetClass m1"
            v-if="itemSet.dimension_type ? itemSet.dimension_type.indexOf('b') >= 0 : false"
          >
            <h2 class="required">核算主体</h2>
            <a-tree-select
              showSearch
              treeNodeFilterProp="label"
              :value="queryParam.accounting"
              style="width: 60%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="bookorgTreeData"
              @change="accountChange"
              placeholder="请选择核算主体"
            >
            </a-tree-select>
          </div>
          <div
            class="chartSetClass m1"
            v-if="itemSet.dimension_type ? itemSet.dimension_type.indexOf('a') >= 0 : false"
          >
            <h2 class="required">所属地区</h2>
            <a-tree-select
              showSearch
              treeNodeFilterProp="label"
              :value="queryParam.areaid"
              style="width: 60%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="areaTreeData"
              @change="areaChange"
              placeholder="请选择地区"
            >
            </a-tree-select>
          </div>
          <div
            class="chartSetClass m1"
            v-if="itemSet.dimension_type ? itemSet.dimension_type.indexOf('g') >= 0 : false"
          >
            <h2 class="required">所属国库</h2>
            <a-tree-select
              showSearch
              treeNodeFilterProp="label"
              :value="queryParam.guokuid"
              style="width: 60%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="guokuTreeData"
              @change="guokuChange"
              placeholder="请选择所属国库"
            >
            </a-tree-select>
          </div>
          <div
            class="chartSetClass m1"
            v-if="itemSet.dimension_type ? itemSet.dimension_type.indexOf('s') >= 0 : false"
          >
            <h2 class="required">科目类&#12288;</h2>
            <a-tree-select
              showSearch
              treeNodeFilterProp="label"
              :value="queryParam.subject_code"
              style="width: 60%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="kemuTreeData"
              @change="kemuChange"
              placeholder="请选择科目类"
            >
            </a-tree-select>
          </div>
          <div
            class="chartSetClass m1"
            v-if="itemSet.dimension_type ? itemSet.dimension_type.indexOf('ts') >= 0 : false"
          >
            <h2 class="required">T科目类&#8194;</h2>
            <a-tree-select
              showSearch
              treeNodeFilterProp="label"
              :value="queryParam.tsubject_code"
              style="width: 60%"
              :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
              :treeData="subjectTreeData"
              @change="subjectChange"
              placeholder="请选择T科目类"
            >
            </a-tree-select>
          </div>
          <div  v-if="imgSrcList != null&&imgSrcList.type=='h'">
            <div class="chartSetClass m1"  style="align-items: flex-start;">
              <h2 class="required">文本标题</h2>
              <a-input placeholder="请输入标题" v-model="textTitle" style="width: 60%"/>
            </div>
            <div class="chartSetClass m1"  style="align-items: flex-start;">
              <h2 class="required">文本内容</h2>
              <div class="htmlText" ref="editor" contenteditable="true" @input="changeText">
                <p></p>
              </div>
            </div>
            
          </div>          
        </div>
      </a-spin>
    </div>
  </a-modal>
</template>

<script>
import dataYear from './dataYear'
import dataMonth from './dataMonth'
import { getAreaTree, getBusinessTypeList, getGalleryList, getGuokuTree, getKeMuTreeName, getOrgTree, getSubjectTree } from '@/api/visScreen'
import moment from 'moment'
import pick from 'lodash.pick'

export default {
  name: 'AddTemplateDrawer',
  components: {
    dataYear,
    dataMonth,
  },
  data() {
    return {
      dateFormatList: 'YYYY-MM-DD',
      title: '操作',
      visible: false,
      bussTypeData: [], // 类型
      areaTreeData: [],
      bookorgTreeData: [],
      guokuTreeData: [], //国库数据
      kemuTreeData: [], //科目数据
      subjectTreeData: [],//T科目数据
      dataSource1: [],
      confirmLoading: false,
      currentImg: false, // 当前选择图片
      imgSrcList: null, // 选择图片的列表
      searchParam: {}, // 图库查询条件
      queryParam: {},
      itemSet: {},
      startTimeData: '', // 至今开始时间
      timeData: '', // 区域时间
      dateFormat: 'YYYY-MM-DD',
      modelData: {},
      startquarterNow: '', //至今开始季
      startquarter: '', // 时间区域开始季
      endquarter: '', // 时间区域结束季
      startYear: '', // 时间区域开始年份
      endYear: '', // 时间区域结束年份
      htmlContent: '', // html内容
      textTitle:'', // html标题
    }
  },
  props: {
    defaultData: { type: Object, default: {} },
  },
  computed: {},
  watch: {
    timeData(value) {
      // console.log(value)
    },
    defaultData(value) {
      if(this.dataSource1.length > 0 && value.gallery_id!='null'&& this.defaultData.gallery_id != null) {
        this.setCurrentData(value)
      }
    },
    dataSource1(value) {
      if(value.length > 0 && this.defaultData.gallery_id != null&& this.defaultData.gallery_id != 'null') {
        this.setCurrentData(this.defaultData)
      }
    }

  },
  mounted() {
    this.loadTree()
  },
  created() {},
  methods: {
    moment,
    changeList(id, item) {
      // 选中模板图片
      if (this.currentImg == id) {
        this.currentImg = !this.currentImg
        this.imgSrcList = null
        this.timeData = undefined
        this.startTimeData = undefined
        this.itemSet = {}
        this.queryParam = {}
        this.textTitle = ''
        this.htmlContent = ''
        // this.clearSetting();
      } else {
        // console.log(item)
        this.textTitle = ''
        this.htmlContent = ''
        this.currentImg = id
        this.imgSrcList = item
        this.startTimeData = undefined
        this.timeData = undefined
        this.itemSet = {}
        this.queryParam = {}
        if(item.type!='h'){
          this.queryParam.unit = '100000000'
          this.itemSet.time_type = item.time_type ? item.time_type : false
          this.itemSet.dimension_type = item.dimension_type ? item.dimension_type : false
          this.itemSet.dacct_radio = item.dacct_radio // 是否为top10
        } else {
          setTimeout(() => {
            this.textTitle = item.title
            this.$refs.editor.firstChild.innerHTML = item.content;
            this.htmlContent = item.content;
          }, 100);
        }
        this.$forceUpdate()
      }
    },
    changeText(val) {
      this.htmlContent = val.target.firstChild.innerHTML
    },
    handleOk() {
      // 确认
      const that = this
      if (this.imgSrcList == null) {
        that.$message.warning('请选择模块图片！')
      } else {
        if (this.itemSet.dimension_type) {
          if (!this.queryParam.accounting && this.itemSet.dimension_type.indexOf('b') >= 0) {
            that.$message.warning('请选择核算主体！')
            return
          }
          if (!this.queryParam.areaid && this.itemSet.dimension_type.indexOf('a') >= 0) {
            that.$message.warning('请选择所属地区！')
            return
          }
          if (!this.queryParam.guokuid && this.itemSet.dimension_type.indexOf('g') >= 0) {
            that.$message.warning('请选择所属国库！')
            return
          }
          if (!this.queryParam.subject_code && this.itemSet.dimension_type.indexOf('s') >= 0) {
            that.$message.warning('请选择科目类！')
            return
          }
          if (!this.queryParam.tsubject_code && this.itemSet.dimension_type.indexOf('ts') >= 0) {
            that.$message.warning('请选择T科目类！')
            return
          }
        }
        if (this.itemSet.time_type) {
          if (!this.queryParam.timeType) {
            that.$message.warning('请选择时间类型！')
            return
          } else {
            if (!this.queryParam.time_type) {
              that.$message.warning('请选择时间周期！')
              return
            }
            if (this.queryParam.timeType == 2) {
              //时间区域
              if (this.queryParam.time_type == 'd' || this.queryParam.time_type == 'm') {
                if (this.timeData == '') {
                  that.$message.warning('请选择时间！')
                  return
                }
              } else if (this.queryParam.time_type == 'q') {
                if (this.startquarter != '' && this.endquarter != '') {
                  if (parseInt(this.startquarter.replace('-Q', '0')) > parseInt(this.endquarter.replace('-Q', '0'))) {
                    that.$message.warning('您的开始时间大于开始时间请重新选择！')
                    return
                  }
                } else {
                  that.$message.warning('请选择时间！')
                  return
                }
              } else if (this.queryParam.time_type == 'y') {
                if (this.endYear == '' || this.startYear == '') {
                  that.$message.warning('请选择时间！')
                  return
                } else {
                  if (parseInt(this.startYear.replace('-Q', '0')) > parseInt(this.endYear.replace('-Q', '0'))) {
                    that.$message.warning('您的开始时间大于开始时间请重新选择！')
                    return
                  }
                }
              }
            } else if (this.queryParam.timeType == 1) {
              // 至今
              if (this.startTimeData == '' || this.startTimeData == undefined) {
                that.$message.warning('请选择时间！')
                return
              }
            }
          }
        }
        let dataItem = {
          gallery_id: null, // 图片id
          title: null, // 选择模块标题
          type: null, // 图库类型
          content: null, // 选择模块的缩略图
          option: null, // 图表option
          query_path: null,
          unit: null,
          time_interval: null, // 时间区间类型 (0:至今;1时间区间)
          time_type: null, // 时间类型 d日 m月 j季 y年
          pageWhere: [], // 对象(每一个图库的查询条件信息)
        }
        dataItem.gallery_id = this.currentImg
        // console.log('this.textTitle'+this.textTitle)
        dataItem.title = this.textTitle!='' ? this.textTitle : this.imgSrcList.title
        dataItem.content = this.htmlContent!='' ? this.htmlContent :this.imgSrcList.content
        dataItem.type = this.imgSrcList.type
        dataItem.option = this.imgSrcList.option
        dataItem.query_path = this.imgSrcList.query_path || this.imgSrcList.interface
        dataItem.unit = this.queryParam.unit
        dataItem.time_type = this.queryParam.time_type
        dataItem.time_interval = this.queryParam.timeType
        if (this.queryParam.accounting) {
          // 核算主体不为空
          let searchData = this.setpageWhere('b', 'bookorg_id', this.queryParam.accounting)
          dataItem.pageWhere.push(searchData)
        }
        if (this.queryParam.guokuid) {
          // 国库不为空
          let searchData = this.setpageWhere('g', 'guoku_id', this.queryParam.guokuid)
          dataItem.pageWhere.push(searchData)
        }
        if (this.queryParam.areaid) {
          // 地区不为空
          let searchData = this.setpageWhere('a', 'area_code', this.queryParam.areaid)
          dataItem.pageWhere.push(searchData)
        }
        if (this.queryParam.subject_code && this.itemSet.dimension_type.indexOf('s') >= 0) {
          // 科目不为空
          let searchData = this.setpageWhere('s', 'subject_code', this.queryParam.subject_code)
          dataItem.pageWhere.push(searchData)
        }
        if (this.queryParam.tsubject_code && this.itemSet.dimension_type.indexOf('ts') >= 0) {
          // t科目不为空
          let searchData = this.setpageWhere('ts', 'subject_code', this.queryParam.tsubject_code)
          dataItem.pageWhere.push(searchData)
        }

        if (this.queryParam.time_type) {
          let time_value = '' // 时间的where_value
          if (this.queryParam.timeType == 1 || this.queryParam.timeType == 4) {
            // 表示至今
            time_value = this.startTimeData != undefined ? this.startTimeData : ''
          } else if (this.queryParam.timeType == 2) {
            // 表示时间区域
            if (this.queryParam.time_type == 'd' || this.queryParam.time_type == 'm') {
              // 类型为日、月
              time_value = this.timeData[0] + ',' + this.timeData[1]
            } else if (this.queryParam.time_type == 'q') {
              // 类型为季
              time_value = this.startquarter + ',' + this.endquarter
            } else {
              // 类型为年
              time_value = this.startYear + ',' + this.endYear
            }
          }
          let timeData = this.setpageWhere('t', 'dacct', time_value)
          dataItem.pageWhere.push(timeData)
        }
        // console.log(dataItem);
        this.$emit('ok', dataItem)
        const time = setTimeout(() => {
          this.clearSetting()
        }, 500)
      }
    },
    setpageWhere(type, key, data) {
      // 设置pageWhere 查询条件数据
      let dataItem = {
        where_type: type,
        where_key: key,
        where_value: data,
      }
      return dataItem
    },
    clearSetting() {
      // 清空设置
      this.currentImg = false
      this.imgSrcList = null
      this.timeData = undefined
      this.startTimeData = undefined
      this.textTitle = ''
      this.htmlContent = ''
      this.itemSet = {}
      this.queryParam = {}
      // this.searchParam = {}
    },
    cycleChange(value) {
      // 时间周期select赋值
      this.queryParam.time_type = value
      this.$forceUpdate()
    },
    unitChange(value) {
      this.queryParam.unit = value
      this.$forceUpdate()
    },
    timeChange(value) {
      // 选择时间select赋值
      this.queryParam.timeType = value.target.value
      this.$forceUpdate()
    },
    accountChange(value) {
      // 核算主体树赋值
      this.queryParam.accounting = value
      this.$forceUpdate()
    },
    guokuChange(value) {
      // 国库树赋值
      this.queryParam.guokuid = value
      this.$forceUpdate()
    },
    kemuChange(value) {
      // 科目树赋值
      this.queryParam.subject_code = value
      this.$forceUpdate()
    },
    subjectChange(value){// T科目树赋值
      this.queryParam.tsubject_code = value
      this.$forceUpdate()
    },
    areaChange(value) {
      // 地区树赋值
      this.queryParam.areaid = value
      this.$forceUpdate()
    },
    getStartQuarterNow(msg) {
      // 至今开始季数据回调
      this.startTimeData = msg
    },
    startquarter1(msg) {
      // 时间区域开始季数据回调
      this.startquarter = msg
    },
    endquarter1(msg) {
      // 时间区域结束季数据回调
      this.endquarter = msg
    },
    getStartYearValue(msg) {
      // 时间区域开始年数据回调
      this.startYear = msg
    },
    getEndYearValue(msg) {
      // 时间区域结束年数据回调
      this.endYear = msg
    },
    getGalleryList() {
      // 图库接口
      let data = this.searchParam
      data.state = 0
      this.confirmLoading = true
      getGalleryList(data).then((res) => {
        this.confirmLoading = false
        if (res.result === 'success') {
          
          this.dataSource1 = res.rows;
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    searchQuery() {
      // 查询图库
      this.getGalleryList()
    },
    searchReset() {
      this.searchParam = {}
      this.getGalleryList()
    },
    loadTree() {
      // 地区树、国库树、核算树请求
      this.getGalleryList()
      var that = this
      let data = {}
      //国库
      getGuokuTree(data).then((res) => {
        if (res.result === 'success') {
          that.guokuTreeData = []
          that.guokuTreeData = res.rows
        }
      })
      //地区
      getAreaTree(data).then((res) => {
        if (res.result === 'success') {
          that.areaTreeData = []
          that.areaTreeData = res.rows
        }
      })
      //核算主体
      getOrgTree(data).then((res) => {
        if (res.result === 'success') {
          that.bookorgTreeData = []
          that.bookorgTreeData = res.rows
        }
      })
      //科目
      getKeMuTreeName(data).then((res) => {
        if (res.result === 'success') {
          that.kemuTreeData = []
          that.kemuTreeData = res.rows
        }
      })
      //T科目
      getSubjectTree(data).then((res) => {
        if (res.result === 'success') {
          that.subjectTreeData = []
          that.subjectTreeData = res.rows
        }
      })
      getBusinessTypeList().then((res) => {
        if (res.result === 'success') {
          that.bussTypeData = res.rows
        }
      })
    },
    setCurrentData(value) { // 模板数据回显
      if (value.gallery_id) {
        this.currentImg = value.gallery_id
        this.dataSource1.forEach((item) => {
          if (item.id == value.gallery_id) {
            this.imgSrcList = item
            this.itemSet.time_type = item.time_type // 时间类型
            this.itemSet.dacct_radio = item.dacct_radio // 是否为top10
            this.itemSet.dimension_type = item.dimension_type // 有哪些选择条件
          }
        })
        if(this.imgSrcList.type !='h'){
          // console.log(value.pageWhere)
          this.timeData = undefined
          this.startTimeData = undefined
          this.queryParam = {}
          this.queryParam.unit = value.unit
          this.queryParam.timeType = value.time_interval
          this.queryParam.time_type = value.time_type
          if (value.pageWhere) {
            let pageWhereData = value.pageWhere
            pageWhereData.forEach((item) => {
              if (item.where_type == 'b') {
                // 核算主体
                this.queryParam.accounting = item.where_value
              } else if (item.where_type == 'g') {
                // 国库
                this.queryParam.guokuid = item.where_value
              } else if (item.where_type == 'a') {
                // 地区
                this.queryParam.areaid = item.where_value
              } else if (item.where_type == 's') {
                // 科目
                this.queryParam.subject_code = item.where_value
              } else if (item.where_type == 'ts') {
                // t科目
                this.queryParam.tsubject_code = item.where_value
              } else if (item.where_type == 't') {
                // 时间
                let timeList = item.where_value.split(',')
                if (timeList.length > 1) {
                  // console.log(item.where_value)
                  this.startquarter = this.queryParam.time_type == 'q' ? timeList[0] : ''
                  this.endquarter = this.queryParam.time_type == 'q' ? timeList[1] : ''
                  this.startYear = this.queryParam.time_type == 'y' ? timeList[0] : ''
                  this.endYear = this.queryParam.time_type == 'y' ? timeList[1] : ''
                  this.timeData = this.queryParam.time_type == 'd' || this.queryParam.time_type == 'm' ? timeList : ''
                } else {
                  this.startTimeData = timeList[0]
                }
              }
            })
          }
        } else {
          setTimeout(() => {
            this.$refs.editor.firstChild.innerHTML = value.content;
            this.textTitle = value.title;
            this.htmlContent = value.content;
          }, 100);
        }
        
      }
    }
  },
}
</script>

<style scoped>
.color {
  background-color: #ff0000;
}
.circle {
  position: relative;
  width: 100%;
  left: 15px;
}
.circle-ul {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  width: 100%;
  height: 52vh;
  overflow-y: scroll;
  padding: 0;
}
.circle-li {
  /* margin-right: 20px; */
  margin: 0 0.5%;
  padding: 0.5%;
  border: 1px solid #ccc;
  list-style: none;
  margin-bottom: 20px;
  width: 32%;
  height: 17vw;
}
.circle-li .circle-img {
  height: 100%;
  position: relative;
}
.circle-img p {
  position: absolute;
  width: 100%;
  bottom: 0;
  text-align: center;
  background: #fff;
  margin-bottom: 0;
}
.circle-li img {
  width: 100%;
  height: 100%;
}
.topic-shade {
  display: none;
}
.topic-shade {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  position: absolute;
  right: 15px;
  bottom: 3px;
}
.sofarButton,
.sofarButton2 {
  cursor: pointer;
  color: #f5222d;
}

.chartSetClass {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-self: start;
  align-items: center;
}
.chartSetClass h2 {
  margin: 0;
  padding-right: 10px;
}
.m1 {
  margin-top: 20px;
}
.chartSetClass .ant-select {
  width: 20%;
}
.required {
  position: relative;
  font-size: 16px;
}
.required::before {
  display: inline-block;
  margin-right: 4px;
  color: #f5222d;
  font-size: 14px;
  font-family: SimSun, sans-serif;
  line-height: 1;
  content: '*';
}
.htmlText {
  width: 60%;
  height: auto;
  min-height: 35vh;
  max-height: 45wh;
  text-align: justify;
  overflow-y: auto;
  outline: none;
  /* margin: 0 auto; */
  padding: 4px 11px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  -webkit-user-select: text;
}
.htmlText:hover {
  border-color: #ff4d4f;
  border-right-width: 1px !important;
}
</style>
