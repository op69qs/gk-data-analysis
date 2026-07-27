# Vastbase Data Source Support Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add Vastbase as a PostgreSQL-compatible, schema-aware data source while preserving the existing Mysql, ClickHouse, DB2, dimension, and saved-query behavior.

**Architecture:** Persist the physical Vastbase database in `DBNAME` and its logical namespace in a new nullable `SCHEMA_NAME` column. Centralize JDBC URL and validation-query behavior in a small Java utility, expose an effective namespace from the mappers, and let the existing data-table and comprehensive-query flows continue to operate on `namespace.table` names.

**Tech Stack:** Java 8, Spring Boot, MyBatis XML, Druid dynamic data sources, PostgreSQL JDBC 42.2.27, Vue 2, Ant Design Vue, Maven/JUnit 4, Node.js static contract tests, Vastbase/PostgreSQL SQL.

## Global Constraints

- Vastbase uses `org.postgresql.Driver` and `jdbc:postgresql://<ip>:<port>/<database>?currentSchema=<schema>`.
- Vastbase follows the existing Mysql business workflow; do not introduce a separate dimension workflow.
- Do not change Mysql, ClickHouse, or DB2 URL generation, configuration semantics, or query behavior.
- Do not change `edw.fm_trs_guoku_base_table` or saved-query table structures.
- Preserve unrelated dirty-worktree changes; every commit stages only files listed in its task.
- Implement behavior test-first and run the targeted red/green cycle before each commit.
- Never print stored data-source passwords in test or command output.

---

### Task 1: Add the schema-aware persistence contract

**Files:**
- Create: `document/psql/vastbase/2026072701-add-vastbase-datasource-support.sql`
- Create: `seo/src/test/java/org/seo/dao/mapper/VastbaseDataSourceMapperContractTest.java`
- Modify: `seo/src/main/resources/mybatis/seo/DataSourceMapper.xml`

**Interfaces:**
- Consumes: existing tables `seo.seo_datasource_database` and `seo.seo_datasource_enum`.
- Produces: nullable column `seo.seo_datasource_database.schema_name`; mapper key `SCHEMA_NAME`; enum `Vastbase` with PostgreSQL-compatible URL template.

- [ ] **Step 1: Write the failing mapper and migration contract test**

Create a JUnit 4 test that loads the mapper and migration as text and asserts the exact contract:

```java
@Test
public void vastbaseSchemaPersistenceIsExplicitAndBackwardCompatible() throws Exception {
    String mapper = resource("mybatis/seo/DataSourceMapper.xml");
    String migration = file("document/psql/vastbase/2026072701-add-vastbase-datasource-support.sql");

    assertTrue(mapper.contains("a.SCHEMA_NAME as \"SCHEMA_NAME\""));
    assertTrue(mapper.contains("SCHEMA_NAME"));
    assertTrue(mapper.contains("INSERT INTO seo.seo_datasource_database"));
    assertTrue(mapper.contains("(ID, SOURCE_ID, DBNAME, SCHEMA_NAME"));
    assertTrue(migration.contains("ADD COLUMN schema_name varchar(100)"));
    assertTrue(migration.contains("'Vastbase'"));
    assertTrue(migration.contains("'org.postgresql.Driver'"));
    assertTrue(migration.contains("currentSchema=SCHEMA_NAME"));
}
```

Use the same UTF-8 resource helper pattern as `ComprehensiveQueryMapperContractTest`. Resolve the repository root for the migration with `Paths.get("..", "document", "psql", "vastbase", ...)` when Maven runs from `seo/`.

- [ ] **Step 2: Run the test and confirm the red state**

Run:

```bash
mvn -q -Dtest=VastbaseDataSourceMapperContractTest test
```

Expected: FAIL because the migration and `SCHEMA_NAME` mapping do not exist.

- [ ] **Step 3: Create the idempotent Vastbase migration**

The migration must add the column only when absent and add the enum only when `DATASOURCE='Vastbase'` is absent:

