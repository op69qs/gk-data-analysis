package org.triber.analysis.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.triber.analysis.service.ForecastAnalysisService;
import org.triber.analysis.service.ForecastThresholdParameterService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/24 14:50
 * @Description 阈值参数维护设置
 */
@Slf4j
@Component
@EnableScheduling
public class ThresholdPredictionJob {

    @Value("${ANALYSIS_PYTHON_PATH}")
    private String analysisPath;

    @Autowired
    private CreateAnalysisSQL analysisSQL;

    @Autowired
    private ForecastAnalysisService analysisService;

    @Autowired
    private ForecastThresholdParameterService parameterService;

    @Scheduled(cron = "${forecastSchedule}")
    public void forecastValuesSchedule() {
        this.saveForecastValues();
    }

    //将预测的指标值入库
    public void saveForecastValues() {
        log.info("*********定时任务开始执行参数维护预测功能*********");
        PageData pageData = new PageData();
        pageData.put("sampleFlag", "01"); //样本内预测
        pageData.put("is_shared", "0"); //默认未超出阈值，不告警
        pageData.put("alarm_time", LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); //默认预测昨天的数据
        List<String> historyDate = new ArrayList<>(); //时间区间
        List<List<String>> allhistoryDate = new ArrayList<>();
        for (int i = 6; i > 0; i--) {
            historyDate.add(LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        allhistoryDate.add(historyDate);

        //查询需要定时预测的指标
        List<Map<String, Object>> list = parameterService.getThresholdData(pageData);
        if (list.size() > 0) {
            list.forEach(data -> {
                pageData.putAll(data);
                pageData.put("dataBase", "devOps".equals(data.get("business_type")) ? "ucloud" : "upm");
                String[] allData = new String[allhistoryDate.size()];
                for (int i = 0; i < allhistoryDate.size(); i++) {
                    pageData.put("analysisSQL", analysisSQL.geThresholdPredictionSQL(pageData, allhistoryDate.get(i)));
                    allData[i] = analysisService.getAnalysisDada(pageData) + "";
                }
                String allHistoryData = Arrays.toString(allData).replace("{value=", "").replace("}", "").replace(" ", "");
                parameterService.insertForecastData(getForecastValues(pageData, allHistoryData)); //预测值入库
            });
        }
    }

    //查询历史数据，调用python脚本进行预测
    private Map<String, Object> getForecastValues(PageData pageData, String allHistoryData) {
        try {
            //调用python脚本
            String[] args1 = new String[]{"python", analysisPath, allHistoryData, "6", pageData.getString("sampleFlag")};
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
                pageData.put("forecastValue", forecastArray.get(0)); //预测值
                pageData.put("errorValue", erroArray.get(0)); //误差值
                pageData.put("actualValue", actualArray.get(0)); //实际值
                this.compareEstimate(pageData);
            } else {
                pageData.put("forecastValue", "0"); //预测值
                pageData.put("errorValue", "0"); //误差值
                pageData.put("actualValue", "0"); //实际值
                this.compareEstimate(pageData);
            }
            in.close();
            process.waitFor();
        } catch (Exception e) {
            log.error("预测报错：" + e);
            e.printStackTrace();
            pageData = new PageData();
        }
        return pageData;
    }

    //按照维护的阈值进行比较
    private void compareEstimate(PageData pageData) {
//        double errorValue = Double.valueOf(pageData.getString("errorValue")); //误差值
//        double operatorValue = Double.valueOf(pageData.getString("operator_value")); //阈值比较值
        pageData.put("is_shared", "1");
        /*if ("0".equals(pageData.get("threshold_type"))) { //阈值类型：数值
            switch (pageData.getString("operator")) {
                case "1":
                    pageData.put("is_shared", (errorValue > operatorValue) ? "1" : "0");
                    break;
                case "2":
                    pageData.put("is_shared", (errorValue < operatorValue) ? "1" : "0");
                    break;
                case "3":
                    pageData.put("is_shared", (errorValue != operatorValue) ? "1" : "0");
                    break;
            }
        } else { //阈值类型：百分比
            switch (pageData.getString("operator")) {
                case "1":
                    pageData.put("is_shared", ((errorValue / 100) * 100 > operatorValue) ? "1" : "0");
                    break;
                case "2":
                    pageData.put("is_shared", ((errorValue / 100) * 100 < operatorValue) ? "1" : "0");
                    break;
                case "3":
                    pageData.put("is_shared", ((errorValue / 100) * 100 != operatorValue) ? "1" : "0");
                    break;
            }
        }*/
    }
}
