package org.dockingProjects.mapper.clickhouse;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HouseMapper
 * @Description TODO
 * @Auther Henrylq
 * @Version 1.0
 */
@Repository
public interface HouseMapper {

//    @Select("select * from test.user_local")
    List<Map<String,Object>> findAll();

    /**
     * 三证合一_个体开业、注销信息  接口信息
     * @param list
     */
    void insertThreeCertificatesSyncretic(@Param("list") List<Map<String, Object>> list);

    /**
     * 企业基本信息-多证合一数据  接口信息
     * @param list
     */
    void insertEnterpriseBasicInformation(@Param("list") List<Map<String, Object>> list);

    /**
     * 非企业团体基本信息 接口信息
     * @param list
     */
    void insertNonEnterpriseBasicInformation(@Param("list") List<Map<String, Object>> list);
}