```sql
DO $migration$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'seo'
          AND table_name = 'seo_datasource_database'
          AND column_name = 'schema_name'
    ) THEN
        ALTER TABLE seo.seo_datasource_database
            ADD COLUMN schema_name varchar(100);
    END IF;
END
$migration$;

INSERT INTO seo.seo_datasource_enum (id, datasource, url, driverclass)
SELECT '4', 'Vastbase',
       'jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME',
       'org.postgresql.Driver'
WHERE NOT EXISTS (
    SELECT 1 FROM seo.seo_datasource_enum WHERE datasource = 'Vastbase'
);
```

Wrap the script in a transaction and add verification queries as comments. Do not update the three existing enum rows.

- [ ] **Step 4: Make mapper reads and writes explicit**

In every data-source/database select, add:

```xml
b.SCHEMA_NAME AS "SCHEMA_NAME"
```

For `getDataBase`, use alias `a.SCHEMA_NAME AS "SCHEMA_NAME"` and add an exact optional filter:

```xml
<if test="params.SCHEMA_NAME!=null and params.SCHEMA_NAME != ''">
    AND a.SCHEMA_NAME = #{params.SCHEMA_NAME}
</if>
```

Replace positional database insertion with named columns:

```xml
INSERT INTO seo.seo_datasource_database
(ID, SOURCE_ID, DBNAME, SCHEMA_NAME, USERNAME, PASSWORD, STATE,
 CREATE_TIME, CREATE_USER, DRIVERCLASS_NAME, DATASOURCE_URL)
VALUES
(#{params.ID}, #{params.SOURCE_ID}, #{params.DBNAME}, #{params.SCHEMA_NAME},
 #{params.USERNAME}, #{params.PASSWORD}, #{params.STATE},
 #{params.CREATE_TIME}, #{params.CREATE_USER}, #{params.DRIVERCLASS_NAME},
 #{params.DATASOURCE_URL})
```

- [ ] **Step 5: Run the persistence contract tests**

Run:

```bash
mvn -q -Dtest=VastbaseDataSourceMapperContractTest,ComprehensiveQueryMapperContractTest test
```

Expected: PASS.

- [ ] **Step 6: Commit the persistence slice**

```bash
git add document/psql/vastbase/2026072701-add-vastbase-datasource-support.sql \
  seo/src/test/java/org/seo/dao/mapper/VastbaseDataSourceMapperContractTest.java \
  seo/src/main/resources/mybatis/seo/DataSourceMapper.xml
git commit -m "feat: persist Vastbase schema configuration"
```

---

### Task 2: Centralize Vastbase JDBC behavior

**Files:**
- Create: `seo/src/main/java/org/seo/util/DataSourceConnectionSupport.java`
- Create: `seo/src/test/java/org/seo/util/DataSourceConnectionSupportTest.java`
- Modify: `seo/src/main/java/org/seo/controller/DataSourceController.java`
- Modify: `seo/src/main/java/org/seo/config/InitTargetDataSources.java`

**Interfaces:**
- Consumes: enum URL template plus `TYPE`, `IP`, `PORT`, `DBNAME`, `SCHEMA_NAME`.
- Produces: `DataSourceConnectionSupport.buildUrl(...)`, `validationQuery(...)`, and `namespace(...)`.

- [ ] **Step 1: Write failing utility tests**

```java
@Test
public void buildsVastbaseUrlWithDatabaseAndSchema() {
    assertEquals(
        "jdbc:postgresql://100.71.11.54:25432/gk_data_analysis?currentSchema=edw",
        DataSourceConnectionSupport.buildUrl(
            "jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME",
            "100.71.11.54", "25432", "gk_data_analysis", "edw"));
}

@Test(expected = IllegalArgumentException.class)
public void rejectsMissingVastbaseSchema() {
    DataSourceConnectionSupport.buildUrl(
        "jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME",
        "127.0.0.1", "5432", "gk_data_analysis", "");
}

@Test
public void keepsLegacyUrlTemplatesUnchanged() {
    assertEquals(
        "jdbc:mysql://127.0.0.1:3306/ods?useSSL=false",
        DataSourceConnectionSupport.buildUrl(
            "jdbc:mysql://ip:port/DBNAME?useSSL=false",
            "127.0.0.1", "3306", "ods", null));
}

@Test
public void usesPortableValidationQueryForPostgresqlDriver() {
    assertEquals("SELECT 1",
        DataSourceConnectionSupport.validationQuery("org.postgresql.Driver"));
}
```

