// MvcConfigService.java

package org.fixedReport.service.mvcconfig;

import java.util.List;
import java.util.Map;

/**
 * 静态页面控制
 * @author Created by Samer on 2019/9/23.
 */
public interface MvcConfigService {

    List<Map<String, String>> getMvcConfig(Map<String, String> params);

}///:~
