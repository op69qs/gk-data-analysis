# org-tribe-system 本地补装 codegenerate 依赖

## 背景

启动 `org-tribe-system` 时，IDE/Java 编译报错：`程序包 org.jeecgframework.codegenerate.window 不存在`。

排查确认：

- `org-tribe-system` 中的 `JeecgOneGUI`、`JeecgOneToMainUtil` 使用了 `org.jeecgframework.codegenerate.*`。
- 模块 `pom.xml` 原本没有声明 `org.jeecgframework:codegenerate` 依赖。
- 用户提供的本地目录 `D:\document\c-project\project-code\project-02\org-tribe-system\package\sys\lib` 中存在 `codegenerate-1.0.5.jar`。

## 处理方案

1. 在 `org-tribe-system/pom.xml` 中补充 `org.jeecgframework:codegenerate:1.0.5` 依赖。
2. 使用 `mvn install:install-file` 将本地 `codegenerate-1.0.5.jar` 安装到 Maven 本地仓库。
3. 执行 `org-tribe-system` 模块编译验证。

## 说明

- 该依赖主要用于代码生成工具类，运行主应用时通常不会走到这些类，但 IDE 启动和工程构建会解析整个模块类路径，所以仍需补齐依赖。
- `maven-compiler-plugin` 已排除 `JeecgOneGUI.java` 和 `JeecgOneToMainUtil.java`，但这只能影响 Maven 编译阶段，不能替代依赖声明。