- [ ] **Step 2: Run tests and confirm they fail**

```bash
mvn -q -Dtest=DataSourceConnectionSupportTest test
```

Expected: compilation failure because the utility does not exist.

- [ ] **Step 3: Implement the focused utility**

```java
public final class DataSourceConnectionSupport {
    private DataSourceConnectionSupport() {}

    public static String buildUrl(String template, String ip, String port,
                                  String database, String schema) {
        if (template.contains("SCHEMA_NAME") && (schema == null || schema.trim().isEmpty())) {
            throw new IllegalArgumentException("Vastbase Schema不能为空");
        }
        return template.replace("ip", ip)
            .replace("port", port)
            .replace("DBNAME", database)
            .replace("SCHEMA_NAME", schema == null ? "" : schema);
    }

    public static String validationQuery(String driver) {
        String value = driver == null ? "" : driver.toLowerCase();
        return value.contains("db2")
            ? "SELECT 1 FROM SYSIBM.SYSDUMMY1"
            : "SELECT 1";
    }

    public static String namespace(String type, String database, String schema) {
        return "Vastbase".equals(type) ? schema : database;
    }

    public static boolean schemaExists(Connection connection, String schema)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM information_schema.schemata WHERE schema_name = ?")) {
            statement.setString(1, schema);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}
```

- [ ] **Step 4: Route URL generation and connection testing through the utility**

In `DataSourceController.getEnum`, replace chained template substitution with:

```java
url = DataSourceConnectionSupport.buildUrl(
    dataSourceEnum.get(0).get("URL").toString(),
    pd.getString("IP"), pd.getString("PORT"),
    pd.getString("DBNAME"), pd.getString("SCHEMA_NAME"));
```

In `testConnection`, build the Vastbase URL through the same method, use `DBHelper.initPostgresql`, then call `schemaExists(conn, SCHEMA_NAME)`. Return `result=false` and `msg=Vastbase Schema不存在` when it returns false. Preserve the existing response shape and do not alter Mysql, ClickHouse, or DB2 branches.

In `editDataSource`, parse Schema values in parallel with existing database values so each recreated child record keeps the correct tuple:

```java
String schemaNamesValue = pd.getString("SCHEMA_NAME");
String[] schemaNames = schemaNamesValue == null || schemaNamesValue.isEmpty()
    ? new String[dbNames.length]
    : schemaNamesValue.split(",", -1);
if (schemaNames.length != dbNames.length) {
    throw new IllegalArgumentException("数据库与Schema配置数量不一致");
}
for (int i = 0; i < dbNames.length; i++) {
    pd.put("DBNAME", dbNames[i]);
    pd.put("SCHEMA_NAME", schemaNames[i]);
    // Preserve the existing username, password, state, ID, and insert flow.
}
```

- [ ] **Step 5: Fix dynamic-source health checks**

Replace the driver-specific chain in `InitTargetDataSources` with:

```java
druidDataSource.setValidationQuery(
    DataSourceConnectionSupport.validationQuery(x.getDRIVERCLASS_NAME()));
```

This changes PostgreSQL/Vastbase from `SELECT 1 FROM DUAL` to `SELECT 1` while preserving DB2.

- [ ] **Step 6: Run targeted and full SEO tests**

```bash
mvn -q -Dtest=DataSourceConnectionSupportTest test
mvn -q test
```

Expected: PASS.

- [ ] **Step 7: Commit the JDBC slice**

