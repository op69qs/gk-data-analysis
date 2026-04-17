package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface InspectionApprovalListMapper {

    Map<String,Object> getApprovalListData(@Param("params") PageData pd);

    void addApprovalList(@Param("params") PageData pd);

    void editApprovalList(@Param("params") PageData pd);

    void delApprovalList(@Param("params") PageData pd);

}
