package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.ReportingBusinessMapper;
import org.jeecg.modules.reporting.vo.ReportChangeCommand;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportChangeServiceTest {
    @Test
    public void originalAmountAndDifferenceAreCalculatedOnServer() {
        ReportingBusinessMapper mapper = mock(ReportingBusinessMapper.class);
        when(mapper.findIncomeAmount(any())).thenReturn(new BigDecimal("100.25"));
        ReportChangeCommand command = new ReportChangeCommand();
        command.setType("income");
        command.setAccountingDate(new Date());
        command.setTreasuryCode("5000000000");
        command.setStatisticsCode("101");
        command.setBudgetLevel("1");
        command.setOldAmount(new BigDecimal("999999"));
        command.setDifferenceAmount(new BigDecimal("999999"));
        command.setNewAmount(new BigDecimal("125.30"));

        new ReportChangeService(mapper).add(command, "operator");

        ArgumentCaptor<ReportChangeCommand> saved = ArgumentCaptor.forClass(ReportChangeCommand.class);
        verify(mapper).insertChange(saved.capture());
        assertEquals(new BigDecimal("100.25"), saved.getValue().getOldAmount());
        assertEquals(new BigDecimal("25.05"), saved.getValue().getDifferenceAmount());
        assertEquals("operator", saved.getValue().getUpdateUser());
    }
}
