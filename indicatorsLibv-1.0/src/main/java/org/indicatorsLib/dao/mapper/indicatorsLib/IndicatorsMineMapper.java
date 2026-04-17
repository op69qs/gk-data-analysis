// IndicatorsMineNew.java

package org.indicatorsLib.dao.mapper.indicatorsLib;

import org.apache.ibatis.annotations.Param;
import org.indicatorsLib.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 我的指标
 *
 * @author Created by Samer on 2019/12/30.
 */
@Repository
public interface IndicatorsMineMapper {
    /**
     * 获取指标父级信息
     *
     * @param params
     * @return
     */
    List<Map<String, Object>>  getIndexParentInfo(@Param(value = "params") PageData params);
    /**
     * 根据指标查询指标的维度
     *
     * @param indexArray
     * @return
     */
    List<Map<String, Object>>  getIndexDimnsn(@Param(value = "indexArray") String[] indexArray);

    /**
     * 根据指标查询指标的周期
     *
     * @param indexArray
     * @return
     */
    List<Map<String, Object>>  getIndexPeriod(@Param(value = "indexArray") String[] indexArray);
    /**
     * 查询用户是否有管理员角色
     *
     * @param params
     * @return
     */
    int selectRoleByUserId(@Param(value = "params") PageData params);
    /**
     * 公共指标对应数据表逻辑删除指标信息
     *
     * @param params
     */
    void detelePublicRelation(@Param(value = "params") PageData params);

    /**
     * 指标管理获取指标信息
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getIndexManageList(@Param(value = "params") PageData params);

    /**
     * 指标管理获取指标计数
     *
     * @param params
     * @return
     */
    int getIndexManageCount(@Param(value = "params") PageData params);

    /**
     * 指标SQL试运行成功查询数据
     *
     * @param runSQL
     * @return
     */
    List<Map<String, Object>> selectDataBySQL(@Param(value = "runSQL") String runSQL);

    /**
     * 获取最大序列号
     *
     * @param params
     * @return
     */
    Map<String, String> getMaxSeq(@Param(value = "params") PageData params);

    /**
     * 获取指标信息
     *
     * @param params
     * @return
     */
    List<Map<String, String>> getIndexInfo(@Param(value = "params") PageData params);

    /**
     * 指标对应数据表新增我的指标信息
     *
     * @param params
     * @return
     */
    void addMineNewRelation(@Param(value = "params") PageData params);

    /**
     * 指标对应数据表修改指标信息
     *
     * @param params
     * @return
     */
    void updateMineRelation(@Param(value = "params") PageData params);

    /**
     * 指标对应数据表删除指标信息
     *
     * @param params
     * @return
     */
    boolean deteleMineRelation(@Param(value = "params") PageData params);

    /**
     * 新增指标公式表信息
     *
     * @param params
     * @return
     */
    void addFormula(@Param(value = "params") PageData params);

    /**
     * 修改指标公式表信息
     *
     * @param params
     * @return
     */
    void updateFormula(@Param(value = "params") PageData params);

    /**
     * 删除指标公式表信息
     *
     * @param params
     * @return
     */
    boolean deleteFormula(@Param(value = "params") PageData params);

    /**
     * 新建指标表
     *
     * @param params
     * @return
     */
    void createIndexTable(@Param(value = "params") PageData params);

    /**
     * 删除指标表
     *
     * @param params
     * @return
     */
    void dropIndexTable(@Param(value = "params") PageData params);

    /**
     * 手动调用加工逻辑
     *
     * @param params
     * @return
     */
    void callExeFormulaHand(@Param(value = "params") PageData params);

    /**
     * 新增指标和个人关系表信息
     *
     * @param params
     * @return
     */
    void addIndexUser(@Param(value = "params") PageData params);

    /**
     * 提交个人指标到公共指标
     *
     * @param params
     * @return
     */
    void submitIndexData(@Param(value = "params") PageData params);

    /**
     * 修改指标和个人关系表信息
     *
     * @param params
     * @return
     */
    void updateIndexUser(@Param(value = "params") PageData params);

    /**
     * 删除指标和个人关系表信息
     *
     * @param params
     * @return
     */
    boolean deleteIndexUser(@Param(value = "params") PageData params);

    /**
     * 指标历史数据跑批
     *
     * @param params
     */
    String indexHistoryRunBatch(@Param(value = "params") PageData params);

    /**
     * 指标历史数据跑批状态修改
     *
     * @param params
     */
    void updateHistoryState(@Param(value = "params") PageData params);

}///:~
