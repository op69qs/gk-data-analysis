package org.fixedReport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.fixedReport.BaseController;
import org.fixedReport.service.NewsFlashQuarterService;
import org.fixedReport.service.ReportService;
import org.fixedReport.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fixedReport.controller.ReportController.getStringToMap;

/**
 * 国库二期季度快报
 *
 * @author Created by dj on 2020/05/11.
 */
@Slf4j
@RestController
@Api(tags = "国库二期季度快报")
@RequestMapping(value = "/newsFlashQuarter", produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsFlashQuarterController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Value("${MAX_WIDTH_SIZE}")
    private int  MAX_WIDTH_SIZE;

    @Value("${MAX_HEIGHT_SIZE}")
    private int MAX_HEIGHT_SIZE;

    @Value("${QTR_MAX_WIDTH_SIZE}")
    private int QTR_MAX_WIDTH_SIZE;

    @Value("${QTR_MAX_HEIGHT_SIZE}")
    private int QTR_MAX_HEIGHT_SIZE;
    @Autowired
    private NewsFlashQuarterService newsFlashQuarterService;
    @Autowired
    private ReportService reportService;

    /**
     * 封装季度快报 4个表（表1直接从text取数） 四个图
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "封装季度快报")
    @PostMapping(value = "/addNewsFlashQuarterReport")
    public Map<String, Object> addNewsFlashQuarterReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            pd.put("monthDate",pd.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2") ? "-06":pd.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1")?"-03":pd.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"-09":"-12");
            List<Map<String, Object>> textList = newsFlashQuarterService.getTextAndParams(pd);
            List<Map<String, Object>> tableParamsList = newsFlashQuarterService.getTableParams(pd);
            List<Map<String, Object>> tableParamsList2 = newsFlashQuarterService.getTableParams2(pd);
            List<Map<String, Object>> tableParamsList3 = newsFlashQuarterService.getTableParams3(pd);
            List listMap = new ArrayList<>();
            List<Map<String, Object>> graphParamsList = newsFlashQuarterService.getGraphParams(pd);
            List<Map<String, Object>> graphParamsList2 = newsFlashQuarterService.getGraphParams2(pd);
            List<Map<String, Object>> graphParamsList3 = newsFlashQuarterService.getGraphParams3(pd);
            List<Map<String, Object>> graphParamsList4 = newsFlashQuarterService.getGraphParams4(pd);
            List<Map<String, Object>> graphParamsList5 = newsFlashQuarterService.getGraphParams5(pd);
            List<Map<String, Object>> graphParamsList6 = newsFlashQuarterService.getGraphParams6(pd);
            //            第一个图需要三个变量  data_dateAll  series1   series2
            Map  map = new HashMap<String,Object>();
            String  series="";
            String  data_dateAll="";
            if(graphParamsList.size()>0){
                data_dateAll=graphParamsList.get(0).get("ACCOUNT_PERIOD").toString();
                map.put("data_dateAll",data_dateAll);
                int data[] = new int[4];
                for(int i=0;i<graphParamsList.size();i++){
                    String id=graphParamsList.get(i).get("id").toString();
                    data[i]=Integer.parseInt(id);
                    series=graphParamsList.get(i).get("VAL").toString();
                    map.put("MAX_VAL"+id,graphParamsList.get(i).get("MAX_VAL").toString());
                    map.put("MIN_VAL"+id,graphParamsList.get(i).get("MIN_VAL").toString());
                    map.put("series"+id,series);
                }
                for(int j=1;j<5;j++){
                     if(ArrayUtils.contains(data, j)){

                     }else {
                         map.put("series"+j,"");
                     }
                }

            }else {
                map.put("MAX_VAL1","0");
                map.put("MIN_VAL1","0");
                map.put("MAX_VAL2","0");
                map.put("MIN_VAL2","0");
                map.put("MAX_VAL3","0");
                map.put("MIN_VAL3","0");
                map.put("MAX_VAL4","0");
                map.put("MIN_VAL4","0");
                map.put("data_dateAll",data_dateAll);
                map.put("series1",series);
                map.put("series2",series);
                map.put("series3",series);
                map.put("series4",series);
            }
            listMap.add(map);
//            第二个图需要三个变量     series
            Map  map2 = new HashMap<String,Object>();
            List<Map> listMap2 = new  ArrayList<>();
            if(graphParamsList2.size()>0) {
                for (int i = 0; i < graphParamsList2.size(); i++) {
                    Map map21 = new HashMap<String, Object>();
                    map21.put("value", graphParamsList2.get(i).get("value").toString());
                    map21.put("name", graphParamsList2.get(i).get("name").toString());
                    listMap2.add(map21);
                }
            }
                map2.put("series", listMap2);
                listMap.add(map2);
//            第三个图需要三个变量     series
                Map map3 = new HashMap<String, Object>();
                List<Map> listMap3 = new ArrayList<>();
                if (graphParamsList3.size() > 0) {
                    for (int i = 0; i < graphParamsList3.size(); i++) {
                        Map map31 = new HashMap<String, Object>();
                        map31.put("value", graphParamsList3.get(i).get("value").toString());
                        map31.put("name", graphParamsList3.get(i).get("name").toString());
                        listMap3.add(map31);
                    }
                }
                map3.put("series", listMap3);
                listMap.add(map3);
                //            第四个图需要三个变量     series
                Map map4 = new HashMap<String, Object>();
                List<Map> listMap4 = new ArrayList<>();
                if (graphParamsList4.size() > 0) {
                    for (int i = 0; i < graphParamsList4.size(); i++) {
                        Map map41 = new HashMap<String, Object>();
                        map41.put("value", graphParamsList4.get(i).get("value").toString());
                        map41.put("name", graphParamsList4.get(i).get("name").toString());
                        listMap4.add(map41);
                    }
                }
                map4.put("series", listMap4);
                listMap.add(map4);
                //            第五个图需要三个变量     series
                Map map5 = new HashMap<String, Object>();
                List<Map> listMap5 = new ArrayList<>();
                if (graphParamsList5.size() > 0) {
                    for (int i = 0; i < graphParamsList5.size(); i++) {
                        Map map51 = new HashMap<String, Object>();
                        map51.put("value", graphParamsList5.get(i).get("value").toString());
                        map51.put("name", graphParamsList5.get(i).get("name").toString());
                        listMap5.add(map51);
                    }
                }
                map5.put("series", listMap5);
                listMap.add(map5);
                //            第六个图需要三个变量     series
                Map map6 = new HashMap<String, Object>();
                List<Map> listMap6 = new ArrayList<>();
                if (graphParamsList6.size() > 0) {
                    for (int i = 0; i < graphParamsList6.size(); i++) {
                        Map map61 = new HashMap<String, Object>();
                        map61.put("value", graphParamsList6.get(i).get("value").toString());
                        map61.put("name", graphParamsList6.get(i).get("name").toString());
                        listMap6.add(map61);
                    }
                }
                map6.put("series", listMap6);
                listMap.add(map6);
//                Map map7 = new HashMap<String, Object>();
//                map.put("",graphParamsList3.get(0).get("INDEX_VALUE").toString());

                rows.put("numAll", graphParamsList3.toString());
                if (textList.size() > 0) {
                    rows.put("textList", textList.toString());
                } else {
                    rows.put("textList", "");
                }
                if (tableParamsList.size() > 0) {
                    rows.put("tableParams", tableParamsList);
                } else {
                    rows.put("tableParams", "");
                }
                if (tableParamsList2.size() > 0) {
                    rows.put("tableParams2", tableParamsList2.toString().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(" ", ""));
                } else {
                    rows.put("tableParams2", "");
                }
                if (tableParamsList3.size() > 0) {
                    rows.put("tableParams3", tableParamsList3);
                } else {
                    rows.put("tableParams3", "");
                }
                String objStr = JSON.toJSONString(listMap);
                rows.put("echartsData", objStr);
                rows.put("REPORT_ID", pd.getString("REPORT_ID"));
                rows.put("AMT_UNIT", pd.getString("AMT_UNIT"));
                rows.put("AMT_UNIT_NAME", pd.getString("AMT_UNIT_NAME"));
                rows.put("ACCOUNT_PERIOD", pd.getString("ACCOUNT_PERIOD"));
                rows.put("REPORT_TYPE_ID", pd.getString("REPORT_TYPE_ID"));
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
    @ApiOperation(value = "获取结构化季度快报")
    @PostMapping(value = "/getMonthlyReport")
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
            if(allDataList.size()>0) {
                if (!allDataList.get("textList").toString().isEmpty()) {
                    mapTextList = getStringToMap(allDataList.get("textList").toString().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(" ", ""));
                }
                JSONObject jsonObj = new JSONObject(mapTextList);
                if (!allDataList.get("tableParams2").toString().isEmpty()) {
                    mapTalbe2 = getStringToMap(allDataList.get("tableParams2").toString());
                }
                JSONObject jsonObjTable2 = new JSONObject(mapTalbe2);
                List<Map<String, Object>> tableParamsList3 = newsFlashQuarterService.getTableParams3(pd);
                List<Map<String, Object>> tableParamsList = newsFlashQuarterService.getTableParams(pd);


                String  echartsData= allDataList.get("echartsData").toString();
                List<Map<String,Object>>  jsoin= (List<Map<String,Object>>)JSONObject.parse(echartsData);
                rows.put("echartsData", jsoin);
                rows.put("tableParams", tableParamsList);
                rows.put("tableParams3", tableParamsList3);
                rows.put("tableParams2", jsonObjTable2);
                rows.put("tableParams2", jsonObjTable2);
                rows.put("textList", jsonObj);
                rows.put("AMT_UNIT_NAME", allDataList.get("AMT_UNIT_NAME").toString());
            }else{
                rows.put("tableParams", "");
                rows.put("tableParams3", "");
                rows.put("tableParams2", "");
                rows.put("textList", "");
                rows.put("AMT_UNIT_NAME", "");
                rows.put("echartsData", "");
            }
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
     * 打包下载月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载季度快报")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @RequestBody(required = false
            ) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        BufferedInputStream bis = null;
        Map<String,Object> cMap = new HashMap();
        Map mapTextList=new HashMap();
        Map mapTalbe2=new HashMap();
        try{
            Map<String, Object> htmlReport = reportService.getHtmlReport(pd);

            Map<String, Object> htmlReportData = reportService.getMonthlyReport(pd);
            pd.put("AMT_UNIT",htmlReportData.get("AMT_UNIT"));
            pd.put("ACCOUNT_PERIOD",htmlReportData.get("ACCOUNT_PERIOD"));
            if(htmlReportData.size()>0) {
                if (!htmlReportData.get("textList").toString().isEmpty()) {
                    mapTextList = getStringToMap(htmlReportData.get("textList").toString().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(" ", ""));
                }
                JSONObject jsonObj = new JSONObject(mapTextList);
                if (!htmlReportData.get("tableParams2").toString().isEmpty()) {
                    mapTalbe2 = getStringToMap(htmlReportData.get("tableParams2").toString());
                }
                JSONObject jsonObjTable2 = new JSONObject(mapTalbe2);
                List<Map<String, Object>> tableParamsList3 = newsFlashQuarterService.getTableParams3(pd);
                if(tableParamsList3.size()==0){
                    tableParamsList3 = newsFlashQuarterService.getTableParams4(pd);
                }
                List<Map<String, Object>> tableParamsList = newsFlashQuarterService.getTableParams(pd);
                cMap.put("mapTextList000252", mapTextList.containsKey("000252")? mapTextList.get("000252"):"");
                cMap.put("mapTextList000458", mapTextList.containsKey("000458")? mapTextList.get("000458"):"");
                cMap.put("mapTextList000459", mapTextList.containsKey("000459")? mapTextList.get("000459"):"");
                cMap.put("mapTextList000460", mapTextList.containsKey("000460")? mapTextList.get("000460"):"");
                cMap.put("mapTextList000461", mapTextList.containsKey("000461")? mapTextList.get("000461"):"");
                cMap.put("mapTextList000453", mapTextList.containsKey("000453")? mapTextList.get("000453"):"");
                cMap.put("mapTextList000331", mapTextList.containsKey("000331")? mapTextList.get("000331"):"");
                cMap.put("mapTextList000228", mapTextList.containsKey("000228")? mapTextList.get("000228"):"");
                cMap.put("mapTextList000001", mapTextList.containsKey("000001")? mapTextList.get("000001"):"");
                cMap.put("mapTextList000008", mapTextList.containsKey("000008")? mapTextList.get("000008"):"");
                cMap.put("mapTextList000052", mapTextList.containsKey("000052")? mapTextList.get("000052"):"");
                cMap.put("mapTextList000015", mapTextList.containsKey("000015")? mapTextList.get("000015"):"");
                cMap.put("mapTextList000117", mapTextList.containsKey("000117")? mapTextList.get("000117"):"");
                cMap.put("mapTextList000021", mapTextList.containsKey("000021")? mapTextList.get("000021"):"");
                cMap.put("mapTextList000194", mapTextList.containsKey("000194")? mapTextList.get("000194"):"");
                cMap.put("mapTextList000198", mapTextList.containsKey("000198")? mapTextList.get("000198"):"");
                cMap.put("mapTextList000202", mapTextList.containsKey("000202")? mapTextList.get("000202"):"");
                cMap.put("mapTextList000206", mapTextList.containsKey("000206")? mapTextList.get("000206"):"");
                cMap.put("mapTextList000210", mapTextList.containsKey("000210")? mapTextList.get("000210"):"");
                cMap.put("mapTextList000214", mapTextList.containsKey("000214")? mapTextList.get("000214"):"");
                cMap.put("mapTextList000027", mapTextList.containsKey("000027")? mapTextList.get("000027"):"");
                cMap.put("mapTextList000034", mapTextList.containsKey("000034")? mapTextList.get("000034"):"");
                cMap.put("mapTextList000040", mapTextList.containsKey("000040")? mapTextList.get("000040"):"");
                cMap.put("mapTextList000046", mapTextList.containsKey("000046")? mapTextList.get("000046"):"");
                cMap.put("mapTextList000131", mapTextList.containsKey("000131")? mapTextList.get("000131"):"");
                cMap.put("mapTextList000133", mapTextList.containsKey("000133")? mapTextList.get("000133"):"");
                cMap.put("mapTextList000253", mapTextList.containsKey("000253")? mapTextList.get("000253"):"");
                cMap.put("mapTextList000229", mapTextList.containsKey("000229")? mapTextList.get("000229"):"");
                cMap.put("mapTextList000002", mapTextList.containsKey("000002")? mapTextList.get("000002"):"");
                cMap.put("mapTextList000009", mapTextList.containsKey("000009")? mapTextList.get("000009"):"");
                cMap.put("mapTextList000054", mapTextList.containsKey("000054")? mapTextList.get("000054"):"");
                cMap.put("mapTextList000016", mapTextList.containsKey("000016")? mapTextList.get("000016"):"");
                cMap.put("mapTextList000120", mapTextList.containsKey("000120")? mapTextList.get("000120"):"");
                cMap.put("mapTextList000022", mapTextList.containsKey("000022")? mapTextList.get("000022"):"");
                cMap.put("mapTextList000195", mapTextList.containsKey("000195")? mapTextList.get("000195"):"");
                cMap.put("mapTextList000199", mapTextList.containsKey("000199")? mapTextList.get("000199"):"");
                cMap.put("mapTextList000203", mapTextList.containsKey("000203")? mapTextList.get("000203"):"");
                cMap.put("mapTextList000207", mapTextList.containsKey("000207")? mapTextList.get("000207"):"");
                cMap.put("mapTextList000211", mapTextList.containsKey("000211")? mapTextList.get("000211"):"");
                cMap.put("mapTextList000215", mapTextList.containsKey("000215")? mapTextList.get("000215"):"");
                cMap.put("mapTextList000028", mapTextList.containsKey("000028")? mapTextList.get("000028"):"");
                cMap.put("mapTextList000035", mapTextList.containsKey("000035")? mapTextList.get("000035"):"");
                cMap.put("mapTextList000041", mapTextList.containsKey("000041")? mapTextList.get("000041"):"");
                cMap.put("mapTextList000047", mapTextList.containsKey("000047")? mapTextList.get("000047"):"");
                cMap.put("mapTextList000132", mapTextList.containsKey("000132")? mapTextList.get("000132"):"");
                cMap.put("mapTextList000134", mapTextList.containsKey("000134")? mapTextList.get("000134"):"");
                cMap.put("mapTextList000172", mapTextList.containsKey("000172")? mapTextList.get("000172"):"");
                cMap.put("mapTextList000177", mapTextList.containsKey("000177")? mapTextList.get("000177"):"");
                cMap.put("mapTextList000180", mapTextList.containsKey("000180")? mapTextList.get("000180"):"");
                cMap.put("mapTextList000038", mapTextList.containsKey("000038")? mapTextList.get("000038"):"");
                cMap.put("mapTextList000044", mapTextList.containsKey("000044")? mapTextList.get("000044"):"");
                cMap.put("mapTextList000050", mapTextList.containsKey("000050")? mapTextList.get("000050"):"");
                cMap.put("mapTextList000222", mapTextList.containsKey("000222")? mapTextList.get("000222"):"");
                cMap.put("mapTextList000226", mapTextList.containsKey("000226")? mapTextList.get("000226"):"");
                cMap.put("mapTextList000255", mapTextList.containsKey("000255")? mapTextList.get("000255"):"");
                cMap.put("mapTextList000231", mapTextList.containsKey("000231")? mapTextList.get("000231"):"");
                cMap.put("mapTextList000006", mapTextList.containsKey("000006")? mapTextList.get("000006"):"");
                cMap.put("mapTextList000012", mapTextList.containsKey("000012")? mapTextList.get("000012"):"");
                cMap.put("mapTextList000218", mapTextList.containsKey("000218")? mapTextList.get("000218"):"");
                cMap.put("mapTextList000018", mapTextList.containsKey("000018")? mapTextList.get("000018"):"");
                cMap.put("mapTextList000118", mapTextList.containsKey("000118")? mapTextList.get("000118"):"");
                cMap.put("mapTextList000024", mapTextList.containsKey("000024")? mapTextList.get("000024"):"");
                cMap.put("mapTextList000196", mapTextList.containsKey("000196")? mapTextList.get("000196"):"");
                cMap.put("mapTextList000200", mapTextList.containsKey("000200")? mapTextList.get("000200"):"");
                cMap.put("mapTextList000204", mapTextList.containsKey("000204")? mapTextList.get("000204"):"");
                cMap.put("mapTextList000208", mapTextList.containsKey("000208")? mapTextList.get("000208"):"");
                cMap.put("mapTextList000213", mapTextList.containsKey("000213")? mapTextList.get("000213"):"");
                cMap.put("mapTextList000217", mapTextList.containsKey("000217")? mapTextList.get("000217"):"");
                cMap.put("mapTextList000032", mapTextList.containsKey("000032")? mapTextList.get("000032"):"");
                cMap.put("mapTextList000037", mapTextList.containsKey("000037")? mapTextList.get("000037"):"");
                cMap.put("mapTextList000043", mapTextList.containsKey("000043")? mapTextList.get("000043"):"");
                cMap.put("mapTextList000049", mapTextList.containsKey("000049")? mapTextList.get("000049"):"");
                cMap.put("mapTextList000221", mapTextList.containsKey("000221")? mapTextList.get("000221"):"");
                cMap.put("mapTextList000225", mapTextList.containsKey("000225")? mapTextList.get("000225"):"");
                cMap.put("mapTextList000179", mapTextList.containsKey("000179")? mapTextList.get("000179"):"");
                cMap.put("mapTextList000182", mapTextList.containsKey("000182")? mapTextList.get("000182"):"");
                cMap.put("mapTextList000233", mapTextList.containsKey("000233")? mapTextList.get("000233"):"");
                cMap.put("mapTextList000013", mapTextList.containsKey("000013")? mapTextList.get("000013"):"");
                cMap.put("mapTextList000134_lib", mapTextList.containsKey("000134")? mapTextList.get("000134").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000013_lib", mapTextList.containsKey("000013")? mapTextList.get("000013").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000219", mapTextList.containsKey("000219")? mapTextList.get("000219"):"");
                cMap.put("mapTextList000219_lib", mapTextList.containsKey("000219")? mapTextList.get("000219").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000019", mapTextList.containsKey("000019")? mapTextList.get("000019"):"");
                cMap.put("mapTextList000119", mapTextList.containsKey("000119")? mapTextList.get("000119"):"");
                cMap.put("mapTextList000025", mapTextList.containsKey("000025")? mapTextList.get("000025"):"");
                cMap.put("mapTextList000197", mapTextList.containsKey("000197")? mapTextList.get("000197"):"");
                cMap.put("mapTextList000201", mapTextList.containsKey("000201")? mapTextList.get("000201"):"");
                cMap.put("mapTextList000205", mapTextList.containsKey("000205")? mapTextList.get("000205"):"");
                cMap.put("mapTextList000209", mapTextList.containsKey("000209")? mapTextList.get("000209"):"");
                cMap.put("mapTextList000039", mapTextList.containsKey("000039")? mapTextList.get("000039"):"");
                cMap.put("mapTextList000045", mapTextList.containsKey("000045")? mapTextList.get("000045"):"");
                cMap.put("mapTextList000051", mapTextList.containsKey("000051")? mapTextList.get("000051"):"");
                cMap.put("mapTextList000223", mapTextList.containsKey("000223")? mapTextList.get("000223"):"");
                cMap.put("mapTextList000227", mapTextList.containsKey("000227")? mapTextList.get("000227"):"");
                cMap.put("mapTextList000235", mapTextList.containsKey("000235")? mapTextList.get("000235"):"");
                cMap.put("mapTextList000237", mapTextList.containsKey("000237")? mapTextList.get("000237"):"");
                cMap.put("mapTextList000239", mapTextList.containsKey("000239")? mapTextList.get("000239"):"");
                cMap.put("mapTextList000241", mapTextList.containsKey("000241")? mapTextList.get("000241"):"");
                cMap.put("mapTextList000243", mapTextList.containsKey("000243")? mapTextList.get("000243"):"");
                cMap.put("mapTextList000245", mapTextList.containsKey("000245")? mapTextList.get("000245"):"");
                cMap.put("mapTextList000247", mapTextList.containsKey("000247")? mapTextList.get("000247"):"");
                cMap.put("mapTextList000249", mapTextList.containsKey("000249")? mapTextList.get("000249"):"");
                cMap.put("mapTextList000251", mapTextList.containsKey("000251")? mapTextList.get("000251"):"");
                cMap.put("mapTextList000254", mapTextList.containsKey("000254")? mapTextList.get("000254"):"");
                cMap.put("mapTextList000230", mapTextList.containsKey("000230")? mapTextList.get("000230"):"");
                cMap.put("mapTextList000232", mapTextList.containsKey("000232")? mapTextList.get("000232"):"");
                cMap.put("mapTextList000234", mapTextList.containsKey("000234")? mapTextList.get("000234"):"");
                cMap.put("mapTextList000236", mapTextList.containsKey("000236")? mapTextList.get("000236"):"");
                cMap.put("mapTextList000238", mapTextList.containsKey("000238")? mapTextList.get("000238"):"");
                cMap.put("mapTextList000240", mapTextList.containsKey("000240")? mapTextList.get("000240"):"");
                cMap.put("mapTextList000242", mapTextList.containsKey("000242")? mapTextList.get("000242"):"");
                cMap.put("mapTextList000244", mapTextList.containsKey("000244")? mapTextList.get("000244"):"");
                cMap.put("mapTextList000246", mapTextList.containsKey("000246")? mapTextList.get("000246"):"");
                cMap.put("mapTextList000248", mapTextList.containsKey("000248")? mapTextList.get("000248"):"");
                cMap.put("mapTextList000250", mapTextList.containsKey("000250")? mapTextList.get("000250"):"");
                cMap.put("mapTextList000212", mapTextList.containsKey("000212")? mapTextList.get("000212"):"");
                cMap.put("mapTextList000216", mapTextList.containsKey("000216")? mapTextList.get("000216"):"");
                cMap.put("mapTextList000031", mapTextList.containsKey("000031")? mapTextList.get("000031"):"");
                cMap.put("mapTextList000036", mapTextList.containsKey("000036")? mapTextList.get("000036"):"");
                cMap.put("mapTextList000042", mapTextList.containsKey("000042")? mapTextList.get("000042"):"");
                cMap.put("mapTextList000048", mapTextList.containsKey("000048")? mapTextList.get("000048"):"");
                cMap.put("mapTextList000220", mapTextList.containsKey("000220")? mapTextList.get("000220"):"");
                cMap.put("mapTextList000224", mapTextList.containsKey("000224")? mapTextList.get("000224"):"");
                cMap.put("mapTextList000176", mapTextList.containsKey("000176")? mapTextList.get("000176"):"");
                cMap.put("mapTextList000178", mapTextList.containsKey("000178")? mapTextList.get("000178"):"");
                cMap.put("mapTextList000181", mapTextList.containsKey("000181")? mapTextList.get("000181"):"");
                cMap.put("mapTextList000441", mapTextList.containsKey("000441")? mapTextList.get("000441"):"");
                cMap.put("mapTextList000005", mapTextList.containsKey("000005")? mapTextList.get("000005"):"");
                cMap.put("mapTextListlib_000423", mapTextList.containsKey("lib_000423")? mapTextList.get("lib_000423"):"");
                cMap.put("mapTextListlib_000423_lib", mapTextList.containsKey("lib_000423")? mapTextList.get("lib_000423"):"");
                cMap.put("mapTextListlib_000345", mapTextList.containsKey("lib_000345")? mapTextList.get("lib_000345"):"");
                cMap.put("mapTextListlib_00031802", mapTextList.containsKey("lib_00031802")? mapTextList.get("lib_00031802"):"");
                cMap.put("mapTextListlib_000178", mapTextList.containsKey("lib_000178")? mapTextList.get("lib_000178"):"");
                cMap.put("mapTextList000175", mapTextList.containsKey("000175")? mapTextList.get("000175"):"");
                cMap.put("mapTextList000273", mapTextList.containsKey("000273")? mapTextList.get("000273"):"");
                cMap.put("mapTextList000392", mapTextList.containsKey("000392")? mapTextList.get("000392"):"");
                cMap.put("mapTextList000423", mapTextList.containsKey("000423")? mapTextList.get("000423"):"");
                cMap.put("mapTextList000345", mapTextList.containsKey("000345")? mapTextList.get("000345"):"");
                cMap.put("mapTextList00031802", mapTextList.containsKey("00031802")? mapTextList.get("00031802"):"");
                cMap.put("mapTextList000178", mapTextList.containsKey("000178")? mapTextList.get("000178"):"");
                cMap.put("mapTextList000341", mapTextList.containsKey("000341")? mapTextList.get("000341"):"");
                cMap.put("mapTextList000343", mapTextList.containsKey("000343")? mapTextList.get("000343"):"");
                cMap.put("mapTextList000342", mapTextList.containsKey("000342")? mapTextList.get("000342"):"");
                cMap.put("mapTextList000346", mapTextList.containsKey("000346")? mapTextList.get("000346"):"");
                cMap.put("mapTextList000348", mapTextList.containsKey("000348")? mapTextList.get("000348"):"");
                cMap.put("mapTextList000347", mapTextList.containsKey("000347")? mapTextList.get("000347"):"");
                cMap.put("mapTextList000365", mapTextList.containsKey("000365")? mapTextList.get("000365"):"");
                cMap.put("mapTextList000393", mapTextList.containsKey("000393")? mapTextList.get("000393"):"");
                cMap.put("mapTextList00032001", mapTextList.containsKey("00032001")? mapTextList.get("00032001"):"");
                cMap.put("mapTextList000395", mapTextList.containsKey("000395")? mapTextList.get("000395"):"");
                cMap.put("mapTextList000396", mapTextList.containsKey("000396")? mapTextList.get("000396"):"");
                cMap.put("mapTextList000372", mapTextList.containsKey("000372")? mapTextList.get("000372"):"");
                cMap.put("mapTextList000376", mapTextList.containsKey("000376")? mapTextList.get("000376"):"");
                cMap.put("mapTextList000192", mapTextList.containsKey("000192")? mapTextList.get("000192"):"");
                cMap.put("mapTextList000337", mapTextList.containsKey("000337")? mapTextList.get("000337"):"");
                cMap.put("mapTextList000262", mapTextList.containsKey("000262")? mapTextList.get("000262"):"");
                cMap.put("mapTextList000264", mapTextList.containsKey("000264")? mapTextList.get("000264"):"");
                cMap.put("mapTextList000260", mapTextList.containsKey("000260")? mapTextList.get("000260"):"");
                cMap.put("mapTextListlib_000209", mapTextList.containsKey("lib_000209")? mapTextList.get("lib_000209"):"");
                cMap.put("mapTextListlib_000032", mapTextList.containsKey("lib_000032")? mapTextList.get("lib_000032"):"");
                cMap.put("mapTextListlib_000181", mapTextList.containsKey("lib_000181")? mapTextList.get("lib_000181"):"");
                cMap.put("mapTextList000178_lib", mapTextList.containsKey("000178_lib")? mapTextList.get("000178_lib").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000176_lib", mapTextList.containsKey("000176_lib")? mapTextList.get("000176_lib").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("Table421", mapTalbe2.containsKey("table421")? mapTalbe2.get("table421"):"");
                cMap.put("Table431", mapTalbe2.containsKey("table431")? mapTalbe2.get("table431"):"");
                cMap.put("Table441", mapTalbe2.containsKey("table441")? mapTalbe2.get("table441"):"");
                cMap.put("Table451", mapTalbe2.containsKey("table451")? mapTalbe2.get("table451"):"");
                cMap.put("mapTextList000253_lib", mapTextList.containsKey("000253")? mapTextList.get("000253").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000229_lib", mapTextList.containsKey("000229")? mapTextList.get("000229").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000002_lib", mapTextList.containsKey("000002")? mapTextList.get("000002").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000009_lib", mapTextList.containsKey("000009")? mapTextList.get("000009").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000054_lib", mapTextList.containsKey("000054")? mapTextList.get("000054").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000016_lib", mapTextList.containsKey("000016")? mapTextList.get("000016").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000120_lib", mapTextList.containsKey("000120")? mapTextList.get("000120").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000022_lib", mapTextList.containsKey("000022")? mapTextList.get("000022").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000195_lib", mapTextList.containsKey("000195")? mapTextList.get("000195").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000199_lib", mapTextList.containsKey("000199")? mapTextList.get("000199").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000203_lib", mapTextList.containsKey("000203")? mapTextList.get("000203").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000207_lib", mapTextList.containsKey("000207")? mapTextList.get("000207").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000211_lib", mapTextList.containsKey("000211")? mapTextList.get("000211").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000215_lib", mapTextList.containsKey("000215")? mapTextList.get("000215").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000028_lib", mapTextList.containsKey("000028")? mapTextList.get("000028").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000035_lib", mapTextList.containsKey("000035")? mapTextList.get("000035").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000041_lib", mapTextList.containsKey("000041")? mapTextList.get("000041").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000047_lib", mapTextList.containsKey("000047")? mapTextList.get("000047").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000132_lib", mapTextList.containsKey("000132")? mapTextList.get("000132").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000255_lib", mapTextList.containsKey("000255")? mapTextList.get("000255").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000231_lib", mapTextList.containsKey("000231")? mapTextList.get("000231").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000006_lib", mapTextList.containsKey("000006")? mapTextList.get("000006").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000019_lib", mapTextList.containsKey("000019")? mapTextList.get("000019").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000119_lib", mapTextList.containsKey("000119")? mapTextList.get("000119").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000025_lib", mapTextList.containsKey("000025")? mapTextList.get("000025").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000197_lib", mapTextList.containsKey("000197")? mapTextList.get("000197").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000201_lib", mapTextList.containsKey("000201")? mapTextList.get("000201").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000205_lib", mapTextList.containsKey("000205")? mapTextList.get("000205").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000209_lib", mapTextList.containsKey("000209")? mapTextList.get("000209").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000213_lib", mapTextList.containsKey("000213")? mapTextList.get("000213").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000217_lib", mapTextList.containsKey("000217")? mapTextList.get("000217").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000032_lib", mapTextList.containsKey("000032")? mapTextList.get("000032").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000038_lib", mapTextList.containsKey("000038")? mapTextList.get("000038").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000044_lib", mapTextList.containsKey("000044")? mapTextList.get("000044").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000050_lib", mapTextList.containsKey("000050")? mapTextList.get("000050").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000222_lib", mapTextList.containsKey("000222")? mapTextList.get("000222").toString().replace("下降","-").replace("增加","").replace("增长",""):"");
                cMap.put("mapTextList000226_lib", mapTextList.containsKey("000226")? mapTextList.get("000226").toString().replace("下降","-").replace("增加","").replace("增长",""):"");

                cMap.put("Table21", mapTalbe2.containsKey("table21")? mapTalbe2.get("table21"):"");
                cMap.put("Table31", mapTalbe2.containsKey("table31")? mapTalbe2.get("table31"):"");
                cMap.put("Table41", mapTalbe2.containsKey("table41")? mapTalbe2.get("table41"):"");
                cMap.put("Table51", mapTalbe2.containsKey("table51")? mapTalbe2.get("table51"):"");
                cMap.put("Table61", mapTalbe2.containsKey("table61")? mapTalbe2.get("table61"):"");
                cMap.put("Table71", mapTalbe2.containsKey("table71")? mapTalbe2.get("table71"):"");
                cMap.put("Table81", mapTalbe2.containsKey("table81")? mapTalbe2.get("table81"):"");
                cMap.put("Table91", mapTalbe2.containsKey("table91")? mapTalbe2.get("table91"):"");
                cMap.put("Table101", mapTalbe2.containsKey("table101")? mapTalbe2.get("table101"):"");
                cMap.put("Table111", mapTalbe2.containsKey("table111")? mapTalbe2.get("table111"):"");
                cMap.put("Table121", mapTalbe2.containsKey("table121")? mapTalbe2.get("table121"):"");
                cMap.put("Table131", mapTalbe2.containsKey("table131")? mapTalbe2.get("table131"):"");
                cMap.put("Table141", mapTalbe2.containsKey("table141")? mapTalbe2.get("table141"):"");
                cMap.put("Table151", mapTalbe2.containsKey("table151")? mapTalbe2.get("table151"):"");
                cMap.put("Table161", mapTalbe2.containsKey("table161")? mapTalbe2.get("table161"):"");
                cMap.put("Table171", mapTalbe2.containsKey("table171")? mapTalbe2.get("table171"):"");
                cMap.put("Table181", mapTalbe2.containsKey("table181")? mapTalbe2.get("table181"):"");
                cMap.put("Table191", mapTalbe2.containsKey("table191")? mapTalbe2.get("table191"):"");
                cMap.put("Table201", mapTalbe2.containsKey("table201")? mapTalbe2.get("table201"):"");
                cMap.put("Table211", mapTalbe2.containsKey("table211")? mapTalbe2.get("table211"):"");
                cMap.put("Table221", mapTalbe2.containsKey("table221")? mapTalbe2.get("table221"):"");
                cMap.put("Table231", mapTalbe2.containsKey("table231")? mapTalbe2.get("table231"):"");
                cMap.put("Table241", mapTalbe2.containsKey("table241")? mapTalbe2.get("table241"):"");
                cMap.put("Table461", mapTalbe2.containsKey("table461")? mapTalbe2.get("table461"):"");
                cMap.put("Table251", mapTalbe2.containsKey("table251")? mapTalbe2.get("table251"):"");
                cMap.put("Table261", mapTalbe2.containsKey("table261")? mapTalbe2.get("table261"):"");
                cMap.put("Table271", mapTalbe2.containsKey("table271")? mapTalbe2.get("table271"):"");
                cMap.put("Table281", mapTalbe2.containsKey("table281")? mapTalbe2.get("table281"):"");
                cMap.put("Table291", mapTalbe2.containsKey("table291")? mapTalbe2.get("table291"):"");
                cMap.put("Table301", mapTalbe2.containsKey("table301")? mapTalbe2.get("table301"):"");
                cMap.put("Table311", mapTalbe2.containsKey("table311")? mapTalbe2.get("table311"):"");
                cMap.put("Table321", mapTalbe2.containsKey("table321")? mapTalbe2.get("table321"):"");
                cMap.put("Table331", mapTalbe2.containsKey("table331")? mapTalbe2.get("table331"):"");
                cMap.put("Table341", mapTalbe2.containsKey("table341")? mapTalbe2.get("table341"):"");
                cMap.put("Table351", mapTalbe2.containsKey("table351")? mapTalbe2.get("table351"):"");
                cMap.put("Table471", mapTalbe2.containsKey("table471")? mapTalbe2.get("table471"):"");
                cMap.put("Table361", mapTalbe2.containsKey("table361")? mapTalbe2.get("table361"):"");
                cMap.put("Table371", mapTalbe2.containsKey("table371")? mapTalbe2.get("table371"):"");
                cMap.put("Table381", mapTalbe2.containsKey("table381")? mapTalbe2.get("table381"):"");
                cMap.put("Table391", mapTalbe2.containsKey("table391")? mapTalbe2.get("table391"):"");
                cMap.put("Table401", mapTalbe2.containsKey("table401")? mapTalbe2.get("table401"):"");
                cMap.put("Table411", mapTalbe2.containsKey("table411")? mapTalbe2.get("table411"):"");
                cMap.put("Table422", mapTalbe2.containsKey("table422")? mapTalbe2.get("table422"):"");
                cMap.put("Table432", mapTalbe2.containsKey("table432")? mapTalbe2.get("table432"):"");
                cMap.put("Table442", mapTalbe2.containsKey("table442")? mapTalbe2.get("table442"):"");
                cMap.put("Table452", mapTalbe2.containsKey("table452")? mapTalbe2.get("table452"):"");

                cMap.put("Table22", mapTalbe2.containsKey("table22")? mapTalbe2.get("table22"):"");
                cMap.put("Table32", mapTalbe2.containsKey("table32")? mapTalbe2.get("table32"):"");
                cMap.put("Table42", mapTalbe2.containsKey("table42")? mapTalbe2.get("table42"):"");
                cMap.put("Table52", mapTalbe2.containsKey("table52")? mapTalbe2.get("table52"):"");
                cMap.put("Table62", mapTalbe2.containsKey("table62")? mapTalbe2.get("table62"):"");
                cMap.put("Table72", mapTalbe2.containsKey("table72")? mapTalbe2.get("table72"):"");
                cMap.put("Table82", mapTalbe2.containsKey("table82")? mapTalbe2.get("table82"):"");
                cMap.put("Table92", mapTalbe2.containsKey("table92")? mapTalbe2.get("table92"):"");
                cMap.put("Table102", mapTalbe2.containsKey("table102")? mapTalbe2.get("table102"):"");
                cMap.put("Table112", mapTalbe2.containsKey("table112")? mapTalbe2.get("table112"):"");
                cMap.put("Table122", mapTalbe2.containsKey("table122")? mapTalbe2.get("table122"):"");
                cMap.put("Table132", mapTalbe2.containsKey("table132")? mapTalbe2.get("table132"):"");
                cMap.put("Table142", mapTalbe2.containsKey("table142")? mapTalbe2.get("table142"):"");
                cMap.put("Table152", mapTalbe2.containsKey("table152")? mapTalbe2.get("table152"):"");
                cMap.put("Table162", mapTalbe2.containsKey("table162")? mapTalbe2.get("table162"):"");
                cMap.put("Table172", mapTalbe2.containsKey("table172")? mapTalbe2.get("table172"):"");
                cMap.put("Table182", mapTalbe2.containsKey("table182")? mapTalbe2.get("table182"):"");
                cMap.put("Table192", mapTalbe2.containsKey("table192")? mapTalbe2.get("table192"):"");
                cMap.put("Table202", mapTalbe2.containsKey("table202")? mapTalbe2.get("table202"):"");
                cMap.put("Table212", mapTalbe2.containsKey("table212")? mapTalbe2.get("table212"):"");
                cMap.put("Table222", mapTalbe2.containsKey("table222")? mapTalbe2.get("table222"):"");
                cMap.put("Table232", mapTalbe2.containsKey("table232")? mapTalbe2.get("table232"):"");
                cMap.put("Table242", mapTalbe2.containsKey("table242")? mapTalbe2.get("table242"):"");
                cMap.put("Table462", mapTalbe2.containsKey("table462")? mapTalbe2.get("table462"):"");
                cMap.put("Table252", mapTalbe2.containsKey("table252")? mapTalbe2.get("table252"):"");
                cMap.put("Table262", mapTalbe2.containsKey("table262")? mapTalbe2.get("table262"):"");
                cMap.put("Table272", mapTalbe2.containsKey("table272")? mapTalbe2.get("table272"):"");
                cMap.put("Table282", mapTalbe2.containsKey("table282")? mapTalbe2.get("table282"):"");
                cMap.put("Table292", mapTalbe2.containsKey("table292")? mapTalbe2.get("table292"):"");
                cMap.put("Table302", mapTalbe2.containsKey("table302")? mapTalbe2.get("table302"):"");
                cMap.put("Table312", mapTalbe2.containsKey("table312")? mapTalbe2.get("table312"):"");
                cMap.put("Table322", mapTalbe2.containsKey("table322")? mapTalbe2.get("table322"):"");
                cMap.put("Table332", mapTalbe2.containsKey("table332")? mapTalbe2.get("table332"):"");
                cMap.put("Table342", mapTalbe2.containsKey("table342")? mapTalbe2.get("table342"):"");
                cMap.put("Table352", mapTalbe2.containsKey("table352")? mapTalbe2.get("table352"):"");
                cMap.put("Table472", mapTalbe2.containsKey("table472")? mapTalbe2.get("table472"):"");
                cMap.put("Table362", mapTalbe2.containsKey("table362")? mapTalbe2.get("table362"):"");
                cMap.put("Table372", mapTalbe2.containsKey("table372")? mapTalbe2.get("table372"):"");
                cMap.put("Table382", mapTalbe2.containsKey("table382")? mapTalbe2.get("table382"):"");
                cMap.put("Table392", mapTalbe2.containsKey("table392")? mapTalbe2.get("table392"):"");
                cMap.put("Table402", mapTalbe2.containsKey("table402")? mapTalbe2.get("table402"):"");
                cMap.put("Table412", mapTalbe2.containsKey("table412")? mapTalbe2.get("table412"):"");
                cMap.put("Table423", mapTalbe2.containsKey("table423")? mapTalbe2.get("table423"):"");
                cMap.put("Table433", mapTalbe2.containsKey("table433")? mapTalbe2.get("table433"):"");
                cMap.put("Table443", mapTalbe2.containsKey("table443")? mapTalbe2.get("table443"):"");
                cMap.put("Table453", mapTalbe2.containsKey("table453")? mapTalbe2.get("table453"):"");

                cMap.put("Table23", mapTalbe2.containsKey("table23")? mapTalbe2.get("table23"):"");
                cMap.put("Table33", mapTalbe2.containsKey("table33")? mapTalbe2.get("table33"):"");
                cMap.put("Table43", mapTalbe2.containsKey("table43")? mapTalbe2.get("table43"):"");
                cMap.put("Table53", mapTalbe2.containsKey("table53")? mapTalbe2.get("table53"):"");
                cMap.put("Table63", mapTalbe2.containsKey("table63")? mapTalbe2.get("table63"):"");
                cMap.put("Table73", mapTalbe2.containsKey("table73")? mapTalbe2.get("table73"):"");
                cMap.put("Table83", mapTalbe2.containsKey("table83")? mapTalbe2.get("table83"):"");
                cMap.put("Table93", mapTalbe2.containsKey("table93")? mapTalbe2.get("table93"):"");
                cMap.put("Table103", mapTalbe2.containsKey("table103")? mapTalbe2.get("table103"):"");
                cMap.put("Table113", mapTalbe2.containsKey("table113")? mapTalbe2.get("table113"):"");
                cMap.put("Table123", mapTalbe2.containsKey("table123")? mapTalbe2.get("table123"):"");
                cMap.put("Table133", mapTalbe2.containsKey("table133")? mapTalbe2.get("table133"):"");
                cMap.put("Table143", mapTalbe2.containsKey("table143")? mapTalbe2.get("table143"):"");
                cMap.put("Table153", mapTalbe2.containsKey("table153")? mapTalbe2.get("table153"):"");
                cMap.put("Table163", mapTalbe2.containsKey("table163")? mapTalbe2.get("table163"):"");
                cMap.put("Table173", mapTalbe2.containsKey("table173")? mapTalbe2.get("table173"):"");
                cMap.put("Table183", mapTalbe2.containsKey("table183")? mapTalbe2.get("table183"):"");
                cMap.put("Table193", mapTalbe2.containsKey("table193")? mapTalbe2.get("table193"):"");
                cMap.put("Table203", mapTalbe2.containsKey("table203")? mapTalbe2.get("table203"):"");
                cMap.put("Table213", mapTalbe2.containsKey("table213")? mapTalbe2.get("table213"):"");
                cMap.put("Table223", mapTalbe2.containsKey("table223")? mapTalbe2.get("table223"):"");
                cMap.put("Table233", mapTalbe2.containsKey("table233")? mapTalbe2.get("table233"):"");
                cMap.put("Table243", mapTalbe2.containsKey("table243")? mapTalbe2.get("table243"):"");
                cMap.put("Table463", mapTalbe2.containsKey("table463")? mapTalbe2.get("table463"):"");
                cMap.put("Table253", mapTalbe2.containsKey("table253")? mapTalbe2.get("table253"):"");
                cMap.put("Table263", mapTalbe2.containsKey("table263")? mapTalbe2.get("table263"):"");
                cMap.put("Table273", mapTalbe2.containsKey("table273")? mapTalbe2.get("table273"):"");
                cMap.put("Table283", mapTalbe2.containsKey("table283")? mapTalbe2.get("table283"):"");
                cMap.put("Table293", mapTalbe2.containsKey("table293")? mapTalbe2.get("table293"):"");
                cMap.put("Table303", mapTalbe2.containsKey("table303")? mapTalbe2.get("table303"):"");
                cMap.put("Table313", mapTalbe2.containsKey("table313")? mapTalbe2.get("table313"):"");
                cMap.put("Table323", mapTalbe2.containsKey("table323")? mapTalbe2.get("table323"):"");
                cMap.put("Table333", mapTalbe2.containsKey("table333")? mapTalbe2.get("table333"):"");
                cMap.put("Table343", mapTalbe2.containsKey("table343")? mapTalbe2.get("table343"):"");
                cMap.put("Table353", mapTalbe2.containsKey("table353")? mapTalbe2.get("table353"):"");
                cMap.put("Table473", mapTalbe2.containsKey("table473")? mapTalbe2.get("table473"):"");
                cMap.put("Table363", mapTalbe2.containsKey("table363")? mapTalbe2.get("table363"):"");
                cMap.put("Table373", mapTalbe2.containsKey("table373")? mapTalbe2.get("table373"):"");
                cMap.put("Table383", mapTalbe2.containsKey("table383")? mapTalbe2.get("table383"):"");
                cMap.put("Table393", mapTalbe2.containsKey("table393")? mapTalbe2.get("table393"):"");
                cMap.put("Table403", mapTalbe2.containsKey("table403")? mapTalbe2.get("table403"):"");
                cMap.put("Table413", mapTalbe2.containsKey("table413")? mapTalbe2.get("table413"):"");
                cMap.put("Table424", mapTalbe2.containsKey("table424")? mapTalbe2.get("table424"):"");
                cMap.put("Table434", mapTalbe2.containsKey("table434")? mapTalbe2.get("table434"):"");
                cMap.put("Table444", mapTalbe2.containsKey("table444")? mapTalbe2.get("table444"):"");
                cMap.put("Table454", mapTalbe2.containsKey("table454")? mapTalbe2.get("table454"):"");
                cMap.put("aTable13", mapTalbe2.containsKey("table3")? mapTalbe2.get("table3"):"");
                cMap.put("aTable12", mapTalbe2.containsKey("table2")? mapTalbe2.get("table2"):"");
                cMap.put("aTable11", mapTalbe2.containsKey("table1")? mapTalbe2.get("table1"):"");
                cMap.put("aTable14", mapTalbe2.containsKey("table4")? mapTalbe2.get("table4"):"");
                cMap.put("Table24", mapTalbe2.containsKey("table24")? mapTalbe2.get("table24"):"");
                cMap.put("Table34", mapTalbe2.containsKey("table34")? mapTalbe2.get("table34"):"");
                cMap.put("Table44", mapTalbe2.containsKey("table44")? mapTalbe2.get("table44"):"");
                cMap.put("Table54", mapTalbe2.containsKey("table54")? mapTalbe2.get("table54"):"");
                cMap.put("Table64", mapTalbe2.containsKey("table64")? mapTalbe2.get("table64"):"");
                cMap.put("Table74", mapTalbe2.containsKey("table74")? mapTalbe2.get("table74"):"");
                cMap.put("Table84", mapTalbe2.containsKey("table84")? mapTalbe2.get("table84"):"");
                cMap.put("Table94", mapTalbe2.containsKey("table94")? mapTalbe2.get("table94"):"");
                cMap.put("Table104", mapTalbe2.containsKey("table104")? mapTalbe2.get("table104"):"");
                cMap.put("Table114", mapTalbe2.containsKey("table114")? mapTalbe2.get("table114"):"");
                cMap.put("Table124", mapTalbe2.containsKey("table124")? mapTalbe2.get("table124"):"");
                cMap.put("Table134", mapTalbe2.containsKey("table134")? mapTalbe2.get("table134"):"");
                cMap.put("Table144", mapTalbe2.containsKey("table144")? mapTalbe2.get("table144"):"");
                cMap.put("Table154", mapTalbe2.containsKey("table154")? mapTalbe2.get("table154"):"");
                cMap.put("Table164", mapTalbe2.containsKey("table164")? mapTalbe2.get("table164"):"");
                cMap.put("Table174", mapTalbe2.containsKey("table174")? mapTalbe2.get("table174"):"");
                cMap.put("Table184", mapTalbe2.containsKey("table184")? mapTalbe2.get("table184"):"");
                cMap.put("Table194", mapTalbe2.containsKey("table194")? mapTalbe2.get("table194"):"");
                cMap.put("Table204", mapTalbe2.containsKey("table204")? mapTalbe2.get("table204"):"");
                cMap.put("Table214", mapTalbe2.containsKey("table214")? mapTalbe2.get("table214"):"");
                cMap.put("Table224", mapTalbe2.containsKey("table224")? mapTalbe2.get("table224"):"");
                cMap.put("Table234", mapTalbe2.containsKey("table234")? mapTalbe2.get("table234"):"");
                cMap.put("Table244", mapTalbe2.containsKey("table244")? mapTalbe2.get("table244"):"");
                cMap.put("Table464", mapTalbe2.containsKey("table464")? mapTalbe2.get("table464"):"");
                cMap.put("Table254", mapTalbe2.containsKey("table254")? mapTalbe2.get("table254"):"");
                cMap.put("Table264", mapTalbe2.containsKey("table264")? mapTalbe2.get("table264"):"");
                cMap.put("Table274", mapTalbe2.containsKey("table274")? mapTalbe2.get("table274"):"");
                cMap.put("Table284", mapTalbe2.containsKey("table284")? mapTalbe2.get("table284"):"");
                cMap.put("Table294", mapTalbe2.containsKey("table294")? mapTalbe2.get("table294"):"");
                cMap.put("Table304", mapTalbe2.containsKey("table304")? mapTalbe2.get("table304"):"");
                cMap.put("Table314", mapTalbe2.containsKey("table314")? mapTalbe2.get("table314"):"");
                cMap.put("Table324", mapTalbe2.containsKey("table324")? mapTalbe2.get("table324"):"");
                cMap.put("Table334", mapTalbe2.containsKey("table334")? mapTalbe2.get("table334"):"");
                cMap.put("Table344", mapTalbe2.containsKey("table344")? mapTalbe2.get("table344"):"");
                cMap.put("Table354", mapTalbe2.containsKey("table354")? mapTalbe2.get("table354"):"");
                cMap.put("Table474", mapTalbe2.containsKey("table474")? mapTalbe2.get("table474"):"");
                cMap.put("Table364", mapTalbe2.containsKey("table364")? mapTalbe2.get("table364"):"");
                cMap.put("Table374", mapTalbe2.containsKey("table374")? mapTalbe2.get("table374"):"");
                cMap.put("Table384", mapTalbe2.containsKey("table384")? mapTalbe2.get("table384"):"");
                cMap.put("Table394", mapTalbe2.containsKey("table394")? mapTalbe2.get("table394"):"");
                cMap.put("Table404", mapTalbe2.containsKey("table404")? mapTalbe2.get("table404"):"");
                cMap.put("Table414", mapTalbe2.containsKey("table414")? mapTalbe2.get("table414"):"");
                cMap.put("tableParams", tableParamsList);
                cMap.put("tableParams3", tableParamsList3);
                cMap.put("AMT_UNIT_NAME", htmlReportData.get("AMT_UNIT_NAME").toString());
            }else{
                cMap.put("tableParams", "");
                cMap.put("tableParams3", "");
                cMap.put("tableParams2", "");
                cMap.put("textList", "");
                cMap.put("AMT_UNIT_NAME", "");
                cMap.put("echartsData", "");
            }

            //按日期设定当前Excel表格的名字
            String newFileName = htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"季度重庆市国库运行简况.docx";
           String jidu=htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2") ? "上半年":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1")?"1季度":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"1-3季度":"全年";
            String    title =  htmlReport.get("YEAR").toString()+"年"+jidu;
//            String srcFilePath=saveDir +"qut.xml";
            String targetFilePath= saveDir + "guoku2/"+newFileName;
//            String fileFullName = filePath + fileName;
            StringBuilder sb1=new StringBuilder();
            StringBuilder sb2=new StringBuilder();
            StringBuilder sb3=new StringBuilder();
            StringBuilder sb4=new StringBuilder();
            StringBuilder sb5=new StringBuilder();
            sb1.append(htmlReport.get("echart1_1").toString().replace("data:image/png;base64,",""));
            sb1.append(htmlReport.get("echart1_2").toString());
            sb1.append(htmlReport.get("echart1_3").toString());
            sb2.append(htmlReport.get("echart2_1").toString().replace("data:image/png;base64,",""));
            sb2.append(htmlReport.get("echart2_2").toString().replace("data:image/png;base64,",""));
            sb2.append(htmlReport.get("echart2_3").toString().replace("data:image/png;base64,",""));
            sb3.append(htmlReport.get("echart3_1").toString().replace("data:image/png;base64,",""));
            sb3.append(htmlReport.get("echart3_2").toString().replace("data:image/png;base64,",""));
            sb3.append(htmlReport.get("echart3_3").toString().replace("data:image/png;base64,",""));
            sb4.append(htmlReport.get("echart4_1").toString().replace("data:image/png;base64,",""));
            sb4.append(htmlReport.get("echart4_2").toString().replace("data:image/png;base64,",""));
            sb4.append(htmlReport.get("echart4_3").toString().replace("data:image/png;base64,",""));
            cMap.put("monthTitle", title);
            cMap.put("YEAR", htmlReport.get("YEAR").toString());
            cMap.put("month", htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1") ? "3":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2")?"6":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"9":"12" );
            cMap.put("qtr", htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("2") ? "上半年":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("1")?"1季度":htmlReport.get("ACCOUNT_PERIOD").toString().substring(5,6).equals("3")?"1-3季度":"全年" );
            cMap.put("monthTitle", title);
            cMap.put("ONE", sb1.toString());
            cMap.put("TWO", sb2.toString());
            cMap.put("THREE", sb3.toString());
            cMap.put("FOUR", sb4.toString());
//            String fileFullName = filePath + fileName;
            FileWriter out = new FileWriter(targetFilePath);
            new FreeMarker_Util().renderTpl(cMap, "qut.xml", out);
            response.setHeader("content-type", "text/plain");
            response.setHeader("content-type", "application/x-msdownload;");
            response.setContentType("text/plain; charset=utf-8");
//            String srcFilePath=saveDir +"qut.xml";
//            String targetFilePath= saveDir + "guoku2/"+newFileName;
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




//        } catch (Exception e) {
//            e.printStackTrace();
//        }



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
}
