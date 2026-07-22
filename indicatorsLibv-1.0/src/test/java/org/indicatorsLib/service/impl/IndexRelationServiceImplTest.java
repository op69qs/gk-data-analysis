package org.indicatorsLib.service.impl;

import org.indicatorsLib.dao.mapper.indicatorsLib.IndexRelationMapper;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IndexRelationServiceImplTest {

    @Test
    public void getIndicatorsTableNormalizesFixedColumnsButPreservesIndicatorIds() throws Exception {
        IndexRelationMapper mapper = mock(IndexRelationMapper.class);
        IndexRelationServiceImpl service = new IndexRelationServiceImpl();
        java.lang.reflect.Field mapperField = IndexRelationServiceImpl.class.getDeclaredField("indexRelationMapper");
        mapperField.setAccessible(true);
        mapperField.set(service, mapper);

        String indicatorId = "18dbf0d2b0a211ea8bc1000c29587404";
        Map<String, Object> databaseRow = new HashMap<>();
        databaseRow.put(indicatorId, "54719364.27");
        databaseRow.put("account_date", "2016-01");
        databaseRow.put("account_period", "2016-01");
        databaseRow.put("code", "2234000000");
        databaseRow.put("gk", "国家金库万州区中心支库");
        when(mapper.getIndicatorsTable("query")).thenReturn(Collections.singletonList(databaseRow));

        List<Map<String, Object>> rows = service.getIndicatorsTable("query");
        Map<String, Object> row = rows.get(0);

        assertEquals("54719364.27", row.get(indicatorId));
        assertEquals("2016-01", row.get("ACCOUNT_DATE"));
        assertEquals("2016-01", row.get("ACCOUNT_PERIOD"));
        assertEquals("2234000000", row.get("CODE"));
        assertEquals("国家金库万州区中心支库", row.get("GK"));
        assertFalse(row.containsKey("account_period"));
        assertTrue(row.containsKey(indicatorId));
    }
}
