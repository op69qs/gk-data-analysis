package org.dockingProjects.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dockingProjects.service.ClickhouseConfigService;
import org.dockingProjects.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HaoJiang.
 * @Ddate 2020/8/4 14:31
 * solarwinds系统调用接口线程池配置
 */
@Slf4j
@Component
public class ApiTaskUtil implements Serializable {

    private String processId;

    private volatile String dataBase;

    private String queryDay;

    @Value("${ucloudProcess}")
    private String ucloudProcess;

    @Value("${upmProcess}")
    private String upmProcess;

    @Value("${solarwindsProcess}")
    private String solarwindsProcess;

    @Value("${restartCount}")
    private int restartCount;

    @Value("${fileSavePath}")
    private String fileSavePath;

    @Value("${UCLOUD_ALERT}")
    private String alarmUrl;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigService mysqlConfigService;

    @Autowired
    private ClickhouseConfigService clickhouseConfigService;

    @Autowired
    private ProcessUtils processUtils;

    @Autowired
    private ApplicationArguments applicationArguments;

    public void executeExecutor() {
        if (applicationArguments.containsOption("processId")) {
            processId = applicationArguments.getOptionValues("processId").get(0);
            if (redisUtil.get("queryDay") != null) {
                //历史跑批
                queryDay = String.valueOf(redisUtil.get("queryDay"));
                log.info("queryDay=" + queryDay);
            }
            //设置进程ID到redis
            redisUtil.set("PROCESS_ID_" + processId, processUtils.getProcessID());
        }

        try {
            //从redis获取进程、线程、系统信息
            Map<Object, Object> threadMap = redisUtil.hmget("THREAD_" + processId);
            //确保配置参数读取到redis中后，再执行定时任务调用
            if ("OK".equals(redisUtil.get("SYSTEM_CONFIG_" + processId)) && "OK".equals(redisUtil.get("INTERFACE_CONFIG_" + processId))) {
                log.info("process-" + processId + "-thread-" + threadMap.get("ID") + " executor start>>>>>>>>>>>>>>>>>>>>>>>>>");

                //TODO 根据系统去创建数据表(可根据实际取数情况变动)
                Map<String, Object> tableMap = new HashMap<>();
                Map<String, Object> procedurMap = new HashMap<>();
                tableMap.put("processId", processId);
                String yesterday = StringUtils.isBlank(queryDay) ? LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : LocalDate.parse(queryDay).minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //获取前一天日期
                if (ucloudProcess.equals(processId)) { //优云系统
                    dataBase = "ucloud";
                    String nowMonth = yesterday.substring(0, 7).replace("-", "");
                    tableMap.put("dataBase", dataBase);
                    tableMap.put("tableMonth", nowMonth);
                    tableMap.put("tableDesc", "优云系统数据(" + nowMonth + ")");
                    mysqlConfigService.createTable(tableMap); //mysql数据库建表
                    clickhouseConfigService.createTable(tableMap); //clickhouse数据库建表
                    //调用汇总存储过程需要
                    procedurMap.put("dataBase", dataBase);
                    procedurMap.put("alarm_data", "ucloud.api_interface_alarm_data");
                    procedurMap.put("index_data", "ucloud.api_interface_system_data" + nowMonth);
                    procedurMap.put("date", yesterday);

                    //对监测器告警数据取数
                    List<Map<String, Object>> alarmList = new ArrayList<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String begin = String.valueOf(dateFormat.parse(yesterday + " 00:00:00").getTime()); //起始日期
                    String end = String.valueOf(dateFormat.parse(yesterday + " 23:59:59").getTime()); //结束日期
                    alarmUrl += "&source=基础资源监控&begin=" + begin + "&end=" + end;
                    log.info("[" + alarmUrl + "]：获取优云系统监测器指标告警数据......");
                    ResponseEntity<String> responseEntity = restTemplate.exchange(alarmUrl, HttpMethod.GET, null, String.class);
                    if (responseEntity.getStatusCodeValue() == 200) {
                        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
                        List<Map<String, Object>> records = (List<Map<String, Object>>) jsonObject.get("records");
                        if (records.size() > 0) {
                            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            records.forEach(map -> {
                                map.put("ID", UuidUtil.get32UUID());
                                map.put("firstOccurTime", dateFormat.format(new Date(Long.valueOf(String.valueOf(map.get("firstOccurTime"))))));
                                map.put("lastOccurTime", dateFormat.format(new Date(Long.valueOf(String.valueOf(map.get("lastOccurTime"))))));
                                map.put("addTime", now);
                                alarmList.add(map);
                            });
                            mysqlConfigService.insertAlarmData(alarmList);
                            clickhouseConfigService.insertAlarmData(alarmList);
                        }
                    } else {
                        log.error("优云系统监测器告警取数失败：[" + alarmUrl + "]");
                    }
                } else if (upmProcess.equals(processId)) { //UPM系统
                    dataBase = "upm";
                    tableMap.put("dataBase", dataBase);
                    tableMap.put("tableMonth", yesterday.replace("-", ""));
                    tableMap.put("tableDesc", "UPM系统数据(" + yesterday.replace("-", "") + ")");
                    mysqlConfigService.createTable(tableMap); //mysql数据库建表
                    clickhouseConfigService.createTable(tableMap); //clickhouse数据库建表
                    //调用汇总存储过程需要
                    procedurMap.put("dataBase", dataBase);
                    procedurMap.put("alarmlog", "upm.alarmlogabnormalbehavior_" + yesterday.replace("-", ""));
                    procedurMap.put("netper", "upm.netperformanceeventlog_" + yesterday.replace("-", ""));
                    procedurMap.put("index_data", "upm.api_interface_system_data" + yesterday.replace("-", ""));
                    procedurMap.put("date", yesterday);
                }

                // 创建一个线程池，这里需要设置线程corePoolSize，如果单线程则设置为1
                int maxPoolSize = Integer.valueOf(redisUtil.get("CORE_POOL_SIZE_" + processId).toString()); //最大线程数
                int corePoolSize = (maxPoolSize / 2) == 0 ? 1 : (maxPoolSize / 2); //核心线程数
                ThreadPoolExecutor executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 3, TimeUnit.HOURS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.AbortPolicy());
                taskSchedule(executorService, null);//调用线程池方法
                executorService.shutdown();// 关闭线程池
                //检查多线程任务是否全部执行完毕
                if (executorService.awaitTermination(40, TimeUnit.MINUTES)) {
                    //调用存储过程汇总数据
                    log.info("执行存储过程进行数据汇总......");
                    mysqlConfigService.callProcedure(procedurMap);
                    //记录进程调用接口结果状态JSONObject
                    JSONObject processStatus = new JSONObject();
                    processStatus.put("task_id", processId);
                    processStatus.put("task_status", "0"); //进程任务执行正常
                    //将进程执行的接口状态写入文本
                    fileUtil.write(fileSavePath, processId + ".txt", processStatus.toJSONString());
                    log.info("process-" + processId + "-thread-" + threadMap.get("ID") + " executor end>>>>>>>>>>>>>>>>>>>>>>>>>");
                    //任务执行完毕，关闭子进程
                    processUtils.killProcessByPid(String.valueOf(redisUtil.get("PROCESS_ID_" + processId)));
                }
            } else {
                log.info("redis configuration parameters reading failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("" + e);
        }
    }

    /**
     * 定时任务调度
     */
    private void taskSchedule(ExecutorService executorService, List<Map<String, Object>> interfaceList) {
        // 获取要执行的任务接口
        if (interfaceList == null || interfaceList.size() == 0) {
            interfaceList = (List<Map<String, Object>>) redisUtil.lGet("INTERFACE_" + processId, 0, -1).get(0);
        }
        log.info("线程任务执行中...");
        //轮询dataList并启动线程调用
        for (Map<String, Object> data : interfaceList) {
            try {
                // 执行任务并获取Future对象
                if (data.get("ID") == null) {
                    data.put("ID", UuidUtil.get32UUID());
                }
                data.put("dataBase", dataBase);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveData(data);
                    }
                });
            } catch (Exception e) {
                log.error("遍历接口参数错误：【参数：" + data + "】\n" + e);
            }
        }
    }

    private void saveData(Map<String, Object> map) {
        HttpHeaders headers = (HttpHeaders) map.get("headers");
        String url = String.valueOf(map.get("api_method"));
        HttpMethod httpMethod = (HttpMethod) map.get("httpMethod");
        JSONObject paramaters = (JSONObject) map.get("paramaters");
        map.put("addTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        try {
            //获取数据的时间范围需要根据需求自己处理
            String params = "";
            String yesterday = StringUtils.isBlank(queryDay) ? LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : LocalDate.parse(queryDay).minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //获取前一天日期
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (ucloudProcess.equals(processId)) { //优云系统
                paramaters.put("start", String.valueOf(dateFormat.parse(yesterday + " " + map.get("api_time_start")).getTime())); //起始日期
                paramaters.put("end", String.valueOf(dateFormat.parse(yesterday + " " + map.get("api_time_end")).getTime())); //结束日期
            } else if (upmProcess.equals(processId)) { //UPM系统
                url = url.replace("@date", (yesterday + " " + map.get("api_time_start")) + " - " + (yesterday + " " + map.get("api_time_end")));
                params = url.substring(url.indexOf("{"));
                url = url.substring(0, url.indexOf("=") + 1) + "{params}";
            }
            //转换http请求类型为代码
            if (httpMethod == HttpMethod.POST) {
                map.put("httpMethod", "1");
            } else if (httpMethod == HttpMethod.GET) {
                map.put("httpMethod", "2");
            }
            //将headers转换为HttpHeaders对象格式的字符串
            map.put("headers", headers.toString());
            //将paramaters转换为JSONObject对象格式的字符串
            map.put("paramaters", paramaters.toJSONString());
            ResponseEntity<String> responseEntity = null;
            if (ucloudProcess.equals(map.get("process_id"))) { //优云系统
                responseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(paramaters, headers), String.class);
            } else if (upmProcess.equals(map.get("process_id"))) { //UPM系统
                responseEntity = restTemplate.exchange(url, httpMethod, null, String.class, params);
            }
            int statusCode = responseEntity.getStatusCodeValue();
            map.put("statusCode", String.valueOf(statusCode));
            if (statusCode == 200) {
                map.put("content", responseEntity.getBody());
                //TODO 根据不同取数系统的返回数据量及格式修改代码
                if (ucloudProcess.equals(processId)) { //优云系统
                    log.info("[url：" + url + "，paramaters:" + paramaters + "]接口请求成功,正在插入数据......");
                    String nowMonth = yesterday.substring(0, 7).replace("-", "");
                    map.put("tableMonth", nowMonth);
                    List<Map<String, Object>> results = JSON.parseObject(responseEntity.getBody(), List.class);
                    if (results != null) {
                        map.put("list", results);
                        //格式化数据
                        map.put("list", AnalysisResultUtils.analysisResult(dataBase, queryDay, map));
                        mysqlConfigService.insertData(map);
                        clickhouseConfigService.insertData(map);
                    }
                } else if (upmProcess.equals(processId)) { //UPM系统
                    log.info("[url：" + url + "，params:" + params + "]接口请求成功,正在插入数据......");
                    map.put("tableMonth", yesterday.replace("-", ""));
                    Map<String, Object> results = JSON.parseObject(responseEntity.getBody(), Map.class);
                    if (results != null && "true".equals(String.valueOf(results.get("success")))) {
                        //获取返回结果集
                        List<Map<String, Object>> resultList = (List<Map<String, Object>>) results.get("data");
                        int start = 0;
                        List<Map<String, Object>> newDataList = new ArrayList<>();
                        for (Map<String, Object> result : resultList) {
                            newDataList.add(result);
                            if (start > 0 && start % 3000 == 0) {
                                map.put("list", newDataList);
                                //格式化数据
                                map.put("list", AnalysisResultUtils.analysisResult(dataBase, queryDay, map));
                                mysqlConfigService.insertData(map);
                                clickhouseConfigService.insertData(map);
                                newDataList.clear();
                            }
                            start++;
                        }
                        if (newDataList.size() > 0) {
                            map.put("list", newDataList);
                            map.put("list", AnalysisResultUtils.analysisResult(dataBase, queryDay, map));
                            mysqlConfigService.insertData(map);
                            clickhouseConfigService.insertData(map);
                        }
                    }
                }
            } else {
                //将请求参数插入状态记录表
                mysqlConfigService.insertApiRecords(map);
                log.info("api接口调用记录插入...");
            }
        } catch (Exception e) {
            log.error("调用接口错误：" + e);
            //将请求参数插入状态记录表
            mysqlConfigService.insertApiRecords(map);
            log.info("api接口调用记录插入...");
        }
    }

    /**
     * 重新调用请求失败的接口
     */
