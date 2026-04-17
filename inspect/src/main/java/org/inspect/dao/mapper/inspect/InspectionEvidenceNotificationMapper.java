// InspectionEvidenceNotificationMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 执法检查取证记录通知书
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionEvidenceNotificationMapper {

    /**
     * 获取子表记录
     * @param params
     */
    List<Map<String, Object>> getRecordSub(@Param(value = "params") PageData params);

    /**
     * 获取记录
     * @param params
     */
    List<Map<String, Object>> getRecord(@Param(value = "params") PageData params);

    /**
     * 修改记录
     * @param params
     */
    void editRecord(@Param(value = "params") PageData params);

    /**
     * 新增记录
     * @param params
     */
    void addRecord(@Param(value = "params") PageData params);

    /**
     * 新增记录子表
     * @param params
     */
    void addRecordSheet(@Param(value = "params") PageData params);

    /**
     * 新增取证记录子表
     * @param params
     */
    void delRecordSheet(@Param(value = "params") PageData params);

}///:~
