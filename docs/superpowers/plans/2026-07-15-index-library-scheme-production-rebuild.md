# 指标库方案生产契约重建 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将“可视化大屏 → 指标库方案”从代理简化实现恢复为生产 JAR 对应的方案查询、删除、四类图表真实预览和预览后保存流程。

**Architecture:** 在 `vis-screen-backend` 中恢复生产 `indexlib` 控制器、服务与 Mapper 契约，并复用现有 `GalleryService` 保存图库；前端用独立 API 和纯函数适配生产大写字段，以一个列表页、一个转图弹窗和一个 ECharts 预览组件完成交互。数据库脚本只核验和补齐生产既有对象，不新增业务表。

**Tech Stack:** Java 8、Spring Boot 2.2、MyBatis、JUnit 4、Vastbase/PostgreSQL 兼容 SQL、Vue 2.6、Ant Design Vue 1.4、ECharts 4.7、Node.js 14。

## Global Constraints

- 生产边界以 `document/指标库方案页面fix/jeecg-boot-module-system-2.3.0.jar` 为准。
- `jeecg-boot-parent.zip` 只补源码，不移植 `IndexStripController`、`IndexSpecialController`、`AreaUnitController`、`SourceService` 或其他地区扩展。
- 页面只开放柱状图、折线图、饼图、柱状折线图；不开放地图、条形图、BigNumber。
- 保存前必须完成当前配置的真实数据预览；配置变化后必须重新预览。
- 不新增业务表；只核验或兼容同步 `vs_lib_index_scheme`、`vs_gallery_info`、`sys_user`、`lib_index_relation` 和 `visual_screen.f_get_IndexName`。
- 后端保持 Java 8 语法；前端保持 Vue 2 Options API，不引入新依赖。
- 每次提交只包含当前任务列出的文件，不提交工作树中已有的无关未跟踪文件。

---

## File Structure

### Backend production contract

- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/controller/IndexSchemeController.java`: 生产方案列表、删除、指标信息接口。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/controller/IndexBarLineController.java`: 柱状、折线、柱折预览和保存接口。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/controller/IndexPieController.java`: 饼图预览和保存接口。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexSchemeService.java`: 方案读取、执行和删除边界。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexRelationService.java`: 批量指标元数据边界。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/impl/IndexSchemeServiceImpl.java`: 方案服务实现。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/impl/IndexRelationServiceImpl.java`: 指标元数据服务实现。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexChartService.java`: 两个生产图表控制器共享的预览和图库保存边界。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/impl/IndexChartServiceImpl.java`: 只实现四类图表的数据组装和保存。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexChartDataAssembler.java`: 将真实查询行转换为生产笛卡尔或饼图响应。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/IndexSchemeMapper.java`: 生产方案 SQL 接口。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/IndexRelationMapper.java`: 指标元数据 SQL 接口。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/xml/IndexSchemeMapper.xml`: Vastbase 兼容的生产方案 SQL。
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/xml/IndexRelationMapper.xml`: 批量指标信息 SQL。
- 删除当前代理实现 `IndexLibrarySchemeController`、`IndexLibrarySchemeService*`、`IndexLibrarySchemeMapper*`，避免两套语义并存。

### Existing gallery contract

- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/GalleryMapper.xml`: 改为生产显式列名插入。
- `document/psql/vastbase/2026071501-index-library-scheme-production-contract.sql`: 幂等核验/补列/函数同步脚本。

### Frontend

- `org-tribe-view/src/api/indexLibraryScheme.js`: 生产接口 URL 的唯一封装。
- `org-tribe-view/src/utils/indexLibraryScheme.js`: 图表类型、字段归一化、条件解析、预览/保存请求构造纯函数。
- `org-tribe-view/src/views/vis/IndexLibraryList.vue`: 现场列表和查询。
- `org-tribe-view/src/views/vis/modules/IndexLibraryConvertModal.vue`: 现场转图配置、状态机和保存流程。
- `org-tribe-view/src/views/vis/modules/IndexLibraryChartPreview.vue`: 四类 ECharts option 生成和渲染。
- `org-tribe-view/tests/indexLibraryScheme.test.mjs`: 纯函数和 Vue 源码契约测试。

---

### Task 1: 锁定生产 JAR 和数据库 SQL 契约

**Files:**
- Create: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib/IndexSchemeProductionContractTest.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib/mapper/IndexSchemeMapperContractTest.java`

**Interfaces:**
- Consumes: 生产 JAR 中三个控制器的方法集合和 `IndexSchemeMapper.xml` 的查询结构。
- Produces: 后续任务必须满足的类名、方法名、Mapper id、生产表名和图库字段断言。

- [ ] **Step 1: 写生产类签名失败测试**

