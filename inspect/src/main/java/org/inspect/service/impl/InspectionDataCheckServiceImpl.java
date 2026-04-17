// InspectionReportServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionDataCheckMapper;
import org.inspect.service.InspectionDataCheckService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/25.
 */
@Service
public class InspectionDataCheckServiceImpl implements InspectionDataCheckService {

    @Autowired
    private InspectionDataCheckMapper inspectionDataCheckMapper;





    /**
     * 根据ID更新检查报告
     * @param pd
     */
    @Override
    public void updateDataCheckById(PageData pd) {
        inspectionDataCheckMapper.updateDataCheckById(pd);
    }

    /**
     * 根据任务ID或者报告ID获取报告信息
     * @param pd
     * @return
     */
    @Override
    public Map<String, String> getDataCheckById(PageData pd) {
        return inspectionDataCheckMapper.getDataCheckById(pd);
    }

    /**
     * 新增检查报告
     * @param pd
     */
    @Override
    public void addInspectionDataCheck(PageData pd) {
        inspectionDataCheckMapper.addInspectionDataCheck(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionCheck(PageData pd) {
        return inspectionDataCheckMapper.getInspectionCheck(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionCheckInspected(PageData pd) {
        return  inspectionDataCheckMapper.getInspectionCheckInspected(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionDataCheckMapper.checkRepeat(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionCheckOne(PageData pd) {
        return inspectionDataCheckMapper.getInspectionCheckOne(pd);
    }

    @Override
    public void delInspectionCheck(PageData pd) {
        inspectionDataCheckMapper.delInspectionCheck(pd);
    }

    @Override
    public int getInspectionCheckCount(PageData pd) {
        return inspectionDataCheckMapper.getInspectionCheckCount(pd);
    }
} ///:~
