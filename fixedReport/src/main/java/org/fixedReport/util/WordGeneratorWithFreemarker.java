package org.fixedReport.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
* @Description:word导出帮助类
* 通过freemarker模板引擎来实现
*   
*/
public class WordGeneratorWithFreemarker {  
    private static Configuration configuration = null;  
    private static Map<String, Template> allTemplates = null;
    /*块*/
    static {  
        configuration = new Configuration(Configuration.VERSION_2_3_23);  
        configuration.setDefaultEncoding("utf-8");  
        configuration.setClassicCompatible(true);
        /*获取模板文件路径，此处路径为项目resources文件夹下绝对路径*/
        configuration.setClassForTemplateLoading(WordGeneratorWithFreemarker.class, "/ftl");
        allTemplates = new HashMap<>();
        try {
            allTemplates.put("AI_ER_TEMP", configuration.getTemplate("AI_ER_TEMP.ftl"));
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        }  
    }  
  
    private WordGeneratorWithFreemarker() {}
  
    public static void createDoc(
            Map<String, Object> dataMap,
            String templateName,
            OutputStream out) throws IOException {

    	Template t = configuration.getTemplate(templateName);
    	WordHtmlGeneratorHelper.handleAllObject(dataMap);
        try {  
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
            Writer w = new OutputStreamWriter(out);  
            t.process(dataMap, w);  
            w.close();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            throw new RuntimeException(ex);  
        }  
        return ;  
    }
    
}  