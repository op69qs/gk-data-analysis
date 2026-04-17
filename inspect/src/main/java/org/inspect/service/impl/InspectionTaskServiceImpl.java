package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionTaskMapper;
import org.inspect.dao.mapper.inspect.QuestionBankMapper;
import org.inspect.service.InspectionPlanService;
import org.inspect.service.InspectionTaskService;
import org.inspect.service.QuestionBankService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionTaskServiceImpl implements InspectionTaskService {

    @Autowired
    private InspectionTaskMapper inspectionTaskMapper;


    @Override
    public List<Map<String, Object>> getInspectionTaskPage(PageData pd) {
        return inspectionTaskMapper.getInspectionTaskPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionTaskData(PageData pd) {
        return inspectionTaskMapper.getInspectionTaskData(pd);
    }

    @Override
    public Integer getInspectionTaskCount(PageData pd) {
        return inspectionTaskMapper.getInspectionTaskCount(pd);
    }

    @Override
    public void addInspectionTask(PageData pd) {
        pd.put("CREATE_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionTaskMapper.addInspectionTask(pd);
    }

    @Override
    public void editInspectionTask(PageData pd) {
        pd.put("UPDATE_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionTaskMapper.editInspectionTask(pd);
    }

    @Override
    public void editTaskLock(PageData pd) {
        pd.put("UPDATE_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        inspectionTaskMapper.editTaskLock(pd);
    }

    @Override
    public void delInspectionTask(PageData pd) {
        inspectionTaskMapper.delInspectionTask(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionTaskMapper.checkRepeat(pd);
    }

    @Override
    public Map<String, Object> getGKbyBook(PageData pd) {
        return inspectionTaskMapper.getGKbyBook(pd);
    }

    @Override
    public List<Map<String, Object>> getPeriod(PageData pd) {
        return inspectionTaskMapper.getPeriod(pd);
    }

    @Override
    public Map<String, Object> getBookbyGuokuId(PageData pd) {
        return inspectionTaskMapper.getBookbyGuokuId(pd);
    }
}
