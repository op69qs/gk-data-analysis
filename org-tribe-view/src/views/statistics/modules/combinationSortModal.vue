<template>
  <!--组合排序-->
  <a-modal
    title="组合排序"
    :maskClosable="false"
    v-model="visibleModal"
    width="70%"
    @ok="handleOk"
    @cancel="handleCancel"
    :confirmLoading="confirmLoading"
  >
    <a-collapse accordion activeKey="0">
      <a-collapse-panel :header="item.title" :key="i" v-for="(item,i) in listData">
        <div style="padding-bottom: 10px;">
          <a-button type="primary" @click="onAdd(i)">新增</a-button>
          <a-button @click="onClear(i)">清空</a-button>
        </div>
        <a-table
          size="middle"
          bordered
          :rowKey="(record, i) => i"
          :columns="columns"
          :dataSource="item.dataSource"
          :pagination="false"
        >
          <span slot="action" slot-scope="text, record, index">
            <a @click="handleDel(i,index)">删除</a>
          </span>
          <template slot="field" slot-scope="text, record">
            <a-select v-model="record.field" style="width: 100%;" allowClear showSearch>
              <a-select-option :value="key.FIELD_SIGN.split('▲')[0]" v-for="(key,i) in item.columnList" :key="i"
                               :disabled="key.disabled">{{key.FIELD_NAME}}
              </a-select-option>
            </a-select>
          </template>
          <template slot="logic" slot-scope="text, record">
            <a-select v-model="record.logic" style="width: 100%;" allowClear>
              <a-select-option value="ASC">升序</a-select-option>
              <a-select-option value="DESC">降序</a-select-option>
            </a-select>
          </template>
        </a-table>
      </a-collapse-panel>
    </a-collapse>
  </a-modal>
</template>

<script>
  export default {
    name: "combinationSortModal",
    data() {
      return {
        confirmLoading: false,
        visibleModal: false,
        columns: [
          {
            title: '排序字段',
            dataIndex: 'field',
            align: 'center',
            width: '30%',
            scopedSlots: {customRender: 'field'},
          }, {
            title: '排序规则',
            dataIndex: 'logic',
            align: 'center',
            width: '30%',
            scopedSlots: {customRender: 'logic'},
          }, {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: '10%',
            scopedSlots: {customRender: 'action'}
          }],
        listData: [],
        oldData: []
      }
    },
    methods: {
      /*新增*/
      onAdd(index) {
        this.setColumnDisabled(index);
        this.listData[index].dataSource.push({field: '', logic: ''});
      },
      /*清空*/
      onClear(index) {
        this.listData[index].dataSource = [];
      },
      /*删除*/
      handleDel(i, index) {
        this.listData[i].dataSource.splice(index, 1);
        this.setColumnDisabled(index);

      },
      handleOk() {
        let filterData = [], flag = true;
        // if (this.listData.length > 0 && this.listData[0].dataSource.length > 0) {
        this.listData.map(item => {
          item.dataSource.map(item1 => {
            try {
              filterData.push(`${item1.field}▲${item1.logic}`)
            } catch (e) {
              this.$message.error('请输入！');
              flag = false;
            }
          })
        });
        if (flag) {
          this.visibleModal = false;
          this.oldData = JSON.parse(JSON.stringify(this.listData));
          this.$emit('ok', filterData);
        }
        // }
      },
      handleCancel() {
        this.visibleModal = false;
        this.listData = JSON.parse(JSON.stringify(this.oldData));
      },
      //设置 column 是否禁用属性
      setColumnDisabled(index) {
        this.listData[index].columnList.map(item1 => {
          item1.disabled = false;
        });
        this.listData[index].dataSource.map(item => {
          this.listData[index].columnList.map(item1 => {
            if (item.field === item1.FIELD_SIGN) {
              item1.disabled = true;
            }
          })
        });
      }
    }
  }
</script>

<style scoped>

  .ant-btn {
    margin-right: 8px;
  }
</style>