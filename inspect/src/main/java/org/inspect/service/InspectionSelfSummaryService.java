// InspectionSelfSummaryService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 自查汇总表
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionSelfSummaryService {


    /**
     * 查询当前任务汇总表信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getCurSelfSumInfo(PageData params);

    /**
     * 根据问题台账ID编辑
     * @param params
     * @return
     */
    void editSelfLedgerByLedgerID(Map<String, String> params);

    /**
     * 获取国库信息
     * @param params
     * @return
     */
    Map<String, String> getGuokuInfo(Map<String, String> params);

} ///:~
