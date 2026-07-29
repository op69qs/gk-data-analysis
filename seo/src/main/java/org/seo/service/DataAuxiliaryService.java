package org.seo.service;

import org.seo.util.PageData;

import java.util.List;
import java.util.Map;

public interface DataAuxiliaryService {

    /**
     * 获取数据表一级分类下拉选
     *
     * @return
     */
    List<Map<String, Object>> getFirstClassifySelection();

    /**
     * 获取数据表二级分类下拉选
     *
     * @return
     */
    List<Map<String, Object>> getSecondClassifySelection(PageData pd);

    /**
     * 获取数据配置的数据源下拉选
     *
     * @return
     */
    List<Map<String, Object>> getDataSourceSelection();

    /**
     * 根据数据源查询数据源下的数据库下拉选
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getDataBaseSelection(PageData pageData);

    /**
     * 根据数据库配置主键查询实际数据库和Schema。
     */
    List<Map<String, Object>> getDataBaseInfo(PageData pageData);

    /**
     * 根据数据源和数据库查询数据库下的数据表下拉选
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getDataTableSelection(PageData pageData);

    /**
     * 根据选择的数据表据查询该数据表/字段注释
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getDataTableComments(PageData pageData);

    /**
     * 根据数据源主键查询数据源的信息
     *
     * @param sourceId
     * @return
     */
    List<Map<String, Object>> getDataSourceInfo(Map<String, Object> map);
}
