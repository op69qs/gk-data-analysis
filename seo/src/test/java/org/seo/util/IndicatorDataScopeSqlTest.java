package org.seo.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IndicatorDataScopeSqlTest {

    @Test
    public void portalVisSchemeUsesCurrentSubjectInsteadOfCreator() {
        String sql = IndicatorDataScopeSql.apply(
                "SELECT V.* FROM data V", "1", "BOOK-001", null);

        assertTrue(sql.contains("d.bookorgcode = 'BOOK-001'"));
        assertTrue(sql.contains("SELECT guoku_id FROM authorized_guoku"));
        assertFalse(sql.contains("sys_user"));
    }

    @Test
    public void localVisSchemeUsesCurrentGuoku() {
        String sql = IndicatorDataScopeSql.apply(
                "SELECT V.* FROM data V", "2", null, "GK-001");

        assertTrue(sql.contains("d.guoku_id = 'GK-001'"));
        assertTrue(sql.contains("SELECT area_no_id FROM authorized_guoku"));
    }
}
