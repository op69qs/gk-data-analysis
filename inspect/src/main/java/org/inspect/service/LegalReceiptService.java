package org.inspect.service;


import org.inspect.util.PageData;

import java.util.Map;

public interface LegalReceiptService {

    Map<String,Object> getLegalReceiptData(PageData pd);

    void addLegalReceipt(PageData pd);

    void editLegalReceipt(PageData pd);

    void delLegalReceipt(PageData pd);
}
