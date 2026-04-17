package org.jeecg.modules.dimnsnSetting.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.dimnsnSetting.model.SubjectImport;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SubjectImportManagerMapper {

    List<Map<String,Object>> getPage(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    List<Map<String,Object>> getAll(@Param("params") PageData pd);

    List<SubjectImport>getExport(@Param("params") PageData pd);

    List<Map<String,Object>>checkRepeat(@Param("params") PageData pd);

    void add(@Param("params") PageData pd);

    void edit(@Param("params") PageData pd);

    void del(@Param("params") PageData pd);

    void editStat(@Param("params") PageData pd);

    void editT(@Param("params") PageData pd);

    void callProc(@Param(value = "params") PageData pd);
}
