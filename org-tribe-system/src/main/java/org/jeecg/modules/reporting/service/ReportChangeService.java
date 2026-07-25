package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.ReportingBusinessMapper;
import org.jeecg.modules.reporting.vo.ReportChangeCommand;
import org.jeecg.modules.reporting.vo.ReportingBusinessQuery;
import org.jeecg.modules.reporting.vo.ReportingPageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
public class ReportChangeService {
    private final ReportingBusinessMapper mapper;

    public ReportChangeService(ReportingBusinessMapper mapper) {
        this.mapper = mapper;
    }

    public ReportingPageResult<Map<String, Object>> source(ReportingBusinessQuery query,
                                                           int pageNo, int pageSize) {
        prepare(query, pageNo, pageSize);
        String type = normalizeType(query.getType());
        if ("income".equals(type)) {
            return new ReportingPageResult<>(mapper.countIncome(query), mapper.queryIncome(query));
        }
        return new ReportingPageResult<>(mapper.countPayout(query), mapper.queryPayout(query));
    }

    public ReportingPageResult<Map<String, Object>> history(ReportingBusinessQuery query,
                                                            int pageNo, int pageSize) {
        prepare(query, pageNo, pageSize);
        if (query.getType() != null && !query.getType().trim().isEmpty()) {
            query.setType(normalizeType(query.getType()));
        }
        return new ReportingPageResult<>(mapper.countChanges(query), mapper.queryChanges(query));
    }

    @Transactional
    public void add(ReportChangeCommand command, String username) {
        validate(command);
        ReportingBusinessQuery query = new ReportingBusinessQuery();
        query.setType(command.getType());
        query.setBizDate(command.getAccountingDate());
        query.setTreCode(command.getTreasuryCode());
        query.setStatisticsCode(command.getStatisticsCode());
        query.setBudgetLevel(command.getBudgetLevel());
        BigDecimal original = "income".equals(command.getType())
                ? mapper.findIncomeAmount(query) : mapper.findPayoutAmount(query);
        if (original == null) {
            throw new IllegalArgumentException("没有找到对应的收入/支出基线明细，不能新增调整记录");
        }
        command.setOldAmount(original);
        command.setDifferenceAmount(command.getNewAmount().subtract(original));
        command.setUpdateDate(new Date());
        command.setUpdateUser(username);
        mapper.insertChange(command);
    }

    private void validate(ReportChangeCommand command) {
        if (command == null || command.getAccountingDate() == null
                || blank(command.getTreasuryCode()) || blank(command.getStatisticsCode())
                || blank(command.getBudgetLevel()) || command.getNewAmount() == null) {
            throw new IllegalArgumentException("账期、国库、统计代码、预算级次和新金额均不能为空");
        }
        command.setType(normalizeType(command.getType()));
    }

    private String normalizeType(String type) {
        String value = type == null ? "" : type.trim().toLowerCase(Locale.ROOT);
        if (!"income".equals(value) && !"payout".equals(value)) {
            throw new IllegalArgumentException("调整类型只能是 income 或 payout");
        }
        return value;
    }

    private void prepare(ReportingBusinessQuery query, int pageNo, int pageSize) {
        if (query == null) throw new IllegalArgumentException("查询条件不能为空");
        int size = Math.min(100, Math.max(1, pageSize));
        query.setLimit(size);
        query.setOffset((Math.max(1, pageNo) - 1) * size);
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
