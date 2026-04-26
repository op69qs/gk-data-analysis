# 私有 Maven 包本地安装记录

## 目标

将仓库内的 `office-html-0.0.1.jar` 安装到本地 Maven 仓库，使 `dwbi-statistical-analysis` 中的以下依赖可以被正常解析：

```xml
<dependency>
    <groupId>office-html</groupId>
    <artifactId>office-html</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 实施过程

私有 jar 来源：`document/office-html-0.0.1.jar`

首次执行 `mvn install:install-file` 时，PowerShell 将 `-Dversion=0.0.1` 错误拆分，导致 Maven 实际安装成了 `0` 版本，并抛出 `Unknown lifecycle phase ".0.1"`。

最终使用 PowerShell 参数直传模式解决：

```powershell
mvn --% install:install-file -Dfile=d:\document\c-project\project-code\project-02\gk-data-analysis\document\office-html-0.0.1.jar -DgroupId=office-html -DartifactId=office-html -Dversion=0.0.1 -Dpackaging=jar
```

## 验证

执行以下命令验证本地仓库解析成功：

```powershell
mvn --% -f d:\document\c-project\project-code\project-02\gk-data-analysis\dwbi-statistical-analysis\pom.xml dependency:get -Dartifact=office-html:office-html:0.0.1
```

验证结果：构建成功，`office-html:office-html:0.0.1` 已可被本地解析。