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
            int pageNo = parsePositive(pd.getString("pageNo"), 1);
            int pageSize = parsePositive(pd.getString("pageSize"), 10);
            pd.put("page", (pageNo - 1) * pageSize);
            pd.put("rows", pageSize);
            List<Map<String, Object>> data = errorLogService.getData(pd);
            Integer count = errorLogService.getCount(pd);
            result.put("msg", "查询成功");
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("result", "success");
        } catch (Exception e) {
            log.error("查询动态刷数任务失败", e);
            result.put("msg", e.getMessage());
            result.put("total", 0);
            result.put("rows", java.util.Collections.emptyList());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增动态刷数任务")
    public Map<String, Object> add(@RequestBody JSONObject param) {
        return execute("新增成功", () -> errorLogService.add(this.getPageData(param)));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("修改动态刷数任务")
    public Map<String, Object> edit(@RequestBody JSONObject param) {
        return execute("修改成功", () -> errorLogService.edit(this.getPageData(param)));
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation("删除动态刷数任务")
    public Map<String, Object> del(@RequestBody JSONObject param) {
        return execute("删除成功", () -> errorLogService.del(this.getPageData(param)));
    }

    @RequestMapping(value = "/callProc", method = RequestMethod.POST)
    @ApiOperation("调用存储过程")
    public Map<String, Object> callProc(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        try {
            String runId = errorLogService.callProc(pd);
            result.put("msg", "任务已提交，请在运行记录中查看执行结果");
            result.put("runId", runId);
            result.put("result", "success");
        } catch (Exception e) {
            log.error("提交动态刷数任务失败", e);
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getRunRecords", method = RequestMethod.POST)
    @ApiOperation("动态刷数运行记录")
    public Map<String, Object> getRunRecords(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            int pageNo = parsePositive(pd.getString("pageNo"), 1);
            int pageSize = parsePositive(pd.getString("pageSize"), 10);
            pd.put("page", (pageNo - 1) * pageSize);
            pd.put("rows", pageSize);
            result.put("msg", "查询成功");
            result.put("total", errorLogService.getRunRecordCount(pd));
            result.put("rows", errorLogService.getRunRecords(pd));
            result.put("result", "success");
        } catch (Exception exception) {
            log.error("查询动态刷数运行记录失败", exception);
            result.put("msg", exception.getMessage());
            result.put("total", 0);
            result.put("rows", java.util.Collections.emptyList());
            result.put("result", "failed");
        }
        return result;
    }

    private Map<String, Object> execute(String successMessage, Runnable action) {
        Map<String, Object> result = new HashMap<>();
        try {
            action.run();
            result.put("msg", successMessage);
            result.put("result", "success");
        } catch (Exception exception) {
            log.error(successMessage + "失败", exception);
            result.put("msg", exception.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    private int parsePositive(String value, int defaultValue) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : defaultValue;
        } catch (Exception ignored) {
            return defaultValue;
        }
    }
}
