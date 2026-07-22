package org.indicatorsLib.dao.mapper.indicatorsLib;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class IndexRelationMapperContractTest {

    @Test
    public void treeAndMetadataQueriesRejectMissingPhysicalIndicatorTables() throws Exception {
        String mapperXml = readResource("mybatis/indicatorsLib/IndexRelationMapper.xml");

        assertTrue(mapperXml.contains("to_regclass('indicators_lib.' || r.INDEX_CORRE_TABLE) IS NULL"));
        assertTrue(mapperXml.contains("to_regclass('indicators_lib.' || r.INDEX_CORRE_TABLE) IS NOT NULL"));
    }

    private String readResource(String path) throws Exception {
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalStateException("Missing test resource: " + path);
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
