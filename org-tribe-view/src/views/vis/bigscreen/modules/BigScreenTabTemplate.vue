<template>
  <div
    class="bigScreenView"
    :style="{
      background: layoutData.background_type == 0 ? '#eee' : layoutData.colour,
      backgroundImage: layoutData.background_type == 0 ? 'url(' + layoutData.content + ')' : layoutData.colour,
    }"
  >
    <grid-layout
      :layout.sync="layout"
      :col-num="12"
      :row-height="rowHeight"
      :is-draggable="true"
      :is-resizable="true"
      :is-mirrored="false"
      :auto-size="true"
      :vertical-compact="true"
      :use-css-transforms="true"
      ref="imageWrapper"
    >
      <grid-item
        v-for="item in layout"
        :key="item.i"
        :static="true"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        :is-draggable="true"
        :is-resizable="true"
      >
        <div
          v-if="item.i == 0"
          :class="layoutData.title_background == 0 ? 'titleImgClass' : 'titleClass'"
          onselectstart="return false;"
        >
          {{ layoutData.name }}
        </div>
        <div v-else class="echartCardClass">
          <div v-if="item.title" class="echartTitle">
            <p class="title">{{ item.title != 'null' ? item.title : '' }}</p>
            <p class="date" v-if="item.dateInfo">
              <!-- <img src="@/assets/dateIcon.png" /> -->
              {{item.dateInfo ? item.dateInfo : ''}}
            </p>
            <p class="date" v-if="item.type == 't'&& item.tableTime">
              <!-- <img src="@/assets/dateIcon.png" /> -->
              {{item.tableTime}}
            </p>
          </div>
          <div class="echartClass">
            <div v-if="item.type == 'h'" v-html="item.content" class="htmlText"></div>
            <div v-if="item.type == 'p'" class="imageBox">
              <img
                v-if="item.imageSrc"
                :src="item.imageSrc"
                class="imageClass"
                @load="handleImageComplete(item, 'load')"
                @error="handleImageComplete(item, 'error')"
              />
              <div v-else class="noDataClass">暂无图片</div>
            </div>
            <div
              v-if="item.option == 'china' || (typeof(item.option)== 'string' && item.option.indexOf('CQ') !=-1)"
              class="geoBox"
            >
            <!-- <div
              v-if="filtersType(item.option,0) == 'map'"
              class="geoBox"
            > -->
              <!-- <div class="timePicker">
                <a-date-picker
                  placeholder="请选择"
                  v-model="timeData"
                  dropdownClassName="selfPicker"
                  @change="loadStatisticalAnalysis"
                ></a-date-picker>
              </div> -->
              <BSGeo
                :mapType="item.option"
                :mapUrl="item.url"
                :options="item.optionData"
                :bubbles="item.bubbles"
                :titleArea="item.titleArea"
                :unitNum="item.unit"
                :maxNum="item.maxNum"
              ></BSGeo>
            </div>
            <BSTable
              v-if="item.type == 't'"
              :smallHeight="item.h"
              :itemData="item"
              v-bind="$props"
              @showDataTime="getDataTime"
            ></BSTable>
            <BScharts
              :options="item.options"
              :theme="item.theme"
              v-if="item.title != 'null' && item.type == 'b' && item.nodata != true"
              ref="bschart"
            ></BScharts>
            <div
              class="noDataClass"
              v-if="
                item.nodata == true &&
                item.type == 'b'
              "
            >
              暂无数据
            </div>
          </div>
        </div>
      </grid-item>
    </grid-layout>
  </div>
</template>

