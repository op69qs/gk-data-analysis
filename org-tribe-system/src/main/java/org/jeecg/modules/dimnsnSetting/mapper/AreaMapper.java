package org.jeecg.modules.dimnsnSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.dimnsnSetting.model.TreeAreaNode;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AreaMapper {

    List<Map<String,Object>> getArea(@Param("params") PageData pd);
    List<TreeAreaNode> getAreaList(@Param("params") PageData pd);
    List<TreeAreaNode> getAreaIsN(@Param("params") PageData pd);
    List<Map<String,Object>> checkCode(@Param("params") PageData pd);
    void addArea(@Param("params") PageData pd);
    void updateAreaPid(@Param("params") PageData pd);
    void editArea(@Param("params") PageData pd);
    void delArea(@Param("params") PageData pd);
    Integer isDelArea(@Param("params") PageData pd);
    void delAreaParent(@Param("params") PageData pd);

}
