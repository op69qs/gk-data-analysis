# 指标库方案生产界面纠偏 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将“可视化大屏 → 指标库方案”的转图弹窗还原为生产演示中的纵向配置、生成图片、深色预览和确定保存流程，并生成真正包含新前后端代码的部署 JAR。

**Architecture:** 保留已验证的生产 Controller 与请求构造，将当前自行设计的左右双栏表单替换为生产 `TurnMap.vue` 的单列结构。表单只负责展示和编辑生产字段，Modal 继续负责异步竞态、真实预览和保存，Preview 负责应用颜色/渐变并导出 `data:image`；最终通过 Maven 构建链把前端复制进 `org-tribe-system` JAR，并独立重新打包 `vis-screen` JAR。

**Tech Stack:** Vue 2.6、Ant Design Vue 1.4.9、Element UI 2.12、ECharts 4.7、Node 14.21.3、Java 8、Maven、JUnit 4、Shell/JAR inspection。

## Global Constraints

- 后端接口和请求字段以 `document/指标库方案页面fix/jeecg-boot-module-system-2.3.0.jar` 为最高权威。
- 可见布局和操作顺序以 `document/指标库方案页面fix/指标库方案-生产演示.mp4` 为准。
- `jeecg-boot-parent.zip` 中的 `TurnMap.vue` 仅补充生产视频中可辨认的细节。
- 只开放柱状图、折线图、饼图、柱状折线图。
- 不得增加地图、条形图、BigNumber、`/vis/api/indexLibraryScheme/toGallery`、客户端 `schemeSql` 或参考分支 Controller。
- 列表、删除、指标信息只使用 `IndexSchemeController`；柱/折/柱折只使用 `IndexBarLineController`；饼图只使用 `IndexPieController`。
- 不新增业务表。
- 指标、维度、周期和单位按方案回显；除图表标题、时间、横轴/统计方向、候选维度、颜色、渐变、比率和柱折方向外，不开放方案定义字段编辑。
- 任一影响预览的配置变化必须让保存失效；只允许最后一次预览响应更新状态。
- 只有重新打包并替换运行 JAR 后才可宣称页面已更新。

---

## File Structure

- `org-tribe-view/src/utils/indexLibraryScheme.js`：生产条件规范化、时间控件状态、颜色、预览和保存载荷纯函数。
- `org-tribe-view/src/views/vis/modules/IndexLibraryConvertForm.vue`：生产顺序的单列表单、四图标、日期控件和图型专属条件。
- `org-tribe-view/src/views/vis/modules/IndexLibraryConvertModal.vue`：弹窗生命周期、生成图片、冻结条件、保存和竞态控制。
- `org-tribe-view/src/views/vis/modules/IndexLibraryChartPreview.vue`：深色 ECharts 预览、颜色/渐变 option 和图片导出。
- `org-tribe-view/tests/indexLibraryScheme.test.mjs`：纯函数、Vue 模板和状态机回归测试。
- `build/verify-index-library-scheme-artifacts.sh`：最终两个 JAR 的可重复静态资源与 Controller 审计。

---

### Task 1: 锁定生产表单与条件契约

**Files:**
- Modify: `org-tribe-view/tests/indexLibraryScheme.test.mjs`
- Modify: `org-tribe-view/src/utils/indexLibraryScheme.js`

**Interfaces:**
- Consumes: `parseSchemeCondition(raw)`、`createInitialForm(record, condition)`、`buildPreviewPayload(form, record)`。
- Produces:
  - `PRODUCTION_COLORS: string[]`
  - `getTimeTypeOptions(dacctRadio): Array<{ value: string, label: string }>`
  - `getDateControl(periodFlag, timeType): { kind: string, range: boolean, disabled: boolean }`
  - `getDimensionCandidates(condition): Array<{ value: string, label: string }>`
  - `validateProductionChartFields(form): Record<string, string>`，供 Task 2
    的 `validateIndexLibraryForm(form)` 合并使用。

