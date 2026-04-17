package org.inspect.service;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.Map;

public interface InspectionReceptionService {

    Map<String,Object> getInspectionReceptionData(@Param("params") PageData pd);

    void addInspectionReception(@Param("params") PageData pd);

    void editInspectionReception(@Param("params") PageData pd);

    void delInspectionReception(@Param("params") PageData pd);
}
