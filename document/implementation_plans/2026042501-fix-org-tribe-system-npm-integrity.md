# org-tribe-system Maven Frontend Integrity Fix

## 背景

在 IDEA 中执行 Maven 构建时，`org-tribe-system` 会通过 `frontend-maven-plugin` 进入 `org-tribe-view` 执行 `npm ci`。

失败日志显示：

- Maven 已固定使用 `Node v14.21.3` 与 `npm 8.19.4`
- 失败点为 `clipboard@2.0.7` 的 `EINTEGRITY`
- `package-lock.json` 记录的完整性值来自 `registry.npmjs.org`
- 但 Maven 额外强制传入 `--registry=https://registry.npmmirror.com`

## 根因

镜像源 `npmmirror` 对 `clipboard@2.0.7` 返回的元数据与锁文件不一致：

- `npmjs` 的 integrity: `sha512-8M8WEZ...`
- `npmmirror` 的 integrity: `sha512-g5zbi...`
- `npmmirror` 返回的 tarball 甚至指向 `clipboard-2.0.6.tgz`

因此这不是 Node 版本未切到 14 导致的问题，而是锁文件与强制镜像源之间的校验冲突。

## 修改

将 `org-tribe-system/pom.xml` 中前端安装命令从：

`npm ci --registry=https://registry.npmmirror.com --legacy-peer-deps`

调整为：

`npm ci --legacy-peer-deps`

这样 `npm ci` 会按锁文件中的 `resolved` 信息取包，避免镜像返回错误元数据时触发完整性校验失败。

另外，窄验证继续向前执行后，暴露出第二个独立问题：

- `org-tribe-view` 启用了 `@vue/cli-plugin-eslint`
- 但仓库根目录没有任何项目级 `.eslintrc*`
- `vue.config.js` 中 `lintOnSave` 为 `undefined`

这会导致生产构建阶段仍尝试挂载 ESLint loader，并因“找不到 ESLint 配置”直接失败。

因此将 `org-tribe-view/vue.config.js` 中的 `lintOnSave` 调整为 `false`，避免 Maven 打包阶段被缺失的 ESLint 配置阻塞。

## 验证

建议执行：

`mvn -pl org-tribe-system -am generate-resources`

若环境需要完整打包，再执行：

`mvn -pl org-tribe-system -am package -DskipTests`