package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVTaskMapper;
import org.inspect.service.InspectionPostSVTaskService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionPostSVTaskServiceImpl implements InspectionPostSVTaskService {

    @Autowired
    private InspectionPostSVTaskMapper inspectionPostSVTaskMapper;


    @Override
    public List<Map<String, Object>> getInspectionTaskPage(PageData pd) {
        return inspectionPostSVTaskMapper.getInspectionTaskPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionTaskData(PageData pd) {
        return inspectionPostSVTaskMapper.getInspectionTaskData(pd);
    }

    @Override
    public Integer getInspectionTaskCount(PageData pd) {
        return inspectionPostSVTaskMapper.getInspectionTaskCount(pd);
    }

    @Override
    public void addInspectionTask(PageData pd) {
        pd.put("CREATE_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionPostSVTaskMapper.addInspectionTask(pd);
    }

    @Override
    public void editInspectionTask(PageData pd) {
        pd.put("UPDATE_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionPostSVTaskMapper.editInspectionTask(pd);
    }

    @Override
    public void editTaskLock(PageData pd) {
        pd.put("UPDATE_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionPostSVTaskMapper.editTaskLock(pd);
    }

    @Override
    public void delInspectionTask(PageData pd) {
        inspectionPostSVTaskMapper.delInspectionTask(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionPostSVTaskMapper.checkRepeat(pd);
    }

    @Override
    public Map<String, Object> getGKbyBook(PageData pd) {
        return inspectionPostSVTaskMapper.getGKbyBook(pd);
    }

    @Override
    public List<Map<String, Object>> getPeriod(PageData pd) {
        return inspectionPostSVTaskMapper.getPeriod(pd);
    }
}
