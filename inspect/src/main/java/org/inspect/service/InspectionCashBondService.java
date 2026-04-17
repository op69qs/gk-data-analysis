
package org.inspect.service;

import org.inspect.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据核查
 * @author Created by dj on 2019/11/15
 */
@Service
public interface InspectionCashBondService {

    /**
     * 根据ID更新
     * @param pd
     */
    void updateCashBondById(PageData pd);

    /**
     * 根据任务ID或者核查数据ID获取信息
     * @return
     */
    Map<String, String> getCashBondById(PageData pd);

    /**
     * 已兑付国家债券现场检查账实核对表
     * @param pd
     */
    void addInspectionCashBond(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);
    List<Map<String,Object>> getInspectionCheck(PageData pd);
    void delInspectionCashBond(PageData pd);

}
