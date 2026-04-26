# 修复 org-tribe-system 启动后前端静态资源 404

## 现象

`org-tribe-system` 启动后，页面能返回 `index.html`，但大量 `js/css` 资源返回 404。

## 根因

当前前端构建链路是：

1. `generate-resources` 阶段构建 `org-tribe-view/dist`
2. `prepare-package` 阶段才把 `dist` 复制到 `${project.build.outputDirectory}/static`

这会导致一个问题：

- 如果只是常规启动、IDEA 运行或执行到编译/资源处理阶段
- 还没进入 `prepare-package`
- 运行时 classpath 下就没有最新的 `js/css` 文件

此时旧的 `src/main/resources/static/index.html` 或运行目录中的页面仍可能被访问到，但它引用的 hash 资源文件并不存在，于是出现大量 404。

## 修改

将以下两个执行从 `prepare-package` 提前到 `process-resources`：

1. 清理并创建 `${project.build.outputDirectory}/static`
2. 将 `org-tribe-view/dist` 复制到 `${project.build.outputDirectory}/static`

这样只要执行到常规资源处理阶段，后端运行时就能拿到最新前端静态资源。

## 验证

执行：

`mvn -pl org-tribe-system -am process-resources`

验证点：

1. `org-tribe-system/target/classes/static/js` 存在最新 hash 文件
2. `org-tribe-system` 启动后访问首页，不再出现大批 `js/css 404`