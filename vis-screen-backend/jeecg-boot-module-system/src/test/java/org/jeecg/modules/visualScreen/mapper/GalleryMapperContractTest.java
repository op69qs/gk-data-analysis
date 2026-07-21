package org.jeecg.modules.visualScreen.mapper;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GalleryMapperContractTest {
    private static final List<String> REQUIRED_TABLES = Arrays.asList(
            "visual_screen.vs_lib_index_scheme",
            "visual_screen.vs_gallery_info",
            "visual_screen.sys_user",
            "indicators_lib.lib_index_relation",
            "edw.cm_guoku_dimnsn");

    private static final List<String> REQUIRED_COLUMNS = Arrays.asList(
            "visual_screen.vs_lib_index_scheme.id",
            "visual_screen.vs_lib_index_scheme.scheme_descr",
            "visual_screen.vs_lib_index_scheme.scheme_sql",
            "visual_screen.vs_lib_index_scheme.scheme_colums",
            "visual_screen.vs_lib_index_scheme.scheme_conditon",
            "visual_screen.vs_lib_index_scheme.add_userid",
            "visual_screen.vs_lib_index_scheme.add_date",
            "visual_screen.vs_gallery_info.id",
            "visual_screen.vs_gallery_info.option",
            "visual_screen.vs_gallery_info.query_path",
            "visual_screen.vs_gallery_info.content",
            "visual_screen.vs_gallery_info.type",
            "visual_screen.vs_gallery_info.title",
            "visual_screen.vs_gallery_info.sort",
            "visual_screen.vs_gallery_info.state",
            "visual_screen.vs_gallery_info.business_id",
            "visual_screen.vs_gallery_info.time_type",
            "visual_screen.vs_gallery_info.dimension_type",
            "visual_screen.sys_user.id",
            "visual_screen.sys_user.realname",
            "indicators_lib.lib_index_relation.index_id",
            "indicators_lib.lib_index_relation.index_name",
            "indicators_lib.lib_index_relation.index_type",
            "indicators_lib.lib_index_relation.index_corre_table",
            "edw.cm_guoku_dimnsn.guoku_dscr",
            "edw.cm_guoku_dimnsn.guoku_id",
            "edw.cm_guoku_dimnsn.area_dscr",
            "edw.cm_guoku_dimnsn.area_no_id");

    private static final List<String> COMPATIBILITY_COLUMNS = Arrays.asList(
            "dacct_radio", "title_old", "add_time", "add_user",
            "index_scheme_id", "index_scheme_name", "condition");

    private static final List<String> PRODUCTION_COLUMNS = Arrays.asList(
            "id", "option", "query_path", "content", "type", "title", "sort", "state",
            "business_id", "time_type", "dimension_type", "dacct_radio", "title_old",
            "add_time", "add_user", "index_scheme_id", "index_scheme_name", "condition");

    private static final List<String> PRODUCTION_BINDINGS = Arrays.asList(
            "id", "option", "query_path", "content", "type", "title", "sort", "state",
            "business_id", "time_type", "dimension_type", "dacct_radio", "title_old",
            "add_time", "add_user", "scheme_id", "scheme_name", "condition");

    private String mapperXml;

    @Before
    public void loadMapper() throws Exception {
        mapperXml = loadResource(
                "org/jeecg/modules/visualScreen/mapper/xml/GalleryMapper.xml");
    }

    @Test
    public void galleryAddUsesExactProductionColumnsAndBindings() {
        String add = statement(mapperXml, "insert", "add");
        Matcher matcher = Pattern.compile(
                "vs_gallery_info\\s*\\((.*?)\\)\\s*VALUES\\s*\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(add);

        assertTrue("Gallery add must use an explicit column list", matcher.find());
        assertEquals(PRODUCTION_COLUMNS, commaSeparatedNames(matcher.group(1)));
        assertEquals(PRODUCTION_BINDINGS, parameterNames(matcher.group(2)));
        assertFalse("Gallery add must not depend on physical table column order",
                Pattern.compile("vs_gallery_info\\s+VALUES",
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(add).find());
    }

    @Test
    public void affectedMapperResourcesRemainWellFormedXml() throws Exception {
        assertWellFormedXml(mapperXml);
        assertWellFormedXml(loadResource(
                "org/jeecg/modules/indexlib/mapper/xml/IndexSchemeMapper.xml"));
    }

    @Test
    public void databaseContractFailsClosedAndNeverCreatesBusinessTables()
            throws Exception {
        String sql = databaseContract().toLowerCase(Locale.ROOT);
        String requiredTableGate = block(normalize(sql), "$contract$");

        assertTrue(sql.contains("before"));
        assertTrue(sql.contains("after"));
        assertTrue(sql.contains("information_schema.tables"));
        assertTrue(sql.contains("information_schema.columns"));
        assertTrue(sql.contains("pg_proc"));
        assertTrue(sql.contains("pg_namespace"));
        assertTrue(sql.contains("raise exception 'missing required production table: %'"));
        assertFalse(sql.matches("(?s).*create\\s+table\\b.*"));
        assertFalse(sql.matches("(?s).*(drop|truncate|delete\\s+from)\\s+"
                + "(visual_screen|indicators_lib)\\..*"));

        for (String table : REQUIRED_TABLES) {
            String[] parts = table.split("\\.");
            String tuple = "'" + parts[0] + "', '" + parts[1] + "'";
            assertTrue("Missing production table gate: " + table,
                    requiredTableGate.contains(tuple));
        }

        int tableGate = sql.indexOf(
                "raise exception 'missing required production table: %'");
        int columnGate = sql.indexOf(
                "raise exception 'missing required production column: %'");
        int firstAlter = sql.indexOf("alter table visual_screen.vs_gallery_info");
        int firstFunction = sql.indexOf("create or replace function");
        assertTrue("Required-table gate must precede all schema changes",
                tableGate >= 0 && firstAlter > tableGate);
        assertTrue("Required-column gate must precede all schema changes",
                columnGate >= 0 && firstAlter > columnGate);
        assertTrue("Required-column gate must precede function replacement",
                firstFunction > columnGate);
        assertTrue("Required-column gate must run after the required-table gate",
                columnGate > tableGate);
    }

    @Test
    public void databaseContractChecksEveryRequiredProductionColumnPrecisely()
            throws Exception {
        String sql = normalize(databaseContract());
        String requiredColumnsGate = block(sql, "$required_columns$");

        assertTrue(requiredColumnsGate.contains(
                "raise exception 'missing required production column: %'"));
        assertTrue(requiredColumnsGate.contains(
                "required_column.table_schema || '.' || "
                        + "required_column.table_name || '.' || "
                        + "required_column.column_name"));

        for (String qualifiedColumn : REQUIRED_COLUMNS) {
            String[] parts = qualifiedColumn.split("\\.");
            String tuple = "'" + parts[0] + "', '" + parts[1] + "', '"
                    + parts[2] + "'";
            assertTrue("Missing required production column gate: " + qualifiedColumn,
                    requiredColumnsGate.contains(tuple));
        }

        for (String compatibilityColumn : COMPATIBILITY_COLUMNS) {
            assertFalse("Compatibility column must remain addable: "
                            + compatibilityColumn,
                    requiredColumnsGate.contains("'" + compatibilityColumn + "'"));
        }
    }

    @Test
    public void databaseContractOnlyAddsMissingProductionGalleryColumnsAsText()
            throws Exception {
        String sql = normalize(databaseContract());

        for (String column : COMPATIBILITY_COLUMNS) {
            assertTrue("Missing idempotent production gallery column: " + column,
                    sql.contains("execute 'alter table visual_screen.vs_gallery_info "
                            + "add column " + column + " text'"));
            assertTrue("Missing information_schema guard for gallery column: " + column,
                    sql.contains("actual.column_name = '" + column + "'"));
        }
        assertFalse(sql.matches("(?s).*alter\\s+column.*"));
        assertFalse(sql.matches("(?s).*drop\\s+column.*"));
        assertEquals("Only the seven approved compatibility columns may be added",
                COMPATIBILITY_COLUMNS.size(),
                countMatches(sql, "alter table visual_screen\\.vs_gallery_info "
                        + "add column"));
        assertFalse(sql.matches("(?s).*\\b(drop\\s+table|truncate|delete\\s+from|"
                + "insert\\s+into|update\\s+)\\b.*"));
    }

    @Test
    public void indexNameFunctionPreservesFirstCsvPositionAndProductionEmptyValue()
            throws Exception {
        String sql = normalize(databaseContract());

        assertTrue(sql.contains("create or replace function "
                + "visual_screen.f_get_indexname"));
        assertTrue(sql.contains("indicators_lib.lib_index_relation"));
        assertTrue(sql.contains("string_agg("));
        assertTrue(sql.contains("order by strpos("));
        assertTrue(sql.contains("',' || scheme_columns || ','"));
        assertTrue(sql.contains("',' || r.index_id || ','"));
        assertTrue("Production function returns an empty string for no matches",
                Pattern.compile("coalesce\\s*\\(\\s*string_agg\\(.*?\\)\\s*,\\s*''\\s*\\)",
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(sql).find());
    }

    @Test
    public void indexNameFunctionProvidesTextSignatureAndKeepsVarcharCompatibility()
            throws Exception {
        String sql = normalize(databaseContract());

        assertTrue("The text column used by IndexSchemeMapper needs an exact overload",
                sql.contains("create or replace function "
                        + "visual_screen.f_get_indexname( scheme_columns text)"));
        assertTrue("The historical varchar(1000) entry point must remain available",
                sql.contains("create or replace function "
                        + "visual_screen.f_get_indexname( "
                        + "varchar(1000))"));
        assertEquals("Only the text implementation and varchar compatibility overload "
                        + "are expected",
                2,
                countMatches(sql, "create or replace function\\s+"
                        + "visual_screen\\.f_get_indexname\\s*\\("));
        assertTrue("The varchar overload must delegate explicitly to the text overload",
                sql.contains("visual_screen.f_get_indexname($1::text)"));
    }

    @Test
    public void databaseContractReportsAndValidatesExactIndexNameIdentityArguments()
            throws Exception {
        String sql = normalize(databaseContract());

        assertTrue(sql.contains("pg_get_function_identity_arguments(p.oid) "
                + "as identity_arguments"));
        assertTrue("BEFORE and AFTER catalog reports must expose exact signatures",
                countMatches(sql, "pg_get_function_identity_arguments\\(p\\.oid\\)")
                        >= 2);
        assertTrue(Pattern.compile("to_regprocedure\\s*\\(\\s*"
                + "'visual_screen\\.f_get_indexname\\(text\\)'\\s*\\)")
                .matcher(sql).find());
        assertTrue(Pattern.compile("to_regprocedure\\s*\\(\\s*"
                + "'visual_screen\\.f_get_indexname\\(character varying\\)'\\s*\\)")
                .matcher(sql).find());
        assertTrue(sql.contains("raise exception "
                + "'missing required index-name function signature: %'"));
    }

    private String loadResource(String resource) throws Exception {
        InputStream input = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resource);
        assertTrue("Mapper resource must exist: " + resource, input != null);
        try (InputStream stream = input;
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = stream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private String databaseContract() throws Exception {
        Path searchRoot = Paths.get(System.getProperty(
                "maven.multiModuleProjectDirectory", ".")).toAbsolutePath();
        for (Path directory = searchRoot;
             directory != null;
             directory = directory.getParent()) {
            Path candidate = directory.resolve("document/psql/vastbase/"
                    + "2026071501-index-library-scheme-production-contract.sql");
            if (Files.isRegularFile(candidate)) {
                return new String(Files.readAllBytes(candidate), StandardCharsets.UTF_8);
            }
        }
        throw new AssertionError("Database contract SQL not found from " + searchRoot);
    }

    private String statement(String xml, String element, String id) {
        Matcher matcher = Pattern.compile("<" + element + "\\s+id=\"" + id
                + "\"[\\s\\S]*?</" + element + ">", Pattern.CASE_INSENSITIVE)
                .matcher(xml);
        assertTrue("Missing " + element + " " + id, matcher.find());
        return matcher.group();
    }

    private List<String> commaSeparatedNames(String text) {
        List<String> names = new ArrayList<>();
        for (String value : text.split(",")) {
            names.add(value.replace("`", "").replace("\"", "").trim()
                    .toLowerCase(Locale.ROOT));
        }
        return names;
    }

    private List<String> parameterNames(String text) {
        Matcher matcher = Pattern.compile("#\\{params\\.([A-Za-z_]+)").matcher(text);
        List<String> names = new ArrayList<>();
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }

    private String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    private void assertWellFormedXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(
                "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setExpandEntityReferences(false);
        factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private String block(String sql, String delimiter) {
        int start = sql.indexOf("do " + delimiter);
        assertTrue("Missing SQL block " + delimiter, start >= 0);
        int end = sql.indexOf(delimiter + " language", start + delimiter.length());
        assertTrue("Unterminated SQL block " + delimiter, end > start);
        return sql.substring(start, end);
    }

    private int countMatches(String text, String regex) {
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
