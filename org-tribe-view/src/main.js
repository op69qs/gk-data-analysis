import Vue from 'vue'
import App from './App.vue'
import Storage from 'vue-ls'
import router from './router'
import store from './store/'

import {VueAxios} from '@/utils/request'
import scroller from '@/utils/table-scroller'

import moment from 'moment'
import VueJsonp from 'vue-jsonp'
import echarts from 'echarts'
import draggable from 'vuedraggable'
import Antd from 'ant-design-vue'
import {
  Table,
  TableColumn,
  Steps,
  Step,
  Input,
  Tree,
  Button,
  Pagination,
  Scrollbar,
  Dialog,
  Loading,
  DatePicker,
  Select,
  Option,
  Card,
  Transfer
} from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Viser from 'viser-vue'
import 'ant-design-vue/dist/antd.less' // or 'ant-design-vue/dist/antd.less'

import '@/permission' // permission control
import '@/utils/filter' // base filter
import '@/utils/directives'
import Print from 'vue-print-nb-jeecg'
import {duplicateCheck} from '@/api/api'
/*import '@babel/polyfill'*/
import VueApexCharts from 'vue-apexcharts'
import VueQuillEditor from 'vue-quill-editor'

import preview from 'vue-photo-preview'
import 'vue-photo-preview/dist/skin.css'
import '@jeecg/antd-onine'
import '@jeecg/antd-onine/dist/OnlineForm.css'

import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'



import {
  ACCESS_TOKEN,
  DEFAULT_COLOR,
  DEFAULT_THEME,
  DEFAULT_LAYOUT_MODE,
  DEFAULT_COLOR_WEAK,
  SIDEBAR_TYPE,
  DEFAULT_FIXED_HEADER,
  DEFAULT_FIXED_HEADER_HIDDEN,
  DEFAULT_FIXED_SIDEMENU,
  DEFAULT_CONTENT_WIDTH_TYPE,
  DEFAULT_MULTI_PAGE
} from '@/store/mutation-types'
import config from '@/defaultSettings'

import JDictSelectTag from './components/dict/index.js'
import hasPermission from '@/utils/hasPermission'
import vueBus from '@/utils/vueBus'
import JeecgComponents from '@/components/jeecg/index'

Vue.config.productionTip = false
Vue.use(Storage, config.storageOptions)
Vue.use(Antd)
//Vue.use(VueJsonp)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(Input)
Vue.use(Dialog)
Vue.use(Loading)
Vue.use(DatePicker)
Vue.use(Select)
Vue.use(Option)
Vue.use(Card)
Vue.use(Steps)
Vue.use(Step)
Vue.use(Tree)
Vue.use(Transfer)
Vue.use(Scrollbar)
Vue.use(Button)
Vue.use(Pagination)
Vue.use(echarts)
Vue.use(draggable)
Vue.use(VueAxios, router)
Vue.use(Viser)
Vue.use(hasPermission)
Vue.use(JDictSelectTag)
Vue.use(Print)
Vue.use(VueApexCharts)
Vue.component('apexchart', VueApexCharts)
Vue.use(preview)
Vue.use(vueBus)
Vue.use(JeecgComponents)
Vue.use(VueQuillEditor)
Vue.use(scroller)
import {downFilePost} from '@/api/manage'
import {Icon} from 'ant-design-vue'