//    private void restartApi(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("dataBase", dataBase);
//        params.put("restartCount", restartCount);
//        //查询api调用失败的接口
//        List<Map<String, Object>> faildList = configService.selectFailedApi(params);
//        for (int i = 0; i < faildList.size(); i++) {
//            Map<String, Object> faildMap = faildList.get(i);
//            //转换HttpHeaders请求头
//            HttpHeaders headers = new HttpHeaders();
//            JSON.parseObject(faildMap.get("api_headers").toString(), Map.class).forEach((key, value) -> {
//                headers.addAll(key.toString(), JSON.parseObject(value.toString(), LinkedList.class));
//            });
//            faildList.get(i).put("headers", headers);
//
//            //转换HttpMethod请求类型
//            faildList.get(i).put("httpMethod", HttpClinetUtil.getHttpMethod().get(faildMap.get("api_type")));
//
//            //转换api参数格式
//            faildList.get(i).put("paramaters", JSON.parseObject(faildMap.get("api_params").toString()));
//        }
//
//        //如果没有调用失败的接口，则不需要执行失败重调定时任务
//        if (faildList == null || faildList.size() == 0) {
//            initialNum = restartCount;
//            return;
//        }
//
//        //调用线程池方法
//        List<Future> futures = taskSchedule(scheduledThreadPoolExecutor, faildList);
//
//        //获取当前时间
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String nowMonth = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
//
//        List<Map<String, Object>> dataList = new ArrayList<>();
//        List<Map<String, Object>> recordList = new ArrayList<>();
//        for (Future future : futures) {
//            try {
//                Map<String, Object> futureMap = (Map<String, Object>) future.get();
//                futureMap.put("addTime", now);
//                if ("200".equals(futureMap.get("statusCode"))) { //API请求成功
//                    futureMap.put("restartCount", 0);
//                    futureMap.put("content", fileUtil.read(futureMap.get("filePath").toString()).get(0));
//                    dataList.add(futureMap);
//                } else {
//                    futureMap.put("restartCount", 1);
//                }
//                recordList.add(futureMap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        //更新API记录表
//        if (recordList != null && recordList.size() > 0) {
//            Map<String, Object> records = new HashMap<>();
//            records.put("dataBase", dataBase);
//            records.put("records", recordList);
//            configService.updateFailedApiData(records);
//            log.info("api接口调用记录更新...");
//
//            //将读取到的数据插入数据表
//            if (dataList.size() > 0) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("dataBase", dataBase);
//                map.put("tableName", "api_interface_system_data" + nowMonth);
//                map.put("list", dataList);
//                configService.insertData(map);
//                log.info("api接口返回数据插入...");
//            }
//        }
//    }

}
