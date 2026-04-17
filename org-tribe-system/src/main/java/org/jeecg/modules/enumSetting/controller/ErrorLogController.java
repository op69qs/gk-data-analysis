package org.jeecg.modules.enumSetting.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.enumSetting.BaseController;
import org.jeecg.modules.enumSetting.service.ErrorLogService;
import org.jeecg.modules.util.PageData;
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
@Api(tags = "手动跑数,错误日志")
@RequestMapping(value = "/errorLogController", produces = MediaType.APPLICATION_JSON_VALUE)
public class ErrorLogController extends BaseController {

    @Autowired
    private ErrorLogService errorLogService;

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    @ApiOperation("列表接口")
    public Map<String, Object> getData(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> data = errorLogService.getData(pd);
            Integer count = errorLogService.getCount(pd);
            result.put("msg", "查询成功");
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/callProc", method = RequestMethod.POST)
    @ApiOperation("调用存储过程")
    public Map<String, Object> callProc(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            errorLogService.callProc(pd);
            result.put("msg", "调用完成");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }
}
