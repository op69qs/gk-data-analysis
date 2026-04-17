<template>
  <a-card :bordered="false" class="card-area">
    <div class="table-page-search-wrapper">
        <el-table
          class="drag_table"
          :data="tableDatas"
          border
          style="width: 100%"
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
          ></el-table-column>
        </el-table>
    </div>
  </a-card>
</template>

<script>
import { selectSchemeData } from '@/api/nationalTreasury'
export default {
  name:'displayScheme',
  data() {
    return {
      tableDatas:[],
      tableData:[]
    }
  },
  created(){
    console.log(JSON.parse(localStorage.getItem('records')))
  },
  mounted(){
    selectSchemeData({schemeId:JSON.parse(localStorage.getItem('records')).ID}).then(res=>{
      if(res.result =='success'){
        this.tableDatas = res.rows;
        this.tableData = res.columns
      }
    })
  },
  methods:{

  }
}
</script>
