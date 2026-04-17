package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionNationalDebtMapper {

    /**
     * 查询国债巡查信息tree list
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getInspectionNationalDebtTreeList(@Param("params") PageData pageData);


    String getqQeryConditon(@Param("params") PageData pageData);

    /**
     * 查询国债巡查检查次数
     *
     * @param map
     * @return
     */
    int selectCheckCount(@Param("params") Map<String, Object> map);

    /**
     * 查询指定国库的下级检查国库信息，以便于巡查汇总
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> selectNationalDebtChildren(@Param("params") PageData pageData);

    /**
     * 校验当前国库是否存在于国债巡查中
     *
     * @param map
     * @return
     */
    int selectNationalDebtCount(@Param("params") Map<String, Object> map);

    /**
     * 新增国债巡查信息
     *
     * @param list
     */
    void insertInspectionNationalDebtData(@Param("list") List<Map<String, Object>> list);

    /**
     * 新增考评项目评分信息
     *
     * @param list
     */
    void insertInspectionProjectRate(@Param("list") List<Map<String, Object>> list);

    /**
     * 修改巡查记录表信息
     *
     * @param map
     */
    void updateNationalDebtData(@Param("params") Map<String, Object> map);

    /**
     * 省、市级国库汇总数据
     *
     * @param summarySQL
     */
    List<Map<String, Object>> getProjectSummaryList(@Param("summarySQL") String summarySQL);

    /**
     * 根据国库Id和检查日期查询当前国库不存在的上级巡查信息
     *
     * @param map
     */
    List<Map<String, Object>> getParentNationalDebt(@Param("params") Map<String, Object> map);

    /**
     * 删除考评项目评分信息
     *
     * @param map
     */
    void deleteInspectionProjectRate(@Param("params") Map<String, Object> map);

    /**
     * 查看考评项目评分信息
     *
     * @param pageData
     */
    List<Map<String, Object>> selectInspectionProjectRate(@Param("params") PageData pageData);

    /**
     * 考评项目评分汇总
     *
     * @param pageData
     */
    List<Map<String, Object>> selectProjectSummary(@Param("params") PageData pageData);

    /**
     * 查询考评项目是否有清零项
     *
     * @param pageData
     * @param array
     * @param list
     * @return
     */
    List<Map<String, Object>> selectClearZeroProjects(@Param("params") PageData pageData, @Param("array") String[] array, @Param("list") List<String> list);

    /**
     * 新增考评项目评分汇总信息
     *
     * @param list
     */
    void insertInspectionProjectSummary(@Param("list") List<Map<String, Object>> list);

    /**
     * 撤销考评项目评分汇总信息
     *
     * @param pageData
     */
    void deleteInspectionProjectSummary(@Param("params") PageData pageData);

    /**
     * 查看考评项目评分汇总信息
     *
     * @param pageData
     */
    List<Map<String, Object>> selectInspectionProjectSummary(@Param("params") PageData pageData);

    /**
     * 查看考评项目评分汇总信息个数
     *
     * @param map
     */
    int selectProjectSummaryCount(@Param("params") Map<String, Object> map);

    /**
     * 获取考评项目的清零项
     *
     * @return
     */
    List<Map<String, Object>> getClearZeroProjectItems();

    /**
     * 根据当前国库Id查询所有该国库的信息
     *
     * @param map
     * @return
     */
    Map<String, Object> getGuoKuDataById(@Param("params") Map<String, Object> map);

    /**
     * 根据当前巡查主键查询所有该国库的信息
     *
     * @param map
     * @return
     */
    Map<String, Object> getGuoKuDataByKey(@Param("params") Map<String, Object> map);

    /**
     * 根据当前国库Id查询所有该国库的上级国库
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> getAllParentGuoKu(@Param("params") Map<String, Object> map);

    /**
     * 查询国库检查次数最大值
     *
     * @param map
     * @return
     */
    Map<String, Object> getMaxCheckCount(@Param("params") Map<String, Object> map);

    /**
     * 考评评分明细查询
     *
     * @param checkDataSQL
     * @return
     */
    List<Map<String, Object>> getCheckDataList(@Param("checkDataSQL") String checkDataSQL);

    /**
     * 获取到处需要的数据
     *
     * @param exportSQL
     * @return
     */
    List<Map<String, Object>> getExprotData(@Param("exportSQL") String exportSQL);
}
