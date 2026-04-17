<template>
  <a-modal title="表格" v-model="visibleModal" width="75%"
           :maskClosable="false">
    <div id="integrityCardModal">
      <el-table :data="dataSource" style="width: 100%" border>
        <el-table-column  label="样本区间" width="200"
                         align="center">
          <el-table-column prop="D_REPORTDATE_START" label="开始" width="100" align="center"></el-table-column>
          <el-table-column prop="D_REPORTDATE" label="结束" width="100"  align="center"></el-table-column>
        </el-table-column>
        <el-table-column  label="实际" width="260" align="center">
          <el-table-column prop="D_REPORTDATE" label="时间" width="100" align="center"></el-table-column>
          <el-table-column prop="FACT_MAT" label="值" width="160" header-align="center" align="right"></el-table-column>
        </el-table-column>
        <el-table-column  label="预测" width="260" align="center">
          <el-table-column prop="D_REPORTDATE_FORECAST" label="时间" width="100" align="center"></el-table-column>
          <el-table-column prop="FORECAST_MAT" label="值" width="160" header-align="center" align="right"></el-table-column>
        </el-table-column>
        <el-table-column  prop="CHA_MAT" label="误差值" width="215" header-align="center"  align="right" />
        <el-table-column  prop="ZHANBI_MAT" label="误差比"  width="215" header-align="center" align="right" />
      </el-table>
    </div>
    <template slot="footer">
      <a-button type="primary"  @click="handleOk">导出</a-button>
      <a-button   @click="handleClose">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
  import {
    getStructuredIssueList,
    addIssueList,
    getQuestionBankPage,
    getStatisticsTable,
    getCurTaskQuestion_1
  } from '@/api/nationalTreasury'

  export default {
    name: 'forecastTable',
    props: ['arr'],
    data() {
      return {
        visibleModal: false,
        drawerWidth: 700,
        confirmLoading: false,
        taskInfo: {},
        model: {},
        dataSource: [],
                
      }
    },
    mounted(){
      /* var oThis = this;
      setTimeout(()=>{
        oThis.dataSource = oThis.arr;
      },1000) */
    },
    methods: {
      edit(){
        this.dataSource = this.arr;
      },
      handleOk() {
        let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
        let userId = userInfo.id
        window.location.href = `${
          window._CONFIG['domianURL']
          }/reportcenter/gkForeCast/getTotalReportExcelOne?userId=${
          userId
          }&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
        this.visibleModal = false
      },
      handleClose() {
        this.visibleModal = false
      },
      formatNum(str) {
      var newStr = ''
      var count = 0
      if (str.indexOf('.') == -1) {
        for (var i = str.length - 1; i >= 0; i--) {
          if (count % 3 == 0 && count != 0) {
            newStr = str.charAt(i) + ',' + newStr
          } else {
            newStr = str.charAt(i) + newStr
          }
          count++
        }
        str = newStr + '.00' //自动补小数点后两位
      } else {
        for (var i = str.indexOf('.') - 1; i >= 0; i--) {
          if (count % 3 == 0 && count != 0) {
            newStr = str.charAt(i) + ',' + newStr
          } else {
            newStr = str.charAt(i) + newStr //逐个字符相接起来
          }
          count++
        }
        str = newStr + (str + '00').substr((str + '00').indexOf('.'), 3)
      }
      return str
    }
    }
  }
</script>

<style scoped>
  #integrityCardModal h2 {
    text-align: center;
  }

</style>