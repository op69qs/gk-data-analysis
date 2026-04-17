// InspectionSupplementController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionSupplementService;
import org.inspect.service.QuestionRuleService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.inspect.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Created by Samer on 2020/3/4.
 */
@Slf4j
@RestController
@Api(tags = "检查数据补录")
@RequestMapping(value = "/inspectionSupplement", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionSupplementController extends BaseController{

    @Autowired
    private QuestionRuleService questionRuleService;

    @Autowired
    private InspectionSupplementService inspectionSupplementService;

    /**
     * 获取检查记录
     * @param param
     * @return
     */
    @PostMapping(value = "/getSupplementLedgerInfo")
    @ApiOperation(value = "获取检查记录")
    public Map<String, Object> getSupplementLedgerInfo(
            @ApiParam(value =
                    " 任务ID:TASK_ID,\n" +
                    " 整改状态：IS_REFORM\n" +
                    " 问题描述: QUESTION_CONTENT\n" +
                    " 制度依据: RULE_FILE_CONTENT")
            @RequestBody(required = false)JSONObject param
    ){
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> result = inspectionSupplementService.getSupplementLedgerInfo(pd);
        res.put("rows", result);//rows键 存放每页记录 list
        res.put("result","success");
        return res;
    }

    /**
     * 编辑检查补录任务信息
     * @param param
     * @return
     */
    @PostMapping(value = "/editSupplementTask")
    @ApiOperation(value = "编辑检查补录任务信息")
    public Map<String, Object> editSupplementTask(
            @RequestBody(required = false)JSONObject param
    ){
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String dataNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);

        pd.put("MODIFY_DATE", dataNow);
        pd.put("MODIFY_USERID", pd.getString("USERID"));
        try{
            inspectionSupplementService.editSupplementTask(pd);
            res.put("msg", "编辑成功");
            res.put("result","success");
        } catch (Exception e){
            res.put("msg", "编辑失败");
            res.put("result","false");
        }
        return res;
    }

    /**
     * 获取检查补录任务信息
     * @param param
     * @return
     */
    @PostMapping(value = "/getSupplementTask")
    @ApiOperation(value = "获取检查补录任务信息")
    public Map<String, Object> getSupplementTask(
            @RequestBody(required = false)JSONObject param
            ){
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
        pd.put("page",pageNo);
        pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
        List<Map<String, Object>> result = inspectionSupplementService.getSupplementTask(pd);
        Integer count = inspectionSupplementService.getSupplementTaskCount(pd);
        res.put("total", count);//total键 存放总记录数，必须的
        res.put("rows", result);//rows键 存放每页记录 list
        res.put("result","success");
        return res;
    }

    /**
     * 新增检查补录任务信息
     * @param param
     * @return
     */
    @PostMapping(value = "/addSupplementTask")
    @ApiOperation(value = "新增检查补录任务信息")
    public Map<String, Object> addSupplementTask(
            @RequestBody(required = false)JSONObject param
    ){
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String dataNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String TASK_ID = this.get32UUID();

        pd.put("TASK_ID", TASK_ID);
        pd.put("ADD_DATE", dataNow);
        pd.put("ADD_USERID", pd.getString("USERID"));
        try{
            inspectionSupplementService.addSupplementTask(pd);
            res.put("msg", "新增成功");
            res.put("result","success");
        } catch (Exception e){
            res.put("msg", "新增失败");
            res.put("result","false");
        }
        return res;
    }

    /**
     * 删除检查补录任务信息
     * @param param
     * @return
     */
    @PostMapping(value = "/delSupplementTask")
    @ApiOperation(value = "删除检查补录任务信息")
    public Map<String, Object> delSupplementTask(
            @RequestBody(required = false)JSONObject param
    ){
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        try{
            inspectionSupplementService.delSupplementTask(pd);
            res.put("msg", "删除成功");
            res.put("result","success");
        } catch (Exception e){
            res.put("msg", "删除失败");
            res.put("result","false");
        }
        return res;
    }

    /**
     * 删除检查记录
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "删除检查记录")
    @PostMapping(value = "/delSupplementLedger")
    public Map<String, String> delSupplementLedger(
            @ApiParam(value =
                    "问题台账ID数组：ledgerIDArr"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        //问题台账ID列表
        List<String> ledgerIDList= (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        try {
            //删除原始信息
            ledgerIDList.forEach(ledgerId -> {
                pd.put("LEDGER_ID", ledgerId);
                //删除原记录
                inspectionSupplementService.delSupplementLedgerById(pd);
                inspectionSupplementService.delSupplementRule(pd);
            });
            res.put("result", "success");
            res.put("msg", "检查记录删除成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "检查记录删除失败");
        }
        return res;
    }

    /**
     * 编辑检查记录
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "编辑检查记录")
    @PostMapping(value = "/editSupplementLedger")
    public Map<String, String> editSupplementLedger(
            @ApiParam(value =
                    "问题台账ID数组：ledgerIDArr,\n" +
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
                    "问题描述：QUESTION_CONTENT,\n" +
                    "整改措施：REFORM_SCHEME,\n" +
                    "是否完成：IS_FINISH,\n" +
                    "完成时间：FINISH_DATE,\n" +
                    "用户ID：USERID,\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        PageData pd_sub = new PageData();
        //问题台账ID列表
        List<String> ledgerIDList= (List<String>) JSONObject.parse(pd.getString("ledgerIDArr"));
        //问题分类对象列表
        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));

        pd.put("MODIFY_USERID", pd.getString("USERID"));
        pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        if("0".equals(pd.getString("IS_FINISH"))){
            pd.put("FINISH_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        }

        try {
            pd.put("LEDGER_ID", ledgerIDList.get(0));
            List<Map<String, Object>> dataList = inspectionSupplementService.getSupplementTask(pd);
            pd.put("TASK_ID", dataList.get(0).get("TASK_ID"));

            if( null != questionArr && !questionArr.isEmpty() ){
                //删除原始信息
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    //删除原记录
                    inspectionSupplementService.delSupplementLedgerById(pd);
                    inspectionSupplementService.delSupplementRule(pd);
                });
                pd.put("ADD_USERID", pd.getString("USERID"));
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                //台账信息新增
                addLedgerOneToMany(questionArr, pd, pd_sub);
            } else {
                ledgerIDList.forEach(ledgerId -> {
                    pd.put("LEDGER_ID", ledgerId);
                    inspectionSupplementService.editSupplementLedgerById(pd);
                });
            }
            res.put("result", "success");
            res.put("msg", "检查记录编辑成功");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "检查记录编辑失败");
        }
        return res;
    }

    /**
     * 新增检出补录检查记录
     *
     * @param param
     * @return res
     */
    @ApiOperation(value = "新增检出补录检查记录")
    @PostMapping(value = "/addSupplementLedger")
    public Map<String, String> addSupplementLedger(
            @ApiParam(value =
                    "当前任务ID：TASK_ID,\n" +
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
                    "问题描述：QUESTION_CONTENT,\n" +
                    "整改措施：REFORM_SCHEME,\n" +
                    "是否完成：IS_FINISH,\n" +
                    "完成时间：FINISH_DATE,\n" +
                    "用户ID：USERID,\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {

        Map<String, String> res = new HashMap<String, String>();
        PageData pd = this.getPageData(param);
        PageData pd_sub = new PageData();
        List<Map<String, String>> questionArr = (List<Map<String, String>>) JSONObject.parse(pd.getString("questionArr"));

        pd.put("ADD_USERID", pd.getString("USERID"));
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        if("0".equals(pd.getString("IS_FINISH"))){
            pd.put("FINISH_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        }
        try {
            addLedgerOneToMany(questionArr, pd, pd_sub);
            res.put("msg", "检查记录新增成功");
            res.put("result", "success");
        } catch (Exception e) {
            res.put("result", "false");
            res.put("msg", "检查记录新增失败");
        }
        return res;
    }

    /*问题描述问题分类一对多添加*/
    private void addLedgerOneToMany(List<Map<String, String>> questionArr, PageData pd, PageData pd_sub)
            throws Exception{
        questionArr.forEach(mapEle -> {
            pd.put("QUESTION_ID_1", mapEle.get("QUESTION_ID_1"));
            pd.put("QUESTION_DSCR_1", mapEle.get("QUESTION_DSCR_1"));
            pd.put("QUESTION_ID_2", mapEle.get("QUESTION_ID_2"));
            pd.put("QUESTION_DSCR_2", mapEle.get("QUESTION_DSCR_2"));
            pd.put("QUESTION_ID_3", mapEle.get("QUESTION_ID_3"));
            pd.put("QUESTION_DSCR_3", mapEle.get("QUESTION_DSCR_3"));
            pd.put("QUESTION_ID", mapEle.get("QUESTION_ID"));
            pd.put("QUESTION_DSCR", mapEle.get("QUESTION_DSCR"));
            pd.put("LEDGER_ID", UuidUtil.get32UUID());
            pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));

            inspectionSupplementService.addSupplementLedger(pd);

            pd_sub.put("QUESTION_ID", pd.getString("QUESTION_ID"));
            List<Map<String, Object>> questionRuleList = questionRuleService.getQuestionRuleByRelation(pd_sub);
            /*新增制度依据*/
            for (int i = 0, len = questionRuleList.size(); i < len; i++) {
                pd.put("RULE_FILE_NO", questionRuleList.get(i).get("RULE_FILE_NO"));
                pd.put("RULE_FILE_NAME", questionRuleList.get(i).get("RULE_FILE_NAME"));
                pd.put("RULE_FILE_CONTENT", questionRuleList.get(i).get("RULE_FILE_CONTENT"));
                pd.put("SORT", questionRuleList.get(i).get("SORT"));
                inspectionSupplementService.addSupplementRule(pd);
            }
        });
    }



} ///:~
