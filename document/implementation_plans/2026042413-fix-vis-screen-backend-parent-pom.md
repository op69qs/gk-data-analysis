# vis-screen-backend 父 POM 修正记录

## 背景

`vis-screen-backend/pom.xml` 原先直接继承 `spring-boot-starter-parent`，没有挂到仓库根父工程 `gk-data-analysis/pom.xml`。

用户已明确该模块应以仓库根父工程作为父 POM，因此需要把 `vis-screen-backend` 纳入现有 Maven 继承链，同时保留它作为其内部 JEECG 子模块的直接父 POM。

## 本次修改

1. 修改 `vis-screen-backend/pom.xml`
2. 将其 `<parent>` 从 `org.springframework.boot:spring-boot-starter-parent:2.1.3.RELEASE` 调整为：
   - `org.triber.dwbi:parent:1.0-SNAPSHOT`
   - `relativePath` 指向 `../pom.xml`
3. 保留当前模块自身坐标：
   - `org.jeecgframework.boot:jeecg-boot-parent:2.3.0`
4. 不改动 `vis-screen-backend` 下各子模块的 parent 关系，继续由 `vis-screen-backend/pom.xml` 作为它们的直接父 POM。

## 验证

执行命令：

```powershell
mvn --% -f vis-screen-backend/pom.xml help:evaluate -Dexpression=project.parent.artifactId -DforceStdout -Dstyle.color=never
```

结果输出 `parent`，说明 `vis-screen-backend` 的 Maven 父工程已正确解析到仓库根父工程。

## 说明

- 本次只修正父 POM 继承关系。
- 未将 `vis-screen-backend` 新增到根聚合 `<modules>` 中，因为这属于聚合构建范围调整，和本次 parent 修正不是同一个问题。