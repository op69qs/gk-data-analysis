package org.indicatorsLib.service;

import org.indicatorsLib.util.PageData;

import java.util.List;
import java.util.Map;

public interface IndexRelationService {

    /**
     * 根据筛选条件查询指标信息
     *
     * @param pd
     * @return
     */
    List<Map<String, Object>> selectIndexRelationInfo(PageData pd);

    /**
     * 获取指标详细描述
     *
     * @param pd
     * @return
     */
    Map<String, Object> getIndexDetails(PageData pd);

    /**
     * 根据筛选条件查询指标Tree
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> selectIndexRelationTree(PageData pageData);

    /**
     * 查询指标count
     *
     * @param schemeSql
     * @return
     */
    Integer getIndicatorsCount(String schemeSql);
    /**
     * 查询指标的检测值
     *
     * @param indexArray
     * @return
     */
    List<Map<String, Object>> getDetectionValue(String[] indexArray);

    /**
     * 查询指标方案table
     *
     * @param schemeSql
     * @return
     */
    List<Map<String, Object>> getIndicatorsTable(String schemeSql);

    /**
     * 获取自定义指标方案表
     *
     * @param array
     * @return
     */
    List<Map<String, Object>> getIndicatorsTableName(String[] array);

    /**
     * 查询指标ECharts
     *
     * @param list
     * @param map
     * @param schemeSql
     * @return
     */
    Object[] getIndicatorsECharts(List<String> list, Map<String, Object> map, String schemeSql);

    /**
     * 查询指标账期最大值和最小值
     *
     * @param accountPeriodSql
     * @return
     */
    Map<String, Object> getMinDateAndMaxDate(String accountPeriodSql);

    /**
     * 查询指标维度信息
     *
     * @param dimensionSql
     * @return
     */
    List<Map<String, Object>> getDimensionData(String dimensionSql);

    /**
     * 查询指标柱状折线图
     *
     * @param list
     * @param map
     * @param schemeSql
     * @return
     */
    Object[] getColumnLineChart(List<String> list, Map<String, Object> map, String schemeSql);

    /**
     * 根据已选择的维度id查询选择的维度信息
     *
     * @param codeArry
     * @return
     */
    List<Map<String, String>> getDimensionSelectById(String[] codeArry);
}
