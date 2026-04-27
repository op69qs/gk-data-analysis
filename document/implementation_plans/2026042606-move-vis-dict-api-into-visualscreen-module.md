# 将 vis 字典接口迁入 visualScreen 模块

## 目的

- 用户明确要求：所有来自 vis 菜单的后台 API 都应在 vis 模块内处理，而不是继续挂在 `system` 模块控制器下。
- 之前为了先恢复 `/vis/system/dict` 页面可用，临时把 vis 专用列表与导出接口挂到了 `SysDictController` 下。
- 本次调整只改接口归属边界，不改 vis 字典白名单口径和返回结构。

## 调整内容

### 1. 后端接口归属迁移

- 从 `org.jeecg.modules.system.controller.SysDictController` 中移除：
  - `GET /sys/dict/visList`
  - `GET /sys/dict/visExportXls`
- 在 `org.jeecg.modules.visualScreen.controller.VisDictController` 中新增 vis 专属接口：
  - `GET /visDict/list`
  - `GET /visDict/exportXls`

通过 GK 网关后的前端实际访问地址为：

- `/vis/api/visDict/list`
- `/vis/api/visDict/exportXls`

### 2. helper 归属迁移

- 将 `VisLegacyDictScope` 从 `org.jeecg.modules.system.util` 迁到 `org.jeecg.modules.visualScreen.util`。
- 白名单 dict code、固定排序规则、分页切片逻辑保持不变。

### 3. 前端路径同步

- `org-tribe-view/src/views/system/DictList.vue`
  - `/vis/system/dict` 路由下的列表地址改为 `/vis/api/visDict/list`
  - 导出地址改为 `/vis/api/visDict/exportXls`
- `org-tribe-view/src/api/visScreen.js`
  - 补充 vis 字典相关路径常量，保持 vis API 入口集中化。

## 验证

### 编译验证

在 `vis-screen-backend` 目录执行：

```powershell
mvn -pl jeecg-boot-module-system -am -DskipTests compile
```

结果：`BUILD SUCCESS`

### 打包验证

在 `vis-screen-backend` 目录执行：

```powershell
mvn -pl jeecg-boot-module-system -am -DskipTests package
```

结果：打包成功，新的 fat jar 已生成。

## 结论

- vis 菜单下的数据字典请求已不再由 `system` 模块里的 `SysDictController` 承接。
- vis 专用字典接口现在归属于 `visualScreen` 模块控制器，符合“vis 菜单后台 API 放在 vis 模块处理”的要求。
- 本次变更没有改动字典返回结构和筛选口径，属于接口边界收口，不是业务口径变更。