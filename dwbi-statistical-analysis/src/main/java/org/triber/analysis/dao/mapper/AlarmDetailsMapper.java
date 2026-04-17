package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/10/12 9:48
 * @Description 综合查询
 */
@Repository
public interface AlarmDetailsMapper {

    /**
     * @Author haojiang
     * @Date 2020/11/18 11:25
     * @Description 获取监测告警明细表数据total
     */
    int getAlarmDetailsDataTotal(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/10/12 10:10
     * @Description 获取监测告警明细表数据
     */
    List<Map<String, Object>> getAlarmDetailsData(@Param("params") Map<String, Object> map);
}
