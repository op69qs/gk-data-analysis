<template>
    <a-card :bordered="false">
        <a-tabs default-active-key="" @change="callback" >
            <a-tab-pane key="" tab="全部">
                <a-row :gutter="[48,48]">
                    <a-col :span="8" v-for="item in dataSource" :key="item.id">
                        <a-card hoverable>
                            <img
                                    v-if="item.type === 'b' || item.type === 't'"
                                    slot="cover"
                                    alt="example"
                                    :src="getCoverSrc(item)"
                                    class="i_h"
                            />
                            <div v-if="item.type==='h'" slot="cover" v-html="item.content" class="d_m_20"></div>
                            <template slot="actions" :slot-scope="{item}">
                                <div class="editTitle">
                                    <span>{{item.title}}</span>
                                    <!-- <span @click="editClick(item)">编辑</span> -->
                                    <span @click="handleEdit(item)">编辑</span>
                                </div>
                            </template>
                            <!-- <a-card-meta :title="item.title">
                                <a-icon key="edit" type="edit" />
                            </a-card-meta> -->
                        </a-card>
                    </a-col>
                </a-row>
            </a-tab-pane>
            <a-tab-pane :key="item.business_id" :tab="item.business_name" v-for="item in dataSource1">
                <a-row :gutter="[48,48]">
                    <a-col :span="8" v-for="item in dataSource" :key="item.id">
                        <a-card hoverable>
                            <img
                                    v-if="item.type === 'b' || item.type === 't'"
                                    slot="cover"
                                    alt="example"
                                    :src="getCoverSrc(item)"
                                    class="i_h"
                            />
                            <div v-if="item.type==='h'" slot="cover" v-html="item.content" class="d_m_20"></div>
                            <!-- <a-card-meta :title="item.title">
                            </a-card-meta> -->
                            <template slot="actions" :slot-scope="{item}">
                                <div class="editTitle">
                                    <span>{{item.title}}</span>
                                    <span @click="handleEdit(item)">编辑</span>
                                </div>
                            </template>
                        </a-card>
                    </a-col>
                </a-row>
            </a-tab-pane>
        </a-tabs>
        <GalleryListModal ref="modalForm" @ok="modalListOk"></GalleryListModal>
    </a-card>
</template>

<script>
    import { getBusinessTypeList, getGalleryList } from '@/api/visScreen'
    import {ListMixin} from '@/mixins/ListMixin'
    import GalleryListModal from './modules/GalleryListModal'
    import { resolveVisMediaUrl } from '@/utils/visMedia'
    export default {
        name: "GalleryList",
        mixins: [ListMixin],
        components:{ GalleryListModal },
        data() {
            return {
                datacc: [],
                dataSource1: [],
                loading: false,
                url: {
                    list: '/vis/api/gallery/getPage'
                },
                queryParams: {}
            }
        },
        created() {
            getBusinessTypeList().then(res => {
                if (res.result === 'success') {
                    this.dataSource1 = res.rows;
                }
            });
            
            window.onscroll = () => {
                this.datacc = this.dataSource;
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
                //变量windowHeight是可视区的高度
                let windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
                //变量scrollHeight是滚动条的总高度
                let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
                //滚动条到底部的条件(距底部20px时触发加载)
                if (scrollTop + windowHeight >= scrollHeight - 20 && this.dataSource.length !== this.ipagination.total) {
                    // 延时触发数据加载
                    setTimeout(() => {
                        this.ipagination.current++;
                        this.loadData();
                    }, 500);
                }
            };
        },
        methods: {
            getCoverSrc(item) {
                if (!item) {
                    return ''
                }
                if (typeof item.content === 'string' && item.content.indexOf('data:image/') === 0) {
                    return item.content
                }
                return resolveVisMediaUrl(item.content)
            },
            loadData(arg) {
                //加载数据 若传入参数1则加载第一页的内容
                if (arg === 1) {
                    this.ipagination.current = 1;
                }
                let params = this.getQueryParams();//查询条件
                this.loading = true;
                params.state=0; // 可用图库
                getGalleryList(params).then((res) => {
                    if (res.result === 'success') {
                        if(this.ipagination.current==1)this.dataSource = []
                        this.dataSource.push(...res.rows);
                        this.ipagination.total = res.total;
                    }
                    if (res.code === 510) {
                        this.$message.warning(res.msg)
                    }
                    this.loading = false;
                }).catch(err => {
                    this.loading = false;
                })
            },
            callback(key) {
                this.dataSource = [];
                this.queryParam.business_id = key;
                this.loadData(1);
            },
            modalListOk(){
                
                this.loadData(1)
            }
        },
    }
</script>

<style scoped>
    .ant-card >>> .ant-card-body {
        border-top: 1px solid #e8e8e8;

    }

    .i_h {
        width:100%;
        height: 21vw;
    }

    .d_m_20 {
        height: 21vw;
        padding: 20px 20px 0 20px;
        /* margin-bottom: 20px; */
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        display: -moz-box;
        display: box;
        -webkit-line-clamp: 14;
        -moz-line-clamp: 14;
        line-clamp: 14;
        /* ! autoprefixer: off */
        -webkit-box-orient: vertical;
        -moz-box-orient: vertical;
        box-orient: vertical;
        /* autoprefixer: on */
    }
    .editTitle{
        display: flex;
        color: #000;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        padding: 0 1rem;
    }
</style>
