# 门户 OAuth 菜单路由修复

## 问题

门户 OAuth 回调成功后，分析平台已经获得本地 JWT 且权限接口返回成功，但前端随即调用退出接口并回到登录页。

门户同步的目录菜单允许 `component` 为空。前端在动态菜单转路由时对该字段直接调用 `indexOf`，同步异常会被路由守卫的 Promise `catch` 捕获，并触发 `Logout`。

## 修改

动态路由转换在 `component` 为空时使用既有的 `layouts/RouteView`。该组件适用于只承载子菜单的目录节点，避免空值中断权限初始化；带有组件的现有菜单不受影响。

## 验证

1. `npm run build` 成功完成。
2. `mvn -pl org-tribe-system process-resources` 成功，将前端静态资源同步到运行服务的 classpath 目录。
3. 使用门户用户 `lj_001` 进行真实 OAuth 浏览器回归：OAuth 回调和权限接口均返回 `200 success`，权限菜单数量为 11，未调用 `/sys/logout`，最终页面为 `/dashboard/analysis`。

## 已知环境问题

`npm run lint -- --no-fix` 无法执行，因为项目缺少 ESLint 配置文件；该问题在本次修改前已存在，改用生产构建完成语法和打包校验。