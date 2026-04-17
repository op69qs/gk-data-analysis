package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface InspectionReceptionMapper {

    Map<String,Object> getInspectionReceptionData(@Param("params") PageData pd);

    void addInspectionReception(@Param("params") PageData pd);

    void editInspectionReception(@Param("params") PageData pd);

    void delInspectionReception(@Param("params") PageData pd);

}