创建 JUnit 4 测试，从项目 classpath 反射目标类并断言只暴露生产方法：

```java
@Test
public void productionControllersExposeOnlyApprovedMethods() throws Exception {
    assertPublicMethods("org.jeecg.modules.indexlib.controller.IndexSchemeController",
            "getIndexInfo", "deleteScheme", "selectSchemeTable");
    assertPublicMethods("org.jeecg.modules.indexlib.controller.IndexBarLineController",
            "saveIndexBarLine", "getIndexBarLineData");
    assertPublicMethods("org.jeecg.modules.indexlib.controller.IndexPieController",
            "saveIndexPie", "getIndexPieData");
}

private void assertPublicMethods(String className, String... expected) throws Exception {
    Set<String> actual = Arrays.stream(Class.forName(className).getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .map(Method::getName)
            .collect(Collectors.toSet());
    assertEquals(new HashSet<>(Arrays.asList(expected)), actual);
}
```

- [ ] **Step 2: 写 Mapper 失败测试**

按现有 Mapper contract test 的资源读取方式加载 `IndexSchemeMapper.xml`，断言：

```java
assertTrue(select("selectSchemeTable").contains("visual_screen.vs_lib_index_scheme"));
assertTrue(select("selectSchemeTable").contains("AS \"SCHEME_DESCR\""));
assertTrue(select("selectSchemeTable").contains("AS \"ADD_DATE\""));
assertTrue(select("selectSchemeTable").contains("AS \"realname\""));
assertTrue(select("selectSchemeTable").contains("#{params.begin_time}"));
assertTrue(select("selectSchemeTable").contains("#{params.end_time}"));
assertTrue(delete("deleteSchemeById").contains("#{params.schemeId}"));
```

- [ ] **Step 3: 运行测试确认红灯**

Run:

```bash
cd vis-screen-backend
rtk mvn -pl jeecg-boot-module-system -Dtest=IndexSchemeProductionContractTest,IndexSchemeMapperContractTest test
```

Expected: FAIL，原因是 `org.jeecg.modules.indexlib` 类和 Mapper 资源尚不存在。

