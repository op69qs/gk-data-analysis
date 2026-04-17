package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QuestionRuleMapper {

    List<Map<String,Object>> getQuestionRulePage(@Param("params")PageData pd);

    List<Map<String,Object>> getQuestionRuleData(@Param("params")PageData pd);

    List<Map<String,Object>> getQuestionRuleByRelation(@Param("params")PageData pd);

    Integer getQuestionRuleCount(@Param("params")PageData pd);

    void addQuestionRule(@Param("params")PageData pd);

    void editQuestionRule(@Param("params")PageData pd);

    void updateSort(@Param("params")PageData pd);

    List<Map<String,Object>> checkRepeat(@Param("params")PageData pd);

}
