package org.seo.service;

import org.seo.util.PageData;
import java.util.List;
import java.util.Map;

public interface DataSourceService {

    List<Map<String,Object>> getDataSourcePage(PageData pd);

    List<Map<String,Object>> getDataSource(PageData pd);

    void addDataSource(PageData pd);

    void delDataSource(PageData pd);

    void editDataSource(PageData pd);

    Integer countDataSource(PageData pd);

    List<Map<String,Object>> getDataSourceEnum(PageData pd);

    List<Map<String,Object>> getDataSourceEnumSelect();

    List<Map<String,Object>> getDataBase(PageData pd);

    void addDataBase(PageData pd);

    void delDataBase(PageData pd);

    List<Map<String,Object>> getDataSourceName(PageData pd);
}
