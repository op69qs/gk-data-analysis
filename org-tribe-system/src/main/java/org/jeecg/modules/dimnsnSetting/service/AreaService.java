package org.jeecg.modules.dimnsnSetting.service;



import org.jeecg.modules.dimnsnSetting.model.TreeAreaNode;
import org.jeecg.modules.util.PageData;


import java.util.List;
import java.util.Map;

public interface AreaService {
    List<TreeAreaNode> getAreaList(PageData pd);
    List<Map<String,Object>> getArea(PageData pd);
    List<Map<String,Object>> checkCode(PageData pd);
    void addArea(PageData pd);
    void editArea(PageData pd);
    void delArea(PageData pd);
    Integer isDelArea(PageData pd);
    void updateAreaPid(PageData pd);
    void delAreaParent(PageData pd);




}
