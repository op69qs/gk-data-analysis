package org.jeecg.modules.visualScreen.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
    public void databaseContractFailsClosedAndNeverCreatesBusinessTables()
            throws Exception {
        String sql = databaseContract().toLowerCase(Locale.ROOT);

        assertTrue(sql.contains("before"));
        assertTrue(sql.contains("after"));
        assertTrue(sql.contains("information_schema.tables"));
        assertTrue(sql.contains("information_schema.columns"));
        assertTrue(sql.contains("information_schema.routines"));
        assertTrue(sql.contains("raise exception 'missing required production table: %'"));
        assertFalse(sql.matches("(?s).*create\\s+table\\b.*"));
        assertFalse(sql.matches("(?s).*(drop|truncate|delete\\s+from)\\s+"
                + "(visual_screen|indicators_lib)\\..*"));

        for (String table : Arrays.asList(
                "'visual_screen', 'vs_lib_index_scheme'",
                "'visual_screen', 'vs_gallery_info'",
                "'visual_screen', 'sys_user'",
                "'indicators_lib', 'lib_index_relation'")) {
            assertTrue("Missing production table gate: " + table, sql.contains(table));
        }

        int gate = sql.indexOf("raise exception 'missing required production table: %'");
        int firstAlter = sql.indexOf("alter table visual_screen.vs_gallery_info");
        assertTrue("Required-table gate must precede all schema changes",
                gate >= 0 && firstAlter > gate);
    }

    @Test
    public void databaseContractOnlyAddsMissingProductionGalleryColumnsAsText()
            throws Exception {
        String sql = normalize(databaseContract());

        for (String column : Arrays.asList(
                "dacct_radio", "title_old", "add_time", "add_user",
                "index_scheme_id", "index_scheme_name", "condition")) {
            assertTrue("Missing idempotent production gallery column: " + column,
                    sql.contains("execute 'alter table visual_screen.vs_gallery_info "
                            + "add column " + column + " text'"));
            assertTrue("Missing information_schema guard for gallery column: " + column,
                    sql.contains("actual.column_name = '" + column + "'"));
        }
        assertFalse(sql.matches("(?s).*alter\\s+column.*"));
        assertFalse(sql.matches("(?s).*drop\\s+column.*"));
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
}
