  <template>
  <a-card :bordered="false" class="card-area">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper search2">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="8" :sm="8">
            <a-form-item label="指标名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input placeholder="请输入指标名称关键字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}" label="指标类型">
              <a-select v-model="queryParam.type" placeholder="请输入指标类型">
                <a-select-option :value="d.id" v-for="d in queryOption" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 1}"
              :label="label.name1"
            >
              <a-select v-model="queryParam.INDEX_DIMNSN" placeholder="请输入指标维度">
                <a-select-option :value="d.id" v-for="d in queryOption1" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18, offset: 1}"
              :label="label.name"
            >
              <a-select v-model="queryParam.INDEX_PERIOD" placeholder="请输入指标周期">
                <a-select-option :value="d.id" v-for="d in queryOption2" :key="d.id">{{d.name}}</a-select-option>
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
            <a-button type="primary" @click="add()">新增</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div id="tables" style="margin-top:20px;">
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        row-key="INDEX_ID"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        
        @change="handleTableChange"
      >
        <span slot="RUN_BATCH_STATUS" slot-scope="text, record">
          <template v-if="record.RUN_BATCH_STATUS==='0'">未开始</template>
          <template v-if="record.RUN_BATCH_STATUS==='1'">进行中</template>
          <template v-if="record.RUN_BATCH_STATUS==='2'">已完成</template>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleProcess(record)">编辑</a>
          <a-divider type="vertical" />
          <a v-if="record.RUN_BATCH_STATUS === '0'" @click="handleRun(record)">跑批</a>
          <a-divider v-if="record.RUN_BATCH_STATUS === '0'" type="vertical" />
          <!-- <a @click="handlDelete(record)">删除</a> -->
          <a-popconfirm
            title="此操作将会删除当前指标的所有数据，是否确定删除？"
            @confirm="handlDelete(record)"
            okText="确定"
            cancelText="取消"
          >
            <a style="margin-right: .8rem">删除</a>
          </a-popconfirm>
          <!-- <a  @click="handleProcess(record)">查看报告</a> -->
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->
  </a-card>
</template>

<script>
/* import {ListMixin} from '@/mixins/ListMixin' */
import { getIndexManageList, deleteMineIndex, historyRunBatch } from '@/api/nationalTreasury'
import { getAction, postAction } from '@/api/manage'
import { GetUrlParam } from '@/utils/request'

export default {
  name: 'manageIndex',
  data() {
    return {
      // 查询条件
      queryParam: {
        pageNo: 1, //第几页
        pageSize: 10 //每页中显示数据的条数,
      },
      label: {
        name: '周' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '期',
        name1: '维' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '度'
      },
      model: {},
      loading: false,
      /* 分页参数 */
      pagination: {
        total: 0,
        pageSize: 10, //每页中显示10条数据
        showSizeChanger: true,
        pageSizeOptions: ['10', '20', '50', '100'], //每页中显示的数据
        showTotal: total => `共有 ${total} 条数据` //分页中显示总的数据
      },
      dataSource: [],
      status: false,
      // 表头
      columns: [
        {
          title: '指标名称',
          align: 'center',
          width: 300,
          dataIndex: 'INDEX_NAME'
        },
        {
          title: '指标类型',
          align: 'center',
          width: 200,
          dataIndex: 'INDEX_TYPE_DSCR'
        },
        {
          title: '维度',
          align: 'center',
          dataIndex: 'INDEX_DIMNSN_DSCR',
          width: 200
        },
        {
          title: '周期',
          align: 'center',
          dataIndex: 'INDEX_PERIOD_DSCR',
          width: 200
        },
        {
          title: '历史数据跑批状态',
          align: 'center',
          dataIndex: 'RUN_BATCH_STATUS',
          scopedSlots: { customRender: 'RUN_BATCH_STATUS' },
          width: 200
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
         
          scopedSlots: { customRender: 'action' }
        }
      ],
      queryOption: [
        { id: '0', name: '数值' },
        { id: '1', name: '比率' }
      ],
      queryOption1: [
        { id: '1', name: '国库' },
        { id: '2', name: '地区' }
      ],
      queryOption2: [
        { id: '1', name: '日' },
        { id: '2', name: '月' },
        { id: '3', name: '季' },
        { id: '4', name: '年' }
      ]
    }
  },
  created() {
    let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
    let userId = userInfo.id
    getIndexManageList({ pageNo: 1, pageSize: 10, USERID: userId }).then(res => {
      if (res.result == 'success') {
        this.loading = false
        this.dataSource = res.rows
        const pagination = { ...this.pagination }
        pagination.total = res.total
        this.pagination = pagination
      }
    })
  },
  methods: {
    //查询
    searchQuery() {
      this.loading = true
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      getIndexManageList({
        pageNo: 1,
        pageSize: 10,
        USERID: userId,
        INDEX_NAME: this.queryParam.name,
        INDEX_TYPE: this.queryParam.type,
        INDEX_DIMNSN: this.queryParam.INDEX_DIMNSN,
        INDEX_PERIOD: this.queryParam.INDEX_PERIOD
      }).then(res => {
        if (res.result == 'success') {
          this.loading = false
          this.dataSource = res.rows
        }
      })
    },
    handleTableChange(pagination, filters, sorter) {
      this.loading = true
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      this.queryParam.pageNo = pagination.current
      this.queryParam.pageSize = pagination.pageSize
      getIndexManageList({
        pageNo: this.queryParam.pageNo,
        pageSize: this.queryParam.pageSize,
        USERID: userId,
        INDEX_NAME: this.queryParam.name,
        INDEX_TYPE: this.queryParam.type,
        INDEX_DIMNSN: this.queryParam.INDEX_DIMNSN,
        INDEX_PERIOD: this.queryParam.INDEX_PERIOD
      }).then(res => {
        if (res.result == 'success') {
          this.loading = false
          this.dataSource = res.rows
        }
      })
    },
    searchReset() {
      this.queryParam = {}
      this.searchQuery()
    },
    add() {
      this.$router.push({ path: '/statistics/checkLibrary' })
    },
    handleProcess(record) {
      //this.$router.push({ path: '/statistics/editLibrary', query: record })
      this.$router.push({
            name: 'statistics-editLibrary',
            params: { result: record }
          })
    },
    handlDelete(record) {
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      deleteMineIndex({
        MODIFY_USERID: userId,
        INDEX_ID: record.INDEX_ID,
        INDEX_CORRE_TABLE: record.INDEX_CORRE_TABLE
      }).then(res => {
        if (res.result == 'success') {
          //this.dataSource2 = res.rows;
          this.searchQuery()
          this.$message.success(res.msg)
        }
      })
    },
    handleRun(record) {
      historyRunBatch({ INDEX_ID: record.INDEX_ID, INDEX_PERIOD: record.INDEX_PERIOD }).then(res => {
        if (res.result === 'success') {
          this.$message.success(res.msg)
          this.searchQuery()
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
.search2 .ant-col-5 {
  width: 20.83333333% !important;
}
#tables .ant-table-tbody > tr td:first-child {
  text-align: left !important;
}
</style>