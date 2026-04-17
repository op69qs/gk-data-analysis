package org.seo.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class Xml2XmlDoc {

    /**
     * 将xml模板转换为后缀为doc文件，本质仍是属于xml
     * @param dataMap	需要填充到模板的数据
     * @param templetFilePath	模板文件路径
     * @param targetFilePath	目标文件保存路径
     * @throws IOException
     * @throws TemplateException
     */
    public static void xml2XmlDoc(Map<String,Object> dataMap, String templetFilePath, String targetFilePath) throws IOException, TemplateException{
        // 将模板文件路径拆分为文件夹路径和文件名称
        String tempLetDir = templetFilePath.substring(0,templetFilePath.lastIndexOf("/"));
        // 注意：templetFilePath.lastIndexOf("/")中，有的文件分隔符为：\ 要注意文件路径的分隔符
        String templetName = templetFilePath.substring(templetFilePath.lastIndexOf("/")+1);
        // 将目标文件保存路径拆分为文件夹路径和文件名称
        String targetDir = targetFilePath.substring(0,targetFilePath.lastIndexOf("/"));
        String targetName = targetFilePath.substring(targetFilePath.lastIndexOf("/")+1);
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        // 如果目标文件目录不存在，则需要创建
        File file = new File(targetDir);
        if(!file.exists()){
            file.mkdirs();
        }
        // 加载模板数据（从文件路径中获取文件，其他方式，可百度查找）
        configuration.setDirectoryForTemplateLoading(new File(tempLetDir));
        // 获取模板实例
        Template template = configuration.getTemplate(templetName);
        File outFile = new File(targetDir + File.separator + targetName);
        //将模板和数据模型合并生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
        //生成文件
        try{
            template.process(dataMap, out);
            out.flush();
        } catch ( Exception e ){
            e.printStackTrace();
            new IOException(e);
        } finally{
            out.close();
        }
    }
}
