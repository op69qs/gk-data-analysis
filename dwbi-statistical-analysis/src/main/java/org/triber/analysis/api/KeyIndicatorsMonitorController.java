package org.triber.analysis.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triber.analysis.service.KeyIndicatorsMonitorService;
import org.triber.analysis.util.BaseController;
import org.triber.analysis.util.CreateAnalysisSQL;
import org.triber.analysis.util.PageData;

import java.util.*;

/**
 * @author haojiang.
 * @Ddate 2020/9/16 15:53
 * @Description 重点指标监测视图
 */
@Slf4j
@RestController
@RequestMapping(value = "/keyIndicatorsMonitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeyIndicatorsMonitorController extends BaseController {

    @Value("${devOps_system}")
    private String devOpsSystem;

    @Value("${network_system}")
    private String networkSystem;

    @Autowired
    private CreateAnalysisSQL analysisSQL;

    @Autowired
    private KeyIndicatorsMonitorService keyIndicatorsService;

    //获取重点指标
    @PostMapping("/getKeyIndicators")
    public List getKeyIndicators() {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.getString("type"))) { //运维
                pageData.put("systemType", "'" + devOpsSystem.replace(",", "','") + "'");
            } else if ("network".equals(pageData.getString("type"))) { //网络
                pageData.put("systemType", "'" + networkSystem.replace(",", "','") + "'");
            }
            dataList = keyIndicatorsService.getKeyIndicators(pageData);
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).put("label", dataList.get(i).get("label").replace("告警次数", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //获取指标平台
    @PostMapping("/getIndexPlatform")
    public List getIndexPlatform() {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            PageData pageData = this.getPageData();
            if ("devOps".equals(pageData.getString("type"))) { //运维
                pageData.put("systemType", devOpsSystem);
            } else if ("network".equals(pageData.getString("type"))) { //网络
                pageData.put("systemType", networkSystem);
            }
            pageData.put("is_keyPoint", "1");
            dataList = keyIndicatorsService.getIndexPlatform(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //重点指标峰值资源序时分析
    @PostMapping("/getkeyIndicatorsPeakValueAnalysis")
    public Map<String, Object> getkeyIndicatorsPeakValueAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            //重点指标峰值资源序时分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            //重点指标峰值资源序时分析series
            pageData.put("analysisSQL", analysisSQL.getKeyIndicatorSQL(pageData, dateRange)); //获取组装的查询SQL
            List<Map<String, Object>> dataList = new ArrayList<>();
            if ("1".equals(pageData.get("period"))) { //半小时(去clickhouse查询)
                dataList = this.getSQLResults(pageData.getString("analysisSQL"));
            } else {
                dataList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            }
            String[] series = new String[dateRange.size()];
            if (dataList.size() > 0) {
                for (int i = 0; i < dateRange.size(); i++) {
                    for (Map<String, Object> data : dataList) {
                        if (dateRange.get(i).equals(data.get("dateTime"))) {
                            series[i] = String.valueOf(data.get("value"));
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < dateRange.size(); i++) {
                    series[i] = "0";
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

    //重点状态指标分析
    @PostMapping("/getkeyIndicatorsStatusAnalysis")
    public Map<String, Object> getkeyIndicatorsStatusAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            List<String> dateRange = analysisSQL.getDateRange(pageData); //时间区间
            //查询重点指标状态分析数据series
            pageData.put("analysisSQL", analysisSQL.getKeyStatusIndicatorSQL(pageData, dateRange)); //获取组装的查询SQL
            List<Map<String, Object>> dataList = this.getSQLResults(pageData.getString("analysisSQL")); //keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            map.put("series", dataList);

            //重点指标状态分析legend
            List<String> legendList = new ArrayList<>();
            for (Map<String, Object> data : dataList) {
                legendList.add(String.valueOf(data.get("name")));
            }
            map.put("legend", legendList);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //重点指标资源排名分析
    @PostMapping("/getkeyIndicatorsResourceRankAnalysis")
    public Map<String, Object> getkeyIndicatorsResourceRankAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            List<String> dateRange = analysisSQL.getDateRange(pageData); //时间区间
            //查询重点指标状态分析数据
            pageData.put("analysisSQL", analysisSQL.getResourceOrderSQL(pageData, dateRange)); //获取组装的查询SQL
            List<Map<String, Object>> dataList = new ArrayList<>();
            if ("1".equals(pageData.get("period"))) { //半小时(去clickhouse查询)
                dataList = this.getSQLResults(pageData.getString("analysisSQL"));
            } else {
                dataList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            }
            //重点指标资源排名分析Y轴刻度、数据
            List<String> seriesList = new ArrayList<>();
            List<String> resourceList = new ArrayList<>();
            for (Map<String, Object> data : dataList) {
                seriesList.add(String.valueOf(data.get("value")));
                resourceList.add(String.valueOf(data.get("name")));
            }
            map.put("yAxis", resourceList);
            map.put("series", seriesList);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //数据包发送接收情况分析
    @PostMapping("/getDataPacketSendAndReceiveAnalysis")
    public Map<String, Object> getDataPacketSendAndReceiveAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            //数据包发送接收情况分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            //数据包发送接收情况分析series
            List<String> sendPacketData = new ArrayList<>(); //数据发送包数据
            List<String> receivePacketrData = new ArrayList<>(); //数据接收包数据
            pageData.put("analysisSQL", analysisSQL.getDataPacketSendAndReceiveSQL(pageData, dateRange)); //获取组装的查询SQL
            List<Map<String, String>> dataList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            for (Map<String, String> data : dataList) {
                sendPacketData.add(data.get("sendData"));
                receivePacketrData.add(data.get("receiveData"));
            }
            map.put("sendPacketData", sendPacketData);
            map.put("receivePacketrData", receivePacketrData);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //网络(DNS)性能警报分析
    @PostMapping("/getNetworkPerformanceAnalysis")
    public Map<String, Object> getNetworkPerformanceAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            //网络(DNS)性能警报分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            //网络(DNS)性能警报分析legend
            List<String> legendIds = new ArrayList<>(); //网络(DNS)性能告警指标主键集合
            List<String> legendData = new ArrayList<>(); //网络(DNS)性能告警指标名称集合
            legendIds.add("ea0c68b5192545e5828f69ff3d682ebd");
            legendData.add("金融城域网交换机无流量告警次数");
            map.put("legend", legendData);

            //网络(DNS)性能警报分析series
            String[] valueArray = new String[dateRange.size()];
            pageData.put("analysisSQL", analysisSQL.getNetworkPerformanceSQL(pageData, dateRange, legendIds)); //获取组装的查询SQL
            List<Map<String, Object>> dataList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (dataList.size() > 0) {
                for (int i = 0; i < dateRange.size(); i++) {
                    for (Map<String, Object> data : dataList) {
                        if (dateRange.get(i).equals(data.get("dateTime"))) {
                            valueArray[i] = String.valueOf(data.get("alarm_count"));
                            break;
                        }
                    }
                    if (valueArray[i] == null) {
                        valueArray[i] = "0";
                    }
                }
            } else {
                for (int i = 0; i < dateRange.size(); i++) {
                    valueArray[i] = "0";
                }
            }
            List<Map<String, Object>> series = new ArrayList<>();
            Map<String, Object> serieMap = new HashMap<>();
            serieMap.put("name", legendData.get(0));
            serieMap.put("value", valueArray);
            series.add(serieMap);
            map.put("series", series);
            map.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
        }
        return map;
    }

    //建立连接情况构成分析
    @PostMapping("/getConnectionCompositionAnalysis")
    public Map<String, Object> getConnectionCompositionAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            //建立连接情况构成分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            if ("0".equals(pageData.get("compositionId"))) { //重置率
                pageData.put("index_id", "2ead4ed8fce24ccdb68c3825b8b26f98");
            } else { //无响应率
                pageData.put("index_id", "f8491d3b2e66424eb06ee56281b2c717");
            }

            //组装各个时点链路top3的SQL
            String[] topIdArray = new String[3]; //链路ID
            String[] topNameArray = new String[3]; //链路名称
            pageData.put("analysisSQL", analysisSQL.getConnectionTop3SQL(pageData, dateRange));
            List<Map<String, Object>> top3List = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            //若查出资源为空，则直接返回
            if (top3List.size() == 0) {
                return map;
            }
            for (int i = 0; i < top3List.size(); i++) {
                topIdArray[i] = top3List.get(i).get("resource_code").toString();
                topNameArray[i] = top3List.get(i).get("resource_desc").toString();
            }

            //建立连接情况构成分析legend
            List<String> legend = new ArrayList<>();
            for (String topName : topNameArray) {
                if ("0".equals(pageData.get("compositionId"))) { //重置率
                    legend.add(topName + "-重置率");
                    legend.add(topName + "-无响应率");
                } else {
                    legend.add(topName + "-无响应率");
                    legend.add(topName + "-重置率");
                }
            }
            map.put("legend", legend);

            //建立连接情况构成分析series
            String[] compositionArray = new String[]{"2ead4ed8fce24ccdb68c3825b8b26f98", "f8491d3b2e66424eb06ee56281b2c717"}; //重置率、无响应率

            //获取组装的top1链路重置率查询SQL
            pageData.put("analysisSQL", analysisSQL.getConnectionCompositionSQL(pageData, dateRange, topIdArray[0], compositionArray[0]));
            List<Map<String, Object>> oneRestList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (oneRestList.get(0) != null) {
                map.put("oneName1", topNameArray[0] + "-重置率");
                map.put("oneRestList", oneRestList.size() == 0 ? new ArrayList<>() : String.valueOf(oneRestList.get(0).get("value")).split(","));
            }

            //获取组装的top1链路无响应率查询SQL
            pageData.put("analysisSQL", analysisSQL.getConnectionCompositionSQL(pageData, dateRange, topIdArray[0], compositionArray[1]));
            List<Map<String, Object>> oneNoResponseList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (oneNoResponseList.get(0) != null) {
                map.put("oneName2", topNameArray[0] + "-无响应率");
                map.put("oneNoResponseList", oneNoResponseList.size() == 0 ? new ArrayList<>() : String.valueOf(oneNoResponseList.get(0).get("value")).split(","));
            }

            //获取组装的top2链路重置率查询SQL
            pageData.put("analysisSQL", analysisSQL.getConnectionCompositionSQL(pageData, dateRange, topIdArray[1], compositionArray[0]));
            List<Map<String, Object>> twoRestList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (twoRestList.get(0) != null) {
                map.put("twoName1", topNameArray[1] + "-重置率");
                map.put("twoRestList", twoRestList.size() == 0 ? new ArrayList<>() : String.valueOf(twoRestList.get(0).get("value")).split(","));
            }

            //获取组装的top2链路无响应率查询SQL
            pageData.put("analysisSQL", analysisSQL.getConnectionCompositionSQL(pageData, dateRange, topIdArray[1], compositionArray[1]));
            List<Map<String, Object>> twoNoResponseList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (twoNoResponseList.get(0) != null) {
                map.put("twoName2", topNameArray[1] + "-无响应率");
                map.put("twoNoResponseList", twoNoResponseList.size() == 0 ? new ArrayList<>() : String.valueOf(twoNoResponseList.get(0).get("value")).split(","));
            }

            //获取组装的top3链路重置率查询SQL
            pageData.put("analysisSQL", analysisSQL.getConnectionCompositionSQL(pageData, dateRange, topIdArray[2], compositionArray[0]));
            List<Map<String, Object>> threeRestList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (threeRestList.get(0) != null) {
                map.put("threeName1", topNameArray[2] + "-重置率");
                map.put("threeRestList", threeRestList.size() == 0 ? new ArrayList<>() : String.valueOf(threeRestList.get(0).get("value")).split(","));
            }

            //获取组装的top3链路无响应率查询SQL
            pageData.put("analysisSQL", analysisSQL.getConnectionCompositionSQL(pageData, dateRange, topIdArray[2], compositionArray[1]));
            List<Map<String, Object>> threeNoResponseList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (threeNoResponseList.get(0) != null) {
                map.put("threeName2", topNameArray[2] + "-无响应率");
                map.put("threeNoResponseList", threeNoResponseList.size() == 0 ? new ArrayList<>() : String.valueOf(threeNoResponseList.get(0).get("value")).split(","));
            }

            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }

    //异常行为警报分析
    @PostMapping("/getAbnormalBehaviorAnalysis")
    public Map<String, Object> getAbnormalBehaviorAnalysis() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pageData = this.getPageData();
            //异常行为警报分析X轴时间区间
            List<String> dateRange = analysisSQL.getDateRange(pageData);
            map.put("xAxis", dateRange);

            //异常行为警报分析legend
            List<String> legendIds = new ArrayList<>(); //异常行为警报指标主键集合
            List<String> legendData = new ArrayList<>(); //异常行为警报指标名称集合
            legendIds.add("bc30f16fa02345809dd5713dbd223a26");
            legendData.add("TCP交易无响应率告警次数");
            map.put("legend", legendData);

            //异常行为警报分析series
            String[] valueArray = new String[dateRange.size()];
            pageData.put("analysisSQL", analysisSQL.getAbnormalBehaviorSQL(pageData, dateRange, legendIds)); //获取组装的查询SQL
            List<Map<String, Object>> dataList = keyIndicatorsService.getKeyIndicatorMonitorDada(pageData);
            if (dataList.size() > 0) {
                for (int i = 0; i < dateRange.size(); i++) {
                    for (Map<String, Object> data : dataList) {
                        if (dateRange.get(i).equals(data.get("dateTime"))) {
                            valueArray[i] = String.valueOf(data.get("alarm_count"));
                            break;
                        }
                    }
                    if (valueArray[i] == null) {
                        valueArray[i] = "0";
                    }
                }
            } else {
                for (int i = 0; i < dateRange.size(); i++) {
                    valueArray[i] = "0";
                }
            }
            List<Map<String, Object>> series = new ArrayList<>();
            Map<String, Object> serieMap = new HashMap<>();
            serieMap.put("name", legendData.get(0));
            serieMap.put("value", valueArray);
            series.add(serieMap);
            map.put("series", series);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            e.printStackTrace();
        }
        return map;
    }
}
