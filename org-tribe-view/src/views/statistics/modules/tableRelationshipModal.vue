<template>
  <!--表间关系-->
  <a-modal
    title="表间关系"
    :maskClosable="false"
    v-model="visibleModal"
    width="65%"
    @ok="handleOk"
    :confirmLoading="confirmLoading"
  >
    <div id="tableRelationshipModal">
      <div>请从下列数据表中选择一张作为主表</div>
      <a-radio-group v-model="value" @change="e => {ArrayData[0].table1 = e.target.value;}">
        <a-radio :style="radioStyle" :value="item.key" v-for="(item,i) in listData" :key="i">{{item.title}}</a-radio>
      </a-radio-group>
    </div>
    <div style="display: flex;justify-content: space-between;padding:10px 15px;" v-if="value">
      设置关联条件
      <a-button type="primary" @click="onAdd()" size="small">新增</a-button>
    </div>
    <div class="related" v-for="(item,index) in ArrayData" v-if="value">
      <a-button type="danger" @click="onDelTable(index)" v-if="index !== 0"
                style="position: absolute;right: -10px;top:-15px;" shape="circle" size="small"></a-button>
      <div>
        <a-select style="width: 200px" disabled v-if="index === 0" v-model="item.table1">
          <a-select-option :value="item.key" v-for="(item,i) in listData" :key="i">{{item.title}}</a-select-option>
        </a-select>
        <a-select style="width: 200px" v-else v-model="item.table1" @change="onChange1(index)">
          <a-select-option :value="item.key" v-for="(item,i) in listData" :key="i">{{item.title}}</a-select-option>
        </a-select>
        <span>关联</span>
        <a-select style="width: 200px" v-model="item.table2" @change="onChange2(index)">
          <a-select-option :value="item.key" v-for="(item,i) in listData" :key="i">{{item.title}}</a-select-option>
        </a-select>
      </div>
      <span>关联条件：</span>
      <div class="relationship">
        <div v-for="(item1,index1) in item.listData">
          <a-select style="width: 200px" v-model="item1.field1" v-if="item.table1">
            <a-select-option v-if="item.table1" :value="item2.FIELD_SIGN.split('▲')[0]" v-for="(item2,index2) in listData[listData.findIndex(function(item3) {
                return item3.key === item.table1;
            })].columnList" :key="index2">{{item2.FIELD_NAME}}
            </a-select-option>
          </a-select>
          <a-select style="width: 200px" v-else></a-select>
          <span>=</span>
          <a-select style="width: 200px" v-model="item1.field2" v-if="item.table2">
            <a-select-option :value="item2.FIELD_SIGN.split('▲')[0]" v-for="(item2,index2) in listData[listData.findIndex(function(item3) {
                return item3.key === item.table2;
            })].columnList" :key="index2">{{item2.FIELD_NAME}}
            </a-select-option>
          </a-select>
          <a-select style="width: 200px" v-else></a-select>
          <a-button type="link" @click="onDelCondition(index,index1)" v-if="index1 !== 0">删除</a-button>
        </div>
      </div>
      <a-button type="link" @click="onAddTo(index)">添加</a-button>
    </div>
  </a-modal>
</template>

<script>
  export default {
    name: "tableRelationshipModal",
    data() {
      return {
        confirmLoading: false,
        visibleModal: false,
        value: '',
        radioStyle: {
          display: 'block',
          height: '30px',
          lineHeight: '30px',
        },
        listData: [],
        ArrayData: [{listData: [{}]}]
      }
    },
    methods: {
      /*新增*/
      onAdd() {
        this.ArrayData.push({listData: [{}]});
      },
      /*添加*/
      onAddTo(index) {
        this.ArrayData[index].listData.push({});
      },
      /*删除条件*/
      onDelCondition(index, index1) {
        this.ArrayData[index].listData.splice(index1, 1);
      },
      /*删除表关系*/
      onDelTable(index) {
        this.ArrayData.splice(index, 1);
      },
      /*表一change*/
      onChange1(index) {
        this.ArrayData[index].listData.map(item => {
          this.$set(item, 'field1', '');
        });
      },
      /*表二change*/
      onChange2(index) {
        this.ArrayData[index].listData.map(item => {
          this.$set(item, 'field2', '');
        });
      },
      handleOk() {

      }
    }
  }
</script>

<style scoped>
  #tableRelationshipModal {
    border: 1px solid rgba(215, 215, 215, 1);
  }

  #tableRelationshipModal > div:nth-child(1) {
    padding: 10px 15px;
    background: #f0f2f5;
    border-bottom: 1px solid rgba(215, 215, 215, 1);
  }

  .related {
    border: 1px solid rgba(215, 215, 215, 1);
    padding: 15px;
    margin-bottom: 20px;
    position: relative;
  }

  .related {
    display: flex;
    justify-content: space-between;
  }

  .related > div span {
    display: inline-block;
    padding: 0 15px;
  }

  .relationship {
    width: 536px;
    padding: 15px 15px 0 15px;
    border: 1px solid rgba(215, 215, 215, 1);

  }

  .relationship > div {
    padding-bottom: 15px;

  }

  .ant-radio-group {
    width: 100%;
  }

  .ant-radio-wrapper {
    padding: 5px 15px;
    border-bottom: 1px solid rgba(215, 215, 215, 1);
    height: auto !important;
    margin-right: 0;
  }

  .ant-radio-wrapper:last-child {
    border-bottom: 0;
  }
</style>