<template>
  <a-card :bordered="false" class="card-area">
    <!-- 查询区域 -->
  <div id="first">
    <div style="display:none;">
    <a-radio-group  @change="onChange"  v-model="esql" :disabled="disabled1">
      <a-radio-button value="0">计算</a-radio-button>
      <a-radio-button value="1">SQL</a-radio-button>
    </a-radio-group>
    </div>
    <div class="table-page-search-wrapper zhibiao">
      <!-- 搜索区域 -->
      <a-form layout="inline" :form="form">
        <a-row :gutter="24">
          <a-col :md="15" :sm="15">
            <a-form-item label="指标名称" :labelCol="{span: 5}" :wrapperCol="{span: 18}" required>
              <a-input
                placeholder="请输入指标名称（注：指标名称中最好能标注科目、级次、周期等信息）"
                v-model="queryParam.INDEX_NAME"
                v-decorator="[ 'INDEX_NAME', validatorRules.INDEX_NAME]"
              ></a-input>
              <!--<a-input-number style="width: 100%" :min="1000" :max="9999" placeholder="请输入所属年度"
              v-model="queryParam.INSPECTION_PLAN_YEAR"/>-->
            </a-form-item>
          </a-col>
          <a-col :md="9" :sm="9">
            <a-form-item label="指标类型" :labelCol="{span: 5}" :wrapperCol="{span: 18}" required>
              <a-select :disabled="disabled" v-model="queryParam.INDEX_TYPE" placeholder="请选择指标类型" v-decorator="[ 'INDEX_TYPE', validatorRules.INDEX_TYPE]" allowClear>
                <a-select-option :value="d.id" v-for="d in PLAN_TYPE_OPTIONS" :key="d.id">{{d.label}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="24" :sm="24">
            <a-form-item label="指标描述" :labelCol="{span: 5}" :wrapperCol="{span: 18}" required>
              <a-textarea
                placeholder="请输入指标描述方便其他人理解指标含义（最多500个字）"
                v-model="queryParam.INDEX_DESCR"
                v-decorator="[ 'INDEX_DESCR', validatorRules.INDEX_DESCR]"
                :rows="1"
                style="width:100%;"
              ></a-textarea>
            </a-form-item>
          </a-col>
          <a-col :md="15" :sm="15">
              <a-form-item
                      label="指标父级"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18}"
                      hasFeedback
                      required
                      :validate-status="validateStatus1"
              >
                  <span slot="help">{{ validateStatus1=='error'?'请选择指标父级':'&nbsp;&nbsp;' }}</span>
                  <a-tree-select
                          style="width:100%"
                          showSearch
                          multiplea-tree-select
                          :maxTagCount="1"
                          labelInValue
                          treeNodeFilterProp="label"
                          :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                          :treeData="indexData"
                          v-model="queryParam.PARENT_ID"
                          placeholder="请选择指标父级"
                  ></a-tree-select>
              </a-form-item>
            </a-col>
          <a-col :md="15" :sm="15">
            <a-form-item
              label="维度"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 12 }"
              style="margin-left:28px;margin-top:20px;"
              required
            >
              <a-select
               :disabled="disabled"
                v-model="queryParam.INDEX_DIMNSN"
                placeholder="请选择新增指标的维度"
                allowClear
                style="width:65%;"
                v-decorator="[ 'INDEX_DIMNSN', validatorRules.INDEX_DIMNSN]"
              >
                <a-select-option
                  :value="d.id"
                  v-for="d in PLAN_DIMEN_OPTIONS"
                  :key="d.id"
                >{{d.label}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="9" :sm="9">
            <a-form-item
              label="周期"
              :labelCol="{span: 5}"
              :wrapperCol="{span: 18}"
              style="margin-left:28px;margin-top:20px;"
              required
            >
              <a-select :disabled="disabled" v-model="queryParam.INDEX_PERIOD" placeholder="请选择新增指标的周期" v-decorator="[ 'INDEX_PERIOD', validatorRules.INDEX_PERIOD]" allowClear>
                <a-select-option
                  :value="d.id"
                  v-for="d in PLAN_CYCLE_OPTIONS"
                  :key="d.id"
                >{{d.label}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <a-row v-if="show1">
        <a-col :md="24" :sm="24" style="margin-bottom:15px;">计算逻辑:</a-col>
        <a-col :md="6" :sm="6" style="border: 1px solid #ccc;height:400px;">
          <a-row style="margin-bottom:20px;">
            <a-col :md="24" :sm="24" style="border-bottom:1px solid #ccc;">
              <!-- <el-input
                placeholder="请输入指标关键字查询"
                v-model="queryParam.filterText"
                icon="circle-close"
                :on-icon-click="handleIconClick"
                class="wd228"
              ></el-input>
              <a-button type="primary" @click="getDatas" style="margin-left:20px;">搜索</a-button> -->
              <a-input-search placeholder="请输入指标关键字查询" v-model="queryParam.filterText" @change="getDatas"></a-input-search>
            </a-col>
          </a-row>
          <el-tree
            id="filter-tree"
            :data="treeData"
            :props="defaultProps"
            ref="tree"
            @node-click="handleNode"
            @node-expand="openClick"
            :expand-on-click-node = 'false'
            node-key="id"
            highlight-current
          >
            <span class="span-ellipsis" slot-scope="{ node, data }">
              <!-- <span :title="node.label.split('▲')[1]">{{ node.label.split('▲')[0] }}</span> -->
              <span  v-on:mouseover="enter($event,node)" v-on:mouseleave="leave($event,node)">{{ node.label.split('▲')[0] }}</span>
            </span>
          </el-tree>
          <div id="perTreeMenu1"  @click="getInfo" v-if="menuVisible1" class="tree_menu1" :style="{...rightMenu1}">
            <!-- <ul>
              <li @click="getInfo"><i class="el-icon-tickets"></i> 详情</li>
            </ul> -->
            {{INDEX_DESCR}}
            <a v-if="rest.INDEX_DETAILS" style="display:block;float:right;margin-top:20px" @click="getInfo">详情</a>
          </div>
          <!-- <a-tree
        @select="handleNode"
        :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
        :treeData="treeData"
        >
         <template slot="title" slot-scope="{text,record}">
          <span>111</span>
        </template> -->
      </a-tree>
        </a-col>
        <a-col :md="18" :sm="18" style="height:400px;border:1px solid #ccc;border-left:none;">
          <!-- <div class="rightDiv">
            <span style="font-size:16px;">选择运算符：</span>
            <icon-font type="icon-jia" @click="jia" />
            <icon-font type="icon-jian" @click="jian" />
            <icon-font type="icon-chenghao" @click="cheng" />
            <icon-font type="icon-chuhao" @click="chu" />
            <icon-font type="icon-zuokuohao" @click="zuokuohao" />
            <icon-font type="icon-youkuohao" @click="youkuohao" />
            <span class="clear" @click="clear">清除</span>
          </div> -->
          <a-row>
            <a-col :md="16" :sm="16">
          <div class="computed">
            <span>{{text}}</span> =
            <!-- <span v-text="typeof(this.textRule)=='string'?this.textRule:this.textRule.join('')"></span> -->
            <quill-editor 
                   id="textarea"
                   v-model="content" 
                   ref="myQuillEditor" 
                   :options="editorOption" 
                   @focus="onEditorFocus($event)"
                   @change="onContentChange($event)"
                   >
               </quill-editor>
               <div id="content2" v-html="content" style="display:none"></div>
          </div>
            </a-col>
            <a-col :md="8" :sm="8">
          <div class="rightDiv">
            <div class="rightSpan" style="font-size:16px;">选择运算符：<span class="clear" @click="clear">清除</span></div>
            <!-- <icon-font type="icon-jia" @click="jia" />
            <icon-font type="icon-jian" @click="jian" />
            <icon-font type="icon-chenghao" @click="cheng" />
            <icon-font type="icon-chuhao" @click="chu" />
            <icon-font type="icon-zuokuohao" @click="zuokuohao" />
            <icon-font type="icon-youkuohao" @click="youkuohao" />
            <span class="clear" @click="clear">清除</span> -->
            <div class="col">
            <div class="c2" @click="jia">
                <p>+</p>
            </div>
            <div class="c2" @click="number(7)">
                <p>7</p>
            </div>
            <div class="c2" @click="number(8)">
                <p>8</p>
            </div>
            <div class="c2" @click="number(9)">
                <p>9</p>
            </div>
            <div class="c2" @click="jian">
                <p>-</p>
            </div>
            <div class="c2" @click="number(4)">
                <p>4</p>
            </div>
            <div class="c2" @click="number(5)">
                <p>5</p>
            </div>
            <div class="c2" @click="number(6)">
                <p>6</p>
            </div>
            <div class="c2" @click="cheng">
                <p>×</p>
            </div>
            <div class="c2" @click="number(1)">
                <p>1</p>
            </div>
            <div class="c2" @click="number(2)">
                <p>2</p>
            </div>
            <div class="c2" @click="number(3)">
                <p>3</p>
            </div>
            <div class="c2" @click="chu">
                <p>÷</p>
            </div>
            <div class="c2" @click="number('.')">
                <p>.</p>
            </div>
            <div class="c2" @click="number(0)">
                <p>0</p>
            </div>
            <div class="c2" @click="deletes">
                <p><icon-font type="icon-tuige" /></p>
            </div>
            <div class="c2" @click="zuokuohao">
                <p>(</p>
            </div>
            <div class="c2" @click="youkuohao">
                <p>)</p>
            </div>
    </div>

          </div>
            </a-col>
          </a-row>
        </a-col>
      </a-row>
      <a-row v-if="show2">
       <a-col :md="24" :sm="24" style="margin-bottom:15px;">查询SQL:<a style="margin-left:20px;" href="#">示例</a></a-col>
       <a-col :md="24" :sm="24">SELECT</a-col>
       <a-form
        :form="form1"
        class="sql-form"
        >
       <a-form-item required>
         <a-input  v-model="ACCOUNT_PERIOD" placeholder="输入数据源表时间字段" style="width:25%;margin-right:30px;"/>
         <span>AS   ACCOUNT_PERIOD,  /*账期*/</span>
      </a-form-item>
      <a-form-item required>
         <a-input  v-model="INDEX_DIM_CODE" placeholder="输入数据源表国库或地区编码字段" style="width:25%;margin-right:30px;"/>
         <span>AS   INDEX_DIM_CODE,  /*指标维度编码，用来存储国库或者地区编码*/</span>
      </a-form-item>
      <a-form-item required>
         <a-input  v-model="INDEX_DIM_DESCR" placeholder="输入数据源表国库或地区编码字段" style="width:25%;margin-right:30px;"/>
         <span>AS   INDEX_DIM_DESCR,  /*指标维度描述，用来存储国库或者地区名称*/</span>
      </a-form-item>
      <a-form-item required>
         <a-input v-model="DIMENSION_FLAG" placeholder="输入维度标识字段" style="width:25%;margin-right:30px;"/>
         <span>AS   DIMENSION_FLAG,  /*维度标识，存储数字1或者2，1代表国库，2代表地区*/</span>
      </a-form-item>
      <a-form-item required>
         <a-input  v-model="PERIOD_FLAG" placeholder="输入周期标识字段" style="width:25%;margin-right:30px;"/>
         <span>AS   PERIOD_FLAG,  /*周期标识，存储数字1、2、3、4，1代表日指标，2代表月指标，3代表季指标，4代表年指标*/</span>
      </a-form-item>
      <a-form-item required>
         <a-input  v-model="INDEX_VALUE" placeholder="输入指标值字段" style="width:25%;margin-right:30px;"/>
         <span>AS   INDEX_VALUE   /*指标值*/</span>
      </a-form-item>
      <a-col :md="24" :sm="24" style="margin-bottom:15px;">FROM</a-col>
         <a-form-item required>
        <a-textarea  v-model="EXE_SQL" style="width:100%;" :rows="8" placeholder="请输入语法正确的SQL语句" />
        </a-form-item>
      </a-form>
      <a-form-item>
        <a-button type="primary" class="login-form-button" @click="testRun">
         试运行
        </a-button>
        <span v-if="resultShow" style="margin-left:20px;">错误信息：{{rest}}</span>
        <el-table
          :data="sqlDatas"
          border
          style="width: 100%"
          row-key="id"
          v-if="sqlShow"
        >
          <el-table-column
            v-for="(col, index) in sqlData"
            :key="index"
            :prop="col"
            :label="col"
            
            header-align="center"
            align="right"
          >
          </el-table-column>
        </el-table>
      </a-form-item>
      </a-row>
      <a-row style="margin-top:20px;">
        <a-col :md="24" :sm="24" style="margin-bottom:15px;">指标详细描述:</a-col>
        <a-textarea style="width:100%;" v-model="desc" :rows="4" placeholder="请详细描述指标的具体含义，并举例说明，（最多5000个字）"/>
      </a-row>
      <a-row style="margin-top:10px;">
        <a-col :span="6" :offset="18">
          <a-button style="float:right;margin-left: 10px;" @click="back">取消</a-button>
          <a-button type="primary" :disabled="disableds" style="float:right;" @click="handleSubmit">确定</a-button>
        </a-col>
      </a-row>

    </div>
    <a-modal title="指标详情" width="40%" :visible="visible0" @cancel="handleCancels1" @ok="handleCancels1"
             :maskClosable="false">
              <span>{{rest.INDEX_DETAILS}}</span>             
        </a-modal>
    </div>
  </a-card>
</template>

<script>
import { selectIndexRelationTree,addMineNew,updateMineIndex,pilotRunSQL,getIndexDetails,getIndexParentInfo } from '@/api/nationalTreasury'
import deepClone from '../../utils/deepClone.js'
import pick from 'lodash.pick'
import { quillEditor, Quill } from 'vue-quill-editor'
const Embed = Quill.import('blots/embed')
const BlockEmbed = Quill.import('blots/block/embed')

class myBlot extends Embed {
  static blotName = 'myblots'
  static tagName = 'b'
  static create(value) {
    const node = super.create(value)
    node.innerHTML = value.desc
    node.style.cursor = 'pointer'
    node.style.display = 'inline-block'
    node.style.lineHeight = '16px'
    node.setAttribute('id', value.desc)
    node.setAttribute('name', value.keys)
    return node
  }
}
Quill.register(myBlot)
export default {
  name: 'checkLibrary',
  components: {
       quillEditor
   },
  data() {
    return {
      content: ``,
      str: '',
      editorOption:{
          modules:{
              toolbar:[
                [],        // toggled buttons
              ]
          }
      },
      queryParam: {},
      queryParams:{},
      PLAN_TYPE_OPTIONS: [
        {
          id:'0',
          label:'数值'
        },
        {
          id:'1',
          label:'比率'
        },
      ],
      PLAN_DIMEN_OPTIONS:[
        {
          id:'1',
          label:'国库'
        },
        {
          id:'2',
          label:'地区'
        },
        /* {
          id:'2',
          label:'账期+核算主体'
        }, */
      ],
      PLAN_CYCLE_OPTIONS:[
        {
          id:'1',
          label:'日'
        },
        {
          id:'2',
          label:'月'
        },
        {
          id:'3',
          label:'季'
        },
        {
          id:'4',
          label:'年'
        }
      ],
      zhibiaodata:[],
      text: '新增指标',
      form: this.$form.createForm(this),
      form1: this.$form.createForm(this),
      validateStatus: '',
      textRule: [],
      textcode: [],
      disabled:false,
      disableds:false,
      disabled1:false,
      records:{},
      disableRun:false,
      flag:true,
      treeData: [
        /* {
          label: '一级 1',
          code:'1',
          children: [
            {
              label: '二级 1-1',
              code:'1-1',
              children: [
                {
                  label: '三级 1-1-1',
                  code:'1-1-1'
                }
              ]
            }
          ]
        },
        {
          label: '一级 2',
          code:'2',
          children: [
            {
              label: '二级 2-1',
              code:'2-1',
              children: [
                {
                  label: '三级 2-1-1',
                  code:'2-1-1',
                }
              ]
            },
            {
              label: '二级 2-2',
              code:'2-2',
              children: [
                {
                  label: '三级 2-2-1',
                  code:'2-2-1',
                }
              ]
            }
          ]
        },
        {
          label: '一级 3',
          code:'3',
          children: [
            {
              label: '二级 3-1',
              code:'3-1',
              children: [
                {
                  label: '三级 3-1-1',
                  code:'3-1-1',
                }
              ]
            },
            {
              label: '二级 3-2',
              code:'3-1',
              children: [
                {
                  label: '三级 3-2-1',
                  code:'3-2-1'
                }
              ]
            }
          ]
        } */
      ],
      filterText: '',
      defaultProps: {
        children: 'children',
        label: 'label',
        title: 'title'
      },
      show1:true,
      show2:false,
      esql:'0',
      desc:'',
      ACCOUNT_PERIOD:'',
      INDEX_DIM_CODE:'',
      INDEX_DIM_DESCR:'',
      DIMENSION_FLAG:'',
      PERIOD_FLAG:'',
      INDEX_VALUE :'',
      EXE_SQL:'',
      menuVisible1:false,
      rest:{},
      rest2:'',
      resultShow:false,
      visible0:false,
      INDEX_DESCR:'',
      timeOutLoading:0,
      blurIndex:'',
      blurendIndex:'',
      blurIndexValue:[],
      textRules:[],
      textRuless:'',
      pos:{},
      sqlData:[],
      sqlDatas:[],
      sqlShow:false,
      isError:false,
      indexData:[],
      validateStatus1:''
    }
  },
  created(){
    selectIndexRelationTree({}).then(res=>{
      if(res.result == 'success'){
         this.treeData = res.rows
      }
      console.log(res);
    })
    getIndexParentInfo({}).then(res=>{
        if(res.result === 'success'){
          this.indexData = res.rows;
        }
      })
  },
  mounted(){
    let aa = document.getElementById('textarea')
    aa.addEventListener('keypress',function(evt){
      var charCode = evt.charCode;
      if (charCode) {
          evt.preventDefault();
      }
    },false)
    if(JSON.stringify(this.$route.params) !== '{}'){
       this.records = this.$route.params.result
       this.form.resetFields();
       this.form1.resetFields();
       this.queryParam = this.records;
       this.esql = this.records.BUILD_TYPE
       this.queryParam.PARENT_ID = {
                        value: this.records.PARENT_ID,
                        label: this.records.PARENT_DSCR,
                    };
       if(this.records.HTML_STR){
         setTimeout(() => {
        const quill = this.$refs.myQuillEditor.quill
        console.log(quill)
        quill.container.querySelector('.ql-editor').innerHTML = this.records.HTML_STR
      })
       }
       this.textRule = this.records.ORIGINAL_DSCR_ARRY.split(",")
       console.log(this.textRule)
       this.textcode = this.records.INDEX_DSCR_ARRY.split(",")
       console.log(this.textcode)
       this.desc = this.records.INDEX_DETAILS
       this.disabled = true;
       this.disabled1 = true;
       let fieldsVal = pick(this.queryParam, 'INDEX_NAME', 'INDEX_TYPE', 'INDEX_DESCR', 'INDEX_DIMNSN', 'INDEX_PERIOD')
       //let fieldsVal1 = pick(this.queryParams, 'ACCOUNT_PERIOD','INDEX_DIM_CODE','INDEX_DIM_DESCR','DIMENSION_FLAG','PERIOD_FLAG','INDEX_VALUE','EXE_SQL')
       //this.textRule = this.records.INDEX_CORRE_TABLE;
       if(this.esql==='1'){
         this.ACCOUNT_PERIOD = this.records.ORIGINAL_DSCR_ARRY.split(',')[0]
         this.INDEX_DIM_CODE = this.records.ORIGINAL_DSCR_ARRY.split(',')[1]
         this.INDEX_DIM_DESCR = this.records.ORIGINAL_DSCR_ARRY.split(',')[2]
         this.DIMENSION_FLAG = this.records.ORIGINAL_DSCR_ARRY.split(',')[3]
         this.PERIOD_FLAG = this.records.ORIGINAL_DSCR_ARRY.split(',')[4]
         this.INDEX_VALUE = this.records.ORIGINAL_DSCR_ARRY.split(',')[5]
         this.EXE_SQL = this.records.INDEX_DSCR_ARRY
       }
       this.$nextTick(() => {
        this.form.setFieldsValue(fieldsVal)
        //this.form1.setFieldsValue(fieldsVal1)
      })
      this.getDatas();
    }
  },
  computed: {
      validatorRules: function () {
        return {
          INDEX_NAME: {rules: [{required: true, message: '请选择指标名称!'}]},
          INDEX_TYPE: {rules: [{required: true, message: '请选择指标类型!'}]},
          INDEX_DESCR: {rules: [{required: true, message: '请输入指标描述!'}]},
          INDEX_DIMNSN: {rules: [{required: true, message: '请选择指标维度!'}]},
         /*  CHECK_ORG_DSCR: {rules: [{required: true, message: '请输入检查行!'}]}, */
          INDEX_PERIOD: {rules: [{required: true, message: '请输入指标周期!'}]},
          ACCOUNT_PERIOD: {rules: [{required: true, message: '请选择指标账期!'}]},
          INDEX_DIM_CODE: {rules: [{required: true, message: '请选择指标编码!'}]},
          INDEX_DIM_DESCR: {rules: [{required: true, message: '请输入指标名称!'}]},
          DIMENSION_FLAG: {rules: [{required: true, message: '请选择指标标识!'}]},
          INDEX_VALUE : {rules: [{required: true, message: '请输入指标值!'}]},
          PERIOD_FLAG: {rules: [{required: true, message: '请输入指标周期标识!'}]},
          EXE_SQL: {rules: [{required: true, message: '请输入SQL!'}]}
        }
      },
      editor() {
            return this.$refs.myQuillEditor.quill;
        },
      /* validatorRules1: function () {
        return {
          ACCOUNT_PERIOD: {rules: [{required: true, message: '请选择指标账期!'}]},
          INDEX_DIM_CODE: {rules: [{required: true, message: '请选择指标编码!'}]},
          INDEX_DIM_DESCR: {rules: [{required: true, message: '请输入指标名称!'}]},
          DIMENSION_FLAG: {rules: [{required: true, message: '请选择指标标识!'}]},
          INDEX_VALUE : {rules: [{required: true, message: '请输入指标值!'}]},
          PERIOD_FLAG: {rules: [{required: true, message: '请输入指标周期标识!'}]}
        }
      } */
    },
  watch: {
    'queryParam.INDEX_NAME'(val) {
      if (val === '') {
        this.text = '新增指标'
      } else {
        this.text = val
      }
    },
    'queryParam.INDEX_PERIOD'(val) {
      if(this.queryParam.INDEX_DIMNSN!==undefined){
        this.getDatas()
      }
    },
    'queryParam.INDEX_DIMNSN'(val) {
      if(this.queryParam.INDEX_PERIOD!==undefined){
        this.getDatas()
      }
    },
    'esql'(val){
      if(val==='0'){
        this.show1 = true;
        this.show2 = false;
        this.disableds = false
      }else{
        this.show1 = false;
        this.show2 = true;
        if(this.disableRun == false){
          this.disableds = true
        }else{
          this.disableds = false
        }
      }
    },
    'disableRun'(val){
      if(val == false){
          this.disableds = true
        }else{
          this.disableds = false
        }
    },
    'EXE_SQL'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'ACCOUNT_PERIOD'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'INDEX_DIM_CODE'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'INDEX_DIM_DESCR'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'DIMENSION_FLAG'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'PERIOD_FLAG'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'INDEX_VALUE'(oldVal,newVal) {
      if(oldVal!==newVal){
        this.disableRun = false;
      }
    },
    'content'(newVal,oldVal) {
      var that = this
      let aa = this.parseDom(newVal);
      //let bb = this.parseDom(oldVal)
      let cc = aa[0].childNodes
      //let dd = bb[0].childNodes
      cc.forEach((item,i,obj)=>{
        if(item.firstChild===null){
          const quill = that.$refs.myQuillEditor.quill
          console.log(quill)
          quill.container.querySelector('.ql-editor').innerHTML = oldVal
        }
        })
      /* console.log(cc)
      console.log(dd)
      if(newVal!==oldVal){
        
      } */
    }
  },
  methods: {
    setRow(){

    },
    doLogin(){
       var regRule = /[^\u4E00-\u9FA5|\d|\a-zA-Z|\r\n\s,.?!，。？！…—&$=()-+/*{}[\]]|\s/g
       //this.review_note = this.review_note.replace(regRule, '')
    },
    onChange(e){
      if (e.target.value === '0') {
        this.show1 = true;
        this.show2 = false;
        this.form.resetFields();
        this.content = ''
        this.queryParam = {};
      }else{
        this.show1 = false;
        this.show2 = true;
        this.form.resetFields();
        this.content = ''
        this.queryParam = {};
      }
    },
    /* select(value,option){
      debugger
      if(this.queryParam.INDEX_PERIOD !== undefined){
        this.getDatas();  
      }
    },
    select1(value,option){
      if(this.queryParam.INDEX_DIMNSN !== undefined){
        this.getDatas();  
      }
    }, */
    getInfo(){
      /* getIndexDetails({INDEX_ID:this.treeId}).then(res=>{
        if(res.result == 'success'){
          this.visible0 = true;
          this.rest = res.rows;
        }
      }) */
      this.visible0 = true;
    },
    enter($event,node){
      console.log($event.currentTarget)
      this.rightMenu1 = {top:$event.pageY+'px',left:$event.pageX-10+'px'}
     this.timeOutLoading = setTimeout(()=>{
      getIndexDetails({INDEX_ID:node.data.id}).then(res=>{
        if(res.result == 'success'){
          this.INDEX_DESCR = res.rows.INDEX_DESCR;
          this.rest = res.rows;
          if(this.INDEX_DESCR!==''){
           this.menuVisible1 = true
         }
        }
      })
      const self = this
      document.onmouseover=function(ev){
        console.log(ev.target)
        if((ev.target!==document.getElementById('perTreeMenu1')&&ev.target!==document.getElementById('perTreeMenu1').getElementsByTagName('a')[0])&&ev.target!==$event.target){
          self.menuVisible1 = false
          document.onmouseover = null;
        }
      }     
      },2000)
      
    },
    leave($event,node){
     //this.menuVisible1 = false;
     clearTimeout(this.timeOutLoading);
    },
    handleCancels1(){
      this.visible0 = false;
    },
    handleIconClick() {
      //清空筛选项
      this.filterText = ''
    },
    getPos(){
      this.pos = window.getSelection().getRangeAt(0);
    },
    handleNode(data = {}) {
      if(data.children.length==0){
        this.zhibiaodata.push(data);
      this.currentSelect = data
      if (this.currentSelect.label === undefined) {
        return false
      } else {
        //var span = document.createElement('input');
        //span.setAttribute("value",this.currentSelect.label.split('▲')[0]);
        //span.setAttribute("disabled",'true');
        //this.pos.insertNode(span);
        //this.textRuless = document.getElementById('myDiv').innerText
        //this.textRule.push(document.getElementById('myDiv').innerText);
        // console.log(text);
        /* this.textRule.push(this.currentSelect.label.split('▲')[0])
        this.textcode.push(this.currentSelect.id) */

         this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: this.currentSelect.id,
                desc: this.currentSelect.label.split('▲')[0]
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
      }
      }
    },
    test($event){
	   if ( $event && $event.preventDefault ){
		   //非IE浏览器
		   $event.preventDefault();
	   } else { 
		   //IE浏览器
		   window.$event.returnValue = false;
	}
},
    number(val){
      //this.textRule.push(val)
      //this.textcode.push(val)

      this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: val,
                desc: val
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
      /* var span = document.createElement('span');
        span.innerText = val;
        this.pos.insertNode(span);
        this.textRuless = document.getElementById('myDiv').innerText
        console.log(this.textRuless) */
    },
    onEditorFocus($event){
      //$event.enable(false);
   },
   
   onContentChange(val){
     console.log(val)
     if(val){
       //debugger
       //document.addEventListener('keypress', this.checkName, false);
       
     }
   },
    handleInputBlur(e){
     this.blurIndex = e.srcElement.selectionStart;
     this.blurendIndex = e.srcElement.selectionEnd;
     let text = this.textRule.join(',')
     let result = text.substring(0, this.blurIndex)+this.blurIndexValue.join(',')+text.substring(this.blurendIndex)
    },
    jia() {
     /* this.textRule.push('+')
     this.textRules.push('+')
     this.textcode.push('+') */

     this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: '+',
                desc: '+'
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    jia() {
     /* this.textRule.push('+')
     this.textRules.push('+')
     this.textcode.push('+') */

     this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: '+',
                desc: '+'
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    jian() {
      /* this.textRule.push('-')
      this.textRules.push('-')
      this.textcode.push('-') */

      this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: '-',
                desc: '-'
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    cheng() {
      /* this.textRule.push('*')
       this.textcode.push('*') */

       this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: '*',
                desc: '*'
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    chu() {
      /* this.textRule.push('/')
       this.textcode.push('/') */

       this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: '/',
                desc: '/'
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    zuokuohao() {
      /* this.textRule.push('(')
       this.textcode.push('(') */

       this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: '(',
                desc: '('
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    youkuohao() {
      /* this.textRule.push(')')
      this.textcode.push(')') */

      this.editor.insertEmbed(this.editor.selection.savedRange.index, 'myblots', {
                keys: ')',
                desc: ')'
      })
      this.editor.setSelection(this.editor.selection.savedRange.index + 1, Quill.sources.SILENT)
    },
    deletes(){
      /* console.log(this.pos);
      this.pos.selectNode(this.pos.commonAncestorContainer)
      this.pos.deleteContents() */
      //this.textRule.splice(this.textRule.length-1,1)
      //this.textcode.splice(this.textcode.length-1,1)
      this.editor.deleteText(this.editor.selection.savedRange.index-1, 1)
      /* this.textRule = this.textRule.substr(0, this.textRule.length - 1);  
      this.textcode = this.textcode.substr(0, this.textcode.length - 1);   */
    },
    clear() {
      this.content = ''
      //this.textRule = []
      //this.textcode = []
      this.zhibiaodata = []
    },
    back() {
        window.history.go(-1)
    },
    getDatas() {
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      selectIndexRelationTree({
        dimensionFlag: this.queryParam.INDEX_DIMNSN, 
        periodFlag: this.queryParam.INDEX_PERIOD,
        pId: '', //根据父节点主键查询子节点(可选可不选)
        name: this.queryParam.filterText==undefined?'':this.queryParam.filterText.replace(/，|,/g,"%") //指标Tree关键字搜索条件
      }).then(res => {
        if (res.result == 'success') {
          console.log(this.tableData)
          this.treeData = res.rows
        }
      })
    },
    openClick(data,node,event){

    },
    testRun(){
        const that = this;
        // 触发表单验证
        this.form1.validateFields((err, values) => {
          if (!err) {
            if(this.ACCOUNT_PERIOD&&this.INDEX_DIM_CODE&&this.INDEX_DIM_DESCR&&this.DIMENSION_FLAG&&this.PERIOD_FLAG&&this.INDEX_VALUE&&this.EXE_SQL){
            let sqldata = this.ACCOUNT_PERIOD+','+this.INDEX_DIM_CODE+','+this.INDEX_DIM_DESCR
      +','+this.DIMENSION_FLAG+','+this.PERIOD_FLAG+','+this.INDEX_VALUE
            pilotRunSQL({ORIGINAL_DSCR_ARRY:sqldata,INDEX_DSCR_ARRY:this.EXE_SQL}).then(res=>{
              if(res.result === 'success'){
                this.$message.success(res.msg);
                //this.resultShow = true
                //this.rest = res.msg
                this.sqlDatas = res.rows;
                this.sqlShow = true;
                this.sqlData = [];
                for (var key in res.rows[0]) {
                  console.log(key);     //获取key值
                  this.sqlData.push(key);
              }
                this.disableRun = true;
                this.disableRun = true;
              }else{
                this.$message.error(res.msg);
                this.resultShow = true
                this.rest = res.msg
                this.disableRun = false;
              }
            })
          }else{
            this.$message.error('请完成填写表单内容！')
          }
          }else {
            this.$message.error('请按照规范输入！')
          }
        })
    },
    handleSubmit() {

      const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            if(!this.queryParam.PARENT_ID){
              this.validateStatus1 = 'error'
              return
            }else{
              this.validateStatus1 = 'success'
            }
            var fields = {};
            this.zhibiaodata.map(itm=>{
               fields[itm.label] = itm.key
          })
          if(this.esq1==='0'){
          let cc = document.getElementById('content2').getElementsByTagName('b')
            this.textRule = [];
            this.textcode = [];
            for (var i = 0; i < cc.length; i++) {
             console.log(cc[i].attributes[1].value); 
             this.textRule.push(cc[i].innerText)
             this.textcode.push(cc[i].attributes[1].value)
            }
          }
          let reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
          let rule = this.textRule.map(el=>{
          if(reg.test(el)){         
          let index = this.textRule.findIndex( item => item == el);
          console.log(isNaN(this.textRule[index+1]),isNaN(this.textRule[index-1]),reg.test(this.textRule[index+1]),reg.test(this.textRule[index-1]))
          if(!(isNaN(this.textRule[index+1]))||!(isNaN(this.textRule[index-1]))||reg.test(this.textRule[index+1])||reg.test(this.textRule[index-1])){
            console.log('报错');
            this.isError = false;
            return el= false;
          }else{
            this.isError = true;
            return el = parseInt(5);
          }
        }else{
            return el;
          }
        })
      console.log(rule.join(''))
    try  {
     //alert("Result:" + eval(rule.join('')));
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value;
      let userId = userInfo.id;
      let sqldata = this.ACCOUNT_PERIOD+','+this.INDEX_DIM_CODE+','+this.INDEX_DIM_DESCR
      +','+this.DIMENSION_FLAG+','+this.PERIOD_FLAG+','+this.INDEX_VALUE
      console.log(this.textRule.join(','),this.textcode)
      console.log(this)
      if(this.esql === '1'||this.$route.params.BUILD_TYPE==='1'){
        if(this.disableRun===false){
           this.$message.error("请先试运行你的SQL代码")
           return;
        }else{
          if(JSON.stringify(this.$route.params) !== '{}'){
         updateMineIndex({INDEX_NAME:this.queryParam.INDEX_NAME,
          INDEX_DESCR:this.queryParam.INDEX_DESCR,
          INDEX_TYPE:this.queryParam.INDEX_TYPE,
          INDEX_DIMNSN:this.queryParam.INDEX_DIMNSN,
          INDEX_PERIOD:this.queryParam.INDEX_PERIOD,
          INDEX_ID:this.$route.params.result.INDEX_ID,
          HTML_STR:this.content,
          ORIGINAL_DSCR:this.textRule.join(''),
          ORIGINAL_DSCR_ARRY:this.esql === '0'?this.textRule.join(','):sqldata,
          INDEX_DSCR_ARRY:this.esql === '0'?this.textcode.join(','):this.EXE_SQL,
          BUILD_TYPE:this.esql,
          INDEX_DSCR:this.textcode.join(''),
          INDEX_DETAILS:this.desc,
          ADD_USERID:userId,
          PARENT_ID:this.queryParam.PARENT_ID.value,}).then(res=>{
          if(res.result == 'success'){
            this.$message.success(res.msg)
             //window.history.go(-1)
             this.$router.push({ path: '/statistics/manageIndex' })
          }
      })
      }else{
      addMineNew({INDEX_NAME:this.queryParam.INDEX_NAME,
          INDEX_DESCR:this.queryParam.INDEX_DESCR,
          INDEX_TYPE:this.queryParam.INDEX_TYPE,
          INDEX_DIMNSN:this.queryParam.INDEX_DIMNSN,
          INDEX_PERIOD:this.queryParam.INDEX_PERIOD,
          ORIGINAL_DSCR:this.textRule.join(''),
          HTML_STR:this.content,
          ORIGINAL_DSCR_ARRY:this.esql === '0'?this.textRule.join(','):sqldata,
          INDEX_DSCR_ARRY:this.esql === '0'?this.textcode.join(','):this.EXE_SQL,
          BUILD_TYPE:this.esql,
          INDEX_DSCR:this.textcode.join(''),
          INDEX_DETAILS:this.desc,
          ADD_USERID:userId,
          PARENT_ID:this.queryParam.PARENT_ID.value,}).then(res=>{
          if(res.result == 'success'){
            this.$message.success(res.msg)
             //window.history.go(-1)
            this.$router.push({ path: '/statistics/manageIndex' })
          }
      })
    }
        }
      }else{
        if(this.isError===true){
      if(JSON.stringify(this.$route.params) !== '{}'){
         updateMineIndex({INDEX_NAME:this.queryParam.INDEX_NAME,
          INDEX_DESCR:this.queryParam.INDEX_DESCR,
          INDEX_TYPE:this.queryParam.INDEX_TYPE,
          INDEX_DIMNSN:this.queryParam.INDEX_DIMNSN,
          INDEX_PERIOD:this.queryParam.INDEX_PERIOD,
          INDEX_ID:this.$route.params.result.INDEX_ID,
          HTML_STR:this.content,
          ORIGINAL_DSCR:this.textRule.join(''),
          ORIGINAL_DSCR_ARRY:this.esql == '0'?this.textRule.join(','):sqldata,
          INDEX_DSCR_ARRY:this.esql == '0'?this.textcode.join(','):this.EXE_SQL,
          BUILD_TYPE:this.esql,
          INDEX_DSCR:this.textcode.join(''),
          INDEX_DETAILS:this.desc,
          ADD_USERID:userId,
          PARENT_ID:this.queryParam.PARENT_ID.value,}).then(res=>{
          if(res.result == 'success'){
            this.$message.success(res.msg)
             //window.history.go(-1)
            this.$router.push({ path: '/statistics/manageIndex' })
          }
      })
      }else{
      addMineNew({INDEX_NAME:this.queryParam.INDEX_NAME,
          INDEX_DESCR:this.queryParam.INDEX_DESCR,
          INDEX_TYPE:this.queryParam.INDEX_TYPE,
          INDEX_DIMNSN:this.queryParam.INDEX_DIMNSN,
          INDEX_PERIOD:this.queryParam.INDEX_PERIOD,
          HTML_STR:this.content,
          ORIGINAL_DSCR:this.textRule.join(''),
          ORIGINAL_DSCR_ARRY:this.esql == '0'?this.textRule.join(','):sqldata,
          INDEX_DSCR_ARRY:this.esql == '0'?this.textcode.join(','):this.EXE_SQL,
          BUILD_TYPE:this.esql,
          INDEX_DSCR:this.textcode.join(''),
          INDEX_DETAILS:this.desc,
          ADD_USERID:userId,
          PARENT_ID:this.queryParam.PARENT_ID.value,}).then(res=>{
          if(res.result == 'success'){
            this.$message.success(res.msg)
             //window.history.go(-1)
            this.$router.push({ path: '/statistics/manageIndex' })
          }
      })
    }
      }else{
        this.$message.error('公式错误');
      }
    }
     }

    catch(exception) {
     this.$message.error('公式错误');
     }
     
          } else {
            this.$message.error('请按照规范输入！')
          }
        })
      
    }
  }
}
</script>

<style>
 #first .zhibiao{
    border: 1px solid #ccc;
    padding: 18px;
    margin-top: 20px;
    height: 500px;
    overflow: scroll;
  }
  #first .ant-radio-button-wrapper{
    height:42px;
    padding:0 30px;
    line-height:42px;
  }
.zhibiao .wd228 {
  width: 68% !important;
  margin-top: 15px;
  padding-bottom: 15px;
  padding-left: 10px;
}
.zhibiao .wd228 .el-input__inner {
  height: 32px;
  line-height: 32px;
}
.zhibiao .el-tree {
  max-height: 300px;
   overflow-y: auto;
      overflow-x: hidden;
      scrollbar-width: none; 
      -ms-overflow-style: none; 
      -webkit-overflow-style: none; 
  margin-top: 10px;
}
/* .zhibiao .rightDiv .anticon {
  font-size: 24px;
  border: 1px solid #fff;
  background: #fff;
  margin-left: 10px;
  cursor: pointer;
  padding: 5px;
} */
.zhibiao .rightDiv .rightSpan{
  padding: 14px;
  background-color: rgba(228, 228, 228, 1);
}
.zhibiao .rightDiv .rightSpan .clear {
  float: right;
  font-size: 16px;
  display: inline-block;
  line-height: 24px;
  color: #1890ff;
  font-weight: normal;
  margin-right: 10px;
  cursor: pointer;
}
.zhibiao .computed {
  padding: 40px 10px;
  font-size: 18px;
  width:100%;
}

.zhibiao .sql-form{
  margin-top:70px;
}
.zhibiao .ant-select-disabled .ant-select-selection{
  background:#e6e6e6 !important;
}
.col {
    border: 1px solid #ccc;
    display: flex;      /*1*/
	  flex-wrap: wrap;    /*2*/
	  background: #fff;
	  width: 100%;
	  height: 348px;
}
 
.c2 {
    margin: 1px;
    border: 1px solid #BBB;    
    text-align: center;
    background: #fff;
	  width: 24%;
	  height: 66px;
}
.c2 p{
  font-size:20px;
  line-height:66px;
}
.c2:hover {
    background-color: #ddd;
}
#myDiv .mobezhibiao{
  width:300px;
  background:red;
  display:inline-block;
}
/* #filter-tree .el-tree-node .el-checkbox .el-checkbox__inner{
display: none !important;
}
#filter-tree .el-tree-node .is-leaf + .el-checkbox .el-checkbox__inner{
display: block !important;
}
#filter-tree .el-tree-node .el-checkbox .el-checkbox__inner{
display: inline-block;
} */

.tree_menu1{
  position: fixed;
  display: block;
  z-index: 20000;
  background-color: #fff;
  width:200px;
  padding:10px 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow:0 2px 12px 0 rgba(0,0,0,.1);
 
  ul{
    margin:0;
    padding:0;
  }
  ul li{
    list-style: none;
    margin:0;
    padding:0 30px;
    font-size: 14px;
    line-height: 30px;
    cursor: pointer;
  }
  ul li:hover{
    background-color: #ebeef5
  }
}
#textarea {
    display: inline-block;
    /* padding-top: 0px; */
    /* width:73%; */
    position: absolute;
    top: 34px;
  }

  #textarea .ql-toolbar {
    display: none;
  }
  #textarea .ql-editor{
    white-space: inherit;
    overflow-y:auto !important;
  }
  #textarea .ql-container {
    border: none;
  }
</style>