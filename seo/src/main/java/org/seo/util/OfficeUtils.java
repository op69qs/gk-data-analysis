package org.seo.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OfficeUtils {
    private static final Logger log = LoggerFactory.getLogger(OfficeUtils.class);

    public OfficeUtils() {
    }

    public static String readXls(String file) throws IOException {
        StringBuilder content = new StringBuilder();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

        for(int numSheets = 0; numSheets < workbook.getNumberOfSheets(); ++numSheets) {
            if (null != workbook.getSheetAt(numSheets)) {
                HSSFSheet aSheet = workbook.getSheetAt(numSheets);

                for(int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); ++rowNumOfSheet) {
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        HSSFRow aRow = aSheet.getRow(rowNumOfSheet);

                        for(short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); ++cellNumOfRow) {
                            if (null != aRow.getCell(cellNumOfRow)) {
                                HSSFCell aCell = aRow.getCell(cellNumOfRow);
                                if (convertCell(aCell).length() > 0) {
                                    content.append(convertCell(aCell));
                                }
                            }

                            content.append("\n");
                        }
                    }
                }
            }
        }

        return content.toString();
    }

    public static String readXlsx(String file) throws IOException {
        StringBuilder content = new StringBuilder();
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        for(int numSheets = 0; numSheets < workbook.getNumberOfSheets(); ++numSheets) {
            if (null != workbook.getSheetAt(numSheets)) {
                XSSFSheet aSheet = workbook.getSheetAt(numSheets);

                for(int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); ++rowNumOfSheet) {
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        XSSFRow aRow = aSheet.getRow(rowNumOfSheet);

                        for(short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); ++cellNumOfRow) {
                            if (null != aRow.getCell(cellNumOfRow)) {
                                XSSFCell aCell = aRow.getCell(cellNumOfRow);
                                if (convertCell(aCell).length() > 0) {
                                    content.append(convertCell(aCell));
                                }
                            }

                            content.append("\n");
                        }
                    }
                }
            }
        }

        return content.toString();
    }

    private static String convertCell(Cell cell) {
        NumberFormat formater = NumberFormat.getInstance();
        formater.setGroupingUsed(false);
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        } else {
            switch(cell.getCellType()) {
                case 0:
                    cellValue = formater.format(cell.getNumericCellValue());
                    break;
                case 1:
                    cellValue = cell.getStringCellValue();
                    break;
                case 2:
                default:
                    cellValue = "";
                    break;
                case 3:
                    cellValue = cell.getStringCellValue();
                    break;
                case 4:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case 5:
                    cellValue = String.valueOf(cell.getErrorCellValue());
            }

            return cellValue.trim();
        }
    }

    public static String readDoc(String file) throws Exception {
        String returnStr = "";
        WordExtractor wordExtractor = new WordExtractor(new FileInputStream(new File(file)));
        returnStr = wordExtractor.getText();
        return returnStr;
    }

    public static String readDocx(String file) throws Exception {
        return (new XWPFWordExtractor(POIXMLDocument.openPackage(file))).getText();
    }

    public static String readPDF(String file) throws IOException {
        String result = null;
        FileInputStream is = null;
        PDDocument document = null;

        try {
            is = new FileInputStream(file);
            PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
            parser.parse();
            document = parser.getPDDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setDropThreshold(4.0F);
            result = stripper.getText(document);
        } finally {
            if (is != null) {
                is.close();
            }

            if (document != null) {
                document.close();
            }

        }

        return result;
    }

    public static String readTXT(String file) throws IOException {
        String encoding = get_charset(new File(file));
        log.debug("file: [{}], encoding: [{}]", file, encoding);
        return encoding.equalsIgnoreCase("GBK") ? FileUtils.readFileToString(new File(file), "gbk") : FileUtils.readFileToString(new File(file), "utf8");
    }

    private static String get_charset(File file) throws IOException {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;

        String var6;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read != -1) {
                if (first3Bytes[0] == -1 && first3Bytes[1] == -2) {
                    charset = "UTF-16LE";
                    checked = true;
                } else if (first3Bytes[0] == -2 && first3Bytes[1] == -1) {
                    charset = "UTF-16BE";
                    checked = true;
                } else if (first3Bytes[0] == -17 && first3Bytes[1] == -69 && first3Bytes[2] == -65) {
                    charset = "UTF-8";
                    checked = true;
                }

                bis.reset();
                if (checked) {
                    return charset;
                }

                int var10 = 0;

                while(true) {
                    while((read = bis.read()) != -1) {
                        ++var10;
                        if (read >= 240 || 128 <= read && read <= 191) {
                            return charset;
                        }

                        if (192 <= read && read <= 223) {
                            read = bis.read();
                            if (128 > read || read > 191) {
                                return charset;
                            }
                        } else if (224 <= read && read <= 239) {
                            read = bis.read();
                            if (128 <= read && read <= 191) {
                                read = bis.read();
                                if (128 <= read && read <= 191) {
                                    charset = "UTF-8";
                                    return charset;
                                }
                            }

                            return charset;
                        }
                    }

                    return charset;
                }
            }

            var6 = charset;
        } finally {
            if (bis != null) {
                bis.close();
            }

        }

        return var6;
    }

    public static String getContent(InputStream... ises) throws IOException {
        if (ises == null) {
            return null;
        } else {
            StringBuilder result = new StringBuilder();
            InputStream[] var4 = ises;
            int var5 = ises.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                InputStream is = var4[var6];
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String line;
                while((line = br.readLine()) != null) {
                    result.append(line);
                }
            }

            return result.toString();
        }
    }

    public static String docToHtml(String path, String fileName) throws Exception {
        InputStream input = new FileInputStream(path + fileName);
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                String type = pictureType.name();
                return "data:image/" + type + ";base64," + new String(Base64.encodeBase64(content));
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty("encoding", "utf-8");
        serializer.setOutputProperty("indent", "yes");
        serializer.setOutputProperty("method", "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        String content = new String(outStream.toByteArray());
        return content;
    }

    public static String docxToHtml(String path,String fileName) throws IOException {
        File f = new File(path+fileName);
        File htmlF = new File(path+fileName.substring(0,fileName.lastIndexOf("."))+".htm");
        if (!f.exists()) {
            return "false";
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {

                // 1) Load DOCX into XWPFDocument
                InputStream in = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(in);

                // 2) Prepare XHTML options (here we set the IURIResolver to
                // load images from a "word/media" folder)
                File imageFolderFile = new File(path+fileName.substring(0,fileName.lastIndexOf(".")));
                XHTMLOptions options = XHTMLOptions.create().URIResolver(
                        new BasicURIResolver(path+fileName.substring(0,fileName.lastIndexOf("."))));
                options.setExtractor(new FileImageExtractor(imageFolderFile));
                //options.setIgnoreStylesIfUnused(false);
                //options.setFragment(true);

                // 3) Convert XWPFDocument to XHTML
                if (!htmlF.exists()){
                    htmlF.createNewFile();
                }
                OutputStream out = new FileOutputStream(htmlF);
                XHTMLConverter.getInstance().convert(document, out, options);
            } else {
                return "false";
            }
        }
        return path+fileName.substring(0,fileName.lastIndexOf("."))+".htm";
    }

    public static String excelToHtml(String path ,String fileName, boolean isWithStyle){
        InputStream is = null;
        String htmlExcel = null;
        String htmlPositon = path + fileName.substring(0,fileName.lastIndexOf("."))+".htm";
        try {
            File sourcefile = new File(path+fileName);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {   //03版excel处理方法
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                htmlExcel = getExcelInfo(xWb,isWithStyle);
            }else if(wb instanceof HSSFWorkbook){  //07及10版以后的excel处理方法
                HSSFWorkbook hWb = (HSSFWorkbook) wb;
                htmlExcel = getExcelInfo(hWb,isWithStyle);
            }
            writeFile(htmlExcel,htmlPositon);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlPositon;
    }

    private static void writeFile(String content,String htmlPath){
        File file2 = new File(htmlPath);
        StringBuilder sb = new StringBuilder();
        try {
            file2.createNewFile();//创建文件
            sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"><title>Html Test</title></head><body>");
            sb.append("<div>");
            sb.append(content);
            sb.append("</div>");
            sb.append("</body></html>");
            PrintStream printStream = new PrintStream(new FileOutputStream(file2));
            printStream.println(sb.toString());//将字符串写入文件
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getExcelInfo(Workbook wb, boolean isWithStyle) {

        StringBuffer sb = new StringBuffer();

        Sheet sheet = wb.getSheetAt(0);// 获取第一个Sheet的内容
        // 去掉表格中没有值的行，获取表格的实际的行数
        int lastRowNum = filterNullRow(sheet);
        Map<String, String> map[] = getRowSpanColSpanMap(sheet);
        sb.append("<table style='border-collapse:collapse;' width='100%'>");
        Row row = null; // 兼容
        Cell cell = null; // 兼容
        for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
            row = sheet.getRow(rowNum);
            if (row == null) {
                sb.append("<tr><td > &nbsp;</td></tr>");
                continue;
            }
            sb.append("<tr>");
            int lastColNum = row.getLastCellNum();
            for (int colNum = 0; colNum < lastColNum; colNum++) {
                cell = row.getCell(colNum);
                if (cell == null) { // 特殊情况 空白的单元格会返回null
                    sb.append("<td>&nbsp;</td>");
                    continue;
                }
                String stringValue = getCellValue(cell);
                // 如果是空值要进行计算判断是否有计算的结果值。
//                if (stringValue==null||"".equals(stringValue.trim())){
//                    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
//                    stringValue = evaluator.evaluate(cell).getNumberValue()+"";
//                }
                if (map[0].containsKey(rowNum + "," + colNum)) {
                    String pointString = map[0].get(rowNum + "," + colNum);
                    map[0].remove(rowNum + "," + colNum);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    int rowSpan = bottomeRow - rowNum + 1;
                    int colSpan = bottomeCol - colNum + 1;
                    sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
                } else if (map[1].containsKey(rowNum + "," + colNum)) {
                    map[1].remove(rowNum + "," + colNum);
                    continue;
                } else {
                    sb.append("<td ");
                }
                // 判断是否需要样式
                if (isWithStyle) {
                    dealExcelStyle(wb, sheet, cell, sb);// 处理单元格样式
                }
                sb.append(">");
                if (stringValue == null || "".equals(stringValue.trim())) {
                    sb.append(" &nbsp; ");
                } else {
                    // 将ascii码为160的空格转换为html下的空格（&nbsp;）
                    sb.append(stringValue.replace(String.valueOf((char) 160), "&nbsp;"));
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }

    private static void dealExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuffer sb) {

        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle != null) {
            short alignment = cellStyle.getAlignment();
            sb.append("align='" + convertAlignToHtml(alignment) + "' ");// 单元格内容的水平对齐方式
            short verticalAlignment = cellStyle.getVerticalAlignment();
            sb.append("valign='" + convertVerticalAlignToHtml(verticalAlignment) + "' ");// 单元格中内容的垂直排列方式
            if (wb instanceof XSSFWorkbook) {
                XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
                short boldWeight = xf.getBoldweight();
                sb.append("style='");
                sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
                sb.append("font-size: " + xf.getFontHeight() / 2 + "%;"); // 字体大小
                int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
                sb.append("width:" + columnWidth + "px;");
                XSSFColor xc = xf.getXSSFColor();
                if (xc != null && !"".equals(xc)) {
                    sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
                }

                XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
                if (bgColor != null && !"".equals(bgColor)) {
                    sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
                }
                sb.append(getBorderStyle(0, cellStyle.getBorderTop(),
                        ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
                sb.append(getBorderStyle(1, cellStyle.getBorderRight(),
                        ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
                sb.append(getBorderStyle(2, cellStyle.getBorderBottom(),
                        ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
                sb.append(getBorderStyle(3, cellStyle.getBorderLeft(),
                        ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));

            } else if (wb instanceof HSSFWorkbook) {

                HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
                short boldWeight = hf.getBoldweight();
                short fontColor = hf.getColor();
                sb.append("style='");
                HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
                HSSFColor hc = palette.getColor(fontColor);
                sb.append("font-weight:" + boldWeight + ";"); // 字体加粗
                sb.append("font-size: " + hf.getFontHeight() / 2 + "%;"); // 字体大小
                String fontColorStr = convertToStardColor(hc);
                if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
                    sb.append("color:" + fontColorStr + ";"); // 字体颜色
                }
                int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
                sb.append("width:" + columnWidth + "px;");
                short bgColor = cellStyle.getFillForegroundColor();
                hc = palette.getColor(bgColor);
                String bgColorStr = convertToStardColor(hc);
                if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
                    sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
                }
                sb.append(getBorderStyle(palette, 0, cellStyle.getBorderTop(), cellStyle.getTopBorderColor()));
                sb.append(getBorderStyle(palette, 1, cellStyle.getBorderRight(), cellStyle.getRightBorderColor()));
                sb.append(getBorderStyle(palette, 3, cellStyle.getBorderLeft(), cellStyle.getLeftBorderColor()));
                sb.append(getBorderStyle(palette, 2, cellStyle.getBorderBottom(), cellStyle.getBottomBorderColor()));
            }

            sb.append("' ");
        }
    }

    static String[] bordesr = { "border-top:", "border-right:", "border-bottom:", "border-left:" };
    static String[] borderStyles = { "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ",
            "solid ", "solid", "solid", "solid", "solid", "solid" };

    private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {
        if (s == 0)
            return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        String borderColorStr = convertToStardColor(palette.getColor(t));
        borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
        return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
    }

    private static String getBorderStyle(int b, short s, XSSFColor xc) {

        if (s == 0)
            return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        if (xc != null && !"".equals(xc)) {
            String borderColorStr = xc.getARGBHex();// t.getARGBHex();
            borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000"
                    : borderColorStr.substring(2);
            return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
        }
        return "";
    }


    private static String convertToStardColor(HSSFColor hc) {

        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
            }
        }

        return sb.toString();
    }

    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }

    /**
     * 单元格内容的水平对齐方式
     *
     * @param alignment
     * @return
     */
    private static String convertAlignToHtml(short alignment) {

        String align = "left";
        switch (alignment) {
            case CellStyle.ALIGN_LEFT:
                align = "left";
                break;
            case CellStyle.ALIGN_CENTER:
                align = "center";
                break;
            case CellStyle.ALIGN_RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }
    /**
     * 单元格中内容的垂直排列方式
     *
     * @param verticalAlignment
     * @return
     */
    private static String convertVerticalAlignToHtml(short verticalAlignment) {

        String valign = "middle";
        switch (verticalAlignment) {
            case CellStyle.VERTICAL_BOTTOM:
                valign = "bottom";
                break;
            case CellStyle.VERTICAL_CENTER:
                valign = "center";
                break;
            case CellStyle.VERTICAL_TOP:
                valign = "top";
                break;
            default:
                break;
        }
        return valign;
    }

    /**
     * 获取表格单元格Cell内容
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        String result = new String();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case Cell.CELL_TYPE_STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case Cell.CELL_TYPE_BLANK:
                result = "";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public static int filterNullRow(Sheet childSheet) {
        int rowNum = childSheet.getLastRowNum();
        int j = 1;
        // 判断末行，如果不为空，直接返回行数
        Row lastRow = childSheet.getRow(rowNum);
        if (!isNullRow(lastRow))
            return rowNum;
        // 如果末行为空，则进入循环，直到遇到不为空的为止
        for (int i = rowNum - 1; i > 0; i--) {
            Row row = childSheet.getRow(i);
            if (row == null || isNullRow(row)) {
                j++;
            } else {
                break;
            }
        }
        return rowNum - j;
    }

    public static boolean isNullRow(Row row) {
        if (row == null)
            return true;
        boolean nullFlag = true;
        for (int k = 0; k < row.getLastCellNum(); k++) {
            Cell cell = row.getCell((short) k);
            if (!"".equals(transferToString(cell))) {
                nullFlag = false;
                break;
            }
        }
        return nullFlag;
    }

    private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
        Map<String, String> map0 = new HashMap<String, String>();
        Map<String, String> map1 = new HashMap<String, String>();
        int mergedNum = sheet.getNumMergedRegions();
        CellRangeAddress range = null;
        for (int i = 0; i < mergedNum; i++) {
            range = sheet.getMergedRegion(i);
            int topRow = range.getFirstRow();
            int topCol = range.getFirstColumn();
            int bottomRow = range.getLastRow();
            int bottomCol = range.getLastColumn();
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        @SuppressWarnings("rawtypes")
        Map[] map = { map0, map1 };
        return map;
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.###");

    public static String transferToString(Cell cell) {
        String transferedStr = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是date类型则 ，获取该cell的date值
                    transferedStr = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
                } else { // 纯数字
                    double cellValue = cell.getNumericCellValue();
                    transferedStr = decimalFormat.format(cellValue);
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                transferedStr = cell.getStringCellValue() + "";
                break;
            default:
                transferedStr = "";
                break;
        }
        return transferedStr;
    }

    public static int getWordNumber(String fileName) throws Exception {
        String content = readfileMatch(fileName);
        content = content.replaceAll("\n", "");
        return content.length();
    }

    public static String readfileMatch(String fileName) throws Exception {
        ZipSecureFile.setMinInflateRatio(-1.0D);
        String content = "";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String var3 = suffix.toLowerCase();
        byte var4 = -1;
        switch(var3.hashCode()) {
            case 99640:
                if (var3.equals("doc")) {
                    var4 = 0;
                }
                break;
            case 110834:
                if (var3.equals("pdf")) {
                    var4 = 4;
                }
                break;
            case 115312:
                if (var3.equals("txt")) {
                    var4 = 5;
                }
                break;
            case 118783:
                if (var3.equals("xls")) {
                    var4 = 2;
                }
                break;
            case 3088960:
                if (var3.equals("docx")) {
                    var4 = 1;
                }
                break;
            case 3271912:
                if (var3.equals("json")) {
                    var4 = 6;
                }
                break;
            case 3682393:
                if (var3.equals("xlsx")) {
                    var4 = 3;
                }
        }

        switch(var4) {
            case 0:
                content = readDoc(fileName);
                break;
            case 1:
                content = readDocx(fileName);
                break;
            case 2:
                content = readXls(fileName);
                break;
            case 3:
                content = readXlsx(fileName);
                break;
            case 4:
                content = readPDF(fileName);
                break;
            case 5:
            case 6:
                content = readTXT(fileName);
        }

        if (StringUtils.isEmpty(content)) {
            log.warn("content is empty, fileName: [{}]", fileName);
        }

        return content;
    }

    public static void convert2TXT(String fileName) throws Exception {
        String text = readfileMatch(fileName);
        if (StringUtils.isEmpty(text)) {
            log.info("file : [{}] is empty.", fileName);
        }

        String txtFileName = FileUtil.getCustomFileName(fileName, "txt");
        FileUtil.save2TXTAndCheckCharset(txtFileName, text);
    }

}
