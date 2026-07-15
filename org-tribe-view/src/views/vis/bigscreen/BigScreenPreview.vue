<template>
  <div class="bigBox" allowfullscreen="true">
    <el-carousel
      :height="height"
      :interval="interval"
      :autoplay="autoSetting"
      :arrow="levelData.length > 1 ? 'always' : 'never'"
      :loop="levelData.length > 1"
      trigger="click"
      ref="carouselFull"
    >
      <el-carousel-item v-for="(item, index) in levelData" :key="index">
        <big-screentab :layoutData="item" :screenHeight="screenHeight" :fatherMethod="fatherMethod"></big-screentab>
      </el-carousel-item>
    </el-carousel>
    <div class="bigLoading" v-if="isShow">
      <div class="loading-pro">
        <p>加载中请稍候...</p>
        <a-progress
          :stroke-color="{
            from: '#108ee9',
            to: '#87d068',
          }"
          :stroke-width="20"
          :percent="percentNum"
          status="active"
        >
          <template #format="percent">
            <span style="color: #fff">{{ percent }}%</span>
          </template>
        </a-progress>
      </div>
    </div>
  </div>
</template>

<script>
import { getSchemeAllPage } from '@/api/visScreen'
import { resolveVisCarouselInterval } from '@/utils/visCarousel'
import { createVisKeyboardCoordinator, shouldSuppressVisBrowserShortcut } from '@/utils/visKeyboardNavigation'
import { createVisWheelCoordinator } from '@/utils/visWheelNavigation'
import { isVisPreviewConsoleDebugEnabled, visPreviewDebug } from '@/utils/visPreviewDebug'
import screenfull from 'screenfull'
import BigScreenTabTemplate from './modules/BigScreenTabTemplate'
export default {
  name: 'BigScreenPreview',
  components: {
    'big-screentab': BigScreenTabTemplate,
  },
  data() {
    return {
      autoSetting: false,
      isFullScreen: false,
      height: document.body.offsetHeight * 1 + 'px',
      circle: false,
      interval: 5000,
      isShow: true,
      levelData: [],
      currentNum: 0, // 当前已请求成功接口数
      totalNum: 0, // 总接口数
      percentNum: 0, // 进度条数据
      screenHeight: window.innerHeight,
      diff: 6000, // 不操作触发间隔
      firstTime: new Date().getTime(), // 代表第一次鼠标移动时间
      lastTime: new Date().getTime(), // 代表最后一次鼠标移动时间
      indulge: false, // 阀门
      switchTime: null, // 判断定时器
      startTime: null, // 开始定时器
      keyboardCoordinator: null,
      wheelCoordinator: null,
      keyboardDebugEnabled: false,
    }
  },
  watch: {
    currentNum(val) {
      let precent = parseInt((this.currentNum / this.totalNum) * 100)
      this.percentNum = precent
      visPreviewDebug('progress changed', {
        currentNum: this.currentNum,
        totalNum: this.totalNum,
        percentNum: this.percentNum,
        isShow: this.isShow
      })
      // this.percentNum = precent == '100' ? precent : parseFloat(precent.toFixed(0))
      // console.log(this.percentNum)
      if (this.currentNum >= this.totalNum) {
        this.percentNum = 100
        visPreviewDebug('loading complete, hide mask scheduled')
        setTimeout(() => {
          this.isShow = false
          // this.mouserChange()
          // this.newchange()
        }, 1000)
      }
    },
  },
  mounted() {
    // console.log('我是最新版本-12/16')
    //全屏
    this.setFontSize() // 字体样式自适应
    this.fullScreen() // 全屏操作
    document.body.onselectstart = document.body.oncontextmenu = function () {
      return false
    }
    this.keyboardDebugEnabled = isVisPreviewConsoleDebugEnabled(window.location.search, window.localStorage)
    this.keyboardCoordinator = createVisKeyboardCoordinator(this.navigateByKeyCode, this.logNavigationDecision)
    this.wheelCoordinator = createVisWheelCoordinator(
      (code) => this.navigateByKeyCode(code, 'wheel'),
      this.logWheelDecision
    )
    window.addEventListener('keydown', this.logNavigationKeyCapture, true)
    window.addEventListener('keyup', this.logNavigationKeyCapture, true)
    document.addEventListener('keydown', this.handleNavigationKeydown, false)
    document.addEventListener('keyup', this.handleNavigationKeyup, false)
    window.addEventListener('wheel', this.handleNavigationWheel, { passive: false })
    window.addEventListener('blur', this.resetNavigationKeys, false)
    window.onresize = () => {
      //监听页面高度
      return (() => {
        this.height = document.body.offsetHeight + 'px'
        window.screenHeight = window.innerHeight
        this.screenHeight = window.screenHeight
        document.querySelector('body').setAttribute('style', 'overflow:auto') // 创建前去掉浏览器导航条
        // 全屏下监控是否按键了ESC
        if (!this.checkFull()) {
          // 全屏下按键esc后要执行的动作
          // this.isFullscreen = false
          this.$message.info('按住ESC即可返回方案界面！')
        }
      })()
    }
    this.getData()
  },
  methods: {
    fullScreen() {
      // 全屏代码
      var elem = document.documentElement
      this.requestFullScreen(elem)
    },
    requestFullScreen(element) {
      // 全屏显示元素
      var requestMethod =
        element.requestFullScreen ||
        element.webkitRequestFullScreen ||
        element.mozRequestFullScreen ||
        element.msRequestFullscreen
      if (requestMethod) {
        requestMethod.call(element)
      } else if (typeof window.ActiveXObject !== 'undefined') {
        var wscript = new ActiveXObject('WScript.Shell')
        if (wscript !== null) {
          wscript.SendKeys('{F11}')
        }
      }
    },
    checkFull() {
      //是否全屏并按键ESC键的方法
      var isFull =
        document.fullscreenEnabled || window.fullScreen || document.webkitIsFullScreen || document.msFullscreenEnabled
      // to fix : false || undefined == undefined
      if (isFull === undefined) {
        isFull = false
      }
      return isFull
    },
    getData() {
      // 页面数据
      let id = this.$route.query.info || this.$route.query.schemeId
      this.autoSetting =  this.$route.query.autoSetting == 1 ? true :false
      this.interval = resolveVisCarouselInterval(this.$route.query.interval)
      visPreviewDebug('getData start', {
        query: this.$route.query,
        schemeId: id,
        autoSetting: this.autoSetting,
        interval: this.interval
      })
      getSchemeAllPage({ scheme_id: id }).then((res) => {
        visPreviewDebug('getSchemeAllPage response', {
          result: res && res.result,
          count: res && res.count,
          dataLength: res && res.data && res.data.length,
          rowsLength: res && res.rows && res.rows.length,
          res: res
        })
        if (res.result == 'success') {
          this.currentNum = 0
          this.percentNum = 0
          this.levelData = res.data || res.rows || []
          // console.log(res)
          this.totalNum = this.getLoadingItemCount(this.levelData)
          visPreviewDebug('loading total resolved', {
            totalNum: this.totalNum,
            levelData: this.levelData
          })
          if (this.totalNum == 0) this.isShow = false
        }
      })
    },
    getLoadingItemCount(levelData) {
      return levelData.reduce((total, page) => {
        let pageSub = page.page_sub || []
        return total + pageSub.filter((item) => {
          if (item.type == 'p') {
            return item.page_id != null && this.hasImageSource(item)
          }
          return item.type != 't' && item.type != 'h' && item.query_path && item.page_id != null && item.query_path != null && item.query_path != 'null'
        }).length
      }, 0)
    },
    hasImageSource(item) {
      return [item.content, item.option, item.query_path].some((value) => {
        return typeof value === 'string' && value != '' && value != 'null'
      })
    },
    fatherMethod() {
      // 当前接口请求数量
      this.currentNum++
      visPreviewDebug('fatherMethod called', {
        currentNum: this.currentNum,
        totalNum: this.totalNum
      })
    },
    setFontSize() {
      // 动态设置字体大小
      let docEl = document.documentElement //documentElement 属性是根节点
      let resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize'
      let recalc = function () {
        var clientWidth = docEl.clientWidth
        if (!clientWidth) return
        docEl.style.fontSize = 20 * (clientWidth / 1920) + 'px' //设置html根元素的font-size
      }
      if (!document.addEventListener) return
      window.addEventListener(resizeEvt, recalc, false)
      document.addEventListener('DOMContentLoaded', recalc, false)
    },
    handleNavigationKeydown(event) {
      this.logNavigationEvent('document-bubble', event)
      this.keyboardCoordinator.handleKeydown(event)
    },
    handleNavigationKeyup(event) {
      this.logNavigationEvent('document-bubble', event)
      this.keyboardCoordinator.handleKeyup(event)
    },
    handleNavigationWheel(event) {
      const decision = this.wheelCoordinator.handleWheel(event, performance.now())
      if (decision.consumed) event.preventDefault()
    },
    logNavigationKeyCapture(event) {
      if (shouldSuppressVisBrowserShortcut(event) && event.cancelable) event.preventDefault()
      this.logNavigationEvent('window-capture', event)
    },
    describeNavigationElement(element) {
      if (!element) return null
      return {
        tag: element.tagName,
        id: element.id,
        className: typeof element.className === 'string' ? element.className : '',
      }
    },
    logNavigationEvent(phase, event) {
      if (!this.keyboardDebugEnabled) return
      visPreviewDebug('keyboard event', {
        phase,
        type: event.type,
        key: event.key,
        code: event.code,
        keyCode: event.keyCode,
        which: event.which,
        repeat: event.repeat,
        defaultPrevented: event.defaultPrevented,
        cancelBubble: event.cancelBubble,
        target: this.describeNavigationElement(event.target),
        activeElement: this.describeNavigationElement(document.activeElement),
        activeIndex: this.getCarouselActiveIndex(),
        timeStamp: event.timeStamp,
      })
    },
    logNavigationDecision(decision) {
      if (!this.keyboardDebugEnabled) return
      visPreviewDebug('keyboard decision', {
        ...decision,
        activeIndex: this.getCarouselActiveIndex(),
      })
    },
    logWheelDecision(decision) {
      if (!this.keyboardDebugEnabled) return
      visPreviewDebug('wheel decision', {
        ...decision,
        activeIndex: this.getCarouselActiveIndex(),
      })
    },
    getCarouselActiveIndex() {
      const carousel = this.$refs.carouselFull
      return carousel && typeof carousel.activeIndex === 'number' ? carousel.activeIndex : null
    },
    resetNavigationKeys() {
      if (this.keyboardCoordinator) this.keyboardCoordinator.reset()
      if (this.wheelCoordinator) this.wheelCoordinator.reset()
    },
    navigateByKeyCode(code, source = 'keyboard') {
      const beforeIndex = this.getCarouselActiveIndex()
      if (code === 27) {
        this.$router.push({ path: '/vis/bigscreen/schemes' })
      } else if (code === 37 && this.$refs.carouselFull) {
        this.$refs.carouselFull.prev()
      } else if (code === 39 && this.$refs.carouselFull) {
        this.$refs.carouselFull.next()
      }
      if (this.keyboardDebugEnabled) this.$nextTick(() => {
        visPreviewDebug(`${source} navigation`, {
          code,
          beforeIndex,
          afterIndex: this.getCarouselActiveIndex(),
          hasCarousel: Boolean(this.$refs.carouselFull),
        })
      })
    },
    mouserChange() {
      var _this = this
      var diff = 6000, //未操作触发间隔
        firstTime = new Date().getTime(),
        lastTime = new Date().getTime(),
        indulge = false //阀门
      //启动
      var switchTime = setTimeout(_this.anm, 5000)
      //停止
      document.addEventListener('mousemove', ()=> {
        indulge = true
        clearTimeout(switchTime)
        firstTime = new Date().getTime()
        document.querySelector('html').style.cursor = ''
        document.querySelector('html').style.pointerEvents = ''
        // console.log('moving')
      })
      //再启动
      const start = setInterval(()=> {
        lastTime = new Date().getTime()
        if (lastTime - firstTime > diff && indulge) {
          indulge = false
          // viewNub = $('.mid-top-menu li.active').index() //如果中途操作鼠标保存选中的下标，下次变化从次开始
          switchTime = setTimeout(_this.anm, 5000)
        }
      }, 1000)
    },
    mouserChange1() {
      let _this = this
      //启动
      _this.switchTime = setTimeout(_this.anm, 5000)
      //停止
      // document.addEventListener('mousemove',_this.mouserHandle)
      document.addEventListener('mousemove', ()=> {
        _this.indulge = true
        // clearTimeout(switchTime)
        _this.firstTime = new Date().getTime()
        document.querySelector('html').style.cursor = ''
        document.querySelector('html').style.pointerEvents = ''
        // console.log('moving')
      })
      //再启动
      const start = setInterval(()=> {
        _this.lastTime = new Date().getTime()
        if (_this.lastTime - _this.firstTime > _this.diff && _this.indulge) {
          _this.indulge = false
          _this.switchTime = setTimeout(_this.anm, 5000)
        }
      }, 1000)
      // this.$once('hook:beforeDestroy', () => {            
      //     clearInterval(start);                                    
      // })

    },
    mouserHandle(){
      let that = this
      that.indulge = true
      // clearInterval(switchTime)
      that.firstTime = new Date().getTime()
      document.querySelector('html').style.cursor = ''
      document.querySelector('html').style.pointerEvents = ''
      // console.log('moving')
    },
    anm() {
      // 触发轮播
      document.querySelector('html').style.cursor = 'none'
      document.querySelector('html').style.pointerEvents = 'none'
      // console.log(this.$refs)
      if(this.$refs.carouselFull) {
        this.$refs.carouselFull.next()
      }
    },
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'overflow:auto') // 创建前去掉浏览器导航条
  },
  beforeDestroy() {
    // document.removeEventListener('mousemove',this.mouserHandle)
    window.removeEventListener('keydown', this.logNavigationKeyCapture, true)
    window.removeEventListener('keyup', this.logNavigationKeyCapture, true)
    document.removeEventListener('keydown', this.handleNavigationKeydown, false)
    document.removeEventListener('keyup', this.handleNavigationKeyup, false)
    window.removeEventListener('wheel', this.handleNavigationWheel, false)
    window.removeEventListener('blur', this.resetNavigationKeys, false)
    this.resetNavigationKeys()
    document.querySelector('body').removeAttribute('style') // 销毁前去掉自己的样式
  },
}
</script>

<style scoped>
.bigBox {
  width: 100%;
  height: 100%;
  position: relative;
}
.bigLoading {
  position: absolute;
  z-index: 2;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
}
.loading-pro {
  position: absolute;
  top: 46%;
  left: 25%;
  width: 50%;
}
.loading-pro p {
  text-align: center;
  font-size: 1.3rem;
  margin-bottom: 10%;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  overflow: hidden;
}

.ant-carousel >>> .custom-slick-arrow {
  width: 25px;
  height: 25px;
  font-size: 25px;
  color: #fff;
  background-color: rgba(31, 45, 61, 0.11);
  opacity: 0.3;
}

.ant-carousel >>> .custom-slick-arrow:before {
  display: none;
}

.ant-carousel >>> .custom-slick-arrow:hover {
  opacity: 0.5;
}

.ant-carousel >>> .slick-slide h3 {
  color: #fff;
}
* {
  /* cursor: 'none'; */
  /* pointer-events: none; */
}
</style>
