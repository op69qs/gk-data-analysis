package org.dockingProjects.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author haojiang.
 * @Ddate 2020/9/8 9:13
 * @Description 解析接口返回结果
 */
@Slf4j
public class AnalysisResultUtils {

    public static List<Map<String, Object>> analysisResult(String dataBase, String queryDay, Map<String, Object> dataMap) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        switch (dataBase) {
            case "ucloud":
                dataList = AnalysisResultUtils.getUCloudResult(queryDay, dataMap);
                break;
            case "upm":
                dataList = AnalysisResultUtils.getUPMResult(queryDay, dataMap);
                break;
            case "solarwinds":
                dataList = AnalysisResultUtils.getSolarWindsResult(dataMap);
                break;
        }
        return dataList;
    }

    /**
     * @Author haojiang
     * @Description 优云系统接口返回结果解析
     * @Date 2020/9/8 10:19
     */
    private static List<Map<String, Object>> getUCloudResult(String queryDay, Map<String, Object> map) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = dateFormat.format(new Date());
            String yesterday = StringUtils.isBlank(queryDay) ? LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : LocalDate.parse(queryDay).minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //获取前一天日期
            String date = String.valueOf(dateFormat.parse(yesterday + " 24:00:00").getTime()); //时间戳
            List<Map<String, Object>> content = (List<Map<String, Object>>) map.get("list");
            Map<String, Object> points = (Map<String, Object>) content.get(0).get("points");
            if (points != null && points.size() > 0) {
                points.forEach((k, v) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("ID", UuidUtil.get32UUID());
                    result.put("system_id", map.get("system_id"));
                    result.put("platform_id", map.get("platform_id"));
                    result.put("index_id", map.get("index_id"));
                    result.put("resource_id", map.get("resource_id"));
                    result.put("resource_code", map.get("api_resource_code"));
                    result.put("resource_ip", map.get("resource_ip"));
                    result.put("time", date.equals(k) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(k))));
                    result.put("content", String.valueOf(v));
                    result.put("addTime", addTime);
                    dataList.add(result);
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dataList;
    }

    /**
     * @Author haojiang
     * @Date 2020/9/8 10:19
     * @Description UPM系统接口返回结果解析
     */
    private static List<Map<String, Object>> getUPMResult(String queryDay, Map<String, Object> map) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<Map<String, Object>> dataList1 = new ArrayList<>();
        List<Map<String, Object>> dataList2 = new ArrayList<>();
        List<Map<String, Object>> dataList3 = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String addTime = dateFormat.format(new Date());
            String yesterday = StringUtils.isBlank(queryDay) ? LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : LocalDate.parse(queryDay).minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //获取前一天日期
            String date = String.valueOf(dateFormat.parse(yesterday + " 24:00:00").getTime()); //时间戳
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) map.get("list");
            if (String.valueOf(map.get("api_method")).contains("netPerformanceEventLog")) { //网络告警日志
                resultList.forEach(data -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("ID", UuidUtil.get32UUID());
                    result.put("category", data.get("name"));
                    result.put("name", data.get("name"));
                    result.put("lineId", data.get("lineId"));
                    result.put("netsegmentId1", data.get("netsegmentId1"));
                    result.put("netsegmentId2", data.get("netsegmentId2"));
                    result.put("level", data.get("eventLevel"));
                    result.put("time", date.equals(String.valueOf(data.get("date"))) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(String.valueOf(data.get("date"))))));
                    result.put("content", data.get("condition"));
                    result.put("probeId", data.get("probeId"));
                    result.put("addTime", addTime);
                    dataList1.add(result);
                });
            } else if (String.valueOf(map.get("api_method")).contains("alarmLogAbnormalBehavior")) { //异常行为告警日志
                resultList.forEach(data -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("ID", UuidUtil.get32UUID());
                    result.put("category", data.get("category"));
                    result.put("name", data.get("name"));
                    result.put("clientIpAddr", data.get("clientIpAddr"));
                    result.put("clientPort", data.get("clientPort"));
                    result.put("clientNetsegmentId", data.get("clientNetsegmentId"));
                    result.put("serverIpAddr", data.get("serverIpAddr"));
                    result.put("serverPort", data.get("serverPort"));
                    result.put("serverNetsegmentId", data.get("serverNetsegmentId"));
                    result.put("ipAddr", data.get("ipAddr"));
                    result.put("netsegmentId", data.get("netsegmentId"));
                    result.put("level", data.get("level"));
                    result.put("time", date.equals(String.valueOf(data.get("date"))) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(String.valueOf(data.get("date"))))));
                    result.put("content", data.get("triggerCondition"));
                    result.put("probeId", data.get("probeId"));
                    result.put("addTime", addTime);
                    dataList2.add(result);
                });
            } else { //应用数据
                if (String.valueOf(map.get("api_index_code")).equals("upm.connection.rstRate,upm.connection.noResponseRate")) { //建立连接重置率、无响应率
                    for (Map<String, Object> data : resultList) {
                        //建立连接重置率值
                        if (data.get("connectionRstRate") != null) {
                            Map<String, Object> result = new HashMap<>();
                            result.put("ID", UuidUtil.get32UUID());
                            result.put("system_id", map.get("system_id"));
                            result.put("platform_id", map.get("platform_id"));
                            result.put("index_id", map.get("index_id"));
                            result.put("resource_id", map.get("resource_id"));
                            result.put("resource_code", data.get("netsegmentId"));
                            result.put("resource_ip", data.get("ipAddr"));
                            result.put("time", date.equals(String.valueOf(data.get("date"))) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(String.valueOf(data.get("date"))))));
                            result.put("content", data.get("connectionRstRate"));
                            result.put("addTime", addTime);
                            dataList3.add(result);
                        }
                        //建立连接无响应率值
                        if (data.get("connectionNoresponseRate") != null) {
                            Map<String, Object> result = new HashMap<>();
                            result.put("ID", UuidUtil.get32UUID());
                            result.put("system_id", map.get("system_id"));
                            result.put("platform_id", map.get("platform_id"));
                            result.put("index_id", map.get("index_id"));
                            result.put("resource_id", map.get("resource_id"));
                            result.put("resource_code", data.get("netsegmentId"));
                            result.put("resource_ip", data.get("ipAddr"));
                            result.put("time", date.equals(String.valueOf(data.get("date"))) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(String.valueOf(data.get("date"))))));
                            result.put("content", data.get("connectionNoresponseRate"));
                            result.put("addTime", addTime);
                            dataList3.add(result);
                        }
                    }
                } else if (String.valueOf(map.get("api_index_code")).equals("upm.txPacket,upm.rxPacket")) { //接收包数、发送包数
                    for (Map<String, Object> data : resultList) {
                        //发送数据包数取值
                        if (data.get("txPacket") != null) {
                            Map<String, Object> result = new HashMap<>();
                            result.put("ID", UuidUtil.get32UUID());
                            result.put("system_id", map.get("system_id"));
                            result.put("platform_id", map.get("platform_id"));
                            result.put("index_id", map.get("index_id"));
                            result.put("resource_id", map.get("resource_id"));
                            result.put("resource_code", data.get("netsegmentId"));
                            result.put("resource_ip", data.get("ipAddr"));
                            result.put("time", date.equals(String.valueOf(data.get("date"))) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(String.valueOf(data.get("date"))))));
                            result.put("content", data.get("txPacket"));
                            result.put("addTime", addTime);
                            dataList3.add(result);
                        }
                        //接收数据包数值
                        if (data.get("rxPacket") != null) {
                            Map<String, Object> result = new HashMap<>();
                            result.put("ID", UuidUtil.get32UUID());
                            result.put("system_id", map.get("system_id"));
                            result.put("platform_id", map.get("platform_id"));
                            result.put("index_id", map.get("index_id"));
                            result.put("resource_id", map.get("resource_id"));
                            result.put("resource_code", data.get("netsegmentId"));
                            result.put("resource_ip", data.get("ipAddr"));
                            result.put("time", date.equals(String.valueOf(data.get("date"))) ? (yesterday + " 24:00:00") : dateFormat.format(new Date(Long.valueOf(String.valueOf(data.get("date"))))));
                            result.put("content", data.get("rxPacket"));
                            result.put("addTime", addTime);
                            dataList3.add(result);
                        }
                    }
                }
            }
            resultMap.put("netPerformanceEventLog", dataList1); //网络性能日志表
            resultMap.put("alarmLogAbnormalBehavior", dataList2); //异常行为日志表
            resultMap.put("otherData", dataList3); //其他数据表(非日志)
            dataList.add(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dataList;
    }

    /**
     * @Author haojiang
     * @Date 2020/9/8 10:19
     * @Description sloarwinds系统接口返回结果解析
     */
    private static List<Map<String, Object>> getSolarWindsResult(Map<String, Object> map) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        // TODO: 2020/10/23 预留接口数据处理

        return dataList;
    }
}
