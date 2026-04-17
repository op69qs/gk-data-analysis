// InspectionReportServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionReportMapper;
import org.inspect.service.InspectionReportService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/25.
 */
@Service
public class InspectionReportServiceImpl implements InspectionReportService {

    @Autowired
    private InspectionReportMapper inspectionReportMapper;

    /**
     * 获取检查项目
     * @return
     */
    public List<Map<String, String>> getInspectItemsByTypeId(PageData params){
        return inspectionReportMapper.getInspectItemsByTypeId(params);
    }

    /**
     * 获取检查组信息
     *
     * @return
     */
    public List<Map<String, String>> getGroupUsers(PageData pd) {
        return inspectionReportMapper.getGroupUsers(pd);
    }

    /**
     * 根据ID更新检查报告
     * @param pd
     */
    @Override
    public void updateReportById(PageData pd) {
        inspectionReportMapper.updateReportById(pd);
    }

    /**
     * 根据任务ID或者报告ID获取报告信息
     * @param pd
     * @return
     */
    @Override
    public Map<String, String> getReportById(PageData pd) {
        return inspectionReportMapper.getReportById(pd);
    }

    /**
     * 新增检查报告
     * @param pd
     */
    @Override
    public void addInspectionReport(PageData pd) {
        inspectionReportMapper.addInspectionReport(pd);
    }
} ///:~
