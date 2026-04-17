package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionProcSubMapper {

    List<Map<String,Object>> getInspectionProcSubData(@Param("params") PageData pd);

    void addInspectionProcSub(@Param("params") PageData pd);

    void editInspectionProcSub(@Param("params") PageData pd);

    void editPostSVInspectionProcSub(@Param("params") PageData pd);

    void editProcBySubProc(@Param("params") PageData pd);

}
