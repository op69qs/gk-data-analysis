package org.seo.dao.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComprehensiveQueryMapperContractTest {

    @Test
    public void metadataQueriesUseProductionEdwTable() throws Exception {
        String dataTableMapper = resource("mybatis/seo/DataTableMapper.xml");
        String auxiliaryMapper = resource("mybatis/seo/DataAuxiliaryMapper.xml");
        String queryMapper = resource("mybatis/seo/ComprehensiveQueryMapper.xml");

        assertTrue(dataTableMapper.contains("edw.fm_trs_guoku_base_table"));
        assertTrue(auxiliaryMapper.contains("edw.fm_trs_guoku_base_table"));
        assertTrue(queryMapper.contains("edw.fm_trs_guoku_base_table"));
        assertFalse(dataTableMapper.contains("seo.fm_trs_guoku_base_table"));
        assertFalse(auxiliaryMapper.contains("seo.fm_trs_guoku_base_table"));
        assertFalse(queryMapper.contains("seo.fm_trs_guoku_base_table"));
    }

    @Test
    public void treeParentAliasAndTextStateAreVastbaseSafe() throws Exception {
        String dataTableMapper = resource("mybatis/seo/DataTableMapper.xml");
        String queryMapper = resource("mybatis/seo/ComprehensiveQueryMapper.xml");

        assertTrue(dataTableMapper.contains("AS \"pId\""));
        assertFalse(dataTableMapper.contains(" AS pId"));
        assertTrue(queryMapper.contains("t.STATE = '0'"));
        assertFalse(queryMapper.contains("t.STATE = 0"));
        assertTrue(queryMapper.contains("JOIN \"jeecg-boot-os\".sys_depart d"));
        assertFalse(queryMapper.contains(") tree_data"));
    }

    @Test
    public void comprehensiveQueryTreeDoesNotCreateSelfParentNodes() throws Exception {
        String queryMapper = resource("mybatis/seo/ComprehensiveQueryMapper.xml");

        assertTrue(queryMapper.contains("t.SECOND_CLASSIFY &lt;&gt; t.FIRST_CLASSIFY"));
        assertTrue(queryMapper.contains("t.SECOND_CLASSIFY = t.FIRST_CLASSIFY"));
        assertTrue(queryMapper.contains("THEN t.FIRST_CLASSIFY"));
    }

    @Test
    public void dataTableTreeExposesStableNodeTypes() throws Exception {
        String dataTableMapper = resource("mybatis/seo/DataTableMapper.xml");

        assertTrue(dataTableMapper.contains("'source' AS \"nodeType\""));
        assertTrue(dataTableMapper.contains("'database' AS \"nodeType\""));
        assertTrue(dataTableMapper.contains("'table' AS \"nodeType\""));
    }

    private String resource(String path) throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IOException("Missing test resource: " + path);
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
