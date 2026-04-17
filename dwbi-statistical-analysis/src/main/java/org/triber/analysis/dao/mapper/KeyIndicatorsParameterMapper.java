package org.triber.analysis.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author haojiang.
 * @Ddate 2020/9/29 15:39
 * @Description
 */
@Repository
public interface KeyIndicatorsParameterMapper {

    /**
     * @Author haojiang
     * @Date 2020/11/18 13:55
     * @Description 获取重点指标维护参数条数
     */
    int getKeyIndicatorsDataTotal(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/29 15:40
     * @Description 获取重点指标维护参数
     */
    List<Map<String, Object>> getKeyIndicatorsData(@Param("params") Map<String, Object> map);

    /**
     * @Author haojiang
     * @Date 2020/9/29 15:40
     * @Description 修改重点指标维护参数
     */
    void updateKeyIndicatorsData(@Param("params") Map<String, Object> map);
}
