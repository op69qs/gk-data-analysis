<template>
  <a-card :bordered="false" class="card-area">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper search2">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="8" :sm="8">
            <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}" label="数据库类型">
              <a-select allowClear showSearch v-model="queryParam.TYPE" placeholder="请选择数据库类型查询" :filterOption="filterOption" >
                <a-select-option :value="d.id" v-for="d in dataBaseType" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery()">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-col>
          </span>
          <a-col :md="24" :sm="24">
            <a-button @click="handleAdd" type="primary">新增</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div id="tables" style="margin-top:20px;">
      <el-table :data="dataSource" :span-method="arraySpanMethod" :header-cell-style="{background:'#fafafa',color:'#606266'}"
        border style="width: 100%; margin-top: 20px">
        <el-table-column key='1' prop="DATASOURCE_NAME" min-width="20%" label="名称" align="left" header-align="center">
        </el-table-column>
        <el-table-column key='2' label="数据库类型" min-width="15%" prop="TYPE" align="center">
        </el-table-column>
        <el-table-column key='3' prop="IP" min-width="15%" label="IP地址" align="center">
        </el-table-column>
        <el-table-column key='4' prop="PORT" min-width="10%" label="端口" align="center">
        </el-table-column>
        <el-table-column key='5' prop="USERNAME" min-width="10%" label="用户名" align="center">
        </el-table-column>
        <el-table-column key='6' prop="DBNAME" min-width="10%" label="数据库" align="center">
        </el-table-column>
        <el-table-column key='7' prop="STATE" min-width="10%" align="center" label="状态">
          <template slot-scope="scope">
            <span v-if="scope.row.STATE === '0'">启用</span>
            <span v-if="scope.row.STATE === '1'" style="color:red">禁用</span>
          </template>
        </el-table-column>
        <el-table-column key='8' prop="action" min-width="10%" align="center" label="操作">
          <template slot-scope="scope">
            <a @click="handleEdit(scope.row)">编辑</a>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- table区域-end -->
    <data-source-modal ref="DataSourceModal" @ok="modalFormOk" :dataBaseType="dataBaseType" :dataSource="dataSource"></data-source-modal>
  </a-card>
</template>


<script>
  import {
    getDataSource,
    getDataSourceEnumSelect
  } from '@/api/integratedQueryApi'
  import {
    getAction,
    postAction
  } from '@/api/manage'
  import {
    JeecgListMixin
  } from '@/mixins/JeecgListMixin'
  import DataSourceModal from './modules/DataSourceModal'

  export default {
    name: 'manageIndex',
    mixins: [JeecgListMixin],
    components: {
      DataSourceModal
    },
    data() {
      return {
        // 查询条件
        queryParam: {},
        model: {},
        loading: false,
        dataSource:[],
        dataBaseType: [],
        rowMergeArrs: {}, // 包含需要一个或多个合并项信息的对象
        needMergeArr: ['DATASOURCE_NAME'], // 有合并项的列
      }
    },
    created() {
      this.loadData();
      this.getDataBaseType();
    },
    methods: {
      //搜索过滤
      filterOption(input, option) {
        return (
          option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
        );
      },
      //获取数据库类型
      getDataBaseType() {
        getDataSourceEnumSelect({}).then(res => {
          if (res.result == 'success') {
            this.dataBaseType = res.rows;
          }
        })
      },
      //加载数据源信息
      loadData() {
        let params = Object.assign({}, this.queryParam);
        getDataSource(params).then(res => {
          if (res.result == 'success') {
            this.loading = false;
            this.dataSource = res.rows;
            this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource);
          }
        })
      },
      //查询
      searchQuery() {
        this.loading = true;
        this.loadData();
      },
      //重置
      searchReset() {
        delete this.queryParam.TYPE
        this.loadData();
      },
      //新增
      handleAdd: function () {
        let ref = this.$refs.DataSourceModal;
        ref.title = '编辑';
        ref.add();
      },
      //编辑
      handleEdit: function (record) {
        let ref = this.$refs.DataSourceModal;
        ref.title = '编辑';
        ref.edit(record);
      },
      //合并
      arraySpanMethod({row,column,rowIndex, columnIndex}) {
        if (column.property === 'DATASOURCE_NAME') return this.mergeAction('DATASOURCE_NAME', rowIndex, column);
        if (column.property === 'TYPE') return this.mergeAction('DATASOURCE_NAME', rowIndex, column);
        if (column.property === 'IP') return this.mergeAction('DATASOURCE_NAME', rowIndex, column);
        if (column.property === 'PORT') return this.mergeAction('DATASOURCE_NAME', rowIndex, column);
        if (column.property === 'action') return this.mergeAction('DATASOURCE_NAME', rowIndex, column);
      }
    }
  }
</script>
