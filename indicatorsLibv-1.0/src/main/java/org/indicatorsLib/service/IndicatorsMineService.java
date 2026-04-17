// IndicatorsMineNewService.java

package org.indicatorsLib.service;

import org.indicatorsLib.util.PageData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 我的指标
 *
 * @author Created by Samer on 2019/12/30.
 */
public interface IndicatorsMineService {

    /**
     * 获取指标父级信息
     *
     * @param params
     * @return
     */
    List<Map<String, Object>>  getIndexParentInfo(PageData params);
    /**
     * 根据指标查询指标的维度
     *
     * @param indexArray
     * @return
     */
    List<Map<String, Object>>  getIndexDimnsn(String[] indexArray);
    /**
     * 公共指标对应数据表逻辑删除指标信息
     *
     * @param params
     * @return
     */
    void detelePublicRelation(PageData params);
    /**
     * 根据指标查询指标的周期
     *
     * @param indexArray
     * @return
     */
    List<Map<String, Object>>  getIndexPeriod(String[] indexArray);
    /**
     * 指标管理获取指标信息
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getIndexManageList(PageData params);

    /**
     * 指标管理获取指标计数
     *
     * @param params
     * @return
     */
    int getIndexManageCount(PageData params);

    /**
     * 指标SQL试运行(获取数据条数)
     *
     * @param runSQL
     * @return
     */
    String[] pilotRunSQL(String runSQL);

    /**
     * 指标SQL试运行成功查询数据
     *
     * @param runSQL
     * @return
     */
    List<Map<String, Object>> selectDataBySQL(String runSQL);

    /**
     * 获取最大序列号
     *
     * @param params
     * @return
     */
    Map<String, String> getMaxSeq(PageData params);

    /**
     * 获取指标信息
     *
     * @param params
     * @return
     */
    List<Map<String, String>> getIndexInfo(PageData params);

    /**
     * 指标对应数据表新增我的指标信息
     *
     * @param params
     * @return
     */
    void addMineNewRelation(PageData params);

    /**
     * 提交个人指标到公共指标
     *
     * @param params
     * @return
     */
    void submitIndexData(PageData params);

    /**
     * 新增指标公式表信息
     *
     * @param params
     * @return
     */
    void addFormula(PageData params);

    /**
     * 修改指标公式表信息
     *
     * @param params
     * @return
     */
    void updateFormula(PageData params);

    /**
     * 删除指标公式表信息
     *
     * @param params
     * @return
     */
    boolean deleteFormula(PageData params);

    /**
     * 新建指标表
     *
     * @param params
     * @return
     */
    void createIndexTable(PageData params);

    /**
     * 删除指标表
     *
     * @param params
     * @return
     */
    void dropIndexTable(PageData params);

    /**
     * 手动调用加工逻辑
     *
     * @param params
     * @return
     */
    void callExeFormulaHand(PageData params);

    /**
     * 新增指标和个人关系表信息
     *
     * @param params
     * @return
     */
    void addIndexUser(PageData params);

    /**
     * 修改指标和个人关系表信息
     *
     * @param params
     * @return
     */
    void updateIndexUser(PageData params);

    /**
     * 删除指标和个人关系表信息
     *
     * @param params
     * @return
     */
    boolean deleteIndexUser(PageData params);

    /**
     * 指标对应数据表修改指标信息
     *
     * @param params
     * @return
     */
    void updateMineRelation(PageData params);

    /**
     * 指标对应数据表删除指标信息
     *
     * @param params
     * @return
     */
    boolean deteleMineRelation(PageData params);


    /**
     * 指标历史数据跑批
     *
     * @param params
     */
    void indexHistoryRunBatch(PageData params);

    /**
     * 指标历史数据跑批状态修改
     *
     * @param params
     */
    void updateHistoryState(PageData params);

}///:~
