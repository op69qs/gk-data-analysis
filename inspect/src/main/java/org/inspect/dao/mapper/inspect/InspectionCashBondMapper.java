package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 *  已兑付国家债券现场检查账实核对表
 * @author Created by dj on 2019/11/15.
 */
public interface InspectionCashBondMapper {


    /**
     * 根据任务ID或者数据核查ID获取数据核查
     * @return
     */
    Map<String, String> getCashBondById(@Param(value = "params") PageData params);

    /**
     * 根据ID更新数据核查
     * @param params
     */
    void updateCashBondById(@Param(value = "params") PageData params);


    /**
     * 新增
     * @param params
     */
    void addInspectionCashBond(@Param(value = "params") PageData params);
    void delInspectionCashBond(@Param(value = "params") PageData params);

    List<Map<String, Object>> getInspectionCheck(@Param(value = "params") PageData params);
    List<Map<String, Object>> checkRepeat(@Param(value = "params") PageData params);

}
