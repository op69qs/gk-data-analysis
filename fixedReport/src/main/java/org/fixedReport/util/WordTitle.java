package org.fixedReport.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordTitle {
    /**
     * word整体样式
     */
    private static CTStyles wordStyles = null;

    /**
     * Word整体样式
     */
    static {
        XWPFDocument template;
        try {
            // 读取模板文档
            template = new XWPFDocument(new FileInputStream("E:/home/app/pbc-file/template/Word.docx"));
            // 获得模板文档的整体样式
            wordStyles = template.getStyle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        // 新建的word文档对象
        XWPFDocument doc = new XWPFDocument();
        // 获取新建文档对象的样式
        XWPFStyles newStyles = doc.createStyles();
        // 关键行// 修改设置文档样式为静态块中读取到的样式
        newStyles.setStyles(wordStyles);

        // 开始内容输入
        // 标题
        XWPFParagraph para0 = doc.createParagraph();
        // 关键行// 1级大纲
        para0.setStyle("a6");
        XWPFRun run0 = para0.createRun();
        // 标题内容
        run0.setText("标题");

        // 标题1，1级大纲
        XWPFParagraph para1 = doc.createParagraph();
        // 关键行// 1级大纲
        para1.setStyle("1");
        XWPFRun run1 = para1.createRun();
        // 标题内容
        run1.setText("标题 1");

        // 标题2
        XWPFParagraph para2 = doc.createParagraph();
        // 关键行// 2级大纲
        para2.setStyle("2");
        XWPFRun run2 = para2.createRun();
        // 标题内容
        run2.setText("标题 2");

        // 标题2
        XWPFParagraph para3 = doc.createParagraph();
        // 关键行// 2级大纲
        para3.setStyle("3");
        XWPFRun run3 = para3.createRun();
        // 标题内容
        run3.setText("标题 3");

        // 正文
        XWPFParagraph paraX = doc.createParagraph();
        XWPFRun runX = paraX.createRun();
        // 正文内容
        runX.setText("正文");

        // word写入到文件
        FileOutputStream fos = new FileOutputStream("E:/home/app/pbc-file/doc/Word.docx");
        doc.write(fos);
        fos.close();
    }

}
