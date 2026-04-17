// InspectionNotificationPushMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

/**
 * 检查任务通知推送
 * @author Created by Samer on 2019/11/11.
 */
public interface InspectionNotificationPushMapper {

    /**
     * 获取国库名称（编码）
     * @param params
     */
    String getReceiveTreDscr(@Param(value = "params")PageData params);

    /**
     * 新增发布信息
     * @param params
     */
    void addInfoPub(@Param(value = "params")PageData params);

    /**
     * 新增附件信息
     * @param params
     */
    void addInfoAtta(@Param(value = "params")PageData params);

    /**
     * 新增接受机构信息
     * @param params
     */
    void addReceiveOrg(@Param(value = "params")PageData params);

}///:~
