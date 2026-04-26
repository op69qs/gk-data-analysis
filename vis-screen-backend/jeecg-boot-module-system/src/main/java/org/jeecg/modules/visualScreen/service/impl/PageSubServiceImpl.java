package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.PageSubMapper;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.service.PageSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PageSubServiceImpl implements PageSubService {

    @Autowired
    private PageSubMapper pageSubMapper;

    @Override
    public List<PageSub> getPageSub(PageData pd) {
        return pageSubMapper.getPageSub(pd);
    }

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return pageSubMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return pageSubMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return pageSubMapper.getAll(pd);
    }

    @Override
    public void add(PageData pd) {
        pageSubMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        pageSubMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        pageSubMapper.del(pd);
    }
}
