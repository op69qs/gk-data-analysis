package org.jeecg.modules.reporting.entity;

import org.jeecg.modules.reporting.enums.ReportStatus;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReportStatusTest {

    @Test
    public void processingCanTransitionToSucceeded() {
        assertTrue(ReportStatus.PROCESSING.canTransitionTo(ReportStatus.SUCCEEDED));
    }

    @Test
    public void succeededCannotReturnToQueued() {
        assertFalse(ReportStatus.SUCCEEDED.canTransitionTo(ReportStatus.QUEUED));
    }
}
