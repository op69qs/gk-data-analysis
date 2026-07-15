<template>
  <div class="table">
    <div class="table-left" v-if="isTop">
      <div v-for="(item, index) in filteredItems" :key="index" class="table-top" :style="{height: CQIndex!=null ? '23%':'30%'}">
        <img :src="imgList[index].img" :class="smallHeight > 5 ? 'imgIcon1' : 'imgIcon2'" />
        {{ item.AREA_DSCR }}
      </div>
      <div class="table-cqtop" v-if="CQIndex!=null">
        <div :class="smallHeight > 5 ? 'cq1' : 'cq2'">
          <img src="@/assets/cqIcon.png"/>
          <p>{{CQIndex}}</p>
        </div>
        重庆
      </div>
    </div>
    <div class="tableCard" :style="{ width: isTop ? '80%' : '100%' }">
      <ul :class="{ 'table-title': true, topTitle: isTop }">
        <li v-for="(item, index) in tableTitle" :key="index">
          {{ index == 0 ? item : capitalize(item)  }}
        </li>
      </ul>
      <vue-seamless-scroll :data="tableData" class="seamless-warp" :class-option="classOption" v-if="tableData.length > 0">
        <div class="table-list">
          <ul v-for="(item,index) in tableData" :key="index">
            <li :class="isTop ? 'areaValueTop' : ''">{{ item.AREA_DSCR }}</li>
            <li v-if="item.INDEX_VALUE">{{ item.INDEX_VALUE }}</li>
            <li v-if="item.GROWTH_INDEX_VALUE">{{ item.GROWTH_INDEX_VALUE ? item.GROWTH_INDEX_VALUE : '暂无' }}</li>
            <li v-if="item.INDEX_VALUE1">{{ item.INDEX_VALUE1 }}</li>
            <li v-if="item.GROWTH_INDEX_VALUE1">{{ item.GROWTH_INDEX_VALUE1 }}</li>
            <li v-if="isTop">{{ index+1 }}</li>
          </ul>
        </div>
      </vue-seamless-scroll>
      <div class="seamless-warp1" v-else>
        <div class="nodata">暂无数据</div>
      </div>
    </div>
  </div>
