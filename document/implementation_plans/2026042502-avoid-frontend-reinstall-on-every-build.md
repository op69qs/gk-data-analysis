# 避免每次 Maven 构建都重装前端依赖

## 问题

`org-tribe-system` 在 Maven 构建时会进入 `org-tribe-view` 执行前端流程。

原有配置存在两个会强制全量重装依赖的点：

1. `initialize` 阶段先删除 `org-tribe-view/node_modules`
2. 后续使用 `npm ci`

这意味着即使 `package.json` 和 `package-lock.json` 都没变化，每次编译也会重新安装依赖。

## 修改

在 `org-tribe-system/pom.xml` 中做两处调整：

1. 保留删除 `dist`，但不再删除 `node_modules`
2. 将 `npm ci --legacy-peer-deps` 调整为 `npm install --legacy-peer-deps --prefer-offline`

## 结果

这样重复执行 Maven 构建时：

- 已存在的 `node_modules` 会被复用
- 依赖未变化时，`npm install` 只做校验，不再全量重装
- 当 `package.json` 或 `package-lock.json` 变化时，`npm install` 会按变更补装或更新依赖

## 验证建议

连续执行两次：

`mvn -pl org-tribe-system -am generate-resources`

第二次构建应不再出现删除 `node_modules`，并且 `npm install` 会明显比首次更快，通常显示 `up to date` 或仅处理少量变更。