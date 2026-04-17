package org.indicatorsLib.service;

import org.indicatorsLib.util.PageData;

import java.util.List;
import java.util.Map;

public interface IndexSchemeService {

    /**
     * 推送指标库到图库
     *
     * @param pageData
     */
    void pushIndexToVS(PageData pageData);


    /**
     * 保存指标方案
     *
     * @param scheme
     */
    void saveIndexScheme(Map<String, Object> scheme);

    /**
     * 查询保存的指标方案表头字段
     *
     * @param schemeId
     * @return
     */
    List<Map<String, Object>> selectSchemeThead(String schemeId);

    /**
     * 根据userId查询个人/公共方案count
     *
     * @param pageData
     * @return
     */
    int getSchemeCount(PageData pageData);

    /**
     * 根据userId查询个人常用的公共方案count
     *
     * @param pageData
     * @return
     */
    int getUsedPublicSchemeCount(PageData pageData);

    /**
     * 查询已保存的指标方案
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> selectSchemeTable(PageData pageData);

    /**
     * 根据指标方案id查询指标方案的执行sql
     *
     * @param schemeId
     * @return
     */
    String selectSchemeSQL(String schemeId);

    /**
     * 根据方案id删除方案
     *
     * @param params
     */
    void deleteSchemeById(Map<String, Object> params);

    /**
     * 查询指标公共方案
     *
     * @param pageData
     */
    List<Map<String, Object>> selectPublicScheme(PageData pageData);

    /**
     * 根据用户Id和指标方案Id查询个人常用公共方案
     *
     * @param pageData
     * @return
     */
    boolean isUsedPublicScheme(PageData pageData);

    /**
     * 新增指标公共方案
     *
     * @param params
     */
    void insertPublicScheme(Map<String, Object> params);

    /**
     * 删除指标公共方案
     *
     * @param params
     */
    void deletePublicScheme(Map<String, Object> params);

    /**
     * 保存指标方案之前校验方案描述的唯一性
     *
     * @param params
     * @return
     */
    Integer validatySchemeDescr(Map<String, Object> params);

    /**
     * 修改已保存的指标方案信息
     *
     * @param params
     */
    void updateSchemeData(Map<String, Object> params);
}
