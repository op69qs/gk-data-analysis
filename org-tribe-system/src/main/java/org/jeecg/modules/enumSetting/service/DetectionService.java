package org.jeecg.modules.enumSetting.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface DetectionService {
    List<Map<String, Object>> getData(PageData pd);

    Integer getCount(PageData pd);

    List<Map<String, Object>> checkCode(PageData pd);

    /**
     * 获取检查值ID最大值
     *
     * @param pd
     * @return
     */
    String getMaxDetectionId(PageData pd);

    void addDetection(PageData pd);

    void editDetection(PageData pd);

    void delDetection(PageData pd);

    void delDetectionNo(PageData pd);

    List<Map<String, Object>> getDetectionType(PageData pd);

    List<Map<String, Object>> getDetectionTypeAll(PageData pd);

}
