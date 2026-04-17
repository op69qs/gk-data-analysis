// DocFileFormatPages.java

package org.fixedReport.util;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DOC文档格式化
 *
 * @author Created by Samer on 2019/11/29.
 */
public class DocFileFormatPages {

    /**
     * @param fileContentPath 内容文本模板全路径
     * @param fileEndPath     结束文本模板全路径
     * @param fileSourcePath  文本内容资源全路径
     * @param targetFilePath  输出目标文件全路径
     * @param pageLines       每页最大行
     * @param lineWords       每行最大字数
     * @param pd              PageData参数
     * @throws Exception
     */
    public static void DocFileFormat(
            String fileContentPath,
            String fileEndPath,
            String fileSourcePath,
            String targetFilePath,
            int pageLines,
            int lineWords,
            PageData pd
    ) throws Exception {
        List<XWPFDocument> xwpfDocuments = new ArrayList<XWPFDocument>();
        List<String> textListArr = new ArrayList<String>();
        String path = fileContentPath;
        String path_2 = fileEndPath;
        XWPFDocument docx = new XWPFDocument(new FileInputStream(fileSourcePath));
        XWPFTable table0 = docx.getTables().get(0);
        XWPFTableRow row = table0.getRow(4);
        XWPFTableCell cell = row.getTableCells().get(0);
        String textxx = cell.getText();
        String text = cell.getText()
                .replace("\n", "")
                .replace("<w:br/>", "")
                .replace("二、其他关注事项", "　　二、其他关注事项");
        String[] textArr = text.split("　　");
        for (int i = 0; i < textArr.length; i++) {
            String ss = textArr[i];
            if (!ss.contains("一、发现的问题") && !ss.contains("二、其他关注事项")){
                ss = "　　" + ss;
            }
            splitText(ss, 0, lineWords, textListArr);
        }
        int textListArrLength = textListArr.size();
        System.out.println(textListArr.size());
        int NUMPAGES = textListArrLength / pageLines;
        if ((textListArrLength - NUMPAGES * pageLines) <= pageLines) {
            NUMPAGES += 1;
        }
        pd.put("NUMPAGES", NUMPAGES);
        for (int i = 1; i <= NUMPAGES; i++) {
            XWPFDocument template = null;
            pd.put("PAGE", i);
            String content = "";
            for (int j = (i - 1) * pageLines; j < i * pageLines; j++) {
                if (j == textListArr.size()) {
                    break;
                }
                content += textListArr.get(j) + "\n";
            }
            pd.put("CONTENT", content);
            if (i == NUMPAGES) {
                template = new XWPFDocument(new FileInputStream(path_2));
            } else {
                template = new XWPFDocument(new FileInputStream(path));
            }
            BokeWordUtils.changeText(template, pd);
            BokeWordUtils.eachTable(template.getTables().get(0).getRows(), pd);
            xwpfDocuments.add(template);
        }
        XWPFDocument xwpfDocument = xwpfDocuments.get(0);
        for (int i = 1; i < xwpfDocuments.size(); i++) {
            xwpfDocument = mergeWord(xwpfDocument, xwpfDocuments.get(i));
        }
        FileUpload.writeToFile(xwpfDocument, targetFilePath);
        System.out.println("done");
    }


    /*文本合并*/
    public static XWPFDocument mergeWord(XWPFDocument document, XWPFDocument doucDocument2) throws Exception {
        XWPFDocument src1Document = document;
        XWPFParagraph p = src1Document.createParagraph();
        /*p.setPageBreak(true);*/
        CTBody src1Body = src1Document.getDocument().getBody();
        XWPFDocument src2Document = doucDocument2;
        CTBody src2Body = src2Document.getDocument().getBody();
        /*XWPFParagraph p2 = src2Document.createParagraph();*/
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = src2Body.xmlText(optionsOuter);
        String srcString = src1Body.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);
        src1Body.set(makeBody);
        return src1Document;
    }

    /*文本分割*/
    public static void splitText(String text, int start, int charLength, List<String> textListArr) {
        if (text.length() >= charLength) {
            String popString = text.substring(start, start + charLength - 1);
            String surplus = text.substring(start + charLength -1, text.length());
            textListArr.add(popString);
            splitText(surplus, start, charLength, textListArr);
        } else {
            if (text != null
                    && !"".equals(text)
                    && !"<w:br/>".equals(text)
                    ) {
                textListArr.add(text);
            }
        }
    }

} ///:~
