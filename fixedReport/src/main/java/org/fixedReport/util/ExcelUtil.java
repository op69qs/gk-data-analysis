package org.fixedReport.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fixedReport.modules.PoiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * excel 导出工具类
 * @author henrylq
 *
 */
public class ExcelUtil {
    private static DecimalFormat df = new DecimalFormat("0");
    // 默认单元格格式化日期字符串
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 格式化数字
    private static DecimalFormat nf = new DecimalFormat("0.00");

    private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void export(List<Map<String, Object>> list, String filename, HttpServletResponse response, String titles) {
        try {
            response.setContentType("application/x-execl");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));
            ServletOutputStream outputStream = response.getOutputStream();

            exportExcel(list, filename, outputStream, titles);
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据导出公用方法
     */
    public static void exportExcel(List<Map<String, Object>> list, String filename, ServletOutputStream outputStream, String titles) {
        try {
            Map<String, Object> map1 = list.get(0);
            StringBuilder sb = new StringBuilder();
            String[] fields = titles.split(",");
            /*sb.append(title);
            String[] fields = sb.toString().split(",");*/
            // 1、创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 1.1、创建合并单元格对象
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, fields.length - 1);// 起始行号，结束行号，起始列号，结束列号
            CellRangeAddress cellRangeAddress1 = new CellRangeAddress(1, 1, 0, fields.length - 1);// 起始行号，结束行号，起始列号，结束列号
            CellRangeAddress cellRangeAddress2 = new CellRangeAddress(1, 1, 7, 12);// 起始行号，结束行号，起始列号，结束列号
            // 1.2、头标题样式
            HSSFCellStyle style1 = createCellStyle(workbook, (short) 18);
            // 1.3、列标题样式
            HSSFCellStyle style2 = createCellStyle(workbook, (short) 13);
            String headRow = filename;
            // 2、创建工作表
            HSSFSheet sheet = workbook.createSheet(headRow);
            // 2.1、加载合并单元格对象
            sheet.addMergedRegion(cellRangeAddress);
            sheet.addMergedRegion(cellRangeAddress1);
            sheet.addMergedRegion(cellRangeAddress2);
            // sheet.addMergedRegion(cellRangeAddress4);
            // 设置默认列宽
            sheet.setDefaultColumnWidth(22);
            sheet.setDefaultRowHeightInPoints(22);
            sheet.autoSizeColumn(1, true);
            //
            // 3、创建行
            // 3.1、创建头标题行；并且设置头标题
            HSSFRow row1 = sheet.createRow(0);
            row1.setHeightInPoints(50);
            HSSFCell cell1 = row1.createCell(0);
            // 加载单元格样式
            cell1.setCellStyle(style1);
            cell1.setCellValue(headRow);
            // 3.1、创建副标题行；并且设置
            HSSFRow row2 = sheet.createRow(1);
            row2.setHeightInPoints(25);
            HSSFCell cell2 = row2.createCell(0);
            // 3.2、创建列标题行；并且设置列标题
            HSSFRow row3 = sheet.createRow(2);

            /*String[] titles = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                titles[i] = getTitles(type, fields[i]);
            }*/

            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell5 = row3.createCell(i);
                // 加载单元格样式
                cell5.setCellStyle(style2);
                cell5.setCellValue(fields[i]);
            }
            // 备注详情
            HSSFRow row36 = sheet.createRow(1);
            row36.setHeightInPoints(18);
            HSSFCell cell36 = row36.createCell(0);
            // 加载单元格样式
            HSSFCellStyle style36 = workbook.createCellStyle();
            style36.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居左
            style36.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
            cell36.setCellStyle(style36);
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗字体
            font.setFontHeightInPoints((short) 16);
            style36.setFont(font);
            //cell36.setCellValue(data_batch);

            // 自适应中文

