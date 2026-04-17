/** 
 * Project Name:dwbi-smartreport 
 * File Name:FreeMarker_t.java 
 * Package Name:org.triber.smartreport.common 
 * Date:2018年2月6日上午9:11:47 
 * Copyright (c) 2018, https://github.com/shiyuren All Rights Reserved. 
 * 
*/  
  
package org.fixedReport.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/** 
 * ClassName:FreeMarker_t <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2018年2月6日 上午9:11:47 <br/> 
 * @author   wl 
 * @version   
 * @since    JDK 1.8
 * @see       
 */
public class FreeMarker_Util {
	private Configuration cfg =null;
	public void init() throws IOException {
		if(cfg==null) {
			cfg=new Configuration(Configuration.VERSION_2_3_23);
			cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/tpl/");
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(false);
		}
	}
	public void renderTpl(Map<String,Object> data,String fileName,HttpServletResponse response) {
		try {
			init();
			Template tpl=cfg.getTemplate(fileName);
			tpl.process(data, response.getWriter());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public void renderTpl(Map<String,Object> data, String fileName, FileWriter out) {
		try {
			init();
			Template tpl=cfg.getTemplate(fileName);
			tpl.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
 