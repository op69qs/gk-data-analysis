package org.dockingProjects.mapper.mysql;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MysqlMapper
 * @Description TODO
 * @Auther Henrylq
 * @Version 1.0
 */
@Mapper
public interface MysqlMapper {

    // @Select("select * from user")
    List<Map<String,Object>> findAll();
}
