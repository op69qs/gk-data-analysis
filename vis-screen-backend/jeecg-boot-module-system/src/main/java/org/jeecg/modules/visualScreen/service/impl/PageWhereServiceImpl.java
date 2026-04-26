package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.PageSubMapper;
import org.jeecg.modules.visualScreen.mapper.PageWhereMapper;
import org.jeecg.modules.visualScreen.service.PageSubService;
import org.jeecg.modules.visualScreen.service.PageWhereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PageWhereServiceImpl implements PageWhereService {

    @Autowired
    private PageWhereMapper pageWhereMapper;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return pageWhereMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return pageWhereMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return pageWhereMapper.getAll(pd);
    }

    @Override
    public void add(PageData pd) {
        pageWhereMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        pageWhereMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        pageWhereMapper.del(pd);
    }
}