</template>
<script>
import { postAction } from '@/api/manage'
import { visPreviewDebug } from '@/utils/visPreviewDebug'
import { normalizeVisRequestUrl as buildVisRequestUrl, normalizeVisTableRows } from '@/utils/visRequest'
export default {
  name: 'BSTable',
  props: {
    smallHeight: {
      type: Number,
    },
    itemData: {
      type: Object,
    },
    fatherMethod: {
      type: Function,
      default: null,
    },
  },
  data() {
    return {
      isTop: true,
      tableTitle: '', // 表格标题
      tableData: [],
      CQIndex: null,
      imgList: [
        {
          img: require('@/assets/firstIcon.png'),
        },
        {
          img: require('@/assets/secondIcon.png'),
        },
        {
          img: require('@/assets/thirdIcon.png'),
        },
      ],
      limitMoveNum: 5,
    }
  },
  computed: {
    filteredItems: function () {
      // 取数组前三
      return this.tableData.slice(0, 3)
    },
    filteredUnit: function () {
      // 换算单位
      let number
      if (this.itemData.unit == '1') {
        number = '(元)'
      } else if (this.itemData.unit == '10000') {
        number = '(万元)'
      } else {
        number = '(亿元)'
      }
      return number
    },
    classOption () { // 滚动插件参数设置
       return {
        step: 0.1, // 数值越大速度滚动越快
        limitMoveNum: this.limitMoveNum, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
      }
    }
  },
//   filters: {
   
//   capitalize: function (value) {
     
//   }
// },
  watch: {},
  mounted() {
    this.getList(this.itemData)
  },
  methods: {
    capitalize(value){ // title设置
    if (!value) return ''
    let newValue = ''
    if(value.indexOf('同比')!=-1) {
      newValue = value + ('%')
    } else if(value.indexOf('金额')!=-1){
      newValue = value + this.filteredUnit
    } else if(value.indexOf('余额')!=-1){
      newValue = value + this.filteredUnit
    }else {
      newValue = value
    }
    return newValue
    },
    getList(item) {
      this.tableTitle = (this.itemData.option || '').split(',').filter(Boolean)
      this.setIsTop()
      let that = this
      let completed = false
      const markComplete = () => {
        if (!completed) {
          completed = true
          visPreviewDebug('table request complete', {
            id: item.id,
            title: item.title,
            type: item.type,
            query_path: item.query_path
          })
          if (this.fatherMethod) this.fatherMethod()
        }
      }
      if (item.query_path && item.page_id != null && item.query_path != null && item.query_path != 'null') {
        let url = buildVisRequestUrl(item.query_path)
        let param = {
          id: item.id,
        }
        visPreviewDebug('table request start', {
          id: item.id,
          title: item.title,
          type: item.type,
          query_path: url,
          param: param
        })
        postAction(url, param).then((res) => {
          visPreviewDebug('table request response', {
            id: item.id,
            title: item.title,
            query_path: url,
            result: res && res.result,
            rowsLength: res && res.rows && res.rows.length,
            res: res
          })
          let options
          if (res.result == 'success') {
            markComplete()
            this.tableData = normalizeVisTableRows(res.rows || [])
            if(this.smallHeight > 5) {
              this.limitMoveNum = 9
            }else {
              this.limitMoveNum = 3
            }
            this.CQIndex = res.CQIndex
            if (res.dateInfo && res.dateInfo.dateDesc) {
              this.itemData.datatime = res.dateInfo.dateDesc
              this.$emit('showDataTime', this.itemData)
            }
            this.setIsTop()
          } else {
            markComplete()
          }
        }).catch(() => {
          visPreviewDebug('table request catch', {
            id: item.id,
            title: item.title,
            query_path: url
          })
          markComplete()
        })
      } else {
        visPreviewDebug('table request skipped because condition false', {
          id: item.id,
          title: item.title,
          type: item.type,
          query_path: item.query_path,
          page_id: item.page_id
        })
      }
    },
    setIsTop() { // 设置表格样式
      if (this.tableData.length < 1 && this.tableTitle.length <= 3) {
        this.isTop = false
      } else if (this.tableData.length > 0 && this.tableTitle.length <= 3) {
        this.isTop = true
      } else {
        this.isTop = false
      }
    },
  },
}
</script>
<style lang="less" scoped>
.table {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-content: flex-start;
}
.table-left {
  width: 22%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;

  .table-top,.table-cqtop {
    width: 100%;
    height: 30%;
    display: -webkit-box;
    display: -ms-flexbox;
    display: -webkit-flex;
    display: flex;  
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    -webkit-justify-content: flex-start;
    justify-content: flex-start;
    -webkit-box-align: center;
    -ms-flex-align: center;
    -webkit-align-items: center;
    align-items: center;
    .imgIcon1 {
      // h>4的样式
      height: 45%;
      margin-right: 0.5rem;
    }
    .imgIcon2 {
      // h <4的样式
      height: 45%;
      margin-right: 0.5rem;
    }
  }
  .table-cqtop{
    position: relative;
    height: 31%;
    .cq1 {
      // h>4的样式
      position: relative;
      height: 45%;
      margin-right: 0.5rem;
      p{
        position: absolute;
        left: 0;
        top: 6%;
        width: 100%;
        text-align: center;
        margin: 0;
        font-size: 1rem;
        line-height: 1.2rem;
      }
    }
    .cq2 {
      // h <4的样式
      position: relative;
      height: 45%;
      margin-right: 0.5rem;
      p{
        position: absolute;
        left: 0;
        top: 6%;
        width: 100%;
        text-align: center;
        margin: 0;
        font-size: 1rem;
        line-height: 1.2rem;
      }
    }
    img{
      height: 100%;
    }
    // p{
    //   position: absolute;
    //   left: 0;
    //   top: 6%;
    //   width: 100%;
    //   text-align: center;
    //   margin: 0;
    //   font-size: 1rem;
    //   line-height: 1.2rem;
    // }
  }
}
.tableCard {
  position: relative;
  width: 75%;
  height: 100%;
  ul {
    margin: 0;
    padding: 0;
  }
  .table-title {
    width: 100%;
    display: table;
    text-align: center;
    background: #10324f;
    border: 1px solid #22293f;
    li {
      cursor: default;
      display: table-cell;
      vertical-align: middle;
      text-align: center;
      border-right: 1px solid #22293f;
      padding: 0.2rem;
      width: 21.25%;
      height: 2.6rem;
    }
    li:nth-child(1) {
      width: 15%;
    }
    li:last-child {
      border-right: 0;
    }
  }
  .topTitle {
    li:nth-child(1) {
      width: 25%;
    }
  }
  .table-list {
    ul {
      width: 100%;
      display: table;
      padding: 0.3rem 0;
      white-space: normal;
      color: #fff;
      text-align: center;
      li {
        cursor: default;
        display: table-cell;
        vertical-align: middle;
        text-align: center;
        width: 21.25%;
        word-wrap:break-word;
        word-break:break-all;
      }
      li:nth-child(1) {
        width: 15%;
      }
      li:last-child {
        border-right: 0;
      }
      .areaValueTop {
        width: 25% !important;
      }
    }
  }
  li {
    list-style: none;
    font-weight: 600;
  }
}
.seamless-warp {
  height: ~'calc(100% - 3.5rem)';
  overflow: hidden;
}
.seamless-warp1 {
  position: relative;
  width: 100%;
  height: ~'calc(100% - 5rem)';
  overflow: hidden;
}
.nodata {
  position: absolute;
  text-align: center;
  width: 100%;
  top: 40%;
}
</style>
