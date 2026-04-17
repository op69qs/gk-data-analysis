package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionContentService {

    List<Map<String,Object>> getContentData(PageData pd);

    void addContent(PageData pd);

    void editContent(PageData pd);

    void delContent(PageData pd);
}
