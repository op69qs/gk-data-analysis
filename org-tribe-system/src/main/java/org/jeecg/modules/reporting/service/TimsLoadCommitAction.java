package org.jeecg.modules.reporting.service;

@FunctionalInterface
public interface TimsLoadCommitAction {
    void afterCommittedRowsLoaded(long committedRows);
}
