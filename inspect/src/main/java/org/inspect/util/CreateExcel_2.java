// CreateExcel_2.java

package org.inspect.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/11/7.
 */
public class CreateExcel_2 {

    /**
     * 生成Excel 并放到指定位置
     *
     * @param filepath  文件路径(要绝对路径)
     * @param filename  文件名称 (如: demo.xls  记得加.xls哦)
     * @param titlelist 表头
     * @param zdlist    字段list
     * @param datalist  数据list (这里也可以改成List<Map<String,String>>  格式的数据)
     * @return 是否正常生成
     * @throws IOException
     * @author: 2018年11月24日 上午11:40:39
     * (titlelist  和  zdlist  顺序要一直, 要一一对应)
     */
    public static boolean createInspectStatisticsTable(
            String title,
            String filepath,
            String filename,
            List<String> titlelist,
            List<String> titlelist_2,
            List<String> titlelist_3,
            String[] zdlist,
            List<Map<String, Object>> datalist,
            String taskType
    ) throws IOException {
        boolean success = false;
        int cellIndex = 0;
        int len_1 = titlelist.size();
        int len_2 = titlelist_2.size();
        int len_3 = titlelist_3.size();
        int totalLen = titlelist.size() + titlelist_3.size();
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            CellStyle cellStyle = wb.createCellStyle();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);
            row0.setHeightInPoints(30);
            Cell cell = row0.createCell(0);
            cell.setCellValue(title);

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 12); //字体高度
            font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
            font.setFontName("黑体"); //字体
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
            font.setItalic(false); //是否使用斜体

            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(font);
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
            cell.setCellStyle(titleStyle);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalLen - 1));

            HSSFRow row1 = sheet.createRow(1);
            HSSFRow row2 = sheet.createRow(2);
            switch(taskType){
                case "001": cellIndex = 5;  break;
                case "005": cellIndex = 7;  break;
                case "006": cellIndex = 1;  break;
            }

            // 添加表头
            for (int i = 0; i < len_1; i++) {
                Cell cellSub = row1.createCell(i);
                Cell cellSub_2 = row2.createCell(i);
                cellSub.setCellValue(titlelist.get(i));
                cellSub.setCellStyle(setCellStyle(cellStyle));
                cellSub_2.setCellStyle(setCellStyle(cellStyle));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
            }
            for (int j = 0; j < len_2; j++) {
                Cell cellSub = row1.createCell(j * 3 + cellIndex);
                Cell cellSub_1 = row1.createCell(j * 3 + (cellIndex + 1));
                Cell cellSub_2 = row1.createCell(j * 3 + (cellIndex + 2));
                cellSub.setCellValue(titlelist_2.get(j));
                cellSub.setCellStyle(setCellStyle(cellStyle));
                cellSub_1.setCellStyle(setCellStyle(cellStyle));
                cellSub_2.setCellStyle(setCellStyle(cellStyle));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, j * 3 + cellIndex, j * 3 + (cellIndex+2)));
            }
            // 添加表头
            for (int k = 0; k < len_3; k++) {
                Cell cellSub = row2.createCell(k + cellIndex);
                cellSub.setCellValue(titlelist_3.get(k));
                cellSub.setCellStyle(setCellStyle(cellStyle));
            }
            sheet.setColumnWidth(0, 256 * 20 + 184);
            for (int m = 1; m < totalLen; m++) {
                sheet.setColumnWidth(m, 256 * 15 + 184);
            }

            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            styleMap.put("cellStyle", setDataStyle(wb));

            //添加表中内容
            for (int row = 0; row < datalist.size(); row++) {//数据行
                //创建新行
                HSSFRow newrow = sheet.createRow(row + 3);//数据从第四行开始
                //获取该行的数据
                @SuppressWarnings("unchecked")
                Map<String, Object> data = datalist.get(row);
                for (int col = 0; col < zdlist.length; col++) {//列
                    //数据从第一列开始
                    //创建单元格并放入数据
                    Cell dataCell = newrow.createCell(col);
                    dataCell.setCellValue(data != null && data.get(zdlist[col]) != null ? String.valueOf(data.get(zdlist[col])) : "");
                    dataCell.setCellStyle(styleMap.get("cellStyle"));
                }
            }
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件1
            FileOutputStream output = new FileOutputStream(filepath + filename);
            wb.write(output);//写入磁盘
            output.close();
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
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


    /**
     * 生成Excel 并放到指定位置
     *
     * @param filepath 文件路径(要绝对路径)
     * @param filename 文件名称 (如: demo.xls  记得加.xls哦)
     * @param title    表头
     * @param zdlist   字段list
     * @param datalist 数据list (这里也可以改成List<Map<String,String>>  格式的数据)
     * @return 是否正常生成
     * @throws IOException
     * @author: 2018年11月24日 上午11:40:39
     * (titlelist  和  zdlist  顺序要一直, 要一一对应)
     */
    public static boolean createInspectSelfSumTable(
            String title, String filepath, String filename,
            String[] zdlist, List<Map<String, Object>> datalist) throws IOException {
        boolean success = false;
        int totalLen = 10;
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            CellStyle cellStyle = wb.createCellStyle();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet(title);
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);
            row0.setHeightInPoints(30);
            Cell cell = row0.createCell(0);
            cell.setCellValue(title);
            cell.setCellStyle(setTitleStyle(wb));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalLen - 1));

            HSSFRow row1 = sheet.createRow(1);
            HSSFRow row2 = sheet.createRow(2);
            HSSFRow row3 = sheet.createRow(3);
            HSSFRow row4 = sheet.createRow(4);
            // 添加表头
            Cell cell_2_1 = row2.createCell(0);
            Cell cell_2_1_1 = row2.createCell(1);
            Cell cell_2_1_2 = row2.createCell(2);
            Cell cell_2_1_3 = row3.createCell(0);
            Cell cell_2_1_4 = row3.createCell(1);
            Cell cell_2_1_5 = row3.createCell(2);
            Cell cell_2_1_6 = row4.createCell(0);
            Cell cell_2_1_7 = row4.createCell(1);
            Cell cell_2_1_8 = row4.createCell(2);
            cell_2_1.setCellValue("问题类别");
            cell_2_1.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_1.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_2.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_3.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_4.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_5.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_6.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_7.setCellStyle(setCellStyle(cellStyle));
            cell_2_1_8.setCellStyle(setCellStyle(cellStyle));


            Cell cell_2_2 = row2.createCell(3);
            Cell cell_2_2_1 = row3.createCell(3);
            Cell cell_2_2_2 = row4.createCell(3);
            cell_2_2.setCellValue("具体情况");
            cell_2_2.setCellStyle(setCellStyle(cellStyle));
            cell_2_2_1.setCellStyle(setCellStyle(cellStyle));
            cell_2_2_2.setCellStyle(setCellStyle(cellStyle));


            Cell cell_2_3 = row2.createCell(4);
            Cell cell_2_3_1 = row2.createCell(4);
            Cell cell_2_3_2 = row2.createCell(4);
            cell_2_3.setCellValue("整改措施");
            cell_2_3.setCellStyle(setCellStyle(cellStyle));
            cell_2_3_1.setCellStyle(setCellStyle(cellStyle));
            cell_2_3_2.setCellStyle(setCellStyle(cellStyle));


            Cell cell_2_4 = row2.createCell(5);
            Cell cell_2_4_1 = row2.createCell(6);
            Cell cell_2_4_2 = row2.createCell(7);
            Cell cell_2_4_3 = row2.createCell(8);
            Cell cell_2_4_4 = row2.createCell(9);
            cell_2_4.setCellValue("存在问题的分库名称及数量");
            cell_2_4.setCellStyle(setCellStyle(cellStyle));
            cell_2_4_1.setCellStyle(setCellStyle(cellStyle));
            cell_2_4_2.setCellStyle(setCellStyle(cellStyle));
            cell_2_4_3.setCellStyle(setCellStyle(cellStyle));
            cell_2_4_4.setCellStyle(setCellStyle(cellStyle));


            Cell cell_3_1 = row3.createCell(5);
            Cell cell_3_1_1 = row4.createCell(5);
            cell_3_1.setCellValue("云南省分库");
            cell_3_1.setCellStyle(setCellStyle(cellStyle));
            cell_3_1_1.setCellStyle(setCellStyle(cellStyle));


            Cell cell_3_2 = row3.createCell(6);
            Cell cell_3_2_1 = row3.createCell(7);
            cell_3_2.setCellValue("中心支库");
            cell_3_2.setCellStyle(setCellStyle(cellStyle));
            cell_3_2_1.setCellStyle(setCellStyle(cellStyle));


            Cell cell_3_3 = row3.createCell(8);
            Cell cell_3_3_1 = row3.createCell(9);
            cell_3_3.setCellValue("支库");
            cell_3_3.setCellStyle(setCellStyle(cellStyle));
            cell_3_3_1.setCellStyle(setCellStyle(cellStyle));

            sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 2));
            sheet.addMergedRegion(new CellRangeAddress(2, 4, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(2, 4, 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 9));
            sheet.addMergedRegion(new CellRangeAddress(3, 4, 5, 5));
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 9));

            Cell cell_4_1 = row4.createCell(6);
            cell_4_1.setCellValue("名称");
            cell_4_1.setCellStyle(setCellStyle(cellStyle));
            Cell cell_4_2 = row4.createCell(7);
            cell_4_2.setCellValue("数量" + "\r\n" + "（个）");
            cell_4_2.setCellStyle(setCellStyle(cellStyle));
            Cell cell_4_3 = row4.createCell(8);
            cell_4_3.setCellValue("名称");
            cell_4_3.setCellStyle(setCellStyle(cellStyle));
            Cell cell_4_4 = row4.createCell(9);
            cell_4_4.setCellValue("数量" + "\r\n" + "（个）");
            cell_4_4.setCellStyle(setCellStyle(cellStyle));

            sheet.setColumnWidth(0, 256 * 20 + 184);
            for (int m = 1; m < totalLen; m++) {
                sheet.setColumnWidth(m, 256 * 15 + 184);
            }

            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            styleMap.put("cellStyle", setDataStyle(wb));

            //添加表中内容
            for (int row = 0; row < datalist.size(); row++) {//数据行
                //创建新行
                HSSFRow newrow = sheet.createRow(row + 5);//数据从第四行开始
                //获取该行的数据
                @SuppressWarnings("unchecked")
                Map<String, Object> data = datalist.get(row);
                for (int col = 0; col < zdlist.length; col++) {//列
                    //数据从第一列开始
                    //创建单元格并放入数据
                    Cell dataCell = newrow.createCell(col);
                    dataCell.setCellValue(data != null && data.get(zdlist[col]) != null ? String.valueOf(data.get(zdlist[col])) : "");
                    dataCell.setCellStyle(styleMap.get("cellStyle"));
                }
            }

            //合并单元格
            Map<Integer, Integer> mergedCol_1 = new HashMap<>();
            int index_1 = 1;
            mergedCol_1.put(index_1, 0);
            Map<Integer, Integer> mergedCol_2 = new HashMap<>();
            int index_2 = 1;
            mergedCol_2.put(index_2, 0);
            Map<Integer, Integer> mergedCol_3 = new HashMap<>();
            int index_3 = 1;
            mergedCol_3.put(index_3, 0);
            for (int i = 0; i < datalist.size() - 1; i++) {
                if (datalist.get(i).get("QUESTION_ID_1").equals(datalist.get(i + 1).get("QUESTION_ID_1"))) {
                    mergedCol_1.put(index_1, mergedCol_1.get(index_1) + 1);
                    if (datalist.get(i).get("QUESTION_ID_2").equals(datalist.get(i + 1).get("QUESTION_ID_2"))) {
                        mergedCol_2.put(index_2, mergedCol_2.get(index_2) + 1);
                        if (datalist.get(i).get("QUESTION_ID_3").equals(datalist.get(i + 1).get("QUESTION_ID_3"))) {
                            mergedCol_3.put(index_3, mergedCol_3.get(index_3) + 1);
                        } else {
                            mergedCol_3.put(index_3 + 1, 1);
                        }
                    } else {
                        mergedCol_2.put(index_2 + 1, 1);
                    }
                } else {
                    mergedCol_1.put(index_1 + 1, 1);
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(5, 5 + mergedCol_1.get(1), 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(5, 5 + mergedCol_2.get(1), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(5, 5 + mergedCol_3.get(1), 2, 2));
            for (int i = 2; i <= index_1; i++) {
                int firstRow = 5 + mergedCol_1.get(i - 1);
                int lastRow = 5 + mergedCol_1.get(i - 1) + mergedCol_1.get(i);
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
            }
            for (int i = 2; i <= index_2; i++) {
                int firstRow = 5 + mergedCol_2.get(i - 1);
                int lastRow = 5 + mergedCol_2.get(i - 1) + mergedCol_2.get(i);
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
            }
            for (int i = 2; i <= index_3; i++) {
                int firstRow = 5 + mergedCol_3.get(i - 1);
                int lastRow = 5 + mergedCol_3.get(i - 1) + mergedCol_3.get(i);
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 2, 2));
            }
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件1
            FileOutputStream output = new FileOutputStream(filepath + filename);
            wb.write(output);//写入磁盘
            output.close();
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 生成Excel 并放到指定位置
     *
     * @param filepath 文件路径(要绝对路径)
     * @param filename 文件名称 (如: demo.xls  记得加.xls哦)
     * @param title    表头
     * @param zdlist   字段list
     * @param datalist 数据list (这里也可以改成List<Map<String,String>>  格式的数据)
     * @return 是否正常生成
     * @throws IOException
     * @author: 2018年11月24日 上午11:40:39
     * (titlelist  和  zdlist  顺序要一直, 要一一对应)
     */
    public static boolean createTemproraySumTable(
            String title,
            String filepath,
            String filename,
            String[] clns,
            String[] zdlist,
            List<Map<String, Object>> datalist) throws IOException {
        boolean success = false;
        int totalLen = 7;
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            CellStyle cellStyle = wb.createCellStyle();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet(title);
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);
            row0.setHeightInPoints(30);
            Cell cell = row0.createCell(0);
            cell.setCellValue(title);
            cell.setCellStyle(setTitleStyle(wb));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalLen - 1));

            HSSFRow row1 = sheet.createRow(1);
            HSSFRow row2 = sheet.createRow(2);

            for (int i = 0; i < clns.length; i++) {
                Cell cell_temp = row2.createCell(i);
                cell_temp.setCellValue(clns[i]);
                cell_temp.setCellStyle(setCellStyle(cellStyle));
            }

            sheet.setColumnWidth(0, 256 * 20 + 184);
            for (int m = 1; m < totalLen; m++) {
                sheet.setColumnWidth(m, 256 * 15 + 184);
            }

            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            styleMap.put("cellStyle", setDataStyle(wb));

            //添加表中内容
            for (int row = 0; row < datalist.size(); row++) {//数据行
                //创建新行
                HSSFRow newrow = sheet.createRow(row + 3);//数据从第四行开始
                //获取该行的数据
                @SuppressWarnings("unchecked")
                Map<String, Object> data = datalist.get(row);
                for (int col = 0; col < zdlist.length; col++) {//列
                    //数据从第一列开始
                    //创建单元格并放入数据
                    Cell dataCell = newrow.createCell(col);
                    dataCell.setCellValue(data != null && data.get(zdlist[col]) != null ? String.valueOf(data.get(zdlist[col])) : "");
                    dataCell.setCellStyle(styleMap.get("cellStyle"));
                }
            }

            //合并单元格
            Map<Integer, Integer> mergedCol_1 = new HashMap<>();
            int index_1 = 1;
            mergedCol_1.put(index_1, 0);
            Map<Integer, Integer> mergedCol_2 = new HashMap<>();
            int index_2 = 1;
            mergedCol_2.put(index_2, 0);
