package org.triber.analysis.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triber.analysis.service.AlarmStatisticsService;
import org.triber.analysis.util.BaseController;
import org.triber.analysis.util.CreateAnalysisSQL;
import org.triber.analysis.util.PageData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author .
 * @Ddate 2020/10/15 9:43
 * @Description 监测告警统计表
 */
@Slf4j
@RestController
@RequestMapping(value = "/alarmStatistics", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmStatisticsController extends BaseController {

    @Value("${DOWNLOAD_PATH}")
    private String filePath;

    @Autowired
    private CreateAnalysisSQL analysisSQL;

    @Autowired
    private AlarmStatisticsService alarmStatisticsService;

    //获取综合查询数据
    @PostMapping("/getAlarmStatisticsData")
    public Map<String, Object> getAlarmStatisticsData() {
        Map<String, Object> map = new HashMap<>();
        try {
            PageData pageData = this.getPageData();
            pageData.put("times", "BETWEEN '" + pageData.get("startTime") + "' AND '" + pageData.get("endTime") + "'");

            //查询分页数据
            Integer page = Integer.parseInt(pageData.getString("page"));//页码
            Integer rows = Integer.parseInt(pageData.getString("rows"));//行数
            pageData.put("page", (page - 1) * rows);
            pageData.put("limit", "LIMIT " + pageData.get("page") + "," + pageData.get("rows"));

            map.put("total", alarmStatisticsService.getAlarmStatisticsDataTotal(pageData));
            List<Map<String, Object>> dataList = alarmStatisticsService.getAlarmStatisticsData(pageData);

            map.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getTotalReportExcelOne")
    public Object getTotalReportExcelOne(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String newFileName = "monitorStatistics_data";
        String title = "监测告警统计表";
        try {
            PageData pageData = this.getPageData();
            pageData.put("times", "BETWEEN '" + pageData.get("startTime") + "' AND '" + pageData.get("endTime") + "'");
            List dataList = alarmStatisticsService.getAlarmStatisticsData(pageData);
            //按日期设定当前Excel表格的名字

            HSSFWorkbook workbook = new HSSFWorkbook();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = workbook.createSheet("Sheet1");
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);
            // 添加title
            row0.createCell(0).setCellValue(title);
            HSSFCell cell3 = row0.getCell(0);
//            title样式
            HSSFCellStyle boderStyleTitle = workbook.createCellStyle();
            //设置背景颜色
            boderStyleTitle.setFillForegroundColor(HSSFColor.TAN.index);
            //垂直居中
            boderStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            boderStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            //solid 填充  foreground  前景色
//            boderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cell3.setCellStyle(boderStyleTitle);
            //合并的单元格样式
            HSSFCellStyle boderStyle = workbook.createCellStyle();
            //垂直居中
            boderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            boderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            //设置一个边框
            boderStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);

            boderStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);

            boderStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);

            boderStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
//            表头
            HSSFRow row1 = sheet.createRow(1);
            if ("1".equals(pageData.getString("displayDevice"))) {
                row1.createCell(0).setCellValue("时间");
                row1.createCell(1).setCellValue("指标名称");
                row1.createCell(2).setCellValue("平台");
                row1.createCell(3).setCellValue("告警次数");
                row1.getCell(0).setCellStyle(boderStyle);
                row1.getCell(1).setCellStyle(boderStyle);
                row1.getCell(2).setCellStyle(boderStyle);
                row1.getCell(3).setCellStyle(boderStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
                //设置列宽
                sheet.setColumnWidth(0, 20 * 256);
                sheet.setColumnWidth(1, 20 * 256);
                sheet.setColumnWidth(2, 20 * 256);
                sheet.setColumnWidth(3, 20 * 256);
            } else {
                row1.createCell(0).setCellValue("时间");
                row1.createCell(1).setCellValue("指标名称");
                row1.createCell(2).setCellValue("平台");
                row1.createCell(3).setCellValue("资源");
                row1.createCell(4).setCellValue("告警次数");
                row1.getCell(0).setCellStyle(boderStyle);
                row1.getCell(1).setCellStyle(boderStyle);
                row1.getCell(2).setCellStyle(boderStyle);
                row1.getCell(3).setCellStyle(boderStyle);
                row1.getCell(4).setCellStyle(boderStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
                //设置列宽
                sheet.setColumnWidth(0, 20 * 256);
                sheet.setColumnWidth(1, 20 * 256);
                sheet.setColumnWidth(2, 20 * 256);
                sheet.setColumnWidth(3, 20 * 256);
                sheet.setColumnWidth(4, 40 * 256);
            }
            //格式化数字
            NumberFormat nf = NumberFormat.getInstance();
            // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
            // 在sheet里创建往下的行
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> db = JSONObject.parseObject(JSON.toJSONString(dataList.get(i)));
                HSSFRow row = sheet.createRow(i + 2);
                if ("1".equals(pageData.getString("displayDevice"))) {
                    row.createCell(0).setCellValue(db.get("alarm_time") != null ? db.get("alarm_time").toString() : "");
                    row.createCell(1).setCellValue(db.get("index_name") != null ? db.get("index_name").toString() : "");
                    row.createCell(2).setCellValue(db.get("platfrom_name") != null ? db.get("platfrom_name").toString() : "");
                    row.createCell(3).setCellValue(db.get("alarm_value") != null ? db.get("alarm_value").toString() : "");
                    row.getCell(0).setCellStyle(boderStyle);
                    row.getCell(1).setCellStyle(boderStyle);
                    row.getCell(2).setCellStyle(boderStyle);
                    row.getCell(3).setCellStyle(boderStyle);
                } else {
                    row.createCell(0).setCellValue(db.get("alarm_time") != null ? db.get("alarm_time").toString() : "");
                    row.createCell(1).setCellValue(db.get("index_name") != null ? db.get("index_name").toString() : "");
                    row.createCell(2).setCellValue(db.get("platfrom_name") != null ? db.get("platfrom_name").toString() : "");
                    row.createCell(3).setCellValue(db.get("resource_desc") != null ? db.get("resource_desc").toString() : "");
                    row.createCell(4).setCellValue(db.get("alarm_value") != null ? db.get("alarm_value").toString() : "");
                    row.getCell(0).setCellStyle(boderStyle);
                    row.getCell(1).setCellStyle(boderStyle);
                    row.getCell(2).setCellStyle(boderStyle);
                    row.getCell(3).setCellStyle(boderStyle);
                    row.getCell(4).setCellStyle(boderStyle);

                }
            }

            FileOutputStream output = new FileOutputStream(filePath + newFileName + ".xls");
            workbook.write(output);//写入磁盘
            output.close();

            //2.获取要下载的文件名
            //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
            File fi = new File(filePath + newFileName + ".xls");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(newFileName + ".xls", "UTF-8"));
            response.addHeader("Content-Length", "" + fi.length());
            response.setContentType("application/octet-stream");
            //4.获取要下载的文件输入流
            InputStream in = new FileInputStream(fi);
            int len = 0;
            //5.创建数据缓冲区
            byte[] buffer1 = new byte[1024];
            //6.通过response对象获取OutputStream流
            OutputStream out = response.getOutputStream();
            //7.将FileInputStream流写入到buffer缓冲区
            while ((len = in.read(buffer1)) > 0) {
                //8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer1, 0, len);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

}
