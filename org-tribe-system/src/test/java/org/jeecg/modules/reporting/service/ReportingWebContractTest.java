package org.jeecg.modules.reporting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.reporting.entity.AgentTreasuryConfig;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.vo.ReportChangeCommand;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportingWebContractTest {

    @Test
    public void acceptsDateOnlyValuesPostedByVuePages() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AgentTreasuryConfig treasury = mapper.readValue(
                "{\"treCode\":\"2200\",\"treName\":\"代理库\",\"startDate\":\"2026-07-01\",\"endDate\":\"2026-07-31\",\"state\":\"0\"}",
                AgentTreasuryConfig.class);
        ReportChangeCommand change = mapper.readValue(
                "{\"accountingDate\":\"2026-07-31\",\"treasuryCode\":\"2200\",\"statisticsCode\":\"101\",\"budgetLevel\":\"1\",\"type\":\"income\",\"newAmount\":1}",
                ReportChangeCommand.class);

        assertEquals("2026-07-01", new java.sql.Date(treasury.getStartDate().getTime()).toString());
        assertEquals("2026-07-31", new java.sql.Date(change.getAccountingDate().getTime()).toString());
    }

    @Test
    public void processCheckAndRegistrationAreSerializedInsideOneServiceInstance() throws Exception {
        Method method = ReportProcessCallService.class.getMethod("callForBatch",
                ReportBatch.class, String.class, String.class, String.class);
        assertTrue(Modifier.isSynchronized(method.getModifiers()));
    }
}
