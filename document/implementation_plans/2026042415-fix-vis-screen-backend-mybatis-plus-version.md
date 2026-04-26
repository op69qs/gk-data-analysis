# vis-screen-backend MyBatis-Plus 版本回调记录

## 现象

`vis-screen-backend` 中多处实体类编译报错：

```text
java: 找不到符号
  符号:   变量 ASSIGN_ID
  位置: 类 com.baomidou.mybatisplus.annotation.IdType
```

## 根因

`vis-screen-backend` 继承仓库根父工程后，根父工程中的 `mybatis-plus.version=3.1.2` 生效，覆盖了 `vis-screen-backend/jeecg-boot-base-common/pom.xml` 原本期望的 `3.3.2`。

而 JEECG 2.3.0 这套源码里使用的 `IdType.ASSIGN_ID` 需要 `MyBatis-Plus 3.3.x` 才提供，因此在 `3.1.2` 下会直接报找不到符号。

## 本次修改

在 `vis-screen-backend/pom.xml` 中显式增加：

```xml
<mybatis-plus.version>3.3.2</mybatis-plus.version>
```

通过子父工程局部覆盖，把 `vis-screen-backend` 恢复到与其源码匹配的 MyBatis-Plus 版本。

## 验证

### 1. 依赖树验证

执行：

```powershell
mvn --% -f vis-screen-backend/jeecg-boot-module-system/pom.xml dependency:tree -Dincludes=com.baomidou:mybatis-plus-annotation,com.baomidou:mybatis-plus-core,com.baomidou:mybatis-plus -Dstyle.color=never
```

结果已经变为：

- `mybatis-plus-boot-starter:3.3.2`
- `mybatis-plus:3.3.2`
- `mybatis-plus-core:3.3.2`
- `mybatis-plus-annotation:3.3.2`

### 2. 文件级错误验证

对 `SysRole.java` 做错误检查，`ASSIGN_ID` 相关错误已消失。

## 说明

本次只修复 `ASSIGN_ID` 对应的 MyBatis-Plus 版本漂移问题。

`vis-screen-backend` 仍可能存在其他独立编译问题，例如 Lombok/JPS 相关符号缺失，这些需要继续分别收敛，不属于本次 `ASSIGN_ID` 修复范围。