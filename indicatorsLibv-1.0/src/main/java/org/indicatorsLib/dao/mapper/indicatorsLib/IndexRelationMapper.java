package org.indicatorsLib.dao.mapper.indicatorsLib;

import org.apache.ibatis.annotations.Param;
import org.indicatorsLib.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IndexRelationMapper {

    /**
     * 根据筛选条件查询指标信息
     *
     * @param pd
     * @return
     */
    List<Map<String, Object>> selectIndexRelationInfo(@Param("params") PageData pd);

    /**
     * 获取指标详细描述
     *
     * @param pd
     * @return
     */
    Map<String, Object> getIndexDetails(@Param("params") PageData pd);

    /**
     * 根据筛选条件查询指标Tree
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> selectIndexRelationTree(@Param("params") PageData pageData);

    /**
     * 查询指标count
     *
     * @param schemeSql
     * @return
     */
    Integer getIndicatorsCount(@Param("schemeSql") String schemeSql);
    /**
     * 查询指标的检测值
     *
     * @param indexArray
     * @return
     */
    List<Map<String, Object>> getDetectionValue(@Param("indexArray") String[] indexArray);
    /**
     * 查询指标方案table
     *
     * @param schemeSql
     * @return
     */
    List<Map<String, Object>> getIndicatorsTable(@Param("schemeSql") String schemeSql);

    /**
     * 获取自定义指标方案表
     *
     * @param array
     * @return
     */
    List<Map<String, Object>> getIndicatorsTableName(@Param("array") String[] array);

    /**
     * 查询指标方案ECharts
     *
     * @param schemeSql
     * @return
     */
    List<Map<String, Object>> getIndicatorsECharts(@Param("schemeSql") String schemeSql);

    /**
     * 查询指标账期最大值和最小值
     *
     * @param accountPeriodSql
     * @return
     */
    Map<String, Object> getMinDateAndMaxDate(@Param("accountPeriodSql") String accountPeriodSql);

    /**
     * 查询指标维度信息
     *
     * @param dimensionSql
     * @return
     */
    List<Map<String, Object>> getDimensionData(@Param("dimensionSql") String dimensionSql);

    /**
     * 根据已选择的维度id查询选择的维度信息
     *
     * @param codeArry
     * @return
     */
    List<Map<String, String>> getDimensionSelectById(@Param("codeArry") String[] codeArry);

}
