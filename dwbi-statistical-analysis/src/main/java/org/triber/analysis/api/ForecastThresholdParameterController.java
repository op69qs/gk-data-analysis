package org.triber.analysis.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triber.analysis.service.ForecastThresholdParameterService;
import org.triber.analysis.util.BaseController;
import org.triber.analysis.util.PageData;
import org.triber.analysis.util.ThresholdPredictionJob;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author haojiang.
 * @Ddate 2020/9/29 15:36
 * @Description 重点指标参数维护
 */
@Slf4j
@RestController
@RequestMapping(value = "/forecastThresholdParameter", produces = MediaType.APPLICATION_JSON_VALUE)
public class ForecastThresholdParameterController extends BaseController {

    @Autowired
    private ThresholdPredictionJob thresholdPredictionJob;

    @Autowired
    private ForecastThresholdParameterService thresholdParameterService;

    //获取监测器指标
    @PostMapping("/getMonitorIndexData")
    public List<Map<String, Object>> getMonitorIndexData() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.get("type"))) { //运维
                pageData.put("type", "ucloud");
            } else if ("network".equals(pageData.get("type"))) { //网络
                pageData.put("type", "upm");
            }
            pageData.put("index_type", "1"); //不查状态类监测器指标
            dataList = thresholdParameterService.getMonitorIndexData(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //获取监测器指标资源
    @PostMapping("/getIndexResourceData")
    public List<Map<String, Object>> getIndexResourceData() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.get("type"))) { //运维
                pageData.put("type", "ucloud");
            } else if ("network".equals(pageData.get("type"))) { //网络
                pageData.put("type", "upm");
            }
            dataList = thresholdParameterService.getIndexResourceData(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


    //执行样本预测方法
    @PostMapping("/getForecastValues")
    public Map<String,Object> getForecastValues(){
        Map<String, Object> map = new HashMap<>();
        try {
            thresholdPredictionJob.saveForecastValues();
            map.put("code", 200);
            map.put("msg", "执行成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "执行失败!");
        }
        return map;
    }

    //获取预测阈值参数表数据
    @PostMapping("/getThresholdData")
    public Map<String, Object> getThresholdData() {
        Map<String, Object> map = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            Integer page = Integer.parseInt(pageData.getString("page"));//页码
            Integer rows = Integer.parseInt(pageData.getString("rows"));//行数
            pageData.put("page", (page - 1) * rows);
            int count = thresholdParameterService.getThresholdCount(pageData);
            List<Map<String, String>> dataList = thresholdParameterService.getThresholdData(pageData);
            map.put("total", count);
            map.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //新增预测阈值参数表数据
    @PostMapping("/insertThresholdData")
    public Map<String, Object> insertThresholdData() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("ID", this.get32UUID());
            pageData.put("addDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            thresholdParameterService.insertThresholdData(pageData);
            result.put("code", 200);
            result.put("msg", "新增成功!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "新增失败!");
            e.printStackTrace();
        }
        return result;
    }

    //修改预测阈值参数表数据
    @PostMapping("/updateThresholdData")
    public Map<String, Object> updateThresholdData() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("modifyDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            thresholdParameterService.updateThresholdData(pageData);
            result.put("code", 200);
            result.put("msg", "修改成功!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "修改失败!");
            e.printStackTrace();
        }
        return result;
    }

    //删除预测阈值参数表数据
    @PostMapping("/deleteThresholdData")
    public Map<String, Object> deleteThresholdData() {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("modifyDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            thresholdParameterService.deleteThresholdData(pageData);
            result.put("code", 200);
            result.put("msg", "删除成功!");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败!");
            e.printStackTrace();
        }
        return result;
    }
}
