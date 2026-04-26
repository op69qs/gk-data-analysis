# 修复 vis 查询链路空请求体空指针

## 现象

整合进来的 vis 后端在访问 `queryData/getStructure` 时出现启动期报错，核心异常链路如下：

1. `PageData(HttpServletRequest)` 在请求没有 query 参数时，转而读取 request body。
2. 当 body 为空时，`JSONObject.parseObject("")` 返回 `null`。
3. 旧实现继续执行 `jsonObject.toJSONString()`，导致 `PageData` 内部 `map` 维持为 `null`。
4. 随后 MyBatis 在解析 `#{params.id}` 或生成缓存键时调用 `PageData.get()`，再次触发空指针。

报错栈里同时能看到两个直接症状：

- `PageData.getRequestJSONObject(PageData.java)` 空指针
- `PageData.get(PageData.java)` 在 MyBatis `createCacheKey` 阶段空指针

## 根因

根因有两层：

1. `PageData` 对空请求体不安全，内部 `map` 可以保持为 `null`。
2. vis 查询服务层对页面区块 `id` 缺失没有做参数校验，导致非法请求直接下沉到 mapper。

这不是 `getStructure` 单个接口的问题，而是所有通过 `queryDataService.getPageSub(pd)` 取区块配置的接口共享的公共缺陷。

## 修复

### 1. `PageData` 空安全

修改文件：`vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/util/PageData.java`

处理内容：

1. `map` 默认初始化为空 `HashMap`，不再允许保持 `null`。
2. `HttpServletRequest` 构造器对 `request == null` 做保护。
3. 读取 request body 时，如果 body 为空或 JSON 解析结果为空，直接返回空 map。
4. `JSONObject` 构造器改为 `putAll`，避免再次覆盖成 `null`。
5. `get()` 增加兜底保护，确保即使出现异常状态也返回 `null` 而不是继续空指针。

### 2. `getPageSub` 参数校验前移

修改文件：`vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/service/impl/QueryDataServiceimpl.java`

处理内容：

1. 在服务层统一校验 `id` 是否为空。
2. 如果 `id` 缺失，直接抛出明确异常：`缺少页面区块id参数`。
3. 如果根据 `id` 查不到区块配置，抛出明确异常：`未找到对应页面区块配置`。

这样可以把“参数缺失”从 MyBatis 深层空指针，提升成服务层可理解的业务异常。

## 回归保护

新增测试文件：

- `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/util/PageDataTest.java`

覆盖两个最小场景：

1. 空 JSON body 不再让 `PageData` 进入非法状态。
2. 正常 JSON body 能继续解析出 `id`。

## 验证

已完成：

1. 在 Java 8 环境下执行模块编译，`jeecg-boot-module-system` 编译通过。
2. 编译产物已更新到模块 `target/classes`。

受环境/构建配置影响未完全完成：

1. 模块当前 Maven/Surefire 配置默认跳过测试，即使指定 `PageDataTest` 也仍显示 `Tests are skipped`。
2. 因此本轮拿到的是“Java 8 编译通过”的可执行验证，而不是测试执行通过。

## 备注

日志末尾还存在一组独立问题：

- `AuthenticationException: token为空!`

这一组是鉴权链路问题，不属于本次 `PageData/getPageSub` 空指针修复范围。