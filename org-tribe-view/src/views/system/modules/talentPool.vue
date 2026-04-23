<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="国库" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input placeholder="请输入国库" v-model="queryParam.GUOKU_DSCR"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="姓名" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input placeholder="请输入姓名" v-model="queryParam.NAME"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="职务" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-select v-model="queryParam.DUTIES" placeholder="请选择职务" allowClear>
                <a-select-option :value="d.id" v-for="d in jobOption" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="学历" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-select v-model="queryParam.EDUCATION" placeholder="请选择学历" allowClear>
                <a-select-option :value="d.id" v-for="d in academicOption" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="专长" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-select v-model="queryParam.EXPERTISE" placeholder="请选择专长" mode="multiple" :maxTagCount="2">
                <a-select-option :value="d.id" v-for="d in expertiseOption" :key="d.id">{{d.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="年龄" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input-group compact>
                <a-input style=" width: 40%; text-align: center" v-model="queryParam.START_AGE"/>
                <a-input style=" width: 50px; border-left: 0; pointer-events: none; background-color: #fff"
                         placeholder="至" disabled/>
                <a-input style="width: 40%; text-align: center; border-left: 0" v-model="queryParam.END_AGE"/>
              </a-input-group>
            </a-form-item>
          </a-col>
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery()">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="margin-top: 5px">
      <a-button @click="handleAdd" type="primary">新增</a-button>
      <a-button type="primary" @click="handleExportXls('人才库')" v-if="show">导出</a-button>
      <a-button type="primary" @click="importVisible = true" v-if="show">导入</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="UUID"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 4000 }"
        :rowSelection="{selectedRowKeys: selectedRowKeys, type:'radio', onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete({UUID:record.UUID,STATE:1})">
                  <a>删除</a>
                </a-popconfirm>
        </span>
        <!-- 字符串超长截取省略号显示-->
        <span slot="component" slot-scope="text">
          <j-ellipsis :value="text"/>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <talent-modals ref="modalForm" @ok="modalFormOk"></talent-modals>
    <!--导入-->
    <a-modal
      title="导入"
      :maskClosable="false"
      :footer="null"
      v-model="importVisible"
      >
      1、
      <a-button type="primary" @click="downFileMethod">下载模板</a-button>
      <br/>
      <br/>
      2、
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary">导入</a-button>
      </a-upload>
    </a-modal>
  </a-card>
</template>

<script>
  import TalentModals from './TalentModals'
  import {ListMixin} from '@/mixins/ListMixin'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import {deleteAction, downFile} from '@/api/manage'
  import {getEnumTypeAll} from '@/api/nationalTreasury'

  export default {
    name: 'talentPool',
    mixins: [ListMixin],
    components: {
      TalentModals,
      JEllipsis
    },
    data() {
      return {
        description: '角色管理页面',
        // 查询条件
        queryParam: {STATE: 0},
        importVisible: false,//导入框是否可见,
        show:false,
        // 表头
        columns: [{
          title: '姓名',
          dataIndex: 'NAME',
          align: 'center'
        }, {
          title: '国库',
          align: 'center',
          dataIndex: 'GUOKU_DSCR'

        }, {
          title: '国库编码',
          align: 'center',
          dataIndex: 'GUOKU_ID'

        }, {
          title: '性别',
          dataIndex: 'SEX_DSCR',
          align: 'center'
        }, {
          title: '出生年月',
          dataIndex: 'BIRTHDAY',
          align: 'center'
        }, {
          title: '年龄',
          dataIndex: 'AGE',
          align: 'center'
        }, {
          title: '民族',
          dataIndex: 'NATION_DSCR',
          align: 'center'
        }, {
          title: '政治面貌',
          dataIndex: 'POLITICS_DSCR',
          align: 'center'
        }, {
          title: '所学专业',
          dataIndex: 'MAJOR',
          align: 'center'
        }, {
          title: '学历',
          dataIndex: 'EDUCATION_DSCR',
          align: 'center'
        }, {
          title: '职务',
          dataIndex: 'DUTIES_DSCR',
          align: 'center'
        }, {
          title: '当前工作岗位',
          dataIndex: 'CUR_POSITION',
          align: 'center'
        }, {
          title: '电话',
          dataIndex: 'PHONE'
        }, {
          title: '从事国库年限',
          dataIndex: 'WORK_LIFE'
        }, {
          title: '专长',
          dataIndex: 'EXPERTISE_DSCR'
        }, {
          title: '职称',
          dataIndex: 'TITLE_DSCR'
        }, {
          title: '是否具有执法证',
          dataIndex: 'LAWCERT_DSCR',
          align: 'center'
        }, {
          title: '执法证号',
          dataIndex: 'LAWCERT_NO',
          align: 'center'
        }, {
          title: '参加检查次数',
          dataIndex: 'CHECK_NO',
          align: 'center'
        }, {
          title: '是否担任过主查',
          dataIndex: 'CHIEF_DSCR',
          align: 'center'
        }, {
          title: '备注',
          dataIndex: 'MEMO'
        }, {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 200,
          scopedSlots: {customRender: 'action'}
        }],
        url: {
          list: '/talentpool/talentPoolController/getData',
          delete: '/talentpool/talentPoolController/editState',
          exportXlsUrl: '/talentpool/talentPoolController/exportXls',
          downFile: '/talentpool/talentPoolController/downExcel'
          // importExcelUrl: "/talentpool/talentPoolController/readExcel?curUserId=" + curUserId + "&curUserName=" + curUserName,
        },
        //职务option
        jobOption: [],
        //学历option
        academicOption: [],
        //专长option
        expertiseOption: [],
      }
    },
    created() {
      /*职务请求*/
      getEnumTypeAll(4).then(data => {
        if (data.result === 'success') {
          this.jobOption = data.rows;
        }
      });
      /*学历请求*/
      getEnumTypeAll(1).then(data => {
        if (data.result === 'success') {
          this.academicOption = data.rows;
        }
      });
      /*专长请求*/
      getEnumTypeAll(3).then(data => {
        if (data.result === 'success') {
          this.expertiseOption = data.rows;
        }
      });
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      //重置
      searchReset() {
        this.queryParam = {STATE: 0};
        this.loadData(1)
      },
      onSelectChange(selectedRowKeys, selectedRows){
          this.selectedRowKeys = selectedRowKeys;
          console.log(this.selectedRowKeys);
          if(this.selectedRowKeys!=''){
              this.$emit('select',selectedRows)
          }
      },
    }
  }
</script>

<style lang="less" scoped>
  @import '~@assets/less/common.less';
</style>