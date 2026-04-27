# vis 集成恢复与旧平台接口停用实施记录

## 背景

- 目标不是做占位修复，而是恢复误删前已经打通过的 vis 集成效果。
- 需要同时满足两类约束：
  - GK 主平台内的 vis 页面、图片、模板、字典来源恢复到历史可用状态。
  - vis-screen-backend 自带的旧登录、用户、角色、权限入口停止对外提供能力，统一由 GK 平台承接。

## 本次改动范围

### 1. 前端页面与路由恢复

- 恢复模板管理页真实模板图展示，不再使用占位卡片。
- 修复图库和页面管理中的媒体地址归一化，兼容相对路径图片。
- 页面管理恢复为卡片网格缩略图布局，避免表格缩略图导致比例失调。
- 指标查询页内部跳转统一改到 `/vis/index-library`。
- 保留旧菜单组件和路径映射，使历史 vis 菜单仍能落到现有 GK 页面。

涉及文件：

- `org-tribe-view/src/views/vis/TemplateList.vue`
- `org-tribe-view/src/views/vis/GalleryList.vue`
- `org-tribe-view/src/views/vis/PageList.vue`
- `org-tribe-view/src/views/statistics/schemeIndex.vue`
- `org-tribe-view/src/utils/util.js`

### 2. vis 字典来源恢复到 vis backend

- `/vis/system/dict` 页面不再走 GK 主系统通用字典接口。
- 前端根据当前路由是否为 `/vis/system/dict`，切换到 vis backend 的 vis 模块专用接口：
  - 列表：`/vis/api/visDict/list`
  - 导出：`/vis/api/visDict/exportXls`
- vis backend 新增 vis 专用字典查询与导出接口，并对白名单字典编码做固定排序。

涉及文件：

- `org-tribe-view/src/views/system/DictList.vue`
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/controller/VisDictController.java`
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/util/VisLegacyDictScope.java`

### 3. 停用 vis 旧平台入口

- 停用以下旧平台入口，统一返回 `410 Gone` 和固定提示：`vis旧平台入口已停用，请从分析平台访问`。
- 精确停用路径：
  - `/sys/login`
  - `/sys/logout`
  - `/sys/mLogin`
  - `/sys/phoneLogin`
- 前缀停用路径：
  - `/thirdLogin/**`
  - `/sys/user/**`
  - `/sys/role/**`
  - `/sys/permission/**`
  - `/sys/sysUserAgent/**`
  - `/sys/sysDepartRole/**`
  - `/sys/sysDepartPermission/**`
- vis 业务接口例如 `/sys/dict/visList` 不在停用范围内，继续走原有鉴权链路。

实现策略：

- Shiro 过滤链上将上述旧平台路径放行为 `anon`，避免先被 JWT/会话校验拦截。
- controller 层通过 `RestControllerAdvice` 按请求路径统一短路并返回 `410`。
- 早期失败的实验性配置类已移除，避免 fat jar 运行时 bean 重名冲突。

涉及文件：

- `vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroConfig.java`
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/system/controller/advice/LegacyPlatformOfflineAdvice.java`

## 关键验证

### 构建验证

在 `vis-screen-backend` 目录执行：

```powershell
mvn -pl jeecg-boot-module-system -am -DskipTests package
```

结果：`BUILD SUCCESS`，生成 fat jar：

- `vis-screen-backend/jeecg-boot-module-system/target/jeecg-boot-module-system-2.3.0.jar`

### 运行态验证

使用 fat jar 启动，避免 `spring-boot:run` 的类路径差异影响结果：

```powershell
Set-Location 'd:\document\c-project\project-code\project-02\gk-data-analysis\vis-screen-backend'
java -jar .\jeecg-boot-module-system\target\jeecg-boot-module-system-2.3.0.jar
```

启动成功标志：

- `Tomcat started on port(s): 8081`
- `Started application`
- `Registering application VIS with eureka with status UP`

### 接口回归结果

1. `POST http://127.0.0.1:8081/sys/login`

```http
HTTP/1.1 410
{"success":false,"message":"vis旧平台入口已停用，请从分析平台访问"}
```

2. `GET http://127.0.0.1:8081/sys/user/list`

```http
HTTP/1.1 410
{"success":false,"message":"vis旧平台入口已停用，请从分析平台访问"}
```

3. `GET http://127.0.0.1:8081/sys/dict/visList`

```http
HTTP/1.1 500
{"message":"Token失效，请重新登录", ...}
```

说明：

- 前两条已证明旧平台登录与用户管理入口在 fat jar 运行态下确实被停用。
- 第三条没有变成 `410`，说明 vis 字典接口没有被误伤，仍然保留在业务鉴权链路中，这正是目标行为。

## 结论

- vis 集成的模板图、图库图、页面缩略图和字典来源已按历史实现方向恢复。
- vis 旧平台登录、用户、角色、权限相关入口已在运行态上停用。
- 旧平台停用的最终可信验证方式是 fat jar 运行态，不应再用单模块 `spring-boot:run` 结果替代。

## 后续建议

- 下一步直接启动 GK 主平台做浏览器联调，重点回归：
  - `/vis/gallery`
  - `/vis/system/dict`
  - `/vis/bigscreen/templates`
  - `/vis/bigscreen/pages`
- 若需要进一步收口旧平台接口，可继续按同样方式回归 `/sys/role/**`、`/sys/permission/**` 等剩余路径，但当前停用策略已经覆盖这些前缀。