            // 4、操作单元格；写入excel
            if (list != null && list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    Map<String, Object> map = list.get(j);
                    //Object bean = getBean(type, map);
                    HSSFRow row = sheet.createRow(j + 3);
                    int i = 0;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String val = "";
                        if ((entry.getValue()) != null && (entry.getValue()) != "null") {
                            val = entry.getValue().toString();
                        } else {
                            val = " ";
                        }
                        HSSFCell cell = row.createCell(i);
                        i++;
                        cell.setCellValue(val);
                        logger.info("第" + (j + 3) + "行,第" + val + "--赋值成功");
                    }
                }
            }
            // 5、输出
            workbook.write(outputStream);

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建单元格样式
     *
     * @param workbook 工作簿
     * @param fontSize 字体大小
     * @return 单元格样式
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontSize) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 创建字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗字体
        font.setFontHeightInPoints(fontSize);
        // 加载字体
        style.setFont(font);
        return style;
    }

    public static DecimalFormat getDf() {
        return df;
    }

    public static void setDf(DecimalFormat df) {
        ExcelUtil.df = df;
    }

    public static SimpleDateFormat getSdf() {
        return sdf;
    }

    public static void setSdf(SimpleDateFormat sdf) {
        ExcelUtil.sdf = sdf;
    }

    public static DecimalFormat getNf() {
        return nf;
    }

    public static void setNf(DecimalFormat nf) {
        ExcelUtil.nf = nf;
    }

    /**
     * 导出多行表头 合并行
     * @param request
     * @param response
     * @param dataList 数据集合
     * @param sheetName  标题行
     * @param headList 副标题集合 （head:副标题内容，headnum：合并位置{"开始行,结束行,开始列,结束列"}）
     * @param detail  表字段
     * @param mergeIndex  合并的列
     * @param isHeJi 合计行
     * @throws Exception
     */
    public static void reportMergeXls(HttpServletRequest request,
                                      HttpServletResponse response, List<Map<String, Object>> dataList,
                                      String sheetName, List<Map<String, Object>> headList, String[] detail, int[] mergeIndex, boolean isHeJi)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 1、创建标题
        String headRow = sheetName;
        // 1.1、头标题样式
        HSSFCellStyle headstyle = createCellStyle(workbook, (short) 18);
        // 1.2、列标题样式
        HSSFCellStyle style1 = createCellStyle(workbook, (short) 13);
        // 2、创建工作表
        HSSFSheet sheet = workbook.createSheet(headRow);
        // 设置默认列宽
        sheet.setDefaultColumnWidth(22);
        sheet.setDefaultRowHeightInPoints(22);
        sheet.autoSizeColumn(1, true);
        // 3、创建行
        // 3.1、创建头标题行；并且设置头标题
        HSSFRow row1 = sheet.createRow(0);
        row1.setHeightInPoints(50);
        HSSFCell cell1 = row1.createCell(0);
        // 加载单元格样式
        cell1.setCellStyle(headstyle);
        cell1.setCellValue(headRow);
        // 3.1、创建副标题行；并且设置
        HSSFRow row2 = sheet.createRow(1);
        row2.setHeightInPoints(25);
        HSSFCell cell2 = row2.createCell(0);
        // 3.2、创建列标题行；并且设置列标题
        HSSFRow row3 = sheet.createRow(2);
        // 第一行表头标题
        String[] head0 = (String[]) headList.get(0).get("head");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head0.length - 1));
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 0x349);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(headstyle);
        CellUtil.setCellValue(cell, sheetName);
        int rowIndex = 1;//副标题第一行开始
        for (int k = 0; k < headList.size(); k++) {
            String[] head = (String[]) headList.get(k).get("head");
            String[] headnum = (String[]) headList.get(k).get("headnum");
            row = sheet.createRow(rowIndex);
            if (k == 0) {
                for (int i = 0; i < head.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(head[i]);
                    cell.setCellStyle(style1);
                }
                if (headnum != null){
                    for (int i = 0; i < headnum.length; i++) {
                        String[] temp = headnum[i].split(",");
                        Integer startrow = Integer.parseInt(temp[0]);
                        Integer overrow = Integer.parseInt(temp[1]);
                        Integer startcol = Integer.parseInt(temp[2]);
                        Integer overcol = Integer.parseInt(temp[3]);
                        sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                                startcol, overcol));
                    }
                }
            } else {
                String[] head1 = (String[]) headList.get(k - 1).get("head");
                for (int i = 0; i < head1.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(style1);
                    if (i > 1) {
                        for (int j = 0; j < head.length; j++) {
                            cell = row.createCell(j);
                            cell.setCellValue(head[j]);
                            cell.setCellStyle(style1);
                        }
                    }
                }
                if (headnum != null){
                    for (int i = 0; i < headnum.length; i++) {
                        String[] temp = headnum[i].split(",");
                        Integer startrow = Integer.parseInt(temp[0]);
                        Integer overrow = Integer.parseInt(temp[1]);
                        Integer startcol = Integer.parseInt(temp[2]);
                        Integer overcol = Integer.parseInt(temp[3]);
                        sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                                startcol, overcol));
                    }
                }
            }
            rowIndex++;
        }

        // 1.3、普通单元格样式（中文）样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居左
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);// 加粗字体
        font.setFontHeightInPoints((short) 10);
        style2.setFont(font);

        if (mergeIndex == null) {
            for (int i = 0; i < dataList.size(); i++) {
                row = sheet.createRow(i + rowIndex);//标题、时间、表头字段共占了3行，所以在填充数据的时候要加3，也就是数据要从第4行开始填充
                for (int j = 0; j < detail.length; j++) {
                    Map tempmap = (HashMap) dataList.get(i);
                    Object data = tempmap.get(detail[j]);
                    cell = row.createCell(j);
                    cell.setCellStyle(style2);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    CellUtil.setCellValue(cell, data);
                }
            }
        } else {
            /*遍历该数据集合*/
            int index = rowIndex;
            List<PoiModel> poiModels = Lists.newArrayList();
            if (null != workbook) {
                Iterator iterator = dataList.iterator();
                while (iterator.hasNext()) {
                    row = sheet.createRow(index);
                    /*取得当前这行的map，该map中以key，value的形式存着这一行值*/
                    Map<String, String> map = (Map<String, String>) iterator.next();
                    /*循环列数，给当前行塞值*/
                    for (int i = 0; i < detail.length; i++) {
                        String old = "";
                        /*old存的是上一行统一位置的单元的值，第一行是最上一行了，所以从第二行开始记*/
                        if (index > rowIndex) {
                            old = poiModels.get(i) == null ? "" : poiModels.get(i).getContent();
                        }

                        /*循环需要合并的列*/
                        for (int j = 0; j < mergeIndex.length; j++) {
                            if (index == rowIndex) {
                                /*记录第一行的开始行和开始列*/
                                PoiModel poiModel = new PoiModel();
                                poiModel.setOldContent(String.valueOf(map.get(detail[i])));
                                poiModel.setContent(String.valueOf(map.get(detail[i])));
                                poiModel.setRowIndex(rowIndex);
                                poiModel.setCellIndex(i);
                                poiModels.add(poiModel);
                                break;
                            } else if (i > 0 && mergeIndex[j] == i) {/*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/
                                /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/
                                /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中||poiModels.get(i).getContent().equals(String.valueOf(map.get(title[i]))) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))去掉就行*/
                                if (!poiModels.get(i).getContent().equals(String.valueOf(map.get(detail[i]))) || poiModels.get(i).getContent().equals(String.valueOf(map.get(detail[i]))) && !poiModels.get(i - 1).getOldContent().equals(map.get(detail[i - 1]))) {
                                    /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                    CellRangeAddress cra = new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                    //在sheet里增加合并单元格
                                    sheet.addMergedRegion(cra);
                                    /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/
                                    poiModels.get(i).setContent(String.valueOf(map.get(detail[i])));
                                    poiModels.get(i).setRowIndex(index);
                                    poiModels.get(i).setCellIndex(i);
                                }
                            }
                            /*处理第一列的情况*/
                            if (mergeIndex[j] == i && i == 0 && !poiModels.get(i).getContent().equals(String.valueOf(map.get(detail[i])))) {
                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                CellRangeAddress cra = new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                                /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                                poiModels.get(i).setContent(String.valueOf(map.get(detail[i])));
                                poiModels.get(i).setRowIndex(index);
                                poiModels.get(i).setCellIndex(i);
                            }

                            /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容 ，index初始值不是1 ，需减去等于1*/
                            if (mergeIndex[j] == i && index == dataList.size()) {
                                CellRangeAddress cra = new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                            }
                        }
                        cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(String.valueOf(map.get(detail[i])));
                        cell.setCellStyle(style2);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        /*在每一个单元格处理完成后，把这个单元格内容设置为old内容*/
                        poiModels.get(i).setOldContent(old);
                    }
                    index++;
                }
            }
        }
        if (isHeJi) {
            row = sheet.createRow(dataList.size() + 3);
            cell = row.createCell(0);
            cell.setCellStyle(style2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            CellUtil.setCellValue(cell, "合计：");
            if (detail.length > 1) {
                for (int i = 1; i < detail.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(style2);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    String colString = CellReference.convertNumToColString(i);  //长度转成ABC列
                    String sumstring = "SUM(" + colString + "1:" + colString + dataList.size() + ")";//求和公式
                    sheet.getRow(dataList.size() + 3).getCell(i).setCellFormula(sumstring);
                }
            }
        }
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        sheet.setForceFormulaRecalculation(true);
        String fileName = new String(sheetName.getBytes("gb2312"), "ISO8859-1");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        response.setContentType("application/x-download;charset=utf-8");
        if (agent.contains("firefox")) {
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls" );
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
        }
//        response.addHeader("Content-Disposition", "attachment;filename="
//                + sheetName + ".xls");
        OutputStream os = response.getOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] b = new byte[1024];
        while ((bais.read(b)) > 0) {
            os.write(b);
        }
        bais.close();
        os.flush();
        os.close();
    }
}
