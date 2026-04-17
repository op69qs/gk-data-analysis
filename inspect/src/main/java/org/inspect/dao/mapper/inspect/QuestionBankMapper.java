package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.model.QueTreeNodeQue;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QuestionBankMapper {

    List<Map<String,Object>> checkQuestionIDIsTop(@Param("params") PageData pd);

    List<Map<String,Object>> checkAddOtherDscr(@Param("params") PageData pd);

    List<Map<String,Object>> getQuestionBankPage(@Param("params") PageData pd);

    List<Map<String,Object>> getQuestionBankData(@Param("params") PageData pd);

    List<Map<String,Object>> getQuestionBankTree(@Param("params") PageData pd);

    Integer getQuestionBankCount(@Param("params") PageData pd);

    void addQuestionBank(@Param("params") PageData pd);

    void addQuestionRuleRelation(@Param("params") PageData pd);

    void delQuestionRuleRelation(@Param("params") PageData pd);

    void addQuestionType(@Param("params") PageData pd);

    void delQuestionType(@Param("params") PageData pd);

    void editQuestionBank(@Param("params") PageData pd);

    List<Map<String,Object>> checkRepeat(@Param("params") PageData pd);

    String getColumn (@Param("params") PageData pd);
//    List<QueTreeNodeQue> getQuestionBankTreeNew(@Param("params") PageData pd);
    List<Map<String,Object>> getQuestionBankTreeNewTree(@Param("params") PageData pd);

}