<script>
import moment from 'moment'
import { GridLayout, GridItem } from 'vue-grid-layout'
import { postAction } from '@/api/manage'
import { visPreviewDebug } from '@/utils/visPreviewDebug'
import { normalizeVisRequestUrl as buildVisRequestUrl } from '@/utils/visRequest'
import BScharts from './BScharts.vue'
import BSGeo from './geoCharts.vue' // 地图echart
import BSTable from './BSTable.vue'
export default {
  name: 'BigScreenTabTemplate',
  components: {
    GridLayout,
    GridItem,
    BScharts,
    BSTable,
    BSGeo,
  },
  props: {
    layoutData: {
      type: Object,
      default: {},
    },
    templateName: {
      type: Number,
      default: 0,
    },
    screenHeight: {
      type: Number,
      default: window.innerHeight,
    },
    fatherMethod: {
      type: Function,
      default: null,
    },
  },
  data() {
    let date = new Date(),
      dates = new Date(new Date().setDate(new Date().getDate() - 2)),
      D_ACCT = this.moment(dates.toLocaleDateString(), 'YYYY-MM-DD')
    return {
      // screenHeight: window.innerHeight,
      rowNum: 0, // h的总和
      rowHeight: 0,
      draggable: true,
      resizable: true,
      width: '100%',
      layout: [],
      scale: 1,
      timeData: D_ACCT,
      tableTime: null
    }
  },
  computed: {
  },
  watch: {
    screenHeight(val) {
      this.screenHeight = val
      this.rowHeight = parseInt(this.screenHeight / this.rowNum) - 9.9
    },
  },
  mounted() {
    this.getData()
  },
  created() {},
  methods: {
    moment,
    // filtersType (value,i) {
    //   let that = this;
    //   if (!value) return ''
    //   value = value.split(",")
    //   return value[i]
    // },
    getData() {
      let dataList = this.layoutData.page_sub
      visPreviewDebug('page getData', {
        pageId: this.layoutData && this.layoutData.id,
        pageName: this.layoutData && this.layoutData.name,
        pageSubLength: dataList && dataList.length,
        pageSub: dataList
      })
      dataList.forEach((item) => {
        // console.log(item)
        item.x = parseInt(item.x)
        item.y = parseInt(item.y)
        item.w = parseInt(item.w)
        item.h = parseInt(item.h)
        item.i = parseInt(item.i)
        item.sort = parseInt(item.sort)
        if (item.type == 'p') {
          this.prepareImageItem(item)
        } else if (item.type != 't' && item.type!= 'h') {
          this.getList(item);
        } else {
          visPreviewDebug('skip child request in tab template', {
            id: item.id,
            title: item.title,
            type: item.type,
            query_path: item.query_path,
            page_id: item.page_id
          })
        }
      })
      this.layout = dataList
      this.rowNum = this.getRowSum()
      this.rowHeight = parseInt(this.screenHeight / this.rowNum) - 9.9
    },
    getRowSum() {
      // 获取总h动态控制框体高度
      let maxY = Math.max.apply(
        Math,
        this.layout.map((item, index) => {
          return item.y
        })
      )
      let maxYList = this.layout.filter((item) => item.y == maxY) // 获取y坐标对应的数组
      let maxH = Math.max.apply(
        Math,
        maxYList.map((item, index) => {
          return item.h
        })
      )
      let sum = maxY + maxH
      return sum
    },
    filteredUnit(val) {
      // 换算单位
      let number
      if (val == '1') {
        number = '元'
      } else if (val == '10000') {
        number = '万元'
      } else {
        number = '亿元'
      }
      return number
    },
    loadStatisticalAnalysis() {
      // console.log(this.timeData)
    },
    getDataTime(data) {
      this.layout.forEach(item=>{
        if(item.id==data.id){
          item.tableTime = data.datatime
        }
      })
      this.$forceUpdate();
      // console.log(layout)
    },
    setTime(vale) {
      return vale
    },
    normalizeVisRequestUrl(url) {
      return buildVisRequestUrl(url)
    },
    normalizeVisImageUrl(url) {
      if (typeof url !== 'string' || url == '' || url == 'null') return ''
      if (/^https?:\/\//.test(url) || url.indexOf('data:image/') === 0) return url
      let webappsIndex = url.indexOf('/webapps/')
      if (webappsIndex >= 0) return url.substring(webappsIndex)
      return url
    },
    getImageSource(item) {
      return this.normalizeVisImageUrl(item.content) || this.normalizeVisImageUrl(item.option) || this.normalizeVisImageUrl(item.query_path)
    },
    prepareImageItem(item) {
      item.imageSrc = this.getImageSource(item)
      visPreviewDebug('image item prepared', {
        id: item.id,
        title: item.title,
        content: item.content,
        option: item.option,
        query_path: item.query_path,
        imageSrc: item.imageSrc
      })
      if (!item.imageSrc) {
        this.handleImageComplete(item, 'empty')
      }
    },
    handleImageComplete(item, status) {
      if (item.imageCompleted) return
      item.imageCompleted = true
      visPreviewDebug('image request complete', {
        id: item.id,
        title: item.title,
        status: status,
        imageSrc: item.imageSrc,
        query_path: item.query_path
      })
      this.fatherMethod()
    },
    getList(item) {
      // 单个请求接口
      let that = this
      let completed = false
      const markComplete = () => {
        if (!completed) {
          completed = true
          visPreviewDebug('chart request complete', {
            id: item.id,
            title: item.title,
            type: item.type,
            query_path: item.query_path
          })
          this.fatherMethod()
        }
      }
      let moneyType = that.filteredUnit(item.unit) // 金额类型
      if (item.query_path && item.page_id != null && item.query_path != null && item.query_path != 'null') {
        let url = this.normalizeVisRequestUrl(item.query_path)
        let param = {
          id: item.id,
          // page_id: item.page_id,
        }
        visPreviewDebug('chart request start', {
          id: item.id,
          title: item.title,
          type: item.type,
          query_path: url,
          param: param
        })
        postAction(url, param).then((res) => {
          visPreviewDebug('chart request response', {
            id: item.id,
            title: item.title,
            query_path: url,
            result: res && res.result,
            type: res && res.type,
            nodata: res && res.nodata,
            res: res
          })
          let that = this
          let options // 最终负责options数据
          let optionData // item.option数据
          if (res.result == 'success') {
            markComplete()
            item.nodata = res.nodata
            if(res.dateInfo) item.dateInfo = res.dateInfo.dateDesc
            if (item.nodata == true) {
              this.$forceUpdate();
              return
            } 
            if (item.option != null) {
              optionData = res.type!='map' ? eval('(' + item.option + ')') : item.option
            }
            if (res.type == 'two_pie') {
              if(res.inCome) { //预算收支总体执行
                optionData.tooltip.formatter =  '{c}'+ moneyType +'({d}%)'  
                optionData.series.forEach((item, index) => {
                  item.label.formatter = res.memo[index]
                })
                res.inCome.forEach((item,index)=>{
                  optionData.series[0].data.value = item.value
                })
                res.payOut.forEach((item,index)=>{
                  optionData.series[1].data.value = item.value
                })
                options = optionData
              }else {// 双子饼图
                let labelPosition = ''
                labelPosition = item.w < 6 ? 'inside' : 'outside'
                optionData.series.forEach((item, index) => {
                  optionData.tooltip.formatter =  '{c}'+ moneyType +'({d}%)'
                  item.label.position = labelPosition
                  if(item.w<6) {
                    optionData.title[0].text = res.本期标题 + '\n' + res.sum[0]
                    optionData.title[1].text = res.同期标题 + '\n' + res.sum[1]
                  } else {
                    optionData.title[0].text = res.本期标题 + '—' + res.sum[0]
                    optionData.title[1].text = res.同期标题 + '—' + res.sum[1]
                  }
                  // optionData.graphic[0].style.text = res.sum[0].replace('合计', '合计\n')
                  // optionData.graphic[1].style.text = res.sum[1].replace('合计', '合计\n')
                })
                options = optionData
                options.series[0].data = res.benqi
                options.series[1].data = res.tongqi
                options.legend.data = res.memo
              }
              item.options = options
            } else if (res.type == 'barAndLine') {
              // 柱状折线图
              if (item.option != null) {
                // optionData.yAxis[0].name = '金额（' + moneyType + '）'
                optionData.yAxis[0].name = '金额('+moneyType+')'
                optionData.legend.top = '0%'
                optionData.legend.width = '75%'
                optionData.xAxis[0].data = res.x
                if (res.amount || res.rate) {
                  // 存在数据
                  if (res.amount.length > 0 && Array.isArray(res.amount[0])) {
                    let length = res.amount.length
                    res.amount.forEach((item, index) => {
                      optionData.series[index].data = item
                    })
                    if (res.rate) {
                      res.rate.forEach((item, index) => {
                        optionData.series[length + index].data = item
                      })
                    }
                  } else {
                    optionData.series[0].data = res.amount
                    optionData.series[1].data = res.rate
                  }
                }
                item.options = optionData
              }
            } else if (res.type == 'bar') {
              // 柱状图
              if (optionData.yAxis.length > 1) {
                // 多柱图
                optionData.yAxis[0].name = that.filteredUnit(item.unit)
              } else {
                // 横向柱图
                optionData.yAxis.data = res.y
              }
              if (Array.isArray(optionData.xAxis)) {
                // 多柱图
                optionData.xAxis[0].data = res.x
              } else {
                optionData.xAxis.data = res.x
              }
              if(Array.isArray(optionData.yAxis)) {
                  optionData.yAxis[0].name = '金额（'+moneyType+'）'
                } else {
                  optionData.yAxis.name = '金额（'+moneyType+'）'
                }
              if (res.amount && res.amount.length > 0 && Array.isArray(res.amount[0])) {
                // 多柱图
                let length = res.amount.length
                res.amount.forEach((item, index) => {
                  optionData.series[index].data = item
                })
              } else if (res.data) {
                if(Array.isArray(optionData.yAxis) ) {
                  optionData.yAxis[0].name = '金额（'+moneyType+'）'
                } else {
                  optionData.yAxis.name = '金额（'+moneyType+'）'
                }
                optionData.series[0].data = res.data
                // console.log(res.data)
                if(url=='queryData/getOrgToGuoku' || url=='queryData/getGuokuToOrg') {
                  let data = res.data;
                  let yAxisData = []
                  data.forEach((item,index)=>{
                    yAxisData.push(index+item.name)
                  })
                  optionData.tooltip.formatter ='{b} : {c}' + moneyType
                  optionData.yAxis.data = yAxisData
                  optionData.yAxis.name = ''
                }
                if(optionData.xAxis.data) optionData.xAxis.data = res.x
              }
              item.options = optionData
            } else if (res.type == 'funnel') {
              // 漏斗图
              optionData.tooltip.formatter = '{a} <br/>{b} : {c}' + moneyType
              optionData.series[0].data = res.benqi
              item.options = optionData
            } else if (res.type == 'pie') {
              // 饼状图
              if (optionData.series.length >= 4) {// 直辖市预算收入
                let pieDataList
                if (res.data.length > 0) {
                  pieDataList = res.data
                  let legendData = []
                  pieDataList.forEach((item) => {
                    legendData.push(item.name + item.value + moneyType)
                  })
                  optionData.legend.top = '0%'
                  optionData.legend.itemGap = 3
                  optionData.legend.data = legendData
                  let total = pieDataList[0].value * 1.2
                  pieDataList.forEach((item, index) => {
                    optionData.series[index].name = legendData[index]
                    optionData.series[index].data[0].value = item.value
                    optionData.series[index].data[0].name = legendData[index]
                    optionData.series[index].data[1].value = total - item.value
                  })
                  optionData.series[0].radius = ['88%', '95%']
                  optionData.series[1].radius = ['78%', '85%']
                  optionData.series[2].radius = ['68%', '75%']
                  optionData.series[3].radius = ['58%', '65%']
                }
              } else {
                if(optionData.series.length > 1) {
                  if(res.inCome) { // 分类预算执行情况
                    optionData.series[0].label.formatter = res.memo
                    res.inCome.forEach((item,index)=>{
                      optionData.series[0].data.value = item.value
                    })
                  }else {
                    optionData.tooltip.formatter = '{b}: {c}' + moneyType + ' ({d}%)'
                    optionData.series[0].data = res.data
                    optionData.series[1].data = res.data1
                  }
                  
                } else {
                  if(optionData.series[0].data[0].selected) {
                    let pieData = res.data;
                    optionData.tooltip.formatter = '库存余额' + '{c}' + moneyType + '({d}%)'
                    // pieDataList.forEach((item) => {
                    //   legendData.push(item.name + item.value + moneyType)
                    // })
                    let serData = res.data.reverse();
                    serData.forEach(item=>{
                      item.name = item.name + '\n' + item.value + moneyType
                    })
                    optionData.series[0].data = serData;
                    // console.log(optionData.series[0].data)
                    optionData.series[0].data[0].selected = true;
                    optionData.series[0].data[0].fontSize = '20%';
                  }else {
                    if(url = "queryData/getSubjectPaySub") item.theme = 'light'
                    if(optionData.tooltip) optionData.tooltip.formatter = '{b}: {c}' + moneyType + ' ({d}%)'
                    if(optionData.series[0].label) optionData.series[0].label.formatter = '{b}: \n{c}' + moneyType + ' ({d}%)'
                    optionData.series[0].data = res.data;
                    if(optionData.legend) {
                      let legendItem = []
                      res.data.forEach(item=>{
                        legendItem.push(item.name)
                      })
                      optionData.legend.data = legendItem
                    }
                  }
                  
                }
                
              }
              item.options = optionData
            } else if (res.type == 'line') {
              // 线图表
              optionData.xAxis.data = res.x
              optionData.grid.bottom = '3%'
              if(Array.isArray(optionData.yAxis)) {
                optionData.yAxis[0].name = '金额（'+moneyType+'）'
              } else if(url != 'queryData/getTreasuryIndex'){
                optionData.yAxis.name = '金额（'+moneyType+'）'
              }
              // legend
              if(url == 'queryData/getTreasuryIndex') {
                optionData.series[0].data = res.dataFact
                optionData.series[1].data = res.dataFact2
              } else {
                if (res.data && res.data.length > 0 && Array.isArray(res.data[0])) {
                  res.data.forEach((item, index) => {
                    optionData.series[index].data = item
                  })
                } else {
                  res.amount.forEach((item, index) => {
                    optionData.series[index].data = item
                  })
                }
              }
              
              item.options = optionData
            } else if (res.type == 'map') {
              // 地图数据逻辑处理
              item.url = url
              if(res.titleArea) {
                item.titleArea = res.titleArea // 重庆分地区数据
              } else {
                item.titleArea = []
              }
              item.optionData = res.data
              item.bubbles = res.bubbles
              item.maxNum = parseInt(res.max)
            }
            this.$forceUpdate();
          } else {
            markComplete()
          }
        }).catch(() => {
          visPreviewDebug('chart request catch', {
            id: item.id,
            title: item.title,
            query_path: url
          })
          markComplete()
        })
      } else {
        visPreviewDebug('chart request skipped because condition false', {
          id: item.id,
          title: item.title,
          type: item.type,
          query_path: item.query_path,
          page_id: item.page_id
        })
      }
    },
  },
}
</script>

<style scoped>
.bigScreenView {
  background-size: 100% 100% !important;
  background-repeat: no-repeat !important;
}
.titleClass {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.6rem;
  font-weight: bold;
  color: #fff;
}
.titleImgClass {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('~@/assets/titleLogo.png');
  background-size: 100% 100%;
  font-size: 1.6rem;
  font-weight: bold;
  color: #fff;
}
.echartCardClass {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  border: 1px solid #ccc;
  border-image-source: url('~@/assets/border.png');
  border-image-slice: 30;
  border-image-width: 15px;
  border-image-repeat: stretch;
  border-image-outset: 0px;
}
.noDataClass {
  position: absolute;
  width: 100%;
  top: 45%;
  text-align: center;
  font-size: 1rem;
}
.echartCardClass .echartClass {
  width: 95%;
  /* height: 78%; */
  color: #fff;
  height: calc(100% - 4rem);
}
.echartCardClass .echartClass .geoBox {
  position: relative;
  width: 100%;
  height: 100%;
}
.echartCardClass .echartClass .imageBox {
  width: 100%;
  height: 100%;
}
.echartCardClass .echartClass .imageClass {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}
.echartCardClass .echartClass .geoBox .timePicker {
  position: absolute;
  z-index: 1;
  top: 0%;
  width: 50%;
}
.timePicker >>> .ant-calendar-picker-input {
  border: 1px solid #03c;
  color: #fff;
  background: transparent;
}
.timePicker >>> .ant-calendar-picker .anticon {
  color: #fff;
  background: transparent;
}
.timePicker >>> .ant-input:focus {
  border: 1px solid #fff;
}
.timePicker >>> .ant-calendar-picker:focus .ant-calendar-picker-input:not(.ant-input-disabled),
.timePicker >>> .ant-calendar-picker:hover .ant-calendar-picker-input:not(.ant-input-disabled) {
  border-color: #40a9ff !important;
}
.timePicker >>> .ant-calendar-picker:focus,
.timePicker >>> .ant-calendar-picker:hover {
  border-color: #40a9ff !important;
  border-right-width: 1px !important;
  outline: 0;
}

.echartCardClass .echartTitle {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  text-align: left;
  margin: 0.8rem 0;
  color: #fff;
  padding-left: 1.2rem;
}
.echartCardClass .echartTitle p {
  margin: 0;
  padding: 0;
}
.echartCardClass .echartTitle .title{
  width: 70%;
  font-size: 1.25rem;
}
.echartCardClass .echartTitle .date{
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  text-align: right;
  width: 40%;
  font-size: 0.8rem;
  margin-right: 1rem;
}
.echartCardClass .echartTitle .date img{
  width: .8rem;
  margin-right: .5rem;
  /* font-size: 1rem; */
}
.htmlText >>> p {
  font-size: 1.15rem !important;
  color: #ffffff !important;
}
</style>
<style>
.selfPicker a {
  color: #1890ff;
}
.selfPicker .ant-calendar-today .ant-calendar-date {
  color: #1890ff;
  font-weight: 700;
  border-color: #1890ff;
}
.selfPicker .ant-calendar-selected-day .ant-calendar-date {
  color: #fff;
  background: #1890ff !important;
}
.selfPicker .ant-calendar-date:hover {
  background: #e6f7ff;
  cursor: pointer;
}
</style>
