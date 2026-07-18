package org.jeecg.modules.enumSetting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class DynamicRefreshStoredProcedureRunner {

    private static final Pattern ROUTINE_NAME = Pattern.compile(
            "adm\\.[A-Za-z_][A-Za-z0-9_]*", Pattern.CASE_INSENSITIVE
    );

    private final DataSource dataSource;
    private final int timeoutSeconds;

    public DynamicRefreshStoredProcedureRunner(DataSource dataSource) {
        this(dataSource, 3600);
    }

    @Autowired
    public DynamicRefreshStoredProcedureRunner(
            DataSource dataSource,
            @Value("${dynamic-refresh.procedure-timeout-seconds:3600}") int timeoutSeconds) {
        this.dataSource = dataSource;
        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("存储过程超时时间必须大于0秒");
        }
        this.timeoutSeconds = timeoutSeconds;
    }

    ProcedureCall buildCall(Map<String, Object> task) {
        Object rawName = task.get("shell_name");
        String routineName = rawName == null ? "" : rawName.toString().trim();
        if (!ROUTINE_NAME.matcher(routineName).matches()) {
            throw new IllegalArgumentException("存储过程名称不合法");
        }

        String taskId = required(task, "id", "任务编号不能为空");
        Object rawParameter = task.get("shell_param");
        String parameter = rawParameter == null ? "" : rawParameter.toString();
        return new ProcedureCall(
                "{call " + routineName + "(?,?)}",
                Arrays.asList(parameter, taskId)
        );
    }

    public void run(Map<String, Object> task) throws SQLException {
        ProcedureCall call = buildCall(task);
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall(call.getSql())) {
            statement.setQueryTimeout(timeoutSeconds);
            for (int index = 0; index < call.getArguments().size(); index++) {
                statement.setString(index + 1, call.getArguments().get(index));
            }
            statement.execute();
        }
    }

    private String required(Map<String, Object> task, String key, String message) {
        Object rawValue = task.get(key);
        String value = rawValue == null ? "" : rawValue.toString().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    static class ProcedureCall {
        private final String sql;
        private final List<String> arguments;

        ProcedureCall(String sql, List<String> arguments) {
            this.sql = sql;
            this.arguments = arguments;
        }

        String getSql() {
            return sql;
        }

        List<String> getArguments() {
            return arguments;
        }
    }
}
