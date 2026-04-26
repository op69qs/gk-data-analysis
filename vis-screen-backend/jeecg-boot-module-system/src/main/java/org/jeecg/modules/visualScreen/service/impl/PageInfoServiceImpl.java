package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.PageInfoMapper;
import org.jeecg.modules.visualScreen.model.PageInfo;
import org.jeecg.modules.visualScreen.service.PageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PageInfoServiceImpl implements PageInfoService {

    @Autowired
    private PageInfoMapper pageInfoMapper;

    @Override
    public PageInfo getPageInfo(PageData pd) {
        return pageInfoMapper.getPageInfo(pd);
    }

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return pageInfoMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return pageInfoMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return pageInfoMapper.getAll(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return pageInfoMapper.checkRepeat(pd);
    }

    @Override
    public void add(PageData pd) {
        pageInfoMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        pageInfoMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        pageInfoMapper.del(pd);
    }
}
