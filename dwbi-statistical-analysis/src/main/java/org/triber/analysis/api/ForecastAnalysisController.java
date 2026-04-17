package org.triber.analysis.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triber.analysis.service.ForecastAnalysisService;
import org.triber.analysis.util.BaseController;
import org.triber.analysis.util.CreateAnalysisSQL;
import org.triber.analysis.util.PageData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author haojiang.
 * @Ddate 2020/9/11 17:47
 * @Description 预测分析
 */
@Slf4j
@RestController
@RequestMapping(value = "/forecastAnalysis", produces = MediaType.APPLICATION_JSON_VALUE)
public class ForecastAnalysisController extends BaseController {

    @Value("${DOWNLOAD_PATH}")
    private String saveDir;

    @Value("${devOps_system}")
    private String devOpsSystem;

    @Value("${network_system}")
    private String networkSystem;

    @Value("${ANALYSIS_PYTHON_PATH}")
    private String analysisPath;

    @Autowired
    private CreateAnalysisSQL analysisSQL;

    @Autowired
    private ForecastAnalysisService analysisService;

    //获取指标
    @PostMapping("/getIndicatorsData")
    public List getIndicatorsData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.getString("type"))) { //运维
                pageData.put("systemType", devOpsSystem);
            } else if ("network".equals(pageData.getString("type"))) { //网络
                pageData.put("systemType", networkSystem);
            }
            dataList = analysisService.getIndexData(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //获取资源
    @PostMapping("/getResourcesData")
    public List getResourcesData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.getString("type"))) { //运维
                pageData.put("systemType", devOpsSystem);
            } else if ("network".equals(pageData.getString("type"))) { //网络
                pageData.put("systemType", networkSystem);
            }
            dataList = analysisService.getResourceData(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * @Author haojiang
     * @Date 2020/10/15 11:49
     * @Description 首页指标预测
     */
    @PostMapping("/getForecastData")
    public Map<String, Object> getForecastData() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            pageData.put("limit", "6");
            pageData.put("is_shared", "1");
            pageData.put("time", yesterday);
            pageData.put("dataBase", "ucloud");
            //获取运维预测页面元素展示数据
            List<Map<String, Object>> devOpsPageList = analysisService.getHistoryAnalysisDada(pageData);
            for (int i = 0; i < devOpsPageList.size(); i++) {
                if (devOpsPageList.get(i).get("resource_name") != null && !"".equals(devOpsPageList.get(i).get("resource_name"))) {
                    devOpsPageList.get(i).put("index_name", devOpsPageList.get(i).get("index_name") + " - " + devOpsPageList.get(i).get("resource_name"));
                }
            }
            map.put("devOpsResult", devOpsPageList);
            //获取运维预测更多页面元素展示数据
            pageData.put("is_shared", null);
            pageData.put("limit", null);
            List<Map<String, Object>> devOpsMoreList = analysisService.getHistoryAnalysisDada(pageData);
            map.put("devOpsData", devOpsMoreList);

            pageData.put("limit", "6");
            pageData.put("is_shared", "1");
            pageData.put("time", yesterday);
            pageData.put("dataBase", "upm");
            //获取网络预测页面元素展示数据
            List<Map<String, Object>> networkPageList = analysisService.getHistoryAnalysisDada(pageData);
            for (int i = 0; i < networkPageList.size(); i++) {
                if (networkPageList.get(i).get("resource_name") != null && !"".equals(networkPageList.get(i).get("resource_name"))) {
                    networkPageList.get(i).put("index_name", networkPageList.get(i).get("index_name") + " - " + networkPageList.get(i).get("resource_name"));
                }
            }
            map.put("networkResult", networkPageList);
            //获取网络预测更多页面元素展示数据
            pageData.put("is_shared", null);
            pageData.put("limit", null);
            List<Map<String, Object>> networkMoreList = analysisService.getHistoryAnalysisDada(pageData);
            map.put("networkData", networkMoreList);

            map.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("devOpsData", new ArrayList<>());
            map.put("networkData", new ArrayList<>());
        }
        return map;
    }

    /**
     * @Author haojiang
     * @Date 2020/9/11 17:50
     * @Description 预测分析echarts数据
     */
    @PostMapping("/getForecastEChartsData")
    public Map<String, Object> getForecastEChartsData() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.getString("type"))) { //运维
                pageData.put("dataBase", "ucloud");
            } else if ("network".equals(pageData.getString("type"))) { //网络
                pageData.put("dataBase", "upm");
            }
            //处理周期、时间
            getAnalysisDateRange(pageData);
            //调用python预测脚本
            List<List<String>> allhistoryDate = (List<List<String>>) pageData.get("dateList");
            String[] allData = new String[allhistoryDate.size()];
            for (int i = 0; i < allhistoryDate.size(); i++) {
                pageData.put("analysisSQL", analysisSQL.getForecastSQL(pageData, allhistoryDate.get(i)));
                allData[i] = analysisService.getAnalysisDada(pageData) + "";
            }
            String allHistoryData = Arrays.toString(allData).replace("{value=", "").replace("}", "").replace(" ", "");
            getForecastValues(pageData, allHistoryData);

            List<List<String>> dateList = (List<List<String>>) pageData.get("dateList");
            JSONArray forecastArray = (JSONArray) pageData.get("forecastArray"); //预测值集合
            List<Map<String, Object>> dataList = new ArrayList<>();
            int lastIndex = dateList.get(0).size() - 1; //预测时间区间的最后一个时间
            if ("02".equals(pageData.getString("sampleFlag"))) { //样本外预测：下一个时间
                for (int i = 0; i < forecastArray.size(); i++) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("startTime", dateList.get(i).get(0));
                    data.put("endTime", dateList.get(i).get(lastIndex));
                    if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
                        data.put("forecastTime", LocalDateTime.parse(dateList.get(i).get(lastIndex), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plusDays(1).toString().replace("T", " ").toString());
                    } else if ("3".equals(pageData.getString("period"))) { //日
                        data.put("forecastTime", LocalDate.parse(dateList.get(i).get(lastIndex)).plusDays(1).toString());
                    } else if ("4".equals(pageData.getString("period"))) { //月
                        data.put("forecastTime", LocalDate.parse(dateList.get(i).get(lastIndex) + "-01").plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")).toString());
                    } else if ("5".equals(pageData.getString("period"))) { //年
                        data.put("forecastTime", Integer.valueOf(dateList.get(i).get(lastIndex)) + 1);
                    }
                    data.put("forecastValue", forecastArray.get(i));
                    dataList.add(data);
                }
            } else { //样本内预测：当前时间
                JSONArray actualArray = (JSONArray) pageData.get("actualArray"); //实际值集合
                JSONArray erroArray = (JSONArray) pageData.get("erroArray"); //误差值集合
                map.put("actualData", actualArray);
                map.put("erroData", erroArray);
                for (int i = 0; i < forecastArray.size(); i++) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("startTime", dateList.get(i).get(0));
                    data.put("endTime", dateList.get(i).get(lastIndex));
                    data.put("forecastTime", dateList.get(i).get(lastIndex));
                    data.put("forecastValue", forecastArray.get(i));
                    data.put("actualValue", actualArray.get(i));
                    data.put("erroValue", erroArray.get(i));
                    dataList.add(data);
                }
            }
            map.put("code", 200);
            map.put("data", dataList);
            map.put("date", pageData.get("dateRange"));
            map.put("forecastData", forecastArray);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
            map.put("errorInfo", "分析数据不足...");
        }
        return map;
    }

    //查询历史数据，调用python脚本进行预测
    private void getForecastValues(PageData pageData, String allHistoryData) {
        try {
            //调用python脚本
            log.info("调用python脚本参数：\n" + allHistoryData);
            String sampleFlag = pageData.getString("sampleFlag"); //样本内/外
//            allHistoryData ="[[1.00,0.00,2.00,1.00,4.00,5.00,5.00],[1.00,3.00,4.00,7.00,9.00,5.00,10.00],[1.00,5.00,2.00,3.00,4.00,8.00,9.00],[7.00,5.00,2.00,8.00,1.00,2.20,4.01],[7.00,5.00,2.00,8.00,1.00,2.20,4.01],[1.00,2.00,5.00,3.00,4.00,0.00,1.00]]";
            String[] args1 = new String[]{"python", analysisPath, allHistoryData, "01".equals(sampleFlag) ? "6" : "7", sampleFlag};
            Process process = Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int i = 0;
            String line;
            String[] split = new String[3];
            while ((line = in.readLine()) != null) {
                split[i] = line;
                System.out.println(line);
                i++;
            }
            if (split[0] != null) {
                JSONObject jsonObject = JSONObject.parseObject(split[0]);
                JSONArray forecastArray = jsonObject.getJSONArray("pre_value");
                JSONArray erroArray = jsonObject.getJSONArray("err_value");
                JSONArray actualArray = jsonObject.getJSONArray("fact_value");
                pageData.put("forecastArray", forecastArray); //预测值
                pageData.put("erroArray", erroArray); //误差值
                pageData.put("actualArray", actualArray); //实际值
                log.info("调用python脚本结果：\n预测值：" + forecastArray + "\n实际值：" + actualArray + "\n误差值：" + erroArray);
            } else {
                //历史样本数据为0，调整预测结果
                List<String> dateRange = (List<String>) pageData.get("dateRange");
                JSONArray forecastArray = new JSONArray();
                JSONArray erroArray = new JSONArray();
                JSONArray actualArray = new JSONArray();
                for (String date : dateRange) {
                    forecastArray.add(0);
                    erroArray.add(0);
                    actualArray.add(0);
                }
                pageData.put("forecastArray", forecastArray); //预测值
                pageData.put("erroArray", erroArray); //误差值
                pageData.put("actualArray", actualArray); //实际值
            }
            in.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author haojiang
     * @Date 2020/9/11 17:50
     * @Description 获取时间点
     */
    @PostMapping("/getTimePoints")
    public Map<String, Object> getTimePoints() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            StringBuilder builder = new StringBuilder();
            if ("1".equals(pageData.getString("period"))) { //周期：半小时
                for (int i = 0; i < 24; i++) {
                    if (i < 10) {
                        builder.append(",{\"value\":\"0" + i + ":00\",\"label\":\"0" + i + ":00\"}");
                        builder.append(",{\"value\":\"0" + i + ":30\",\"label\":\"0" + i + ":30\"}");
                    } else {
                        builder.append(",{\"value\":\"" + i + ":00\",\"label\":\"" + i + ":00\"}");
                        if (i < 24) {
                            builder.append(",{\"value\":\"" + i + ":30\",\"label\":\"" + i + ":30\"}");
                        }
                    }
                }
            } else if ("2".equals(pageData.getString("period"))) { //周期：一小时
                for (int i = 0; i < 24; i++) {
                    if (i < 10) {
                        builder.append(",{\"value\":\"0" + i + ":00\",\"label\":\"0" + i + ":00\"}");
                    } else {
                        builder.append(",{\"value\":\"" + i + ":00\",\"label\":\"" + i + ":00\"}");
                    }
                }
            }
            //将周期时间转为list对象
            List<Map<String, String>> dateList = JSON.parseObject("[" + builder.toString().substring(1) + "]", List.class);

            map.put("code", 200);
            map.put("data", dateList);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //获取echarts时间区间
    private static List<String> getDateRange(PageData pageData) {
        List<String> dateRange = new ArrayList<>();
        //处理半小时、一小时周期时间
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            if (StringUtils.isBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始时间为空
                dateRange.add(pageData.getString("endTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isBlank(pageData.getString("endTime"))) { //结束时间为空
                dateRange.add(pageData.getString("startTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始、结束时间都不为空
                if (pageData.getString("startTime").equals(pageData.getString("endTime"))) {
                    dateRange.add(pageData.getString("startTime"));
                } else {
                    if ("1".equals(pageData.getString("period"))) { //半小时
                        for (int i = 0; i < 48; i++) {
                            if (pageData.getString("endTime").equals(LocalTime.parse(pageData.getString("startTime")).plusMinutes(30 * i).toString())) {
                                dateRange.add(pageData.getString("endTime"));
                                break;
                            } else {
                                dateRange.add(LocalTime.parse(pageData.getString("startTime")).plusMinutes(30 * i).toString());
                            }
                        }
                    } else { //一小时
                        for (int i = 0; i < 24; i++) {
                            if (pageData.getString("endTime").equals(LocalTime.parse(pageData.getString("startTime")).plusHours(i).toString())) {
                                dateRange.add(pageData.getString("endTime"));
                                break;
                            } else {
                                dateRange.add(LocalTime.parse(pageData.getString("startTime")).plusHours(i).toString());
                            }
                        }
                    }
                }
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            if (StringUtils.isBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始时间为空
                dateRange.add(pageData.getString("endTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isBlank(pageData.getString("endTime"))) { //结束时间为空
                dateRange.add(pageData.getString("startTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始、结束时间都不为空
                if (pageData.getString("startTime").equals(pageData.getString("endTime"))) {
                    dateRange.add(pageData.getString("startTime"));
                } else {
                    long distance = 0;
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    switch (pageData.getString("period")) {
                        case "3":
                            startDate = LocalDate.parse(pageData.getString("startTime"));
                            endDate = LocalDate.parse(pageData.getString("endTime"));
                            distance = ChronoUnit.DAYS.between(startDate, endDate);
                            Stream.iterate(startDate, date -> {
                                return date.plusDays(1);
                            }).limit(distance + 1).forEach(filter -> {
                                dateRange.add(filter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            });
                            break;
                        case "4":
                            startDate = LocalDate.parse(pageData.getString("startTime") + "-01");
                            endDate = LocalDate.parse(pageData.getString("endTime") + "-01");
                            distance = ChronoUnit.MONTHS.between(startDate, endDate);
                            Stream.iterate(startDate, date -> {
                                return date.plusMonths(1);
                            }).limit(distance + 1).forEach(filter -> {
                                dateRange.add(filter.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                            });
                            break;
                        case "5":
                            startDate = LocalDate.parse(pageData.getString("startTime") + "-01-01");
                            endDate = LocalDate.parse(pageData.getString("endTime") + "-01-01");
                            distance = ChronoUnit.YEARS.between(startDate, endDate);
                            Stream.iterate(startDate, date -> {
                                return date.plusYears(1);
                            }).limit(distance + 1).forEach(filter -> {
                                dateRange.add(filter.format(DateTimeFormatter.ofPattern("yyyy")));
                            });
                            break;
                    }
                }
            }
        }
        pageData.put("dateRange", dateRange);
        return dateRange;
    }

    //获取预测分析时间区间
    private static void getAnalysisDateRange(PageData pageData) {
        int beforeNum = 5;
        List<List<String>> dateList = new ArrayList<>();
        List<String> dateCondition = new ArrayList();
        List<String> dateRange = getDateRange(pageData);
        //处理半小时、一小时周期时间
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String today = LocalDate.now().minusDays(1).format(formatter);
            if (StringUtils.isBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始时间为空
                for (int i = beforeNum; i >= 0; i--) {
                    dateCondition.add(LocalDate.parse(today).minusDays(i) + " " + pageData.getString("endTime"));
                }
                dateList.add(dateCondition);
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isBlank(pageData.getString("endTime"))) { //结束时间为空
                for (int i = beforeNum; i >= 0; i--) {
                    dateCondition.add(LocalDate.parse(today).minusDays(i) + " " + pageData.getString("startTime"));
                }
                dateList.add(dateCondition);
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始、结束时间都不为空
                if (pageData.getString("startTime").equals(pageData.getString("endTime"))) {
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(today).minusDays(i) + " " + pageData.getString("startTime"));
                    }
                    dateList.add(dateCondition);
                } else {
                    for (String time : dateRange) {
                        List<String> newDateList = new ArrayList<>();
                        for (int k = beforeNum; k >= 0; k--) {
                            String date = LocalDate.parse(today).minusDays(k).toString();
                            newDateList.add(date + " " + time);
                            dateCondition.add(date + " " + time);
                        }
                        dateList.add(newDateList);
                    }
                }
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            if (StringUtils.isBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始时间为空
                String endTime = pageData.getString("endTime");
                if ("3".equals(pageData.getString("period"))) {
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(endTime).minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                } else if ("4".equals(pageData.getString("period"))) {
                    endTime = endTime + "-01";
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(endTime).minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                    }
                } else if ("5".equals(pageData.getString("period"))) {
                    endTime = endTime + "-01-01";
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(endTime).minusYears(i).format(DateTimeFormatter.ofPattern("yyyy")));
                    }
                }
                dateList.add(dateCondition);
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isBlank(pageData.getString("endTime"))) { //结束时间为空
                String startTime = pageData.getString("startTime");
                if ("3".equals(pageData.getString("period"))) {
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(startTime).minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                } else if ("4".equals(pageData.getString("period"))) {
                    startTime = startTime + "-01";
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(startTime).minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                    }
                } else if ("5".equals(pageData.getString("period"))) {
                    startTime = startTime + "-01-01";
                    for (int i = beforeNum; i >= 0; i--) {
                        dateCondition.add(LocalDate.parse(startTime).minusYears(i).format(DateTimeFormatter.ofPattern("yyyy")));
                    }
                }
                dateList.add(dateCondition);
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始、结束时间都不为空
                if (pageData.getString("startTime").equals(pageData.getString("endTime") + "'")) {
                    for (int i = beforeNum; i >= 0; i--) {
                        if ("3".equals(pageData.getString("period"))) {
                            dateCondition.add(LocalDate.parse(pageData.getString("startTime")).minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        } else if ("4".equals(pageData.getString("period"))) {
                            dateCondition.add(LocalDate.parse(pageData.getString("startTime") + "-01").minusYears(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                        } else if ("5".equals(pageData.getString("period"))) {
                            dateCondition.add(String.valueOf(Integer.valueOf(pageData.getString("startTime")) - i));
                        }
                    }
                    dateList.add(dateCondition);
                } else {
                    if ("3".equals(pageData.getString("period"))) {
                        for (String date : dateRange) {
                            List<String> newDateList = new ArrayList<>();
                            for (int i = beforeNum; i >= 0; i--) {
                                newDateList.add(LocalDate.parse(date).minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                                dateCondition.add(LocalDate.parse(date).minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            }
                            dateList.add(newDateList);
                        }
                    } else if ("4".equals(pageData.getString("period"))) {
                        for (String date : dateRange) {
                            List<String> newDateList = new ArrayList<>();
                            for (int i = beforeNum; i >= 0; i--) {
                                newDateList.add(LocalDate.parse(date + "-01").minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                                dateCondition.add(LocalDate.parse(date + "-01").minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                            }
                            dateList.add(newDateList);
                        }
                    } else if ("5".equals(pageData.getString("period"))) {
                        for (String date : dateRange) {
                            List<String> newDateList = new ArrayList<>();
                            for (int i = beforeNum; i >= 1; i--) {
                                newDateList.add(LocalDate.parse(date + "-01-01").minusYears(i + 1).format(DateTimeFormatter.ofPattern("yyyy")));
                                dateCondition.add(LocalDate.parse(date + "-01-01").minusYears(i + 1).format(DateTimeFormatter.ofPattern("yyyy")));
                            }
                            dateList.add(newDateList);
                        }
                    }
                }
            }
        }
        pageData.put("dateList", dateList);
        pageData.put("dateCondition", dateCondition);
    }

}
