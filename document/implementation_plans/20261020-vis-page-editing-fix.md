# 大数据可视化页面管理、页面编辑修复

## 问题与修复范围

- 模块设置调用图库分页接口时缺少 `pageNo/pageSize`，后端解析失败又返回 `success`，导致选图为空。前端补分页及错误提示，后端兼容省略分页参数的旧请求，异常返回 `failed`。
- 页面列表依赖全局滚动事件，延时请求可能重复或跳页，查询结果也可能被旧请求覆盖。改为显式分页，并忽略过期响应。
- 编辑路由依赖内存中的 `params.itemData`，刷新会丢失数据。改用 URL 中的页面 ID 重新加载；缓存的编辑器在新增、编辑切换时清空旧页面 ID，防止新增误写旧页面。
- 保存、截图或编辑请求失败时释放提交状态，保留编辑内容以便重试；保存和发布明确传递各自状态。
- 图库不在当前页时按 ID 加载原模块设置；切换图库页不再覆盖已修改的条件。模块替换使用 Vue 可追踪的对象更新。
- 区分 `ts` 与 `s` 维度，校验空日期，保留完整多段 HTML，保留已有标题背景设置，并避免失效颜色选择器的全局点击异常。
- 页面新增、修改、删除使用事务；子模块或条件写入失败时回滚并返回失败。空页面返回数组，同一页面重复使用同一图库时按 `sub_id` 分别恢复条件。

涉及 `org-tribe-view` 和 `vis-screen-backend/jeecg-boot-module-system`，无数据库表结构变更。

## 本地验证

修改前执行 `git pull --ff-only`，远端已是最新。保留既有 `org-tribe-system/src/main/resources/application.yml` 本地配置修改。

- Node 22 执行 `org-tribe-view/tests/*.test.mjs`：11 个测试文件通过，其中页面编辑回归 14 项。
- Java 8 编译、打包 vis 后端成功；直接使用 JUnitCore 执行该模块全部测试：66 项通过，其中页面编辑接口回归 8 项。
- Node 14 前端生产构建成功。构建仍有登录页 CSS 顺序和资源体积告警。
- 浏览器运行实际生产构建，接口用本地模拟数据：验证编辑链接刷新、原模块位于图库第二页时的回显、图库翻页、模块替换、多段文本、保存失败重试、编辑后新增、保存/发布状态、删除及背景图片上传。未连接现场数据库。

前端回归命令（测试工具使用 Node 22，业务构建使用 Node 14）：

```bash
cd org-tribe-view
node tests/visPageEditing.test.mjs
# 切换到 Node 14 后执行
npm run build
```

后端回归命令（Java 8，从仓库根执行）：

```bash
mvn -o -f vis-screen-backend/pom.xml -pl jeecg-boot-module-system -am package
java -cp 'vis-screen-backend/jeecg-boot-module-system/target/classes:vis-screen-backend/jeecg-boot-module-system/target/test-classes:vis-screen-backend/jeecg-boot-module-system/target/lib/*' \
  org.junit.runner.JUnitCore org.jeecg.modules.visualScreen.controller.PageEditingControllerTest
```

注意：vis 父 POM 硬编码了 `skipTests=true`，Maven 的成功日志不代表已执行测试；上述 JUnitCore 命令单独运行 8 项接口回归。

## 部署后验收

前端静态资源与 vis 后端 JAR 需要一并更新，沿用现场原有配置与部署流程；更新前备份原资源及 JAR，异常时两者一起恢复。部署后强制刷新浏览器缓存。

1. 页面管理按名称、发布状态查询，重置及翻页正常。
2. 新增页面，选择模板、背景颜色或上传图片，保存后重新进入能正确回显。
3. 双击模板模块，图库可查询、重置及翻页，选择图表后配置单位、日期和维度，确认后预览立即更新。
4. 同一图库放置到两个模块，分别设置不同日期或国库条件，保存后重新编辑，条件分别保持。
5. 修改已有页面，保存后再新增，原页面仍存在，新页面拥有独立 ID。
6. 文本模块输入多段内容，标题背景设为无，保存并刷新后设置保持。
7. 保存为未发布、编辑器发布及列表发布的状态正确；删除取消不影响记录，确认后只删除选定页面。
8. 在测试环境模拟请求失败，界面提示失败、提交遮罩消失、可重试；后端事务回滚需要现场数据库集成验收。

## 客户反馈后的二次排查（2026-10-21）

客户反馈前后端均已更新，页面编辑仍未解决。上轮模拟接口验收不足以证明现场问题解决。本轮使用配置中可访问的数据库及原 Controller、Service、Mapper 进行只读联调，包含视频同名的“国库部分业务凭证展示”页面；该数据库不是客户现场环境。

本轮确认并修正：

- 数据库菜单的编辑路由名是 `VisPageEditor`，静态入口名是 `VisPageEditorEntry`。原缓存重置只判断后者，现按规范路径识别编辑页，兼容实际菜单及历史导航。
- 国库、地区、科目树 SQL 的 `pId` 别名未引用，JDBC 实际返回 `pid`，但组树代码读取 `pId`。已在 vis 模块引用别名，保持现有接口字段契约。真实数据库两个回归测试修改前均失败，修改后通过。
- 真实图库首屏 12 条返回约 7 MB，联调中耗时约 14 秒；隐藏弹窗此前还会预先下载整页图库。现仅在打开时请求图库，并增加明确的加载、空结果、失败及重试提示。这里的耗时是本次联调环境观测，不能替代现场网络测量。

真实只读数据库测试需显式启用，默认不会连接数据库；连接强制只读，结束时回滚：

```bash
java -Dvis.editor.readonly.integration=true \
  -Dvis.editor.config=/absolute/path/to/application-dev.yml \
  -cp 'vis-screen-backend/jeecg-boot-module-system/target/classes:vis-screen-backend/jeecg-boot-module-system/target/test-classes:vis-screen-backend/jeecg-boot-module-system/target/lib/*' \
  org.junit.runner.JUnitCore org.jeecg.modules.visualScreen.controller.VisEditorReadOnlyIntegrationTest
```

交付时需确认主服务 JAR 内 `static/index.html` 与本次前端构建完全一致；只更新 vis-screen JAR 不会替换主服务承载的前端。若现场使用独立 Web 服务器托管前端，还需更新其静态目录。现场保存、发布与多表回滚仍需实际部署验收。

本轮验证结果：

- 前端 11 个测试文件通过，页面编辑回归增至 16 项；vis 后端原 66 项回归及新增 2 项真实只读数据库测试通过。
- 真实浏览器运行生产构建，使用数据库实际菜单路由及视频同名页面：加载原有 5 个模块，隐藏弹窗不请求图库；双击模块后加载真实图库，查询并替换图表，选择日期及地区条件后预览更新。地区树为 1 个根节点、42 个子节点，页面无 JavaScript 异常。登录壳使用本地测试身份，业务查询调用原 Controller、Service、Mapper；未执行保存、发布或其他数据库写入。
- Node 14 前端构建、Java 8 主服务和 vis 服务打包通过。逐文件核对主服务 JAR 内全部 57 个前端文件与本次构建一致，并核对 vis JAR 内维度 Mapper 与修改后的源文件一致。前端仍有原有 CSS 顺序及资源体积构建告警。
- 增量更新包包含主服务 JAR、vis 服务 JAR、独立托管用的前端目录及校验清单；保留现场原有配置、依赖与部署方式。现场保存、发布、删除和事务回滚仍待部署后的实际验收，本轮只读联调不替代该验收。
