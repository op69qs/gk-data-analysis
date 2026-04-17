// InspectionEvidenceRecordService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 执法检查取证记录
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionEvidenceRecordService {

    /**
     * 获取主表信息
     * @param params
     */
    List<Map<String, Object>> getEvidenceRecordMainInfo(PageData params);


    /**
     * 获取取证记录子表
     * @param params
     */
    List<Map<String, Object>> getEvidenceRecordSheet(PageData params);

    /**
     * 获取取证记录
     * @param params
     */
    List<Map<String, Object>> getEvidenceRecord(PageData params);

    /**
     * 修改取证记录
     * @param params
     */
    void editEvidenceRecord(PageData params);

    /**
     * 新增取证记录
     * @param params
     */
    void addEvidenceRecord(PageData params);

    /**
     * 删除取证记录
     * @param params
     */
    void deleteEvidenceRecord(PageData params);

    /**
     * 新增取证记录子表
     * @param params
     */
    void addEvidenceRecordSheet(PageData params);

    /**
     * 新增取证记录
     * @param params
     */
    void editEvidenceRecordSheet(PageData params);
    

} ///:~
