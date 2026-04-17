package org.jeecg.modules.dimnsnSetting.service.impl;

import org.jeecg.modules.dimnsnSetting.mapper.LevyingBodiesMapper;
import org.jeecg.modules.dimnsnSetting.model.LevyingBodies;
import org.jeecg.modules.dimnsnSetting.model.SubjectImport;
import org.jeecg.modules.dimnsnSetting.service.LevyingBodiesService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class LevyingBodiesServiceImpl implements LevyingBodiesService {

    @Autowired
    private LevyingBodiesMapper levyingBodiesMapper;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return levyingBodiesMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return levyingBodiesMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return levyingBodiesMapper.getAll(pd);
    }

    @Override
    public List<LevyingBodies> getExport(PageData pd) {
        return levyingBodiesMapper.getExport(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return levyingBodiesMapper.checkRepeat(pd);
    }

    @Override
    public void add(PageData pd) {
        levyingBodiesMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        levyingBodiesMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        levyingBodiesMapper.del(pd);
    }
}
