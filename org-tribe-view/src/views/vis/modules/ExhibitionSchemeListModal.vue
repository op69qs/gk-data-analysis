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

        <a-spin :spinning="confirmLoading" tip="正在保存方案，请稍候...">
            <a-alert v-if="loadError" :message="loadError" type="error" show-icon style="margin-bottom: 16px" />
            <a-button v-if="loadError" @click="retryLoad" icon="reload">重新加载</a-button>
            <a-alert v-if="hasUnavailablePages" message="方案包含已删除的页面，已保留原有顺序；可在选择框中移除这些页面。"
                     type="warning" show-icon style="margin-bottom: 16px" />
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
                              :loading="optionsLoading" :disabled="optionsLoading || !!loadError"
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
                            <div v-for="item in page" :key="item.id" class="d_w">
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
                optionsLoading: false,
                loadError: '',
                dialogRequest: 0,
                form: this.$form.createForm(this),
                validatorRules: {
                    name: {
                        rules: [
                            {required: true, message: '请输入方案名称!'},
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
        computed: {
            hasUnavailablePages() {
                return this.page.some(item => item.unavailable);
            }
        },
        filters: {
            handleText(value) {
                if (!value) return ''
                if (value.length > 5) {
                    return value.slice(0, 5) + '...'
                }
                return value
            }
        },
        methods: {
            ...mapGetters(["userInfo"]),
            add() {
                return this.edit({});
            },
            async edit(record) {
                const request = ++this.dialogRequest;
                this.confirmLoading = false;
                this.optionsLoading = true;
                this.loadError = '';
                this.pageOptions = [];
                this.page = [];
                this.form.resetFields();
                this.model = Object.assign({}, record);
                this.model.rotation_interval = parseInt(record.rotation_interval, 10) || 5000;
                this.validatorRules.rotation_interval.initialValue = this.model.rotation_interval;
                this.visible = true;
                await this.$nextTick();
                if (request !== this.dialogRequest) return;
                this.form.setFieldsValue(pick(this.model, 'name'));
                try {
                    const [options, relations] = await Promise.all([
                        this.getListData(),
                        record.id ? getSchemeRel({scheme_id: record.id}) : Promise.resolve({result: 'success', rows: []})
                    ]);
                    if (request !== this.dialogRequest) return;
                    if (!relations || relations.result !== 'success' || !Array.isArray(relations.rows)) {
                        throw new Error('方案页面关系加载失败');
                    }
                    const missing = relations.rows.filter(item => !options.some(page => page.id === item.page_id));
                    const restored = await Promise.all(missing.map(async item => {
                        const res = await getPageList({id: item.page_id, pageNo: 1, pageSize: 1});
                        if (!res || res.result !== 'success' || !Array.isArray(res.rows)) {
                            throw new Error('关联页面加载失败');
                        }
                        return res.rows.find(page => page.id === item.page_id) || {
                            id: item.page_id,
                            name: `页面已删除（${String(item.page_id).slice(0, 8)}）`,
                            thumbnail: item.thumbnail,
                            unavailable: true
                        };
                    }));
                    if (request !== this.dialogRequest) return;
                    this.pageOptions = options.concat(restored);
                    this.model.level = relations.rows.map(item => item.page_id);
                    this.handleChange(this.model.level);
                    this.form.setFieldsValue(pick(this.model, 'level'));
                } catch (error) {
                    if (request !== this.dialogRequest) return;
                    this.loadError = '方案页面加载失败，请重新加载后再保存';
                    this.$message.error(this.loadError);
                } finally {
                    if (request === this.dialogRequest) this.optionsLoading = false;
                }
            },
            retryLoad() {
                return this.edit({...this.model, name: this.form.getFieldValue('name')});
            },
            close() {
                this.dialogRequest++;
                this.confirmLoading = false;
                this.optionsLoading = false;
                this.$emit('close');
                this.visible = false;
            },
            handleOk() {
                if (this.confirmLoading || this.optionsLoading) return;
                const request = this.dialogRequest;
                if (this.loadError) {
                    this.$message.warning(this.loadError);
                    return;
                }
                // 触发表单验证
                return this.form.validateFields(async (err, values) => {
                    if (!err && !this.confirmLoading && request === this.dialogRequest) {
                        if (!values.level || this.page.length !== values.level.length) {
                            this.$message.warning('所选页面已不可用，请重新选择');
                            return;
                        }
                        this.confirmLoading = true;
                        let formData = Object.assign({}, this.model, values);
                        delete formData.level
                        formData.add_user = this.userInfo().id; // 创建人id
                        formData.schemeRel = []
                        this.page.forEach((item,index)=>{
                            let source = {
                                page_id: item.id, //所选中的页面id
                                thumbnail: item.thumbnail, // 选中页面的缩略图
                                sort: index, //排序
                            }
                            formData.schemeRel.push(source)
                        })
                        try {
                            const res = await (this.model.id ? editSchemeInfo(formData) : addSchemeInfo(formData));
                            if (request !== this.dialogRequest) return;
                            if (res && res.result === 'success') {
                                this.$message.success(res.msg);
                                this.$emit('ok');
                                this.close();
                            } else {
                                this.$message.warning((res && res.msg) || '方案保存失败，请重试');
                            }
                        } catch (error) {
                            if (request === this.dialogRequest) this.$message.error('方案保存失败，请重试');
                        } finally {
                            if (request === this.dialogRequest) this.confirmLoading = false;
                        }
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
            async getListData () {
                const options = [];
                let pageNo = 1;
                while (true) {
                    const res = await getPageList({...this.postData, pageNo});
                    if (!res || res.result !== 'success' || !Array.isArray(res.rows)) {
                        throw new Error('展示页面加载失败');
                    }
                    options.push(...res.rows);
                    if (options.length >= Number(res.total)) return options;
                    if (!res.rows.length) throw new Error('展示页面分页数据不完整');
                    pageNo++;
                }
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
