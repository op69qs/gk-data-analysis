package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionPostSVProcService;
import org.inspect.service.InspectionPostSVProcSubService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@Api(tags="流程")
@RequestMapping(value = "/inspectionPostSVProcController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionPostSVProcController extends BaseController {

    @Autowired
    private InspectionPostSVProcService inspectionPostSVProcService;
    @Autowired
    private InspectionPostSVProcSubService inspectionPostSVProcSubService;
    

    @RequestMapping("/getInspectionProcData")
    @ApiOperation("获取主流程(不分页)")
    public Object getInspectionProcData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionPostSVProcService.getInspectionProcData(pd));
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
            jsonMap.put("rows", inspectionPostSVProcSubService.getInspectionProcSubData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }

}
