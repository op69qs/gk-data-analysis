package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface QuestionRuleService {

    List<Map<String,Object>> getQuestionRulePage(PageData pd);

    List<Map<String,Object>> getQuestionRuleData(PageData pd);

    List<Map<String,Object>> getQuestionRuleByRelation(PageData pd);

    Integer getQuestionRuleCount(PageData pd);

    void addQuestionRule(PageData pd);

    void editQuestionRule(PageData pd);

    void updateSort(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);
}
