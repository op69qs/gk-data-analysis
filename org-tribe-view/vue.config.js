const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// vue.config.js
module.exports = {
  publicPath: '/',
  /*
    Vue-cli3:
    Crashed when using Webpack `import()` #2463
    https://github.com/vuejs/vue-cli/issues/2463
   */
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  /*
  pages: {
    index: {
      entry: 'src/main.js',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    }
  },
  */
  configureWebpack: config => {
    // 生产环境取消 console.log
    if (process.env.NODE_ENV === 'production') {
      if (config.optimization && config.optimization.minimizer) {
        // 寻找内置的 TerserPlugin
        const terserPlugin = config.optimization.minimizer.find(
            p => p.constructor.name === 'TerserPlugin'
        );

        if (terserPlugin && terserPlugin.options && terserPlugin.options.terserOptions) {
          terserPlugin.options.terserOptions.compress = terserPlugin.options.terserOptions.compress || {};
          terserPlugin.options.terserOptions.compress.drop_console = true;
        } else {
          // 如果上面找不到（部分现代脚手架结构不同），尝试直接操作
          // 或者是使用可选链 (Optional Chaining) 防止崩溃
          if (config.optimization.minimizer[0]?.options?.terserOptions?.compress) {
            config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true;
          }
        }
      }
    }
  },
  chainWebpack: config => {
    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))
      .set('@layout', resolve('src/layout'))
      .set('@static', resolve('src/static'))
  },

  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */
          /*
          'primary-color': '#F5222D',
          'link-color': '#F5222D',
          'border-radius-base': '4px',
          */
        },
        javascriptEnabled: true
      }
    }
  },

  devServer: {
    port: 3000,
    proxy: {
      '/complatform': {
        // target: 'http://192.168.100.126:9095/', //126
        //  target: 'http://192.168.100.25:8080/', //郝江
        // target: 'http://xianyam.iok.la:48352', //花生壳镜像
        // target: 'http://192.168.100.15:8080/', //杨晓明
        // target: 'http://192.168.100.46:8080/', //
        // target: 'http://192.168.100.51:8989/', // 张超
        // target: 'http://192.168.155.3:9092/', //张超昆明现场
        // target: 'http://192.168.100.37:9090/', //党娇
        target: 'http://10.10.127.21:9090/', //党娇
        // target: 'http://192.168.100.25:8081/', //郝江
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/complatform': '' //默认所有请求都加了jeecg-boot前缀，需要去掉
        }
      }
    }
  },

  lintOnSave: false
}