```bash
git add seo/src/main/java/org/seo/util/DataSourceConnectionSupport.java \
  seo/src/test/java/org/seo/util/DataSourceConnectionSupportTest.java \
  seo/src/main/java/org/seo/controller/DataSourceController.java \
  seo/src/main/java/org/seo/config/InitTargetDataSources.java
git commit -m "feat: connect Vastbase through PostgreSQL JDBC"
```

---

### Task 3: Expose Schema through metadata and tree contracts

**Files:**
- Modify: `seo/src/test/java/org/seo/dao/mapper/VastbaseDataSourceMapperContractTest.java`
- Modify: `seo/src/main/resources/mybatis/seo/DataAuxiliaryMapper.xml`
- Modify: `seo/src/main/resources/mybatis/seo/DataTableMapper.xml`
- Modify: `seo/src/main/resources/mybatis/seo/ComprehensiveQueryMapper.xml`
- Modify: `seo/src/main/java/org/seo/controller/DataTableController.java`

**Interfaces:**
- Consumes: persisted `DBNAME`, `SCHEMA_NAME`, and `TYPE`.
- Produces: mapper alias `NAMESPACE`; effective `DBNAME` for existing comprehensive-query callers; Schema labels in tree/select APIs.

- [ ] **Step 1: Add failing namespace contract assertions**

```java
@Test
public void vastbaseUsesSchemaAsEffectiveNamespace() throws Exception {
    String sourceMapper = resource("mybatis/seo/DataSourceMapper.xml");
    String auxiliaryMapper = resource("mybatis/seo/DataAuxiliaryMapper.xml");
    String tableMapper = resource("mybatis/seo/DataTableMapper.xml");
    String queryMapper = resource("mybatis/seo/ComprehensiveQueryMapper.xml");

    assertTrue(auxiliaryMapper.contains("AS \"NAMESPACE\""));
    assertTrue(auxiliaryMapper.contains("b.SCHEMA_NAME"));
    assertTrue(tableMapper.contains("a.TYPE = 'Vastbase'"));
    assertTrue(queryMapper.contains("AS \"SCHEMA_NAME\""));
    assertTrue(queryMapper.contains("AS \"DBNAME\""));
    assertTrue(sourceMapper.contains("SCHEMA_NAME"));
}
```

- [ ] **Step 2: Run the contract test and confirm failure**

```bash
mvn -q -Dtest=VastbaseDataSourceMapperContractTest test
```

Expected: FAIL on missing effective namespace aliases.

- [ ] **Step 3: Make table and database selectors Schema-aware**

In `DataAuxiliaryMapper.getDataBaseSelection`, join the source and select the effective display namespace:

```sql
SELECT b.ID AS id,
       CASE WHEN a.TYPE = 'Vastbase' THEN b.SCHEMA_NAME ELSE b.DBNAME END AS name
FROM seo.seo_datasource_database b
JOIN seo.seo_datasource a ON a.ID = b.SOURCE_ID
WHERE b.SOURCE_ID = #{params.SOURCE_ID}
  AND b.STATE = '0'
```

For Vastbase table and comment lookups, replace `#{params.DATABASE}` with `#{params.SCHEMA_NAME}`. The table-list branch must accept both `BASE TABLE` and `VIEW`:

```sql
WHERE table_schema = #{params.SCHEMA_NAME}
  AND table_type IN ('BASE TABLE', 'VIEW')
```

Extend `getDataSourceInfo` with:

```sql
b.DBNAME,
b.SCHEMA_NAME,
CASE WHEN a.TYPE = 'Vastbase' THEN b.SCHEMA_NAME ELSE b.DBNAME END AS "NAMESPACE"
```

- [ ] **Step 4: Make the maintenance tree show Schema**

In the database branch of `DataTableMapper.getDataSourceTree`, join `seo_datasource` and label nodes with:

```sql
CASE WHEN a.TYPE = 'Vastbase' THEN b.SCHEMA_NAME ELSE b.DBNAME END AS lable
```

