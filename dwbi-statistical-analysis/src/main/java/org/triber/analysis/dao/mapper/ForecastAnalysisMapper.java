package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/14 17:55
 * @Description
 */
@Repository
public interface ForecastAnalysisMapper {

    /**
     * @Author haojiang
     * @Date 2020/9/14 18:01
     * @Description 获取指标
     */
    List<Map<String, Object>> getIndexData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/27 15:37
     * @Description 获取资源
     */
    List<Map<String, Object>> getResourceData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/14 18:01
     * @Description 获取预测分析数据
     */
    List<Map<String, Object>> getAnalysisData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/16 10:24
     * @Description 获取历史分析数据
     */
    List<Map<String, Object>> getHistoryAnalysisDada(@Param("params") Map<String, Object> map);
}
