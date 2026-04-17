package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionApprovalListMapper;
import org.inspect.dao.mapper.inspect.InspectionNoticeMapper;
import org.inspect.dao.mapper.inspect.InspectionUserMapper;
import org.inspect.service.InspectionApprovalListService;
import org.inspect.service.InspectionNoticeService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionApprovalListServiceImpl implements InspectionApprovalListService {

    @Autowired
    private InspectionApprovalListMapper inspectionApprovalListMapper;

    @Override
    public Map<String, Object> getApprovalListData(PageData pd) {
        return inspectionApprovalListMapper.getApprovalListData(pd);
    }

    @Override
    public void addApprovalList(PageData pd) {
        inspectionApprovalListMapper.addApprovalList(pd);
    }

    @Override
    public void editApprovalList(PageData pd) {
        inspectionApprovalListMapper.editApprovalList(pd);
    }

    @Override
    public void delApprovalList(PageData pd) {
        inspectionApprovalListMapper.delApprovalList(pd);
    }
}
