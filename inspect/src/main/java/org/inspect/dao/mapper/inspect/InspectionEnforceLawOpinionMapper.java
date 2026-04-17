// InspectionEnforceLawOpinionMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.Map;

/**
 * 执法检查意见书
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionEnforceLawOpinionMapper {

    /**
     * 获取记录
     * @param params
     */
    Map<String, Object> getRecord(@Param(value = "params") PageData params);

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


}///:~
