package org.jeecg.modules.enumSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ErrorLogMapper {

    List<Map<String, Object>> getData(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    int add(@Param("params") PageData pd);

    int edit(@Param("params") PageData pd);

    int del(@Param("id") String id);

    Map<String, Object> getTaskById(@Param("id") String id);

    int updateTaskStatus(@Param("id") String id, @Param("status") String status);

    int markTaskRunning(@Param("id") String id);
}
