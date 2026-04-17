package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionGroupMapper;
import org.inspect.dao.mapper.inspect.InspectionUserMapper;
import org.inspect.service.InspectionGroupService;
import org.inspect.service.InspectionUserService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionUserServiceImpl implements InspectionUserService {

    @Autowired
    private InspectionUserMapper inspectionUserMapper;


    @Override
    public List<Map<String, Object>> getInspectionUserPage(PageData pd) {
        return inspectionUserMapper.getInspectionUserPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionUserData(PageData pd) {
        return inspectionUserMapper.getInspectionUserData(pd);
    }

    @Override
    public List<Map<String, Object>> getUserData(PageData pd) {
        return inspectionUserMapper.getUserData(pd);
    }

    @Override
    public Map<String, Object> getUserBySysId(PageData pd) {
        return inspectionUserMapper.getUserBySysId(pd);
    }

    @Override
    public List<Map<String, Object>> getRoleBySysId(PageData pd) {
        return inspectionUserMapper.getRoleBySysId(pd);
    }

    @Override
    public Integer getInspectionUserCount(PageData pd) {
        return inspectionUserMapper.getInspectionUserCount(pd);
    }

    @Override
    public void addInspectionUser(PageData pd) {
        inspectionUserMapper.addInspectionUser(pd);
    }

    @Override
    public void editInspectionUser(PageData pd) {
        inspectionUserMapper.editInspectionUser(pd);
    }

    @Override
    public void delInspectionUser(PageData pd) {
        inspectionUserMapper.delInspectionUser(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionUserMapper.checkRepeat(pd);
    }

    @Override
    public String getDuties(PageData pd) {
        return inspectionUserMapper.getDuties(pd);
    }
}
