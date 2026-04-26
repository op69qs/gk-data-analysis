# 2026-04-18 Backend Module Startup Notes

## Scope

- Workspace: `gk-data-analysis`
- Runtime: JDK 8 (`C:\Users\skyqty\.jdks\corretto-1.8.0_472\bin\java.exe`)
- Goal: start backend modules from packaged `target` directories and fix startup blockers.

## Key fixes

### dwbi-system-docking

- Removed stray `hh` text from `src/main/resources/mybatis/mysql/TreasuryAccessMapper.xml` so MyBatis XML can parse.

### dwbi-statistical-analysis

- Replaced old Spring Cloud starters with Netflix-specific starters.
- Added missing runtime dependencies required by shared security and DAO code:
  - `spring-boot-starter-security`
  - `pagehelper-spring-boot-starter`
  - `commons-pool2`
  - `jedis`
  - `cas-client-core`
  - `spring-security-cas`
  - `mysql-connector-java`
  - `jjwt`
  - `org.json`
- Fixed Redis config key in `application.yml` from `hostName` to `host`.
- Quoted the database password in YAML.
- Stopped using `--spring.config.location=application.yml` when launching from `target`; that option prevented `application.properties` from loading and broke `${DOWNLOAD_PATH}`.

### org-tribe-system

- Added missing runtime dependencies:
  - `commons-pool2`
  - `mysql-connector-java`
  - `freemarker`
  - `aliyun-java-sdk-dysmsapi`
- Added standard `spring.datasource.url/username/password/driver-class-name` to `application-dev.yml` so Spring Boot's default datasource/health path does not fail before dynamic datasource wiring finishes.

### seo

- Added `commons-pool2`.
- Launch from `target` must use only `--spring.profiles.active=dev`; forcing `spring.config.location` prevented `application.properties` from loading, which broke `spring.datasource.default.*` placeholders.

### fixedReport

- Launch from `target` must use only `--spring.profiles.active=dev`; forcing `spring.config.location` prevented `application.properties` from loading, which broke `${TEMPLATE_FILE_PATH}`.

## Final launch pattern

- Launch packaged jars from each module's `target` directory.
- Prefer `--spring.profiles.active=dev` only.
- Avoid overriding `spring.config.location` unless the full config chain is re-specified.

## Verified process state

At the end of this round, these packaged services were present in the Java process list:

- `indicatorsLib`
- `dwbi-system-docking`
- `fixedReport`
- `seo`
- `org-tribe-system`
- `dwbi-statistical-analysis`