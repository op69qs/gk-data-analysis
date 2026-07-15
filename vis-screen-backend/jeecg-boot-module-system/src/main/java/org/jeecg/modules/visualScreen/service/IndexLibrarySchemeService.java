package org.jeecg.modules.visualScreen.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface IndexLibrarySchemeService {

    List<Map<String, Object>> getPage(PageData pd);

    Integer getCount(PageData pd);

    Map<String, Object> getById(PageData pd);

    void del(PageData pd);

    void toGallery(PageData pd);
}
