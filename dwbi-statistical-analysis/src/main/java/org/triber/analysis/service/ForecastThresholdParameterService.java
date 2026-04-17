package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/10 13:46
 * @Description
 */
public interface ForecastThresholdParameterService {

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 获取监测器指标
     */
    List<Map<String, Object>> getMonitorIndexData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 获取监测器指标资源
     */
    List<Map<String, Object>> getIndexResourceData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/4 17:43
     * @Description 获取预测阈值参数个数
     */
    int getThresholdCount(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 获取预测阈值参数
     */
    List<Map<String, Object>> getThresholdData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 新增预测阈值参数
     */
    void insertThresholdData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 修改预测阈值参数
     */
    void updateThresholdData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/10 14:16
     * @Description 删除预测阈值参数
     */
    void deleteThresholdData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/16 11:04
     * @Description 新增参数维护指标预测数据
     */
    void insertForecastData(Map<String, Object> map);
}
