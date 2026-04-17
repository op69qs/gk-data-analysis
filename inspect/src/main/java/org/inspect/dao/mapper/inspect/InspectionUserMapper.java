package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionUserMapper {

    List<Map<String,Object>> getInspectionUserPage(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionUserData(@Param("params") PageData pd);

    List<Map<String,Object>> getUserData(@Param("params") PageData pd);

    List<Map<String,Object>> getRoleBySysId(@Param("params") PageData pd);

    Map<String,Object> getUserBySysId(@Param("params") PageData pd);

    Integer getInspectionUserCount(@Param("params") PageData pd);

    void addInspectionUser(@Param("params") PageData pd);

    void editInspectionUser(@Param("params") PageData pd);

    void delInspectionUser(@Param("params") PageData pd);

    List<Map<String,Object>> checkRepeat(@Param("params") PageData pd);

    String getDuties(@Param("params") PageData pd);

}