- [ ] **Step 1: 在现有测试文件中先增加生产契约失败断言**

增加断言，要求：

```js
assert.deepStrictEqual(
  getTimeTypeOptions('1').map(item => item.value),
  ['1', '2', '3']
)
assert.deepStrictEqual(
  getTimeTypeOptions('0').map(item => item.value),
  ['3', '4']
)
assert.deepStrictEqual(
  getDateControl('2', '2'),
  { kind: 'month', range: true, disabled: false }
)
assert.deepStrictEqual(
  getDateControl('3', '1'),
  { kind: 'quarter', range: false, disabled: false }
)
assert.deepStrictEqual(
  getDateControl('4', '3'),
  { kind: 'year', range: false, disabled: true }
)
assert.ok(PRODUCTION_COLORS.length >= 10)
```

增加 `validateProductionChartFields` 断言，至少覆盖：

```js
assert.deepStrictEqual(
  validateProductionChartFields({ type: 'pie', direction: 'X', GK: '' }),
  { GK: '请选择国库或地区' }
)
assert.deepStrictEqual(
  validateProductionChartFields({ type: 'bar', xTurn: '1', dateId: '' }),
  { dateId: '请选择账期' }
)
```

- [ ] **Step 2: 运行测试并确认 RED**

Run:

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH \
node tests/indexLibraryScheme.test.mjs
```

Expected: 因新增导出不存在或生产模板文案不存在而失败；不得是语法错误。

- [ ] **Step 3: 实现最小纯函数**

在 `indexLibraryScheme.js` 中添加：

```js
export const PRODUCTION_COLORS = Object.freeze([
  '#2670F7', '#FBE268', '#39C6FF', '#FF7147', '#AC9EBF',
  '#72EDFF', '#DBA644', '#2DB1CB', '#58C5D7', '#5DB5D9'
])

export function getTimeTypeOptions(dacctRadio) {
  return String(dacctRadio) === '0'
    ? [{ value: '3', label: '当前' }, { value: '4', label: '时间' }]
    : [
        { value: '1', label: '至今' },
        { value: '2', label: '时间区间' },
        { value: '3', label: '当前' }
      ]
}

export function getDateControl(periodFlag, timeType) {
  const kinds = { '1': 'date', '2': 'month', '3': 'quarter', '4': 'year' }
  return {
    kind: kinds[String(periodFlag)] || 'date',
    range: String(timeType) === '2',
    disabled: String(timeType) === '3'
  }
}
```

`getDimensionCandidates` 只读取方案条件中已存在的维度数组/编码，不请求参考分支 API，也不生成虚构候选值。新增候选字段必须加入
`CONDITION_FIELDS` 白名单，并用
`parseSchemeCondition → createInitialForm → getDimensionCandidates`
的真实链路测试，不能直接把未经规范化的对象传给 helper。

`getTimeTypeOptions` 对缺失、空值或非法 `dacct_radio` 只返回“当前”，
不得猜测为模式 `1`。

- [ ] **Step 4: 运行测试确认新增纯函数 GREEN**

Run: Task 1 Step 2 的同一命令。

Expected: Task 1 的纯函数和验证断言全部通过；本提交整体为绿色。

- [ ] **Step 5: 提交 Task 1**

```bash
git add org-tribe-view/src/utils/indexLibraryScheme.js \
        org-tribe-view/tests/indexLibraryScheme.test.mjs
