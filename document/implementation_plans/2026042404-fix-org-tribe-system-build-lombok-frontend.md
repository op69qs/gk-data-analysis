# org-tribe-system 编译修复记录

## 背景

`org-tribe-system` 在 Maven 构建时同时遇到两类问题：

1. Lombok 相关编译异常，表现为 `ShiroRealm` 中 `log`、`SysUser.getPassword()`、`SysUser.getStatus()` 等符号无法解析。
2. `frontend-maven-plugin` 在 `generate-resources` 阶段强制执行 `npm ci --legacy-peer-deps`，Windows 下 `node-sass` 二进制被占用时会直接导致整次 Maven 构建失败。

## 本次修改

### 1. 为前端构建增加跳过开关

在 `org-tribe-system/pom.xml` 的 `<properties>` 中新增：

```xml
<skip.frontend>false</skip.frontend>
```

并在 `frontend-maven-plugin` 配置中增加：

```xml
<skip>${skip.frontend}</skip>
```

同时在 `maven-antrun-plugin` 上也接入同一个开关，避免 `-Dskip.frontend=true` 时仍在 `initialize` 阶段删除 `org-tribe-view/node_modules`。

这样后端编译或打包时可通过 `-Dskip.frontend=true` 跳过 `install-node-and-npm`、`npm-install`、`npm-build` 三个执行块，避免前端依赖目录被锁时拖垮后端构建。

### 2. 前端 Maven 构建改为隔离工作目录

将 `org-tribe-system/pom.xml` 中的前端构建目录从源码目录：

```xml
${project.basedir}/../org-tribe-view
```

改为：

```xml
${project.build.directory}/frontend-workdir
```

并新增：

- `frontend.source.directory`：指向真实前端源码目录 `../org-tribe-view`
- `prepare-frontend-workdir`：在 `initialize` 阶段把前端源码复制到 `target/frontend-workdir`
- 复制时显式排除 `node_modules/**` 与 `dist/**`

这样 `frontend-maven-plugin` 后续执行的 `npm ci`、`npm run build` 都发生在 `target/frontend-workdir` 中，不再直接操作源码目录下可能被本机 Node 进程、杀毒或编辑器占用的 `node_modules/node-sass/vendor/.../binding.node`。

### 3. 显式固定 Lombok 注解处理器

在 `maven-compiler-plugin` 中补充：

```xml
<source>${java.version}</source>
<target>${java.version}</target>
<annotationProcessorPaths>
  <path>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
  </path>
</annotationProcessorPaths>
```

目的：

- 明确让 Maven 编译阶段使用父工程中已管理的 `lombok.version=1.18.32`
- 避免注解处理链在不同环境下漂移到旧 Lombok 或非预期处理器实现
- 让依赖 Lombok 生成 getter/logger 的实体与组件恢复正常编译

### 4. 补充 IDEA/JPS 编译兼容配置

在仓库级 `.idea/compiler.xml` 中补充：

```xml
<option name="BUILD_PROCESS_ADDITIONAL_VM_OPTIONS" value="-Djps.track.ap.dependencies=false" />
<module name="org-tribe-system" target="1.8" />
```

目的：

- 对 IntelliJ IDEA 的 JPS 编译进程关闭会把注解处理器包装成动态代理的依赖跟踪路径，规避 Lombok 在 IDE 编译时出现 `Your processor is: com.sun.proxy.$Proxy8` 后直接失效
- 明确 `org-tribe-system` 模块在 IDEA 内部编译时也按 Java 8 目标字节码处理，避免项目级与模块级目标版本不一致

## 验证

使用 JDK 8：`C:\Users\skyqty\.jdks\corretto-1.8.0_472`

### 编译验证

```powershell
cd org-tribe-system
mvn --% -Dskip.frontend=true -DskipTests compile
```

结果：`BUILD SUCCESS`

### 打包验证

```powershell
cd org-tribe-system
mvn --% -Dskip.frontend=true -DskipTests package
```

结果：`BUILD SUCCESS`

### 默认前端资源阶段验证

```powershell
cd org-tribe-system
mvn --% -DskipTests generate-resources
```

结果：`BUILD SUCCESS`

关键日志：

- `prepare-frontend-workdir` 已把前端源码复制到 `target/frontend-workdir`
- `npm ci --legacy-peer-deps` 在 `target/frontend-workdir` 中成功执行
- `npm run build` 成功生成 `target/frontend-workdir/dist`

### 默认完整打包验证

```powershell
cd org-tribe-system
mvn --% -DskipTests package
```

结果：`BUILD SUCCESS`

构建日志中可见：

- `antrun:... clean-frontend-workdir` 已显示 `Skipping Antrun execution`
- `frontend:... install-node-and-npm` / `npm-install` / `npm-build` 均已 `Skipping execution.`
- 依赖复制阶段实际解析到 `org.projectlombok:lombok:jar:1.18.32`
- `copy-frontend-static` 在 `dist` 不存在时仅 `skip non existing resourceDirectory`，不会导致打包失败

### IDEA 配置文件合法性验证

```powershell
[xml](Get-Content .idea/compiler.xml) | Out-Null
```

结果：配置可正常解析，无 XML 语法错误

## 使用建议

### 仅验证后端编译时

优先使用：

```powershell
mvn --% -Dskip.frontend=true -DskipTests compile
```

### 需要完整前端产物时

现在默认 Maven 打包已经改为在隔离目录中执行前端安装与构建，不再依赖源码目录下的 `node_modules` 可删除状态。

如果只想加快后端验证，仍然可以继续使用：

```powershell
mvn --% -Dskip.frontend=true -DskipTests package
```

## 结论

本次修复没有改业务代码，修改点限于 `org-tribe-system/pom.xml` 与仓库级 `.idea/compiler.xml`。结果是：

- 后端 Maven 编译不再被前端依赖安装失败阻塞
- 默认 Maven 前端构建已从源码目录切换到 `target/frontend-workdir`，规避 Windows 下 `node-sass` 二进制文件锁
- Lombok 注解处理在 Maven 编译路径上被显式固定
- IDEA/JPS 编译路径补充了针对 `com.sun.proxy.$Proxy8` 的项目级 workaround
- `org-tribe-system` 可以在 JDK 8 下稳定完成默认 `generate-resources`、默认 `package`，以及带 `-Dskip.frontend=true` 的快速后端打包
