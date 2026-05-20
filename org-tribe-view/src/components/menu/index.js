import Menu from 'ant-design-vue/es/menu'
import Icon from 'ant-design-vue/es/icon'

const { Item, SubMenu } = Menu

export default {
  name: 'SMenu',
  data () {
    return {
      openKeys: [],
      selectedKeys: [],
      cachedOpenKeys: [],
      showVisBadge: false
    }
  },
  props: {
    menu: {
      type: Array,
      required: true
    },
    theme: {
      type: String,
      required: false,
      default: 'dark'
    },
    mode: {
      type: String,
      required: false,
      default: 'inline'
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  computed: {
    rootSubmenuKeys: vm => {
      const keys = []
      vm.menu.forEach(item => keys.push(item.path))
      return keys
    }
  },
  mounted () {
    this.initVisBadge()
    this.updateMenu()
  },
  watch: {
    collapsed (val) {
      if (val) {
        this.cachedOpenKeys = this.openKeys.concat()
        this.openKeys = []
      } else {
        this.openKeys = this.cachedOpenKeys
      }
    },
    $route: function () {
      this.updateMenu()
    }
  },
  methods: {
    initVisBadge () {
      if (typeof window === 'undefined' || !window.localStorage) {
        return
      }
      const storageKey = 'gk-data-analysis-vis-menu-first-seen-at'
      const now = Date.now()
      const monthMs = 30 * 24 * 60 * 60 * 1000
      const storedValue = window.localStorage.getItem(storageKey)
      const firstSeenAt = storedValue ? Number(storedValue) : now

      if (!storedValue || Number.isNaN(firstSeenAt)) {
        window.localStorage.setItem(storageKey, String(now))
        this.showVisBadge = true
        return
      }

      this.showVisBadge = now - firstSeenAt < monthMs
    },
    getMenuClassNames (menu) {
      const classNames = ['s-menu__item']
      if (menu && this.isVisMenu(menu)) {
        classNames.push('s-menu__item--vis')
      }
      return classNames.join(' ')
    },
    getSubMenuPopupClassName (menu) {
      const classNames = []
      if (this.mode === 'horizontal') {
        classNames.push('portal-topmenu-submenu-popup')
      }
      if (this.isVisRootMenu(menu)) {
        classNames.push('portal-vis-submenu-popup')
      }
      return classNames.join(' ')
    },
    getSubMenuPopupOffset (menu) {
      if (this.mode !== 'horizontal') {
        return undefined
      }
      
      return [-10, 10]
    },
    shouldShowVisBadge (menu) {
      return this.showVisBadge && this.isVisRootMenu(menu)
    },
    renderMenuLabel (menu) {
      return (
        <span class="s-menu__label">
          <span>{menu.meta.title}</span>
          {this.shouldShowVisBadge(menu) ? <span class="s-menu__badge">NEW</span> : null}
        </span>
      )
    },
    isVisMenu (menu) {
      if (!menu) {
        return false
      }
      const title = menu.meta && menu.meta.title ? String(menu.meta.title) : ''
      return String(menu.path || '').startsWith('/vis') || title.indexOf('可视化大屏') >= 0
    },
    isVisRootMenu (menu) {
      if (!menu) {
        return false
      }
      const title = menu.meta && menu.meta.title ? String(menu.meta.title) : ''
      return String(menu.path || '') === '/vis' || title.indexOf('可视化大屏') >= 0
    },
    // select menu item
    onOpenChange (openKeys) {

      // 在水平模式下时执行，并且不再执行后续
      if (this.mode === 'horizontal') {
        this.openKeys = openKeys
        return
      }
      // 非水平模式时
      const latestOpenKey = openKeys.find(key => !this.openKeys.includes(key))
      if (!this.rootSubmenuKeys.includes(latestOpenKey)) {
        this.openKeys = openKeys
      } else {
        this.openKeys = latestOpenKey ? [latestOpenKey] : []
      }
    },
    updateMenu () {
      const routes = this.$route.matched.concat()
      const { hidden } = this.$route.meta
      if (routes.length >= 3 && hidden) {
        routes.pop()
        this.selectedKeys = [routes[routes.length - 1].path]
      } else {
        this.selectedKeys = [routes.pop().path]
      }
      const openKeys = []
      if (this.mode === 'inline') {
        routes.forEach(item => {
          openKeys.push(item.path)
        })
      }
      //update-begin-author:taoyan date:20190510 for:online表单菜单点击展开的一级目录不对
      if(!this.selectedKeys || this.selectedKeys[0].indexOf(":")<0){
        this.collapsed ? (this.cachedOpenKeys = openKeys) : (this.openKeys = openKeys)
      }
      //update-end-author:taoyan date:20190510 for:online表单菜单点击展开的一级目录不对
    },

    // render
    renderItem (menu) {
      if (!menu.hidden) {
        return menu.children && !menu.alwaysShow ? this.renderSubMenu(menu) : this.renderMenuItem(menu)
      }
      return null
    },
    renderMenuItem (menu) {
      const target = menu.meta.target || null
      const tag = target && 'a' || 'router-link'
      let props = { to: { name: menu.name } }
      if(menu.route && menu.route === '0'){
        props = { to: { path: menu.path } }
      }

      const attrs = { href: menu.path, target: menu.meta.target }

      if (menu.children && menu.alwaysShow) {
        // 把有子菜单的 并且 父菜单是要隐藏子菜单的
        // 都给子菜单增加一个 hidden 属性
        // 用来给刷新页面时， selectedKeys 做控制用
        menu.children.forEach(item => {
          item.meta = Object.assign(item.meta, { hidden: true })
        })
      }

      return (
        <Item class={this.getMenuClassNames(menu)} {...{ key: menu.path }}>
          <tag {...{ props, attrs }}>
            {this.renderIcon(menu.meta.icon)}
            {this.renderMenuLabel(menu)}
          </tag>
        </Item>
      )
    },
    renderSubMenu (menu) {
      const itemArr = []
      if (!menu.alwaysShow) {
        menu.children.forEach(item => itemArr.push(this.renderItem(item)))
      }
      return (
        <SubMenu
          class={this.getMenuClassNames(menu)}
          popupClassName={this.getSubMenuPopupClassName(menu)}
          popupOffset={this.getSubMenuPopupOffset(menu)}
          {...{ key: menu.path }}
        >
          <span slot="title">
            {this.renderIcon(menu.meta.icon)}
            {this.renderMenuLabel(menu)}
          </span>
          {itemArr}
        </SubMenu>
      )
    },
    renderIcon (icon) {
      if (icon === 'none' || icon === undefined) {
        return null
      }
      const props = {}
      typeof (icon) === 'object' ? props.component = icon : props.type = icon
      return (
        <Icon {... { props } }/>
      )
    }
  },

  render () {
    const { mode, theme, menu } = this
    const props = {
      mode: mode,
      theme: theme,
      openKeys: this.openKeys
    }
    const on = {
      select: obj => {
        this.selectedKeys = obj.selectedKeys
        this.$emit('select', obj)
      },
      openChange: this.onOpenChange
    }

    const menuTree = menu.map(item => {
      if (item.hidden) {
        return null
      }
      return this.renderItem(item)
    })
    // {...{ props, on: on }}
    return (
      <Menu vModel={this.selectedKeys} {...{ props, on: on }}>
        {menuTree}
      </Menu>
    )
  }
}
