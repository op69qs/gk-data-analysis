// IndustryService.java

package org.jeecg.modules.dimnsnSetting.service;


import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/7/1.
 */
public interface IndustryService {

    /**
     * 获取行业门类
     * @param params
     * @return
     */
    List<Map<String, Object>> getIndustryLv1(PageData params);

    /**
     * 获取行业大类
     * @param params
     * @return
     */
    List<Map<String, Object>> getIndustryLv2(PageData params);

}///:~
