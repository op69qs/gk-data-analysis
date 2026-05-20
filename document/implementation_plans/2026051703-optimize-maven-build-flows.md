# 优化 Maven 开发与交付构建流程

## 背景
- 仓库根工程聚合了多个后端模块，同时 `org-tribe-system` 又在常规 Maven 生命周期里绑定了前端 `npm install` 和 `npm run build`。
- 结果是即使只执行 `mvn compile`，也会先跑前端依赖安装与构建，开发反馈很慢，且在只读或受限环境中容易直接失败。
- `deploy-package-assembly` 也一直作为常规打包链路的一部分存在，不利于把“日常开发构建”和“最终交付装配”区分开。

## 目标
- `mvn package` 默认直接生成完整、最新的项目部署包。
- 只有在显式传参时，才跳过前端构建或交付装配。

## 本次调整

### 1. 根工程增加双模式开关
文件：`pom.xml`

默认行为：
- 不传 `skip.frontend.build` 时自动执行前端构建
- 不传 `skip.deploy.assembly` 时自动执行部署装配

兼容保留 profile：
- `full-package`
  - `skip.frontend.build=false`
  - `skip.deploy.assembly=false`

含义：
- 默认构建：执行前端构建并生成完整 deploy-package / ZIP 交付物。
- 显式跳过：通过 `-Dskip.frontend.build=true` 或 `-Dskip.deploy.assembly=true` 关闭对应步骤。
- `-Pfull-package`：保留为兼容入口，但不再是必需条件。

### 2. org-tribe-system 的前端链路改为按 profile 激活
文件：`org-tribe-system/pom.xml`

处理方式：
- 将以下逻辑从默认 `build/plugins` 中移出：
  - `frontend-maven-plugin`
  - 清理前端产物和准备静态目录的 `maven-antrun-plugin`
  - 复制 `org-tribe-view/dist` 到后端静态目录的 `maven-resources-plugin` 执行
- 改为放入 `with-frontend-build` profile 中
- 当未显式传 `-Dskip.frontend.build=true` 时自动激活

效果：
- `mvn package` 默认会构建前端并拷贝最新静态资源
- 如果只想跳过前端，可显式传 `-Dskip.frontend.build=true`

### 3. deploy-package-assembly 改为按 profile 激活
文件：`deploy-package-assembly/pom.xml`

处理方式：
- 将原有装配 `maven-antrun-plugin` 放入 `with-deploy-assembly` profile
- 当未显式传 `-Dskip.deploy.assembly=true` 时自动激活

效果：
- 默认 Maven 打包会顺手组装 `deploy-package/` 和最终 ZIP
- 如果只想跳过装配，可显式传 `-Dskip.deploy.assembly=true`

## 推荐命令

### 开发阶段
- 只编译 `org-tribe-system`
  - `cd org-tribe-system && mvn -DskipTests compile`
- 根工程完整打包并生成最新部署包
  - `mvn -DskipTests package`
- 根工程快速跳过前端与装配
  - `mvn -DskipTests package -Dskip.frontend.build=true -Dskip.deploy.assembly=true`

### 交付阶段
- 默认命令即可走完整前端 + 后端 + deploy-package 链路
  - `mvn -DskipTests package`
- 保留兼容写法
  - `mvn -Pfull-package -DskipTests package`

## 验证结果
- 已验证 `mvn -pl org-tribe-system -am clean package -DskipTests -Pfull-package` 会触发前端重建并产出最新静态资源
- 本次调整后，根工程默认属性已切换为完整构建开启；后续 `mvn package` 将直接复用同一条完整链路

## 后续建议
- 如果后面还想继续优化，可以再补一个显式快速构建 profile，例如 `fast-package`
- 当前先保留属性式跳过参数和 `full-package` 兼容入口，减少对现有命令习惯的冲击。
