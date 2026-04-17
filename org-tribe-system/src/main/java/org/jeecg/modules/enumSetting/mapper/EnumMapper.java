package org.jeecg.modules.enumSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EnumMapper {

    List<Map<String, Object>> getData(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    List<Map<String, Object>> checkCode(@Param("params") PageData pd);

    /**
     * 查询逻辑运算符号
     *
     * @return
     */
    List<Map<String, Object>> getLogicalOperator();

    void addEnum(@Param("params") PageData pd);

    void editEnum(@Param("params") PageData pd);

    void delEnum(@Param("params") PageData pd);

    void delEnumNo(@Param("params") PageData pd);

    List<Map<String, Object>> getEnumType(@Param("params") PageData pd);

    List<Map<String, Object>> getEnumTypeAll(@Param("params") PageData pd);

    List<Map<String, Object>> getFirst(@Param("params") PageData pd);

    List<Map<String, Object>> getSecond(@Param("params") PageData pd);

    List<Map<String, Object>> getThird(@Param("params") PageData pd);

    String getEnumDscr(@Param("params") PageData pd);

}
