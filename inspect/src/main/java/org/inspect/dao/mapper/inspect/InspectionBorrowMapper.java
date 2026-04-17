package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.model.BorrowData;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionBorrowMapper {

    List<Map<String,Object>> getInspectionBorrowData(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionBorrowTemp(@Param("params") PageData pd);

    void addInspectionBorrow(@Param("params") PageData pd);

    void editInspectionBorrow(@Param("params") PageData pd);

    void editBorrowUser(@Param("params") PageData pd);

    void editBorrowCharge(@Param("params") PageData pd);

    void delInspectionBorrow(@Param("params") PageData pd);

    List<Map<String,Object>> checkRepeat(@Param("params") PageData pd);

    List<BorrowData> getInspectionBorrow(@Param("params") PageData pd);

    List<Map<String,Object>> getEnum(@Param("params") PageData pd);

}
