// InspectionTemporarySumService.java

package org.inspect.service;

import java.util.List;
import java.util.Map;

/**
 * 检查任务-问题台账服务接口
 * @author Created by Samer on 2019/10/17.
 */
public interface InspectionTemporarySumService {

    /**
     * 获取子流程信息
     * @param params
     * @return
     */
    Map<String, String> getProcSubTitle(Map<String, Object> params);

    /**
     * 根据任务ID获取台账及整改信息
     * @return
     */
    List<Map<String, Object>> getLedgerReformInfoByTaskId(Map<String, Object> params);

} ///:~
