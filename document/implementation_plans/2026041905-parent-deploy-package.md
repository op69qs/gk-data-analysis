# 父 pom deploy-package 打包改造

## 本次修改

- 在根 `pom.xml` 新增 `deploy-package` 组装流程，输出结构固定为：
  - `deploy-package/app`
  - `deploy-package/bin`
  - `deploy-package/config/{module}`
  - `deploy-package/lib/{module}`
- `app` 统一收口 6 个聚合后端模块的主 jar。
- `config/{module}` 统一收口各模块打包到 `target` 根目录的 `application*.yml` 和 `application.properties`。
- `lib/{module}` 统一收口各模块 `target/lib` 依赖，不再给每个模块单独顶层目录。
- 新增统一启动脚本模板 `build/start-all.sh`，打包时复制到 `deploy-package/bin/start-all.sh`。
- 为避免父工程在 Reactor 最前面执行导致组装时机过早，新增末尾聚合模块 `deploy-package-assembly`，专门在 6 个后端模块打包完成后再统一组装 `deploy-package`。

## org-tribe-system 前端处理

- 在 `org-tribe-system/pom.xml` 接入 `frontend-maven-plugin`。
- 打包时不再依赖开发机全局 Node，而是由 Maven 自动下载并使用固定版本：
  - Node `v14.21.3`
  - npm `8.19.4`
- 选择这个版本组合的原因：
  - `org-tribe-view` 仍是 Vue CLI 3 + `node-sass 4.11.0` 的旧前端工程。
  - 如果直接用更高的 Node 主版本，`node-sass` 很容易在安装或编译阶段失败。
  - 同时项目仓库已经提交 `package-lock.json`，因此用 `npm ci` 比无锁的 `yarn install` 更稳定，能避免构建时漂移到要求 Node 18+ 的新传递依赖。
  - 将 Node/npm 版本固化到 Maven 流程里，能避免不同机器打包结果不一致。
- 前端构建流程为：
  1. 安装固定 Node/npm
  2. 先清理 `org-tribe-view/node_modules` 与旧 `dist`
  3. 在 `org-tribe-view` 执行 `npm ci`
  4. 执行 `npm run build`
  5. 在 `org-tribe-system` 的 `prepare-package` 阶段清空 `target/classes/static`
  6. 将 `org-tribe-view/dist` 复制到 `target/classes/static`

- 额外处理：
  - 在 Windows 下如果工作区里残留旧的 `node_modules` 或前一次失败后的半成品文件，`npm ci` 很容易在删除阶段报 `EPERM`。
  - 因此 Maven 流程里先在 `initialize` 阶段显式删除 `org-tribe-view/node_modules` 和旧 `dist`，保证后续 `generate-resources` 阶段执行前端安装与构建时目录是干净的，同时不会误删刚刚生成的 `dist`。
  - 另外补齐了部分老 Vue 页面中 `style` 块对 `.less` 公共样式的声明方式，将这些页面切换到 `lang="less"`，避免生产构建时把 `.less` 当普通 CSS 导致公共样式路径无法解析。

## deploy-package 组装修正

- `deploy-package-assembly` 中不再通过 `${project.parent.basedir}` 给 Ant 传递根目录路径。
- 改为在 Maven 属性中显式定义 `${project.basedir}/..` 作为 Reactor 根目录，再统一派生 `deploy-package`、模块 `target` 和 `build/start-all.sh` 的拷贝源路径。
- 这样可以避免 Ant 执行时把未展开的父工程变量字面量写进目录，确保最终产物稳定输出到仓库根目录下的 `deploy-package/`。

## 统一启动脚本设计

- 由于最终目录结构改为 `app + lib/{module} + config/{module}`，不能再依赖各模块 jar 清单里原来的 `lib/` 相对路径。
- `start-all.sh` 改为显式使用 `java -cp "app/module.jar:lib/{module}/*" MainClass` 启动。
- 脚本支持 `start|stop|restart|status` 四种动作。
- 启动时统一追加：
  - `--spring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}`
  - `--spring.config.additional-location=config/{module}/`
- 日志输出到 `deploy-package/logs/{module}`，pid 文件写到 `deploy-package/run`。

## 目录结果

最终 `deploy-package` 设计如下：

```text
deploy-package/
  app/
    *.jar
  bin/
    start-all.sh
  config/
    dwbi-statistical-analysis/
    dwbi-system-docking/
    fixedReport/
    indicatorsLibv-1.0/
    org-tribe-system/
    seo/
  lib/
    dwbi-statistical-analysis/
    dwbi-system-docking/
    fixedReport/
    indicatorsLibv-1.0/
    org-tribe-system/
    seo/
```

## 说明

- 本次只覆盖父 pom 当前聚合的 6 个后端模块，不包含 `inspect`。
- 前端不再独立进入 `deploy-package`，而是随 `org-tribe-system` jar 内嵌交付。
- `org-tribe-system/src/main/resources/static` 现有历史产物不再作为最终交付来源，最终以 `org-tribe-view/dist` 构建结果覆盖到 `target/classes/static` 为准。