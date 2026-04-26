# vis-screen-backend AutoPoi 依赖解析修正

## 现象

`jeecg-boot-module-system` 中的 `ImportFileServiceImpl` 编译时报错：

```text
java: 找不到符号
  符号:   类 ImportFileServiceI
  位置: 程序包 org.jeecgframework.poi.excel.imports.base
```

## 排查结论

1. `ImportFileServiceI` 不在 `autopoi-web` 包里，而是在 `org.jeecgframework:autopoi` 核心包里。
2. `vis-screen-backend` 改为继承仓库根父工程后，根父工程的 `dependencyManagement` 会带入 `autopoi.version=1.0.3`。
3. JEECG 2.3.0 这套源码依赖的是 `autopoi 1.2.1`；该版本才包含 `ImportFileServiceI`。

## 本次修改

### 1. 在 `vis-screen-backend/pom.xml` 中显式覆盖 AutoPoi 版本

```xml
<autopoi.version>1.2.1</autopoi.version>
```

### 2. 在 `jeecg-boot-module-system/pom.xml` 中增加直连依赖

```xml
<dependency>
    <groupId>org.jeecgframework</groupId>
    <artifactId>autopoi</artifactId>
    <version>${autopoi.version}</version>
</dependency>
```

这样 `ImportFileServiceImpl` 使用的 `ImportFileServiceI` 不再依赖不稳定的传递依赖链。

## 验证

执行：

```powershell
mvn --% -f vis-screen-backend/jeecg-boot-module-system/pom.xml dependency:tree -Dincludes=org.jeecgframework:autopoi,org.jeecgframework:autopoi-web -Dstyle.color=never
```

结果：

- `org.jeecgframework:autopoi-web:1.2.1`
- `org.jeecgframework:autopoi:1.2.1`

说明当前模块的 AutoPoi 依赖已与 JEECG 2.3.0 源码对齐。

## 额外说明

本次只修正 `ImportFileServiceI` 缺类问题。

`mvn compile` 仍然存在其他历史编译错误，例如：

- `IdType.ASSIGN_ID` 不匹配
- 多处 Lombok 生成方法未识别

这些问题说明 `vis-screen-backend` 在继承仓库根父工程后，还存在更大范围的版本漂移，需单独继续收敛 MyBatis-Plus、Lombok 等依赖版本。