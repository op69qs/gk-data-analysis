package org.jeecg.modules.reporting.event;

public class ReportBatchExecutionRequested {
    private final String taskId;
    private final String batchId;
    private final String requestedTaskType;
    private final String userId;
    private final String username;

    public ReportBatchExecutionRequested(String taskId, String batchId, String requestedTaskType,
                                         String userId, String username) {
        this.taskId = taskId;
        this.batchId = batchId;
        this.requestedTaskType = requestedTaskType;
        this.userId = userId;
        this.username = username;
    }

    public String getTaskId() { return taskId; }
    public String getBatchId() { return batchId; }
    public String getRequestedTaskType() { return requestedTaskType; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
}
