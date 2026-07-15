<template>
  <a-card :bordered="false">
    <a-row :gutter="[48, 36]">
      <a-col v-for="item in templates" :key="item.id" :span="8">
        <a-card hoverable>
          <img slot="cover" :src="item.src" :alt="item.name" class="i_wh" />
          <a-card-meta :title="item.name" />
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'TemplateList',
  data() {
    return {
      description: '这是模板管理页面',
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

<style scoped>
.ant-card >>> .ant-card-body {
  border-top: 1px solid #e8e8e8;
}

.i_wh {
  height: 300px;
}
</style>
