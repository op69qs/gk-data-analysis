package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.model.QueTreeNode;
import org.inspect.model.QueTreeNodeQue;
import org.inspect.service.InspectionApprovalService;
import org.inspect.service.QuestionBankService;
import org.inspect.util.DateUtil;
import org.inspect.util.GuokuTreeUtils;
import org.inspect.util.PageData;
import org.inspect.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags = "问题库")
@RequestMapping(value = "/questionBankController", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionBankController extends BaseController {

    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private InspectionApprovalService inspectionApprovalService;

    @RequestMapping(value = "addQuestionBank")
    @ApiOperation("新增问题")
    public Map<String, Object> addQuestionBank(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        Integer rs = checkRepeat(pd);
        String questionId = get32UUID();
        result.put("result", "success");
        result.put("msg", "新增成功！");
        String p_path = pd.getString("p_path");
        String path = (p_path == null || "".equals(p_path)) ? questionId : p_path + "," + questionId;
        try {
            pd.put("QUESTION_ID", questionId);
            pd.put("PATH", path);
            pd.put("ADD_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            questionBankService.addQuestionBank(pd);
            boolean aa = questionBankService.checkAddOtherDscr(pd).isEmpty();
            /*当前新增节点是否为二级节点并且没有添加“其他关注事项”三级节点,如果是二级节点则默认添加一个“其他关注事项”三级节点*/
            if (!questionBankService.checkQuestionIDIsTop(pd).isEmpty()
                    && questionBankService.checkAddOtherDscr(pd).isEmpty()) {
                String sort = pd.getString("SORT") + "999";
                PageData questionId_3 = new PageData();
                String questionId_3_id = get32UUID();
                String c_path = path + "," + questionId_3_id;
                questionId_3.put("QUESTION_ID", questionId_3_id);
                questionId_3.put("QUESTION_DSCR", "其他关注事项");
                questionId_3.put("QUESTION_PID", questionId);
                questionId_3.put("QUESTION_DETAILS", null);
                questionId_3.put("QUESTION_QUALITATIVE", null);
                questionId_3.put("QUESTION_OPINIONS", null);
                questionId_3.put("PATH", c_path);
                questionId_3.put("QUESTION_SEVERITY", null);
                questionId_3.put("QUESTION_RECTIFICATION_PLAN", null);
                questionId_3.put("isleaf", "0");
                questionId_3.put("SORT", sort);
                questionId_3.put("SPARE1", null);
                questionId_3.put("SPARE2", null);
                questionId_3.put("SPARE3", null);
                questionId_3.put("SPARE4", null);
                questionId_3.put("SPARE5", null);
                questionId_3.put("SPARE6", null);
                questionId_3.put("SPARE7", null);
                questionId_3.put("SPARE8", null);
                questionId_3.put("SPARE9", null);
                questionId_3.put("SPARE10", null);
                questionBankService.addQuestionBank(questionId_3);
            }
            String type = pd.getString("QUESTION_TYPE");
            if (null != type && !type.equals("")) {
                String[] types = type.split(",");
                for (int i = 0; i < types.length; i++) {
                    PageData pdtemp = new PageData();
                    pdtemp.put("ID", get32UUID());
                    pdtemp.put("QUESTION_ID", pd.getString("QUESTION_ID"));
                    pdtemp.put("TYPE", types[i]);
                    questionBankService.addQuestionType(pdtemp);
                }
            }

            PageData appPd = new PageData();
            appPd.put("type", 2);
            List<Map<String, Object>> list = inspectionApprovalService.getAppravalProcess(appPd);
            if (!list.isEmpty() && list.size() > 0) {
                appPd.put("id", get32UUID());
                appPd.put("auth_id", get32UUID());
                appPd.put("subject_id", questionId);
                appPd.put("add_user", pd.get("ADD_USER"));
                appPd.put("add_time", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                appPd.put("app_role", list.get(0).get("role"));
                appPd.put("app_step", list.get(0).get("step"));
                appPd.put("app_org", list.get(0).get("organ"));
                inspectionApprovalService.addInspectionApproval(appPd);
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    private Integer checkRepeat(PageData pd) {
        List<Map<String, Object>> data = questionBankService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = {"/getQuestionBankPage"}, method = RequestMethod.POST)
    @ApiOperation("获取问题列表(分页)")
    public Object getQuestionBankPage(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            pd = getCol(pd);
            List<Map<String, Object>> result = questionBankService.getQuestionBankPage(pd);
            Integer count = questionBankService.getQuestionBankCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getQuestionBankData")
    @ApiOperation("获取问题列表(不分页)")
    public Object getQuestionBankData(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            pd = getCol(pd);
            jsonMap.put("rows", questionBankService.getQuestionBankData(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getQuestionBankTree")
    @ApiOperation("获取问题列表树")
    public Map<String, Object> getQuestionBankTree(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = questionBankService.getQuestionBankTree(pd);
            List<QueTreeNode> treeNodeList = new ArrayList<>();
            getTreeList(treeNodeList, data, null);
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

    private void getTreeList(List<QueTreeNode> treeList, List<Map<String, Object>> metaList, QueTreeNode temp) {
        for (int i = 0; i < metaList.size(); i++) {
            String tempPid = metaList.get(i).get("pid").toString();
            QueTreeNode tree = new QueTreeNode();
            tree.setValue(metaList.get(i).get("id").toString());
            tree.setKey(metaList.get(i).get("id").toString());
            tree.setTitle(metaList.get(i).get("name").toString());
            tree.setLabel(metaList.get(i).get("name").toString());
            tree.setIsleaf(metaList.get(i).get("isleaf").toString());
            tree.setPID_SORT(metaList.get(i).get("sort").toString());
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


    @RequestMapping(value = "/editQuestionBank")
    @ApiOperation("修改问题")
    public Map<String, Object> editQuestionBank(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            questionBankService.editQuestionBank(pd);
            String type = pd.getString("QUESTION_TYPE");
            questionBankService.delQuestionType(pd);
            if (null != type && !type.equals("")) {
                String[] types = type.split(",");
                for (int i = 0; i < types.length; i++) {
                    PageData pdtemp = new PageData();
                    pdtemp.put("ID", get32UUID());
                    pdtemp.put("QUESTION_ID", pd.getString("QUESTION_ID"));
                    pdtemp.put("TYPE", types[i]);
                    questionBankService.addQuestionType(pdtemp);
                }
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "/saveQuestionRuleRelation")
    @ApiOperation("保存问题与条列关系")
    public Map<String, Object> addQuestionRuleRelation(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        if (null == pd.getString("QUESTION_ID") || pd.getString("QUESTION_ID").equals("")) {
            result.put("msg", "问题编码为空，保存失败");
            result.put("result", "false");
            return result;
        }
        try {
            questionBankService.delQuestionRuleRelation(pd);
            String RULE_ID = pd.getString("RULE_ID");
            if (null != RULE_ID && !RULE_ID.equals("")) {
                String[] RULE_IDS = RULE_ID.split(",");
                for (int i = 0; i < RULE_IDS.length; i++) {
                    PageData temp = new PageData();
                    temp.put("ID", get32UUID());
                    temp.put("QUESTION_ID", pd.getString("QUESTION_ID"));
                    temp.put("RULE_ID", RULE_IDS[i]);
                    questionBankService.addQuestionRuleRelation(temp);
                }
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = "/delQuestionRuleRelation")
    @ApiOperation("删除问题与条列关系")
    public Map<String, Object> delQuestionRuleRelation(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try {
            String ID = pd.getString("ID");
            if (null != ID && !ID.equals("")) {
                String[] IDS = ID.split(",");
                for (int i = 0; i < IDS.length; i++) {
                    PageData temp = new PageData();
                    temp.put("ID", IDS[i]);
                    questionBankService.delQuestionRuleRelation(temp);
                }
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }


    private PageData getCol(PageData pd) {
        pd.put("column", questionBankService.getColumn(pd));
        return pd;
    }

    @RequestMapping("/getQuestionBankTreeNew")
    @ApiOperation("获取问题列表树")
    public Map<String, Object> getQuestionBankTreeNew(@RequestBody(required = false) JSONObject param) {
        PageData pd = this.getPageData(param);
        Map<String, Object> result = new HashMap<>();
        Integer count = 0;
        try {
            if ("".equals(pd.getString("QUESTION_DSCR")) && "".equals(pd.getString("QUESTION_TYPE")) && "".equals(pd.getString("RULE_FILE_CONTENT"))) {
                pd = getCol(pd);
                List<Map<String, Object>> result1 = questionBankService.getQuestionBankPage(pd);
                count = questionBankService.getQuestionBankCount(pd);
                result.put("rows", result1);
                result.put("msg", "操作成功");
                result.put("result", "success");
                result.put("total", count);//total键 存放总记录数，必须的
            } else {
                List<Map<String, Object>> data = questionBankService.getQuestionBankTreeNewTree(pd);
                List<QueTreeNodeQue> treeNodeList = new ArrayList<>();
                getTreeListTree(treeNodeList, data, null);
                String QUESTION_DSCR = pd.getString("QUESTION_DSCR");
                String QUESTION_TYPE = pd.getString("QUESTION_TYPE");
                String RULE_FILE_CONTENT = pd.getString("RULE_FILE_CONTENT");
                GuokuTreeUtils.QuestionBankTreeNodes(treeNodeList, QUESTION_DSCR, QUESTION_TYPE, RULE_FILE_CONTENT);

                result.put("msg", "操作成功");
                result.put("rows", treeNodeList);
                result.put("result", "success");
                result.put("total", count);//total键 存放总记录数，必须的
            }
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    //    //list添加子节点
    private void getTreeListTree(List<QueTreeNodeQue> treeList, List<Map<String, Object>> metaList, QueTreeNodeQue temp) {
        for (int i = 0; i < metaList.size(); i++) {
            String tempPid = metaList.get(i).get("pid").toString();
            QueTreeNodeQue tree = new QueTreeNodeQue();
            tree.setValue(metaList.get(i).get("id").toString());
            tree.setId(metaList.get(i).get("id").toString());
            tree.setQUESTION_ID(metaList.get(i).get("id").toString());
            tree.setKey(metaList.get(i).get("id").toString());
            tree.setTitle(metaList.get(i).get("name").toString());
            tree.setLabel(metaList.get(i).get("name").toString());
            tree.setIsleaf(metaList.get(i).get("isleaf").toString());
            tree.setQUESTION_DSCR(metaList.get(i).get("name").toString());
            tree.setQuestion_rectification_plan(metaList.get(i).get("QUESTION_RECTIFICATION_PLAN").toString());
            tree.setQuestion_type_dscr(metaList.get(i).get("QUESTION_TYPE_DSCR").toString());
            tree.setQuestion_type(metaList.get(i).get("QUESTION_TYPE").toString());
            tree.setRule_file_content(metaList.get(i).get("RULE_FILE_CONTENT").toString());
            tree.setSORT(metaList.get(i).get("SORT").toString());
            tree.setPath(metaList.get(i).get("path").toString());
            if (temp == null && (oConvertUtils.isEmpty(tempPid))) {
                treeList.add(tree);
                if (!tree.getIsleaf().equals("0")) {
                    getTreeListTree(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getIsleaf().equals("0")) {
                    getTreeListTree(treeList, metaList, tree);
                }
            }
        }
    }

}
