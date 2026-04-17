package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionTaskMapper {

    List<Map<String,Object>> getInspectionTaskPage(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionTaskData(@Param("params") PageData pd);

    Integer getInspectionTaskCount(@Param("params") PageData pd);

    void addInspectionTask(@Param("params") PageData pd);

    void editInspectionTask(@Param("params") PageData pd);

    void editTaskLock(@Param("params") PageData pd);

    void delInspectionTask(@Param("params") PageData pd);

    List<Map<String,Object>> checkRepeat(@Param("params") PageData pd);

    Map<String,Object> getGKbyBook(@Param("params") PageData pd);

    Map<String,Object> getBookById(@Param("params") PageData pd);

    List<Map<String,Object>> getPeriod(@Param("params") PageData pd);

    Map<String,Object> getBookbyGuokuId(@Param("params") PageData pd);

}
