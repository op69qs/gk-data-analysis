package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionNoticeMapper {

    Map<String,Object> getInspectionNoticeData(@Param("params") PageData pd);

    void addInspectionNotice(@Param("params") PageData pd);

    void editInspectionNotice(@Param("params") PageData pd);

    void editNoticeUser(@Param("params") PageData pd);

    void delInspectionNotice(@Param("params") PageData pd);

}
