// inspectionRoutinePeriodServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionRoutinePeriodMapper;
import org.inspect.service.InspectionRoutinePeriodService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/2/24.
 */
@Service
public class InspectionRoutinePeriodServiceImpl implements InspectionRoutinePeriodService {

    @Autowired
    private InspectionRoutinePeriodMapper inspectionRoutinePeriodMapper;

    /**
     * 获取例行检查类型与周期记录
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getRoutinePeriodInfo(PageData params) {
        return inspectionRoutinePeriodMapper.getRoutinePeriodInfo(params);
    }

    /**
     * 根据检查类型删除周期
     * @param params
     * @return
     */
    public void delRoutinePeriodByTypeId(PageData params){
        inspectionRoutinePeriodMapper.delRoutinePeriodByTypeId(params);
    }


    /**
     * 新增检查类型周期
     * @param params
     * @return
     */
    public void insertRoutinePeriod(PageData params){
        inspectionRoutinePeriodMapper.insertRoutinePeriod(params);
    }

} ///:~
