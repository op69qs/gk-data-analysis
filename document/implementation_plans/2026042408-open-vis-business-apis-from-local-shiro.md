# 放开 vis 业务接口的本地 Shiro 校验

## 背景

在修复 `PageData` 空请求体问题后，vis 后端日志里仍然出现：

- `AuthenticationException: token为空!`

进一步对照后发现：

1. 主前端 `org-tribe-view/src/utils/request.js` 的 axios 拦截器会统一注入 `X-Access-Token`。
2. 因此，通过 `postAction/getAction` 发出的常规接口请求，理论上会携带 token。
3. vis 后端当前 `application-dev.yml` 只对白名单放开了：
   - `/schemeInfo/getAllPage`
   - `/queryData/**`
   - `/queryTableData/**`
   - `/queryMapData/**`

这意味着剩余的 vis 业务接口仍会继续走 9082 本地 Shiro 校验，例如：

- `/schemeInfo/**`
- `/pageInfo/**`
- `/pageSub/**`
- `/pageWhere/**`
- `/gallery/**`
- `/bussType/**`
- `/GuokuController/**`

而这与当前整合目标是冲突的。

## 判断

本次整合目标已经明确：

> vis 原有登录、用户、权限、角色管理取消，统一并入 GK 主系统。

因此 9082 这个 vis 后端不应再保留一套独立的业务接口 Shiro 鉴权策略。继续依赖它本地验 token，只会让集成状态停留在“页面迁过去了，但后端仍半独立鉴权”的中间态。

## 修改

修改两个文件：

1. `vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-dev.yml`
2. `vis-screen-backend/jeecg-boot-module-system/target/application-dev.yml`

把 `jeecg.shiro.excludeUrls` 从只放开部分查询接口，扩展为整组 vis 业务控制器：

- `/schemeInfo/**`
- `/pageInfo/**`
- `/pageSub/**`
- `/pageWhere/**`
- `/gallery/**`
- `/bussType/**`
- `/GuokuController/**`
- `/queryData/**`
- `/queryTableData/**`
- `/queryMapData/**`

## 结果

这样调整后，vis 自身业务接口不再依赖 9082 本地 Shiro 单独验 token，符合“vis 鉴权能力被 GK 主系统接管”的集成方向。

同时也避免了以下情况继续触发 `token为空`：

1. 某些页面调用没有经过统一 axios 封装。
2. 某些浏览器直连、探测或预览请求没有带 `X-Access-Token`。
3. 某些 vis 旧逻辑仍保留对 9082 的直连访问。

## 验证

已完成：

1. source 配置与当前 runtime `target` 配置已同步。
2. YAML 静态校验通过，无新增配置错误。

待运行态确认：

1. 重启 9082 后，确认同类 vis 业务接口不再抛出 `token为空`。
2. 继续联调 GK 前端下的方案、页面、图库、国库等管理页面。