# 本次修改

- 在仓库根目录新增聚合父工程 `pom.xml`。
- 将 `dwbi-statistical-analysis`、`dwbi-system-docking`、`fixedReport`、`indicatorsLibv-1.0`、`org-tribe-system`、`seo` 纳入同一个 Maven 多模块工程。
- 将公共依赖版本上提到根父工程的 `properties` 和 `dependencyManagement`，并让相关子模块改为继承根父工程或其间接父工程。
- 在根父工程统一覆盖 `lombok.version`，避免新 JDK 环境下旧版 Lombok 触发编译器模块访问错误。
- 为 `dwbi-statistical-analysis` 补齐直接编译依赖，减少对 `dwbi-common` 非完整传递依赖的隐式依赖。
- 替换 `fixedReport`、`seo` 中依赖 `sun.misc.BASE64*` 的旧实现，兼容当前 JDK 编译环境。
- 在 JDK 8 环境下继续推进整仓构建，并为 `org-tribe-system` 回补原外部父 POM 隐含提供的核心基础依赖。
- 明确排除 `inspect`、`kunMgk`，同时不把 `org-tribe-view`、`jar` 等非 Maven 模块纳入聚合。

# 设计原因

- `dwbi-statistical-analysis/pom.xml` 已经依赖 `org.triber.dwbi:parent:1.0-SNAPSHOT`，根目录补这个父工程后可直接解析。
- `org-tribe-system` 现已直接继承根父工程，不再需要中间桥接父模块；公共版本管理也统一由根父工程直接提供。
- `fixedReport`、`indicatorsLibv-1.0`、`seo`、`dwbi-system-docking` 改为直接继承根父工程，以便共享公共版本管理，同时保留原有 `groupId/artifactId/version` 不变。
- 其余模块保持原有父 POM 不变，避免影响各模块当前的 Spring Boot 或 JEECG 依赖管理。
- 根工程仅承担聚合与基础属性职责，改动范围最小。

# 影响说明

- 现在可以在仓库根目录执行 Maven 聚合命令，例如 `mvn -pl dwbi-statistical-analysis -am validate`。
- `org-tribe-system` 现在直接继承仓库根父工程；后续若它还依赖私服中的其他组件，则仍需要本地仓库或私服可用。
- 已统一上提的主要是重复出现的通用依赖版本；少数模块特有或版本明显不一致的依赖仍保留在子模块内声明。
- `inspect`、`kunMgk` 仍保持独立模块，不受这次聚合影响。

# 最终验证

- 已在 JDK 8 环境 `C:\Users\skyqty\.jdks\corretto-1.8.0_472` 下完成根父工程整仓打包验证。
- 通过命令：`mvn --% clean package -Dmaven.test.skip=true`。
- 最终 Reactor 结果：`dwbi-statistical-analysis`、`dwbi-system-docking`、`fixedReport`、`indicatorsLibv-1.0`、`org-tribe-system`、`seo` 全部 `SUCCESS`。
- `org-tribe-system` 为完成 JDK 8 下聚合编译，额外补齐了阿里云 SDK、Hutool、邮件依赖，并排除了两个依赖代码生成工具库的历史工具类编译入口。
- `org-tribe-system` 现已补齐 `junit` 与 `spring-boot-starter-test`，因此 IDEA 或命令行使用 `-DskipTests=true` 时也可完成 `testCompile` 后再跳过测试执行。
- 若仍需连测试源码编译也一并跳过，PowerShell 中应使用 `mvn --% clean package -Dmaven.test.skip=true`，避免 `.` 被错误解析。
- 后续已进一步移除 `org-tribe-parent` 桥接层：`org-tribe-system` 直接挂接根父工程，空目录与 IDEA 残留路径也已清理。