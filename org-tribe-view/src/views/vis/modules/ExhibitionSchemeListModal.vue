<template>
    <a-modal
            :title="title"
            width="60%"
            :visible="visible"
            
            @ok="handleOk"
            @cancel="handleCancel"
            cancelText="关闭"
            wrapClassName="ant-modal-cust-warp"
            style="top:5%;height: 85%;overflow-y: hidden">

        <a-spin :spinning="confirmLoading">
            <a-form :form="form">

                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="方案名称">
                    <a-input placeholder="请输入方案名称" v-decorator.trim="[ 'name', validatorRules.name]" autocomplete="off"/>
                </a-form-item>

                <a-form-item
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="选择展示页面">
                    <a-select placeholder="选择展示页面" v-decorator="[ 'level', validatorRules.level]" mode="multiple"
                              :maxTagTextLength="6" :maxTagCount="3" @change="handleChange">
                        <a-select-option v-for="d in pageOptions" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item
                        v-if="form.getFieldValue('level') && form.getFieldValue('level').length > 1"
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="选择切换顺序">
                    <draggable v-model="page" handle=".i_s" animation="100">
                        <div v-for="item in page" class="d_w">
                            <img :src="item.thumbnail" :alt="item.name"
                                 :key="item.id" class="i_s"/>
                            <div>{{item.name | handleText}}</div>
                        </div>
                    </draggable>


                </a-form-item>

                <a-form-item
                        v-if="form.getFieldValue('level') && form.getFieldValue('level').length > 1"
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        label="页面切换间隔">
                    <a-input-number
                            style="width: 100%"
                            v-decorator.trim="[ 'rotation_interval', validatorRules.rotation_interval]"
                            :min="5000"
                            :formatter="value => `${value}毫秒`"
                            :parser="value => value.replace('毫秒', '')"
                    />
                </a-form-item>

            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
    import pick from 'lodash.pick'
    import Draggable from 'vuedraggable'
    import { addSchemeInfo, editSchemeInfo, getPageList, getSchemeRel } from '@/api/visScreen'
    import {mapGetters} from "vuex"
    export default {
        name: "ExhibitionSchemeListModal",
        data() {
            return {
                title: "操作",
                visible: false,
                roleDisabled: false,
                model: {},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16},
                },
                confirmLoading: false,
                form: this.$form.createForm(this),
                validatorRules: {
                    name: {
                        rules: [
                            {required: true, message: '请输入角色名称!'},
                            {min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
                        ]
                    },
                    level: {
                        rules: [
                            {required: true, message: '请选择界面!'}
                        ]
                    },
                    rotation_interval: {
                        initialValue: '5000',
                        rules: [
                            {required: true, message: '请输入页面切换间隔!'}
                        ]
                    }
                },
                pageOptions:[],
                page: [],
                postData: {
                    state: 0,
                    pageNo: 1,
                    pageSize: 10
                } // 页面请求参数
            }
        },
        components: {Draggable},
        filters: {
            handleText(value) {
                if (!value) return ''
                if (value.length > 5) {
                    return value.slice(0, 5) + '...'
                }
                return value
            }
        },
        mounted(){
            this.getListData()
        },
        methods: {
            ...mapGetters(["userInfo"]),
            add() {
                this.edit({});
            },
            edit(record) {
                this.confirmLoading = true;
                this.form.resetFields();
                this.model = Object.assign({}, record);
                if(record.rotation_interval!=null){
                    this.validatorRules.rotation_interval.initialValue = record.rotation_interval
                }
                this.model.rotation_interval = parseInt(this.model.rotation_interval)
                this.visible = true;
                //编辑页面获取回显值
                if(this.model.id){
                    getSchemeRel({scheme_id:this.model.id}).then((res)=>{
                        this.confirmLoading = false;
                        if(res.result='success') {
                            let data = res.rows;
                            let level = [];
                            data.forEach((item)=>{
                                level.push(item.page_id)
                            })
                            this.model.level = level;
                            this.page = []
                            // this.handleChange(this.model.level);
                            level.forEach((item)=>{
                                this.pageOptions.forEach((jitem)=>{
                                    if(item == jitem.id) {
                                        this.page.push(jitem)
                                    }
                                })
                            })
                            // this.pageOptions.filter(item => e.indexOf(item.id) !== -1)
                            this.form.setFieldsValue(pick(this.model, 'name', 'level'))
                        }
                    })
                }
                // if (this.model.id) {
                //     this.roleDisabled = true;
                // } else {
                //     this.roleDisabled = false;
                // }
                // this.$nextTick(() => {
                //     // this.form.setFieldsValue(pick(this.model, 'name', 'status', 'id'))
                //     console.log(this.model.level)
                //     this.form.setFieldsValue(pick(this.model, 'name', 'level'))
                // });

            },
            close() {
                this.$emit('close');
                this.visible = false;
            },
            handleOk() {
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        that.confirmLoading = true;
                        let formData = Object.assign(this.model, values);
                        delete formData.level
                        // that.confirmLoading = false;
                        formData.add_user = this.userInfo().id; // 创建人id
                        formData.schemeRel = []
                        that.page.forEach((item,index)=>{
                            let source = {
                                page_id: item.id, //所选中的页面id
                                thumbnail: item.thumbnail, // 选中页面的缩略图
                                sort: index, //排序
                            }
                            formData.schemeRel.push(source)
                        })
                        let obj;
                        if (!this.model.id) {
                            obj = addSchemeInfo(formData);
                        } else {
                            formData.scheme_id = this.model.id
                            obj = editSchemeInfo(formData);
                        }
                        obj.then((res) => {
                            if (res.result=='success') {
                                that.$message.success(res.msg);
                                that.$emit('ok');
                            } else {
                                that.$message.warning(res.msg);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                            that.close();
                        })
                        this.handleCancel();
                    }
                })
            },
            handleCancel() {
                this.close()
            },
            //展示界面change
            handleChange(e) {
                let levelData = e;
                let pageData = []
                levelData.forEach((item)=>{
                    this.pageOptions.forEach((jitem)=>{
                        if(item == jitem.id) {
                            pageData.push(jitem)
                        }
                    })
                })
                this.page = pageData
                // let pageData = this.pageOptions.filter(item => e.indexOf(item.id) !== -1);
                // this.page = pageData.reverse();
                
            },
            getListData () {
                let postData = this.postData
                getPageList(postData).then(res => {
                   if(res.result=='success') {
                       if(this.postData.pageNo == 1) {
                            this.pageOptions = res.rows;
                        }else {
                            this.pageOptions.push(...res.rows);
                        }
                        if(res.total > this.pageOptions.length) { // 当展示页面数据未全部获取时再次自动请求
                            this.postData.pageNo ++
                            this.getListData()
                        }
                   }else{
                        this.pageOptions = []
                   }
                })
            }

        }
    }
</script>

<style scoped>
    .d_w {
        width: 25%;
        display: inline-block;
        margin-right: 20px;
    }

    .d_w > div {
        text-align: center;
    }

    .i_s {
        width: 100%;
        height: 7vw;
    }

    .i_s:last-child {
        margin-right: 0;
    }
</style>
