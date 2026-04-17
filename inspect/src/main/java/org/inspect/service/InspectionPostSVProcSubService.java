package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionPostSVProcSubService {

    List<Map<String,Object>> getInspectionProcSubData(PageData pd);

}
