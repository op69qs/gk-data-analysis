package org.jeecg.modules.reporting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.reporting.entity.AgentTreasuryConfig;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReportingWebContractTest {

    @Test
    public void acceptsDateOnlyValuesPostedByVuePages() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AgentTreasuryConfig treasury = mapper.readValue(
                "{\"treCode\":\"2200\",\"treName\":\"代理库\",\"startDate\":\"2026-07-01\",\"endDate\":\"2026-07-31\",\"state\":\"0\"}",
                AgentTreasuryConfig.class);
        assertEquals("2026-07-01", new java.sql.Date(treasury.getStartDate().getTime()).toString());
    }

    @Test
    public void unverifiedEdwChangeControllerIsNotExposed() {
        assertFalse(classExists("org.jeecg.modules.reporting.controller.ReportChangeRecordController"));
    }

    @Test
    public void processCheckAndRegistrationAreSerializedInsideOneServiceInstance() throws Exception {
        Method method = ReportProcessCallService.class.getMethod("callForBatch",
                ReportBatch.class, String.class, String.class, String.class);
        assertTrue(Modifier.isSynchronized(method.getModifiers()));
    }

    private boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException expected) {
            return false;
        }
    }
}
