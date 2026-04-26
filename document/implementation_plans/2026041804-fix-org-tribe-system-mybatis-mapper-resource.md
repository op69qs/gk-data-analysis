# org-tribe-system MyBatis Mapper 资源修复

## 问题现象

- 登录接口调用 `SysUserMapper.getUserByName` 时抛出 `Invalid bound statement (not found)`。

## 根因分析

- `SysUserMapper.java` 与 `SysUserMapper.xml` 都存在，XML 中也定义了 `getUserByName`。
- `SysUserMapper.xml` 实际放在 `src/main/java/org/jeecg/modules/system/mapper/xml/`。
- `application-*.yml` 的 `mybatis-plus.mapper-locations` 指向 `classpath*:org/jeecg/modules/**/xml/*Mapper.xml`，要求 XML 进入运行时类路径。
- 当前 `pom.xml` 未将 `src/main/java` 下的 XML 作为资源复制到 `target/classes`，导致运行时扫描不到该 mapper XML。

## 实施修改

- 在 `org-tribe-system/pom.xml` 的 `<build>` 下补充 `<resources>`。
- 保留 `src/main/resources` 的默认资源复制。
- 新增 `src/main/java` 下 `**/*.xml` 的资源复制规则，确保 mapper XML 进入 `target/classes`。

## 预期结果

- 打包或编译后，`target/classes/org/jeecg/modules/system/mapper/xml/SysUserMapper.xml` 存在。
- MyBatis 能加载 `SysUserMapper.getUserByName`，登录接口不再因 statement 缺失报错。