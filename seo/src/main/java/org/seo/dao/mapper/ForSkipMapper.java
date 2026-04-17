package org.seo.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.seo.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ForSkipMapper {

    List<Map<String, Object>> getFileList(@Param("params") PageData pd);
}
