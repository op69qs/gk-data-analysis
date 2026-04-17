// InspectionTaskReminderController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionReminderService;
import org.inspect.service.InspectionTaskService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/14.
 */
@Slf4j
@RestController
@Api(tags = "检查任务提醒控制类")
@RequestMapping(value = "/inspectionReminder", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionReminderController extends BaseController {

    /*是否发布（0:未发布,1:已发布）*/
    private static final String PUBLISHED = "1";
    private static final String UNPUBLISHED = "0";

    /*提醒类型(1立即提醒，2定时提醒)*/
    private static final String IMMEDIATELY = "1";
    private static final String TIMER = "2";

    @Autowired
    private InspectionReminderService inspectionReminderService;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    /**
     * 添加立即提醒信息
     *
     * @param param 包含立即提醒标题、国库、内容、任务、发布人等
     * @return 返回成功success、失败false，msg失败信息
     */
    @RequestMapping(value = "/addImmediatelyReminder", method = RequestMethod.POST)
    @ApiOperation(value = "添加立即提醒信息")
    public Map<String, String> addImmediatelyReminder(
            @ApiParam(value = "参数包含：" +
                    "任务ID TASK_ID，" +
                    "任务名称 TASK_NAME，" +
                    "接收国库编码 RECEIVE_ORG_ID， " +
                    "接收机构名称 RECEIVE_ORG_DSCR，" +
                    "提醒标题 REMINDER_TITLE， " +
                    "提醒内容 REMINDER_CONTENT，" +
                    "新增人ID ADD_USERID, " +
                    "发布机构 PUBLISH_ORG, " +
                    "发布人 PUBLISH_USERID")
            @RequestBody(required = false) JSONObject param
    ) {
        String uuid = UuidUtil.get32UUID();
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("REMINDER_ID", uuid);
        pd.put("REMINDER_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
        pd.put("REMINDER_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS));
        pd.put("REMINDER_TYPE", IMMEDIATELY);
        /*发布状态设置为未发布，通过后续调用的存储过程修改此状态*/
        pd.put("IS_PUBLISH", UNPUBLISHED);
        pd.put("IS_DEL", "1");
        pd.put("PUBLISH_DATE", null);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        try {
            inspectionReminderService.addReminderInfo(pd);
            /*调用同步存储过程*/
            Map<String, String> proParam = new HashMap<String, String>();
            proParam.put("REMINDER_TYPE", IMMEDIATELY);
            proParam.put("PUBLISH", PUBLISHED);
            inspectionReminderService.callReminderSyncProcedure(proParam);
            res.put("result", "success");
            res.put("msg", "立即提醒发布成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "立即提醒发布失败");
        }
        return res;
    }

    @RequestMapping(value = "/addTimerReminder", method = RequestMethod.POST)
    @ApiOperation(value = "添加定时提醒信息")
    public Map<String, String> addTimerReminder(
            @ApiParam(value =
                    "任务ID   TASK_ID，       " +
                    "任务名称 TASK_NAME，       " +
                    "接收国库编码 RECEIVE_ORG_ID， " +
                    "接收机构名称 RECEIVE_ORG_DSCR，" +
                    "提醒标题 REMINDER_TITLE， " +
                    "提醒内容 REMINDER_CONTENT，" +
                    "新增人ID ADD_USERID,          " +
                    "发布机构 PUBLISH_ORG, " +
                    "提醒日期 REMINDER_DATE，  " +
                    "提醒时间 REMINDER_TIME，   " +
                    "发布人 PUBLISH_USERID，")
            @RequestBody(required = false) JSONObject param
    ) {
        String uuid = UuidUtil.get32UUID();
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("REMINDER_ID", uuid);
        pd.put("REMINDER_TYPE", TIMER);
        /*发布状态设置为未发布，通过数据库定时任务调用的存储过程修改此状态*/
        pd.put("IS_PUBLISH", UNPUBLISHED);
        pd.put("IS_DEL", "1");
        pd.put("PUBLISH_DATE", null);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        try {
            inspectionReminderService.addReminderInfo(pd);
            /*调用同步存储过程*/
            Map<String, String> proParam = new HashMap<String, String>();
            proParam.put("REMINDER_TYPE", TIMER);
            proParam.put("PUBLISH", UNPUBLISHED);
            inspectionReminderService.callReminderSyncProcedure(proParam);
            res.put("result", "success");
            res.put("msg", "定时提醒发布成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "定时提醒发布失败");
        }
        return res;
    }

    /**
     * 添加计划立即提醒信息
     *
     * @param param 包含立即提醒标题、国库、内容、任务、发布人等
     * @return 返回成功success、失败false，msg失败信息
     */
    @RequestMapping(value = "/addPlanImmediatelyReminder", method = RequestMethod.POST)
    @ApiOperation(value = "添加计划立即提醒信息")
    public Map<String, String> addPlanImmediatelyReminder(
            @ApiParam(value = "参数包含：" +
                    "计划ID PLAN_ID，" +
                    "计划名称 PLAN_NAME，" +
                    "提醒标题 REMINDER_TITLE， " +
                    "提醒内容 REMINDER_CONTENT，" +
                    "新增人ID ADD_USERID, " +
                    "发布机构 PUBLISH_ORG, " +
                    "发布人 PUBLISH_USERID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("INSPECTION_PLAN_ID", pd.get("PLAN_ID"));
        List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(pd);
        for( Map<String, Object> taskData : taskDataList ){
            String uuid = UuidUtil.get32UUID();
            pd.put("TASK_ID", taskData.get("INSPECTION_TASK_ID"));
            pd.put("TASK_NAME", taskData.get("INSPECTION_TASK_NAME"));
            pd.put("RECEIVE_ORG_ID", taskData.get("INSPECTED_GUOKU_ID"));
            pd.put("RECEIVE_ORG_DSCR", taskData.get("INSPECTED_GUOKU_DSCR"));
            pd.put("REMINDER_ID", uuid);
            pd.put("REMINDER_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD));
            pd.put("REMINDER_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS));
            pd.put("REMINDER_TYPE", IMMEDIATELY);
            /*发布状态设置为未发布，通过后续调用的存储过程修改此状态*/
            pd.put("IS_PUBLISH", UNPUBLISHED);
            pd.put("IS_DEL", "1");
            pd.put("PUBLISH_DATE", null);
            pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            try {
                inspectionReminderService.addReminderInfo(pd);
            } catch (Exception e) {
                res.put("result", "false");
                res.put("msg", "立即提醒发布失败，信息新增失败");
            }
        }
        try {
            /*调用同步存储过程*/
            Map<String, String> proParam = new HashMap<String, String>();
            proParam.put("REMINDER_TYPE", IMMEDIATELY);
            proParam.put("PUBLISH", PUBLISHED);
            inspectionReminderService.callReminderSyncProcedure(proParam);
            res.put("result", "success");
            res.put("msg", "立即提醒发布成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "立即提醒发布失败，信息同步失败");
        }
        return res;
    }

    @RequestMapping(value = "/addPlanTimerReminder", method = RequestMethod.POST)
    @ApiOperation(value = "添加计划定时提醒信息")
    public Map<String, String> addPlanTimerReminder(
            @ApiParam(value =
                    "任务ID   PLAN_ID，       " +
                            "任务名称 PLAN_NAME，       " +
                            "提醒标题 REMINDER_TITLE， " +
                            "提醒内容 REMINDER_CONTENT，" +
                            "新增人ID ADD_USERID,          " +
                            "发布机构 PUBLISH_ORG, " +
                            "提醒日期 REMINDER_DATE，  " +
                            "提醒时间 REMINDER_TIME，   " +
                            "发布人 PUBLISH_USERID，")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("INSPECTION_PLAN_ID", pd.get("PLAN_ID"));
        List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(pd);
        for( Map<String, Object> taskData : taskDataList ) {
            String uuid = UuidUtil.get32UUID();
            pd.put("TASK_ID", taskData.get("INSPECTION_TASK_ID"));
            pd.put("TASK_NAME", taskData.get("INSPECTION_TASK_NAME"));
            pd.put("RECEIVE_ORG_ID", taskData.get("INSPECTED_GUOKU_ID"));
            pd.put("RECEIVE_ORG_DSCR", taskData.get("INSPECTED_GUOKU_DSCR"));
            pd.put("REMINDER_ID", uuid);
            pd.put("REMINDER_TYPE", TIMER);
            /*发布状态设置为未发布，通过数据库定时任务调用的存储过程修改此状态*/
            pd.put("IS_PUBLISH", UNPUBLISHED);
            pd.put("IS_DEL", "1");
            pd.put("PUBLISH_DATE", null);
            pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            try {
                inspectionReminderService.addReminderInfo(pd);
            } catch (Exception e) {
                res.put("result", "false");
                res.put("msg", "定时提醒发布失败，信息新增失败");
            }
        }
        try {
            /*调用同步存储过程*/
            Map<String, String> proParam = new HashMap<String, String>();
            proParam.put("REMINDER_TYPE", TIMER);
            proParam.put("PUBLISH", UNPUBLISHED);
            inspectionReminderService.callReminderSyncProcedure(proParam);
            res.put("result", "success");
            res.put("msg", "定时提醒发布成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "定时提醒发布失败，信息同步失败");
        }
        return res;
    }


} ///:~
