package org.jeecg.modules.enumSetting.service.impl;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DynamicRefreshStoredProcedureRunnerTest {

    private final DynamicRefreshStoredProcedureRunner runner = new DynamicRefreshStoredProcedureRunner(null);

    @Test
    public void buildsBoundCallForProductionStyleRoutineName() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_name", "adm.P_ALL_CONTROL");
        task.put("shell_param", "202510@CQ");

        DynamicRefreshStoredProcedureRunner.ProcedureCall call = runner.buildCall(task);

        assertEquals("{call adm.P_ALL_CONTROL(?,?)}", call.getSql());
        assertEquals(Arrays.asList("202510", "CQ"), call.getArguments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsRoutineNameContainingSql() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_name", "adm.P_ALL_CONTROL; DROP TABLE adm.exec_shell_task");

        runner.buildCall(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsRoutineOutsideAdmSchema() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_name", "etl.run_task");

        runner.buildCall(task);
    }
}
