<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
    <div class="container">
      <div style="padding:30px 0;text-align: center;font-size: 30px;font-weight: bold;color:#fff;">大数据可视化平台
      </div>
      <route-view></route-view>
     
      <div style="bottom: 2%;position: absolute;left: 44%;">
        <div style="color:#f5f5f5;text-align: center;padding-bottom: 10px;padding-right: 15px">
          <span>WinXP浏览器：</span>
          <a href="/Firefox Setup 52.9.0esr-64位(WinXP).exe" style="text-decoration: underline;color:#f5f5f5">64位</a>&nbsp;&nbsp;
          <a href="/Firefox Setup 52.9.0esr-32位(WinXP).exe" style="text-decoration: underline;color:#f5f5f5">32位</a>
        </div>
        <div style="color:#f5f5f5;text-align: center;padding-bottom: 15px;padding-right: 15px">
          <span>Win7及以上浏览器：</span>
          <a href="/Firefox Setup 74.0-64位(Win7及以上).exe" style="text-decoration: underline;color:#f5f5f5">64位</a>&nbsp;&nbsp;
          <a href="/Firefox Setup 74.0-32位(Win7及以上).exe" style="text-decoration: underline;color:#f5f5f5">32位</a>
        </div>
      </div>
    </div>
     
    <!--<div style="position: fixed;bottom: 40px;left: 45.3%;color:#1890ff;">
      火狐浏览器下载：
      <a href="/Firefox Setup 74.0.exe" style="text-decoration: underline;">64位</a>&nbsp;&nbsp;
      <a href="/Firefox Setup 74.0-32.exe" style="text-decoration: underline;">32位</a>
    </div>-->
  </div>
</template>

<script>
  import RouteView from "@/components/layouts/RouteView"
  import {mixinDevice} from '@/utils/mixin.js'

  export default {
    name: "UserLayout",
    components: {RouteView},
    mixins: [mixinDevice],
    data() {
      return {}
    },
    mounted() {
      document.body.classList.add('userLayout')
    },
    beforeDestroy() {
      document.body.classList.remove('userLayout')
    },
  }
</script>

<style lang="scss" scoped>
  #userLayout.user-layout-wrapper {
    height: 100%;

    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }

    .container {
      width: 100%;
      min-height: 100%;
      background: #f0f2f5 url(~@/assets/backgroud3.png) no-repeat 50%;
      background-size: cover;
      padding: 5% 0 144px;
      position: relative;

      a {
        text-decoration: none;
      }

      .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 44px;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, .85);
            font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }

        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 260px;
        /**width: 500px;**/
        margin: 0 auto;
        /*position: absolute;
        right: 33%;
        bottom: 32%;*/
        /** background: #f5f5f5;**/
        background-color: rgba(0, 0, 0, 0.15);
        width: 450px;
        padding: 10px 30px;
        border-radius: 10px;
      }

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;

          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;

            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }

        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }
      }
    }
  }
</style>