git commit -m "test(vis): lock production chart form contract"
```

---

### Task 2: 复刻生产单列表单

**Files:**
- Modify: `org-tribe-view/src/utils/indexLibraryScheme.js`
- Modify: `org-tribe-view/src/views/vis/modules/IndexLibraryConvertForm.vue`
- Modify: `org-tribe-view/tests/indexLibraryScheme.test.mjs`

**Interfaces:**
- Consumes: Task 1 的 `PRODUCTION_COLORS`、`getTimeTypeOptions`、`getDateControl`、`getDimensionCandidates`、`validateProductionChartFields`。
- Produces:
  - `validate(): Promise<boolean>`
  - `selectChartType(type): void`
  - `selectColors(indexes): void`
  - 对父组件传入 `form` 的生产字段更新。

- [ ] **Step 1: 扩充 RED 模板和状态断言**

断言以下结构同时存在：

```js
assert.match(convertFormSource, /<a-radio-group[^]*chartTypes/)
assert.match(convertFormSource, /图表标题/)
assert.match(convertFormSource, /<a-input[^]*disabled[^]*指标/)
assert.match(convertFormSource, /时间类型/)
assert.match(convertFormSource, /选择时间/)
assert.match(convertFormSource, /横轴显示/)
assert.match(convertFormSource, /统计方向/)
assert.match(convertFormSource, /生成图片/)
assert.match(convertFormSource, /设置图表颜色/)
assert.match(convertFormSource, /是否包含比率/)
assert.doesNotMatch(convertFormSource, /config-panel/)
```

并为验证函数增加缺失维度/账期/饼图方向时的失败断言。
`IndexLibraryConvertForm.vue` 的 `validateIndexLibraryForm` 必须合并
`validateProductionChartFields(form)` 的结果，避免 Task 1 的纯函数契约
与实际表单验证脱节。

- [ ] **Step 2: 运行测试确认 RED**

Run: Task 1 Step 2 命令。

Expected: 生产表单结构或验证断言失败。

- [ ] **Step 3: 将表单改成生产顺序**

模板骨架必须是：

```vue
<div class="production-chart-form">
  <div class="form-label">选择图表类型：</div>
  <a-radio-group v-model="form.chartValue" @change="onChartChange">
    <a-radio v-for="item in chartTypes" :key="item.value" :value="item.value">
      <img :src="item.icon" :alt="item.label" :title="item.label">
    </a-radio>
  </a-radio-group>

  <a-form layout="inline">
    <!-- 图表标题、只读指标 -->
    <!-- 只读维度、周期、单位 -->
    <!-- 时间类型和按日/月/季/年切换的日期控件 -->
    <!-- 图型专属横轴/统计方向和候选值 -->
    <!-- 生成图片 -->
    <!-- 成功生成后颜色、渐变、比率和柱折方向 -->
  </a-form>
</div>
```

要求：

- `form.type` 与 `chartValue` 映射仍使用既有 `CHART_TYPES`；
- 切换图型时清理 `direction/GK/indexName/dateId`，并将 `xTurn`
  恢复为生产默认账期模式，防止饼图的 `X/Y` 被柱图当作维度编码；
- 指标显示为 `indexOptions.map(item => item.name).join('、')` 的禁用输入；
- 维度、周期、单位使用禁用 select；
- 日/月使用 `el-date-picker`；季使用现有 `dataMonth.vue`；年区间使用 `dataYear.vue`；
- 所有候选项来自方案条件或生产接口已经返回的数据；
- 按钮文案必须是“生成图片”，由 `$emit('generate')` 触发父组件预览；
- 已生成状态由 prop `generated` 控制颜色和渐变区域。
- 非饼图 `xTurn='1'` 时，`buildPreviewPayload` 必须把所选
  `dateId` 转换成 `startDate=endDate=dateId`，且不得把 `dateId`
  发送给后端；`timeType='4'` 单时间同样发送相同起止边界。
- 柱折 `chartDirection` 只允许 `Columnar/Line`；非法回显按指标顺序
  规范化，删除至空必须阻止生成。

- [ ] **Step 4: 运行测试和 Vue 模板编译**

Run:

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH \
node tests/indexLibraryScheme.test.mjs

PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH \
node -e "const fs=require('fs');const c=require('vue-template-compiler');for(const f of ['src/views/vis/modules/IndexLibraryConvertForm.vue']){const r=c.parseComponent(fs.readFileSync(f,'utf8'));const x=c.compile(r.template.content);if(x.errors.length)throw new Error(x.errors.join('\\n'))}console.log('Vue template compile passed')"
```

