package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionApprovalMapper;
import org.inspect.service.InspectionApprovalService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionApprovalServiceImpl implements InspectionApprovalService {

    @Autowired
    private InspectionApprovalMapper inspectionApprovalMapper;

    @Override
    public List<Map<String, Object>> getInspectionApprovalPage(PageData pd) {
        return inspectionApprovalMapper.getInspectionApprovalPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionApprovalData(PageData pd) {
        return inspectionApprovalMapper.getInspectionApprovalData(pd);
    }

    @Override
    public List<Map<String, Object>> getAppravalProcess(PageData pd) {
        return inspectionApprovalMapper.getAppravalProcess(pd);
    }

    @Override
    public Integer getInspectionApprovalCount(PageData pd) {
        return inspectionApprovalMapper.getInspectionApprovalCount(pd);
    }

    @Override
    public void addInspectionApproval(PageData pd) {
        inspectionApprovalMapper.addInspectionApproval(pd);
    }

    @Override
    public void editInspectionApproval(PageData pd) {
        inspectionApprovalMapper.editInspectionApproval(pd);
    }

    @Override
    public void delInspectionApproval(PageData pd) {
        inspectionApprovalMapper.delInspectionApproval(pd);
    }

    @Override
    public void updateApproval(PageData pd) {
        inspectionApprovalMapper.updateApproval(pd);
    }
}
