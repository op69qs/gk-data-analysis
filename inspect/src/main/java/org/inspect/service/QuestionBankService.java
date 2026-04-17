package org.inspect.service;


import org.inspect.model.QueTreeNodeQue;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface QuestionBankService {

    List<Map<String,Object>> checkQuestionIDIsTop(PageData pd);

    List<Map<String,Object>> checkAddOtherDscr(PageData pd);

    List<Map<String,Object>> getQuestionBankPage(PageData pd);

    List<Map<String,Object>> getQuestionBankData(PageData pd);

    List<Map<String,Object>> getQuestionBankTree(PageData pd);

    Integer getQuestionBankCount(PageData pd);

    void addQuestionBank(PageData pd);

    void addQuestionRuleRelation(PageData pd);

    void delQuestionRuleRelation(PageData pd);

    void addQuestionType(PageData pd);

    void delQuestionType(PageData pd);

    void editQuestionBank(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);

    String getColumn (PageData pd);
//    List<QueTreeNodeQue> getQuestionBankTreeNew(PageData pd);
    List<Map<String,Object>> getQuestionBankTreeNewTree(PageData pd);
}
