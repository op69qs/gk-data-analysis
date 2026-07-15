package org.jeecg.modules.enumSetting.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface ErrorLogService {

    List<Map<String, Object>> getData(PageData pd);

    void callProc(PageData pd);

    Integer getCount(PageData pd);

    void add(PageData pd);

    void edit(PageData pd);

    void del(PageData pd);
}
