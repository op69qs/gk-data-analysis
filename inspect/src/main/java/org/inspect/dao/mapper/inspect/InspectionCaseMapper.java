package org.inspect.dao.mapper.inspect;


import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionCaseMapper {

    List<Map<String,Object>> getInspectionCasePage(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionCaseData(@Param("params") PageData pd);

    Integer countCase(@Param("params") PageData pd);

    void addInspectionCase(@Param("params") PageData pd);

    void editInspectionCase(@Param("params") PageData pd);

    void delInspectionCase(@Param("params") PageData pd);
}
