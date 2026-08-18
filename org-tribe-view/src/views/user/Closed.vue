<template>
  <div class="closed-hint">
    <div class="closed-hint-title">本系统已退出</div>
    <div class="closed-hint-desc">请手动关闭当前浏览器标签页</div>
  </div>
</template>

<script>
  export default {
    name: 'Closed'
  }
</script>

<style scoped>
  .closed-hint {
    text-align: center;
    padding: 40px 20px 80px;
    color: #f5f5f5;
  }
  .closed-hint-title {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 16px;
  }
  .closed-hint-desc {
    font-size: 16px;
  }
</style>
