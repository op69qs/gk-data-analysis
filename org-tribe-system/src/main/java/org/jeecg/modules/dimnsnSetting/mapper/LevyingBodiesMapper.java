package org.jeecg.modules.dimnsnSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.dimnsnSetting.model.LevyingBodies;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LevyingBodiesMapper {

    List<Map<String,Object>> getPage(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    List<Map<String,Object>> getAll(@Param("params") PageData pd);

    List<LevyingBodies>getExport(@Param("params") PageData pd);

    List<Map<String,Object>>checkRepeat(@Param("params") PageData pd);

    void add(@Param("params") PageData pd);

    void edit(@Param("params") PageData pd);

    void del(@Param("params") PageData pd);
}
