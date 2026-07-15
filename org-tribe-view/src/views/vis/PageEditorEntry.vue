<template>
  <div
    :style="{ width: '80%', border: '1px solid #e9e9e9', padding: '10px 16px', background: '#fff', margin: '0 auto' }"
  >
    <h1 class="addTitle">{{ viewTitle }}</h1>
    <a-spin :spinning="editLoading">
      <a-form :form="form" :label-col="{ span: 4 }" :wrapper-col="{ span: 17 }" @submit="handleSubmit">
        <a-form-item :label="nameLabel">
          <a-input
            v-decorator="['name', validatorRules.name]"
            placeholder="请输入名称"
            autocomplete="off"
            @change="nameChange"
          />
        </a-form-item>
        <a-form-item :label="screenLabel">
          <a-select
            v-decorator="['template', validatorRules.template]"
            placeholder="请选择界面"
            @change="handleSelectChange"
          >
            <a-select-option :value="item.id" v-for="(item, i) in selectOption" :key="i">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :label="backgroundLabel">
          <a-radio-group v-model="backgroundValue" @change="onChange">
            <a-radio :value="0"> 背景图片 </a-radio>
            <!--上传背景图片-->
            <span style="position:relative；padding-right:15px;" v-show="backgroundValue == 0">
              <a-upload @change="handleChange" :show-upload-list="false" :before-upload="beforeUpload">
                <a-button> <a-icon type="upload" /> 上传图片 </a-button>
              </a-upload>
            </span>
            <a-radio :value="1"> 背景颜色 </a-radio>
            <!-- 背景颜色选择器 -->
            <span style="position: relative" v-show="backgroundValue == 1">
              <span>默认颜色：#0d2941</span>
              <span :style="{ 'background-color': bgc }" class="colorClass" @click.stop="colorClick = !colorClick">
              </span>
              <chrome-picker v-model="colors" v-show="colorClick == true" id="colorPicker" />
            </span>
          </a-radio-group>
        </a-form-item>
        <a-form-item
          :label="templateLabel"
          v-show="modelShow"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 18, offset: 3 }"
        >
          <div class="imgView" >
            <grid-layout
              :layout="layout"
              :col-num="12"
              :row-height="30"
              :is-draggable="draggable"
              :is-resizable="resizable"
              :vertical-compact="true"
              :use-css-transforms="true"
              @layout-updated="layoutUpdatedEvent"
              :style="{
                background: backgroundValue == 0 ? '#eee' : bgc,
                backgroundImage: backgroundValue == 0 ? 'url(' + coverImgUrl + ')' : bgc,
              }"
              ref="imageWrapper"
            >
              <grid-item
                v-for="item in layout"
                :key="item.i"
                :static="false"
                :x="item.x"
                :y="item.y"
                :w="item.w"
                :h="item.h"
                :i="item.i"
              >
                <div
                  v-if="item.i == 0"
                  :class="titleImgSrc ? 'titleImgClass' : 'titleClass'"
                  @dblclick="gridClick(item)"
                  onselectstart="return false;"
                >
                  {{ titleName ? titleName : '标题名称' }}
                </div>
                <div v-else style="width: 100%; height: 100%" :class="!item.gallery_id || item.gallery_id=='null'? 'imgClass' : 'imgSelectClass'">
                  <div v-if="item.type == 'h'&&item.title&&item.title!='null'" class="imgtitle">{{ item.title }}</div>
                  <div v-if="item.type == 'h'" v-html="item.content" @dblclick="gridClick(item)" class="htmlClass"></div>
                  <div
                    v-else
                    :class="!item.gallery_id || item.gallery_id=='null'? 'defalutClass' : 'currentImgClass'"
                    @dblclick="gridClick(item)"
                    onselectstart="return false;"
                  >
                    <div v-if="item.title&&item.title!='null'" class="imgtitle">{{ item.title }}</div>
                    <img :src="item.content ? item.content : defaultImg" />
                  </div>
                </div>
              </grid-item>
            </grid-layout>
          </div>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 14, offset: 9 }" :style="{ textAlign: 'right' }">
          <a-button :style="{ marginRight: '8px' }" @click="$router.push({ path: '/vis/bigscreen/pages' })">取消</a-button>
          <a-button :style="{ marginRight: '8px' }" type="primary" html-type="submit">保存</a-button>
          <a-button type="primary" @click="handleSubmit">发布</a-button>
        </a-form-item>
      </a-form>
    </a-spin>
    <!--侧边栏-->
    <add-template-drawer ref="modalForm" @ok="getTemplateData" :defaultData="defaultData"></add-template-drawer>
    <div id="loading" v-if="addLoading">
      <p>提交中，请稍候...</p>
    </div>
    <a-modal v-model="titleModel" title="标题背景设置" ok-text="确认" cancel-text="取消" @ok="hideModal">
      <span style="margin-right: 10px">是否设置标题背景：</span>
      <a-radio-group v-model="titleValue">
        <a-radio :value="0"> 是 </a-radio>
        <a-radio :value="1"> 否 </a-radio>
      </a-radio-group>
    </a-modal>
  </div>
