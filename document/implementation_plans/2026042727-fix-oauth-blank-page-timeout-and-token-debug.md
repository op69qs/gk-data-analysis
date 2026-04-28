# 修复 OAuth 跳转后白页与换 token 400 的可观测性问题

## 问题现象
- 门户点击进入子系统后，页面提示成功但出现白页/停留异常。
- 后端日志出现 `NexusOAuthController.exchangeCodeForToken` 400。
- 权限接口只看到进入日志，后续结果日志缺失，疑似调用门户权限接口阻塞。

## 关键排查结论
1. 浏览器侧复现：
- 访问 `http://localhost:3000/` 登录后，工作台可看到 `GK_DATA_ANALYSIS`。
- 系统列表接口返回该系统配置：
  - `baseUrl=http://localhost:9090`
  - `routePrefix=/org-tribe`
  - `embedMode=newwindow`
- 直接访问 `http://localhost:3000/org-tribe` 返回 404（Whitelabel Error Page）。
- 访问 `http://localhost:9090/` 时，`/sys/permission/getUserPermissionByToken` 请求失败并跳回登录页。

2. 后端链路风险：
- `SysPermissionController.fetchPortalPermissionCodes` 使用 `RestTemplate` 无超时，若上游慢/阻塞，前端首屏菜单初始化会长期等待，表现为白页。
- `exchangeCodeForToken` 400 时之前没有打印响应体，无法快速判断是 `redirect_uri` 不一致、`code` 复用还是客户端配置错误。

## 本次代码改动

### 1) RestTemplate 增加超时
文件：`org-tribe-system/src/main/java/org/jeecg/config/HttpClientConfig.java`
- connectTimeout: 5000ms
- readTimeout: 10000ms

### 2) 门户权限拉取增加耗时与状态日志
文件：`org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`
- 拉取前打印：portalUserId/sysCode/requestUrl
- 拉取后打印：status/costMs
- 异常时打印：costMs/errorType/error

### 3) OAuth 换 token 失败输出响应体
文件：`org-tribe-system/src/main/java/org/jeecg/modules/oauth/controller/NexusOAuthController.java`
- 捕获 `HttpClientErrorException`
- 记录 `status` 与响应体（最长 500 字符）

## 预期收益
- 避免门户权限拉取阻塞导致首屏长时间白页。
- 能直接从日志判断 400 根因，不再靠猜。

## 验证建议
1. 重启 `org-tribe-system`。
2. 重走门户登录。
3. 关注日志：
- `开始拉取门户权限...`
- `门户权限拉取完成，status=..., costMs=...`
- `Nexus token exchange failed, status=..., responseBody=...`（仅失败时）
4. 若仍白页，优先核对 BFF 路由前缀 `/org-tribe` 代理是否真实可达。