/*            Map<Integer, Integer> mergedCol_3 = new HashMap<>();
            int index_3 = 1;
            mergedCol_3.put(index_3, 0);*/
            for (int i = 0; i < datalist.size() - 1; i++) {
                if (datalist.get(i).get("QUESTION_ID_1").equals(datalist.get(i + 1).get("QUESTION_ID_1"))) {
                    mergedCol_1.put(index_1, mergedCol_1.get(index_1) + 1);
                    if (datalist.get(i).get("QUESTION_ID_2").equals(datalist.get(i + 1).get("QUESTION_ID_2"))) {
                        mergedCol_2.put(index_2, mergedCol_2.get(index_2) + 1);
                        /*if (datalist.get(i).get("QUESTION_ID_3").equals(datalist.get(i + 1).get("QUESTION_ID_3"))) {
                            mergedCol_3.put(index_3, mergedCol_3.get(index_3) + 1);
                        } else {
                            mergedCol_3.put(index_3 + 1, 1);
                        }*/
                    } else {
                        mergedCol_2.put(index_2 + 1, 1);
                    }
                } else {
                    mergedCol_1.put(index_1 + 1, 1);
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(3, 3 + mergedCol_1.get(1), 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(3, 3 + mergedCol_2.get(1), 1, 1));
            //sheet.addMergedRegion(new CellRangeAddress(5, 5 + mergedCol_3.get(1), 2, 2));
            for (int i = 2; i <= index_1; i++) {
                int firstRow = 3 + mergedCol_1.get(i - 1);
                int lastRow = 3 + mergedCol_1.get(i - 1) + mergedCol_1.get(i);
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
            }
            for (int i = 2; i <= index_2; i++) {
                int firstRow = 3 + mergedCol_2.get(i - 1);
                int lastRow = 3 + mergedCol_2.get(i - 1) + mergedCol_2.get(i);
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
            }
            /*for (int i = 2; i <= index_3; i++) {
                int firstRow = 5 + mergedCol_3.get(i - 1);
                int lastRow = 5 + mergedCol_3.get(i - 1) + mergedCol_3.get(i);
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 2, 2));
            }*/
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件1
            FileOutputStream output = new FileOutputStream(filepath + filename);
            wb.write(output);//写入磁盘
            output.close();
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    private static CellStyle setTitleStyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("黑体"); //字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
        font.setItalic(false); //是否使用斜体

        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setWrapText(true);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
        return titleStyle;
    }

    /**
     * 表头样式
     *
     * @param cellStyle
     * @return
     */
    private static CellStyle setCellStyle(CellStyle cellStyle) {
        cellStyle.setWrapText(true);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 前景色
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);  // 左边边框
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色

        cellStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
        cellStyle.setBorderTop(CellStyle.BORDER_THIN); // 上边边框
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());  // 上边边框颜色

        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
        return cellStyle;
    }

    /**
     * 表体数据样式
     *
     * @param wb
     * @return
     */
    private static CellStyle setDataStyle(HSSFWorkbook wb) {
        CellStyle dataStyle = wb.createCellStyle();

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


} ///:~
