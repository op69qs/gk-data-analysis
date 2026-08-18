<template>
  <a-layout class="layout" :class="[device]">

    <template v-if="layoutMode === 'sidemenu'">
      <a-drawer
        v-if="device === 'mobile'"
        :wrapClassName="'drawer-sider ' + navTheme"
        :maskClosable="false"
        placement="left"
        @close="() => this.collapsed = false"
        :closable="false"
        :visible="collapsed"
        width="200px"
      >
        <side-menu
          mode="inline"
          :menus="menus"
          @menuSelect="menuSelect"
          :theme="navTheme"
          :collapsed="false"
          :collapsible="true"></side-menu>
      </a-drawer>

      <side-menu
        v-else
        mode="inline"
        :menus="menus"
        @menuSelect="myMenuSelect"
        :theme="navTheme"
        :collapsed="collapsed"
        :collapsible="true"></side-menu>
    </template>
    <!-- 下次优化这些代码 -->
    <template v-else>
      <a-drawer
        v-if="device === 'mobile'"
        :wrapClassName="'drawer-sider ' + navTheme"
        :maskClosable="false"
        placement="left"
        @close="() => this.collapsed = false"
        :closable="false"
        :visible="collapsed"
        width="200px"
      >
        <side-menu
          mode="inline"
          :menus="menus"
          @menuSelect="menuSelect"
          :theme="navTheme"
          :collapsed="false"
          :collapsible="true"></side-menu>
      </a-drawer>
    </template>

    <a-layout
      :class="[layoutMode, `content-width-${contentWidth}`]"
      :style="{ paddingLeft: fixSiderbar && isDesktop() ? `${sidebarOpened ? 200 : 80}px` : '0' }">
      <!-- layout header -->
      <global-header
        :mode="layoutMode"
        :menus="menus"
        :theme="navTheme"
        :collapsed="collapsed"
        :device="device"
        @toggle="toggle"
      />

      <!-- layout content -->
      <a-layout-content :style="{ height: '100%', paddingTop: fixedHeader ? '59px' : '0' }">
        <slot></slot>
      </a-layout-content>

      <!-- layout footer -->
      <a-layout-footer style="padding: 0">
        <!--<global-footer/>-->
      </a-layout-footer>
    </a-layout>

    <setting-drawer></setting-drawer>
  </a-layout>
</template>

<script>
  import SideMenu from '@/components/menu/SideMenu'
  import GlobalHeader from '@/components/page/GlobalHeader'
  import GlobalFooter from '@/components/page/GlobalFooter'
  import SettingDrawer from '@/components/setting/SettingDrawer'
  import {triggerWindowResizeEvent} from '@/utils/util'
  import {mapState, mapActions} from 'vuex'
  import {mixin, mixinDevice} from '@/utils/mixin.js'

  export default {
    name: 'GlobalLayout',
    components: {
      SideMenu,
      GlobalHeader,
      GlobalFooter,
      SettingDrawer
    },
    mixins: [mixin, mixinDevice],
    data() {
      return {
        collapsed: false,
        activeMenu: {},
        menus: []
      }
    },
    computed: {
      ...mapState({
        // 主路由
        mainRouters: state => state.permission.addRouters,
        // 后台菜单
        permissionMenuList: state => state.user.permissionList
      })
    },
    watch: {
      sidebarOpened(val) {
        this.collapsed = !val
      }
    },
    created() {
      //--update-begin----author:scott---date:20190320------for:根据后台菜单配置，判断是否路由菜单字段，动态选择是否生成路由（为了支持参数URL菜单）------
      //this.menus = this.mainRouters.find((item) => item.path === '/').children;
      this.menus = this.permissionMenuList
      // 根据后台配置菜单，重新排序加载路由信息
      console.log('----加载菜单逻辑----')
      console.log(this.mainRouters)
      console.log(this.permissionMenuList)
      console.log('----navTheme------' + this.navTheme)
      //--update-end----author:scott---date:20190320------for:根据后台菜单配置，判断是否路由菜单字段，动态选择是否生成路由（为了支持参数URL菜单）------
    },
    methods: {
      ...mapActions(['setSidebar']),
      toggle() {
        this.collapsed = !this.collapsed
        this.setSidebar(!this.collapsed)
        triggerWindowResizeEvent()
      },
      menuSelect() {
        if (!this.isDesktop()) {
          this.collapsed = false
        }
      },
      //update-begin-author:taoyan date:20190430 for:动态路由title显示配置的菜单title而不是其对应路由的title
      myMenuSelect(value) {
        //此处触发动态路由被点击事件
        this.findMenuBykey(this.menus, value.key)
        this.$emit("dynamicRouterShow", value.key, this.activeMenu.meta.title)
      },
      findMenuBykey(menus, key) {
        for (let i of menus) {
          if (i.path == key) {
            this.activeMenu = {...i}
          } else if (i.children && i.children.length > 0) {
            this.findMenuBykey(i.children, key)
          }
        }
      }
      //update-end-author:taoyan date:20190430 for:动态路由title显示配置的菜单title而不是其对应路由的title
    }
  }