Keep node ID as `b.ID`, parent ID as `b.SOURCE_ID`, and node type as `database`.

- [ ] **Step 5: Preserve comprehensive-query callers with an effective alias**

Change `ComprehensiveQueryMapper.getType` to return both the physical values and the effective namespace:

```sql
SELECT a.DBNAME AS "DATABASE_NAME",
       a.SCHEMA_NAME AS "SCHEMA_NAME",
       CASE WHEN b.TYPE = 'Vastbase' THEN a.SCHEMA_NAME ELSE a.DBNAME END AS "DBNAME",
       b.TYPE AS "TYPE"
FROM seo.seo_datasource_database a
JOIN seo.seo_datasource b ON a.SOURCE_ID = b.ID
WHERE a.ID = #{params.ID}
```

Existing controller expressions `type.get("DBNAME") + "." + table` then produce `schema.table` for Vastbase and retain `database.table` for legacy types.

- [ ] **Step 6: Pass the effective namespace to comment refresh**

In `DataTableController.getColumnList`:

```java
pageData.put("DATABASE", sourceMap.get("DBNAME"));
pageData.put("SCHEMA_NAME", sourceMap.get("NAMESPACE"));
```

The legacy mapper branches continue reading `DATABASE`; the Vastbase branch reads `SCHEMA_NAME`.

- [ ] **Step 7: Run mapper and SEO regression tests**

```bash
mvn -q -Dtest=VastbaseDataSourceMapperContractTest,ComprehensiveQueryMapperContractTest test
mvn -q test
```

Expected: PASS.

- [ ] **Step 8: Commit the metadata slice**

```bash
git add seo/src/test/java/org/seo/dao/mapper/VastbaseDataSourceMapperContractTest.java \
  seo/src/main/resources/mybatis/seo/DataAuxiliaryMapper.xml \
  seo/src/main/resources/mybatis/seo/DataTableMapper.xml \
  seo/src/main/resources/mybatis/seo/ComprehensiveQueryMapper.xml \
  seo/src/main/java/org/seo/controller/DataTableController.java
git commit -m "feat: browse Vastbase schemas and tables"
```

---

### Task 4: Add Schema to the data-source form without changing legacy UX

**Files:**
- Create: `org-tribe-view/tests/vastbaseDatasourceSupport.test.mjs`
- Modify: `org-tribe-view/src/views/statistics/modules/DataSourceModal.vue`

**Interfaces:**
- Consumes: `dataBaseType` API results and database records containing `SCHEMA_NAME`.
- Produces: add/edit/test payloads containing `SCHEMA_NAME` only for Vastbase; uniqueness request containing both `DBNAME` and `SCHEMA_NAME`.

- [ ] **Step 1: Write the failing frontend contract test**

```javascript
const modal = source('../src/views/statistics/modules/DataSourceModal.vue')

assert.match(modal, /label=["']Schema["']/)
assert.match(modal, /v-if=["']isVastbase["']/)
assert.match(modal, /SCHEMA_NAME/)
assert.match(modal, /params\.SCHEMA_NAME\.push/)
assert.match(modal, /DBNAME:\s*values\.DBNAME[\s\S]*SCHEMA_NAME:\s*values\.SCHEMA_NAME/)
assert.match(modal, /this\.selectedType\s*===\s*['"]Vastbase['"]/)
```

Also assert that the existing status-code behavior remains present.

- [ ] **Step 2: Run the frontend test and confirm failure**

```bash
/root/.local/share/fnm/node-versions/v22.22.2/installation/bin/node \
  tests/vastbaseDatasourceSupport.test.mjs
```

Expected: FAIL because the form has no Schema contract.

- [ ] **Step 3: Add conditional Schema fields**

Add a computed property:

```javascript
computed: {
  isVastbase() {
    return this.selectedType === 'Vastbase'
  }
}
```

Add `selectedType: ''` to component data, set it from `record.TYPE` in edit mode, clear it in add mode, and add `@change="handleTypeChange"` to the type select:

