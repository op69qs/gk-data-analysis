<template>
  <div class="logo">
    <router-link :to="{name:'dashboard'}">

      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->
      <!--<img v-if="navTheme === 'dark'" src="~@/assets/logo-white.png" alt="logo">
      <img v-else src="~@/assets/logo.svg" alt="logo">-->
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->

      <h1 v-if="showTitle">
        <span class="sys-name">大数据可视化</span>
        <span class="sys-abbr">DVP</span>
      </h1>
    </router-link>
  </div>
</template>

<script>
  import { mixin } from '@/utils/mixin.js'

  export default {
    name: 'Logo',
    mixins: [mixin],
    props: {
      title: {
        type: String,
        default: '大数据可视化',
        required: false
      },
      showTitle: {
        type: Boolean,
        default: true,
        required: false
      }
    }
  }
</script>
<style lang="scss" scoped>
  /*缩小首页布 局顶部的高度*/
  $height: 59px;

  .logo {
    a {
      display: flex;
      align-items: center;
      height: 100%;
    }

    h1 {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: center;
      margin: 0;
      line-height: 1.15;
    }

    .sys-name {
      font-size: 18px;
      font-weight: 700;
      letter-spacing: 0.04em;
      white-space: nowrap;
    }

    .sys-abbr {
      margin-top: 1px;
      font-size: 11px;
      font-weight: 400;
      letter-spacing: 0.16em;
      opacity: 0.82;
      white-space: nowrap;
    }
  }

  .sider {
    box-shadow: none !important;
    .logo {
      height: $height !important;
      line-height: normal !important;
      box-shadow: none !important;
      transition: background 300ms;

      a {
        color: white;
        &:hover {
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }

    &.light .logo {
      background-color: #1890ff;
    }
  }
</style>
