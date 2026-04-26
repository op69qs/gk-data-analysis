# 本地信创版与测试机 MySQL 基线版部署验证记录

## 背景

- 本地使用仓库内 `deploy-package` 启动信创改造后的 Vastbase 版本。
- 测试服务器 `cui02-t` 使用 `document/生产/opt（数据分析平台程序）.zip` 中的老生产版，数据库切到测试机本机 MySQL `127.0.0.1:3308`。
- 测试服务器 Redis 使用本机 `127.0.0.1:6379`。
- 测试服务器 Eureka 使用本机 `http://eureka:aml@localhost:8761/eureka/`。

## 本地改动

### 1. 本地信创版启动脚本

- 更新 `deploy-package/bin/start-all.sh`
  - 增加 `/opt/bin/java/jdk1.8.0_202/bin/java` 兜底。
  - 增加按模块默认堆限制，避免 Java 进程占满内存。
  - 增加 WSL 下 `java.exe` 路径兼容分支。
- 新增 `deploy-package/bin/start-all.ps1`
  - 直接在 Windows 下用 `java.exe` 启动 6 个模块。
  - 复用按模块限堆策略。
  - 生成 `run/*.pid` 和 `logs/*/console.log` / `console.err.log`。
  - 默认优先选择本机 JDK8，避免误用 JDK21 导致 Spring Boot 2.0/CGLIB 启动失败。

### 2. 本地信创版启动结果

- 6 个模块均能在 Windows 下拉起：
  - `dwbi-statistical-analysis`
  - `dwbi-system-docking`
  - `fixedReport`
  - `indicatorsLibv-1.0`
  - `org-tribe-system`
  - `seo`
- 网关 `http://127.0.0.1:9090/` 返回 200。
- 关键模块日志已出现 `Started ...Application` / `Tomcat started on port(s)`。

### 3. 本地信创版页面验证结论

- 使用账号 `admin / Ysyyrps4` 可登录。
- `sys/permission/getUserPermissionByToken` 返回的菜单树与测试机基线版一致。
- 但本地前端存在明显路由异常：
  - 登录后页面 URL 会落到 `/dashboard/analysis#/...`。
  - 叶子菜单路由切换后页面主体大多只显示“首页”壳子，没有渲染实际业务页面。
  - 这不是权限接口缺失；菜单树已成功返回。
- 因此，本地信创版当前不能完成与基线版等价的逐菜单页面级验证，问题集中在前端路由/页面渲染层，而不是登录鉴权或菜单权限接口层。

## 测试服务器改动

### 1. 远端部署路径

- 部署到：`/root/gk-data-analysis-prod`
- 使用自建启动脚本：`/root/gk-data-analysis-prod/bin/start-all.sh`
- 启动模块：
  - `org-tribe-system`
  - `fixedReport`
  - `indicatorsLib`
  - `seo`

### 2. 远端配置改动

- `fixedReport/application-dev.yml`
  - MySQL 改为 `127.0.0.1:3308`
  - 密码改为 `1i&Lutjh2#RtC&`
  - Eureka 改为 `http://eureka:aml@localhost:8761/eureka/`
- `indicatorsLib/application-dev.yml`
  - MySQL 改为 `127.0.0.1:3308`
  - 密码改为 `1i&Lutjh2#RtC&`
  - Eureka 改为 `http://eureka:aml@localhost:8761/eureka/`
- `org-tribe-system/application-dev.yml`
  - MySQL 改为 `127.0.0.1:3308`
  - 密码改为 `1i&Lutjh2#RtC&`
  - Eureka 改为 `http://eureka:aml@localhost:8761/eureka/`
- `seo/application-dev.yml`
  - Eureka 改为 `http://eureka:aml@localhost:8761/eureka/`
- `seo/application.properties`
  - `spring.datasource.default.url` 改为 `jdbc:mysql://127.0.0.1:3308/jeecg-boot-os?...`
  - `spring.datasource.default.username=root`
  - `spring.datasource.default.password=1i&Lutjh2#RtC&`

### 3. 远端启动与访问结果

- 测试机网关通过本地隧道 `http://127.0.0.1:19090/` 可访问并登录。
- `admin / Ysyyrps4` 登录成功。
- 菜单权限接口可正常返回完整菜单树。
- 通过浏览器逐个遍历叶子菜单路由后，结果如下。

## 菜单验证结果

### 1. 基线版可正常打开并返回页面数据的菜单

- 首页
- 菜单管理
- 部门管理
- 角色管理
- 用户管理
- 数据字典
- 角色维护
- 国库
- 核算主体
- 征收机关
- 报告管理
- 指标查询
- 指标管理
- 公共指标管理
- 分行业报表
- 企业报表
- 企业排名
- 动态刷数

### 2. 基线版页面可打开，但数据为空或为空表的菜单

- 预算科目
- 地区
- 指标查询（部分区域显示“暂无数据”）
- 企业排名
- 动态刷数

这些页面在基线版中可进入，属于“页面可用但当前数据为空”的状态。

### 3. 基线版明确报 500 的菜单

以下 4 个菜单在基线版中就存在后端 500，不是信创版新增回归：

- 数据源维护
  - `/seo/dataSourceController/getDataSourceEnumSelect`
  - `/seo/dataSourceController/getDataSource`
- 数据表维护
  - `/seo/dataTableController/getDataSourceTree`
  - `/seo/dataAuxiliaryController/getDataSourceSelection`
  - `/seo/dataAuxiliaryController/getFirstClassifySelection`
- 数据查询
  - `/seo/seoController/getTableName`
- 维度表
  - `/seo/dimensionController/getMainPage`

这些页面前端仍可进入，但会出现 500 和“暂无数据”。

### 4. 信创版相对基线版的主要新增问题

- 本地信创版登录成功、菜单接口成功，但绝大多数叶子菜单页面没有实际渲染业务内容，只保留“首页”壳子。
- 这与测试机基线版逐菜单可进入并展示表格/筛选区的行为不一致。
- 当前最可疑的是前端路由落点异常：`/dashboard/analysis#/...`。

## 收集到的关键错误日志

### 1. 远端基线版

- `fixedReport`
  - 存在 MySQL `only_full_group_by` SQL 异常。
- `org-tribe-system`
  - 存在 `dmcode.CM_GUOKU_AREA_CODE` 表不存在。
  - 存在 `adm.exec_shell_task` 表不存在。
- `seo`
  - 对综合查询相关接口持续返回 500。

### 2. 本地信创版

- `dwbi-statistical-analysis`
  - 访问外部 Eureka `192.168.160.244:8761` 超时。
  - 该问题来自“本地外部依赖保持不变”的约束，不影响本地网关和模块本身已启动。
- `org-tribe-system`
  - 登录后存在一次 `Token...` 相关空指针日志。
- 前端页面
  - 本地信创版存在前端路由/页面渲染异常，导致逐菜单 UI 校验无法与基线版对齐。

## 结论

- 测试机 MySQL 基线版已部署并可登录，基线行为已跑通并可作为对照组。
- 本地信创版后端 6 模块已启动，网关可登录，菜单权限接口也正常。
- 但本地信创版存在前端路由/菜单页面渲染异常，导致逐菜单页面功能验证无法达到与基线版一致的效果。
- 同时，基线版自身也保留 4 个 `seo` 相关菜单接口 500 和若干 MySQL 侧历史问题，这些不应误判为信创版新增回归。