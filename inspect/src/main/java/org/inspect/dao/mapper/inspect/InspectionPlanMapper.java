package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dj on 2019/9/6
 */
@Repository
public interface InspectionPlanMapper {

    /*获取检查计划名称*/
    List<Map<String, Object>> getCurPlanName(@Param("params") PageData pd);

    /*查询当前计划问题台账中的一级问题分类*/
    List<Map<String, Object>> getCurTaskQuestion_1(@Param("params") PageData pd);

    /*查询计划包含任务的汇总表信息*/
    List<Map<String, Object>> getStatisticsTableByPlanId(@Param("params") PageData pd);

    /*获取检查计划主查询数据*/
    List<Map<String, Object>> getInspectionPlanData(@Param("params") PageData pd);
    List<Map<String, Object>> getTalentPoolAll(@Param("params") PageData pd);
    List<Map<String, Object>> getTalentPool(@Param("params") PageData pd);
    List<Map<String, Object>> getInspectionPlanInspected(@Param("params") PageData pd);
    List<Map<String, Object>> getInspectionProcess(@Param("params") PageData pd);
    List<Map<String, Object>> isEditUserDuties(@Param("params") PageData pd);
    List<Map<String, Object>> checkRepeat(@Param("params") PageData pd);

    /*获取检查计划主查询数据 计数*/
    int getInspectionPlanDataCount(@Param("params") PageData pd);

//    保存检查计划
    void saveInspectPlan(@Param("params") PageData pd);
    void saveInspectProcess(@Param("params") PageData pd);
    void saveInspectPlanSub(@Param("params") PageData pd);
    void saveInspectPlanGroup(@Param("params") PageData pd);
    void delInspectPlanMain(@Param("params") Map<String, Object> params);
    void delInspectPlanSub(@Param("params") Map<String, Object> params);
    void delInspectPlanUser(@Param("params") Map<String, Object> params);
    void abolishInspectMain(@Param("params") Map<String, Object> params);
    void abolishInspectSub(@Param("params") Map<String, Object> params);
    void abolishInspectTask(@Param("params") Map<String, Object> params);

    void editInspect(@Param("params") PageData pd);
    void editInspected(@Param("params") PageData pd);
    void editUserDuties(@Param("params") PageData pd);
    void delUser(@Param("params") PageData pd);
    void saveInspectUser(@Param("params") PageData pd);
    void DelInspectUser(@Param("params") PageData pd);
    int  isAddPerson(@Param("params") PageData pd);

    Map<String,Object>getGuokuById(@Param("params") PageData pd);

}
