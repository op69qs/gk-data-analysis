// MvcConfigMapper.java

package org.fixedReport.dao.mapper.mvcconfig;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 静态页面控制
 * @author Created by Samer on 2019/9/23.
 */
@Repository
public interface MvcConfigMapper {

    List<Map<String, String>> getMvcConfig(@Param(value = "params")Map<String, String> params);

}///:~
