// inspectionPostSVLedger.java

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
@Api(tags = "事后监督问题台账控制类")
@RequestMapping(value = "/inspectionPostSVLedger", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVLedgerController extends BaseController {

    @Autowired
    private InspectionPostSVLedgerService inspectionPostSVLedgerService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionPostSVReformService inspectionPostSVReformService;

    @Autowired
    private QuestionRuleService questionRuleService;

    @Autowired
    private InspectionPostSVListService inspectionPostSVListService;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    /**
     * 根据台账ID删除一对多
     *
     * @param param "问题台账ID数组：ledgerIDArr"
     * @return res
     */
    @ApiOperation(value = "根据台账ID删除一对多")
    @PostMapping(value = "/delQuestionLedgerOneToMany")
    public Map<String, String> delQuestionLedgerOneToMany(
            @ApiParam(value = "问题台账ID数组：ledgerIDArr")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);

        //问题台账ID列表
        List<String> ledgerIDList = (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        pd.put("LEDGER_ID", ledgerIDList.get(0));
        List<Map<String, String>> dataList = inspectionPostSVLedgerService.getPostSVLedgerByLedgerID(pd);
        pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

        try {

            //删除原始信息
            ledgerIDList.forEach(ledgerId -> {
                pd.put("LEDGER_ID", ledgerId);
                //整改台账同步
                if ("0".equals(pd.getString("IS_REPORT"))) {
                    pd.put("type", "delete");
                    inspectionPostSVReformService.updateReform(pd);
                }
                inspectionPostSVLedgerService.delPostSVLedgerByLedgerId(pd);
                inspectionPostSVLedgerService.delPostSVLedgerRule(pd);
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
    @PostMapping(value = "/editQuestionLedgerOneToMany")
    public Map<String, String> editQuestionLedgerOneToMany(
            @ApiParam(value =
                    "问题台账ID数组：ledgerIDArr,\n" +
                            "当前流程ID：PROC_ID,\n" +
                            "当前问题分类数组：questionArr:[{" +
                            "   当前问题分类一级ID：QUESTION_ID_1,\n" +
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

        //问题台账ID列表
        List<String> ledgerIDList = (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        //问题分类对象列表
        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));
        pd.put("LEDGER_ID", ledgerIDList.get(0));
        List<Map<String, String>> dataList = inspectionPostSVLedgerService.getPostSVLedgerByLedgerID(pd);
        pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

        try {
            if (null != questionArr && !questionArr.isEmpty()) {
                //删除原始信息
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //整改台账同步
                    if ("0".equals(pd.getString("IS_REPORT"))) {
                        pd.put("type", "delete");
                        inspectionPostSVReformService.updateReform(pd);
                    }
                    inspectionPostSVLedgerService.delPostSVLedgerByLedgerId(pd);
                    inspectionPostSVLedgerService.delPostSVLedgerRule(pd);
                });
                //新增
                addLedgerOneToMany(questionArr, pd, pd_sub);
            } else {
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //整改台账同步
                    if ("0".equals(pd.getString("IS_REPORT"))) {
                        pd.put("type", "delete");
                        inspectionPostSVReformService.updateReform(pd);
                        pd.put("type", "add");
                        inspectionPostSVReformService.updateReform(pd);
                    }
                    inspectionPostSVLedgerService.editPostSVLedgerByLedgerID(pd);
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
                    "是否记入通知书：IS_REPORT,\n" +
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

        try {

            //新增
            addLedgerOneToMany(questionArr, pd, pd_sub);

            res.put("msg", "问题台账新增成功");
            //当前问题台账流程完成，激活当前现场检查所有后续流程
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishPostSVCurSubProcessById(pd);
            inspectionProcessControlService.activatePostSvFollowProcessSub(pd);
            res.put("result", "success");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "问题台账新增失败");
        }
        return res;
    }

    /*问题台账一对多新增*/
    private void addLedgerOneToMany(List<Map<String, String>> questionArr, PageData pd, PageData pd_sub) {
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

            inspectionPostSVLedgerService.addPostSVLedger(pd);

            pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
            List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
                /*新增问题台账制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionPostSVLedgerService.addPostSVLedgerRule(pd);
            }

            //整改台账同步
            if (pd.getString("IS_REPORT").equals("0")) {
                pd.put("type", "add");
                inspectionPostSVReformService.updateReform(pd);
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
        String questionType = inspectionPostSVLedgerService.getQuestionTypeByTaskId(pd);
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
        List<Map<String, String>> dataList = inspectionPostSVLedgerService.getPostSVLedgerByUserIdTaskID(pd);
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
        List<Map<String, String>> dataList = inspectionPostSVLedgerService.getLedgerAddUserByTaskId(pd);
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
        List<Map<String, String>> dataList = inspectionPostSVLedgerService.getPostSVLedgerByLedgerID(pd);
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
                    "是否记入通知书：IS_REPORT,\n" +
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
        List<Map<String, Object>> opinionList = inspectionPostSVLedgerService.getQuestionOpinionById(pd_sub);
        pd.put("QUESTION_OPINIONS", opinionList.get(0).get("QUESTION_OPINIONS"));
        String procCode = pd.getString("PROC_ID").substring(0, 3);
        try {
            inspectionPostSVLedgerService.addPostSVLedger(pd);
            /*新增问题台账制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionPostSVLedgerService.addPostSVLedgerRule(pd);
            }
            pd.put("procCode", procCode);
            //整改台账同步
            if (pd.getString("IS_REPORT").equals("0")) {
                pd.put("type", "add");
                inspectionPostSVReformService.updateReform(pd);
            }
            res.put("msg", "问题台账新增成功");
            //当前问题台账流程完成，激活当前现场检查所有后续流程
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionProcessControlService.finishPostSVCurSubProcessById(pd);
            inspectionProcessControlService.activatePostSvFollowProcessSub(pd);
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
        List<Map<String, Object>> opinionList = inspectionPostSVLedgerService.getQuestionOpinionById(pd_sub);
        pd.put("QUESTION_OPINIONS", opinionList.get(0).get("QUESTION_OPINIONS"));
        try {
            inspectionPostSVLedgerService.editPostSVLedgerByLedgerID(pd);
            /*修改问题台账制度依据*/
            inspectionPostSVLedgerService.delPostSVLedgerRule(pd);
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionPostSVLedgerService.addPostSVLedgerRule(pd);
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
            List<Map<String, String>> dataList = inspectionPostSVLedgerService.getPostSVLedgerByLedgerID(pd);
            inspectionPostSVLedgerService.delPostSVLedgerByLedgerId(pd);
            inspectionPostSVLedgerService.delPostSVLedgerRule(pd);
            pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));
            //整改台账同步
            if ("0".equals(pd.getString("IS_REPORT"))) {
                pd.put("type", "delete");
                inspectionPostSVReformService.updateReform(pd);
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
    @ApiOperation("获取问题列表树")
    public Map<String, Object> getQuestionBankTreeForQuestionLedger(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        PageData subPd = new PageData();
        Map<String, Object> result = new HashMap<>();
        try {
            subPd.put("INSPECTION_TASK_ID", pd.getString("TASK_ID"));
            Map<String, String> taskDataList = inspectionPostSVListService.getTaskInfoByTaskId(pd);
            String type = taskDataList.get("INSPECTION_TASK_TYPE");
            subPd.put("QUESTION_TYPE", type);
            List data = inspectionPostSVLedgerService.getQuestionBankTreeForPostSVLedger(subPd);

            List<QueTreeNode> treeNodeList = new ArrayList<>();
            GuokuTreeUtils.getTreeList(treeNodeList, data, null, 0);
            /*GuokuTreeUtils.filterNOdes(treeNodeList, type, "0", "其他关注事项");*/
            String queQuery = pd.getString("queQuery");
            if( null != queQuery && !"".equals(queQuery) ){
                GuokuTreeUtils.queryTreeNodes(treeNodeList, queQuery);
            }

            result.put("msg", "操作成功");
            result.put("rows", treeNodeList);
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    /*private void getTreeList(List<QueTreeNode> treeList, List<Map<String, Object>> metaList, QueTreeNode temp) {
        for (int i = 0; i < metaList.size(); i++) {
            String tempPid = metaList.get(i).get("pid").toString();
            QueTreeNode tree = new QueTreeNode();
            tree.setValue(metaList.get(i).get("id").toString());
            tree.setKey(metaList.get(i).get("id").toString());
            if (metaList.get(i).get("RULE_DSCR") == null) {
                tree.setConcent("");
            } else {
                tree.setConcent(metaList.get(i).get("RULE_DSCR").toString());
            }
            tree.setPid(metaList.get(i).get("pid").toString());
            tree.setTitle(metaList.get(i).get("name").toString());
            tree.setLabel(metaList.get(i).get("name").toString());
            tree.setIsleaf(metaList.get(i).get("isleaf").toString());
            tree.setType((metaList.get(i).get("TYPE") == null || metaList.get(i).get("TYPE").toString() == "") ? "" : metaList.get(i).get("TYPE").toString());
            if (temp == null) {
                if (oConvertUtils.isEmpty(tempPid)) {
                    treeList.add(tree);
                    if (tree.getIsleaf().equals("1")) {
                        getTreeList(treeList, metaList, tree);
                    }
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getValue())) {
                temp.getChildren().add(tree);
                if (tree.getIsleaf().equals("1")) {
                    getTreeList(treeList, metaList, tree);
                }
            }
        }
    }

    *//*树形结构裁剪*//*
    private void filterNOdes(List<QueTreeNode> treeList, String type, String flag, String exceptionWord) {
        for (int i = 0; i < treeList.size(); i++) {
            if (treeList.get(i).getChildren() != null && treeList.get(i).getChildren().size() > 0) {
                filterNOdes(treeList.get(i).getChildren(), type, flag, exceptionWord);
            } else {
                Iterator<QueTreeNode> iterator = treeList.iterator();
                while (iterator.hasNext()) {
                    QueTreeNode tempNode_2 = iterator.next();
                    if (!((type.equals(tempNode_2.getType()) && flag.equals(tempNode_2.getIsleaf())))
                            && !(exceptionWord.equals(tempNode_2.getLabel()) && flag.equals(tempNode_2.getIsleaf()))
                            && !(tempNode_2.getChildren() != null && tempNode_2.getChildren().size() > 0)) {
                        iterator.remove();
                        i--;
                    }
                }
            }
        }
        Iterator<QueTreeNode> iterator = treeList.iterator();
        while (iterator.hasNext()) {
            QueTreeNode tempNode_2 = iterator.next();
            if (tempNode_2.getChildren() == null || tempNode_2.getChildren().size() == 0) {
                if (!((type.equals(tempNode_2.getType()) && flag.equals(tempNode_2.getIsleaf())))
                        && !(exceptionWord.equals(tempNode_2.getLabel()) && flag.equals(tempNode_2.getIsleaf()))
                        && !(tempNode_2.getChildren() != null && tempNode_2.getChildren().size() > 0)) {
                    iterator.remove();
                }
            }
        }
    }*/

} ///:~
