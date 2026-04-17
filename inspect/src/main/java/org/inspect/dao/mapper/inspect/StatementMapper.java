package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StatementMapper {

    List<Map<String,Object>> getStatementData(@Param("params") PageData pd);

    void addStatement(@Param("params") PageData pd);

    void editStatement(@Param("params") PageData pd);

    void delStatement(@Param("params") PageData pd);

}
