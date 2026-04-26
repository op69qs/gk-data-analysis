package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.BussTypeMapper;
import org.jeecg.modules.visualScreen.service.BussTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BussTypeServiceImpl implements BussTypeService {

    @Autowired
    private BussTypeMapper bussTypeMapper;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return bussTypeMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return bussTypeMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll() {
        return bussTypeMapper.getAll();
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return bussTypeMapper.checkRepeat(pd);
    }

    @Override
    public void add(PageData pd) {
        bussTypeMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        bussTypeMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        bussTypeMapper.del(pd);
    }

    @Override
    public String getMaxId() {
        return bussTypeMapper.getMaxId();
    }
}
