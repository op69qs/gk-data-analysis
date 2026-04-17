package org.jeecg.modules.enumSetting.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface EnumService {
    List<Map<String, Object>> getData(PageData pd);

    Integer getCount(PageData pd);

    List<Map<String, Object>> checkCode(PageData pd);

    /**
     * 查询逻辑运算符号
     *
     * @return
     */
    List<Map<String, Object>> getLogicalOperator();

    void addEnum(PageData pd);

    void editEnum(PageData pd);

    void delEnum(PageData pd);

    void delEnumNo(PageData pd);

    List<Map<String, Object>> getEnumType(PageData pd);

    List<Map<String, Object>> getEnumTypeAll(PageData pd);

    List<Map<String, Object>> getFirst(PageData pd);

    List<Map<String, Object>> getSecond(PageData pd);

    List<Map<String, Object>> getThird(PageData pd);

    String getEnumDscr(PageData pd);

}
