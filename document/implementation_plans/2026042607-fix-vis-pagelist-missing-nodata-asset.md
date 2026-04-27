# 修复 vis 页面管理构建时缺失的占位图资源

## 问题现象

- 根目录执行聚合 `mvn compile` 时，在 `org-tribe-system` 的前端构建步骤失败。
- 失败点不是 Java 编译，而是 `org-tribe-view` 的 `npm run build`。
- 具体报错为：

```text
This dependency was not found:
* @/assets/nodata.png in ./src/views/vis/PageList.vue
```

## 根因

- [org-tribe-view/src/views/vis/PageList.vue](org-tribe-view/src/views/vis/PageList.vue) 在缩略图缺失时回退引用了 `@/assets/nodata.png`。
- 仓库内实际使用的命名是 `noData.png`，并非 `nodata.png`。
- 由于大小写和文件名不一致，webpack 在生产构建阶段解析失败，进而导致 Maven 聚合构建被前端步骤拦住。

## 处理方式

- 将 `PageList.vue` 中的：

```javascript
return require('@/assets/nodata.png')
```

- 修改为：

```javascript
return require('@/assets/noData.png')
```

## 验证

### 前端单独验证

在 `org-tribe-view` 目录执行：

```powershell
node "C:\nvm4w\nodejs\node_modules\npm\bin\npm-cli.js" run build
```

结果：构建完成，仅剩若干历史 CSS 顺序和体积 warning，无阻塞错误。

### 预期影响

- `vis/bigscreen/pages` 页面在缩略图缺失时仍能正常显示占位图。
- 聚合 Maven 编译不再因该静态资源路径错误而失败。

## 结论

- 这是一次前端静态资源引用错误，不涉及接口、数据口径或后端逻辑。
- 修复后应继续以聚合 `mvn compile` 结果作为最终闭环依据。