package org.seo.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.seo.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataSourceMapper {

    List<Map<String,Object>> getDataSourcePage(@Param("params") PageData pd);

    List<Map<String,Object>> getDataSource(@Param("params") PageData pd);

    List<Map<String,Object>> getDataSourceEnum(@Param("params") PageData pd);

    List<Map<String,Object>> getDataSourceEnumSelect();

    void addDataSource(@Param("params") PageData pd);

    void delDataSource(@Param("params") PageData pd);

    void editDataSource(@Param("params") PageData pd);

    Integer countDataSource(@Param("params") PageData pd);

    List<Map<String,Object>> getDataBase(@Param("params") PageData pd);

    void addDataBase(@Param("params") PageData pd);

    void delDataBase(@Param("params") PageData pd);

    List<Map<String,Object>> getDataSourceName(@Param("params") PageData pd);
}
