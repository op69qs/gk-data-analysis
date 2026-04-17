package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionContentMapper {

    List<Map<String,Object>> getContentData(@Param("params") PageData pd);

    void addContent(@Param("params") PageData pd);

    void editContent(@Param("params") PageData pd);

    void delContent(@Param("params") PageData pd);

}
