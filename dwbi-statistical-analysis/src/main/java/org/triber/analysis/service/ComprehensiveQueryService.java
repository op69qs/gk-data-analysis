package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:45
 * @Description 综合查询
 */
public interface ComprehensiveQueryService {

    /**
     * @Author haojiang
     * @Date 2020/11/25 10:58
     * @Description 查询综合查询指标平台
     */
    List<Map<String, Object>> getPlatformData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/11 11:37
     * @Description 获取运维综合查询指标下拉选(不查监测器)
     */
    List<Map<String, Object>> getDevOpsIndicatorsData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/11 11:37
     * @Description 获取网络综合查询指标下拉选
     */
    List<Map<String, Object>> getNetworkIndicatorsData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/11 11:37
     * @Description 获取运维综合查询资源下拉选
     */
    List<Map<String, Object>> getDevOpsResourceData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/11/4 15:42
     * @Description 网络综合查询资源下拉选
     */
    List<Map<String, Object>> getNetworkResourceData(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/12 09:45
     * @Description 获取综合查询数据
     */
    List<Map<String, Object>> getComprehensiveQueryData(Map<String, Object> map);
}
