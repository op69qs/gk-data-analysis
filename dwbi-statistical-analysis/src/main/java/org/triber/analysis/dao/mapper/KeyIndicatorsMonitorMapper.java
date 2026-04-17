package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/17 15:11
 * @Description
 */
@Repository
public interface KeyIndicatorsMonitorMapper {

    /**
     * @Author haojiang
     * @Date 2020/9/27 16:40
     * @Description 获取重点指标
     */
    List<Map<String, Object>> getKeyIndicators(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/27 16:40
     * @Description 获取指标平台
     */
    List<Map<String, Object>> getIndexPlatform(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/2 14:48
     * @Description 查询数据库中的数据表
     */
    List<Map<String, Object>> getTableByDataBase(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/2 14:48
     * @Description 查询资源排名
     */
    List<Map<String, Object>> getResourceTop(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/28 10:04
     * @Description 获取重点指标监测视图数据
     */
    List<Map<String, Object>> getKeyIndicatorMonitorDada(@Param("params") Map<String, Object> map);
}