```javascript
handleTypeChange(value) {
  this.selectedType = value
  if (value !== 'Vastbase') {
    this.form.setFieldsValue({ SCHEMA_NAME: undefined })
  }
}
```

For add mode, render the required field only when Vastbase is selected:

```vue
<a-form-item v-if="isVastbase" label="Schema"
             :labelCol="labelCol" :wrapperCol="wrapperCol">
  <a-input v-decorator="['SCHEMA_NAME', {
    rules: [{ required: true, message: '请输入Schema' }]
  }]"/>
</a-form-item>
```

For edit mode, add a `SCHEMA_NAME` table column shown for Vastbase. Include it in `loadData`, `handleChange`, empty validation, and comma-separated edit payloads.

- [ ] **Step 4: Make duplicate checks schema-aware**

For a new Vastbase child, call:

```javascript
getDataBase({
  SOURCE_ID: formData.ID,
  DBNAME: values.DBNAME,
  SCHEMA_NAME: values.SCHEMA_NAME
})
```

For non-Vastbase types, omit `SCHEMA_NAME` so the old check remains unchanged. In edit mode, compare the tuple `${DBNAME}\u0000${SCHEMA_NAME || ''}` instead of only `DBNAME`.

- [ ] **Step 5: Run frontend contract and production build**

```bash
/root/.local/share/fnm/node-versions/v22.22.2/installation/bin/node \
  tests/vastbaseDatasourceSupport.test.mjs
/root/.local/share/fnm/node-versions/v22.22.2/installation/bin/node \
  tests/comprehensiveQueryIssue6.test.mjs
npm run build
```

Expected: both tests PASS; build exits 0 with no new error (existing CSS order/size warnings are acceptable).

- [ ] **Step 6: Commit the frontend slice**

```bash
git add org-tribe-view/tests/vastbaseDatasourceSupport.test.mjs \
  org-tribe-view/src/views/statistics/modules/DataSourceModal.vue
git commit -m "feat: configure Vastbase schemas in datasource UI"
```

---

### Task 5: Apply the migration and verify the live Vastbase flow

**Files:**
- Modify only if a discovered defect requires it: files already listed in Tasks 1-4.
- Test evidence: runtime commands and API responses; do not commit credentials or response dumps.

**Interfaces:**
- Consumes: migration script, rebuilt SEO service, and local Vastbase database `gk_data_analysis`.
- Produces: live proof for enum, connection, schema table discovery, maintenance tree, and comprehensive query execution.

- [ ] **Step 1: Back up configuration tables**

Create uniquely named backup tables before migration:

```sql
CREATE SCHEMA IF NOT EXISTS codex_backup;
CREATE TABLE codex_backup.seo_datasource_database_before_vastbase_support AS
TABLE seo.seo_datasource_database;
CREATE TABLE codex_backup.seo_datasource_enum_before_vastbase_support AS
TABLE seo.seo_datasource_enum;
```

Abort if those backup names already exist instead of overwriting them.

- [ ] **Step 2: Apply the migration with fail-fast behavior**

```bash
psql -v ON_ERROR_STOP=1 -f \
  document/psql/vastbase/2026072701-add-vastbase-datasource-support.sql
```

Use the configured test connection without printing its password. Run the migration twice; the second run must succeed without adding duplicate fields or enum rows.

- [ ] **Step 3: Restart SEO and verify the enum**

Restart the local SEO module using the established Maven command, wait for port 9094, then call:

```bash
curl -sS -H 'Content-Type: application/json' -d '{}' \
  http://127.0.0.1:9094/dataSourceController/getDataSourceEnumSelect
```

Expected: exactly one `{ "id": "Vastbase", "name": "Vastbase" }` entry in addition to the legacy entries.

- [ ] **Step 4: Verify connection testing against local Vastbase**

POST a request using test host `100.71.11.54`, database `gk_data_analysis`, and Schema `edw`. Read the password from local configuration into a shell variable and construct the request with `jq` so it is not written to command output:

