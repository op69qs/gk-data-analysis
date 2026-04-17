// talentPoolSysUserService.java

package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

/**
 * 人才库与用户表关联
 * @author Created by Samer on 2019/11/8.
 */
public interface TalentPoolSysUserService {

    /**
     * 获取用户对应的人才库姓名
     * @param params
     */
    Map<String, Object> getRelationTalentPoolName(Map<String, Object> params);

    /**
     * 修改关联关系
     * @param params
     */
    void editTalentPoolSysUserRelation(Map<String, Object> params);

    /**
     * 是否已存在关系
     * @param params
     */
    List<Map<String, String>> isExistRelation(Map<String, Object> params);

    /**
     * 关联关系新增
     * @param params
     */
    void addTalentPoolSysUserRelation(Map<String, Object> params);

}///:~