</template>

<script>
import pick from 'lodash.pick'
import {mapGetters} from "vuex"
import { GridLayout, GridItem } from 'vue-grid-layout'
import domtoimage from 'dom-to-image'
import AddTemplateDrawer from './modules/AddTemplateDrawer'
import { addPageInfo, editPageInfo, getPageSubAll } from '@/api/visScreen'
import { Chrome } from 'vue-color'
let defaultProps = {
  hex: '#0d2941',
  a: 1,
}
export default {
  name: 'AddTemplate',
  components: {
    GridLayout,
    GridItem,
    AddTemplateDrawer,
    'chrome-picker': Chrome,
  },
  data() {
    return {
      defaultImg: require('@/assets/default.png'), // 默认占位图
      coverImgUrl: '', // 背景照片链接
      colors: defaultProps,
      colorClick: false,
      type: '', // 新增或修改界面
      viewTitle: '新增界面',
      formLayout: 'horizontal',
      form: this.$form.createForm(this, { name: 'coordinated' }),
      titleName: null, // 模板标题名称
      modelShow: false, // 模板显示隐藏
      nameLabel: '名称',
      screenLabel: '选择界面',
      templateLabel: '模板样式',
      backgroundLabel: '页面背景',
      backgroundValue: 1, // 默认是选择图片
      titleModel: false, // 标题背景图模态框
      titleValue: 0,
      titleImgSrc: true,
      selectOption: [
        {
          name: '模板1',
          id: '1',
        },
        {
          name: '模板2',
          id: '2',
        },
        {
          name: '模板3',
          id: '3',
        },
        {
          name: '模板4',
          id: '4',
        },
        {
          name: '模板5',
          id: '5',
        },
        {
          name: '模板6',
          id: '6',
        },
        {
          name: '模板7',
          id: '7',
        },
        // {
        //   name: '模板8',
        //   id: '8',
        // },
      ],
      layout: [], // 可拖拽模板数组
      draggable: false, // 是否可拖拽
      resizable: false, // 是否可改变大小
      index: 0,
      imgUrl: '', // 模板图片base64
      defaultData: {},
      addLoading: false,
      editLoading: false, // 修改界面加载提示框
    }
  },
  computed: {
    validatorRules: function () {
      return {
        name: { rules: [{ required: true, message: '请输入名称!' }] },
        template: { rules: [{ required: true, message: '请选择界面!' }] },
      }
    },
    bgc() {
      return this.colors.hex
    },
  },
  watch: {
    // layout: {
    //     deep: true, // 深度监听设置为 true
    //     handler: function(newV,oldV){
    //         console.log('watch中：', newV)
    //     }
    // }
  },
  created() {
    let that = this
    window.addEventListener('click', this.setColorPicker, true)
    // 修改模板赋值
    this.type = this.$route.params.type;
    if (this.type == 'edit') {
      this.viewTitle = '修改界面'
      this.form.resetFields()
      this.handleEdit()
    } else {
      this.viewTitle = '新增界面'
    }
  },
  mounted() {
    // this.$refs.modalForm.visible = true;
  },
  methods: {
    ...mapGetters(["userInfo"]),
    handleEdit(){
      // console.log(this.$route.params.itemData)
      let model = Object.assign({}, this.$route.params.itemData)
      let id = this.$route.params.itemData.id;
      this.$nextTick(() => {
        // 设置初始值
        this.form.setFieldsValue(pick(model, 'name', 'template'))
        this.coverImgUrl = model.content ? model.content : ''
        this.titleImgSrc = model.title_background == 0 ? true : false
        this.backgroundValue = parseInt(model.background_type);
        defaultProps.hex = model.colour
        this.colors = defaultProps
        this.modelShow = true
        this.titleName = model.name
        
      })
      this.editLoading = true;
      getPageSubAll({page_id:id}).then(res => {
        this.editLoading = false;
        if(res.result =='success') {
          let dataList = res.rows;
          dataList.forEach((item)=>{
            item.x = parseInt(item.x);
            item.y = parseInt(item.y);
            item.w = parseInt(item.w);
            item.h = parseInt(item.h);
            item.i = parseInt(item.i);
            item.sort = parseInt(item.sort)
          })
          this.layout=dataList
        }
      })
    },
    handleSubmit(e) {
      let that = this;
      // 确认提交
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          if (this.backgroundValue == 0 && this.coverImgUrl == '') {
            this.$message.warning('请上传背景图片!')
            return false
          }
          this.addLoading = true;
          this.resizable = false;
          let postData = null;
          postData = Object.assign({}, values);
          postData.title_background = this.titleValue; //是否显示标题背景图(0是;1否)
          postData.background_type  = this.backgroundValue; // 背景类型(0 背景色;1背景图)
          postData.add_user  = this.userInfo().id; // 创建人id
          postData.content = this.coverImgUrl; // 背景图片file
          postData.colour = this.bgc; // 背景颜色
          postData.state = e.type=='submit' ? '1' : '0' // 0 表示发布1表示未发布
          let page_sub = this.layout;
          page_sub.forEach((item)=>{
              delete item.moved
          })
          postData.page_sub = page_sub; // 对象(页面包含的图库信息)
          this.toImage(postData); // 截取图片，图片截取成功发送请求
        }
      })
    },
    handleEditSubmit(editData){
      editPageInfo(editData).then(res => {
        if(res.result =='success') {
          this.$message.success(res.msg);
          this.$router.push({path: '/vis/bigscreen/pages'});
          this.clearSetting();
        }else{
          this.$message.warning(res.msg);
        }
      })
    },
    handleSelectChange(value) {
      // 选择界面下拉框
      this.$nextTick(() => {
        this.modelShow = true
        // this.titleImgSrc = false; // 切换模板title背景图设置为false
        this.layout = []
        this.layout = this.templateData(value)
      })
    },
    templateData(id) {
      // 默认模板样式
      let value = parseInt(id)
      let data1 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 4, h: 6, i: '1', content: '' },
        { x: 4, y: 2, w: 4, h: 8, i: '2', content: '' },
        { x: 8, y: 2, w: 4, h: 4, i: '3', content: '' },
        { x: 0, y: 8, w: 4, h: 6, i: '4', content: '' },
        { x: 4, y: 8, w: 4, h: 4, i: '5', content: '' },
        { x: 8, y: 6, w: 4, h: 4, i: '6', content: '' },
        { x: 8, y: 10, w: 4, h: 4, i: '7', content: '' },
      ]
      let data2 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 6, h: 8, i: '1', content: '' },
        { x: 6, y: 2, w: 6, h: 4, i: '2', content: '' },
        { x: 6, y: 10, w: 6, h: 4, i: '3', content: '' },
        { x: 0, y: 6, w: 6, h: 4, i: '4', content: '' },
        { x: 6, y: 8, w: 6, h: 4, i: '5', content: '' },
      ]
      let data3 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 3, h: 6, i: '1', content: '' },
        { x: 3, y: 2, w: 6, h: 12, i: '2', content: '' },
        { x: 9, y: 2, w: 3, h: 6, i: '3', content: '' },
        { x: 0, y: 8, w: 3, h: 6, i: '4', content: '' },
        { x: 9, y: 8, w: 3, h: 6, i: '5', content: '' },
      ]
      let data4 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 4, h: 6, i: '1', content: '' },
        { x: 4, y: 2, w: 4, h: 6, i: '2', content: '' },
        { x: 8, y: 2, w: 4, h: 6, i: '3', content: '' },
        { x: 0, y: 8, w: 4, h: 6, i: '4', content: '' },
        { x: 4, y: 8, w: 4, h: 6, i: '5', content: '' },
        { x: 8, y: 8, w: 4, h: 6, i: '6', content: '' },
      ]
      let data5 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 4, h: 4, i: '1', content: '' },
        { x: 4, y: 2, w: 4, h: 4, i: '2', content: '' },
        { x: 8, y: 2, w: 4, h: 4, i: '3', content: '' },
        { x: 0, y: 6, w: 4, h: 4, i: '4', content: '' },
        { x: 4, y: 6, w: 4, h: 4, i: '5', content: '' },
        { x: 8, y: 6, w: 4, h: 4, i: '6', content: '' },
        { x: 0, y: 10, w: 4, h: 4, i: '7', content: '' },
        { x: 4, y: 10, w: 4, h: 4, i: '8', content: '' },
        { x: 8, y: 10, w: 4, h: 4, i: '9', content: '' },
      ]
      let data6 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 6, h: 6, i: '1', content: '' },
        { x: 6, y: 2, w: 6, h: 6, i: '2', content: '' },
        { x: 0, y: 8, w: 6, h: 6, i: '3', content: '' },
        { x: 6, y: 8, w: 6, h: 6, i: '4', content: '' },
      ]
      let data7 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 12, h: 12, i: '1', content: '' },
      ]
      let data8 = [
        { x: 0, y: 0, w: 12, h: 2, i: '0', content: '' },
        { x: 0, y: 2, w: 6, h: 12, i: '1', content: '' },
        { x: 6, y: 2, w: 6, h: 4, i: '2', content: '' },
        { x: 6, y: 6, w: 6, h: 4, i: '3', content: '' },
        { x: 6, y: 10, w: 6, h: 4, i: '4', content: '' },
      ]
      let dataItem = []
      switch (value) {
        case 1:
          dataItem = { ...data1 }
          break
        case 2:
          dataItem = { ...data2 }
          break
        case 3:
          dataItem = { ...data3 }
          break
        case 4:
          dataItem = { ...data4 }
          break
        case 5:
          dataItem = { ...data5 }
          break
        case 6:
          dataItem = { ...data6 }
          break
        case 7:
          dataItem = { ...data7 }
          break
        case 8:
          dataItem = { ...data8 }
          break
        default:
          break
      }
      return Object.values(dataItem)
    },
    toImage(itemData) {
      let postData = itemData
      let that = this
      // 生成截图
      domtoimage
        // .toPng(this.$refs.imageWrapper)
        .toPng(this.$refs.imageWrapper.$el)
        .then((dataUrl) => {
          this.imgUrl = dataUrl
          this.resizable = true
          const time = setTimeout(() => {
            postData.thumbnail = that.imgUrl; // 缩略图base64
            if(that.$route.params.itemData) {
              postData.id = that.$route.params.itemData.id;
              that.handleEditSubmit(postData)
            } else {
              let obj;
              obj = addPageInfo(postData);
              obj.then((res) => {
                if (res.result =='success') {
                  that.$message.success(res.msg);
                  that.$router.push({path: '/vis/bigscreen/pages'});
                  that.clearSetting();
                } else {
                  that.$message.warning(res.msg);
                }
              }).finally(() => {
                that.addLoading = false;
              })
            }
          }, 100)
        })
        .catch((error) => {
          console.error('oops, something went wrong!', error)
        })
    },
    gridClick(item) {
      // 模板模块选择
      this.index = item.i
      if (item.i==0) {
        // 是标题显示选择背景图还是无背景图
        this.titleModel = true
      } else {
        // 是图片显示图库弹框
        this.$refs.modalForm.title = '模块设置'
        this.$refs.modalForm.visible = true
        this.$refs.modalForm.disableSubmit = false
        if (item.gallery_id != undefined && item.gallery_id != null) {
        //   this.gallery_id = item.gallery_id
          let da = {}
          da.gallery_id = item.gallery_id; // 图库id
          if(item.type == 'h') { // 为html
            da.title = item.title
            da.content = item.content
          } else {
            da.time_interval = item.time_interval; // 时间区间类型 (0:至今;1时间区间)
            da.time_type = item.time_type; // 时间类型 d日 m月 j季 y
            da.unit = item.unit;
            da.pageWhere = item.pageWhere;
            da.page_id = item.page_id;
          }         
          this.defaultData = Object.assign({}, da)
        } else {
          this.defaultData = {}
        }
      }
    },
    getTemplateData(val) {
      // 模态框回调函数
      let dataItem = val
      this.$refs.modalForm.visible = false
      this.layout.forEach((item, i) => {
        if (item.i == this.index) {
          item = Object.assign(item, dataItem)
        }
      })
      this.defaultData = {}
    },
    clearSetting() {
      // 重置数据
      this.form.resetFields()
      this.layout = []
      this.modelShow = false
    },
    layoutUpdatedEvent(newLayout) {
      // 拖拽视图更新
      // console.log("Updated layout");
      // console.log("Updated layout: ", newLayout);
    },
    hideModal() {
      // 标题背景选择框
      if (this.titleValue == 0) {
        this.titleImgSrc = true
      } else {
        this.titleImgSrc = false
      }
      this.titleModel = false
    },
    onChange(e) {
      // 点击页面背景单选框
      if (this.backgroundValue == 1) {
        this.colorClick = true
      } else {
        this.colorClick = false
      }
    },
    setColorPicker(e) {
      // 当拾色器展示时候点击其他地方进行隐藏
      let that = this
      let colorDox = document.getElementById('colorPicker')
      if (colorDox.contains(e.target)) {
        that.colorClick = true
      } else {
        that.colorClick = false
      }
    },
    handleChange(info) {
      // 上传背景图片
      this.getBase64(info.file, (imageUrl) => {
        this.coverImgUrl = imageUrl
      })
    },
    beforeUpload(file) {
      // 上传前
      var fileType = file.type
      if (fileType.indexOf('image') < 0) {
        this.$message.warning('请上传图片!')
        return false
      }
      if (file.size > 5242880) {
        this.$message.warning('上传图片的大小不能超过5M!')
        return false
      }
      return false // 设置为false 手动上传
    },
    getBase64(img, callback) {
      // file转换为base64
      const reader = new FileReader()
      reader.addEventListener('load', () => callback(reader.result))
      reader.readAsDataURL(img)
    },
    nameChange(event) {
      // 标题名称change事件
      this.titleName = event.target.value
    },
    makeFormData(obj, form_data) {
      let that = this;
      var data = []
      if (obj instanceof File) {
        data.push({ key: '', value: obj })
      } else if (obj instanceof Array) {
        for (var j = 0, len = obj.length; j < len; j++) {
          var arr = that.makeFormData(obj[j])
          for (var k = 0, l = arr.length; k < l; k++) {
            var key = !!form_data ? j + arr[k].key : '[' + j + ']' + arr[k].key
            data.push({ key: key, value: arr[k].value })
          }
        }
      } else if (typeof obj == 'object') {
        for (var j in obj) {
          var arr = that.makeFormData(obj[j])
          for (var k = 0, l = arr.length; k < l; k++) {
            var key = !!form_data ? j + arr[k].key : '[' + j + ']' + arr[k].key
            data.push({ key: key, value: arr[k].value })
          }
        }
      } else {
        data.push({ key: '', value: obj })
      }
      if (!!form_data) {
        // 封装
        for (var i = 0, len = data.length; i < len; i++) {
          form_data.append(data[i].key, data[i].value)
        }
      } else {
        return data
      }
    },
  },
  destroyed() {
    window.removeEventListener('click', this.setColorPicker, true) // 去掉监听
  },
}
</script>