```bash
password="$(sed -n 's/^spring.datasource.default.password=//p' \
  seo/src/main/resources/application.properties)"
jq -n --arg password "$password" '{
  TYPE: "Vastbase",
  IP: "100.71.11.54",
  PORT: "25432",
  DBNAME: "gk_data_analysis",
  SCHEMA_NAME: "edw",
  USERNAME: "vastbase_test",
  PASSWORD: $password
}' | curl -sS -H 'Content-Type: application/json' --data-binary @- \
  http://127.0.0.1:9094/dataSourceController/testConnection
```

Expected: `result=success`. Repeat with a nonexistent Schema and expect `result=false` with a Schema-specific message.

- [ ] **Step 5: Verify schema metadata through a temporary configuration**

Insert a temporary Vastbase source and database row with unique IDs, restart or wait for the dynamic-source refresh, and call `getDataTableSelection` and `getDataTableComments`. Expected: tables from `edw` and their columns are returned. Delete only the temporary rows after evidence is captured.

- [ ] **Step 6: Verify tree and comprehensive-query contracts**

Temporarily map one known metadata table to the temporary Vastbase `DATABASE_ID`, then verify:

- data-table tree second-level label is `edw`;
- table node is attached under that Schema;
- `getType` returns `DATABASE_NAME=gk_data_analysis`, `SCHEMA_NAME=edw`, effective `DBNAME=edw`, `TYPE=Vastbase`;
- a read-only comprehensive query executes `edw.<table>` and returns rows.

Restore the metadata row from its backup immediately after the check.

- [ ] **Step 7: Run all regression tests**

```bash
cd seo && mvn -q test
cd ../org-tribe-view && \
  /root/.local/share/fnm/node-versions/v22.22.2/installation/bin/node \
  tests/comprehensiveQueryIssue6.test.mjs && \
  /root/.local/share/fnm/node-versions/v22.22.2/installation/bin/node \
  tests/vastbaseDatasourceSupport.test.mjs && \
  npm run build
```

Expected: all commands exit 0.

---

### Task 6: Sync the built frontend and perform browser verification

**Files:**
- Generated, not committed: `org-tribe-view/dist/**`
- Generated, not committed: `org-tribe-system/target/classes/static/**`

**Interfaces:**
- Consumes: verified frontend production build.
- Produces: port 9090 serves the new bundle and the authenticated pages expose Vastbase without console/network errors.

- [ ] **Step 1: Copy the verified frontend build through the existing Maven execution**

```bash
cd org-tribe-system
mvn resources:copy-resources@copy-frontend-static
```

Expected: copy exits 0 and `cmp` reports that `dist/index.html` equals `target/classes/static/index.html`.

- [ ] **Step 2: Verify the served bundle**

```bash
curl -sS http://127.0.0.1:9090/
```

Expected: HTML references the newly built app/chunk hashes. The Vastbase chunk contains `SCHEMA_NAME` and `Vastbase`.

- [ ] **Step 3: Run authenticated browser checks**

Using the browser-testing-with-devtools workflow, verify:

1. 数据源维护下拉 contains Vastbase.
2. Selecting Vastbase shows required Database and Schema fields.
3. Connection test succeeds for `gk_data_analysis/edw`.
4. Edit mode round-trips Schema.
5. 数据表维护 shows the Vastbase Schema and tables.
6. 数据查询 loads fields and executes a read-only query.
7. 维度表 behavior is unchanged.
8. Console has no new exception and relevant network requests return no 500.

If authentication is unavailable, record that limitation and provide direct API plus static-bundle evidence instead of claiming browser-level completion.

- [ ] **Step 4: Review the final diff and commit any verification-only test adjustment**

```bash
git diff --check
git status --short
```

Do not stage `dist`, `target`, archive files, credentials, backup dumps, or unrelated pre-existing changes. If no source correction was needed during runtime verification, no final commit is required.
