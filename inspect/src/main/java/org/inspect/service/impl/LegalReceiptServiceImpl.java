package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.LegalNoticeMapper;
import org.inspect.dao.mapper.inspect.LegalReceiptMapper;
import org.inspect.service.LegalNoticeService;
import org.inspect.service.LegalReceiptService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LegalReceiptServiceImpl implements LegalReceiptService {

    @Autowired
    private LegalReceiptMapper legalReceiptMapper;


    @Override
    public Map<String, Object> getLegalReceiptData(PageData pd) {
        return legalReceiptMapper.getLegalReceiptData(pd);
    }

    @Override
    public void addLegalReceipt(PageData pd) {
        legalReceiptMapper.addLegalReceipt(pd);
    }

    @Override
    public void editLegalReceipt(PageData pd) {
        legalReceiptMapper.editLegalReceipt(pd);
    }

    @Override
    public void delLegalReceipt(PageData pd) {
        legalReceiptMapper.delLegalReceipt(pd);
    }
}
