<template>
  <!--查看方案-->
  <a-modal
    title="查看方案"
    :maskClosable="false"
    v-model="visibleModal"
    width="60%"
    :confirmLoading="confirmLoading"
    :footer="null"
    @cancel="handleCancel"
  >
    <div>关键字：
      <a-input v-model="queryParam.SCHEME_NAME" style="width: 30%;margin-bottom:15px;"></a-input>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <a-button type="primary" @click="loadData(1)">查询</a-button>
    </div>
    <a-table
      ref="table"
      size="middle"
      bordered
      rowKey="ID"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :loading="loading"
      @change="handleTableChange">
          <span slot="action" slot-scope="text, record">
            <a @click="onCarriedOut(record)">执行</a>
            <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDel(record)">
                  <a>删除</a>
                </a-popconfirm>
          </span>
    </a-table>
  </a-modal>
</template>

<script>
  import {getSchemeMainPage, delSchemeMain} from '@/api/integratedQueryApi'

  export default {
    name: "viewSchemeModal",
    data() {
      return {
        confirmLoading: false,
        visibleModal: false,
        columns: [{
          title: '方案描述',
          dataIndex: 'SCHEME_NAME'
        }, {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: '20%',
          scopedSlots: {customRender: 'action'}
        }],
        queryParam: {},
        dataSource: [],
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        loading: false
      }
    },
    methods: {
      loadData(arg) {
        this.loading = true;
        let param = Object.assign({}, this.queryParam);
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        param.CREATE_USER = this.$sessionStorage.ls.get('Login_Userinfo').id;
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        getSchemeMainPage(param).then((res) => {
          if (res) {
            this.dataSource = res.rows;
            this.ipagination.total = res.total;
          }
          if (res.code === 510) {
            this.$message.warning(res.msg)
          }
          this.loading = false;
        }).catch(err => {
          this.loading = false;
          this.$message.error('网络异常');
        });
      },
      /*分页、排序、筛选变化时触发*/
      handleTableChange(pagination, filters, sorter) {
        this.ipagination = pagination;
        this.loadData();
      },
      /*执行*/
      onCarriedOut(record) {
        this.handleCancel();
        this.$emit('ok', record);
      },
      /*删除*/
      handleDel(record) {
        this.loading = true;
        delSchemeMain({
          ID: record.ID
        }).then(res => {
          this.loading = false;
          if (res.result === 'success') {
            this.$message.success(res.msg);
            this.loadData(1);
          } else {
            this.$message.warning(res.msg);
          }
        }).catch(err => {
          this.loading = false;
        })
      },
      //关闭
      handleCancel() {
        this.visibleModal = false;
        this.dataSource = [];
        this.ipagination.total = 0;
      }
    }
  }
</script>

<style scoped>

</style>