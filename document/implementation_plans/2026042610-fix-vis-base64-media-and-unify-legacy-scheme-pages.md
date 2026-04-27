# 修复 vis base64 缩略图与旧指标页统一收口

## 本次目标

- 修复 `/vis/bigscreen/pages` 中 base64 缩略图被错误拼成 `/vis/api/sys/common/static/data:image...` 的问题。
- 让原有“指标库/指标查询”页面与 vis 菜单下“指标库方案”始终展示同一内容页。
- 判别 vis backend 日志里的 Redis / token 告警是否等价于真实鉴权失败。

## 修改内容

### 1. base64 媒体地址归一修复

- 修改 `org-tribe-view/src/utils/visMedia.js`。
- 对以下地址直接原样返回，不再进入 vis 静态资源路径拼接逻辑：
  - `data:`
  - `blob:`
  - `http://`
  - `https://`
  - `//`

这样 `thumbnail` 或 `content` 本身就是 `data:image/png;base64,...` 时，前端会直接把它作为图片源使用，而不会继续被错误改写成：

```text
/vis/api/sys/common/static/data:image/png;base64,...
```

### 2. 旧指标页收成 vis 统一方案页

- 将以下历史页面改成薄包装组件，直接复用 `vis/SchemeList`：
  - `org-tribe-view/src/views/statistics/schemeIndex.vue`
  - `org-tribe-view/src/views/statistics/indexLibrary.vue`

这样无论后台菜单仍然返回旧 component，还是用户直接打开旧页面入口，最终都只会渲染 vis 的统一方案列表页，不再出现旧页和 vis 页两套实现分叉。

## 运行态判别结论

### 1. 页面管理缩略图 400 的根因

- 浏览器运行态当前仍在请求：

```text
http://127.0.0.1:9090/vis/api/sys/common/static/data:image/png;base64,...
```

- 这与源码修复后的逻辑不一致，说明 9090 当前仍在服务旧前端 bundle。
- 也就是说：
  - 源码层修复已经完成。
  - 运行页尚未吃到新 bundle，因此浏览器里仍表现为旧错误。

### 2. token 告警是否等于鉴权失败

- 使用当前浏览器里的 `X-Access-Token` 调用：

```text
POST /vis/api/pageInfo/getPage
```

- 请求能够正常进入业务接口并返回 `success` 数据，不是被鉴权层拦成“token失效”。
- 因此当前日志中的：
  - `Redis cache get failed`
  - `Redis cache put failed`
  - `Redis token校验失败，回退为JWT验签`

表示的是 Redis `192.168.160.30:6379` 不可达后的降级告警，不等价于本次请求真实鉴权失败。

## 验证结果

### 静态校验

以下文件 `get_errors` 均为 `No errors found`：

- `org-tribe-view/src/utils/visMedia.js`
- `org-tribe-view/src/views/statistics/schemeIndex.vue`
- `org-tribe-view/src/views/statistics/indexLibrary.vue`

### 浏览器校验

- `/vis/bigscreen/pages` 当前页面前 10 张缩略图的 `src` 仍全部带有：

```text
/vis/api/sys/common/static/data:image
```

- 该结果再次证明线上 9090 使用的是旧 bundle，而不是新源码。

### 接口校验

- `POST /vis/api/pageInfo/getPage` 使用当前登录 token 能正常返回页面数据。
- 说明 JWT 回退链路当前可用，问题焦点仍然是 Redis 连通性告警，而不是这次请求被判无效。

## 结论

- base64 图片路径问题已经在源码根因处修复。
- 旧“指标库/指标查询”页面已经被强制统一到 vis 方案页，不再依赖路由映射表是否完全命中。
- token 日志目前反映的是 Redis 不可达后的降级告警，不是接口层面的真实鉴权失败。
- 下一步如果要看到页面管理和图库的新行为，必须重新生成并部署 `org-tribe-view` 的前端 bundle。