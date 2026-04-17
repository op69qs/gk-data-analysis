package org.seo.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.seo.model.DataSourceInfoEntity;

import java.util.List;

@Mapper
public interface DataSourceInfoMapper {

    @Select("SELECT\n" +
            "\tb.ID,\n" +
            "\ta.DATASOURCE_NAME,\n" +
            "\tb.DRIVERCLASS_NAME,\n" +
            "\tb.DATASOURCE_URL,\n" +
            "\tb.USERNAME AS DATASOURCE_USERNAME,\n" +
            "\tb.`PASSWORD` AS DATASOURCE_PASSWORD\n" +
            "FROM\n" +
            "\tseo.seo_datasource a,\n" +
            "\tseo.seo_datasource_database b\n" +
            "WHERE\n" +
            "\ta.ID = b.SOURCE_ID")
    List<DataSourceInfoEntity> getList();
}
