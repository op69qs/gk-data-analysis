package org.inspect.service;


import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * Created by dj on 2019/9/6
 */
//@Service
public interface InspectionPlanService {

    /*获取检查计划名称*/
    List<Map<String, Object>> getCurPlanName(PageData pd);

    /*查询当前计划问题台账中的一级问题分类*/
    List<Map<String, Object>> getCurTaskQuestion_1(PageData pd);

    /*查询计划包含任务的汇总表信息*/
    List<Map<String, Object>> getStatisticsTableByPlanId(PageData pd);

    /*获取检查计划主查询数据*/
    List<Map<String, Object>> getInspectionPlanData(PageData pd);
    List<Map<String, Object>> getTalentPool(PageData pd);
    List<Map<String, Object>> getTalentPoolAll(PageData pd);
    List<Map<String, Object>> isEditUserDuties(PageData pd);
    List<Map<String, Object>> checkRepeat(PageData pd);
    List<Map<String, Object>> getInspectionPlanInspected(PageData pd);
    List<Map<String, Object>> getInspectionProcess(PageData pd);

    /*获取检查计划主查询数据 计数*/
    int getInspectionPlanDataCount(PageData pd);

//    保存检查计划
    void saveInspectPlan(PageData pd);
    void saveInspectPlanSub(PageData pd);
    void saveInspectPlanGroup(PageData pd);
    void saveInspectProcess(PageData pd);
    void delInspectPlanMain(PageData pd);
    void delInspectPlanSub(PageData pd);
    void delInspectPlanUser(PageData pd);

    void abolishInspectMain(PageData pd);
    void abolishInspectSub(PageData pd);
    void abolishInspectTask(PageData pd);
    void delUser(PageData pd);
    void saveInspectUser(PageData pd);
    void DelInspectUser(PageData pd);
    int  isAddPerson(PageData pd);
    void editInspect(PageData pd);
    void editInspected(PageData pd);
    void editUserDuties(PageData pd);

    Map<String,Object>getGuokuById(PageData pd);

}
