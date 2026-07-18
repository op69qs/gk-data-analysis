package org.jeecg.modules.indexlib.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void schemeListUsesProductionTableFieldsAndFilters() {
        String sql = statement(mapperXml, "select", "selectSchemeTable");
        assertTrue(sql.contains("visual_screen.vs_lib_index_scheme"));
        assertTrue(sql.contains("AS \"SCHEME_DESCR\""));
        assertTrue(sql.contains("AS \"ADD_DATE\""));
        assertTrue(sql.contains("AS \"realname\""));
        assertTrue(sql.contains("visual_screen.f_get_IndexName"));
        assertTrue(sql.contains("#{params.begin_time}"));
        assertTrue(sql.contains("#{params.end_time}"));
    }

    @Test
    public void schemeDeleteUsesProductionParameter() {
        assertTrue(statement(mapperXml, "delete", "deleteSchemeById")
                .contains("#{params.schemeId}"));
    }

    @Test
    public void relationQueriesUseProductionTableAndKeys() {
        String batch = statement(relationMapperXml, "select", "getBatchIndexInfo");
        assertTrue(batch.contains("indicators_lib.lib_index_relation"));
        assertTrue(batch.contains("#{params.SCHEME_COLUMS}"));

        String details = statement(relationMapperXml, "select", "getIndexDetails");
        assertTrue(details.contains("indicators_lib.lib_index_relation"));
        assertTrue(details.contains("#{params.INDEX_ID}"));
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
}
