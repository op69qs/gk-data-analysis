<template>
  <div
    class="v-echarts"
    :style="{ width, height }"
  ></div>
</template>

<script>
// import echarts from 'echarts/lib/echarts'
// import 'echarts/lib/chart/bar'
// import 'echarts/lib/chart/line'
import 'echarts/theme/macarons' // echarts theme
import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
export default {
  name: 'v-echarts',
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '100%'
    },
    opts: {
      type: Object
    },
    options: {
      type: Object
    },
    theme: [Object, String]
  },
  data() {
    return {
      chart: null,
      ceshioption: null
    }
  },
  created() {
    this.chart = null
  },
  mounted() {
    this.$nextTick(() => {
      this.init()
    })    
  },
  beforeDestroy() {
    if(this.chart) this.clean()
    
  },
  computed: {
  },
  watch: {
    options: {
      deep: true,
      immediate: true,
      handler(v) {
        if (!v) return
        this.init()
        // this.chart.setOption(v, true)
      }
    }
  },
  methods: {
    init() {
      if (this.options) {
        // this.chart = this.$echarts.init(this.$el, this.theme, this.opts)
        this.chart = this.$echarts.init(this.$el, this.theme)
        this.chart.clear()
        this.chart.resize()
        this.chart.setOption(this.options)
        if(this.chart) window.addEventListener('resize', this.chart.resize)
      }
    },
    getList(item) {
      let that = this;
      if(item.query_path && item.page_id!=null && item.query_path!=null) {
        let url = item.query_path;
        let param = {
          gallery_id : item.gallery_id,
          page_id: item.page_id
        }
        postAction(url, param).then((res) => {
          let options;
          if (res.result == 'success') {
            if(res.type == 'barAndLine'){
              if(item.option != null) {
                // let optionData = JSON.parse(item.option);
                let optionData = eval('('+item.option+')');
                // optionData.series[0].color = ''
                item.options = optionData;
                this.ceshioption = item.options;
                this.init()
              }
            }
          }
        })
      }
    },
    clean() {
      window.removeEventListener('resize', this.chart.resize)
      this.chart.dispose();
      this.chart = null;
    }
  }
}
</script>
