package org.seo.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.List;


/**
 * docx转html处理
 */

public class OfficeDocxUtils {


    //docx转换html

    public static String docxToHtml(String fileName) throws IOException {
        File htmlF = new File(fileName.substring(0,fileName.lastIndexOf("."))+".htm");
        XWPFDocument document = new XWPFDocument(new FileInputStream(fileName));
        List<XWPFPictureData> list = document.getAllPictures();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, outputStream, null);

        String s = new String(outputStream.toByteArray());
        s = setImg(s, list);
        if (!htmlF.exists()){
            htmlF.createNewFile();
        }
        FileWriter fw= new FileWriter(htmlF);
        fw.write(s);
        fw.flush();
        fw.close();
        return fileName.substring(0,fileName.lastIndexOf("."))+".htm";
    }

    private static String setImg(String html, List<XWPFPictureData> list){
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("img");
        if (elements != null && elements.size() > 0 && list != null){
            for(Element element : elements){
                String src = element.attr("src");
                for (XWPFPictureData data: list){
                    if (src.contains(data.getFileName())){
                        String type = src.substring(src.lastIndexOf(".") + 1);
                        String base64 = "data:image/" + type + ";base64," + new String(Base64.encodeBase64(data.getData()));
                        element.attr("src", base64);
                        break;
                    }
                }
            }
        }

        return doc.toString();
    }

}