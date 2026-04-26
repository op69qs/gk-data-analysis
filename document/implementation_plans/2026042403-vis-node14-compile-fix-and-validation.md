# vis 前端 Node14 编译修复与验证记录

## 背景

- 用户补充本机可用 Node 14 路径：`org-tribe-system/target/frontend/node/node.exe`
- 目标是继续验证 vis 并入后的系统管理、图库和大屏设置页面。

## 本次处理

### 1. 切换到 Node 14 启动 `org-tribe-view`

使用以下方式启动开发服务器：

```powershell
Set-Location org-tribe-view
$env:Path = '.../org-tribe-system/target/frontend/node;' + $env:Path
& '.../org-tribe-system/target/frontend/node/node.exe' .\node_modules\@vue\cli-service\bin\vue-cli-service.js serve
```

结论：Node 14 环境下，之前的 webpack4/OpenSSL 兼容问题不再出现。

### 2. 修复真实编译错误

开发服务器首次编译失败，报错点为：

- `org-tribe-view/src/views/system/BusinessTypeList.vue`

错误信息：

- `Can't resolve '~@assets/less/common.less'`

进一步排查后确认有两个根因：

1. 当前仓库缺少被多个旧页面共同引用的 `src/assets/less/common.less`
2. `BusinessTypeList.vue` 的样式块写成了默认 `css`，但内部却导入了 less 文件

### 3. 实际修复

- 新增：`org-tribe-view/src/assets/less/common.less`
  - 从原 vis 前端补回通用列表/弹窗样式，兼容现有大量页面的 `@import '~@assets/less/common.less'`
- 修改：`org-tribe-view/src/views/system/BusinessTypeList.vue`
  - 将样式块改为 `lang="less" scoped`

## 验证结果

### 编译验证

修复后，`org-tribe-view` 在 Node 14 下已可成功启动：

- 本地地址：`http://localhost:3000/`

当前仅剩已有页面的 autoprefixer warning，不影响运行：

- `src/views/system/RoleList.vue` 中的老注释写法告警

### 浏览器验证

浏览器已打开登录页，但登录链路未能继续验证，原因如下：

- 登录请求返回 `404`
- 当前阻塞点在本地可用后端/网关代理，而不是这次新增的 vis 页面代码

这意味着：

- `BusinessTypeList` / `TreasuryList` / `GalleryList` 相关前端代码已通过编译层验证
- 菜单点击后的运行级验证，仍依赖可用的后端登录与权限接口

## 结论

本次已经把“页面代码无法编译”这层问题解决掉。

剩余未闭环部分不是前端迁移代码本身，而是本地运行环境中的登录/后端代理链路。后续如果要继续做浏览器级回归，优先补通可用后端，再验证：

- 系统管理：数据字典、国库、业务类型
- vis：图库、大屏设置、旧路径兼容入口