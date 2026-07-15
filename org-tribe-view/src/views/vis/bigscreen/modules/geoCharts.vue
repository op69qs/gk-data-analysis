<template>
  <div class="g-echarts" :style="{ width, height }"></div>
</template>

<script>
import 'echarts/theme/macarons' // echarts theme
import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import '@/utils/areaMap'
export default {
  name: 'geoCharts',
  props: {
    width: {
      // 宽度
      type: String,
      default: '100%',
    },
    height: {
      // 高度
      type: String,
      default: '100%',
    },
    mapUrl: {
      //请求的url用来判断是否是重庆支出分地区展示
      type: String,
    },
    mapType: {
      // china-中国地图、CQ1合并区重庆地图、CQ2分区地图
      type: String,
    },
    colorClass: {
      // 地图颜色
      type: String,
    },
    options: {
      // 金额数据
      type: Array,
    },
    bubbles: {
      // tooltip
      type: Array,
    },
    unitNum: {
      // 单位
      type: String,
    },
    maxNum: {
      // 最大数
      type: Number,
    },
    titleArea: {
      // 两江、高新、重庆数据
      type: Array,
    },
    theme: [Object, String],
  },
  data() {
    return {
      chart: null,
      isType: 'piecewise', // continuous 连续型,piecewise 类型为分段型
      isMap: 'CQ1',
      muchMapColor: ['#58AAEF', '#66BC91', '#FBE268', '#F1A16D'], // 重庆分地区地图、全国地图统一色号
      threeColor: [
        ['#75B8F0', '#0E87ED'], // CQ1
        ['#3CB371', '#20B2AA', '#4169E1'], // CQ3
        ['#8A2BE2', '#191970'], // CQ7的颜色
      ],
      colorList: [
        ['#75B8F0', '#0E87ED'], // 全国地图china、重庆市预算收入分地区展示CQ2、重庆经济区域公共预算收入展示CQ1
        ['#c9fffb', '#88fbf2', '#06f7e4'], // 重庆经济区域税收收入展示CQ3-》CQ1
        ['#8AA3F3', '#4C72ED'], // 重庆市支出分地区展示地图颜色CQ4-》CQ2
        ['#FBE068', '#E09045'], // 重庆市库款分地区展示地图颜色CQ5-》CQ2
        ['#8A2BE2', '#191970'], // CQ7的颜色
      ],
      borderStyle: '#E9B458', // 重庆市库款分地区展示地图颜色CQ5的边框色
      mapColor1: ['#75B8F0', '#0E87ED'],
      mapColor2: ['#92F7EF', '#68F3E8', '#51EEE2'],
      mapColor2: ['#8AA3F3', '#4C72ED'],
      eChartsOption: null, // 地图option
      eChartsOption1: {
        // 重庆分地区展示地图option
        backgroundColor: '#00a0e900',
        tooltip: {
          trigger: 'item',
        },
        visualMap: {
          type: 'continuous',
          orient: 'horizontal',
          realtime: false,
          calculable: false, // true
          show: true,
          left: 0,
          bottom: 0,
          min: 0,
          // max: 10,
          // text: ['亿元'],
          // itemHeight: 100,
          inRange: {
            color:  ['#58AAEF', '#66BC91', '#FBE268', '#F1A16D'],
          },
          textStyle: {
            color: '#fff',
          },
        },
        geo: {
          map: 'CQ2',
          show: false,
          roam: true,
          label: {
            normal: {
              show: false,
            },
            emphasis: {
              show: false,
            },
          },
          itemStyle: {
            normal: {
              areaColor: '#272235',
              borderColor: '#0FEFFC',
              fontWeightL: 700,
              borderWidth: 1,
            },
          },
        },
        series: [
          {
            type: 'map',
            z: 1,
            zoom: 1.2,
            left: '20%',
            top: '8%',
            aspectScale: 1.0,
            itemStyle: {
              normal: {
                areaColor: '#272235',
                borderColor: '#0FEFFC',
                fontWeightL: 700,
                borderWidth: 1,
              },
            },
            mapType: 'CQ2', // 自定义扩展图表类型
            label: {
              // formatter: '{b}',
              formatter: function (value) {
                let name = ''
                if (
                  value.name != '九龙坡区' &&
                  value.name != '沙坪坝区' &&
                  value.name != '南岸区' &&
                  value.name != '江北区' &&
                  value.name != '大渡口区' &&
                  value.name != '渝中区'
                ) {
                  name = value.name
                }
                return name
              },
              show: true,
              color: '#fff',
              lineHeight: 16,
            },
            // 自定义名称映射
            nameMap: {
              石柱土家族自治县: '石柱县',
              彭水苗族土家族自治县: '彭水县',
              酉阳土家族苗族自治县: '酉阳县',
              秀山土家族苗族自治县: '秀山县',
              开县: '开州区',
              武隆县: '武隆区',
              垫江区: '垫江区',
              潼南县: '潼南区',
              璧山县: '璧山区',
              铜梁县: '铜梁区',
              大足县: '大足区',
              荣昌县: '荣昌区',
              綦江县: '綦江区',
              梁平县: '梁平区',
            },
            data: [],
          },
          {
            type: 'map',
            z: 2,
            zoom: 0.35,
            left: '60%',
            top: '40%',
            aspectScale: 1.0,
            aspectScale: 1,
            itemStyle: {
              normal: {
                areaColor: '#272235',
                borderColor: '#0FEFFC',
                fontWeightL: 700,
                borderWidth: 1,
              },
            },
            mapType: 'CQpart1', // 自定义扩展图表类型
            label: {
              formatter: '{b}',
              show: true,
              color: '#fff',
              lineHeight: 16,
            },
            // label: {
            //   show: true,
            //   formatter: function (params) {
            //     if (params.value < 0) return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
            //     else return params.name + '\n' + params.value.toFixed(2)
            //   },
            //   rich: {
            //     a: {
            //       color: 'red',
            //       fontFamily: 'Microsoft YaHei',
            //       fontSize: 12,
            //       fontWeight: 500,
            //     },
            //   },
            //   fontSize: 12,
            //   fontWeight: 500,
            // },
            data: [],
          },
          {
            name: 'effect',
            type: 'effectScatter',
            coordinateSystem: 'geo',
            symbolSize: 30,
            showEffectOn: 'render',
            hoverAnimation: true,
            tooltip:{
              formatter:'{b}'
            },
            z: 3,
            rippleEffect: {
              //涟漪特效
              scale: 2,
              period: 4,
              brushType: 'fill', //波纹绘制方式 stroke, fill
            },
            label: {
              normal: {
                show: true,
                position: 'inside', //显示位置
                formatter: function (params) {
                  //圆环显示文字
                  return params.data.name
                },
                fontSize: 13,
                color: '#FFFFFF',
              },
              emphasis: {
                show: true,
              },
            },
            itemStyle: {
              normal: {
                show: true,
                color: '#F1A16D',
              },
            },
            data: [],
          },
        ],
      },
      eChartsOption2: {
        // 全国地图、迁徙地图、合并区地图
        backgroundColor: '#00a0e900',
        tooltip: {
          trigger: 'item',
        },
        visualMap: {
          type: 'piecewise',
          orient: 'horizontal',
          realtime: false,
          calculable: false,
          show: true,
          left: 0,
          bottom: 0,
          min: 0,
          inRange: {
            color: ['#75B8F0', '#0E87ED'],
          },
          textStyle: {
            color: '#fff',
          },
        },
        geo: {
          map: 'CQ2',
          show: false,
          zoom: 1.2,
          aspectScale: 1.0,
          label: {
            emphasis: {
              show: false,
            },
          },
          roam: false, //是否允许缩放
          itemStyle: {
            normal: {
              borderColor: '#6898AE',
              borderWidth: 1,
              color: '#1B3C5B', //地图背景色
            },
            emphasis: {
              color: 'rgba(37, 43, 61, .5)', //悬浮背景
            },
          },
        },
        series: [
          {
            type: 'map',
            zoom: 1.2,
            aspectScale: 0.75,
            z: 1,
            zoom: 1.1,
            itemStyle: {
              normal: {
                areaColor: '#272235',
                borderColor: '#0FEFFC',
                fontWeightL: 700,
                borderWidth: 1,
              },
            },
            mapType: 'CQ1', // 自定义扩展图表类型
            label: {
              formatter: '{b}',
              // formatter: function (value) {
              //     let name = ''
              //     if(value.name !='巫山县') {
              //       name = value.name
              //     }
              //     return name;
              // },
              show: true,
              color: '#fff',
              lineHeight: 16,
            },
            // 自定义名称映射
            nameMap: {
              石柱土家族自治县: '石柱县',
              彭水苗族土家族自治县: '彭水县',
              酉阳土家族苗族自治县: '酉阳县',
              秀山土家族苗族自治县: '秀山县',
              开县: '开州区',
              武隆县: '武隆区',
              垫江区: '垫江区',
              潼南县: '潼南区',
              璧山县: '璧山区',
              铜梁县: '铜梁区',
              大足县: '大足区',
              荣昌县: '荣昌区',
              綦江县: '綦江区',
              梁平县: '梁平区',
            },
            data: [],
          },
        ],
      },
    }
  },
  created() {
    this.chart = null
  },
  mounted() {
    this.$nextTick(() => {
      this.init()
    })
  },
  beforeDestroy() {
    this.clean()
  },
  computed: {
    filteredUnit: function () {
      // 换算单位
      let number
      if (this.unitNum == '1') {
        number = '元'
      } else if (this.unitNum == '10000') {
        number = '万元'
      } else {
        number = '亿元'
      }
      return number
    },
  },
  watch: {
    options: {
      deep: true,
      immediate: true,
      handler(v) {
        if (!v) return
        this.init()
        // this.chart.setOption(v, true)
      },
    },
  },
  methods: {
    init() {
      if (this.options && this.options.length > 0) {
        this.setData() // 设置options
        setTimeout(() => {
          this.chart = this.$echarts.init(this.$el, this.theme)
          this.chart.clear
          this.chart.resize()
          this.chart.setOption(this.eChartsOption)
          window.addEventListener('resize', this.chart.resize)
        }, 100)
      }
      if (this.mapType == 'CQ6') {
        // 当为重庆迁徙图时候option重新设置
        this.eChartsOption = this.eChartsOption2
        this.eChartsOption.tooltip.show = false
        this.setCQ6()
        setTimeout(() => {
          this.chart = this.$echarts.init(this.$el, this.theme)
          this.chart.clear
          this.chart.resize()
          this.chart.setOption(this.eChartsOption)
          window.addEventListener('resize', this.chart.resize)
        }, 100)
        return
      }
    },
    setData() {
      // echart数据处理
      if (this.mapType == 'CQ2' || this.mapType == 'CQ4' || this.mapType == 'CQ5') {
        // 当为重庆分地区时候option切换为echartOption2
        this.eChartsOption = this.eChartsOption1
        if (this.options && this.options.length > 0) {
          let data = this.options
          let series2 = []
          data.forEach((value) => {
            if (
              value.name == '九龙坡区' ||
              value.name == '沙坪坝区' ||
              value.name == '南岸区' ||
              value.name == '江北区' ||
              value.name == '大渡口区' ||
              value.name == '渝中区'
            ) {
              series2.push(value)
            }
          })
          // let pointData = [[112.2000005, 31.59462861],[112.2000005, 30.99462861],[112.2000005, 30.39462861]]
          let effData = [
            {
              name: '市级',
              value: [112.2000005, 31.59462861],
            },
            {
              name: '两江新区',
              value: [112.2000005, 30.99462861],
            },
            {
              name: '高新区',
              value: [112.2000005, 30.39462861],
            },
          ]
          this.eChartsOption.series[1].data = series2
          this.eChartsOption.series[2].data = effData
        }
        if (this.titleArea && this.titleArea.length > 0) {
          this.eChartsOption.series[2].data.forEach((itemData) => {
            let titleAreaData = this.titleArea,
            data = titleAreaData.filter((item) => item.areaDscr == itemData.name)
            if (this.mapUrl == 'queryMapData/getEconomicPay') { // 支出分地区
              itemData.value.push(itemData.titleAreaPayOut)
            } else if (this.mapUrl == 'queryMapData/getBudgetRevenue') { // 预算收入分地区
              itemData.value.push(itemData.titleAreaYS)
            } else { // 库款分地区
              itemData.value.push(itemData.titleAreaStock)
            }
          })
          this.eChartsOption.series[2].tooltip.formatter = (mapInfo) => {
            let bubbles = this.titleArea,
            data = bubbles.filter((item) => item.areaDscr == mapInfo.name),
            str = ''
            if (this.mapUrl == 'queryMapData/getEconomicPay') {
              str = ''
              str = data[0].areaDscr + `<br/>支出金额：${data[0].titleAreaPayOut} ${this.filteredUnit}`+
              `<br/>支出同比增速：${data[0].titleAreaPayOutTB}%`
            } else if (this.mapUrl == 'queryMapData/getBudgetRevenue') {
              str = data[0].areaDscr + `<br/>一般公共预算收入金额：${data[0].titleAreaYS} ${this.filteredUnit}`+
              `<br/>一般公共预算收入同比增速：${data[0].titleAreaYSTB}%` + 
              `<br/>税收收入金额：${data[0].titleAreaSS} ${this.filteredUnit}` + 
              `<br/>税收收入同比增速：${data[0].titleAreaSSTB}%`
            } else {
              str = data[0].areaDscr + `<br/>库款金额：${data[0].titleAreaStock} ${this.filteredUnit}`+
              `<br/>库款同比增速：${data[0].titleAreaStockTB}%`
            }
            return str
          }
        }
        this.eChartsOption.visualMap.type = this.isType
      } else {
        this.eChartsOption = this.eChartsOption2
        this.eChartsOption.visualMap.type = this.isType
      }
      this.eChartsOption.series[0].data = this.options // 地图数据赋值
      if (this.maxNum) {
        // 最大间隔
        this.eChartsOption.visualMap.max = this.maxNum * 1.05
      } else {
        this.eChartsOption.visualMap.max = 12
      }

      if (this.mapType == 'CQ3' || this.mapType == 'CQ7') {
        // 地图类型为合并区
        this.eChartsOption.series[0].mapType = 'CQ1'
      }
      if (this.mapType == 'china' || this.mapType == 'CQ2' || this.mapType == 'CQ1') {
        // 全国地图china与CQ2、CQ1的样式
        this.eChartsOption.series[0].mapType = this.mapType
        this.eChartsOption.visualMap.inRange.color = this.colorList[0]
      }
      if (this.eChartsOption.series[0].mapType != 'CQ1') {
        this.eChartsOption.visualMap.inRange.color = this.muchMapColor
      }
      if (this.eChartsOption.visualMap.type == 'piecewise') {
        if (this.eChartsOption.series[0].mapType == 'CQ1') {
          let picesValue = []
          let colorList = ['#825BC6', '#655CDD', '#5AC8C7']
          let picesData = this.options
          
          picesData.forEach((item, index) => {
            let itemValue = {
              value: item.value,
              label: item.name,
              color: colorList[index],
            }
            picesValue.push(itemValue)
          })
          // console.log(picesValue)
          // var pieces= [
          //   {value: 1.5, label: '渝东北三峡库区城镇群',color:'#5AC8C7'},
          //   {value: 7.19, label: '渝东南武陵山区城镇群',color:'#655CDD'},
          //   {value: 1.99, label: '主城都市区', color: '#825BC6'}
          // ]
          this.eChartsOption.visualMap.pieces = picesValue
          // console.log(this.eChartsOption.visualMap)
        } else {
          // 不连续显示
          this.eChartsOption.visualMap.calculable = false
          let max = 0
          if (this.maxNum) {
            max = this.maxNum * 1.07
          } else {
            max = 12 * 1.07
          }
          let colorLength = this.eChartsOption.visualMap.inRange.color.length
          // if(this.maxNum > 500) colorLength = 5
          let avg = parseInt(max / colorLength)
          let gtList = []
          for (let i = 0; i <= colorLength - 1; i++) {
            let gtItem = {
              gt: null,
              lte: null,
              label: null,
            }
            gtItem.gt = avg * i
            gtItem.lte = avg * (i + 1)
            gtItem.label = gtItem.gt + '~' + gtItem.lte + this.filteredUnit
            gtList.push(gtItem)
          }
          this.eChartsOption.visualMap.pieces = gtList
        }
      } else {
        this.eChartsOption.visualMap.text[0] = this.filteredUnit // 单位
      }
      this.eChartsOption.series[0].aspectScale = this.mapType == 'china' ? 0.75 : 1
      if (this.bubbles) {
        // 设置悬浮内容
        this.eChartsOption.tooltip.show = true
        this.eChartsOption.tooltip.formatter = (mapInfo) => {
          let bubbles = this.bubbles,
            data = bubbles.filter((item) => item[0] === mapInfo.name),
            str = ''
          if (data.length > 0) {
            if (this.mapUrl == 'queryMapData/getEconomicPay') {
              // 支出地区展示
              data[0].forEach((element, index) => {
                if (index == 1) {
                  str = `${data[0][0]}`
                  str += `<br/>支出金额：${data[0][1]} ${this.filteredUnit}`
                } else if (index == 2) {
                  str += `<br/>支出同比增速：${data[0][2]}%`
                }
              })
            } else if (this.mapUrl == 'queryMapData/getInventoryBalance') {
              // 支出地区展示
              data[0].forEach((element, index) => {
                if (index == 1) {
                  str = `${data[0][0]}`
                  str += `<br/>库款金额：${data[0][1]} ${this.filteredUnit}`
                } else if (index == 2) {
                  str += `<br/>库款同比增速：${data[0][2]}%`
                }
              })
            } else if (this.mapUrl == 'queryMapData/getEconomicTaxation') {
              // 经济区域税收收入的悬停
              data[0].forEach((element, index) => {
                if (index == 1) {
                  str = `${data[0][0]}`
                  str += `<br/>税收收入金额：${data[0][1]} ${this.filteredUnit}`
                } else if (index == 2) {
                  str += `<br/>税收收入同比增速：${data[0][2]}%`
                }
              })
            } else if (this.mapUrl == 'queryMapData/getBudgetIncome') {
              // 区域收支情况
              data[0].forEach((element, index) => {
                if (index == 1) {
                  str = `${data[0][0]}`
                  str += `<br/>收入金额：${data[0][1]} ${this.filteredUnit}`
                } else if (index == 2) {
                  str += `<br/>收入同比增速：${data[0][2]}%`
                } else if (index == 3) {
                  str += `<br/>支出金额：${data[0][3]} ${this.filteredUnit}`
                } else if (index == 4) {
                  str += `<br/>支出同比增速：${data[0][4]}%`
                }
              })
            } else {
              // 一般公共预算的悬停
              data[0].forEach((element, index) => {
                if (index == 1) {
                  str = `${data[0][0]}`
                  str += `<br/>一般公共预算收入金额：${data[0][1]} ${this.filteredUnit}`
                } else if (index == 2) {
                  str += `<br/>一般公共预算收入同比增速：${data[0][2]}%`
                } else if (index == 3) {
                  str += `<br/>税收收入金额：${data[0][3]} ${this.filteredUnit}`
                } else if (index == 4) {
                  str += `<br/>税收收入同比增速：${data[0][4]}%`
                }
              })
            }
          }
          return str
        }
        if (this.eChartsOption.series[0].mapType == 'CQ1') {
          this.eChartsOption.series[0].label.formatter = (mapInfo) => {
            let bubbles = this.bubbles,
              data = bubbles.filter((item) => item[0] === mapInfo.name),
              str = ''
            if (data.length > 0) {
              // 合并区
              if(this.mapUrl =='queryMapData/getBudgetIncome') {
                data[0].forEach((element, index) => {
                  if (index == 1) {
                    str = `${data[0][0]}`
                    str += `\n收入${data[0][1]} ${this.filteredUnit}`
                  } else if (index == 2) {
                    str += `，${data[0][2]}%`
                  } else if (index == 3) {
                    str += `\n支出${data[0][3]} ${this.filteredUnit}`
                  } else if (index == 4) {
                    str += `，${data[0][4]}%`
                  } 
                })
              } else {
                data[0].forEach((element, index) => {
                  if (index == 1) {
                    str = `${data[0][0]}`
                    str += `\n金额：${data[0][1]} ${this.filteredUnit}`
                  } else if (index == 2) {
                    str += `\n增速：${data[0][2]}%`
                  }
                })
              }
              
            }
            return str
          }
        }
      } else {
        if (!this.options || this.options.length == 0) {
          this.eChartsOption.tooltip.show = false
        }
      }
    },
    setCQ6() {
      // 重庆迁徙图设置
      let visualMap = {
        //图例值控制
        min: 0,
        max: 5,
        calculable: true,
        show: false,
        color: ['#E47C02', '#ff8c06', '#fc9700', '#ffde00', '#ffde00'],
        textStyle: {
          color: '#1e84d9',
        },
      }
      var geoCoordMap = {
        北京: [106.4377397, 31.66025875],
        高新区: [107.5, 28.5],
        两江新区: [107.9, 28.5],
        北碚: [106.5, 29.81],
        城口: [108.6520475, 31.90676506],
        大足: [105.7692868, 29.65392091],
        垫江: [107.4004904, 30.24903189],
        丰都: [107.7461781, 29.91492542],
        奉节: [109.3758974, 30.98202119],
        合川: [106.2833096, 30.09766756],
        江北: [106.6214893, 29.64821182],
        江津: [106.2647885, 28.98483627],
        开州: [108.1818518, 31.29466521],
        南岸: [106.6379653, 29.47704825],
        南川: [107.1406799, 29.12047319],
        彭水: [108.2573507, 29.36444557],
        綦江: [106.8036647, 28.96872774],
        黔江: [108.7559876, 29.44290625],
        石柱: [108.2438988, 30.07512144],
        重庆: [106.4377397, 29.52648606],
        铜梁: [106.0616035, 29.81036286],
        潼南: [105.811692, 30.13795513],
        万州: [108.0828876, 30.73353669],
        巫山: [109.8779184, 31.09568937],
        巫溪: [109.2970739, 31.48090521],
        武隆: [107.6831317, 29.36831708],
        秀山: [108.9997005, 28.49462861],
        永川: [105.8347961, 29.41042605],
        酉阳: [108.7911679, 28.88330557],
        云阳: [108.7533957, 30.96025875],
        长寿: [107.24, 29.95],
        忠县: [107.9279014, 30.33522658],
        涪陵: [107.3488646, 29.68233099],
      }
      let flowDatas = [
        [
          {
            name: '北京',
            value: 8,
          },
        ],
        [
          {
            name: '高新区',
            value: 5,
          },
        ],
        [
          {
            name: '两江新区',
            value: 5,
          },
        ],
        [
          {
            name: '北碚',
            value: 1,
          },
        ],
        [
          {
            name: '城口',
            value: 2,
          },
        ],
        [
          {
            name: '大足',
            value: 1,
          },
        ],
        [
          {
            name: '垫江',
            value: 1,
          },
        ],
        [
          {
            name: '丰都',
            value: 3,
          },
        ],
        [
          {
            name: '奉节',
            value: 1,
          },
        ],
        [
          {
            name: '合川',
            value: 3,
          },
        ],
        [
          {
            name: '江北',
            value: 1,
          },
        ],
        [
          {
            name: '江津',
            value: 3,
          },
        ],
        [
          {
            name: '开州',
            value: 1,
          },
        ],
        [
          {
            name: '南岸',
            value: 1,
          },
        ],
        [
          {
            name: '南川',
            value: 3,
          },
        ],
        [
          {
            name: '彭水',
            value: 1,
          },
        ],
        [
          {
            name: '綦江',
            value: 1,
          },
        ],
        [
          {
            name: '黔江',
            value: 2,
          },
        ],
        [
          {
            name: '石柱',
            value: 1,
          },
        ],
        [
          {
            name: '重庆',
            value: 8,
          },
        ],
        [
          {
            name: '铜梁',
            value: 1,
          },
        ],
        [
          {
            name: '潼南',
            value: 1,
          },
        ],
        [
          {
            name: '万州',
            value: 1,
          },
        ],
        [
          {
            name: '巫山',
            value: 4,
          },
        ],
        [
          {
            name: '巫溪',
            value: 3,
          },
        ],
        [
          {
            name: '武隆',
            value: 1,
          },
        ],
        [
          {
            name: '秀山',
            value: 1,
          },
        ],
        [
          {
            name: '永川',
            value: 3,
          },
        ],
        [
          {
            name: '酉阳',
            value: 5,
          },
        ],
        [
          {
            name: '云阳',
            value: 3,
          },
        ],
        [
          {
            name: '长寿',
            value: 1,
          },
        ],
        [
          {
            name: '忠县',
            value: 1,
          },
        ],
        [
          {
            name: '涪陵',
            value: 1,
          },
        ],
      ]
      let flowDatas1 = [
        [
          {
            name: '重庆',
            value: 4,
          },
        ],
      ]
      let convertData = function (data, latitude) {
        let res = []
        for (let i = 0; i < data.length; i++) {
          let dataItem = data[i]
          if (dataItem[0].value != 0) {
            let fromCoord = latitude
            let toCoord = geoCoordMap[dataItem[0].name]
            if (fromCoord && toCoord) {
              res.push([
                {
                  coord: fromCoord,
                  value: dataItem[0].value,
                },
                {
                  coord: toCoord,
                },
              ])
            }
          }
        }
        return res
      }
      let series = []
      ;[
        ['重庆', flowDatas, [106.4377397, 29.52648606]],
        ['北京', flowDatas1, [106.4377397, 31.66025875]],
      ].forEach(function (item, i) {
        series.push(
          {
            type: 'lines',
            zlevel: 2,
            effect: {
              show: true,
              period: 4, //箭头指向速度，值越小速度越快
              trailLength: 0.02, //特效尾迹长度[0,1]值越大，尾迹越长重
              symbolSize: 5, //图标大小
            },
            lineStyle: {
              normal: {
                width: 1, //尾迹线条宽度
                opacity: 1, //尾迹线条透明度
                curveness: 0.3, //尾迹线条曲直度
              },
            },
            data: convertData(item[1], item[2]),
          },
          {
            type: 'effectScatter',
            coordinateSystem: 'geo',
            zlevel: 2,

            symbolSize: function (val) {
              return val[2] / 100
            },
            showEffectOn: 'render',
            rippleEffect: {
              //涟漪特效
              scale: 2,
              period: 3,
              brushType: 'stroke', //波纹绘制方式 stroke, fill
            },
            label: {
              normal: {
                show: true,
                position: 'inside', //显示位置
                formatter: function (params) {
                  //圆环显示文字
                  return params.data.name
                },
                fontSize: 13,
                color: '#FFFFFF',
              },
              emphasis: {
                show: true,
              },
            },
            symbol: 'circle',
            symbolSize: function (val) {
              return 5 + val[2] * 5 //圆环大小
            },
            itemStyle: {
              normal: {
                show: false,
                color: '#f00',
              },
            },
            data: item[1].map(function (dataItem) {
              return {
                name: dataItem[0].name,
                value: geoCoordMap[dataItem[0].name].concat([dataItem[0].value]),
              }
            }),
          }
        )
      })

      this.eChartsOption.series[0].mapType = 'CQ2'
      this.eChartsOption.visualMap = visualMap
      this.eChartsOption.geo.show = true
      this.eChartsOption.series = series
    },
    clean() {
      window.removeEventListener('resize', this.chart.resize)
      this.chart.dispose()
      this.chart = null
    },
  },
}
</script>
