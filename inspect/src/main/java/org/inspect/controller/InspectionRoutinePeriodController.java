// InspectionRoutinePeriodController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionRoutinePeriodService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/2/24.
 */
@Slf4j
@RestController
@Api(tags = "例行检查类型与周期记录")
@RequestMapping(value = "/routinePeriod", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionRoutinePeriodController extends BaseController{

    @Autowired
    private InspectionRoutinePeriodService inspectionRoutinePeriodService;

    /**
     * 获取例行检查类型与周期记录
     * @return
     */
    @PostMapping(value = "/getRoutinePeriodInfo")
    public Map<String, Object> getRoutinePeriodInfo(
            @RequestBody(required = false) JSONObject param
    ){
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionRoutinePeriodService.getRoutinePeriodInfo(pd);
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 编辑期次信息
     * @param param
     * @return
     */
    @PostMapping(value = "/editRoutinePeriodInfo")
    public Map<String, Object> editRoutinePeriodInfo(
            @RequestBody(required = false) JSONObject param
    ){
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        inspectionRoutinePeriodService.delRoutinePeriodByTypeId(pd);
        List<Map<String, String>> dataList = (List<Map<String, String>>)JSONObject.parse(pd.getString("dataList"));
        pd.put("dataList", dataList);
        inspectionRoutinePeriodService.insertRoutinePeriod(pd);
        res.put("result", "success");
        return res;
    }

} ///:~
