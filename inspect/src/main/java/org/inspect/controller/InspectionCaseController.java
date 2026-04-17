package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="案例库")
@RequestMapping(value = "/inspectionCaseController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionCaseController extends BaseController {

    @Autowired
    private InspectionCaseService inspectionCaseService;


    @RequestMapping(value = {"/getInspectionCasePage"}, method = RequestMethod.POST)
    @ApiOperation("获取任务列表(分页)")
    public Object getInspectionCasePage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = inspectionCaseService.getInspectionCasePage(pd);
            Integer count = inspectionCaseService.countCase(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getInspectionCaseData"} , method = RequestMethod.POST)
    @ApiOperation("获取任务列表(不分页)")
    public Object getInspectionCaseData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionCaseService.getInspectionCaseData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value={"/editInspectionCase"} , method = RequestMethod.POST)
    @ApiOperation("修改任务")
    public Map<String,Object>editInspectionCase(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            inspectionCaseService.editInspectionCase(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/addInspectionCase"} , method = RequestMethod.POST)
    @ApiOperation("新增任务")
    public Map<String,Object>addInspectionCase(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "新增成功！");
        result.put("result", "success");
        try {
            pd.put("ID",get32UUID());
            pd.put("ADD_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionCaseService.addInspectionCase(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value={"/delInspectionCase"} , method = RequestMethod.POST)
    @ApiOperation("删除任务")
    public Map<String,Object>delInspectionCase(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            inspectionCaseService.delInspectionCase(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }
}
