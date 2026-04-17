package org.jeecg.modules.enumSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ErrorLogMapper {

    List<Map<String, Object>> getData(@Param("params") PageData pd);

    void callProc(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);
}
