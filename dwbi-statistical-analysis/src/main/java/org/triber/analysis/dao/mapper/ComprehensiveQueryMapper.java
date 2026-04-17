package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:48
 * @Description 综合查询
 */
@Repository
public interface ComprehensiveQueryMapper {

    /**
     * @Author haojiang
     * @Date 2020/11/25 10:58
     * @Description 查询综合查询指标平台
     */
    List<Map<String,Object>> getPlatformData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/11 11:37
     * @Description 获取运维综合查询指标下拉选(不查监测器)
     */
    List<Map<String,Object>> getDevOpsIndicatorsData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/11 11:37
     * @Description 获取网络综合查询指标下拉选(不查监测器)
     */
    List<Map<String,Object>> getNetworkIndicatorsData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/11 11:37
     * @Description 获取运维综合查询资源下拉选
     */
    List<Map<String,Object>> getDevOpsResourceData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/4 15:42
     * @Description 网络综合查询资源下拉选
     */
    List<Map<String, Object>> getNetworkResourceData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/12 10:10
     * @Description 获取预测阈值参数
     */
    List<Map<String, Object>> getComprehensiveQueryData(@Param("params") Map<String, Object> map);
}
