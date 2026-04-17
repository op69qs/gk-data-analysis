package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionApprovalListMapper;
import org.inspect.dao.mapper.inspect.LegalNoticeMapper;
import org.inspect.service.InspectionApprovalListService;
import org.inspect.service.LegalNoticeService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LegalNoticeServiceImpl implements LegalNoticeService {

    @Autowired
    private LegalNoticeMapper legalNoticeMapper;

    @Override
    public Map<String, Object> getLegalNoticeData(PageData pd) {
        return legalNoticeMapper.getLegalNoticeData(pd);
    }

    @Override
    public void addLegalNotice(PageData pd) {
        legalNoticeMapper.addLegalNotice(pd);
    }

    @Override
    public void editLegalNotice(PageData pd) {
        legalNoticeMapper.editLegalNotice(pd);
    }

    @Override
    public void delLegalNotice(PageData pd) {
        legalNoticeMapper.delLegalNotice(pd);
    }
}