Vue.prototype.$sessionStorage = Vue
const IconFont = Icon.createFromIconfontCN({
  scriptUrl: '/font_1465248_r12loy9em3/iconfont.js'
  // scriptUrl: '//at.alicdn.com/t/font_1465248_wlvnlaunuv.js'
})
Vue.component('IconFont', IconFont)
Vue.mixin({
  data() {
    return {spanArr: []}
  },
  methods: {
    /**年龄计算
     * @param str 格式：’2019-01-01‘
     * @returns number
     * */
    ages(str) {
      let r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})$/);
      if (r == null) return false
      let d = new Date(r[1], r[3] - 1);
      if (d.getFullYear() == r[1] && d.getMonth() + 1 == r[3]) {
        let Y = new Date().getFullYear()
        return Y - r[1]
      }
      return Vue.$message.warning('输入的日期格式错误');
    },
    /*电话校验*/
    validatePhone(rule, value, callback) {
      if (!value) {
        callback()
      } else {
        if (new RegExp(/^([1]\d{10}|([\(（]?0[0-9]{2,3}[）\)]?[-]?)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)$/).test(value)) {
          var params = {
            tableName: 'sys_user',
            fieldName: 'phone',
            fieldVal: value,
            dataId: this.userId
          }
          duplicateCheck(params).then(res => {
            if (res.success) {
              callback()
            } else {
              callback('手机号已存在!')
            }
          })
        } else {
          callback('请输入正确格式的手机号码!')
        }
      }
    },
    moment,
    /*文件导出*/
    handleDownload(fileName, url, params) {
      if (!fileName || typeof fileName != 'string') {
        fileName = '导出文件'
      }
      downFilePost(url, params).then(data => {
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
    /*element 合并格式化数据*/
    getSpanArr(data, name) {
      this.spanArr = []
      for (var i = 0; i < data.length; i++) {
        if (i === 0) {
          // 如果是第一条记录（即索引是0的时候），向数组中加入１
          this.spanArr.push(1)
          this.pos = 0
        } else {
          if (data[i][name] === data[i - 1][name]) {
            // 如果useName相等就累加，并且push 0
            this.spanArr[this.pos] += 1
            this.spanArr.push(0)
          } else {
            // 不相等push 1
            this.spanArr.push(1)
            this.pos = i
          }
        }
      }
    },
    /*input数字校验*/
    handleInputDigital(e) {
      this.val = e.target.value.replace(/[^\d]/g, '')
    },
    /*数据合并index*/
    rowMergeHandle(arr, data) {
      if (!Array.isArray(arr) && !arr.length) return false
      if (!Array.isArray(data) && !data.length) return false
      let needMerge = {}
      arr.forEach(i => {
        needMerge[i] = {
          rowArr: [],
          rowMergeNum: 0,
          ledgerIDArr: [],
          // questionArr: [],
          // QUESTION_DSCR: [],
          questionArr: [],
          IDArr: []
        }
        data.forEach((item, index) => {
          if (index === 0) {
            needMerge[i].rowArr.push(1)
            //整改台账id
            if (item.ID) needMerge[i].IDArr.push(item.ID)
            //问题台账id
            if (item.LEDGER_ID) needMerge[i].ledgerIDArr.push(item.LEDGER_ID)
            //问题分类id
            if (item.QUESTION_ID) {
              // needMerge[i].questionArr.push(item.QUESTION_ID);
              //组合数据1，2，3，4级及制度依据
              needMerge[i].questionArr.push([
                {
                  path: item.path,
                  QUESTION_ID: item.QUESTION_ID,
                  QUESTION_DSCR: item.QUESTION_DSCR,
                  concent: item.concent ? item.concent : ''
                }
              ])
            }else{
              needMerge[i].questionArr.push([
                {
                  path: '',
                  QUESTION_ID: '',
                  QUESTION_DSCR: '',
                  concent: ''
                }
              ])
            }
            // 问题分类
            /*if (item.QUESTION_DSCR)
              needMerge[i].QUESTION_DSCR.push(item.QUESTION_DSCR);*/
            needMerge[i].rowMergeNum = 0
          } else {
            if (item[i] === data[index - 1][i]) {
              needMerge[i].rowArr[needMerge[i].rowMergeNum] += 1
              needMerge[i].rowArr.push(0)
              //整改台账id
              if (item.ID) {
                needMerge[i].IDArr[needMerge[i].rowMergeNum] += `,${item.ID}`
                needMerge[i].IDArr.push(item.ID)
              }
              if (item.LEDGER_ID) {
                needMerge[i].ledgerIDArr[needMerge[i].rowMergeNum] += `,${item.LEDGER_ID}`
                needMerge[i].ledgerIDArr.push(item.LEDGER_ID)
              }
              if (item.QUESTION_ID) {
                /*needMerge[i].questionArr[needMerge[i].rowMergeNum] += `,${item.QUESTION_ID}`;
                needMerge[i].questionArr.push(item.QUESTION_ID);*/
                needMerge[i].questionArr[needMerge[i].rowMergeNum].push({
                  path: item.path,
                  QUESTION_ID: item.QUESTION_ID,
                  QUESTION_DSCR: item.QUESTION_DSCR,
                  concent: item.concent ? item.concent : ''
                })
                needMerge[i].questionArr.push([
                  {
                    path: item.path,
                    QUESTION_ID: item.QUESTION_ID,
                    QUESTION_DSCR: item.QUESTION_DSCR,
                    concent: item.concent ? item.concent : ''
                  }
                ])
              }else{
                needMerge[i].questionArr[needMerge[i].rowMergeNum].push({
                  path: '',
                  QUESTION_ID: '',
                  QUESTION_DSCR: '',
                  concent: ''
                })
                needMerge[i].questionArr.push([
                  {
                    path: '',
                    QUESTION_ID: '',
                    QUESTION_DSCR: '',
                    concent: ''
                  }
                ])
              }
            } else {
              needMerge[i].rowArr.push(1)
              //整改台账id
              if (item.ID) {
                needMerge[i].IDArr.push(item.ID)
              }
              if (item.LEDGER_ID) needMerge[i].ledgerIDArr.push(item.LEDGER_ID)
              if (item.QUESTION_ID) {
                // needMerge[i].questionArr.push(item.QUESTION_ID);
                //组合数据1，2，3，4级及制度依据
                needMerge[i].questionArr.push([
                  {
                    path: item.path,
                    QUESTION_ID: item.QUESTION_ID,
                    QUESTION_DSCR: item.QUESTION_DSCR,
                    concent: item.concent ? item.concent : ''
                  }
                ])
              }else{
                needMerge[i].questionArr.push([
                  {
                    path: '',
                    QUESTION_ID: '',
                    QUESTION_DSCR: '',
                    concent: ''
                  }
                ])
              }
              /* if (item.QUESTION_DSCR)
                 needMerge[i].QUESTION_DSCR.push(item.QUESTION_DSCR);*/
              needMerge[i].rowMergeNum = index
            }
          }
        })
      })
      return needMerge
    },
    /**
     * @description 根据数组来确定单元格是否需要合并
     * @param val:String 需要合并的列name 如:'name' 'id'
     * @param rowIndex:Number 当前行的行数，由合并函数传入
     * @param colData:Object 当前列的数据，由合并函数传入
     * @return 返回值为一个数组表示该单元格是否需要合并; 说明: [0,0]表示改行被合并了 [n+,1]n为1时表示该单元格不动,n大于1时表示合并了N-1个单元格
     */
    mergeAction(val, rowIndex, colData) {
      let _row = this.rowMergeArrs[val].rowArr[rowIndex]
      let _col = _row > 0 ? 1 : 0
      if (this.rowMergeArrs[val].ledgerIDArr)
        this.dataSource[rowIndex].ledgerIDArr = this.rowMergeArrs[val].ledgerIDArr[rowIndex]
      // this.dataSource[rowIndex].questionArr = this.rowMergeArrs[val].questionArr[rowIndex];
      if (this.rowMergeArrs[val].QUESTION_DSCR)
        this.dataSource[rowIndex].QUESTION_DSCR = this.rowMergeArrs[val].QUESTION_DSCR[rowIndex]
      if (this.rowMergeArrs[val].questionArr)
        this.dataSource[rowIndex].questionArr = this.rowMergeArrs[val].questionArr[rowIndex]
      if (this.rowMergeArrs[val].IDArr) this.dataSource[rowIndex].IDArr = this.rowMergeArrs[val].IDArr[rowIndex]
      return [_row, _col]
    }
  }
})
new Vue({
  router,
  store,
  mounted() {
    const storedLayoutMode = Vue.ls.get(DEFAULT_LAYOUT_MODE, config.layout)
    const layoutMode = storedLayoutMode === 'sidemenu' ? 'topmenu' : storedLayoutMode

    store.commit('SET_SIDEBAR_TYPE', Vue.ls.get(SIDEBAR_TYPE, true))
    store.commit('TOGGLE_THEME', Vue.ls.get(DEFAULT_THEME, config.navTheme))
    store.commit('TOGGLE_LAYOUT_MODE', layoutMode)
    store.commit('TOGGLE_FIXED_HEADER', Vue.ls.get(DEFAULT_FIXED_HEADER, config.fixedHeader))
    store.commit('TOGGLE_FIXED_SIDERBAR', Vue.ls.get(DEFAULT_FIXED_SIDEMENU, config.fixSiderbar))
    store.commit('TOGGLE_CONTENT_WIDTH', Vue.ls.get(DEFAULT_CONTENT_WIDTH_TYPE, config.contentWidth))
    store.commit('TOGGLE_FIXED_HEADER_HIDDEN', Vue.ls.get(DEFAULT_FIXED_HEADER_HIDDEN, config.autoHideHeader))
    store.commit('TOGGLE_WEAK', Vue.ls.get(DEFAULT_COLOR_WEAK, config.colorWeak))
    store.commit('TOGGLE_COLOR', Vue.ls.get(DEFAULT_COLOR, config.primaryColor))
    store.commit('SET_TOKEN', Vue.ls.get(ACCESS_TOKEN))
    store.commit('SET_MULTI_PAGE', Vue.ls.get(DEFAULT_MULTI_PAGE, true))
  },
  render: h => h(App)
}).$mount('#app')
