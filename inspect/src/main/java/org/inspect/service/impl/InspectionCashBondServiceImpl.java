// InspectionReportServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionCashBondMapper;
import org.inspect.service.InspectionCashBondService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/25.
 */
@Service
public class InspectionCashBondServiceImpl implements InspectionCashBondService {

    @Autowired
    private InspectionCashBondMapper inspectionCashBondMapper;

    /**
     * 根据ID更新
     * @param pd
     */
    @Override
    public void updateCashBondById(PageData pd) {
        inspectionCashBondMapper.updateCashBondById(pd);
    }
    /**
     * 根据任务ID或者报告ID获取信息
     * @param pd
     * @return
     */
    @Override
    public Map<String, String> getCashBondById(PageData pd) {
        return inspectionCashBondMapper.getCashBondById(pd);
    }

    /**
     * 新增
     * @param pd
     */
    @Override
    public void addInspectionCashBond(PageData pd) {
        inspectionCashBondMapper.addInspectionCashBond(pd);
    }


    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionCashBondMapper.checkRepeat(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionCheck(PageData pd) {
        return inspectionCashBondMapper.getInspectionCheck(pd);
    }

    @Override
    public void delInspectionCashBond(PageData pd) {
        inspectionCashBondMapper.delInspectionCashBond(pd);
    }


} ///:~
