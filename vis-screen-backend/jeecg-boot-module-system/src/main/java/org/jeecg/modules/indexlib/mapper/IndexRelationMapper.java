package org.jeecg.modules.indexlib.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IndexRelationMapper {
    List<Map<String, String>> getBatchIndexInfo(@Param("params") PageData pageData);

    Map<String, Object> getIndexDetails(@Param("params") PageData pageData);
}
