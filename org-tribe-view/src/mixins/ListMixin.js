/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import {filterObj} from '@/utils/util'
import {deleteAction, postAction, downFilePost, downFile} from '@/api/manage'
import Vue from 'vue'
import {ACCESS_TOKEN} from '@/store/mutation-types'

export const ListMixin = {
  data() {
    return {
      //token header
      tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
      /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
      queryParam: {},
      /* 数据源 */
      dataSource: [],
      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '50', '100'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      isorter: {
        column: 'createTime',
        order: 'desc'
      },
      /* 筛选参数 */
      filters: {},
      /* table加载状态 */
      loading: false,
      /* table选中keys*/
      selectedRowKeys: [],
      /* table选中records*/
      selectionRows: [],
      /* 查询折叠 */
      toggleSearchStatus: false,
      /* 高级查询条件生效状态 */
      superQueryFlag: false,
      /* 高级查询条件 */
      superQueryParams: '',
      expandedRowKeys: [], //展开的行，控制属性
      rowMergeArrs: {} // 包含需要一个或多个合并项信息的对象
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData(arg) {
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      this.loading = true
      postAction(this.url.list, params)
        .then(res => {
          if (res) {
            this.dataSource = res.rows
            this.ipagination.total = res.total
          }
          if (res.code === 510) {
            this.$message.warning(res.msg)
          }
          this.loading = false
        })
        .catch(err => {
          this.loading = false
          this.$message.error('网络异常')
        })
    },
    /*下载模板*/
    downFileMethod() {
      window.open(window._CONFIG['domianURL'] + this.url.downFile + '?X-Access-Token=' + JSON.parse(localStorage['pro__Access-Token']).value)
    },
    handleSuperQuery(arg) {
      //高级查询方法
      if (!arg) {
        this.superQueryParams = ''
        this.superQueryFlag = false
      } else {
        this.superQueryFlag = true
        this.superQueryParams = JSON.stringify(arg)
      }
      this.loadData()
    },
    getQueryParams() {
      //获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
      }
      var param = Object.assign(sqp, this.queryParam)
      /* if(this.queryParam.INSPECTION_TASK_BEGINTIME){
        param.INSPECTION_TASK_ENDTIME = this.queryParam.INSPECTION_TASK_BEGINTIME[1].format('YYYY-MM-DD');
        param.INSPECTION_TASK_BEGINTIME = this.queryParam.INSPECTION_TASK_BEGINTIME[0].format('YYYY-MM-DD');
      } */
      // param.field = this.getQueryField();
      param.pageNo = this.ipagination.current
      param.pageSize = this.ipagination.pageSize
      // return filterObj(param);
      return param
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = 'id,'
      this.columns.forEach(function (value) {
        str += ',' + value.dataIndex
      })
      return str
    },

    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectionRows = selectionRows
    },
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    searchQuery() {
      this.loadData(1)
    },
    superQuery() {
      this.$refs.superQueryModal.show()
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1)
    },
    batchDel: function () {
      if (!this.url.deleteBatch) {
        this.$message.error('请设置url.deleteBatch属性!')
        return
      }
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！')
        return
      } else {
        var ids = ''
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ','
        }
        var that = this
        this.$confirm({
          title: '确认删除',
          content: '是否删除选中数据?',
          onOk: function () {
            deleteAction(that.url.deleteBatch, {ids: ids}).then(res => {
              if (res.result === 'success') {
                that.$message.success(res.msg)
                that.loadData()
                that.onClearSelected()
              } else {
                that.$message.warning(res.msg)
              }
            })
          }
        })
      }
    },
    handleDelete: function (params) {
      if (!this.url.delete) {
        this.$message.error('请设置url.delete属性!')
        return
      }
      var that = this
      postAction(that.url.delete, params).then(res => {
        if (res.result === 'success') {
          that.$message.success(res.msg)
          that.loadData()
        } else {
          that.$message.warning(res.msg)
        }
      })
    },
    handleEdit: function (record) {
      record.type = '2' //修改
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = '编辑'
      this.$refs.modalForm.disableSubmit = true
    },
    handleAdd: function (record) {
      this.$refs.modalForm.add(record)
      this.$refs.modalForm.title = '新增'
      this.$refs.modalForm.disableSubmit = false
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field
        this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      }
      this.ipagination = pagination
      this.loadData()
    },
    handleToggleSearch() {
      this.toggleSearchStatus = !this.toggleSearchStatus
    },
    modalFormOk() {
      // 新增/修改 成功时，重载列表
      this.loadData()
    },
    handleDetail: function (record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = '详情'
      this.$refs.modalForm.disableSubmit = true
    },
    /* 导出 */
    handleExportXls2() {
      let paramsStr = encodeURI(JSON.stringify(this.getQueryParams()))
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`
      window.location.href = url
    },
    //tableTree格式化数据
    getDataByResult(result) {
      return result.map(item => {
        //判断是否标记了带有子节点
        if (item['isleaf'] === '1') {
          let loadChild = {id: item.id + '_loadChild', name: 'loading...', isLoading: true}
          item.children = [loadChild]
        }
        return item
      })
    },
    //tableTree格式化数据
    getDataByResult2(result) {
      return result.map(item => {
        //判断是否标记了带有子节点
        if (item['isleaf'] === '1') {
          let loadChild = {GUOKU_ID: item.GUOKU_ID + item.CHECK_DATE, name: 'loading...', isLoading: true}
          item.children = [loadChild]
        }
        return item
      })
    },
    //点击展开图标时触发
    handleExpand(expanded, record) {
      // 判断是否是展开状态
      if (expanded) {
        this.expandedRowKeys.push(record.id)
        if (record.children.length > 0 && record.children[0].isLoading === true) {
          let params = Object.assign({}, this.queryParam) //查询条件
          params['parentId'] = record.id
          postAction(this.url.childList, params).then(res => {
            if (res.result === 'success') {
              if (res.rows && res.rows.length > 0) {
                record.children = this.getDataByResult(res.rows)
                this.dataSource = [...this.dataSource]
              } else {
                record.children = ''
                // record.isleaf = '0';
              }
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      } else {
        let keyIndex = this.expandedRowKeys.indexOf(record.id)
        if (keyIndex >= 0) {
          this.expandedRowKeys.splice(keyIndex, 1)
        }
      }
    },

    //点击展开图标时触发
    handleExpand2(expanded, record) {
      // 判断是否是展开状态
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      if (expanded) {
        this.expandedRowKeys.push(record.GUOKU_ID + record.CHECK_DATE)
        if (record.children.length > 0 && record.children[0].isLoading === true) {
          let params = Object.assign({}, this.queryParam) //查询条件
          delete params.CURRENT_GK
          params['parentId'] = record.GUOKU_ID
          params['CHECK_DATE'] = record.CHECK_DATE
          params['CURRENT_GK'] = userInfo.guokuId
          params['LEVEL'] = record.LEVEL
          postAction(this.url.list, params).then(res => {
            if (res.result === 'success') {
              if (res.rows && res.rows.length > 0) {
                record.children = this.getDataByResult2(res.rows)
                this.dataSource = [...this.dataSource]
              } else {
                record.children = ''
                record.isleaf = '0'
              }
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      } else {
        let keyIndex = this.expandedRowKeys.indexOf(record.GUOKU_ID + record.CHECK_DATE)
        if (keyIndex >= 0) {
          this.expandedRowKeys.splice(keyIndex, 1)
        }
      }
    },
    handleExportXls(fileName) {
      if (!fileName || typeof fileName != 'string') {
        fileName = '导出文件'
      }
      let param = {...this.queryParam}
      if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
        param['selections'] = this.selectedRowKeys.join(',')
      }
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      param.username = userInfo.username
      param.realname = userInfo.realname
      console.log('导出参数', param)
      downFilePost(this.url.exportXlsUrl, param).then(data => {
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
    /* 导入 */
    handleImportExcel(info) {
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList)
      }
      if (info.file.status === 'done') {
        if (info.file.response.result === 'success') {
          this.$message.success(` 文件上传成功`)
          if (this.importVisible) {
            this.importVisible = false
          }
          this.loadData()
        } else {
          this.$message.error(`${info.file.name} ${info.file.response.msg}.`)
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`文件上传失败 `)
      }
    },
    /* 图片预览 */
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','))
      }
      return window._CONFIG['imgDomainURL'] + '/' + text
    },
    /* 文件下载 */
    uploadFile(text) {
      if (!text) {
        this.$message.warning('未知的文件')
        return
      }
      if (text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','))
      }
      window.open(window._CONFIG['domianURL'] + '/sys/common/download/' + text)
    }
  }
}
