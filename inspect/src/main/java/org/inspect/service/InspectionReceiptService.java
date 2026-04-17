package org.inspect.service;

import org.inspect.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 国家债券收款单现场检查账实核对表
 * @author Created by dj on 2019/11/15.
 */
@Service
public interface InspectionReceiptService {

    /**
     * 根据ID更新
     * @param pd
     */
    void updateReceiptById(PageData pd);

    void addReceiptList(PageData pd);

    List<Map<String, Object>> getInspectionCheck(PageData pd);
    List<Map<String,Object>> checkRepeat(PageData pd);
    void delInspectionCheck(PageData pd);
    void skipInspection(PageData pd);

}
