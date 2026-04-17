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
@Api(tags="检查内容")
@RequestMapping(value = "/inspectionContentController", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionContentController extends BaseController {

    @Autowired
    private InspectionContentService inspectionContentService;


    @RequestMapping(value={"/addContent"} , method = RequestMethod.POST)
    @ApiOperation("新增")
    public Map<String,Object> addContent(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("result", "success");
        result.put("msg", "新增成功！");
        try {
            pd.put("id",get32UUID());
            pd.put("ADD_TIME",DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            inspectionContentService.addContent(pd);
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
            return result;
        }
        return result;
    }

    @RequestMapping(value={"/getContentData"} , method = RequestMethod.POST)
    @ApiOperation("查询")
    public Object getContentData(@RequestBody(required = false) JSONObject param){
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", inspectionContentService.getContentData(pd));
            jsonMap.put("result","success");
        }catch (Exception e){
            jsonMap.put("result","false");
            jsonMap.put("msg",e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value={"/editContent"} , method = RequestMethod.POST)
    @ApiOperation("修改")
    public Map<String,Object>editContent(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            inspectionContentService.editContent(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }


    @RequestMapping(value={"/delContent"} , method = RequestMethod.GET)
    @ApiOperation("删除")
    public Map<String,Object>delContent(@RequestBody(required = false) JSONObject param){
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        result.put("msg", "保存成功！");
        result.put("result", "success");
        try{
            String ID = pd.getString("id");
            if (null != ID && !ID.equals("")){
                String []IDS = ID.split(",");
                for (int i = 0;i<IDS.length;i++){
                    PageData temp = new PageData();
                    temp.put("id",IDS[i]);
                    inspectionContentService.delContent(temp);
                }
            }
        }catch (Exception e){
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }
}
