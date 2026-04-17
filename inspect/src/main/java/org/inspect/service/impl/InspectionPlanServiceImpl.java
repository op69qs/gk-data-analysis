package org.inspect.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.inspect.dao.mapper.inspect.InspectionPlanMapper;
import org.inspect.service.InspectionPlanService;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * Created by dj on 2019/9/6
 */
@Service
public class InspectionPlanServiceImpl implements InspectionPlanService {
    @Autowired
    private InspectionPlanMapper inspectionPlanMapper;

    @Override
    public List<Map<String, Object>> getCurPlanName(PageData pd) {
        return inspectionPlanMapper.getCurPlanName(pd);
    }

    @Override
    public List<Map<String, Object>> getCurTaskQuestion_1(PageData pd) {
        return inspectionPlanMapper.getCurTaskQuestion_1(pd);
    }

    @Override
    public List<Map<String, Object>> getStatisticsTableByPlanId(PageData pd) {
        return inspectionPlanMapper.getStatisticsTableByPlanId(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionPlanData(PageData pd) {
        return inspectionPlanMapper.getInspectionPlanData(pd);
    }

    @Override
    public List<Map<String, Object>> getTalentPool(PageData pd) {
        return inspectionPlanMapper.getTalentPool(pd);
    }

    @Override
    public List<Map<String, Object>> getTalentPoolAll(PageData pd) {
        return inspectionPlanMapper.getTalentPoolAll(pd);
    }

    @Override
    public List<Map<String, Object>> isEditUserDuties(PageData pd) {
        return inspectionPlanMapper.isEditUserDuties(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionPlanMapper.checkRepeat(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionPlanInspected(PageData pd) {
        return inspectionPlanMapper.getInspectionPlanInspected(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionProcess(PageData pd) {
        return inspectionPlanMapper.getInspectionProcess(pd);
    }


    @Override
    public int getInspectionPlanDataCount(PageData pd) {
        return inspectionPlanMapper.getInspectionPlanDataCount(pd);
    }

    @Override
    public void saveInspectPlan(PageData pd) {
        inspectionPlanMapper.saveInspectPlan(pd);
    }

    @Override
    public void saveInspectPlanSub(PageData pd) {
        inspectionPlanMapper.saveInspectPlanSub(pd);
    }

    @Override
    public void saveInspectPlanGroup(PageData pd) {
        inspectionPlanMapper.saveInspectPlanGroup(pd);
    }

    @Override
    public void saveInspectProcess(PageData pd) {
        inspectionPlanMapper.saveInspectProcess(pd);
    }

    @Override
    public void delInspectPlanMain(PageData pd) {
        inspectionPlanMapper.delInspectPlanMain(pd);
    }

    @Override
    public void delInspectPlanSub(PageData pd) {
        inspectionPlanMapper.delInspectPlanSub(pd);
    }

    @Override
    public void delInspectPlanUser(PageData pd) {
        inspectionPlanMapper.delInspectPlanUser(pd);
    }

    @Override
    public void abolishInspectMain(PageData pd) {
        inspectionPlanMapper.abolishInspectMain(pd);
    }

    @Override
    public void abolishInspectSub(PageData pd) {
        inspectionPlanMapper.abolishInspectSub(pd);
    }

    @Override
    public void abolishInspectTask(PageData pd) {
        inspectionPlanMapper.abolishInspectTask(pd);
    }

    @Override
    public void delUser(PageData pd) {
        inspectionPlanMapper.delUser(pd);

    }

    @Override
    public void saveInspectUser(PageData pd) {
        inspectionPlanMapper.saveInspectUser(pd);
    }

    @Override
    public void DelInspectUser(PageData pd) {
        inspectionPlanMapper.DelInspectUser(pd);
    }

    @Override
    public int isAddPerson(PageData pd) {
        return inspectionPlanMapper.isAddPerson(pd);
    }

    @Override
    public void editInspect(PageData pd) {
        inspectionPlanMapper.editInspect(pd);
    }

    @Override
    public void editInspected(PageData pd) {

        inspectionPlanMapper.editInspected(pd);
    }

    @Override
    public void editUserDuties(PageData pd) {
        inspectionPlanMapper.editUserDuties(pd);
    }

    @Override
    public Map<String, Object> getGuokuById(PageData pd) {
        return inspectionPlanMapper.getGuokuById(pd);
    }
}
