package org.inspect.service;


import org.inspect.util.PageData;

import java.util.Map;

public interface LegalNoticeService {

    Map<String,Object> getLegalNoticeData(PageData pd);

    void addLegalNotice(PageData pd);

    void editLegalNotice(PageData pd);

    void delLegalNotice(PageData pd);
}
