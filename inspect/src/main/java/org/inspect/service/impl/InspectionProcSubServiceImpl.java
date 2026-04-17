package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionProcMapper;
import org.inspect.dao.mapper.inspect.InspectionProcSubMapper;
import org.inspect.service.InspectionProcService;
import org.inspect.service.InspectionProcSubService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionProcSubServiceImpl implements InspectionProcSubService {

    @Autowired
    private InspectionProcSubMapper inspectionProcSubMapper;
    @Autowired
    private InspectionProcMapper inspectionProcMapper;


    @Override
    public List<Map<String, Object>> getInspectionProcSubData(PageData pd) {
        return inspectionProcSubMapper.getInspectionProcSubData(pd);
    }

    @Override
    public void addInspectionProcSub(PageData pd) {
        inspectionProcSubMapper.addInspectionProcSub(pd);
    }

    @Override
    public void editInspectionProcSub(PageData pd) {
        inspectionProcSubMapper.editInspectionProcSub(pd);
        List<Map<String, Object>> subMap = getInspectionProcSubData(pd);
        if (null != subMap && !subMap.isEmpty()){
            PageData proPd = new PageData();
            proPd.put("PROCESS_ID",subMap.get(0).get("PROCESS_ID"));
            proPd.put("INSPECTION_PROCESS_SUB_SIGN","1");
            List<Map<String, Object>> map = getInspectionProcSubData(proPd);
            if (null == map || map.isEmpty()){
                proPd.put("ID",proPd.get("PROCESS_ID"));
                proPd.put("INSPECTION_PROCESS_SIGN","0");
                proPd.put("FINISH_TIME", DateUtil. getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionProcMapper.editInspectionProc(proPd);
            }
        }
    }

    @Override
    public void editPostSVInspectionProcSub(PageData pd) {
        inspectionProcSubMapper.editPostSVInspectionProcSub(pd);
    }

    @Override
    public void editProcBySubProc(PageData pd) {
        inspectionProcSubMapper.editProcBySubProc(pd);
    }
}
