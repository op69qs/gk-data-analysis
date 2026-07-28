package org.jeecg.modules.reporting.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Shared lifecycle used by reporting batches, tasks and process calls.
 * Retries create a new task, so a completed task never moves back to QUEUED.
 */
public enum ReportStatus {
    QUEUED,
    PROCESSING,
    SUCCEEDED,
    PARTIALLY_SUCCEEDED,
    FAILED,
    CANCELLED,
    LOGICALLY_DELETED;

    public boolean canTransitionTo(ReportStatus target) {
        return target != null && allowedTargets().contains(target);
    }

    public void requireTransitionTo(ReportStatus target) {
        if (!canTransitionTo(target)) {
            throw new IllegalStateException("Illegal reporting status transition: " + this + " -> " + target);
        }
    }

    private Set<ReportStatus> allowedTargets() {
        switch (this) {
            case QUEUED:
                return EnumSet.of(PROCESSING, FAILED, CANCELLED);
            case PROCESSING:
                return EnumSet.of(SUCCEEDED, PARTIALLY_SUCCEEDED, FAILED, CANCELLED);
            case SUCCEEDED:
            case PARTIALLY_SUCCEEDED:
            case FAILED:
            case CANCELLED:
                return EnumSet.of(LOGICALLY_DELETED);
            case LOGICALLY_DELETED:
            default:
                return Collections.emptySet();
        }
    }
}
