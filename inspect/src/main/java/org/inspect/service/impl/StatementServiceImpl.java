package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.StatementMapper;
import org.inspect.service.StatementService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatementServiceImpl implements StatementService {

    @Autowired
    private StatementMapper statementMapper;


    @Override
    public List<Map<String,Object>> getStatementData(PageData pd) {
        return statementMapper.getStatementData(pd);
    }

    @Override
    public void addStatement(PageData pd) {
        statementMapper.addStatement(pd);
    }

    @Override
    public void editStatement(PageData pd) {
        statementMapper.editStatement(pd);
    }

    @Override
    public void delStatement(PageData pd) {
        statementMapper.delStatement(pd);
    }
}
