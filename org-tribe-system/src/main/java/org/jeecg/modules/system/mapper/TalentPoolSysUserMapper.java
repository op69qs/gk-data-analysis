// talentPoolSysUserMapper.java

package org.jeecg.modules.system.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 人才库与用户表关联
 * @author Created by Samer on 2019/11/8.
 */
public interface TalentPoolSysUserMapper {

    /**
     * 获取用户对应的人才库姓名
     * @param params
     */
    Map<String, Object> getRelationTalentPoolName(@Param(value = "params") Map<String, Object> params);

    /**
     * 修改关联关系
     * @param params
     */
    void editTalentPoolSysUserRelation(@Param(value = "params") Map<String, Object> params);

    /**
     * 是否已存在关系
     * @param params
     */
    List<Map<String, String>> isExistRelation(@Param(value = "params") Map<String, Object> params);

    /**
     * 删除已有关系
     * @param params
     */
    void delExistRelation(@Param(value = "params") Map<String, Object> params);

    /**
     * 关联关系新增
     * @param params
     */
    void addTalentPoolSysUserRelation(@Param(value = "params") Map<String, Object> params);

}///:~
