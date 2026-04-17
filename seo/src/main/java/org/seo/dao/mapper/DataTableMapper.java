package org.seo.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.seo.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataTableMapper {

    /**
     * 查询数据源Tree
     *
     * @return
     */
    List<Map<String, Object>> getDataSourceTree(@Param("params") PageData pageData);

    /**
     * 查询数据源关系表Tree
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getRelationTree(@Param("params") PageData pageData);

    /**
     * 查询数据源关系表数据
     *
     * @param pageData
     * @return
     */
    List<Map<String, Object>> getRelationData(@Param("params") PageData pageData);

    /**
     * 判断选择的数据表是否已新增
     *
     * @param pageData
     * @return
     */
    int getTableCount(@Param("params") PageData pageData);

    /**
     * 新增数据表
     *
     * @param list
     * @return
     */
    void addDataTable(@Param("list") List<Map<String, Object>> list);

    /**
     * 删除数据表
     *
     * @param tableId
     * @return
     */
    void deleteDataTable(@Param("tableId") String tableId);

}
