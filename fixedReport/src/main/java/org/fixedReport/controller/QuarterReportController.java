package org.fixedReport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.fixedReport.BaseController;
import org.fixedReport.service.NewsFlashService;
import org.fixedReport.service.QuarterReportService;
import org.fixedReport.service.ReportService;
import org.fixedReport.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 国库二期季度报告
 *
 */
@Slf4j
@RestController
@Api(tags = "国库二期季度报告")
@RequestMapping(value = "/qurReport", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuarterReportController extends BaseController {
    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;


    @Value("${MAX_WIDTH_SIZE}")
    private int MAX_WIDTH_SIZE;

    @Value("${MAX_HEIGHT_SIZE}")
    private int MAX_HEIGHT_SIZE;

    @Value("${QTR_MAX_WIDTH_SIZE}")
    private int QTR_MAX_WIDTH_SIZE;

    @Value("${QTR_MAX_HEIGHT_SIZE}")
    private int QTR_MAX_HEIGHT_SIZE;

    @Autowired
    private QuarterReportService quarterReportService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private NewsFlashService newsFlashService;

    /**
     * 封装季度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "封装季度报告")
    @PostMapping(value = "/addQuarterReport")
    public Map<String, Object> addQuarterReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> textList = quarterReportService.getTextAndParams(pd);
            List<Map<String, Object>> tableParamsList = quarterReportService.getTableParams(pd);
            List<Map<String, Object>> tableParamsList2 = quarterReportService.getTableParams2(pd);
            List<Map<String, Object>> tableParamsList3 = quarterReportService.getTableParams3(pd);
            List listMap = new ArrayList<>();
            List<Map<String, Object>> graphParamsList = quarterReportService.getGraphParams(pd);
            List<Map<String, Object>> graphParamsList2 = quarterReportService.getGraphParams2(pd);
            List<Map<String, Object>> graphParamsList3 = quarterReportService.getGraphParams3(pd);
            List<Map<String, Object>> graphParamsList4 = quarterReportService.getGraphParams4(pd);
            List<Map<String, Object>> graphParamsList5 = quarterReportService.getGraphParams5(pd);
            List<Map<String, Object>> graphParamsList6 = quarterReportService.getGraphParams6(pd);
            List<Map<String, Object>> graphParamsList7 = quarterReportService.getGraphParams7(pd);
            List<Map<String, Object>> graphParamsList8 = quarterReportService.getGraphParams8(pd);
            List<Map<String, Object>> graphParamsList9 = quarterReportService.getGraphParams9(pd);
            List<Map<String, Object>> graphParamsList10 = quarterReportService.getGraphParams10(pd);
//            第一个图需要三个变量  data_dateAll  series1   series2
                Map  map = new HashMap<String,Object>();
                String  data_dateAll="";
                String series="";
                if(graphParamsList.size()>0){
                    data_dateAll=graphParamsList.get(0).get("ACCOUNT_PERIOD").toString();
                    map.put("data_dateAll",data_dateAll);
                    for(int i=0;i<graphParamsList.size();i++){
                        String id=graphParamsList.get(i).get("id").toString();
                        series=graphParamsList.get(i).get("VAL").toString();
                        map.put("MAX_VAL"+id,graphParamsList.get(i).get("MAX_VAL").toString());
                        map.put("MIN_VAL"+id,graphParamsList.get(i).get("MIN_VAL").toString());
                        map.put("series"+id,series);
                    }
                }else {
                    map.put("MAX_VAL1","0");
                    map.put("MIN_VAL1","0");
                    map.put("MAX_VAL2","0");
                    map.put("MIN_VAL2","0");
                    map.put("data_dateAll",data_dateAll);
                    map.put("series1",series);
                    map.put("series2",series);
                }
                listMap.add(map);
//            第二个图需要三个变量  data_dateAll  series1   series2
                Map  map2 = new HashMap<String,Object>();
            String  series2="";
            String  data_dateAll2="";
            if(graphParamsList2.size()>0){
                data_dateAll2=graphParamsList2.get(0).get("ACCOUNT_PERIOD").toString();
                map2.put("data_dateAll",data_dateAll2);
                for(int i=0;i<graphParamsList2.size();i++){
                    String id=graphParamsList2.get(i).get("id").toString();
                    series2=graphParamsList2.get(i).get("VAL").toString();
                    map2.put("MAX_VAL"+id,graphParamsList2.get(i).get("MAX_VAL").toString());
                    map2.put("MIN_VAL"+id,graphParamsList2.get(i).get("MIN_VAL").toString());
                    map2.put("series"+id,series2);
                }
            }else {
                map2.put("MAX_VAL1","0");
                map2.put("MIN_VAL1","0");
                map2.put("MAX_VAL2","0");
                map2.put("MIN_VAL2","0");
                map2.put("MAX_VAL3","0");
                map2.put("MIN_VAL3","0");
                map2.put("MAX_VAL4","0");
                map2.put("MIN_VAL4","0");
                map2.put("data_dateAll",data_dateAll2);
                map2.put("series1",series2);
                map2.put("series2",series2);
                map2.put("series3",series2);
                map2.put("series4",series2);
            }
//
                listMap.add(map2);
            //            第三个图需要三个变量  data_dateAll  series1   series2
            Map  map3 = new HashMap<String,Object>();
            String  series3="";
            String  data_dateAll3="";
            if(graphParamsList3.size()>0){
                data_dateAll3=graphParamsList3.get(0).get("ACCOUNT_PERIOD").toString();
                map3.put("data_dateAll",data_dateAll3);
                for(int i=0;i<graphParamsList3.size();i++){
                    String id=graphParamsList3.get(i).get("id").toString();
                    series3=graphParamsList3.get(i).get("VAL").toString();
                    map3.put("MAX_VAL"+id,graphParamsList3.get(i).get("MAX_VAL").toString());
                    map3.put("MIN_VAL"+id,graphParamsList3.get(i).get("MIN_VAL").toString());
                    map3.put("series"+id,series3);
                }
            }else {
                map3.put("MAX_VAL1","0");
                map3.put("MIN_VAL1","0");
                map3.put("MAX_VAL2","0");
                map3.put("MIN_VAL2","0");
                map3.put("MAX_VAL3","0");
                map3.put("MIN_VAL3","0");
                map3.put("data_dateAll",data_dateAll3);
                map3.put("series1",series3);
                map3.put("series2",series3);
                map3.put("series3",series3);
            }
//            String  strArrayMap3=graphParamsList3.get(0).get("ACCOUNT_PERIOD").toString();
//            String strArrayMap32=graphParamsList3.get(0).get("VAL").toString();
//            String strArrayMap33=graphParamsList3.get(1).get("VAL").toString();
//            String strArrayMap34=graphParamsList3.get(1).get("VAL").toString();
//            map3.put("data_dateAll",strArrayMap3);
//            map3.put("series1",strArrayMap32);
//            map3.put("series2",strArrayMap33);
//            map3.put("series3",strArrayMap34);
//            map3.put("MAX_VAL1",graphParamsList3.get(0).get("MAX_VAL").toString());
//            map3.put("MIN_VAL1",graphParamsList3.get(0).get("MIN_VAL").toString());
//            map3.put("MAX_VAL2",graphParamsList3.get(1).get("MAX_VAL").toString());
//            map3.put("MIN_VAL2",graphParamsList3.get(1).get("MIN_VAL").toString());
//            map3.put("MAX_VAL3",graphParamsList3.get(2).get("MAX_VAL").toString());
//            map3.put("MIN_VAL3",graphParamsList3.get(2).get("MIN_VAL").toString());
            listMap.add(map3);

//            第4个图需要三个变量  data_dateAll  series1   series2
            Map  map4 = new HashMap<String,Object>();
            String  series4="";
            String  data_dateAll4="";
            if(graphParamsList4.size()>0){
                data_dateAll4=graphParamsList4.get(0).get("ACCOUNT_PERIOD").toString();
                map4.put("data_dateAll",data_dateAll4);
                for(int i=0;i<graphParamsList4.size();i++){
                    String id=graphParamsList4.get(i).get("id").toString();
                    series4=graphParamsList4.get(i).get("VAL").toString();
                    map4.put("MAX_VAL"+id,graphParamsList4.get(i).get("MAX_VAL").toString());
                    map4.put("MIN_VAL"+id,graphParamsList4.get(i).get("MIN_VAL").toString());
                    map4.put("series"+id,series4);
                }
            }else {
                map4.put("MAX_VAL1","0");
                map4.put("MIN_VAL1","0");
                map4.put("MAX_VAL2","0");
                map4.put("MIN_VAL2","0");
                map4.put("MAX_VAL3","0");
                map4.put("MIN_VAL3","0");
                map4.put("data_dateAll",data_dateAll4);
                map4.put("series1",series4);
                map4.put("series2",series4);
                map4.put("series3",series4);
            }
            listMap.add(map4);
//            第5个图需要三个变量  data_dateAll  series1   series2
            Map  map5 = new HashMap<String,Object>();
            String  series5="";
            String  data_dateAll5="";
            if(graphParamsList5.size()>0){
                data_dateAll5=graphParamsList5.get(0).get("ACCOUNT_PERIOD").toString();
                map5.put("data_dateAll",data_dateAll5);
                for(int i=0;i<graphParamsList5.size();i++){
                    String id=graphParamsList5.get(i).get("id").toString();
                    series5=graphParamsList5.get(i).get("VAL").toString();
                    map5.put("MAX_VAL"+id,graphParamsList5.get(i).get("MAX_VAL").toString());
                    map5.put("MIN_VAL"+id,graphParamsList5.get(i).get("MIN_VAL").toString());
                    map5.put("series"+id,series5);
                }
            }else {
                map5.put("MAX_VAL1","0");
                map5.put("MIN_VAL1","0");
                map5.put("MAX_VAL2","0");
                map5.put("MIN_VAL2","0");
                map5.put("MAX_VAL3","0");
                map5.put("MIN_VAL3","0");
                map5.put("data_dateAll",data_dateAll5);
                map5.put("series1",series5);
                map5.put("series2",series5);
                map5.put("series3",series5);
            }
            listMap.add(map5);
            //            第6个图需要三个变量  data_dateAll  series1   series2
            Map  map6 = new HashMap<String,Object>();
            String  series6="";
            String  data_dateAll6="";
            if(graphParamsList6.size()>0){
                data_dateAll6=graphParamsList6.get(0).get("ACCOUNT_PERIOD").toString();
                map6.put("data_dateAll",data_dateAll6);
                for(int i=0;i<graphParamsList6.size();i++){
                    String id=graphParamsList6.get(i).get("id").toString();
                    series6=graphParamsList6.get(i).get("VAL").toString();
                    map6.put("MAX_VAL"+id,graphParamsList6.get(i).get("MAX_VAL").toString());
                    map6.put("MIN_VAL"+id,graphParamsList6.get(i).get("MIN_VAL").toString());
                    map6.put("series"+id,series6);
                }
            }else {
                map6.put("MAX_VAL1","0");
                map6.put("MIN_VAL1","0");
                map6.put("MAX_VAL2","0");
                map6.put("MIN_VAL2","0");
                map6.put("MAX_VAL3","0");
                map6.put("MIN_VAL3","0");
                map6.put("data_dateAll",data_dateAll6);
                map6.put("series1",series6);
                map6.put("series2",series6);
                map6.put("series3",series6);
            }
            listMap.add(map6);


//            第7个图需要三个变量  data_dateAll  series1   series2
            Map  map7 = new HashMap<String,Object>();
            String  series7="";
            String  data_dateAll7="";
            if(graphParamsList7.size()>0){
                data_dateAll7=graphParamsList7.get(0).get("ACCOUNT_PERIOD").toString();
                map7.put("data_dateAll",data_dateAll7);
                for(int i=0;i<graphParamsList7.size();i++){
                    String id=graphParamsList7.get(i).get("id").toString();
                    series7=graphParamsList7.get(i).get("VAL").toString();
                    map7.put("MAX_VAL"+id,graphParamsList7.get(i).get("MAX_VAL").toString());
                    map7.put("MIN_VAL"+id,graphParamsList7.get(i).get("MIN_VAL").toString());
                    map7.put("series"+id,series7);
                }
            }else {
                map7.put("MAX_VAL1","0");
                map7.put("MIN_VAL1","0");
                map7.put("MAX_VAL2","0");
                map7.put("MIN_VAL2","0");
                map7.put("MAX_VAL3","0");
                map7.put("MIN_VAL3","0");
                map7.put("data_dateAll",data_dateAll7);
                map7.put("series1",series7);
                map7.put("series2",series7);
                map7.put("series3",series7);
            }
            listMap.add(map7);
//            第8个图需要三个变量  data_dateAll  series1   series2
            Map  map8 = new HashMap<String,Object>();
            String  series8="";
            String  data_dateAll8="";
            if(graphParamsList8.size()>0){
                data_dateAll8=graphParamsList8.get(0).get("ACCOUNT_PERIOD").toString();
                map8.put("data_dateAll",data_dateAll8);
                for(int i=0;i<graphParamsList8.size();i++){
                    String id=graphParamsList8.get(i).get("id").toString();
                    series8=graphParamsList8.get(i).get("VAL").toString();
                    map8.put("MAX_VAL"+id,graphParamsList8.get(i).get("MAX_VAL").toString());
                    map8.put("MIN_VAL"+id,graphParamsList8.get(i).get("MIN_VAL").toString());
                    map8.put("series"+id,series8);
                }
            }else {
                map8.put("MAX_VAL1","0");
                map8.put("MIN_VAL1","0");
                map8.put("MAX_VAL2","0");
                map8.put("MIN_VAL2","0");
                map8.put("data_dateAll",data_dateAll8);
                map8.put("series1",series8);
                map8.put("series2",series8);
            }
            listMap.add(map8);
            //            第9个图需要三个变量  data_dateAll  series1   series2
            Map  map9 = new HashMap<String,Object>();
            String  series9="";
            String  data_dateAll9="";
            if(graphParamsList9.size()>0){
                data_dateAll9=graphParamsList9.get(0).get("ACCOUNT_PERIOD").toString();
                map9.put("data_dateAll",data_dateAll9);
                for(int i=0;i<graphParamsList9.size();i++){
                    String id=graphParamsList9.get(i).get("id").toString();
                    series9=graphParamsList9.get(i).get("VAL").toString();
                    map9.put("MAX_VAL"+id,graphParamsList9.get(i).get("MAX_VAL").toString());
                    map9.put("MIN_VAL"+id,graphParamsList9.get(i).get("MIN_VAL").toString());
                    map9.put("series"+id,series9);
                }
            }else {
                map9.put("MAX_VAL1","0");
                map9.put("MIN_VAL1","0");
                map9.put("MAX_VAL2","0");
                map9.put("MIN_VAL2","0");
                map9.put("MAX_VAL3","0");
                map9.put("MIN_VAL3","0");
                map9.put("data_dateAll",data_dateAll9);
                map9.put("series1",series9);
                map9.put("series2",series9);
                map9.put("series3",series9);
            }
            listMap.add(map9);
                //            第10个图  data_dateYear   data_dateAll    data_dateYear1  data_dateYear2
                //            data_dateYear3 data_dateYear4 data_dateYear5
                Map  map10 = new HashMap<String,Object>();
            String strArrayMap10="";
            String strArrayMap101="";
            String strArrayMap102="";
            String strArrayMap103="";
            String strArrayMap104="";
            String strArrayMap105="";
            String strArrayMap106="";
            String strArrayMap107="";
            String strArrayMap108="";
            String strArrayMap109="";
            String strArrayMap1010="";
            String strArrayMap1011="";
            if(graphParamsList10.size()>0 && graphParamsList10.size()<=1) {
                strArrayMap10 = graphParamsList10.get(0).get("data_dateYear").toString();
                strArrayMap101 = graphParamsList10.get(0).get("data_dateAll").toString();
                strArrayMap102 = graphParamsList10.get(0).get("data_dateYear1").toString();
                strArrayMap107 = graphParamsList10.get(0).get("series").toString();
            }else  if(graphParamsList10.size()>0 && graphParamsList10.size()<=2) {
                strArrayMap10 = graphParamsList10.get(0).get("data_dateYear").toString();
                strArrayMap101 = graphParamsList10.get(0).get("data_dateAll").toString();
                strArrayMap102 = graphParamsList10.get(0).get("data_dateYear1").toString();
                strArrayMap103 = graphParamsList10.get(1).get("data_dateYear2").toString();
                strArrayMap107 = graphParamsList10.get(0).get("series").toString();
                strArrayMap108 = graphParamsList10.get(1).get("series").toString();
            }else  if(graphParamsList10.size()>0 && graphParamsList10.size()<=3) {
                strArrayMap10 = graphParamsList10.get(0).get("data_dateYear").toString();
                strArrayMap101 = graphParamsList10.get(0).get("data_dateAll").toString();
                strArrayMap102 = graphParamsList10.get(0).get("data_dateYear1").toString();
                strArrayMap103 = graphParamsList10.get(1).get("data_dateYear2").toString();
                strArrayMap104 = graphParamsList10.get(2).get("data_dateYear3").toString();
                strArrayMap107 = graphParamsList10.get(0).get("series").toString();
                strArrayMap108 = graphParamsList10.get(1).get("series").toString();
                strArrayMap109 = graphParamsList10.get(2).get("series").toString();

            }else  if(graphParamsList10.size()>0 && graphParamsList10.size()<=4) {
                strArrayMap10 = graphParamsList10.get(0).get("data_dateYear").toString();
                strArrayMap101 = graphParamsList10.get(0).get("data_dateAll").toString();
                strArrayMap102 = graphParamsList10.get(0).get("data_dateYear1").toString();
                strArrayMap103 = graphParamsList10.get(1).get("data_dateYear2").toString();
                strArrayMap104 = graphParamsList10.get(2).get("data_dateYear3").toString();
                strArrayMap105 = graphParamsList10.get(3).get("data_dateYear4").toString();
                strArrayMap107 = graphParamsList10.get(0).get("series").toString();
                strArrayMap108 = graphParamsList10.get(1).get("series").toString();
                strArrayMap109 = graphParamsList10.get(2).get("series").toString();
                strArrayMap1010 = graphParamsList10.get(3).get("series").toString();
            }else  if(graphParamsList10.size()>0 && graphParamsList10.size()<=5) {
                strArrayMap10 = graphParamsList10.get(0).get("data_dateYear").toString();
                strArrayMap101 = graphParamsList10.get(0).get("data_dateAll").toString();
                strArrayMap102 = graphParamsList10.get(0).get("data_dateYear1").toString();
                strArrayMap103 = graphParamsList10.get(1).get("data_dateYear2").toString();
                strArrayMap104 = graphParamsList10.get(2).get("data_dateYear3").toString();
                strArrayMap105 = graphParamsList10.get(3).get("data_dateYear4").toString();
                strArrayMap106 = graphParamsList10.get(4).get("data_dateYear5").toString();
                strArrayMap107 = graphParamsList10.get(0).get("series").toString();
                strArrayMap108 = graphParamsList10.get(1).get("series").toString();
                strArrayMap109 = graphParamsList10.get(2).get("series").toString();
                strArrayMap1010 = graphParamsList10.get(3).get("series").toString();
                strArrayMap1011 = graphParamsList10.get(4).get("series").toString();
            }

            map10.put("data_dateYear",strArrayMap10);
            map10.put("data_dateAll",  strArrayMap101);
            map10.put("data_dateYear1",strArrayMap102);
            map10.put("data_dateYear2",strArrayMap103);
            map10.put("data_dateYear3",strArrayMap104);
            map10.put("data_dateYear4",strArrayMap105);
            map10.put("data_dateYear5",strArrayMap106);
            map10.put("series1",strArrayMap107);
            map10.put("series2",strArrayMap108);
            map10.put("series3",strArrayMap109);
            map10.put("series4",strArrayMap1010);
            map10.put("series5",strArrayMap1011);
            listMap.add(map10);
                rows.put("textList", textList.toString());
                rows.put("tableParams", tableParamsList.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
                rows.put("tableParams2", tableParamsList2.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
//                rows.put("tableParams3", tableParamsList3.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
            if (tableParamsList.size() > 0) {
                rows.put("tableParams3", tableParamsList3);
            } else {
                rows.put("tableParams3", "");
            }
                rows.put("REPORT_ID", pd.getString("REPORT_ID"));
                rows.put("AMT_UNIT", pd.getString("AMT_UNIT"));
                rows.put("AMT_UNIT_NAME", pd.getString("AMT_UNIT_NAME"));
                rows.put("ACCOUNT_PERIOD", pd.getString("ACCOUNT_PERIOD"));
                rows.put("REPORT_TYPE_ID", pd.getString("REPORT_TYPE_ID"));
                String objStr = JSON.toJSONString(listMap);
                rows.put("echartsData", objStr);
                reportService.insertFact(rows);
                reportService.updateEntityReport(rows);
                res.put("result", "success");
                res.put("rows", rows);
            }catch (Exception e){
                e.printStackTrace();
                res.put("result", "fail");
                res.put("rows", e.getMessage());
            }
            return res;
    }
    /**
     * 获取结构化月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取结构化季度报告")
    @PostMapping(value = "/getQuarterReport")
    public Map<String, Object> getMonthlyReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        Map mapTextList=new HashMap();
        Map mapTalbe2=new HashMap();
        Map mapTalbe3=new HashMap();
        Map mapTalbe=new HashMap();
        try {
            PageData pd = this.getPageData(param);
            Map<String, Object> allDataList = reportService.getMonthlyReport(pd);
            if(!allDataList.get("textList").toString().isEmpty()){
                mapTextList=getStringToMap1(allDataList.get("textList").toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
            }
            JSONObject jsonObj=new JSONObject(mapTextList);
            if(!allDataList.get("tableParams2").toString().isEmpty()){
                mapTalbe2=getStringToMap(allDataList.get("tableParams2").toString());
            }
            JSONObject jsonObjTable2=new JSONObject(mapTalbe2);
            if(!allDataList.get("tableParams").toString().isEmpty()){
                mapTalbe=getStringToMap(allDataList.get("tableParams").toString());
            }
            JSONObject jsonObjTable=new JSONObject(mapTalbe);
            if(!allDataList.get("tableParams3").toString().isEmpty()){
                mapTalbe3=getStringToMap(allDataList.get("tableParams3").toString());
            }
            JSONObject jsonObjTable3=new JSONObject(mapTalbe3);

            List<Map<String, Object>> tableParamsList3 = quarterReportService.getTableParams3(pd);
            String  echartsData= allDataList.get("echartsData").toString();
            List<Map<String,Object>>  jsoin= (List<Map<String,Object>>)JSONObject.parse(echartsData);
            rows.put("echartsData", jsoin);
            rows.put("tableParams", jsonObjTable);
            rows.put("tableParams3", tableParamsList3);
            rows.put("tableParams2", jsonObjTable2);
            rows.put("textList", jsonObj);
            rows.put("AMT_UNIT_NAME", allDataList.get("AMT_UNIT_NAME").toString());
            res.put("result", "success");
            res.put("rows", rows);
        }catch (Exception e){
            e.printStackTrace();
            res.put("result", "fail");
            res.put("rows", e.getMessage());
        }
        return res;
    }
    /**
     * 打包下载月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载月度快报")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @RequestBody(required = false
            ) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String,Object> cMap = new HashMap();
        BufferedInputStream bis = null;
        try{
            Map<String, Object> htmlReport = reportService.getHtmlReport(pd);
            Map<String, Object> htmlReportData = reportService.getMonthlyReport(pd);
            pd.put("AMT_UNIT",htmlReportData.get("AMT_UNIT"));
            pd.put("ACCOUNT_PERIOD",htmlReportData.get("ACCOUNT_PERIOD"));
            if(htmlReportData.size()>0) {
                List<Map<String, Object>> mapTextList = quarterReportService.getTextAndParams(pd);
                String a000422_lib = mapTextList.get(0).containsKey("000422")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";
                String a000422_value= mapTextList.get(0).containsKey("000422")? mapTextList.get(0).get("000422").toString():"";
                cMap.put("text000422_lib", mapTextList.get(0).containsKey("000422")?  "，"+a000422_lib+a000422_value+"个百分点":"");
                String a000496_lib = mapTextList.get(0).containsKey("000496")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";


                String a000496_value= mapTextList.get(0).containsKey("000496")? Double.parseDouble(mapTextList.get(0).get("000496").toString().replace(" ",""))>0 ? "扩大"+mapTextList.get(0).get("000496").toString():"收窄"+mapTextList.get(0).get("000496").toString():"";
                cMap.put("text000496_lib", mapTextList.get(0).containsKey("000496")?  "，"+a000496_lib+a000496_value+"个百分点":"");
                String a000497_lib = mapTextList.get(0).containsKey("000497")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";
                String a000497_value= mapTextList.get(0).containsKey("000497")? Double.parseDouble(mapTextList.get(0).get("000497").toString())>0 ? "扩大"+mapTextList.get(0).get("000497").toString():"收窄"+mapTextList.get(0).get("000497").toString():"";
                cMap.put("text000497_lib", mapTextList.get(0).containsKey("000497")?  "，"+a000497_lib+a000497_value+"个百分点":"");
                String a000498_lib = mapTextList.get(0).containsKey("000498")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";
                String a000498_value= mapTextList.get(0).containsKey("000498")? Double.parseDouble(mapTextList.get(0).get("000498").toString())>0 ? "扩大"+mapTextList.get(0).get("000498").toString():"收窄"+mapTextList.get(0).get("000498").toString():"";
                cMap.put("text000498_lib", mapTextList.get(0).containsKey("000498")?  "，"+a000498_lib+a000498_value+"个百分点":"");
                String a000500_lib = mapTextList.get(0).containsKey("000500")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";
                String a000500_value= mapTextList.get(0).containsKey("000500")? Double.parseDouble(mapTextList.get(0).get("000500").toString())>0 ? "扩大"+mapTextList.get(0).get("000500").toString():"收窄"+mapTextList.get(0).get("000500").toString():"";
                cMap.put("text000500_lib", mapTextList.get(0).containsKey("000500")?  "，"+a000500_lib+a000500_value+"个百分点":"");
                String a000319_lib = mapTextList.get(0).containsKey("000319")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";
//                String a000319_value= mapTextList.get(0).containsKey("000319")? Double.parseDouble(mapTextList.get(0).get("000319").toString())>0 ? "扩大"+mapTextList.get(0).get("000319").toString():"收窄"+mapTextList.get(0).get("000319").toString():"";
                String a000319_value= mapTextList.get(0).containsKey("000319")? mapTextList.get(0).get("000319").toString():"";
                cMap.put("text000319_lib", mapTextList.get(0).containsKey("000319")?  "，"+a000319_lib+a000319_value+"个百分点":"");
                String a000326_lib = mapTextList.get(0).containsKey("000326")? mapTextList.get(0).get("qtr").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("qtr").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("qtr").equals("4") ? "降幅较前3季度":"":"";
//                String a000326_value= mapTextList.get(0).containsKey("000326")? Double.parseDouble(mapTextList.get(0).get("000326").toString())>0 ? "扩大"+mapTextList.get(0).get("000326").toString():"收窄"+mapTextList.get(0).get("000326").toString():"";
                String a000326_value= mapTextList.get(0).containsKey("000326")? mapTextList.get(0).get("000326").toString():"";
                cMap.put("text000326_lib", mapTextList.get(0).containsKey("000326")? "，"+a000326_lib+a000326_value+"个百分点":"");
                String a000515_lib = mapTextList.get(0).containsKey("000515")? mapTextList.get(0).get("000515").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("000515").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("000515").equals("4") ? "降幅较前3季度":"":"";
                String a000515_value= mapTextList.get(0).containsKey("000515")? mapTextList.get(0).get("000515").toString():"";
                cMap.put("text000515_lib", mapTextList.get(0).containsKey("000515")? "，"+a000515_lib+a000515_value+"个百分点":"");
                String a000508_lib = mapTextList.get(0).containsKey("000508")? mapTextList.get(0).get("000508").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("000508").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("000508").equals("4") ? "降幅较前3季度":"":"";
                String a000508_value= mapTextList.get(0).containsKey("000508")? mapTextList.get(0).get("000508").toString():"";
                cMap.put("text000508_lib", mapTextList.get(0).containsKey("000508")? "，"+a000508_lib+a000508_value+"个百分点":"");
                String a000320_lib = mapTextList.get(0).containsKey("000320")? mapTextList.get(0).get("000320").equals("2") ? "降幅较前1季度" : mapTextList.get(0).get("000320").equals("3") ? "降幅较前2季度" : mapTextList.get(0).get("000320").equals("4") ? "降幅较前3季度":"":"";
                String a000320_value= mapTextList.get(0).containsKey("000320")? mapTextList.get(0).get("000320").toString():"";
                cMap.put("text000320_lib", mapTextList.get(0).containsKey("000320")? "，"+a000320_lib+a000320_value+"个百分点":"");



                cMap.put("year", htmlReportData.get("ACCOUNT_PERIOD").toString().substring(0,4));
                cMap.put("Month", mapTextList.get(0).containsKey("MONTH")? mapTextList.get(0).get("MONTH"):"");
                cMap.put("LAST_DAY", mapTextList.get(0).containsKey("LAST_DAY")? mapTextList.get(0).get("LAST_DAY"):"");
                cMap.put("qtr", mapTextList.get(0).containsKey("qtr")? mapTextList.get(0).get("qtr"):"");
                cMap.put("lastQtr", mapTextList.get(0).containsKey("lastQtr")? mapTextList.get(0).get("lastQtr"):"");
                cMap.put("unit", htmlReportData.get("AMT_UNIT").equals("1")? "元":htmlReportData.get("AMT_UNIT").equals("10000")?"万元":"亿元");
//                cMap.put("mapTextList000252", mapTextList.get(0).containsKey("000252")? mapTextList.get(0).get("000252"):"");
//                cMap.put("mapTextList000458", mapTextList.get(0).containsKey("000458")? mapTextList.get(0).get("000458"):"");
//                cMap.put("mapTextList000459", mapTextList.get(0).containsKey("000459")? mapTextList.get(0).get("000459"):"");
//text
                cMap.put("sum000402big", mapTextList.get(0).containsKey("sum000402big")? mapTextList.get(0).get("sum000402big").toString().replace("|",","):"");
                cMap.put("max000402Area", mapTextList.get(0).containsKey("max000402Area")? mapTextList.get(0).get("max000402Area").toString().replace("|",","):"");
                cMap.put("max000342Area", mapTextList.get(0).containsKey("max000342Area")? mapTextList.get(0).get("max000342Area").toString().replace("|",","):"");
                cMap.put("sum000342", mapTextList.get(0).containsKey("sum000342")? mapTextList.get(0).get("sum000342").toString().replace("|",","):"");
                cMap.put("max000402", mapTextList.get(0).containsKey("max000402")? mapTextList.get(0).get("max000402").toString().replace("|",","):"");
                cMap.put("max000342", mapTextList.get(0).containsKey("max000342")? mapTextList.get(0).get("max000342").toString().replace("|",","):"");
                cMap.put("text000518_lib", mapTextList.get(0).containsKey("000518")? mapTextList.get(0).get("000518").toString().replace(",","、").replace("|",","):"");
                cMap.put("text000519_lib", mapTextList.get(0).containsKey("000519")? mapTextList.get(0).get("000519").toString().replace(",","、").replace("|",","):"");
                cMap.put("text000520_lib", mapTextList.get(0).containsKey("000520")? mapTextList.get(0).get("000520").toString().replace(",","、").replace("|",","):"");
                cMap.put("text000521_lib", mapTextList.get(0).containsKey("000521")? mapTextList.get(0).get("000521").toString().replace(",","、").replace("|",","):"");
                cMap.put("text000522_lib", mapTextList.get(0).containsKey("000522")? mapTextList.get(0).get("000522").toString().replace("|",","):"");
                cMap.put("text000510_lib", mapTextList.get(0).containsKey("000510")? mapTextList.get(0).get("000510").toString().replace("|",","):"");
                cMap.put("text000511_lib", mapTextList.get(0).containsKey("000511")? mapTextList.get(0).get("000511").toString().replace("|",","):"");
                cMap.put("text000512_lib", mapTextList.get(0).containsKey("000512")? mapTextList.get(0).get("000512").toString().replace("|",","):"");
                cMap.put("text000513_lib", mapTextList.get(0).containsKey("000513")? mapTextList.get(0).get("000513").toString().replace(",","、").replace("|",","):"");
                cMap.put("text000514_lib", mapTextList.get(0).containsKey("000514")? mapTextList.get(0).get("000514").toString().replace("|",","):"");
                cMap.put("text000516_lib", mapTextList.get(0).containsKey("000516")? mapTextList.get(0).get("000516").toString().replace("|",","):"");
                cMap.put("text000517_lib", mapTextList.get(0).containsKey("000517")? mapTextList.get(0).get("000517").toString().replace("|",","):"");
//                cMap.put("text000496_lib", mapTextList.get(0).containsKey("000496")? mapTextList.get(0).get("000496").toString().replace("|",","):"");
                cMap.put("text000410_lib", mapTextList.get(0).containsKey("000410")? mapTextList.get(0).get("000410").toString().replace("|",","):"");
                cMap.put("text000423_lib", mapTextList.get(0).containsKey("000423")? mapTextList.get(0).get("000423").toString().replace("|",","):"");
//                cMap.put("text000497_lib", mapTextList.get(0).containsKey("000497")? mapTextList.get(0).get("000497").toString().replace("|",","):"");
//                cMap.put("text000498_lib", mapTextList.get(0).containsKey("000498")? mapTextList.get(0).get("000498").toString().replace("|",","):"");
                cMap.put("text000499_lib", mapTextList.get(0).containsKey("000499")? mapTextList.get(0).get("000499").toString().replace("|",","):"");
//                cMap.put("text000500_lib", mapTextList.get(0).containsKey("000500")? mapTextList.get(0).get("000500").toString().replace("|",","):"");
//                cMap.put("text000515_lib", mapTextList.get(0).containsKey("000515")? mapTextList.get(0).get("000515").toString().replace("|",","):"");
                cMap.put("text000501_lib", mapTextList.get(0).containsKey("000501")? mapTextList.get(0).get("000501").toString().replace("|",","):"");
                cMap.put("text000502_lib", mapTextList.get(0).containsKey("000502")? mapTextList.get(0).get("000502").toString().replace("|",","):"");
                cMap.put("text000503_lib", mapTextList.get(0).containsKey("000503")? mapTextList.get(0).get("000503").toString().replace("|",","):"");
                cMap.put("text000504_lib", mapTextList.get(0).containsKey("000504")? mapTextList.get(0).get("000504").toString().replace("|",","):"");
                cMap.put("text000505_lib", mapTextList.get(0).containsKey("000505")? mapTextList.get(0).get("000505").toString().replace("|",","):"");
                cMap.put("text000506_lib", mapTextList.get(0).containsKey("000506")? mapTextList.get(0).get("000506").toString().replace("|",","):"");
                cMap.put("text000507_lib", mapTextList.get(0).containsKey("000507")? mapTextList.get(0).get("000507").toString().replace("|",","):"");
//                cMap.put("text000508_lib", mapTextList.get(0).containsKey("000508")? mapTextList.get(0).get("000508").toString().replace("|",","):"");
                cMap.put("text000509_lib", mapTextList.get(0).containsKey("000509")? mapTextList.get(0).get("000509").toString().replace("|",","):"");
                cMap.put("text000401_lib", mapTextList.get(0).containsKey("000401")? mapTextList.get(0).get("000401").toString().replace("|",","):"");
                cMap.put("text000488_lib", mapTextList.get(0).containsKey("000488")? mapTextList.get(0).get("000488").toString().replace("|",","):"");
                cMap.put("text000489_lib", mapTextList.get(0).containsKey("000489")? mapTextList.get(0).get("000489").toString().replace("|",","):"");
                cMap.put("text000490_lib", mapTextList.get(0).containsKey("000490")? mapTextList.get(0).get("000490").toString().replace("|",","):"");
                cMap.put("text000491_lib", mapTextList.get(0).containsKey("000491")? mapTextList.get(0).get("000491").toString().replace("|",","):"");
                cMap.put("text000492_lib", mapTextList.get(0).containsKey("000492")? mapTextList.get(0).get("000492").toString().replace("|",","):"");
                cMap.put("text000493_lib", mapTextList.get(0).containsKey("000493")? mapTextList.get(0).get("000493").toString().replace("|",","):"");
                cMap.put("text000494_lib", mapTextList.get(0).containsKey("000494")? mapTextList.get(0).get("000494").toString().replace("|",","):"");
                cMap.put("text000495_lib", mapTextList.get(0).containsKey("000495")? mapTextList.get(0).get("000495").toString().replace("|",","):"");
                cMap.put("text000417_lib", mapTextList.get(0).containsKey("000417")? mapTextList.get(0).get("000417").toString().replace("|",","):"");
                cMap.put("text000402_lib", mapTextList.get(0).containsKey("000402")? mapTextList.get(0).get("000402").toString().replace("|",","):"");
                cMap.put("text000177_lib", mapTextList.get(0).containsKey("000177")? mapTextList.get(0).get("000177").toString().replace("|",","):"");
                cMap.put("text000420_lib", mapTextList.get(0).containsKey("000420")? mapTextList.get(0).get("000420").toString().replace("|",","):"");
                cMap.put("text000397_lib", mapTextList.get(0).containsKey("000397")? mapTextList.get(0).get("000397").toString().replace("|",","):"");
                cMap.put("text000421_lib", mapTextList.get(0).containsKey("000421")? mapTextList.get(0).get("000421").toString().replace("|",","):"");
                cMap.put("text000398_lib", mapTextList.get(0).containsKey("000398")? mapTextList.get(0).get("000398").toString().replace("|",","):"");
//                cMap.put("text000422_lib", mapTextList.get(0).containsKey("000422")? mapTextList.get(0).get("000422").toString().replace("|",","):"");
                cMap.put("text000271_lib", mapTextList.get(0).containsKey("000271")? mapTextList.get(0).get("000271").toString().replace("|",","):"");
                cMap.put("text000272_lib", mapTextList.get(0).containsKey("000272")? mapTextList.get(0).get("000272").toString().replace("|",","):"");
                cMap.put("text000012_lib", mapTextList.get(0).containsKey("000012")? mapTextList.get(0).get("000012").toString().replace("|",","):"");
                cMap.put("text000392_lib", mapTextList.get(0).containsKey("000392")? mapTextList.get(0).get("000392").toString().replace("|",","):"");
                cMap.put("text000013_lib", mapTextList.get(0).containsKey("000013")? mapTextList.get(0).get("000013").toString().replace("|",","):"");
                cMap.put("text000424_lib", mapTextList.get(0).containsKey("000424")? mapTextList.get(0).get("000424").toString().replace("|",","):"");
                cMap.put("text000341_lib", mapTextList.get(0).containsKey("000341")? mapTextList.get(0).get("000341").toString().replace("|",","):"");
                cMap.put("text000342_lib", mapTextList.get(0).containsKey("000342")? mapTextList.get(0).get("000342").toString().replace("|",","):"");
                cMap.put("text000345_lib", mapTextList.get(0).containsKey("000345")? mapTextList.get(0).get("000345").toString().replace("|",","):"");
                cMap.put("text000275_lib", mapTextList.get(0).containsKey("000275")? mapTextList.get(0).get("000275").toString().replace("|",","):"");
                cMap.put("text000346_lib", mapTextList.get(0).containsKey("000346")? mapTextList.get(0).get("000346").toString().replace("|",","):"");
                cMap.put("text000347_lib", mapTextList.get(0).containsKey("000347")? mapTextList.get(0).get("000347").toString().replace("|",","):"");
                cMap.put("text000216_lib", mapTextList.get(0).containsKey("000216")? mapTextList.get(0).get("000216").toString().replace("|",","):"");
                cMap.put("text000217_lib", mapTextList.get(0).containsKey("000217")? mapTextList.get(0).get("000217").toString().replace("|",","):"");
                cMap.put("text000218_lib", mapTextList.get(0).containsKey("000218")? mapTextList.get(0).get("000218").toString().replace("|",","):"");
                cMap.put("text000219_lib", mapTextList.get(0).containsKey("000219")? mapTextList.get(0).get("000219").toString().replace("|",","):"");
                cMap.put("text000287_lib", mapTextList.get(0).containsKey("000287")? mapTextList.get(0).get("000287").toString().replace("|",","):"");
                cMap.put("text000288_lib", mapTextList.get(0).containsKey("000288")? mapTextList.get(0).get("000288").toString().replace("|",","):"");
                cMap.put("text000289_lib", mapTextList.get(0).containsKey("000289")? mapTextList.get(0).get("000289").toString().replace("|",","):"");
                cMap.put("text000291_lib", mapTextList.get(0).containsKey("000291")? mapTextList.get(0).get("000291").toString().replace("|",","):"");
                cMap.put("text000292_lib", mapTextList.get(0).containsKey("000292")? mapTextList.get(0).get("000292").toString().replace("|",","):"");
                cMap.put("text000293_lib", mapTextList.get(0).containsKey("000293")? mapTextList.get(0).get("000293").toString().replace("|",","):"");
                cMap.put("text000295_lib", mapTextList.get(0).containsKey("000295")? mapTextList.get(0).get("000295").toString().replace("|",","):"");
                cMap.put("text000297_lib", mapTextList.get(0).containsKey("000297")? mapTextList.get(0).get("000297").toString().replace("|",","):"");
                cMap.put("text000301_lib", mapTextList.get(0).containsKey("000301")? mapTextList.get(0).get("000301").toString().replace("|",","):"");
                cMap.put("text000302_lib", mapTextList.get(0).containsKey("000302")? mapTextList.get(0).get("000302").toString().replace("|",","):"");
                cMap.put("text000303_lib", mapTextList.get(0).containsKey("000303")? mapTextList.get(0).get("000303").toString().replace("|",","):"");
                cMap.put("text000305_lib", mapTextList.get(0).containsKey("000305")? mapTextList.get(0).get("000305").toString().replace("|",","):"");
                cMap.put("text000306_lib", mapTextList.get(0).containsKey("000306")? mapTextList.get(0).get("000306").toString().replace("|",","):"");
                cMap.put("text000350_lib", mapTextList.get(0).containsKey("000350")? mapTextList.get(0).get("000350").toString().replace("|",","):"");
                cMap.put("text000351_lib", mapTextList.get(0).containsKey("000351")? mapTextList.get(0).get("000351").toString().replace("|",","):"");
                cMap.put("text000353_lib", mapTextList.get(0).containsKey("000353")? mapTextList.get(0).get("000353").toString().replace("|",","):"");
                cMap.put("text000354_lib", mapTextList.get(0).containsKey("000354")? mapTextList.get(0).get("000354").toString().replace("|",","):"");
                cMap.put("text000444_lib", mapTextList.get(0).containsKey("000444")? mapTextList.get(0).get("000444").toString().replace("|",","):"");
                cMap.put("text000445_lib", mapTextList.get(0).containsKey("000445")? mapTextList.get(0).get("000445").toString().replace("|",","):"");
                cMap.put("text000419_lib", mapTextList.get(0).containsKey("000419")? mapTextList.get(0).get("000419").toString().replace("|",","):"");
                cMap.put("text000403_lib", mapTextList.get(0).containsKey("000403")? mapTextList.get(0).get("000403").toString().replace("|",","):"");
                cMap.put("text000418_lib", mapTextList.get(0).containsKey("000418")? mapTextList.get(0).get("000418").toString().replace("|",","):"");
                cMap.put("text000404_lib", mapTextList.get(0).containsKey("000404")? mapTextList.get(0).get("000404").toString().replace("|",","):"");
                cMap.put("text000206_lib", mapTextList.get(0).containsKey("000206")? mapTextList.get(0).get("000206").toString().replace("|",","):"");
                cMap.put("text000416_lib", mapTextList.get(0).containsKey("000416")? mapTextList.get(0).get("000416").toString().replace("|",","):"");
                cMap.put("text000207_lib", mapTextList.get(0).containsKey("000207")? mapTextList.get(0).get("000207").toString().replace("|",","):"");
                cMap.put("text000043_lib", mapTextList.get(0).containsKey("000043")? mapTextList.get(0).get("000043").toString().replace("|",","):"");
                cMap.put("text000044_lib", mapTextList.get(0).containsKey("000044")? mapTextList.get(0).get("000044").toString().replace("|",","):"");
                cMap.put("text000260_lib", mapTextList.get(0).containsKey("000260")? mapTextList.get(0).get("000260").toString().replace("|",","):"");
                cMap.put("text000261_lib", mapTextList.get(0).containsKey("000261")? mapTextList.get(0).get("000261").toString().replace("|",","):"");
                cMap.put("text000172_lib", mapTextList.get(0).containsKey("000172")? mapTextList.get(0).get("000172").toString().replace("|",","):"");
                cMap.put("text000356_lib", mapTextList.get(0).containsKey("000356")? mapTextList.get(0).get("000356").toString().replace("|",","):"");
                cMap.put("text000323_lib", mapTextList.get(0).containsKey("000323")? mapTextList.get(0).get("000323").toString().replace("|",","):"");
                cMap.put("text000357_lib", mapTextList.get(0).containsKey("000357")? mapTextList.get(0).get("000357").toString().replace("|",","):"");
//                cMap.put("text000326_lib", mapTextList.get(0).containsKey("000326")? mapTextList.get(0).get("000326").toString().replace("|",","):"");
                cMap.put("text000176_lib", mapTextList.get(0).containsKey("000176")? mapTextList.get(0).get("000176").toString().replace("|",","):"");
                cMap.put("text000175_lib", mapTextList.get(0).containsKey("000175")? mapTextList.get(0).get("000175").toString().replace("|",","):"");
                cMap.put("text000334_lib", mapTextList.get(0).containsKey("000334")? mapTextList.get(0).get("000334").toString().replace("|",","):"");
                cMap.put("text000178_lib", mapTextList.get(0).containsKey("000178")? mapTextList.get(0).get("000178").toString().replace("|",",").replace(" ",""):"");
                cMap.put("text000179_lib", mapTextList.get(0).containsKey("000179")? mapTextList.get(0).get("000179").toString().replace("|",","):"");
                cMap.put("text000336_lib", mapTextList.get(0).containsKey("000336")? mapTextList.get(0).get("000336").toString().replace("|",","):"");
                cMap.put("text000180_lib", mapTextList.get(0).containsKey("000180")? mapTextList.get(0).get("000180").toString().replace("|",","):"");
                cMap.put("text000337_lib", mapTextList.get(0).containsKey("000337")? mapTextList.get(0).get("000337").toString().replace("|",","):"");
                cMap.put("text000181_lib", mapTextList.get(0).containsKey("000181")? mapTextList.get(0).get("000181").toString().replace("|",","):"");
                cMap.put("text000182_lib", mapTextList.get(0).containsKey("000181")? mapTextList.get(0).get("000181").toString().replace("|",","):"");
                cMap.put("text000339_lib", mapTextList.get(0).containsKey("000339")? mapTextList.get(0).get("000339").toString().replace("|",","):"");
                cMap.put("text000031_lib", mapTextList.get(0).containsKey("000031")? mapTextList.get(0).get("000031").toString().replace("|",","):"");
                cMap.put("text000331_lib", mapTextList.get(0).containsKey("000331")? mapTextList.get(0).get("000331").toString().replace("|",","):"");
                cMap.put("text000032_lib", mapTextList.get(0).containsKey("000032")? mapTextList.get(0).get("000032").toString().replace("|",","):"");
                cMap.put("text000037_lib", mapTextList.get(0).containsKey("000037")? mapTextList.get(0).get("000037").toString().replace("|",","):"");
                cMap.put("text000332_lib", mapTextList.get(0).containsKey("000332")? mapTextList.get(0).get("000332").toString().replace("|",","):"");
                cMap.put("text000038_lib", mapTextList.get(0).containsKey("000038")? mapTextList.get(0).get("000038").toString().replace("|",","):"");
                cMap.put("text000368_lib", mapTextList.get(0).containsKey("000368")? mapTextList.get(0).get("000368").toString().replace("|",","):"");
                cMap.put("text000370_lib", mapTextList.get(0).containsKey("000370")? mapTextList.get(0).get("000370").toString().replace("|",","):"");
                cMap.put("text000372_lib", mapTextList.get(0).containsKey("000372")? mapTextList.get(0).get("000372").toString().replace("|",","):"");
                cMap.put("text000374_lib", mapTextList.get(0).containsKey("000374")? mapTextList.get(0).get("000374").toString().replace("|",","):"");
                cMap.put("text000373_lib", mapTextList.get(0).containsKey("000373")? mapTextList.get(0).get("000373").toString().replace("|",","):"");
                cMap.put("text000376_lib", mapTextList.get(0).containsKey("000376")? mapTextList.get(0).get("000376").toString().replace("|",","):"");
                cMap.put("text000377_lib", mapTextList.get(0).containsKey("000377")? mapTextList.get(0).get("000377").toString().replace("|",","):"");
                cMap.put("text000230_lib", mapTextList.get(0).containsKey("000230")? mapTextList.get(0).get("000230").toString().replace("|",","):"");
                cMap.put("text000283_lib", mapTextList.get(0).containsKey("000283")? mapTextList.get(0).get("000283").toString().replace("|",","):"");
                cMap.put("text000231_lib", mapTextList.get(0).containsKey("000231")? mapTextList.get(0).get("000231").toString().replace("|",","):"");
                cMap.put("text000427_lib", mapTextList.get(0).containsKey("000427")? mapTextList.get(0).get("000427").toString().replace("|",","):"");
                cMap.put("text000005_lib", mapTextList.get(0).containsKey("000005")? mapTextList.get(0).get("000005").toString().replace("|",","):"");
                cMap.put("text000006_lib", mapTextList.get(0).containsKey("000006")? mapTextList.get(0).get("000006").toString().replace("|",","):"");
                cMap.put("text000007_lib", mapTextList.get(0).containsKey("000007")? mapTextList.get(0).get("000007").toString().replace("|",","):"");
                cMap.put("text000284_lib", mapTextList.get(0).containsKey("000284")? mapTextList.get(0).get("000284").toString().replace("|",","):"");
                cMap.put("text000208_lib", mapTextList.get(0).containsKey("000208")? mapTextList.get(0).get("000208").toString().replace("|",","):"");
                cMap.put("text000278_lib", mapTextList.get(0).containsKey("000278")? mapTextList.get(0).get("000278").toString().replace("|",","):"");
                cMap.put("text000209_lib", mapTextList.get(0).containsKey("000209")? mapTextList.get(0).get("000209").toString().replace("|",","):"");
                cMap.put("text000259_lib", mapTextList.get(0).containsKey("000259")? mapTextList.get(0).get("000259").toString().replace("|",","):"");
                cMap.put("text000279_lib", mapTextList.get(0).containsKey("000279")? mapTextList.get(0).get("000279").toString().replace("|",","):"");
                cMap.put("text000204_lib", mapTextList.get(0).containsKey("000204")? mapTextList.get(0).get("000204").toString().replace("|",","):"");
                cMap.put("text000280_lib", mapTextList.get(0).containsKey("000280")? mapTextList.get(0).get("000280").toString().replace("|",","):"");
                cMap.put("text000205_lib", mapTextList.get(0).containsKey("000205")? mapTextList.get(0).get("000205").toString().replace("|",","):"");
                cMap.put("text000249_lib", mapTextList.get(0).containsKey("000249")? mapTextList.get(0).get("000249").toString().replace("|",","):"");
                cMap.put("text000282_lib", mapTextList.get(0).containsKey("000282")? mapTextList.get(0).get("000282").toString().replace("|",","):"");
                cMap.put("text000365_lib", mapTextList.get(0).containsKey("000365")? mapTextList.get(0).get("000365").toString().replace("|",","):"");
                cMap.put("text000393_lib", mapTextList.get(0).containsKey("000393")? mapTextList.get(0).get("000393").toString().replace("|",","):"");
                cMap.put("text000119_lib", mapTextList.get(0).containsKey("000119")? mapTextList.get(0).get("000119").toString().replace("|",","):"");
//                cMap.put("text000319_lib", mapTextList.get(0).containsKey("000319")? mapTextList.get(0).get("000319").toString().replace("|",","):"");
                cMap.put("text000366_lib", mapTextList.get(0).containsKey("000366")? mapTextList.get(0).get("000366").toString().replace("|",","):"");
                cMap.put("text000367_lib", mapTextList.get(0).containsKey("000367")? mapTextList.get(0).get("000367").toString().replace("|",","):"");
//                cMap.put("text000320_lib", mapTextList.get(0).containsKey("000320")? mapTextList.get(0).get("000320").toString().replace("|",","):"");
                cMap.put("text000394_lib", mapTextList.get(0).containsKey("000394")? mapTextList.get(0).get("000394").toString().replace("|",","):"");
                cMap.put("text000395_lib", mapTextList.get(0).containsKey("000395")? mapTextList.get(0).get("000395").toString().replace("|",","):"");
                cMap.put("text000396_lib", mapTextList.get(0).containsKey("000396")? mapTextList.get(0).get("000396").toString().replace("|",","):"");
                cMap.put("stock_area_1", mapTextList.get(0).containsKey("stock_area_1")? mapTextList.get(0).get("stock_area_1").toString().replace("|",","):"");
                cMap.put("value_7", mapTextList.get(0).containsKey("value_7")? mapTextList.get(0).get("value_7").toString().replace("|",","):"");
                cMap.put("stock_area_2", mapTextList.get(0).containsKey("stock_area_2")? mapTextList.get(0).get("stock_area_2").toString().replace("|",","):"");
                cMap.put("value_8", mapTextList.get(0).containsKey("value_8")? mapTextList.get(0).get("value_8").toString().replace("|",","):"");
                cMap.put("subject_1", mapTextList.get(0).containsKey("subject_1")? mapTextList.get(0).get("subject_1").toString().replace("|",","):"");
                cMap.put("value_4", mapTextList.get(0).containsKey("value_4")? mapTextList.get(0).get("value_4").toString().replace("|",","):"");
                cMap.put("ratio_1", mapTextList.get(0).containsKey("ratio_1")? mapTextList.get(0).get("ratio_1").toString().replace("|",","):"");
                cMap.put("text000441_lib", mapTextList.get(0).containsKey("000441")? mapTextList.get(0).get("000441").toString().replace("|",","):"");
                cMap.put("text000411_lib", mapTextList.get(0).containsKey("000411")? mapTextList.get(0).get("000411").toString().replace("|",","):"");
                cMap.put("text000412_lib", mapTextList.get(0).containsKey("000412")? mapTextList.get(0).get("000412").toString().replace("|",","):"");
                cMap.put("text000413_lib", mapTextList.get(0).containsKey("000413")? mapTextList.get(0).get("000413").toString().replace("|",","):"");
                cMap.put("text000414_lib", mapTextList.get(0).containsKey("000414")? mapTextList.get(0).get("000414").toString().replace("|",","):"");


System.out.println("---------++++++++++"+cMap.get("text000412_lib"));
System.out.println("--------------77777----------"+mapTextList.get(0).get("000518").toString());

                cMap.put("mapTextList000231_lib", "567890ghjkl");
//                table 1
                List<Map<String, Object>> tableParamsList = quarterReportService.getTableParams(pd);
                cMap.put("text000401_lib", tableParamsList.get(0).containsKey("000401")? tableParamsList.get(0).get("000401").toString().replace("|",","):"");
                cMap.put("text000403_lib", tableParamsList.get(0).containsKey("000403")? tableParamsList.get(0).get("000403").toString().replace("|",","):"");
                cMap.put("text000264_lib", tableParamsList.get(0).containsKey("000264")? tableParamsList.get(0).get("000264").toString().replace("|",","):"");
                cMap.put("text000024_lib", tableParamsList.get(0).containsKey("000024")? tableParamsList.get(0).get("000024").toString().replace("|",","):"");
                cMap.put("text000407_lib", tableParamsList.get(0).containsKey("000407")? tableParamsList.get(0).get("000407").toString().replace("|",","):"");
                cMap.put("text000037_lib", tableParamsList.get(0).containsKey("000037")? tableParamsList.get(0).get("000037").toString().replace("|",","):"");
                cMap.put("text000043_lib", tableParamsList.get(0).containsKey("000043")? tableParamsList.get(0).get("000043").toString().replace("|",","):"");
                cMap.put("text000266_lib", tableParamsList.get(0).containsKey("000266")? tableParamsList.get(0).get("000266").toString().replace("|",","):"");
                cMap.put("text000268_lib", tableParamsList.get(0).containsKey("000268")? tableParamsList.get(0).get("000268").toString().replace("|",","):"");
                cMap.put("text000049_lib", tableParamsList.get(0).containsKey("000049")? tableParamsList.get(0).get("000049").toString().replace("|",","):"");
                cMap.put("text000397_lib", tableParamsList.get(0).containsKey("000397")? tableParamsList.get(0).get("000397").toString().replace("|",","):"");
                cMap.put("text000399_lib", tableParamsList.get(0).containsKey("000399")? tableParamsList.get(0).get("000399").toString().replace("|",","):"");
                cMap.put("text000405_lib", tableParamsList.get(0).containsKey("000405")? tableParamsList.get(0).get("000405").toString().replace("|",","):"");
                cMap.put("text000012_lib", tableParamsList.get(0).containsKey("000012")? tableParamsList.get(0).get("000012").toString().replace("|",","):"");
                cMap.put("text000018_lib", tableParamsList.get(0).containsKey("000018")? tableParamsList.get(0).get("000018").toString().replace("|",","):"");
                cMap.put("text000262_lib", tableParamsList.get(0).containsKey("000262")? tableParamsList.get(0).get("000262").toString().replace("|",","):"");
                cMap.put("text000264_lib", tableParamsList.get(0).containsKey("000264")? tableParamsList.get(0).get("000264").toString().replace("|",","):"");
                cMap.put("text000024_lib", tableParamsList.get(0).containsKey("000024")? tableParamsList.get(0).get("000024").toString().replace("|",","):"");
                cMap.put("text000005_lib", tableParamsList.get(0).containsKey("000005")? tableParamsList.get(0).get("000005").toString().replace("|",","):"");


                cMap.put("text000037_lib", tableParamsList.get(0).containsKey("000037")? tableParamsList.get(0).get("000037").toString().replace("|",","):"");
                cMap.put("text000043_lib", tableParamsList.get(0).containsKey("000043")? tableParamsList.get(0).get("000043").toString().replace("|",","):"");
                cMap.put("text000266_lib", tableParamsList.get(0).containsKey("000266")? tableParamsList.get(0).get("000266").toString().replace("|",","):"");
                cMap.put("text000268_lib", tableParamsList.get(0).containsKey("000268")? tableParamsList.get(0).get("000268").toString().replace("|",","):"");
                cMap.put("text000049_lib", tableParamsList.get(0).containsKey("000049")? tableParamsList.get(0).get("000049").toString().replace("|",","):"");
                cMap.put("text000262_lib", tableParamsList.get(0).containsKey("000262")? tableParamsList.get(0).get("000262").toString().replace("|",","):"");
                cMap.put("text000402_lib_1", tableParamsList.get(0).containsKey("000402")? tableParamsList.get(0).get("000402").toString().replace("|",","):"");
                cMap.put("text000404_lib_1", tableParamsList.get(0).containsKey("000404")? tableParamsList.get(0).get("000404").toString().replace("|",","):"");
                cMap.put("text000263_lib_1", tableParamsList.get(0).containsKey("000263")? tableParamsList.get(0).get("000263").toString().replace("|",","):"");
                cMap.put("text000265_lib_1", tableParamsList.get(0).containsKey("000265")? tableParamsList.get(0).get("000265").toString().replace("|",","):"");
                cMap.put("text000025_lib_1", tableParamsList.get(0).containsKey("000025")? tableParamsList.get(0).get("000025").toString().replace("|",","):"");
                cMap.put("text000408_lib_1", tableParamsList.get(0).containsKey("000408")? tableParamsList.get(0).get("000408").toString().replace("|",","):"");
                cMap.put("text000038_lib_1", tableParamsList.get(0).containsKey("000038")? tableParamsList.get(0).get("000038").toString().replace("|",","):"");
                cMap.put("text000044_lib_1", tableParamsList.get(0).containsKey("000044")? tableParamsList.get(0).get("000044").toString().replace("|",","):"");
                cMap.put("text000267_lib_1", tableParamsList.get(0).containsKey("000267")? tableParamsList.get(0).get("000267").toString().replace("|",","):"");
                cMap.put("text000269_lib_1", tableParamsList.get(0).containsKey("000269")? tableParamsList.get(0).get("000269").toString().replace("|",","):"");
                cMap.put("text000050_lib_1", tableParamsList.get(0).containsKey("000050")? tableParamsList.get(0).get("000050").toString().replace("|",","):"");
                cMap.put("text000032_lib_1", tableParamsList.get(0).containsKey("000032")? tableParamsList.get(0).get("000032").toString().replace("|",","):"");
                cMap.put("text000398_lib_1", tableParamsList.get(0).containsKey("000398")? tableParamsList.get(0).get("000398").toString().replace("|",","):"");
                cMap.put("text000400_lib_1", tableParamsList.get(0).containsKey("000400")? tableParamsList.get(0).get("000400").toString().replace("|",","):"");
                cMap.put("text000406_lib_1", tableParamsList.get(0).containsKey("000406")? tableParamsList.get(0).get("000406").toString().replace("|",","):"");
                cMap.put("text000013_lib_1", tableParamsList.get(0).containsKey("000013")? tableParamsList.get(0).get("000013").toString().replace("|",","):"");
                cMap.put("text000019_lib_1", tableParamsList.get(0).containsKey("000019")? tableParamsList.get(0).get("000019").toString().replace("|",","):"");
                cMap.put("text000263_lib_1", tableParamsList.get(0).containsKey("000263")? tableParamsList.get(0).get("000263").toString().replace("|",","):"");
                cMap.put("text000265_lib_1", tableParamsList.get(0).containsKey("000265")? tableParamsList.get(0).get("000265").toString().replace("|",","):"");
                cMap.put("text000025_lib_1", tableParamsList.get(0).containsKey("000025")? tableParamsList.get(0).get("000025").toString().replace("|",","):"");
                cMap.put("text000006_lib_1", tableParamsList.get(0).containsKey("000006")? tableParamsList.get(0).get("000006").toString().replace("|",","):"");
                cMap.put("text000038_lib_1", tableParamsList.get(0).containsKey("000038")? tableParamsList.get(0).get("000038").toString().replace("|",","):"");
                cMap.put("text000044_lib_1", tableParamsList.get(0).containsKey("000044")? tableParamsList.get(0).get("000044").toString().replace("|",","):"");
                cMap.put("text000267_lib_1", tableParamsList.get(0).containsKey("000267")? tableParamsList.get(0).get("000267").toString().replace("|",","):"");
                cMap.put("text000269_lib_1", tableParamsList.get(0).containsKey("000269")? tableParamsList.get(0).get("000269").toString().replace("|",","):"");
                cMap.put("text000050_lib_1", tableParamsList.get(0).containsKey("000050")? tableParamsList.get(0).get("000050").toString().replace("|",","):"");
                cMap.put("text000032_lib_1", tableParamsList.get(0).containsKey("000032")? tableParamsList.get(0).get("000032").toString().replace("|",","):"");
//                table 2
                List<Map<String, Object>> tableParamsList2 = quarterReportService.getTableParams2(pd);
                cMap.put("text000389_lib", tableParamsList2.get(0).containsKey("000389")? tableParamsList2.get(0).get("000389").toString().replace("|",","):"");
                cMap.put("text000390_lib", tableParamsList2.get(0).containsKey("000390")? tableParamsList2.get(0).get("000390").toString().replace("|",","):"");
                cMap.put("text000391_lib", tableParamsList2.get(0).containsKey("000391")? tableParamsList2.get(0).get("000391").toString().replace("|",","):"");
                cMap.put("text000378_lib", tableParamsList2.get(0).containsKey("000378")? tableParamsList2.get(0).get("000378").toString().replace("|",","):"");
                cMap.put("text000379_lib", tableParamsList2.get(0).containsKey("000379")? tableParamsList2.get(0).get("000379").toString().replace("|",","):"");
                cMap.put("text000380_lib", tableParamsList2.get(0).containsKey("000380")? tableParamsList2.get(0).get("000380").toString().replace("|",","):"");
                cMap.put("text000386_lib", tableParamsList2.get(0).containsKey("000386")? tableParamsList2.get(0).get("000386").toString().replace("|",","):"");
                cMap.put("text000387_lib", tableParamsList2.get(0).containsKey("000387")? tableParamsList2.get(0).get("000387").toString().replace("|",","):"");
                cMap.put("text000388_lib", tableParamsList2.get(0).containsKey("000388")? tableParamsList2.get(0).get("000388").toString().replace("|",","):"");
                cMap.put("text000384_lib", tableParamsList2.get(0).containsKey("000384")? tableParamsList2.get(0).get("000384").toString().replace("|",","):"");
                cMap.put("text000385_lib", tableParamsList2.get(0).containsKey("000385")? tableParamsList2.get(0).get("000385").toString().replace("|",","):"");
                cMap.put("text000333_lib", tableParamsList2.get(0).containsKey("000333")? tableParamsList2.get(0).get("000333").toString().replace("|",","):"");
                cMap.put("text000368_lib", tableParamsList2.get(0).containsKey("000368")? tableParamsList2.get(0).get("000368").toString().replace("|",","):"");
                cMap.put("text000369_lib", tableParamsList2.get(0).containsKey("000369")? tableParamsList2.get(0).get("000369").toString().replace("|",","):"");
                cMap.put("text000370_lib", tableParamsList2.get(0).containsKey("000370")? tableParamsList2.get(0).get("000370").toString().replace("|",","):"");
                cMap.put("text000381_lib", tableParamsList2.get(0).containsKey("000381")? tableParamsList2.get(0).get("000381").toString().replace("|",","):"");
                cMap.put("text000382_lib", tableParamsList2.get(0).containsKey("000382")? tableParamsList2.get(0).get("000382").toString().replace("|",","):"");
                cMap.put("text000383_lib", tableParamsList2.get(0).containsKey("000383")? tableParamsList2.get(0).get("000383").toString().replace("|",","):"");

//                table 3
                List<Map<String, Object>> tableParamsList3 = quarterReportService.getTableParams4(pd);
                cMap.put("tableParams3", tableParamsList3);
//                cMap.put("table1", tableParamsList3.get(0).containsKey("table1")? tableParamsList3.get(0).get("table1").toString().replace("|",","):"");
//                cMap.put("table2", tableParamsList3.get(0).containsKey("table2")? tableParamsList3.get(0).get("table2").toString().replace("|",","):"");
//                cMap.put("table4", tableParamsList3.get(0).containsKey("table4")? tableParamsList3.get(0).get("table4").toString().replace("|",","):"");
//                cMap.put("table46", tableParamsList3.get(0).containsKey("table46")? tableParamsList3.get(0).get("table46").toString().replace("|",","):"");
//                cMap.put("table47", tableParamsList3.get(0).containsKey("table47")? tableParamsList3.get(0).get("table47").toString().replace("|",","):"");
//                cMap.put("table49", tableParamsList3.get(0).containsKey("table49")? tableParamsList3.get(0).get("table49").toString().replace("|",","):"");
//                cMap.put("table31", tableParamsList3.get(0).containsKey("table31")? tableParamsList3.get(0).get("table31").toString().replace("|",","):"");
//                cMap.put("table32", tableParamsList3.get(0).containsKey("table32")? tableParamsList3.get(0).get("table32").toString().replace("|",","):"");
//                cMap.put("table34", tableParamsList3.get(0).containsKey("table34")? tableParamsList3.get(0).get("table34").toString().replace("|",","):"");
//                cMap.put("table51", tableParamsList3.get(0).containsKey("table51")? tableParamsList3.get(0).get("table51").toString().replace("|",","):"");
//                cMap.put("table52", tableParamsList3.get(0).containsKey("table52")? tableParamsList3.get(0).get("table52").toString().replace("|",","):"");
//                cMap.put("table54", tableParamsList3.get(0).containsKey("table54")? tableParamsList3.get(0).get("table54").toString().replace("|",","):"");
//                cMap.put("table61", tableParamsList3.get(0).containsKey("table61")? tableParamsList3.get(0).get("table61").toString().replace("|",","):"");
//                cMap.put("table62", tableParamsList3.get(0).containsKey("table62")? tableParamsList3.get(0).get("table62").toString().replace("|",","):"");
//                cMap.put("table64", tableParamsList3.get(0).containsKey("table64")? tableParamsList3.get(0).get("table64").toString().replace("|",","):"");
//                cMap.put("table41", tableParamsList3.get(0).containsKey("table41")? tableParamsList3.get(0).get("table41").toString().replace("|",","):"");
//                cMap.put("table42", tableParamsList3.get(0).containsKey("table42")? tableParamsList3.get(0).get("table42").toString().replace("|",","):"");
//                cMap.put("table44", tableParamsList3.get(0).containsKey("table44")? tableParamsList3.get(0).get("table44").toString().replace("|",","):"");
//                cMap.put("table56", tableParamsList3.get(0).containsKey("table56")? tableParamsList3.get(0).get("table56").toString().replace("|",","):"");
//                cMap.put("table57", tableParamsList3.get(0).containsKey("table57")? tableParamsList3.get(0).get("table57").toString().replace("|",","):"");
//                cMap.put("table54", tableParamsList3.get(0).containsKey("table54")? tableParamsList3.get(0).get("table54").toString().replace("|",","):"");
//                cMap.put("table36", tableParamsList3.get(0).containsKey("table36")? tableParamsList3.get(0).get("table36").toString().replace("|",","):"");
//                cMap.put("table37", tableParamsList3.get(0).containsKey("table37")? tableParamsList3.get(0).get("table37").toString().replace("|",","):"");
//                cMap.put("table39", tableParamsList3.get(0).containsKey("table39")? tableParamsList3.get(0).get("table39").toString().replace("|",","):"");
//                cMap.put("table6", tableParamsList3.get(0).containsKey("table6")? tableParamsList3.get(0).get("table6").toString().replace("|",","):"");
//                cMap.put("table7", tableParamsList3.get(0).containsKey("table7")? tableParamsList3.get(0).get("table7").toString().replace("|",","):"");
//                cMap.put("table9", tableParamsList3.get(0).containsKey("table9")? tableParamsList3.get(0).get("table9").toString().replace("|",","):"");
//                cMap.put("table126", tableParamsList3.get(0).containsKey("table126")? tableParamsList3.get(0).get("table126").toString().replace("|",","):"");
//                cMap.put("table127", tableParamsList3.get(0).containsKey("table127")? tableParamsList3.get(0).get("table127").toString().replace("|",","):"");
//                cMap.put("table129", tableParamsList3.get(0).containsKey("table129")? tableParamsList3.get(0).get("table129").toString().replace("|",","):"");
//                cMap.put("table86", tableParamsList3.get(0).containsKey("table86")? tableParamsList3.get(0).get("table86").toString().replace("|",","):"");
//                cMap.put("table87", tableParamsList3.get(0).containsKey("table87")? tableParamsList3.get(0).get("table87").toString().replace("|",","):"");
//                cMap.put("table89", tableParamsList3.get(0).containsKey("table89")? tableParamsList3.get(0).get("table89").toString().replace("|",","):"");
//                cMap.put("table76", tableParamsList3.get(0).containsKey("table76")? tableParamsList3.get(0).get("table76").toString().replace("|",","):"");
//                cMap.put("table77", tableParamsList3.get(0).containsKey("table77")? tableParamsList3.get(0).get("table77").toString().replace("|",","):"");
//                cMap.put("table79", tableParamsList3.get(0).containsKey("table79")? tableParamsList3.get(0).get("table79").toString().replace("|",","):"");
//                cMap.put("table96", tableParamsList3.get(0).containsKey("table96")? tableParamsList3.get(0).get("table96").toString().replace("|",","):"");
//                cMap.put("table97", tableParamsList3.get(0).containsKey("table97")? tableParamsList3.get(0).get("table97").toString().replace("|",","):"");
//                cMap.put("table99", tableParamsList3.get(0).containsKey("table99")? tableParamsList3.get(0).get("table99").toString().replace("|",","):"");
//                cMap.put("table26", tableParamsList3.get(0).containsKey("table26")? tableParamsList3.get(0).get("table26").toString().replace("|",","):"");
//                cMap.put("table27", tableParamsList3.get(0).containsKey("table27")? tableParamsList3.get(0).get("table27").toString().replace("|",","):"");
//                cMap.put("table29", tableParamsList3.get(0).containsKey("table29")? tableParamsList3.get(0).get("table29").toString().replace("|",","):"");
//                cMap.put("table21", tableParamsList3.get(0).containsKey("table21")? tableParamsList3.get(0).get("table21").toString().replace("|",","):"");
//                cMap.put("table22", tableParamsList3.get(0).containsKey("table22")? tableParamsList3.get(0).get("table22").toString().replace("|",","):"");
//                cMap.put("table24", tableParamsList3.get(0).containsKey("table24")? tableParamsList3.get(0).get("table24").toString().replace("|",","):"");
//                cMap.put("table91", tableParamsList3.get(0).containsKey("table91")? tableParamsList3.get(0).get("table91").toString().replace("|",","):"");
//                cMap.put("table92", tableParamsList3.get(0).containsKey("table92")? tableParamsList3.get(0).get("table92").toString().replace("|",","):"");
//                cMap.put("table94", tableParamsList3.get(0).containsKey("table94")? tableParamsList3.get(0).get("table94").toString().replace("|",","):"");
//                cMap.put("table71", tableParamsList3.get(0).containsKey("table71")? tableParamsList3.get(0).get("table71").toString().replace("|",","):"");
//                cMap.put("table72", tableParamsList3.get(0).containsKey("table72")? tableParamsList3.get(0).get("table72").toString().replace("|",","):"");
//                cMap.put("table74", tableParamsList3.get(0).containsKey("table74")? tableParamsList3.get(0).get("table74").toString().replace("|",","):"");
//                cMap.put("table66", tableParamsList3.get(0).containsKey("table66")? tableParamsList3.get(0).get("table66").toString().replace("|",","):"");
//                cMap.put("table67", tableParamsList3.get(0).containsKey("table67")? tableParamsList3.get(0).get("table67").toString().replace("|",","):"");
//                cMap.put("table69", tableParamsList3.get(0).containsKey("table69")? tableParamsList3.get(0).get("table69").toString().replace("|",","):"");
//                cMap.put("table111", tableParamsList3.get(0).containsKey("table111")? tableParamsList3.get(0).get("table111").toString().replace("|",","):"");
//                cMap.put("table112", tableParamsList3.get(0).containsKey("table112")? tableParamsList3.get(0).get("table112").toString().replace("|",","):"");
//                cMap.put("table114", tableParamsList3.get(0).containsKey("table114")? tableParamsList3.get(0).get("table114").toString().replace("|",","):"");
//                cMap.put("table141", tableParamsList3.get(0).containsKey("table141")? tableParamsList3.get(0).get("table141").toString().replace("|",","):"");
//                cMap.put("table142", tableParamsList3.get(0).containsKey("table142")? tableParamsList3.get(0).get("table142").toString().replace("|",","):"");
//                cMap.put("table144", tableParamsList3.get(0).containsKey("table144")? tableParamsList3.get(0).get("table144").toString().replace("|",","):"");
//                cMap.put("table186", tableParamsList3.get(0).containsKey("table186")? tableParamsList3.get(0).get("table186").toString().replace("|",","):"");
//                cMap.put("table187", tableParamsList3.get(0).containsKey("table187")? tableParamsList3.get(0).get("table187").toString().replace("|",","):"");
//                cMap.put("table189", tableParamsList3.get(0).containsKey("table189")? tableParamsList3.get(0).get("table189").toString().replace("|",","):"");
//                cMap.put("table116", tableParamsList3.get(0).containsKey("table116")? tableParamsList3.get(0).get("table116").toString().replace("|",","):"");
//                cMap.put("table117", tableParamsList3.get(0).containsKey("table117")? tableParamsList3.get(0).get("table117").toString().replace("|",","):"");
//                cMap.put("table119", tableParamsList3.get(0).containsKey("table119")? tableParamsList3.get(0).get("table119").toString().replace("|",","):"");
//                cMap.put("table136", tableParamsList3.get(0).containsKey("table136")? tableParamsList3.get(0).get("table136").toString().replace("|",","):"");
//                cMap.put("table137", tableParamsList3.get(0).containsKey("table137")? tableParamsList3.get(0).get("table137").toString().replace("|",","):"");
//                cMap.put("table139", tableParamsList3.get(0).containsKey("table139")? tableParamsList3.get(0).get("table139").toString().replace("|",","):"");
//                cMap.put("table156", tableParamsList3.get(0).containsKey("table156")? tableParamsList3.get(0).get("table156").toString().replace("|",","):"");
//                cMap.put("table157", tableParamsList3.get(0).containsKey("table157")? tableParamsList3.get(0).get("table157").toString().replace("|",","):"");
//                cMap.put("table159", tableParamsList3.get(0).containsKey("table159")? tableParamsList3.get(0).get("table159").toString().replace("|",","):"");
//                cMap.put("table106", tableParamsList3.get(0).containsKey("table106")? tableParamsList3.get(0).get("table106").toString().replace("|",","):"");
//                cMap.put("table107", tableParamsList3.get(0).containsKey("table107")? tableParamsList3.get(0).get("table107").toString().replace("|",","):"");
//                cMap.put("table109", tableParamsList3.get(0).containsKey("table109")? tableParamsList3.get(0).get("table109").toString().replace("|",","):"");
//                cMap.put("table121", tableParamsList3.get(0).containsKey("table121")? tableParamsList3.get(0).get("table121").toString().replace("|",","):"");
//                cMap.put("table122", tableParamsList3.get(0).containsKey("table122")? tableParamsList3.get(0).get("table122").toString().replace("|",","):"");
//                cMap.put("table124", tableParamsList3.get(0).containsKey("table124")? tableParamsList3.get(0).get("table124").toString().replace("|",","):"");
//                cMap.put("table101", tableParamsList3.get(0).containsKey("table101")? tableParamsList3.get(0).get("table101").toString().replace("|",","):"");
//                cMap.put("table102", tableParamsList3.get(0).containsKey("table102")? tableParamsList3.get(0).get("table102").toString().replace("|",","):"");
//                cMap.put("table104", tableParamsList3.get(0).containsKey("table104")? tableParamsList3.get(0).get("table104").toString().replace("|",","):"");
//                cMap.put("table131", tableParamsList3.get(0).containsKey("table131")? tableParamsList3.get(0).get("table131").toString().replace("|",","):"");
//                cMap.put("table132", tableParamsList3.get(0).containsKey("table132")? tableParamsList3.get(0).get("table132").toString().replace("|",","):"");
//                cMap.put("table134", tableParamsList3.get(0).containsKey("table134")? tableParamsList3.get(0).get("table134").toString().replace("|",","):"");
//                cMap.put("table181", tableParamsList3.get(0).containsKey("table181")? tableParamsList3.get(0).get("table181").toString().replace("|",","):"");
//                cMap.put("table182", tableParamsList3.get(0).containsKey("table182")? tableParamsList3.get(0).get("table182").toString().replace("|",","):"");
//                cMap.put("table184", tableParamsList3.get(0).containsKey("table184")? tableParamsList3.get(0).get("table184").toString().replace("|",","):"");
//                cMap.put("table151", tableParamsList3.get(0).containsKey("table151")? tableParamsList3.get(0).get("table151").toString().replace("|",","):"");
//                cMap.put("table152", tableParamsList3.get(0).containsKey("table152")? tableParamsList3.get(0).get("table152").toString().replace("|",","):"");
//                cMap.put("table154", tableParamsList3.get(0).containsKey("table154")? tableParamsList3.get(0).get("table154").toString().replace("|",","):"");
//                cMap.put("table176", tableParamsList3.get(0).containsKey("table176")? tableParamsList3.get(0).get("table176").toString().replace("|",","):"");
//                cMap.put("table177", tableParamsList3.get(0).containsKey("table177")? tableParamsList3.get(0).get("table177").toString().replace("|",","):"");
//                cMap.put("table179", tableParamsList3.get(0).containsKey("table179")? tableParamsList3.get(0).get("table179").toString().replace("|",","):"");
//                cMap.put("table161", tableParamsList3.get(0).containsKey("table161")? tableParamsList3.get(0).get("table161").toString().replace("|",","):"");
//                cMap.put("table162", tableParamsList3.get(0).containsKey("table162")? tableParamsList3.get(0).get("table162").toString().replace("|",","):"");
//                cMap.put("table164", tableParamsList3.get(0).containsKey("table164")? tableParamsList3.get(0).get("table164").toString().replace("|",","):"");
//                cMap.put("table146", tableParamsList3.get(0).containsKey("table146")? tableParamsList3.get(0).get("table146").toString().replace("|",","):"");
//                cMap.put("table147", tableParamsList3.get(0).containsKey("table147")? tableParamsList3.get(0).get("table147").toString().replace("|",","):"");
//                cMap.put("table149", tableParamsList3.get(0).containsKey("table149")? tableParamsList3.get(0).get("table149").toString().replace("|",","):"");
//                cMap.put("table206", tableParamsList3.get(0).containsKey("table206")? tableParamsList3.get(0).get("table206").toString().replace("|",","):"");
//                cMap.put("table207", tableParamsList3.get(0).containsKey("table207")? tableParamsList3.get(0).get("table207").toString().replace("|",","):"");
//                cMap.put("table209", tableParamsList3.get(0).containsKey("table209")? tableParamsList3.get(0).get("table209").toString().replace("|",","):"");
//                cMap.put("table171", tableParamsList3.get(0).containsKey("table171")? tableParamsList3.get(0).get("table171").toString().replace("|",","):"");
//                cMap.put("table172", tableParamsList3.get(0).containsKey("table172")? tableParamsList3.get(0).get("table172").toString().replace("|",","):"");
//                cMap.put("table174", tableParamsList3.get(0).containsKey("table174")? tableParamsList3.get(0).get("table174").toString().replace("|",","):"");
//                cMap.put("table201", tableParamsList3.get(0).containsKey("table201")? tableParamsList3.get(0).get("table201").toString().replace("|",","):"");
//                cMap.put("table202", tableParamsList3.get(0).containsKey("table202")? tableParamsList3.get(0).get("table202").toString().replace("|",","):"");
//                cMap.put("table204", tableParamsList3.get(0).containsKey("table204")? tableParamsList3.get(0).get("table204").toString().replace("|",","):"");
//                cMap.put("table196", tableParamsList3.get(0).containsKey("table196")? tableParamsList3.get(0).get("table196").toString().replace("|",","):"");
//                cMap.put("table197", tableParamsList3.get(0).containsKey("table197")? tableParamsList3.get(0).get("table197").toString().replace("|",","):"");
//                cMap.put("table199", tableParamsList3.get(0).containsKey("table199")? tableParamsList3.get(0).get("table199").toString().replace("|",","):"");
//                cMap.put("table166", tableParamsList3.get(0).containsKey("table166")? tableParamsList3.get(0).get("table166").toString().replace("|",","):"");
//                cMap.put("table167", tableParamsList3.get(0).containsKey("table167")? tableParamsList3.get(0).get("table167").toString().replace("|",","):"");
//                cMap.put("table169", tableParamsList3.get(0).containsKey("table169")? tableParamsList3.get(0).get("table169").toString().replace("|",","):"");
//                cMap.put("table191", tableParamsList3.get(0).containsKey("table191")? tableParamsList3.get(0).get("table191").toString().replace("|",","):"");
//                cMap.put("table192", tableParamsList3.get(0).containsKey("table192")? tableParamsList3.get(0).get("table192").toString().replace("|",","):"");
//                cMap.put("table194", tableParamsList3.get(0).containsKey("table194")? tableParamsList3.get(0).get("table194").toString().replace("|",","):"");
//                cMap.put("table11", tableParamsList3.get(0).containsKey("table11")? tableParamsList3.get(0).get("table11").toString().replace("|",","):"");
//                cMap.put("table12", tableParamsList3.get(0).containsKey("table12")? tableParamsList3.get(0).get("table12").toString().replace("|",","):"");
//                cMap.put("table14", tableParamsList3.get(0).containsKey("table14")? tableParamsList3.get(0).get("table14").toString().replace("|",","):"");
//                cMap.put("table16", tableParamsList3.get(0).containsKey("table16")? tableParamsList3.get(0).get("table16").toString().replace("|",","):"");
//                cMap.put("table17", tableParamsList3.get(0).containsKey("table17")? tableParamsList3.get(0).get("table17").toString().replace("|",","):"");
//                cMap.put("table19", tableParamsList3.get(0).containsKey("table19")? tableParamsList3.get(0).get("table19").toString().replace("|",","):"");
//                cMap.put("table81", tableParamsList3.get(0).containsKey("table81")? tableParamsList3.get(0).get("table81").toString().replace("|",","):"");
//                cMap.put("table82", tableParamsList3.get(0).containsKey("table82")? tableParamsList3.get(0).get("table82").toString().replace("|",","):"");
//                cMap.put("table84", tableParamsList3.get(0).containsKey("table84")? tableParamsList3.get(0).get("table84").toString().replace("|",","):"");

            }else{
                cMap.put("tableParams", "");
                cMap.put("tableParams3", "");
                cMap.put("tableParams2", "");
                cMap.put("textList", "");
                cMap.put("AMT_UNIT_NAME", "");
                cMap.put("echartsData", "");
            }
System.out.println("-----------------------"+cMap.get("text000031_lib").toString());
            //按日期设定当前Excel表格的名字
            String newFileName = htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"季度重庆市国库运行简况.docx";
            String jidu=htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2") ? "上半年":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1")?"1季度":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"1-3季度":"全年";
            String    title =  htmlReport.get("YEAR").toString()+"年"+jidu;
            String targetFilePath= saveDir + "guoku2/"+newFileName;
            cMap.put("monthTitle", title);
            cMap.put("YEAR", htmlReport.get("YEAR").toString());
            cMap.put("month", htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1") ? "3":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2")?"6":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"9":"12" );
            cMap.put("qtr", htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2") ? "上半年":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1")?"1季度":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"1-3季度":"全年" );
            cMap.put("monthTitle", title);
            FileWriter out = new FileWriter(targetFilePath);
            new FreeMarker_Util().renderTpl(cMap, "qutReport.xml", out);
            response.setHeader("content-type", "text/plain");
            response.setHeader("content-type", "application/x-msdownload;");
            response.setContentType("text/plain; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(newFileName.getBytes("utf-8"), "ISO-8859-1"));
            byte[] buff = new byte[1024];
            OutputStream os = null;
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(targetFilePath));
            int i = bis.read(buff);

            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        BufferedInputStream bis = null;
//        try{
//            Map<String, Object> htmlReport = reportService.getHtmlReport(pd);
//            //word内容
//            String content=htmlReport.get("HTML_REPORT").toString().replace("\\","\\");
////            if("0".equals(pd.getString("state"))){
//////                content=content.replace("font-size:14px;text-align:center;","font-size:3px;text-align:center;");
//////            }
//            if ("0".equals(pd.getString("state"))) {
//                // state 0   windows 7
//                content = content.replace("font-size:14px;text-align:center;", "font-size:3px;text-align:center;");
//            }
////            content = content.replace("font-family: SimSun;", "");
//            content = content.replace("font-size: 14px; text-align: center; font-family: SimSun;", "font-size:14px;font-family: 'FangSong';text-align:center;");
//            content = content.replace("line-height: 38px;", "line-height: 20px;font-size:21.3328px;font-family: 'FangSong';");
//            content = content.replace("padding: 8px 0px;", "margin: 0.6665 0px;line-height: 39.9990px;font-family: 'FangSong';");
//            content = content.replace("font-family: SimSun; margin: 0px;", "text-align: center;margin: 0px;line-height: 39.9990px;font-family: 'FangSong';font-size:24px");
//            content = content.replace("font-family: SimSun;", "font-family: 'FangSong';");
//            content = content.replace("text-indent: 2em;", "text-indent: 3.24324em;");
//            content = content.replace("font-family: FangSong;", "font-family: 'FangSong_GB2312';");
//            String foot="";
//            String    title =  htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"月";
//            String    area =  htmlReport.get("DIM_DESC").toString();
//
//            StringBuilder sb = new StringBuilder(content);
//            if("1".equals(htmlReport.get("ISNOTES"))){
////            替换的固定字符注释
//                String replaceName="";
////            replaceNameNum替换字符的长度;indexNum替换字符在整篇文档中第几次出现的位置;
//                int replaceNameNum,indexNum;
////            详细注释说明
//                String replaceNameDesc="";
//                JSONArray jsonNotes= JSONArray.parseArray(htmlReport.get("NOTES").toString());
//                JSONArray jsonNotesNum= JSONArray.parseArray(htmlReport.get("NOTESNUM").toString());
//                JSONArray jsonNotesDesc= JSONArray.parseArray(htmlReport.get("NOTESDESC").toString());
//                for(int i=0 ;i<jsonNotes.size();i++){
//                    replaceName=jsonNotes.get(i).toString();
//                    replaceNameNum=replaceName.length();
//                    indexNum=Integer.parseInt(jsonNotesNum.get(i).toString());
//                    replaceNameDesc=jsonNotesDesc.get(i).toString();
////                 replaceName="重庆市";
////                 replaceNameNum=replaceName.length();
////                 indexNum=3;
////                 replaceNameDesc="地方级国库收入包括一般公共预算收入、基金预算收入和国有资本经营预算收入，即地方政府自有财力，不包含地方政府债务收入和转移性收入。";
//                    int cc=getCharacterPosition(sb.toString(),1,"<sup");
//                    int dd=getCharacterPosition(sb.toString(),1,"</sup>");
//                    sb.replace(cc, dd+6, "");
//                    int ee=getCharacterPosition(sb.toString(),1,"<b");//
//                    int ff=getCharacterPosition(sb.toString(),1,"</b>");//
//                    sb.replace(ee, ff+4, "");
//                    int bb=getCharacterPosition(sb.toString(),indexNum,replaceName);
//                    String str = "<span\n" +
//                            "lang=EN-US style='font-size:21.3328px;font-family:FangSong;mso-fareast-theme-font:minor-fareast;\n" +
//                            "mso-font-kerning:0pt'>"+replaceName+"<a style='mso-footnote-id:ftn1' href==\n" +
//                            "\"#_ftn1\"\n" +
//                            "name=\"_ftnref1\" title=\"\"><span class=MsoFootnoteReference><span\n" +
//                            "style='mso-special-character:footnote'><![if !supportFootnotes]><span\n" +
//                            "class=MsoFootnoteReference><span lang=EN-US style='font-size:12.0pt;f=\n" +
//                            "ont-family:\n" +
//                            "\"Times New Roman\",serif;mso-fareast-font-family:DengXian;mso-fareast-theme-=\n" +
//                            "font:\n" +
//                            "minor-fareast;mso-ansi-language:EN-US;mso-fareast-language:ZH-CN;mso-bidi-l=\n" +
//                            "anguage:\n" +
//                            "AR-SA'>[1]</span></span><![endif]></span></span></a><o:p></o:p></span>";
//                    foot="<div style=3D'mso-element:footnote' id=3Dftn1> <p class=3DMsoFootnoteText><a style=3D'mso-footnote-id:ftn1' href=3D\"#_ftnr=\n" +
//                            "ef1\"\n" +
//                            "name=3D\"_ftn1\" title=3D\"\"><span class=3DMsoFootnoteReference><span lang=3DE=\n" +
//                            "N-US><span\n" +
//                            "style=3D'mso-special-character:footnote'><![if !supportFootnotes]><span\n" +
//                            "class=3DMsoFootnoteReference><span lang=3DEN-US style=3D'font-size:9.0pt;fo=\n" +
//                            "nt-family:\n" +
//                            "\\FFFD\\FFFD\\FFFD\\FFFD;mso-fareast-font-family:SimSun;mso-bidi-font-family:\"T=\n" +
//                            "imes New Roman\";\n" +
//                            "mso-font-kerning:1.0pt;mso-ansi-language:EN-US;mso-fareast-language:ZH-CN;\n" +
//                            "mso-bidi-language:AR-SA'>[1]</span></span><![endif]></span></span></span></=\n" +
//                            "a><span\n" +
//                            "lang=3DEN-US> "+replaceNameDesc+"</span></p> </div></div> <div class=3DWordSection1> <p class=3D><span lang=3DEN-US><o:p>&nbsp;</o:p></span></p></div>";
////                      "lang=3DEN-US> "+stringToUnicode(replaceNameDesc)+"</span></p> </div></div> <div class=3DWordSection1> <p class=3D><span lang=3DEN-US><o:p>&nbsp;</o:p></span></p></div>";
////              sb = new StringBuilder(content);
//                    sb.replace(bb, bb+replaceNameNum, str);
//                }
//            }
//            String filePath = saveDir+pd.getString("ADD_USERID")+ File.separator ;
////            String fileName = "季度报告.docx";
//            //            2020年6月
//            String fileName = htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"月重庆市国库资金运行报告.docx";
//            String fileFullName = filePath +fileName;
//            //判断是否存在目录. 不存在则创建
//            isChartPathExist(filePath);
//            RichHtmlHandler handler = new RichHtmlHandler(sb.toString());
//            handler.setDocSrcLocationPrex("file:///C:/EA1B1DF0");
//            handler.setDocSrcParent("AI_ER_TEMP.files");
//            handler.setNextPartId("01D61BE2.D6AD0370");
//            handler.setShapeidPrex("图片_x0020_2");
//            handler.setSpidPrex("_x0000_i1025");
//            handler.setTypeid("#_x0000_t75");
//            handler.handledHtml(false,0,MAX_HEIGHT_SIZE,MAX_WIDTH_SIZE,QTR_MAX_HEIGHT_SIZE,QTR_MAX_WIDTH_SIZE);
////            handler.handledHtml(false,MAX_WIDTH_SIZE, MAX_HEIGHT_SIZE);
//            Map<String, Object> fileData = new HashMap<>();
//            fileData.put("AIER_CONTENT", handler.getHandledDocBodyBlock());
//            fileData.put("AIER_FOOT", foot);
//            fileData.put("title", title);
//            fileData.put("area", htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"季度国库运行快报.docx");
//            fileData.put("ISCOVER", pd.getString("isCover"));
//            fileData.put("ISNOTES", htmlReport.get("ISNOTES"));
//            fileData.put("imagesXmlHrefString", handler.getXmlImgRefs());
//            fileData.put("imagesBase64String", handler.getDocBase64BlockResults());
//            FileOutputStream out = new FileOutputStream(fileFullName);
//            WordGeneratorWithFreemarker.createDoc(fileData, "AI_ER_TEMP.ftl", out);
//            response.setHeader("content-type", "text/plain");
//            response.setHeader("content-type", "application/x-msdownload;");
//            response.setContentType("text/plain; charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
//            byte[] buff = new byte[1024];
//            OutputStream os = null;
//            os = response.getOutputStream();
//            bis = new BufferedInputStream(new FileInputStream(fileFullName));
//            int i = bis.read(buff);
//
//            while (i != -1) {
//                os.write(buff, 0, buff.length);
//                os.flush();
//                i = bis.read(buff);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bis != null) {
//                try {
//                    bis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        System.out.println("export file finish");


    }
    /**
     * 判断文件夹是否存在，如果不存在则新建
     *
     * @param dirPath 文件夹路径
     */
    private static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public static int getCharacterPosition(String string,int indexNum,String replaceName){
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile(replaceName).matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if(mIdx == indexNum){
                break;
            }
        }
        return slashMatcher.start();
    }
    /**
     * 字符串转unicode
     *
     * @param str
     * @return
     */
    public static String stringToUnicode(String str) {
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            sb.append("\\u" + Integer.toHexString(c[i]));
        }
        return sb.toString();
    }
    /**
     *
     * String转map
     * @param str
     * @return
     */
    public static Map<String,Object> getStringToMap(String str){
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String,Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split("=");
            if(str2.length>1){
                //str2[0]为KEY,str2[1]为值
                map.put(str2[0],str2[1].replace("|",","));
            }else{
                //str2[0]为KEY,str2[1]为值

                map.put(str2[0],"0.00");
            }
        }
        return map;
    }
    /**
     *
     * String转map
     * @param str
     * @return
     */
    public static Map<String,Object> getStringToMap1(String str){
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String,Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split("=");
            if(str2.length>1){
                //str2[0]为KEY,str2[1]为值
                map.put(str2[0],str2[1].replace(",","、").replace("|",","));
            }else{
                //str2[0]为KEY,str2[1]为值

                map.put(str2[0],"0.00");
            }
        }
        return map;
    }

}