<style scoped>
.addTitle {
  text-align: center;
  padding: 10px 0;
}

.vue-grid-layout {
  background: #eee;
  background-size: 100% 100% !important;
  background-repeat: no-repeat !important;
}

.imgView {
  width: 100%;
  height: 100%;
  position: relative;
}
.imgView .imgClass {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid #ccc;
}
.imgView .imgSelectClass {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  border: 0.9rem solid transparent;
  -webkit-border-image:  url('~@/assets/border.png') 20 stretch; /* Safari 3.1-5 */
  -o-border-image:  url('~@/assets/border.png') 20 stretch; /* Opera 11-12.1 */
  border-image:  url('~@/assets/border.png') 30 stretch;
  /* border-image-outset: 0px;  */
}
.imgView .defalutClass {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.imgView .currentImgClass{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
.titleClass {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
  font-weight: bold;
  color: #fff;
}
.titleImgClass {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('~@/assets/titleLogo.png');
  background-size: 100% 100%;
  font-size: 1.2rem;
  font-weight: bold;
  color: #fff;
}
.imgSelectClass img {
  width: 90%;
  height: 78%;
}
.imgClass img {
  width: 115px;
  height: 77px;
}
.vue-grid-item .resizing {
  opacity: 0.9;
}

.vue-grid-item .static {
  background: #cce;
}

.vue-grid-item .text {
  font-size: 24px;
  text-align: center;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  height: 100%;
  width: 100%;
}

.vue-grid-item .no-drag {
  height: 100%;
  width: 100%;
}

.vue-grid-item .minMax {
  font-size: 12px;
}

.vue-grid-item .add {
  cursor: pointer;
}

.vue-draggable-handle {
  position: absolute;
  width: 20px;
  height: 20px;
  top: 0;
  left: 0;
  background: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='10' height='10'><circle cx='5' cy='5' r='5' fill='#999999'/></svg>")
    no-repeat;
  background-position: bottom right;
  padding: 0 8px 8px 0;
  background-repeat: no-repeat;
  background-origin: content-box;
  box-sizing: border-box;
  cursor: pointer;
}

.vue-grid-item {
  touch-action: none;
}
#loading {
  width: 100%;
  height: 100%;
  color: #9f9f9f;
  top: 0px;
  left: 0px;
  position: fixed;
  z-index: 1999;
  text-align: center;
  background: rgba(0, 0, 0, 0.6);
}
#loading p {
  position: absolute;
  width: 100%;
  color: #fff;
  font-size: 16px;
  top: 45%;
}
.colorClass {
  position: relative;
  padding: 0 20px;
  border: 1px solid #ccc;
}
.vc-chrome {
  position: absolute;
  left: 50px;
  top: -130px;
  z-index: 99;
}
.imgtitle {
  width: 100%;
  height: 15%;
  color: #fff;
  line-height: 30px;
  padding-left: 1rem;
}
.htmlClass{
  padding:8px;
  overflow: hidden;
}
.htmlClass >>> p {
    color: #fff!important;
}
</style>
