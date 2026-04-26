package org.jeecg.modules.visualScreen.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageSub;

import java.util.List;
import java.util.Map;

public interface PageSubService {

    List<PageSub> getPageSub(PageData pd);

    List<Map<String, Object>> getPage(PageData pd);

    Integer getCount(PageData pd);

    List<Map<String, Object>> getAll(PageData pd);

    void add(PageData pd);

    void edit(PageData pd);

    void del(PageData pd);
}
