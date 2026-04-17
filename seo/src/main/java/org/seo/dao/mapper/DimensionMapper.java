package org.seo.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.seo.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DimensionMapper {

    List<Map<String, Object>> getMainPage(
            @Param("params") PageData pd);

    List<Map<String, Object>> getMainAll(@Param("params") PageData pd);

    List<Map<String, Object>> checkMain(@Param("params") PageData pd);

    void addMain(@Param("params") PageData pd);

    void editMain(@Param("params") PageData pd);

    void delMain(@Param("params") PageData pd);

    Integer countMain(@Param("params") PageData pd);

    List<Map<String, Object>> getSubPage(@Param("params") PageData pd);

    List<Map<String, Object>> getSubAll(@Param("params") PageData pd);

    List<Map<String, Object>> checkSub(@Param("params") PageData pd);

    void addSub(@Param("params") PageData pd);

    void editSub(@Param("params") PageData pd);

    void delSub(@Param("params") PageData pd);

    Integer countSub(@Param("params") PageData pd);
}
