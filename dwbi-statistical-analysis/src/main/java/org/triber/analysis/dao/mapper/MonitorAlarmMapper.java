package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/16 16:41
 * @Description
 */
@Repository
public interface MonitorAlarmMapper {

    /**
     * @Author haojiang
     * @Date 2020/9/26 14:20
     * @Description 查询监测指标
     */
    List<Map<String, Object>> getAlarmIndexData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/26 14:20
     * @Description 查询监测器平台
     */
    List<Map<String, Object>> getAlarmPlatformData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/26 14:20
     * @Description 查询监测器设备资源
     */
    List<Map<String, Object>> getAlarmResourceData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/7 16:39
     * @Description 查询监测器告警数据
     */
    List<Map<String, Object>> getAlarmAnalysisData(@Param("params") Map<String, Object> map);
}
