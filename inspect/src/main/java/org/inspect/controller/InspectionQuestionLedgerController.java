// inspectionQuestionLedger.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.model.QueTreeNode;
import org.inspect.service.*;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Created by Samer on 2019/10/17.
 */
@Slf4j
@RestController
@Api(tags = "问题台账控制类")
@RequestMapping(value = "/inspectionQuestionLedger", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionQuestionLedgerController extends BaseController {

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionReformService inspectionReformService;

    @Autowired
    private InspectionStatisticsTableService inspectionStatisticsTableService;

    @Autowired
    private QuestionRuleService questionRuleService;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private InspectionPostSVListService inspectionPostSVListService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据台账ID删除一对多
     *
     * @param param "问题台账ID数组：ledgerIDArr"
     * @return res
     */
    @ApiOperation(value = "根据台账ID删除一对多")
    @PostMapping(value = "/delQuestionLedgerOneToMany")
    public Map<String, String> delQuestionLedgerOneToMany(
            @ApiParam(value = "问题台账ID数组：ledgerIDArr," +
                    "当前流程ID：PROC_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        pd.put("tableType", "questionLedger");

        //问题台账ID列表
        List<String> ledgerIDList = (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        pd.put("LEDGER_ID", ledgerIDList.get(0));
        List<Map<String, String>> dataList = inspectionQuestionLedgerService.getQuestionLedgerByLedgerID(pd);
        pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

        try {

            //删除原始信息
            ledgerIDList.forEach(ledgerId -> {
                pd.put("LEDGER_ID", ledgerId);
                //整改台账同步
                if ("0".equals(pd.getString("IS_REPORT"))) {
                    pd.put("type", "delete");
                    inspectionReformService.updateReform(pd);
                }
                //删除原记录
                inspectionQuestionLedgerService.delQuestionLedgerByLedgerId(pd);
                inspectionQuestionLedgerService.delQuestionLedgerRule(pd);
            });

           /*统计汇总表信息同步*/
            String procCode = pd.getString("PROC_ID").substring(0, 3);
            pd.put("procCode", procCode);
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
            }
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
    @PostMapping(value = "/editQuestionLedgerOneToMany")
    public Map<String, String> editQuestionLedgerOneToMany(
            @ApiParam(value =
                    "问题台账ID数组：ledgerIDArr,\n" +
                            "当前流程ID：PROC_ID,\n" +
                            "当前问题分类数组：questionArr:[{" +
                            "   当前问题path：path,\n" +
                            "   当前问题分类末级ID：QUESTION_ID,\n" +
                            "   当前问题分类末级描述：QUESTION_DSCR\n" +
                            "}]" +
                            "是否记入清单：IS_LIST,\n" +
                            "是否记入检查报告：IS_REPORT,\n" +
                            "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        PageData pd_sub = new PageData();
        String procCode = pd.getString("PROC_ID").substring(0, 3);
        pd.put("procCode", procCode);
        pd.put("tableType", "questionLedger");

        //问题台账ID列表
        List<String> ledgerIDList = (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        //问题分类对象列表
        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));
        pd.put("LEDGER_ID", ledgerIDList.get(0));
        List<Map<String, String>> dataList = inspectionQuestionLedgerService.getQuestionLedgerByLedgerID(pd);
        pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

        try {

            if (null != questionArr && !questionArr.isEmpty()) {
                //删除原始信息
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //整改台账同步
                    if ("0".equals(pd.getString("IS_REPORT"))) {
                        pd.put("type", "delete");
                        inspectionReformService.updateReform(pd);
                    }
                    //删除原记录
                    inspectionQuestionLedgerService.delQuestionLedgerByLedgerId(pd);
                    inspectionQuestionLedgerService.delQuestionLedgerRule(pd);
                });
                //台账信息新增
                addLedgerOneToMany(questionArr, pd, pd_sub);
            } else {
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //整改台账同步
                    if ("0".equals(pd.getString("IS_REPORT"))) {
                        pd.put("type", "delete");
                        inspectionReformService.updateReform(pd);
                        pd.put("type", "add");
                        inspectionReformService.updateReform(pd);
                    }
                    inspectionQuestionLedgerService.editQuestionLedgerByLedgerID(pd);
                });
            }

            /*统计汇总表信息同步*/
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
            }

            /*统计汇总表信息同步*/
            pd.put("procCode", procCode);
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
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
     * 问题台账问题描述与问题分类一对多新增
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "问题台账问题描述与问题分类一对多新增")
    @PostMapping(value = "/addQuestionLedgerOneToMany")
    public Map<String, String> addQuestionLedgerOneToMany(
            @ApiParam(value = "当前任务ID：TASK_ID,\n" +
                    "当前任务所处大流程ID：PROC_ID,\n" +
                    "当前任务所处小流程ID：PROC_SUB_ID,\n" +
                    "当前问题分类数组：questionArr:[{" +
                    "   当前问题path：path,\n" +
                    "   当前问题分类末级ID：QUESTION_ID,\n" +
                    "   当前问题分类末级描述：QUESTION_DSCR\n" +
                    "}]" +
                    "是否记入清单：IS_LIST,\n" +
                    "是否记入检查报告：IS_REPORT,\n" +
                    "新增人：ADD_USERID,\n" +
                    "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {

        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        PageData pd_sub = new PageData();
        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));
        String procCode = pd.getString("PROC_ID").substring(0, 3);
        pd.put("procCode", procCode);
        pd.put("tableType", "questionLedger");

        try {

            addLedgerOneToMany(questionArr, pd, pd_sub);

            /*统计汇总表信息同步*/
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
            }

            res.put("msg", "问题台账新增成功");

            //当前问题台账流程完成，激活当前现场检查所有后续流程
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if ("009".equals(procCode)) {
                if (inspectionProcessControlService.activateFollowProc(pd)) {
                    /*res.put("msg", res.get("msg") + "," + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
                }
            } else {
                inspectionProcessControlService.activateFollowProcessSub(pd);
            }

            res.put("result", "success");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账新增失败");
        }
        return res;
    }

    /*问题描述问题分类一对多添加*/
    private void addLedgerOneToMany(List<Map<String, String>> questionArr, PageData pd, PageData pd_sub)
            throws Exception {
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
            String QUESTION_DSCR_1 = String.valueOf(((Map<String, Object>) (inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPg)).get(0)).get("name"));
            subPg.put("QUESTION_ID", idArr[1]);
            String QUESTION_DSCR_2 = String.valueOf(((Map<String, Object>) (inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPg)).get(0)).get("name"));
            subPg.put("QUESTION_ID", idArr[2]);
            String QUESTION_DSCR_3 = String.valueOf(((Map<String, Object>) (inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPg)).get(0)).get("name"));
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

            inspectionQuestionLedgerService.addQuestionLedger(pd);

            pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
            List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
                /*新增问题台账制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionQuestionLedgerService.addQuestionLedgerRule(pd);
            }

            //整改台账同步
            if (pd.getString("IS_REPORT").equals("0")) {
                pd.put("type", "add");
                inspectionReformService.updateReform(pd);
            }

        });
    }

    /**
     * 根据任务ID获取检查分类
     *
     * @param param params.TASK_ID 当前检查任务ID
     * @return questionType
     */
    @ApiOperation(value = "根据任务ID获取检查分类")
    @PostMapping(value = "/getQuestionTypeByTaskId")
    public Map<String, Object> getQuestionTypeByTaskId(
            @ApiParam(value = "TASK_ID：当前检查任务ID")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        String questionType = inspectionQuestionLedgerService.getQuestionTypeByTaskId(pd);
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("rows", questionType);
        return res;
    }

    /**
     * 根据用户ID任务ID获取问题台账
     *
     * @param param params.TASK_ID 当前检查任务ID
     *              params.ADD_USERID 添加人ID
     * @return dataList
     */
    @ApiOperation(value = "根据用户ID任务ID获取问题台账")
    @PostMapping(value = "/getQuestionLedgerByUserIdTaskID")
    public Map<String, Object> getQuestionLedgerByUserIdTaskID(
            @ApiParam(value = "TASK_ID：当前检查任务ID\n" +
                    "ADD_USERID：添加人ID" +
                    "QUERY_TYPE：查询类型（详情1 编辑2）")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionQuestionLedgerService.getQuestionLedgerByUserIdTaskID(pd);
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
        List<Map<String, String>> dataList = inspectionQuestionLedgerService.getLedgerAddUserByTaskId(pd);
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
        List<Map<String, String>> dataList = inspectionQuestionLedgerService.getQuestionLedgerByLedgerID(pd);
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
    @PostMapping(value = "/addQuestionLedger")
    public Map<String, String> addQuestionLedger(
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
                    "是否记入清单：IS_LIST,\n" +
                    "是否记入检查报告：IS_REPORT,\n" +
                    "新增人：ADD_USERID,\n" +
                    "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {
        String uuid = UuidUtil.get32UUID();
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        PageData pd_sub = new PageData();
        pd.put("LEDGER_ID", uuid);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
        List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
        List<Map<String, Object>> opinionList = inspectionQuestionLedgerService.getQuestionOpinionById(pd_sub);
        pd.put("QUESTION_OPINIONS", opinionList.get(0).get("QUESTION_OPINIONS"));
        String procCode = pd.getString("PROC_ID").substring(0, 3);
        try {
            inspectionQuestionLedgerService.addQuestionLedger(pd);
            /*新增问题台账制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionQuestionLedgerService.addQuestionLedgerRule(pd);
            }

            pd.put("procCode", procCode);
            /*统计汇总表信息同步*/
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
            }
            //整改台账同步
            if (pd.getString("IS_REPORT").equals("0")) {
                pd.put("type", "add");
                inspectionReformService.updateReform(pd);
            }
            res.put("msg", "问题台账新增成功");
            //当前问题台账流程完成，激活当前现场检查所有后续流程
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if ("009".equals(procCode)) {
                if (inspectionProcessControlService.activateFollowProc(pd)) {
                    /*res.put("msg", res.get("msg") + "," + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
                }
            } else {
                inspectionProcessControlService.activateFollowProcessSub(pd);
            }
            res.put("result", "success");
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
    @PostMapping(value = "/editQuestionLedgerByLedgerID")
    public Map<String, String> editQuestionLedgerByLedgerID(
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
                    "是否记入清单：IS_LIST,\n" +
                    "是否记入检查报告：IS_REPORT,\n" +
                    "问题描述：QUESTION_CONTENT")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        PageData pd_sub = new PageData();
        pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
        List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
        List<Map<String, Object>> opinionList = inspectionQuestionLedgerService.getQuestionOpinionById(pd_sub);
        pd.put("QUESTION_OPINIONS", opinionList.get(0).get("QUESTION_OPINIONS"));
        try {
            inspectionQuestionLedgerService.editQuestionLedgerByLedgerID(pd);
            /*修改问题台账制度依据*/
            inspectionQuestionLedgerService.delQuestionLedgerRule(pd);
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionQuestionLedgerService.addQuestionLedgerRule(pd);
            }
            /*统计汇总表信息同步*/
            String procCode = pd.getString("PROC_ID").substring(0, 3);
            pd.put("procCode", procCode);
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
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
    @PostMapping(value = "/delQuestionLedgerByLedgerId")
    public Map<String, String> delQuestionLedgerByLedgerId(
            @ApiParam(value = "问题台账ID：LEDGER_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        try {
            List<Map<String, String>> dataList = inspectionQuestionLedgerService.getQuestionLedgerByLedgerID(pd);
            inspectionQuestionLedgerService.delQuestionLedgerByLedgerId(pd);
            inspectionQuestionLedgerService.delQuestionLedgerRule(pd);
            pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));
            //整改台账同步
            if ("0".equals(pd.getString("IS_REPORT"))) {
                pd.put("type", "delete");
                inspectionReformService.updateReform(pd);
            }
           /*统计汇总表信息同步*/
            String procCode = pd.getString("PROC_ID").substring(0, 3);
            pd.put("procCode", procCode);
            if ("001".equals(procCode) || "005".equals(procCode) || "006".equals(procCode)) {
                inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
            }
            res.put("result", "success");
            res.put("msg", "问题台账删除成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账删除失败");
        }
        return res;
    }

    @RequestMapping("/getQuestionBankTreeForQuestionLedger")
    @ApiOperation("获取问题列表树异步缓存")
    public Map<String, Object> getQuestionBankTreeForQuestionLedger(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        PageData subPd = new PageData();
        Map<String, Object> result = new HashMap<>();
        try {
            String type = "";
            String postsv = pd.getString("postsv");
            if (!"".equals(pd.getString("TASK_ID"))
                    && pd.getString("TASK_ID") != null
                    && ("".equals(pd.getString("TASK_TYPE")) || pd.getString("TASK_TYPE") == null)
                    ) {
                if (postsv != null && "true".equals(postsv)) {
                    subPd.put("TASK_ID", pd.getString("TASK_ID"));
                    Map<String, String> taskDataList = inspectionPostSVListService.getTaskInfoByTaskId(subPd);
                    type = taskDataList.get("INSPECTION_TASK_TYPE").toString();
                } else {
                    subPd.put("INSPECTION_TASK_ID", pd.getString("TASK_ID"));
                    List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(subPd);
                    type = taskDataList.get(0).get("INSPECTION_TASK_TYPE").toString();
                }
                subPd.put("QUESTION_TYPE", type);
            }
            /*008自查*/
            if (!"".equals(pd.getString("TASK_TYPE"))
                    && pd.getString("TASK_TYPE") != null
                    && ("".equals(pd.getString("TASK_ID")) || pd.getString("TASK_ID") == null)
                    ) {
                type = pd.getString("TASK_TYPE");
                subPd.put("QUESTION_TYPE", type);
            }

            List<Map<String, Object>> data = inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPd);

            List<QueTreeNode> treeNodeList = new ArrayList<>();
            GuokuTreeUtils.getTreeList(treeNodeList, data, null, 0);
            GuokuTreeUtils.filterNOdes(treeNodeList, type, "0", "");
            String queQuery = pd.getString("queQuery");
            if (null != queQuery && !"".equals(queQuery)) {
                GuokuTreeUtils.queryTreeNodes(treeNodeList, queQuery);
            }

            //result.put("rows", treeNodeList);

            String userId = pd.getString("ADD_USERID") + "_";
            /*删除当前用户旧数据*/
            /*Runtime rt = Runtime.getRuntime();
            rt.exec("cd /home/redis/redis3/bin/");
            rt.exec("./redis-cli keys \"" + userId + "*\" | xargs ./redis-cli del");*/
            Set<String> set = redisUtil.getKeys(userId + "*");
            redisUtil.del(set);

            /*添加当前用户新数据*/
            addTreeNodeToRedis(treeNodeList, userId);

            Iterator<String> iSet = redisUtil.getKeys(userId + "top*").iterator();
            List<Object> nodeList = new ArrayList<>();
            while (iSet.hasNext()) {
                String key = iSet.next();
                nodeList.add(redisUtil.get(key));
            }
            Collections.sort(nodeList, new sortBySort());

            result.put("rows", nodeList);
            result.put("msg", "操作成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }


    @RequestMapping("/getQuestionBankTree")
    @ApiOperation("获取问题列表树")
    public Map<String, Object> getQuestionBankTree(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        PageData subPd = new PageData();
        Map<String, Object> result = new HashMap<>();
        try {
            String type = "";
            String postsv = pd.getString("postsv");
            if (!"".equals(pd.getString("TASK_ID"))
                    && pd.getString("TASK_ID") != null
                    && ("".equals(pd.getString("TASK_TYPE")) || pd.getString("TASK_TYPE") == null)
                    ) {
                if (postsv != null && "true".equals(postsv)) {
                    subPd.put("TASK_ID", pd.getString("TASK_ID"));
                    Map<String, String> taskDataList = inspectionPostSVListService.getTaskInfoByTaskId(subPd);
                    type = taskDataList.get("INSPECTION_TASK_TYPE").toString();
                } else {
                    subPd.put("INSPECTION_TASK_ID", pd.getString("TASK_ID"));
                    List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(subPd);
                    type = taskDataList.get(0).get("INSPECTION_TASK_TYPE").toString();
                }
                subPd.put("QUESTION_TYPE", type);
            }
            /*008自查*/
            if (!"".equals(pd.getString("TASK_TYPE"))
                    && pd.getString("TASK_TYPE") != null
                    && ("".equals(pd.getString("TASK_ID")) || pd.getString("TASK_ID") == null)
                    ) {
                type = pd.getString("TASK_TYPE");
                subPd.put("QUESTION_TYPE", type);
            }

            List<Map<String, Object>> data = inspectionQuestionLedgerService.getQuestionBankTreeForQuestionLedger(subPd);

            List<QueTreeNode> treeNodeList = new ArrayList<>();
            GuokuTreeUtils.getTreeList(treeNodeList, data, null, 0);
            GuokuTreeUtils.filterNOdes(treeNodeList, type, "0", "");
            String queQuery = pd.getString("queQuery");
            if (null != queQuery && !"".equals(queQuery)) {
                GuokuTreeUtils.queryTreeNodes(treeNodeList, queQuery);
            }
            result.put("rows", treeNodeList);
            result.put("msg", "操作成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping("/getCurTreNodeArr")
    @ApiOperation("获取当前节点所有下级节点")
    public Map<String, Object> getCurTreNodeArr(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = pd.getString("ADD_USERID") + "_";
            String key = userId + pd.getString("id") + "*";
            Set<String> set = redisUtil.getKeys(key);
            List<String> list = new ArrayList<>(set);
            List<Object> nodeList = new ArrayList<>();
            if (list.size() != 0) {
                for (String str : list) {
                    nodeList.add(redisUtil.get(str));
                }
            }
            Collections.sort(nodeList, new sortBySort());
            result.put("msg", "操作成功");
            result.put("rows", nodeList);
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    /*将国库节点放入redis*/
    private void addTreeNodeToRedis(List<QueTreeNode> treeNodeList, String userId) {
        for (int i = 0; i < treeNodeList.size(); i++) {
            QueTreeNode treeNode = treeNodeList.get(i);
            String key = treeNode.getPid() == null || "".equals(treeNode.getPid()) ? userId + "top" + i : userId + treeNode.getPid() + i;
            String delKey = treeNode.getPid() == null || "".equals(treeNode.getPid()) ? userId + "top*" : userId + treeNode.getPid() + "*";
            if (treeNode.getChildren() != null && treeNode.getChildren().size() > 0) {
                addTreeNodeToRedis(treeNode.getChildren(), userId);
            }
            treeNode.getChildren().clear();
            redisUtil.set(key, treeNode);
        }
    }

}

//实现Comparator接口
class sortBySort implements Comparator {

    public int compare(Object o1, Object o2) {
        String s1 = ((QueTreeNode) o1).getSort();
        s1 = s1.substring(s1.length()-3, s1.length());
        String s2 = ((QueTreeNode) o2).getSort();
        s2 = s2.substring(s2.length()-3, s2.length());
        if (Integer.parseInt(s1) > Integer.parseInt(s2))
            return 1;
        return -1;
    }

}

///:~
