package org.seo.service;

import org.seo.util.PageData;
import java.util.List;
import java.util.Map;

public interface ForSkipService {

    List<Map<String, Object>> getFileList(PageData pd,String dataSource_id);
}
