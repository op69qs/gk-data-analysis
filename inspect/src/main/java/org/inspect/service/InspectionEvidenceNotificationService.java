// InspectionEvidenceNotificationService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 执法检查取证记录通知书
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionEvidenceNotificationService {

    /**
     * 获取子表记录
     * @param params
     */
    List<Map<String, Object>> getRecordSub(PageData params);

    /**
     * 获取记录
     * @param params
     */
    List<Map<String, Object>> getRecord( PageData params);

    /**
     * 修改记录
     * @param params
     */
    void editRecord( PageData params);

    /**
     * 新增记录
     * @param params
     */
    void addRecord( PageData params);
    

} ///:~
