<template>
  <div>
    <el-input placeholder="请选择季" v-model="showValue" style="width:100%;" @focus="showSeason=true" :disabled="isDisabled">
      <i slot="prefix" class="el-input__icon el-icon-date"></i>
    </el-input>
    <el-card
      class="box-card"
      style="width:322px;padding: 0 3px 20px;margin-top:10px;position:fixed;z-index:9999"
      v-show="showSeason"
    >
      <div slot="header" class="clearfix" style="text-align:center;padding:0">
        <button
          type="button"
          aria-label="前一年"
          class="el-picker-panel__icon-btn el-date-picker__prev-btn el-icon-d-arrow-left"
          @click="prev"
        ></button>
        <span role="button" class="el-date-picker__header-label">{{year}}年</span>
        <button
          type="button"
          aria-label="后一年"
          @click="next"
          class="el-picker-panel__icon-btn el-date-picker__next-btn el-icon-d-arrow-right"
        ></button>
      </div>
      <div class="text item" style="text-align:center;">
        <el-button
          type="text"
          size="medium"
          style="width:40%;color: #606266;float:left;"
          @click="selectSeason(0)"
        >第一季度
        </el-button>
        <el-button
          type="text"
          size="medium"
          style="float:right;width:40%;color: #606266;"
          @click="selectSeason(1)"
        >第二季度
        </el-button>
      </div>
      <div class="text item" style="text-align:center;">
        <el-button
          type="text"
          size="medium"
          style="width:40%;color: #606266;float:left;"
          @click="selectSeason(2)"
        >第三季度
        </el-button>
        <el-button
          type="text"
          size="medium"
          style="float:right;width:40%;color: #606266;"
          @click="selectSeason(3)"
        >第四季度
        </el-button>
      </div>
    </el-card>
  </div>
</template>
<script>
  /**
   * @file:  View 组件 季节选择控件
   * @author: v_zhuchun
   * @date: 2019-05-23
   * @description: UI组件  可选择季节
   * @api: valueArr : 季度value defalut['01-03', '04-06', '07-09', '10-12'] 默认值待设置
   */
  export default {
    name: 'dateQuarter',
    props: {
      isDisabled: false,
      valueArr: {
        default: () => {
          return ['01-03', '04-06', '07-09', '10-12']
        },
        type: Array
      },
      getValue: {
        default: () => {
        },
        type: Function
      },
      defaultValue: {
        default: '',
        type: String
      }
    },
    data() {
      return {
        showSeason: false,
        season: '',
        year: new Date().getFullYear(),
        showValue: ''
      }
    },
    created() {
      if (this.defaultValue) {
        let value = this.defaultValue
        let arr = value.split('-')
        this.year = arr[0].slice(0, 4)
        let str = arr[0].slice(4, 6) + '-' + arr[1].slice(4, 6)
        let arrAll = this.valueArr
        this.showValue = `${this.year}年${arrAll.indexOf(str) + 1}季度`
      }
    },
    watch: {
      defaultValue: function (value, oldValue) {
        let arr = value.split('-')
        this.year = arr[0].slice(0, 4)
        let str = arr[0].slice(4, 6) + '-' + arr[1].slice(4, 6)
        let arrAll = this.valueArr
        this.showValue = `${this.year}年${arrAll.indexOf(str) + 1}季度`
      }
    },
    methods: {
      one() {
        this.showSeason = false
      },
      prev() {
        this.year = this.year * 1 - 1
      },
      next() {
        this.year = this.year * 1 + 1
      },
      selectSeason(i) {
        let that = this
        that.season = i + 1
        let arr = that.valueArr[i].split('-')
        that.getValue(that.year + arr[0] + '-' + that.year + arr[1])
        that.showSeason = false
        this.showValue = `${this.year}年${this.season}季度`
        this.$emit('value', `${this.year}Q${this.season}`);
        console.log(`${this.year}Q${this.season}`)
      }
    }
  }
</script>
