package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.enumSetting.mapper.DetectionMapper;
import org.jeecg.modules.enumSetting.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("detectionService")
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    private DetectionMapper detectionMapper;

    @Override
    public List<Map<String, Object>> getData(PageData pd) {
        return detectionMapper.getData(pd);
    }


    @Override
    public Integer getCount(PageData pd) {
        return detectionMapper.getCount(pd);
    }


    @Override
    public List<Map<String, Object>> checkCode(PageData pd) {
        return detectionMapper.checkCode(pd);
    }

    @Override
    public String getMaxDetectionId(PageData pd) {
        return detectionMapper.getMaxDetectionId(pd);
    }

    @Override
    public void addDetection(PageData pd) {
        detectionMapper.addDetection(pd);
    }

    @Override
    public void editDetection(PageData pd) {
        detectionMapper.editDetection(pd);
    }

    @Override
    public void delDetection(PageData pd) {
        detectionMapper.delDetection(pd);
    }

    @Override
    public void delDetectionNo(PageData pd) {
        detectionMapper.delDetectionNo(pd);
    }


    @Override
    public List<Map<String, Object>> getDetectionType(PageData pd) {
        return detectionMapper.getDetectionType(pd);
    }

    @Override
    public List<Map<String, Object>> getDetectionTypeAll(PageData pd) {
        return detectionMapper.getDetectionTypeAll(pd);
    }

}
