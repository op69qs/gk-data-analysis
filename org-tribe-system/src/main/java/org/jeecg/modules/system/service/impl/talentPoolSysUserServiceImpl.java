// talentPoolSysUserServiceImpl.java

package org.jeecg.modules.system.service.impl;

import org.jeecg.modules.system.mapper.TalentPoolSysUserMapper;
import org.jeecg.modules.system.service.TalentPoolSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * 人才库与用户表关联
 * @author Created by Samer on 2019/11/8.
 */
@Service
public class talentPoolSysUserServiceImpl implements TalentPoolSysUserService {

    @Autowired
    private TalentPoolSysUserMapper talentPoolSysUserMapper;

    /**
     * 获取用户对应的人才库姓名
     * @param params
     */
    @Override
    public Map<String, Object> getRelationTalentPoolName(Map<String, Object> params){
        return talentPoolSysUserMapper.getRelationTalentPoolName(params);
    }

    /**
     * 修改关联关系
     * @param params
     */
    @Override
    public void editTalentPoolSysUserRelation(Map<String, Object> params) {
        talentPoolSysUserMapper.delExistRelation(params);
        talentPoolSysUserMapper.addTalentPoolSysUserRelation(params);
    }

    /**
     * 是否已存在关系
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> isExistRelation(Map<String, Object> params) {
        return talentPoolSysUserMapper.isExistRelation(params);
    }

    /**
     * 关联关系新增
     * @param params
     */
    @Override
    public void addTalentPoolSysUserRelation(Map<String, Object> params) {
        talentPoolSysUserMapper.addTalentPoolSysUserRelation(params);
    }
} ///:~
