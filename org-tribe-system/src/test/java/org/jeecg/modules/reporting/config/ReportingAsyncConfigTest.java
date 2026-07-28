package org.jeecg.modules.reporting.config;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.Assert.assertEquals;

public class ReportingAsyncConfigTest {
    @Test
    public void reportingExecutorUsesOneWorkerOnSharedServer() {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) new ReportingAsyncConfig().reportingTaskExecutor();

        assertEquals(1, executor.getCorePoolSize());
        assertEquals(1, executor.getMaxPoolSize());
        executor.shutdown();
    }
}
