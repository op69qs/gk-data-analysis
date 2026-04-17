package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionApprovalMapper {

    List<Map<String,Object>> getInspectionApprovalPage(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionApprovalData(@Param("params") PageData pd);
    List<Map<String,Object>> getAppravalProcess(@Param("params") PageData pd);

    Integer getInspectionApprovalCount(@Param("params") PageData pd);

    void addInspectionApproval(@Param("params") PageData pd);

    void editInspectionApproval(@Param("params") PageData pd);

    void delInspectionApproval(@Param("params") PageData pd);
    void updateApproval(@Param("params") PageData pd);
}
