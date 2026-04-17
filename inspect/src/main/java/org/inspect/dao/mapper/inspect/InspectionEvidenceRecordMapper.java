// InspectionEvidenceRecordMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 执法检查取证记录
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionEvidenceRecordMapper {

    /**
     * 获取主表信息
     * @param params
     */
    List<Map<String, Object>> getEvidenceRecordMainInfo(@Param(value = "params") PageData params);

    /**
     * 获取取证记录子表
     * @param params
     */
    List<Map<String, Object>> getEvidenceRecordSheet(@Param(value = "params") PageData params);

    /**
     * 获取取证记录
     * @param params
     */
    List<Map<String, Object>> getEvidenceRecord(@Param(value = "params") PageData params);

    /**
     * 修改取证记录
     * @param params
     */
    void editEvidenceRecord(@Param(value = "params") PageData params);

    /**
     * 新增取证记录
     * @param params
     */
    void addEvidenceRecord(@Param(value = "params") PageData params);

    /**
     * 删除取证记录
     * @param params
     */
    void deleteEvidenceRecord(@Param(value = "params") PageData params);

    /**
     * 新增取证记录子表
     * @param params
     */
    void addEvidenceRecordSheet(@Param(value = "params") PageData params);

    /**
     * 新增取证记录子表
     * @param params
     */
    void delEvidenceRecordSheet(@Param(value = "params") PageData params);

}///:~
