package org.inspect.service;


import org.inspect.model.BorrowData;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionBorrowService {

    List<Map<String,Object>> getInspectionBorrowData(PageData pd);

    List<Map<String,Object>> getInspectionBorrowTemp(PageData pd);

    void addInspectionBorrow(PageData pd);

    void editInspectionBorrow(PageData pd);

    void editBorrowUser(PageData pd);

    void editBorrowCharge(PageData pd);

    void delInspectionBorrow(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);

    List<BorrowData> getInspectionBorrow(PageData pd);

    List<Map<String,Object>> getEnum(PageData pd);
}
