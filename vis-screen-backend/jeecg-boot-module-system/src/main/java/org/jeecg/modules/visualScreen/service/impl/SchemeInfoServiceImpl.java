package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.PageInfoMapper;
import org.jeecg.modules.visualScreen.mapper.SchemeInfoMapper;
import org.jeecg.modules.visualScreen.service.PageInfoService;
import org.jeecg.modules.visualScreen.service.SchemeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SchemeInfoServiceImpl implements SchemeInfoService {

    @Autowired
    private SchemeInfoMapper schemeInfoMapper;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return schemeInfoMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return schemeInfoMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return schemeInfoMapper.getAll(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return schemeInfoMapper.checkRepeat(pd);
    }

    @Override
    public void add(PageData pd) {
        schemeInfoMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        schemeInfoMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        schemeInfoMapper.del(pd);
    }

    @Override
    public List<Map<String, Object>> getAllRel(PageData pd) {
        return schemeInfoMapper.getAllRel(pd);
    }

    @Override
    public void addRel(PageData pd) {
        schemeInfoMapper.addRel(pd);
    }

    @Override
    public void delRel(PageData pd) {
        schemeInfoMapper.delRel(pd);
    }
}
