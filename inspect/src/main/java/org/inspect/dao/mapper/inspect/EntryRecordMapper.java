package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface EntryRecordMapper {

    Map<String,Object> getEntryRecordData(@Param("params") PageData pd);

    void addEntryRecord(@Param("params") PageData pd);

    void editEntryRecord(@Param("params") PageData pd);

    void delEntryRecord(@Param("params") PageData pd);

}
