// InspectionSelfLedgerController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自查问题台账
 *
 * @author Created by Samer on 2019/10/17.
 */
@Slf4j
@RestController
@Api(tags = "自查问题台账")
@RequestMapping(value = "/inspectionSelfLedger", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionSelfLedgerController extends BaseController {

    @Autowired
    private InspectionSelfLedgerService inspectionSelfLedgerService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionReformService inspectionReformService;

    @Autowired
    private QuestionRuleService questionRuleService;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    /**
     * 根据台账ID删除一对多
     *
     * @param param "问题台账ID数组：ledgerIDArr"
     * @return res
     */
    @ApiOperation(value = "根据台账ID删除一对多")
    @PostMapping(value = "/delSelfLedgerOneToMany")
    public Map<String, String> delSelfLedgerOneToMany(
            @ApiParam(value = "问题台账ID数组：ledgerIDArr")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("tableType", "selfLedger");

        //问题台账ID列表
        List<String> ledgerIDList = (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        pd.put("LEDGER_ID", ledgerIDList.get(0));
        List<Map<String, String>> dataList = inspectionSelfLedgerService.getSelfLedgerByLedgerID(pd);
        pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

        try {
            //删除原始信息
            ledgerIDList.forEach(ledgerId -> {
                pd.put("LEDGER_ID", ledgerId);
                //整改台账同步
                pd.put("type", "delete");
                inspectionReformService.updateReform(pd);
                //删除记录
                inspectionSelfLedgerService.delSelfLedgerByLedgerId(pd);
                inspectionSelfLedgerService.delQuestionLedgerRule(pd);
            });
            res.put("result", "success");
            res.put("msg", "问题台账删除成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账删除失败");
        }
        return res;
    }

    /**
     * 根据问题台账ID编辑一对多
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "根据问题台账ID编辑一对多")
    @PostMapping(value = "/editSelfLedgerOneToMany")
    public Map<String, String> editSelfLedgerOneToMany(
            @ApiParam(value =
                    "问题台账ID数组：ledgerIDArr,\n" +
                            "当前流程ID：PROC_ID,\n" +
                            "当前问题分类数组：questionArr:[{" +
                            "   当前问题path：path,\n" +
                            "   当前问题分类末级ID：QUESTION_ID,\n" +
                            "   当前问题分类末级描述：QUESTION_DSCR\n" +
                            "}]" +
                            "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("tableType", "selfLedger");

        String COUNTY = "",
                CITY = "",
                PROVINCE = "";
        PageData pd_sub = new PageData();
        Map<String, String> taskData = inspectionSelfLedgerService.getTaskInfoById(pd);
        pd.put("guoku_id", taskData.get("INSPECTED_GUOKU_ID"));
        Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(pd);
        String level = String.valueOf(guokuData.get("level"));
        pd.put("COUNTY_STATE", "1");
        pd.put("CITY_STATE", "1");
        pd.put("PROVINCE_STATE", "1");
        if ("4".equals(level) || "5".equals(level)) {
            COUNTY = guokuData.get("guoku_id");
            CITY = guokuData.get("guoku_pid");
            pd.put("guoku_id", guokuData.get("guoku_pid"));
            Map<String, String> guokuData_1 = inspectionSelfLedgerService.getGuokuInfo(pd);
            PROVINCE = guokuData_1.get("guoku_pid");
        }
        if ("3".equals(level)) {
            pd.put("COUNTY_STATE", "2");
            CITY = guokuData.get("guoku_id");
            PROVINCE = guokuData.get("guoku_pid");
        }
        if ("2".equals(level)) {
            pd.put("COUNTY_STATE", "2");
            pd.put("CITY_STATE", "2");
            PROVINCE = guokuData.get("guoku_id");
        }
        pd.put("COUNTY", COUNTY);
        pd.put("CITY", CITY);
        pd.put("PROVINCE", PROVINCE);
        pd.put("IS_LOCK", "1");

        //问题台账ID列表
        List<String> ledgerIDList = (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        pd.put("LEDGER_ID", ledgerIDList.get(0));
        List<Map<String, String>> dataList = inspectionSelfLedgerService.getSelfLedgerByLedgerID(pd);
        pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));

        try {

            if (null != questionArr && !questionArr.isEmpty()) {
                //删除原始信息
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //整改台账同步
                    pd.put("type", "delete");
                    inspectionReformService.updateReform(pd);
                    //删除记录
                    inspectionSelfLedgerService.delSelfLedgerByLedgerId(pd);
                    inspectionSelfLedgerService.delQuestionLedgerRule(pd);
                });
                addLedgerOneToMany(questionArr, pd, pd_sub);
            } else {
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //整改台账同步
                    pd.put("type", "delete");
                    inspectionReformService.updateReform(pd);
                    pd.put("type", "add");
                    inspectionReformService.updateReform(pd);
                    inspectionSelfLedgerService.editSelfLedgerByLedgerID(pd);
                });
            }

            res.put("result", "success");
            res.put("msg", "问题台账编辑成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账编辑失败");
        }
        return res;
    }

    /**
     * 问题台账新增一对多
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "问题台账新增一对多")
    @PostMapping(value = "/addSelfLedgerOneToMany")
    public Map<String, String> addSelfLedgerOneToMany(
            @ApiParam(value = "当前任务ID：TASK_ID,\n" +
                    "当前任务所处大流程ID：PROC_ID,\n" +
                    "当前任务所处小流程ID：PROC_SUB_ID,\n" +
                    "当前问题分类数组：questionArr:[{" +
                    "   当前问题分类一级ID：QUESTION_ID_1,\n" +
                    "   当前问题分类一级描述：QUESTION_DSCR_1,\n" +
                    "   当前问题分类二级ID：QUESTION_ID_2,\n" +
                    "   当前问题分类二级描述：QUESTION_DSCR_2,\n" +
                    "   当前问题分类三级ID：QUESTION_ID_3,\n" +
                    "   当前问题分类三级描述：QUESTION_DSCR_3,\n" +
                    "   当前问题分类末级ID：QUESTION_ID,\n" +
                    "   当前问题分类末级描述：QUESTION_DSCR\n" +
                    "}]" +
                    "新增人：ADD_USERID,\n" +
                    "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {


        Map<String, String> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        pd.put("tableType", "selfLedger");
        String COUNTY = "",
                CITY = "",
                PROVINCE = "";
        PageData pd_sub = new PageData();
        Map<String, String> taskData = inspectionSelfLedgerService.getTaskInfoById(pd);
        pd.put("guoku_id", taskData.get("INSPECTED_GUOKU_ID"));
        Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(pd);
        String level = String.valueOf(guokuData.get("level"));
        pd.put("COUNTY_STATE", "1");
        pd.put("CITY_STATE", "1");
        pd.put("PROVINCE_STATE", "1");
        if ("4".equals(level) || "5".equals(level)) {
            COUNTY = guokuData.get("guoku_id");
            CITY = guokuData.get("guoku_pid");
            pd.put("guoku_id", guokuData.get("guoku_pid"));
            Map<String, String> guokuData_1 = inspectionSelfLedgerService.getGuokuInfo(pd);
            PROVINCE = guokuData_1.get("guoku_pid");
        }
        if ("3".equals(level)) {
            pd.put("COUNTY_STATE", "2");
            CITY = guokuData.get("guoku_id");
            PROVINCE = guokuData.get("guoku_pid");
        }
        if ("2".equals(level)) {
            pd.put("COUNTY_STATE", "2");
            pd.put("CITY_STATE", "2");
            PROVINCE = guokuData.get("guoku_id");
        }
        pd.put("COUNTY", COUNTY);
        pd.put("CITY", CITY);
        pd.put("PROVINCE", PROVINCE);
        pd.put("IS_LOCK", "1");

        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));

        try {

            addLedgerOneToMany(questionArr, pd, pd_sub);

            //当前问题台账流程完成，激活当前现场检查所有后续流程
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            inspectionProcessControlService.activateFollowProcessSub(pd);
             /*标记当前主流程结束*/
            inspectionProcessControlService.finishCurProcessById(pd);
             /*激活后续主流程*/
            inspectionProcessControlService.activateFollowProcess(pd);
            /*激活指定流程*/
            pd.put("PROC_SUB_ID", "0080401");
            inspectionProcessControlService.activateSpecifyProcessSub(pd);
            String procCode = pd.getString("PROC_ID").substring(0, 3);
            pd.put("procCode", procCode);

            res.put("result", "success");
            res.put("msg", "问题台账新增成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账新增失败");
        }
        return res;
    }

    /*一对多问题台账新增*/
    private void addLedgerOneToMany(List<Map<String, String>> questionArr, PageData pd, PageData pd_sub) throws Exception {
        PageData subPg = new PageData();
        questionArr.forEach(mapEle -> {
            /*pd.put("QUESTION_ID_1", mapEle.get("QUESTION_ID_1"));
            pd.put("QUESTION_DSCR_1", mapEle.get("QUESTION_DSCR_1"));
            pd.put("QUESTION_ID_2", mapEle.get("QUESTION_ID_2"));
            pd.put("QUESTION_DSCR_2", mapEle.get("QUESTION_DSCR_2"));
            pd.put("QUESTION_ID_3", mapEle.get("QUESTION_ID_3"));
            pd.put("QUESTION_DSCR_3", mapEle.get("QUESTION_DSCR_3"));*/

            String path = String.valueOf(mapEle.get("path"));
            String[] idArr = path.split(",");
            subPg.put("QUESTION_ID", idArr[0]);
            String QUESTION_DSCR_1 = String.valueOf(((Map<String, Object>)(inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPg)).get(0)).get("name"));
            subPg.put("QUESTION_ID", idArr[1]);
            String QUESTION_DSCR_2 = String.valueOf(((Map<String, Object>)(inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPg)).get(0)).get("name"));
            subPg.put("QUESTION_ID", idArr[2]);
            String QUESTION_DSCR_3 = String.valueOf(((Map<String, Object>)(inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPg)).get(0)).get("name"));
            pd.put("QUESTION_ID_1", idArr[0]);
            pd.put("QUESTION_DSCR_1", QUESTION_DSCR_1);
            pd.put("QUESTION_ID_2", idArr[1]);
            pd.put("QUESTION_DSCR_2", QUESTION_DSCR_2);
            pd.put("QUESTION_ID_3", idArr[2]);
            pd.put("QUESTION_DSCR_3", QUESTION_DSCR_3);

            pd.put("QUESTION_ID", mapEle.get("QUESTION_ID"));
            pd.put("QUESTION_DSCR", mapEle.get("QUESTION_DSCR"));
            pd.put("LEDGER_ID", UuidUtil.get32UUID());
            pd.put("PATH", path);
            pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));

            inspectionSelfLedgerService.addSelfLedger(pd);

            pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
            List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
               /*新增问题台账制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionSelfLedgerService.addQuestionLedgerRule(pd);
            }

            //整改台账同步
            pd.put("type", "add");
            inspectionReformService.updateReform(pd);

        });
    }


    /**
     * 根据用户ID任务ID获取问题台账
     *
     * @param param params.TASK_ID 当前检查任务ID
     *              params.ADD_USERID 添加人ID
     * @return dataList
     */
    @ApiOperation(value = "根据用户ID任务ID获取问题台账")
    @PostMapping(value = "/getSelfLedgerByUserIdTaskID")
    public Map<String, Object> getSelfLedgerByUserIdTaskID(
            @ApiParam(value = "TASK_ID：当前检查任务ID\n" +
                    "ADD_USERID：添加人ID" +
                    "QUERY_TYPE：查询类型（详情1 编辑2）")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionSelfLedgerService.getSelfLedgerByUserIdTaskID(pd);
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 根据检查任务ID获取该台账添加人
     *
     * @param param params.TASK_ID 检查任务ID
     * @return dataList
     */
    @ApiOperation(value = "根据检查任务ID获取该台账添加人")
    @PostMapping(value = "/getLedgerAddUserByTaskId")
    public Map<String, Object> getLedgerAddUserByTaskId(
            @ApiParam(value = "检查任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionSelfLedgerService.getLedgerAddUserByTaskId(pd);
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 根据台账ID获取台账信息
     *
     * @param param 问题台账ID：LEDGER_ID
     * @return dataList
     */
    @ApiOperation(value = "根据台账ID获取台账信息")
    @PostMapping(value = "/getQuestionLedgerByLedgerId")
    public Map<String, Object> getQuestionLedgerByLedgerId(
            @ApiParam(value = "问题台账ID：LEDGER_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionSelfLedgerService.getSelfLedgerByLedgerID(pd);
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 问题台账新增
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "问题台账新增")
    @PostMapping(value = "/addSelfLedger")
    public Map<String, String> addSelfLedger(
            @ApiParam(value = "当前任务ID：TASK_ID,\n" +
                    "当前任务所处大流程ID：PROC_ID,\n" +
                    "当前任务所处小流程ID：PROC_SUB_ID,\n" +
                    "当前问题分类一级ID：QUESTION_ID_1,\n" +
                    "当前问题分类一级描述：QUESTION_DSCR_1,\n" +
                    "当前问题分类二级ID：QUESTION_ID_2,\n" +
                    "当前问题分类二级描述：QUESTION_DSCR_2,\n" +
                    "当前问题分类三级ID：QUESTION_ID_3,\n" +
                    "当前问题分类三级描述：QUESTION_DSCR_3,\n" +
                    "当前问题分类末级ID：QUESTION_ID,\n" +
                    "当前问题分类末级描述：QUESTION_DSCR,\n" +
                    "新增人：ADD_USERID,\n" +
                    "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {
        String COUNTY = "",
                CITY = "",
                PROVINCE = "";
        String uuid = UuidUtil.get32UUID();
        Map<String, String> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        pd.put("LEDGER_ID", uuid);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        Map<String, String> taskData = inspectionSelfLedgerService.getTaskInfoById(pd);
        pd.put("guoku_id", taskData.get("INSPECTED_GUOKU_ID"));
        Map<String, String> guokuData = inspectionSelfLedgerService.getGuokuInfo(pd);
        pd.put("COUNTY_STATE", "1");
        pd.put("CITY_STATE", "1");
        pd.put("PROVINCE_STATE", "1");
        if ("4".equals(String.valueOf(guokuData.get("level"))) || "5".equals(String.valueOf(guokuData.get("level")))) {
            COUNTY = guokuData.get("guoku_id");
            CITY = guokuData.get("guoku_pid");
            pd.put("guoku_id", guokuData.get("guoku_pid"));
            Map<String, String> guokuData_1 = inspectionSelfLedgerService.getGuokuInfo(pd);
            PROVINCE = guokuData_1.get("guoku_pid");
        }
        if ("3".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("COUNTY_STATE", "2");
            CITY = guokuData.get("guoku_id");
            PROVINCE = guokuData.get("guoku_pid");
        }
        if ("2".equals(String.valueOf(guokuData.get("level")))) {
            pd.put("COUNTY_STATE", "2");
            pd.put("CITY_STATE", "2");
            PROVINCE = guokuData.get("guoku_id");
        }
        pd.put("COUNTY", COUNTY);
        pd.put("CITY", CITY);
        pd.put("PROVINCE", PROVINCE);
        pd.put("IS_LOCK", "1");
        PageData pd_sub = new PageData();
        pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
        List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
        try {

            inspectionSelfLedgerService.addSelfLedger(pd);
            /*新增问题台账制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionSelfLedgerService.addQuestionLedgerRule(pd);
            }

            //当前问题台账流程完成，激活当前现场检查所有后续流程
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            inspectionProcessControlService.activateFollowProcessSub(pd);
             /*标记当前主流程结束*/
            inspectionProcessControlService.finishCurProcessById(pd);
             /*激活后续主流程*/
            inspectionProcessControlService.activateFollowProcess(pd);
            /*激活指定流程*/
            pd.put("PROC_SUB_ID", "0080401");
            inspectionProcessControlService.activateSpecifyProcessSub(pd);
            String procCode = pd.getString("PROC_ID").substring(0, 3);
            pd.put("procCode", procCode);
            //整改台账同步
            pd.put("type", "add");
            inspectionReformService.updateReform(pd);
            res.put("result", "success");
            res.put("msg", "问题台账新增成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账新增失败");
        }
        return res;
    }

    /**
     * 根据问题台账ID编辑
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "根据问题台账ID编辑")
    @PostMapping(value = "/editSelfLedgerByLedgerID")
    public Map<String, String> editSelfLedgerByLedgerID(
            @ApiParam(value = "前台账ID：LEDGER_ID,\n" +
                    "当前流程ID：PROC_ID,\n" +
                    "当前问题分类一级ID：QUESTION_ID_1,\n" +
                    "当前问题分类一级描述：QUESTION_DSCR_1,\n" +
                    "当前问题分类二级ID：QUESTION_ID_2,\n" +
                    "当前问题分类二级描述：QUESTION_DSCR_2,\n" +
                    "当前问题分类三级ID：QUESTION_ID_3,\n" +
                    "当前问题分类三级描述：QUESTION_DSCR_3,\n" +
                    "当前问题分类末级ID：QUESTION_ID,\n" +
                    "当前问题分类末级描述：QUESTION_DSCR,\n" +
                    "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        pd.put("MODIFY_DATE", dateNow);
        PageData pd_sub = new PageData();
        pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
        List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
        try {
            inspectionSelfLedgerService.editSelfLedgerByLedgerID(pd);
            /*修改问题台账制度依据*/
            inspectionSelfLedgerService.delQuestionLedgerRule(pd);
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionSelfLedgerService.addQuestionLedgerRule(pd);
            }

            res.put("result", "success");
            res.put("msg", "问题台账编辑成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账编辑失败");
        }
        return res;
    }

    /**
     * 根据台账ID删除
     *
     * @param param "问题台账ID：LEDGER_ID"
     * @return res
     */
    @ApiOperation(value = "根据台账ID删除")
    @PostMapping(value = "/delSelfLedgerByLedgerId")
    public Map<String, String> delSelfLedgerByLedgerId(
            @ApiParam(value = "问题台账ID：LEDGER_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        try {
            List<Map<String, String>> dataList = inspectionSelfLedgerService.getSelfLedgerByLedgerID(pd);
            inspectionSelfLedgerService.delSelfLedgerByLedgerId(pd);
            pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));
            //整改台账同步
            pd.put("type", "delete");
            inspectionReformService.updateReform(pd);
            res.put("result", "success");
            res.put("msg", "问题台账删除成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账删除失败");
        }
        return res;
    }

} ///:~
