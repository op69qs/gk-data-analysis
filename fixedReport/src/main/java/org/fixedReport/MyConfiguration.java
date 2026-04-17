// MyConfiguration.java

package org.fixedReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.fixedReport.service.mvcconfig.MvcConfigService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Created by Samer on 2019/9/16.
 */
@Configuration
public class MyConfiguration implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private MvcConfigService mvcConfigService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        Map<String, String> params = new HashMap<>();
        params.put("applicationName", applicationName);
        List<Map<String, String>> mvcMapperList = mvcConfigService.getMvcConfig(params);
        /*配置多个视图控制器，可做为菜单配置的跳转页面*/
        if( mvcMapperList == null ||  mvcMapperList.isEmpty()){
            return;
        }
        for( int i = 0; i < mvcMapperList.size(); i++ ){
            registry.addViewController(mvcMapperList.get(i).get("REQUEST")).setViewName(mvcMapperList.get(i).get("MAPPER"));
        }
    }


} ///:~
