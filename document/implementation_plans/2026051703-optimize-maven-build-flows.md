# 优化 Maven 开发与交付构建流程

## 背景
- 仓库根工程聚合了多个后端模块，同时 `org-tribe-system` 又在常规 Maven 生命周期里绑定了前端 `npm install` 和 `npm run build`。
- 结果是即使只执行 `mvn compile`，也会先跑前端依赖安装与构建，开发反馈很慢，且在只读或受限环境中容易直接失败。
- `deploy-package-assembly` 也一直作为常规打包链路的一部分存在，不利于把“日常开发构建”和“最终交付装配”区分开。

## 目标
- 开发默认走快速 Java 构建，不主动触发前端安装和交付装配。
- 需要发布交付包时，再显式开启完整链路。

## 本次调整

### 1. 根工程增加双模式开关
文件：`pom.xml`

新增属性：
- `skip.frontend.build=true`
- `skip.deploy.assembly=true`

新增 profile：
- `full-package`
  - `skip.frontend.build=false`
  - `skip.deploy.assembly=false`

含义：
- 默认构建：跳过前端构建与 deploy-package 装配。
- `-Pfull-package`：开启完整交付链路。

### 2. org-tribe-system 的前端链路改为按 profile 激活
文件：`org-tribe-system/pom.xml`

处理方式：
- 将以下逻辑从默认 `build/plugins` 中移出：
  - `frontend-maven-plugin`
  - 清理前端产物和准备静态目录的 `maven-antrun-plugin`
  - 复制 `org-tribe-view/dist` 到后端静态目录的 `maven-resources-plugin` 执行
- 改为放入 `with-frontend-build` profile 中
- 当 `skip.frontend.build=false` 时自动激活

效果：
- `mvn compile` / `mvn package` 默认只编译后端，不再先做 `npm install`
- 需要全量打包时，才会构建前端并拷贝静态资源

### 3. deploy-package-assembly 改为按 profile 激活
文件：`deploy-package-assembly/pom.xml`

处理方式：
- 将原有装配 `maven-antrun-plugin` 放入 `with-deploy-assembly` profile
- 当 `skip.deploy.assembly=false` 时自动激活

效果：
- 默认 Maven 打包不再顺手组装 `deploy-package/`
- 只有交付场景才执行部署目录装配

## 推荐命令

### 开发阶段
- 只编译 `org-tribe-system`
  - `cd org-tribe-system && mvn -DskipTests compile`
- 根工程快速打包后端模块
  - `mvn -DskipTests package`

### 交付阶段
- 走完整前端 + 后端 + deploy-package 链路
  - `mvn -Pfull-package -DskipTests package`

## 验证结果
- 已验证 `cd org-tribe-system && mvn -DskipTests compile` 可以直接进入 Java 资源复制与编译流程，不再触发 `frontend-maven-plugin` 与 `npm install`
- 当前环境下该命令编译成功

## 后续建议
- 如果后面还想继续优化，可以再补两个显式 profile 名称：
  - `dev-fast`
  - `dist-package`
- 目前先保留一个 `full-package`，对现有团队习惯的侵入最小。
