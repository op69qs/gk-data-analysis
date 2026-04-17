package org.jeecg.modules.dimnsnSetting.service.impl;

import org.jeecg.modules.dimnsnSetting.mapper.AreaMapper;
import org.jeecg.modules.dimnsnSetting.model.TreeAreaNode;
import org.jeecg.modules.dimnsnSetting.service.AreaService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("areaService")
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;


    @Override
    public List<TreeAreaNode> getAreaList(PageData pd) {
        return areaMapper.getAreaList(pd);
    }

    @Override
    public List<Map<String, Object>> getArea(PageData pd) {
        return areaMapper.getArea(pd);
    }

    @Override
    public List<Map<String, Object>> checkCode(PageData pd) {
        return areaMapper.checkCode(pd);
    }

    @Override
    public void addArea(PageData pd) {
        areaMapper.addArea(pd);
    }

    @Override
    public void editArea(PageData pd) {
        areaMapper.editArea(pd);
    }

    @Override
    public void delArea(PageData pd) {
        areaMapper.delArea(pd);
    }

    @Override
    public Integer isDelArea(PageData pd) {
        return areaMapper.isDelArea(pd);
    }

    @Override
    public void updateAreaPid(PageData pd) {
        areaMapper.updateAreaPid(pd);
    }

    @Override
    public void delAreaParent(PageData pd) {
        areaMapper.delAreaParent(pd);
    }
}
