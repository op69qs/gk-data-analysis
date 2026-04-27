package org.fixedReport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.fixedReport.BaseController;
import org.fixedReport.service.NewsFlashService;
import org.fixedReport.service.ReportService;
import org.fixedReport.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fixedReport.controller.ReportController.getStringToMap;

/**
 * 国库二期月度报告
 *
 * @author Created by dj on 2020/05/11.
 */
@Slf4j
@RestController
@Api(tags = "国库二期快报")
@RequestMapping(value = "/newsFlash", produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsFlashController extends BaseController {

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
    private NewsFlashService newsFlashService;
    @Autowired
    private ReportService reportService;


    /***
     * 获取所有报告列表
     */
    @RequestMapping(value = "/getReportAll", method = RequestMethod.POST)
    @ApiOperation("获取检所有报表列表")
    public Map<String, Object> getReportAll(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = newsFlashService.getReportAll(pd);
            Integer count = newsFlashService.countReportAll(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }
    /**
     * 新增生成的报告信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增生成的报告信息")
    @PostMapping(value = "/addEntityReport")
    public Map<String, Object> addEntityReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String REPORT_ID=UuidUtil.get32UUID();
        try{
            pd.put("REPORT_ID",REPORT_ID);
            boolean nameIsExist=newsFlashService.getNameIsExist(pd);
            if(nameIsExist){
                if("0".equals(pd.getString("isNotes"))){
                    pd.put("notes","[]");
                    pd.put("notesNum","[]");
                    pd.put("notesDesc","[]");
                }
                newsFlashService.addEntityReport(pd);
                res.put("result", "success");
                res.put("msg", "已新增");
                res.put("REPORT_ID", REPORT_ID);
            }else{
                res.put("result", "fail");
                res.put("msg", "报告名已存在，请重新输入！");
            }

        }catch (Exception e){
            res.put("result", "fail");
            res.put("msg", "新增失败");
        }
        return res;
    }
    /**
     * 删除生成的报告信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除生成的报告信息")
    @PostMapping(value = "/delEntityReport")
    public Map<String, Object> delEntityReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        try{
            newsFlashService.delEntityReport(pd);
            newsFlashService.delEntityReportFact(pd);
            res.put("result", "success");
            res.put("msg", "已删除");
        }catch (Exception e){
            res.put("result", "fail");
            res.put("msg", "删除失败");
        }
        return res;
    }
    /**
     * 修改生成的报告信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改生成的报告信息")
    @PostMapping(value = "/editEntityReport")
    public Map<String, Object> editEntityReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        try{
            pd.put("HTML_REPORT",pd.getString("HTML_REPORT").replace("\\","\\"));
            pd.put("echart1_1",pd.getString("chart1_1"));
            pd.put("echart1_2",pd.getString("chart1_2"));
            pd.put("echart1_3",pd.getString("chart1_3"));
            pd.put("echart2_1",pd.getString("chart2_1"));
            pd.put("echart2_2",pd.getString("chart2_2"));
            pd.put("echart2_3",pd.getString("chart2_3"));
            pd.put("echart3_1",pd.getString("chart3_1"));
            pd.put("echart3_2",pd.getString("chart3_2"));
            pd.put("echart3_3",pd.getString("chart3_3"));
            pd.put("echart4_1",pd.getString("chart4_1"));
            pd.put("echart4_2",pd.getString("chart4_2"));
            pd.put("echart4_3",pd.getString("chart4_3"));
//            pd.put("echart3",pd.getString("chart3").replace("\\","\\"));
//            pd.put("echart4",pd.getString("chart4").replace("\\","\\"));
            newsFlashService.editEntityReport(pd);
            res.put("result", "success");
            res.put("msg", "已修改");
        }catch (Exception e){
            e.printStackTrace();
            res.put("result", "fail");
            res.put("msg", "修改失败");
        }
        return res;
    }

    /**
     * 封装月度快报  3个表 四个图 五段话
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "封装月度快报")
    @PostMapping(value = "/addNewsFlashReport")
    public Map<String, Object> addMonthlyReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            List<Map<String, Object>> textList = newsFlashService.getTextAndParams(pd);
            List<Map<String, Object>> tableParamsList = newsFlashService.getTableParams(pd);
            List<Map<String, Object>> tableParamsList2 = newsFlashService.getTableParams2(pd);
            List<Map<String, Object>> tableParamsList3 = newsFlashService.getTableParams3(pd);
            if (textList.isEmpty()) {
                res.put("result", "fail");
                res.put("rows", "当前账期缺少快报文字数据，无法生成上半部分文字内容");
                return res;
            }
            if(textList.size()>0){
                rows.put("textList", textList.toString());
            }else {
                rows.put("textList", "");
            }
            if(tableParamsList.size()>0){
                rows.put("tableParams", tableParamsList.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
            }else {
                rows.put("tableParams", "");
            }
            if(tableParamsList2.size()>0){
                rows.put("tableParams2", tableParamsList2.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
            }else {
                rows.put("tableParams2", "");
            }
            if(tableParamsList3.size()>0){
                rows.put("tableParams3", tableParamsList3);
            }else {
                rows.put("tableParams3", "");
            }

//            rows.put("tableParams", tableParamsList.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
//            rows.put("tableParams2", tableParamsList2.toString().replace("[","").replace("]","").replace("{","").replace("}","").replace(" ",""));
//            rows.put("tableParams3", tableParamsList3);
            rows.put("REPORT_ID", pd.getString("REPORT_ID"));
            rows.put("AMT_UNIT", pd.getString("AMT_UNIT"));
            rows.put("AMT_UNIT_NAME", pd.getString("AMT_UNIT_NAME"));
            rows.put("ACCOUNT_PERIOD", pd.getString("ACCOUNT_PERIOD"));
            rows.put("REPORT_TYPE_ID", pd.getString("REPORT_TYPE_ID"));
            newsFlashService.insertFact(rows);
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
    @ApiOperation(value = "获取结构化月度快报")
    @PostMapping(value = "/getMonthlyReport")
    public Map<String, Object> getMonthlyReport(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> rows = new HashMap<>();
        Map mapTextList=new HashMap();
        Map mapTalbe2=new HashMap();
        Map mapTalbe=new HashMap();
        try {
            PageData pd = this.getPageData(param);
            Map<String, Object> allDataList = reportService.getMonthlyReport(pd);
            if(allDataList.size()>0) {
                if (!allDataList.get("textList").toString().isEmpty()) {
                    mapTextList = getStringToMap(allDataList.get("textList").toString().replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(" ", ""));
                } else {
                    List<Map<String, Object>> liveTextList = newsFlashService.getTextAndParams(pd);
                    if (!liveTextList.isEmpty()) {
                        mapTextList = liveTextList.get(0);
                    }
                }
                JSONObject jsonObj = new JSONObject(mapTextList);
                if (!allDataList.get("tableParams2").toString().isEmpty()) {
                    mapTalbe2 = getStringToMap(allDataList.get("tableParams2").toString());
                }
                JSONObject jsonObjTable2 = new JSONObject(mapTalbe2);
                List<Map<String, Object>> tableParamsList3 = newsFlashService.getTableParams3(pd);
                if (!allDataList.get("tableParams").toString().isEmpty()) {
                    mapTalbe = getStringToMap(allDataList.get("tableParams").toString());
                }
                JSONObject jsonObj1 = new JSONObject(mapTalbe);
                rows.put("tableParams", jsonObj1);
                rows.put("tableParams3", tableParamsList3);
                rows.put("tableParams2", jsonObjTable2);
                rows.put("textList", jsonObj);
                rows.put("AMT_UNIT_NAME", allDataList.get("AMT_UNIT_NAME").toString());
            }else{
                rows.put("tableParams", "");
                rows.put("tableParams3", "");
                rows.put("tableParams2", "");
                rows.put("textList", "");
                rows.put("AMT_UNIT_NAME", "");
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
    @ApiOperation(value = "下载月度快报")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @RequestBody(required = false
            ) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        BufferedInputStream bis = null;
        try{
            Map<String, Object> htmlReport = reportService.getHtmlReport(pd);
            //word内容
            String content=htmlReport.get("HTML_REPORT").toString().replace("\\","\\");
//            if("0".equals(pd.getString("state"))){
//                content=content.replace("font-size:14px;text-align:center;","font-size:3px;text-align:center;");
//            }
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
            String foot="";
            String    title =  htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"月";
            String    area =  htmlReport.get("DIM_DESC").toString();
//            int count = StringUtils.countMatches(content,"<td ");
            StringBuilder sb = new StringBuilder(content);
            if("1".equals(htmlReport.get("ISNOTES"))){
//            替换的固定字符注释
                String replaceName="";
//            replaceNameNum替换字符的长度;indexNum替换字符在整篇文档中第几次出现的位置;
                int replaceNameNum,indexNum;
//            详细注释说明
                String replaceNameDesc="";
                JSONArray jsonNotes= JSONArray.parseArray(htmlReport.get("NOTES").toString());
                JSONArray jsonNotesNum= JSONArray.parseArray(htmlReport.get("NOTESNUM").toString());
                JSONArray jsonNotesDesc= JSONArray.parseArray(htmlReport.get("NOTESDESC").toString());
                for(int i=0 ;i<jsonNotes.size();i++){
                    replaceName=jsonNotes.get(i).toString();
                    replaceNameNum=replaceName.length();
                    indexNum=Integer.parseInt(jsonNotesNum.get(i).toString());
                    replaceNameDesc=jsonNotesDesc.get(i).toString();
//                 replaceName="重庆市";
//                 replaceNameNum=replaceName.length();
//                 indexNum=3;
//                 replaceNameDesc="地方级国库收入包括一般公共预算收入、基金预算收入和国有资本经营预算收入，即地方政府自有财力，不包含地方政府债务收入和转移性收入。";
                    int cc=getCharacterPosition(sb.toString(),1,"<sup");
                    int dd=getCharacterPosition(sb.toString(),1,"</sup>");
                    sb.replace(cc, dd+6, "");
                    int ee=getCharacterPosition(sb.toString(),1,"<b");//1522
                    int ff=getCharacterPosition(sb.toString(),1,"</b>");//1916
                    sb.replace(ee, ff+4, "");
                    int bb=getCharacterPosition(content,indexNum,replaceName);
                    String str = "<span\n" +
                            "lang=EN-US style='font-size:21.3328px;font-family:FangSong;mso-fareast-theme-font:minor-fareast;\n" +
                            "mso-font-kerning:0pt'>"+replaceName+"<a style='mso-footnote-id:ftn1' href==\n" +
                            "\"#_ftn1\"\n" +
                            "name=\"_ftnref1\" title=\"\"><span class=MsoFootnoteReference><span\n" +
                            "style='mso-special-character:footnote'><![if !supportFootnotes]><span\n" +
                            "class=MsoFootnoteReference><span lang=EN-US style='font-size:12.0pt;f=\n" +
                            "ont-family:\n" +
                            "\"Times New Roman\",serif;mso-fareast-font-family:DengXian;mso-fareast-theme-=\n" +
                            "font:\n" +
                            "minor-fareast;mso-ansi-language:EN-US;mso-fareast-language:ZH-CN;mso-bidi-l=\n" +
                            "anguage:\n" +
                            "AR-SA'>[1]</span></span><![endif]></span></span></a><o:p></o:p></span>";
                    foot="<div style=3D'mso-element:footnote' id=3Dftn1> <p class=3DMsoFootnoteText><a style=3D'mso-footnote-id:ftn1' href=3D\"#_ftnr=\n" +
                            "ef1\"\n" +
                            "name=3D\"_ftn1\" title=3D\"\"><span class=3DMsoFootnoteReference><span lang=3DE=\n" +
                            "N-US><span\n" +
                            "style=3D'mso-special-character:footnote'><![if !supportFootnotes]><span\n" +
                            "class=3DMsoFootnoteReference><span lang=3DEN-US style=3D'font-size:9.0pt;fo=\n" +
                            "nt-family:\n" +
                            "\\FFFD\\FFFD\\FFFD\\FFFD;mso-fareast-font-family:SimSun;mso-bidi-font-family:\"T=\n" +
                            "imes New Roman\";\n" +
                            "mso-font-kerning:1.0pt;mso-ansi-language:EN-US;mso-fareast-language:ZH-CN;\n" +
                            "mso-bidi-language:AR-SA'>[1]</span></span><![endif]></span></span></span></=\n" +
                            "a><span\n" +
                            "lang=3DEN-US> "+replaceNameDesc+"</span></p> </div></div> <div class=3DWordSection1> <p class=3D><span lang=3DEN-US><o:p>&nbsp;</o:p></span></p></div>";
//                      "lang=3DEN-US> "+stringToUnicode(replaceNameDesc)+"</span></p> </div></div> <div class=3DWordSection1> <p class=3D><span lang=3DEN-US><o:p>&nbsp;</o:p></span></p></div>";
//              sb = new StringBuilder(content);
                    sb.replace(bb, bb+replaceNameNum, str);
                }
            }
            String aa=sb.toString();
            String filePath = saveDir+pd.getString("ADD_USERID")+ File.separator ;
//            String fileName = "月度快报.docx";

            //            2020年6月
            String fileName = htmlReport.get("YEAR").toString()+"年"+htmlReport.get("MONTH").toString()+"月重庆市国库运行简况.docx";
            String fileFullName = filePath +fileName;
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filePath);
            RichHtmlHandler handler = new RichHtmlHandler(sb.toString());
            handler.setDocSrcLocationPrex("file:///C:/EA1B1DF0");
            handler.setDocSrcParent("AI_ER_TEMP.files");
            handler.setNextPartId("01D61BE2.D6AD0370");
            handler.setShapeidPrex("图片_x0020_2");
            handler.setSpidPrex("_x0000_i1025");
            handler.setTypeid("#_x0000_t75");
//            handler.handledHtml(false,MAX_WIDTH_SIZE, MAX_HEIGHT_SIZE);
            handler.handledHtml(false,0,MAX_HEIGHT_SIZE,MAX_WIDTH_SIZE,QTR_MAX_HEIGHT_SIZE,QTR_MAX_WIDTH_SIZE);

            Map<String, Object> fileData = new HashMap<>();
            fileData.put("AIER_CONTENT", handler.getHandledDocBodyBlock());
            fileData.put("AIER_FOOT", foot);
            fileData.put("title", title);
            fileData.put("area", area+"国库运行快报");
            fileData.put("ISCOVER", pd.getString("isCover"));
            fileData.put("ISNOTES", htmlReport.get("ISNOTES"));
            fileData.put("imagesXmlHrefString", handler.getXmlImgRefs());
            fileData.put("imagesBase64String", handler.getDocBase64BlockResults());
            FileOutputStream out = new FileOutputStream(fileFullName);
            WordGeneratorWithFreemarker.createDoc(fileData, "AI_ER_TEMP.ftl", out);
            response.setHeader("content-type", "text/plain");
            response.setHeader("content-type", "application/x-msdownload;");
            response.setContentType("text/plain; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
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
