package org.triber.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:45
 * @Description 综合查询
 */
public interface AlarmDetailsService {

    /**
     * @Author haojiang
     * @Date 2020/11/18 11:25
     * @Description 获取监测告警明细表数据total
     */
    int getAlarmDetailsDataTotal(Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/12 09:45
     * @Description 获取综合查询数据
     */
    List<Map<String, Object>> getAlarmDetailsData(Map<String, Object> map);
}
