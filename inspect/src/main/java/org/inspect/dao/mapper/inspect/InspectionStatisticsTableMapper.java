// InspectionStatisticsTableMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 被查国库统计表
 * @author Created by Samer on 2019/10/29.
 */
public interface InspectionStatisticsTableMapper {

    /**
     * 查询当前任务问题台账中的一级问题分类
     * @return
     */
    List<Map<String, Object>> getCurTaskQuestion_1(@Param(value = "params")PageData params);

    /**
     * 根据问题编码查询描述
     * @return
     */
    String getQuestionDscrById(@Param(value = "QUESTION_ID")String QUESTION_ID);

    /**
     * 获取被查国库统计表
     * @return
     */
    List<Map<String, Object>> getStatisticsTable(@Param(value = "params")PageData params);

    /**
     * 检查统计表同步过程调用
     * @return
     */
    void callStatisticsTableSYNC_TYPE(@Param(value = "params")PageData params);

}///:~
