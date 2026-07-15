package org.jeecg.modules.enumSetting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

        List<String> arguments = splitArguments(task.get("shell_param"));
        StringBuilder sql = new StringBuilder("{call ").append(routineName).append('(');
        for (int index = 0; index < arguments.size(); index++) {
            if (index > 0) {
                sql.append(',');
            }
            sql.append('?');
        }
        sql.append(")}");
        return new ProcedureCall(sql.toString(), arguments);
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

    private List<String> splitArguments(Object rawParameters) {
        if (rawParameters == null || rawParameters.toString().trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] values = rawParameters.toString().split("@", -1);
        List<String> arguments = new ArrayList<>(values.length);
        Collections.addAll(arguments, values);
        return arguments;
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
