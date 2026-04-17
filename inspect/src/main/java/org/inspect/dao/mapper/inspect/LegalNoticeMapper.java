package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LegalNoticeMapper {

    Map<String,Object> getLegalNoticeData(@Param("params") PageData pd);

    void addLegalNotice(@Param("params") PageData pd);

    void editLegalNotice(@Param("params") PageData pd);

    void delLegalNotice(@Param("params") PageData pd);

}
