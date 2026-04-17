// InspectionReportMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 数据核查
 * @author Created by Samer on 2019/10/25.
 */
public interface InspectionReceiptMapper {
    /**
     * 根据ID更新
     * @param params
     */
    void updateReceiptById(@Param(value = "params") PageData params);

    void addReceiptList(@Param(value = "params") PageData params);

    void delInspectionCheck(@Param(value = "params") PageData params);
    void skipInspection(@Param(value = "params") PageData params);

    List<Map<String, Object>> getInspectionCheck(@Param(value = "params") PageData params);

    List<Map<String, Object>> checkRepeat(@Param(value = "params") PageData params);

}