Expected: `indexLibraryScheme tests passed` 和 `Vue template compile passed`。

- [ ] **Step 5: 提交 Task 2**

```bash
git add org-tribe-view/src/utils/indexLibraryScheme.js \
        org-tribe-view/src/views/vis/modules/IndexLibraryConvertForm.vue \
        org-tribe-view/tests/indexLibraryScheme.test.mjs
git commit -m "feat(vis): restore production index chart form"
```

---

### Task 3: 恢复生成图片、预览和确定保存状态机

**Files:**
- Modify: `org-tribe-view/src/views/vis/modules/IndexLibraryConvertModal.vue`
- Modify: `org-tribe-view/src/views/vis/modules/IndexLibraryChartPreview.vue`
- Modify: `org-tribe-view/tests/indexLibraryScheme.test.mjs`

**Interfaces:**
- Consumes: Task 2 的 `generate` 事件和 `generated` prop。
- Produces:
- `handleGenerate(): Promise<boolean>`
- `handleOk(): Promise<boolean>`
- `invalidatePreview(): void`
- `buildChartOption(type, response, condition)` 支持 `condition.colourArray` 和 `condition.isGradual`。
- `hasGeneratedPreview: boolean` 仅控制生成后的颜色/渐变区可见性；
  `previewReady: boolean` 单独控制当前配置是否允许保存。
- 独立 `saveRevision: number` 屏蔽关闭或切换方案后的旧保存响应。

- [ ] **Step 1: 增加状态机 RED 测试**

增加测试：

```js
assert.match(convertModalSource, /:title="modalTitle"/)
assert.match(convertModalSource, /@generate="handleGenerate"/)
assert.match(convertModalSource, /okText="确定"/)
assert.doesNotMatch(convertModalSource, /预览图表/)
assert.doesNotMatch(convertModalSource, /保存图表/)
```

在既有 VM harness 中验证：

- `handleGenerate()` 成功前 `handleOk()` 不调用保存；
- 标题、时间、颜色、渐变、比率或方向改变后保存重新失效；
- 旧 preview response 不得覆盖新 response；
- 保存中关闭或打开另一方案后，旧 save response 不得关闭新弹窗、发消息、
  发出 `ok` 或提前清除新保存的 loading；
- `frozenCondition.colourArray`、`isGradual`、`isRate` 写入 `condition`；
- `content` 仍为 `data:image/...`。

- [ ] **Step 2: 运行测试确认 RED**

Run: Task 1 Step 2 命令。

Expected: 新状态机或模板断言失败。

- [ ] **Step 3: 将 Modal 改成生产单列流程**

模板改为：

```vue
<a-modal
  :title="modalTitle"
  width="80%"
  :visible="visible"
  :confirmLoading="saving"
  :okButtonProps="{ props: { disabled: !previewReady || saving } }"
  cancelText="关闭"
  okText="确定"
  @ok="handleOk"
  @cancel="handleCancel"
>
  <index-library-convert-form
    ref="convertForm"
  :form="form"
  :index-options="indexOptions"
    :generated="hasGeneratedPreview"
    @generate="handleGenerate"
  />
  <index-library-chart-preview
    v-if="previewState === 'ready'"
    ref="chartPreview"
    :type="frozenCondition.type"
    :response="previewResponse"
    :condition="frozenCondition"
  />
</a-modal>
```

将当前 `handlePreview` 重命名为 `handleGenerate`，保留 revision 竞态防护。
`handleOk` 复用既有保存逻辑，并继续以冻结条件生成保存 payload。
`modalTitle` 返回 `${方案名称} - 转图`，与生产演示一致；方案名称为空时回退
为“转图”。

