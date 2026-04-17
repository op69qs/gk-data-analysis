package org.indicatorsLib.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class CreateSchemeExcel {

    public static boolean exportExcel(Map<String, Object> map) throws Exception {
        boolean success = false;
        String[] titleArray = (String[]) map.get("titleArray");
        String[] columnArray = (String[]) map.get("columnArray");
        List<Map<String, Object>> datalist = (List<Map<String, Object>>) map.get("datalist");
        SXSSFWorkbook workbook = exportNoNum(titleArray, columnArray, datalist);
        //判断是否存在目录. 不存在则创建
        isChartPathExist(map.get("filePath").toString());
        try (FileOutputStream outStream = new FileOutputStream(new File(map.get("filePath").toString() + "/" + map.get("fileName").toString()));) {
            if (workbook != null) {
                workbook.write(outStream);
                outStream.flush();
                success = true;
            }
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    public static SXSSFWorkbook exportNoNum(String[] titleArray, String[] columnArray, List<Map<String, Object>> datalist) throws Exception {
        int rowaccess = 100;//内存中缓存记录行数
        int sheetRowNum = 5000; //sheet页中的行数
        int listStartIndex = 0; //每次遍历集合的起始点下标
        int currRowNum = 1; //当前数据所在的行数
        SXSSFWorkbook workbook = new SXSSFWorkbook(rowaccess); // 创建工作簿对象
        //初始化Excel对象中的参数
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        Map<String, Object> dataRows = null;
        CellStyle style = getStyle(workbook); // 单元格样式对象
        CellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
        String sheets = new DecimalFormat("0.0000").format((float) datalist.size() / sheetRowNum);//格式化小数
        int sheetNum = (int) Math.ceil(Double.valueOf(sheets));//每sheetRowNum条数据生成1个SHEET
        //如果数据小于sheet设置的行数则sheet页最大行数等于数据数量
        int sheetRow = datalist.size() > sheetRowNum ? sheetRowNum : datalist.size();

        for (int sh = 0; sh < sheetNum; sh++) {
            sheet = workbook.createSheet(); // 创建工作表
            //每个SHEET有sheetRow+1行(表头)
            for (int rownum = 0; rownum < sheetRow + 1; rownum++) {
                row = sheet.createRow(rownum);
                if (rownum == 0) {
                    //产生Excel表格表头列标题行
                    for (int cellnum = 0; cellnum < titleArray.length; cellnum++) {
                        // 将列头设置到sheet的单元格中
                        cell = row.createCell(cellnum); //创建列头对应个数的单元格
                        cell.setCellType(Cell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
                        cell.setCellStyle(columnTopStyle); // 设置列头单元格样式
                        sheet.setColumnWidth(cell.getColumnIndex(), 256 * 30); //设置单元格的宽度
                        cell.setCellValue(new XSSFRichTextString(titleArray[cellnum])); // 设置列头单元格的值
                    }
                } else {
                    //list集合下标自增
                    listStartIndex = currRowNum - 1;
                    if (listStartIndex == datalist.size()) break;
                    dataRows = datalist.get(listStartIndex);
                    //将查询出的数据设置到sheet对应的单元格中
                    for (int i = 0; i < columnArray.length; i++) {
                        // 设置单元格的值
                        CellUtil.createCell(row, i, dataRows.get(columnArray[i]).toString()).setCellStyle(style);
                    }
                    //写入成功一行数据递增行数
                    currRowNum += 1;
                }

                //每当行数达到设置的值就刷新数据到硬盘,以清理内存
                if (listStartIndex % rowaccess == 0) {
                    ((SXSSFSheet) sheet).flushRows();
                }
            }
        }
        datalist.clear();
        return workbook;
    }

    /*
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

    private static CellStyle getColumnTopStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
//        style.setFillForegroundColor((short) 21);// 设置标题背景色
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);// 填充标题背景色
        // 设置底边框;
        style.setBorderBottom(CellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(CellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(CellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(CellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }

    /*
     * 列数据信息单元格样式
     */
    private static CellStyle getStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(CellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(CellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(CellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(CellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return style;
    }
}