- [ ] **Step 4: 保存测试提交**

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib
git commit -m "test(vis): lock index scheme production contract"
```

---

### Task 2: 恢复生产 IndexSchemeController 方案链路

**Files:**
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/controller/IndexSchemeController.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexSchemeService.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/impl/IndexSchemeServiceImpl.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/IndexSchemeMapper.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/xml/IndexSchemeMapper.xml`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexRelationService.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/impl/IndexRelationServiceImpl.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/IndexRelationMapper.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/mapper/xml/IndexRelationMapper.xml`
- Delete: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/controller/IndexLibrarySchemeController.java`
- Delete: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/service/IndexLibrarySchemeService.java`
- Delete: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/service/impl/IndexLibrarySchemeServiceImpl.java`
- Delete: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/IndexLibrarySchemeMapper.java`
- Delete: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/IndexLibrarySchemeMapper.xml`
- Test: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib/IndexSchemeControllerTest.java`

**Interfaces:**
- Consumes: JSON request fields `name`, `begin_time`, `end_time`, `pageNo`, `pageSize`, `schemeId`, `SCHEME_COLUMS`.
- Produces: `POST /indexSchemeController/selectSchemeTable|deleteScheme|getIndexInfo`；响应固定使用 `result`、`msg`、`rows`、`total`。

- [ ] **Step 1: 写控制器行为失败测试**

使用 Mockito 直接实例化控制器并注入 mock service，覆盖分页映射和删除参数：

```java
@Test
public void listMapsProductionQueryFields() {
    JSONObject request = new JSONObject();
    request.put("pageNo", "2");
    request.put("pageSize", "10");
    request.put("name", "收入");
    request.put("begin_time", "2026-01-01");
    request.put("end_time", "2026-01-31");

    Map<String, Object> response = controller.selectSchemeTable(request);

    ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
    verify(indexSchemeService).selectSchemeTable(captor.capture());
    assertEquals("收入", captor.getValue().getString("schemeDescr"));
    assertEquals(10, captor.getValue().get("page"));
    assertEquals("success", response.get("result"));
}
```

- [ ] **Step 2: 运行控制器测试确认红灯**

Run: `rtk mvn -pl jeecg-boot-module-system -Dtest=IndexSchemeControllerTest test`

Expected: FAIL，控制器尚不存在。

- [ ] **Step 3: 创建 Mapper 和服务接口**

`IndexSchemeMapper` 必须精确提供：

```java
String getAllTrsInfo();
String getAllAreaInfo();
List<Map<String, Object>> execSchemeSql(@Param("params") PageData pd);
Map<String, String> getSchemeInfoById(@Param("params") PageData pd);
int getSchemeCount(@Param("params") PageData pd);
List<Map<String, Object>> selectSchemeTable(@Param("params") PageData pd);
void deleteSchemeById(@Param("params") Map<String, Object> params);
List<Map<String, String>> getIndexNames(@Param("params") PageData pd);
```

`IndexRelationMapper` 只提供当前页面使用的批量查询：

```java
List<Map<String, String>> getBatchIndexInfo(@Param("params") PageData pd);
```

- [ ] **Step 4: 按生产字段创建 Vastbase Mapper SQL**

列表 SQL 使用显式大写别名并避免 MySQL 用户变量：

```xml
<select id="selectSchemeTable" resultType="java.util.HashMap">
  SELECT s.id AS "ID",
         s.scheme_descr AS "SCHEME_DESCR",
         s.scheme_conditon AS "SCHEME_CONDITON",
         visual_screen.f_get_IndexName(s.scheme_colums) AS "INDEX_NAME",
         s.add_date AS "ADD_DATE",
         su.realname AS "realname"
  FROM visual_screen.vs_lib_index_scheme s
  LEFT JOIN visual_screen.sys_user su ON s.add_userid = su.id
  WHERE 1 = 1
  <if test="params.schemeDescr != null and params.schemeDescr != ''">
    AND s.scheme_descr LIKE CONCAT(CONCAT('%', #{params.schemeDescr}), '%')
  </if>
  <if test="params.begin_time != null and params.begin_time != ''">
    AND LEFT(s.add_date, 10) &gt;= #{params.begin_time}
  </if>
  <if test="params.end_time != null and params.end_time != ''">
    AND LEFT(s.add_date, 10) &lt;= #{params.end_time}
  </if>
  ORDER BY s.add_date DESC NULLS LAST
  LIMIT #{params.pageSize,jdbcType=INTEGER} OFFSET #{params.page,jdbcType=INTEGER}
</select>
```

- [ ] **Step 5: 创建生产控制器**

类级映射和公开方法必须为：

```java
@RestController
@RequestMapping(value = "indexSchemeController", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexSchemeController extends BaseController {
    public Map<String, Object> getIndexInfo(JSONObject jsonObject) {
        PageData pd = getPageData(jsonObject);
        return success("indexInfoList", indexRelationService.getBatchIndexInfo(pd));
    }
    public Map<String, Object> deleteScheme(JSONObject jsonObject) {
        PageData pd = getPageData(jsonObject);
        indexSchemeService.deleteSchemeById(pd);
        return success("msg", "删除指标方案成功");
    }
    public Map<String, Object> selectSchemeTable(JSONObject jsonObject) {
        PageData pd = getPageData(jsonObject);
        int pageSize = Integer.parseInt(pd.getString("pageSize"));
        pd.put("page", (Integer.parseInt(pd.getString("pageNo")) - 1) * pageSize);
        pd.put("pageSize", pageSize);
        pd.put("schemeDescr", pd.getString("name"));
        Map<String, Object> result = success("rows", indexSchemeService.selectSchemeTable(pd));
        result.put("total", indexSchemeService.getSchemeCount(pd));
        return result;
    }
    private Map<String, Object> success(String key, Object value) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        result.put(key, value);
        return result;
    }
}
```

三个方法异常时返回 `result=failed`，不得把失败标记为 success。

- [ ] **Step 6: 删除代理控制器和第二套 Mapper**

删除五个 `IndexLibraryScheme*` 文件；用 `rtk rg -n 'IndexLibraryScheme' vis-screen-backend` 验证只允许测试说明中出现旧名称。

- [ ] **Step 7: 运行 Task 1/2 测试**

Run:

```bash
rtk mvn -pl jeecg-boot-module-system -Dtest=IndexSchemeProductionContractTest,IndexSchemeMapperContractTest,IndexSchemeControllerTest test
```

Expected: PASS。

- [ ] **Step 8: 提交方案链路**

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib \
        vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen \
        vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib
git commit -m "feat(vis): restore production index scheme endpoints"
```

---

### Task 3: 恢复四类图表预览和保存

**Files:**
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/controller/IndexBarLineController.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/controller/IndexPieController.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexChartService.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/impl/IndexChartServiceImpl.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib/service/IndexChartDataAssembler.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib/IndexChartServiceTest.java`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib/IndexChartControllerTest.java`

**Interfaces:**
- Consumes: `condition` JSON、`scheme_id`、`type`、`title`、`content`，以及方案 SQL/指标元数据。
- Produces: `POST /IndexBarLine/getIndexBarLineData|saveIndexBarLine`、`POST /IndexPie/getIndexPieData|saveIndexPie`；预览响应包含 `result`、`type`、`x`、`data`、`indexInfoList`。`IndexChartDataAssembler.assemble(String, List<Map<String,Object>>, List<Map<String,String>>, PageData)` 是唯一数据组装入口。

- [ ] **Step 1: 写图表类型和保存门槛失败测试**

```java
@Test
public void onlyFourProductionTypesAreAccepted() {
    assertTrue(service.supports("bar"));
    assertTrue(service.supports("line"));
    assertTrue(service.supports("pie"));
    assertTrue(service.supports("barAndLine"));
    assertFalse(service.supports("map"));
    assertFalse(service.supports("strip"));
    assertFalse(service.supports("bigNumber"));
}

@Test(expected = IllegalArgumentException.class)
public void saveRejectsUnsupportedType() {
    PageData params = new PageData();
    params.put("type", "map");
    service.saveGallery(params);
}
```

- [ ] **Step 2: 运行图表测试确认红灯**

Run: `rtk mvn -pl jeecg-boot-module-system -Dtest=IndexChartServiceTest,IndexChartControllerTest test`

Expected: FAIL，图表服务和控制器尚不存在。

- [ ] **Step 3: 定义共享服务接口**

```java
public interface IndexChartService {
    boolean supports(String type);
    Map<String, Object> preview(PageData params);
    void saveGallery(PageData params);
}
```

实现中固定类型集合：

```java
private static final Set<String> TYPES = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("bar", "line", "pie", "barAndLine")));
```

- [ ] **Step 4: 实现生产方案驱动的真实预览**

`preview` 必须完成以下确定流程：

```java
String schemeId = required(params, "scheme_id");
String type = required(params, "type");
if (!supports(type)) throw new IllegalArgumentException("不支持的图表类型: " + type);
Map<String, String> scheme = indexSchemeService.getSchemeInfoById(params);
if (scheme == null) throw new IllegalArgumentException("指标方案不存在");
params.put("schemeSql", scheme.get("SCHEME_SQL"));
params.put("indexColumns", scheme.get("SCHEME_COLUMS"));
List<Map<String, Object>> rows = indexSchemeService.execSchemeSql(params);
return chartDataAssembler.assemble(type, rows,
        indexSchemeService.getIndexNames(params), params);
```

`IndexChartServiceImpl` 同时定义空值校验，所有调用使用同一实现：

```java
private String required(PageData params, String key) {
    String value = params.getString(key);
    if (value == null || value.trim().isEmpty()) {
        throw new IllegalArgumentException(key + "不能为空");
    }
    return value;
}
```

组装约束：柱/线/柱折返回分类轴 `x` 和二维 `data`；饼图返回 ECharts `{name,value}` 数据；空结果返回 `result=success`、空集合，由前端展示空状态但禁止保存。生产方案 SQL 执行异常返回 `result=failed`，不得伪造示例数据。

- [ ] **Step 5: 实现独立数据组装器**

`IndexChartDataAssembler` 不访问数据库，只处理输入行。公开方法完整签名：

```java
public Map<String, Object> assemble(String type,
        List<Map<String, Object>> rows,
        List<Map<String, String>> indexInfo,
        PageData params)
```

实现按 `params.dimensionFlag` 读取分类轴；按 `indexInfo.INDEX_ID` 读取系列。`pie` 只使用第一个指标并输出 `{name,value}`；其余类型输出 `x`、`data`、`indexInfoList`。缺少维度或指标字段时抛出 `IllegalArgumentException`，不得猜测列名。

- [ ] **Step 6: 通过 GalleryService.add 保存生产字段**

禁止在图表服务中直接写 `vs_gallery_info`。构造 `PageData`：

```java
gallery.put("id", UuidUtil.get32UUID());
gallery.put("option", params.getString("option"));
gallery.put("query_path", "pie".equals(type)
        ? "/IndexPie/getIndexPieData" : "/IndexBarLine/getIndexBarLineData");
gallery.put("content", params.getString("content"));
gallery.put("type", type);
gallery.put("title", required(params, "title"));
gallery.put("sort", params.get("sort"));
gallery.put("state", "0");
gallery.put("business_id", "1010");
gallery.put("time_type", params.getString("time_type"));
gallery.put("dimension_type", params.getString("dimensionFlag"));
gallery.put("dacct_radio", params.getString("dacct_radio"));
gallery.put("title_old", params.getString("title_old"));
gallery.put("add_time", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
gallery.put("add_user", params.getString("add_user"));
gallery.put("scheme_id", required(params, "scheme_id"));
gallery.put("scheme_name", scheme.get("SCHEME_DESCR"));
gallery.put("condition", params.getString("condition"));
galleryService.add(gallery);
```

- [ ] **Step 7: 创建两个生产控制器**

`IndexBarLineController` 仅公开 `saveIndexBarLine`、`getIndexBarLineData`；`IndexPieController` 仅公开 `saveIndexPie`、`getIndexPieData`。控制器只负责 `JSONObject → PageData`、固定类型校验、调用 service 和组装生产 `result/msg`。

- [ ] **Step 8: 运行后端图表测试**

Run:

```bash
rtk mvn -pl jeecg-boot-module-system -Dtest=IndexSchemeProductionContractTest,IndexChartServiceTest,IndexChartControllerTest test
```

Expected: PASS；测试还应验证 `map`、`strip`、`bigNumber` 均返回失败。

- [ ] **Step 9: 提交图表链路**

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/indexlib \
        vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/indexlib
git commit -m "feat(vis): restore production index chart preview and save"
```

---

### Task 4: 对齐图库字段并交付数据库同步脚本

**Files:**
- Modify: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/GalleryMapper.xml`
- Create: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/GalleryMapperContractTest.java`
- Create: `document/psql/vastbase/2026071501-index-library-scheme-production-contract.sql`

**Interfaces:**
- Consumes: Task 3 传给 `GalleryService.add(PageData)` 的 18 个生产字段。
- Produces: 显式列名 `INSERT`；可重复执行的 Vastbase 对象核验/同步脚本。

- [ ] **Step 1: 写图库 Mapper 失败测试**

断言 `add` SQL 包含显式列清单，且不再出现 `INSERT INTO visual_screen.vs_gallery_info VALUES`：

```java
assertTrue(add.contains("index_scheme_id"));
assertTrue(add.contains("index_scheme_name"));
assertTrue(add.contains("condition"));
assertTrue(add.contains("add_time"));
assertTrue(add.contains("add_user"));
assertFalse(add.matches("(?is).*vs_gallery_info\\s+VALUES.*"));
```

- [ ] **Step 2: 运行测试确认红灯**

Run: `rtk mvn -pl jeecg-boot-module-system -Dtest=GalleryMapperContractTest test`

Expected: FAIL，当前 Mapper 仍是 11 列无列名插入。

- [ ] **Step 3: 将 GalleryMapper.add 改为生产显式列名**

使用生产 JAR 的 18 个字段和参数名：

```xml
<insert id="add">
  INSERT INTO visual_screen.vs_gallery_info
    (id, option, query_path, content, type, title, sort, state,
     business_id, time_type, dimension_type, dacct_radio, title_old,
     add_time, add_user, index_scheme_id, index_scheme_name, condition)
  VALUES
    (#{params.id}, #{params.option}, #{params.query_path}, #{params.content},
     #{params.type}, #{params.title}, #{params.sort}, #{params.state},
     #{params.business_id}, #{params.time_type}, #{params.dimension_type},
     #{params.dacct_radio}, #{params.title_old}, #{params.add_time},
     #{params.add_user}, #{params.scheme_id}, #{params.scheme_name},
     #{params.condition})
</insert>
```

- [ ] **Step 4: 创建幂等 Vastbase 同步脚本**

脚本使用 `information_schema.tables/columns/routines` 输出 BEFORE/AFTER；只对既有 `visual_screen.vs_gallery_info` 执行：

```sql
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS dacct_radio varchar(64);
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS title_old varchar(500);
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS add_time varchar(32);
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS add_user varchar(64);
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS index_scheme_id varchar(64);
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS index_scheme_name varchar(500);
ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN IF NOT EXISTS condition text;
```

脚本必须使用匿名 PL/pgSQL 块逐表查询 `information_schema.tables`；任一 `vs_lib_index_scheme`、`vs_gallery_info`、`sys_user` 或 `lib_index_relation` 不存在时执行 `RAISE EXCEPTION 'missing required production table: %', table_name` 并停止，避免误建空业务表。

- [ ] **Step 5: 在脚本中同步 visual_screen.f_get_IndexName**

创建 Vastbase 兼容函数，按逗号分隔 ID 顺序聚合指标名称：

```sql
CREATE OR REPLACE FUNCTION visual_screen.f_get_IndexName(scheme_columns varchar)
RETURNS varchar AS $$
  SELECT string_agg(r.index_name, ',' ORDER BY strpos(',' || scheme_columns || ',', ',' || r.index_id || ','))
  FROM indicators_lib.lib_index_relation r
  WHERE strpos(',' || scheme_columns || ',', ',' || r.index_id || ',') > 0;
$$ LANGUAGE SQL STABLE;
```

- [ ] **Step 6: 运行 Mapper 测试并静态检查脚本**

Run:

```bash
rtk mvn -pl jeecg-boot-module-system -Dtest=GalleryMapperContractTest,IndexSchemeMapperContractTest test
rtk rg -n 'ADD COLUMN IF NOT EXISTS|f_get_IndexName|RAISE EXCEPTION' document/psql/vastbase/2026071501-index-library-scheme-production-contract.sql
```

Expected: 测试 PASS；脚本包含 7 个兼容字段、函数和缺表失败保护。

- [ ] **Step 7: 提交数据库契约**

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/GalleryMapper.xml \
        vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/GalleryMapperContractTest.java \
        document/psql/vastbase/2026071501-index-library-scheme-production-contract.sql
git commit -m "fix(vis): align gallery schema with production contract"
```

---

### Task 5: 建立前端生产契约适配层

**Files:**
- Create: `org-tribe-view/src/api/indexLibraryScheme.js`
- Create: `org-tribe-view/src/utils/indexLibraryScheme.js`
- Create: `org-tribe-view/tests/indexLibraryScheme.test.mjs`

**Interfaces:**
- Consumes: 后端生产大写字段和 Task 2/3 URL。
- Produces: `CHART_TYPES`、`normalizeSchemeRow`、`parseSchemeCondition`、`buildPreviewPayload`、`buildSavePayload` 以及 7 个 API 函数。

- [ ] **Step 1: 写纯函数失败测试**

```javascript
assert.deepStrictEqual(CHART_TYPES.map(item => item.type), [
  'bar', 'line', 'pie', 'barAndLine'
])
assert.strictEqual(CHART_TYPES.some(item => item.type === 'map'), false)

const row = normalizeSchemeRow({
  ID: 's1', SCHEME_DESCR: '收入', ADD_DATE: '2026-01-01', realname: '管理员'
})
assert.deepStrictEqual(row, {
  id: 's1', name: '收入', createTime: '2026-01-01', username: '管理员',
  raw: { ID: 's1', SCHEME_DESCR: '收入', ADD_DATE: '2026-01-01', realname: '管理员' }
})
assert.throws(() => parseSchemeCondition('{broken'), /方案条件格式错误/)
```

- [ ] **Step 2: 运行 Node 测试确认红灯**

Run: `cd org-tribe-view && rtk node tests/indexLibraryScheme.test.mjs`

Expected: FAIL，模块不存在。

- [ ] **Step 3: 创建 API 封装**

```javascript
import { postAction } from '@/api/manage'

export const listSchemes = params => postAction('/vis/api/indexSchemeController/selectSchemeTable', params)
export const deleteScheme = params => postAction('/vis/api/indexSchemeController/deleteScheme', params)
export const getIndexInfo = params => postAction('/vis/api/indexSchemeController/getIndexInfo', params)
export const previewBarLine = params => postAction('/vis/api/IndexBarLine/getIndexBarLineData', params)
export const saveBarLine = params => postAction('/vis/api/IndexBarLine/saveIndexBarLine', params)
export const previewPie = params => postAction('/vis/api/IndexPie/getIndexPieData', params)
export const savePie = params => postAction('/vis/api/IndexPie/saveIndexPie', params)
```

- [ ] **Step 4: 创建纯函数模块**

图表常量固定为：

```javascript
export const CHART_TYPES = Object.freeze([
  { value: '1', type: 'bar', label: '柱状图' },
  { value: '2', type: 'line', label: '折线图' },
  { value: '3', type: 'pie', label: '饼图' },
  { value: '4', type: 'barAndLine', label: '柱状折线图' }
])
```

为使 Node 测试可直接导入，图标路径从纯数据中移到 Vue 组件；纯模块只保留 `value/type/label`，组件用 `type → asset` 映射。

`buildSavePayload(form, record, previewReady)` 必须要求 `previewReady === true`，否则抛出 `请先预览当前图表配置`；请求体不增加生产 JAR 中不存在的字段。

- [ ] **Step 5: 运行纯函数测试**

Run: `rtk node tests/indexLibraryScheme.test.mjs`

Expected: `indexLibraryScheme tests passed`。

- [ ] **Step 6: 提交适配层**

```bash
git add org-tribe-view/src/api/indexLibraryScheme.js org-tribe-view/src/utils/indexLibraryScheme.js org-tribe-view/tests/indexLibraryScheme.test.mjs
git commit -m "test(vis): add index scheme frontend contract"
```

---

### Task 6: 将列表页改为生产字段和生产接口

**Files:**
- Modify: `org-tribe-view/src/views/vis/IndexLibraryList.vue`
- Modify: `org-tribe-view/tests/indexLibraryScheme.test.mjs`

**Interfaces:**
- Consumes: `listSchemes`、`deleteScheme`、`normalizeSchemeRow`。
- Produces: `begin_time/end_time` 查询、生产字段列表、删除后分页回退、`convertModal.open(record.raw)`。

- [ ] **Step 1: 增加列表源码失败断言**

```javascript
assert.match(listSource, /begin_time/)
assert.match(listSource, /end_time/)
assert.match(listSource, /listSchemes/)
assert.match(listSource, /deleteScheme/)
assert.doesNotMatch(listSource, /indexLibraryScheme\/getPage/)
assert.doesNotMatch(listSource, /indexLibraryScheme\/del/)
```

- [ ] **Step 2: 运行测试确认红灯**

Run: `rtk node tests/indexLibraryScheme.test.mjs`

Expected: FAIL，列表仍使用代理 URL 和 `startDate/endDate`。

- [ ] **Step 3: 改写列表加载和查询**

不再使用通用 `ListMixin` 的代理字段映射；组件显式维护 loading、pagination，并提交：

```javascript
const params = {
  name: this.queryParam.name || '',
  begin_time: this.queryParam.begin_time || '',
  end_time: this.queryParam.end_time || '',
  pageNo: this.pagination.current,
  pageSize: this.pagination.pageSize
}
```

当开始日期晚于结束日期时提示 `开始日期不能大于结束日期` 并停止请求。

- [ ] **Step 4: 实现删除后的页码处理**

删除成功后：

```javascript
if (this.dataSource.length === 1 && this.pagination.current > 1) {
  this.pagination.current -= 1
}
this.loadData()
```

请求参数必须是 `{ schemeId: record.ID }`。

- [ ] **Step 5: 运行前端契约测试和 lint**

Run:

```bash
rtk node tests/indexLibraryScheme.test.mjs
rtk npx eslint src/views/vis/IndexLibraryList.vue src/api/indexLibraryScheme.js src/utils/indexLibraryScheme.js
```

Expected: PASS，无新增 lint error。

- [ ] **Step 6: 提交列表页**

```bash
git add org-tribe-view/src/views/vis/IndexLibraryList.vue org-tribe-view/tests/indexLibraryScheme.test.mjs
git commit -m "feat(vis): align index scheme list with production"
```

---

### Task 7: 重建现场四类转图弹窗和真实预览

**Files:**
- Modify: `org-tribe-view/src/views/vis/modules/IndexLibraryConvertModal.vue`
- Create: `org-tribe-view/src/views/vis/modules/IndexLibraryChartPreview.vue`
- Modify: `org-tribe-view/tests/indexLibraryScheme.test.mjs`

**Interfaces:**
- Consumes: Task 5 API/纯函数，列表传入的生产 raw record。
- Produces: 四类图标、条件回填、真实预览和保存状态机。

- [ ] **Step 1: 增加弹窗源码失败断言**

```javascript
assert.match(modalSource, /width="80%"/)
assert.match(modalSource, /previewBarLine/)
assert.match(modalSource, /previewPie/)
assert.match(modalSource, /previewReady/)
assert.match(modalSource, /保存图表/)
assert.doesNotMatch(modalSource, /value="map"/)
assert.doesNotMatch(modalSource, /toGallery/)
```

- [ ] **Step 2: 运行测试确认红灯**

Run: `rtk node tests/indexLibraryScheme.test.mjs`

Expected: FAIL，当前弹窗是简化下拉框并调用 `toGallery`。

- [ ] **Step 3: 重建弹窗字段和四图标选择**

使用 `a-form-model`，包含只读方案名称、必填图表标题、指标、维度、时间粒度、开始/结束日期、单位值、单位。图表类型为四个 radio 图片：`9.png`、`8.png`、`10.png`、`7.png`。

`open(record)` 必须：

```javascript
this.record = record
this.condition = parseSchemeCondition(record.SCHEME_CONDITON)
this.form = createInitialForm(record, this.condition)
this.previewReady = false
this.previewOption = null
this.visible = true
this.loadIndexInfo()
```

- [ ] **Step 4: 实现预览状态机**

所有影响请求的表单字段 watcher 调用：

```javascript
invalidatePreview() {
  this.previewReady = false
  this.previewOption = null
}
```

点击“预览”时先校验表单，再按 `type` 调用 bar-line 或 pie API。仅当 `res.result === 'success'` 且 `hasChartData(res)` 时设置 `previewReady=true` 和 option；空数据展示 `a-empty description="暂无可预览数据"`。

- [ ] **Step 5: 实现预览组件**

`IndexLibraryChartPreview.vue` 接收 `type` 和 `response`，生成：

```javascript
// bar / line / barAndLine
{
  tooltip: { trigger: 'axis' },
  legend: { data: response.indexInfoList.map(item => item.name) },
  xAxis: { type: 'category', data: response.x },
  yAxis: { type: 'value' },
  series: buildCartesianSeries(type, response)
}

// pie
{
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [{ type: 'pie', radius: '60%', data: response.data }]
}
```

背景使用现场深色预览区；组件销毁时移除 resize listener 并 dispose chart。

- [ ] **Step 6: 实现预览后保存**

保存按钮绑定 `:disabled="!previewReady"`。按图表类型调用 `saveBarLine` 或 `savePie`；请求必须复用最近一次预览时冻结的规范化 condition。保存失败不关闭弹窗，保存成功提示后关闭并发出 `ok`。

- [ ] **Step 7: 运行前端测试、lint 和构建**

Run:

```bash
rtk node tests/indexLibraryScheme.test.mjs
rtk npx eslint src/views/vis/IndexLibraryList.vue src/views/vis/modules/IndexLibraryConvertModal.vue src/views/vis/modules/IndexLibraryChartPreview.vue src/api/indexLibraryScheme.js src/utils/indexLibraryScheme.js
rtk npm run build
```

Expected: 测试 PASS；lint 无 error；build 成功。

- [ ] **Step 8: 提交转图界面**

```bash
git add org-tribe-view/src/views/vis/modules/IndexLibraryConvertModal.vue \
        org-tribe-view/src/views/vis/modules/IndexLibraryChartPreview.vue \
        org-tribe-view/tests/indexLibraryScheme.test.mjs
git commit -m "feat(vis): restore production index chart conversion"
```

---

### Task 8: 全量构建、数据库核验和真实浏览器验收

**Files:**
- Modify only if verification finds a defect in files introduced by Tasks 1-7.

**Interfaces:**
- Consumes: 完整后端、前端、数据库脚本。
- Produces: 可复现的构建结果、接口证据、浏览器截图和无错误控制台。

- [ ] **Step 1: 运行全量后端测试**

Run:

```bash
cd vis-screen-backend
rtk mvn -pl jeecg-boot-module-system test
```

Expected: BUILD SUCCESS，所有 indexlib 和既有 visualScreen tests PASS。

- [ ] **Step 2: 运行前端测试、lint 和生产构建**

Run:

```bash
cd org-tribe-view
rtk node tests/indexLibraryScheme.test.mjs
rtk npm run lint
rtk npm run build
```

Expected: 测试输出 `indexLibraryScheme tests passed`；lint/build 成功。

- [ ] **Step 3: 在目标数据库执行同步脚本**

Run:

```bash
psql -v ON_ERROR_STOP=1 -f document/psql/vastbase/2026071501-index-library-scheme-production-contract.sql
```

Expected: BEFORE/AFTER 均显示四个既有表；`vs_gallery_info` 具有 18 个生产字段；`visual_screen.f_get_IndexName` 存在。若整表缺失，脚本按设计失败并列出缺失对象，不继续启动功能验证。

- [ ] **Step 4: 启动目标服务并验证接口**

依项目现有部署脚本启动后，依次验证：

```text
POST /vis/api/indexSchemeController/selectSchemeTable
POST /vis/api/indexSchemeController/getIndexInfo
POST /vis/api/IndexBarLine/getIndexBarLineData
POST /vis/api/IndexPie/getIndexPieData
```

Expected: 列表返回生产大写字段；两类预览入口返回真实数据或明确空结果；不存在 `/vis/api/indexLibraryScheme/toGallery`。

- [ ] **Step 5: 使用 Chrome DevTools 验收页面**

1. 登录并进入“可视化大屏 → 指标库方案”。
2. 验证名称查询、日期查询、重置和分页请求体。
3. 打开一条方案，确认只显示四个图标。
4. 依次预览柱状、折线、饼图、柱状折线图。
5. 修改标题后确认保存重新禁用，重新预览后再保存。
6. 在图库页确认新图可读取。
7. 删除测试方案并确认页码处理。
8. 截图列表、转图配置和四类预览；检查 console 与 network。

Expected: 页面与生产演示一致；请求 URL/字段符合契约；console 无新增 error/warning；请求无 4xx/5xx。

- [ ] **Step 6: 检查最终差异和提交状态**

Run:

```bash
rtk git status --short
rtk git diff --check
rtk git log --oneline -8
```

Expected: 只有用户原有的无关未跟踪文件；本功能文件均已提交；`git diff --check` 无输出。

- [ ] **Step 7: 提交仅由验收发现的修复**

若 Step 1-5 修改了功能文件，仅暂存 `rtk git status --short` 中属于本计划 File Structure 的路径，然后提交：

```bash
git commit -m "fix(vis): address index scheme verification findings"
```

若无修复，不创建空提交。
