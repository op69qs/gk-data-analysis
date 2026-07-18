package org.jeecg.modules.enumSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DynamicRefreshRunLogMapper {

    int add(@Param("params") PageData pd);

    List<Map<String, Object>> getData(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    Map<String, Object> getById(@Param("id") String id);

    int complete(@Param("id") String id,
                 @Param("status") String status,
                 @Param("resultMessage") String resultMessage);

    int deleteByTaskId(@Param("taskId") String taskId);
}
