package org.fixedReport.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fixedReport.BaseController;
import org.fixedReport.service.ReportTndustryService;
import org.fixedReport.util.CreateExcel_2;
import org.fixedReport.util.FileDownload;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@Api(tags = "国库二期报表")
@RequestMapping(value = "/reportTndustry", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportTndustryController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;
    @Autowired
    private ReportTndustryService reportTndustryService;
    /**
     * 查询报表
     */
    @RequestMapping(value = {"/getTndustryTaxData"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("行业税收--分页")
    public Map<String, Object> getTndustryTaxData(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        String sqlCol  = "select  *    ";
        String orderBy  = "order by ";
        String groupBy  = "group by ";
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String,Object>> subjectAllCodes=reportTndustryService.getAllSubject();
//            返回数据和总条数
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Integer count = 0;
//            1为显示2为不显示
            if ("1".equals(pd.getString("DISPLAY_ENTERPRISE"))) {
                sqlCol = "select  D_ACCT,INDUSTRY_NAME , ENTERPRISE_NAME, ENTERPRISE_CODE  ";
                if (null != pd.getString("SUBJECTCODE") && !"".equals(pd.getString("SUBJECTCODE"))) {
                    String[] subjectCodes = pd.getString("SUBJECTCODE").split(",");
                    for (int i = 0; i < subjectCodes.length; i++) {
                        sqlCol += "  ,round(F_AMT_" + subjectCodes[i] + "/"+ Integer.parseInt(pd.getString("UNIT"))+",2) as "+"F_AMT_" + subjectCodes[i]+", round(F_AMT_" + subjectCodes[i] + "_YEAR*100,2) as "+"F_AMT_" + subjectCodes[i] + "_YEAR" ;
                    }
                }
                if (null != pd.getString("SORTCOL") && !"".equals(pd.getString("SORTCOL"))) {
                    orderBy = "order by project ";
                    String[] sortCols = pd.getString("SORTCOL").split(",");
                    for (int i = 0; i < sortCols.length; i++) {
                        orderBy += "  ," + sortCols[i];
                    }

                } else {
                    orderBy += " project ";
                }
                pd.put("sqlCol", sqlCol);
                pd.put("orderBy", orderBy);
                pd.put("groupBy", groupBy);
                result = reportTndustryService.getTndustryTaxData1(pd);
                count = reportTndustryService.countTndustryTaxData1(pd);

            }else{
                groupBy += " INDUSTRY_NAME";
                if (null != pd.getString("SUBJECTCODE") && !"".equals(pd.getString("SUBJECTCODE"))) {
                    sqlCol = "select  D_ACCT,INDUSTRY_NAME   ";
                    String[] subjectCodes = pd.getString("SUBJECTCODE").split(",");
                    for (int i = 0; i < subjectCodes.length; i++) {
                        sqlCol += ",round(sum(F_AMT_" + subjectCodes[i] + ")"  + "/"+ Integer.parseInt(pd.getString("UNIT"))+",2) as F_AMT_" + subjectCodes[i] + ", round((sum(F_AMT_" + subjectCodes[i] + ")-sum(F_AMT_" + subjectCodes[i] + "_YEAR))/ sum(F_AMT_" + subjectCodes[i] + "_YEAR) *100,2) as F_AMT_" + subjectCodes[i] + "_YEAR";

                    }
                }
                if (null != pd.getString("SORTCOL") && !"".equals(pd.getString("SORTCOL"))) {
                    orderBy = "order by project ";
                    String[] sortCols = pd.getString("SORTCOL").split(",");
                    for (int i = 0; i < sortCols.length; i++) {
                        orderBy += "  ," + sortCols[i];
                    }

                } else {
                    orderBy += " project ";
                }
                pd.put("sqlCol", sqlCol);
                pd.put("orderBy", orderBy);
                pd.put("groupBy", groupBy);
                result = reportTndustryService.getTndustryTaxData(pd);
                count = reportTndustryService.countTndustryTaxData(pd);
            }
//            pd.put("sqlCol", sqlCol);
//            pd.put("orderBy", orderBy);
//            pd.put("groupBy", groupBy);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getTndustryTaxDataAll"}, method = RequestMethod.POST)
    @ApiOperation("行业税收--不分页")
    public Object getTndustryTaxDataAll(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> jsonMap = new HashMap<>();
        String sqlCol  = "select  *    ";
        String orderBy  = "order by ";
        String groupBy  = "group by ";
        try {
            PageData pd = this.getPageData(param);
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String,Object>> subjectAllCodes=reportTndustryService.getAllSubject();
//            返回数据和总条数
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Integer count = 0;
//            1为显示2为不显示
            if ("1".equals(pd.getString("DISPLAY_ENTERPRISE"))) {
                sqlCol = "select  D_ACCT,INDUSTRY_NAME , ENTERPRISE_NAME, ENTERPRISE_CODE  ";
                if (null != pd.getString("SUBJECTCODE") && !"".equals(pd.getString("SUBJECTCODE"))) {
                    String[] subjectCodes = pd.getString("SUBJECTCODE").split(",");
                    for (int i = 0; i < subjectCodes.length; i++) {
                        sqlCol += "  ,round(F_AMT_" + subjectCodes[i] + "/"+ Integer.parseInt(pd.getString("UNIT"))+",2) as "+"F_AMT_" + subjectCodes[i]+", round(F_AMT_" + subjectCodes[i] + "_YEAR*100,2) as "+"F_AMT_" + subjectCodes[i] + "_YEAR" ;

                    }
                }
                if (null != pd.getString("SORTCOL") && !"".equals(pd.getString("SORTCOL"))) {
                    orderBy = "order by project ";
                    String[] sortCols = pd.getString("SORTCOL").split(",");
                    for (int i = 0; i < sortCols.length; i++) {
                        orderBy += "  ," + sortCols[i];
                    }

                } else {
                    orderBy += " project ";
                }
                pd.put("sqlCol", sqlCol);
                pd.put("orderBy", orderBy);
                pd.put("groupBy", groupBy);
                result = reportTndustryService.getTndustryTaxData1(pd);
                count = reportTndustryService.countTndustryTaxData1(pd);

            }else{
                groupBy += " INDUSTRY_NAME";
                if (null != pd.getString("SUBJECTCODE") && !"".equals(pd.getString("SUBJECTCODE"))) {
                    sqlCol = "select  D_ACCT,INDUSTRY_NAME   ";
                    String[] subjectCodes = pd.getString("SUBJECTCODE").split(",");
                    for (int i = 0; i < subjectCodes.length; i++) {
                        sqlCol += ",round(sum(F_AMT_" + subjectCodes[i] + ")"  + "/"+ Integer.parseInt(pd.getString("UNIT"))+",2) as F_AMT_" + subjectCodes[i] + ", round((sum(F_AMT_" + subjectCodes[i] + ")-sum(F_AMT_" + subjectCodes[i] + "_YEAR))/ sum(F_AMT_" + subjectCodes[i] + "_YEAR)*100,2) as F_AMT_" + subjectCodes[i] + "_YEAR";

                    }
                }
                if (null != pd.getString("SORTCOL") && !"".equals(pd.getString("SORTCOL"))) {
                    orderBy = "order by project ";
                    String[] sortCols = pd.getString("SORTCOL").split(",");
                    for (int i = 0; i < sortCols.length; i++) {
                        orderBy += "  ," + sortCols[i];
                    }

                } else {
                    orderBy += " project ";
                }
                pd.put("sqlCol", sqlCol);
                pd.put("orderBy", orderBy);
                pd.put("groupBy", groupBy);
                result = reportTndustryService.getTndustryTaxData(pd);
                count = reportTndustryService.countTndustryTaxData(pd);
            }
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "fail");
            jsonMap.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return jsonMap;
    }


    @RequestMapping(value = "/exportXls", method = RequestMethod.POST)
    @ApiOperation("报表导出公共方法")

    public void exportXls(
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws IOException {
        PageData pd = this.getPageData(param);
        String fileName = pd.getString("CENTER_REPORT") + ".xls";
        String filepath = saveDir;
        String sqlCol = "select  *    ";
        String orderBy = "order by ";
        String groupBy = "group by ";
        String tableCol = "";
        Map map =new HashMap();
        List<Map<String, Object>> data = null;
        try {
//            1为显示2为不显示
            if ("1".equals(pd.getString("DISPLAY_ENTERPRISE"))) {
                sqlCol = "select  D_ACCT,INDUSTRY_NAME , ENTERPRISE_NAME, ENTERPRISE_CODE  ";
                tableCol = "'D_ACCT','INDUSTRY_NAME', 'ENTERPRISE_NAME', 'ENTERPRISE_CODE'  ";
                if (null != pd.getString("SUBJECTCODE") && !"".equals(pd.getString("SUBJECTCODE"))) {
                    String[] subjectCodes = pd.getString("SUBJECTCODE").split(",");
                    for (int i = 0; i < subjectCodes.length; i++) {
                        sqlCol += "  ,round(F_AMT_" + subjectCodes[i] + "/"+ Integer.parseInt(pd.getString("UNIT"))+",2) as "+"F_AMT_" + subjectCodes[i]+", round(F_AMT_" + subjectCodes[i] + "_YEAR*100,2) as "+"F_AMT_" + subjectCodes[i] + "_YEAR" ;

                        tableCol += " ,'F_AMT_" + subjectCodes[i] + "', 'F_AMT_" + subjectCodes[i] + "_YEAR'";
                    }
                }
                if (null != pd.getString("SORTCOL") && !"".equals(pd.getString("SORTCOL"))) {
                    orderBy = "order by project ";
                    String[] sortCols = pd.getString("SORTCOL").split(",");
                    for (int i = 0; i < sortCols.length; i++) {
                        orderBy += "  ," + sortCols[i];
                    }

                } else {
                    orderBy += " project ";
                }
                pd.put("sqlCol", sqlCol);
                pd.put("orderBy", orderBy);
                pd.put("groupBy", groupBy);
                pd.put("tableCol", tableCol);
                data = reportTndustryService.getTndustryTaxData1All(pd);
            } else {
                groupBy += " INDUSTRY_NAME";
                if (null != pd.getString("SUBJECTCODE") && !"".equals(pd.getString("SUBJECTCODE"))) {
                    sqlCol = "select  D_ACCT,INDUSTRY_NAME   ";
                    tableCol = "'D_ACCT','INDUSTRY_NAME' ";
                    String[] subjectCodes = pd.getString("SUBJECTCODE").split(",");
                    for (int i = 0; i < subjectCodes.length; i++) {
                        sqlCol += ",round(sum(F_AMT_" + subjectCodes[i] + ")"  + "/"+ Integer.parseInt(pd.getString("UNIT"))+",2) as F_AMT_" + subjectCodes[i] + ", round((sum(F_AMT_" + subjectCodes[i] + ")-sum(F_AMT_" + subjectCodes[i] + "_YEAR))/ sum(F_AMT_" + subjectCodes[i] + "_YEAR)*100,2) as F_AMT_" + subjectCodes[i] + "_YEAR";
                        tableCol += ",'F_AMT_" + subjectCodes[i] + "', 'F_AMT_" + subjectCodes[i] + "_YEAR'";
                    }
                }
                if (null != pd.getString("SORTCOL") && !"".equals(pd.getString("SORTCOL"))) {
                    orderBy = "order by project ";
                    String[] sortCols = pd.getString("SORTCOL").split(",");
                    for (int i = 0; i < sortCols.length; i++) {
                        orderBy += "  ," + sortCols[i];
                    }

                } else {
                    orderBy += " project ";
                }
                pd.put("sqlCol", sqlCol);
                pd.put("orderBy", orderBy);
                pd.put("groupBy", groupBy);
                pd.put("tableCol", tableCol);
                data = reportTndustryService.getTndustryTaxDataAll(pd);
            }
            List<Map<String, Object>> titlelistAll = reportTndustryService.getColALL(pd);
            List<Map<String, Object>> titlelistColHeB = reportTndustryService.getColHeB(pd);
            List<Map<String, Object>> titlelistColHeBF = reportTndustryService.getColHeBF(pd);
            String[] queryCols = new String[titlelistAll.size()];
            String[] titlelist = new String[titlelistAll.size()];
            String[] titleColHeB = new String[titlelistColHeB.size()];
            String[] titleColHeBF = new String[titlelistColHeBF.size()];
            for (int i = 0; i < titlelistAll.size(); i++) {
                queryCols[i] = titlelistAll.get(i).get("CHECK_COL").toString();
                titlelist[i] = titlelistAll.get(i).get("CHECK_COL_NAME").toString();
            }
            for (int i = 0; i < titlelistColHeB.size(); i++) {
                titleColHeB[i] = titlelistColHeB.get(i).get("name").toString();
            }
            for (int i = 0; i < titlelistColHeBF.size(); i++) {
                titleColHeBF[i] = titlelistColHeBF.get(i).get("CHECK_COL_NAME").toString();
            }

            pd.put("columns", queryCols);

            map.put(new String("单位 ".getBytes("gbk"), "utf-8"),new String("万元".getBytes("gbk"), "utf-8"));
            String title = "税收收入分行业分税种统计月报总表";
            String aa=pd.getString("S_TRENAME");
             title  = aa+new String(title.getBytes("gbk"), "utf-8");
            int cellIndex = 0;
            int len_1 = titlelist.length;
            int title_index=len_1/2;
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            CellStyle cellStyle = wb.createCellStyle();
            CellStyle dataCellStyle = wb.createCellStyle();

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 12); //字体高度
            font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
            font.setFontName("黑体"); //字体
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
            font.setItalic(false); //是否使用斜体
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(font);
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式

            if(data.size()<=60000) {
                // 建立新的sheet对象（excel的表单）
                HSSFSheet sheet = wb.createSheet("sheet1");
                // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
                HSSFRow row0 = sheet.createRow(0);
                row0.setHeightInPoints(30);
                Cell cell = row0.createCell(title_index);
                cell.setCellValue(title);
                cell.setCellStyle(titleStyle);
                if(map.size()>0){
                    HSSFRow rowColSub = sheet.createRow(1);
                    int i=0;
                    Set<Map.Entry<String,String>> entrySet = map.entrySet();
                    for(Map.Entry<String,String> e:entrySet) {
                        String key = e.getKey();
                        String  value = e.getValue();
                        Cell cellSub = rowColSub.createCell(i);
                        cellSub.setCellValue(key+":"+value);
                        i++;
                        System.out.println(key+":"+value);
                    }
                }
                HSSFRow row2 = sheet.createRow(2);
                HSSFRow row3 = sheet.createRow(3);
                //            1为显示2为不显示
                if ("1".equals(pd.getString("DISPLAY_ENTERPRISE"))) {

                    // 添加表头
                    for (int i = 0; i < titleColHeB.length; i++) {
                        if(i>3){
                            Cell cellSub = row2.createCell((i*2)-3);
                            cellSub.setCellValue(titleColHeB[i]);
                            cellSub.setCellStyle(cellStyle);
                        }else{
                            Cell cellSub = row2.createCell(i);
                            cellSub.setCellValue(titleColHeB[i]);
                            cellSub.setCellStyle(cellStyle);
                        }

                    }
                    // 添加表头
                    for (int i = 0; i < titleColHeBF.length; i++) {
                        Cell cellSub = row3.createCell(i+3);
                        cellSub.setCellValue(titleColHeBF[i]);
                        cellSub.setCellStyle(cellStyle);
                    }
                    sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
                    int j=3;
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 6));
                    for (int i = 3; i < titleColHeB.length; i++) {
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, j, j+1));
                        j=j+2;
                    }

                }else {

                    // 添加表头
                    for (int i = 0; i < titleColHeB.length; i++) {
                        if(i>2){
                            Cell cellSub = row2.createCell((i*2)-2);
                            cellSub.setCellValue(titleColHeB[i]);
                            cellSub.setCellStyle(cellStyle);
                        }else{
                            Cell cellSub = row2.createCell(i);
                            cellSub.setCellValue(titleColHeB[i]);
                            cellSub.setCellStyle(cellStyle);
                        }

                    }
                    // 添加表头
                    for (int i = 0; i < titleColHeBF.length; i++) {
                        Cell cellSub = row3.createCell(i+2);
                        cellSub.setCellValue(titleColHeBF[i]);
                        cellSub.setCellStyle(cellStyle);
                    }
                    sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));

                    int j=2;
                    int z=3;
                    for (int i = 2; i < titleColHeB.length; i++) {
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, j, j+1));
                        j=j+2;
                    }
                }


                //添加表中内容
                for (int row = 0; row < data.size(); row++) {//数据行
                    //创建新行
                    HSSFRow newrow = sheet.createRow(row + 4);//数据从第三行开始
                    //获取该行的数据
                    @SuppressWarnings("unchecked")
                    Map<String, Object> data1 = data.get(row);
                    for (int col = 0; col < queryCols.length; col++) {//列
                        //数据从第一列开始
                        //创建单元格并放入数据
                        Cell dataCell = newrow.createCell(col);
                        dataCell.setCellValue(data1 != null && data1.get(queryCols[col]) != null ? String.valueOf(data1.get(queryCols[col])) : "");
                        dataCell.setCellStyle(setDataStyle(dataCellStyle));
                    }
                }
            }else{
                int c = data.size()%60000 == 0 ? (data.size()/60000) : (data.size()/60000)+1;
                for(int k=1;k<=c;k++){
                    System.out.println("开始第"+k+"个页签,当前页签从"+ ((k-1)*60000+1) +"到"+(k==c?data.size():k*60000));
                    // 建立新的sheet对象（excel的表单）
                    HSSFSheet sheet = wb.createSheet("第"+ ((k-1)*60000+1) +"条——第"+ (k==c?data.size():k*60000)+"条");
                    // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
                    HSSFRow row0 = sheet.createRow(0);
                    row0.setHeightInPoints(30);
                    Cell cell = row0.createCell(title_index);
                    cell.setCellValue(title);
                    cell.setCellStyle(titleStyle);

                    HSSFRow row1 = sheet.createRow(1);
                    // 添加表头
                    for (int i = 0; i < len_1; i++) {
                        Cell cellSub = row1.createCell(i);
                        cellSub.setCellValue(titlelist[i]);
                        cellSub.setCellStyle(cellStyle);
                    }

                    //添加表中内容
                    for (int row = (k-1)*60000; row < (k==c?data.size():k*60000) ; row++) {//数据行
                        //创建新行
                        HSSFRow newrow = sheet.createRow(row%60000 + 2);//数据从第三行开始
                        //获取该行的数据
                        @SuppressWarnings("unchecked")
                        Map<String, Object> data1 = data.get(row);
                        for (int col = 0; col < queryCols.length; col++) {//列
                            //数据从第一列开始
                            //创建单元格并放入数据
                            Cell dataCell = newrow.createCell(col);
                            dataCell.setCellValue(data1 != null && data1.get(queryCols[col]) != null ? String.valueOf(data1.get(queryCols[col])) : "");
                            dataCell.setCellStyle(setDataStyle(dataCellStyle));
                        }
                    }

                }
            }
            HSSFCellStyle setBorder = wb.createCellStyle();
            setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件1
            FileOutputStream output = new FileOutputStream(filepath + fileName);
            wb.write(output);//写入磁盘
            output.close();

