package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.QuestionBankMapper;
import org.inspect.model.QueTreeNodeQue;
import org.inspect.service.QuestionBankService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Override
    public List<Map<String, Object>> checkQuestionIDIsTop(PageData pd) {
        return questionBankMapper.checkQuestionIDIsTop(pd);
    }

    @Override
    public List<Map<String, Object>> checkAddOtherDscr(PageData pd) {
        return questionBankMapper.checkAddOtherDscr(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionBankPage(PageData pd) {
        return questionBankMapper.getQuestionBankPage(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionBankData(PageData pd) {
        return questionBankMapper.getQuestionBankData(pd);
    }

    @Override
    public List<Map<String, Object>> getQuestionBankTree(PageData pd) {
        return questionBankMapper.getQuestionBankTree(pd);
    }

    @Override
    public Integer getQuestionBankCount(PageData pd) {
        return questionBankMapper.getQuestionBankCount(pd);
    }

    @Override
    public void addQuestionBank(PageData pd) {
        questionBankMapper.addQuestionBank(pd);
    }

    @Override
    public void addQuestionRuleRelation(PageData pd) {
        questionBankMapper.addQuestionRuleRelation(pd);
    }

    @Override
    public void delQuestionRuleRelation(PageData pd) {
        questionBankMapper.delQuestionRuleRelation(pd);
    }

    @Override
    public void addQuestionType(PageData pd) {
        questionBankMapper.addQuestionType(pd);
    }

    @Override
    public void delQuestionType(PageData pd) {
        questionBankMapper.delQuestionType(pd);
    }

    @Override
    public void editQuestionBank(PageData pd) {
        questionBankMapper.editQuestionBank(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return questionBankMapper.checkRepeat(pd);
    }

    @Override
    public String getColumn(PageData pd) {
        return questionBankMapper.getColumn(pd);
    }

//    @Override
//    public List<QueTreeNodeQue> getQuestionBankTreeNew(PageData pd) {
//        return questionBankMapper.getQuestionBankTreeNew(pd);
//    }
    @Override
    public List<Map<String, Object>> getQuestionBankTreeNewTree(PageData pd) {
        return questionBankMapper.getQuestionBankTreeNewTree(pd);
    }
}
