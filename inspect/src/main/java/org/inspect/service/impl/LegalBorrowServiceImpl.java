package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.LegalBorrowMapper;
import org.inspect.service.LegalBorrowService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LegalBorrowServiceImpl implements LegalBorrowService {

    @Autowired
    private LegalBorrowMapper legalBorrowMapper;

    @Override
    public void editInspectProjectName(PageData pd) {
        legalBorrowMapper.editInspectProjectName(pd);
    }

    @Override
    public List<Map<String, Object>> getLegalBorrowData(PageData pd) {
        return legalBorrowMapper.getLegalBorrowData(pd);
    }

    @Override
    public void addLegalBorrow(PageData pd) {
        legalBorrowMapper.addLegalBorrow(pd);
    }

    @Override
    public void editLegalBorrow(PageData pd) {
        legalBorrowMapper.editLegalBorrow(pd);
    }

    @Override
    public void delLegalBorrow(PageData pd) {
        legalBorrowMapper.delLegalBorrow(pd);
    }
}
