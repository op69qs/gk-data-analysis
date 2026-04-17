package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionPostSVProcService {

    List<Map<String,Object>> getInspectionProcData(PageData pd);

}
