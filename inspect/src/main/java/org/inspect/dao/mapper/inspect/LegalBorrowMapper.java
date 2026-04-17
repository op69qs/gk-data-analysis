package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.model.BorrowData;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LegalBorrowMapper {

    void editInspectProjectName(@Param("params") PageData pd);

    List<Map<String,Object>> getLegalBorrowData(@Param("params") PageData pd);

    void addLegalBorrow(@Param("params") PageData pd);

    void editLegalBorrow(@Param("params") PageData pd);

    void delLegalBorrow(@Param("params") PageData pd);

}
