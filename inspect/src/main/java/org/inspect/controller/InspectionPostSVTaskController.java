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
@Api(tags="事后检查任务")
@RequestMapping(value = "/inspectionPostSVTaskController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVTaskController extends BaseController {

    @Autowired
    private InspectionTaskService inspectionTaskService;
    @Autowired
    protected InspectionUserService inspectionUserService;

    @Autowired
    private InspectionPostSVTaskService inspectionPostSVTaskService;


    @RequestMapping(value = {"/getInspectionTaskPage"}, method = RequestMethod.POST)
    @ApiOperation("获取任务列表(分页)")
    public Object getInspectionTaskPage(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo"))-1)*Integer.parseInt(pd.getString("pageSize"));
            pd.put("page",pageNo);
            pd.put("rows",Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = inspectionPostSVTaskService.getInspectionTaskPage(pd);
            Integer count = inspectionPostSVTaskService.getInspectionTaskCount(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getGKbyBook"} , method = RequestMethod.POST)
    @ApiOperation("跟据核算主体取国库")
    public Object getGKbyBook(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows",inspectionTaskService.getGKbyBook(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value={"/getInspectionTaskData"} , method = RequestMethod.POST)
    @ApiOperation("获取任务列表(不分页)")
    public Object getInspectionTaskData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionPostSVTaskService.getInspectionTaskData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

}
