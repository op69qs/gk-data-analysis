package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionFileMapper {

    List<Map<String,Object>> getInspectionFileData(@Param("params") PageData pd);

    void addInspectionFile(@Param("params") PageData pd);

    void editInspectionFile(@Param("params") PageData pd);

    void delInspectionFile(@Param("params") PageData pd);

}
