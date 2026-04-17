package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.QuestionRuleService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="规章制度")
@RequestMapping(value = "/questionRuleController", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionRuleController extends BaseController {

    @Autowired
    private QuestionRuleService questionRuleService;

    @RequestMapping(value = "addQuestionRule")
    @ApiOperation("新增条款")
    public Map<String,Object> addQuestionRule(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        Integer rs = checkRepeat(pd);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        if (null != rs && rs>0) {
            result.put("msg", "新增失败！该条款已存在，请修改后重试。");
            result.put("result", "false");
        }else{
            try {
                pd.put("RULE_ID",get32UUID());
                PageData sortPd = new PageData();
                sortPd.put("RULE_FILE_NO",pd.getString("RULE_FILE_NO"));
                sortPd.put("SORT",pd.getString("SORT"));
                List<Map<String,Object>> sortMap = questionRuleService.getQuestionRuleData(sortPd);
                if (null != sortMap && !sortMap.isEmpty()){
                    questionRuleService.updateSort(pd);
                }
                questionRuleService.addQuestionRule(pd);
            }catch (Exception e){
                result.put("msg", e.getMessage());
                result.put("result", "false");
                return result;
            }
        }
        return result;
    }

    private Integer checkRepeat(PageData pd){
        List<Map<String, Object>> data = questionRuleService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @RequestMapping(value = {"/getQuestionRulePage"}, method = RequestMethod.POST)
    @ApiOperation("获取条款列表(分页)")
    public Object getQuestionRulePage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = questionRuleService.getQuestionRulePage(pd);
            Integer count = questionRuleService.getQuestionRuleCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getQuestionRuleData")
    @ApiOperation("获取条款列表(不分页)")
    public Map<String, Object>getQuestionRuleData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", questionRuleService.getQuestionRuleData(pd));//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getQuestionRuleByRelation")
    @ApiOperation("根据问题获取条款")
    public Map<String, Object>getQuestionRuleByRelation(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", questionRuleService.getQuestionRuleByRelation(pd));//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = "/editQuestionRule")
    @ApiOperation("修改条款")
    public Map<String,Object>editQuestionRule(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        if(null != pd.getString("RULE_FILE_CONTENT") && !pd.getString("RULE_FILE_CONTENT").equals("")) {
            Integer rn = checkRepeat(pd);
            if (null != rn && rn>0) {
                result.put("msg", "修改失败！该条款已存在，请修改后重试。");
                result.put("result", "false");
                return result;
            }
        }
        try{
            PageData sortPd = new PageData();
            sortPd.put("RULE_FILE_NO",pd.getString("RULE_FILE_NO"));
            sortPd.put("SORT",pd.getString("SORT"));
            List<Map<String,Object>> sortMap = questionRuleService.getQuestionRuleData(sortPd);
            if (null != sortMap && !sortMap.isEmpty()){
                questionRuleService.updateSort(pd);
            }
            questionRuleService.editQuestionRule(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }

        return result;
    }
}
