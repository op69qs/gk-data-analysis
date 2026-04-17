// InspectionPostSVWorkDiaryService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.Map;

/**
 * 事后监督工作日志
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionPostSVWorkDiaryService {

    /**
     * 获取记录
     * @param params
     */
    Map<String, Object> getRecord(PageData params);

    /**
     * 修改记录
     * @param params
     */
    void editRecord(PageData params);

    /**
     * 新增记录
     * @param params
     */
    void addRecord(PageData params);
    

} ///:~
