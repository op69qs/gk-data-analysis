package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionGroupMapper {

    List<Map<String,Object>> getInspectionGroupPage(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionGroupData(@Param("params") PageData pd);

    Integer getInspectionGroupCount(@Param("params") PageData pd);

    void addInspectionGroup(@Param("params") PageData pd);

    void editInspectionGroup(@Param("params") PageData pd);

    void delInspectionGroup(@Param("params") PageData pd);

    List<Map<String,Object>> checkRepeat(@Param("params") PageData pd);

}
