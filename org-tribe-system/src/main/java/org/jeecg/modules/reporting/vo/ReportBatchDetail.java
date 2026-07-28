package org.jeecg.modules.reporting.vo;

import lombok.Data;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportParseError;
import org.jeecg.modules.reporting.entity.ReportProcessCall;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportTaskLog;

import java.util.List;

@Data
public class ReportBatchDetail {
    private ReportBatch batch;
    private List<ReportFile> files;
    private List<ReportTask> tasks;
    private List<ReportTaskLog> timeline;
    private List<ReportParseError> parseErrors;
    private List<ReportProcessCall> processCalls;
}
