package org.inspect.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExcel {
    /**
     * 生成Excel 并放到指定位置
     *@param filepath 文件路径(要绝对路径)
     *@param filename 文件名称 (如: demo.xls  记得加.xls哦)
     *@param titlelist 标题名称list
     *@param zdlist 字段list
     *@param datalist 数据list (这里也可以改成List<Map<String,String>>  格式的数据)
     *@return 是否正常生成
     *@throws IOException
     *@author:
     * (titlelist  和  zdlist  顺序要一直, 要一一对应)
     */
    public static boolean createExcel(String title,
                                      String filepath,
                                      String filename,
                                      List<String> titlelist,
                                      List<String> zdlist,
                                      List<Map<String,Object>> datalist,
                                      String inspected_charge,
                                      String leader,
                                      String type) throws IOException {
        boolean success = false;
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            // 建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row0 = sheet.createRow(0);
            row0.setHeightInPoints(30);
            Cell cell=row0.createCell(0);
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

            sheet.addMergedRegion(new CellRangeAddress(0,0,0,7));

            HSSFRow rowX = sheet.createRow(1);
            Cell celX=rowX.createCell(0);

            if (null != type && (type.equals("001") || type.equals("006"))){
                celX.setCellValue("被检查国库：" + datalist.get(0).get("inspected_guoku").toString());
            }

            HSSFRow row1 = sheet.createRow(2);
            // 添加表头
            for(int i = 0;i<titlelist.size();i++){
                Cell cellSub=row1.createCell(i);
                cellSub.setCellValue(titlelist.get(i));
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

                cellSub.setCellStyle(cellStyle);
            }

            sheet.setColumnWidth(0,256*5+184);
            sheet.setColumnWidth(1,256*40+184);
            sheet.setColumnWidth(2,256*5+184);
            sheet.setColumnWidth(3,256*25+184);
            sheet.setColumnWidth(4,256*15+184);
            sheet.setColumnWidth(5,256*15+184);
            sheet.setColumnWidth(6,256*15+184);
            sheet.setColumnWidth(7,256*15+184);

            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            styleMap.put("cellStyle", setDataStyle(wb));

            //添加表中内容
            int lastNum = 0;
            lastNum = datalist.size()+3;
            for(int row = 0;row<datalist.size();row++){//数据行
                //创建新行
                HSSFRow newrow = sheet.createRow(row+3);//数据从第四行开始
                //获取该行的数据
                @SuppressWarnings("unchecked")
                Map<String,Object> data = datalist.get(row);

                for(int col = 0;col<zdlist.size();col++){//列
                    //数据从第一列开始
                    //创建单元格并放入数据
                    Cell dataCell = newrow.createCell(col);
                    dataCell.setCellValue(data!=null&&data.get(zdlist.get(col))!=null?String.valueOf(data.get(zdlist.get(col))):"");
                    dataCell.setCellStyle(styleMap.get("cellStyle"));

                }
            }
            HSSFRow lastRow = sheet.createRow(lastNum);
            Cell inspectedCell = lastRow.createCell(0);
            Cell leaderCell = lastRow.createCell(4);
            if (null != type && type.equals("001")){
                inspectedCell.setCellValue("被查国库部门负责人：" + inspected_charge);
                leaderCell.setCellValue("检查组负责人：" + leader);
            }
            if (null != type && type.equals("005")){
                inspectedCell.setCellValue("被查国库部门负责人：" + inspected_charge);
                leaderCell.setCellValue("事后监督部门负责人：" + datalist.get(0).get("supervisor").toString());
                HSSFRow supervisorRow = sheet.createRow(lastNum+1);
                Cell supervisorCell = supervisorRow.createCell(0);
                supervisorCell.setCellValue("检查组组长：" + leader);
            }
            if (null != type && type.equals("006")){
                inspectedCell.setCellValue("被查国库部门负责人：" + inspected_charge);
            }

            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件1
            FileOutputStream output=new FileOutputStream(filepath+filename);
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

}