</script>

<style lang="scss">
  body {
    // 打开滚动条固定显示
    overflow-y: hidden !important;
    color: black !important;
    font-weight: bold;

    &.colorWeak {
      filter: invert(80%);
    }
  }

  .ant-modal, .ant-checkbox-wrapper {
    color: black !important;
  }

  .ant-modal-body {
    max-height: 550px;
    overflow: auto;
  }

  .layout {
    .topmenu,
    .top-nav-header-index {
      --portal-blue-start: #1d95f4;
      --portal-blue-end: #1d6cf4;
      --portal-ink: #071c33;
      --portal-ink-strong: #04162a;
      --portal-overlay-border: rgba(142, 197, 255, 0.18);
      --portal-overlay-shadow: 0 18px 36px rgba(4, 24, 48, 0.28);
      --portal-overlay-hover: linear-gradient(135deg, rgba(59, 166, 255, 0.2) 0%, rgba(44, 210, 255, 0.14) 100%);
      --portal-overlay-active: linear-gradient(135deg, rgba(83, 182, 255, 0.28) 0%, rgba(33, 118, 255, 0.22) 100%);
      --portal-menu-font: "Microsoft YaHei", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
      --portal-menu-text: rgba(255, 255, 255, 0.92);
      --portal-menu-hover: rgba(255, 255, 255, 0.12);
      --portal-menu-active: rgba(255, 255, 255, 0.2);
      --portal-vis-start: #2fd6ff;
      --portal-vis-end: #1677ff;
    }

    min-height: 100vh !important;
    overflow-x: hidden;

    &.mobile {

      .ant-layout-content {

        .content {
          margin: 24px 0 0;
        }
      }

      /**
       * ant-table-wrapper
       * 覆盖的表格手机模式样式，如果想修改在手机上表格最低宽度，可以在这里改动
       */
      .ant-table-wrapper {
        .ant-table-content {
          overflow-y: auto;
        }

        .ant-table-body {
          min-width: 800px;
        }
      }

      .sidemenu {
        .ant-header-fixedHeader {

          &.ant-header-side-opened, &.ant-header-side-closed {
            width: 100%
          }
        }
      }

      .topmenu {
        /* 必须为 topmenu  才能启用流式布局 */
        &.content-width-Fluid {
          .header-index-wide {
            margin-left: 0;
          }
        }
      }

      .header, .top-nav-header-index {
        .user-wrapper .action {
          padding: 0 12px;
        }
      }
    }

    &.ant-layout-has-sider {
      flex-direction: row;
    }

    .trigger {
      font-size: 22px;
      line-height: 42px;
      padding: 0 18px;
      cursor: pointer;
      transition: color 300ms, background 300ms;

      &:hover {
        background: rgba(255, 255, 255, 0.3);
      }
    }

    .topmenu {
      .ant-header-fixedHeader {
        position: fixed;
        top: 0;
        right: 0;
        z-index: 9;
        width: 100%;
        transition: width .2s;

        &.ant-header-side-opened {
          width: 100%;
        }

        &.ant-header-side-closed {
          width: 100%;
        }
      }

      /* 必须为 topmenu  才能启用流式布局 */
      &.content-width-Fluid {
        .header-index-wide {
          max-width: unset;
          margin-left: 24px;
        }

        .page-header-index-wide {
          max-width: unset;
        }
      }

    }

    .sidemenu {
      .ant-header-fixedHeader {
        position: fixed;
        top: 0;
        right: 0;
        z-index: 9;
        width: 100%;
        transition: width .2s;

        &.ant-header-side-opened {
          width: calc(100% - 200px)
        }

        &.ant-header-side-closed {
          width: calc(100% - 80px)
        }
      }
    }

    .header {
      height: 64px;
      padding: 0 12px 0 0;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
      position: relative;
    }

    .header, .top-nav-header-index {

      .user-wrapper {
        float: right;
        height: 100%;

        .action {
          cursor: pointer;
          padding: 0 14px;
          display: inline-block;
          transition: all .3s;

          height: 70%;
          line-height: 46px;

          &.action-full {
            height: 100%;
          }

          &:hover {
            background: rgba(255, 255, 255, 0.3);
          }

          .avatar {
            margin: 20px 10px 20px 0;
            color: #1890ff;
            background: hsla(0, 0%, 100%, .85);
            vertical-align: middle;
          }

          .icon {
            font-size: 16px;
            padding: 4px;
          }
        }
      }

      &.dark {
        .user-wrapper {

          .action {
            color: black;

            &:hover {
              background: rgba(0, 0, 0, 0.05);
            }
          }
        }
      }
    }

    &.mobile {
      .top-nav-header-index {

        .header-index-wide {

          .header-index-left {

            .trigger {
              color: rgba(255, 255, 255, 0.85);
              padding: 0 12px;
            }

            .logo.top-nav-header {
              text-align: center;
              width: 56px;
              line-height: 58px;
            }
          }
        }

        .user-wrapper .action .avatar {
          margin: 20px 0;
        }

        &.dark {
          .ant-menu-horizontal > .s-menu__item--vis {
            > a,
            > .ant-menu-submenu-title {
              padding-inline: 14px;
            }

            > a::after,
            > .ant-menu-submenu-title::after {
              display: none;
            }
          }
        }

        &.light {

          .header-index-wide {

            .header-index-left {
              .trigger {
                color: rgba(0, 0, 0, 0.65);
              }
            }
          }

          //
        }
      }
    }

    &.tablet {
      // overflow: hidden; text-overflow:ellipsis; white-space: nowrap;
      .top-nav-header-index {

        .header-index-wide {

          .header-index-left {
            .logo > a {
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
        }
      }

    }

    .top-nav-header-index {
      box-shadow: 0 10px 28px rgba(16, 68, 140, 0.18);
      position: relative;
      transition: background .3s, width .2s;

      .header-index-wide {
        width: 100%;
        margin: auto;
        padding: 0 20px 0 0;
        display: flex;
        height: 59px;

        .ant-menu.ant-menu-horizontal {
          border: none;
          height: 64px;
          line-height: 64px;
          background: transparent;
        }

        .header-index-left {
          flex: 1 1;
          display: flex;

          .logo.top-nav-header {
            width: 165px;
            height: 64px;
            position: relative;
            line-height: normal;
            display: flex;
            align-items: center;
            transition: all .3s;
            overflow: hidden;

            img {
              display: inline-block;
              vertical-align: middle;
              height: 32px;
            }

            h1 {
              color: #fff;
              display: flex;
              flex-direction: column;
              align-items: flex-start;
              justify-content: center;
              margin: 0;
              font-size: inherit;
              font-weight: inherit;
              line-height: 1.15;
            }
          }
        }

        .header-index-right {
          float: right;
          height: 59px;
          overflow: hidden;

          .action:hover {
            background-color: rgba(255, 255, 255, 0.12);
          }
        }
      }

      &.light {
        background-color: #fff;

        .header-index-wide {
          .header-index-left {
            .logo {
              h1 {
                color: #002140;
              }
            }
          }
        }
      }

      &.dark {
        background: linear-gradient(90deg, var(--portal-blue-start) 0%, var(--portal-blue-end) 100%);

        .user-wrapper {

          .action {
            color: white;

            &:hover {
              background: rgba(255, 255, 255, 0.3);
            }
          }
        }

        .header-index-wide .header-index-left .trigger:hover {
          background: rgba(255, 255, 255, 0.3);
        }
      }

    }

    .top-nav-header-index.dark {
      .header-index-wide {
        .header-index-left {
          .logo.top-nav-header > a {
            color: #ffffff;
          }
        }
      }

      .user-wrapper {
        .action {
          color: #ffffff;
          font-family: var(--portal-menu-font);
          font-weight: 600;
        }

        .action .avatar {
          color: var(--portal-blue-end);
          background: rgba(255, 255, 255, 0.92);
        }
      }

      .ant-menu-horizontal {
        background: transparent;
        border-bottom: none;

        > .ant-menu-item,
        > .ant-menu-submenu {
          top: 0;
          margin-top: 0;
          border-bottom: none;
          padding: 0 16px;
          margin-inline: 4px;
          font-family: var(--portal-menu-font);
          font-size: 15px;
          font-weight: 600;
          letter-spacing: 0.01em;
          color: var(--portal-menu-text);
          transition: all .25s ease;

          > a,
          > .ant-menu-submenu-title {
            color: inherit;
          }

          .anticon {
            margin-right: 8px;
            font-size: 15px;
          }

          &:hover {
            color: #ffffff;
            background: var(--portal-menu-hover);
          }
        }

        > .ant-menu-item-selected,
        > .ant-menu-submenu-selected,
        > .ant-menu-submenu-active,
        > .ant-menu-submenu-open {
          color: #ffffff;
          background: var(--portal-menu-active);
          box-shadow: inset 0 -3px 0 rgba(255, 255, 255, 0.75);
        }

        .s-menu__label {
          display: inline-flex;
          align-items: center;
          gap: 10px;
        }

        .s-menu__badge {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          min-width: 34px;
          height: 20px;
          padding: 0 8px;
          border-radius: 999px;
          background: rgba(255, 255, 255, 0.18);
          color: #eff8ff;
          font-size: 11px;
          font-weight: 700;
          letter-spacing: 0.08em;
          line-height: 20px;
          box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.14);
        }
      }
    }

    // 内容区
    .layout-content {
      margin: 24px 24px 0px;
      height: 64px;
      padding: 0 12px 0 0;
    }

  }

  .topmenu {
    .page-header-index-wide {
      margin: 0 auto;
      width: 100%;
    }
  }

  .portal-topmenu-submenu-popup.ant-menu-submenu-popup,
  .portal-topmenu-submenu-popup.ant-menu-submenu-popup.ant-menu-dark,
  .portal-topmenu-submenu-popup .ant-menu,
  .portal-topmenu-submenu-popup .ant-menu.ant-menu-dark,
  .portal-topmenu-submenu-popup .ant-menu-dark,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-sub {
    background: transparent !important;
  }

  .portal-topmenu-submenu-popup.ant-menu-submenu-popup {
    margin-top: 10px;
    padding: 0;
    border: none !important;
    border-radius: 0;
    box-shadow: none !important;
    backdrop-filter: none;
    min-width: auto;
  }

  .portal-topmenu-submenu-popup .ant-menu,
  .portal-topmenu-submenu-popup .ant-menu.ant-menu-dark,
  .portal-topmenu-submenu-popup .ant-menu-dark,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-sub {
    min-width: 224px;
    padding: 10px;
    border-radius: 18px;
    background: linear-gradient(180deg, rgba(248, 251, 255, 0.98) 0%, rgba(240, 246, 255, 0.99) 100%) !important;
    border: 1px solid rgba(105, 156, 219, 0.18) !important;
    box-shadow: 0 18px 38px rgba(15, 55, 104, 0.18) !important;
    backdrop-filter: blur(16px);
  }

  .portal-topmenu-submenu-popup .ant-menu-item,
  .portal-topmenu-submenu-popup .ant-menu-submenu-title,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-item,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-submenu-title,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-sub .ant-menu-item,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-sub .ant-menu-submenu-title {
    height: 44px;
    line-height: 44px;
    margin: 4px 0;
    border-radius: 12px;
    color: #16324f !important;
    font-family: var(--portal-menu-font);
    font-size: 15px;
    font-weight: 600;
    transition: all .2s ease;
    background: transparent !important;

    a {
      color: inherit !important;
    }

    .anticon {
      color: #4d8fd3 !important;
    }
  }

  .portal-topmenu-submenu-popup .ant-menu-item:hover,
  .portal-topmenu-submenu-popup .ant-menu-submenu-title:hover,
  .portal-topmenu-submenu-popup .ant-menu-submenu-active > .ant-menu-submenu-title,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-item:hover,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-submenu-title:hover,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-submenu-active > .ant-menu-submenu-title {
    color: #0f2d4b !important;
    background: linear-gradient(135deg, rgba(76, 157, 255, 0.16) 0%, rgba(93, 200, 255, 0.12) 100%) !important;
  }

  .portal-topmenu-submenu-popup .ant-menu-item-selected,
  .portal-topmenu-submenu-popup .ant-menu-submenu-selected > .ant-menu-submenu-title,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-item-selected,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-submenu-selected > .ant-menu-submenu-title {
    color: #ffffff !important;
    background: linear-gradient(135deg, #2890f0 0%, #3a9dff 100%) !important;
    box-shadow: 0 10px 22px rgba(40, 124, 212, 0.24);
  }

  .portal-topmenu-submenu-popup .ant-menu-item-selected a,
  .portal-topmenu-submenu-popup .ant-menu-submenu-selected > .ant-menu-submenu-title,
  .portal-topmenu-submenu-popup .ant-menu-item-selected .anticon,
  .portal-topmenu-submenu-popup .ant-menu-submenu-selected > .ant-menu-submenu-title .anticon,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-item-selected a,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-submenu-selected > .ant-menu-submenu-title,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-item-selected .anticon,
  .portal-topmenu-submenu-popup .ant-menu-dark .ant-menu-submenu-selected > .ant-menu-submenu-title .anticon {
    color: #ffffff !important;
  }

  .portal-topmenu-submenu-popup .ant-menu-item-selected::after,
  .portal-topmenu-submenu-popup .ant-menu-item-active::after {
    display: none;
  }

  .portal-topmenu-submenu-popup .ant-menu-submenu-arrow::before,
  .portal-topmenu-submenu-popup .ant-menu-submenu-arrow::after {
    background: rgba(83, 122, 163, 0.72) !important;
  }

  // drawer-sider 自定义
  .ant-drawer.drawer-sider {
    .sider {
      box-shadow: none;
    }

    &.dark {
      .ant-drawer-content {
        background-color: rgb(0, 21, 41);
      }
    }

    &.light {
      box-shadow: none;

      .ant-drawer-content {
        background-color: #fff;
      }
    }

    .ant-drawer-body {
      padding: 0
    }
  }

  // 菜单样式
  .sider {
    box-shadow: 2px 116px 6px 0 rgba(0, 21, 41, .35);
    position: relative;
    z-index: 10;

    &.ant-fixed-sidemenu {
      position: fixed;
      height: 100%;
    }

    .logo {
      height: 64px;
      position: relative;
      line-height: normal;
      display: flex;
      align-items: center;
      padding-left: 24px;
      -webkit-transition: all .3s;
      transition: all .3s;
      background: #002140;
      overflow: hidden;

      img {
        height: 32px;
        margin-right: 8px;
      }

      h1 {
        color: #fff;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        justify-content: center;
        margin: 0;
        font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
      }
    }

    &.light {
      background-color: #fff;
      box-shadow: 2px 116px 8px 0 rgba(29, 35, 41, 0.05);

      .logo {
        background: #fff;
        box-shadow: 1px 1px 0 0 #e8e8e8;

        h1 {
          color: unset;
        }
      }

      .ant-menu-light {
        border-right-color: transparent;
      }
    }

  }

  // 外置的样式控制
  .user-dropdown-menu-wrapper.ant-dropdown-menu {
    padding: 4px 0;

    .ant-dropdown-menu-item {
      width: 160px;
    }

    .ant-dropdown-menu-item > .anticon:first-child,
    .ant-dropdown-menu-item > a > .anticon:first-child,
    .ant-dropdown-menu-submenu-title > .anticon:first-child
    .ant-dropdown-menu-submenu-title > a > .anticon:first-child {
      min-width: 12px;
      margin-right: 8px;
    }

  }

  // 数据列表 样式
  .table-alert {
    margin-bottom: 16px;
  }

  .table-page-search-wrapper {

    .ant-form-inline {

      .ant-form-item {
        display: flex;
        margin-bottom: 24px;
        margin-right: 0;

        .ant-form-item-control-wrapper {
          flex: 1 1;
          display: inline-block;
          vertical-align: middle;
        }

        > .ant-form-item-label {
          line-height: 32px;
          padding-right: 8px;
          width: auto;
        }

        .ant-form-item-control {
          height: 32px;
          line-height: 32px;
        }
      }
    }

    .table-page-search-submitButtons {
      display: block;
      margin-bottom: 24px;
      white-space: nowrap;
    }

  }

  .content {

    .table-operator {
      margin-bottom: 18px;

      button {
        margin-right: 8px;
      }
    }
  }
</style>
