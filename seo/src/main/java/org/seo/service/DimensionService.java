package org.seo.service;

import org.apache.ibatis.annotations.Param;
import org.seo.util.PageData;

import java.util.List;
import java.util.Map;

public interface DimensionService {

    List<Map<String, Object>> getMainPage(PageData pd);

    List<Map<String, Object>> getMainAll(PageData pd);

    List<Map<String, Object>> checkMain(PageData pd);

    void addMain(PageData pd, String dataSource_id);

    void editMain(PageData pd, String dataSource_id);

    void delMain(PageData pd, String dataSource_id);

    Integer countMain(PageData pd);

    List<Map<String, Object>> getSubPage(PageData pd);

    List<Map<String, Object>> getSubAll(PageData pd);

    List<Map<String, Object>> checkSub(PageData pd);

    void addSub(PageData pd, String dataSource_id);

    void editSub(PageData pd, String dataSource_id);

    void delSub(PageData pd, String dataSource_id);
    Integer countSub(PageData pd);
}
