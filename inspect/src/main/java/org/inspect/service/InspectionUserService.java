package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionUserService {

    List<Map<String,Object>> getInspectionUserPage(PageData pd);

    List<Map<String,Object>> getInspectionUserData(PageData pd);

    List<Map<String,Object>> getUserData(PageData pd);

    Map<String,Object> getUserBySysId(PageData pd);

    List<Map<String,Object>> getRoleBySysId(PageData pd);

    Integer getInspectionUserCount(PageData pd);

    void addInspectionUser(PageData pd);

    void editInspectionUser(PageData pd);

    void delInspectionUser(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);

    String getDuties(PageData pd);
}
