package org.jeecg.modules.reporting.service;

/** Observes a fully validated spool before the STG replacement transaction starts. */
@FunctionalInterface
public interface TimsPreparationAction {
    void afterPrepared(int fileCount, long rowCount);
}
