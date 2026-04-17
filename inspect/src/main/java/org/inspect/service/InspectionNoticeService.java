package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionNoticeService {

    Map<String,Object> getInspectionNoticeData(PageData pd);

    void addInspectionNotice(PageData pd);

    void editInspectionNotice(PageData pd);

    void editNoticeUser(PageData pd);

    void delInspectionNotice(PageData pd);
}
