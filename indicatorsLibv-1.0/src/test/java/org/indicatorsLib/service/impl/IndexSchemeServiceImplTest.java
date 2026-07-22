package org.indicatorsLib.service.impl;

import org.indicatorsLib.dao.mapper.indicatorsLib.IndexSchemeMapper;
import org.indicatorsLib.util.PageData;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexSchemeServiceImplTest {

    private IndexSchemeMapper mapper;
    private IndexSchemeServiceImpl service;

    @Before
    public void setUp() throws Exception {
        mapper = mock(IndexSchemeMapper.class);
        service = new IndexSchemeServiceImpl();
        java.lang.reflect.Field mapperField = IndexSchemeServiceImpl.class.getDeclaredField("indexSchemeMapper");
        mapperField.setAccessible(true);
        mapperField.set(service, mapper);
    }

    @Test
    public void saveRejectsLegacyMysqlSqlBeforeWriting() {
        Map<String, Object> scheme = new HashMap<>();
        scheme.put("schemeSql", "SELECT IFNULL (SUM (IF (aa.COLID = 'I1', VALUE, NULL)), 0) FROM t");

        assertRejected(() -> service.saveIndexScheme(scheme));

        verify(mapper, never()).saveIndexScheme(scheme);
    }

    @Test
    public void saveAcceptsVastbaseSqlWithUppercaseAliases() {
        Map<String, Object> scheme = new HashMap<>();
        scheme.put("schemeSql", validSchemeSql());

        service.saveIndexScheme(scheme);

        verify(mapper).saveIndexScheme(scheme);
    }

    @Test
    public void updateRejectsSqlWithoutQuotedUppercaseAliases() {
        Map<String, Object> scheme = new HashMap<>();
        scheme.put("schemeSql", "SELECT ACCOUNT_DATE,ACCOUNT_PERIOD,CODE,GK FROM t");

        assertRejected(() -> service.updateSchemeData(scheme));

        verify(mapper, never()).updateSchemeData(scheme);
    }

    @Test
    public void updateWithoutSqlStillUpdatesOtherSchemeFields() {
        Map<String, Object> scheme = new HashMap<>();
        scheme.put("schemeId", "scheme-1");
        scheme.put("IS_PUSH", "0");

        service.updateSchemeData(scheme);

        verify(mapper).updateSchemeData(scheme);
    }

    @Test
    public void pushRejectsLegacySqlAlreadyStoredInSourceScheme() {
        PageData pageData = new PageData();
        pageData.put("ID", "scheme-1");
        when(mapper.selectSchemeSQL("scheme-1"))
                .thenReturn("SELECT * FROM indicators_lib.`legacy_table`");

        assertRejected(() -> service.pushIndexToVS(pageData));

        verify(mapper, never()).pushIndexToVS(pageData);
    }

    @Test
    public void pushAcceptsVastbaseSqlAndCopiesToVis() {
        PageData pageData = new PageData();
        pageData.put("ID", "scheme-1");
        when(mapper.selectSchemeSQL("scheme-1")).thenReturn(validSchemeSql());

        service.pushIndexToVS(pageData);

        verify(mapper).pushIndexToVS(pageData);
    }

    private String validSchemeSql() {
        return "SELECT ACCOUNT_DATE AS \"ACCOUNT_DATE\","
                + "ACCOUNT_PERIOD AS \"ACCOUNT_PERIOD\","
                + "CODE AS \"CODE\",GK AS \"GK\" FROM t";
    }

    private void assertRejected(Runnable action) {
        try {
            action.run();
            fail("Expected legacy SQL to be rejected");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}
