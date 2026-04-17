package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.QuestionRuleMapper;
import org.inspect.service.QuestionRuleService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionRuleServiceImpl implements QuestionRuleService {

    @Autowired
    private QuestionRuleMapper questionRuleMapper;

    @Override
    public List<Map<String, Object>> getQuestionRulePage(PageData pd) {
        return questionRuleMapper.getQuestionRulePage(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionRuleData(PageData pd) {
        return questionRuleMapper.getQuestionRuleData(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionRuleByRelation(PageData pd) {
        return questionRuleMapper.getQuestionRuleByRelation(pd);
    }

    @Override
    public Integer getQuestionRuleCount(PageData pd) {
        return questionRuleMapper.getQuestionRuleCount(pd);
    }

    @Override
    public void addQuestionRule(PageData pd) {
        questionRuleMapper.addQuestionRule(pd);
    }

    @Override
    public void editQuestionRule(PageData pd) {
        questionRuleMapper.editQuestionRule(pd);
    }

    @Override
    public void updateSort(PageData pd) {
        questionRuleMapper.updateSort(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return questionRuleMapper.checkRepeat(pd);
    }
}