`open`、`handleCancel` 和每次 `handleOk` 必须推进独立
`saveRevision`。保存请求捕获 revision 与当前方案 ID，`then/catch/finally`
只有在两者仍匹配时才能修改弹窗、消息、事件或 `saving`。

首次生成成功时同时设置 `hasGeneratedPreview=true` 与
`previewReady=true`。后续标题、时间、横轴、颜色、渐变、比率或柱折方向
变化时，只将 `previewReady=false` 并保留 `hasGeneratedPreview=true`，
从而让生产后处理控件继续可见，但要求重新点击“生成图片”才能确定保存。
关闭或切换方案时两者都清零。

- [ ] **Step 4: 在 Preview 应用生产颜色和渐变**

`buildChartOption` 必须：

```js
const colors = Array.isArray(condition.colourArray) &&
  condition.colourArray.length
  ? condition.colourArray
  : PRODUCTION_COLORS
option.color = colors
```

柱状 series 在 `isGradual === true` 时使用 ECharts 线性渐变；折线和饼图保持选择的实色。预览容器保持生产视频中的深灰背景和白色坐标文字。

- [ ] **Step 5: 运行前端测试和模板编译**

Run: Task 2 Step 4 的两个命令，并将 Modal、Preview 加入编译文件列表。

Expected: 全部通过。

- [ ] **Step 6: 提交 Task 3**

```bash
git add org-tribe-view/src/views/vis/modules/IndexLibraryConvertModal.vue \
        org-tribe-view/src/views/vis/modules/IndexLibraryChartPreview.vue \
        org-tribe-view/tests/indexLibraryScheme.test.mjs
git commit -m "feat(vis): restore production chart generation flow"
```

---

### Task 4: 生成并审计最终部署 JAR

**Files:**
- Create: `build/verify-index-library-scheme-artifacts.sh`
- Generated/ignored: `org-tribe-view/dist/`
- Generated/ignored: `org-tribe-system/target/org-tribe-system-2.1.0.jar`
- Generated/ignored: `vis-screen-backend/jeecg-boot-module-system/target/jeecg-boot-module-system-2.3.0.jar`
- Generated/ignored: `deploy-package/app/org-tribe-system-2.1.0.jar`
- Generated/ignored: `deploy-package/app/vis-screen-2.3.0.jar`

**Interfaces:**
- Consumes: Tasks 1–3 的已测试源码。
- Produces: 两个可部署 JAR 和一个可重复审计脚本。

- [ ] **Step 1: 先写会对旧部署包失败的审计脚本**

脚本使用 `set -euo pipefail`，解压到临时目录并断言：

```bash
rg -a -q '生成图片' "${system_extract}/static/js"
! rg -a -q '图库标题' "${system_extract}/static/js"
! rg -a -q '/vis/api/indexLibraryScheme/toGallery' "${system_extract}/static/js"
test -f "${vis_extract}/org/jeecg/modules/indexlib/controller/IndexSchemeController.class"
test -f "${vis_extract}/org/jeecg/modules/indexlib/controller/IndexBarLineController.class"
test -f "${vis_extract}/org/jeecg/modules/indexlib/controller/IndexPieController.class"
```

脚本参数默认指向 `deploy-package/app` 中两个 JAR，并允许显式传入路径。

- [ ] **Step 2: 在旧部署包上运行并确认 RED**

Run:

```bash
bash build/verify-index-library-scheme-artifacts.sh
```

Expected: 因旧 system JAR 没有“生成图片”或旧 vis-screen JAR 没有 Controller 而失败。

- [ ] **Step 3: 运行前后端测试**

前端：

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH \
node org-tribe-view/tests/indexLibraryScheme.test.mjs
```

后端使用项目既有 Java 8 JUnitCore harness，运行全部指标方案测试。

Expected: 前端输出 `indexLibraryScheme tests passed`；后端输出 `OK` 且失败数为 0。

- [ ] **Step 4: 构建前端和 system JAR**

Run:

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH \
npm run build
```

在 `org-tribe-view` 执行，Expected: exit 0。

