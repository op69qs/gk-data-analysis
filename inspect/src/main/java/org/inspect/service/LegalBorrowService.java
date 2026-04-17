package org.inspect.service;


import org.inspect.model.BorrowData;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface LegalBorrowService {

    void editInspectProjectName(PageData pd);

    List<Map<String,Object>> getLegalBorrowData(PageData pd);

    void addLegalBorrow(PageData pd);

    void editLegalBorrow(PageData pd);

    void delLegalBorrow(PageData pd);
}
