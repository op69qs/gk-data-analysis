package org.jeecg.modules.enumSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface DetectionMapper {

    List<Map<String, Object>> getData(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    List<Map<String, Object>> checkCode(@Param("params") PageData pd);

    /**
     * 获取检查值ID最大值
     *
     * @param pd
     * @return
     */
    String getMaxDetectionId(@Param("params") PageData pd);

    void addDetection(@Param("params") PageData pd);

    void editDetection(@Param("params") PageData pd);

    void delDetection(@Param("params") PageData pd);

    void delDetectionNo(@Param("params") PageData pd);

    List<Map<String, Object>> getDetectionType(@Param("params") PageData pd);

    List<Map<String, Object>> getDetectionTypeAll(@Param("params") PageData pd);

}
