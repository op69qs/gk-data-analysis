package org.indicatorsLib.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IndicatorDataScopeSqlTest {

    @Test
    public void portalUserUsesSubjectCodeBookorgTree() {
        IndicatorRequestContext context = IndicatorRequestContext.portal("portal-user", "BOOK-001");

        String sql = IndicatorDataScopeSql.apply("SELECT V.* FROM data V", "1", context);

        assertTrue(sql.contains("d.bookorgcode = 'BOOK-001'"));
        assertTrue(sql.contains("SELECT guoku_id FROM authorized_guoku"));
        assertFalse(sql.contains("sys_user"));
        assertFalse(sql.contains("portal-user"));
    }

    @Test
    public void localUserUsesGuokuRootWithoutUsingUserId() {
        IndicatorRequestContext context = IndicatorRequestContext.local("local-user", "GK-001");

        String sql = IndicatorDataScopeSql.apply("SELECT V.* FROM data V", "2", context);

        assertTrue(sql.contains("d.guoku_id = 'GK-001'"));
        assertTrue(sql.contains("SELECT area_no_id FROM authorized_guoku"));
        assertFalse(sql.contains("local-user"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingTreasuryContextFailsClosed() {
        IndicatorDataScopeSql.apply(
                "SELECT V.* FROM data V",
                "1",
                new IndicatorRequestContext("user", null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedDimensionFailsClosed() {
        IndicatorDataScopeSql.apply(
                "SELECT V.* FROM data V",
                "3",
                IndicatorRequestContext.local("user", "GK-001"));
    }
}
