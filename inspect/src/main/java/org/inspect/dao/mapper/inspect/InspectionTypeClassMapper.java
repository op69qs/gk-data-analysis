// InspectionTypeClassMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检查类型分类
 * @author Created by Samer on 2019/11/8.
 */
public interface InspectionTypeClassMapper {

    /**
     * 获取检查类型分类
     * @return
     */
    List<Map<String, Object>> getInspectionTypeClass();

}///:~
