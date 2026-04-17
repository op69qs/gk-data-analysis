package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InspectionPostSVReformMapper {

    List<Map<String,Object>> getInspectionReformPage(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionReformData(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionReformSchemeData(@Param("params") PageData pd);

    List<Map<String,Object>> getInspectionReformReplayData(@Param("params") PageData pd);

    List<Map<String,Object>> getQuestionLedgerLvById(@Param("params") PageData pd);

    List<Map<String,Object>> getQuestionLedgerLvOne(@Param("params") PageData pd);

    List<Map<String,Object>> getReformData(@Param("params") PageData pd);

    List<Map<String,Object>> isComplete(@Param("params") PageData pd);

    void delInspectionReform(@Param("params") PageData pd);

    void updateReform(@Param("params") PageData pd);

    void delInspectionReformScheme(@Param("params") PageData pd);

    void delInspectionReformReplay(@Param("params") PageData pd);

    Integer countInspectionReform(@Param("params") PageData pd);

    void addInspectionReform(@Param("params") PageData pd);

    void addInspectionScheme(@Param("params") PageData pd);

    void addInspectionReplay(@Param("params") PageData pd);

    List<Map<String,Object>> toAddCase(@Param("params") PageData pd);

}
