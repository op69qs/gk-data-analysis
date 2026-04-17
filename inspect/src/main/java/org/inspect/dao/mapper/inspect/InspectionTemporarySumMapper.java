// InspectionTemporarySumMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检查任务-问题台账
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionTemporarySumMapper {

    /**
     * 获取子流程信息
     * @param params
     * @return
     */
    Map<String, String> getProcSubTitle(@Param(value = "params") Map<String, Object> params);

    /**
     * 根据任务ID获取台账及整改信息
     * @param params
     * @return
     */
    List<Map<String, Object>> getLedgerReformInfoByTaskId(@Param(value = "params") Map<String, Object> params);


}///:~
