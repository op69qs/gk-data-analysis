<template>
  <a-card :bordered="false">
    <a-page-header title="模板管理" />
    <a-row :gutter="[24, 24]">
      <a-col v-for="item in templates" :key="item.id" :xl="8" :lg="12" :md="12" :sm="24">
        <a-card hoverable>
          <img :src="item.src" :alt="item.name" class="vis-template-list__poster" />
          <a-card-meta :title="item.name" />
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'VisTemplateList',
  data() {
    return {
      templates: [
        { id: '1', name: '模板1', src: require('@/assets/template1.png') },
        { id: '2', name: '模板2', src: require('@/assets/template2.png') },
        { id: '3', name: '模板3', src: require('@/assets/template3.png') },
        { id: '4', name: '模板4', src: require('@/assets/template4.png') },
        { id: '5', name: '模板5', src: require('@/assets/template5.png') },
        { id: '6', name: '模板6', src: require('@/assets/template6.png') },
        { id: '7', name: '模板7', src: require('@/assets/template7.png') }
      ]
    }
  }
}
</script>

<style lang="less" scoped>
.vis-template-list__poster {
  height: 300px;
  width: 100%;
  object-fit: contain;
}
</style>