package org.fixedReport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.fixedReport.BaseController;
import org.fixedReport.model.TreeNodeArea;
import org.fixedReport.service.*;
import org.fixedReport.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 国库二期月度报告
 *
 * @author Created by dj on 2020/05/11.
 */
@Slf4j
@RestController
@Api(tags = "国库二期月度报告")
@RequestMapping(value = "/monthReport", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController extends BaseController {

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
    private ReportService reportService;

    /**
     * 打包下载月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载月度报告")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @RequestBody(required = false
            ) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        BufferedInputStream bis = null;
        try {
            Map<String, Object> htmlReport = reportService.getHtmlReport(pd);
            //word内容
            String content = htmlReport.get("HTML_REPORT").toString().replace("\\", "\\");
            if ("0".equals(pd.getString("state"))) {
                // state 0   windows 7
                content = content.replace("font-size:14px;text-align:center;", "font-size:3px;text-align:center;");
            }
            content = content.replace("font-size: 14px; text-align: center; font-family: SimSun;", "font-size:14px;font-family: 'FangSong';text-align:center;");
            content = content.replace("line-height: 38px;", "line-height: 39.9990px;font-size:21.3328px;font-family: 'FangSong';");
            content = content.replace("padding: 8px 0px;", "margin: 0.6665 0px;line-height: 39.9990px;font-family: 'FangSong';");
            content = content.replace("font-family: SimSun; margin: 0px;", "text-align: center;margin: 0px;line-height: 39.9990px;font-family: 'FangSong';font-size:29.3px");
            content = content.replace("font-family: SimSun;", "font-family: 'FangSong';");
            content = content.replace("text-indent: 2em;", "text-indent: 3.24324em;");
            content = content.replace("font-family: FangSong;", "font-family: 'FangSong_GB2312';");
            String filePath = saveDir + pd.getString("ADD_USERID") + File.separator;
//            2020年6月
            String fileName = htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"月重庆市国库资金运行报告.docx";
            String fileFullName = filePath + fileName;
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filePath);
            RichHtmlHandler handler = new RichHtmlHandler(content.toString());
            handler.setDocSrcLocationPrex("file:///C:/EA1B1DF0");
            handler.setDocSrcParent("AI_ER_TEMP.files");
            handler.setNextPartId("01D61BE2.D6AD0370");
            handler.setShapeidPrex("图片_x0020_2");
            handler.setSpidPrex("_x0000_i1025");
            handler.setTypeid("#_x0000_t75");
            handler.handledHtml(false, 2, MAX_HEIGHT_SIZE, MAX_WIDTH_SIZE, QTR_MAX_HEIGHT_SIZE, QTR_MAX_WIDTH_SIZE);
//            handler.handledHtml(false,MAX_WIDTH_SIZE, MAX_HEIGHT_SIZE);
            Map<String, Object> fileData = new HashMap<>();
            fileData.put("AIER_CONTENT", handler.getHandledDocBodyBlock());
            fileData.put("imagesXmlHrefString", handler.getXmlImgRefs());
            fileData.put("imagesBase64String", handler.getDocBase64BlockResults());
            fileData.put("ISCOVER", pd.getString("isCover"));
            FileOutputStream out = new FileOutputStream(fileFullName);
            WordGeneratorWithFreemarker.createDoc(fileData, "AI_ER_TEMP.ftl", out);
            response.setHeader("content-type", "text/plain");
            response.setHeader("content-type", "application/x-msdownload;");
            response.setContentType("text/plain; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
            byte[] buff = new byte[1024];
            OutputStream os = null;
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(fileFullName));
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
        System.out.println("export file finish");


    }


    /**
     * 获取结构化月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取结构化月度报告")
    @PostMapping(value = "/getMonthlyReport")
    public Map<String, Object> getMonthlyReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Map<String, Object> allDataList = reportService.getMonthlyReport(pd);
            List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> tableList2 = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> tableList3 = new ArrayList<Map<String, Object>>();
            if (allDataList.size() > 0) {
                Map mapTextList = getStringToMap(allDataList.get("textList").toString().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(" ", ""));
                JSONObject jsonObj = new JSONObject(mapTextList);
                Map mapNumList = getStringToMap(allDataList.get("numAll").toString().replace("}", ""));
                JSONObject jsonObjNum = new JSONObject(mapNumList);
//            Map mapAreaList=getStringToMap(allDataList.get("areaAll").toString());
//            JSONObject jsonObjArea=new JSONObject(mapAreaList);
                Map mapArea1 = new HashMap();
                String[] argArea = allDataList.get("areaAll").toString().replace(" ", "").split("}");
                mapArea1 = getStringToMap(argArea[0]);
                JSONObject jsonObjArea = new JSONObject(mapArea1);

                String echartsData = allDataList.get("echartsData").toString();
                List<Map<String, Object>> jsoin = (List<Map<String, Object>>) JSONObject.parse(echartsData);
                String mapTalbe = "";
                String mapTalbe2 = "";
                String mapTalbe3 = "";
                if (!allDataList.get("tableParams").toString().isEmpty()) {
                    mapTalbe = allDataList.get("tableParams").toString();
                    Map mapTable1 = new HashMap();
                    String[] arg = mapTalbe.split("}");
                    for (int i = 0; i < arg.length; i++) {
                        mapTable1 = getStringToMap(arg[i]);
                        JSONObject jsonObjT1 = new JSONObject(mapTable1);
                        tableList.add(jsonObjT1);
                    }
                }
                if (!allDataList.get("tableParams2").toString().isEmpty()) {
                    mapTalbe2 = allDataList.get("tableParams2").toString();
                    Map mapTable2 = new HashMap();
                    String[] arg2 = mapTalbe2.split("}");
                    for (int i = 0; i < arg2.length; i++) {
                        mapTable2 = getStringToMap(arg2[i]);
                        JSONObject jsonObjT2 = new JSONObject(mapTable2);
                        tableList2.add(jsonObjT2);
                    }
                }

                if (!allDataList.get("tableParams3").toString().isEmpty()) {
                    mapTalbe3 = allDataList.get("tableParams3").toString();
                    Map mapTable3 = new HashMap();
                    String[] arg3 = mapTalbe3.split("}");
                    for (int i = 0; i < arg3.length; i++) {
                        mapTable3 = getStringToMap(arg3[i]);
                        JSONObject jsonObjT3 = new JSONObject(mapTable3);
                        tableList3.add(jsonObjT3);
                    }
                }
                rows.put("areaAll", jsonObjArea);
                rows.put("numAll", jsonObjNum);
                rows.put("textList", jsonObj);
                rows.put("echartsData", jsoin);
                rows.put("AMT_UNIT_NAME", allDataList.get("AMT_UNIT_NAME").toString());
            } else {
                JSONObject object = new JSONObject();
                rows.put("areaAll", object);
                rows.put("numAll", object);
                rows.put("textList", object);
                rows.put("echartsData", object);
                rows.put("AMT_UNIT_NAME", "");
            }
            rows.put("tableParams", tableList);
            rows.put("tableParams2", tableList2);
            rows.put("tableParams3", tableList3);
            res.put("result", "success");
            res.put("rows", rows);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "fail");
            res.put("rows", e.getMessage());
        }
        return res;
    }

    /**
     * String转map
     *
     * @param str
     * @return
     */
    public static Map<String, Object> getStringToMap(String str) {
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String, Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split("=");
            if (str2.length > 1) {
                //str2[0]为KEY,str2[1]为值
                map.put(str2[0], str2[1].replace("|", ","));
            } else {
                //str2[0]为KEY,str2[1]为值

                map.put(str2[0], "0.00");
            }
        }
        return map;
    }

    /**
     * 获取结构化月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "查看html是否存在")
    @PostMapping(value = "/getHtmlReport")
    public Map<String, Object> getHtmlReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Map<String, Object> htmlReport = reportService.getHtmlReport(pd);
            rows.put("HTML_REPORT", htmlReport.get("HTML_REPORT").toString());
            res.put("result", "success");
            res.put("rows", rows);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "fail");
            res.put("rows", e.getMessage());
        }
        return res;
    }

    /**
     * 封装月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "封装月度报告")
    @PostMapping(value = "/addMonthlyReport")
    public Map<String, Object> addMonthlyReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> textList = reportService.getTextAndParams(pd);
            List<Map<String, Object>> tableParamsList = reportService.getTableParams(pd);
            List<Map<String, Object>> tableParamsList2 = reportService.getTableParams2(pd);
            List<Map<String, Object>> tableParamsList3 = reportService.getTableParams3(pd);
            List<Map<String, Object>> areaAll = reportService.getAreaParams(pd);
            List<Map<String, Object>> numAll = reportService.getNumParams(pd);
            List listMap = new ArrayList<>();
            List<Map<String, Object>> graphParamsList = reportService.getGraphParams(pd);
            List<Map<String, Object>> graphParamsList2 = reportService.getGraphParams2(pd);
            List<Map<String, Object>> graphParamsList3 = reportService.getGraphParams3(pd);
            List<Map<String, Object>> graphParamsList4 = reportService.getGraphParams4(pd);
            List<Map<String, Object>> graphParamsList5 = reportService.getGraphParams5(pd);
//            第一个图需要三个变量  data_dateAll  series1   series2
            Map map = new HashMap<String, Object>();
            String series = "";
            String data_dateAll = "";
            if (graphParamsList.size() > 0) {
                data_dateAll = graphParamsList.get(0).get("ACCOUNT_PERIOD").toString();
                map.put("data_dateAll", data_dateAll);
                for (int i = 0; i < graphParamsList.size(); i++) {
                    String id = graphParamsList.get(i).get("id").toString();
                    series = graphParamsList.get(i).get("VAL").toString();
                    map.put("MAX_VAL" + id, graphParamsList.get(i).get("MAX_VAL").toString());
                    map.put("MIN_VAL" + id, graphParamsList.get(i).get("MIN_VAL").toString());
                    map.put("series" + id, series);
                }
            } else {
                map.put("MAX_VAL1", "0");
                map.put("MIN_VAL1", "0");
                map.put("MAX_VAL2", "0");
                map.put("MIN_VAL2", "0");
                map.put("data_dateAll", data_dateAll);
                map.put("series1", series);
                map.put("series2", series);
            }
//
//            String  data_dateAll="";
//            String  strArray2="";
//            String  strArray3="";
//            if(graphParamsList.size()>0){
//                data_dateAll=graphParamsList.get(0).get("ACCOUNT_PERIOD").toString();
//                strArray2=graphParamsList.get(0).get("VAL").toString();
//                strArray3=graphParamsList.get(1).get("VAL").toString();
//            }
//            map.put("data_dateAll",data_dateAll);
//            map.put("series1",strArray2);
//            map.put("series2",strArray3);
            listMap.add(map);
//            第二个图需要三个变量  data_dateAll  series1   series2
            Map map2 = new HashMap<String, Object>();
            String series2 = "";
            String data_dateAll2 = "";
            if (graphParamsList2.size() > 0) {
                data_dateAll2 = graphParamsList2.get(0).get("ACCOUNT_PERIOD").toString();
                map2.put("data_dateAll", data_dateAll2);
                for (int i = 0; i < graphParamsList2.size(); i++) {
                    String id = graphParamsList2.get(i).get("id").toString();
                    series2 = graphParamsList2.get(i).get("VAL").toString();
                    map2.put("MAX_VAL" + id, graphParamsList2.get(i).get("MAX_VAL").toString());
                    map2.put("MIN_VAL" + id, graphParamsList2.get(i).get("MIN_VAL").toString());
                    map2.put("series" + id, series2);
                }
            } else {
                map2.put("MAX_VAL1", "0");
                map2.put("MIN_VAL1", "0");
                map2.put("MAX_VAL2", "0");
                map2.put("MIN_VAL2", "0");
                map2.put("data_dateAll", data_dateAll2);
                map2.put("series1", series2);
                map2.put("series2", series2);
            }
//
//
//            String  strArrayMap2="";
//            String  strArrayMap22="";
//            String  strArrayMap23="";
//            if(graphParamsList2.size()>0){
//                  strArrayMap2=graphParamsList2.get(0).get("ACCOUNT_PERIOD").toString();
//                  strArrayMap22=graphParamsList2.get(0).get("VAL").toString();
//                  strArrayMap23=graphParamsList2.get(1).get("VAL").toString();
//                map2.put("MAX_VAL",graphParamsList2.get(0).get("MAX_VAL").toString());
//                map2.put("MIN_VAL",graphParamsList2.get(0).get("MIN_VAL").toString());
//                map2.put("MAX_VAL2",graphParamsList2.get(1).get("MAX_VAL").toString());
//                map2.put("MIN_VAL2",graphParamsList2.get(1).get("MIN_VAL").toString());
//            }else{
//                map2.put("MAX_VAL","0");
//                map2.put("MIN_VAL","0");
//                map2.put("MAX_VAL2","0");
//                map2.put("MIN_VAL2","0");
//            }
//            map2.put("data_dateAll",strArrayMap2);
//            map2.put("series1",strArrayMap22);
//            map2.put("series2",strArrayMap23);
            listMap.add(map2);
            //            第三个图需要三个变量  series             name
            List<Map> listMap3 = new ArrayList<>();
            Map map38 = new HashMap<String, Object>();
            if (graphParamsList3.size() > 0) {
                Map map3 = new HashMap<String, Object>();
                Map map32 = new HashMap<String, Object>();
                Map map33 = new HashMap<String, Object>();
                Map map34 = new HashMap<String, Object>();
                Map map35 = new HashMap<String, Object>();
                Map map36 = new HashMap<String, Object>();
                Map map37 = new HashMap<String, Object>();
                map3.put("value", graphParamsList3.get(0).get("value").toString());
                map3.put("name", graphParamsList3.get(0).get("name").toString());
                map32.put("value", graphParamsList3.get(1).get("value").toString());
                map32.put("name", graphParamsList3.get(1).get("name").toString());
                map33.put("value", graphParamsList3.get(2).get("value").toString());
                map33.put("name", graphParamsList3.get(2).get("name").toString());
                map34.put("value", graphParamsList3.get(3).get("value").toString());
                map34.put("name", graphParamsList3.get(3).get("name").toString());
                map35.put("value", graphParamsList3.get(4).get("value").toString());
                map35.put("name", graphParamsList3.get(4).get("name").toString());
                map36.put("value", graphParamsList3.get(5).get("value").toString());
                map36.put("name", graphParamsList3.get(5).get("name").toString());
                map37.put("value", graphParamsList3.get(6).get("value").toString());
                map37.put("name", graphParamsList3.get(6).get("name").toString());
                listMap3.add(map3);
                listMap3.add(map32);
                listMap3.add(map33);
                listMap3.add(map34);
                listMap3.add(map35);
                listMap3.add(map36);
                listMap3.add(map37);

            }
            map38.put("series", listMap3);
            listMap.add(map38);
//            第四个图  data_dateAll   series1    series2  min   max
            Map map4 = new HashMap<String, Object>();
            String series4 = "";
            String data_dateAll4 = "";
            if (graphParamsList4.size() > 0) {
                data_dateAll4 = graphParamsList4.get(0).get("ACCOUNT_PERIOD").toString();
                map4.put("data_dateAll", data_dateAll4);
                for (int i = 0; i < graphParamsList4.size(); i++) {
                    String id = graphParamsList4.get(i).get("id").toString();
                    series4 = graphParamsList4.get(i).get("VAL").toString();
                    map4.put("MAX_VAL" + id, graphParamsList4.get(i).get("MAX_VAL").toString());
                    map4.put("MIN_VAL" + id, graphParamsList4.get(i).get("MIN_VAL").toString());
                    map4.put("series" + id, series4);
                }
            } else {
                map4.put("MAX_VAL1", "0");
                map4.put("MIN_VAL1", "0");
                map4.put("MAX_VAL2", "0");
                map4.put("MIN_VAL2", "0");
                map4.put("data_dateAll", data_dateAll4);
                map4.put("series1", series4);
                map4.put("series2", series4);
            }
            listMap.add(map4);
            //            第五个图  data_dateYear   data_dateAll    data_dateYear1  data_dateYear2
            //            data_dateYear3 data_dateYear4 data_dateYear5
            Map map5 = new HashMap<String, Object>();
            String strArrayMap5 = "";
            String strArrayMap51 = "";
            String strArrayMap52 = "";
            String strArrayMap53 = "";
            String strArrayMap54 = "";
            String strArrayMap55 = "";
            String strArrayMap56 = "";
            String strArrayMap57 = "";
            String strArrayMap58 = "";
            String strArrayMap59 = "";
            String strArrayMap510 = "";
            String strArrayMap511 = "";
            if (graphParamsList5.size() == 1) {
                strArrayMap5 = graphParamsList5.get(0).get("data_dateYear").toString();
                strArrayMap51 = graphParamsList5.get(0).get("data_dateAll").toString();
                strArrayMap52 = graphParamsList5.get(0).get("data_dateYear1").toString();
                strArrayMap57 = graphParamsList5.get(0).get("series").toString();
            } else if (graphParamsList5.size() == 2) {
                strArrayMap5 = graphParamsList5.get(0).get("data_dateYear").toString();
                strArrayMap51 = graphParamsList5.get(0).get("data_dateAll").toString();
                strArrayMap52 = graphParamsList5.get(0).get("data_dateYear1").toString();
                strArrayMap53 = graphParamsList5.get(1).get("data_dateYear2").toString();
                strArrayMap57 = graphParamsList5.get(0).get("series").toString();
                strArrayMap58 = graphParamsList5.get(1).get("series").toString();
            } else if (graphParamsList5.size() == 3) {
                strArrayMap5 = graphParamsList5.get(0).get("data_dateYear").toString();
                strArrayMap51 = graphParamsList5.get(0).get("data_dateAll").toString();
                strArrayMap52 = graphParamsList5.get(0).get("data_dateYear1").toString();
                strArrayMap53 = graphParamsList5.get(1).get("data_dateYear2").toString();
                strArrayMap54 = graphParamsList5.get(2).get("data_dateYear3").toString();
                strArrayMap57 = graphParamsList5.get(0).get("series").toString();
                strArrayMap58 = graphParamsList5.get(1).get("series").toString();
                strArrayMap59 = graphParamsList5.get(2).get("series").toString();
            } else if (graphParamsList5.size() == 4) {
                strArrayMap5 = graphParamsList5.get(0).get("data_dateYear").toString();
                strArrayMap51 = graphParamsList5.get(0).get("data_dateAll").toString();
                strArrayMap52 = graphParamsList5.get(0).get("data_dateYear1").toString();
                strArrayMap53 = graphParamsList5.get(1).get("data_dateYear2").toString();
                strArrayMap54 = graphParamsList5.get(2).get("data_dateYear3").toString();
                strArrayMap55 = graphParamsList5.get(3).get("data_dateYear4").toString();
//                strArrayMap56 = graphParamsList5.get(4).get("data_dateYear5").toString();
                strArrayMap57 = graphParamsList5.get(0).get("series").toString();
                strArrayMap58 = graphParamsList5.get(1).get("series").toString();
                strArrayMap59 = graphParamsList5.get(2).get("series").toString();
                strArrayMap510 = graphParamsList5.get(3).get("series").toString();
            } else if (graphParamsList5.size() == 5) {
                strArrayMap5 = graphParamsList5.get(0).get("data_dateYear").toString();
                strArrayMap51 = graphParamsList5.get(0).get("data_dateAll").toString();
                strArrayMap52 = graphParamsList5.get(0).get("data_dateYear1").toString();
                strArrayMap53 = graphParamsList5.get(1).get("data_dateYear2").toString();
                strArrayMap54 = graphParamsList5.get(2).get("data_dateYear3").toString();
                strArrayMap55 = graphParamsList5.get(3).get("data_dateYear4").toString();
                strArrayMap56 = graphParamsList5.get(4).get("data_dateYear5").toString();
                strArrayMap57 = graphParamsList5.get(0).get("series").toString();
                strArrayMap58 = graphParamsList5.get(1).get("series").toString();
                strArrayMap59 = graphParamsList5.get(2).get("series").toString();
                strArrayMap510 = graphParamsList5.get(3).get("series").toString();
                strArrayMap511 = graphParamsList5.get(4).get("series").toString();
            }
            map5.put("data_dateYear", strArrayMap5);
            map5.put("data_dateAll", strArrayMap51);
            map5.put("data_dateYear1", strArrayMap52);
            map5.put("data_dateYear2", strArrayMap53);
            map5.put("data_dateYear3", strArrayMap54);
            map5.put("data_dateYear4", strArrayMap55);
            map5.put("data_dateYear5", strArrayMap56);
            map5.put("series1", strArrayMap57);
            map5.put("series2", strArrayMap58);
            map5.put("series3", strArrayMap59);
            map5.put("series4", strArrayMap510);
            map5.put("series5", strArrayMap511);
            listMap.add(map5);
            rows.put("textList", textList.toString());
            if (tableParamsList.size() > 0) {
                rows.put("tableParams", tableParamsList.toString().replace("[", "").replace("]", "").replace("{", "").replace(" ", "").replace("},", "}"));
            } else {
                rows.put("tableParams", "");
            }
            if (tableParamsList2.size() > 0) {
                rows.put("tableParams2", tableParamsList2.toString().replace("[", "").replace("]", "").replace("{", "").replace(" ", "").replace("},", "}"));
            } else {
                rows.put("tableParams2", "");
            }
            if (tableParamsList3.size() > 0) {
                rows.put("tableParams3", tableParamsList3.toString().replace("[", "").replace("]", "").replace("{", "").replace(" ", "").replace("},", "}"));
            } else {
                rows.put("tableParams3", "");
            }
            String AA = areaAll.toString().replace("[", "").replace("]", "").replace("{", "").replace("},", "}");
            if (areaAll.size() > 0) {
                rows.put("areaAll", areaAll.toString().replace("[", "").replace("]", "").replace("{", "").replace("},", "}"));
            } else {
                rows.put("areaAll", "");
            }
            System.out.print("999999999999999999999-" + areaAll.toString());
            if (numAll.size() > 0) {
                rows.put("numAll", numAll.toString().replace("[", "").replace("]", "").replace("{", "").replace("},", "}").replace(" ", ""));
            } else {
                rows.put("numAll", "");
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
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "fail");
            res.put("rows", e.getMessage());
        }
        return res;
    }

    /**
     * 封装月度报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取地区信息")
    @PostMapping(value = "/areaReport")
    public Map<String, Object> areaReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> areaReport = reportService.getAreaReport(pd);
            TransUtilArea.getRootNode(areaReport);
            TreeNodeArea treeNodecomm = TransUtilArea.treeTrans(areaReport, null);
            List<TreeNodeArea> treeNodeList = new ArrayList<>();
            treeNodeList.add(treeNodecomm);
            res.put("result", "success");
            res.put("rows", treeNodeList);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "fail");
            res.put("rows", e.getMessage());
        }
        return res;
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
}
