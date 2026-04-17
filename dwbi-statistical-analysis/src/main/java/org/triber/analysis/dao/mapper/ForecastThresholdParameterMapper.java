package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/10 13:46
 * @Description
 */
@Repository
public interface ForecastThresholdParameterMapper {

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 获取监测器指标
     */
    List<Map<String, Object>> getMonitorIndexData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 获取监测器指标资源
     */
    List<Map<String, Object>> getIndexResourceData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/4 17:43
     * @Description 获取预测阈值参数个数
     */
    int getThresholdCount(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 获取预测阈值参数
     */
    List<Map<String, Object>> getThresholdData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 新增预测阈值参数
     */
    void insertThresholdData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 修改预测阈值参数
     */
    void updateThresholdData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 删除预测阈值参数
     */
    void deleteThresholdData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/16 11:04
     * @Description 新增参数维护指标预测数据
     */
    void insertForecastData(@Param("params") Map<String, Object> map);
}
