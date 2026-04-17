// ExcelCreateHelper.java

package org.indicatorsLib.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/30.
 */
public class ExcelCreateHelper {

    /**
     * 单表头EXCEL组装
     *
     * @param title     表名
     * @param filepath  存储路径文件夹
     * @param filename  文件名称
     * @param titlelist 表头列表
     * @param zdlist    需要填充的字段ID
     * @param datalist  数据列表
     * @return
     * @throws IOException
     */
    public static boolean createSimpleExcel(
            String title,
            String ADD_DATE,
            String sub_title_1,
            String sub_title_2,
            String sub_title_3,
            String filepath,
            String filename,
            List<String> titlelist,
            String[] zdlist,
            List<Map<String, Object>> datalist
    ) throws IOException {
        boolean success = false;
        int len_1 = titlelist.size();
        int divlen = len_1 / 3;
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(title);
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

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, len_1 - 1));

            HSSFRow row1 = sheet.createRow(1);
            Cell cAddDate = row1.createCell(0);
            cAddDate.setCellValue(ADD_DATE);
            CellStyle titleStyle1 = wb.createCellStyle();
            titleStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
            titleStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
            cAddDate.setCellStyle(titleStyle1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, len_1 - 1));

            HSSFRow row2 = sheet.createRow(2);
            if (sub_title_1 != null) {
                Cell subTitle1 = row2.createCell(0);
                subTitle1.setCellValue(sub_title_1);
                if (divlen >= 2) {
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
                }
            }
            if (sub_title_2 != null) {
                Cell subTitle2 = row2.createCell(0 + divlen);
                subTitle2.setCellValue(sub_title_2);
                if (divlen >= 2) {
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0 + divlen, 0 + divlen + 1));
                }
            }
            if (sub_title_3 != null) {
                Cell subTitle3 = row2.createCell(len_1 - 1);
                subTitle3.setCellValue(sub_title_3);
            }

            HSSFRow row3 = sheet.createRow(3);
            // 添加表头
            for (int i = 0; i < len_1; i++) {
                Cell cellSub = row3.createCell(i);
                cellSub.setCellValue(titlelist.get(i));
                cellSub.setCellStyle(setCellStyle(wb));
            }
            //列宽
            for (int m = 0; m < len_1; m++) {
                sheet.setColumnWidth(m, 256 * 15 + 184);
            }
            sheet.setColumnWidth(1, 256 * 40 + 184);

            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            styleMap.put("cellStyle", setDataStyle(wb));

            //添加表中内容
            for (int row = 0; row < datalist.size(); row++) {//数据行
                //创建新行
                HSSFRow newrow = sheet.createRow(row + 4);//数据从第四行开始
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
     * 表头样式
     *
     * @param wb
     * @return
     */
    private static CellStyle setCellStyle(HSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
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
