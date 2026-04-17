package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionProcService;
import org.inspect.service.InspectionProcSubService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="流程")
@RequestMapping(value = "/inspectionProcController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionProcController extends BaseController {

    @Autowired
    private InspectionProcService inspectionProcService;
    @Autowired
    private InspectionProcSubService inspectionProcSubService;

    @RequestMapping(value = "addInspectionProc")
    @ApiOperation("新增流程")
    public Map<String,Object> addInspectionProc(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            inspectionProcService.addInspectionProc(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping("/getInspectionProcData")
    @ApiOperation("获取主流程(不分页)")
    public Object getInspectionProcData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionProcService.getInspectionProcData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping("/getInspectionProcSubData")
    @ApiOperation("获取子流程(不分页)")
    public Object getInspectionProcSubData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionProcSubService.getInspectionProcSubData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value = "/editInspectionProc")
    @ApiOperation("修改主流程")
    public Map<String,Object>editInspectionProc(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try{
            inspectionProcService.editInspectionProc(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

}
