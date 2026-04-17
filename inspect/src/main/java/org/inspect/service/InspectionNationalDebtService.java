package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionNationalDebtService {

    /**
     * 查询国债巡查信息tree list
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getInspectionNationalDebtTreeList(PageData pageData);

    /**
     * 修改国债巡查信息
     *
     * @param map
     * @return
     */
    void updateNationalDebtData(Map<String, Object> map);

    /**
     * 新增考评项目评分信息
     *
     * @param map
     * @param list
     */
    void insertInspectionProjectRate(Map<String, Object> map, List<Map<String, Object>> list);


    /**
     * 编辑考评项目评分信息
     *
     * @param map
     * @param list
     */
    void updateInspectionProjectRate(Map<String, Object> map, List<Map<String, Object>> list);

    /**
     * 查看考评项目评分信息
     *
     * @param pageData
     */
    List<Map<String, Object>> selectInspectionProjectRate(PageData pageData);

    /**
     * 新增考评项目评分汇总信息
     *
     * @param map
     * @param list
     */
    void insertInspectionProjectSummary(Map<String, Object> map, List<Map<String, Object>> list);

    /**
     * 编辑考评项目评分汇总信息
     *
     * @param pageData
     * @param list
     */
    void updateInspectionProjectSummary(PageData pageData, List<Map<String, Object>> list);

    /**
     * 撤销考评项目评分汇总信息
     *
     * @param pageData
     */
    void revokeNationalSummary(PageData pageData);

    /**
     * 查看考评项目评分汇总信息
     *
     * @param pageData
     */
    List<Map<String, Object>> selectInspectionProjectSummary(PageData pageData);

    /**
     * 查看考评项目评分汇总信息个数
     *
     * @param map
     */
    int selectProjectSummaryCount(Map<String, Object> map);

    /**
     * 考评项目评分汇总
     *
     * @param pageData
     * @param list
     * @return
     */
    List<Map<String, Object>> selectProjectSummary(PageData pageData, List<String> list);

    /**
     * 查询考评项目是否有清零项
     *
     * @param pageData
     * @param array
     * @param list
     * @return
     */
    List<Map<String, Object>> selectClearZeroProjects(PageData pageData, String[] array, List<String> list);

    /**
     * 获取考评项目的清零项
     *
     * @return
     */
    Map<String, Object> getClearZeroProjectItems();

    /**
     * 新增巡查信息是获取主键值和检查次数
     *
     * @param map
     * @return
     */
    String[] getNationalDebtId(Map<String, Object> map);

    /**
     * 考评评分明细查询
     *
     * @param pageData
     * @return
     */
    Map<String, Object> getCheckDataList(PageData pageData);

    /**
     * 获取到处需要的数据
     *
     * @param pageData
     * @return
     */
    Map<String, Object> getExprotData(PageData pageData);
}
