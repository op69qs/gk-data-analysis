package org.indicatorsLib.dao.mapper.indicatorsLib;

import org.apache.ibatis.annotations.Param;
import org.indicatorsLib.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IndexSchemeMapper {

    /**
     * 推送指标库到图库
     *
     * @param pageData
     */
    void pushIndexToVS(@Param("params") PageData pageData);

    /**
     * 保存指标方案
     *
     * @param params
     */
    void saveIndexScheme(@Param("params") Map<String, Object> params);

    /**
     * 根据指标方案id 查询保存的指标方案表SCHEME_COLUMS字段拿到指标ID
     *
     * @param schemeId
     * @return
     */
    String selectSchemeColumn(@Param("schemeId") String schemeId);

    /**
     * 查询保存的指标方案表头字段
     *
     * @param array
     * @return
     */
    List<Map<String, Object>> selectSchemeThead(@Param("array") String[] array);

    /**
     * 根据userId查询个人/公共方案count
     *
     * @param pageData
     * @return
     */
    int getSchemeCount(@Param("params") PageData pageData);

    /**
     * 根据userId查询个人常用的公共方案count
     *
     * @param pageData
     * @return
     */
    int getUsedPublicSchemeCount(@Param("params") PageData pageData);

    /**
     * 查询个人保存的指标方案
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> selectSchemeTable(@Param("params") PageData pageData);

    /**
     * 根据指标方案id查询指标方案的执行sql
     *
     * @param schemeId
     * @return
     */
    String selectSchemeSQL(@Param("schemeId") String schemeId);

    /**
     * 根据方案id删除方案
     *
     * @param params
     */
    void deleteSchemeById(@Param("params") Map<String, Object> params);

    /**
     * 查询指标公共方案
     *
     * @param pageData
     */
    List<Map<String, Object>> selectPublicScheme(@Param("params") PageData pageData);

    /**
     * 根据用户Id和指标方案Id查询个人常用公共方案
     *
     * @param params
     * @return
     */
    int isUsedPublicScheme(@Param("params") Map<String, Object> params);

    /**
     * 新增指标公共方案
     *
     * @param params
     */
    void insertPublicScheme(@Param("params") Map<String, Object> params);

    /**
     * 删除指标公共方案
     *
     * @param params
     */
    void deletePublicScheme(@Param("params") Map<String, Object> params);

    /**
     * 保存指标方案之前校验方案描述的唯一性
     *
     * @param params
     * @return
     */
    Integer validatySchemeDescr(@Param("params") Map<String, Object> params);

    /**
     * 修改已保存的指标方案信息
     *
     * @param params
     */
    void updateSchemeData(@Param("params") Map<String, Object> params);
}
