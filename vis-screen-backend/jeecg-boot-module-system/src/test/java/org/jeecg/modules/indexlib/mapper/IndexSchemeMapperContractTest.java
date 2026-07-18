package org.jeecg.modules.indexlib.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IndexSchemeMapperContractTest {
    private String mapperXml;
    private String relationMapperXml;

    @Before
    public void loadMappers() throws Exception {
        mapperXml = load("org/jeecg/modules/indexlib/mapper/xml/IndexSchemeMapper.xml");
        relationMapperXml = load(
                "org/jeecg/modules/indexlib/mapper/xml/IndexRelationMapper.xml");
    }

    @Test
    public void mapperInterfacesMatchXmlStatementIds() {
        assertEquals(methodNames(IndexSchemeMapper.class), statementIds(mapperXml));
        assertEquals(methodNames(IndexRelationMapper.class), statementIds(relationMapperXml));
    }

    @Test
    public void descriptionAggregationsDeduplicateBeforeOrdering() {
        assertDescriptionAggregation(
                statement(mapperXml, "select", "getAllTrsInfo"),
                "guoku_dscr", "guoku_id");
        assertDescriptionAggregation(
                statement(mapperXml, "select", "getAllAreaInfo"),
                "area_dscr", "area_no_id");
    }

    @Test
    public void schemeDetailsExposeProductionChartKeys() {
        String details = statement(mapperXml, "select", "getSchemeInfoById");
        assertAliases(details, "SCHEME_DESCR", "SCHEME_COLUMS",
                "SCHEME_SQL", "SCHEME_CONDITON");
        assertTrue(details.contains("#{params.scheme_id}"));
    }

    @Test
    public void schemeCountAndListShareFiltersAndListUsesBoundPagination() {
        String count = statement(mapperXml, "select", "getSchemeCount");
        String list = statement(mapperXml, "select", "selectSchemeTable");
        Set<String> expectedFilters = new HashSet<>(
                Arrays.asList("schemeDescr", "begin_time", "end_time"));

        assertEquals(expectedFilters, parameters(count));
        Set<String> listParameters = parameters(list);
        assertTrue(listParameters.remove("pageSize"));
        assertTrue(listParameters.remove("page"));
        assertEquals(expectedFilters, listParameters);

        String normalizedList = normalize(list);
        assertTrue(normalizedList.contains(
                "limit #{params.pagesize,jdbctype=integer} "
                        + "offset #{params.page,jdbctype=integer}"));
        assertAliases(list, "SCHEME_DESCR", "ADD_DATE");
        assertTrue(list.contains("AS \"realname\""));
        assertTrue(list.contains("visual_screen.f_get_IndexName"));
    }

    @Test
    public void schemeDeleteUsesProductionParameter() {
        assertTrue(statement(mapperXml, "delete", "deleteSchemeById")
                .contains("#{params.schemeId}"));
    }

    @Test
    public void relationQueriesExposeProductionChartKeys() {
        String batch = statement(relationMapperXml, "select", "getBatchIndexInfo");
        assertTrue(batch.contains("indicators_lib.lib_index_relation"));
        assertTrue(batch.contains("#{params.SCHEME_COLUMS}"));

        String details = statement(relationMapperXml, "select", "getIndexDetails");
        assertTrue(details.contains("indicators_lib.lib_index_relation"));
        assertTrue(details.contains("#{params.INDEX_ID}"));
        assertAliases(details, "INDEX_NAME", "INDEX_TYPE", "INDEX_CORRE_TABLE");
    }

    private String load(String resource) throws Exception {
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

    private String statement(String xml, String element, String id) {
        Matcher matcher = Pattern.compile("<" + element + "\\s+id=\\\"" + Pattern.quote(id)
                + "\\\"[\\s\\S]*?</" + element + ">", Pattern.CASE_INSENSITIVE)
                .matcher(xml);
        assertTrue("Missing " + element + " " + id, matcher.find());
        return matcher.group();
    }

    private Set<String> methodNames(Class<?> mapperInterface) {
        return Arrays.stream(mapperInterface.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    private Set<String> statementIds(String xml) {
        Matcher matcher = Pattern.compile(
                "<(?:select|insert|update|delete)\\b[^>]*\\bid=\"([^\"]+)\"",
                Pattern.CASE_INSENSITIVE).matcher(xml);
        Set<String> ids = new HashSet<>();
        while (matcher.find()) {
            ids.add(matcher.group(1));
        }
        return ids;
    }

    private Set<String> parameters(String sql) {
        Matcher matcher = Pattern.compile("#\\{params\\.([A-Za-z_]+)").matcher(sql);
        Set<String> parameters = new HashSet<>();
        while (matcher.find()) {
            parameters.add(matcher.group(1));
        }
        return parameters;
    }

    private void assertDescriptionAggregation(String sql, String description, String id) {
        String normalized = normalize(sql);
        assertTrue(normalized.contains(
                "string_agg(d." + description + ", ',' order by d.sort_id)"));
        assertTrue(normalized.contains(
                "min(a." + id + ") as sort_id"));
        assertTrue(normalized.contains("group by a." + description));
    }

    private void assertAliases(String sql, String... aliases) {
        for (String alias : aliases) {
            assertTrue("Missing quoted production alias " + alias,
                    sql.contains("AS \"" + alias + "\""));
        }
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim().toLowerCase();
    }
}
