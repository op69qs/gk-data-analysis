package org.dockingProjects.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/8/14 17:04
 * @Description
 */
public interface TreasuryAccessHouseService {


    /**
     * 三证合一_个体开业、注销信息  接口信息
     * @param list
     */
    void insertThreeCertificatesSyncreticHouse(List<Map<String, Object>> list);

    /**
     * 企业基本信息-多证合一数据  接口信息
     * @param list
     */
    void insertEnterpriseBasicInformationHouse(List<Map<String, Object>> list);

    /**
     * 非企业团体基本信息 接口信息
     * @param list
     */
    void insertNonEnterpriseBasicInformationHouse(List<Map<String, Object>> list);


}
