<template>
  <a-modal
    :title="title+'关联人才库'"
    v-model="visible"
    width="78%"
    @ok="handleOk"
    :maskClosable="false"
  >
    <div
      :style="{width: '100%',border: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',}"
    >
      <a-spin :spinning="confirmLoading">
        <talent-pool ref="modalForms" @select="select"></talent-pool>
      </a-spin>
    </div>
  </a-modal>
</template>

<script>
import { isExistRelation } from '@/api/nationalTreasury'
import talentPool from './talentPool'

export default {
  name: 'talentModal',
  components: { talentPool },
  data() {
    return {
      drawerWidth: 900,
      title: '操作',
      visible: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      show: false,
      selectKey: '',
      selectLabel: ''
    }
  },
  methods: {
    edit() {
      this.visible = true
      //his.$refs.modalForms.show = false;
      //this.$refs.modalForms.isShow = '2';
    },
    select(key) {
      console.log(key)
      this.selectKey = key[0].UUID
      this.selectLabel = key[0].NAME
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleCancel() {
      this.close()
    },
    handleOk() {
      isExistRelation({ TALENTPOOL_ID: this.selectKey }).then(res => {
        console.log(this.selectKey)
        console.log(this.selectLabel)
        if (res.result == 'success') {
          this.visible = false
          this.$emit('userShow')
          this.$emit('user', this.selectKey)
          this.$emit('realName', this.selectLabel)
        } else {
          this.visible = true
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>