package org.triber.analysis.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triber.analysis.service.MonitorAlarmService;
import org.triber.analysis.util.BaseController;
import org.triber.analysis.util.CreateAnalysisSQL;
import org.triber.analysis.util.PageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/16 15:53
 * @Description 监测告警专题视图
 */
@Slf4j
@RestController
@RequestMapping(value = "/monitorAlarm", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitorAlarmController extends BaseController {

    @Autowired
    private MonitorAlarmService alarmService;

    @Autowired
    private CreateAnalysisSQL analysisSQL;

    //获取平台监测器
    @PostMapping("/getPlatformMonitor")
    public Map<String, Object> getPlatformMonitor() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            List<Map<String, Object>> dataList = alarmService.getAlarmPlatformData(pageData);
            map.put("code", 200);
            map.put("data", dataList);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //查询监测器告警构成分析数据
    @PostMapping("/getAlarmCompositionAnalysisData")
    public Map<String, Object> getAlarmCompositionAnalysisData() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("system_code", "ucloud");
            List<String> dateRange = analysisSQL.getDateRange(pageData); //时间区间
            List<Map<String, Object>> platformData = alarmService.getAlarmPlatformData(pageData);
            pageData.put("analysisSQL", analysisSQL.getCompositionAnalysis1SQL(pageData, dateRange, platformData));
            List<Map<String, Object>> innerData = alarmService.getAlarmAnalysisData(pageData);
            map.put("innerData", innerData); //平台监测器告警数量
            pageData.put("platform", "");
            List<Map<String, Object>> indexList = alarmService.getAlarmIndexData(pageData);
            pageData.put("analysisSQL", analysisSQL.getCompositionAnalysis2SQL(pageData, dateRange, indexList));
            List<Map<String, Object>> outerData = alarmService.getAlarmAnalysisData(pageData);
            map.put("outerData", outerData); //指标监测器告警数量
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //查询监测器告警序时分析数据
    @PostMapping("/getAlarmChronologyAnalysisData")
    public Map<String, Object> getAlarmChronologyAnalysisData() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("system_code", "ucloud");
            pageData.put("platform", "");
            //监测器告警序时分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            //监测器告警序时分析legend
            List<String> legendIds = new ArrayList<>(); //监测器主键集合
            List<String> legendData = new ArrayList<>(); //监测器名称主键集合
            List<Map<String, Object>> indexData = alarmService.getAlarmIndexData(pageData);
            for (Map<String, Object> data : indexData) {
                legendIds.add(String.valueOf(data.get("value")));
                legendData.add(String.valueOf(data.get("label")));
            }
            map.put("legend", legendData);

            pageData.put("analysisSQL", analysisSQL.getChronologyAnalysisSQL(pageData, dateRange, legendIds));
            List<Map<String, Object>> dataList = alarmService.getAlarmAnalysisData(pageData);
            Map<String, String> newDataMap = new HashMap<>();
            dataList.forEach(data -> newDataMap.merge(data.get("name").toString(), data.get("value").toString(), (oldValue, newValue) -> (oldValue + newValue)));
            List<Map<String, Object>> series = new ArrayList<>();
            for (String key : newDataMap.keySet()) {
                Map<String, Object> map1 = new HashMap<>();
                String[] array = new String[dateRange.size()];
                List<Map<String, String>> list = JSON.parseObject("[" + newDataMap.get(key) + "]", List.class);
                if (list.size() > 0 && list.get(0) != null) {
                    for (int i = 0; i < dateRange.size(); i++) {
                        for (int j = 0; j < list.size(); j++) { //遍历比较日期
                            if (list.get(j).get(dateRange.get(i)) != null) {
                                array[i] = list.get(j).get(dateRange.get(i));
                            }
                        }
                        if (StringUtils.isBlank(array[i])) { //将不存的数据置为0
                            array[i] = "0";
                        }
                    }
                } else {
                    for (int i = 0; i < dateRange.size(); i++) {
                        array[i] = "0";
                    }
                }
                map1.put("name", key);
                map1.put("value", array);
                series.add(map1);
            }
            map.put("series", series);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //查询分平台监测器告警分析数据
    @PostMapping("/getPlatformAlarmAnalysisData")
    public Map<String, Object> getPlatformAlarmAnalysisData() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("system_code", "ucloud");
            //监测器告警序时分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            //监测器告警序时分析legend
            List<String> legendIds = new ArrayList<>(); //监测器主键集合
            List<String> legendData = new ArrayList<>(); //监测器名称主键集合
            List<Map<String, Object>> indexData = alarmService.getAlarmIndexData(pageData);
            for (Map<String, Object> data : indexData) {
                legendIds.add(String.valueOf(data.get("value")));
                legendData.add(String.valueOf(data.get("label")));
            }
            map.put("legend", legendData);
            pageData.put("analysisSQL", analysisSQL.getPlatformAlarmAnalysisSQL(pageData, dateRange, legendIds));
            List<Map<String, Object>> dataList = alarmService.getAlarmAnalysisData(pageData);
            Map<String, String> newDataMap = new HashMap<>();
            dataList.forEach(data -> newDataMap.merge(data.get("name").toString(), data.get("value").toString(), (oldValue, newValue) -> (oldValue + newValue)));
            List<Map<String, Object>> series = new ArrayList<>();
            for (String key : newDataMap.keySet()) {
                Map<String, Object> map1 = new HashMap<>();
                String[] array = new String[dateRange.size()];
                List<Map<String, String>> list = JSON.parseObject("[" + newDataMap.get(key) + "]", List.class);
                if (list.size() > 0 && list.get(0) != null) {
                    for (int i = 0; i < dateRange.size(); i++) {
                        for (int j = 0; j < list.size(); j++) { //遍历比较日期
                            if (list.get(j).get(dateRange.get(i)) != null) {
                                array[i] = list.get(j).get(dateRange.get(i));
                            }
                        }
                        if (StringUtils.isBlank(array[i])) { //将不存的数据置为0
                            array[i] = "0";
                        }
                    }
                } else {
                    for (int i = 0; i < dateRange.size(); i++) {
                        array[i] = "0";
                    }
                }
                map1.put("name", key);
                map1.put("value", array);
                series.add(map1);
            }
            map.put("series", series);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //查询分平台分设备监测器告警分析数据
    @PostMapping("/getDeviceAlarmAnalysisData")
    public Map<String, Object> getEquipmentAlarmAnalysisData() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("system_code", "ucloud");
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            //分平台分设备监测器告警分析X轴设备
            List<String> deviceIds = new ArrayList<>(); //监测器设备主键集合
            List<String> deviceData = new ArrayList<>(); //监测器设备名称主键集合
            pageData.put("analysisSQL", analysisSQL.getAlarmResourceSQL(pageData, dateRange));
            List<Map<String, Object>> resourceData = alarmService.getAlarmAnalysisData(pageData);
            if (resourceData != null && resourceData.size() > 0) {
                for (Map<String, Object> data : resourceData) {
                    deviceIds.add(String.valueOf(data.get("value")));
                    deviceData.add(String.valueOf(data.get("label")));
                }
            }
            map.put("xAxis", deviceData);

            //分平台分设备监测器告警分析legend
            List<String> legendIds = new ArrayList<>(); //监测器主键集合
            List<String> legendData = new ArrayList<>(); //监测器名称主键集合
            Map<String, Object> indexMap = new HashMap<>();
            if (deviceIds.size() > 0) {
                pageData.put("analysisSQL", analysisSQL.getAlarmResourceOfIndexSQL(pageData, dateRange, deviceIds));
                List<Map<String, Object>> indexData = alarmService.getAlarmAnalysisData(pageData);
                if (indexData.size() > 0 && StringUtils.isNotBlank(String.valueOf(indexData.get(0).get("value")))) {
                    for (Map<String, Object> data : indexData) {
                        legendIds.add(String.valueOf(data.get("value")));
                        legendData.add(String.valueOf(data.get("label")));
                        indexMap.put(String.valueOf(data.get("value")), data.get("label"));
                    }
                }
            }
            map.put("legend", legendData);

            pageData.put("analysisSQL", analysisSQL.getDeviceAlarmAnalysisSQL(pageData, dateRange, indexMap, deviceIds));
            List<Map<String, Object>> series = new ArrayList<>();
            if (StringUtils.isNotBlank(pageData.getString("analysisSQL"))) {
                List<Map<String, Object>> dataList = alarmService.getAlarmAnalysisData(pageData);
                if (dataList != null && dataList.size() > 0) {
                    for (Map<String, Object> data : dataList) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("name", data.get("name"));
                        map1.put("value", String.valueOf(data.get("value")).split(","));
                        series.add(map1);
                    }
                }
            }
            map.put("series", series);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }
}
