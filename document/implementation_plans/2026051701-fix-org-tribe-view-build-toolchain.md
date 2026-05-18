# 修复 org-tribe-view 构建工具链漂移

## 背景

- `org-tribe-view` 是一套老的 Vue 2 + webpack 4 + ant-design-vue 1.x 工程。
- 本次 `npm run build` 首先在 `vue.config.js` 里因压缩器结构变化报错，随后继续暴露出：
  - `vue-loader/dist/stylePostLoader.js` 处理 scoped less 时抛错；
  - JSX 文件无法被 Babel 解析；
  - 组件样式与运行时版本出现 Vue 2 / Vue 3 混搭。

## 根因

- `package.json` 中多项关键依赖被抬升到不兼容的现代版本：
  - `vue` 从 2.x 漂移到 3.x；
  - `vue-loader` 从 15.x 漂移到 17.x；
  - `webpack` 从 4.x 漂移到 5.x；
  - `@vue/cli-service` 从 3.x 漂移到 5.x；
  - 同时 `axios`、`echarts`、`tinymce` 等配套库也被一起抬高。
- 工程根目录缺少显式 `babel.config.js`，导致 JSX 组件文件在当前安装组合下无法稳定走 Vue JSX 转换链。

## 处理

### 1. 恢复兼容依赖声明

- 将 `org-tribe-view/package.json` 恢复到 Vue 2 工程兼容的主要版本区间：
  - `vue@^2.6.10`
  - `vue-loader@^15.7.0`
  - `webpack@^4.43.0`
  - `@vue/cli-plugin-babel@^3.3.0`
  - `@vue/cli-plugin-eslint@^3.3.0`
  - `@vue/cli-service@^3.3.0`
  - 同步恢复 `axios`、`echarts`、`jquery`、`tinymce`、`@tinymce/tinymce-vue` 的旧版本。

### 2. 补齐 Babel JSX 配置

- 新增 `org-tribe-view/babel.config.js`，显式启用：
  - `@vue/cli-plugin-babel/preset`
  - `babel-plugin-transform-vue-jsx`
  - `babel-plugin-transform-runtime`

## 说明

- 当前仓库里已有一份未跟踪的 `package-lock.json`，它是基于漂移后的新依赖生成的，不能代表这套 Vue 2 工程的正确锁文件。
- 因当前会话环境没有可直接调用的 Node/NPM，我未在本轮里完成重装依赖和二次构建。
- 应在本机实际 Node 环境中先清理现有安装结果，再重新安装依赖后验证构建。

## 建议验证步骤

```bash
cd /root/work-project/project-02/gk-data-analysis/org-tribe-view
rm -rf node_modules package-lock.json
npm install --registry https://registry.npm.taobao.org
npm run build
```

## 预期

- `NumberInfo.vue` 的 scoped less 不再进入 Vue 3 的 style post loader 路径。
- `src/components/menu/index.js` 与 `AvatarList/List.vue` 中的 JSX 可被正确编译。
- 后续若还有构建报错，应再按真实源码问题继续排查，而不是继续被错误的工具链放大。

## 后续补充

- 当前仓库缺少历史记录中提到的 `template1.png` 到 `template7.png` 七张 vis 模板预览图。
- 为先恢复构建，`org-tribe-view/src/views/vis/TemplateList.vue` 暂时回退为引用仓库已存在的 `1.png`、`2.png`、`3.png`、`5.png`、`6.png`、`7.png`、`8.png`。
- 如果后续找回旧 vis 原始模板预览图，建议再把该页面资源名切回 `template1.png` 到 `template7.png`，以恢复原设计语义。
- 针对新环境构建兼容，再补了三项收敛：
  - 用 `sass` 替代 `node-sass`，避免 Node 运行时不被旧二进制支持。
  - 在 Babel 中显式开启 `@babel/plugin-proposal-class-properties`，覆盖 `editLibrary.vue`、`checkLibrary.vue` 中的静态类字段语法。
  - `NumberInfo.vue` 去掉 `scoped`，绕开当前工具链下 less 作用域改写报错；该组件样式本身已通过前缀类名隔离，风险较低。
- 重新安装依赖时又暴露一个 npm 7+/10 的 peer 依赖解析问题：
  - `package.json` 中原先写的是 `vue-apexcharts: ^1.3.2`。
  - 该范围会被解析到 `vue-apexcharts@1.7.0`，而这个版本要求 `apexcharts >= 4.0.0`。
  - 项目实际仍使用 `apexcharts@^3.6.5`，因此 `npm install` 会直接在依赖解析阶段失败。
- 处理方式：
  - 将 `vue-apexcharts` 从范围版本改为精确版本 `1.3.2`，锁回旧 Vue 2 工程可兼容的组合，避免 npm 自动漂移到新 peer 约束版本。
- 重新安装继续暴露第二个 npm 新版解析问题：
  - `vue-loader@15.x` 对 `css-loader` 的 peer 约束过宽，在新 npm 下会被自动尝试匹配到最新的 `css-loader@7.x`。
  - `css-loader@7.x` 需要 `webpack ^5.27.0`，而本项目工具链仍是 `webpack 4`，因此在安装阶段再次触发 `ERESOLVE`。
- 处理方式：
  - 在 `package.json` 中显式增加 `css-loader: 1.0.1`，将样式 loader 锁回 Vue CLI 3 / webpack 4 兼容线，避免 npm 拉取到 7.x。
