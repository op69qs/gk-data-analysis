package org.jeecg.modules.dimnsnSetting.service;

import org.jeecg.modules.dimnsnSetting.model.LevyingBodies;
import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface LevyingBodiesService {

    List<Map<String,Object>> getPage(PageData pd);

    Integer getCount(PageData pd);

    List<Map<String,Object>> getAll(PageData pd);

    List<LevyingBodies>getExport(PageData pd);

    List<Map<String,Object>>checkRepeat(PageData pd);

    void add(PageData pd);

    void edit(PageData pd);

    void del(PageData pd);
}
