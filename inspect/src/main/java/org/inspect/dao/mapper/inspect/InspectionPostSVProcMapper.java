package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionPostSVProcMapper {

    List<Map<String,Object>> getInspectionProcData(@Param("params") PageData pd);

}
