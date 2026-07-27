package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.mapper.ReportRuntimeLockMapper;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportRuntimeLockServiceTest {

    @Test
    public void onlyOneOwnerCanAcquireAndCurrentOwnerCanRelease() {
        ReportRuntimeLockMapper mapper = mock(ReportRuntimeLockMapper.class);
        when(mapper.acquire(eq("TIMS_LOAD"), eq("owner-1"), any(), any())).thenReturn(1);
        when(mapper.acquire(eq("TIMS_LOAD"), eq("owner-2"), any(), any())).thenReturn(0);
        ReportRuntimeLockService service = new ReportRuntimeLockService(mapper, properties());

        assertTrue(service.acquireTims("owner-1"));
        assertFalse(service.acquireTims("owner-2"));
        service.releaseTims("owner-1");

        verify(mapper).release("TIMS_LOAD", "owner-1");
    }

    @Test(expected = IllegalStateException.class)
    public void transactionCannotContinueAfterLeaseOwnershipChanges() {
        ReportRuntimeLockMapper mapper = mock(ReportRuntimeLockMapper.class);
        when(mapper.lockOwnerForUpdate("TIMS_LOAD")).thenReturn("owner-2");

        new ReportRuntimeLockService(mapper, properties()).assertOwnedForUpdate("owner-1");
    }

    private ReportingProperties properties() {
        ReportingProperties properties = new ReportingProperties();
        properties.setTimsLockLeaseMinutes(60);
        return properties;
    }
}
