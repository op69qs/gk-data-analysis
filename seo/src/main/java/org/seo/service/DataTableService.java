package org.seo.service;

import org.seo.util.IndexTreeNode;
import org.seo.util.PageData;

import java.util.List;
import java.util.Map;

public interface DataTableService {

    /**
     * 查询数据源Tree
     *
     * @param pageData
     * @return
     */
    List<IndexTreeNode> getDataSourceTree(PageData pageData);

    /**
     * 查询数据源关系表Tree
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getRelationTree(PageData pageData);

    /**
     * 查询数据源关系表数据
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getRelationData(PageData pageData);

    /**
     * 判断选择的数据表是否已新增
     *
     * @param pageData
     * @return
     */
    int getTableCount(PageData pageData);

    /**
     * 新增数据表
     *
     * @param list
     * @return
     */
    void addDataTable(List<Map<String, Object>> list);

    /**
     * 编辑数据表
     *
     * @param tableId
     * @param list
     */
    void editDataTable(String tableId, List<Map<String, Object>> list);
}
