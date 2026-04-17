package org.fixedReport.dao.mapper.fixedReport;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author .
 * @Ddate 2020/8/13 15:22
 */
@Repository
public interface TreasuryAccessMapper {

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
    void delThreeCertificatesSyncretic();
    void delEnterpriseBasicInformation();
    void delNonEnterpriseBasicInformation();

    void callThreeCertificatesSyncretic();
    void callEnterpriseBasicInformation();
    void callNonEnterpriseBasicInformation();
}
