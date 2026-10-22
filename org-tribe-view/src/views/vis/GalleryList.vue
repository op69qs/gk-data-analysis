<template>
    <a-card :bordered="false">
        <a-tabs default-active-key="" @change="callback" >
            <a-tab-pane key="" tab="全部">
                <a-row :gutter="[48,48]">
                    <a-col :span="8" v-for="item in dataSource" :key="item.id">
                        <a-card hoverable>
                            <img
                                    v-if="isImageGalleryItem(item)"
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
                                    v-if="isImageGalleryItem(item)"
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
        <div v-if="loading" style="padding: 16px; text-align: center">正在加载图库，请稍候...</div>
        <a-alert v-if="loadError" :message="loadError" type="error" show-icon />
        <a-button v-if="loadError" @click="loadData(retryPage)" icon="reload">重新加载</a-button>
        <a-empty v-if="!loading && !loadError && !dataSource.length" description="暂无可用图库" />
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
                galleryRequest: 0,
                loadError: '',
                retryPage: 1,
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
            }).catch(() => this.$message.error('图库分类加载失败，请刷新重试'));
        },
        mounted() {
            window.addEventListener('scroll', this.handleScroll, true);
        },
        activated() {
            window.addEventListener('scroll', this.handleScroll, true);
        },
        deactivated() {
            window.removeEventListener('scroll', this.handleScroll, true);
        },
        beforeDestroy() {
            this.galleryRequest++;
            window.removeEventListener('scroll', this.handleScroll, true);
        },
        methods: {
            handleScroll(event) {
                if (this.loading || this.loadError || this.dataSource.length >= this.ipagination.total) return;
                const root = document.documentElement;
                const body = document.body;
                const target = event && event.target;
                const container = target && target !== document && target !== window && target !== root && target !== body
                    ? target : null;
                if (container && (!container.contains || !container.contains(this.$el))) return;
                const scrollTop = container ? container.scrollTop : root.scrollTop || body.scrollTop;
                const windowHeight = container ? container.clientHeight : root.clientHeight || body.clientHeight;
                const scrollHeight = container ? container.scrollHeight : root.scrollHeight || body.scrollHeight;
                if (scrollTop + windowHeight >= scrollHeight - 20) {
                    return this.loadData(this.ipagination.current + 1);
                }
            },
            isImageGalleryItem(item) {
                if (!item) {
                    return false
                }
                return ['p', 'b', 't', 'bar', 'line', 'pie', 'barAndLine']
                    .indexOf(item.type) !== -1
            },
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
                const pageNo = typeof arg === 'number' ? arg : this.ipagination.current;
                const request = ++this.galleryRequest;
                const params = {...this.getQueryParams(), pageNo, state: '0'};
                this.retryPage = pageNo;
                this.loadError = '';
                this.loading = true;
                return getGalleryList(params).then((res) => {
                    if (request !== this.galleryRequest) return;
                    if (!res || res.result !== 'success' || !Array.isArray(res.rows)) {
                        throw new Error('图库加载失败');
                    }
                    this.dataSource = pageNo === 1 ? res.rows : this.dataSource.concat(res.rows);
                    this.ipagination.current = pageNo;
                    this.ipagination.total = Number(res.total) || 0;
                }).catch(() => {
                    if (request === this.galleryRequest) this.loadError = '图库加载失败，请重新加载';
                }).finally(() => {
                    if (request === this.galleryRequest) this.loading = false;
                });
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
