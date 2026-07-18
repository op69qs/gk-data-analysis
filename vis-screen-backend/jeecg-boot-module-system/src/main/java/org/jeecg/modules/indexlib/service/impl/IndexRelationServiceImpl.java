package org.jeecg.modules.indexlib.service.impl;

import org.jeecg.modules.indexlib.mapper.IndexRelationMapper;
import org.jeecg.modules.indexlib.service.IndexRelationService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexRelationServiceImpl implements IndexRelationService {

    @Autowired
    private IndexRelationMapper indexRelationMapper;

    @Override
    public List<Map<String, String>> getBatchIndexInfo(PageData pageData) {
        return indexRelationMapper.getBatchIndexInfo(pageData);
    }

    @Override
    public Map<String, Object> getIndexDetails(PageData pageData) {
        return indexRelationMapper.getIndexDetails(pageData);
    }
}