//            CreateExcel_2.createInspectStatisticsTable(map,title, filepath, fileName, titlelist, queryCols,titleColHeB,titleColHeBF, data, data, 0, data.size());
            FileDownload.fileDownload(response, filepath + fileName, fileName, this.getRequest());
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
    /**
     * 表体数据样式
     *
     * @param dataStyle
     * @return
     */
    private static CellStyle setDataStyle(CellStyle dataStyle) {

        dataStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex()); // 前景色

        dataStyle.setWrapText(true);
        dataStyle.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
        dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        dataStyle.setBorderLeft(CellStyle.BORDER_THIN);  // 左边边框
        dataStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色

        dataStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        dataStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
        dataStyle.setBorderTop(CellStyle.BORDER_THIN); // 上边边框
        dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());  // 上边边框颜色

        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
        dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
        return dataStyle;
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
    @RequestMapping(value = "/getSubjectAll", method = RequestMethod.POST)
    @ApiOperation("获取科目接口")
    public Map<String, Object> getSubjectAll() {
        Map<String, Object> result = new HashMap<>();
        try {
//            List<Map<String, Object>> data = reportTndustryService.getSubjectAll(pd);
            List<Map<String,Object>> data=reportTndustryService.getAllSubject();
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }

        return result;
    }
}
