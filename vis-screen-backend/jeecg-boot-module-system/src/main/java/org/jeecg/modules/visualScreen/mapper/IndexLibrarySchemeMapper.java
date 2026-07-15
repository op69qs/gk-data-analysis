package org.jeecg.modules.visualScreen.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IndexLibrarySchemeMapper {

    List<Map<String, Object>> getPage(@Param("params") PageData pd);

    Integer getCount(@Param("params") PageData pd);

    Map<String, Object> getById(@Param("params") PageData pd);

    void del(@Param("params") PageData pd);

    Integer getGalleryMaxSort();

    void insertGallery(@Param("params") PageData pd);
}