然后在仓库根目录使用 Java 8 执行：

```bash
mvn -pl org-tribe-system package -DskipTests -Dskip.frontend.build=true
```

在执行前将新 `dist` 明确复制到 `org-tribe-system/target/classes/static`；
若使用完整 `with-frontend-build` profile，则不得设置
`-Dskip.frontend.build=true`，并以 Maven 复制结果为准。

- [ ] **Step 5: 构建 vis-screen JAR**

Run:

```bash
mvn -f vis-screen-backend/pom.xml \
  -pl jeecg-boot-module-system -am package -DskipTests
```

Expected: 生成的模块 JAR 中包含 `org/jeecg/modules/indexlib/`。

- [ ] **Step 6: 刷新 deploy-package 并运行 GREEN 审计**

将两个新 JAR复制到：

```text
deploy-package/app/org-tribe-system-2.1.0.jar
deploy-package/app/vis-screen-2.3.0.jar
```

Run:

```bash
bash build/verify-index-library-scheme-artifacts.sh
sha256sum deploy-package/app/org-tribe-system-2.1.0.jar \
          deploy-package/app/vis-screen-2.3.0.jar
```

Expected: 审计脚本输出 `index scheme artifact verification passed`。

- [ ] **Step 7: 提交审计脚本**

```bash
git add build/verify-index-library-scheme-artifacts.sh
git commit -m "build(vis): verify index scheme deployment artifacts"
```

---

### Task 5: 浏览器验收与最终复审

**Files:**
- Modify only if browser verification or review exposes a defect.
- Evidence: `.superpowers/sdd/`（忽略，不提交）。

**Interfaces:**
- Consumes: Task 4 的最终 JAR 或同一 commit 的生产构建。
- Produces: 可见布局、网络请求、控制台和四图型行为证据。

- [ ] **Step 1: 启动与最终 JAR 同源的本地页面**

优先启动 `deploy-package/app` 中 system JAR；如果真实依赖或登录不可用，
使用生产构建静态资源加生产形状 API mock，但必须在报告中明确标注。

- [ ] **Step 2: 用 Chrome/Playwright 验证列表**

检查：

- 方案名称和创建日期查询；
- 重置；
- 分页；
- 删除使用 `record.id`；
- 控制台无新增 error/warning。

- [ ] **Step 3: 验证转图弹窗**

检查：

- 标题为“方案名称 - 转图”，宽 80%，单列纵向；
- 只有四个生产图标；
- 指标、维度、周期、单位只读；
- 日期使用选择器而非自由文本；
- 柱、折、饼、柱折均调用正确生产 URL；
- 按钮为“生成图片”；生成前“确定”不可保存；
- 颜色/渐变/比率按图型显示；
- 生成后显示深色预览；
- 修改配置后确定重新失效；
- 保存 payload 的 `content` 为 `data:image`，`condition.option` 为 JSON。

- [ ] **Step 4: 截图并检查网络/控制台**

至少保存：

- 列表；
- 柱状图生成前；
- 柱状图生成后；
- 饼图配置；
- 柱折方向表；
- 一个窄屏布局。

Expected: 0 console errors，0 console warnings，接口 URL 无旧
`indexLibraryScheme/toGallery`。

- [ ] **Step 5: 最终全分支复审**

审查范围从 `211cc23` 到 HEAD，重点检查：

- 与书面规格逐项一致；
- 无参考分支扩展接口；
- 无地图等越界功能；
- 无竞态回归；
- 生成的部署 JAR 与 HEAD 对应；
- 所有 Critical/Important 发现修复并复审。

- [ ] **Step 6: 最终验证**

Run:

```bash
git diff --check 211cc23..HEAD
git status --short --branch
bash build/verify-index-library-scheme-artifacts.sh
```

重新运行前端契约测试、Vue 模板编译和后端 JUnitCore 全套。

Expected: 所有命令退出 0；只保留用户已有的无关未跟踪文件。
